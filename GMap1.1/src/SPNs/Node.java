package SPNs;

import java.util.ArrayList;

public class Node {
	protected String type;
	protected String name;  
	protected int index;	//�Լ�������
	protected double value;
	protected Node Fathers[];
	protected boolean calculated=false;
	public double MAP_value=0;
	public ArrayList<String> scope=new ArrayList<String>();
	
	public double get_Value()//�õ��ڵ�ĸ���ֵ
	{
		return value;
	}
	
	public int get_Index()//�õ��ڵ�������ǩ
	{
		return index;
	}
}
