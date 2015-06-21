package com.markario.nn.genetics;

import java.util.function.Supplier;

/**
 * Created by markzepeda on 6/21/15.
 */
public class GeneticsConfig<T> {
    public int populationSize = 100;

    //probability of chromosomes crossing over bits
    //0.7 is pretty good
    public double crossoverRate = 0.7;

    //probability that a chromosomes bits will mutate.
    //Try figures around 0.05 to 0.3 ish
    public double mutationRate = 0.1;

    //How much the chromosomes will change
    public double maxPerturbation = 0.3;

    public int numGenomeWeights;

    public Supplier<T> weightFactory;
}
