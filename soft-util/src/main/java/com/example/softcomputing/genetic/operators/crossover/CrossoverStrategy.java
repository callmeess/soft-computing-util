package com.example.soft_util.interfaces;

import java.util.List;

public interface CrossoverStrategy<T> {
	List<T> crossover(T parent1, T parent2);
}
