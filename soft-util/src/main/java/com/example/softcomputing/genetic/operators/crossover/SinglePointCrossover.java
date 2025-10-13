package com.example.softcomputing.genetic.operators.crossover;

import java.util.ArrayList;
import java.util.List;

public class SinglePointCrossover<Chromosome> implements CrossoverStrategy<Chromosome> {
    @Override
    public List<Chromosome> crossover(Chromosome parent1, Chromosome parent2) {
        List<Chromosome> children = new ArrayList<>();
        // this is a stub: return parents as "children"
        children.add(parent1);
        children.add(parent2);
        return children;
    }
}
