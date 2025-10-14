package com.example.softcomputing.genetic.operators.mutation;
import com.example.softcomputing.genetic.chromosome.Chromosome;

public interface MutationStrategy<C extends Chromosome<?>> {
	C mutate(C individual);
}
