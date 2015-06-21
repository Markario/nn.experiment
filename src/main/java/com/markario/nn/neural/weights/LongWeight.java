package com.markario.nn.neural.weights;

import java.util.Random;

/**
 * Created by markzepeda on 6/21/15.
 */
public class LongWeight implements GenericWeight<LongWeight> {
    private long value;
    private static final Random random = new Random();

    @Override
    public LongWeight zero() {
        value = 0l;
        return this;
    }

    @Override
    public LongWeight identity() {
        value = 1l;
        return this;
    }

    @Override
    public LongWeight random() {
        value = random.nextLong() - random.nextLong();
        return this;
    }

    @Override
    public LongWeight add(LongWeight otherWeight) {
        value =  value + otherWeight.value;
        return this;
    }

    @Override
    public LongWeight multiply(LongWeight otherWeight) {
        value = value * otherWeight.value;
        return this;
    }

    @Override
    public LongWeight copy(LongWeight otherWeight) {
        value = otherWeight.value;
        return this;
    }
}
