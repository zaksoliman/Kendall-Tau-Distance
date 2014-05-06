import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.*;


public class PermutationGenerator {
	
	public PermutationGenerator()
	{
		try
		{
			fos = new FileOutputStream("permutations.txt");
			oos = new ObjectOutputStream(fos);
		}
		catch(IOException e)
		{}
	}
	
	public void GeneratePermutations(int size, int index)
	{
		permutation = GenerateInitialPermutation(size);
		permute(index);
	}
	
	private ArrayList<Integer> GenerateInitialPermutation(int size)
	{
		ArrayList<Integer> init = new ArrayList<Integer>(size);
		int seed = 1;
		for (int i=1; i<=size; i++)
			init.add(i);
		
		java.util.Collections.shuffle(init, new Random(seed));
		
		return init;
	}
	
	private void permute(int index)
	{
		if (index == permutation.size()-1)
		{
			try
			{
				oos.writeObject(permutation);
			}
			catch (IOException e)
			{
				
			}
		}else 
			
			for (int j = index; j<permutation.size(); j++)
			{
				Collections.swap(permutation, index, j);
				permute(index+1);
				Collections.swap(permutation, index, j); 
			}
	}
	
	private ArrayList<Integer> permutation;
	private FileOutputStream fos;
	private ObjectOutputStream oos;
}
