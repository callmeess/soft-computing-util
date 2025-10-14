package com.example.softcomputing.genetic;

import java.util.ArrayList;
import java.util.List;

import com.example.softcomputing.genetic.chromosome.IntegerChromosome;
import com.example.softcomputing.genetic.chromosome.Factories.ChromosomeFactory;
import com.example.softcomputing.genetic.chromosome.Factories.IntegerChromosomeFactory;
import com.example.softcomputing.genetic.core.GeneticAlgorithm;
import com.example.softcomputing.genetic.operators.crossover.SinglePointCrossover;
import com.example.softcomputing.genetic.operators.mutation.IntegerMutation;
import com.example.softcomputing.genetic.operators.replacement.FullGenerationReplacement;
import com.example.softcomputing.genetic.operators.selection.RandomSelection;

public class SoftUtilApplication {

    public static void main(String[] args) {
        System.out.println("Starting soft-util console application...");

		double mutationRate = 0.05;
        int populationSize = 50;
        int geneLength = 10;
        ChromosomeFactory<Integer,IntegerChromosome> factory = new IntegerChromosomeFactory();

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
			.build();

        ga.run();
    }
}