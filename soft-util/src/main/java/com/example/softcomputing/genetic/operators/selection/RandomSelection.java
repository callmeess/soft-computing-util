package com.example.softcomputing.genetic.operators.selection;

import java.util.List;
import java.util.Random;

public class RandomSelection implements SelectionStrategy {
    private final Random rnd = new Random();

    @Override
    public <T> T select(List<T> population) {
        if (population == null || population.isEmpty()) return null;
        return population.get(rnd.nextInt(population.size()));
    }
}
