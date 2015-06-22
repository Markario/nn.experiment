package com.markario.nn.genetics;

import com.markario.nn.Trace;
import com.markario.nn.neural.weights.WeightHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Created by markzepeda on 6/20/15.
 */
public class Genetics<T> {
    private List<Genome<T>> population;
    private GeneticsConfig<T> config;
    private int generationNum = 0;
    private Stats stats;
    Supplier<T> weightFactory;
    WeightHandler<T> weightHandler;

    private transient Random random = new Random();

    public Genetics() {
    }

    public Genetics(GeneticsConfig<T> config, int numWeights) {
        this.config = config;
        this.stats = new Stats();
        population = new ArrayList<>(config.populationSize);
        weightHandler = config.weightHandler;
        weightFactory = weightHandler::getIdentityWeight;

        for(int i = 0; i < config.populationSize; i++){
            population.add(new Genome<>(numWeights, weightHandler::getRandomWeight));
        }
    }

    private double randomClampedDouble() {
        return (random.nextDouble() - random.nextDouble());
    }

    private void mutateGenome(Genome<T> genome) {
        genome.weights().filter(t -> randomClampedDouble() < config.mutationRate)
                .forEachOrdered(weight -> weightHandler.mutateWeight(weight, randomClampedDouble() * config.maxMutationPercentage));
    }

    private Genome<T> roulette() {
        double slice = random.nextDouble() * stats.getPosTotalFitness();
        double fitnessCounter = 0.0;

        for (Genome<T> genome : population) {
            //Genomes with negative fitness do not get to reproduce.
            if(genome.getFitness() > 0) {
                fitnessCounter += genome.getFitness();
            }

            if (fitnessCounter >= slice) {
                return genome;
            }
        }

        if (fitnessCounter < stats.getTotalFitness()) {
            throw new IllegalStateException("Total fitness does not equal fitness calculated in roulette after reaching end");
        }

        throw new IllegalStateException("Should never happen... total fitness was reached in counter yet no genome was returned");
    }

    public void generation() {
        List<Genome<T>> newPopulation = new ArrayList<>(population.size());
        Collections.sort(population);
        Collections.reverse(population);
        stats.reset();
        stats.calculate();

        //If even numbered of elite genomes will be carried over.
        if (config.numBestToCopy * config.numCopiesOfBest % 2 == 0) {
            population.stream().limit(config.numBestToCopy).forEachOrdered(tGenome -> {
                for (int i = 0; i < config.numCopiesOfBest; i++) {
                    newPopulation.add(tGenome.getCopy(weightFactory, weightHandler));
                }
            });
        }

        while (newPopulation.size() < population.size()) {
            Genome<T> parent1 = roulette();
            Genome<T> parent2 = roulette();

            if (parent1 == parent2 || random.nextDouble() > config.crossoverRate) {
                newPopulation.add(parent1.getCopy(weightFactory, weightHandler));
                newPopulation.add(parent2.getCopy(weightFactory, weightHandler));
                continue;
            }

            int numWeights = parent1.getWeights().size();
            int crossoverIndex = random.nextInt(numWeights);

            List<T> parent1Weights = parent1.getWeights();
            List<T> parent2Weights = parent2.getWeights();

            List<T> weights1 = new ArrayList<>(numWeights);
            List<T> weights2 = new ArrayList<>(numWeights);

            for (int i = 0; i < numWeights; i++) {
                if (i < crossoverIndex) {
                    weights1.add(copyOf(parent1Weights.get(i)));
                    weights2.add(copyOf(parent2Weights.get(i)));
                } else {
                    weights1.add(copyOf(parent2Weights.get(i)));
                    weights2.add(copyOf(parent1Weights.get(i)));
                }
            }

            Genome<T> child1 = new Genome<>(0, weights1);
            Genome<T> child2 = new Genome<>(0, weights2);

            mutateGenome(child1);
            mutateGenome(child2);

            newPopulation.add(child1);
            newPopulation.add(child2);
        }

        Trace.v("Created new generation.");
        Trace.v(String.format("Previous generation: %d stats: %s", generationNum, stats.toString()));

        population.clear();
        population = newPopulation;
        generationNum++;
    }

    private T copyOf(T weight) {
        T newWeight = weightFactory.get();
        config.weightHandler.copyIntoWeight(newWeight, weight);
        return newWeight;
    }

    public class Stats {
        private double posTotalFitness = 0;
        private double totalFitness = 0;
        private double bestFitness = 0;
        private double averageFitness = 0;
        private double worstFitness = Double.MAX_VALUE;
        private int mostFitGenomeIndex = 0;

        public void calculate() {
            if (population.size() == 0) {
                return;
            }

            posTotalFitness = 0;
            totalFitness = 0;
            bestFitness = 0;
            worstFitness = Double.MAX_VALUE;
            for (int i = 0; i < population.size(); i++) {
                double currentFitness = population.get(i).getFitness();
                if (currentFitness > bestFitness) {
                    bestFitness = currentFitness;
                    mostFitGenomeIndex = i;
                }
                if (currentFitness < worstFitness) {
                    worstFitness = currentFitness;
                }
                totalFitness += currentFitness;

                if(currentFitness > 0){
                    posTotalFitness += currentFitness;
                }
            }

            averageFitness = totalFitness / population.size();
        }

        public void reset() {
            posTotalFitness = 0;
            totalFitness = 0;
            bestFitness = 0;
            averageFitness = 0;
            worstFitness = Double.MAX_VALUE;
            mostFitGenomeIndex = 0;
        }

        @Override
        public String toString() {
            return "Stats{" +
                    "totalFitness=" + totalFitness +
                    ", bestFitness=" + bestFitness +
                    ", averageFitness=" + averageFitness +
                    ", worstFitness=" + worstFitness +
                    ", mostFitGenomeIndex=" + mostFitGenomeIndex +
                    '}';
        }

        public double getPosTotalFitness() {
            return posTotalFitness;
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

        public int getMostFitGenomeIndex() {
            return mostFitGenomeIndex;
        }
    }

    public Stats getStats() {
        return stats;
    }

    public Stream<Genome<T>> genomes() {
        return genomes(this);
    }

    public static <T> Stream<Genome<T>> genomes(Genetics<T> genetics) {
        return genetics.population.stream();
    }

    public List<Genome<T>> getPopulation() {
        return population;
    }

    public int getGenerationNum() {
        return generationNum;
    }

}
