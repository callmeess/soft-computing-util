package com.example.softcomputing.utils;

import com.example.softcomputing.genetic.chromosome.Chromosome;

public class FitnessFunction {
    // public static boolean evaluate(Chromosome<?> parent1 , Chromosome<?> parent2) {

    //     return parent1.evaluate() > parent2.evaluate(); 
    // }
    

    public static double getDecimalValForBinChrom(Chromosome<?> chromosome) {
        double fitness = 0.0;
        int len = chromosome.length();
        for (int i = 0; i < len; i++) {
            int gene = (Integer) chromosome.getGene(i);
            fitness += gene * Math.pow(2, len - i - 1);
        }
        return fitness;
    }

    public static double getProductForFloatChrom(Chromosome<?> chromosome) {
        double fitness = 1.0;
        int len = chromosome.length();
        for (int i = 0; i < len; i++) {
            double gene = (Double) chromosome.getGene(i);
            fitness *= gene;
        }
        return fitness;
    }

    public static double getSumForIntChrom(Chromosome<?> chromosome) {
        double fitness = 0.0;
        int len = chromosome.length();
        for (int i = 0; i < len; i++) {
            int gene = (Integer) chromosome.getGene(i);
            fitness += gene;
        }
        return fitness;
    }

}
