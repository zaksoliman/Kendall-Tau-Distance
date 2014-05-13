import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import com.sun.org.apache.xerces.internal.parsers.IntegratedParserConfiguration;


public class PermutationGenerator {
	
	public PermutationGenerator(String filePath)
	{
		this.filePath = filePath;
	}
	
	public ArrayList<ArrayList<ArrayList<Integer>>> getAllPermutationSets(int setSize, int permutationSize)
	{
		ArrayList<ArrayList<Integer>> allPermutations = getAllPermutations(permutationSize);
		ArrayList<Integer> tempPermutation1, tempPermutation2, tempPermutation3;
		ArrayList<ArrayList<Integer>> tempPermSet = new ArrayList<ArrayList<Integer>>();
		ArrayList<ArrayList<ArrayList<Integer>>> sets = new ArrayList<ArrayList<ArrayList<Integer>>>();
		
		for(int i=0; i<allPermutations.size()-2 ; i++){
			
			tempPermutation1 = allPermutations.get(i);
			tempPermSet.add(new ArrayList<Integer>(tempPermutation1));
			
			for (int j=i+1; j <allPermutations.size()-1; j++){
				
				tempPermutation2 = allPermutations.get(j);
				tempPermSet.add(new ArrayList<Integer>(tempPermutation2));
				
				for (int k = j+1; k < allPermutations.size(); k++){
					
					tempPermutation3 = allPermutations.get(k);
					tempPermSet.add(new ArrayList<Integer>(tempPermutation3));
					sets.add(new ArrayList<ArrayList<Integer>>(tempPermSet));
					tempPermSet.remove(tempPermSet.size()-1);										
				}
				tempPermSet.remove(tempPermSet.size()-1);	
			}
			tempPermSet.clear();	
		}
		
		return sets;
	}
	
	public ArrayList<ArrayList<Integer>>  generateRandomPermutationSet(int size, int numOfPrmutations)
	{
		generatedPermutations.clear();
		permutation = generateInitialPermutation(size);
		
		permute(0);
		
		Random rnd = new Random();
		int toRemove = generatedPermutations.size() - numOfPrmutations;
		int elemIndex; //holds index of element to be randomly removed
		
		for (int i = 0; i < toRemove; i++) {			
			elemIndex = rnd.nextInt(generatedPermutations.size());
			complementSet.add(generatedPermutations.get(elemIndex));	
			generatedPermutations.remove(elemIndex);					
		}
		
		//permutationSet = new HashSet<ArrayList<Integer>>(generatedPermutations);
		
		return generatedPermutations;		
	}

	public ArrayList<ArrayList<Integer>> getComplement()
	{
		return complementSet;
	}
	
	private ArrayList<ArrayList<Integer>> getAllPermutations(int size)
	{
		generatedPermutations.clear();
		permutation = generateInitialPermutation(size);
		permute(0);
		
		return generatedPermutations;
	}
	
	private ArrayList<Integer> generateInitialPermutation(int size)
	{
		ArrayList<Integer> init = new ArrayList<Integer>(size);
		for (int i=1; i<=size; i++)
			init.add(i);
		
		return init;
	}
	
	private void permute(int index)
	{
		if (index == permutation.size()-1)
		{
			//print(permutation, filePath);
			generatedPermutations.add(new ArrayList<Integer>(permutation));
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
	private ArrayList<ArrayList<Integer>> generatedPermutations = new ArrayList<ArrayList<Integer>>();
	private ArrayList<ArrayList<Integer>> complementSet = new ArrayList<ArrayList<Integer>>();
	//private HashSet<ArrayList<Integer>> permutationSet;
}
