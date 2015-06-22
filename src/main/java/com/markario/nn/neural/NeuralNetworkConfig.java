package com.markario.nn.neural;

import com.markario.nn.neural.weights.WeightHandler;

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
    public WeightHandler<T> weightHandler;

    public NeuralNetworkConfig(T activationResponse,
                               T bias,
                               int numInputs,
                               int numHiddenLayers,
                               int numNeuronsPerHiddenLayer,
                               int numOutputs,
                               WeightHandler<T> weightHandler) {

        this.activationResponse = activationResponse;
        this.bias = bias;
        this.numInputs = numInputs;
        this.numHiddenLayers = numHiddenLayers;
        this.numNeuronsPerHiddenLayer = numNeuronsPerHiddenLayer;
        this.numOutputs = numOutputs;
        this.weightHandler = weightHandler;
    }
}
