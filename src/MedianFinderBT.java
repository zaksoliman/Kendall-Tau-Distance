import java.util.*;

public class MedianFinderBT {
	

	// Constructors
	@SuppressWarnings("unused")
	private MedianFinderBT() {
	}
	public MedianFinderBT(ArrayList<ArrayList<Integer>> permutationSet) {
		this.numOfElements = permutationSet.get(0).size();	//size of permutations	
		this.permutationSet = permutationSet;

		//Initializing the counter matrix
		counterMatrix = new int[numOfElements + 1][numOfElements + 1];
		
		right = new ArrayList<HashSet<Integer>>(numOfElements + 1);
		left = new ArrayList<HashSet<Integer>>(numOfElements + 1);

		//Initialize the sets
		for (int i = 0; i < numOfElements + 1; i++) {
			right.add(new HashSet<Integer>());
			left.add(new HashSet<Integer>());
		}

		// Any integer can go after zero, so that if the backtrack algorithm starts with and empty list
		//then we can add any element as the first elements for every branch
		right.get(0).addAll(permutationSet.get(0));
		startingInstances = new ArrayList<ArrayList<Integer>>();

		ConstructConstraints();
		ConstructInitialSolutions();
	}

	/*
	 * PUBLIC FUNCTIONS
	 */
	
	public ArrayList<ArrayList<Integer>> FindMed() {
		for (ArrayList<Integer> initialSolution : startingInstances) {
			FindMedBT(initialSolution);
		}
		
		return solutions;
	}

	public int getKendallTauDistance() {
		return minDistance;
	}
	
	public void setPermutationSet(ArrayList<ArrayList<Integer>> permutationSet){
		
		this.permutationSet = permutationSet;
	}
	/*
	 * PRIVATE FUNCTIONS 
	 * */
	
	private void ConstructConstraints() {
		//Start counting in how many sets a given integer i comes before j for every pair (i,j) for all permutations
		for (ArrayList<Integer> permutation : permutationSet) {
			for (int i = 0; i < permutation.size() - 1; i++) {
				for (int j = i + 1; j < permutation.size(); j++) {
					counterMatrix[permutation.get(i)][permutation.get(j)]++;
				}
			}
		}

		int majority = (int) Math.ceil((permutationSet.size() / 2.0));

		for (int i = 0; i < counterMatrix.length; i++) {
			for (int j = 0; j < counterMatrix.length; j++) {
				if (counterMatrix[i][j] >= majority) {
					// to the right of i we have j in a majority of permutations
					right.get(i).add(j);

					// to the left of j we have i in a majority of permutations
					left.get(j).add(i);
				}
			}
		}
	}

	// Construct the set of initial solutions for the backtrack algorithm
	private void ConstructInitialSolutions() {
		int size = left.size();

		ArrayList<Integer> partialSolution = new ArrayList<Integer>(
				numOfElements);

		// Find (if it exists) the elements that will be the first element of a
		// median
		for (int i = 1; i < size; i++) {
			// Found the first element of a solution
			if (left.get(i).isEmpty()) {
				partialSolution.add(i);

				// continue adding elements to the partial solution until it is
				// no longer possible
				boolean done = false;
				while (!done) {
					// TODO: check if size is zero
					int lastElementAdded = partialSolution.get(partialSolution.size() - 1);

					done = true;
					for (HashSet<Integer> set : left) {
						if (set.size() == 1 && set.contains(lastElementAdded)) {
							partialSolution.add(left.indexOf(set));
							done = false;
						}
					}
				}

				startingInstances.add(new ArrayList<Integer>(partialSolution));
				partialSolution.clear();
			}
		}
		
		//If we can't build any initial solution we start with an empty one
		if(startingInstances.isEmpty())
			startingInstances.add(new ArrayList<Integer>());
	}

	private void FindMedBT(ArrayList<Integer> currentSolution) {
		// Case 1: We have a potential solution if it is the correct size
		if (currentSolution.size() == numOfElements) {
			int kendallTauDist = KendalTauDist(currentSolution);

			// Check if it's a better solution than the ones we have
			if (kendallTauDist < minDistance) {
				minDistance = kendallTauDist;
				solutions.clear();
				solutions.add(new ArrayList<Integer>(currentSolution));
				return;
			}
			// Check if it is as good as the solutions we found
			else if (kendallTauDist == minDistance) {
				solutions.add(new ArrayList<Integer>(currentSolution));
				return;
			}
			// The permutation is not a solution
			else
				return;
		}
		// Case 2: Check if the current partial solution is valid
		else if (!IsValid(currentSolution))
			return;

		int lastElement;

		// Find the elements that we can add to the current solution and recurse
		if (currentSolution.size() != 0)
			lastElement = currentSolution.get(currentSolution.size() - 1);
		else
			lastElement = 0;

		for (Integer element : right.get(lastElement)) {
			if (!currentSolution.contains(element)) {
				currentSolution.add(element);
				FindMedBT(currentSolution);
				if (!currentSolution.isEmpty())
					currentSolution.remove(currentSolution.size() - 1);
			}

		}

	}

	private int KendalTauDist(ArrayList<Integer> permutation) {
		int distance = 0;
		for (ArrayList<Integer> perm : permutationSet) {
			distance += KendallTauDist(permutation, perm);
		}

		return distance;
	}

	private int KendallTauDist(ArrayList<Integer> permutationA, ArrayList<Integer> permutationB) {
		if (permutationA.size() != permutationB.size()) {
			return -1;
		}

		int size = permutationA.size();

		int counter = 0;
		int[] inversePermutationA = new int[size];
		int[] inversePermutationB = new int[size];

		for (int i = 0; i < size; i++) {
			inversePermutationA[permutationA.get(i) - 1] = i;
			inversePermutationB[permutationB.get(i) - 1] = i;
		}

		for (int i = 0; i < size - 1; i++) {
			for (int j = i + 1; j < size; j++) {
				if ((inversePermutationA[i] < inversePermutationA[j])
						&& (inversePermutationB[i] > inversePermutationB[j]))
					counter++;
				if ((inversePermutationA[i] > inversePermutationA[j])
						&& (inversePermutationB[i] < inversePermutationB[j]))
					counter++;
			}
		}

		return counter;
	}

	private boolean IsValid(ArrayList<Integer> candidate) {
		boolean valid = true;

		int lastElement = 0;

		if (candidate.size() != 0)
			lastElement = candidate.get(candidate.size() - 1);

		// If we can't add anymore elements to the current permutations we can't
		// continue
		if (right.get(lastElement).isEmpty()) {
			valid = false;
		}

		return valid;
	}

	/*
	 * PRIVATE FIELDS
	 * */
	
	private int numOfElements;
	private int[][] counterMatrix;
	private ArrayList<ArrayList<Integer>> permutationSet;
	private ArrayList<ArrayList<Integer>> startingInstances;	
	
	// for every index i, a set holds the elements to the right of the integer i
	private ArrayList<HashSet<Integer>> right;
	
	//for every index i, a set holds the elements to the left of the integer i
	private ArrayList<HashSet<Integer>> left;
	
	// keeps track of the minimum kendall-tau distance found
	private int minDistance = Integer.MAX_VALUE;
	
	//holds all the solutions (medians)
	public ArrayList<ArrayList<Integer>> solutions = new ArrayList<ArrayList<Integer>>();

}
