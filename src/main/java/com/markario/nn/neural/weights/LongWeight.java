package com.markario.nn.neural.weights;

import com.sun.istack.internal.NotNull;

import java.util.Random;

/**
 * Created by markzepeda on 6/21/15.
 */
public class LongWeight implements GenericWeight<LongWeight> {
    public long value;
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
    public LongWeight negate() {
        value = -value;
        return this;
    }

    @Override
    public LongWeight random() {
        value = random.nextLong() - random.nextLong();
        return this;
    }

    @Override
    public LongWeight add(LongWeight otherWeight) {
        value = value + otherWeight.value;
        return this;
    }

    @Override
    public LongWeight multiply(LongWeight otherWeight) {
        value = value * otherWeight.value;
        return this;
    }

    @Override
    public LongWeight sigmoid(LongWeight activationResponse) {
        value = (1 / (1 + ((long) Math.exp(-value / activationResponse.value))));
        return this;
    }

    @Override
    public LongWeight copy(LongWeight otherWeight) {
        value = otherWeight.value;
        return this;
    }

    @Override
    public LongWeight mutate(Double percentage) {
        value = (long) (value * (1.0 + percentage));
        return this;
    }

    @Override
    public int compareTo(LongWeight o) {
        return Long.compare(value, o.value);
    }
}
