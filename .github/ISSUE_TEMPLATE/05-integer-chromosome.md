---
name: Implement IntegerChromosome
about: Create integer vector chromosome representation
title: 'Implement IntegerChromosome'
labels: implementation, algorithm, phase1, chromosome
assignees: ''
milestone: Phase 1
---

## Summary

Implement an `IntegerChromosome` class that encodes solutions as vectors of integer values. This chromosome type is suitable for combinatorial optimization problems, permutation problems, and integer programming.

## Description

The IntegerChromosome represents solutions as fixed-length arrays of integer values. It must support:

- Encoding solutions with configurable bounds per gene
- Random initialization within specified ranges
- Efficient storage and access to integer genes
- Integration with integer-specific crossover operators
- Integration with integer-specific mutation operators
- Validation of bounds and constraints
- Support for both bounded and unbounded integers

This implementation enables solving discrete optimization problems like scheduling, routing, and resource allocation.

## Tasks

- [ ] Create `IntegerChromosome` class
- [ ] Add constructor accepting gene array and bounds
- [ ] Implement static factory method `random(int length, int min, int max)`
- [ ] Add constructor supporting per-gene bounds
- [ ] Implement `getGene(int index)` method
- [ ] Implement `getGenes()` to return all values
- [ ] Add `getLength()` method
- [ ] Implement `setGene(int index, int value)` with bounds checking
- [ ] Add `getMinBound(int index)` and `getMaxBound(int index)` methods
- [ ] Implement validation for bounds constraints
- [ ] Add `clone()` or copy constructor
- [ ] Override `equals()` and `hashCode()`
- [ ] Override `toString()` for debugging
- [ ] Add utility methods for common operations (sum, average)
- [ ] Support uniform bounds (all genes same range) and per-gene bounds
- [ ] Add method to check if chromosome satisfies all constraints

## Acceptance Criteria

- [ ] IntegerChromosome can be created with specified length and bounds
- [ ] Random initialization respects min/max bounds
- [ ] All genes can be accessed and modified correctly
- [ ] Bounds validation prevents invalid gene values
- [ ] Chromosome length is immutable after creation
- [ ] Supports both uniform and per-gene bounds
- [ ] Works correctly with integer-specific crossover
- [ ] Works correctly with integer-specific mutation
- [ ] Handles edge cases (min=max, negative bounds)
- [ ] Equals/hashCode follow proper contracts
- [ ] ToString provides readable representation
- [ ] Comprehensive unit tests cover all scenarios
- [ ] Documentation includes usage examples

## Technical Notes

- Use `int[]` for internal storage
- Validate bounds: min <= max for all genes
- Consider immutability or defensive copying
- Support both inclusive and exclusive bounds if needed
- Document whether mutations create new instances or modify in place

## Example Usage

```java
// Create random integer chromosome with uniform bounds
IntegerChromosome chromosome = IntegerChromosome.random(5, 0, 100);

// Create with specific values
int[] genes = {10, 20, 30, 40, 50};
IntegerChromosome custom = new IntegerChromosome(genes, 0, 100);

// Create with per-gene bounds
int[] mins = {0, 10, 20, 0, 5};
int[] maxs = {50, 100, 200, 30, 15};
IntegerChromosome bounded = IntegerChromosome.random(5, mins, maxs);

// Access genes
int gene = chromosome.getGene(2);
```

## Related Issues

- Used by: Issue #11 (TwoPointCrossover)
- Used by: Issue #14 (IntegerMutation)
- Related to: Issue #2 (Population Management)
- Related to: Issue #3 (FitnessFunction Interface)
