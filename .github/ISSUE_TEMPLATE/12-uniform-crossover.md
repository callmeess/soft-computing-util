---
name: Implement UniformCrossover
about: Implement uniform crossover operator with gene-level mixing
title: 'Implement UniformCrossover'
labels: implementation, algorithm, phase1, crossover
assignees: ''
milestone: Phase 1
---

## Summary

Implement Uniform Crossover, where each gene is independently chosen from either parent with a specified probability. This provides maximum mixing and is particularly effective when gene interactions are not position-dependent.

## Description

Uniform Crossover treats each gene position independently, deciding for each gene whether to inherit from parent1 or parent2. The implementation must:

- Iterate through all gene positions
- For each position, randomly decide which parent contributes
- Use configurable mixing ratio (default 0.5 for equal probability)
- Create two complementary offspring
- Maintain chromosome length and validity
- Support all chromosome types (Binary, Integer, Float)
- Provide maximum disruption of gene linkage

This crossover method is ideal when there's no positional bias in gene importance.

## Tasks

- [ ] Create `UniformCrossover<T>` class implementing `CrossoverStrategy<T>`
- [ ] Add constructor accepting crossover probability and mixing ratio
- [ ] Implement `crossover(T parent1, T parent2)` returning offspring list
- [ ] Validate both parents have same length
- [ ] Iterate through each gene position
- [ ] For each gene, generate random value to decide inheritance
- [ ] If random < mixingRatio, child1 gets parent1's gene, else parent2's
- [ ] Create complementary child2 (opposite inheritance)
- [ ] Apply overall crossover probability
- [ ] Support BinaryChromosome crossover
- [ ] Support IntegerChromosome crossover
- [ ] Support FloatingPointChromosome crossover
- [ ] Ensure offspring maintain valid bounds/constraints
- [ ] Add configuration for swap probability per gene
- [ ] Handle edge case: mixing ratio = 0.0 or 1.0
- [ ] Add unit tests for each chromosome type
- [ ] Document expected gene inheritance distribution

## Acceptance Criteria

- [ ] Each gene is independently inherited from one parent
- [ ] Mixing ratio controls probability of inheriting from parent1
- [ ] Two offspring are produced from two parents
- [ ] Offspring are complementary (genes not from parent1 come from parent2)
- [ ] Offspring maintain same length as parents
- [ ] Offspring maintain valid gene types and bounds
- [ ] Crossover applied with specified probability
- [ ] Works correctly with BinaryChromosome
- [ ] Works correctly with IntegerChromosome
- [ ] Works correctly with FloatingPointChromosome
- [ ] Default mixing ratio is 0.5 (equal probability)
- [ ] When mixing ratio = 0.0, child1 = parent2, child2 = parent1
- [ ] When mixing ratio = 1.0, child1 = parent1, child2 = parent2
- [ ] No index out of bounds errors
- [ ] Comprehensive unit tests verify distribution
- [ ] Documentation includes visual examples

## Technical Notes

- Crossover probability typically 0.6 to 0.9
- Mixing ratio typically 0.5 for balanced inheritance
- Can adjust mixing ratio for biased inheritance
- More disruptive than single/two-point crossover
- May break building blocks more frequently
- Consider performance implications of per-gene randomization

## Example Usage

```java
FloatingPointChromosome parent1 = new FloatingPointChromosome(
    new double[]{1.0, 2.0, 3.0, 4.0, 5.0}
);
FloatingPointChromosome parent2 = new FloatingPointChromosome(
    new double[]{6.0, 7.0, 8.0, 9.0, 10.0}
);

// Mixing ratio 0.5 means 50% chance from each parent per gene
UniformCrossover<FloatingPointChromosome> crossover = 
    new UniformCrossover<>(0.8, 0.5);

List<FloatingPointChromosome> offspring = 
    crossover.crossover(parent1, parent2);
// Example result (random per execution):
// offspring[0] = [1.0, 7.0, 3.0, 9.0, 5.0]
// offspring[1] = [6.0, 2.0, 8.0, 4.0, 10.0]
```

## Visual Example

```
Parent1:     [1  2  3  4  5]
Parent2:     [6  7  8  9  10]
Random mask: [1  0  1  0  1]  (1 = from parent1, 0 = from parent2)

Child1:      [1  7  3  9  5]  (uses mask)
Child2:      [6  2  8  4  10] (uses inverted mask)
```

## Algorithm Pseudocode

```
1. Validate parents have same length
2. Create empty offspring1 and offspring2
3. For each gene position i from 0 to length-1:
   a. Generate random value r in [0, 1]
   b. If r < mixingRatio:
      - offspring1[i] = parent1[i]
      - offspring2[i] = parent2[i]
   c. Else:
      - offspring1[i] = parent2[i]
      - offspring2[i] = parent1[i]
4. Return [offspring1, offspring2]
```

## Configuration Options

- **crossoverProbability**: Probability of applying crossover (default: 0.8)
- **mixingRatio**: Probability of inheriting from parent1 per gene (default: 0.5)

## Related Issues

- Implements: CrossoverStrategy interface
- Alternative to: Issue #10 (SinglePointCrossover), Issue #11 (TwoPointCrossover)
- Used by: Issue #1 (Core GeneticAlgorithm Engine)
- Works with: Issue #6 (FloatingPointChromosome)
