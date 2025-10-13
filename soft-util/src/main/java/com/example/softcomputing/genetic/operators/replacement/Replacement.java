package com.example.softcomputing.genetic.operators.replacement;

public interface Replacement<T> {
	boolean shouldTerminate(int generation, T best);
}
