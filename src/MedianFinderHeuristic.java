import java.util.*;

public class MedianFinderHeuristic {

	/*
	 * CONSTRUCTORS
	 * */
	@SuppressWarnings("unused")
	private MedianFinderHeuristic() {
	}
	public MedianFinderHeuristic(int numOfPermutations, int numOfElements,
			ArrayList<ArrayList<Integer>> permutationSet) {
		/*
		 * we use numOfElements +1 to make it easier to lookup the weights of
		 * the graph edges
		 */
		solutions = new ArrayList<ArrayList<Integer>>();
		this.permutationSet = permutationSet;
		this.numOfElements = numOfElements;
		this.numOfPermutations = numOfPermutations;
		buildInversePermutations();

	}

	/*
	 * PUBLIC FUNCTIONS
	 * */
	public ArrayList<ArrayList<Integer>> FindMedian() {
		int currentOptimalDist = Integer.MAX_VALUE;
		int foundDistance;

		for (ArrayList<Integer> permutation : permutationSet) {
			
			ArrayList<Integer> currentPermutation = new ArrayList<Integer>(permutation);
			
			foundDistance = FindMed(currentPermutation);

			if (foundDistance < currentOptimalDist) {
				//Update current optimal distance
				currentOptimalDist = foundDistance;
				// All the solutions previously found are now no longer optimal
				solutions.clear();
				// Add the optimal solution to the solution set
				solutions.add(new ArrayList<Integer>(currentPermutation));
			} else if (foundDistance == currentOptimalDist) {
				solutions.add(currentPermutation);
			}

		}
		
		distance = currentOptimalDist;

		return solutions;
	}

	public int getDistance()
	{
		return distance;
	}
	
	/*
	 * PRIVATE FUNCTIONS
	 * */
	
	// returns the Kendall Tau distance and the permutation given as parameter
	// becomes the new permutation
	private int FindMed(ArrayList<Integer> permutation) {
		int kendallTauDist = KendalTauDist(permutation, permutationSet);
		initializeGraph(permutation);		
		boolean done = false;

		while (!done) {
			int numOfChanges = 0;
			for (int i = 0; i<numOfElements - 1; i++) {
				for (int j = i+1; j<numOfElements; j++) {
					if (isGoodcyclicMovement(i, j, permutation)) {
						kendallTauDist += difference;
						numOfChanges++;
					}
				}
			}
			if (numOfChanges == 0)
				done = true;
		}

		return kendallTauDist;
	}

	/*
	 * Given the current distance, the current permutation and the start and end
	 * index of the movement this function will return true if a cyclic movement
	 * will decrease the Kendall tau distance and false if not. the diff Integer
	 * reference will point to a new value(the distance difference after the
	 * good cyclic movement is performed)
	 */
	private boolean isGoodcyclicMovement(int start, int end, ArrayList<Integer> permutation) {
		/*
		 * holds the difference of the Kendall tau distance between the
		 * permutation and the set of permutations for the left and right cyclic
		 * movements
		 */
		int distanceDiffLeftShift = 0;
		int distanceDiffRightShift = 0;
		
		boolean isGood = false;
		
		/*
		 * let i=start and j=end:
		 * 
		 * For a cyclic movement to the right of the sub array [i ... j], only
		 * the edges (p[t],p[j]), i<= t <= j-1, of the disagreement graph are changed.
		 * 
		 * For a left cyclic movement to the left of the sub array [i .. j],
		 * only the edges (p[i],p[t]), i+1 <= t <= j, of the disagreement graph are
		 * changed
		 */

		// Initialize the distance
		for (int t = start; t <= end-1; t++) {
			distanceDiffRightShift += (numOfPermutations - disagreementGraph[permutation.get(t)][permutation.get(end)]);
		}
		
		for (int t = start+1; t <= end; t++) {
			distanceDiffLeftShift += (numOfPermutations - disagreementGraph[permutation.get(start)][permutation.get(t)]);
		}
		
		// Check if the right or left movements are good and which is better	
		
		// Compute the distance difference if we would perform a right cyclic
		// movement
		for (int t = start; t <= end-1; t++) {
			distanceDiffRightShift -=  disagreementGraph[permutation.get(t)][permutation.get(end)];
		}

		// Compute the distance difference if we would perform a left cyclic
		// movement
		for (int t = start+1; t <= end; t++) {
			distanceDiffLeftShift -= disagreementGraph[permutation.get(start)][permutation.get(t)];
		}

		if ((distanceDiffLeftShift < 0) || (distanceDiffRightShift < 0)) {
			// Check if a left shift is better
			if (distanceDiffLeftShift < distanceDiffRightShift) {
				// change the reference of the new optimal value
				difference = distanceDiffLeftShift;
				// perform movement
				leftCyclicMovement(permutation, start, end);
				
				isGood = true;
			}
			// Check if a right shift is better
			if (distanceDiffRightShift < distanceDiffLeftShift) {
				// perform cyclic movement
				rightCyclicMovement(permutation, start, end);
				// change the reference of the new optimal value
				difference = distanceDiffRightShift;
				
				isGood = true;
			} else if (distanceDiffLeftShift == distanceDiffRightShift) {
				rightCyclicMovement(permutation, start, end);
				// change the reference of the new optimal value
				difference = distanceDiffRightShift;
				
				isGood = true;
			}
		} else
			isGood = false;

		return isGood;
	}

	private void rightCyclicMovement(ArrayList<Integer> permutation, int start,
			int end) {
		
		//update the disagreement graph
		for (int t = start; t <= end-1; t++) {
			disagreementGraph[permutation.get(t)][permutation.get(end)] = (numOfPermutations - disagreementGraph[permutation.get(t)][permutation.get(end)]);
		}
				
		for (int i = end; i > start; i--) {
			Collections.swap(permutation, i, i - 1);
		}
		
	}

	private void leftCyclicMovement(ArrayList<Integer> permutation, int start,
			int end) {
		
		//update the disagreement graph
		for (int t = start+1; t <= end; t++) {
			disagreementGraph[permutation.get(start)][permutation.get(t)] = (numOfPermutations - disagreementGraph[permutation.get(start)][permutation.get(t)]);
		}
				
		for (int i = start; i < end; i++) {
			Collections.swap(permutation, i, i + 1);
		}
		

	}

	// Initializes the disagreement graph for the given permutation
	private void initializeGraph(ArrayList<Integer> permutation) {
		
		int size = permutation.size();

		disagreementGraph = new int[numOfElements + 1][numOfElements + 1];
		
		for (ArrayList<Integer> invPerm : inversePermutationSet) {
			for (int i = 0; i < size; i++) {
				for (int j = i + 1; j < size; j++) {
					Integer first = permutation.get(i);
					Integer next = permutation.get(j);

					// count in how many permutation in the permutation set the
					// element next appears before first

					// Check if position of element first is after element next
					// in the permutation p
					if (invPerm.get(first-1) > invPerm.get(next-1)) {
						// there is a disagreement so we increment the weight of
						// the graph's edge
						disagreementGraph[first][next]++;
					}
				}
			}
		}
	}

	private void buildInversePermutations() {
		inversePermutationSet = new ArrayList<ArrayList<Integer>>();

		// The inverse array will allow us to get at index i, the position of
		// the element i+1
		Integer[] inverse = new Integer[numOfElements];

		for (ArrayList<Integer> permutation : permutationSet) {
			for (int i = 0; i < inverse.length; i++) {
				inverse[permutation.get(i) - 1] = i;
			}

			inversePermutationSet.add(new ArrayList<Integer>(Arrays.asList(inverse)));
		}

	}
	
	private int KendalTauDist(ArrayList<Integer> permutation,
			ArrayList<ArrayList<Integer>> permutationSet) {
		int distance = 0;
		for (ArrayList<Integer> perm : permutationSet) {
			distance += KendallTauDist(permutation, perm);
		}

		return distance;
	}
	
	private int KendallTauDist(ArrayList<Integer> permutationA,
			ArrayList<Integer> permutationB) {
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

	/*
	 * PRIVATE MEMBERS
	 * */
	private int numOfElements;
	private int numOfPermutations;
	private int[][] disagreementGraph;
	private int distance;
	private int difference;
	private ArrayList<ArrayList<Integer>> solutions;
	private ArrayList<ArrayList<Integer>> permutationSet;
	private ArrayList<ArrayList<Integer>> inversePermutationSet;

}
