package com.example.softcomputing.utils;

import com.example.softcomputing.genetic.chromosome.Chromosome;

public interface FitnessFunction<C extends Chromosome<?>> {
    double evaluate(C chromosome);
}
