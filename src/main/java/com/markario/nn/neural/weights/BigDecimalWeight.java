package com.markario.nn.neural.weights;

import com.sun.istack.internal.NotNull;

import java.math.BigDecimal;
import java.util.Random;

/**
 * Created by markzepeda on 6/21/15.
 */
public class BigDecimalWeight implements GenericWeight<BigDecimalWeight> {
    private BigDecimal value = new BigDecimal(0);
    private static final Random random = new Random();

    @Override
    public BigDecimalWeight zero() {
        value = BigDecimal.ZERO;
        return this;
    }

    @Override
    public BigDecimalWeight identity() {
        value = BigDecimal.ONE;
        return this;
    }

    @Override
    public BigDecimalWeight negate() {
        value = value.negate();
        return this;
    }

    @Override
    public BigDecimalWeight random() {
        value = BigDecimal.valueOf(random.nextDouble() - random.nextDouble());
        return this;
    }

    @Override
    public BigDecimalWeight add(BigDecimalWeight otherWeight) {
        value =  value.add(otherWeight.value);
        return this;
    }

    @Override
    public BigDecimalWeight multiply(BigDecimalWeight otherWeight) {
        value = value.multiply(otherWeight.value);
        return this;
    }

    @Override
    public BigDecimalWeight sigmoid(BigDecimalWeight activationResponse) {
        BigDecimal exp = BigDecimal.valueOf(Math.E).pow(value.negate().divide(activationResponse.value).intValue());
        value = (BigDecimal.ONE.divide(BigDecimal.ONE.add(exp)));
        return this;
    }

    @Override
    public BigDecimalWeight copy(BigDecimalWeight otherWeight) {
        value = otherWeight.value;
        return this;
    }

    @Override
    public BigDecimalWeight mutate(Double percentage) {
        value = value.multiply(BigDecimal.valueOf(1 + percentage));
        return this;
    }

    @Override
    public int compareTo(BigDecimalWeight o) {
        return value.compareTo(o.value);
    }
}
