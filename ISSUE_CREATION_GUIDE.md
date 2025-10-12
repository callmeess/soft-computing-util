# Issue Creation Guide

## Current Status

✅ **3 out of 20 issues have been created:**
- Issue #4: Implement Core GeneticAlgorithm Engine
- Issue #6: Implement Population Management  
- Issue #7: Implement FitnessFunction Interface

❌ **17 issues remaining to be created**

## Why Can't Copilot Create Issues Directly?

The GitHub Copilot agent running in the workspace does not have direct access to GitHub credentials for creating issues. However, there are several ways you can create the remaining issues.

## Option 1: Use the Automated Script (Recommended)

The repository includes a `create-issues.sh` script that can create all issues automatically.

### Prerequisites
1. Install GitHub CLI: https://cli.github.com/
   ```bash
   # macOS
   brew install gh
   
   # Linux
   sudo apt install gh
   
   # Windows
   winget install --id GitHub.cli
   ```

2. Authenticate with GitHub:
   ```bash
   gh auth login
   ```

### Create All Issues
```bash
cd /path/to/soft-computing-util
chmod +x create-issues.sh
./create-issues.sh
```

The script will:
- Check for GitHub CLI installation
- Verify authentication
- Create the "Phase 1" milestone if it doesn't exist
- Create all 20 issues from templates
- Apply appropriate labels
- Handle rate limiting automatically

## Option 2: Use GitHub Web Interface

### Manual Creation (For Individual Issues)

1. Go to https://github.com/Eslam-Sayed7/soft-computing-util/issues
2. Click "New Issue"
3. Select one of the templates from the list
4. Review the pre-filled content
5. Click "Submit new issue"

### Bulk Creation Using GitHub UI

Repeat the above steps for each of the 17 remaining issues:

**Remaining Issues to Create:**
- [ ] Issue: Implement BinaryChromosome (Template: `04-binary-chromosome.md`)
- [ ] Issue: Implement IntegerChromosome (Template: `05-integer-chromosome.md`)
- [ ] Issue: Implement FloatingPointChromosome (Template: `06-floating-point-chromosome.md`)
- [ ] Issue: Implement RouletteWheelSelection (Template: `07-roulette-wheel-selection.md`)
- [ ] Issue: Implement TournamentSelection (Template: `08-tournament-selection.md`)
- [ ] Issue: Implement RankSelection (Template: `09-rank-selection.md`)
- [ ] Issue: Implement SinglePointCrossover (Template: `10-single-point-crossover.md`)
- [ ] Issue: Implement TwoPointCrossover (Template: `11-two-point-crossover.md`)
- [ ] Issue: Implement UniformCrossover (Template: `12-uniform-crossover.md`)
- [ ] Issue: Implement BinaryMutation (Template: `13-binary-mutation.md`)
- [ ] Issue: Implement IntegerMutation (Template: `14-integer-mutation.md`)
- [ ] Issue: Implement FloatingPointMutation (Template: `15-floating-point-mutation.md`)
- [ ] Issue: Implement ElitismReplacement (Template: `16-elitism-replacement.md`)
- [ ] Issue: Implement GenerationalReplacement (Template: `17-generational-replacement.md`)
- [ ] Issue: Implement SteadyStateReplacement (Template: `18-steady-state-replacement.md`)
- [ ] Issue: Implement Configuration & Parameter Defaults (Template: `19-configuration-parameter-defaults.md`)
- [ ] Issue: Implement InfeasibleSolutionHandler (Template: `20-infeasible-solution-handler.md`)

## Option 3: Use GitHub CLI Manually

For more control, create issues individually:

```bash
# Example: Create BinaryChromosome issue
gh issue create \
  --title "Implement BinaryChromosome" \
  --body-file .github/ISSUE_TEMPLATE/04-binary-chromosome.md \
  --label "implementation,algorithm,phase1,chromosome" \
  --milestone "Phase 1"

# Repeat for other issues...
```

## Option 4: Use GitHub API

If you prefer using the API directly:

```bash
# Set your GitHub token
export GITHUB_TOKEN="your_token_here"

# Create an issue
curl -X POST \
  -H "Authorization: token $GITHUB_TOKEN" \
  -H "Accept: application/vnd.github+json" \
  https://api.github.com/repos/Eslam-Sayed7/soft-computing-util/issues \
  -d '{
    "title": "Implement BinaryChromosome",
    "body": "..."
    "labels": ["implementation", "algorithm", "phase1", "chromosome"],
    "milestone": 1
  }'
```

## Recommended Approach

**For fastest bulk creation:** Use Option 1 (automated script)
- Takes ~2 minutes
- Creates all issues consistently
- Handles errors gracefully

**For selective creation:** Use Option 2 (GitHub UI)
- Good for creating a few specific issues
- Visual interface
- Easy to preview before creating

## After Creating Issues

1. Review created issues on GitHub
2. Assign issues to team members
3. Set up a project board (optional)
4. Start implementation following `PHASE1_ISSUES_SUMMARY.md`

## Troubleshooting

### "gh: command not found"
Install GitHub CLI following the instructions in Option 1.

### "error: failed to create issue"
- Check you're authenticated: `gh auth status`
- Verify you have write permissions to the repository
- Check if you've hit GitHub rate limits (wait a few minutes)

### "milestone not found"
The script will attempt to create the milestone. If it fails:
```bash
gh api repos/Eslam-Sayed7/soft-computing-util/milestones \
  -X POST \
  -f title='Phase 1' \
  -f description='Initial implementation of core GA framework' \
  -f state='open'
```

## Need Help?

- Review `.github/ISSUE_TEMPLATE/README.md` for template documentation
- Check `ISSUES_PREVIEW.md` for visual guide
- See `PHASE1_ISSUES_SUMMARY.md` for implementation roadmap

---

**Note:** Once issues are created, you can track progress using GitHub's built-in project management features or create a project board to organize the work.
