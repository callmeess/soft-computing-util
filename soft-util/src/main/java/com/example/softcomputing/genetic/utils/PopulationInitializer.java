package com.example.softcomputing.genetic.utils;

import com.example.softcomputing.genetic.chromosome.BinaryChromosome;
import com.example.softcomputing.genetic.chromosome.IntegerChromosome;
import com.example.softcomputing.genetic.chromosome.FloatingPointChromosome;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Helper class to create randomized initial populations for different chromosome types.
 */
public class PopulationInitializer {

    /**
     * Creates a random Binary chromosome population.
     * @param populationSize number of individuals
     * @param geneLength number of genes per chromosome
     * @return list of randomized binary chromosomes
     */
    public static List<BinaryChromosome> randomBinaryPopulation(int populationSize, int geneLength) {
        List<BinaryChromosome> population = new ArrayList<>(populationSize);
        for (int i = 0; i < populationSize; i++) {
            population.add(BinaryChromosome.random(geneLength));
        }
        return population;
    }

    /**
     * Creates a random Integer chromosome population.
     * @param populationSize number of individuals
     * @param geneLength number of genes per chromosome
     * @param min minimum gene value (inclusive)
     * @param max maximum gene value (inclusive)
     * @return list of randomized integer chromosomes
     */
    public static List<IntegerChromosome> randomIntegerPopulation(int populationSize, int geneLength, int min, int max) {
        List<IntegerChromosome> population = new ArrayList<>(populationSize);
        Random rnd = new Random();
        for (int i = 0; i < populationSize; i++) {
            Integer[] genes = new Integer[geneLength];
            for (int g = 0; g < geneLength; g++) {
                genes[g] = rnd.nextInt(max - min + 1) + min;
            }
            population.add(new IntegerChromosome(genes));
        }
        return population;
    }

    /**
     * Creates a random FloatingPoint chromosome population.
     * @param populationSize number of individuals
     * @param geneLength number of genes per chromosome
     * @param lowerBound lower bound for gene values
     * @param upperBound upper bound for gene values
     * @return list of randomized floating point chromosomes
     */
    public static List<FloatingPointChromosome> randomFloatingPopulation(int populationSize, int geneLength, double lowerBound, double upperBound) {
        List<FloatingPointChromosome> population = new ArrayList<>(populationSize);
        for (int i = 0; i < populationSize; i++) {
            population.add(FloatingPointChromosome.randomInit(geneLength, lowerBound, upperBound));
        }
        return population;
    }
}
