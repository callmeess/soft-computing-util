package com.example.softcomputing.genetic.chromosome.Factories;
import com.example.softcomputing.genetic.chromosome.IntegerChromosome;

public class IntegerChromosomeFactory implements ChromosomeFactory<Integer, IntegerChromosome> {

    @Override
    public IntegerChromosome create(Integer[] genes) {
        return new IntegerChromosome(genes);
    }
}