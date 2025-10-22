

package com.example.softcomputing.genetic.chromosome;

import java.util.Arrays;
import java.util.Random;

public class BinaryChromosome implements Chromosome<Integer> {
    private final int[] genes;
    private static final Random rand = new Random();

    public BinaryChromosome(int[] genes) {
        this.genes = Arrays.copyOf(genes, genes.length);
    }

    public BinaryChromosome(int length) {
        this.genes = new int[length];
    }

    public static BinaryChromosome random(int length) {
        int[] g = new int[length];
        for (int i = 0; i < length; i++) {
            g[i] = rand.nextBoolean() ? 1 : 0;
        }
        return new BinaryChromosome(g);
    }

    @Override
    public Integer[] toArray() {
        Integer[] arr = new Integer[genes.length];
        for (int i = 0; i < genes.length; i++) arr[i] = genes[i];
        return arr;
    }

    @Override
    public int length() {
        return genes.length;
    }

    @Override
    public Integer getGene(int index) {
        return genes[index];
    }

    @Override
    public void setGene(int index, Integer value) {
        genes[index] = value == 0 ? 0 : 1;
    }

    @Override
    public double evaluate() {
        // Fitness: decimal value of the bit string
        return toInt();
    }

    public int toInt() {
        int val = 0;
        for (int i = 0; i < genes.length; i++) {
            val = (val << 1) | genes[i];
        }
        return val;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int g : genes) sb.append(g);
        return "BinaryChromosome" + sb.toString();
    }
}
