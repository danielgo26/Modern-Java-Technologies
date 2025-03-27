package bg.sofia.uni.fmi.mjt.imagekit.algorithm.detection;

import bg.sofia.uni.fmi.mjt.imagekit.algorithm.grayscale.LuminosityGrayscale;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.aggregator.AggregateWith;

import java.awt.Color;
import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.*;

class SobelEdgeDetectionTest {
    private static EdgeDetectionAlgorithm edgeDetection;
    private static final int[][] randomImagePixels = {
        {23, 45, 67, 89, 110},
        {54, 67, 89, 120, 130},
        {45, 78, 96, 78, 110},
        {89, 110, 120, 140, 160},
        {99, 150, 130, 120, 90}
    };

    private static final int[][] expectedImagePixelsAfterSobelOperator = {
        {0, 0, 0, 0, 0},
        {0, 921102, 789516, 657930, 0},
        {0, 986895, 657930, 657930, 0},
        {0, 1184274, 921102, 526344, 0},
        {0, 0, 0, 0, 0}};

    @BeforeAll
    static void setup() {
        edgeDetection = new SobelEdgeDetection(new LuminosityGrayscale());
    }

    @Test
    void testSobelEdgeDetectionShouldThrowWhenNullGrayscaleAlgorithmIsGivenWhileCreation() {
        assertThrows(IllegalArgumentException.class, () -> new SobelEdgeDetection(null),
            "Expected IllegalArgumentException to be thrown, but nothing was!");
    }

    @Test
    void testProcessShouldThrowWhenImageIsNull() {
        assertThrows(IllegalArgumentException.class, () -> edgeDetection.process(null),
            "Process expected to throw IllegalArgumentException, but nothing was thrown!");
    }

    @Test
    void testProcessShouldHaveNoEffectApplyingSobelEdgeDetectionFilterOnImageContainingOnlyZeroBytes() {
        BufferedImage image = createImageContainingOnlyZeroBytes();
        BufferedImage imageWithDetectedEdges = edgeDetection.process(image);
        int height = imageWithDetectedEdges.getHeight();
        int width = imageWithDetectedEdges.getWidth();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int originalColor = image.getRGB(x, y);
                int colorWithFilter = imageWithDetectedEdges.getRGB(x, y);

                assertEquals(originalColor, colorWithFilter,
                    String.format("Expected pixel with coordinates: %d : %d to be %d, but is %d instead!", x, y,
                        originalColor, colorWithFilter));
            }
        }
    }

    private BufferedImage createImageContainingOnlyZeroBytes() {
        int height = 10;
        int width = 10;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                image.setRGB(x, y, 0);
            }
        }

        return image;
    }

    @Test
    void testProcessShouldApplySobelEdgeDetectionFilterOnImage() {
        BufferedImage image =
            createImageFromMatrix(randomImagePixels, randomImagePixels.length, randomImagePixels.length);
        BufferedImage imageWithDetectedEdges = edgeDetection.process(image);
        BufferedImage expectedImage = createImageFromMatrix(expectedImagePixelsAfterSobelOperator,
            expectedImagePixelsAfterSobelOperator.length, expectedImagePixelsAfterSobelOperator.length);
        int height = imageWithDetectedEdges.getHeight();
        int width = imageWithDetectedEdges.getWidth();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int expectedColor = expectedImage.getRGB(x, y);
                int colorWithFilter = imageWithDetectedEdges.getRGB(x, y);

                assertEquals(expectedColor, colorWithFilter,
                    String.format("Expected pixel with coordinates: %d : %d to be %d, but is %d instead!", x, y,
                        expectedColor, colorWithFilter));
            }
        }
    }

    private BufferedImage createImageFromMatrix(int[][] matrix, int height, int width) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                image.setRGB(x, y, matrix[y][x]);
            }
        }

        return image;
    }

}