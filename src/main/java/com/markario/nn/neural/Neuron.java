package com.markario.nn.neural;

import java.util.ArrayList;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Created by markzepeda on 6/20/15.
 */
public class Neuron<T> {
    private ArrayList<T> weights;

    public Neuron(int numInputs, Supplier<T> factory){
        weights = new ArrayList<>(numInputs);

        while(numInputs > 0){
            weights.add(factory.get());
            numInputs--;
        }
    }

    public Neuron(ArrayList<T> weights){
        this.weights = weights;
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
