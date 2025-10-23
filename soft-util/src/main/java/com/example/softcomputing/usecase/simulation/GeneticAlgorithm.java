package com.example.softcomputing.usecase.simulation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.OptionalDouble;
import java.util.Random;

import com.example.softcomputing.genetic.chromosome.FloatingPointChromosome;
import com.example.softcomputing.genetic.chromosome.Factories.FloatingPointChromosomeFactory;
import com.example.softcomputing.genetic.operators.crossover.CrossoverStrategy;
import com.example.softcomputing.genetic.operators.mutation.MutationStrategy;
import com.example.softcomputing.genetic.operators.replacement.Replacement;
import com.example.softcomputing.genetic.operators.selection.SelectionStrategy;
import com.example.softcomputing.neuralnetwork.core.NeuralNetwork;
import com.example.softcomputing.usecase.simulation.entity.Car;

public class GeneticAlgorithm {
    private final int populationSize;
    private final boolean[][] trackGrid;

    private final CrossoverStrategy<FloatingPointChromosome> crossoverStrategy;
    private final MutationStrategy<FloatingPointChromosome> mutationStrategy;
    private final SelectionStrategy<FloatingPointChromosome> selectionStrategy;
    private final Replacement<FloatingPointChromosome> replacementStrategy;
    private final FloatingPointChromosomeFactory chromosomeFactory;

    private List<Car> population;
    private int generation;
    private double bestFitness;
    private Car bestCar;
    private double avgFitness;
    private int aliveCars;
    private long generationStartTime;
    private final long GENERATION_TIMEOUT_MS = 30_000; // 30 seconds

    private final Random random = new Random();

    public GeneticAlgorithm(
            int populationSize,
            boolean[][] trackGrid,
            CrossoverStrategy<FloatingPointChromosome> crossoverStrategy,
            MutationStrategy<FloatingPointChromosome> mutationStrategy,
            SelectionStrategy<FloatingPointChromosome> selectionStrategy,
            Replacement<FloatingPointChromosome> replacementStrategy) {
        this.populationSize = populationSize;
        this.trackGrid = trackGrid;
        this.crossoverStrategy = crossoverStrategy;
        this.mutationStrategy = mutationStrategy;
        this.selectionStrategy = selectionStrategy;
        this.replacementStrategy = replacementStrategy;
        this.chromosomeFactory = new FloatingPointChromosomeFactory(-1.0, 1.0);
        this.population = new ArrayList<>();
        this.generation = 0;
    }

    public void initializePopulation() {
        population.clear();
        double[] startPos = { 150, 700 };

        for (int i = 0; i < populationSize; i++) {
            NeuralNetwork nn = new NeuralNetwork(5, 8, 1);
            double startAngle = random.nextDouble() * Math.PI * 2;

            Car car = new Car(startPos[0], startPos[1], startAngle, trackGrid, nn);
            population.add(car);
        }
        generationStartTime = System.currentTimeMillis();
        System.out.println("Initialized population with " + populationSize + " cars at position (" +
                (int) startPos[0] + ", " + (int) startPos[1] + ")");
    }

    public void updatePopulation() {
        aliveCars = 0;
        Car currentBest = null;
        double maxFitness = Double.NEGATIVE_INFINITY;

        for (Car car : population) {
            car.update();
            if (car.isAlive())
                aliveCars++;

            if (car.getFitness() > maxFitness) {
                maxFitness = car.getFitness();
                currentBest = car;
            }
        }

        // Store best car
        bestCar = currentBest;
        bestFitness = maxFitness;
    }

    /**
     * When all cars are dead evolve.
     */
    public boolean shouldEvolve() {
        long elapsed = System.currentTimeMillis() - generationStartTime;
        boolean timeoutReached = elapsed >= GENERATION_TIMEOUT_MS;
        boolean allDead = aliveCars == 0;

        return allDead || timeoutReached;
    }

    public void evolveGeneration() {
        long evolveStartTime = System.currentTimeMillis();
        long generationDuration = evolveStartTime - generationStartTime;
        generation++;

        List<FloatingPointChromosome> parents = new ArrayList<>(populationSize);
        for (int i = 0; i < population.size(); i++) {
            Car car = population.get(i);
            NeuralNetwork nn = car.getNeuralNetwork();
            double[] weights = nn.flatten();
            Double[] boxed = Arrays.stream(weights).boxed().toArray(Double[]::new);
            FloatingPointChromosome chrom = chromosomeFactory.create(boxed);
            chrom.setFitness(car.getFitness());
            parents.add(chrom);
        }

        OptionalDouble max = parents.stream().mapToDouble(FloatingPointChromosome::getFitness).max();
        bestFitness = max.orElse(0);

        List<FloatingPointChromosome> newChromosomes = new ArrayList<>(populationSize);
        int operationNum = 0;
        while (newChromosomes.size() < populationSize) {
            FloatingPointChromosome parent1 = selectionStrategy.selectIndividual(parents);
            FloatingPointChromosome parent2 = selectionStrategy.selectIndividual(parents);

            int p1Index = parents.indexOf(parent1);
            int p2Index = parents.indexOf(parent2);

            if (operationNum < 5) {
                System.out.println(String.format("Operation %d:", operationNum));
                System.out.println(String.format("  Selected parent1 (Car %d): fitness=%.2f",
                        p1Index, parent1.getFitness()));
                System.out.println(String.format("  Selected parent2 (Car %d): fitness=%.2f",
                        p2Index, parent2.getFitness()));
            }

            List<FloatingPointChromosome> children = crossoverStrategy.crossover(parent1, parent2);

            for (int childIdx = 0; childIdx < children.size(); childIdx++) {
                FloatingPointChromosome child = children.get(childIdx);
                FloatingPointChromosome mutated = mutationStrategy.mutate(child);
                mutated.setFitness(0.0);
                newChromosomes.add(mutated);
                if (newChromosomes.size() >= populationSize)
                    break;
            }
            operationNum++;
        }

        List<FloatingPointChromosome> nextGenChromosomes = replacementStrategy.replacePopulation(parents,
                newChromosomes);
        List<Car> newPopulation = new ArrayList<>(populationSize);
        double[] startPos = { 150, 700 };

        for (int i = 0; i < nextGenChromosomes.size(); i++) {
            FloatingPointChromosome chrom = nextGenChromosomes.get(i);
            NeuralNetwork nn = new NeuralNetwork(5, 8, 1);
            double[] genes = Arrays.stream(chrom.toArray()).mapToDouble(Double::doubleValue).toArray();
            nn.setWeights(genes);

            double offsetX = (random.nextDouble() - 0.5) * 20;
            double offsetY = (random.nextDouble() - 0.5) * 20;
            double startAngle = random.nextDouble() * Math.PI * 2;

            Car car = new Car(startPos[0] + offsetX, startPos[1] + offsetY, startAngle, trackGrid, nn);
            newPopulation.add(car);

        }

        this.population = newPopulation;

        long evolveEndTime = System.currentTimeMillis();
        generationStartTime = System.currentTimeMillis();
    }

    // Getters
    public int getAliveCars() {
        return aliveCars;
    }

    public double getBestFitness() {
        return bestFitness;
    }

    public double getAvgFitness() {
        return avgFitness;
    }

    public int getGeneration() {
        return generation;
    }

    public List<Car> getPopulation() {
        return population;
    }

    public Car getBestCar() {
        return bestCar;
    }

    public boolean[][] getTrackGrid() {
        return trackGrid;
    }
}