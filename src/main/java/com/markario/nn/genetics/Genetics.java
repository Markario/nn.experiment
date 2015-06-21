package com.markario.nn.genetics;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Created by markzepeda on 6/20/15.
 */
public class Genetics<T> {
    private List<Genome<T>> population;
    private GeneticsConfig<T> config;
    private double totalFitness = 0;
    private double bestFitness = 0;
    private double averageFitness = 0;
    private double worstFitness = Double.MAX_VALUE;
    private int	fittestGenomeIndex = 0;
    private int	generationNum = 0;

    public Genetics(GeneticsConfig<T> config){
        this.config = config;
        population = new ArrayList<>(config.populationSize);
    }

    public Stream<Genome<T>> genomes(){
        return genomes(this);
    }

    public static <T> Stream<Genome<T>> genomes(Genetics<T> genetics){
        return genetics.population.stream();
    }

    public List<Genome<T>> getPopulation() {
        return population;
    }

    public double getTotalFitness() {
        return totalFitness;
    }

    public double getBestFitness() {
        return bestFitness;
    }

    public double getAverageFitness() {
        return averageFitness;
    }

    public double getWorstFitness() {
        return worstFitness;
    }

    public int getFittestGenomeIndex() {
        return fittestGenomeIndex;
    }

    public int getGenerationNum() {
        return generationNum;
    }

}
