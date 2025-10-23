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
        double[] startPos = {150,700};

        for (int i = 0; i < populationSize; i++) {
            NeuralNetwork nn = new NeuralNetwork(5, 8, 1);
            double startAngle = random.nextDouble() * Math.PI * 2;

            Car car = new Car(startPos[0], startPos[1], startAngle, trackGrid, nn);
            population.add(car);
        }
        generationStartTime = System.currentTimeMillis();
        System.out.println("Initialized population with " + populationSize + " cars at position (" +
                (int)startPos[0] + ", " + (int)startPos[1] + ")");
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
        System.out.println("\n" + "=".repeat(80));
        System.out.println("EVOLUTION DEBUG - Generation " + (generation + 1));
        System.out.println("=".repeat(80));
        long evolveStartTime = System.currentTimeMillis();
        long generationDuration = evolveStartTime - generationStartTime;

        generation++;

        // Step 1: Convert cars to chromosomes and print each car's details
        System.out.println("\n[1] CURRENT GENERATION CARS:");
        System.out.println("-".repeat(80));
        List<FloatingPointChromosome> parents = new ArrayList<>(populationSize);

        for (int i = 0; i < population.size(); i++) {
            Car car = population.get(i);
            NeuralNetwork nn = car.getNeuralNetwork();
            double[] weights = nn.flatten();
            Double[] boxed = Arrays.stream(weights).boxed().toArray(Double[]::new);
            FloatingPointChromosome chrom = chromosomeFactory.create(boxed);
            chrom.setFitness(car.getFitness());
            parents.add(chrom);

            System.out.println(String.format("Car %2d: fitness=%7.2f | dist_to_finish=%7.2f | alive=%s | weights=%d",
                    i, car.getFitness(), car.getClosestDistanceToFinish(),
                    car.isAlive() ? "Y" : "N", weights.length));

            // Print first 10 weights for inspection
            System.out.print("        Weights[0-9]: ");
            for (int w = 0; w < Math.min(10, weights.length); w++) {
                System.out.print(String.format("%.3f ", weights[w]));
            }
            System.out.println("...");
        }

        // Step 2: Calculate fitness statistics
        System.out.println("\n[2] FITNESS STATISTICS:");
        System.out.println("-".repeat(80));
        OptionalDouble max = parents.stream().mapToDouble(FloatingPointChromosome::getFitness).max();
        OptionalDouble avg = parents.stream().mapToDouble(FloatingPointChromosome::getFitness).average();
        OptionalDouble min = parents.stream().mapToDouble(FloatingPointChromosome::getFitness).min();
        bestFitness = max.orElse(0);
        avgFitness = avg.orElse(0);
        double minFitness = min.orElse(0);

        System.out.println(String.format("Best fitness:  %7.2f", bestFitness));
        System.out.println(String.format("Avg fitness:   %7.2f", avgFitness));
        System.out.println(String.format("Min fitness:   %7.2f", minFitness));
        System.out.println(String.format("Generation duration: %dms", generationDuration));

        // Step 3: Create offspring through selection, crossover, and mutation
        System.out.println("\n[3] SELECTION, CROSSOVER & MUTATION:");
        System.out.println("-".repeat(80));
        List<FloatingPointChromosome> newChromosomes = new ArrayList<>(populationSize);
        int operationNum = 0;

        while (newChromosomes.size() < populationSize) {
            FloatingPointChromosome parent1 = selectionStrategy.selectIndividual(parents);
            FloatingPointChromosome parent2 = selectionStrategy.selectIndividual(parents);

            // Find parent indices for debugging
            int p1Index = parents.indexOf(parent1);
            int p2Index = parents.indexOf(parent2);

            if (operationNum < 5) { // Print first 5 operations
                System.out.println(String.format("Operation %d:", operationNum));
                System.out.println(String.format("  Selected parent1 (Car %d): fitness=%.2f",
                        p1Index, parent1.getFitness()));
                System.out.println(String.format("  Selected parent2 (Car %d): fitness=%.2f",
                        p2Index, parent2.getFitness()));
            }

            List<FloatingPointChromosome> children = crossoverStrategy.crossover(parent1, parent2);

            for (int childIdx = 0; childIdx < children.size(); childIdx++) {
                FloatingPointChromosome child = children.get(childIdx);

                if (operationNum < 5) {
                    double[] childWeights = Arrays.stream(child.toArray())
                            .mapToDouble(Double::doubleValue).toArray();
                    System.out.print(String.format("  Child %d weights[0-4]: ", childIdx));
                    for (int w = 0; w < Math.min(5, childWeights.length); w++) {
                        System.out.print(String.format("%.3f ", childWeights[w]));
                    }
                    System.out.println("...");
                }

                FloatingPointChromosome mutated = mutationStrategy.mutate(child);

                if (operationNum < 5) {
                    double[] mutatedWeights = Arrays.stream(mutated.toArray())
                            .mapToDouble(Double::doubleValue).toArray();
                    System.out.print(String.format("  Mutated weights[0-4]: "));
                    for (int w = 0; w < Math.min(5, mutatedWeights.length); w++) {
                        System.out.print(String.format("%.3f ", mutatedWeights[w]));
                    }
                    System.out.println("...");
                }

                mutated.setFitness(0.0);
                newChromosomes.add(mutated);
                if (newChromosomes.size() >= populationSize)
                    break;
            }
            operationNum++;
        }

        if (operationNum > 5) {
            System.out.println(String.format("... %d more operations (not shown)", operationNum - 5));
        }
        System.out.println(String.format("Total operations: %d", operationNum));
        System.out.println(String.format("New chromosomes created: %d", newChromosomes.size()));

        // Step 4: Apply replacement strategy
        System.out.println("\n[4] REPLACEMENT STRATEGY:");
        System.out.println("-".repeat(80));
        System.out.println(String.format("Parent population size: %d", parents.size()));
        System.out.println(String.format("Offspring population size: %d", newChromosomes.size()));

        List<FloatingPointChromosome> nextGenChromosomes = replacementStrategy.replacePopulation(parents,
                newChromosomes);

        System.out.println(String.format("Next generation size: %d", nextGenChromosomes.size()));

        // Step 5: Create new car population
        System.out.println("\n[5] CREATING NEW CAR POPULATION:");
        System.out.println("-".repeat(80));
        List<Car> newPopulation = new ArrayList<>(populationSize);
        double[] startPos = {150,700};

        for (int i = 0; i < nextGenChromosomes.size(); i++) {
            FloatingPointChromosome chrom = nextGenChromosomes.get(i);
            NeuralNetwork nn = new NeuralNetwork(5, 8, 1);
            double[] genes = Arrays.stream(chrom.toArray()).mapToDouble(Double::doubleValue).toArray();
            nn.setWeights(genes);

            // Add small random offset for each car
            double offsetX = (random.nextDouble() - 0.5) * 20;
            double offsetY = (random.nextDouble() - 0.5) * 20;
            double startAngle = random.nextDouble() * Math.PI * 2;

            Car car = new Car(startPos[0] + offsetX, startPos[1] + offsetY, startAngle, trackGrid, nn);
            newPopulation.add(car);

            if (i < 5) {
                System.out.println(String.format("New car %d: pos=(%.1f, %.1f) | angle=%.2f | weights[0]=%.3f",
                        i, startPos[0] + offsetX, startPos[1] + offsetY, startAngle, genes[0]));
            }
        }

        if (nextGenChromosomes.size() > 5) {
            System.out.println(String.format("... %d more cars created", nextGenChromosomes.size() - 5));
        }

        this.population = newPopulation;

        long evolveEndTime = System.currentTimeMillis();
        System.out.println("\n[6] EVOLUTION SUMMARY:");
        System.out.println("-".repeat(80));
        System.out.println(String.format("New population size: %d", newPopulation.size()));
        System.out.println(String.format("Evolution time: %dms", (evolveEndTime - evolveStartTime)));
        System.out.println("=".repeat(80));
        System.out.println(String.format("Generation %d complete - Best: %.2f | Avg: %.2f\n",
                generation, bestFitness, avgFitness));

        // Reset generation timer
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