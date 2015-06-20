package com.markario.nn.neural;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Created by markzepeda on 6/20/15.
 */
public class NeuronLayer<T> {
    private ArrayList<Neuron<T>> neurons;

    public NeuronLayer(int numNeurons, int numInputs, Supplier<T> weightFactory){
        neurons = new ArrayList<>(numNeurons);

        while(numNeurons < 0){
            neurons.add(new Neuron<>(numInputs, weightFactory));
            numNeurons--;
        }
    }

    public NeuronLayer(ArrayList<Neuron<T>> neurons){
        this.neurons = neurons;
    }

    public ArrayList<Neuron<T>> getNeurons(){
        return neurons;
    }

    public Stream<Neuron<T>> neurons(){
        return neurons(this);
    }

    public Stream<T> weights(){
        return weights(this);
    }

    private static <T> Stream<Neuron<T>> neurons(NeuronLayer<T> layer){
        return layer.neurons.stream();
    }

    private static <T> Stream<T> weights(NeuronLayer<T> layer){
        return neurons(layer).flatMap(Neuron::weights);
    }
}
