---
name: Implement SinglePointCrossover
about: Implement single-point crossover operator
title: 'Implement SinglePointCrossover'
labels: implementation, algorithm, phase1, crossover
assignees: ''
milestone: Phase 1
---

## Summary

Implement Single-Point Crossover, the most basic and widely used crossover operator. A random crossover point is selected, and genetic material is exchanged between two parent chromosomes at that point to create two offspring.

## Description

Single-Point Crossover divides each parent chromosome at a randomly chosen position and creates offspring by swapping the segments. The implementation must:

- Select a random crossover point between positions
- Split both parents at the crossover point
- Create two children by combining segments from both parents
- Maintain chromosome length and validity
- Support all chromosome types (Binary, Integer, Float)
- Handle edge cases (crossover at beginning/end)
- Apply crossover with configurable probability

This is the foundation crossover method that demonstrates the core principle of genetic recombination.

## Tasks

- [ ] Create `SinglePointCrossover<T>` class implementing `CrossoverStrategy<T>`
- [ ] Add constructor accepting crossover probability parameter
- [ ] Implement `crossover(T parent1, T parent2)` returning offspring list
- [ ] Generate random crossover point (1 to length-1)
- [ ] Validate both parents have same length
- [ ] Create first child: parent1[0..point] + parent2[point..end]
- [ ] Create second child: parent2[0..point] + parent1[point..end]
- [ ] Apply crossover probability (may return parents unchanged)
- [ ] Support BinaryChromosome crossover
- [ ] Support IntegerChromosome crossover
- [ ] Support FloatingPointChromosome crossover
- [ ] Ensure offspring maintain valid bounds/constraints
- [ ] Handle edge case: crossover point at position 0
- [ ] Handle edge case: crossover point at last position
- [ ] Add unit tests for each chromosome type
- [ ] Document expected behavior and constraints

## Acceptance Criteria

- [ ] Crossover point is randomly selected
- [ ] Two offspring are produced from two parents
- [ ] Offspring maintain same length as parents
- [ ] Offspring maintain valid gene types and bounds
- [ ] Crossover applied with specified probability
- [ ] When probability < 1.0, sometimes returns parents unchanged
- [ ] Works correctly with BinaryChromosome
- [ ] Works correctly with IntegerChromosome
- [ ] Works correctly with FloatingPointChromosome
- [ ] No index out of bounds errors
- [ ] Offspring are new instances (not references to parents)
- [ ] Edge cases handled correctly
- [ ] Comprehensive unit tests verify correctness
- [ ] Documentation includes diagrams/examples

## Technical Notes

- Crossover probability typically 0.6 to 0.9
- Crossover point in range [1, length-1] to ensure mixing
- Consider whether to allow crossover at boundaries
- Ensure deep copy of gene values, not references
- Use generic programming to support all chromosome types
- May need different implementations per chromosome type

## Example Usage

```java
BinaryChromosome parent1 = new BinaryChromosome(
    new boolean[]{true, true, false, false, true}
);
BinaryChromosome parent2 = new BinaryChromosome(
    new boolean[]{false, false, true, true, false}
);

SinglePointCrossover<BinaryChromosome> crossover = 
    new SinglePointCrossover<>(0.8);

List<BinaryChromosome> offspring = crossover.crossover(parent1, parent2);
// If crossover point = 2:
// offspring[0] = [true, true | true, true, false]
// offspring[1] = [false, false | false, false, true]
```

## Visual Example

```
Parent1:  [1 1 0 | 0 1]    Crossover point = 3
Parent2:  [0 0 1 | 1 0]

Child1:   [1 1 0 | 1 0]    (Parent1 before | Parent2 after)
Child2:   [0 0 1 | 0 1]    (Parent2 before | Parent1 after)
```

## Related Issues

- Implements: CrossoverStrategy interface
- Alternative to: Issue #11 (TwoPointCrossover), Issue #12 (UniformCrossover)
- Used by: Issue #1 (Core GeneticAlgorithm Engine)
- Works with: Issue #4 (BinaryChromosome)
