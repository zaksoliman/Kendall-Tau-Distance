import java.io.*;
import java.util.*;

import com.sun.org.apache.xerces.internal.parsers.IntegratedParserConfiguration;
public class Tests 
{
	public static void main(String[] args)
	{	
		//test(read("Resultats/Redo Number of medians 9.txt"));	
		
		printResultsSubsets();
	}
	
	private static void printResultsRandomPermutations()
	{
		String filePath = "Results.txt";
		File file = new File(filePath);
		PermutationGenerator pg = new PermutationGenerator("");
		MedianFinderBT medFinder;
		
		ArrayList<ArrayList<Integer>> permSetA = new ArrayList<ArrayList<Integer>>();
		ArrayList<ArrayList<Integer>> permSetB = new ArrayList<ArrayList<Integer>>();
		
		ArrayList<ArrayList<Integer>> medSetA, medSetB, results, exp;
		
		Random rnd = new Random();
		int sizeOfSet = rnd.nextInt(12)+1;
		int sizeOfPermutation = 4;
		
		PermutationGenerator pgA = new PermutationGenerator("permutations.txt");
		PermutationGenerator pgB = new PermutationGenerator("permutations.txt");
		permSetA = pgA.generateRandomPermutationSet(sizeOfPermutation, sizeOfSet);
		permSetB = pgB.generateRandomPermutationSet(sizeOfPermutation, sizeOfSet);
		
		//ArrayList<ArrayList<Integer>> AinterB = inter(permSetA, permSetB);
		ArrayList<ArrayList<Integer>> AunionB;
		//ArrayList<ArrayList<Integer>> notA;
		//ArrayList<ArrayList<Integer>> notB;
		
		try
		{
			FileWriter fw = new FileWriter(file);
			BufferedWriter bw = new BufferedWriter(fw);
			int numOfIterations = 5;
			bw.write("Median Set, Distance, Median set size, Permutation Size \n");
			
			for(int i=0; i< numOfIterations ; i++)
			{
				
				sizeOfSet = rnd.nextInt(12)+1;				
				permSetA = pgA.generateRandomPermutationSet(sizeOfPermutation, sizeOfSet);
				permSetB = pgB.generateRandomPermutationSet(sizeOfPermutation, sizeOfSet);
				AunionB = union(permSetA, permSetB);
				
				
				
				bw.write("**m=" + permSetA.size() + " n=" + permSetA.get(0).size() + "\n\n");
				
				bw.write("A=" + permSetA + "\n");
				bw.write("B=" + permSetB + "\n");			
				bw.write("AUB=" + AunionB + "\n\n");
				
				bw.write("Median Set, Distance, Median set size, Permutation Size \n");
				
				medFinder = new MedianFinderBT(permSetA);			
				medSetA = medFinder.FindMed();			
				bw.write("M(A)=" + medSetA + ",D_kt=" + medFinder.getKendallTauDistance() + ",#=" + medSetA.size() + "\n");
			
				medFinder = new MedianFinderBT(permSetB);
				medSetB = medFinder.FindMed();
				bw.write("M(B)=" + medSetB + ",D_kt=" + medFinder.getKendallTauDistance() + ",#=" + medSetB.size() + "\n");
			
				medFinder = new MedianFinderBT(AunionB);
				results = medFinder.FindMed();
				bw.write("M(AUB)=" + results + ",D_kt=" + medFinder.getKendallTauDistance() + ",#=" + results.size() + "\n");
				
				exp = union(medSetA, medSetB);
				medFinder = new MedianFinderBT(exp);
				results = medFinder.FindMed();				
				bw.write("M(M(A) U M(B))=" + results + ",D_kt=" + medFinder.getKendallTauDistance() + ",#=" + results.size() + "\n\n");
			}
			
			bw.close();
		}
		catch (IOException e)
		{
			System.out.println(e.getMessage());
		}
		
	}

	private static void printResultsSubsets()
	{
		String filePath = "Permutation 3.txt";
		File file = new File(filePath);
		PermutationGenerator pg = new PermutationGenerator("");
		MedianFinderBT medFinder;
		
		ArrayList<ArrayList<ArrayList<Integer>>> testCases;
		
		ArrayList<ArrayList<Integer>> results;
				
		try
		{
			FileWriter fw = new FileWriter(file);
			BufferedWriter bw = new BufferedWriter(fw);
			testCases = pg.getAllPermutationSets(3, 6);
			bw.write("Median Set, Distance, Median set size, Permutation Size \n");
			
			for (ArrayList<ArrayList<Integer>> permSet : testCases) {
				medFinder = new MedianFinderBT(permSet);
				
				results = medFinder.FindMed();		
				
				bw.write(results + ",D_kt=" + medFinder.getKendallTauDistance() + ",#=" + results.size() + "\n");
			}
			
			
			bw.close();
		}
		catch (IOException e)
		{
			System.out.println(e.getMessage());
		}
	}
	
	
 	private static ArrayList<ArrayList<ArrayList<Integer>>> read(String filePath)
	{
		String input;
		//Hold all the permutation sets
		ArrayList<ArrayList<ArrayList<Integer>>> testCases = new ArrayList<ArrayList<ArrayList<Integer>>>();
		ArrayList<ArrayList<Integer>> permutationSet = new ArrayList<ArrayList<Integer>>();
		ArrayList<ArrayList<Integer>> medSet = new ArrayList<ArrayList<Integer>>();
		ArrayList<Integer> permutation = new ArrayList<Integer>();
		
		File file = new File(filePath);
		String[] inputData = null;
		String[] permSetString = null;
		String[] medSetString = null;
		//String[] medStringArray = null;
		
		try
		{
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			
			br.readLine();
			String tst = br.readLine();
			
			//System.out.print(tst);
			HashMap<String, Object> result = new HashMap<String, Object>();
			
			while(!(input = br.readLine()).isEmpty())
			{
				inputData = input.split("\\t");		

				permSetString = inputData[0].split("],\\s");
				int startOfMedSet = inputData[1].indexOf('[');
				int endOfMedSet = inputData[1].lastIndexOf(']');
				medSetString = inputData[1].substring(startOfMedSet, endOfMedSet-1).split("],\\s"); //We get the median set
				
				String medDistString = inputData[1].split("distance")[1].split(" ")[1]; //we get the distance
				
				for(String s : permSetString)
				{
					s = s.replace("[", "");
					s = s.replace("]", "");					
					
					String[] parsedPermSet = s.split(",\\s");
					
				
					for (String elem : parsedPermSet)
					{
						permutation.add(Integer.parseInt(elem));
					}
				
					permutationSet.add(new ArrayList<Integer>(permutation));
					permutation.clear();
				}
				
				for(String s : medSetString)
				{
					s = s.replace("[", "");
					s = s.replace("]", "");				
					
					String[] parsedMedSet = s.split(",\\s");
				
					for (String elem : parsedMedSet)
					{
						permutation.add(Integer.parseInt(elem));
					}
				
					medSet.add(new ArrayList<Integer>(permutation));
					permutation.clear();
				}
				
				testCases.add(new ArrayList<ArrayList<Integer>>(permutationSet));
				result.put(MEDIAN_SET_KEY, new ArrayList<ArrayList<Integer>>(medSet));
				result.put(DISTANCE_KEY, (Integer) Integer.parseInt(medDistString));
				testResults.add(new HashMap<String, Object>(result));
				medSet.clear();
				result.clear();
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

	void readPermutations()
	{
		String filePath = "permutation.txt";
		File file = new File(filePath);
		
		if(file.exists())
		{
			file.delete();
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		PermutationGenerator p = new PermutationGenerator(filePath);
		p.generateRandomPermutationSet(4, 1);
		
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
	}
	
	@SuppressWarnings("unchecked")
	private static void test(ArrayList<ArrayList<ArrayList<Integer>>> testCases)
	{
		
		//ArrayList<ArrayList<Integer>> permSet = new ArrayList<ArrayList<Integer>>();
		
		MedianFinderBT medFinder;
		ArrayList<ArrayList<Integer>> solutions, correctSolutions;
		int distance, correctDistance;
		int index = 0;
		boolean correct = false;
		boolean allCorrect = true;
		
		for (ArrayList<ArrayList<Integer>> permutationSet : testCases) {
		
			medFinder = new MedianFinderBT(permutationSet);	
			solutions = medFinder.FindMed();
			distance = medFinder.getKendallTauDistance();
			
			correctSolutions = (ArrayList<ArrayList<Integer>>) testResults.get(index).get(MEDIAN_SET_KEY);
			correctDistance = (Integer) testResults.get(index).get(DISTANCE_KEY);
			
			if(correctDistance == distance)
				correct = true;
			else
				correct = false;
			
			for (ArrayList<Integer> sol : correctSolutions) {
				if(solutions.contains(sol))
					correct = true;
				else
					correct = false;
			}
			
			if(correct)
				System.out.println("OK!");
			else if(!correct){
				System.out.println("INCORRECT");
				allCorrect = false;
			}
	
			//System.out.println("For the set: " + permutationSet);		
			//System.out.println("Kendall tau distance = " + distance);
	
			/*for(ArrayList<Integer> sol : solutions)
			{
				System.out.println(Arrays.deepToString(sol.toArray()));
			}*/
	
			System.out.println("done with backtrack!\n");			
			index++;
		}
		
		if(allCorrect)
			System.out.println("All cases are correct");
		else if(!allCorrect)
			System.out.println("One or more incorrect cases");
		/*permSet.add(new ArrayList<Integer>(Arrays.asList(new Integer[]{2,4,5,3,6,1})));
		permSet.add(new ArrayList<Integer>(Arrays.asList(new Integer[]{4,2,1,3,6,5}))); 
		permSet.add(new ArrayList<Integer>(Arrays.asList(new Integer[]{6,1,5,4,2,3})));
	
	
		permSet.add(new ArrayList<Integer>(Arrays.asList(new Integer[]{1, 2, 3, 4})));
		permSet.add(new ArrayList<Integer>(Arrays.asList(new Integer[]{1, 2, 4, 3}))); 
		permSet.add(new ArrayList<Integer>(Arrays.asList(new Integer[]{1, 4, 2, 3})));
	
		MedianFinderBT medFinder = new MedianFinderBT(permSet);
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
		
		
	}
	
	static final private String MEDIAN_SET_KEY = "MedianSet"; 
	static final private String DISTANCE_KEY = "distance";
	static private ArrayList<HashMap<String, Object>> testResults = new ArrayList<HashMap<String,Object>>();
}
