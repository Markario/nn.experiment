package com.markario.nn.neural.weights;

import java.util.Random;

/**
 * Created by markzepeda on 6/21/15.
 */
public class DoubleWeight implements GenericWeight<DoubleWeight, Double> {
    private double value;
    private static final Random random = new Random();

    @Override
    public DoubleWeight zero() {
        value = 0d;
        return this;
    }

    @Override
    public DoubleWeight identity() {
        value = 1d;
        return this;
    }

    @Override
    public DoubleWeight negate() {
        return this;
    }

    @Override
    public DoubleWeight random() {
        value = random.nextDouble() - random.nextDouble();
        return this;
    }

    @Override
    public DoubleWeight add(DoubleWeight otherWeight) {
        value =  value + otherWeight.value;
        return this;
    }

    @Override
    public DoubleWeight multiply(DoubleWeight otherWeight) {
        value = value * otherWeight.value;
        return this;
    }

    @Override
    public DoubleWeight copy(DoubleWeight otherWeight) {
        value = otherWeight.value;
        return this;
    }

    @Override
    public Double value() {
        return value;
    }

    @Override
    public int compareTo(DoubleWeight o) {
        return Double.compare(value, o.value);
    }
}
