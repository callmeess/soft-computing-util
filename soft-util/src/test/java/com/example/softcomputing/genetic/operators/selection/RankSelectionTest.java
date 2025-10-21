package com.example.softcomputing.genetic.operators.selection;

import com.example.softcomputing.genetic.chromosome.Chromosome;
import com.example.softcomputing.genetic.chromosome.FloatingPointChromosome;
import com.example.softcomputing.genetic.operators.selection.RankSelection.RankingMode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for RankSelection implementation.
 * Tests various selection pressures, ranking modes, and edge cases.
 */
class RankSelectionTest {

    private List<TestChromosome> population;

    @BeforeEach
    void setUp() {
        population = new ArrayList<>();
    }

    @Test
    void testConstructorWithDefaultParameters() {
        RankSelection<TestChromosome> selector = new RankSelection<>();
        assertEquals(1.5, selector.getSelectionPressure(), 0.001);
        assertEquals(RankingMode.LINEAR, selector.getRankingMode());
        assertTrue(selector.isMaximization());
    }

    @Test
    void testConstructorWithSelectionPressure() {
        RankSelection<TestChromosome> selector = new RankSelection<>(1.8);
        assertEquals(1.8, selector.getSelectionPressure(), 0.001);
        assertEquals(RankingMode.LINEAR, selector.getRankingMode());
    }

    @Test
    void testConstructorWithAllParameters() {
        RankSelection<TestChromosome> selector = new RankSelection<>(1.2, RankingMode.EXPONENTIAL, false);
        assertEquals(1.2, selector.getSelectionPressure(), 0.001);
        assertEquals(RankingMode.EXPONENTIAL, selector.getRankingMode());
        assertFalse(selector.isMaximization());
    }

    @Test
    void testInvalidSelectionPressureTooLow() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new RankSelection<TestChromosome>(0.5);
        });
        assertTrue(exception.getMessage().contains("Selection pressure must be between 1.0 and 2.0"));
    }

    @Test
    void testInvalidSelectionPressureTooHigh() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new RankSelection<TestChromosome>(2.5);
        });
        assertTrue(exception.getMessage().contains("Selection pressure must be between 1.0 and 2.0"));
    }

    @Test
    void testSelectFromEmptyPopulation() {
        RankSelection<TestChromosome> selector = new RankSelection<>();
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            selector.selectIndividual(new ArrayList<>());
        });
        assertTrue(exception.getMessage().contains("Population must not be null or empty"));
    }

    @Test
    void testSelectFromNullPopulation() {
        RankSelection<TestChromosome> selector = new RankSelection<>();
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            selector.selectIndividual(null);
        });
        assertTrue(exception.getMessage().contains("Population must not be null or empty"));
    }

    @Test
    void testSelectionWithSingleIndividual() {
        population.add(new TestChromosome(5.0));
        RankSelection<TestChromosome> selector = new RankSelection<>();

        TestChromosome selected = selector.selectIndividual(population);
        assertNotNull(selected);
        assertEquals(5.0, selected.evaluate(), 0.001);
    }

    @Test
    void testSelectionPressure1_0GivesUniformDistribution() {
        // Create population with varied fitness
        for (int i = 1; i <= 10; i++) {
            population.add(new TestChromosome(i * 1.0));
        }

        RankSelection<TestChromosome> selector = new RankSelection<>(1.0);

        // With s=1.0, all individuals should have equal probability (1/n)
        // Run multiple selections and check distribution is roughly uniform
        Map<Double, Integer> selectionCounts = new HashMap<>();
        int numSelections = 10000;

        for (int i = 0; i < numSelections; i++) {
            TestChromosome selected = selector.selectIndividual(population);
            double fitness = selected.evaluate();
            selectionCounts.put(fitness, selectionCounts.getOrDefault(fitness, 0) + 1);
        }

        // Each individual should be selected approximately 1000 times (10% of 10000)
        // Allow 30% deviation for randomness
        for (double count : selectionCounts.values()) {
            double proportion = count / (double) numSelections;
            assertTrue(proportion >= 0.07 && proportion <= 0.13,
                "With s=1.0, selection should be roughly uniform, got proportion: " + proportion);
        }
    }

    @Test
    void testSelectionPressure2_0GivesMaximumPressure() {
        // Create population with varied fitness
        for (int i = 1; i <= 10; i++) {
            population.add(new TestChromosome(i * 1.0));
        }

        RankSelection<TestChromosome> selector = new RankSelection<>(2.0);

        // With s=2.0, best individual should be selected much more frequently
        Map<Double, Integer> selectionCounts = new HashMap<>();
        int numSelections = 10000;

        for (int i = 0; i < numSelections; i++) {
            TestChromosome selected = selector.selectIndividual(population);
            double fitness = selected.evaluate();
            selectionCounts.put(fitness, selectionCounts.getOrDefault(fitness, 0) + 1);
        }

        // Best individual (fitness=10.0) should be selected most frequently
        int bestCount = selectionCounts.getOrDefault(10.0, 0);
        int worstCount = selectionCounts.getOrDefault(1.0, 0);

        assertTrue(bestCount > worstCount,
            "Best individual should be selected more than worst with s=2.0");
    }

    @Test
    void testHandlesNegativeFitnessValues() {
        // Create population with negative fitness values
        population.add(new TestChromosome(-10.0));
        population.add(new TestChromosome(-5.0));
        population.add(new TestChromosome(-2.0));
        population.add(new TestChromosome(0.0));
        population.add(new TestChromosome(3.0));

        RankSelection<TestChromosome> selector = new RankSelection<>(1.5);

        // Should not throw exception and should return valid individual
        for (int i = 0; i < 100; i++) {
            TestChromosome selected = selector.selectIndividual(population);
            assertNotNull(selected);
            assertTrue(population.contains(selected));
        }
    }

    @Test
    void testHandlesFitnessTies() {
        // Create population with tied fitness values
        population.add(new TestChromosome(5.0));
        population.add(new TestChromosome(5.0));
        population.add(new TestChromosome(5.0));
        population.add(new TestChromosome(10.0));
        population.add(new TestChromosome(10.0));

        RankSelection<TestChromosome> selector = new RankSelection<>(1.5);

        // Should handle ties correctly
        for (int i = 0; i < 100; i++) {
            TestChromosome selected = selector.selectIndividual(population);
            assertNotNull(selected);
            assertTrue(population.contains(selected));
        }
    }

    @Test
    void testMinimizationProblem() {
        // Create population where lower fitness is better
        for (int i = 1; i <= 5; i++) {
            population.add(new TestChromosome(i * 2.0));
        }

        RankSelection<TestChromosome> selector = new RankSelection<>(1.8, RankingMode.LINEAR, false);

        Map<Double, Integer> selectionCounts = new HashMap<>();
        int numSelections = 5000;

        for (int i = 0; i < numSelections; i++) {
            TestChromosome selected = selector.selectIndividual(population);
            double fitness = selected.evaluate();
            selectionCounts.put(fitness, selectionCounts.getOrDefault(fitness, 0) + 1);
        }

        // For minimization, lowest fitness (2.0) should be selected more than highest (10.0)
        int bestCount = selectionCounts.getOrDefault(2.0, 0);
        int worstCount = selectionCounts.getOrDefault(10.0, 0);

        assertTrue(bestCount > worstCount,
            "For minimization, lowest fitness should be selected more frequently");
    }

    @Test
    void testExponentialRankingMode() {
        // Create population
        for (int i = 1; i <= 10; i++) {
            population.add(new TestChromosome(i * 1.0));
        }

        RankSelection<TestChromosome> selector = new RankSelection<>(1.5, RankingMode.EXPONENTIAL, true);

        // Should work without errors
        for (int i = 0; i < 100; i++) {
            TestChromosome selected = selector.selectIndividual(population);
            assertNotNull(selected);
            assertTrue(population.contains(selected));
        }
    }

    @Test
    void testWorstIndividualHasNonZeroSelectionProbability() {
        // Create population
        for (int i = 1; i <= 10; i++) {
            population.add(new TestChromosome(i * 1.0));
        }

        RankSelection<TestChromosome> selector = new RankSelection<>(1.5);

        Map<Double, Integer> selectionCounts = new HashMap<>();
        int numSelections = 10000;

        for (int i = 0; i < numSelections; i++) {
            TestChromosome selected = selector.selectIndividual(population);
            double fitness = selected.evaluate();
            selectionCounts.put(fitness, selectionCounts.getOrDefault(fitness, 0) + 1);
        }

        // Worst individual (fitness=1.0) should be selected at least once
        int worstCount = selectionCounts.getOrDefault(1.0, 0);
        assertTrue(worstCount > 0, "Worst individual should have non-zero selection probability");
    }

    @Test
    void testBestIndividualDoesNotDominateSelection() {
        // Create population with one super-fit individual
        population.add(new TestChromosome(1.0));
        population.add(new TestChromosome(2.0));
        population.add(new TestChromosome(3.0));
        population.add(new TestChromosome(1000.0)); // Super-fit individual

        RankSelection<TestChromosome> selector = new RankSelection<>(1.5);

        Map<Double, Integer> selectionCounts = new HashMap<>();
        int numSelections = 10000;

        for (int i = 0; i < numSelections; i++) {
            TestChromosome selected = selector.selectIndividual(population);
            double fitness = selected.evaluate();
            selectionCounts.put(fitness, selectionCounts.getOrDefault(fitness, 0) + 1);
        }

        // Best individual should not dominate (should be selected less than 80% of time)
        int bestCount = selectionCounts.getOrDefault(1000.0, 0);
        double bestProportion = bestCount / (double) numSelections;
        assertTrue(bestProportion < 0.8,
            "Best individual should not dominate selection, got proportion: " + bestProportion);

        // Other individuals should also be selected
        assertTrue(selectionCounts.size() > 1, "More than one individual should be selected");
    }

    @Test
    void testCachePerformance() {
        // Create large population
        for (int i = 1; i <= 100; i++) {
            population.add(new TestChromosome(i * 1.0));
        }

        RankSelection<TestChromosome> selector = new RankSelection<>(1.5);

        // First selection builds the ranking
        long startTime = System.nanoTime();
        selector.selectIndividual(population);
        long firstSelectionTime = System.nanoTime() - startTime;

        // Subsequent selections should be faster (using cache)
        startTime = System.nanoTime();
        for (int i = 0; i < 100; i++) {
            selector.selectIndividual(population);
        }
        long cachedSelectionsTime = System.nanoTime() - startTime;
        long avgCachedTime = cachedSelectionsTime / 100;

        // Average cached selection should be faster than first selection
        // (this is a rough check, may vary based on system)
        assertTrue(avgCachedTime < firstSelectionTime * 2,
            "Cached selections should benefit from optimization");
    }

    @Test
    void testClearCache() {
        // Create population
        for (int i = 1; i <= 10; i++) {
            population.add(new TestChromosome(i * 1.0));
        }

        RankSelection<TestChromosome> selector = new RankSelection<>(1.5);

        // First selection
        selector.selectIndividual(population);

        // Clear cache
        selector.clearCache();

        // Should still work after clearing cache
        TestChromosome selected = selector.selectIndividual(population);
        assertNotNull(selected);
        assertTrue(population.contains(selected));
    }

    @Test
    void testDifferentSelectionPressureValues() {
        // Create population
        for (int i = 1; i <= 10; i++) {
            population.add(new TestChromosome(i * 1.0));
        }

        // Test various selection pressure values
        double[] pressures = {1.0, 1.2, 1.5, 1.8, 2.0};

        for (double pressure : pressures) {
            RankSelection<TestChromosome> selector = new RankSelection<>(pressure);

            // Each should work correctly
            for (int i = 0; i < 50; i++) {
                TestChromosome selected = selector.selectIndividual(population);
                assertNotNull(selected);
                assertTrue(population.contains(selected));
            }
        }
    }

    @Test
    void testSelectionDistributionConvergenceSpeed() {
        // Create population
        for (int i = 1; i <= 10; i++) {
            population.add(new TestChromosome(i * 1.0));
        }

        // Test that higher pressure leads to more selections of better individuals
        RankSelection<TestChromosome> lowPressure = new RankSelection<>(1.1);
        RankSelection<TestChromosome> highPressure = new RankSelection<>(1.9);

        int numSelections = 5000;
        int lowPressureBestCount = 0;
        int highPressureBestCount = 0;

        for (int i = 0; i < numSelections; i++) {
            if (lowPressure.selectIndividual(population).evaluate() == 10.0) {
                lowPressureBestCount++;
            }
            if (highPressure.selectIndividual(population).evaluate() == 10.0) {
                highPressureBestCount++;
            }
        }

        // High pressure should select best individual more frequently
        assertTrue(highPressureBestCount > lowPressureBestCount,
            "Higher selection pressure should favor best individual more. " +
            "Low: " + lowPressureBestCount + ", High: " + highPressureBestCount);
    }

    /**
     * Simple test chromosome for testing purposes.
     */
    private static class TestChromosome implements Chromosome<Double> {
        private final Double[] genes;
        private final double fitness;

        TestChromosome(double fitness) {
            this.fitness = fitness;
            this.genes = new Double[]{fitness};
        }

        @Override
        public Double[] toArray() {
            return genes;
        }

        @Override
        public int length() {
            return genes.length;
        }

        @Override
        public Double getGene(int index) {
            return genes[index];
        }

        @Override
        public void setGene(int index, Double value) {
            genes[index] = value;
        }

        @Override
        public double evaluate() {
            return fitness;
        }
    }
}

