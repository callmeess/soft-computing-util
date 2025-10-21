package com.example.softcomputing.genetic.operators.mutation;

import com.example.softcomputing.genetic.chromosome.BinaryChromosome;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive unit tests for BinaryMutation operator.
 * Tests cover all requirements including mutation rate statistics,
 * edge cases, immutability, and validation.
 */
class BinaryMutationTest {

    @Test
    @DisplayName("Constructor should validate mutation probability in range [0.0, 1.0]")
    void testConstructorValidation() {
        // Valid probabilities
        assertDoesNotThrow(() -> new BinaryMutation(0.0));
        assertDoesNotThrow(() -> new BinaryMutation(0.5));
        assertDoesNotThrow(() -> new BinaryMutation(1.0));

        // Invalid probabilities
        assertThrows(IllegalArgumentException.class, () -> new BinaryMutation(-0.1));
        assertThrows(IllegalArgumentException.class, () -> new BinaryMutation(1.1));
        assertThrows(IllegalArgumentException.class, () -> new BinaryMutation(-1.0));
        assertThrows(IllegalArgumentException.class, () -> new BinaryMutation(2.0));
    }

    @Test
    @DisplayName("Mutation with probability 0.0 should return identical copy")
    void testZeroMutationProbability() {
        BinaryMutation mutation = new BinaryMutation(0.0);
        BinaryChromosome original = new BinaryChromosome(new int[]{1, 0, 1, 1, 0});

        BinaryChromosome mutated = mutation.mutate(original);

        // Verify bits are identical
        assertEquals(original.length(), mutated.length());
        for (int i = 0; i < original.length(); i++) {
            assertEquals(original.getGene(i), mutated.getGene(i),
                "Bit " + i + " should not be mutated with probability 0.0");
        }
    }

    @Test
    @DisplayName("Mutation with probability 1.0 should flip all bits")
    void testFullMutationProbability() {
        BinaryMutation mutation = new BinaryMutation(1.0);
        BinaryChromosome original = new BinaryChromosome(new int[]{1, 0, 1, 1, 0});

        BinaryChromosome mutated = mutation.mutate(original);

        // Verify all bits are flipped
        assertEquals(original.length(), mutated.length());
        for (int i = 0; i < original.length(); i++) {
            assertEquals(1 - original.getGene(i), mutated.getGene(i),
                "Bit " + i + " should be flipped with probability 1.0");
        }
    }

    @Test
    @DisplayName("Original chromosome should not be modified (immutability)")
    void testImmutability() {
        BinaryMutation mutation = new BinaryMutation(0.5);
        int[] originalGenes = {1, 0, 1, 1, 0};
        BinaryChromosome original = new BinaryChromosome(originalGenes.clone());

        // Store original values
        Integer[] originalValues = new Integer[original.length()];
        for (int i = 0; i < original.length(); i++) {
            originalValues[i] = original.getGene(i);
        }

        // Mutate
        BinaryChromosome mutated = mutation.mutate(original);

        // Verify original is unchanged
        for (int i = 0; i < original.length(); i++) {
            assertEquals(originalValues[i], original.getGene(i),
                "Original chromosome should not be modified");
        }

        // Verify different objects
        assertNotSame(original, mutated, "Mutated chromosome should be a new object");
    }

    @Test
    @DisplayName("Mutated chromosome should have same length as original")
    void testChromosomeLengthPreserved() {
        BinaryMutation mutation = new BinaryMutation(0.2);

        // Test various lengths
        int[] lengths = {1, 5, 10, 50, 100};
        for (int length : lengths) {
            BinaryChromosome original = BinaryChromosome.random(length);
            BinaryChromosome mutated = mutation.mutate(original);

            assertEquals(original.length(), mutated.length(),
                "Chromosome length should be preserved for length " + length);
        }
    }

    @Test
    @DisplayName("Empty chromosome should return empty chromosome")
    void testEmptyChromosome() {
        BinaryMutation mutation = new BinaryMutation(0.5);
        BinaryChromosome empty = new BinaryChromosome(new int[0]);

        BinaryChromosome mutated = mutation.mutate(empty);

        assertEquals(0, mutated.length(), "Empty chromosome should remain empty");
    }

    @Test
    @DisplayName("Each bit should be independently considered for mutation")
    void testIndependentBitMutation() {
        // Use controlled random for deterministic testing
        Random controlledRandom = new Random(42);
        BinaryMutation mutation = new BinaryMutation(0.5, controlledRandom);

        BinaryChromosome original = new BinaryChromosome(new int[]{1, 1, 1, 1, 1});
        BinaryChromosome mutated = mutation.mutate(original);

        // With controlled random, we should see some bits flipped, not all or none
        int flippedCount = 0;
        for (int i = 0; i < original.length(); i++) {
            if (!original.getGene(i).equals(mutated.getGene(i))) {
                flippedCount++;
            }
        }

        // With 50% probability and 5 bits, we expect some but not all to flip
        assertTrue(flippedCount > 0 && flippedCount < original.length(),
            "With independent mutation, some (but not all) bits should flip");
    }

    @Test
    @DisplayName("Bits should be correctly flipped (0→1, 1→0)")
    void testBitFlippingCorrectness() {
        BinaryMutation mutation = new BinaryMutation(1.0); // Flip all

        BinaryChromosome original = new BinaryChromosome(new int[]{0, 1, 0, 1, 0});
        BinaryChromosome mutated = mutation.mutate(original);

        assertArrayEquals(new Integer[]{1, 0, 1, 0, 1}, mutated.toArray(),
            "All bits should be correctly flipped");
    }

    @RepeatedTest(10)
    @DisplayName("Statistical test: mutation rate should approximate specified probability")
    void testMutationRateStatistics() {
        double expectedRate = 0.2;
        BinaryMutation mutation = new BinaryMutation(expectedRate);
        int chromosomeLength = 100;
        int numTrials = 1000;

        int totalBits = chromosomeLength * numTrials;
        int totalFlips = 0;

        for (int trial = 0; trial < numTrials; trial++) {
            BinaryChromosome original = BinaryChromosome.random(chromosomeLength);
            BinaryChromosome mutated = mutation.mutate(original);

            // Count flipped bits
            for (int i = 0; i < chromosomeLength; i++) {
                if (!original.getGene(i).equals(mutated.getGene(i))) {
                    totalFlips++;
                }
            }
        }

        double actualRate = (double) totalFlips / totalBits;

        // Allow 10% deviation from expected rate (statistical tolerance)
        double tolerance = expectedRate * 0.1;
        assertEquals(expectedRate, actualRate, tolerance,
            "Actual mutation rate should approximate expected rate");
    }

    @Test
    @DisplayName("Standard rate constructor should calculate 1/L correctly")
    void testStandardRateConstructor() {
        BinaryMutation mutation = BinaryMutation.withStandardRate(10);
        assertEquals(0.1, mutation.getMutationProbability(), 0.0001,
            "Standard rate should be 1/chromosomeLength");

        mutation = BinaryMutation.withStandardRate(100);
        assertEquals(0.01, mutation.getMutationProbability(), 0.0001,
            "Standard rate should be 1/chromosomeLength");

        assertThrows(IllegalArgumentException.class,
            () -> BinaryMutation.withStandardRate(0),
            "Should reject non-positive chromosome length");

        assertThrows(IllegalArgumentException.class,
            () -> BinaryMutation.withStandardRate(-5),
            "Should reject negative chromosome length");
    }

    @Test
    @DisplayName("Getter methods should return correct values")
    void testGetters() {
        BinaryMutation mutation1 = new BinaryMutation(0.3);
        assertEquals(0.3, mutation1.getMutationProbability(), 0.0001);
        assertFalse(mutation1.isAdaptive());

        BinaryMutation mutation2 = new BinaryMutation(0.5, true);
        assertEquals(0.5, mutation2.getMutationProbability(), 0.0001);
        assertTrue(mutation2.isAdaptive());
    }

    @Test
    @DisplayName("toString should provide meaningful representation")
    void testToString() {
        BinaryMutation mutation = new BinaryMutation(0.25);
        String str = mutation.toString();

        assertTrue(str.contains("0.25") || str.contains("0.2500"),
            "toString should include mutation probability");
        assertTrue(str.contains("BinaryMutation"),
            "toString should include class name");
    }

    @Test
    @DisplayName("No index out of bounds errors for various chromosome sizes")
    void testNoIndexOutOfBounds() {
        BinaryMutation mutation = new BinaryMutation(0.5);

        // Test edge cases
        assertDoesNotThrow(() -> {
            mutation.mutate(new BinaryChromosome(new int[]{0})); // Single bit
            mutation.mutate(new BinaryChromosome(new int[]{0, 1})); // Two bits
            mutation.mutate(BinaryChromosome.random(1000)); // Large chromosome
        });
    }

    @Test
    @DisplayName("Multiple mutations should produce different results (randomness)")
    void testRandomness() {
        BinaryMutation mutation = new BinaryMutation(0.3);
        BinaryChromosome original = new BinaryChromosome(new int[]{1, 0, 1, 1, 0, 1, 0, 1, 1, 0});

        // Perform multiple mutations
        BinaryChromosome mutated1 = mutation.mutate(original);
        BinaryChromosome mutated2 = mutation.mutate(original);
        BinaryChromosome mutated3 = mutation.mutate(original);

        // At least one should be different from others (with high probability)
        boolean allSame = true;
        for (int i = 0; i < original.length(); i++) {
            if (!mutated1.getGene(i).equals(mutated2.getGene(i)) ||
                !mutated2.getGene(i).equals(mutated3.getGene(i))) {
                allSame = false;
                break;
            }
        }

        assertFalse(allSame, "Multiple mutations should produce different results");
    }

    @Test
    @DisplayName("Mutation with typical rate 1/L should work correctly")
    void testTypicalMutationRate() {
        int chromosomeLength = 20;
        double typicalRate = 1.0 / chromosomeLength; // 0.05
        BinaryMutation mutation = new BinaryMutation(typicalRate);

        BinaryChromosome original = BinaryChromosome.random(chromosomeLength);

        assertDoesNotThrow(() -> {
            for (int i = 0; i < 100; i++) {
                BinaryChromosome mutated = mutation.mutate(original);
                assertEquals(chromosomeLength, mutated.length());
            }
        });
    }

    @Test
    @DisplayName("Verify mutation on all-zeros chromosome")
    void testAllZerosChromosome() {
        BinaryMutation mutation = new BinaryMutation(1.0);
        BinaryChromosome allZeros = new BinaryChromosome(new int[]{0, 0, 0, 0, 0});

        BinaryChromosome mutated = mutation.mutate(allZeros);

        // All bits should flip to 1
        for (int i = 0; i < mutated.length(); i++) {
            assertEquals(1, mutated.getGene(i),
                "All zeros should become all ones with probability 1.0");
        }
    }

    @Test
    @DisplayName("Verify mutation on all-ones chromosome")
    void testAllOnesChromosome() {
        BinaryMutation mutation = new BinaryMutation(1.0);
        BinaryChromosome allOnes = new BinaryChromosome(new int[]{1, 1, 1, 1, 1});

        BinaryChromosome mutated = mutation.mutate(allOnes);

        // All bits should flip to 0
        for (int i = 0; i < mutated.length(); i++) {
            assertEquals(0, mutated.getGene(i),
                "All ones should become all zeros with probability 1.0");
        }
    }

    @Test
    @DisplayName("Mutation rate statistics for small probability")
    void testSmallMutationRateStatistics() {
        double smallRate = 0.01; // 1% per bit
        BinaryMutation mutation = new BinaryMutation(smallRate);
        int chromosomeLength = 100;
        int numTrials = 500;

        int totalFlips = 0;

        for (int trial = 0; trial < numTrials; trial++) {
            BinaryChromosome original = BinaryChromosome.random(chromosomeLength);
            BinaryChromosome mutated = mutation.mutate(original);

            for (int i = 0; i < chromosomeLength; i++) {
                if (!original.getGene(i).equals(mutated.getGene(i))) {
                    totalFlips++;
                }
            }
        }

        double actualRate = (double) totalFlips / (chromosomeLength * numTrials);

        // For small rates, allow larger relative tolerance
        assertEquals(smallRate, actualRate, smallRate * 0.3,
            "Small mutation rate should be statistically accurate");
    }

    @Test
    @DisplayName("Mutation rate statistics for high probability")
    void testHighMutationRateStatistics() {
        double highRate = 0.9; // 90% per bit
        BinaryMutation mutation = new BinaryMutation(highRate);
        int chromosomeLength = 50;
        int numTrials = 200;

        int totalFlips = 0;

        for (int trial = 0; trial < numTrials; trial++) {
            BinaryChromosome original = BinaryChromosome.random(chromosomeLength);
            BinaryChromosome mutated = mutation.mutate(original);

            for (int i = 0; i < chromosomeLength; i++) {
                if (!original.getGene(i).equals(mutated.getGene(i))) {
                    totalFlips++;
                }
            }
        }

        double actualRate = (double) totalFlips / (chromosomeLength * numTrials);

        assertEquals(highRate, actualRate, 0.05,
            "High mutation rate should be statistically accurate");
    }
}

