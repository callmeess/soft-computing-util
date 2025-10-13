package com.example.softcomputing.genetic.operators.mutation;

import com.example.softcomputing.genetic.chromosome.Chromosome;
import com.example.softcomputing.genetic.chromosome.IntegerChromosome;

public class IntegerMutation implements MutationStrategy<Integer,IntegerChromosome > {

    @Override
    public Chromosome<Integer> mutate(Chromosome<Integer> individual, Double mutationRate) {

            if (Math.random() < mutationRate) {
            int mutationValue = (int) (Math.random() * (10 - 0 + 1)) + 0; //  between 0 and 10
            int mutationSign = Math.random() < 0.5 ? -1 : 1;
            mutationValue *= mutationSign;

            int mutationIndex = (int) (Math.random() * individual.length());
            individual.setGene(mutationIndex, mutationValue);

            return individual;
        }
        return individual;
    }
}
