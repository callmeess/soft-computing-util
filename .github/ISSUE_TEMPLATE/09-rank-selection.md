---
name: Implement RankSelection
about: Implement rank-based selection strategy
title: 'Implement RankSelection'
labels: implementation, algorithm, phase1, selection
assignees: ''
milestone: Phase 1
---

## Summary

Implement Rank Selection, where selection probability is based on relative rank rather than absolute fitness values. This method addresses the issues of Roulette Wheel Selection by preventing premature convergence and handling negative fitness values naturally.

## Description

Rank Selection first ranks all individuals by fitness, then assigns selection probabilities based on rank position rather than raw fitness values. This provides more consistent selection pressure throughout the evolutionary process. The implementation must:

- Sort population by fitness to determine ranks
- Assign selection probabilities based on rank
- Support linear and exponential ranking schemes
- Handle both maximization and minimization problems
- Maintain constant selection pressure regardless of fitness scale
- Prevent domination by super-fit individuals
- Support configurable selection pressure parameter

This method is particularly useful when fitness values have large variance or when preventing premature convergence is important.

## Tasks

- [ ] Create `RankSelection` class implementing `SelectionStrategy`
- [ ] Add constructor accepting selection pressure parameter
- [ ] Implement `select(List<T> population, FitnessFunction<T> fitness)` method
- [ ] Sort population by fitness to determine ranks
- [ ] Implement linear ranking probability calculation
- [ ] Calculate selection probability: p(i) = (2-s)/n + (2*i*(s-1))/(n*(n-1))
- [ ] Where s is selection pressure (1.0 to 2.0), n is population size
- [ ] Build cumulative probability array
- [ ] Implement roulette wheel selection using rank probabilities
- [ ] Handle ties in fitness (assign same rank)
- [ ] Support both maximization and minimization
- [ ] Add optional exponential ranking mode
- [ ] Optimize by caching sorted population and probabilities
- [ ] Validate selection pressure parameter (1.0 <= s <= 2.0)
- [ ] Add unit tests for various pressure values
- [ ] Document relationship between pressure and convergence speed

## Acceptance Criteria

- [ ] Population is correctly ranked by fitness
- [ ] Selection probability based on rank, not raw fitness
- [ ] Selection pressure is configurable (default s=1.5)
- [ ] Works correctly for both maximization and minimization
- [ ] Handles negative and zero fitness values
- [ ] Handles fitness ties appropriately
- [ ] Worst individual has non-zero selection probability
- [ ] Best individual doesn't dominate selection
- [ ] When s=1.0, selection is uniform (random)
- [ ] When s=2.0, selection pressure is maximum
- [ ] Efficient implementation O(n log n) for sorting, O(log n) per selection
- [ ] Thread-safe if concurrent selections occur
- [ ] Comprehensive unit tests verify probability distribution
- [ ] Documentation includes mathematical formula and examples

## Technical Notes

- Linear ranking formula: `p(i) = (2-s)/n + (2*i*(s-1))/(n*(n-1))`
- Selection pressure s typically in range [1.1, 1.9]
- Higher s → stronger selection pressure → faster convergence
- Cache ranking and probabilities when doing multiple selections
- For exponential ranking: `p(i) = (1-e^(-i)) / C` where C normalizes
- Consider storing (fitness, originalIndex) pairs for ranking

## Example Usage

```java
Population<FloatingPointChromosome> population = ...; // size 100
FitnessFunction<FloatingPointChromosome> fitness = new SphereFitness();

// Create rank selection with selection pressure 1.5
RankSelection selector = new RankSelection(1.5);

// Select parents
FloatingPointChromosome parent1 = selector.select(population, fitness);
FloatingPointChromosome parent2 = selector.select(population, fitness);
```

## Algorithm Pseudocode

```
1. Evaluate fitness for all individuals
2. Sort population by fitness (ascending or descending)
3. Assign rank: worst=1, best=n
4. For each rank i, calculate probability:
   p(i) = (2-s)/n + (2*i*(s-1))/(n*(n-1))
5. Build cumulative probability array
6. Generate random number r in [0, 1]
7. Select individual using cumulative probabilities
8. Return selected individual
```

## Configuration Options

- **selectionPressure**: Controls selection intensity (1.0 to 2.0, default: 1.5)
- **rankingMode**: LINEAR or EXPONENTIAL (default: LINEAR)

## Related Issues

- Implements: SelectionStrategy interface
- Alternative to: Issue #7 (RouletteWheelSelection), Issue #8 (TournamentSelection)
- Used by: Issue #1 (Core GeneticAlgorithm Engine)
