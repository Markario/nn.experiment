package com.markario.nn.genetics;

import com.markario.nn.neural.weights.WeightHandler;
import com.sun.tools.javac.jvm.Gen;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Created by markzepeda on 6/20/15.
 *
 * Note: this class has a natural ordering that is inconsistent with equals.
 */
public class Genome<T> implements Comparable<Genome<T>> {
    private List<T> weights;
    private double fitness;

    public Genome(){}

    public Genome(int numWeights, Supplier<T> randomWeightGenerator){
        fitness = 0;
        weights = new ArrayList<>(numWeights);
        for(int i = 0; i < numWeights; i++){
            weights.add(randomWeightGenerator.get());
        }
    }

    public Genome(double fitness, List<T> weights){
        this.fitness = fitness;
        this.weights = weights;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public void addFitness(double amount){
        fitness += amount;
    }

    public double getFitness() {
        return fitness;
    }

    public List<T> getWeights(){
        return weights;
    }

    public Stream<T> weights(){
        return weights.stream();
    }

    public Genome<T> getCopy(Supplier<T> weightFactory, WeightHandler<T> weightHandler){
        Genome<T> newGenome = new Genome<>();
        newGenome.weights = new ArrayList<>(weights.size());
        newGenome.fitness = fitness;

        weights().forEach(t -> {
            T weight = weightFactory.get();
            weightHandler.copyIntoWeight(weight, t);
            newGenome.weights.add(weight);
        });
        return newGenome;
    }

    @Override
    public int compareTo(Genome<T> o) {
        return Double.compare(fitness, o.fitness);
    }
}
