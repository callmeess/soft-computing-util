package com.example.softcomputing.genetic.core;

import java.util.List;
import java.util.Objects;

import com.example.softcomputing.genetic.chromosome.Chromosome;
import com.example.softcomputing.genetic.chromosome.Factories.ChromosomeFactory;
import com.example.softcomputing.genetic.operators.crossover.CrossoverStrategy;
import com.example.softcomputing.genetic.operators.mutation.MutationStrategy;
import com.example.softcomputing.genetic.operators.replacement.Replacement;
import com.example.softcomputing.genetic.operators.selection.SelectionStrategy;

public class GeneticAlgorithmBuilder<C extends Chromosome<?>> {
    SelectionStrategy<C> selection;
    CrossoverStrategy<C> crossover;
    MutationStrategy<C> mutation;
    Replacement<C> replacement;
    int populationSize = 100;
    List<C> population = null;
    ChromosomeFactory<?, C> chromosomeFactory;

    public GeneticAlgorithmBuilder<C> withChromosomeFactory(ChromosomeFactory<?, C> factory) {
        this.chromosomeFactory = factory;
        return this;
    }

    public GeneticAlgorithmBuilder<C> withPopulation(List<C> population) {
        this.population = population;
        return this;
    }

    public GeneticAlgorithmBuilder<C> withSelectionStrategy(SelectionStrategy<C> s) {
        this.selection = s;
        return this;
    }

    public GeneticAlgorithmBuilder<C> withCrossoverStrategy(CrossoverStrategy<C> c) {
        this.crossover = c;
        return this;
    }

    public GeneticAlgorithmBuilder<C> withMutationStrategy(MutationStrategy<C> m) {
        this.mutation = m;
        return this;
    }

    public GeneticAlgorithmBuilder<C> withReplacementStrategy(Replacement<C> r) {
        this.replacement = r;
        return this;
    }

    public GeneticAlgorithmBuilder<C> withPopulationSize(int size) {
        this.populationSize = size;
        return this;
    }
    

    public GeneticAlgorithm<C> build() {
        Objects.requireNonNull(selection, "selection strategy is required");
        Objects.requireNonNull(crossover, "crossover strategy is required");
        Objects.requireNonNull(mutation, "mutation strategy is required");
        Objects.requireNonNull(replacement, "replacement strategy is required");
        Objects.requireNonNull(chromosomeFactory, "chromosome factory is required");
        if (populationSize <= 0) throw new IllegalArgumentException("populationSize must be > 0");


        return new GeneticAlgorithm<C>(this);
    }
}