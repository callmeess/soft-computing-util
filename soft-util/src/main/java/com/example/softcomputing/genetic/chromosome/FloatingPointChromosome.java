package com.example.softcomputing.genetic.chromosome;

import java.util.Arrays;
import java.util.function.ToDoubleFunction;

public class FloatingPointChromosome implements Chromosome<Double> {

    private final Double[] genes;
    private final double lowerBound;
    private final double upperBound;
    private double fitness = 0.0;

    public FloatingPointChromosome(Double[] genes, double lowerBound, double upperBound) {
        this.genes = genes;
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
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

    @Override
    public String toString() {
        return "FloatingPointChromosome" + Arrays.toString(genes);
    }

    @Override
    public double evaluate() {
        return 1.0;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public double getFitness() {
        return fitness;
    }

    public double evaluate(ToDoubleFunction<Chromosome<Double>> evaluator) {
        if (evaluator != null) {
            return evaluator.applyAsDouble(this);
        }
        return evaluate();
    }
}
