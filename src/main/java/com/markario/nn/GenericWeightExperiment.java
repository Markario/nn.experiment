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
        DoubleWeight weight = new DoubleWeight();
        inputs.add(weight);

        for(int gen = 0; gen < 250000; gen++){
            double input = 0.0d;
            while(input <= 2 * Math.PI){
                for(int i = 0; i < config.populationSize; i++){
                    double target = Math.sin(input);
                    weight.value = input;
                    List<DoubleWeight> outputs = experiment.update(i, inputs);
                    //double sum = outputs.stream().mapToDouble(value -> value.value).sum();
                    double output = outputs.get(0).value;
                    double fitness = 1 - Math.abs(target-output);
                    experiment.addFitness(i, fitness);
                    //Trace.v(String.format("i: %d, target: %f, sum: %f, fitnessChange: %f, totalFitness: %f",i, target, sum, fitness, experiment.getFitness(i)));
                }
                input += .01;
            }

            experiment.nextGeneration();

            if(gen % 10 == 0){
                Trace.v(String.format("Gen: %d, stats: %s",experiment.genetics.getGenerationNum(), experiment.genetics.getStats().toString()));
            }
        }

        Trace.v("------------------------------");
        Trace.v("Best chosen");
        double input = 0.0d;
        experiment.setFitness(0,0);
        double maxFitness = 0d;
        int iterations = 10;
        while(input < Math.PI * 2 && iterations > 0){
            weight.value = input;
            double correctValue = Math.sin(input);
            List<DoubleWeight> outputs = experiment.update(0, inputs);
            //train the networks to output higher values.
            double sum = outputs.stream().mapToDouble(value -> value.value).sum();
            Trace.v(String.format("Input: %f CorrectValue: %f, Output: %f AbsDiff: %f", input, correctValue, sum, Math.abs(correctValue-sum)));
            double fitness = 1 - Math.abs(correctValue-sum);
            experiment.addFitness(0, fitness);
            input += .01;
            maxFitness += 1;
            if(input >= Math.PI * 2){
                iterations--;
                input = 0;
            }
        }
        Trace.v("BestGenome's fitness: "+experiment.getFitness(0)+" maxFitnessPossible: "+maxFitness+" grade: "+((experiment.getFitness(0)/maxFitness)*100d));
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

        if(!(config.populationSize % 2 == 0)){
            throw new IllegalArgumentException("Population size must be even");
        }

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
