package com.example.softcomputing.genetic.operators.mutation;
import com.example.softcomputing.genetic.chromosome.Chromosome;

public interface MutationStrategy<G , C extends Chromosome<G> > {
	C mutate(C individual , Double mutationRate);
}
