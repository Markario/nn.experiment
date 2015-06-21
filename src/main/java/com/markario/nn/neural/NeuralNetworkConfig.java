package com.markario.nn.neural;

import java.util.function.BiFunction;
import java.util.function.Supplier;

/**
 * Created by markzepeda on 6/21/15.
 */
public class NeuralNetworkConfig<T> {
    public T activationResponse; // = 1;
    public T bias; // = -1
    public int numInputs = 4;
    public int numHiddenLayers = 1;
    public int numNeuronsPerHiddenLayer = 6;
    public int numOutputs = 2;
    public Supplier<T> weightFactory;
    public BiFunction<T, T, T> weightFilter;

    public NeuralNetworkConfig(T activationResponse,
                               T bias,
                               int numInputs,
                               int numHiddenLayers,
                               int numNeuronsPerHiddenLayer,
                               int numOutputs,
                               Supplier<T> weightFactory,
                               BiFunction<T, T, T> weightFilter) {

        this.activationResponse = activationResponse;
        this.bias = bias;
        this.numInputs = numInputs;
        this.numHiddenLayers = numHiddenLayers;
        this.numNeuronsPerHiddenLayer = numNeuronsPerHiddenLayer;
        this.numOutputs = numOutputs;
        this.weightFactory = weightFactory;
        this.weightFilter = weightFilter;
    }
}
