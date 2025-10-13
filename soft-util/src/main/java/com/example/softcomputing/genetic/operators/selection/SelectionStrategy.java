package com.example.softcomputing.genetic.operators.selection;

import java.util.List;

public interface SelectionStrategy {
	<T> T selectIndividual(List<T> population);
}
