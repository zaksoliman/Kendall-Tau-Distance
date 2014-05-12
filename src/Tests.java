import java.io.*;
import java.util.*;
public class Tests 
{
	public static void main(String[] args)
	{	
		String filePath = "Results.txt";
		File file = new File(filePath);
		
		ArrayList<ArrayList<Integer>> permSetA = new ArrayList<ArrayList<Integer>>();
		ArrayList<ArrayList<Integer>> permSetB = new ArrayList<ArrayList<Integer>>();
		
		//ArrayList<ArrayList<Integer>> medOfSetA = new ArrayList<ArrayList<Integer>>();
		//ArrayList<ArrayList<Integer>> medOfSetB = new ArrayList<ArrayList<Integer>>();
		
		Random rnd = new Random();
		int sizeOfSet; // = rnd.nextInt(12)+1;
		int sizeOfPermutation = 4;
		
		PermutationGenerator pgA = new PermutationGenerator("permutations.txt");
		PermutationGenerator pgB = new PermutationGenerator("permutations.txt");
		//permSetA = pgA.generatePermutations(sizeOfPermutation, sizeOfSet);
		//permSetB = pgB.generatePermutations(sizeOfPermutation, sizeOfSet);
		
		//ArrayList<ArrayList<Integer>> AinterB = inter(permSetA, permSetB);
		ArrayList<ArrayList<Integer>> AunionB; //= union(permSetA, permSetB);
		
		/*System.out.print(permSetA + "\n");
		System.out.println(permSetB);
		
		System.out.println(AinterB);
		System.out.println(AunionB);*/
			
		
		try
		{
			FileWriter fw = new FileWriter(file);
			BufferedWriter bw = new BufferedWriter(fw);
			int numOfIterations = 5;
			
			for(int i=0; i< numOfIterations ; i++)
			{
				
				sizeOfSet = rnd.nextInt(12)+1;
				
				permSetA = pgA.generatePermutations(sizeOfPermutation, sizeOfSet);
				permSetB = pgB.generatePermutations(sizeOfPermutation, sizeOfSet);
				AunionB = union(permSetA, permSetB);
				bw.write("**m=" + permSetA.size() + " n=" + permSetA.get(0).size() + "\n\n");
				
				bw.write("A=" + permSetA + "\n");
				bw.write("B=" + permSetB + "\n\n");
			
				bw.write("AUB=" + AunionB + "\n\n");
				//bw.write("A inter B=" + AinterB + "\n\n");
			
				MedianFinderBT medFinder = new MedianFinderBT(permSetA);			
				ArrayList<ArrayList<Integer>> results = medFinder.FindMed();			
				bw.write("M(A)=" + results + ",Distance:" + medFinder.getKendallTauDistance() + "\n\n");
			
				medFinder = new MedianFinderBT(permSetB);
				results = medFinder.FindMed();
				bw.write("M(B)=" + results + ",Distance:" + medFinder.getKendallTauDistance() + "\n\n");
			
				medFinder = new MedianFinderBT(AunionB);
				results = medFinder.FindMed();
				bw.write("M(A U B)=" + results + ",Distance:" + medFinder.getKendallTauDistance() + "\n\n");
			}
			bw.close();
		}
		catch (IOException e)
		{
			System.out.println(e.getMessage());
		}	
			
		
		
		/*
		permSet.add(new ArrayList<Integer>(Arrays.asList(new Integer[]{2,4,5,3,6,1})));
		permSet.add(new ArrayList<Integer>(Arrays.asList(new Integer[]{4,2,1,3,6,5}))); 
		permSet.add(new ArrayList<Integer>(Arrays.asList(new Integer[]{6,1,5,4,2,3})));
		*/
		
		/*permSet.add(new ArrayList<Integer>(Arrays.asList(new Integer[]{1, 2, 3, 4})));
		permSet.add(new ArrayList<Integer>(Arrays.asList(new Integer[]{1, 2, 4, 3}))); 
		permSet.add(new ArrayList<Integer>(Arrays.asList(new Integer[]{1, 4, 2, 3})));*/
		
		/*MedianFinderBT medFinder = new MedianFinderBT(permSet.get(0).size(), permSet);
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
		}*/
		
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
	
	private static void print(String filePath)
	{
		//TODO: implement a function to print out results in a file
	}
	
	private static ArrayList<ArrayList<Integer>> union(ArrayList<ArrayList<Integer>> setA, ArrayList<ArrayList<Integer>> setB)
	{
		ArrayList<ArrayList<Integer>> union = new ArrayList<ArrayList<Integer>>(setA);
		
		for (ArrayList<Integer> perm : setB) {
			if(!union.contains(perm))
				union.add(perm);
		}
		
		return union;
	}
	
	private static ArrayList<ArrayList<Integer>> inter(ArrayList<ArrayList<Integer>> setA, ArrayList<ArrayList<Integer>> setB){
		
		ArrayList<ArrayList<Integer>> intersection = new ArrayList<ArrayList<Integer>>();
		
		for (ArrayList<Integer> perm : setA) {
			if(setB.contains(perm))
				intersection.add(perm);
		}
		
		return intersection;	
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
