package com.example.softcomputing.genetic.operators.crossover;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.example.softcomputing.genetic.chromosome.Chromosome;
import com.example.softcomputing.genetic.chromosome.Factories.ChromosomeFactory;

public class SinglePointCrossover<G, C extends Chromosome<G>> implements CrossoverStrategy<C> {
    private final ChromosomeFactory<G, C> factory;
    private final Random random = new Random();

    public SinglePointCrossover(ChromosomeFactory<G, C> factory) {
        this.factory = factory;
    }

    @Override
    public List<C> crossover(C parent1, C parent2) {
        List<C> children = new ArrayList<>();
        
        int length = parent1.length();
        if (length != parent2.length()) {
            throw new IllegalArgumentException("Parents must have the same length");
        }

        // Pick a random crossover point (1 to length-1)
        int crossoverPoint = 1 + random.nextInt(length - 1);

        // Get parent genes
        G[] p1Genes = parent1.toArray();
        G[] p2Genes = parent2.toArray();

        // Create child genes arrays
        G[] child1Genes = p1Genes.clone();
        G[] child2Genes = p2Genes.clone();

        // Swap genes after crossover point
        for (int i = crossoverPoint; i < length; i++) {
            child1Genes[i] = p2Genes[i];
            child2Genes[i] = p1Genes[i];
        }

        // Create children using factory
        children.add(factory.create(child1Genes));
        children.add(factory.create(child2Genes));

        return children;
    }
}
