package SPT;

/*
 * This program is Java class to 
 * find the permutations of synoyms of all terms from above 
 */
import java.util.*;

public class Permutation
{
	// private constructor so no one can access
	private Permutation()
	{
	}

    // public methods
	// The input is a Vector of Vector,
	// each element is a Vector with the first element is a term
	//and the rest are the synonyms (equivalent)
	public static Vector<String> GetPermutation(Vector<Vector<String>> inList)
	{
		Vector<String> permutationStrs = new Vector<String>();

		// go through all elements (terms) of the inList
		for(int i = 0; i < inList.size(); i++)
		{
			int index = inList.size()-1-i;    // add the end first, look nicer
			Vector<String> curList = inList.elementAt(index);  // last Vector
            int curSize = curList.size();
            if(i == 0)
            {
				// if first time word, copy curList to out
                permutationStrs.addAll(curList); 
            }
			else        // not the first time word
            {
                // copy current set to itself for j-1 times.
                Vector<String> temp = new Vector<String>(permutationStrs);
                int tempSize = temp.size();
                for(int j = 1; j < curSize; j++)
                {
                    permutationStrs.addAll(temp);
                }
                // update elements by adding new String
                for(int j = 0; j < permutationStrs.size(); j++)
                {
                    String curStr = null;
                    if(curSize > 0)
                    {
                        // add word from curList
                        curStr = curList.elementAt(j/tempSize);
                    }
					StringBuffer buffer = new StringBuffer();
                    buffer.append(curStr);
                    buffer.append(TrieTreeMatch.SPACE_STR);
                    buffer.append(permutationStrs.elementAt(j));
                    String newStr = buffer.toString();
                    permutationStrs.setElementAt(newStr, j);
                }
            }
        }

		return permutationStrs;
	}

	// private methods

    // data member
}
