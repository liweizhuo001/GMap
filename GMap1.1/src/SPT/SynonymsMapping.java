package SPT;

/*
 * This program is used to:
 * 1. find simple synonms 
 * 2. find recursive synonms 
 * 3. find recursive synonms with detail information
 */

import java.io.*;
import java.util.*;

public class SynonymsMapping extends Synonyms
{
	// publci constructor
	public SynonymsMapping()
	{
		super();
	}

	public SynonymsMapping(String inFile)
	{
		super(inFile);
	}

    // public methods
	public Vector<String> FindSynonymStrs(String inWord)
	{
		Vector<MappedSynonym> mappedSynonyms = FindSynonyms(inWord);
		Vector<String> synonyms = new Vector<String>();

		for(int i = 0; i < mappedSynonyms.size(); i++)
		{
			synonyms.addElement(mappedSynonyms.elementAt(i).GetSynonym());
		}

		return synonyms;
	}

	// find synonyms of the inWord
	public Vector<MappedSynonym> FindSynonyms(String inWord)
	{
		String inWordLc = inWord.toLowerCase();
		Vector<MappedSynonym> synonyms = new Vector<MappedSynonym>();

		// Find the indexes
		if(synonymIndex_.containsKey(inWordLc) == true)
		{
			Vector<Integer> indexes = synonymIndex_.get(inWordLc);
			for(int i = 0; i < indexes.size(); i++)
			{
				int index = indexes.elementAt(i).intValue();
				String synonym = synonymList_.elementAt(index).GetSynonym();
				MappedSynonym mappedSynonym 
					= new MappedSynonym(synonym, inWordLc);
				synonyms.addElement(mappedSynonym);
			}
		}

		return synonyms;
	}

	// find recursive synonyms by specifying inWord
	public Vector<String> FindRecursiveSynonymStrs(String inWord)
	{
		Vector<MappedSynonym> mappedSynonyms = FindRecursiveSynonyms(inWord);
		Vector<String> synonyms = new Vector<String>();

		for(int i = 0; i < mappedSynonyms.size(); i++)
		{
			synonyms.addElement(mappedSynonyms.elementAt(i).GetSynonym());
		}

		return synonyms;
	}

	// find recursive synonyms by specifying inWord
	public Vector<MappedSynonym> FindRecursiveSynonyms(String inWord)
	{
		return FindRecursiveSynonyms(inWord, DEPTH_NO_LIMIT);
	}

	// find recursive synonyms by specifying inWord and depth
	public Vector<MappedSynonym> FindRecursiveSynonyms(String inWord, 
		int maxDepth)
	{
		// Reset the Recursive Synonyms
		Vector<MappedSynonym> rSynonyms = new Vector<MappedSynonym>();
		String inWordLc = inWord.toLowerCase(); 
		MappedSynonym inSynoym = new MappedSynonym(inWordLc, inWordLc, 0);
		rSynonyms.addElement(inSynoym);

		// find the recursive mapped synonyms
		rSynonyms = FindRecursiveSynonyms(inSynoym, rSynonyms, inWordLc,
			0, maxDepth);

		// revove the first item, which is the original input
		rSynonyms.removeElementAt(0);

		return rSynonyms;
	}

	public void PrintSynonyms(Vector<MappedSynonym> synonyms)
	{
		PrintSynonyms(synonyms, false);
	}

	public void PrintSynonyms(Vector<MappedSynonym> synonyms, boolean detailFlag)
	{
		for(int i = 0; i < synonyms.size(); i++)
		{
			System.out.println(i + ": " 
				+ synonyms.elementAt(i).ToString(detailFlag));
		}
	}

    // private methods
	private Vector<MappedSynonym> FindRecursiveSynonyms(MappedSynonym inSynonym,
		Vector<MappedSynonym> rSynonyms, String rHistory, int rDepth, 
		int maxDepth)
	{
		String inWordLc = inSynonym.GetSynonym().toLowerCase();
		// update depth and history
		if(rDepth > 0)
		{
			rHistory += " -> " + inWordLc;
		}
		rDepth++;

		// Find the synonyms of the input
		Vector<MappedSynonym> tempSynonyms = FindSynonyms(inWordLc);

		// check cur Depth: if depth exceed max depth or not limit
		if((maxDepth == DEPTH_NO_LIMIT)
		|| (rDepth <= maxDepth))
		{
			// go through all synonyms
			for(int i = 0; i < tempSynonyms.size(); i++)
			{
				MappedSynonym tempSynonym = tempSynonyms.elementAt(i);

				// if the synonym does not exist, add into Vector, and recursive
				if(rSynonyms.contains(tempSynonym) == false)
				{
					tempSynonym.SetDepth(rDepth);
					tempSynonym.SetHistory(rHistory);
					rSynonyms.addElement(tempSynonym);
					rSynonyms = FindRecursiveSynonyms(tempSynonym, rSynonyms, 
						rHistory, rDepth, maxDepth);
				}
			}
		}

		return rSynonyms;
	}

	// test driver
    public static void main(String[] args)
    {
        try
        {
            // Parse the inputs, default is infl.data
			String inWord = "Dog";
            String inFile = "/export/home/lu/Development/LVG/Components/Spt/data/test.data";

            if(args.length == 2) 
			{
                inWord = args[0];
                inFile = args[1];
			}
            else if(args.length == 1)
			{
                inWord = args[0];
			}
            else if(args.length != 0)
            {
          		System.err.println("Usage: java SynonymMapping <inWord> <inFileName>");
				System.exit(0);
            }

            // Create synonyms object from a file
			SynonymsMapping synonymsMapping = new SynonymsMapping(inFile);

			// find  & print synonyms
			Vector<MappedSynonym> synonyms = synonymsMapping.FindSynonyms(inWord);
			System.out.println("----- synonyms of [" + inWord + "]:");
			synonymsMapping.PrintSynonyms(synonyms, true);

			// find and print out resursive synonyms
			System.out.println("---------------------");
			synonyms = synonymsMapping.FindRecursiveSynonyms(inWord);
			System.out.println("-- recursive synonyms of [" + inWord + "]:");
			synonymsMapping.PrintSynonyms(synonyms, true);

			// find and print out resursive synonyms
			System.out.println("---------------------");
			synonyms = synonymsMapping.FindRecursiveSynonyms(inWord, 2);
			System.out.println("-- recursive synonyms of [" + inWord + ", 2]:");
			synonymsMapping.PrintSynonyms(synonyms, true);

			// find and print out resursive synonyms
			System.out.println("---------------------");
			inWord = "K9";
			synonyms = synonymsMapping.FindRecursiveSynonyms(inWord, 2);
			System.out.println("-- recursive synonyms of [" + inWord + ", 2]:");
			synonymsMapping.PrintSynonyms(synonyms, true);
        }
        catch (Exception e)
        {
            System.err.println(e);
        }
	}

    // data member
	public final static int DEPTH_NO_LIMIT = -1; 
}
