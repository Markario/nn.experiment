package com.markario.nn.genetics;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by markzepeda on 6/20/15.
 *
 * Note: this class has a natural ordering that is inconsistent with equals.
 */
public class Genome<T> implements Comparable<Genome<T>> {
    private List<T> weights;
    private double fitness;

    public Genome(){
        fitness = 0;
        weights = new LinkedList<>();
    }

    public Genome(double fitness, List<T> weights){
        this.fitness = fitness;
        this.weights = weights;
    }

    @Override
    public int compareTo(Genome<T> o) {
        return Double.compare(fitness, o.fitness);
    }
}
