package com.example.softcomputing.utils;

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
import com.example.softcomputing.genetic.operators.mutation.BinaryMutation;
import com.example.softcomputing.genetic.operators.mutation.IntegerMutation;
import com.example.softcomputing.genetic.operators.mutation.UniformMutation;
import com.example.softcomputing.genetic.operators.replacement.FullGenerationReplacement;
import com.example.softcomputing.genetic.operators.selection.RandomSelection;
import com.example.softcomputing.genetic.utils.PopulationInitializer;

public class TestCases {

    private static final AppLogger _logger = AppLogger.getLogger(TestCases.class);

    public static void runBinaryExample() {
        _logger.info("--- Binary Chromosome GA ---");

        int populationSize = 50;
        int geneLength = 10;
        double mutationRate = 0.05;
        int maxGenerations = 100;

        _logger.info(String.format("Config: popSize=%d, geneLength=%d, mutationRate=%.3f, maxGen=%d",
                populationSize, geneLength, mutationRate, maxGenerations));

        List<BinaryChromosome> population = new ArrayList<>();
        for (int i = 0; i < populationSize; i++) {
            population.add(BinaryChromosome.random(geneLength));
        }

        ChromosomeFactory<Integer, BinaryChromosome> factory = new BinaryChromosomeFactory();

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
        _logger.info("Binary GA completed.\n");
    }

    public static void runIntegerExample() {
        _logger.info("--- Integer Chromosome GA ---");
        
        int populationSize = 50;
        int geneLength = 10;
        int geneMin = 0;
        int geneMax = 99;
        double mutationRate = 0.05;
        int maxGenerations = 100;
        
        _logger.info(String.format("Config: popSize=%d, geneLength=%d, geneRange=[%d,%d], mutationRate=%.3f, maxGen=%d", 
            populationSize, geneLength, geneMin, geneMax, mutationRate, maxGenerations));
        
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
        _logger.info("Integer GA completed.\n");
    }

    public static void runFloatingPointExample() {
        _logger.info("--- FloatingPoint Chromosome GA ---");

        int populationSize = 50;
        int geneLength = 10;
        double lowerBound = -10.0;
        double upperBound = 10.0;
        double mutationRate = 0.05;
        int maxGenerations = 100;

        _logger.info(String.format("Config: popSize=%d, geneLength=%d, bounds=[%.1f,%.1f], mutationRate=%.3f, maxGen=%d",
            populationSize, geneLength, lowerBound, upperBound, mutationRate, maxGenerations));
        
        List<FloatingPointChromosome> population;
        population = PopulationInitializer.randomFloatingPopulation(populationSize, geneLength, lowerBound, upperBound);
        
        ChromosomeFactory<Double, FloatingPointChromosome> factory = 
            new FloatingPointChromosomeFactory(lowerBound, upperBound);
        
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
        _logger.info("FloatingPoint GA completed.\n");
    }
}
