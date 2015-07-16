package SPT;

/*
 * This program is Java Object for synonym.
 * It includes two fields word|synonym.
 */

import java.io.*;
import java.util.*;

public class Synonym
{
	// public constructor
	public Synonym()
	{
	}

	public Synonym(String word, String synonym)
	{
		word_ = word;
		synonym_ = synonym;
	}

    // public methods
	public void SetWord(String word)
	{
		word_ = word;
	}

	public void SetSynonym(String synonym)
	{
		synonym_ = synonym;
	}

	public String GetWord()
	{
		return word_;
	}

	public String GetSynonym()
	{
		return synonym_;
	}

    // data member
	private String word_ = null;	// input word 
	private String synonym_ = null;	// output mapped synonym
}
