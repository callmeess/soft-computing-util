package com.example.softcomputing.genetic.chromosome.Factories;

import com.example.softcomputing.genetic.chromosome.BinaryChromosome;

public class BinaryChromosomeFactory implements ChromosomeFactory<Integer, BinaryChromosome> {

    @Override
    public BinaryChromosome create(Integer[] genes) {
        int[] bits = new int[genes.length];
        for (int i = 0; i < genes.length; i++) {
            bits[i] = genes[i];
        }
        return new BinaryChromosome(bits);
    }
}
