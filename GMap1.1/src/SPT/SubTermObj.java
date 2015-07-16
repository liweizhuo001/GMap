package SPT;

/*
 * This program is Java Object for Sub Term.
 * It includes three fields Term|Start index|End index.
 */

public class SubTermObj
{
	// public constructor
	public SubTermObj()
	{
	}

	public SubTermObj(String term, int startIndex, int endIndex)
	{
		term_ = term;
		startIndex_ = startIndex;
		endIndex_ = endIndex;
	}

    // public methods
	public void SetTerm(String term)
	{
		term_ = term;
	}

	public void SetStartIndex(int startIndex)
	{
		startIndex_ = startIndex;
	}

	public void SetEndIndex(int endIndex)
	{
		endIndex_ = endIndex;
	}

	public String GetTerm()
	{
		return term_;
	}

	public int GetStartIndex()
	{
		return startIndex_;
	}

	public int GetEndIndex()
	{
		return endIndex_;
	}

	public String ToString()
	{
		String outStr 
			= term_ + Synonyms.SP + startIndex_ + Synonyms.SP + endIndex_;
		return outStr;	
	}

    // data member
	private String term_ = null;	// input term 
	private int startIndex_ = -1;	// start index
	private int endIndex_ = -1;	// end index
}
