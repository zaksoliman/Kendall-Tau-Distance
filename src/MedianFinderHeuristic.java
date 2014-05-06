/*import java.util.*;

public class KendallTauDistH
{

	public ArrayList<Integer> FindMed(ArrayList<Integer> permutation, HashSet<ArrayList<Integer>> permutationSet)
	{
		int length = permutation.size();
		int kValue;
		for (int i=0; i<=length-2; i++)
		{
			for (int j=i+1; j<=length-1; j++)
			{				
				GoodCyclicMovement(kValue,i,j,permutation);
			}
		}
	}

	public boolean GoodCyclicMovement(int kValue, int i, int j, ArrayList<Integer> permutation)
	{
		 TODO
 		* shift sub array [i...j] left
 		* shift sub array [i...j] rigth
 		* get diffrence of distance w/ left and original  permuation and rigth w/ permutation
 		* take the minimum
 		* if bigger than kValue reject
 		* else accept
 		

		ArrayList<Integer> rightShift = new ArrayList<Integer>(permutation);
		ArrayList<Integer> leftShift = new ArrayList<Integer>(permutation);
		
		
	}

}
*/