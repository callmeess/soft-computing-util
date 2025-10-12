#!/bin/bash

# Script to create ONLY the remaining Phase 1 GitHub issues
# This script checks which issues already exist and only creates the missing ones
# Requires: GitHub CLI (gh) installed and authenticated

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo -e "${GREEN}=====================================${NC}"
echo -e "${GREEN}Create Remaining Phase 1 Issues${NC}"
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

# Parse command line arguments
DRY_RUN=false
FORCE=false

while [[ $# -gt 0 ]]; do
    case $1 in
        --dry-run)
            DRY_RUN=true
            shift
            ;;
        --force)
            FORCE=true
            shift
            ;;
        --help)
            echo "Usage: $0 [OPTIONS]"
            echo ""
            echo "Options:"
            echo "  --dry-run    Show what would be created without actually creating issues"
            echo "  --force      Create issues even if similar ones exist"
            echo "  --help       Show this help message"
            exit 0
            ;;
        *)
            echo -e "${RED}Unknown option: $1${NC}"
            echo "Use --help for usage information"
            exit 1
            ;;
    esac
done

# Define all issue data (title, file, labels)
declare -A ISSUES=(
    ["Implement Core GeneticAlgorithm Engine"]="01-core-genetic-algorithm-engine.md|implementation,algorithm,phase1,core"
    ["Implement Population Management"]="02-population-management.md|implementation,algorithm,phase1,core"
    ["Implement FitnessFunction Interface"]="03-fitness-function-interface.md|implementation,algorithm,phase1,core"
    ["Implement BinaryChromosome"]="04-binary-chromosome.md|implementation,algorithm,phase1,chromosome"
    ["Implement IntegerChromosome"]="05-integer-chromosome.md|implementation,algorithm,phase1,chromosome"
    ["Implement FloatingPointChromosome"]="06-floating-point-chromosome.md|implementation,algorithm,phase1,chromosome"
    ["Implement RouletteWheelSelection"]="07-roulette-wheel-selection.md|implementation,algorithm,phase1,selection"
    ["Implement TournamentSelection"]="08-tournament-selection.md|implementation,algorithm,phase1,selection"
    ["Implement RankSelection"]="09-rank-selection.md|implementation,algorithm,phase1,selection"
    ["Implement SinglePointCrossover"]="10-single-point-crossover.md|implementation,algorithm,phase1,crossover"
    ["Implement TwoPointCrossover"]="11-two-point-crossover.md|implementation,algorithm,phase1,crossover"
    ["Implement UniformCrossover"]="12-uniform-crossover.md|implementation,algorithm,phase1,crossover"
    ["Implement BinaryMutation"]="13-binary-mutation.md|implementation,algorithm,phase1,mutation"
    ["Implement IntegerMutation"]="14-integer-mutation.md|implementation,algorithm,phase1,mutation"
    ["Implement FloatingPointMutation"]="15-floating-point-mutation.md|implementation,algorithm,phase1,mutation"
    ["Implement ElitismReplacement"]="16-elitism-replacement.md|implementation,algorithm,phase1,replacement"
    ["Implement GenerationalReplacement"]="17-generational-replacement.md|implementation,algorithm,phase1,replacement"
    ["Implement SteadyStateReplacement"]="18-steady-state-replacement.md|implementation,algorithm,phase1,replacement"
    ["Implement Configuration & Parameter Defaults"]="19-configuration-parameter-defaults.md|implementation,algorithm,phase1,core"
    ["Implement InfeasibleSolutionHandler"]="20-infeasible-solution-handler.md|implementation,algorithm,phase1,core"
)

if [ "$DRY_RUN" = true ]; then
    echo -e "${BLUE}DRY RUN MODE - No issues will be created${NC}"
    echo ""
fi

# Get existing issues
echo -e "${YELLOW}Checking existing issues...${NC}"
EXISTING_ISSUES=$(gh issue list --limit 100 --state all --json title --jq '.[].title')

# Arrays to track what we'll do
declare -a TO_CREATE=()
declare -a ALREADY_EXISTS=()

# Check which issues need to be created
for title in "${!ISSUES[@]}"; do
    if echo "$EXISTING_ISSUES" | grep -q "^${title}$"; then
        ALREADY_EXISTS+=("$title")
    else
        TO_CREATE+=("$title")
    fi
done

# Display summary
echo ""
echo -e "${GREEN}Status Summary:${NC}"
echo -e "  ${GREEN}✓ Already exist: ${#ALREADY_EXISTS[@]}${NC}"
echo -e "  ${YELLOW}○ To be created: ${#TO_CREATE[@]}${NC}"
echo -e "  ${BLUE}━ Total issues: ${#ISSUES[@]}${NC}"
echo ""

if [ ${#ALREADY_EXISTS[@]} -gt 0 ]; then
    echo -e "${GREEN}Already Exists:${NC}"
    for title in "${ALREADY_EXISTS[@]}"; do
        echo -e "  ${GREEN}✓${NC} $title"
    done
    echo ""
fi

if [ ${#TO_CREATE[@]} -eq 0 ]; then
    echo -e "${GREEN}All issues have already been created! 🎉${NC}"
    exit 0
fi

echo -e "${YELLOW}Will Create:${NC}"
for title in "${TO_CREATE[@]}"; do
    echo -e "  ${YELLOW}○${NC} $title"
done
echo ""

if [ "$DRY_RUN" = true ]; then
    echo -e "${BLUE}Dry run complete. Run without --dry-run to create these issues.${NC}"
    exit 0
fi

# Ask for confirmation
read -p "Do you want to create ${#TO_CREATE[@]} issues? (y/n) " -n 1 -r
echo ""

if [[ ! $REPLY =~ ^[Yy]$ ]]; then
    echo -e "${YELLOW}Cancelled by user${NC}"
    exit 0
fi

# Check if milestone exists
echo -e "\n${YELLOW}Checking if 'Phase 1' milestone exists...${NC}"
if ! gh api repos/:owner/:repo/milestones 2>/dev/null | grep -q '"title":"Phase 1"'; then
    echo -e "${YELLOW}Creating 'Phase 1' milestone...${NC}"
    gh api repos/:owner/:repo/milestones \
        -X POST \
        -f title='Phase 1' \
        -f description='Initial implementation of core GA framework with basic operators' \
        -f state='open' 2>/dev/null || echo -e "${YELLOW}Note: Could not create milestone automatically. Issues will be created without milestone.${NC}"
else
    echo -e "${GREEN}✓ Milestone 'Phase 1' exists${NC}"
fi

CREATED=0
FAILED=0

# Create issues
for title in "${TO_CREATE[@]}"; do
    IFS='|' read -r filename labels <<< "${ISSUES[$title]}"
    
    echo ""
    echo -e "${YELLOW}Creating: ${title}${NC}"
    
    filepath=".github/ISSUE_TEMPLATE/${filename}"
    
    if [ ! -f "$filepath" ]; then
        echo -e "${RED}  ✗ Error: Template file not found: ${filepath}${NC}"
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
        
        echo -e "${GREEN}  ✓ Successfully created${NC}"
        ((CREATED++))
        
        # Sleep to avoid rate limiting
        sleep 2
    else
        echo -e "${RED}  ✗ Failed to create${NC}"
        ((FAILED++))
    fi
done

# Summary
echo ""
echo -e "${GREEN}=====================================${NC}"
echo -e "${GREEN}Summary${NC}"
echo -e "${GREEN}=====================================${NC}"
echo -e "Issues to create: ${#TO_CREATE[@]}"
echo -e "${GREEN}Successfully created: ${CREATED}${NC}"
if [ $FAILED -gt 0 ]; then
    echo -e "${RED}Failed: ${FAILED}${NC}"
fi
echo -e "${GREEN}Already existed: ${#ALREADY_EXISTS[@]}${NC}"
echo -e "${BLUE}Total: ${#ISSUES[@]}${NC}"
echo ""

if [ $CREATED -gt 0 ]; then
    echo -e "${GREEN}Issues created successfully!${NC}"
    echo -e "${GREEN}View them at: https://github.com/$(gh repo view --json nameWithOwner -q .nameWithOwner)/issues${NC}"
fi

if [ $FAILED -gt 0 ]; then
    echo -e "${YELLOW}Some issues could not be created. You may need to create them manually.${NC}"
fi

echo ""
echo -e "${YELLOW}Next steps:${NC}"
echo "1. Review created issues in GitHub"
echo "2. Assign issues to team members"
echo "3. Start with Phase 1a issues (Foundation)"
echo "4. Follow the implementation plan in PHASE1_ISSUES_SUMMARY.md"
echo ""
