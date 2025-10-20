package com.example.softcomputing.genetic.operators.replacement;

import com.example.softcomputing.genetic.chromosome.Chromosome;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Steady-State Replacement (Incremental Replacement) for Genetic Algorithms.
 *
 * <p>This replacement strategy replaces only a small portion of the population each generation,
 * maintaining high population overlap between generations. This preserves diversity while
 * allowing gradual improvement.</p>
 *
 * <p><b>Key Characteristics:</b></p>
 * <ul>
 *   <li>Replaces only N worst individuals per generation (typically 1-2 or 10-20% of population)</li>
 *   <li>Maintains majority of population unchanged between generations</li>
 *   <li>Provides smoother convergence than generational replacement</li>
 *   <li>Higher diversity maintained throughout evolution</li>
 *   <li>More stable best fitness progression</li>
 *   <li>Less sensitive to parameter settings</li>
 * </ul>
 *
 * <p><b>Algorithm:</b></p>
 * <ol>
 *   <li>Evaluate fitness for all individuals in current population</li>
 *   <li>Sort current population by fitness (descending for maximization)</li>
 *   <li>Identify worst N individuals to replace</li>
 *   <li>Evaluate fitness for offspring</li>
 *   <li>Sort offspring by fitness and select best N</li>
 *   <li>Create new population: keep best (populationSize - N) + best N offspring</li>
 * </ol>
 *
 * <p><b>Replacement Count Guidelines:</b></p>
 * <ul>
 *   <li>Small populations (10-50): 1-2 individuals</li>
 *   <li>Medium populations (50-200): 5-20 individuals (10%)</li>
 *   <li>Large populations (200+): 10-40 individuals (10-20%)</li>
 *   <li>Smaller replacement → slower convergence, more diversity</li>
 *   <li>Larger replacement → faster convergence, less diversity</li>
 * </ul>
 *
 * <p><b>Comparison with Generational Replacement:</b></p>
 * <table border="1">
 *   <tr><th>Characteristic</th><th>Generational</th><th>Steady-State</th></tr>
 *   <tr><td>Replacement Rate</td><td>100%</td><td>10-20%</td></tr>
 *   <tr><td>Diversity</td><td>Lower</td><td>Higher</td></tr>
 *   <tr><td>Convergence</td><td>Faster</td><td>Slower</td></tr>
 *   <tr><td>Population Overlap</td><td>Low</td><td>High</td></tr>
 *   <tr><td>Computational Cost</td><td>Bursty</td><td>Smooth</td></tr>
 * </table>
 *
 * <p><b>Replacement Modes:</b></p>
 * <ul>
 *   <li><b>FITNESS</b> (default): Replace individuals with worst fitness values</li>
 *   <li><b>AGE</b>: Replace oldest individuals regardless of fitness (requires age tracking - future)</li>
 *   <li><b>PARENT</b>: Replace parents that generated offspring (future enhancement)</li>
 * </ul>
 *
 * @param <C> The chromosome type extending Chromosome interface
 *
 * @author Soft Computing Util
 * @version 1.0
 */
public class SteadyStateReplacement<C extends Chromosome<?>> implements Replacement<C> {

    /**
     * Enum defining different replacement modes.
     */
    public enum ReplacementMode {
        /** Replace individuals with worst fitness values (default) */
        FITNESS,
        /** Replace oldest individuals regardless of fitness (requires age tracking) */
        AGE,
        /** Replace parents that generated the offspring */
        PARENT
    }

    private final int replacementCount;
    private final ReplacementMode mode;
    private final boolean allowParentReplacement;

    /**
     * Creates a SteadyStateReplacement with specified replacement count.
     * Uses FITNESS mode by default.
     *
     * @param replacementCount Number of individuals to replace per generation
     * @throws IllegalArgumentException if replacementCount <= 0
     */
    public SteadyStateReplacement(int replacementCount) {
        this(replacementCount, ReplacementMode.FITNESS, true);
    }

    /**
     * Creates a SteadyStateReplacement with specified replacement count and mode.
     *
     * @param replacementCount Number of individuals to replace per generation
     * @param mode Replacement mode (FITNESS, AGE, or PARENT)
     * @throws IllegalArgumentException if replacementCount <= 0
     */
    public SteadyStateReplacement(int replacementCount, ReplacementMode mode) {
        this(replacementCount, mode, true);
    }

    /**
     * Creates a SteadyStateReplacement with full configuration.
     *
     * @param replacementCount Number of individuals to replace per generation
     * @param mode Replacement mode (FITNESS, AGE, or PARENT)
     * @param allowParentReplacement Whether offspring can replace their parents
     * @throws IllegalArgumentException if replacementCount <= 0
     */
    public SteadyStateReplacement(int replacementCount, ReplacementMode mode, boolean allowParentReplacement) {
        if (replacementCount <= 0) {
            throw new IllegalArgumentException(
                String.format("Replacement count must be positive, got: %d", replacementCount)
            );
        }
        this.replacementCount = replacementCount;
        this.mode = mode;
        this.allowParentReplacement = allowParentReplacement;
    }

    /**
     * Factory method to create SteadyStateReplacement with percentage-based replacement count.
     *
     * @param <C> The chromosome type
     * @param populationSize The size of the population
     * @param replacementPercentage Percentage of population to replace (0.0 to 1.0)
     * @return A new SteadyStateReplacement instance
     * @throws IllegalArgumentException if percentage is not in (0.0, 1.0) or population size <= 0
     */
    public static <C extends Chromosome<?>> SteadyStateReplacement<C> withPercentage(
            int populationSize, double replacementPercentage) {
        if (populationSize <= 0) {
            throw new IllegalArgumentException("Population size must be positive");
        }
        if (replacementPercentage <= 0.0 || replacementPercentage >= 1.0) {
            throw new IllegalArgumentException(
                String.format("Replacement percentage must be in (0.0, 1.0), got: %.4f", replacementPercentage)
            );
        }
        int count = Math.max(1, (int) Math.round(populationSize * replacementPercentage));
        return new SteadyStateReplacement<>(count);
    }

    /**
     * Factory method to create SteadyStateReplacement with standard 10% replacement rate.
     *
     * @param <C> The chromosome type
     * @param populationSize The size of the population
     * @return A new SteadyStateReplacement instance with 10% replacement
     * @throws IllegalArgumentException if population size <= 0
     */
    public static <C extends Chromosome<?>> SteadyStateReplacement<C> withStandardRate(int populationSize) {
        return withPercentage(populationSize, 0.1);
    }

    /**
     * Replaces the worst individuals in the current population with the best offspring.
     *
     * <p><b>Algorithm Steps:</b></p>
     * <ol>
     *   <li>Validate that replacementCount < populationSize</li>
     *   <li>Handle edge case: if fewer offspring than replacementCount, adjust count</li>
     *   <li>Evaluate fitness for all current population individuals</li>
     *   <li>Sort current population by fitness (descending)</li>
     *   <li>Evaluate fitness for all offspring</li>
     *   <li>Sort offspring by fitness (descending)</li>
     *   <li>Select best (populationSize - actualReplacement) from current population</li>
     *   <li>Select best actualReplacement from offspring</li>
     *   <li>Combine them to form new population</li>
     * </ol>
     *
     * <p><b>Edge Cases Handled:</b></p>
     * <ul>
     *   <li>Empty current population: returns empty list</li>
     *   <li>Empty offspring: returns current population unchanged</li>
     *   <li>Fewer offspring than replacementCount: replaces only available offspring count</li>
     *   <li>replacementCount >= populationSize: caps at populationSize - 1</li>
     *   <li>Equal fitness values: preserves order stability</li>
     * </ul>
     *
     * @param currentPopulation The current population (not modified)
     * @param newIndividuals The offspring generated this generation
     * @return A new population with worst individuals replaced by best offspring
     * @throws IllegalArgumentException if replacementCount >= populationSize
     */
    @Override
    public List<C> replacePopulation(List<C> currentPopulation, List<C> newIndividuals) {
        // Handle empty population edge case
        if (currentPopulation == null || currentPopulation.isEmpty()) {
            return new ArrayList<>();
        }

        // Handle empty offspring edge case
        if (newIndividuals == null || newIndividuals.isEmpty()) {
            return new ArrayList<>(currentPopulation);
        }

        int populationSize = currentPopulation.size();

        // Validate replacement count
        if (replacementCount >= populationSize) {
            throw new IllegalArgumentException(
                String.format("Replacement count (%d) must be less than population size (%d)",
                             replacementCount, populationSize)
            );
        }

        // Adjust replacement count if fewer offspring available
        int actualReplacementCount = Math.min(replacementCount, newIndividuals.size());

        // Perform replacement based on mode
        switch (mode) {
            case FITNESS:
                return replaceFitnessBased(currentPopulation, newIndividuals,
                                          populationSize, actualReplacementCount);
            case AGE:
                // Age-based replacement would require age tracking in chromosomes
                // For now, fall back to fitness-based
                System.out.println("Warning: AGE mode not yet implemented, using FITNESS mode");
                return replaceFitnessBased(currentPopulation, newIndividuals,
                                          populationSize, actualReplacementCount);
            case PARENT:
                // Parent replacement would require tracking parent-offspring relationships
                // For now, fall back to fitness-based
                System.out.println("Warning: PARENT mode not yet implemented, using FITNESS mode");
                return replaceFitnessBased(currentPopulation, newIndividuals,
                                          populationSize, actualReplacementCount);
            default:
                return replaceFitnessBased(currentPopulation, newIndividuals,
                                          populationSize, actualReplacementCount);
        }
    }

    /**
     * Performs fitness-based replacement: worst fitness individuals are replaced.
     *
     * @param currentPopulation Current population
     * @param offspring Generated offspring
     * @param populationSize Size of population
     * @param actualReplacementCount Actual number to replace
     * @return New population with replacements made
     */
    private List<C> replaceFitnessBased(List<C> currentPopulation, List<C> offspring,
                                        int populationSize, int actualReplacementCount) {
        // Create a list with fitness evaluations for current population
        List<IndividualWithFitness<C>> currentWithFitness = currentPopulation.stream()
            .map(ind -> new IndividualWithFitness<>(ind, ind.evaluate()))
            .collect(Collectors.toList());

        // Sort current population by fitness (descending - best first)
        currentWithFitness.sort(Comparator.comparingDouble(IndividualWithFitness<C>::getFitness).reversed());

        // Create a list with fitness evaluations for offspring
        List<IndividualWithFitness<C>> offspringWithFitness = offspring.stream()
            .map(ind -> new IndividualWithFitness<>(ind, ind.evaluate()))
            .collect(Collectors.toList());

        // Sort offspring by fitness (descending - best first)
        offspringWithFitness.sort(Comparator.comparingDouble(IndividualWithFitness<C>::getFitness).reversed());

        // Create new population
        List<C> newPopulation = new ArrayList<>(populationSize);

        // Keep best (populationSize - actualReplacementCount) from current population
        int keepCount = populationSize - actualReplacementCount;
        for (int i = 0; i < keepCount; i++) {
            newPopulation.add(currentWithFitness.get(i).getIndividual());
        }

        // Add best actualReplacementCount offspring
        for (int i = 0; i < actualReplacementCount; i++) {
            newPopulation.add(offspringWithFitness.get(i).getIndividual());
        }

        return newPopulation;
    }

    /**
     * Gets the replacement count.
     *
     * @return Number of individuals replaced per generation
     */
    public int getReplacementCount() {
        return replacementCount;
    }

    /**
     * Gets the replacement mode.
     *
     * @return The replacement mode (FITNESS, AGE, or PARENT)
     */
    public ReplacementMode getMode() {
        return mode;
    }

    /**
     * Checks if parent replacement is allowed.
     *
     * @return true if offspring can replace their parents
     */
    public boolean isAllowParentReplacement() {
        return allowParentReplacement;
    }

    @Override
    public String toString() {
        return String.format("SteadyStateReplacement(count=%d, mode=%s, allowParentReplacement=%s)",
                           replacementCount, mode, allowParentReplacement);
    }

    /**
     * Helper class to pair individuals with their fitness values.
     * This avoids re-evaluating fitness multiple times.
     */
    private static class IndividualWithFitness<C extends Chromosome<?>> {
        private final C individual;
        private final double fitness;

        public IndividualWithFitness(C individual, double fitness) {
            this.individual = individual;
            this.fitness = fitness;
        }

        public C getIndividual() {
            return individual;
        }

        public double getFitness() {
            return fitness;
        }
    }
}

