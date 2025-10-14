package com.example.softcomputing.genetic.operators.replacement;

import java.util.List;
import com.example.softcomputing.genetic.chromosome.Chromosome;

public interface Replacement<C extends Chromosome<?>> {
  List<C> replacePopulation(List<C> currentPopulation, List<C> newIndividuals);
}