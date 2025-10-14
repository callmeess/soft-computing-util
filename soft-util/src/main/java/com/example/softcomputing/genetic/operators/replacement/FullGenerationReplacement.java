package com.example.softcomputing.genetic.operators.replacement;

import java.util.List;

import com.example.softcomputing.genetic.chromosome.Chromosome;

public class FullGenerationReplacement<C extends Chromosome<?>> implements Replacement<C> {

    @Override
    public List<C> replacePopulation(List<C> currentPopulation, List<C> newIndividuals) {
        return newIndividuals;
    }
    
}
