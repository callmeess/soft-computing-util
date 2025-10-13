package com.example.softcomputing.genetic.operators.crossover;
import java.util.List;

public interface CrossoverStrategy<Chromosome> {
	List<Chromosome> crossover(Chromosome parent1, Chromosome parent2);
}
