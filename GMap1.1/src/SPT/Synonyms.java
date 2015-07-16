package SPT;

/*
 * This program is used to load synonyms from a given file:
 * 1. read in synonyms from a file
 * 2. store synonyms in a Vector
 * 3. store synonym indexes in a Hashtable
 *
 * The input format:
 * 0. word|synonym
 * 1. use # for comments
 * 2. Ignore duplication
 * 3. Generate symmetric synonyms automatically
 * 4. lowered case
 */

import java.io.*;
import java.util.*;

public class Synonyms
{
	// public constructor
	public Synonyms()
	{
	}

	public Synonyms(String inFile)
	{
		LoadSynonyms(inFile);
	}

    // public methods
	public Vector<Synonym> GetSynonymList()
	{
		return synonymList_;
	}

	public Hashtable<String, Vector<Integer>> GetSynonymIndex()
	{
		return synonymIndex_;
	}

	public void AppendSynonyms(String inFile)
    {
        String line = null;
        int lineNo = 0;
        int synonymNo = 0;

        try
        {
            BufferedReader fIn = new BufferedReader(new InputStreamReader(
				new FileInputStream(inFile), "UTF-8"));

            // read in line by line from a file
            while((line = fIn.readLine()) != null)
            {
                // skip the line if it is empty or comments (#)
                if((line.length() > 0) && (line.charAt(0) != '#'))
                {
                    // use '|' and as delimiter to parse token
                    StringTokenizer buf = new StringTokenizer(line, "|");

                    // read in data & get citations & inflections
                    String inSynonym = buf.nextToken().toLowerCase();
					String outSynonym = buf.nextToken().toLowerCase();

					AddToSynonymList(inSynonym, outSynonym);

					// print out
					synonymNo++;
				}

				// update lineNo & print out current lineNo
                lineNo++;
             /*   if((lineNo%1000) == 0)
                {
                    System.out.println("-- Cur : " + lineNo + ", Synonyms: "+ synonymNo);
                }*/
            }
            fIn.close();

            System.out.println("-- Read through " + lineNo + " lines and found " + synonymNo + " pairs of synonym.");
        }
        catch(Exception e)
        {
            System.err.println("Exception: " + e.toString());
            System.err.println("Line(" + lineNo + "):" + line);
        }
    }

    // private methods
	// read in synonyms, modify, save, then print out
	private void LoadSynonyms(String inFile)
    {
		// resest synonymList_ and synonymIndex_
		synonymList_ = new Vector<Synonym>();
		synonymIndex_ = new Hashtable<String, Vector<Integer>>();

		// Append synonyms from file
		AppendSynonyms(inFile);
	}

	// print out unique synonym, not include symmetric synonym (even index)
	private void PrintOutUniqueSynonyms()
	{
		for(int i = 0; i < synonymList_.size(); i++)
		{
			// print out even index synonym
			if(i%2 == 0)
			{
				Synonym synonym = synonymList_.elementAt(i);
				String outStr = synonym.GetWord() + SP + synonym.GetSynonym();
				System.out.println(outStr);
			}
		}
	}

	// print out synonym list
	private void PrintOutSynonymList()
	{
		for(int i = 0; i < synonymList_.size(); i++)
		{
			Synonym synonym = synonymList_.elementAt(i);
			String outStr = synonym.GetWord() + SP + synonym.GetSynonym();
			System.out.println(outStr);
		}
	}

	// print out synonym indexes
	private void PrintOutSynonymIndexes()
	{
		Enumeration<String> e = synonymIndex_.keys();
		while(e.hasMoreElements() == true)
		{
			String inWord = e.nextElement();

			Vector<Integer> indexes = synonymIndex_.get(inWord);
			System.out.print(inWord + SP + ": ");
			for(int i = 0; i < indexes.size(); i++)
			{
				int index = indexes.elementAt(i).intValue();
				System.out.print(index + ", ");
			}
			System.out.println("");
		}
	}

	private void AddToSynonymList(String inSynonym, String outSynonym)
	{
		// add to synonym list if inSynonym does not exist
		if(IsSynonymExist(inSynonym, outSynonym) == false)
		{
			// add to synonym list
			Synonym synonym1 = new Synonym(inSynonym, outSynonym);
			Synonym synonym2 = new Synonym(outSynonym, inSynonym);
			synonymList_.addElement(synonym1);
			synonymList_.addElement(synonym2);

			// update synonym index
			int listSize = synonymList_.size();
			int index1 = listSize - 2;
			int index2 = listSize - 1;
			AddSynonymIndex(inSynonym, index1);
			AddSynonymIndex(outSynonym, index2);
		}
	}

	private void AddSynonymIndex(String synonym, int index)
	{
		Integer indexObj = new Integer(index);
		Vector<Integer> indexList = synonymIndex_.get(synonym);
		if(indexList == null) 	// add new index to index list
		{
			Vector<Integer> newIndexList = new Vector<Integer>();
			newIndexList.addElement(indexObj);
			synonymIndex_.put(synonym, newIndexList);
		}
		else	// add index to exist index list
		{
			synonymIndex_.get(synonym).addElement(indexObj);
		}
	}

	private boolean IsSynonymExist(String inSynonym, String outSynonym)
	{
		boolean existFlag = false;

		Vector<Integer> indexList = synonymIndex_.get(inSynonym);
		if(indexList != null) 	// same in synonym
		{
			// go through each indexed out synonym
			for(int i = 0; i < indexList.size(); i++)
			{
				int curIndex = indexList.elementAt(i).intValue();
				String curOutSynonym 
					= synonymList_.elementAt(curIndex).GetSynonym();
				if(outSynonym.equals(curOutSynonym) == true)
				{
					existFlag = true;
					break;
				}
			}
		}

		return existFlag;
	}

	// test driver
    public static void main(String[] args)
    {
        try
        {
            // Parse the inputs, default is infl.data
            String inFile = 
				"/export/home/lu/Development/LVG/Components/Spt/data/test.data";
            String inFile1 = 
				"/export/home/lu/Development/LVG/Components/Spt/data/test1.data";

            if(args.length == 1)
			{
                inFile = args[1];
			}
            else if(args.length != 0)
            {
          		System.err.println("Usage: java Synonyms <inFileName>");
				System.exit(0);
            }

            // test reading synonyms from file
            Synonyms synonyms = new Synonyms(inFile);
			System.out.println("----- Synonym List -----");
        	synonyms.PrintOutSynonymList();
			System.out.println("----- Unique Synonym List -----");
			synonyms.PrintOutUniqueSynonyms();

			System.out.println("----- Synonym Indexes -----");
			synonyms.PrintOutSynonymIndexes();

            // test appending synonyms from file
			synonyms.AppendSynonyms(inFile1);
			System.out.println("----- Synonym List -----");
        	synonyms.PrintOutSynonymList();
			System.out.println("----- Unique Synonym List -----");
			synonyms.PrintOutUniqueSynonyms();

			System.out.println("----- Synonym Indexes -----");
			synonyms.PrintOutSynonymIndexes();
        }
        catch (Exception e)
        {
            System.err.println(e);
        }
	}

    // data member
	public final static String SP = "|";

	protected Vector<Synonym> synonymList_ = new Vector<Synonym>();
	protected Hashtable<String, Vector<Integer>> synonymIndex_
		= new Hashtable<String, Vector<Integer>>();
}
