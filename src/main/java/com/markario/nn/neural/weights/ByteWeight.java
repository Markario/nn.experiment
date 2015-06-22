package com.markario.nn.neural.weights;

import java.util.Random;

/**
 * Created by markzepeda on 6/21/15.
 */
public class ByteWeight implements GenericWeight<ByteWeight> {
    public byte value;
    private static final Random random = new Random();

    @Override
    public ByteWeight zero() {
        value = 0b000_0000;
        return this;
    }

    @Override
    public ByteWeight identity() {
        value = 0b111_1111;
        return this;
    }

    @Override
    public ByteWeight negate() {
        value = (byte) (~value & 0b111_1111);
        return this;
    }

    @Override
    public ByteWeight random() {
        byte[] bytes = new byte[1];
        random.nextBytes(bytes);
        value = bytes[0];
        return this;
    }

    @Override
    public ByteWeight add(ByteWeight otherWeight) {
        value += otherWeight.value;
        return this;
    }

    @Override
    public ByteWeight multiply(ByteWeight otherWeight) {
        value *= otherWeight.value;
        return this;
    }

    @Override
    public ByteWeight sigmoid(ByteWeight activationResponse) {
        value = (byte) (1 / (1 + ((byte) Math.exp(-value / activationResponse.value))));
        return this;
    }

    @Override
    public ByteWeight copy(ByteWeight otherWeight) {
        value = otherWeight.value;
        return this;
    }

    @Override
    public ByteWeight mutate(Double percentage) {
        value = (byte) (value * (1.0 + percentage));
        return this;
    }

    @Override
    public int compareTo(ByteWeight o) {
        return Byte.compare(value, o.value);
    }
}
