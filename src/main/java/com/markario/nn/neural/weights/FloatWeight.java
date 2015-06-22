package com.markario.nn.neural.weights;

import java.util.Random;

/**
 * Created by markzepeda on 6/21/15.
 */
public class FloatWeight implements GenericWeight<FloatWeight> {
    private float value;
    private static final Random random = new Random();

    @Override
    public FloatWeight zero() {
        value = 0f;
        return this;
    }

    @Override
    public FloatWeight identity() {
        value = 1f;
        return this;
    }

    @Override
    public FloatWeight negate() {
        return this;
    }

    @Override
    public FloatWeight random() {
        value = random.nextFloat() - random.nextFloat();
        return this;
    }

    @Override
    public FloatWeight add(FloatWeight otherWeight) {
        value =  value + otherWeight.value;
        return this;
    }

    @Override
    public FloatWeight multiply(FloatWeight otherWeight) {
        value = value * otherWeight.value;
        return this;
    }

    @Override
    public FloatWeight sigmoid(FloatWeight activationResponse) {
        value = ( 1 / ( 1 + ((float)Math.exp(-value / activationResponse.value))));
        return this;
    }

    @Override
    public FloatWeight copy(FloatWeight otherWeight) {
        value = otherWeight.value;
        return this;
    }

    @Override
    public FloatWeight mutate(Double percentage) {
        value = (float) (value * (1.0 + percentage));
        return this;
    }

    @Override
    public int compareTo(FloatWeight o) {
        return Float.compare(value, o.value);
    }
}
