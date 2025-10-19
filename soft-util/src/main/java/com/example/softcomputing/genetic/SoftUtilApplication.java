package com.example.softcomputing.genetic;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.example.softcomputing.genetic.chromosome.BinaryChromosome;
import com.example.softcomputing.genetic.chromosome.FloatingPointChromosome;
import com.example.softcomputing.genetic.chromosome.IntegerChromosome;
import com.example.softcomputing.genetic.chromosome.Factories.BinaryChromosomeFactory;
import com.example.softcomputing.genetic.chromosome.Factories.ChromosomeFactory;
import com.example.softcomputing.genetic.chromosome.Factories.FloatingPointChromosomeFactory;
import com.example.softcomputing.genetic.chromosome.Factories.IntegerChromosomeFactory;
import com.example.softcomputing.genetic.core.GeneticAlgorithm;
import com.example.softcomputing.genetic.operators.crossover.SinglePointCrossover;
import com.example.softcomputing.genetic.operators.crossover.UniformCrossover;
import com.example.softcomputing.genetic.operators.mutation.BinaryMutation;
import com.example.softcomputing.genetic.operators.mutation.IntegerMutation;
import com.example.softcomputing.genetic.operators.mutation.UniformMutation;
import com.example.softcomputing.genetic.operators.replacement.FullGenerationReplacement;
import com.example.softcomputing.genetic.operators.selection.RandomSelection;
import com.example.softcomputing.genetic.operators.selection.RouletteWheelSelection;

/**
 * Main application with static examples for Binary, Integer, and FloatingPoint chromosomes.
 * Uncomment the example you want to run in main().
 */
public class SoftUtilApplication {
   
    public static void main(String[] args) {
        System.out.println("=== SoftUtil Genetic Algorithm Examples ===\n");
        System.out.println("Choose chromosome type:");
        System.out.println("1. Binary Chromosome");
        System.out.println("2. Integer Chromosome");
        System.out.println("3. FloatingPoint Chromosome");
        System.out.print("\nEnter your choice (1, 2, or 3): ");
        
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        int choice = scanner.nextInt();
        System.out.println();
        
        switch (choice) {
            case 1:
                runBinaryExample();
                break;
            case 2:
                runIntegerExample();
                break;
            case 3:
                runFloatingPointExample();
                break;
            default:
                System.out.println("Invalid choice. Please run again and select 1, 2, or 3.");
        }
        
        scanner.close();
    }

    private static void runBinaryExample() {
        System.out.println("--- Binary Chromosome GA ---");
        
        // Configuration (easy to change) - MATCHED TO INTEGER EXAMPLE
        int populationSize = 50;  // Increased from 4 to 50
        int geneLength = 10;
        double mutationRate = 0.05;
        int maxGenerations = 100;  // Increased from 10 to 100
        
        System.out.printf("Config: popSize=%d, geneLength=%d, mutationRate=%.3f, maxGen=%d\n", 
            populationSize, geneLength, mutationRate, maxGenerations);
        
        // Create random initial population
        List<BinaryChromosome> population = new ArrayList<>();
        for (int i = 0; i < populationSize; i++) {
            population.add(BinaryChromosome.random(geneLength));
        }
        
        // Factory for crossover
        ChromosomeFactory<Integer, BinaryChromosome> factory = new BinaryChromosomeFactory();
        
        // Build and run GA
        GeneticAlgorithm<BinaryChromosome> ga = GeneticAlgorithm.<BinaryChromosome>builder()
            .withPopulationSize(populationSize)
            .withPopulation(population)
            .withChromosomeFactory(factory)
            .withSelectionStrategy(new RandomSelection<>())
            .withCrossoverStrategy(new SinglePointCrossover<>(factory))
            .withMutationStrategy(new BinaryMutation(mutationRate))
            .withReplacementStrategy(new FullGenerationReplacement<>())
            .withMaxGenerations(maxGenerations)
            .build();
        
        ga.run();
        System.out.println("Binary GA completed.\n");
    }

    /**
     * Integer Chromosome Example with randomized population.
     * Default: popSize=50, geneLength=10, geneRange=[0,99], mutationRate=0.05, maxGen=100
     */
    private static void runIntegerExample() {
        System.out.println("--- Integer Chromosome GA ---");
        
        // Configuration (easy to change)
        int populationSize = 50;
        int geneLength = 10;
        int geneMin = 0;
        int geneMax = 99;
        double mutationRate = 0.05;
        int maxGenerations = 100;
        
        System.out.printf("Config: popSize=%d, geneLength=%d, geneRange=[%d,%d], mutationRate=%.3f, maxGen=%d\n", 
            populationSize, geneLength, geneMin, geneMax, mutationRate, maxGenerations);
        
        // Create random initial population
        Random rnd = new Random();
        ChromosomeFactory<Integer, IntegerChromosome> factory = new IntegerChromosomeFactory();
        List<IntegerChromosome> population = new ArrayList<>();
        
        for (int i = 0; i < populationSize; i++) {
            Integer[] genes = new Integer[geneLength];
            for (int g = 0; g < geneLength; g++) {
                genes[g] = rnd.nextInt(geneMax - geneMin + 1) + geneMin;
            }
            population.add(factory.create(genes));
        }
        
        // Build and run GA
        GeneticAlgorithm<IntegerChromosome> ga = GeneticAlgorithm.<IntegerChromosome>builder()
            .withPopulationSize(populationSize)
            .withPopulation(population)
            .withChromosomeFactory(factory)
            .withSelectionStrategy(new RandomSelection<>())
            .withCrossoverStrategy(new SinglePointCrossover<>(factory))
            .withMutationStrategy(new IntegerMutation(mutationRate))
            .withReplacementStrategy(new FullGenerationReplacement<>())
            .withMaxGenerations(maxGenerations)
            .build();
        
        ga.run();
        System.out.println("Integer GA completed.\n");
    }

    /**
     * FloatingPoint Chromosome Example with randomized population.
     * Default: popSize=50, geneLength=10, bounds=[-10.0, 10.0], mutationRate=0.05, maxGen=100
     */
    private static void runFloatingPointExample() {
        System.out.println("--- FloatingPoint Chromosome GA ---");
        
        // Configuration (easy to change)
        int populationSize = 50;
        int geneLength = 10;
        double lowerBound = -10.0;
        double upperBound = 10.0;
        double mutationRate = 0.05;
        int maxGenerations = 100;
        
        System.out.printf("Config: popSize=%d, geneLength=%d, bounds=[%.1f,%.1f], mutationRate=%.3f, maxGen=%d\n", 
            populationSize, geneLength, lowerBound, upperBound, mutationRate, maxGenerations);
        
        // Create random initial population
        List<FloatingPointChromosome> population = new ArrayList<>();
        for (int i = 0; i < populationSize; i++) {
            population.add(FloatingPointChromosome.randomInit(geneLength, lowerBound, upperBound));
        }
        
        // Factory for crossover
        ChromosomeFactory<Double, FloatingPointChromosome> factory = 
            new FloatingPointChromosomeFactory(lowerBound, upperBound);
        
        // Build and run GA
        GeneticAlgorithm<FloatingPointChromosome> ga = GeneticAlgorithm.<FloatingPointChromosome>builder()
            .withPopulationSize(populationSize)
            .withPopulation(population)
            .withChromosomeFactory(factory)
            .withSelectionStrategy(new RandomSelection<>())
            .withCrossoverStrategy(new SinglePointCrossover<>(factory))
            .withMutationStrategy(new UniformMutation(mutationRate))
            .withReplacementStrategy(new FullGenerationReplacement<>())
            .withMaxGenerations(maxGenerations)
            .build();
        
        ga.run();
        System.out.println("FloatingPoint GA completed.\n");
    }
}
