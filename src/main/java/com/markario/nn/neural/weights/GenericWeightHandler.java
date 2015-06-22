package com.markario.nn.neural.weights;

import java.util.function.Supplier;

/**
 * Created by markzepeda on 6/21/15.
 */
public class GenericWeightHandler<T extends GenericWeight<T>> implements WeightHandler<T> {

    private Supplier<T> weightFactory;

    public GenericWeightHandler(Supplier<T> weightFactory){
        this.weightFactory = weightFactory;
    }

    @Override
    public T getZeroedWeight() {
        return weightFactory.get().zero();
    }

    @Override
    public T getIdentityWeight() {
        return weightFactory.get().identity();
    }

    @Override
    public T getRandomWeight() {
        return weightFactory.get().random();
    }

    @Override
    public T addIntoWeight(T weight1, T weight2) {
        return weight1.add(weight2);
    }

    @Override
    public T copyIntoWeight(T into, T from) {
        return into.copy(from);
    }

    @Override
    public T multiplyIntoWeight(T weight1, T weight2) {
        return weight1.multiply(weight2);
    }

    @Override
    public T mutateWeight(T weight, Double percentage) {
        return weight.mutate(percentage);
    }

    @Override
    public T negateWeight(T weight) {
        return weight.negate();
    }

    @Override
    public T sigmoid(T weight, T activationResponse) {
        return weight.sigmoid(activationResponse);
    }
}
