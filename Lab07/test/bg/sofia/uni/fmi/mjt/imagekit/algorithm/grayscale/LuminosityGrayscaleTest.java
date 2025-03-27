package bg.sofia.uni.fmi.mjt.imagekit.algorithm.grayscale;

import bg.sofia.uni.fmi.mjt.imagekit.algorithm.detection.EdgeDetectionAlgorithm;
import bg.sofia.uni.fmi.mjt.imagekit.algorithm.detection.SobelEdgeDetection;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.*;

class LuminosityGrayscaleTest {
    private static GrayscaleAlgorithm grayscale;
    private static final int[][] randomImagePixels = {
        {23, 45, 4},
        {78, 7, 45},
        {54, 67, 7}};

    private static final int[][] expectedImagePixelsAfterLuminosity = {
        {65793, 197379, 0},
        {328965, 0, 197379},
        {197379, 263172, 0}};

    @BeforeAll
    static void setup() {
        grayscale = new LuminosityGrayscale();
    }

    @Test
    void testProcessShouldThrowWhenImageIsNull() {
        assertThrows(IllegalArgumentException.class, () -> grayscale.process(null),
            "Process expected to throw IllegalArgumentException, but nothing was thrown!");
    }

    @Test
    void testProcessShouldHaveNoEffectApplyingLuminosityFilterOnImageContainingOnlyZeroBytes() {
        BufferedImage image = createImageContainingOnlyZeroBytes();
        BufferedImage imageWithDetectedEdges = grayscale.process(image);
        int height = imageWithDetectedEdges.getHeight();
        int width = imageWithDetectedEdges.getWidth();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int originalColor = image.getRGB(x, y);
                int colorWithFilter = imageWithDetectedEdges.getRGB(x, y);

                assertEquals(originalColor, colorWithFilter,
                    String.format("Expected pixel with coordinates: %d : %d to be %d, but is %d instead", x, y,
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
    void testProcessShouldApplyLuminosityFilterOnImage() {
        BufferedImage image = createImageFromMatrix(randomImagePixels, randomImagePixels.length, randomImagePixels.length);
        BufferedImage imageWithDetectedEdges = grayscale.process(image);
        BufferedImage expectedImage = createImageFromMatrix(expectedImagePixelsAfterLuminosity,
            expectedImagePixelsAfterLuminosity.length, expectedImagePixelsAfterLuminosity.length);
        int height = imageWithDetectedEdges.getHeight();
        int width = imageWithDetectedEdges.getWidth();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int expectedColor = expectedImage.getRGB(x, y);
                int colorWithFilter = imageWithDetectedEdges.getRGB(x, y);

                assertEquals(expectedColor, colorWithFilter,
                    String.format("Expected pixel with coordinates: %d : %d to be %d, but is %d instead", x, y,
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