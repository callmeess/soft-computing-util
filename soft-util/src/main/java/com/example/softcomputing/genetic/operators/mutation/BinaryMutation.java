package com.example.softcomputing.genetic.operators.mutation;

import com.example.softcomputing.genetic.chromosome.BinaryChromosome;
import java.util.Random;

/**
 * Binary Mutation (Bit-Flip Mutation) for BinaryChromosome.
 * This mutation operator introduces genetic diversity by randomly flipping bits
 * in a chromosome. Each bit is independently considered for mutation based on
 * the specified mutation probability.
 *
 * <p>Mutation Rate Guidelines:</p>
 * <ul>
 *   <li>Standard: 1/L where L = chromosome length</li>
 *   <li>Low diversity: Increase to 2/L or 3/L</li>
 *   <li>Small chromosomes (L &lt; 20): 0.05 - 0.1</li>
 *   <li>Large chromosomes (L &gt; 100): 0.001 - 0.01</li>
 *   <li>Too high: disrupts good solutions (random search)</li>
 *   <li>Too low: insufficient diversity, premature convergence</li>
 * </ul>
 *
 */
public class BinaryMutation implements MutationStrategy<BinaryChromosome> {
    private final double mutationProbability;
    private final Random random;
    private final boolean adaptive;

    /**
     * Creates a BinaryMutation with specified mutation probability.
     *
     * @param mutationProbability Per-bit mutation probability [0.0, 1.0]
     * @throws IllegalArgumentException if probability is not in [0.0, 1.0]
     */
    public BinaryMutation(double mutationProbability) {
        this(mutationProbability, false);
    }

    /**
     * Creates a BinaryMutation with specified mutation probability and adaptive option.
     *
     * @param mutationProbability Per-bit mutation probability [0.0, 1.0]
     * @param adaptive Enable adaptive mutation rate (future feature)
     * @throws IllegalArgumentException if probability is not in [0.0, 1.0]
     */
    public BinaryMutation(double mutationProbability, boolean adaptive) {
        validateMutationProbability(mutationProbability);
        this.mutationProbability = mutationProbability;
        this.adaptive = adaptive;
        this.random = new Random();
    }

    /**
     * Creates a BinaryMutation with custom random generator (for testing).
     *
     * @param mutationProbability Per-bit mutation probability [0.0, 1.0]
     * @param random Custom random number generator
     * @throws IllegalArgumentException if probability is not in [0.0, 1.0]
     */
    public BinaryMutation(double mutationProbability, Random random) {
        validateMutationProbability(mutationProbability);
        this.mutationProbability = mutationProbability;
        this.adaptive = false;
        this.random = random;
    }

    /**
     * Validates that mutation probability is in the valid range [0.0, 1.0].
     *
     * @param probability The probability to validate
     * @throws IllegalArgumentException if probability is not in [0.0, 1.0]
     */
    private void validateMutationProbability(double probability) {
        if (probability < 0.0 || probability > 1.0) {
            throw new IllegalArgumentException(
                String.format("Mutation probability must be in [0.0, 1.0], got: %.4f", probability)
            );
        }
    }

    /**
     * Mutates the given chromosome by independently flipping each bit
     * with the specified mutation probability.
     *
     * <p>Algorithm:</p>
     * <ol>
     *   <li>Create a copy of the chromosome genes</li>
     *   <li>For each bit position i from 0 to length-1:</li>
     *   <li>  a. Generate random value r in [0, 1)</li>
     *   <li>  b. If r &lt; mutationProbability: flip bit at position i</li>
     *   <li>Return new BinaryChromosome with mutated genes</li>
     * </ol>
     *
     * <p>Edge Cases:</p>
     * <ul>
     *   <li>mutationProbability = 0.0: Returns identical copy (no mutation)</li>
     *   <li>mutationProbability = 1.0: All bits are flipped</li>
     *   <li>Empty chromosome: Returns empty chromosome</li>
     * </ul>
     *
     * @param individual The chromosome to mutate (not modified)
     * @return A new mutated chromosome (original is preserved)
     */
    @Override
    public BinaryChromosome mutate(BinaryChromosome individual) {
        int length = individual.length();

        // Handle empty chromosome
        if (length == 0) {
            return new BinaryChromosome(new int[0]);
        }

        // Create a copy of genes to maintain immutability
        Integer[] originalGenes = individual.toArray();
        int[] mutatedGenes = new int[length];

        // Copy and mutate genes
        for (int i = 0; i < length; i++) {
            mutatedGenes[i] = originalGenes[i];

            // Decide whether to flip this bit based on mutation probability
            if (random.nextDouble() < mutationProbability) {
                // Flip the bit (0 -> 1, 1 -> 0)
                mutatedGenes[i] = 1 - mutatedGenes[i];
            }
        }

        // Return new chromosome with mutated genes (immutability preserved)
        return new BinaryChromosome(mutatedGenes);
    }

    /**
     * Gets the mutation probability per bit.
     *
     * @return The mutation probability
     */
    public double getMutationProbability() {
        return mutationProbability;
    }

    /**
     * Checks if adaptive mutation is enabled.
     *
     * @return true if adaptive mutation is enabled
     */
    public boolean isAdaptive() {
        return adaptive;
    }

    /**
     * Creates a BinaryMutation with standard mutation rate (1/L).
     * This is the recommended default for most binary genetic algorithms.
     *
     * @param chromosomeLength The length of chromosomes to be mutated
     * @return A BinaryMutation with mutation probability = 1/chromosomeLength
     * @throws IllegalArgumentException if chromosomeLength &lt;= 0
     */
    public static BinaryMutation withStandardRate(int chromosomeLength) {
        if (chromosomeLength <= 0) {
            throw new IllegalArgumentException("Chromosome length must be positive");
        }
        return new BinaryMutation(1.0 / chromosomeLength);
    }

    @Override
    public String toString() {
        return String.format("BinaryMutation(probability=%.4f, adaptive=%s)",
                           mutationProbability, adaptive);
    }
}
