package com.example.softcomputing.tests;

import java.util.List;
import com.example.softcomputing.tests.fitness.MaxProductFitness;
import com.example.softcomputing.genetic.chromosome.FloatingPointChromosome;
import com.example.softcomputing.genetic.chromosome.Factories.FloatingPointChromosomeFactory;
import com.example.softcomputing.genetic.core.GeneticAlgorithm;
import com.example.softcomputing.genetic.operators.crossover.SinglePointCrossover;
import com.example.softcomputing.genetic.operators.mutation.UniformMutation;
import com.example.softcomputing.genetic.operators.replacement.ElitismReplacement;
import com.example.softcomputing.genetic.operators.selection.RankSelection;
import com.example.softcomputing.genetic.utils.PopulationInitializer;

public class FloatingMaxProductTest {

    public static void runTest() {
        MaxProductFitness fitness = new MaxProductFitness();

        List<FloatingPointChromosome> population = PopulationInitializer.randomFloatingPopulation(50, 5, 0.0, 10.0);

        for (FloatingPointChromosome c : population) {
            c.setFitness(fitness.evaluate(c));
        }

        FloatingPointChromosomeFactory factory = new FloatingPointChromosomeFactory(0.0, 10.0);

        GeneticAlgorithm<FloatingPointChromosome> ga = GeneticAlgorithm.<FloatingPointChromosome>builder()
                .withPopulationSize(50)
                .withPopulation(population)
                .withChromosomeFactory(factory)
                .withSelectionStrategy(new RankSelection<>())
                .withCrossoverStrategy(new SinglePointCrossover<>(0.8, factory))
                .withMutationStrategy(new UniformMutation(0.05))
                .withReplacementStrategy(new ElitismReplacement<>(5))
                .withMaxGenerations(100)
                .build();

        ga.run();
    }
}