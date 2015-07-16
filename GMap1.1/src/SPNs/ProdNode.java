package SPNs;

import java.util.ArrayList;


public class ProdNode extends Node{
	protected ArrayList<Node> prod_c = new ArrayList<Node>();
	
	public void process()
	{
		int children_num=prod_c.size();
		double product=1;
		double max_product=1;
		for(int i=0;i<children_num;i++)
		{
			product=product*prod_c.get(i).value;
		}
		value=product;
		for(int i=0;i<children_num;i++)
		{
			max_product=max_product*prod_c.get(i).MAP_value;
		}
		MAP_value=max_product;
		calculated=true;
	}
	
	public boolean isCalculated()//判断是否所有的儿子结点都可以计算
	{
		int right_num=0;
		for(int i=0;i<prod_c.size();i++)
		{
			if(prod_c.get(i).calculated==true)
				right_num++;				
		}
		if(right_num==prod_c.size())
			return true;
		else
			return false;
	}
}
