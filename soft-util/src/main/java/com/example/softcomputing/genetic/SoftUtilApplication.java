package com.example.softcomputing.genetic;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import com.example.softcomputing.genetic.chromosome.BinaryChromosome;
import com.example.softcomputing.genetic.operators.mutation.BinaryMutation;
import com.example.softcomputing.genetic.chromosome.IntegerChromosome;
import com.example.softcomputing.genetic.chromosome.Factories.ChromosomeFactory;
import com.example.softcomputing.genetic.chromosome.Factories.IntegerChromosomeFactory;
import com.example.softcomputing.genetic.core.GeneticAlgorithm;

import com.example.softcomputing.genetic.operators.selection.RandomSelection;
import com.example.softcomputing.genetic.operators.crossover.UniformCrossover;
import com.example.softcomputing.utils.AppLogger;

import com.example.softcomputing.genetic.operators.crossover.SinglePointCrossover;
import com.example.softcomputing.genetic.operators.mutation.IntegerMutation;
import com.example.softcomputing.genetic.operators.replacement.FullGenerationReplacement;

public class SoftUtilApplication {
    private static AppLogger logger;

    private static void runBinaryGAExample() {
        System.out.println("\n--- Binary GA Example (as in slides) ---");
        int populationSize = 3 + (int) (Math.random() * 6); // random size between 3 and 8
        double mutationRate = 0.01 + Math.random() * 0.3; // random mutation rate between 0.01 and 0.31
        BinaryChromosome[] pop = new BinaryChromosome[populationSize];
        for (int i = 0; i < populationSize; i++) {
            pop[i] = BinaryChromosome.random(populationSize);
        }
        System.out.printf("Randomized parameters: popSize=%d, mutationRate=%.3f\n", populationSize, mutationRate);

        // Print initial population and fitness
        System.out.println("Generation G0:");
        for (int i = 0; i < populationSize; i++) {
            System.out.printf("G0_%d: %s\tFitness: %d\n", i + 1, pop[i], pop[i].toInt());
        }

        // Mutation (flip bits with probability)
        BinaryMutation mut = new BinaryMutation(mutationRate);

        // Generalized loop: keep evolving until optimal or max generations
        BinaryChromosome[] population = new BinaryChromosome[populationSize];
        for (int i = 0; i < populationSize; i++) {
            population[i] = pop[i];
        }
        int maxTries = 100;
        int generation = 0;
        int optimal = (1 << populationSize) - 1; // all bits 1 for current chromosome size
        boolean found = false;
        int bestFitness = 0;
        while (generation < maxTries && !found) {
            System.out.println("\nGeneration G" + generation + ":");
            for (int i = 0; i < populationSize; i++) {
                int fit = population[i].toInt();
                System.out.printf("G%d_%d: %s\tFitness: %d\n", generation, i + 1, population[i], fit);
                if (fit == optimal) {
                    found = true;
                }
                if (fit > bestFitness) {
                    bestFitness = fit;
                }
            }
            if (found) {
                break;
            }
            // Select two best
            int[] fitness = new int[populationSize];
            for (int i = 0; i < populationSize; i++) {
                fitness[i] = population[i].toInt();
            }
            int first = 0, second = 1;
            if (fitness[1] > fitness[0]) {
                first = 1;
                second = 0;
            }
            for (int i = 2; i < populationSize; i++) {
                if (fitness[i] > fitness[first]) {
                    second = first;
                    first = i;
                } else if (fitness[i] > fitness[second]) {
                    second = i;
                }
            }
            BinaryChromosome parent1 = population[first];
            BinaryChromosome parent2 = population[second];
            System.out.println("\nSelected for crossover:");
            System.out.printf("Parent1: %s (%d)\n", parent1, parent1.toInt());
            System.out.printf("Parent2: %s (%d)\n", parent2, parent2.toInt());
            // Uniform crossover for each generation
            ChromosomeFactory<Integer, BinaryChromosome> binFactory = genes -> {
                int[] bits = new int[genes.length];
                for (int i = 0; i < genes.length; i++) {
                    bits[i] = genes[i];
                }
                return new BinaryChromosome(bits);
            };
            UniformCrossover<Integer, BinaryChromosome> crossover = new UniformCrossover<>(binFactory);
            java.util.List<BinaryChromosome> children = crossover.crossover(parent1, parent2);
            BinaryChromosome child1 = children.get(0);
            BinaryChromosome child2 = children.get(1);
            // Mutate children
            mut.mutate(child1);
            mut.mutate(child2);
            System.out.println("\nChildren after crossover and mutation:");
            System.out.printf("Child1: %s\tFitness: %d\n", child1, child1.toInt());
            System.out.printf("Child2: %s\tFitness: %d\n", child2, child2.toInt());
            // Replace two worst in population
            int[] idx = new int[populationSize];
            for (int i = 0; i < populationSize; i++) {
                idx[i] = i;
            }
            // Sort idx by fitness ascending
            for (int i = 0; i < populationSize - 1; i++) {
                for (int j = i + 1; j < populationSize; j++) {
                    if (fitness[idx[i]] > fitness[idx[j]]) {
                        int t = idx[i];
                        idx[i] = idx[j];
                        idx[j] = t;
                    }
                }
            }
            population[idx[0]] = child1;
            population[idx[1]] = child2;
            generation++;
        }
        if (found) {
            System.out.println("\nOptimal solution found!");
        } else {
            System.out.printf("\nMax tries (%d) reached, best fitness found: %d\n", maxTries, bestFitness);
        }
        for (int i = 0; i < populationSize; i++) {
            System.out.printf("Final G%d_%d: %s\tFitness: %d\n", generation, i + 1, population[i], population[i].toInt());
        }
    }

    public static void main(String[] args) {
        // runBinaryGAExample();

        AppLogger.configure(Level.INFO);
        logger = AppLogger.getLogger(SoftUtilApplication.class);
        logger.info("Starting soft-util...");
        logger.info("Running Integer Chromosome GA example...");

        double mutationRate = 0.05;
        int populationSize = 50;
        int geneLength = 10;
        ChromosomeFactory<Integer, IntegerChromosome> factory = new IntegerChromosomeFactory();

        java.util.Random rnd = new java.util.Random();

        List<IntegerChromosome> initialPopulation = new ArrayList<>(populationSize);
        for (int i = 0; i < populationSize; i++) {
            Integer[] genes = new Integer[geneLength];
            for (int g = 0; g < geneLength; g++) {
                genes[g] = rnd.nextInt(100); //  [0,99]
            }
            initialPopulation.add(factory.create(genes));
        }

        GeneticAlgorithm<IntegerChromosome> ga = GeneticAlgorithm.<IntegerChromosome>builder()
                .withPopulationSize(populationSize)
                .withPopulation(initialPopulation)
                .withChromosomeFactory(factory)
                .withSelectionStrategy(new RandomSelection<IntegerChromosome>())
                .withCrossoverStrategy(new SinglePointCrossover<IntegerChromosome>())
                .withMutationStrategy(new IntegerMutation(mutationRate))
                .withReplacementStrategy(new FullGenerationReplacement<IntegerChromosome>())
                .withMaxGenerations(100)
                .build();

        ga.run();
    }

}
