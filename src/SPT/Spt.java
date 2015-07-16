package SPT;

/*
 * This program is main Java class for Synonyms Generation Tool 
 */
import java.io.*;
import java.util.*;

public class Spt
{
	// private constructor
	public Spt()
	{
	}

    // public methods
	public static HashSet<String> GetSynonymPermutationRecursive(String inFile,
		String inTerm, boolean recursiveFlag, boolean debugFlag, int rLevel)
	{
		Vector<String> synonymPermutations = new Vector<String>();
		Vector<String> inTerms = new Vector<String>();
		inTerms.addElement(inTerm);
		//System.out.println( inTerms.size());
		// go through each recursive level
		for(int i = 0; i < rLevel; i++)
		{
			// go through each inTerms
			for(int j = 0; j < inTerms.size(); j++)
			{
				String tempInTerm = inTerms.elementAt(j);
				String parts[]=tempInTerm.split(" ");
				Vector<String> tempResults=new Vector<String>();
				if(parts.length>=3)
				{
					String term="";
					String leftTerm="";
					for(int k=0;k<parts.length-2;k++)
					{
						leftTerm=leftTerm+parts[k]+" ";
					}
					for(int k=parts.length-2;k<parts.length;k++)
					{
						term=term+parts[k]+" ";
					}
					term=term.trim();
					//String term=tempInTerm.replace(parts[0]+" ","");
					Vector<String> termResults = GetSynonymPermutation(inFile, term, recursiveFlag, debugFlag);
					for(int t=0;t<termResults.size();t++)
					{
						tempResults.add(leftTerm+termResults.get(t));
					}			
				}
				else
					tempResults = GetSynonymPermutation(inFile, tempInTerm, recursiveFlag, debugFlag);
				AddToSynonymResults(tempResults, synonymPermutations);	
			}
			// reset the input terms
			inTerms = new Vector<String>(synonymPermutations);
		}	
		HashSet<String>set =transformToHashSet(synonymPermutations);
		return set;
	}
	
	public static HashSet<String> transformToHashSet(Vector<String> vec)
	{
		HashSet<String> set=new HashSet<String>();
		for(int i = 0; i < vec.size(); i++)
		{
			set.add(vec.elementAt(i));
			//System.out.println(vec.elementAt(i));
		}
		return set;
	}

	public static Vector<String> GetSynonymPermutation(String inFile, 
		String inTerm, boolean recursiveFlag, boolean debugFlag)
	{
		// 1. Create synonyms object from a file
		SynonymsMapping synonymsMapping = new SynonymsMapping(inFile);
		Vector<String> terms = new Vector<String>(synonymsMapping.GetSynonymIndex().keySet());

		// 2. instantiate trie tree
		TrieTreeMatch trieTreeMatch = new TrieTreeMatch(terms);

		// 3. find pattern permutation
		Vector<Pattern> patterns = PatternPermutation.FindPatternPermutation(
			inTerm, synonymsMapping, trieTreeMatch, recursiveFlag, debugFlag);

		// 4. get synonyms permutation from paterns	
		Vector<String> synonymsPermutations = GetSynonymsPermutation(patterns);

		return synonymsPermutations;	
	}

	// private method
	private static void AddToSynonymResults(Vector<String> tempResults,
		Vector<String> accumulateResults)
	{
		for(int i = 0; i < tempResults.size(); i++)
		{
			String tempResult = tempResults.elementAt(i);
			if(accumulateResults.contains(tempResult) == false)
			{
				accumulateResults.addElement(tempResult);
			}
		}
	}

	// convert patterns to termsSynonyms
	// by going through each pattern and get permutation,
	// then add each element in patterns to termsSynonyms
	private static Vector<String> GetSynonymsPermutation(
		Vector<Pattern> patterns) 
	{
		Vector<String> synonymsPermutations = new Vector<String>();
		for(int i = 0; i < patterns.size(); i++)
		{
			// get all synonyms permutation from a pattern	
			Vector<Vector<String>> tempTermsSynonyms = patterns.elementAt(i).GetTermsSynonyms();
			Vector<String> tempSynonymsPermutations= Permutation.GetPermutation(tempTermsSynonyms);

			// add new SynonymsPermutation to synonymsPermutations (results)
			for(int j = 0; j < tempSynonymsPermutations.size(); j++)
			{
				String tempSynonymsPermutation= tempSynonymsPermutations.elementAt(j);
				if(synonymsPermutations.contains(tempSynonymsPermutation) == false)
				{
					synonymsPermutations.addElement(tempSynonymsPermutation);
				}
			}
		}

		return synonymsPermutations;
	}

	// test driver
    public static void main(String[] args)
    {
        try
        {
            // Parse the inputs, default is infl.data
			boolean recursiveFlag = true;
			boolean debugFlag = false;
			int rLevel = 1;
           // String inFile = "/export/home/lu/Development/LVG/Components/Applications/SynonymPermutationTool/data/Spt/test.data";
            String inFile = "dic/normTermSynonyms.data.2015";
            
            
            if(args.length > 3) 
			{
          		System.err.println("Usage: java Spt <-d> <-f:inFile> <-r> <rl:int>");
				System.exit(0);
			}

			// set teh inFile and flags from cmd arguments
			for(int i = 0; i < args.length; i++)
			{
				if(args[i].equals("-d"))
				{
					debugFlag = true;
				}
				else if(args[i].startsWith("-f:"))
				{
					inFile = args[i].substring(3);
				}
				else if(args[i].equals("-r"))
				{
					recursiveFlag = true;
				}
				else if(args[i].startsWith("-rl:"))
				{
					rLevel = Integer.parseInt(args[i].substring(4));
				}
				else
				{
					System.err.println("Usage: java Spt <-d> <-f:inFile> <-r> <-rs:level>");
					break;
				}
			}

			// print out options
			System.out.println("====== Synonym Permutation Generation ======");
			System.out.println("- debug flag: " + debugFlag);
			System.out.println("- synonyms file: " + inFile);
			System.out.println("- recursive flag: " + recursiveFlag);
			System.out.println("- recursive level: " + rLevel);
			System.out.println("============================================");

			// interactive interface: get synonyms permutations for the input
			// This is how we set things up to read lines of text from the user
			BufferedReader in =
				new BufferedReader(new InputStreamReader(System.in));
			for(;;)
			{
				// Display a prompt to the user
				System.out.print("Please input a term (Quit = q) > ");
				
				// Read a line from the input
				String line = in.readLine();

				// Quit when reach the end-of-file, or if the user types "quit"
				if ((line == null)
				|| line.equals("q")
				|| line.equals("quit"))
				{
					break;
				}

				// get synonyms permutations
				String inTerm = line;
			/*	Vector<String> synonymsPermutations 	= GetSynonymPermutationRecursive(inFile, inTerm, recursiveFlag, debugFlag, rLevel);

				// print out results	
				System.out.println("----- Synonyms permutation results ------");
				for(int i = 0; i < synonymsPermutations.size(); i++)
				{
					System.out.println(synonymsPermutations.elementAt(i));
				}*/
				HashSet<String> synonymsPermutations 	= GetSynonymPermutationRecursive(inFile, inTerm, recursiveFlag, debugFlag, rLevel);

				// print out results	
				System.out.println("----- Synonyms permutation results ------");
				for(String a:synonymsPermutations)
				{
					System.out.println(a);
				}
				System.out.println(inTerm+"'s  set of synonyms is "+synonymsPermutations.size());
			}
        }
        catch (Exception e)
        {
            System.err.println(e);
        }
	}

    // data member
}
