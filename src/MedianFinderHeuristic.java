import java.util.*;

public class MedianFinderHeuristic
{
	
	private MedianFinderHeuristic(){}
	
	public MedianFinderHeuristic(int numOfPermutations, int numOfElements, ArrayList<ArrayList<Integer>> permutationSet)
	{
		disagrementGraph = new int[numOfElements+1][numOfElements+1]; /*we use numOfElements+1 to make it easier to lookup the weights of the graph edges*/
		solutions = new ArrayList<ArrayList<Integer>>();
		this.permutationSet = permutationSet;
		
	}

	public ArrayList<ArrayList<Integer>> FindMedian()
	{
		int currentMinDist = Integer.MAX_VALUE;
		int foundDistace;
		
		for(ArrayList<Integer> permutation : permutationSet)
		{
			foundDistace = FindMed(permutation);
			
			if(foundDistace < currentMinDist)
			{
				currentMinDist = foundDistace;
				
				//All the solutions previously found are no longer optimal
				solutions.clear();
				solutions.add(new ArrayList<Integer>(permutation));
			}
			else if (foundDistace == currentMinDist)
			{
				solutions.add(permutation);
			}
				
		}
		
		return solutions;
	}
	
	//returns the Kendall Tau distance and the initial permutation becomes the transformed permutation
	private int FindMed(ArrayList<Integer> permutation)
	{
		int length = permutation.size();
		Integer currentK = 0;
		int kendallTauDist = KendalTauDist(permutation, permutationSet);
		for (int i=0; i<=length-2; i++)
		{
			for (int j=i+1; j<=length-1; j++)
			{				
				if(isGoodcyclicMovement(currentK,i,j,permutation))
					kendallTauDist += currentK;
					
			}
		}
		
		//TODO
		return kendallTauDist;
	}

	//will transform the permutation if the cyclic movement and changes the value of the k value if good movement
	//if not the permutation given as a parameter stays the same
	private boolean isGoodcyclicMovement(Integer kValue, int i, int j, ArrayList<Integer> permutation)
	{
		 //TODO
		
 		 /*
 		   shift sub array [i...j] left
 		   shift sub array [i...j] right
 		   get difference of distance w/ left and original  permutation and right w/ permutation
 		   take the minimum
 		   if bigger than kValue reject
 		  else accept
 		*/
		
		//Check

		ArrayList<Integer> rightShift = new ArrayList<Integer>(permutation);
		ArrayList<Integer> leftShift = new ArrayList<Integer>(permutation);
		
		//TODO
		return null;
	}

	private void initializeGraph()
	{
		for (ArrayList<Integer> permutation : permutationSet)
		{
			for (int i = 0 ; i < disagrementGraph.length; i++)
			{
				for (int j = 0; j < disagrementGraph.length; j++) 
				{
					
				}
			}
		}
	}
	
	private int KendalTauDist(ArrayList<Integer> permutation, ArrayList<ArrayList<Integer>> permutationSet)
	{
		int distance = 0;
		for (ArrayList<Integer> perm : permutationSet)
		{
			distance += KendallTauDist(permutation, perm);
		}
		
		return distance;
	}
	
	private int KendallTauDist(ArrayList<Integer> permutationA, ArrayList<Integer> permutationB)
	{	
		if(permutationA.size() != permutationB.size())
		{
			return -1;
		}
		
		int size = permutationA.size();
		
		int counter = 0;
		int[] inversePermutationA = new int[size];
		int[] inversePermutationB = new int[size];
		
		for (int i = 0; i < size; i++)
		{
			inversePermutationA[permutationA.get(i)-1]= i;
			inversePermutationB[permutationB.get(i)-1] = i;
		}
		
		for (int i = 0; i < size-1; i++) 
		{
			for (int j = i+1; j < size; j++) 
			{
				if ((inversePermutationA[i]<inversePermutationA[j]) && (inversePermutationB[i]>inversePermutationB[j]))
					counter++;
				if ((inversePermutationA[i]>inversePermutationA[i]) && (inversePermutationB[i]<inversePermutationB[j]))
					counter++;
			}			
		}
				
		return counter;
	}
	
	private int[][] disagrementGraph;
	private ArrayList<ArrayList<Integer>> solutions;
	private ArrayList<ArrayList<Integer>> permutationSet;
	
}
