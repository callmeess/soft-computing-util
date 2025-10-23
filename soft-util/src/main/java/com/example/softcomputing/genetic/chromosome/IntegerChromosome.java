package com.example.softcomputing.genetic.chromosome;

import java.util.Arrays;
import java.util.function.ToDoubleFunction;

public class IntegerChromosome implements  Chromosome<Integer> {

    private Integer[] _genes;

    public IntegerChromosome(Integer[] genes) {
        this._genes = genes;
    }

    @Override
    public Integer[] toArray() {
        return java.util.Arrays.copyOf(_genes, _genes.length);
    }

    @Override
    public int length() {
        return _genes.length;
    }

    @Override
    public Integer getGene(int index) {
        return _genes[index];
    }

    @Override
    public void setGene(int index, Integer value) {
        _genes[index] = value;
    }

    @Override
    public double evaluate() {
        double fitness = 0.0;
        for (Integer gene : _genes) {
            fitness += gene;
        }
        System.err.println("Fitness as max sum : " + fitness);
        return fitness;
    }

    @Override
    public double evaluate(ToDoubleFunction<Chromosome<Integer>> evaluator) {
        if (evaluator != null) {
            return evaluator.applyAsDouble(this);
        }
        return evaluate();
    }

    public String toString() {
        return "IntegerChromosome" + Arrays.toString(_genes);
    }
}
