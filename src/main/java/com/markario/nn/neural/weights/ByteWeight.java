package com.markario.nn.neural.weights;

import java.util.Random;

/**
 * Created by markzepeda on 6/21/15.
 */
public class ByteWeight implements GenericWeight<ByteWeight> {
    private byte value;
    private static final Random random = new Random();

    @Override
    public ByteWeight zero() {
        value = 0x0;
        return this;
    }

    @Override
    public ByteWeight identity() {
        value = 0xF;
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
        value &= otherWeight.value;
        return this;
    }

    @Override
    public ByteWeight copy(ByteWeight otherWeight) {
        value = otherWeight.value;
        return this;
    }
}
