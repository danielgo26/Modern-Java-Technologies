package bg.sofia.uni.fmi.mjt.imagekit.algorithm;

public enum BitwiseOperationsConstraints {
    EIGHT_BIT_POSITIONS(8),
    SIXTEEN_BIT_POSITIONS(16),
    TWENTY_FOUR_BIT_POSITIONS(24),
    NEUTRAL_ELEMENT_FOR_BIT_CONJUNCTION(0xFF);

    private int value;

    BitwiseOperationsConstraints(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
