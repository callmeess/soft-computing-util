#!/bin/bash

# Script to create all 20 Phase 1 GitHub issues from templates
# Requires: GitHub CLI (gh) installed and authenticated
# Usage: ./create-issues.sh

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${GREEN}=====================================${NC}"
echo -e "${GREEN}Phase 1 Issue Creation Script${NC}"
echo -e "${GREEN}=====================================${NC}"
echo ""

# Check if gh is installed
if ! command -v gh &> /dev/null; then
    echo -e "${RED}Error: GitHub CLI (gh) is not installed${NC}"
    echo "Please install it from: https://cli.github.com/"
    exit 1
fi

# Check if authenticated
if ! gh auth status &> /dev/null; then
    echo -e "${RED}Error: Not authenticated with GitHub${NC}"
    echo "Please run: gh auth login"
    exit 1
fi

# Check if we're in the right directory
if [ ! -d ".github/ISSUE_TEMPLATE" ]; then
    echo -e "${RED}Error: .github/ISSUE_TEMPLATE directory not found${NC}"
    echo "Please run this script from the repository root"
    exit 1
fi

# Define issue data (title, file, labels)
declare -a ISSUES=(
    "Implement Core GeneticAlgorithm Engine|01-core-genetic-algorithm-engine.md|implementation,algorithm,phase1,core"
    "Implement Population Management|02-population-management.md|implementation,algorithm,phase1,core"
    "Implement FitnessFunction Interface|03-fitness-function-interface.md|implementation,algorithm,phase1,core"
    "Implement BinaryChromosome|04-binary-chromosome.md|implementation,algorithm,phase1,chromosome"
    "Implement IntegerChromosome|05-integer-chromosome.md|implementation,algorithm,phase1,chromosome"
    "Implement FloatingPointChromosome|06-floating-point-chromosome.md|implementation,algorithm,phase1,chromosome"
    "Implement RouletteWheelSelection|07-roulette-wheel-selection.md|implementation,algorithm,phase1,selection"
    "Implement TournamentSelection|08-tournament-selection.md|implementation,algorithm,phase1,selection"
    "Implement RankSelection|09-rank-selection.md|implementation,algorithm,phase1,selection"
    "Implement SinglePointCrossover|10-single-point-crossover.md|implementation,algorithm,phase1,crossover"
    "Implement TwoPointCrossover|11-two-point-crossover.md|implementation,algorithm,phase1,crossover"
    "Implement UniformCrossover|12-uniform-crossover.md|implementation,algorithm,phase1,crossover"
    "Implement BinaryMutation|13-binary-mutation.md|implementation,algorithm,phase1,mutation"
    "Implement IntegerMutation|14-integer-mutation.md|implementation,algorithm,phase1,mutation"
    "Implement FloatingPointMutation|15-floating-point-mutation.md|implementation,algorithm,phase1,mutation"
    "Implement ElitismReplacement|16-elitism-replacement.md|implementation,algorithm,phase1,replacement"
    "Implement GenerationalReplacement|17-generational-replacement.md|implementation,algorithm,phase1,replacement"
    "Implement SteadyStateReplacement|18-steady-state-replacement.md|implementation,algorithm,phase1,replacement"
    "Implement Configuration & Parameter Defaults|19-configuration-parameter-defaults.md|implementation,algorithm,phase1,core"
    "Implement InfeasibleSolutionHandler|20-infeasible-solution-handler.md|implementation,algorithm,phase1,core"
)

echo -e "${YELLOW}This script will create 20 GitHub issues from templates${NC}"
echo -e "${YELLOW}Milestone: Phase 1${NC}"
echo ""
read -p "Do you want to proceed? (y/n) " -n 1 -r
echo ""

if [[ ! $REPLY =~ ^[Yy]$ ]]; then
    echo -e "${YELLOW}Cancelled by user${NC}"
    exit 0
fi

# Check if milestone exists
echo -e "\n${YELLOW}Checking if 'Phase 1' milestone exists...${NC}"
if ! gh api repos/:owner/:repo/milestones | grep -q '"title":"Phase 1"'; then
    echo -e "${YELLOW}Creating 'Phase 1' milestone...${NC}"
    gh api repos/:owner/:repo/milestones \
        -X POST \
        -f title='Phase 1' \
        -f description='Initial implementation of core GA framework with basic operators' \
        -f state='open' 2>/dev/null || echo -e "${YELLOW}Note: Could not create milestone automatically. Please create it manually in GitHub.${NC}"
fi

CREATED=0
FAILED=0

# Create issues
for issue_data in "${ISSUES[@]}"; do
    IFS='|' read -r title filename labels <<< "$issue_data"
    
    echo ""
    echo -e "${GREEN}Creating issue: ${title}${NC}"
    
    filepath=".github/ISSUE_TEMPLATE/${filename}"
    
    if [ ! -f "$filepath" ]; then
        echo -e "${RED}Error: Template file not found: ${filepath}${NC}"
        ((FAILED++))
        continue
    fi
    
    # Extract content after front matter (remove YAML front matter)
    body=$(awk '/^---$/{if(++count==2) next} count>=2' "$filepath")
    
    # Create the issue
    if gh issue create \
        --title "$title" \
        --body "$body" \
        --label "$labels" \
        --milestone "Phase 1" 2>/dev/null; then
        
        echo -e "${GREEN}✓ Successfully created${NC}"
        ((CREATED++))
        
        # Sleep to avoid rate limiting
        sleep 2
    else
        echo -e "${RED}✗ Failed to create${NC}"
        ((FAILED++))
    fi
done

# Summary
echo ""
echo -e "${GREEN}=====================================${NC}"
echo -e "${GREEN}Summary${NC}"
echo -e "${GREEN}=====================================${NC}"
echo -e "Total issues to create: ${#ISSUES[@]}"
echo -e "${GREEN}Successfully created: ${CREATED}${NC}"
if [ $FAILED -gt 0 ]; then
    echo -e "${RED}Failed: ${FAILED}${NC}"
fi
echo ""

if [ $CREATED -eq ${#ISSUES[@]} ]; then
    echo -e "${GREEN}All issues created successfully!${NC}"
    echo -e "${GREEN}View them at: https://github.com/$(gh repo view --json nameWithOwner -q .nameWithOwner)/issues${NC}"
else
    echo -e "${YELLOW}Some issues could not be created. Please check the errors above.${NC}"
    echo -e "${YELLOW}You may need to create them manually using the GitHub web interface.${NC}"
fi

echo ""
echo -e "${YELLOW}Next steps:${NC}"
echo "1. Review created issues in GitHub"
echo "2. Assign issues to team members"
echo "3. Start with Phase 1a issues (Foundation)"
echo "4. Follow the implementation plan in PHASE1_ISSUES_SUMMARY.md"
echo ""
