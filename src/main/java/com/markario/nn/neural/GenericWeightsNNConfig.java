package com.markario.nn.neural;

import com.markario.nn.neural.weights.GenericWeight;

import java.util.function.BiFunction;
import java.util.function.Supplier;

/**
 * Created by markzepeda on 6/21/15.
 */
public class GenericWeightsNNConfig<T extends GenericWeight<T>> extends NeuralNetworkConfig<T> {
    public GenericWeightsNNConfig(T activationResponse, T bias, int numInputs, int numHiddenLayers, int numNeuronsPerHiddenLayer, int numOutputs, Supplier<T> weightFactory) {
        super(activationResponse, bias, numInputs, numHiddenLayers, numNeuronsPerHiddenLayer, numOutputs, weightFactory, GenericWeightsNNConfig::filter);
    }

    public static <T extends GenericWeight<T>> T filter(T weight, T activationResponse){
        return weight.sigmoid(activationResponse);
    }
}
