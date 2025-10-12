# GitHub Issue Templates for Genetic Algorithm Library - Phase 1

This directory contains 20 comprehensive issue templates for implementing a complete Genetic Algorithm library in Java. Each template provides detailed specifications, acceptance criteria, and implementation guidance.

## 📋 Overview

The templates are organized into six categories covering all aspects of a modular, extensible GA framework:

### 🧱 Core Structure (Issues 1-3)
1. **Core GeneticAlgorithm Engine** - Main GA workflow orchestrator
2. **Population Management** - Chromosome collection management
3. **FitnessFunction Interface** - Problem-specific fitness evaluation

### 🧬 Chromosome Types (Issues 4-6)
4. **BinaryChromosome** - Bitstring representation
5. **IntegerChromosome** - Integer vector representation
6. **FloatingPointChromosome** - Real-valued vector representation

### 🎯 Selection Algorithms (Issues 7-9)
7. **RouletteWheelSelection** - Fitness-proportionate selection
8. **TournamentSelection** - Tournament-based selection
9. **RankSelection** - Rank-based selection

### 🔗 Crossover Algorithms (Issues 10-12)
10. **SinglePointCrossover** - One-point recombination
11. **TwoPointCrossover** - Two-point recombination
12. **UniformCrossover** - Gene-level mixing

### 🧫 Mutation Algorithms (Issues 13-15)
13. **BinaryMutation** - Bit-flip mutation
14. **IntegerMutation** - Integer perturbation
15. **FloatingPointMutation** - Gaussian mutation

### 🔁 Replacement Strategies (Issues 16-18)
16. **ElitismReplacement** - Preserve best solutions
17. **GenerationalReplacement** - Full population replacement
18. **SteadyStateReplacement** - Incremental replacement

### ⚙️ Integration & Utilities (Issues 19-20)
19. **Configuration & Parameter Defaults** - Configuration system
20. **InfeasibleSolutionHandler** - Constraint handling

## 🏷️ Labels Used

Each issue template includes these labels:
- `implementation` - Code implementation required
- `algorithm` - Algorithm implementation
- `phase1` - Part of Phase 1 milestone
- **Category labels**: `core`, `chromosome`, `selection`, `crossover`, `mutation`, `replacement`

## 🎯 Milestone

All issues belong to **Phase 1** milestone.

## 📝 How to Create Issues from Templates

### Option 1: Using GitHub UI
1. Go to the repository's Issues tab
2. Click "New Issue"
3. Select the appropriate template
4. The title, description, labels, and milestone will be pre-filled
5. Click "Submit new issue"

### Option 2: Bulk Creation Script

You can create all 20 issues programmatically using the GitHub CLI:

```bash
# Install GitHub CLI if needed
# brew install gh  # macOS
# Or download from: https://cli.github.com/

# Authenticate
gh auth login

# Navigate to repository
cd soft-computing-util

# Create issues from templates (example for one issue)
gh issue create \
  --title "Implement Core GeneticAlgorithm Engine" \
  --body-file .github/ISSUE_TEMPLATE/01-core-genetic-algorithm-engine.md \
  --label "implementation,algorithm,phase1,core" \
  --milestone "Phase 1"

# Repeat for all 20 templates...
```

### Option 3: Automated Script

Create a script to process all templates at once:

```bash
#!/bin/bash
for file in .github/ISSUE_TEMPLATE/*.md; do
  if [[ $file != *"README.md"* ]]; then
    # Extract title from front matter
    title=$(grep "^title:" "$file" | sed "s/title: '\(.*\)'/\1/")
    
    # Extract labels from front matter
    labels=$(grep "^labels:" "$file" | sed "s/labels: //")
    
    # Create issue
    gh issue create \
      --title "$title" \
      --body-file "$file" \
      --label "$labels" \
      --milestone "Phase 1"
    
    echo "Created issue: $title"
    sleep 2  # Avoid rate limiting
  fi
done
```

## 📖 Template Structure

Each template follows this structure:

```markdown
---
name: [Issue Name]
about: [Brief description]
title: '[Issue Title]'
labels: [comma-separated labels]
assignees: ''
milestone: Phase 1
---

## Summary
[High-level overview]

## Description
[Detailed explanation]

## Tasks
- [ ] Task checklist

## Acceptance Criteria
- [ ] Criteria checklist

## Technical Notes
[Implementation guidance]

## Example Usage
[Code examples]

## Related Issues
[Dependencies and relationships]
```

## 🔄 Implementation Order

Recommended order for implementation:

**Phase 1a - Foundation:**
1. Issue #3 (FitnessFunction Interface)
2. Issue #4 (BinaryChromosome)
3. Issue #2 (Population Management)

**Phase 1b - Basic Operators:**
4. Issue #7 (RouletteWheelSelection) or #8 (TournamentSelection)
5. Issue #10 (SinglePointCrossover)
6. Issue #13 (BinaryMutation)

**Phase 1c - Core Engine:**
7. Issue #1 (Core GeneticAlgorithm Engine)
8. Issue #16 (ElitismReplacement)
9. Issue #19 (Configuration)

**Phase 1d - Extensions:**
10. Issues #5-6 (Other Chromosome Types)
11. Issues #9, #11-12, #14-15, #17-18 (Additional Algorithms)
12. Issue #20 (Constraint Handling)

## 🎓 Design Principles

All implementations should follow these principles:

1. **Strategy Pattern**: Use interfaces for all algorithm components
2. **Builder Pattern**: Use builders for configuration
3. **Open/Closed Principle**: Easy to add new strategies without modifying existing code
4. **Clean Code**: Well-documented, tested, maintainable
5. **Generic Programming**: Support multiple chromosome types
6. **Immutability**: Prefer immutable designs where appropriate

## 📚 Additional Resources

- [Genetic Algorithm Tutorial](https://en.wikipedia.org/wiki/Genetic_algorithm)
- [GA Parameter Tuning Guide](https://www.sciencedirect.com/topics/computer-science/genetic-algorithm)
- Design Patterns: Strategy, Builder, Template Method

## 🤝 Contributing

When implementing these issues:
- Follow the acceptance criteria strictly
- Write comprehensive unit tests
- Include JavaDoc documentation
- Provide usage examples
- Update README with implementation status

## 📧 Questions?

For questions about these templates or the implementation plan, please open a discussion in the repository.

---

**Total Issues**: 20  
**Estimated Effort**: 8-12 weeks for complete Phase 1 implementation  
**Lines of Code**: ~5,000-7,000 LOC expected
