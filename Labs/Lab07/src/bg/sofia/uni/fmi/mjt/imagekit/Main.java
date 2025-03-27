package bg.sofia.uni.fmi.mjt.imagekit;

import bg.sofia.uni.fmi.mjt.imagekit.algorithm.detection.EdgeDetectionAlgorithm;
import bg.sofia.uni.fmi.mjt.imagekit.algorithm.detection.SobelEdgeDetection;

import bg.sofia.uni.fmi.mjt.imagekit.algorithm.grayscale.GrayscaleAlgorithm;
import bg.sofia.uni.fmi.mjt.imagekit.algorithm.grayscale.LuminosityGrayscale;
import bg.sofia.uni.fmi.mjt.imagekit.filesystem.FileSystemImageManager;
import bg.sofia.uni.fmi.mjt.imagekit.filesystem.LocalFileSystemImageManager;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        FileSystemImageManager manager = new LocalFileSystemImageManager();
        GrayscaleAlgorithm gs = new LuminosityGrayscale();
        EdgeDetectionAlgorithm ed = new SobelEdgeDetection(gs);
        try {
            BufferedImage a = manager.loadImage(new File("C:\\Users\\User_1\\Desktop\\s.png"));
            //List<BufferedImage> a = manager.loadImagesFromDirectory(new File("C:\\Users\\User_1\\Desktop\\car.jpg"));
            //BufferedImage b = gs.process(a.get(0));
            //a = ed.process(a);
            manager.saveImage(a, new File("C:\\Users\\User_1\\Desktop\\transformed.png"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}