package com.example.softcomputing.genetic.operators.selection;

import java.util.List;
import java.util.Random;
import com.example.softcomputing.genetic.chromosome.Chromosome;

public class RandomSelection implements SelectionStrategy<Chromosome<?>> {
    private final Random rnd = new Random();

    @Override
    public Chromosome<?> selectIndividual(List<Chromosome<?>> population) {
        if (population == null || population.isEmpty()) return null;
        return population.get(rnd.nextInt(population.size()));
    }
}
