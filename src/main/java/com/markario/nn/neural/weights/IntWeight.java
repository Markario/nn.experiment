package com.markario.nn.neural.weights;

import java.util.Random;

/**
 * Created by markzepeda on 6/21/15.
 */
public class IntWeight implements GenericWeight {
    private int value;
    private static final Random random = new Random();

    @Override
    public GenericWeight zero() {
        value = 0;
        return this;
    }

    @Override
    public GenericWeight identity() {
        value = 1;
        return this;
    }

    @Override
    public GenericWeight random() {
        value = random.nextInt() - random.nextInt();
        return this;
    }

    @Override
    public GenericWeight add(GenericWeight otherWeight) {
        value =  value + ((IntWeight)otherWeight).value;
        return this;
    }

    @Override
    public GenericWeight multiply(GenericWeight otherWeight) {
        value = value * ((IntWeight)otherWeight).value;
        return this;
    }

    @Override
    public GenericWeight copy(GenericWeight otherWeight) {
        value = ((IntWeight)otherWeight).value;
        return this;
    }
}
