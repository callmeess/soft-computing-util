package com.example.soft_util.impl;

import java.util.List;
import java.util.Random;
import com.example.soft_util.interfaces.SelectionStrategy;

public class RandomSelection implements SelectionStrategy {
    private final Random rnd = new Random();

    @Override
    public <T> T select(List<T> population) {
        if (population == null || population.isEmpty()) return null;
        return population.get(rnd.nextInt(population.size()));
    }
}
