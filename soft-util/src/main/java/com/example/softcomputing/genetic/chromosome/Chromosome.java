package com.example.softcomputing.genetic.chromosome;

public interface Chromosome<G> {
    G[] toArray();     // array of genes
    int length();      // number of genes
    G getGene(int index); // get gene at index
    void setGene(int index, G value); // set gene at index
}
