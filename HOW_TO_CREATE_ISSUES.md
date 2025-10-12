# Quick Guide: Creating GitHub Issues from Templates

## TL;DR

**You have 3 options:**

### ⚡ Option 1: Quick Automated Script (Fastest - Recommended)
```bash
./create-remaining-issues.sh
```

### 🔍 Option 2: Preview First (Safe)
```bash
./create-remaining-issues.sh --dry-run
```

### 🌐 Option 3: Use GitHub Web UI (Manual but easy)
Go to: https://github.com/Eslam-Sayed7/soft-computing-util/issues/new/choose

---

## Current Status

✅ **3 issues created** (Issues #4, #6, #7)  
⏳ **17 issues remaining**

## What's Available

### Scripts in this Repository

1. **`create-issues.sh`** - Original script to create all 20 issues
   - Creates all issues (including duplicates of already-created ones)
   - Use if you want to start fresh or haven't created any yet

2. **`create-remaining-issues.sh`** - Smart script (NEW! ⭐)
   - Only creates issues that don't already exist
   - Shows preview of what will be created
   - Supports `--dry-run` mode
   - **Recommended for your situation**

3. **`ISSUE_CREATION_GUIDE.md`** - Detailed guide
   - All possible methods explained
   - Troubleshooting tips
   - API examples

## Why Can't the AI Create Issues Directly?

The GitHub Copilot workspace agent doesn't have GitHub API credentials, so it can't create issues programmatically. However, the scripts provided will work when **you** run them (with your GitHub credentials).

## Prerequisites

You need GitHub CLI installed and authenticated:

```bash
# Check if you have it
gh --version

# If not, install it
# macOS:  brew install gh
# Linux:  sudo apt install gh  
# Windows: winget install GitHub.cli

# Authenticate
gh auth login
```

## Recommended Workflow

### Step 1: Preview what will be created
```bash
cd /path/to/soft-computing-util
./create-remaining-issues.sh --dry-run
```

This shows you:
- Which issues already exist ✓
- Which issues will be created ○
- No actual changes made

### Step 2: Create the issues
```bash
./create-remaining-issues.sh
```

The script will:
1. Check existing issues
2. Ask for confirmation
3. Create only the missing issues
4. Show progress for each issue
5. Display final summary

### Step 3: Verify
```bash
gh issue list --limit 25
```

Or visit: https://github.com/Eslam-Sayed7/soft-computing-util/issues

## What Issues Will Be Created?

The remaining 17 issues are:

**Chromosome Types (3):**
- [ ] Implement BinaryChromosome
- [ ] Implement IntegerChromosome
- [ ] Implement FloatingPointChromosome

**Selection Algorithms (3):**
- [ ] Implement RouletteWheelSelection
- [ ] Implement TournamentSelection
- [ ] Implement RankSelection

**Crossover Algorithms (3):**
- [ ] Implement SinglePointCrossover
- [ ] Implement TwoPointCrossover
- [ ] Implement UniformCrossover

**Mutation Algorithms (3):**
- [ ] Implement BinaryMutation
- [ ] Implement IntegerMutation
- [ ] Implement FloatingPointMutation

**Replacement Strategies (3):**
- [ ] Implement ElitismReplacement
- [ ] Implement GenerationalReplacement
- [ ] Implement SteadyStateReplacement

**Utilities (2):**
- [ ] Implement Configuration & Parameter Defaults
- [ ] Implement InfeasibleSolutionHandler

## Troubleshooting

### "gh: command not found"
Install GitHub CLI: https://cli.github.com/

### "Not authenticated"
Run: `gh auth login`

### "Permission denied"
Run: `chmod +x create-remaining-issues.sh`

### Want to see what would happen first?
Use dry-run mode: `./create-remaining-issues.sh --dry-run`

## Alternative: Manual Creation via Web

If you prefer not to use the command line:

1. Go to: https://github.com/Eslam-Sayed7/soft-computing-util/issues/new/choose
2. Pick a template (you'll see all 20 listed)
3. Click on the one you want
4. Review and submit
5. Repeat for each issue

## Need More Help?

- **Detailed guide:** See `ISSUE_CREATION_GUIDE.md`
- **Templates info:** See `.github/ISSUE_TEMPLATE/README.md`
- **Implementation plan:** See `PHASE1_ISSUES_SUMMARY.md`
- **Preview of issues:** See `ISSUES_PREVIEW.md`

---

**Ready to create issues?** Run: `./create-remaining-issues.sh --dry-run` first!
