---
name: Implement FloatingPointChromosome
about: Create floating-point vector chromosome representation
title: 'Implement FloatingPointChromosome'
labels: implementation, algorithm, phase1, chromosome
assignees: ''
milestone: Phase 1
---

## Summary

Implement a `FloatingPointChromosome` class that encodes solutions as vectors of real numbers (doubles). This chromosome type is ideal for continuous optimization problems, function optimization, and real-parameter problems.

## Description

The FloatingPointChromosome represents solutions as fixed-length arrays of double values. It must support:

- Encoding solutions with configurable precision and bounds
- Random initialization within specified continuous ranges
- Efficient storage and access to real-valued genes
- Integration with real-valued crossover operators
- Integration with Gaussian mutation operators
- Validation of bounds and constraints
- High precision for sensitive optimization problems

This implementation enables solving continuous optimization problems like function minimization, parameter tuning, and scientific computing applications.

## Tasks

- [ ] Create `FloatingPointChromosome` class
- [ ] Add constructor accepting double array and bounds
- [ ] Implement static factory method `random(int length, double min, double max)`
- [ ] Add constructor supporting per-gene bounds
- [ ] Implement `getGene(int index)` method
- [ ] Implement `getGenes()` to return all values
- [ ] Add `getLength()` method
- [ ] Implement `setGene(int index, double value)` with bounds checking
- [ ] Add `getMinBound(int index)` and `getMaxBound(int index)` methods
- [ ] Implement validation for bounds constraints
- [ ] Add `clone()` or copy constructor
- [ ] Override `equals()` and `hashCode()` (with epsilon for double comparison)
- [ ] Override `toString()` with formatted output
- [ ] Add utility methods (euclideanDistance, normalize)
- [ ] Support uniform bounds and per-gene bounds
- [ ] Add method to check constraint satisfaction
- [ ] Handle special values (NaN, Infinity) appropriately

## Acceptance Criteria

- [ ] FloatingPointChromosome can be created with specified length and bounds
- [ ] Random initialization respects min/max bounds
- [ ] All genes can be accessed and modified correctly
- [ ] Bounds validation prevents invalid gene values
- [ ] Chromosome length is immutable after creation
- [ ] Supports both uniform and per-gene bounds
- [ ] Proper handling of floating-point precision issues
- [ ] Works correctly with real-valued crossover (blend, arithmetic)
- [ ] Works correctly with Gaussian mutation
- [ ] Handles special values (NaN, Infinity) gracefully
- [ ] Equals/hashCode use epsilon comparison for doubles
- [ ] ToString provides readable formatted representation
- [ ] Comprehensive unit tests cover all scenarios including edge cases
- [ ] Documentation includes usage examples and precision notes

## Technical Notes

- Use `double[]` for internal storage
- Validate bounds: min <= max for all genes
- Use epsilon (e.g., 1e-9) for double equality comparisons
- Consider immutability or defensive copying
- Document precision limitations of double representation
- Consider clamping values to bounds vs. rejecting out-of-bounds

## Example Usage

```java
// Create random floating-point chromosome with uniform bounds
FloatingPointChromosome chromosome = 
    FloatingPointChromosome.random(5, 0.0, 1.0);

// Create with specific values
double[] genes = {0.1, 0.5, 0.3, 0.9, 0.2};
FloatingPointChromosome custom = 
    new FloatingPointChromosome(genes, 0.0, 1.0);

// Create with per-gene bounds for constrained optimization
double[] mins = {-5.0, 0.0, -10.0, 0.0, -1.0};
double[] maxs = {5.0, 100.0, 10.0, 1.0, 1.0};
FloatingPointChromosome bounded = 
    FloatingPointChromosome.random(5, mins, maxs);

// Access genes
double gene = chromosome.getGene(2);
```

## Related Issues

- Used by: Issue #12 (UniformCrossover)
- Used by: Issue #15 (FloatingPointMutation)
- Related to: Issue #2 (Population Management)
- Related to: Issue #3 (FitnessFunction Interface)
