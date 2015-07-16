package SPT;

/*
 * This program is Java class to 
 * 1. Find all subTerms has synonyms of an input term (FindMatchTerms)
 * 2. Find the pattern permutations of synoyms of all terms from above 
 */
import java.util.*;

public class PatternPermutation
{
	// private constructor
	private PatternPermutation()
	{
	}

    // public methods

	// Find all patterns permutation
	public static Vector<Pattern> FindPatternPermutation(String inTerm, 
		SynonymsMapping synonymsMapping, TrieTreeMatch trieTreeMatch,
		boolean recursiveFlag, boolean debugFlag)
	{
		// I. find all subTerms has synonyms
		Vector<SubTermObj> subTerms = trieTreeMatch.FindMatchTermObjs(inTerm);
		// sort them by start index, then end index
		SubTermObjComparator<SubTermObj> sc 
			= new SubTermObjComparator<SubTermObj>();	
		Collections.sort(subTerms, sc);

		// II. construct the pattern permutation
		Vector<String> inWords 
			= TrieTree.TokenizeWordsFromTerm(inTerm.toLowerCase());

		// patterns, terms, synonyms
		Vector<Pattern> patterns = new Vector<Pattern>();

		int inWordIndex = 0;	// cur index of inWord
		int subTermObjIndex = 0;  // index for terms has synonyms (subTerms)

		// go through each inWord
		while(inWordIndex < inWords.size())
		{
			String inWord = inWords.elementAt(inWordIndex);
			// the start index of current subTerm has synonyms
			int startIndex = GetStartIndex(subTerms, subTermObjIndex);
			// the start Index of previous subTerm has synonyms
			int prevStartIndex 
				= GetPrevStartIndex(subTerms, subTermObjIndex);	
			
			// current inWord is not a subTerm
			if((subTermObjIndex >= subTerms.size()) // after all subTerms
			|| (inWordIndex != startIndex))   // cur inWord is not subTerm 
			{
				// add cur inWord to syonyms and add to all patterns
				AddInWordToPatterns(inWord, inWordIndex, patterns);

				// print out debug message
				PatternPermutationMsg.PrintCaseI(inWordIndex, inWord,
					subTerms, subTermObjIndex, startIndex, prevStartIndex,
					patterns, debugFlag);

				// update inWord index	
				inWordIndex++;
			}
			// current inWord is subTerm (has synonyms)
			else if((subTermObjIndex < subTerms.size())
			&& (inWordIndex == startIndex))
			{
				SubTermObj subTermObj = subTerms.elementAt(subTermObjIndex);

				// get of synonyms of cur subTerm
				Vector<String> synonyms = GetSynonyms(subTermObj,
					synonymsMapping, recursiveFlag);

				// if base term of subTermObj is a single word
				// must be the first subTerm of current inWord
				if(subTermObj.GetEndIndex()-1 == subTermObj.GetStartIndex())
				{
					// add synonyms of subTermObj to all patterns
					AddSynonymsToPatterns(synonyms, patterns, subTermObj);

					// print out debug message
					PatternPermutationMsg.PrintCaseII1(inWordIndex, 
						inWord, subTerms, subTermObjIndex, startIndex, 
						prevStartIndex, patterns, debugFlag);
				}
				else	// base term of subTermObj is multi-words
				{
					// add cur inWord to pattern
					AddInWordToPatterns(inWord, inWordIndex, patterns);

					// get last pattern for all patterns
					Vector<Pattern> lastPatterns = new Vector<Pattern>();
					for(int i = 0; i < patterns.size(); i++)
					{
						Pattern tempPattern = patterns.elementAt(i);
						Pattern lastPattern = tempPattern.GetLastPattern();

						// add to last patterns if 
						// startIndex = the BaseWordCount of last pattern
						// => same position for the subTerm replacement
						// and it does not exist (the same)
						int totalBaseTermsWordCount
							= lastPattern.GetTotalBaseTermsWordCount();
						if((startIndex == totalBaseTermsWordCount)
						&& (lastPatterns.contains(lastPattern) == false))
						{
							lastPatterns.addElement(lastPattern);
						}
					}

					// add synonyms to all last pattern
					for(int i = 0; i < lastPatterns.size(); i++)
					{
						lastPatterns.elementAt(i).AddSynonyms(synonyms);
					}
					
					// add all last atterns to patterns
					patterns.addAll(lastPatterns);

					PatternPermutationMsg.PrintCaseII2(inWordIndex, 
						inWord, subTerms, subTermObjIndex, startIndex, 
						prevStartIndex, patterns, debugFlag);
				}

				// update inWordIndex
				int nextStartIndex 
					= GetNextStartIndex(subTerms, subTermObjIndex);
				if(startIndex != nextStartIndex)
				{
					inWordIndex++;
				}

				// update the subTermObjIndex
				if(subTermObjIndex < subTerms.size())
				{
					subTermObjIndex++;
				}
			}
		}

		return patterns;
	}

	// private methods
	private static void AddInWordToPatterns(String inWord, int inWordIndex,
		Vector<Pattern> patterns)
	{
		Vector<String> synonyms = GetSynonyms(inWord);
		SubTermObj subTermObj = new SubTermObj(inWord, inWordIndex,
			inWordIndex + 1);
		AddSynonymsToPatterns(synonyms, patterns, subTermObj);
	}

	// Get synonyms with current inWord, which has no synonyms
	private static Vector<String> GetSynonyms(String inWord)
	{
		Vector<String> synonyms = new Vector<String>();
		synonyms.addElement(inWord);

		return synonyms;
	}

	// Get synonyms with current subTerm, which has synonyms
	private static Vector<String> GetSynonyms(SubTermObj subTermObj,
		SynonymsMapping synonymsMapping, boolean recursiveFlag)
	{
		// get info of synonyms of cur term
		String term = subTermObj.GetTerm();

		Vector<String> synonyms = new Vector<String>();
		synonyms.addElement(term);	// term with syonyms

		// recursive synonyms mapping
		if(recursiveFlag == true)
		{
			synonyms.addAll(synonymsMapping.FindRecursiveSynonymStrs(term));
		}
		else	// non-recursive sysnonym mapping
		{
			synonyms.addAll(synonymsMapping.FindSynonymStrs(term));
		}

		return synonyms;
	}

	// add snonyms to patterns
	private static void AddSynonymsToPatterns(Vector<String> synonyms,
		Vector<Pattern> patterns, SubTermObj subTermObj)
	{
		// no patterns exist, first word
		if(patterns.size() == 0)
		{
			// initiate a new pattern and add synonyms to patterns
			Pattern tempPattern = new Pattern();
			tempPattern.AddSynonyms(synonyms);

			// add pattern to patterns
			patterns.add(tempPattern);
		}
		else	// add tempTermsSynonyms to each existing termsSynonyms
		{
			// add inWord to patterns
			for(int i = 0; i < patterns.size(); i++)
			{
				// add synonyms if base of last element does not contain subTerm
				// meaning there subTerm is beyond or overlap
				// inWord if it is outside the baseTerms word
				// add if new subTerm is not inside the last subTerm of patterns
				if(patterns.elementAt(i).LastElementContains(subTermObj) 
					== false)
				{
					patterns.elementAt(i).AddSynonyms(synonyms);
				}
			}
		}
	}

	// get teh start index of the subTerm at index
	private static int GetStartIndex(Vector<SubTermObj> subTerms, int index)
	{
		int startIndex = -1; 

		// check input
		if((index >= 0) 
		&& (subTerms != null)
		&& (index < subTerms.size()))
		{
			startIndex = subTerms.elementAt(index).GetStartIndex();
		}

		return startIndex;
	}

	// get teh start index of the subTerm at prev-index (index-1)
	private static int GetPrevStartIndex(Vector<SubTermObj> subTerms, int index)
	{
		int prevIndex = index - 1;
		int prevStartIndex = GetStartIndex(subTerms, prevIndex); 

		return prevStartIndex;
	}

	// get teh start index of the subTerm at next-index (index+1)
	private static int GetNextStartIndex(Vector<SubTermObj> subTerms, int index)
	{
		int nextIndex = index + 1;
		int nextStartIndex = GetStartIndex(subTerms, nextIndex); 

		return nextStartIndex;
	}

    // data member
}
