package com.example.softcomputing.genetic.chromosome;

public interface ChromosomeFactory<G, C extends Chromosome<G>> {
    C create(G[] genes);
}
