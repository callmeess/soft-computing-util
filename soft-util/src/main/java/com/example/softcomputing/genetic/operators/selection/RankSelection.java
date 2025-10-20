package com.example.softcomputing.genetic.operators.selection;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import com.example.softcomputing.genetic.chromosome.Chromosome;

/**
 * Rank Selection implementation where selection probability is based on relative rank
 * rather than absolute fitness values. This addresses issues of Roulette Wheel Selection
 * by preventing premature convergence and handling negative fitness values naturally.
 *
 * <p>This implementation supports two ranking modes:</p>
 * <ul>
 *   <li>LINEAR: Uses formula p(i) = (2-s)/n + (2*i*(s-1))/(n*(n-1))</li>
 *   <li>EXPONENTIAL: Uses formula p(i) = (1-e^(-i)) / C where C normalizes</li>
 * </ul>
 *
 * <p>Selection pressure (s) controls selection intensity:</p>
 * <ul>
 *   <li>When s=1.0, selection is uniform (random)</li>
 *   <li>When s=2.0, selection pressure is maximum</li>
 *   <li>Typical range: [1.1, 1.9], default: 1.5</li>
 * </ul>
 *
 * @param <C> the chromosome type
 */
public class RankSelection<C extends Chromosome<?>> implements SelectionStrategy<C> {

    /**
     * Ranking mode for probability calculation
     */
    public enum RankingMode {
        LINEAR,
        EXPONENTIAL
    }

    private final double selectionPressure;
    private final RankingMode rankingMode;
    private final Random random;
    private final boolean maximization;

    // Cache for performance optimization
    private List<C> cachedPopulation;
    private List<RankedIndividual<C>> rankedPopulation;
    private double[] cumulativeProbabilities;

    /**
     * Creates a RankSelection with default parameters.
     * Uses linear ranking mode with selection pressure 1.5 and maximization.
     */
    public RankSelection() {
        this(1.5, RankingMode.LINEAR, true);
    }

    /**
     * Creates a RankSelection with specified selection pressure.
     * Uses linear ranking mode and maximization.
     *
     * @param selectionPressure the selection pressure (must be between 1.0 and 2.0)
     * @throws IllegalArgumentException if selection pressure is not in valid range
     */
    public RankSelection(double selectionPressure) {
        this(selectionPressure, RankingMode.LINEAR, true);
    }

    /**
     * Creates a RankSelection with specified selection pressure and ranking mode.
     *
     * @param selectionPressure the selection pressure (must be between 1.0 and 2.0)
     * @param rankingMode the ranking mode (LINEAR or EXPONENTIAL)
     * @param maximization true for maximization problems, false for minimization
     * @throws IllegalArgumentException if selection pressure is not in valid range
     */
    public RankSelection(double selectionPressure, RankingMode rankingMode, boolean maximization) {
        if (selectionPressure < 1.0 || selectionPressure > 2.0) {
            throw new IllegalArgumentException(
                "Selection pressure must be between 1.0 and 2.0, got: " + selectionPressure
            );
        }
        this.selectionPressure = selectionPressure;
        this.rankingMode = rankingMode;
        this.maximization = maximization;
        this.random = new Random();
    }

    /**
     * Selects an individual from the population based on rank-based probabilities.
     *
     * @param population the population to select from
     * @return the selected individual
     * @throws IllegalArgumentException if population is null or empty
     */
    @Override
    public C selectIndividual(List<C> population) {
        if (population == null || population.isEmpty()) {
            throw new IllegalArgumentException("Population must not be null or empty");
        }

        // Check if we need to rebuild the ranking (population changed)
        if (cachedPopulation != population || rankedPopulation == null) {
            buildRanking(population);
            cachedPopulation = population;
        }

        // Perform roulette wheel selection using cumulative probabilities
        double randomValue = random.nextDouble();

        for (int i = 0; i < cumulativeProbabilities.length; i++) {
            if (randomValue <= cumulativeProbabilities[i]) {
                return rankedPopulation.get(i).individual;
            }
        }

        // Fallback: return best individual (should rarely happen due to floating point)
        return rankedPopulation.get(rankedPopulation.size() - 1).individual;
    }

    /**
     * Builds the ranking and calculates selection probabilities.
     * Complexity: O(n log n) for sorting, O(n) for probability calculation
     *
     * @param population the population to rank
     */
    private void buildRanking(List<C> population) {
        int n = population.size();

        // Step 1: Evaluate fitness for all individuals and create ranked list
        rankedPopulation = new ArrayList<>(n);
        for (C individual : population) {
            double fitness = individual.evaluate();
            rankedPopulation.add(new RankedIndividual<>(individual, fitness));
        }

        // Step 2: Sort population by fitness
        // For maximization: ascending order (worst first, rank 1 = lowest fitness)
        // For minimization: descending order (worst first, rank 1 = highest fitness)
        rankedPopulation.sort(maximization
            ? Comparator.comparingDouble((RankedIndividual<C> ri) -> ri.fitness)
            : Comparator.comparingDouble((RankedIndividual<C> ri) -> ri.fitness).reversed()
        );

        // Step 3: Assign ranks and handle ties
        assignRanks();

        // Step 4: Calculate selection probabilities based on rank
        double[] probabilities = new double[n];

        if (rankingMode == RankingMode.LINEAR) {
            calculateLinearProbabilities(probabilities, n);
        } else {
            calculateExponentialProbabilities(probabilities, n);
        }

        // Step 5: Build cumulative probability array for roulette wheel selection
        cumulativeProbabilities = new double[n];
        cumulativeProbabilities[0] = probabilities[0];
        for (int i = 1; i < n; i++) {
            cumulativeProbabilities[i] = cumulativeProbabilities[i - 1] + probabilities[i];
        }

        // Normalize to ensure last cumulative probability is exactly 1.0
        if (cumulativeProbabilities[n - 1] > 0) {
            for (int i = 0; i < n; i++) {
                cumulativeProbabilities[i] /= cumulativeProbabilities[n - 1];
            }
        }
    }

    /**
     * Assigns ranks to individuals, handling ties by assigning the same rank.
     */
    private void assignRanks() {
        if (rankedPopulation.isEmpty()) return;

        int currentRank = 1;
        rankedPopulation.get(0).rank = currentRank;

        for (int i = 1; i < rankedPopulation.size(); i++) {
            // If fitness is different from previous, increment rank
            if (Math.abs(rankedPopulation.get(i).fitness - rankedPopulation.get(i - 1).fitness) > 1e-10) {
                currentRank = i + 1;
            }
            // Assign rank (same rank for ties)
            rankedPopulation.get(i).rank = currentRank;
        }
    }

    /**
     * Calculates selection probabilities using linear ranking.
     * Formula: p(i) = (2-s)/n + (2*i*(s-1))/(n*(n-1))
     * Where s is selection pressure, n is population size, i is rank
     *
     * @param probabilities array to store probabilities
     * @param n population size
     */
    private void calculateLinearProbabilities(double[] probabilities, int n) {
        double s = selectionPressure;

        for (int i = 0; i < n; i++) {
            int rank = rankedPopulation.get(i).rank;

            // Linear ranking formula
            // p(i) = (2-s)/n + (2*i*(s-1))/(n*(n-1))
            if (n == 1) {
                probabilities[i] = 1.0;
            } else {
                probabilities[i] = (2.0 - s) / n + (2.0 * rank * (s - 1.0)) / (n * (n - 1.0));
            }
        }
    }

    /**
     * Calculates selection probabilities using exponential ranking.
     * Formula: p(i) = (1 - e^(-i)) / C where C normalizes probabilities to sum to 1
     *
     * @param probabilities array to store probabilities
     * @param n population size
     */
    private void calculateExponentialProbabilities(double[] probabilities, int n) {
        double sumProbabilities = 0.0;

        // Calculate unnormalized probabilities
        for (int i = 0; i < n; i++) {
            int rank = rankedPopulation.get(i).rank;
            probabilities[i] = 1.0 - Math.exp(-rank);
            sumProbabilities += probabilities[i];
        }

        // Normalize probabilities
        if (sumProbabilities > 0) {
            for (int i = 0; i < n; i++) {
                probabilities[i] /= sumProbabilities;
            }
        } else {
            // Fallback to uniform distribution
            for (int i = 0; i < n; i++) {
                probabilities[i] = 1.0 / n;
            }
        }
    }

    /**
     * Gets the current selection pressure.
     *
     * @return the selection pressure
     */
    public double getSelectionPressure() {
        return selectionPressure;
    }

    /**
     * Gets the current ranking mode.
     *
     * @return the ranking mode
     */
    public RankingMode getRankingMode() {
        return rankingMode;
    }

    /**
     * Checks if this is a maximization problem.
     *
     * @return true if maximization, false if minimization
     */
    public boolean isMaximization() {
        return maximization;
    }

    /**
     * Clears the cache, forcing re-ranking on next selection.
     * Useful when population has changed externally.
     */
    public void clearCache() {
        cachedPopulation = null;
        rankedPopulation = null;
        cumulativeProbabilities = null;
    }

    /**
     * Helper class to store an individual with its fitness and rank.
     */
    private static class RankedIndividual<C extends Chromosome<?>> {
        final C individual;
        final double fitness;
        int rank;

        RankedIndividual(C individual, double fitness) {
            this.individual = individual;
            this.fitness = fitness;
        }
    }
}

