package com.markario.nn.neural;

import com.markario.nn.neural.NeuralNetworkConfig;
import com.markario.nn.neural.NeuralNetwork;
import com.markario.nn.neural.weights.GenericWeight;

/**
 * Created by markzepeda on 6/21/15.
 */
public class GenericWeightsNeuralNetwork<T extends GenericWeight<T>> extends NeuralNetwork<T> {

    public GenericWeightsNeuralNetwork(NeuralNetworkConfig<T> config) {
        super(config);
    }

    @Override
    public T getZeroedWeight() {
        return config.weightFactory.get().zero();
    }

    @Override
    public T addWeights(T weight1, T weight2) {
        return weight1.add(weight2);
    }

    @Override
    public T copyWeight(T into, T from) {
        return into.copy(from);
    }

    @Override
    public T multiplyWeight(T weight1, T weight2) {
        return weight1.multiply(weight2);
    }
}
