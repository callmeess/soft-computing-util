package com.example.softcomputing.genetic.core;

import java.util.ArrayList;
import java.util.List;

import com.example.softcomputing.genetic.operators.crossover.CrossoverStrategy;
import com.example.softcomputing.genetic.operators.mutation.MutationStrategy;
import com.example.softcomputing.genetic.operators.replacement.Replacement;
import com.example.softcomputing.genetic.operators.selection.SelectionStrategy;

public class GeneticAlgorithm {
    private final SelectionStrategy selection;
    private final CrossoverStrategy crossover;
    private final MutationStrategy mutation;
    private final Replacement termination;
    private final int populationSize;

    private GeneticAlgorithm(Builder builder) {
        this.selection = builder.selection;
        this.crossover = builder.crossover;
        this.mutation = builder.mutation;
        this.termination = builder.replacement;
        this.populationSize = builder.populationSize;
    }

    public void run() {
        System.out.println("Running GeneticAlgorithm with population size: " + populationSize);

        List<Integer> population = new ArrayList<>();
        for (int i = 0; i < populationSize; i++) population.add(i);

        int generation = 0;
        while (true) {
            generation++;
            // Just a placeholder for selection/crossover/mutation steps
            Object best = population.get(0);
            if (termination.shouldTerminate(generation, best)) {
                System.out.println("Termination triggered at generation " + generation);
                break;
            }
            if (generation > 10000) { // safety
                System.out.println("Reached safety limit");
                break;
            }
        }
    }

    public static class Builder {
        private SelectionStrategy selection;
        private CrossoverStrategy crossover;
        private MutationStrategy mutation;
        private Replacement replacement;
        private int populationSize = 100;

        public Builder withSelectionStrategy(SelectionStrategy s) {
            this.selection = s;
            return this;
        }

        public Builder withCrossoverStrategy(CrossoverStrategy c) {
            this.crossover = c;
            return this;
        }

        public Builder withMutationStrategy(MutationStrategy m) {
            this.mutation = m;
            return this;
        }

        public Builder withTerminationStrategy(Replacement r) {
            this.replacement = r;
            return this;
        }

        public Builder withPopulationSize(int size) {
            this.populationSize = size;
            return this;
        }

        public GeneticAlgorithm build() {
            return new GeneticAlgorithm(this);
        }
    }
}
