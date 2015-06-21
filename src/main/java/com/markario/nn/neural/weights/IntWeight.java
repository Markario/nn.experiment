package com.markario.nn.neural.weights;

import java.util.Random;

/**
 * Created by markzepeda on 6/21/15.
 */
public class IntWeight implements GenericWeight<IntWeight> {
    private int value;
    private static final Random random = new Random();

    @Override
    public IntWeight zero() {
        value = 0;
        return this;
    }

    @Override
    public IntWeight identity() {
        value = 1;
        return this;
    }

    @Override
    public IntWeight negate() {
        return this;
    }

    @Override
    public IntWeight random() {
        value = random.nextInt() - random.nextInt();
        return this;
    }

    @Override
    public IntWeight add(IntWeight otherWeight) {
        value =  value + otherWeight.value;
        return this;
    }

    @Override
    public IntWeight multiply(IntWeight otherWeight) {
        value = value * otherWeight.value;
        return this;
    }

    @Override
    public IntWeight sigmoid(IntWeight activationResponse) {
        value = ( 1 / ( 1 + ((int)Math.exp(-value / activationResponse.value))));
        return this;
    }

    @Override
    public IntWeight copy(IntWeight otherWeight) {
        value = otherWeight.value;
        return this;
    }

    @Override
    public int compareTo(IntWeight o) {
        return Integer.compare(value, o.value);
    }
}
