package com.example.softcomputing.genetic.operators.replacement;

import java.util.List;

import com.example.softcomputing.genetic.chromosome.Chromosome;

public class FullGenerationReplacement  implements Replacement<Chromosome<?>> {

    @Override
    public List<Chromosome<?>> replacePopulation(List<Chromosome<?>> currentPopulation, List<Chromosome<?>> newIndividuals) {
        return newIndividuals;
    }
    
}
