package com.example.softcomputing.genetic.operators.selection;

import java.util.List;

import com.example.softcomputing.genetic.chromosome.Chromosome;

public interface SelectionStrategy <C extends Chromosome<?>> {
	 C selectIndividual(List<C> population);
}
