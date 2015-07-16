package SPT;

/*
 * This program is Java Object for mapped synonym.
 * It includes three field word|synonym|depth.
 * where: 
 * - depth > 0 
 * - depth is 1 if it is not recursive.
 */

import java.io.*;
import java.util.*;

public class MappedSynonym
{
	// public constructor
	public MappedSynonym()
	{
	}

	public MappedSynonym(String synonym)
	{
		this(synonym, new String(), 0);
	}

	public MappedSynonym(String synonym, String history)
	{
		this(synonym, history, 1);
	}

	public MappedSynonym(String synonym, String history, int depth)
	{
		synonym_ = synonym;
		history_ = history;
		depth_ = depth;
	}

    // public methods
	public void SetSynonym(String synonym)
	{
		synonym_ = synonym;
	}

	public void SetHistory(String history)
	{
		history_ = history;
	}

	public void AppendHistory(String history)
	{
		history_ += history;
	}

	public void SetDepth(int depth)
	{
		depth_ = depth;
	}

	public String GetSynonym()
	{
		return synonym_;
	}

	public String GetHistory()
	{
		return history_;
	}

	public int GetDepth()
	{
		return depth_;
	}

	public String ToString()
	{
		return ToString(false);
	}

	public String ToString(boolean detailFlag)
	{
		String outStr = synonym_;
		
		if(detailFlag == true)
		{
			outStr += Synonyms.SP + depth_ + Synonyms.SP + history_;
		}

		return outStr;
	}

	// override equals method for contains to use
	public boolean equals(Object obj)
	{
		 String objSynonym = ((MappedSynonym)obj).GetSynonym();
		 return synonym_.equals(objSynonym);
	}

    // data member
	private String synonym_ = null;	// output mapped synonym
	private String history_ = new String();	// recursive history 
	private int depth_ = SynonymsMapping.DEPTH_NO_LIMIT;// depth of the rSynonym
}
