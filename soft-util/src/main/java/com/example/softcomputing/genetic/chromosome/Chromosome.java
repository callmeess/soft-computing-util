package com.example.softcomputing.genetic.chromosome;

import java.util.function.ToDoubleFunction;

public interface  Chromosome<G> {
    G[] toArray();     // genes
    int length();       
    G getGene(int index); 
    void setGene(int index, G value); 
    double evaluate(); 
    double evaluate(ToDoubleFunction<Chromosome<G>> evaluator);
}
