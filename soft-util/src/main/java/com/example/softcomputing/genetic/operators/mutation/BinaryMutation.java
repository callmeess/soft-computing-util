package com.example.softcomputing.genetic.operators.mutation;

import com.example.softcomputing.genetic.chromosome.BinaryChromosome;
import java.util.Random;

public class BinaryMutation implements MutationStrategy<BinaryChromosome> {
    private final double mutationRate;
    private final Random random = new Random();

    public BinaryMutation(double mutationRate) {
        this.mutationRate = mutationRate;
    }

    @Override
    public BinaryChromosome mutate(BinaryChromosome individual) {
        for (int i = 0; i < individual.length(); i++) {
            if (random.nextDouble() < mutationRate) {
                int current = individual.getGene(i);
                individual.setGene(i, 1 - current);
            }
        }
        return individual;
    }
}
