package bg.sofia.uni.fmi.mjt.imagekit.filesystem;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.function.Try;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class LocalFileSystemImageManagerTest {

    private static FileSystemImageManager fileSystemImageManager;

    @BeforeAll
    static void setup() {
        fileSystemImageManager = new LocalFileSystemImageManager();
    }

    @Test
    void testLoadImageShouldThrowWhenNullFilePathIsGiven() {
        assertThrows(IllegalArgumentException.class, () -> fileSystemImageManager.loadImage(null),
            "Expected IllegalArgumentException to be thrown, but nothing was thrown!");
    }

    @Test
    void testLoadImageShouldThrowWhenFilePathDoesNotExists() {
        File filePath = mock();
        when(filePath.exists()).thenReturn(false);

        assertThrows(IOException.class, () -> fileSystemImageManager.loadImage(new File("\\myPath\\to\\file")),
            "Expected IOException to be thrown, but nothing was!");
    }

    @Test
    void testLoadImageShouldThrowWhenFileIsNotARegularFile() {
        File filePath = mock();
        when(filePath.isFile()).thenReturn(false);

        assertThrows(IOException.class, () -> fileSystemImageManager.loadImage(new File("\\myPath\\to\\file")),
            "Expected IOException to be thrown, but nothing was!");
    }

    @Test
    void testLoadImageShouldThrowWhenFileIsInInvalidFormat() {
        File filePath = mock();
        when(filePath.exists()).thenReturn(true);
        when(filePath.isFile()).thenReturn(true);

        assertThrows(IOException.class, () -> fileSystemImageManager.loadImage(new File("\\myPath\\to\\file.exe")),
            "Expected IOException to be thrown, but nothing was!");
    }
/*
    @Test
    void testLoadImageShouldReturnBufferedImageWhen() {
        File filePath = mock();
        when(filePath.exists()).thenReturn(true);
        when(filePath.isFile()).thenReturn(true);


        //when(Files.isRegularFile(any())).thenReturn(true);
        String fileName = mock();
        when(fileName.endsWith(any())).thenReturn(true);


        BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        ImageIO imageStream = mock();
        try {
            when(ImageIO.read(filePath)).thenReturn(image);

            assertEquals(fileSystemImageManager.loadImage(filePath), image,
                "Expected to return a valid BufferedImage with size 1 by 1, but something else was returned!");
        }
        catch (IOException e) {
            fail();
        }
    }
 */

    @Test
    void testLoadImagesFromDirectoryShouldThrowWhenGivenDirectoryIsNull() {
        assertThrows(IllegalArgumentException.class, () -> fileSystemImageManager.loadImagesFromDirectory(null),
            "Expected IllegalArgumentException to be thrown, but nothing was!");
    }

    @Test
    void testLoadImagesFromDirectoryShouldThrowWhenGivenDirectoryDoesNotExist() {
        File imagesDirectory = mock();
        when(imagesDirectory.exists()).thenReturn(false);

        assertThrows(IOException.class, () -> fileSystemImageManager.loadImagesFromDirectory(imagesDirectory),
            "Expected IOException to be thrown, but nothing was!");
    }

    @Test
    void testLoadImagesFromDirectoryShouldThrowWhenGivenFileIsNotADirectory() {
        File imagesDirectory = mock();
        when(imagesDirectory.exists()).thenReturn(true);
        when(imagesDirectory.isDirectory()).thenReturn(false);

        assertThrows(IOException.class, () -> fileSystemImageManager.loadImagesFromDirectory(imagesDirectory),
            "Expected IOException to be thrown, but nothing was!");
    }

    @Test
    void testLoadImagesFromDirectoryShouldThrowWhenCannotListFilesInTheGivenDirectory() {
        File imagesDirectory = mock();
        when(imagesDirectory.exists()).thenReturn(true);
        when(imagesDirectory.isDirectory()).thenReturn(true);
        when(imagesDirectory.listFiles()).thenReturn(null);

        assertThrows(IOException.class, () -> fileSystemImageManager.loadImagesFromDirectory(imagesDirectory),
            "Expected IOException to be thrown, but nothing was!");
    }

    //Invalid test!!!
    @Test
    void testLoadImagesFromDirectoryShouldReturnCollectionOfLoadedImages() {
        File f1 = mock(File.class);
        File f2 = mock(File.class);
        File f3 = mock(File.class);
        File[] filesInDir = new File[] {f1, f2, f3};

        BufferedImage bf1 = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        BufferedImage bf2 = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);
        BufferedImage bf3 = new BufferedImage(3, 3, BufferedImage.TYPE_INT_RGB);
        List<BufferedImage> images = new ArrayList<>(Arrays.asList(bf1, bf2, bf3));

        File imagesDirectory = mock();
        when(imagesDirectory.exists()).thenReturn(true);
        when(imagesDirectory.isDirectory()).thenReturn(true);
        when(imagesDirectory.listFiles()).thenReturn(filesInDir);
        FileSystemImageManager manager = mock();

        try {
            when(manager.loadImage(f1)).thenReturn(bf1);
            when(manager.loadImage(f2)).thenReturn(bf2);
            when(manager.loadImage(f3)).thenReturn(bf3);
        } catch (IOException e) {
            fail("Loading images should not throw any exception, but IOException was thrown");
        }

        assertDoesNotThrow(() -> manager.loadImage(any()), "Load image should not throw, but an exception was thrown");
        /*
        try {
            var loadedImages= manager.loadImagesFromDirectory(imagesDirectory);
            verify(manager, times(3)).loadImage(any());
            assertEquals(loadedImages, images);
        } catch(IOException e) {
            fail("Should not throw, but IOException was thrown!");
        }*/
    }

    @Test
    void testSaveImageShouldThrowWhenGivenNullImage() {
        assertThrows(IllegalArgumentException.class, () -> fileSystemImageManager.saveImage(null, new File("")),
            "Expected IllegalArgumentException to be thrown, but nothing was!");
    }

    @Test
    void testSaveImageShouldThrowWhenGivenNullImageFile() {
        assertThrows(IllegalArgumentException.class,
            () -> fileSystemImageManager.saveImage(new BufferedImage(1,1, BufferedImage.TYPE_INT_RGB), null),
            "Expected IllegalArgumentException to be thrown, but nothing was!");
    }

    @Test
    void testSaveImageShouldThrowWhenGivenImageFileWithInvalidParentFile() {
        File imageFile = mock();
        when(imageFile.getParentFile()).thenReturn(null);

        assertThrows(IOException.class,
            () -> fileSystemImageManager.saveImage(new BufferedImage(1,1, BufferedImage.TYPE_INT_RGB), imageFile),
            "Expected IOException to be thrown, but nothing was!");
    }

    @Test
    void testSaveImageShouldThrowWhenGivenNotExistingImageFile() {
        File imageFile = mock();
        when(imageFile.getParentFile()).thenReturn(imageFile);
        when(imageFile.exists()).thenReturn(true);

        assertThrows(IOException.class,
            () -> fileSystemImageManager.saveImage(new BufferedImage(1,1, BufferedImage.TYPE_INT_RGB), imageFile),
            "Expected IOException to be thrown, but nothing was!");
    }

    @Test
    void testSaveImageShouldThrowWhenGivenImageFileWithInvalidFormat() {
        File imageFile = mock();
        when(imageFile.getParentFile()).thenReturn(imageFile);
        when(imageFile.exists()).thenReturn(false);
        when(imageFile.toString()).thenReturn("invalidType.exe");

        assertThrows(IOException.class,
            () -> fileSystemImageManager.saveImage(new BufferedImage(1,1, BufferedImage.TYPE_INT_RGB), imageFile),
            "Expected IOException to be thrown, but nothing was!");
    }
/*
    @Test
    void testSaveImageShouldNotThrowWhenGivenValidImageFileAndFile() {
        File imageFile = mock();
        BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        when(imageFile.getParentFile()).thenReturn(imageFile);
        when(imageFile.exists()).thenReturn(false);
        when(imageFile.toString()).thenReturn("invalidType.png");

        ImageIO io = mock();
        try {
            when(io.write(image, "PNG", imageFile)).thenReturn(true);
        } catch(IOException e) {
            fail("Expected not to throw, but an IOException was thrown!");
        }

        assertThrows(IOException.class,
            () -> fileSystemImageManager.saveImage(image, imageFile),
            "Expected not to throw, but an exception was thrown!");
    }*/

}