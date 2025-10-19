package com.example.softcomputing.genetic.chromosome;

import java.util.Arrays;
import java.util.Random;
import com.example.softcomputing.genetic.chromosome.Factories.ChromosomeFactory;

public class FloatingPointChromosome implements Chromosome<Double> {
    private final Double[] genes;
    private final double lowerBound;
    private final double upperBound;

    public FloatingPointChromosome(Double[] genes, double lowerBound, double upperBound) {
        this.genes = genes;
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    public static FloatingPointChromosome randomInit(int size, double lowerBound, double upperBound) {
        Random rand = new Random();
        Double[] genes = new Double[size];
        for (int i = 0; i < size; i++) {
            genes[i] = lowerBound + rand.nextDouble() * (upperBound - lowerBound);
        }
        return new FloatingPointChromosome(genes, lowerBound, upperBound);
    }

    @Override
    public Double[] toArray() {
        return genes;
    }

    @Override
    public int length() {
        return genes.length;
    }

    public Double getGene(int index) {
        return genes[index];
    }

    public void setGene(int index, Double value) {
        genes[index] = value;
    }

    public double getLowerBound() {
        return lowerBound;
    }

    public double getUpperBound() {
        return upperBound;
    }

    public static ChromosomeFactory<Double, FloatingPointChromosome> factory(double lower, double upper) {
        return genes -> new FloatingPointChromosome(genes, lower, upper);
    }

    @Override
    public String toString() {
        return "FloatingPointChromosome" + Arrays.toString(genes);
    }

    @Override
    public double evaluate() {
        // Fitness: sum of all gene values (maximize sum)
        double fitness = 0.0;
        for (Double gene : genes) {
            fitness += gene;
        }
        return fitness;
    }
}
