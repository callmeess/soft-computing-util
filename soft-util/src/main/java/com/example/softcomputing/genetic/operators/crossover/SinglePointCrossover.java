package com.example.softcomputing.genetic.operators.crossover;

import java.util.ArrayList;
import java.util.List;

public class SinglePointCrossover<T> implements CrossoverStrategy<T> {
    @Override
    public List<T> crossover(T parent1, T parent2) {
        List<T> children = new ArrayList<>();
        // this is a stub: return parents as "children"
        children.add(parent1);
        children.add(parent2);
        return children;
    }
}
