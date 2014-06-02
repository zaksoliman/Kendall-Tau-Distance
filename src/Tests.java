import java.io.*;
import java.lang.reflect.Array;
import java.util.*;

public class Tests 
{
	public static void main(String[] args)
	{	
		/*String inputData = "[[2, 9, 5, 11, 3, 6, 4, 8, 10, 7, 1], [2, 9, 11, 3, 5, 6, 4, 8, 10, 7, 1], [2, 9, 3, 5, 11, 6, 4, 8, 10, 7, 1], [2, 9, 3, 5, 6, 4, 11, 8, 10, 7, 1], [2, 9, 3, 5, 4, 11, 6, 8, 10, 7, 1]]";
		int startOfMedSet = inputData.indexOf('[');
		int endOfMedSet = inputData.lastIndexOf(']');
		String[] medSetString = inputData.substring(startOfMedSet, endOfMedSet-1).split("],\\s"); //We get the median set
		
		ArrayList<ArrayList<Integer>> medSet = new ArrayList<ArrayList<Integer>>();
		ArrayList<Integer> permutation = new ArrayList<Integer>();
		
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
		
		System.out.println(medSet);
		
		MedianFinderBT mf = new MedianFinderBT(medSet);
		
		System.out.println(mf.FindMed());*/
		
		
		
		/*if(test(read("Resultats/Redo Number of medians 9.txt")))
			System.out.println("All good!");
		else 
			System.out.println("There are errors");*/
		
		/*for (int i = 3; i <=4; i++) {
			printResultsSubsets(i);
		}
		*/
		
		/*for (int i=3; i<=7 ; i++){
			printResultsRandomPermutations(70, i);
			System.out.println("Done with permutations of size " + i);
		}*/
		
		/*Random rnd = new Random();
		PermutationGenerator pg = new PermutationGenerator("");
		System.out.println("Starting...");
		
		try {
			int counter = 0;
			while(counter <= 50){
				convergenceTest(pg.generateRandomPermutationSet(4, (rnd.nextInt(10)+2) 4));
				System.out.println("At: " + counter);
				counter++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}	*/
		
		//[[],[],[],[],[],[],[],[],[]]
		
		ArrayList<ArrayList<Integer>> permutationSet = new ArrayList<ArrayList<Integer>>();
		//ArrayList<Integer> permutation = new ArrayList<Integer>();
		//permutation.addAll(Arrays.asList(new Integer[]{8, 7, 10, 12, 11, 9, 2, 3, 4, 1, 13, 6, 5}));
		
		permutationSet.add(new ArrayList<Integer>(Arrays.asList(new Integer[]{1,10,7,9,12,8,6,2,11,4,13,3,5})));
		permutationSet.add(new ArrayList<Integer>(Arrays.asList(new Integer[]{4,7,10,5,11,6,9,3,8,12,13,2,1})));
		permutationSet.add(new ArrayList<Integer>(Arrays.asList(new Integer[]{6,9,3,11,12,10,4,2,8,1,13,7,5})));
		permutationSet.add(new ArrayList<Integer>(Arrays.asList(new Integer[]{8,4,9,12,1,5,2,3,7,6,13,11,10})));
		permutationSet.add(new ArrayList<Integer>(Arrays.asList(new Integer[]{8,4,10,6,13,7,9,1,3,2,12,5,11})));
		permutationSet.add(new ArrayList<Integer>(Arrays.asList(new Integer[]{8,7,10,12,11,9,2,3,4,1,13,6,5})));
		permutationSet.add(new ArrayList<Integer>(Arrays.asList(new Integer[]{10,9,1,2,8,7,5,13,11,4,6,12,3})));
		permutationSet.add(new ArrayList<Integer>(Arrays.asList(new Integer[]{12,1,5,9,8,10,13,3,11,2,4,6,7})));
		permutationSet.add(new ArrayList<Integer>(Arrays.asList(new Integer[]{13,2,7,8,4,5,9,6,3,1,12,11,10})));
		//MedianFinderBT mf = new MedianFinderBT(permutationSet);
		
		for (ArrayList<Integer> permutation : permutationSet) {
			System.out.println("The Distance between: " + permutation + " and the set is " +KendallTauDistSet(permutation, permutationSet));
		}
		
		
		//printResultsRandomPermutationsTXT(50);
	}

	private static void printResultsRandomPermutationsTXT(int numbOfInterations)
	{
		String filePath = "Results/RandomPermutations_NoCommon.txt";
				
		File file = new File(filePath);
				
		if(file.exists()){
			file.delete();
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
				
		MedianFinderBT medFinder;		
		ArrayList<ArrayList<Integer>> permSetA = new ArrayList<ArrayList<Integer>>();
		ArrayList<ArrayList<Integer>> permSetB = new ArrayList<ArrayList<Integer>>();		
		ArrayList<ArrayList<Integer>> medSetA, medSetB, medSetUnion, unionMedInterA, unionMedInterB, AunionB, medAmedB;
		
		/*int min = 1, max =12;
		if (max % 2 == 0) 
			--max;
		if (min % 2 == 0) 
			++min;*/
		Random rndSetSize = new Random(1);
		Random rndPermSize = new Random(0);
		int sizeOfSet;
		int sizeOfPermutation = 4;
		int medADist,medBDist,medUnionDist;
		
		PermutationGenerator pgA = new PermutationGenerator("permutations.txt");
		PermutationGenerator pgB = new PermutationGenerator("permutations.txt");
		
		try
		{
			//For txt
			FileWriter fw = new FileWriter(file);
			BufferedWriter bw = new BufferedWriter(fw);
			
			boolean printA = true;
			boolean printB = true;
			
			for(int i=0; i< numbOfInterations ; i++)
			{	
				sizeOfSet = rndSetSize.nextInt(11)+1; //min + 2*(int)(Math.random()*((max-min)/2+1));
				sizeOfPermutation = rndPermSize.nextInt(6)+4;
				permSetA = pgA.generateRandomPermutationSet(sizeOfPermutation, sizeOfSet);
				permSetB = pgB.generateRandomPermutationSet(sizeOfPermutation, sizeOfSet);
				AunionB = union(permSetA, permSetB);				
												
				medFinder = new MedianFinderBT(permSetA);			
				medSetA = medFinder.FindMed();
				medADist = medFinder.getKendallTauDistance();
				
				medFinder = new MedianFinderBT(permSetB);
				medSetB = medFinder.FindMed();
				medBDist = medFinder.getKendallTauDistance();
				
				
				if(inter(medSetA, permSetA).size()>=1)
					printA = false;
				
				if(inter(medSetB,permSetB).size() >=1)
					printB = false;
				
				bw.write("**m=" + permSetA.size() + " n=" + permSetA.get(0).size() + "\n\n");				
				bw.write("A=" + permSetA + "\n");
				bw.write("B=" + permSetB + "\n");			
				bw.write("AUB=" + AunionB + "\n\n");
				
				medFinder = new MedianFinderBT(AunionB);
				medSetUnion = medFinder.FindMed();
				medUnionDist = medFinder.getKendallTauDistance();
								
				medAmedB = inter(medSetA, medSetB);
				unionMedInterA = inter(medSetUnion, permSetA);
				unionMedInterB = inter(medSetUnion, permSetB);
				
				bw.write("M(A)=" + medSetA + ",D_kt(M(A),A)=" + medADist + ",#=" + medSetA.size() + "\n");			
				bw.write("M(B)=" + medSetB + ",D_kt(M(B),B)=" + medBDist + ",#=" + medSetB.size() + "\n");
				bw.write("M(AUB)=" + medSetUnion + ",D_kt(U)=" + medUnionDist + ",#=" + medSetUnion.size() + "\n");
				
				int maxDistance = 0;
				int minDistance = Integer.MAX_VALUE;
				int distance;
				
				ArrayList<ArrayList<Integer>> farthestPermutations = new ArrayList<ArrayList<Integer>>();
				ArrayList<ArrayList<Integer>> closestPermutations = new ArrayList<ArrayList<Integer>>();
				if(printA){
					
					bw.write("\n\n");
					//Find and Print the distance between every permutation of A and A
				for (ArrayList<Integer> perm : permSetA) {
					distance = KendallTauDistSet(perm, permSetA);
					
					if (distance>=maxDistance){
						maxDistance = distance;
						farthestPermutations.add(perm);
					}
					if(distance<=minDistance){
						minDistance=distance;
						closestPermutations.add(perm);
					}					
					
					bw.write("Distance between "+ perm +" and A is " + distance + "\n");
				}
				
				//Print the distance between every permutation of Med(A) and the "farthest" and "closest" permutation in A to A
				
				bw.write("\n\n");
				bw.write("For closest permutations \n");	
				//closest first
				for (ArrayList<Integer> median : medSetA) {
					for (ArrayList<Integer> perm : closestPermutations) {
						bw.write("Distance between " + median +" and " + perm + " is " + KendallTauDist(median, perm) + "\n");
					}
				}
				bw.write("\n\n");
				
				bw.write("For Farthest permutations \n");			
				//farthest
				for (ArrayList<Integer> median : medSetA) {
					for (ArrayList<Integer> perm : farthestPermutations) {
						bw.write("Distance between " + median +" and " + perm + " is " + KendallTauDist(median, perm) + "\n");
					}
				}
				
				bw.write("\n\n");
				}
				
				
				if(printB){				
				//Find and Print the distance between every permutation of B and B
				closestPermutations.clear();
				farthestPermutations.clear();
				maxDistance = 0;
				minDistance = Integer.MAX_VALUE;
				
				for (ArrayList<Integer> perm : permSetB) {
					distance = KendallTauDistSet(perm, permSetB);
					
					if (distance>=maxDistance){
						maxDistance = distance;
						farthestPermutations.add(perm);
					}
					if(distance<=minDistance){
						minDistance=distance;
						closestPermutations.add(perm);
					}					
					
					bw.write("Distance between "+ perm +" and B is " + distance + "\n");
				}
				
				//Print the distance between every permutation of Med(A) and the "farthest" and "closesest" permutation in A to A
				
				bw.write("\n\n");
				bw.write("For closest permutations\n");	
				//closest first
				for (ArrayList<Integer> median : medSetB) {
					for (ArrayList<Integer> perm : closestPermutations) {
						bw.write("Distance between " + median +" and " + perm + " is " + KendallTauDist(median, perm) + "\n");
					}
				}
				bw.write("\n\n");
				
				bw.write("For Farthest permutations\n");			
				//farthest
				for (ArrayList<Integer> median : medSetB) {
					for (ArrayList<Integer> perm : farthestPermutations) {
						bw.write("Distance between " + median +" and " + perm + " is " + KendallTauDist(median, perm) + "\n");
					}
				}
				bw.write("\n\n");
				}
				
				medFinder = new MedianFinderBT(medSetA);
				medSetA=medFinder.FindMed();
				bw.write("M(M(A))=" + medSetA + "\n\n");
				
				medFinder = new MedianFinderBT(medSetB);
				medSetB=medFinder.FindMed();
				bw.write("M(M(B))=" + medSetB + "\n\n");				
				bw.write("M(A)M(B)=" + medAmedB + "\n");
				
				
				bw.write("\n\n");
				
				bw.write("M(AUB) has "+ countCommonElements(medSetUnion, permSetA) + " permutation in common with A" + "\n");
				bw.write("M(AUB)A =" + unionMedInterA + "\n");
				bw.write("M(AUB) has "+ countCommonElements(medSetUnion, permSetB) + " permutation in common with B" + "\n");
				bw.write("M(AUB)B =" + unionMedInterB + "\n\n");
								
				
				bw.write("\n\n");
				bw.write("M(AUB) has "+ countCommonElements(medSetUnion, medSetA) + " permutation in common with M(A)" + "\n");
				bw.write("M(AUB)M(A) =" + inter(medSetUnion, medSetA) + "\n");
				bw.write("M(AUB) has "+ countCommonElements(medSetUnion, medSetB) + " permutation in common with M(B)" + "\n");
				bw.write("M(AUB)M(B) =" + inter(medSetUnion, medSetB) + "\n\n");
					
			}
			
			bw.close();
		}
		catch (IOException e)
		{
			System.out.println(e.getMessage());
		}
	}
	
	private static void printResultsRandomPermutations(int numbOfInterations, int sizeOfPerm)
	{
		String filePath = "Results/RandomPermutationSets(NoCommon)_"+ sizeOfPerm +".txt"; 
		String filePathCSV = "Results/RandomPermutationSets(NoCommon)_" + sizeOfPerm + ".csv";
		
		File file = new File(filePath);
		File csv = new File(filePathCSV);
		
		if(file.exists()){
			file.delete();
			try {
				file.createNewFile();
			} catch (IOException e) {
			
				e.printStackTrace();
			}
		}
		
		if(csv.exists()){
			csv.delete();
			try {
				csv.createNewFile();
			} catch (IOException e) {
			
				e.printStackTrace();
			}
		}
		
		MedianFinderBT medFinder;		
		ArrayList<ArrayList<Integer>> permSetA = new ArrayList<ArrayList<Integer>>();
		ArrayList<ArrayList<Integer>> permSetB = new ArrayList<ArrayList<Integer>>();		
		ArrayList<ArrayList<Integer>> medSetA, medSetB, medSetUnion, unionMedInterA, unionMedInterB, AunionB, medAmedB;
		
		/*int min = 1, max =12;
		if (max % 2 == 0) 
			--max;
		if (min % 2 == 0) 
			++min;*/
		Random rnd = new Random(1);
		int sizeOfSet;
		int sizeOfPermutation = sizeOfPerm;
		int maxSize = (fact(sizeOfPermutation)/2);
		int universeSize, medADist,medBDist,medUnionDist,medSetA_size, medSetB_size, medSetUnion_size, unionMedInterA_size,
		unionMedInterB_size, AunionB_size, medAmedB_size, medAUMedB_size, unionMedInterMedA_size,unionMedInterMedB_size, 
		unionMedInterUnionSet_size,MedAinterA_size,MedBinterB_size;
		
		PermutationGenerator pgA = new PermutationGenerator("permutations.txt");
		PermutationGenerator pgB = new PermutationGenerator("permutations.txt");
		
		try
		{
			//For txt
			FileWriter fw = new FileWriter(file);
			BufferedWriter bw = new BufferedWriter(fw);
			
			//For csv
			FileWriter fw_csv = new FileWriter(csv);
			BufferedWriter bw_csv = new BufferedWriter(fw_csv);
			
			//bw.write("Permutation set size,Permutation Size,Permutation Set,Median Set,Median set size,Distance\n");
			//bw_csv.write("|U|,|A|,|M(A)|,|M(A)A|,|M(B)|,|M(B)B|,|AUB|,|M(AUB)|,|M(AUB)AUB|,|M(A)UM(B)|,|M(A)M(B)|,|M(AUB)M(A)|,|M(AUB)M(B)|,|M(AUB)A|,|M(AUB)B|\n");
			bw_csv.write("|A|,|M(A)|,|M(A)A|,|M(B)|,|M(B)B|\n");
			
			for(int i=0; i< numbOfInterations ; i++)
			{				
				//universeSize = fact(sizeOfPermutation);
				sizeOfSet = rnd.nextInt(maxSize-1)+1;//min + 2*(int)(Math.random()*((max-min)/2+1));;				
				permSetA = pgA.generateRandomPermutationSet(sizeOfPermutation, sizeOfSet);
				permSetB = pgB.generateRandomPermutationSet(sizeOfPermutation, sizeOfSet);
				AunionB = union(permSetA, permSetB);				
				
				bw.write("(" + i + ")" + ". **m=" + permSetA.size() + " n=" + permSetA.get(0).size() + "\n\n");
				
				bw.write("A=" + permSetA + "\n");
				bw.write("B=" + permSetB + "\n");			
				bw.write("AUB=" + AunionB + "\n\n");
				
				medFinder = new MedianFinderBT(permSetA);			
				medSetA = medFinder.FindMed();
				medADist = medFinder.getKendallTauDistance();
				
				medFinder = new MedianFinderBT(permSetB);
				medSetB = medFinder.FindMed();
				medBDist = medFinder.getKendallTauDistance();
				
				medFinder = new MedianFinderBT(AunionB);
				medSetUnion = medFinder.FindMed();
				medUnionDist = medFinder.getKendallTauDistance();
				
				AunionB_size = AunionB.size();
				medSetA_size = medSetA.size();
				medSetB_size = medSetB.size();	
				medSetUnion_size = medSetUnion.size();
				medSetUnion_size = medSetUnion.size();
				
				medAUMedB_size = union(medSetA, medSetB).size();				
				
				medAmedB = inter(medSetA, medSetB);
				unionMedInterA = inter(medSetUnion, permSetA);
				unionMedInterB = inter(medSetUnion, permSetB);
				
				unionMedInterA_size = unionMedInterA.size();
				unionMedInterB_size = unionMedInterB.size();
				medAmedB_size = medAmedB.size();				
				unionMedInterMedA_size = inter(medSetUnion, medSetA).size();
				unionMedInterMedB_size = inter(medSetUnion, medSetB).size();
				
				MedAinterA_size = inter(medSetA, permSetA).size();
				MedBinterB_size = inter(medSetB, permSetB).size();
				
				unionMedInterUnionSet_size = inter(medSetUnion,AunionB).size();
				
				bw.write("M(A)=" + medSetA + ",D_kt(M(A),A)=" + medADist + ",#=" + medSetA.size() + "\n");			
				bw.write("M(B)=" + medSetB + ",D_kt(M(B),B)=" + medBDist + ",#=" + medSetB.size() + "\n");
				bw.write("M(AUB)=" + medSetUnion + ",D_kt(U)=" + medUnionDist + ",#=" + medSetUnion.size() + "\n");
				
				
				int maxDistance = 0;
				int minDistance = Integer.MAX_VALUE;
				int distance;
				
				ArrayList<ArrayList<Integer>> farthestPermutations = new ArrayList<ArrayList<Integer>>();
				ArrayList<ArrayList<Integer>> closestPermutations = new ArrayList<ArrayList<Integer>>();
									
				bw.write("\n\n");
				//Find and Print the distance between every permutation of A and A
				for (ArrayList<Integer> perm : permSetA) {
					distance = KendallTauDistSet(perm, permSetA);
					
					if (distance>maxDistance){
						maxDistance = distance;
						farthestPermutations.clear();
						farthestPermutations.add(perm);
					}
					else if(distance<minDistance){
						minDistance=distance;
						closestPermutations.clear();
						closestPermutations.add(perm);
					}
					
					if(distance == maxDistance)
						farthestPermutations.add(perm);
					if(distance == minDistance)
						closestPermutations.add(perm);
					
					bw.write("Distance between "+ perm +" and A is " + distance + "\n");
				}
				
				//Print the distance between every permutation of Med(A) and the "farthest" and "closest" permutation in A to A
				
				bw.write("\n\n");
				bw.write("For closest permutations \n");	
				//closest first
				for (ArrayList<Integer> median : medSetA) {
					for (ArrayList<Integer> perm : closestPermutations) {
						bw.write("Distance between " + median +" and " + perm + " is " + KendallTauDist(median, perm) + "\n");
					}
				}
				bw.write("\n\n");
				
				bw.write("For Farthest permutations \n");			
				//farthest
				for (ArrayList<Integer> median : medSetA) {
					for (ArrayList<Integer> perm : farthestPermutations) {
						bw.write("Distance between " + median +" and " + perm + " is " + KendallTauDist(median, perm) + "\n");
					}
				}
				
				bw.write("\n\n");
												
				//Find and Print the distance between every permutation of B and B
				closestPermutations.clear();
				farthestPermutations.clear();
				maxDistance = 0;
				minDistance = Integer.MAX_VALUE;
				
				for (ArrayList<Integer> perm : permSetB) {
					distance = KendallTauDistSet(perm, permSetB);
					
					if (distance>maxDistance){
						maxDistance = distance;
						farthestPermutations.clear();
						farthestPermutations.add(perm);
					}
					else if(distance<minDistance){
						minDistance=distance;
						closestPermutations.clear();
						closestPermutations.add(perm);
					}	
					
					if(distance == maxDistance)
						farthestPermutations.add(perm);
					if(distance == minDistance)
						closestPermutations.add(perm);
					
					bw.write("Distance between "+ perm +" and B is " + distance + "\n");
				}
				
				//Print the distance between every permutation of Med(A) and the "farthest" and "closesest" permutation in A to A
				
				bw.write("\n\n");
				bw.write("For closest permutations\n");	
				//closest first
				for (ArrayList<Integer> median : medSetB) {
					for (ArrayList<Integer> perm : closestPermutations) {
						bw.write("Distance between " + median +" and " + perm + " is " + KendallTauDist(median, perm) + "\n");
					}
				}
				bw.write("\n\n");
				
				bw.write("For Farthest permutations\n");			
				//farthest
				for (ArrayList<Integer> median : medSetB) {
					for (ArrayList<Integer> perm : farthestPermutations) {
						bw.write("Distance between " + median +" and " + perm + " is " + KendallTauDist(median, perm) + "\n");
					}
				}
				bw.write("\n\n");
												
				/*bw.write("M(A)M(B)=" + medAmedB + "\n");
				
				int index = 0;
				bw.write("Distances between permutations of M(AUB) and A\n");
				for (ArrayList<Integer> perm : medSetUnion) {
					bw.write("D_kt(p'[" + index + "],A)" + "=" + KendallTauDistSet(perm, permSetA) + "\n");
					index++;
				}
				
				index=0;
				bw.write("Distances between permutations of M(AUB) and B\n");
				for (ArrayList<Integer> perm : medSetUnion) {
					bw.write("D_kt(p'[" + index + "],B)" + "=" + KendallTauDistSet(perm, permSetB) + "\n");
					index++;
				}
				bw.write("\n\n");
				
				bw.write("M(AUB) has "+ countCommonElements(medSetUnion, permSetA) + " permutation in common with A" + "\n");
				bw.write("M(AUB)A =" + unionMedInterA + "\n");
				bw.write("M(AUB) has "+ countCommonElements(medSetUnion, permSetB) + " permutation in common with B" + "\n");
				bw.write("M(AUB)B =" + unionMedInterB + "\n\n");
				
				bw.write("Distances between permutations of M(AUB)A and A\n");
				index=0;
				for (ArrayList<Integer> perm : unionMedInterA) {
					bw.write("D_kt(p[" + index + "],A)" + "=" + KendallTauDistSet(perm, permSetA) + "\n");
					index++;
				}
				bw.write("\n\n");
				bw.write("Distances between permutations of M(AUB)B and B\n");
				index=0;
				for (ArrayList<Integer> perm : unionMedInterB) {
					bw.write("D_kt(p[" + index + "],B)" + "=" + KendallTauDistSet(perm, permSetB) + "\n");
					index++;
				}*/
				bw.write("\n\n");
				/*bw.write("M(AUB) has "+ countCommonElements(medSetUnion, medSetA) + " permutation in common with M(A)" + "\n");
				bw.write("M(AUB)M(A) =" + inter(medSetUnion, medSetA) + "\n");
				bw.write("M(AUB) has "+ countCommonElements(medSetUnion, medSetB) + " permutation in common with M(B)" + "\n");
				bw.write("M(AUB)M(B) =" + inter(medSetUnion, medSetB) + "\n\n");*/
				
				
				//|U|,|A|,|M(A)|,|M(A)A|,|M(B)|,|M(B)B|,|AUB|,|M(AUB)|,|M(AUB)AUB|,|M(A)UM(B)|,|M(A)M(B)|,|M(AUB)M(A)|,|M(AUB)M(B)|,|M(AUB)A|,|M(AUB)B|
				/*bw_csv.write(universeSize + "," + sizeOfSet + "," + medSetA_size + "," + MedAinterA_size + "," + medSetB_size + "," + MedBinterB_size
						+ "," + AunionB_size + "," + medSetUnion_size + "," + unionMedInterUnionSet_size + "," + medAUMedB_size + "," 
						+ medAmedB_size +","+ unionMedInterMedA_size + "," + unionMedInterMedB_size +  "," 
						+ unionMedInterA_size + "," + unionMedInterB_size + "\n");*/
				
				//|A|,|M(A)|,|M(A)A|,|M(B)|,|M(B)B|
				bw_csv.write(sizeOfSet + "," + medSetA_size + "," + MedAinterA_size + "," + medSetB_size + "," + MedBinterB_size + "\n");
				
			}
			
			bw_csv.close();
			bw.close();
		}
		catch (IOException e)
		{
			System.out.println(e.getMessage());
		}
		
	}
	
	private static void printResultsRandomPermutationsCSV(int numbOfInterations)
	{
		String filePathCSV = "RandomPermutationsOddSets.csv";
		
		File csv = new File(filePathCSV);
		
		if(csv.exists()){
			csv.delete();
			try {
				csv.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		MedianFinderBT medFinder;		
		ArrayList<ArrayList<Integer>> permSetA = new ArrayList<ArrayList<Integer>>();
		ArrayList<ArrayList<Integer>> permSetB = new ArrayList<ArrayList<Integer>>();		
		ArrayList<ArrayList<Integer>> medSetA, medSetB, medSetUnion, unionMedInterA, unionMedInterB, AunionB, medAmedB, medAUMedB;
		
		int min = 1, max =12;
		if (max % 2 == 0) --max;
		if (min % 2 == 0) ++min;
		int sizeOfSet;
		int sizeOfPermutation = 4;
		int universeSize, medADist,medBDist,medUnionDist,medSetA_size, medSetB_size, medSetUnion_size, unionMedInterA_size,
		unionMedInterB_size, AunionB_size, medAmedB_size, medAUMedB_size, unionMedInterMedA_size,unionMedInterMedB_size;
		
		PermutationGenerator pgA = new PermutationGenerator("");
		PermutationGenerator pgB = new PermutationGenerator("");
		
		try
		{
			FileWriter fw_csv = new FileWriter(csv);
			BufferedWriter bw_csv = new BufferedWriter(fw_csv);
			
			//bw.write("Permutation set size,Permutation Size,Permutation Set,Median Set,Median set size,Distance\n");
			bw_csv.write("|U|,|A|,n,|M(A)|,|M(B)|,|AUB|,|M(AUB)|,|M(A)UM(B)|,|M(A)M(B)|,|M(AUB)M(A)|,|M(AUB)M(B)|,|M(AUB)A|,|M(AUB)B|\n");			
			
			for(int i=0; i< numbOfInterations ; i++)
			{
				universeSize = fact(sizeOfPermutation);
				sizeOfSet = min + 2*(int)(Math.random()*((max-min)/2+1));;				
				permSetA = pgA.generateRandomPermutationSet(sizeOfPermutation, sizeOfSet);
				permSetB = pgB.generateRandomPermutationSet(sizeOfPermutation, sizeOfSet);
				AunionB = union(permSetA, permSetB);
				AunionB_size = AunionB.size();
				
				medFinder = new MedianFinderBT(permSetA);			
				medSetA = medFinder.FindMed();
				medADist = medFinder.getKendallTauDistance();				
				medSetA_size = medSetA.size();				
				
				medFinder = new MedianFinderBT(permSetB);
				medSetB = medFinder.FindMed();
				medBDist = medFinder.getKendallTauDistance();
				medSetB_size = medSetB.size();				
				
				medFinder = new MedianFinderBT(AunionB);
				medSetUnion = medFinder.FindMed();
				medUnionDist = medFinder.getKendallTauDistance();
				medSetUnion_size = medSetUnion.size();	
				
				medAUMedB_size = union(medSetA, medSetB).size();
				
				unionMedInterA_size = inter(medSetUnion, permSetA).size();
				unionMedInterB_size = inter(medSetUnion, permSetB).size();
				medAmedB_size = inter(medSetA,medSetB).size();
				
				unionMedInterMedA_size = inter(medSetUnion, medSetA).size();
				unionMedInterMedB_size = inter(medSetUnion, medSetB).size();
				
				medSetA=null;
				medSetB=null;
				medSetUnion=null;			
				
				bw_csv.write(universeSize + "," + sizeOfSet + "," + sizeOfPermutation + "," + medSetA_size + "," + medSetB_size 
						+ "," + AunionB_size + "," + medSetUnion_size + "," + medAUMedB_size + "," 
						+ medAmedB_size +","+ unionMedInterMedA_size + "," + unionMedInterMedB_size +  "," 
						+ unionMedInterA_size + "," + unionMedInterB_size + "\n");
			}
			bw_csv.close();			
		}
		catch (IOException e)
		{
			System.out.println(e.getMessage());
		}
		
	}	
	
	private static int fact(int n)
	{
		int fact =1;
		for (int i = 2; i <= n ; i++) {
			fact*=i;			
		}
		
		return fact;
	}
	
	private static int KendallTauDistSet(ArrayList<Integer> permutation, ArrayList<ArrayList<Integer>> permutationSet) {
		int distance = 0;
		for (ArrayList<Integer> perm : permutationSet) {
			distance += KendallTauDist(permutation, perm);
		}

		return distance;
	}

	private static int KendallTauDist(ArrayList<Integer> permutationA, ArrayList<Integer> permutationB) {
		if (permutationA.size() != permutationB.size()) {
			return -1;
		}

		int size = permutationA.size();

		int counter = 0;
		int[] inversePermutationA = new int[size];
		int[] inversePermutationB = new int[size];

		for (int i = 0; i < size; i++) {
			inversePermutationA[permutationA.get(i) - 1] = i;
			inversePermutationB[permutationB.get(i) - 1] = i;
		}

		for (int i = 0; i < size - 1; i++) {
			for (int j = i + 1; j < size; j++) {
				if ((inversePermutationA[i] < inversePermutationA[j])
						&& (inversePermutationB[i] > inversePermutationB[j]))
					counter++;
				if ((inversePermutationA[i] > inversePermutationA[j])
						&& (inversePermutationB[i] < inversePermutationB[j]))
					counter++;
			}
		}

		return counter;
	}

	private static int countCommonElements(ArrayList<ArrayList<Integer>> setA, ArrayList<ArrayList<Integer>> setB)
	{
		int counter = 0;
		
		for (ArrayList<Integer> elem : setA) {
			if(setB.contains(elem))
				counter++;
		}
		
		return counter;
	}
	
	private static void printResultsSubsets(int permSize)
	{
		int permutationSize = permSize;
		
		File file = new File("Results");
		
		if(!file.exists())
			file.mkdir();		
		String filePath = "Results/ensemble_de_4_permutations_" + permutationSize + ".csv";
		file = new File(filePath);
		
		
		PermutationGenerator pg = new PermutationGenerator("");
		MedianFinderBT medFinder;
		
		ArrayList<ArrayList<ArrayList<Integer>>> testCases;
		
		ArrayList<ArrayList<Integer>> results;
				
		try
		{
			FileWriter fw = new FileWriter(file);
			BufferedWriter bw = new BufferedWriter(fw);
			testCases = pg.generateSubsets(4, permutationSize);
			bw.write("Distance,Median set size,Permutation size\n");
			
			for (ArrayList<ArrayList<Integer>> permSet : testCases) {
				medFinder = new MedianFinderBT(permSet);
				
				results = medFinder.FindMed();
				
				/*if(results.size()<3)
					continue;*/
				
				bw.write(permSet + ";" + medFinder.getKendallTauDistance() + "," +results.size() + "," + permutationSize + "\n");
				
				//bw.write(permSet+ ";" +results + ";D_kt=" + medFinder.getKendallTauDistance() + ";#=" + results.size() + "\n");
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
	
	private static ArrayList<Integer> reversePermutation(ArrayList<Integer> permutation)
	{
		ArrayList<Integer> reverse = new ArrayList<Integer>(permutation.size());
		
		for (Integer elem : permutation) {
			
			reverse.add(0, elem);
		}
		
		return reverse;
	}
	
	private static ArrayList<ArrayList<Integer>> reversePermutationSet(ArrayList<ArrayList<Integer>> set)
	{
		ArrayList<ArrayList<Integer>> reverse = new ArrayList<ArrayList<Integer>>(set.size());
		
		for (ArrayList<Integer> perm : set) {
			reverse.add(reversePermutation(perm));
		}
		
		return reverse;
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
	
	private static void convergenceTest(ArrayList<ArrayList<Integer>> permSet) throws IOException
	{
		
		String filePath = "Results/ConvergenceTest(1).txt";
		
		File file = new File(filePath);
				
		/*if(file.exists()){
			file.delete();
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}*/
		
		MedianFinderBT medFinder = new MedianFinderBT(permSet);
		int counter = 0;
		ArrayList<ArrayList<Integer>> medianSet = new ArrayList<ArrayList<Integer>>();
		medianSet = permSet; //medFinder.FindMed();
		
		int permuationSize = permSet.get(0).size();		
		int midPoint = fact(permuationSize)/2;
		
		FileWriter fw = new FileWriter(file, true);
		BufferedWriter bw = new BufferedWriter(fw);
		
		bw.write("Starting point: " + medianSet + "\n\n");
		
		while( counter<=5  /*(medianSet.size()>1 && medianSet.size()<=midPoint)|| (counter <100 && medianSet.size()<=midPoint)*/)
		{
			medFinder = new MedianFinderBT(medianSet);
			medianSet = medFinder.FindMed();
			counter++;
			bw.write("After " + counter + " Iterations we have: " + medianSet + "\n\n");
			
			if(medianSet.size()==1)
				break;
		}		
		
		bw.close();
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
	private static boolean test(ArrayList<ArrayList<ArrayList<Integer>>> testCases)
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
				{
					correct = false;
					break;
				}
			}
			
			if(correct){
				//System.out.println("OK!");
				index++;
				continue;
			}
			else if(!correct){
				//System.out.println("INCORRECT");
				allCorrect = false;
				break;
			}
	
			//System.out.println("For the set: " + permutationSet);		
			//System.out.println("Kendall tau distance = " + distance);
	
			/*for(ArrayList<Integer> sol : solutions)
			{
				System.out.println(Arrays.deepToString(sol.toArray()));
			}*/
	
			System.out.println("done with backtrack!\n");			
			//index++;
		}
		
		/*if(allCorrect)
			System.out.println("All cases are correct");
		else if(!allCorrect)
			System.out.println("One or more incorrect cases");*/
		
		return allCorrect;
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
