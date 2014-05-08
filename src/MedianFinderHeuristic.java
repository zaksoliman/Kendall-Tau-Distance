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
		disagrementGraph = new int[numOfElements + 1][numOfElements + 1];
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
		int foundDistace;

		for (ArrayList<Integer> permutation : permutationSet) {
			foundDistace = FindMed(permutation);

			if (foundDistace < currentOptimalDist) {
				currentOptimalDist = foundDistace;

				// All the solutions previously found are no longer optimal
				solutions.clear();
				solutions.add(new ArrayList<Integer>(permutation));
			} else if (foundDistace == currentOptimalDist) {
				solutions.add(permutation);
			}

		}

		return solutions;
	}

	/*
	 * PRIVATE FUNCTIONS
	 * */
	
	// returns the Kendall Tau distance and the permutation given as parameter
	// becomes the new permutation
	private int FindMed(ArrayList<Integer> permutation) {
		int length = permutation.size();
		Integer difference = 0; /* this value should always be <= 0 */
		int kendallTauDist = KendalTauDist(permutation, permutationSet);
		initialiseGraph(permutation);
		int numOfChanges = 0;
		boolean done = false;

		while (!done) {
			for (int i = 0; i<length - 1; i++) {
				for (int j = i+1; j<length; j++) {
					if (isGoodcyclicMovement(difference, i, j, permutation)) {
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
	private boolean isGoodcyclicMovement(Integer diff, int start, int end,
			ArrayList<Integer> permutation) {
		int distance = 0; // holds the sum of weight of the edges that will be
							// affected by the cyclic movement
		boolean isGood = false;

		// Initialise the distance
		for (int i = 0; i < disagrementGraph.length; i++) {
			for (int j = 0; j < disagrementGraph.length; j++) {
				distance += disagrementGraph[i][j];
			}
		}

		int distanceDiffLeftShift, distanceDiffRightShift;

		/*
		 * holds the difference of the Kendall tau distance between the
		 * permutation and the set of permutations for the left and right cyclic
		 * movements
		 */

		/*
		 * let i=start and j=end:
		 * 
		 * For a cyclic movement to the right of the sub array [i ... j], only
		 * the edges (j,t), i<= t <= j-1, of the disagreement graph are changed.
		 * 
		 * For a left cyclic movement to the left of the sub array [i .. j],
		 * only the edges (t,i), i+1 <= t <= j, of the disagreement graph are
		 * changed
		 */

		// Check if the right or left movements are good and which is better

		distanceDiffRightShift = distance;
		distanceDiffLeftShift = distance;

		// Compute the distance difference if we would perform a right cyclic
		// movement
		for (int j = start; j <= end - 1; j++) {
			distanceDiffRightShift -= (numOfPermutations - disagrementGraph[end][j]);
		}

		// Compute the distance difference if we would perform a left cyclic
		// movement
		for (int i = start + 1; i < end; i++) {
			distanceDiffLeftShift -= (numOfPermutations - disagrementGraph[i][start]);
		}

		if ((distanceDiffLeftShift < 0) || (distanceDiffRightShift < 0)) {
			// Check if a left shift is better
			if (distanceDiffLeftShift < distanceDiffRightShift) {
				// change the reference of the new optimal value
				diff = new Integer(distanceDiffLeftShift);
				// perform movement
				leftCyclicMovement(permutation, start, end);
				isGood = true;
			}
			if (distanceDiffRightShift < diff.intValue()) {
				// perform cyclic movement
				rightCyclicMovement(permutation, start, end);
				// change the reference of the new optimal value
				diff = new Integer(distanceDiffRightShift);
				isGood = true;
			} else if (distanceDiffLeftShift == distanceDiffRightShift) {
				rightCyclicMovement(permutation, start, end);
				// change the reference of the new optimal value
				diff = new Integer(distanceDiffRightShift);
				isGood = true;
			}
		} else
			isGood = false;

		return isGood;
	}

	private void rightCyclicMovement(ArrayList<Integer> permutation, int start,
			int end) {
		for (int i = end; i > start; i--) {
			Collections.swap(permutation, i, i - 1);
		}

	}

	private void leftCyclicMovement(ArrayList<Integer> permutation, int start,
			int end) {
		for (int i = start; i < end; i++) {
			Collections.swap(permutation, i, i + 1);
		}

	}

	// Initialises the disagreement graph for the given permutation
	private void initialiseGraph(ArrayList<Integer> permutation) {
		for (ArrayList<Integer> p : inversePermutationSet) {
			for (int i = 0; i < disagrementGraph.length; i++) {
				for (int j = i + 1; j < disagrementGraph.length; j++) {
					Integer first = permutation.get(i);
					Integer next = permutation.get(j);

					// count in how many permutation in the permutation set the
					// element next appears before first

					// Check if position of element first is after element next
					// in the permutation p
					if (p.get(first) - 1 > p.get(next) - 1) {
						// there is a disagreement so we increment the weight of
						// the graph's edge
						disagrementGraph[first][next]++;
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

			inversePermutationSet.add(new ArrayList<Integer>(Arrays
					.asList(inverse)));
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
				if ((inversePermutationA[i] > inversePermutationA[i])
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
	private int[][] disagrementGraph;
	private ArrayList<ArrayList<Integer>> solutions;
	private ArrayList<ArrayList<Integer>> permutationSet;
	private ArrayList<ArrayList<Integer>> inversePermutationSet;

}
