package com.markario.nn.genetics;

import com.markario.nn.neural.weights.WeightHandler;

/**
 * Created by markzepeda on 6/21/15.
 */
public class GeneticsConfig<T> {
    public int populationSize = 100;

    //change that two genomes will cross genes
    public double crossoverRate = 0.7;

    //chance for mutation
    public double mutationRate = 0.1;

    //How much the chromosomes will change
    public double maxMutationPercentage = 0.3;

    //number of the genomes with highest fitness to copy over directly into a new generation.
    public int numBestToCopy = 4;

    //number of copies of the above highest fitness genomes to copy over in a new generation.
    public int numCopiesOfBest = 1;

    public WeightHandler<T> weightHandler;

    public GeneticsConfig(int populationSize, double crossoverRate, double mutationRate, double maxMutationPercentage, int numBestToCopy, int numCopiesOfBest, WeightHandler<T> weightHandler) {
        this.populationSize = populationSize;
        this.crossoverRate = crossoverRate;
        this.mutationRate = mutationRate;
        this.maxMutationPercentage = maxMutationPercentage;
        this.numBestToCopy = numBestToCopy;
        this.numCopiesOfBest = numCopiesOfBest;
        this.weightHandler = weightHandler;
    }
}
