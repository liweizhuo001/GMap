package SPT;

/*
 * This program is Java class to 
 * 1. construct a TrieTree.
 * 2. Find is a term is in the Trri Tree (Match)
 * 3. Find all terms of an input term in the Trie Tree (FindMatchTerms)
 */
import java.util.*;

public class TrieTreeMatch extends TrieTree
{
	// public constructor
	public TrieTreeMatch()
	{
		super();
	}

	public TrieTreeMatch(Vector<String> terms)
	{
		super(terms);
	}

    // public methods

	// Find the exact match of the input term, a whole branch of the tree
	// return true if the inTerm match a complete branch of the tree
	public boolean Match(String inTerm)
	{
		// tokenize the in term into words and add "END_NODE_STR" to it
        String newInTerm 
			= inTerm.trim().toLowerCase() + " " + TrieNode.END_NODE_STR;

        // tokenize in term into a list of words
		Vector<String> inWords = TrieTree.TokenizeWordsFromTerm(newInTerm);

		// travels the trie tree to find the term
        boolean findFlag = false;
		TrieNode curNode = GetRoot();
        for(int i = 0; i < inWords.size(); i++)
        {
			// init TrieNode of curWord
            String curWord = inWords.elementAt(i);
			TrieNode curWordNode = new TrieNode(curWord);//node for next match 

			Vector<TrieNode> curChilds = curNode.GetChilds();

			// check if curChilds contains curWordNode
            if(curChilds.contains(curWordNode) == true)
            {
                // check if find the word, at the end of complete word
                if((curWordNode.GetKey().equals(TrieNode.END_NODE_STR) == true)
                && (i == inWords.size() - 1))
                {
                    findFlag = true;	// term in the tree
                    break;
                }

                curNode = curChilds.elementAt(curChilds.indexOf(curWordNode));
            }
            else    // no match for the current word => term is not in the tree
            {
                break;
            }
        }

		return findFlag;
	}

	// Find all matched terms (a whole branch of the tree) in the input term
	// Returns all possible matched terms
	public Vector<String> FindMatchTerms(String inTerm)
	{
		Vector<String> matchTerms = new Vector<String>();

		// find match term objs
		Vector<SubTermObj> matchTermObjs = FindMatchTermObjs(inTerm);
		for(int i = 0; i < matchTermObjs.size(); i++)
		{
			String matchTerm = matchTermObjs.elementAt(i).GetTerm(); 
			matchTerms.addElement(matchTerm);
		}

		return matchTerms;
	}

	public Vector<SubTermObj> FindMatchTermObjs(String inTerm)
	{
		Vector<SubTermObj> matchTerms = new Vector<SubTermObj>();

		// tokenize the in term into words
        String newInTerm = inTerm.trim().toLowerCase(); 

        // tokenize in term into a list of words
		Vector<String> inWords = TrieTree.TokenizeWordsFromTerm(newInTerm);

		// go through each words of inWords and find all matched term
		for(int i = 0; i < inWords.size(); i++)
		{
			// curTerm is the sub term from current index (i) to the end
			String curTerm = GetSubTermFrom(inWords, i);
			Vector<SubTermObj> branchMatches = FindBranchMatches(curTerm, i);

			if((branchMatches != null)
			&& (branchMatches.size() > 0))
			{
				matchTerms.addAll(branchMatches);
			}
		}

		return matchTerms;
	}

	// get a partial term from a inWords, start from 0 to endIndex
	public static String GetSubTermTo(Vector<String> inWords, int endIndex)
	{
		return GetSubTerm(inWords, 0, endIndex);
	}

	// get a partial term from a inWords, start from startIndex to the end
	public static String GetSubTermFrom(Vector<String> inWords, int startIndex)
	{
		return GetSubTerm(inWords, startIndex, inWords.size());
	}

	// get a partial term from a in words from start index to end index
	public static String GetSubTerm(Vector<String> inWords, int startIndex,
		int endIndex)
	{
		String outStr = new String();
		for(int i = startIndex; i < endIndex; i++)
		{
			outStr += inWords.elementAt(i) + SPACE_STR;
		}

		return outStr.trim();
	}

	// check if childs contains END_NODE
	public static boolean HasEndNode(Vector<TrieNode> childs)
	{
		boolean endBranchFlag = false;
		TrieNode endNode = new TrieNode(TrieNode.END_NODE_STR);
		
		return childs.contains(endNode);
	}

	// private methods
	// find the whole matched branches and return them as Vector<Str> 
	private Vector<SubTermObj> FindBranchMatches(String inTerm, int startIndex)
	{
		Vector<SubTermObj> branchMatches = new Vector<SubTermObj>();
		// tokenize the in term into words and add END_NODE_STR
        String newInTerm = inTerm.trim().toLowerCase() + SPACE_STR
			+ TrieNode.END_NODE_STR;

        // tokenize in term into a list of words
		Vector<String> inWords = TrieTree.TokenizeWordsFromTerm(newInTerm);

		// travels the trie tree to find all branch matched terms
		TrieNode curNode = GetRoot();
        for(int i = 0; i < inWords.size(); i++)
        {
			// init TrieNode of curWord
            String curWord = inWords.elementAt(i);
			TrieNode curWordNode = new TrieNode(curWord);

			Vector<TrieNode> curChilds = curNode.GetChilds();

			// find branch match, add branch to found matches
			if(HasEndNode(curChilds) == true)
			{
				// term in the tree
				String tempTerm = GetSubTermTo(inWords, i);
				int endIndex = startIndex + i;
				SubTermObj subTermObj 
					= new SubTermObj(tempTerm, startIndex, endIndex);
				branchMatches.addElement(subTermObj);
			}

			// check if curChilds contains curWordNode
            if(curChilds.contains(curWordNode) == true)
            {
                curNode = curChilds.elementAt(curChilds.indexOf(curWordNode));
            }
            else    // no match for the current word => term is not in the tree
            {
                break;
            }
        }

		return branchMatches;
	}

	// test driver
    public static void main(String[] args)
    {
        try
        {
            // Parse the inputs, default is infl.data
			String inTerm = "Rain dog and cat out";
            String inFile = "/export/home/lu/Development/LVG/Components/Spt/data/test.data";

            if(args.length == 2) 
			{
                inTerm = args[0];
                inFile = args[1];
			}
            else if(args.length == 1)
			{
                inTerm = args[0];
			}
            else if(args.length != 0)
            {
          		System.err.println("Usage: java TrieTreeMatch <inTerm> <inFileName>");
				System.exit(0);
            }

            // Create synonyms object from a file
			Synonyms synonyms = new Synonyms(inFile);
			Vector<String> terms 
				= new Vector<String>(synonyms.GetSynonymIndex().keySet());

			// print all synonyms
			for(int i = 0; i < terms.size(); i++)
			{
				System.out.println(i + "." + terms.elementAt(i));
			}
			System.out.println("---------------------");

			// instantiate trie tree
			TrieTreeMatch trieTreeMatch = new TrieTreeMatch(terms);

			// print trie tree
			System.out.print(trieTreeMatch.ToString());
			System.out.println("--------- Match & Partial Match Tests -------");

			// print find
			System.out.println("===========================================");
			System.out.println("--  Match [" + inTerm + "]: " 
				+ trieTreeMatch.Match(inTerm));
			System.out.println("---  Partial Match: ---");
			Vector<SubTermObj> patialMatches 
				= trieTreeMatch.FindMatchTermObjs(inTerm);
			for(int i = 0; i < patialMatches.size(); i++)
			{
				System.out.println("PM[" + i + "]: " + patialMatches.elementAt(i).ToString());
			}

			inTerm = "Dog and cat";
			System.out.println("===========================================");
			System.out.println("--  Match [" + inTerm + "]: " 
				+ trieTreeMatch.Match(inTerm));
			patialMatches = trieTreeMatch.FindMatchTermObjs(inTerm);
			for(int i = 0; i < patialMatches.size(); i++)
			{
				System.out.println("PM[" + i + "]: " + patialMatches.elementAt(i).ToString());
			}

			inTerm = "Dog and";
			System.out.println("===========================================");
			System.out.println("--  Match [" + inTerm + "]: " 
				+ trieTreeMatch.Match(inTerm));
			patialMatches = trieTreeMatch.FindMatchTermObjs(inTerm);
			for(int i = 0; i < patialMatches.size(); i++)
			{
				System.out.println("PM[" + i + "]: " + patialMatches.elementAt(i).ToString());
			}

			inTerm = "CAT";
			System.out.println("===========================================");
			System.out.println("--  Match [" + inTerm + "]: " 
				+ trieTreeMatch.Match(inTerm));
			patialMatches = trieTreeMatch.FindMatchTermObjs(inTerm);
			for(int i = 0; i < patialMatches.size(); i++)
			{
				System.out.println("PM[" + i + "]: " + patialMatches.elementAt(i).ToString());
			}
        }
        catch (Exception e)
        {
            System.err.println(e);
        }
	}

    // data member
	// root node of the trie tree
}
