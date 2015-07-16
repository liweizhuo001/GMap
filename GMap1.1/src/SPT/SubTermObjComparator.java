package SPT;

import java.util.*;

/*
 * This program is Java class for Sub Term Object comparator.
 * It includes three fields Term|Start index|End index.
 */

public class SubTermObjComparator<T> implements Comparator<T>
{
	// public constructor
	// sort by start indext first, then end index
	public int compare(T o1, T o2)
	{
		int startIndex1 = ((SubTermObj) o1).GetStartIndex();
		int startIndex2 = ((SubTermObj) o2).GetStartIndex();

		if(startIndex1 != startIndex2)
		{
			return (startIndex1 - startIndex2);
		}
		else
		{
			int endIndex1 = ((SubTermObj) o1).GetEndIndex();
			int endIndex2 = ((SubTermObj) o2).GetEndIndex();

			return (endIndex1 - endIndex2);
		}
	}
}
