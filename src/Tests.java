/*import java.io.*;
import java.util.*;

public class Tests 
{
	public static void main(String[] args) throws IOException 
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
		
		MedianFinderBT medFinder = new MedianFinderBT(4, permutationSet);
		medFinder.FindMed();
		
		System.out.println("done!");	

	}
}
*/