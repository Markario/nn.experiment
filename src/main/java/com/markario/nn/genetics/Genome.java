package com.markario.nn.genetics;

import java.util.LinkedList;

/**
 * Created by markzepeda on 6/20/15.
 *
 * Note: this class has a natural ordering that is inconsistent with equals.
 */
public class Genome<T> implements Comparable {
    private LinkedList<T> weights;
    private float fitness;

    public Genome(){
        fitness = 0f;
        weights = new LinkedList<>();
    }

    public Genome(float fitness, LinkedList<T> weights){
        this.fitness = fitness;
        this.weights = weights;
    }

    @Override
    public int compareTo(Object o) {
        return (int) Math.signum(fitness - ((Genome) o).fitness);
    }
}
