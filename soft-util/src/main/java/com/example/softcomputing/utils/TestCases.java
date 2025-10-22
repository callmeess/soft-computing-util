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
import com.example.softcomputing.genetic.operators.crossover.TwoPointCrossover;
import com.example.softcomputing.genetic.operators.crossover.UniformCrossover;
import com.example.softcomputing.genetic.operators.mutation.BinaryMutation;
import com.example.softcomputing.genetic.operators.mutation.IntegerMutation;
import com.example.softcomputing.genetic.operators.mutation.UniformMutation;
import com.example.softcomputing.genetic.operators.mutation.NonUniformMutation;
import com.example.softcomputing.genetic.operators.replacement.FullGenerationReplacement;
import com.example.softcomputing.genetic.operators.replacement.SteadyStateReplacement;
import com.example.softcomputing.genetic.operators.replacement.ElitismReplacement;
import com.example.softcomputing.genetic.operators.selection.RandomSelection;
import com.example.softcomputing.genetic.operators.selection.RankSelection;
import com.example.softcomputing.genetic.utils.PopulationInitializer;

public class TestCases {

    private static final AppLogger _logger = AppLogger.getLogger(TestCases.class);

    public static void runBinaryExample() {
        _logger.info("--- Binary Chromosome GA ---");

        int populationSize = 50;
        int geneLength = 10;
        double crossoverRate = 0.7;
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
                .withCrossoverStrategy(new SinglePointCrossover<>(crossoverRate, factory))
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
        double crossoverRate = 0.7;
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
                .withCrossoverStrategy(new SinglePointCrossover<>(crossoverRate, factory))
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
        double crossoverRate = 0.7;
        double mutationRate = 0.05;
        int maxGenerations = 100;

        _logger.info(
                String.format("Config: popSize=%d, geneLength=%d, bounds=[%.1f,%.1f], mutationRate=%.3f, maxGen=%d",
                        populationSize, geneLength, lowerBound, upperBound, mutationRate, maxGenerations));

        List<FloatingPointChromosome> population;
        population = PopulationInitializer.randomFloatingPopulation(populationSize, geneLength, lowerBound, upperBound);

        ChromosomeFactory<Double, FloatingPointChromosome> factory = new FloatingPointChromosomeFactory(lowerBound,
                upperBound);

        GeneticAlgorithm<FloatingPointChromosome> ga = GeneticAlgorithm.<FloatingPointChromosome>builder()
                .withPopulationSize(populationSize)
                .withPopulation(population)
                .withChromosomeFactory(factory)
                .withSelectionStrategy(new RandomSelection<>())
                .withCrossoverStrategy(new SinglePointCrossover<>(crossoverRate, factory))
                .withMutationStrategy(new UniformMutation(mutationRate))
                .withReplacementStrategy(new FullGenerationReplacement<>())
                .withMaxGenerations(maxGenerations)
                .build();

        ga.run();
        _logger.info("FloatingPoint GA completed.\n");
    }

    public static void runIntegrationPermutationTests() {
        _logger.info("--- Integration Permutation Tests (wrapper) ---");
        // wrapper that calls isolated scenario methods; individual methods can be
        // called directly for isolated testing
        runIntegrationIntegerPermutations();
        runIntegrationBinaryPermutation();
        runIntegrationFloatingPermutation();
        _logger.info("Integration Permutation Tests completed.\n");
    }

    public static void runIntegrationIntegerPermutations() {
        _logger.info("--- Integration: Integer Chromosome Permutations ---");

        int populationSize = 6;
        int geneLength = 5;
        int maxGenerations = 10;

        _logger.info(String.format("Config: popSize=%d, geneLength=%d, maxGen=%d",
                populationSize, geneLength, maxGenerations));

        _logger.info("Integer chromosome permutations:");
        ChromosomeFactory<Integer, IntegerChromosome> intFactory = new IntegerChromosomeFactory();
        List<IntegerChromosome> intPop = new ArrayList<>();
        Random rnd = new Random(12345);
        int intMin = 0, intMax = 5; // small range for easy validation
        for (int i = 0; i < populationSize; i++) {
            Integer[] genes = new Integer[geneLength];
            for (int g = 0; g < geneLength; g++) {
                genes[g] = rnd.nextInt(intMax - intMin + 1) + intMin;
            }
            intPop.add(intFactory.create(genes));
        }

        // try SinglePoint crossover + IntegerMutation + FullReplacement
        GeneticAlgorithm<IntegerChromosome> intGa1 = GeneticAlgorithm.<IntegerChromosome>builder()
                .withPopulationSize(populationSize)
                .withPopulation(new ArrayList<>(intPop))
                .withChromosomeFactory(intFactory)
                .withSelectionStrategy(new RandomSelection<>())
                .withCrossoverStrategy(new SinglePointCrossover<>(intFactory))
                .withMutationStrategy(new IntegerMutation(0.2))
                .withReplacementStrategy(new FullGenerationReplacement<>())
                .withMaxGenerations(maxGenerations)
                .build();

        _logger.info("Running Integer GA permutation 1 (SinglePoint + IntegerMutation + FullGeneration)");
        intGa1.run();

        // try TwoPoint crossover + IntegerMutation + Elitism
        GeneticAlgorithm<IntegerChromosome> intGa2 = GeneticAlgorithm.<IntegerChromosome>builder()
                .withPopulationSize(populationSize)
                .withPopulation(new ArrayList<>(intPop))
                .withChromosomeFactory(intFactory)
                .withSelectionStrategy(new RandomSelection<>())
                .withCrossoverStrategy(new TwoPointCrossover<>(0.9, intFactory))
                .withMutationStrategy(new IntegerMutation(0.3))
                .withReplacementStrategy(new ElitismReplacement<>())
                .withMaxGenerations(maxGenerations)
                .build();

        _logger.info("Running Integer GA permutation 2 (TwoPoint + IntegerMutation + Elitism)");
        intGa2.run();
    }

    public static void runIntegrationBinaryPermutation() {
        _logger.info("--- Integration: Binary Chromosome Permutation ---");

        int populationSize = 6;
        int geneLength = 5;
        int maxGenerations = 10;

        _logger.info(String.format("Config: popSize=%d, geneLength=%d, maxGen=%d",
                populationSize, geneLength, maxGenerations));

        _logger.info("Binary chromosome permutations:");
        ChromosomeFactory<Integer, BinaryChromosome> binFactory = new BinaryChromosomeFactory();
        List<BinaryChromosome> binPop = new ArrayList<>();
        for (int i = 0; i < populationSize; i++) {
            binPop.add(BinaryChromosome.random(geneLength));
        }

        GeneticAlgorithm<BinaryChromosome> binGa = GeneticAlgorithm.<BinaryChromosome>builder()
                .withPopulationSize(populationSize)
                .withPopulation(new ArrayList<>(binPop))
                .withChromosomeFactory(binFactory)
                .withSelectionStrategy(new RandomSelection<>())
                .withCrossoverStrategy(new UniformCrossover<>(binFactory))
                .withMutationStrategy(new BinaryMutation(0.25))
                .withReplacementStrategy(new SteadyStateReplacement<>())
                .withMaxGenerations(maxGenerations)
                .build();

        _logger.info("Running Binary GA permutation (UniformCrossover + BinaryMutation + SteadyState)");
        binGa.run();
    }

    public static void runIntegrationFloatingPermutation() {
        _logger.info("--- Integration: FloatingPoint Chromosome Permutation ---");

        int populationSize = 6;
        int geneLength = 5;
        int maxGenerations = 10;

        _logger.info(String.format("Config: popSize=%d, geneLength=%d, maxGen=%d",
                populationSize, geneLength, maxGenerations));

        _logger.info("Floating point chromosome permutations:");
        double lower = -2.0, upper = 2.0;
        ChromosomeFactory<Double, FloatingPointChromosome> floatFactory = new FloatingPointChromosomeFactory(lower,
                upper);
        List<FloatingPointChromosome> floatPop = PopulationInitializer.randomFloatingPopulation(populationSize,
                geneLength, lower, upper);

        GeneticAlgorithm<FloatingPointChromosome> floatGa = GeneticAlgorithm.<FloatingPointChromosome>builder()
                .withPopulationSize(populationSize)
                .withPopulation(new ArrayList<>(floatPop))
                .withChromosomeFactory(floatFactory)
                .withSelectionStrategy(new RankSelection<>())
                .withCrossoverStrategy(new TwoPointCrossover<>(floatFactory))
                .withMutationStrategy(new NonUniformMutation(0.2, maxGenerations, 2.0))
                .withReplacementStrategy(new ElitismReplacement<>())
                .withMaxGenerations(maxGenerations)
                .build();

        _logger.info(
                "Running FloatingPoint GA permutation (RankSelection + TwoPointCrossover + NonUniformMutation + Elitism)");
        floatGa.run();
    }
}
