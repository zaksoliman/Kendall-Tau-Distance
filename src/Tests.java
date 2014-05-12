import java.io.*;
import java.util.*;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

public class Tests 
{
	public static void main(String[] args)
	{		
		ArrayList<ArrayList<Integer>> permSet = new ArrayList<ArrayList<Integer>>();
		permSet.add(new ArrayList<Integer>(Arrays.asList(new Integer[]{2,4,5,3,6,1})));
		permSet.add(new ArrayList<Integer>(Arrays.asList(new Integer[]{4,2,1,3,6,5}))); 
		permSet.add(new ArrayList<Integer>(Arrays.asList(new Integer[]{6,1,5,4,2,3})));
		
		
		/*permSet.add(new ArrayList<Integer>(Arrays.asList(new Integer[]{1, 2, 3, 4})));
		permSet.add(new ArrayList<Integer>(Arrays.asList(new Integer[]{1, 2, 4, 3}))); 
		permSet.add(new ArrayList<Integer>(Arrays.asList(new Integer[]{1, 4, 2, 3})));*/
		
		MedianFinderBT medFinder = new MedianFinderBT(permSet.get(0).size(), permSet);
		medFinder.FindMed();
		
		System.out.println("Kendall tau distance = " + medFinder.getKendallTauDistance());
		
		ArrayList<ArrayList<Integer>> solutions = medFinder.solutions;
		
		for(ArrayList<Integer> sol : solutions)
		{
			System.out.println(Arrays.deepToString(sol.toArray()));
		}
		
		System.out.println("done with backtrack!");
		
		MedianFinderHeuristic medianFinderH = new MedianFinderHeuristic(3, permSet.get(0).size(), permSet);
		
		solutions = medianFinderH.FindMedian();
		
		System.out.println("Kendall tau distance = " + medianFinderH.getDistance());
		
		for(ArrayList<Integer> sol : solutions)
		{
			System.out.println(Arrays.deepToString(sol.toArray()));
		}
		
		/*MedianFinderBT medFinder;
		
		ArrayList<ArrayList<ArrayList<Integer>>> testCases = read("Results/Triplets11.txt");
		
		for (ArrayList<ArrayList<Integer>> permSet : testCases) {
			
			medFinder = new MedianFinderBT(permSet.get(0).size(), permSet);
		
			medFinder.FindMed();
		
			System.out.println("For the set: " + permSet);
			
			System.out.println("Kendall tau distance = " + medFinder.getKendallTauDistance());
		
			ArrayList<ArrayList<Integer>> solutions = medFinder.solutions;
		
			for(ArrayList<Integer> sol : solutions)
			{
				System.out.println(Arrays.deepToString(sol.toArray()));
			}
		
			System.out.println("done with backtrack!\n");
		}*/
	}
	
	private static ArrayList<ArrayList<ArrayList<Integer>>> read(String filePath)
	{
		//String filePath = "Results/Redo Number of medians 4.txt";
		String input;
		ArrayList<ArrayList<ArrayList<Integer>>> testCases = new ArrayList<ArrayList<ArrayList<Integer>>>();
		ArrayList<ArrayList<Integer>> permutationSet = new ArrayList<ArrayList<Integer>>();
		ArrayList<Integer> permutation = new ArrayList<Integer>();
		
		File file = new File(filePath);
		String[] lineSplit = null;
		String[] permSetString = null;
		
		try
		{
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			
			br.readLine();
			String tst = br.readLine();
			
			System.out.print(tst);
			
			while(!(input = br.readLine()).isEmpty())
			{
				lineSplit = input.split("\\t");		

				permSetString = lineSplit[0].split("],\\s");
			
				for(String s : permSetString)
				{
					s = s.replace("[", "");
					s = s.replace("]", "");
					String[] parsed = s.split(",\\s");
				
					for (String elem : parsed)
					{
						permutation.add(Integer.parseInt(elem));
					}
				
					permutationSet.add(new ArrayList<Integer>(permutation));
					permutation.clear();
				}
				
				testCases.add(new ArrayList<ArrayList<Integer>>(permutationSet));
				permutationSet.clear();
			}
			
			br.close();
		}
		catch (IOException e)
		{
		System.out.println(e.getMessage());
	}	
		
		return testCases;
	}
	
	/*void read()
	{
		String filePath = "permutation.txt";
		File file = new File(filePath);
		
		if(file.exists())
		{
			file.delete();
			file.createNewFile();
		}
		
		PermutationGenerator p = new PermutationGenerator(filePath);
		p.generatePermutations(4, 1);
		
		String list;
		ArrayList<ArrayList<Integer>> permutationSet = new ArrayList<ArrayList<Integer>>(); 
		
		try
		{
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			
			while ((list = br.readLine()) != null)
			{
				ArrayList<Integer> arr = new ArrayList<Integer>();
				list = list.replace("[", "");
				list = list.replace("]", "");
				String[] parsedList = list.split(",\\s");
				for (String elem : parsedList)
				{
					arr.add(Integer.parseInt(elem));
				}
				
				permutationSet.add(arr);
				
				if (permutationSet.size() == 3)
					break;
			}
			
			br.close();
		}
		
		catch (IOException e)
		{
			System.out.println(e.getMessage());
		}
	}*/
}
