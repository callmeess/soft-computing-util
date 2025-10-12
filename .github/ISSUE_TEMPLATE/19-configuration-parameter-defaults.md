---
name: Implement Configuration & Parameter Defaults
about: Create configuration system with sensible default parameters
title: 'Implement Configuration & Parameter Defaults'
labels: implementation, algorithm, phase1, core
assignees: ''
milestone: Phase 1
---

## Summary

Implement a configuration system that stores default GA parameters and allows user customization via Builder pattern. This provides sensible defaults while enabling easy parameter tuning for specific problems.

## Description

The configuration system centralizes all GA parameters and provides a clean interface for customization. It must:

- Define default values for all GA parameters
- Validate parameter ranges and constraints
- Support Builder pattern for configuration
- Provide presets for common problem types
- Allow parameter override at runtime
- Include documentation for each parameter
- Support parameter persistence and loading
- Validate parameter combinations

This system makes the GA framework easier to use while maintaining flexibility for advanced users.

## Tasks

- [ ] Create `GAConfiguration` class with all parameters
- [ ] Define default values for population size (100)
- [ ] Define default crossover probability (0.8)
- [ ] Define default mutation probability (1/chromosomeLength)
- [ ] Define default selection pressure (for rank selection)
- [ ] Define default tournament size (3)
- [ ] Define default elite count (2 or 5%)
- [ ] Define default maximum generations (1000)
- [ ] Add Builder for GAConfiguration
- [ ] Add validation for all parameters
- [ ] Validate ranges (probabilities in [0,1], sizes > 0)
- [ ] Validate logical constraints (eliteCount < populationSize)
- [ ] Create preset configurations:
  - [ ] Standard GA (default settings)
  - [ ] Fast convergence (high selection pressure, large elite)
  - [ ] High diversity (low selection pressure, small elite)
  - [ ] Micro GA (small population)
- [ ] Add methods to export/import configuration (JSON/Properties)
- [ ] Add toString() for debugging
- [ ] Create unit tests for validation
- [ ] Document recommended values for different problem types

## Acceptance Criteria

- [ ] All GA parameters have sensible defaults
- [ ] Parameters can be overridden via Builder
- [ ] Invalid parameters are rejected with clear error messages
- [ ] Parameter validation catches logical inconsistencies
- [ ] Preset configurations work out-of-box
- [ ] Configuration can be saved and loaded
- [ ] Builder pattern provides fluent API
- [ ] Immutable after construction (or defensive copies)
- [ ] Unit tests verify all validation rules
- [ ] Documentation includes parameter tuning guide

## Technical Notes

- Consider making GAConfiguration immutable
- Use Builder pattern for construction
- Provide static factory methods for presets
- Include parameter interdependencies in validation
- Consider parameter auto-tuning based on problem characteristics

## Parameters to Include

### Population Parameters
- **populationSize**: Number of individuals (default: 100)
- **chromosomeLength**: Length of chromosomes (problem-specific)
- **chromosomeType**: Binary, Integer, or FloatingPoint

### Genetic Operators
- **crossoverProbability**: Probability of applying crossover (default: 0.8)
- **mutationProbability**: Probability per gene (default: 1/length)
- **crossoverType**: SinglePoint, TwoPoint, Uniform
- **mutationType**: BitFlip, Gaussian, Uniform

### Selection Parameters
- **selectionType**: Roulette, Tournament, Rank
- **tournamentSize**: For tournament selection (default: 3)
- **selectionPressure**: For rank selection (default: 1.5)

### Replacement Parameters
- **replacementType**: Generational, SteadyState, Elitism
- **eliteCount**: Number of elites (default: 2)
- **replacementCount**: For steady-state (default: 10% of population)

### Termination Conditions
- **maxGenerations**: Maximum iterations (default: 1000)
- **targetFitness**: Stop when reached (optional)
- **convergenceThreshold**: Stop when no improvement (optional)
- **timeLimit**: Maximum runtime in seconds (optional)

## Example Usage

```java
// Use default configuration
GAConfiguration config = new GAConfiguration.Builder()
    .build();

// Custom configuration
GAConfiguration config = new GAConfiguration.Builder()
    .populationSize(200)
    .crossoverProbability(0.9)
    .mutationProbability(0.01)
    .eliteCount(5)
    .maxGenerations(500)
    .build();

// Use preset
GAConfiguration fastConvergence = 
    GAConfiguration.Presets.FAST_CONVERGENCE;

// Configure GA with configuration
GeneticAlgorithm ga = new GeneticAlgorithm.Builder()
    .withConfiguration(config)
    .withFitnessFunction(myFitness)
    .build();
```

## Configuration Presets

### Standard (Default)
```java
populationSize: 100
crossoverProbability: 0.8
mutationProbability: 1/L
tournamentSize: 3
eliteCount: 2
maxGenerations: 1000
```

### Fast Convergence
```java
populationSize: 50
selectionPressure: 1.9 (high)
crossoverProbability: 0.9
eliteCount: 10% of population
```

### High Diversity
```java
populationSize: 200
mutationProbability: 3/L (high)
tournamentSize: 2 (low pressure)
eliteCount: 1
```

### Micro GA
```java
populationSize: 5-10
eliteCount: 1
Restart when converged
```

## Validation Rules

- `0 < populationSize <= 10000`
- `0.0 <= crossoverProbability <= 1.0`
- `0.0 <= mutationProbability <= 1.0`
- `2 <= tournamentSize <= populationSize`
- `1.0 <= selectionPressure <= 2.0`
- `0 <= eliteCount < populationSize`
- `maxGenerations > 0`
- `eliteCount + replacementCount <= populationSize` (for steady-state)

## Related Issues

- Used by: Issue #1 (Core GeneticAlgorithm Engine)
- Related to: All algorithm implementations
- Supports: All strategy interfaces
