package com.markario.nn;

import com.markario.nn.genetics.Genetics;
import com.markario.nn.genetics.GeneticsConfig;
import com.markario.nn.genetics.Genome;
import com.markario.nn.neural.NeuralNetwork;
import com.markario.nn.neural.NeuralNetworkConfig;
import com.markario.nn.neural.weights.DoubleWeight;
import com.markario.nn.neural.weights.GenericWeight;
import com.markario.nn.neural.weights.GenericWeightHandler;
import com.markario.nn.neural.weights.WeightHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * Created by markzepeda on 6/21/15.
 */
public class GenericWeightExperiment<T extends GenericWeight<T>> {

    public static void main(String[] args){
        GenericWeightExperiment<DoubleWeight> experiment = new GenericWeightExperiment<>(() -> new DoubleWeight().zero());
    }

    WeightHandler<T> weightHandler;
    Supplier<T> randomWeightFactory;
    NeuralNetworkConfig<T> networkConfig;
    GeneticsConfig<T> geneticsConfig;
    Genetics<T> genetics;
    List<NeuralNetwork<T>> neuralNetworks;

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

    public GenericWeightExperiment(Supplier<T> weightFactory){
        Trace.v("NN Experiment.");

        weightHandler = new GenericWeightHandler<>(weightFactory);
        randomWeightFactory = weightHandler::getRandomWeight;

        T activationResponse = weightHandler.getIdentityWeight().negate();
        T bias = weightHandler.getIdentityWeight().negate();

        networkConfig = new NeuralNetworkConfig<>(activationResponse, bias, numInputs, numHiddenLayers, numNeuronsPerHiddenLayer, numOutputs, weightHandler);
        geneticsConfig = new GeneticsConfig<>(populationSize, crossoverRate, mutationRate, maxMutationPercentage, numBestToCopy, numCopiesOfBest, weightHandler);

        int numWeightsPerGenome = NeuralNetwork.calculateNumWeights(networkConfig);
        genetics = new Genetics<>(geneticsConfig, numWeightsPerGenome);
        neuralNetworks = new ArrayList<>(populationSize);
        List<Genome<T>> population = genetics.getPopulation();

        for(int i = 0; i < populationSize; i++){
            NeuralNetwork<T> neuralNetwork = new NeuralNetwork<>(networkConfig);
            neuralNetwork.setWeights(population.get(i).getWeights());
        }
    }
}
