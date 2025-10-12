---
name: Implement FitnessFunction Interface
about: Define interface for problem-specific fitness evaluation
title: 'Implement FitnessFunction Interface'
labels: implementation, algorithm, phase1, core
assignees: ''
milestone: Phase 1
---

## Summary

Define a `FitnessFunction` interface that allows users to implement problem-specific fitness evaluation logic. This interface will be injected into the genetic algorithm to evaluate the quality of chromosome solutions.

## Description

The FitnessFunction interface is the bridge between the generic GA framework and specific optimization problems. It must provide:

- A clear contract for evaluating chromosome fitness
- Support for different chromosome types
- Ability to handle both maximization and minimization problems
- Optional validation of chromosome feasibility
- Clear documentation for implementers

This interface enables the Strategy pattern, allowing users to swap fitness functions without modifying the core GA logic.

## Tasks

- [ ] Define `FitnessFunction<T>` generic interface
- [ ] Add `evaluate(T chromosome)` method returning fitness value
- [ ] Add `isMaximization()` method to indicate optimization direction
- [ ] Consider adding `isValid(T chromosome)` method for constraint checking
- [ ] Add JavaDoc documentation with implementation examples
- [ ] Create abstract base class `AbstractFitnessFunction<T>` with common utilities
- [ ] Implement 2-3 example fitness functions for testing:
  - [ ] OneMaxProblem (for binary chromosomes)
  - [ ] SphereFunction (for floating-point chromosomes)
  - [ ] SimpleIntegerSum (for integer chromosomes)
- [ ] Add utility methods for common fitness transformations
- [ ] Consider caching mechanisms for expensive evaluations
- [ ] Add validation helpers for common constraints

## Acceptance Criteria

- [ ] FitnessFunction interface is properly defined with generics
- [ ] Interface can be easily implemented for different problem types
- [ ] Works seamlessly with all chromosome types (Binary, Integer, Float)
- [ ] Can be injected into Population and GeneticAlgorithm classes
- [ ] Example implementations demonstrate correct usage
- [ ] Documentation clearly explains contract and usage
- [ ] Support for both maximization and minimization
- [ ] Validation methods help identify infeasible solutions
- [ ] No performance bottlenecks in the interface design
- [ ] Unit tests validate example implementations

## Technical Notes

- Use `double` as the return type for fitness values
- Consider making the interface functional (`@FunctionalInterface`)
- Ensure thread-safety for concurrent fitness evaluation
- Document expected behavior for invalid/infeasible chromosomes
- Consider providing fitness scaling/normalization utilities

## Example Usage

```java
public class OneMaxFitness implements FitnessFunction<BinaryChromosome> {
    @Override
    public double evaluate(BinaryChromosome chromosome) {
        return chromosome.countOnes();
    }
    
    @Override
    public boolean isMaximization() {
        return true;
    }
}
```

## Related Issues

- Used by: Issue #2 (Population Management)
- Used by: Issue #1 (Core GeneticAlgorithm Engine)
- Related to: Issues #4-6 (Chromosome implementations)
