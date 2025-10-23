package com.example.softcomputing.tests;

import java.util.List;

import com.example.softcomputing.tests.fitness.BinaryTargetFitness;
import com.example.softcomputing.genetic.chromosome.BinaryChromosome;
import com.example.softcomputing.genetic.chromosome.Factories.BinaryChromosomeFactory;
import com.example.softcomputing.genetic.core.GeneticAlgorithm;
import com.example.softcomputing.genetic.operators.crossover.SinglePointCrossover;
import com.example.softcomputing.genetic.operators.crossover.TwoPointCrossover;
import com.example.softcomputing.genetic.operators.mutation.BinaryMutation;
import com.example.softcomputing.genetic.operators.replacement.ElitismReplacement;
import com.example.softcomputing.genetic.operators.selection.RouletteWheelSelection;
import com.example.softcomputing.genetic.operators.selection.TournametSelection;

import com.example.softcomputing.genetic.utils.PopulationInitializer;

public class BinaryTargetTest {

    public static void runTest() {
        int[] target = { 1, 1, 1, 1, 1, 1, 1, 1 };
        BinaryTargetFitness fitness = new BinaryTargetFitness(target);

        List<BinaryChromosome> population = PopulationInitializer.randomBinaryPopulation(50, 8);

        for (BinaryChromosome c : population) {
            c.setFitness(fitness.evaluate(c));
        }

        BinaryChromosomeFactory factory = new BinaryChromosomeFactory();

        GeneticAlgorithm<BinaryChromosome> ga = GeneticAlgorithm.<BinaryChromosome>builder()
                .withPopulationSize(50)
                .withPopulation(population)
                .withChromosomeFactory(factory)
                .withSelectionStrategy(new TournametSelection<>(5))
                .withCrossoverStrategy(new TwoPointCrossover<>(0.8, factory))
                .withMutationStrategy(new BinaryMutation(0.05))
                .withReplacementStrategy(new ElitismReplacement<>(5))
                .withMaxGenerations(100)
                .build();

        ga.run();
    }
}