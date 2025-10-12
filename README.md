# soft-computing-util

A comprehensive Genetic Algorithm library for Java, implementing modular evolutionary computing components with clean architecture and design patterns.

## 📋 Quick Links

- **[HOW TO CREATE GITHUB ISSUES](HOW_TO_CREATE_ISSUES.md)** ⭐ Start here to create all 20 Phase 1 issues
- **[Copilot Response](COPILOT_RESPONSE.md)** - Understanding issue creation and limitations
- **[Issue Creation Guide](ISSUE_CREATION_GUIDE.md)** - Detailed guide with all methods
- **[Phase 1 Summary](PHASE1_ISSUES_SUMMARY.md)** - Implementation roadmap
- **[Issues Preview](ISSUES_PREVIEW.md)** - Visual guide to all issues

## 🚀 Quick Start: Create GitHub Issues

Your repository has 20 comprehensive issue templates for Phase 1 implementation.

**Status:** 3 created, 17 remaining

**Create all remaining issues:**
```bash
./create-remaining-issues.sh --dry-run  # Preview first
./create-remaining-issues.sh            # Create issues
```

## 🏗️ Project Structure

```
soft-computing-util/
├── .github/
│   └── ISSUE_TEMPLATE/          # 20 issue templates for Phase 1
├── soft-util/                   # Java source code
│   ├── src/main/java/          # Implementation
│   └── src/test/java/          # Tests
├── create-issues.sh             # Original script (creates all 20)
├── create-remaining-issues.sh   # Smart script (creates only missing)
└── Documentation files          # Guides and roadmaps
```

## 📦 What's Included

### Issue Templates (20)
- **Core Structure** (3): GA Engine, Population, Fitness Function
- **Chromosomes** (3): Binary, Integer, Floating Point
- **Selection** (3): Roulette Wheel, Tournament, Rank
- **Crossover** (3): Single Point, Two Point, Uniform
- **Mutation** (3): Binary, Integer, Floating Point
- **Replacement** (3): Elitism, Generational, Steady State
- **Utilities** (2): Configuration, Constraint Handling

### Documentation
- Phase 1 implementation roadmap
- Issue creation guides
- Architecture overview
- Testing strategies

### Automation Scripts
- Smart issue creation script
- Original bulk creation script

## 🎯 Phase 1 Implementation Plan

### Phase 1a: Foundation (Weeks 1-2)
- FitnessFunction Interface
- BinaryChromosome
- Population Management

### Phase 1b: Basic Operators (Weeks 3-4)
- TournamentSelection
- SinglePointCrossover
- BinaryMutation
- GenerationalReplacement

### Phase 1c: Core Integration (Weeks 5-6)
- Core GeneticAlgorithm Engine
- ElitismReplacement
- Configuration System

### Phase 1d: Extensions (Weeks 7-10)
- Additional chromosome types
- More selection/crossover/mutation algorithms
- Advanced replacement strategies
- Constraint handling

## 🔧 Technology Stack

- **Language:** Java 17+
- **Build Tool:** Maven
- **Testing:** JUnit 5
- **Design Patterns:** Strategy, Builder, Template Method

## 🤝 Contributing

1. Create GitHub issues from templates
2. Assign issues to team members
3. Follow the implementation guide
4. Submit pull requests
5. Ensure tests pass

## 📚 Resources

- [Genetic Algorithms (Wikipedia)](https://en.wikipedia.org/wiki/Genetic_algorithm)
- [Strategy Pattern](https://refactoring.guru/design-patterns/strategy)
- [Builder Pattern](https://refactoring.guru/design-patterns/builder)

## 📄 License

MIT License - See [LICENSE](LICENSE) file for details.

---

**Ready to start?** Read [HOW_TO_CREATE_ISSUES.md](HOW_TO_CREATE_ISSUES.md) to create your GitHub issues!