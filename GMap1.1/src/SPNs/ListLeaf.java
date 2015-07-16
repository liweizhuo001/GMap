package SPNs;

import java.util.ArrayList;

public class ListLeaf<E> extends ArrayList{
	char Variable;
	public int Max_index=-1;
	public String Max_name="";
	public boolean assigned=false;
	//protected  ArrayList<Integer> assignment = new ArrayList<Integer>();//取不同的布尔值
	//E a;
	public int get_Assignments()
	{
		return this.size();
	}
	
}
