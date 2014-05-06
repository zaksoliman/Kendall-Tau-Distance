import java.util.*;

public class MedianFinderBT 
{
	private MedianFinderBT(){}
	
	//Constructor
	public MedianFinderBT(int m /*number of permutations*/
			,int n /*number of elements in each permutation*/
			,ArrayList<ArrayList<Integer>> permutationSet)
	{
		this.n = n;
		this.m = m;
		counterMatrix = new int[n][n];
		this.permutationSet = permutationSet;
	}
	
	public void ConstructConstraints()
	{
		for (ArrayList<Integer> permutation : permutationSet)
		{
			for (int i=0; i < permutation.size()-1; i++)
			{
				for(int j=i+1; j<permutation.size(); j++)
				{
					counterMatrix[permutation.get(i)][permutation.get(j)]++;
				}
			}
		}
		
		int majority = (int) Math.ceil(permutationSet.size()/2);
		
		for (int i=0; i<counterMatrix.length; i++)
		{
			for (int j=0; j<counterMatrix.length; j++)
			{
				if (counterMatrix[i][j] >= majority)
				{
					//to the right of i we have j in a majority of permutations
					right.get(i).add(j);
					//to the left of j we have i in a majority of permutations
					left.get(j).add(i);
				}
			}
		}
	}
	
	//Conctruct the set of initial solutions for the backtrack algorithm
	public void ConstructInitialSolutions()
	{
		int size = left.size();
		
		ArrayList<Integer> partialSolution = new ArrayList<Integer>();
		
		//Find (if it exists) the elements that will be the first element of a median
		for(int i=1; i<size; i++)
		{
			//Found the first element of a solution
			if(left.get(i).isEmpty())
			{				
				partialSolution.add(i);
				
				//continue adding elements to the partial solution until it is no longer posible				
				boolean done = false;
				
				while(!done)
				{
					int currentElement = partialSolution.get(partialSolution.size()-1);
					
					done = true;
					for (HashSet<Integer> set: left)
					{
						if(set.size()==1 && set.contains(currentElement))
						{
							partialSolution.add(left.indexOf(set));
							done = false;
						}
					}
				}
				
				startingInstances.add(partialSolution);
				partialSolution.clear();
			}
		}
	}
	
	public void FindMedBT(ArrayList<Integer> currentSolution)
	{
		//Case 1: We have a potential solution if it is the correct size
		if (currentSolution.size() == n)
		{
			int kendallTauDist = KendalTauDist(currentSolution);
			
			//Check if it's a better solution than the ones we have
			if (kendallTauDist < minDistance)
			{
				minDistance = kendallTauDist;
				solutions.clear();
				solutions.add(currentSolution);
				return;
			}
			//Check if it is as good as the solutions we found
			else if(kendallTauDist == minDistance)
			{
				solutions.add(currentSolution);
				return;
			}
			//The permutation is not a solution
			else
				return;
		}
		//Case 2: Check if the current partial solution is valid
		else if(!IsValid(currentSolution))
			return;
		
		//Find the elements that we can add to the current solution and recurse
		int lastElement = currentSolution.get(currentSolution.size()-1);
		
		for (Integer element: right.get(lastElement))
		{
			if (!currentSolution.contains(element))
			{
				currentSolution.add(element);
				FindMedBT(currentSolution);
				if(!currentSolution.isEmpty())
					currentSolution.remove(currentSolution.size()-1);
			}
			
		}
		
	}
	
	public void FindMed()
	{
		for (ArrayList<Integer> initialSolution : startingInstances)
		{
			FindMedBT(initialSolution);
		}
	}
	
	private int KendalTauDist(ArrayList<Integer> permutation)
	{
		int distance = 0;
		for (ArrayList<Integer> perm : permutationSet)
		{
			distance += KendallTauDist(permutation, perm);
		}
		
		return distance;
	}
	
	private int KendallTauDist(ArrayList<Integer> permutation1, ArrayList<Integer> permutation2)
	{
		//TODO		
		ArrayList<Integer> permInverse1 = new ArrayList<Integer>(permutation1.size());
		ArrayList<Integer> newPerm2 = new ArrayList<Integer>(permutation2.size());
		
		for (int i = 0; i < permutation1.size(); i++)
		{
			permInverse1.set(permutation1.get(i), i);
		}
				
		for (int i=0; i<permutation2.size(); i++)
		{
			newPerm2.set(i, permInverse1.get(permutation2.get(i)));
		}
		
		return Inversions(newPerm2);
	}
	
	private int Inversions(ArrayList<Integer> permutation)
	{
		//TODO
		return 0;
	}
	
	private boolean IsValid(ArrayList<Integer> permutation)
	{
		boolean valid = true;
		
		int lastElement = permutation.get(permutation.size()-1);
		
		//If we can't add anymore elements to the current permutations we can't continue
		if (right.get(lastElement).isEmpty())
		{
			valid = false;
		}
		
		return valid;	
	}
	
	
	
	public ArrayList<ArrayList<Integer>> solutions; //holds all the solutions (medians)
	
	private int n,m;
	private int[][] counterMatrix;
	private ArrayList<HashSet<Integer>> right; /*for every index i, a set holds the elements to the right of the integer i*/
	private ArrayList<HashSet<Integer>> left; /*for every index i, a set holds the elements to the left of the integer i*/
	private int minDistance = Integer.MAX_VALUE; //keeps track of the minium kendall-tau distance found
	private ArrayList<ArrayList<Integer>> permutationSet;
	private ArrayList<ArrayList<Integer>> startingInstances = new ArrayList<ArrayList<Integer>>();	
	//private HashSet<Integer> currentlyUsed = new HashSet<Integer>(); //Holds all the integers that have been used by the backtrack algorithm at a certain point in it's execution
	

}
