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
        if (individual.length() == 0) return individual;
        // With probability mutationRate, flip one random bit
        if (random.nextDouble() < mutationRate) {
            int idx = random.nextInt(individual.length());
            int current = individual.getGene(idx);
            individual.setGene(idx, 1 - current);
        }
        return individual;
    }
}
