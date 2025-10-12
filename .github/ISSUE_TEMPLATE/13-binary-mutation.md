---
name: Implement BinaryMutation
about: Implement bit-flip mutation for binary chromosomes
title: 'Implement BinaryMutation'
labels: implementation, algorithm, phase1, mutation
assignees: ''
milestone: Phase 1
---

## Summary

Implement Binary Mutation (bit-flip mutation) for BinaryChromosome, where each bit is independently flipped (0→1 or 1→0) with a specified mutation probability. This is the standard mutation operator for binary-encoded genetic algorithms.

## Description

Binary Mutation introduces genetic diversity by randomly flipping bits in a chromosome. The implementation must:

- Iterate through all bit positions in the chromosome
- For each bit, decide whether to flip based on mutation probability
- Flip selected bits (0 becomes 1, 1 becomes 0)
- Maintain chromosome length
- Support configurable mutation rate
- Create a new mutated chromosome (immutability)
- Handle edge cases (mutation rate 0.0 or 1.0)

This mutation operator is essential for preventing premature convergence and maintaining population diversity in binary GAs.

## Tasks

- [ ] Create `BinaryMutation` class implementing `MutationStrategy<BinaryChromosome>`
- [ ] Add constructor accepting mutation probability parameter
- [ ] Implement `mutate(BinaryChromosome chromosome)` returning mutated copy
- [ ] Validate mutation probability is in range [0.0, 1.0]
- [ ] Iterate through each bit position
- [ ] For each bit, generate random value to decide mutation
- [ ] If random < mutationProbability, flip the bit
- [ ] Create and return new BinaryChromosome with mutations
- [ ] Preserve chromosome length
- [ ] Ensure immutability (don't modify original chromosome)
- [ ] Handle edge case: mutation probability = 0.0 (no mutation)
- [ ] Handle edge case: mutation probability = 1.0 (flip all bits)
- [ ] Add configuration for adaptive mutation rates
- [ ] Add unit tests verifying mutation rate statistics
- [ ] Document typical mutation rate recommendations

## Acceptance Criteria

- [ ] Each bit is independently considered for mutation
- [ ] Mutation occurs with specified probability per bit
- [ ] Bits are correctly flipped (0→1, 1→0)
- [ ] Mutated chromosome has same length as original
- [ ] Original chromosome is not modified (immutability)
- [ ] When mutation probability = 0.0, returns identical copy
- [ ] When mutation probability = 1.0, all bits are flipped
- [ ] Typical mutation rate 1/L (L = chromosome length) works well
- [ ] No index out of bounds errors
- [ ] Statistical tests verify mutation rate over many operations
- [ ] Comprehensive unit tests cover all scenarios
- [ ] Documentation includes mutation rate guidelines

## Technical Notes

- Typical mutation probability: 1/L where L is chromosome length
- Lower rates (0.001 - 0.01) for large chromosomes
- Higher rates (0.05 - 0.1) for small chromosomes
- Too high: disrupts good solutions (random search)
- Too low: insufficient diversity, premature convergence
- Consider adaptive mutation (varies with convergence)

## Example Usage

```java
BinaryChromosome original = new BinaryChromosome(
    new boolean[]{true, false, true, true, false}
);

// Mutation probability 0.2 (20% chance per bit)
BinaryMutation mutation = new BinaryMutation(0.2);

BinaryChromosome mutated = mutation.mutate(original);
// Example result (random per execution):
// mutated = [false, false, true, false, false]
//           (bit 0 flipped, bit 3 flipped)
```

## Visual Example

```
Original:   [1 0 1 1 0]
Mutation:   [* - - * -]  (* = mutate, - = keep)
Result:     [0 0 1 0 0]  (bits 0 and 3 flipped)
```

## Algorithm Pseudocode

```
1. Validate mutation probability in [0.0, 1.0]
2. Create copy of chromosome
3. For each bit position i from 0 to length-1:
   a. Generate random value r in [0, 1]
   b. If r < mutationProbability:
      - Flip bit at position i
4. Return mutated chromosome
```

## Mutation Rate Guidelines

- **Standard**: `1/L` where L = chromosome length
- **Low diversity**: Increase to `2/L` or `3/L`
- **Converged population**: Increase temporarily
- **Early generations**: Can use slightly higher rate
- **Late generations**: Can use slightly lower rate

## Configuration Options

- **mutationProbability**: Per-bit mutation probability (default: 1/length)
- **adaptive**: Enable adaptive mutation rate (optional)

## Related Issues

- Implements: MutationStrategy interface
- Works with: Issue #4 (BinaryChromosome)
- Used by: Issue #1 (Core GeneticAlgorithm Engine)
- Related to: Issue #14 (IntegerMutation), Issue #15 (FloatingPointMutation)
