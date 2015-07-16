package SPT;

/*
 * This program is Java Object for node in retrieve (Trie) tree.
 * It includes three fields key|level|childs.
 */
import java.util.*;

public class TrieNode
{
	// public constructor
	public TrieNode()
	{
	}

	public TrieNode(String key)
	{
		key_ = key;
	}

	public TrieNode(String key, int level)
	{
		key_ = key;
		level_ = level;
	}

	public TrieNode(String key, int level, Vector<TrieNode> childs)
	{
		key_ = key;
		level_ = level;
		childs_ = childs;
	}

    // public methods
	public void SetChilds(Vector<TrieNode> childs)
	{
		childs_ = childs;
	}

	public String GetKey()
	{
		return key_;
	}

	public int GetLevel()
	{
		return level_;
	}

	public Vector<TrieNode> GetChilds()
	{
		return childs_;
	}

	public String ToString()
	{
		String outStr 
			= "[" + key_ + ", " + level_ + ", " + childs_.size() + "]";
		return outStr;
	}

	// override equals method for contains to use
	public boolean equals(Object obj)
	{
		 String objKey = ((TrieNode)obj).GetKey();
		 return key_.equals(objKey);
	}

    // data member
	public final static String ROOT_NODE_STR = "$_ROOT";
	public final static String END_NODE_STR = "$_END";

	private String key_ = ROOT_NODE_STR;	// the beginning of trie tree
	private int level_ = -1;	//level in the trie tree, root is 0
	private Vector<TrieNode> childs_ = new Vector<TrieNode>();	// children 
}
