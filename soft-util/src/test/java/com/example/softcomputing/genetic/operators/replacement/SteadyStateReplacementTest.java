package com.example.softcomputing.genetic.operators.replacement;

import com.example.softcomputing.genetic.chromosome.BinaryChromosome;
import com.example.softcomputing.genetic.chromosome.Chromosome;
import com.example.softcomputing.genetic.chromosome.FloatingPointChromosome;
import com.example.softcomputing.genetic.chromosome.IntegerChromosome;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SteadyStateReplacementTest {

    @Nested
    @DisplayName("Constructor and Validation Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Constructor should validate replacement count is positive")
        void testConstructorValidation() {
            // Valid counts
            assertDoesNotThrow(() -> new SteadyStateReplacement<BinaryChromosome>(1));
            assertDoesNotThrow(() -> new SteadyStateReplacement<BinaryChromosome>(10));
            assertDoesNotThrow(() -> new SteadyStateReplacement<BinaryChromosome>(100));

            // Invalid counts
            assertThrows(IllegalArgumentException.class,
                () -> new SteadyStateReplacement<BinaryChromosome>(0));
            assertThrows(IllegalArgumentException.class,
                () -> new SteadyStateReplacement<BinaryChromosome>(-1));
            assertThrows(IllegalArgumentException.class,
                () -> new SteadyStateReplacement<BinaryChromosome>(-10));
        }

        @Test
        @DisplayName("Constructor with mode should accept valid parameters")
        void testConstructorWithMode() {
            assertDoesNotThrow(() -> new SteadyStateReplacement<BinaryChromosome>(
                5, SteadyStateReplacement.ReplacementMode.FITNESS));
            assertDoesNotThrow(() -> new SteadyStateReplacement<BinaryChromosome>(
                5, SteadyStateReplacement.ReplacementMode.AGE));
            assertDoesNotThrow(() -> new SteadyStateReplacement<BinaryChromosome>(
                5, SteadyStateReplacement.ReplacementMode.PARENT));
        }

        @Test
        @DisplayName("Constructor with full parameters should accept valid values")
        void testFullConstructor() {
            assertDoesNotThrow(() -> new SteadyStateReplacement<BinaryChromosome>(
                5, SteadyStateReplacement.ReplacementMode.FITNESS, true));
            assertDoesNotThrow(() -> new SteadyStateReplacement<BinaryChromosome>(
                5, SteadyStateReplacement.ReplacementMode.FITNESS, false));
        }

        @Test
        @DisplayName("withPercentage factory should validate parameters")
        void testWithPercentageValidation() {
            // Valid parameters
            assertDoesNotThrow(() ->
                SteadyStateReplacement.withPercentage(100, 0.1));
            assertDoesNotThrow(() ->
                SteadyStateReplacement.withPercentage(100, 0.5));

            // Invalid population size
            assertThrows(IllegalArgumentException.class, () ->
                SteadyStateReplacement.withPercentage(0, 0.1));
            assertThrows(IllegalArgumentException.class, () ->
                SteadyStateReplacement.withPercentage(-10, 0.1));

            // Invalid percentage
            assertThrows(IllegalArgumentException.class, () ->
                SteadyStateReplacement.withPercentage(100, 0.0));
            assertThrows(IllegalArgumentException.class, () ->
                SteadyStateReplacement.withPercentage(100, 1.0));
            assertThrows(IllegalArgumentException.class, () ->
                SteadyStateReplacement.withPercentage(100, -0.1));
            assertThrows(IllegalArgumentException.class, () ->
                SteadyStateReplacement.withPercentage(100, 1.5));
        }

        @Test
        @DisplayName("withStandardRate factory should create 10% replacement")
        void testWithStandardRate() {
            SteadyStateReplacement<BinaryChromosome> replacement =
                SteadyStateReplacement.withStandardRate(100);

            assertEquals(10, replacement.getReplacementCount());
        }

        @Test
        @DisplayName("withPercentage should calculate correct count")
        void testWithPercentageCalculation() {
            SteadyStateReplacement<BinaryChromosome> r1 =
                SteadyStateReplacement.withPercentage(100, 0.1);
            assertEquals(10, r1.getReplacementCount());

            SteadyStateReplacement<BinaryChromosome> r2 =
                SteadyStateReplacement.withPercentage(50, 0.2);
            assertEquals(10, r2.getReplacementCount());

            // Should round to at least 1
            SteadyStateReplacement<BinaryChromosome> r3 =
                SteadyStateReplacement.withPercentage(10, 0.05);
            assertTrue(r3.getReplacementCount() >= 1);
        }
    }

    @Nested
    @DisplayName("Basic Replacement Behavior Tests")
    class BasicReplacementTests {

        @Test
        @DisplayName("Should replace worst N individuals with best N offspring")
        void testBasicReplacement() {
            // Create population with known fitness values
            List<TestChromosome> population = createTestPopulation(
                new double[]{90, 85, 80, 75, 70, 65, 60, 55, 50, 45}
            );

            // Create offspring with known fitness values
            List<TestChromosome> offspring = createTestPopulation(
                new double[]{88, 82, 78, 72, 68}
            );

            SteadyStateReplacement<TestChromosome> replacement =
                new SteadyStateReplacement<>(3);

            List<TestChromosome> newPopulation =
                replacement.replacePopulation(population, offspring);

            // Verify population size maintained
            assertEquals(10, newPopulation.size());

            // Extract fitness values
            double[] fitnesses = newPopulation.stream()
                .mapToDouble(TestChromosome::evaluate)
                .sorted()
                .toArray();

            // Should contain: [90, 85, 80, 75, 70, 65, 60] from original
            // and [88, 82, 78] from offspring
            // Expected sorted: [60, 65, 70, 75, 78, 80, 82, 85, 88, 90]
            assertArrayEquals(
                new double[]{60, 65, 70, 75, 78, 80, 82, 85, 88, 90},
                fitnesses,
                0.001
            );
        }

        @Test
        @DisplayName("Should maintain population size")
        void testPopulationSizeMaintained() {
            List<TestChromosome> population = createTestPopulation(
                new double[]{10, 9, 8, 7, 6, 5, 4, 3, 2, 1}
            );

            List<TestChromosome> offspring = createTestPopulation(
                new double[]{9.5, 8.5, 7.5}
            );

            SteadyStateReplacement<TestChromosome> replacement =
                new SteadyStateReplacement<>(2);

            List<TestChromosome> newPopulation =
                replacement.replacePopulation(population, offspring);

            assertEquals(population.size(), newPopulation.size());
        }

        @Test
        @DisplayName("Should keep best individuals from current population")
        void testBestIndividualsKept() {
            List<TestChromosome> population = createTestPopulation(
                new double[]{100, 90, 80, 70, 60}
            );

            List<TestChromosome> offspring = createTestPopulation(
                new double[]{75, 65, 55}
            );

            SteadyStateReplacement<TestChromosome> replacement =
                new SteadyStateReplacement<>(2);

            List<TestChromosome> newPopulation =
                replacement.replacePopulation(population, offspring);

            // Best 3 from original (100, 90, 80) should be kept
            // Best 2 from offspring (75, 65) should replace worst 2 (70, 60)
            double[] fitnesses = newPopulation.stream()
                .mapToDouble(TestChromosome::evaluate)
                .sorted()
                .toArray();

            assertArrayEquals(
                new double[]{65, 75, 80, 90, 100},
                fitnesses,
                0.001
            );
        }

        @Test
        @DisplayName("Should select best offspring as replacements")
        void testBestOffspringSelected() {
            List<TestChromosome> population = createTestPopulation(
                new double[]{50, 40, 30, 20, 10}
            );

            // Offspring with varying quality
            List<TestChromosome> offspring = createTestPopulation(
                new double[]{60, 55, 45, 35, 25, 15, 5}
            );

            SteadyStateReplacement<TestChromosome> replacement =
                new SteadyStateReplacement<>(2);

            List<TestChromosome> newPopulation =
                replacement.replacePopulation(population, offspring);

            // Should keep best 3 from population (50, 40, 30)
            // Should add best 2 from offspring (60, 55)
            double[] fitnesses = newPopulation.stream()
                .mapToDouble(TestChromosome::evaluate)
                .sorted()
                .toArray();

            assertArrayEquals(
                new double[]{30, 40, 50, 55, 60},
                fitnesses,
                0.001
            );
        }
    }

    @Nested
    @DisplayName("Edge Case Tests")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle empty current population")
        void testEmptyCurrentPopulation() {
            List<TestChromosome> population = new ArrayList<>();
            List<TestChromosome> offspring = createTestPopulation(
                new double[]{10, 9, 8}
            );

            SteadyStateReplacement<TestChromosome> replacement =
                new SteadyStateReplacement<>(1);

            List<TestChromosome> newPopulation =
                replacement.replacePopulation(population, offspring);

            assertTrue(newPopulation.isEmpty());
        }

        @Test
        @DisplayName("Should handle null current population")
        void testNullCurrentPopulation() {
            List<TestChromosome> offspring = createTestPopulation(
                new double[]{10, 9, 8}
            );

            SteadyStateReplacement<TestChromosome> replacement =
                new SteadyStateReplacement<>(1);

            List<TestChromosome> newPopulation =
                replacement.replacePopulation(null, offspring);

            assertTrue(newPopulation.isEmpty());
        }

        @Test
        @DisplayName("Should handle empty offspring")
        void testEmptyOffspring() {
            List<TestChromosome> population = createTestPopulation(
                new double[]{10, 9, 8, 7, 6}
            );
            List<TestChromosome> offspring = new ArrayList<>();

            SteadyStateReplacement<TestChromosome> replacement =
                new SteadyStateReplacement<>(2);

            List<TestChromosome> newPopulation =
                replacement.replacePopulation(population, offspring);

            // Should return unchanged population
            assertEquals(population.size(), newPopulation.size());
        }

        @Test
        @DisplayName("Should handle null offspring")
        void testNullOffspring() {
            List<TestChromosome> population = createTestPopulation(
                new double[]{10, 9, 8, 7, 6}
            );

            SteadyStateReplacement<TestChromosome> replacement =
                new SteadyStateReplacement<>(2);

            List<TestChromosome> newPopulation =
                replacement.replacePopulation(population, null);

            // Should return unchanged population
            assertEquals(population.size(), newPopulation.size());
        }

        @Test
        @DisplayName("Should handle fewer offspring than replacement count")
        void testFewerOffspringThanReplacementCount() {
            List<TestChromosome> population = createTestPopulation(
                new double[]{10, 9, 8, 7, 6, 5}
            );

            // Only 2 offspring, but replacement count is 5
            List<TestChromosome> offspring = createTestPopulation(
                new double[]{9.5, 8.5}
            );

            SteadyStateReplacement<TestChromosome> replacement =
                new SteadyStateReplacement<>(5);

            List<TestChromosome> newPopulation =
                replacement.replacePopulation(population, offspring);

            // Should still have correct population size
            assertEquals(6, newPopulation.size());

            // Should only replace 2 (number of available offspring)
            double[] fitnesses = newPopulation.stream()
                .mapToDouble(TestChromosome::evaluate)
                .sorted()
                .toArray();

            // Should keep best 4 from population (10, 9, 8, 7)
            // Should add 2 offspring (9.5, 8.5)
            assertArrayEquals(
                new double[]{7, 8, 8.5, 9, 9.5, 10},
                fitnesses,
                0.001
            );
        }

        @Test
        @DisplayName("Should throw exception when replacement count >= population size")
        void testReplacementCountTooLarge() {
            List<TestChromosome> population = createTestPopulation(
                new double[]{10, 9, 8, 7, 6}
            );

            List<TestChromosome> offspring = createTestPopulation(
                new double[]{9.5, 8.5, 7.5, 6.5, 5.5, 4.5}
            );

            // Replacement count equals population size
            SteadyStateReplacement<TestChromosome> replacement1 =
                new SteadyStateReplacement<>(5);

            assertThrows(IllegalArgumentException.class, () ->
                replacement1.replacePopulation(population, offspring)
            );

            // Replacement count exceeds population size
            SteadyStateReplacement<TestChromosome> replacement2 =
                new SteadyStateReplacement<>(10);

            assertThrows(IllegalArgumentException.class, () ->
                replacement2.replacePopulation(population, offspring)
            );
        }

        @Test
        @DisplayName("Should handle replacement count of 1 (minimal disruption)")
        void testMinimalReplacement() {
            List<TestChromosome> population = createTestPopulation(
                new double[]{100, 90, 80, 70, 60}
            );

            List<TestChromosome> offspring = createTestPopulation(
                new double[]{95, 85, 75}
            );

            SteadyStateReplacement<TestChromosome> replacement =
                new SteadyStateReplacement<>(1);

            List<TestChromosome> newPopulation =
                replacement.replacePopulation(population, offspring);

            // Should keep 4 best from population, add 1 best from offspring
            double[] fitnesses = newPopulation.stream()
                .mapToDouble(TestChromosome::evaluate)
                .sorted()
                .toArray();

            assertArrayEquals(
                new double[]{70, 80, 90, 95, 100},
                fitnesses,
                0.001
            );
        }
    }

    @Nested
    @DisplayName("Different Chromosome Type Tests")
    class DifferentChromosomeTests {

        @Test
        @DisplayName("Should work with BinaryChromosome")
        void testWithBinaryChromosome() {
            // Create binary chromosomes with predictable fitness
            List<BinaryChromosome> population = Arrays.asList(
                new BinaryChromosome(new int[]{1, 1, 1, 1, 1}), // fitness = 5
                new BinaryChromosome(new int[]{1, 1, 1, 1, 0}), // fitness = 4
                new BinaryChromosome(new int[]{1, 1, 1, 0, 0}), // fitness = 3
                new BinaryChromosome(new int[]{1, 1, 0, 0, 0}), // fitness = 2
                new BinaryChromosome(new int[]{1, 0, 0, 0, 0})  // fitness = 1
            );

            List<BinaryChromosome> offspring = Arrays.asList(
                new BinaryChromosome(new int[]{1, 1, 1, 1, 0}), // fitness = 4
                new BinaryChromosome(new int[]{1, 1, 1, 0, 0})  // fitness = 3
            );

            SteadyStateReplacement<BinaryChromosome> replacement =
                new SteadyStateReplacement<>(2);

            List<BinaryChromosome> newPopulation =
                replacement.replacePopulation(population, offspring);

            assertEquals(5, newPopulation.size());

            // Verify no null references
            for (BinaryChromosome chr : newPopulation) {
                assertNotNull(chr);
            }
        }

        @Test
        @DisplayName("Should work with IntegerChromosome")
        void testWithIntegerChromosome() {
            // Create integer chromosomes with random values
            List<IntegerChromosome> population = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                Integer[] genes = new Integer[5];
                for (int j = 0; j < 5; j++) {
                    genes[j] = (int) (Math.random() * 100);
                }
                population.add(new IntegerChromosome(genes));
            }

            List<IntegerChromosome> offspring = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                Integer[] genes = new Integer[5];
                for (int j = 0; j < 5; j++) {
                    genes[j] = (int) (Math.random() * 100);
                }
                offspring.add(new IntegerChromosome(genes));
            }

            SteadyStateReplacement<IntegerChromosome> replacement =
                new SteadyStateReplacement<>(2);

            List<IntegerChromosome> newPopulation =
                replacement.replacePopulation(population, offspring);

            assertEquals(5, newPopulation.size());

            // Verify no null references
            for (IntegerChromosome chr : newPopulation) {
                assertNotNull(chr);
            }
        }

        @Test
        @DisplayName("Should work with FloatingPointChromosome")
        void testWithFloatingPointChromosome() {
            // Create floating point chromosomes with random values
            List<FloatingPointChromosome> population = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                Double[] genes = new Double[5];
                for (int j = 0; j < 5; j++) {
                    genes[j] = Math.random() * 20.0 - 10.0; // Range [-10.0, 10.0]
                }
                population.add(new FloatingPointChromosome(genes, -10.0, 10.0));
            }

            List<FloatingPointChromosome> offspring = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                Double[] genes = new Double[5];
                for (int j = 0; j < 5; j++) {
                    genes[j] = Math.random() * 20.0 - 10.0; // Range [-10.0, 10.0]
                }
                offspring.add(new FloatingPointChromosome(genes, -10.0, 10.0));
            }

            SteadyStateReplacement<FloatingPointChromosome> replacement =
                new SteadyStateReplacement<>(2);

            List<FloatingPointChromosome> newPopulation =
                replacement.replacePopulation(population, offspring);

            assertEquals(5, newPopulation.size());

            // Verify no null references
            for (FloatingPointChromosome chr : newPopulation) {
                assertNotNull(chr);
            }
        }
    }

    @Nested
    @DisplayName("Fitness Direction Tests (Maximization)")
    class FitnessDirectionTests {

        @Test
        @DisplayName("Should correctly identify worst for maximization")
        void testMaximizationProblem() {
            // In maximization, worst = lowest fitness
            List<TestChromosome> population = createTestPopulation(
                new double[]{100, 80, 60, 40, 20}
            );

            List<TestChromosome> offspring = createTestPopulation(
                new double[]{90, 70, 50}
            );

            SteadyStateReplacement<TestChromosome> replacement =
                new SteadyStateReplacement<>(2);

            List<TestChromosome> newPopulation =
                replacement.replacePopulation(population, offspring);

            // Should replace worst 2 (40, 20) with best 2 offspring (90, 70)
            double[] fitnesses = newPopulation.stream()
                .mapToDouble(TestChromosome::evaluate)
                .sorted()
                .toArray();

            assertArrayEquals(
                new double[]{60, 70, 80, 90, 100},
                fitnesses,
                0.001
            );
        }
    }

    @Nested
    @DisplayName("Getter and ToString Tests")
    class AccessorTests {

        @Test
        @DisplayName("getReplacementCount should return correct value")
        void testGetReplacementCount() {
            SteadyStateReplacement<TestChromosome> replacement =
                new SteadyStateReplacement<>(7);

            assertEquals(7, replacement.getReplacementCount());
        }

        @Test
        @DisplayName("getMode should return correct mode")
        void testGetMode() {
            SteadyStateReplacement<TestChromosome> replacement =
                new SteadyStateReplacement<>(5,
                    SteadyStateReplacement.ReplacementMode.FITNESS);

            assertEquals(SteadyStateReplacement.ReplacementMode.FITNESS,
                        replacement.getMode());
        }

        @Test
        @DisplayName("isAllowParentReplacement should return correct value")
        void testIsAllowParentReplacement() {
            SteadyStateReplacement<TestChromosome> replacement1 =
                new SteadyStateReplacement<>(5,
                    SteadyStateReplacement.ReplacementMode.FITNESS, true);

            assertTrue(replacement1.isAllowParentReplacement());

            SteadyStateReplacement<TestChromosome> replacement2 =
                new SteadyStateReplacement<>(5,
                    SteadyStateReplacement.ReplacementMode.FITNESS, false);

            assertFalse(replacement2.isAllowParentReplacement());
        }

        @Test
        @DisplayName("toString should provide meaningful description")
        void testToString() {
            SteadyStateReplacement<TestChromosome> replacement =
                new SteadyStateReplacement<>(10,
                    SteadyStateReplacement.ReplacementMode.FITNESS, true);

            String description = replacement.toString();

            assertTrue(description.contains("10"));
            assertTrue(description.contains("FITNESS"));
            assertTrue(description.contains("true"));
        }
    }

    // Helper method to create test population with specific fitness values
    private List<TestChromosome> createTestPopulation(double[] fitnessValues) {
        List<TestChromosome> population = new ArrayList<>();
        for (double fitness : fitnessValues) {
            population.add(new TestChromosome(fitness));
        }
        return population;
    }

    /**
     * Simple test chromosome with controllable fitness for testing purposes.
     */
    private static class TestChromosome implements Chromosome<Double> {
        private final double fitness;

        public TestChromosome(double fitness) {
            this.fitness = fitness;
        }

        @Override
        public Double[] toArray() {
            return new Double[]{fitness};
        }

        @Override
        public int length() {
            return 1;
        }

        @Override
        public Double getGene(int index) {
            return fitness;
        }

        @Override
        public void setGene(int index, Double value) {
            // Not needed for tests
        }

        @Override
        public double evaluate() {
            return fitness;
        }
    }
}

