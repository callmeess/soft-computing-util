package com.example.softcomputing.genetic.chromosome.Factories;

import com.example.softcomputing.genetic.chromosome.Chromosome;

public interface ChromosomeFactory<G, C extends Chromosome<G>> {
    C create(G[] genes);
}
