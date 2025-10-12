---
name: Implement IntegerMutation
about: Implement random perturbation mutation for integer chromosomes
title: 'Implement IntegerMutation'
labels: implementation, algorithm, phase1, mutation
assignees: ''
milestone: Phase 1
---

## Summary

Implement Integer Mutation for IntegerChromosome, where gene values are randomly perturbed within their valid bounds. This mutation operator adds or subtracts random values to genes, maintaining diversity while respecting constraints.

## Description

Integer Mutation introduces genetic diversity by randomly modifying integer gene values. The implementation must:

- Iterate through all gene positions
- For each gene, decide whether to mutate based on probability
- Apply random perturbation (addition or subtraction)
- Respect gene bounds (min/max constraints)
- Support configurable mutation rate and perturbation magnitude
- Create new mutated chromosome (immutability)
- Handle edge cases (at boundaries, small ranges)

This mutation operator is crucial for exploring the integer solution space effectively.

## Tasks

- [ ] Create `IntegerMutation` class implementing `MutationStrategy<IntegerChromosome>`
- [ ] Add constructor accepting mutation probability parameter
- [ ] Add constructor accepting mutation probability and perturbation range
- [ ] Implement `mutate(IntegerChromosome chromosome)` returning mutated copy
- [ ] Validate mutation probability in range [0.0, 1.0]
- [ ] Iterate through each gene position
- [ ] For each gene, generate random value to decide mutation
- [ ] If mutation occurs, add random perturbation to gene value
- [ ] Clamp mutated value to gene bounds [min, max]
- [ ] Create and return new IntegerChromosome with mutations
- [ ] Support uniform random perturbation mode
- [ ] Support bounded increment/decrement mode
- [ ] Handle edge case: gene at min or max bound
- [ ] Ensure immutability (don't modify original)
- [ ] Add unit tests for various bounds and ranges
- [ ] Document perturbation strategies

## Acceptance Criteria

- [ ] Each gene is independently considered for mutation
- [ ] Mutation occurs with specified probability per gene
- [ ] Mutated values respect gene bounds [min, max]
- [ ] Mutated chromosome has same length as original
- [ ] Original chromosome is not modified (immutability)
- [ ] Perturbation magnitude is configurable
- [ ] When mutation probability = 0.0, returns identical copy
- [ ] Boundary cases handled correctly (clamping or rejection)
- [ ] Works with both uniform and per-gene bounds
- [ ] Statistical tests verify mutation rate
- [ ] Comprehensive unit tests cover edge cases
- [ ] Documentation includes perturbation strategy guidelines

## Technical Notes

- Typical mutation probability: 1/L where L is chromosome length
- Perturbation strategies:
  - Uniform: new_value = old_value + random(-delta, +delta)
  - Creep: new_value = old_value + random({-1, +1})
  - Random reset: new_value = random(min, max)
- Always clamp to bounds: value = max(min, min(value, max))
- Consider adaptive perturbation (decrease over time)

## Example Usage

```java
IntegerChromosome original = new IntegerChromosome(
    new int[]{10, 25, 50, 75, 90},
    0, 100  // min=0, max=100
);

// Mutation probability 0.2, perturbation range ±10
IntegerMutation mutation = new IntegerMutation(0.2, 10);

IntegerChromosome mutated = mutation.mutate(original);
// Example result (random per execution):
// mutated = [10, 30, 50, 68, 90]
//          (gene 1: +5, gene 3: -7)
```

## Visual Example

```
Original:      [10  25  50  75  90]
Mutate genes:  [-   *   -   *   -]   (* = mutate)
Perturbation:  [    +5      -7    ]
Result:        [10  30  50  68  90]
Clamped:       [10  30  50  68  90]  (all within [0, 100])
```

## Algorithm Pseudocode

```
1. Validate mutation probability in [0.0, 1.0]
2. Create copy of chromosome genes
3. For each gene position i from 0 to length-1:
   a. Generate random value r in [0, 1]
   b. If r < mutationProbability:
      - Generate perturbation delta in [-range, +range]
      - newValue = genes[i] + delta
      - genes[i] = clamp(newValue, min[i], max[i])
4. Return new IntegerChromosome with mutated genes
```

## Perturbation Strategies

1. **Uniform Random**: Add uniform random value in [-delta, +delta]
2. **Creep Mutation**: Add small increment (-1, 0, or +1)
3. **Random Reset**: Replace with random value in [min, max]
4. **Adaptive**: Reduce perturbation magnitude over generations

## Configuration Options

- **mutationProbability**: Per-gene mutation probability (default: 1/length)
- **perturbationRange**: Maximum change per mutation (default: 10% of range)
- **perturbationStrategy**: UNIFORM, CREEP, or RESET (default: UNIFORM)

## Related Issues

- Implements: MutationStrategy interface
- Works with: Issue #5 (IntegerChromosome)
- Used by: Issue #1 (Core GeneticAlgorithm Engine)
- Related to: Issue #13 (BinaryMutation), Issue #15 (FloatingPointMutation)
