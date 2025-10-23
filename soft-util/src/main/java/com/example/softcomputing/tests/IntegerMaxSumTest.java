package com.example.softcomputing.tests;

import java.util.List;
import com.example.softcomputing.tests.fitness.MaxSumFitness;
import com.example.softcomputing.genetic.chromosome.IntegerChromosome;
import com.example.softcomputing.genetic.chromosome.Factories.IntegerChromosomeFactory;
import com.example.softcomputing.genetic.core.GeneticAlgorithm;
import com.example.softcomputing.genetic.operators.crossover.TwoPointCrossover;
import com.example.softcomputing.genetic.operators.mutation.IntegerMutation;
import com.example.softcomputing.genetic.operators.replacement.ElitismReplacement;
import com.example.softcomputing.genetic.operators.selection.RandomSelection;
import com.example.softcomputing.genetic.utils.PopulationInitializer;

public class IntegerMaxSumTest {

    public static void runTest() {
        MaxSumFitness fitness = new MaxSumFitness();

        List<IntegerChromosome> population = PopulationInitializer.randomIntegerPopulation(50, 10, 0, 99);

        for (IntegerChromosome c : population) {
            c.setFitness(fitness.evaluate(c));
        }

        IntegerChromosomeFactory factory = new IntegerChromosomeFactory();

        GeneticAlgorithm<IntegerChromosome> ga = GeneticAlgorithm.<IntegerChromosome>builder()
                .withPopulationSize(50)
                .withPopulation(population)
                .withChromosomeFactory(factory)
                .withSelectionStrategy(new RandomSelection<>())
                .withCrossoverStrategy(new TwoPointCrossover<>(0.7, factory))
                .withMutationStrategy(new IntegerMutation(0.05))
                .withReplacementStrategy(new ElitismReplacement<>(5))
                .withMaxGenerations(100)
                .build();

        ga.run();
    }
}