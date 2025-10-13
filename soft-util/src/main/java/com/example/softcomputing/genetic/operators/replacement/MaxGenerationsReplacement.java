package com.example.softcomputing.genetic.operators.replacement;


public class MaxGenerationsReplacement<T> implements Replacement<T> {
    private final int maxGenerations;

    public MaxGenerationsReplacement(int maxGenerations) {
        this.maxGenerations = maxGenerations;
    }

    @Override
    public boolean shouldTerminate(int generation, T best) {
        return generation >= maxGenerations;
    }
}
