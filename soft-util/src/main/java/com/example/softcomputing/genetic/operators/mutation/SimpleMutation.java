package com.example.soft_util.impl;

import com.example.soft_util.interfaces.MutationStrategy;

public class SimpleMutation<T> implements MutationStrategy<T> {
    @Override
    public T mutate(T individual) {
        // stub: no mutation
        return individual;
    }
}
