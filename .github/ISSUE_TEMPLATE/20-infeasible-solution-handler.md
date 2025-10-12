---
name: Implement InfeasibleSolutionHandler
about: Detect and repair constraint-violating chromosomes
title: 'Implement InfeasibleSolutionHandler'
labels: implementation, algorithm, phase1, core
assignees: ''
milestone: Phase 1
---

## Summary

Implement an InfeasibleSolutionHandler that detects constraint violations in chromosomes and applies repair strategies to make them feasible. This ensures that all chromosomes in the population satisfy problem-specific constraints.

## Description

Many optimization problems have constraints that must be satisfied for a solution to be valid. The InfeasibleSolutionHandler:

- Detects constraint violations in chromosomes
- Applies repair strategies to fix infeasible solutions
- Supports multiple repair methods (clamping, projection, penalty)
- Validates chromosomes before fitness evaluation
- Provides extensible framework for custom constraints
- Works with all chromosome types
- Maintains efficient validation

This component is essential for constrained optimization problems.

## Tasks

- [ ] Create `InfeasibleSolutionHandler<T>` interface
- [ ] Define `isValid(T chromosome)` method
- [ ] Define `repair(T chromosome)` method returning fixed chromosome
- [ ] Create `BoundsConstraint<T>` for range constraints
- [ ] Implement bounds checking for IntegerChromosome
- [ ] Implement bounds checking for FloatingPointChromosome
- [ ] Create `ClampingRepair` strategy (bring to nearest valid value)
- [ ] Create `ProjectionRepair` strategy (project onto feasible region)
- [ ] Create `RandomResetRepair` strategy (regenerate invalid genes)
- [ ] Create `PenaltyHandler` (reduce fitness instead of repair)
- [ ] Implement constraint composition (AND/OR of multiple constraints)
- [ ] Add validation in Population initialization
- [ ] Add validation after genetic operators
- [ ] Create custom constraint examples:
  - [ ] SumConstraint (sum of genes must equal value)
  - [ ] OrderConstraint (genes must be in order)
  - [ ] UniquenessConstraint (all genes must be unique)
- [ ] Add unit tests for each repair strategy
- [ ] Document when to use each repair method

## Acceptance Criteria

- [ ] Can detect constraint violations in chromosomes
- [ ] Provides multiple repair strategies
- [ ] Repaired chromosomes are always feasible
- [ ] Repair strategies are problem-appropriate
- [ ] Works with all chromosome types
- [ ] Minimal fitness distortion from repairs
- [ ] Efficient validation (O(1) to O(n) depending on constraints)
- [ ] Extensible for custom constraints
- [ ] Integrates seamlessly with GA workflow
- [ ] Handles complex constraint combinations
- [ ] Unit tests verify repair correctness
- [ ] Documentation includes constraint definition examples

## Technical Notes

- Validation should happen after:
  - Population initialization
  - Crossover operations
  - Mutation operations
- Consider constraint checking overhead
- Some problems better handled by penalty functions
- Death penalty: reject infeasible solutions entirely
- Repair can change solution significantly
- Balance between repair effort and fitness preservation

## Constraint Types

### Bounds Constraints
- Each gene must be within [min, max]
- Most common constraint type
- Easy to check and repair

### Equality Constraints
- Some property must equal target value
- Example: sum of weights = capacity
- Often require projection or normalization

### Inequality Constraints
- Some property must be ≤ or ≥ value
- Example: total cost ≤ budget
- May need iterative repair

### Structural Constraints
- Genes must have specific relationships
- Example: permutations must be valid
- Example: dependencies between genes

## Repair Strategies

### 1. Clamping (Simplest)
```java
gene = Math.max(min, Math.min(gene, max))
```
- Pros: Fast, deterministic
- Cons: May introduce bias toward boundaries

### 2. Projection
```java
// Project onto nearest point in feasible region
// Complex for non-linear constraints
```
- Pros: Minimal distortion
- Cons: Computationally expensive

### 3. Random Reset
```java
gene = random(min, max)
```
- Pros: Maintains diversity
- Cons: Large change to solution

### 4. Penalty (No Repair)
```java
if (!isValid(chromosome)) {
    fitness = fitness * penaltyFactor;
}
```
- Pros: Preserves original solution
- Cons: Infeasible solutions in population

### 5. Rejection (Death Penalty)
```java
if (!isValid(chromosome)) {
    fitness = worst_possible;
}
```
- Pros: Only feasible solutions considered
- Cons: May waste evaluations

## Example Usage

```java
// Define bounds constraint
BoundsConstraint<IntegerChromosome> bounds = 
    new BoundsConstraint<>(0, 100);

// Create handler with clamping repair
InfeasibleSolutionHandler<IntegerChromosome> handler = 
    new InfeasibleSolutionHandler<>(bounds, new ClampingRepair());

// Validate and repair chromosome
IntegerChromosome chromosome = ...;
if (!handler.isValid(chromosome)) {
    chromosome = handler.repair(chromosome);
}

// Or integrate with GA
GeneticAlgorithm ga = new GeneticAlgorithm.Builder()
    .withInfeasibleSolutionHandler(handler)
    .build();
```

## Example Constraints

### Sum Constraint (Knapsack)
```java
public class SumConstraint implements Constraint<IntegerChromosome> {
    private final int targetSum;
    
    @Override
    public boolean isValid(IntegerChromosome chromosome) {
        int sum = Arrays.stream(chromosome.getGenes()).sum();
        return sum <= targetSum;
    }
}
```

### Permutation Constraint (TSP)
```java
public class PermutationConstraint implements Constraint<IntegerChromosome> {
    @Override
    public boolean isValid(IntegerChromosome chromosome) {
        int[] genes = chromosome.getGenes();
        return hasAllUniqueValues(genes) && 
               hasValuesInRange(genes, 0, genes.length - 1);
    }
}
```

## Integration Points

1. **Population Initialization**
   - Validate after creating random chromosomes
   - Repair before adding to population

2. **After Crossover**
   - Check offspring for constraint violations
   - Repair before adding to population

3. **After Mutation**
   - Mutation may violate constraints
   - Repair before fitness evaluation

4. **Before Fitness Evaluation**
   - Final validation check
   - Ensure all evaluated solutions are feasible

## Configuration Options

- **repairStrategy**: CLAMP, PROJECT, RESET, PENALTY, REJECT
- **validationPoints**: INIT, CROSSOVER, MUTATION, PRE_EVAL, ALL
- **penaltyFactor**: Multiplier for penalty method (default: 0.5)
- **maxRepairAttempts**: Maximum tries before rejection (default: 10)

## Related Issues

- Used by: Issue #1 (Core GeneticAlgorithm Engine)
- Used by: Issue #2 (Population Management)
- Works with: Issue #4-6 (Chromosome implementations)
- Complements: Issue #3 (FitnessFunction Interface)
