---
name: Implement SteadyStateReplacement
about: Implement steady-state (incremental) replacement strategy
title: 'Implement SteadyStateReplacement'
labels: implementation, algorithm, phase1, replacement
assignees: ''
milestone: Phase 1
---

## Summary

Implement Steady-State Replacement (also called incremental replacement), where only a few individuals (typically the worst) are replaced each generation. This maintains high population overlap between generations, preserving diversity while allowing gradual improvement.

## Description

Steady-State Replacement replaces only a small portion of the population each iteration. The implementation must:

- Identify worst individuals in current population
- Replace them with new offspring
- Maintain majority of population unchanged
- Support configurable replacement count
- Preserve population size
- Work with all chromosome types
- Optionally use fitness-based or age-based replacement

This strategy provides smoother convergence and maintains more diversity than generational replacement.

## Tasks

- [ ] Create `SteadyStateReplacement<T>` class implementing `ReplacementStrategy<T>`
- [ ] Add constructor accepting replacement count parameter
- [ ] Implement `replace(Population<T> current, List<T> offspring, FitnessFunction<T> fitness)` method
- [ ] Validate replacement count: 0 < count < populationSize
- [ ] Evaluate fitness for current population
- [ ] Sort current population by fitness
- [ ] Identify worst N individuals to replace
- [ ] Select best N offspring as replacements
- [ ] Create new population: keep best, replace worst
- [ ] Ensure new population has correct size
- [ ] Support replacement by fitness (worst fitness replaced)
- [ ] Support replacement by age (oldest replaced)
- [ ] Handle edge case: fewer offspring than replacement count
- [ ] Add option for parent-offspring competition
- [ ] Add unit tests for various replacement counts
- [ ] Document trade-offs vs generational replacement

## Acceptance Criteria

- [ ] Replacement count is configurable (default: 1-2 or 10% of population)
- [ ] Worst N individuals are correctly identified
- [ ] Worst individuals replaced by best offspring
- [ ] Majority of population carries over unchanged
- [ ] New population maintains original size
- [ ] No null references in new population
- [ ] Works correctly for maximization problems
- [ ] Works correctly for minimization problems
- [ ] When replacementCount = 1, minimal disruption per generation
- [ ] When replacementCount = populationSize, equivalent to generational
- [ ] Comprehensive unit tests verify behavior
- [ ] Documentation explains convergence characteristics

## Technical Notes

- Typical replacement count: 1-2 individuals or 5-20% of population
- Smaller replacement → slower convergence, more diversity
- Larger replacement → faster convergence, less diversity
- Often combined with tournament selection (2-4 parents, 2 offspring)
- May replace parents with offspring immediately after generation
- Consider tracking age of individuals for age-based replacement

## Example Usage

```java
Population<FloatingPointChromosome> currentPop = ...; // size 100
List<FloatingPointChromosome> offspring = ...; // size 20
FitnessFunction<FloatingPointChromosome> fitness = new SphereFitness();

// Replace worst 10 individuals each generation (10% replacement)
SteadyStateReplacement<FloatingPointChromosome> replacement = 
    new SteadyStateReplacement<>(10);

Population<FloatingPointChromosome> nextPop = 
    replacement.replace(currentPop, offspring, fitness);
// nextPop contains:
// - 90 individuals from currentPop (best 90)
// - 10 best offspring (replacing worst 10 from current)
```

## Visual Example

```
Current Population (size 10):
[90, 85, 80, 75, 70, 65, 60, 55, 50, 45]  (fitness values)
                                   ^^^^^^^ worst 3

Offspring (size 5):
[88, 82, 78, 72, 68]
 ^^^^^^^^^^^^^^^^ best 3 offspring

Steady-State (replace 3):
Next Generation = [90, 85, 80, 75, 70, 65, 60 | 88, 82, 78]
                  ^^^^^^^^^^^^^^^^^^^^^^^^^^^^   ^^^^^^^^^^
                  Keep best 7 from current       Replace worst 3 with best offspring
```

## Algorithm Pseudocode

```
1. Validate replacementCount > 0 and < populationSize
2. Evaluate fitness for current population
3. Sort current population by fitness (descending for max)
4. Identify worst replacementCount individuals
5. Evaluate fitness for offspring
6. Sort offspring by fitness
7. Select best replacementCount offspring
8. Create new population:
   - Keep top (populationSize - replacementCount) from current
   - Add selected offspring
9. Return new population
```

## Replacement Strategies

**Fitness-Based (Standard):**
- Replace individuals with worst fitness
- Direct selection pressure

**Age-Based:**
- Replace oldest individuals regardless of fitness
- Maintains diversity better
- Requires tracking individual age

**Parent Replacement:**
- Replace the parents that generated the offspring
- Maintains population structure

**Tournament-Based:**
- Run reverse tournament to select individuals for replacement
- More stochastic than pure fitness-based

## Configuration Options

- **replacementCount**: Number of individuals to replace per generation (default: 2 or populationSize * 0.1)
- **replacementMode**: FITNESS, AGE, or PARENT (default: FITNESS)
- **allowParentReplacement**: Whether offspring can replace their parents (default: true)

## Convergence Characteristics

- **Smoother convergence** than generational
- **Higher diversity** maintained
- **Slower to converge** but may find better solutions
- **More stable** best fitness progression
- **Less sensitive** to parameter settings

## Comparison Table

| Characteristic | Generational | Steady-State |
|----------------|--------------|--------------|
| Replacement Rate | 100% | 10-20% |
| Diversity | Lower | Higher |
| Convergence | Faster | Slower |
| Population Overlap | Low | High |
| Computational Cost | Bursty | Smooth |

## Related Issues

- Implements: ReplacementStrategy interface
- Alternative to: Issue #16 (ElitismReplacement), Issue #17 (GenerationalReplacement)
- Used by: Issue #1 (Core GeneticAlgorithm Engine)
