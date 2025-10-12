package com.example.soft_util.interfaces;

import java.util.List;

public interface SelectionStrategy {
	<T> T select(List<T> population);
}
