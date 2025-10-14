package com.example.softcomputing.genetic.chromosome;

public class IntegerChromosome implements  Chromosome<Integer> {

    private Integer[] _genes;

    public IntegerChromosome(Integer[] genes) {
        this._genes = genes;
    }

    @Override
    public Integer[] toArray() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'toArray'");
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

    
}
