package SPT;

/*
 * This program is Java Object for pattern: Synonyms of terms.
 */

import java.util.*; 

public class Pattern
{
	// public constructor
	public Pattern()
	{
		termsSynonyms_ = new Vector<Vector<String>>();
	}

	public Pattern(Vector<Vector<String>> termsSynonyms)
	{
		termsSynonyms_ = termsSynonyms;
	}

    // public methods
	// Get the last pattern: current pattern with the last element (subTerm) removed
	public Pattern GetLastPattern()
	{
		Vector<Vector<String>> lastPattern = new Vector<Vector<String>>();

		// check: termSynonyms_.size() must >= 1
		if(termsSynonyms_.size() < 1)
		{
			System.err.println("** ERR@Pattern.GetLastPattern( ): termSynonyms_.size(" + termsSynonyms_.size() + ") < 1 ");
		}

		// add all elements, except for the last element, to lastPattern
		for(int i = 0; i < termsSynonyms_.size()-1; i++)
		{
			Vector<String> synonyms 
				= new Vector<String>(termsSynonyms_.elementAt(i));
			lastPattern.addElement(synonyms);
		}

		Pattern pattern = new Pattern(lastPattern);
		return pattern;
	}

	// add a new syonyms to the objects at term[index],
	// this index is different from endIndex_
	public void AddSynonymAtTerm(String synonym, int index)
	{
		Vector<String> synonyms = termsSynonyms_.elementAt(index);
		synonyms.addElement(synonym);
	}

	// add a new syonym to the end of pattern 
	public void AddSynonyms(String synonym)
	{
		Vector<String> synonyms = new Vector<String>();
		synonyms.addElement(synonym);

		termsSynonyms_.addElement(synonyms);
	}

	// add a new syonyms to the end of pattern
	public void AddSynonyms(Vector<String> synonyms)
	{
		if((synonyms != null)
		&& (synonyms.size() > 0))
		{
			termsSynonyms_.addElement(synonyms);
		}
	}

	public int GetTotalBaseTermsWordCount()
	{
		return GetBaseTermsWordCount(termsSynonyms_.size());
	}

	public int GetBaseTermsWordCount(int size)
	{
		if((size < 0)
		|| (size > termsSynonyms_.size()))
		{
			return -1;
		}

		// count the total word count of base term
		int wordCount = 0;
		for(int i = 0; i < size; i++)
		{
			String baseTerm = termsSynonyms_.elementAt(i).elementAt(0);
			wordCount += GetWordCount(baseTerm);
		}

		return wordCount;
	}


	public void SetTermsSynonyms(Vector<Vector<String>> termsSynonyms)
	{
		termsSynonyms_ = termsSynonyms;
	}

	public Vector<Vector<String>> GetTermsSynonyms()
	{
		return termsSynonyms_;
	}

	// check if the base of last element contains the subTerm
	public boolean LastElementContains(SubTermObj subTermObj)
	{
		boolean containFlag = false;
		
		int lastElementStartIndex 
			= GetBaseTermsWordCount(termsSynonyms_.size()-1);
		int lastElementEndIndex = GetTotalBaseTermsWordCount();
		// inWord if it is outside the baseTerms word
		// add if new subTerm is not inside the last subTerm of patterns
		int subTermObjStartIndex = subTermObj.GetStartIndex();
		int subTermObjEndIndex = subTermObj.GetEndIndex();
		if((IsInside(lastElementStartIndex, lastElementEndIndex, subTermObjStartIndex) == true)
		&& (IsInside(lastElementStartIndex, lastElementEndIndex, subTermObjEndIndex) == true))
		{
			containFlag = true;
		}

		return containFlag;
	}

	private static boolean IsInside(int startIndex, int endIndex, int inIndex)
	{
		boolean flag = false;
		if((inIndex >= startIndex)
		&& (inIndex <= endIndex))
		{
			flag = true;
		}

		return flag;
	}

	public String ToString()
	{
		String outStr = new String();
		for(int i = 0; i < termsSynonyms_.size(); i++)
		{
			Vector<String> synonyms = termsSynonyms_.elementAt(i);
			for(int j = 0; j < synonyms.size(); j++)
			{
				outStr += synonyms.elementAt(j) + Synonyms.SP;
			}
			outStr.trim();
			outStr += TrieTree.LS_STR;
		}
		outStr += "[" + GetTotalBaseTermsWordCount() + "]" + TrieTree.LS_STR;

		return outStr;	
	}

	// override equals method for contains to use
	public boolean equals(Object obj)
	{
		String objString = ((Pattern) obj).ToString();
		return ToString().equals(objString);
	}

	// private method
	private static int GetWordCount(String inTerm)
	{
		int wordCount = 0;
		StringTokenizer buf = new StringTokenizer(inTerm, " \t");
		while(buf.hasMoreTokens() == true)
		{
			buf.nextToken();
			wordCount++;
		}

		return wordCount;
	}

	// test driver
    public static void main(String[] args)
    {
        try
        {
            // Parse the inputs, default is infl.data
            if(args.length != 0)
            {
          		System.err.println("Usage: java TermSynonyms");
				System.exit(0);
            }

            // test reading synonyms from file
            Pattern pattern = new Pattern();

			pattern.AddSynonyms("one");
			pattern.AddSynonyms("two");
			pattern.AddSynonyms("three some");
			pattern.AddSynonyms("four");
			pattern.AddSynonymAtTerm("2", 1);
			pattern.AddSynonymAtTerm("Too and to", 1);
			pattern.AddSynonymAtTerm("4", 3);

			// clone
			Pattern pattern2 = pattern.GetLastPattern();
			pattern2.AddSynonymAtTerm("3", 2);

			System.out.println("----- Pattern: Terms|Synonyms -----");
			System.out.println(pattern.ToString());

			System.out.println("----- Pattern 2: Terms|Synonyms -----");
			System.out.println(pattern2.ToString());
        }
        catch (Exception e)
        {
            System.err.println(e);
        }
	}

    // data member
	private Vector<Vector<String>> termsSynonyms_ 
		= new Vector<Vector<String>>();
}
