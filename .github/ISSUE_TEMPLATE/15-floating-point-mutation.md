---
name: Implement FloatingPointMutation
about: Implement Gaussian mutation for floating-point chromosomes
title: 'Implement FloatingPointMutation'
labels: implementation, algorithm, phase1, mutation
assignees: ''
milestone: Phase 1
---

## Summary

Implement Floating-Point Mutation (Gaussian mutation) for FloatingPointChromosome, where gene values are perturbed by adding Gaussian-distributed random noise. This is the standard mutation operator for real-valued genetic algorithms.

## Description

Floating-Point Mutation introduces continuous variation by adding Gaussian (normal) distributed noise to gene values. The implementation must:

- Iterate through all gene positions
- For each gene, decide whether to mutate based on probability
- Add Gaussian noise with configurable mean and standard deviation
- Respect gene bounds (min/max constraints)
- Support configurable mutation rate and noise parameters
- Create new mutated chromosome (immutability)
- Handle boundary violations (clamping, reflection, or resampling)

This mutation operator provides fine-grained exploration of continuous solution spaces.

## Tasks

- [ ] Create `FloatingPointMutation` class implementing `MutationStrategy<FloatingPointChromosome>`
- [ ] Add constructor accepting mutation probability and standard deviation
- [ ] Implement `mutate(FloatingPointChromosome chromosome)` returning mutated copy
- [ ] Validate mutation probability in range [0.0, 1.0]
- [ ] Iterate through each gene position
- [ ] For each gene, generate random value to decide mutation
- [ ] Generate Gaussian noise using `Random.nextGaussian()`
- [ ] Add scaled Gaussian noise to gene value
- [ ] Handle boundary violations (clamp, reflect, or resample)
- [ ] Create and return new FloatingPointChromosome with mutations
- [ ] Support configurable standard deviation per gene
- [ ] Support uniform and per-gene standard deviations
- [ ] Implement adaptive mutation (decreasing sigma over time)
- [ ] Add unit tests for distribution and bounds
- [ ] Document sigma selection guidelines

## Acceptance Criteria

- [ ] Each gene is independently considered for mutation
- [ ] Mutation occurs with specified probability per gene
- [ ] Gaussian noise follows normal distribution N(0, sigma²)
- [ ] Mutated values respect gene bounds [min, max]
- [ ] Mutated chromosome has same length as original
- [ ] Original chromosome is not modified (immutability)
- [ ] Standard deviation (sigma) is configurable
- [ ] When mutation probability = 0.0, returns identical copy
- [ ] Boundary violations handled consistently
- [ ] Works with both uniform and per-gene bounds
- [ ] Statistical tests verify Gaussian distribution
- [ ] Comprehensive unit tests cover edge cases
- [ ] Documentation includes sigma tuning guidelines

## Technical Notes

- Typical mutation probability: 1/L where L is chromosome length
- Standard deviation (sigma) controls mutation strength:
  - Small sigma (0.01 - 0.1): Local search, fine-tuning
  - Large sigma (0.5 - 1.0): Global search, exploration
- Sigma often set to 10-20% of the variable range
- Boundary handling strategies:
  - **Clamping**: value = max(min, min(value, max))
  - **Reflection**: reflect back from boundary
  - **Resampling**: regenerate if out of bounds
- Consider self-adaptive mutation (sigma evolves)

## Example Usage

```java
FloatingPointChromosome original = new FloatingPointChromosome(
    new double[]{0.5, 1.0, 2.5, 3.0, 4.5},
    0.0, 5.0  // min=0.0, max=5.0
);

// Mutation probability 0.2, standard deviation 0.5
FloatingPointMutation mutation = new FloatingPointMutation(0.2, 0.5);

FloatingPointChromosome mutated = mutation.mutate(original);
// Example result (random per execution):
// mutated = [0.5, 1.23, 2.5, 2.81, 4.5]
//          (gene 1: +0.23, gene 3: -0.19 from Gaussian noise)
```

## Visual Example

```
Original:      [0.5   1.0   2.5   3.0   4.5]
Mutate genes:  [-     *     -     *     -]    (* = mutate)
Gaussian δ:    [      +0.23      -0.19    ]   (from N(0, 0.5²))
Result:        [0.5   1.23  2.5   2.81  4.5]
Clamped:       [0.5   1.23  2.5   2.81  4.5]  (all within [0, 5])
```

## Algorithm Pseudocode

```
1. Validate mutation probability in [0.0, 1.0]
2. Create copy of chromosome genes
3. For each gene position i from 0 to length-1:
   a. Generate random value r in [0, 1]
   b. If r < mutationProbability:
      - Generate Gaussian noise: delta = nextGaussian() * sigma
      - newValue = genes[i] + delta
      - genes[i] = clamp(newValue, min[i], max[i])
4. Return new FloatingPointChromosome with mutated genes
```

## Standard Deviation (Sigma) Guidelines

- **Start of run**: Larger sigma (exploration)
  - sigma = 0.2 * (max - min)
- **Middle of run**: Moderate sigma (balance)
  - sigma = 0.1 * (max - min)
- **End of run**: Smaller sigma (fine-tuning)
  - sigma = 0.05 * (max - min)
- **Adaptive approach**: Decrease sigma with generation number
  - sigma(t) = sigma_0 * exp(-decay * t)

## Boundary Handling

1. **Clamping** (simplest):
   - `value = Math.max(min, Math.min(value, max))`
2. **Reflection**:
   - If value < min: `value = min + (min - value)`
   - If value > max: `value = max - (value - max)`
3. **Resampling**:
   - Regenerate mutation until value is in bounds

## Configuration Options

- **mutationProbability**: Per-gene mutation probability (default: 1/length)
- **sigma**: Standard deviation of Gaussian noise (default: 0.1 * range)
- **boundaryHandling**: CLAMP, REFLECT, or RESAMPLE (default: CLAMP)
- **adaptive**: Enable adaptive sigma (decreases over time)

## Related Issues

- Implements: MutationStrategy interface
- Works with: Issue #6 (FloatingPointChromosome)
- Used by: Issue #1 (Core GeneticAlgorithm Engine)
- Related to: Issue #13 (BinaryMutation), Issue #14 (IntegerMutation)
