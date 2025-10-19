package com.example.softcomputing.genetic.chromosome.Factories;

import com.example.softcomputing.genetic.chromosome.FloatingPointChromosome;

public class FloatingPointChromosomeFactory implements ChromosomeFactory<Double, FloatingPointChromosome> {
    private final double lowerBound;
    private final double upperBound;

    public FloatingPointChromosomeFactory(double lowerBound, double upperBound) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    @Override
    public FloatingPointChromosome create(Double[] genes) {
        return new FloatingPointChromosome(genes, lowerBound, upperBound);
    }
}
