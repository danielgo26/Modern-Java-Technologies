package bg.sofia.uni.fmi.mjt.imagekit.algorithm.detection;

import bg.sofia.uni.fmi.mjt.imagekit.algorithm.BitwiseOperationsConstraints;
import bg.sofia.uni.fmi.mjt.imagekit.algorithm.ImageAlgorithm;

import java.awt.image.BufferedImage;

public class SobelEdgeDetection implements EdgeDetectionAlgorithm {

    private final ImageAlgorithm grayscaleAlgorithm;

    private static final int[][] SOBEL_HORIZONTAL_KERNEL = {
        {-1, 0, 1},
        {-2, 0, 2},
        {-1, 0, 1}
    };

    private static final int[][] SOBEL_VERTICAL_KERNEL = {
        {-1, -2, -1},
        { 0,  0,  0},
        { 1,  2,  1}
    };

    static final int NEUTRAL_BYTE = BitwiseOperationsConstraints.NEUTRAL_ELEMENT_FOR_BIT_CONJUNCTION.getValue();

    public SobelEdgeDetection(ImageAlgorithm grayscaleAlgorithm) {
        if (grayscaleAlgorithm == null) {
            throw new IllegalArgumentException("The given grayscale algorithm is null");
        }
        this.grayscaleAlgorithm = grayscaleAlgorithm;
    }

    @Override
    public BufferedImage process(BufferedImage image) {
        if (image == null) {
            throw new IllegalArgumentException("The given image is null");
        }
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage grayscaleImage = this.grayscaleAlgorithm.process(image);
        BufferedImage edgeDetectedImage = new BufferedImage(width, height,
            BufferedImage.TYPE_INT_RGB);
        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {
                int gx = 0;
                int gy = 0;

                for (int ky = -1; ky <= 1; ky++) {
                    for (int kx = -1; kx <= 1; kx++) {
                        int pixel = grayscaleImage.getRGB(x + kx, y + ky) & NEUTRAL_BYTE;

                        gx += SOBEL_HORIZONTAL_KERNEL[ky + 1][kx + 1] * pixel;
                        gy += SOBEL_VERTICAL_KERNEL[ky + 1][kx + 1] * pixel;
                    }
                }

                int magnitude = (int) Math.min(NEUTRAL_BYTE, Math.sqrt(gx * gx + gy * gy));
                edgeDetectedImage.setRGB(x, y, calculateEdgePixelValue(magnitude));
            }
        }

        return edgeDetectedImage;
    }

    private int calculateEdgePixelValue(int magnitude) {
        return (magnitude << BitwiseOperationsConstraints.SIXTEEN_BIT_POSITIONS.getValue()) |
            (magnitude << BitwiseOperationsConstraints.EIGHT_BIT_POSITIONS.getValue()) | magnitude;
    }

}