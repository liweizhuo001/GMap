package SPT;

/*
 * This program is Java Object for TrieTree.
 * It includes three fields key|level|child.
 */
import java.util.*;

public class TrieTree
{
	// public constructor
	public TrieTree()
	{
	}

	public TrieTree(Vector<String> terms)
	{
		LoadTrieTree(terms);
	}

    // public methods
	public TrieNode GetRoot()
	{
		return root_;
	}

	public String ToString()
	{
		return ToString(root_, new String());
	}

	// tokenize words froma terms by space and tab
	public static Vector<String> TokenizeWordsFromTerm(String term)
	{
		Vector<String> words = new Vector<String>();

		if(term != null)
		{
			StringTokenizer buf = new StringTokenizer(term, " \t");
			while(buf.hasMoreTokens() == true)
			{
				String word = buf.nextToken();
				words.addElement(word);
			}
		}

		return words;
	}

	// private methods
	private String ToString(TrieNode node, String inStr)
	{
		String outStr = inStr;

		// indent str
		String indStr = new String();
		for(int i = 0; i < node.GetLevel(); i++)
		{
			indStr += "--";
		}
		indStr += "-> ";

		// cur node
		outStr += indStr + node.ToString() + LS_STR;

		// child node
		Vector<TrieNode> childs = node.GetChilds();
		for(int i = 0; i < childs.size(); i++)
		{
			TrieNode child = childs.elementAt(i);
			outStr = ToString(child, outStr);
		}

		return outStr;
	}

	// load the TrieTree afrom a Vecor<String>
	private void LoadTrieTree(Vector<String> terms)
	{
		int curLevel = 0;
		TrieNode curNode = null;

		// go through all terms
		for(int i = 0; i < terms.size(); i++)
		{
			// all terms are added to the tree from the root
			curNode = root_;

			// tokenize term into words
			String term = terms.elementAt(i);
			Vector<String> words = TokenizeWordsFromTerm(term);

			// add all words into tree
			int j = 0;
			for(j = 0; j < words.size(); j++)
			{
				String word = words.elementAt(j);
				curNode = AddNodeToTrieTree(word, curNode, curLevel+j+1);
			}

			// Add the end node String, "$END" to end the term
			AddNodeToTrieTree(TrieNode.END_NODE_STR, curNode, curLevel+j+1);
		}
	}

	// Add a new node to the tree
	private TrieNode AddNodeToTrieTree(String word, TrieNode curNode, int level)
	{
		// construct the child node
		TrieNode curWordNode = new TrieNode(word, level);

		Vector<TrieNode> childs = curNode.GetChilds();
		// add child to childs
		if(childs.contains(curWordNode) == true)	// child node exist
		{
			curNode = childs.elementAt(childs.indexOf(curWordNode));
		}
		else	// child node does not exist
		{
			childs.addElement(curWordNode);
			curNode = curWordNode;
		}

		return curNode;
	}

	// test driver
    public static void main(String[] args)
    {
        try
        {
            // Parse the inputs, default is infl.data
            String inFile = "/export/home/lu/Development/LVG/Components/Spt/data/test.data";

            if(args.length == 1) 
			{
                inFile = args[0];
			}
            else if(args.length != 0)
            {
          		System.err.println("Usage: java TrieTree <inFileName>");
				System.exit(0);
            }

            // Create synonyms object from a file
			Synonyms synonyms = new Synonyms(inFile);
			Vector<String> terms 
				= new Vector<String>(synonyms.GetSynonymIndex().keySet());

			// print all synonyms
			System.out.println("------- Symonyms --------------");
			for(int i = 0; i < terms.size(); i++)
			{
				System.out.println(i + "." + terms.elementAt(i));
			}

			// instantiate trie tree
			TrieTree trieTree = new TrieTree(terms);

			// print trie tree
			System.out.println("------- Trie Tree of Synonyms words ---------");
			System.out.print(trieTree.ToString());
        }
        catch (Exception e)
        {
            System.err.println(e);
        }
	}

    // data member
	public final static String LS_STR 
		= System.getProperty("line.separator").toString();
	public final static String SPACE_STR = " ";
	// root node of the trie tree
	private TrieNode root_ = new TrieNode(TrieNode.ROOT_NODE_STR, 0);  
}
