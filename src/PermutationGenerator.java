import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;


public class PermutationGenerator {
	
	public PermutationGenerator(String filePath)
	{
		this.filePath = filePath;
	}
	
	public void generatePermutations(int size, int index)
	{
		permutation = generateInitialPermutation(size);
		permute(index);
	}
	
	private ArrayList<Integer> generateInitialPermutation(int size)
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
			print(permutation, filePath);
		}
		else 
			
			for (int j = index; j<permutation.size(); j++)
			{
				Collections.swap(permutation, index, j);
				permute(index+1);
				Collections.swap(permutation, index, j); 
			}
	}
	
	private void print(ArrayList<Integer> array, String fileName)
	{
		Integer[] arr = new Integer[array.size()];		
		String list = Arrays.deepToString(array.toArray(arr));
		try
		{
			File file = new File(fileName);
			
			if (!file.exists())
				file.createNewFile();
			
			FileWriter fw = new FileWriter(file, true);
			BufferedWriter bw = new BufferedWriter(fw);
			
			bw.write(list);
			bw.newLine();
			bw.close();
			
		}
		catch (IOException e)
		{
			System.out.println(e.getMessage());
		}
		
	}
	
	private ArrayList<Integer> permutation;
	private String filePath;
}
