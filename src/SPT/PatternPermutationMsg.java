package SPT;

/*
 * This program is Java class to 
 * Print out debug and Err message for pattern permutation
 */
import java.util.*;

public class PatternPermutationMsg
{
	// private constructor
	private PatternPermutationMsg()
	{
	}

    // public methods
	// Print out patterns permutation case I: cur inWord is not a subTerm
	public static void PrintCaseI(int inWordIndex, String inWord,
		Vector<SubTermObj> subTerms, int subTermObjIndex, int startIndex,
		int prevStartIndex, Vector<Pattern> patterns, boolean debugFlag)
	{
		if(debugFlag == true)
		{
			System.out.println("=> Case I: cur inWord is not a subTerm");
			System.out.println(GetDebugMsg(inWordIndex, inWord, subTerms,
				subTermObjIndex, startIndex, prevStartIndex, patterns));
		}
	}


	// Print out patterns permutation case II.1: cur inWord is a subTerm
	// base term of subTermObj is a single word
	public static void PrintCaseII1(int inWordIndex, String inWord,
		Vector<SubTermObj> subTerms, int subTermObjIndex, int startIndex,
		int prevStartIndex, Vector<Pattern> patterns, boolean printFlag)
	{
		if(printFlag == true)
		{
			System.out.println("=> Case II.1: cur inWord is a subTerm, base term of subTerm is a single word");
			System.out.println(GetDebugMsg(inWordIndex, inWord, subTerms,
				subTermObjIndex, startIndex, prevStartIndex, patterns));
		}
	}

	public static void PrintCaseII2(int inWordIndex, String inWord,
		Vector<SubTermObj> subTerms, int subTermObjIndex, int startIndex,
		int prevStartIndex, Vector<Pattern> patterns, boolean printFlag)
	{
		if(printFlag == true)
		{
			System.out.println("=> Case II.2: cur inWord is a subTerm, base term of subTerm is a multi- word");
			System.out.println(GetDebugMsg(inWordIndex, inWord, subTerms,
				subTermObjIndex, startIndex, prevStartIndex, patterns));
		}
	}

	private static String GetDebugMsg(int inWordIndex, String inWord,
		Vector<SubTermObj> subTerms, int subTermObjIndex, int startIndex,
		int prevStartIndex, Vector<Pattern> patterns)
	{
		String debugMsg = new String();
		debugMsg += "----- inWord[" + inWordIndex + "]: " + inWord;
		debugMsg += "\n subTerms.size(): " + subTerms.size();
		debugMsg += "\n subTerm[" + subTermObjIndex + "]: ";
		debugMsg += GetSubTermStr(subTerms, subTermObjIndex);
		debugMsg += "\n startIndex: " + startIndex;
		debugMsg += ", prevStartIndex: " + prevStartIndex;
		debugMsg += "\n patterns:\n" + GetPatternStr(patterns);

		return debugMsg;
	}

	private static String GetSubTermStr(Vector<SubTermObj> subTerms, int index)
	{
		String outStr = "null";

		if((index >= 0)
		&& (index < subTerms.size()))
		{
			outStr = subTerms.elementAt(index).ToString();
		}

		return outStr;
	}

	private static String GetPatternStr(Vector<Pattern> patterns)
	{
		String outStr = new String();

		for(int i = 0; i < patterns.size(); i++)
		{
			outStr += "========== Pattern[" + i + "]: ============\n";
			outStr += patterns.elementAt(i).ToString() + "\n";
		}

		return outStr;
	}

    // data member
}
