import java.io.*;
import java.util.*;

public class Tests 
{
	public static void main(String[] args) throws IOException 
	{
		
		/*String filePath = "permutation.txt";
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
		}*/
		
		
		ArrayList<ArrayList<Integer>> permSet = new ArrayList<ArrayList<Integer>>();
		permSet.add(new ArrayList<Integer>(Arrays.asList(new Integer[]{2,4,5,3,6,1})));
		permSet.add(new ArrayList<Integer>(Arrays.asList(new Integer[]{4,2,1,3,6,5}))); 
		permSet.add(new ArrayList<Integer>(Arrays.asList(new Integer[]{6,1,5,4,2,3})));
		
		/*MedianFinderBT medFinder = new MedianFinderBT(6, permSet);
		medFinder.FindMed();
		
		System.out.println("Kendall tau distance = " + medFinder.getKendallTauDistance());
		
		ArrayList<ArrayList<Integer>> solutions = medFinder.solutions;
		
		for(ArrayList<Integer> sol : solutions)
		{
			System.out.println(Arrays.deepToString(sol.toArray()));
		}
		
		System.out.println("done with backtrack!");*/
		
		MedianFinderHeuristic medianFinderH = new MedianFinderHeuristic(3, 6, permSet);
		
		ArrayList<ArrayList<Integer>> solutions = medianFinderH.FindMedian();
		
		System.out.println("Kendall tau distance = " + medianFinderH.getDistance());
		
		for(ArrayList<Integer> sol : solutions)
		{
			System.out.println(Arrays.deepToString(sol.toArray()));
		}
		

	}
}
