package bg.sofia.uni.fmi.mjt.imagekit.filesystem;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LocalFileSystemImageManager implements FileSystemImageManager {

    static Set<String> validImageFormats = new HashSet<>(ValidImageFormats.convertValuesToStringArray());

    public LocalFileSystemImageManager() {
    }

    @Override
    public BufferedImage loadImage(File imageFile) throws IOException {
        if (imageFile == null) {
            throw new IllegalArgumentException("File cannot be loaded, because file is null");
        }

        if (!imageFile.exists() || !imageFile.isFile() || !checkValidFormat(imageFile)) {
            throw new IOException("File does not satisfy the needed criteria to be loaded");
        }

        return ImageIO.read(imageFile);
    }

    private boolean checkValidFormat(File file) {
        return checkPathEndsWith(file, validImageFormats);
    }

    private boolean checkPathEndsWith(File file, Set<String> fileTypes) {
        for (String fileType : fileTypes) {
            fileType = "." + fileType;
            fileType = fileType.toLowerCase();
            if (file.toString().endsWith(fileType)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public List<BufferedImage> loadImagesFromDirectory(File imagesDirectory) throws IOException {
        if (imagesDirectory == null) {
            throw new IllegalArgumentException("Directory cannot be loaded, because directory is null");
        }
        if (!imagesDirectory.exists()) {
            throw new IOException("File does not satisfy the needed criteria to be loaded");
        }

        if (!imagesDirectory.isDirectory()) {
            throw new IOException("File does not satisfy the needed criteria to be loaded");
        }

        File[] filesWithIn = imagesDirectory.listFiles();
        if (filesWithIn == null) {
            throw new IOException("Cannot list the content of the given file");
        }

        List<BufferedImage> images = new ArrayList<>();
        for (File file : filesWithIn) {
            images.add(loadImage(file));
        }

        return images;
    }

    @Override
    public void saveImage(BufferedImage image, File imageFile) throws IOException {
        if (image == null) {
            throw new IllegalArgumentException("Image to be saved is null");
        }
        if (imageFile == null) {
            throw new IllegalArgumentException("The file to save the image to is null");
        }
        if (imageFile.getParentFile() == null) {
            throw new IOException(String.format("The parent directory of %s does not exists", imageFile));
        }
        if (imageFile.exists()) {
            throw new IOException(String.format("File %s already exists", imageFile));
        }
        if (!checkValidFormat(imageFile)) {
            throw new IOException("File is not in valid format");
        }

        if (!ImageIO.write(image, getType(imageFile), imageFile)) {
            throw new IOException("Could not save image into file");
        }
    }

    private String getType(File filePath) {
        String path = filePath.toString();
        int typePointIndex = path.indexOf('.');
        return path.substring(typePointIndex + 1);
    }

}