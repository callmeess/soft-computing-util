package com.example.softcomputing.genetic.chromosome.Factories;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.example.softcomputing.genetic.chromosome.FloatingPointChromosome;

public class FloatingPointChromosomeFactory implements  ChromosomeFactory<Double, FloatingPointChromosome> {

    private double lowerBound;
    private double upperBound;

    public FloatingPointChromosomeFactory(double lowerBound, double upperBound) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    @Override
    public FloatingPointChromosome create(Double[] genes) {
        return new FloatingPointChromosome(genes, lowerBound, upperBound);
    }

    @Override
    public List<FloatingPointChromosome> createPopulation(int populationSize, int geneLength) {

        List<FloatingPointChromosome> initialPopulation = new ArrayList<>(populationSize);

        Random rand = new Random();
        Double[] genes = new Double[populationSize];
        for (int i = 0; i < populationSize; i++) {
            genes[i] = lowerBound + rand.nextDouble() * (upperBound - lowerBound);
            initialPopulation.add(this.create(genes));
        }
        return initialPopulation;
    }
    
}
