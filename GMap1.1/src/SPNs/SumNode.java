package SPNs;

import java.util.ArrayList;


public class SumNode extends Node{
	public  ArrayList<Node> sum_c = new ArrayList<Node>();
	public  ArrayList<Double> sum_w = new ArrayList<Double>();
	//public double MAP_value=0;
	public int Max_index=-1;
	
	
	public double get_MAP_value()//取得MAP时最大的值
	{
		return MAP_value;
	}
	
	public int get_MAP_index()	//确定使MAP最大的儿子
	{
		return Max_index;
	}
	
	public void process()
	{
		int children_num=sum_c.size();
		double sum=0;
		double max=0;
		int max_index=0;
		//简单的加权求和的过程
		for(int i=0;i<children_num;i++)
		{
			double contribution=sum_c.get(i).value*sum_w.get(i);
			sum=sum+contribution;
		}
		for(int i=0;i<children_num;i++)
		{
			double contribution=sum_c.get(i).MAP_value*sum_w.get(i);
			if(max<contribution)
			{
				max=contribution;
				max_index=i;
			}
		}
		value=sum;
		MAP_value=max; 	
		Max_index=sum_c.get(max_index).index;//这里存储的是SPN中的标号		
		calculated=true;
	}
	
	public boolean isCalculated()//判断是否所有的儿子结点都可以计算
	{
		int right_num=0;
		for(int i=0;i<sum_c.size();i++)
		{
			if(sum_c.get(i).calculated==true)
				right_num++;				
		}
		if(right_num==sum_c.size())
			return true;
		else
			return false;
	}

}
