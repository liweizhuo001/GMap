package SPNs;

import java.util.ArrayList;

public class Node {
	protected String type;
	protected String name;  
	protected int index;	//自己的索引
	protected double value;
	protected Node Fathers[];
	protected boolean calculated=false;
	public double MAP_value=0;
	public ArrayList<String> scope=new ArrayList<String>();
	
	public double get_Value()//得到节点的概率值
	{
		return value;
	}
	
	public int get_Index()//得到节点索引标签
	{
		return index;
	}
}
