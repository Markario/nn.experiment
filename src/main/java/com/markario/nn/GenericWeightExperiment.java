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
import com.markario.nn.util.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

/**
 * Created by markzepeda on 6/21/15.
 */
public class GenericWeightExperiment<T extends GenericWeight<T>> {

    public static void main(String[] args){
        ExperimentConfig config = FileUtil.readTFromTextFile(new File("config.json"), ExperimentConfig.class);
        GenericWeightExperiment<DoubleWeight> experiment = new GenericWeightExperiment<>(config, () -> new DoubleWeight().zero());

        List<DoubleWeight> inputs = new ArrayList<>(config.numInputs);
        for(int i = 0; i < config.numInputs; i++){
            inputs.add(new DoubleWeight().random());
        }

        Random random = new Random();
        for(int gen = 0; gen < 100; gen++){
            for(int updates = 0; updates < 100; updates++){
                for(int i = 0; i < config.populationSize; i++){
                    List<DoubleWeight> outputs = experiment.update(i, inputs);
                    //train the networks to output higher values.
                    double totalFitness = outputs.stream().mapToDouble(value -> value.value).sum();
                    outputs.stream().forEach(value -> System.out.print(" "+value.value));
                    Trace.v("");
                    experiment.setFitness(i, totalFitness);
                }
                Trace.v("----");
            }

            //end of generation.
            Trace.v("------------------------------");
            experiment.nextGeneration();
            Trace.v("------------------------------");
        }
    }

    private transient WeightHandler<T> weightHandler;
    private transient Supplier<T> randomWeightFactory;
    private transient NeuralNetworkConfig<T> networkConfig;
    private transient GeneticsConfig<T> geneticsConfig;
    private transient Genetics<T> genetics;
    private transient List<NeuralNetwork<T>> neuralNetworks;
    private ExperimentConfig config;
    private transient int numWeightsPerGenome;

    public GenericWeightExperiment(ExperimentConfig config, Supplier<T> weightFactory){
        Trace.v("NN Experiment.");

        this.config = config;
        weightHandler = new GenericWeightHandler<>(weightFactory);
        randomWeightFactory = weightHandler::getRandomWeight;

        T activationResponse = weightHandler.getIdentityWeight().negate();
        T bias = weightHandler.getIdentityWeight().negate();

        networkConfig = new NeuralNetworkConfig<>(activationResponse, bias, config.numInputs, config.numHiddenLayers, config.numNeuronsPerHiddenLayer, config.numOutputs, weightHandler);
        geneticsConfig = new GeneticsConfig<>(config.populationSize, config.crossoverRate, config.mutationRate, config.maxMutationPercentage, config.numBestToCopy, config.numCopiesOfBest, weightHandler);

        numWeightsPerGenome = NeuralNetwork.calculateNumWeights(networkConfig);
        genetics = new Genetics<>(geneticsConfig, numWeightsPerGenome);
        neuralNetworks = new ArrayList<>(config.populationSize);
        List<Genome<T>> population = genetics.getPopulation();

        for(int i = 0; i < config.populationSize; i++){
            NeuralNetwork<T> neuralNetwork = new NeuralNetwork<>(networkConfig);
            neuralNetwork.setWeights(population.get(i).getWeights());
            neuralNetworks.add(neuralNetwork);
        }
    }

    public List<List<T>> updateAll(List<T> inputs){
        List<List<T>> outputSets = new ArrayList<>(config.populationSize);
        for(int i = 0; i < config.populationSize; i++){
            outputSets.add(neuralNetworks.get(i).update(inputs));
        }
        return outputSets;
    }

    public void nextGeneration(){
        genetics.generation();
        List<Genome<T>> population = genetics.getPopulation();
        for(int i = 0; i < config.populationSize; i++){
            getNeuralNetwork(i).setWeights(population.get(i).getWeights());
        }
    }

    public List<T> update(int index, List<T> inputs){
        return neuralNetworks.get(index).update(inputs);
    }

    public NeuralNetwork<T> getNeuralNetwork(int index){
        return neuralNetworks.get(index);
    }

    public double getFitness(int index){
        return genetics.getPopulation().get(index).getFitness();
    }

    public void addFitness(int index, double amount){
        genetics.getPopulation().get(index).addFitness(amount);
    }

    public void setFitness(int index, double fitness){
        genetics.getPopulation().get(index).setFitness(fitness);
    }

    public WeightHandler<T> getWeightHandler() {
        return weightHandler;
    }

    public Supplier<T> getRandomWeightFactory() {
        return randomWeightFactory;
    }

    public NeuralNetworkConfig<T> getNetworkConfig() {
        return networkConfig;
    }

    public GeneticsConfig<T> getGeneticsConfig() {
        return geneticsConfig;
    }

    public Genetics<T> getGenetics() {
        return genetics;
    }

    public List<NeuralNetwork<T>> getNeuralNetworks() {
        return neuralNetworks;
    }

    public ExperimentConfig getConfig() {
        return config;
    }

    public int getNumWeightsPerGenome() {
        return numWeightsPerGenome;
    }
}
