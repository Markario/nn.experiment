package com.markario.nn.neural;

import java.util.ArrayList;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Created by markzepeda on 6/20/15.
 */
public class Neuron<T> {
    private final ArrayList<T> weights;
    private final int numInputs;

    public Neuron(int numInputs, Supplier<T> factory){
        weights = new ArrayList<>(numInputs);

        numInputs += 1; //Add in bias value that can also evolve

        while(numInputs > 0){
            weights.add(factory.get());
            numInputs--;
        }

        this.numInputs = weights.size() - 1;
    }

    public Neuron(ArrayList<T> weights){
        this.weights = weights;
        this.numInputs = weights.size() - 1;
    }

    public int getNumInputs() {
        return numInputs;
    }

    public ArrayList<T> getWeights(){
        return weights;
    }

    public Stream<T> weights(){
        return weights(this);
    }

    private static <T> Stream<T> weights(Neuron<T> neuron){
        return neuron.weights.stream();
    }
}
