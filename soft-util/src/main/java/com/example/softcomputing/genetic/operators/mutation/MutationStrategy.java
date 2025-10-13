package com.example.softcomputing.genetic.operators.mutation;

import com.example.softcomputing.genetic.chromosome.Chromosome;

public interface MutationStrategy<G, T extends Chromosome<G>> {
    T mutate(T individual);
}
