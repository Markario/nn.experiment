package com.markario.nn.neural;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by markzepeda on 6/20/15.
 */
public class NeuralNetwork<T> {
    private int numInputs;
    private int numOutputs;
    private int numHiddenLayers;
    private int numNeuronsPerHiddenLayer;
    private ArrayList<NeuronLayer<T>> layers;

    public NeuralNetwork(int numInputs,
                         int numOutputs,
                         int numHiddenLayers,
                         int numNeuronsPerHiddenLayer,
                         Supplier<T> weightFactory){

        layers = new ArrayList<>(numHiddenLayers+1);

        if(numHiddenLayers > 0){
            layers.add(new NeuronLayer<>(numNeuronsPerHiddenLayer, numInputs, weightFactory));
            for(int i = 0; i < numHiddenLayers - 1; i++){
                layers.add(new NeuronLayer<>(numNeuronsPerHiddenLayer, numNeuronsPerHiddenLayer, weightFactory));
            }
            layers.add(new NeuronLayer<>(numOutputs, numNeuronsPerHiddenLayer, weightFactory));
        }else{
            layers.add(new NeuronLayer<>(numOutputs, numInputs, weightFactory));
        }
    }

    public void setWeights(ArrayList<T> newWeights){
        int weightIndex = 0;
        List<Neuron<T>> neurons = getNeurons();
        for(int i = 0; i < neurons.size() && weightIndex < newWeights.size(); i++){
            List<T> neuronWeights = neurons.get(i).getWeights();
            for(int j = 0; j <neuronWeights.size(); j++){
                neuronWeights.set(j, newWeights.get(weightIndex));
                weightIndex++;
            }
        }
    }

    public int getNumWeights(){
        return weights().mapToInt(value -> 1).sum();
    }

    public List<T> getWeights(){
        return weights().collect(Collectors.toList());
    }

    public List<Neuron<T>> getNeurons(){
        return neurons().collect(Collectors.toList());
    }

    public Stream<NeuronLayer<T>> layers(){
        return layers(this);
    }

    public Stream<Neuron<T>> neurons(){
        return neurons(this);
    }

    public Stream<T> weights(){
        return weights(this);
    }

    public static <T> Stream<NeuronLayer<T>> layers(NeuralNetwork<T> neuralNetwork){
        return neuralNetwork.layers.stream();
    }

    public static <T> Stream<Neuron<T>> neurons(NeuralNetwork<T> neuralNetwork){
        return layers(neuralNetwork).flatMap(NeuronLayer::neurons);
    }

    public static <T> Stream<T> weights(NeuralNetwork<T> neuralNetwork){
        return neurons(neuralNetwork).flatMap(Neuron::weights);
    }
}
