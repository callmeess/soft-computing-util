---
name: Implement GenerationalReplacement
about: Implement full generational replacement strategy
title: 'Implement GenerationalReplacement'
labels: implementation, algorithm, phase1, replacement
assignees: ''
milestone: Phase 1
---

## Summary

Implement Generational Replacement (also called Complete Generational Replacement), where the entire population is replaced by offspring in each generation. This is the standard replacement strategy in classic genetic algorithms.

## Description

Generational Replacement completely replaces the current population with newly generated offspring. The implementation must:

- Accept a complete set of offspring
- Replace entire current population
- Maintain constant population size
- Support both with and without elitism
- Ensure no overlap between generations (except elites)
- Work with all chromosome types
- Handle edge cases (insufficient offspring)

This strategy provides clear generational boundaries and is conceptually simple.

## Tasks

- [ ] Create `GenerationalReplacement<T>` class implementing `ReplacementStrategy<T>`
- [ ] Add default constructor (no parameters needed for pure version)
- [ ] Implement `replace(Population<T> current, List<T> offspring, FitnessFunction<T> fitness)` method
- [ ] Validate offspring list has sufficient individuals
- [ ] If offspring.size() >= populationSize, select best offspring
- [ ] If offspring.size() < populationSize, handle shortage (duplicate or error)
- [ ] Create new population from selected offspring
- [ ] Ensure new population has correct size
- [ ] Add option to combine with elitism (preserve top N from current)
- [ ] Handle edge case: no offspring generated
- [ ] Handle edge case: fewer offspring than population size
- [ ] Support random selection vs fitness-based selection from offspring
- [ ] Add unit tests for various offspring sizes
- [ ] Document when to use vs steady-state replacement

## Acceptance Criteria

- [ ] Entire population is replaced by offspring each generation
- [ ] New population size equals original population size
- [ ] No individuals from current generation appear in next (unless elitism enabled)
- [ ] When offspring.size() > populationSize, best offspring selected
- [ ] When offspring.size() = populationSize, all offspring used
- [ ] When offspring.size() < populationSize, handled appropriately (error or fill)
- [ ] Works correctly for all chromosome types
- [ ] No null references in new population
- [ ] Optional elitism can be applied
- [ ] Comprehensive unit tests verify behavior
- [ ] Documentation explains use cases and characteristics

## Technical Notes

- Most common in traditional GAs
- Requires generating populationSize offspring per generation
- Clear generation boundaries simplify convergence analysis
- Can be combined with elitism for better performance
- Consider offspring generation strategy:
  - Generate exactly populationSize offspring
  - Generate more and select best
  - Generate in pairs (crossover produces 2 children)

## Example Usage

```java
Population<IntegerChromosome> currentPop = ...; // size 100
List<IntegerChromosome> offspring = ...; // size 100
FitnessFunction<IntegerChromosome> fitness = new SumFitness();

// Pure generational replacement
GenerationalReplacement<IntegerChromosome> replacement = 
    new GenerationalReplacement<>();

Population<IntegerChromosome> nextPop = 
    replacement.replace(currentPop, offspring, fitness);
// nextPop contains exactly the offspring (or best 100 if more generated)
```

## Visual Example

```
Current Population (size 10):
[90, 85, 80, 75, 70, 65, 60, 55, 50, 45]  (fitness values)

Offspring (size 10):
[88, 82, 78, 72, 68, 62, 58, 52, 48, 42]

Generational Replacement:
Next Generation = [88, 82, 78, 72, 68, 62, 58, 52, 48, 42]
                  ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
                  All from offspring, none from current
```

## Algorithm Pseudocode

```
1. Validate offspring list is not empty
2. If offspring.size() > populationSize:
   a. Evaluate fitness for all offspring
   b. Sort offspring by fitness
   c. Select top populationSize offspring
3. Else if offspring.size() = populationSize:
   a. Use all offspring
4. Else if offspring.size() < populationSize:
   a. Throw error OR fill with duplicates/random
5. Create new population from selected offspring
6. Return new population
```

## Handling Insufficient Offspring

**Option 1: Strict (Recommended)**
- Throw exception if offspring.size() < populationSize
- Forces caller to generate sufficient offspring

**Option 2: Permissive**
- Fill remaining slots by duplicating random offspring
- Or by keeping some individuals from current population

**Option 3: Adaptive**
- Automatically generate additional offspring to reach populationSize

## Configuration Options

- **selectionMode**: BEST_FITNESS or RANDOM (default: BEST_FITNESS)
- **allowDuplicates**: Allow duplicate offspring in population (default: true)
- **fillStrategy**: How to handle insufficient offspring (ERROR, DUPLICATE, KEEP_OLD)

## Comparison with Other Strategies

| Strategy | Parents Overlap | Diversity | Convergence Speed |
|----------|----------------|-----------|-------------------|
| Generational | No (except elitism) | Medium | Medium |
| Steady-State | High | High | Slower |
| Elitism | Partial | Medium-Low | Faster |

## Related Issues

- Implements: ReplacementStrategy interface
- Alternative to: Issue #16 (ElitismReplacement), Issue #18 (SteadyStateReplacement)
- Used by: Issue #1 (Core GeneticAlgorithm Engine)
