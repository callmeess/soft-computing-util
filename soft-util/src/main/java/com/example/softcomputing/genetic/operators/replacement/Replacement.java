package com.example.soft_util.interfaces;

public interface Replacement<T> {
	boolean shouldTerminate(int generation, T best);
}
