package com.example.soft_util.interfaces;

public interface MutationStrategy<T> {
	T mutate(T individual);
}
