package com.markario.nn;

/**
 * Created by markzepeda on 6/21/15.
 */
public class ExperimentConfig {
    int numInputs = 4;
    int numHiddenLayers = 3;
    int numNeuronsPerHiddenLayer = 5;
    int numOutputs = 10;

    int populationSize = 25;
    double crossoverRate = .7;
    double mutationRate = .1;
    double maxMutationPercentage = .3;
    int numBestToCopy = 4;
    int numCopiesOfBest = 1;
}
