package bg.sofia.uni.fmi.mjt.poll.server;

import bg.sofia.uni.fmi.mjt.poll.command.CommandCreator;
import bg.sofia.uni.fmi.mjt.poll.command.CommandExecutor;
import bg.sofia.uni.fmi.mjt.poll.server.repository.PollRepository;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

public class PollServer {
    private static final int BUFFER_SIZE = 1024;
    private static final String HOST = "localhost";

    private final CommandExecutor commandExecutor;

    private final int port;
    private boolean isServerWorking;

    private ByteBuffer buffer;
    private Selector selector;

    public PollServer(int port, PollRepository pollRepository) {
        this.port = port;
        if (pollRepository == null) {
            throw new IllegalArgumentException("The given repository cannot be null!");
        }
        this.commandExecutor = new CommandExecutor(pollRepository);
    }

    public void start() {
        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
            selector = Selector.open();
            configureServerSocketChannel(serverSocketChannel, selector);
            this.buffer = ByteBuffer.allocate(BUFFER_SIZE);
            isServerWorking = true;

            loopServerEvaluation();

        } catch (IOException e) {
            throw new UncheckedIOException("failed to start server", e);
        }
    }

    public void stop() {
        this.isServerWorking = false;
        if (selector.isOpen()) {
            selector.wakeup();
        }
    }

    private void loopServerEvaluation() {
        while (isServerWorking) {
            try {
                int readyChannels = selector.select();
                if (readyChannels != 0) {
                    Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
                    while (keyIterator.hasNext()) {
                        handleKey(keyIterator.next());
                        keyIterator.remove();
                    }
                }
            } catch (IOException e) {
                System.out.println("Error occurred while processing client request: " + e.getMessage());
            }
        }
    }

    private void handleKey(SelectionKey key) throws IOException {
        if (key.isReadable()) {
            SocketChannel clientChannel = (SocketChannel) key.channel();
            String clientInput = getClientInput(clientChannel);
            if (clientInput == null) {
                handleError(clientChannel, "Client input cannot be null.");
            }
            try {
                String output = commandExecutor.execute(CommandCreator.newCommand(clientInput));
                writeClientOutput(clientChannel, output);
                checkForDisconnect(clientInput, clientChannel, key);
            } catch (Exception e) {
                handleError(clientChannel, "The command is not in the valid syntax.");
            }
        } else if (key.isAcceptable()) {
            accept(selector, key);
        }
    }

    private void handleError(SocketChannel clientChannel, String msg) {
        try {
            String error =
                "{\"status\":\"ERROR\",\"message\":\"" + msg + "\"}\n";
            writeClientOutput(clientChannel, error);
        } catch (IOException e) {
            System.out.println("Error occurred while processing client request: " + e.getMessage());
        }
    }

    private void checkForDisconnect(String clientInput, SocketChannel clientChannel, SelectionKey key) {
        try {
            if ("disconnect".equals(clientInput)) {
                disconnectClient(clientChannel, key);
            }
        } catch (IOException e) {
            System.out.println("Error occurred while processing client request: " + e.getMessage());
        }
    }

    private void configureServerSocketChannel(ServerSocketChannel channel, Selector selector) throws IOException {
        channel.bind(new InetSocketAddress(HOST, this.port));
        channel.configureBlocking(false);
        channel.register(selector, SelectionKey.OP_ACCEPT);
    }

    private String getClientInput(SocketChannel clientChannel) throws IOException {
        buffer.clear();

        int readBytes = clientChannel.read(buffer);
        if (readBytes < 0) {
            clientChannel.close();
            return null;
        }

        buffer.flip();
        byte[] clientInputBytes = new byte[buffer.remaining()];
        buffer.get(clientInputBytes);

        return new String(clientInputBytes, StandardCharsets.UTF_8);
    }

    private void writeClientOutput(SocketChannel clientChannel, String output) throws IOException {
        buffer.clear();
        buffer.put(output.getBytes());
        buffer.flip();

        clientChannel.write(buffer);
    }

    private void accept(Selector selector, SelectionKey key) throws IOException {
        ServerSocketChannel sockChannel = (ServerSocketChannel) key.channel();
        SocketChannel accept = sockChannel.accept();

        accept.configureBlocking(false);
        accept.register(selector, SelectionKey.OP_READ);
    }

    private void disconnectClient(SocketChannel clientChannel, SelectionKey key) throws IOException {
        clientChannel.close();
        key.cancel();
        System.out.println("Client disconnected and connection closed.");
    }

}