---
name: Implement BinaryChromosome
about: Create binary string chromosome representation
title: 'Implement BinaryChromosome'
labels: implementation, algorithm, phase1, chromosome
assignees: ''
milestone: Phase 1
---

## Summary

Implement a `BinaryChromosome` class that encodes solutions as bitstrings (arrays of 0s and 1s). This is one of the most fundamental chromosome representations in genetic algorithms, suitable for binary optimization problems.

## Description

The BinaryChromosome represents solutions as fixed-length binary strings. It must support:

- Encoding solutions as boolean arrays or bit vectors
- Random initialization with configurable length
- Efficient storage and access to individual bits
- Integration with binary-specific crossover operators
- Integration with binary-specific mutation operators
- Conversion to/from different representations
- Validation of chromosome structure

This implementation should be efficient, immutable where appropriate, and work seamlessly with the GA framework.

## Tasks

- [ ] Create `BinaryChromosome` class
- [ ] Add constructor accepting length and boolean array
- [ ] Implement static factory method `random(int length)` for initialization
- [ ] Add getter methods for accessing bits
- [ ] Implement `getLength()` method
- [ ] Add `getBit(int index)` method
- [ ] Implement `toBooleanArray()` conversion method
- [ ] Implement `toIntArray()` conversion method (0s and 1s)
- [ ] Add `countOnes()` utility method
- [ ] Add `countZeros()` utility method
- [ ] Implement `flipBit(int index)` method for mutation support
- [ ] Add `clone()` or copy constructor for safe copying
- [ ] Override `equals()` and `hashCode()` for proper comparison
- [ ] Override `toString()` for debugging
- [ ] Add validation for invalid indices and null inputs
- [ ] Document bit ordering conventions (MSB vs LSB)

## Acceptance Criteria

- [ ] BinaryChromosome can be created with specified length
- [ ] Random initialization produces valid binary strings
- [ ] All bits can be accessed and modified correctly
- [ ] Chromosome length is immutable after creation
- [ ] No index out of bounds errors with proper validation
- [ ] Efficient memory usage (consider using BitSet internally)
- [ ] Works correctly with single-point crossover
- [ ] Works correctly with bit-flip mutation
- [ ] Equals/hashCode follow proper contracts
- [ ] ToString provides readable representation
- [ ] Comprehensive unit tests cover all operations
- [ ] Documentation includes usage examples

## Technical Notes

- Consider using `java.util.BitSet` for efficient storage
- Ensure immutability for thread-safety or use defensive copying
- Validate length > 0 in constructors
- Consider caching frequently computed values (ones count)
- Document whether operations create new instances or modify existing ones

## Example Usage

```java
// Create random binary chromosome of length 10
BinaryChromosome chromosome = BinaryChromosome.random(10);

// Access individual bits
boolean bit = chromosome.getBit(5);

// Count ones
int ones = chromosome.countOnes();

// Create specific chromosome
BinaryChromosome custom = new BinaryChromosome(
    new boolean[]{true, false, true, true, false}
);
```

## Related Issues

- Used by: Issue #10 (SinglePointCrossover)
- Used by: Issue #13 (BinaryMutation)
- Related to: Issue #2 (Population Management)
- Related to: Issue #3 (FitnessFunction Interface)
