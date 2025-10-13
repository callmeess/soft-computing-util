package com.example.softcomputing.genetic;

import com.example.softcomputing.genetic.core.GeneticAlgorithm;
import com.example.softcomputing.genetic.operators.crossover.SinglePointCrossover;

public class SoftUtilApplication {

	public static void main(String[] args) {
		System.out.println("Starting soft-util console application...");

		// Example usage: construct a GeneticAlgorithm with minimal dummy strategies
	GeneticAlgorithm ga = new GeneticAlgorithm.Builder()
		.withPopulationSize(50)
		// .withSelectionStrategy(new RandomSelection())
		.withCrossoverStrategy(new SinglePointCrossover<Object>())
		// .withMutationStrategy(new SimpleMutation<Object>())
		// .withTerminationStrategy(new MaxGenerationsReplacement<Object>(100))
		.build();

		ga.run();

		System.out.println("Done.");
	}

}
