package com.example.softcomputing.genetic.operators.selection;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.softcomputing.genetic.chromosome.Chromosome;

public class RouletteWheelSelection implements SelectionStrategy<Chromosome<?>> {

    private Map<Chromosome<?>, Pair<Double, Double>> wheel;

    public void createWheel(List<Chromosome<?>> population) {
        if (population == null || population.isEmpty()) {
            throw new IllegalArgumentException("Population must not be null or empty");
        }
        wheel = new HashMap<>();
        Double lowerBound = 0.0, upperBound = 0.0;
        Double currentFitness = 0.0, totalFitness = 0.0;

        Map<Chromosome<?>, Double> fitnessMap = new HashMap<>();
        wheel = new HashMap<>();

        for (Chromosome<?> individual : population) {
            currentFitness = individual.getFitness();
            totalFitness += currentFitness;
            fitnessMap.put(individual, currentFitness);
        }

        for (Map.Entry<Chromosome<?>, Double> entry : fitnessMap.entrySet()) {
            Chromosome<?> individual = entry.getKey();
            Double fitness = entry.getValue();

            upperBound = lowerBound + (fitness / totalFitness);
            lowerBound = upperBound;
            wheel.put(individual, new Pair<>(lowerBound, upperBound));
        }
    }

    @Override
    public Chromosome<?> selectIndividual(List<Chromosome<?>> population) {

        if (wheel == null) {
            createWheel(population);
        }
        Double randomValue = Math.random();
        for (Map.Entry<Chromosome<?>, Pair<Double, Double>> entry : wheel.entrySet()) {
            Pair<Double, Double> bounds = entry.getValue();
            if (randomValue >= bounds.getFirst() && randomValue < bounds.getSecond()) {
                return entry.getKey();
            }
        }
        return null;
    }
}
