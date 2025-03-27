package bg.sofia.uni.fmi.mjt.imagekit.algorithm.grayscale;

import bg.sofia.uni.fmi.mjt.imagekit.algorithm.BitwiseOperationsConstraints;

import java.awt.image.BufferedImage;

public class LuminosityGrayscale implements GrayscaleAlgorithm {
    private static final double RED_LUMINOSITY_FACTOR = 0.21;
    private static final double GREEN_LUMINOSITY_FACTOR = 0.72;
    private static final double BLUE_LUMINOSITY_FACTOR = 0.07;
    static final int NEUTRAL_BYTE = BitwiseOperationsConstraints.NEUTRAL_ELEMENT_FOR_BIT_CONJUNCTION.getValue();

    public LuminosityGrayscale() {
    }

    @Override
    public BufferedImage process(BufferedImage image) {
        if (image == null) {
            throw new IllegalArgumentException("The given image is null");
        }

        BufferedImage grayscaleImage =
            new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);

        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                grayscaleImage.setRGB(x, y, getNewRgba(image.getRGB(x, y)));
            }
        }

        return grayscaleImage;
    }

    private int getNewRgba(int rgba) {
        int red = (rgba >> BitwiseOperationsConstraints.SIXTEEN_BIT_POSITIONS.getValue()) & NEUTRAL_BYTE;
        int green = (rgba >> BitwiseOperationsConstraints.EIGHT_BIT_POSITIONS.getValue()) & NEUTRAL_BYTE;
        int blue = rgba & NEUTRAL_BYTE;

        int luminance = (int) (RED_LUMINOSITY_FACTOR * red +
            GREEN_LUMINOSITY_FACTOR * green +
            BLUE_LUMINOSITY_FACTOR * blue);

        return (luminance << BitwiseOperationsConstraints.SIXTEEN_BIT_POSITIONS.getValue() |
            luminance << BitwiseOperationsConstraints.EIGHT_BIT_POSITIONS.getValue() |
            luminance);
    }

}