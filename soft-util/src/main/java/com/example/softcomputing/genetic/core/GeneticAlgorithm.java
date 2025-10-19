package com.example.softcomputing.genetic.core;

import java.util.ArrayList;
import java.util.List;

import com.example.softcomputing.genetic.chromosome.Chromosome;
import com.example.softcomputing.genetic.operators.crossover.CrossoverStrategy;
import com.example.softcomputing.genetic.operators.mutation.MutationStrategy;
import com.example.softcomputing.genetic.operators.replacement.Replacement;
import com.example.softcomputing.genetic.operators.selection.SelectionStrategy;

public class GeneticAlgorithm<C extends Chromosome<?>> {

    private SelectionStrategy<C> _selection;
    private CrossoverStrategy<C> _crossover;
    private MutationStrategy<C> _mutation;
    private Replacement<C> _replacement;
    private int _populationSize;
    private List<C> _population;
    private long _MaxGeneration = 100;

    public GeneticAlgorithm(GeneticAlgorithmBuilder<C> builder) {
        this._selection = builder.selection;
        this._crossover = builder.crossover;
        this._mutation = builder.mutation;
        this._replacement = builder.replacement;
        this._populationSize = builder.populationSize;
        this._population = builder.population;
        this._MaxGeneration = builder.maxGenerations;
    }

    public void run() {

        if (_population == null || _population.isEmpty()) {
            System.out.println("No initial population provided. Aborting run.");
            return;
        }

        if (_selection == null || _crossover == null || _mutation == null || _replacement == null) {
            System.out.println("One or more strategies are not configured. Aborting run.");
            return;
        }

        // Track overall best solution found across all generations
        C overallBest = null;
        double overallBestFitness = Double.NEGATIVE_INFINITY;

        for (int gen = 1; gen <= _MaxGeneration; gen++) {
            List<C> offspring = new ArrayList<>(_populationSize);

            while (offspring.size() < _populationSize) {
                C parent1 = _selection.selectIndividual(_population);
                C parent2 = _selection.selectIndividual(_population);

                List<C> children = _crossover.crossover(parent1, parent2);

                for (C child : children) {
                    C mutated = _mutation.mutate(child);
                    offspring.add(mutated);
                    if (offspring.size() >= _populationSize) break;
                }
            }

            //  replacement
            _population = _replacement.replacePopulation(new ArrayList<>(_population), offspring);

            // evaluate and report best individual of this generation
            C best = null;
            double bestFitness = Double.NEGATIVE_INFINITY;
            for (C ind : _population) {
                double fitness = ind.evaluate();
                if (best == null || fitness > bestFitness) {
                    best = ind;
                    bestFitness = fitness;
                }
            }

            // Update overall best if current generation is better
            if (bestFitness > overallBestFitness) {
                overallBest = best;
                overallBestFitness = bestFitness;
            }

            System.out.println("Generation " + gen + " bestFitness=" + bestFitness + " best=" + best);

            if (Double.isFinite(bestFitness) && bestFitness >= Double.POSITIVE_INFINITY - 1) break;
        }

        // Print overall best solution found
        System.out.println("\n====================================");
        System.out.println("BEST SOLUTION FOUND OVERALL:");
        System.out.println("Best Fitness: " + overallBestFitness);
        System.out.println("Best Chromosome: " + overallBest);
        System.out.println("====================================\n");
    }

    public static <C extends Chromosome<?>> GeneticAlgorithmBuilder<C> builder() {
        return new GeneticAlgorithmBuilder<C>();
    }
}