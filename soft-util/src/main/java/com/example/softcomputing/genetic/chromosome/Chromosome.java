package com.example.softcomputing.genetic.chromosome;

public interface  Chromosome<G> {
    G[] toArray();     // genes
    int length();       
    G getGene(int index); 
    void setGene(int index, G value); 
    double evaluate(); 
}
