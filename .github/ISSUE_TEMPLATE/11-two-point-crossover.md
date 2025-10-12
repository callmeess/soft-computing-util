---
name: Implement TwoPointCrossover
about: Implement two-point crossover operator
title: 'Implement TwoPointCrossover'
labels: implementation, algorithm, phase1, crossover
assignees: ''
milestone: Phase 1
---

## Summary

Implement Two-Point Crossover, an enhanced version of single-point crossover that uses two crossover points. A segment between two randomly selected points is exchanged between parent chromosomes, often providing better mixing than single-point crossover.

## Description

Two-Point Crossover selects two random positions in the chromosome and swaps the segment between them. This approach:

- Reduces positional bias present in single-point crossover
- Preserves building blocks at the ends of chromosomes
- Provides better exploration of the search space
- Can exchange segments from any part of the chromosome
- Maintains chromosome length and validity
- Supports all chromosome types (Binary, Integer, Float)

This crossover method is particularly effective for problems where gene interactions are important.

## Tasks

- [ ] Create `TwoPointCrossover<T>` class implementing `CrossoverStrategy<T>`
- [ ] Add constructor accepting crossover probability parameter
- [ ] Implement `crossover(T parent1, T parent2)` returning offspring list
- [ ] Generate two random crossover points (point1 < point2)
- [ ] Validate both parents have same length
- [ ] Create first child: parent1[0..p1] + parent2[p1..p2] + parent1[p2..end]
- [ ] Create second child: parent2[0..p1] + parent1[p1..p2] + parent2[p2..end]
- [ ] Apply crossover probability
- [ ] Support BinaryChromosome crossover
- [ ] Support IntegerChromosome crossover
- [ ] Support FloatingPointChromosome crossover
- [ ] Ensure offspring maintain valid bounds/constraints
- [ ] Handle edge cases (points at boundaries)
- [ ] Add validation: point1 < point2
- [ ] Optimize point selection (avoid redundant sorting)
- [ ] Add unit tests for each chromosome type
- [ ] Document advantages over single-point crossover

## Acceptance Criteria

- [ ] Two distinct crossover points are randomly selected
- [ ] Crossover points satisfy: 0 <= point1 < point2 <= length
- [ ] Two offspring are produced from two parents
- [ ] Middle segment is exchanged between parents
- [ ] Offspring maintain same length as parents
- [ ] Offspring maintain valid gene types and bounds
- [ ] Crossover applied with specified probability
- [ ] Works correctly with BinaryChromosome
- [ ] Works correctly with IntegerChromosome
- [ ] Works correctly with FloatingPointChromosome
- [ ] No index out of bounds errors
- [ ] Offspring are new instances (not parent references)
- [ ] Edge cases handled correctly (adjacent points, boundary points)
- [ ] Comprehensive unit tests verify correctness
- [ ] Documentation includes visual examples

## Technical Notes

- Crossover probability typically 0.6 to 0.9
- Points selected in range [0, length] with point1 < point2
- When point1 = 0 and point2 = length, returns copies of parents
- Ensure points are distinct (point1 ≠ point2)
- Use efficient point generation (single random + offset)
- Consider sorting points if generated independently

## Example Usage

```java
IntegerChromosome parent1 = new IntegerChromosome(
    new int[]{1, 2, 3, 4, 5, 6}
);
IntegerChromosome parent2 = new IntegerChromosome(
    new int[]{7, 8, 9, 10, 11, 12}
);

TwoPointCrossover<IntegerChromosome> crossover = 
    new TwoPointCrossover<>(0.8);

List<IntegerChromosome> offspring = crossover.crossover(parent1, parent2);
// If crossover points = 2, 4:
// offspring[0] = [1, 2 | 9, 10 | 5, 6]
// offspring[1] = [7, 8 | 3, 4 | 11, 12]
```

## Visual Example

```
Parent1:  [1 2 | 3 4 | 5 6]    Points: 2 and 4
Parent2:  [7 8 | 9 10 | 11 12]

Child1:   [1 2 | 9 10 | 5 6]   (P1 | P2 | P1)
Child2:   [7 8 | 3 4 | 11 12]  (P2 | P1 | P2)
```

## Algorithm Pseudocode

```
1. Validate parents have same length
2. Generate point1 = random(0, length-1)
3. Generate point2 = random(point1+1, length)
4. Create offspring1:
   - Copy parent1[0:point1]
   - Copy parent2[point1:point2]
   - Copy parent1[point2:length]
5. Create offspring2:
   - Copy parent2[0:point1]
   - Copy parent1[point1:point2]
   - Copy parent2[point2:length]
6. Return [offspring1, offspring2]
```

## Related Issues

- Implements: CrossoverStrategy interface
- Alternative to: Issue #10 (SinglePointCrossover), Issue #12 (UniformCrossover)
- Used by: Issue #1 (Core GeneticAlgorithm Engine)
- Works with: Issue #5 (IntegerChromosome)
