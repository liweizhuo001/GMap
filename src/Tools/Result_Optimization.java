package Tools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Result_Optimization {
	
	public ArrayList<String> Stable_Marriage(double [][]array)
    {
		List<Map.Entry<Integer,Double>> value = new ArrayList<Map.Entry<Integer,Double>>();
		HashMap<Integer,Double> map=new HashMap<Integer,Double>();  //存储每一行
		int num=0;
		for(int i=0;i<array.length;i++)
		{
			for(int j=0;j<array[0].length;j++)
			{
				map.put(num, array[i][j]);  //对于每一行进行排序
				num++;
			}			
		}		
		//利用函数将生产的数组进行排序(按照升序进行排列)
		value.addAll(map.entrySet());
		Collections.sort(value, new Comparator<Map.Entry<Integer, Double>>() {   
			public int compare(Map.Entry<Integer, Double> o1, Map.Entry<Integer, Double> o2) {      
				//return (o2.getValue() - o1.getValue()); 
				return (o2.getValue()).compareTo(o1.getValue());
			}
		});				
		ArrayList<Double> Stable_value=new ArrayList<Double>();
		ArrayList<Integer> Used_row_index=new ArrayList<Integer>();
		ArrayList<Integer> Used_column_index=new ArrayList<Integer>();
		for(int i=0;i<value.size();i++)
		{
			int index=value.get(i).getKey();	//数组的下标也是从0开始的
			int row_index=index/array[0].length;//定位到第几行第几列	
			int column_index=index%array[0].length;
			//值的个数肯定不会超过整个数据的行（行较小），保证行与列的索引均没有被用过
			if(Stable_value.size()<=array.length&&!Used_row_index.contains(row_index)&&!Used_column_index.contains(column_index))
			{
				Used_row_index.add(row_index);
				Used_column_index.add(column_index);
				Stable_value.add(value.get(i).getValue());
			}
			else if(Stable_value.size()>array.length)
				break;
		}
		ArrayList<String> pair=new ArrayList<String>();
		//将值、行列下标以列表的形式加入到链表中
		double global_sum=0;
		for(int i=0;i<Stable_value.size();i++)
		{
			String used_index=Used_row_index.get(i)+","+Used_column_index.get(i);
			double similarity=Stable_value.get(i);
			pair.add(used_index+","+similarity);
			global_sum=global_sum+similarity;
		}	
		System.out.println("Stable的最优值为:"+global_sum);
    	return pair;
    }
	
	public int[] FindGreedySim_Pair(double [][]array,String []str1,String []str2)
    {
    	double sim=-0.1;
    	double sum=0;   
    	//int min_length=Math.min(str1.length, str2.length);//只去长度少的长度
    	int pair[]=new int[str1.length];//最终返回的匹配对的长度应该是最小的字符串的长度
    	for(int i=0;i<array[0].length;i++)//这里采用的是遍历的方法(或许以后要用到动态规划)
    	{  
    		//预先定义一个链表，并且存储第一行某列的方法
    		ArrayList<Integer> labeled_row=new ArrayList<Integer>();
    		labeled_row.add(i);
    		sum=array[0][i];
    		for(int k=1;k<array.length;k++)
    		{
    			int label=FindsuitLabel(array[k],labeled_row);
    			labeled_row.add(label);  
    			sum=sum+array[k][label];
    		}		
    		if(sim<sum)
    		{
    			sim=sum; //获取相似度最大的情况   		
    			for(int L=0;L<labeled_row.size();L++)
    				pair[L]=labeled_row.get(L);
    		}
    	}
    	return pair;
    }
	
	public int FindsuitLabel(double []row,ArrayList<Integer> label)//通过已标记的标签来找每一行的最大值
    {
    	int suit_label=0;
    	double max_value=-0.1;
    	for(int i=0;i<row.length;i++)
    	{
    		if(max_value<row[i]&&!label.contains(i))
    		{
    			max_value=row[i];
    			suit_label=i;
    		}
    	}
    	return suit_label;//其实找到的对应的标签，也就知道其最大值是多少。因为下标代表行数。
    }
	
	public ArrayList<Integer> Dynamic_Programming(double [][]array)
	{
		//以列为统计个数自顶向下进行累加
		double global_sum=0;
		ArrayList<Integer> global_index=new ArrayList<Integer>();		
		double sum[]=new double[array[0].length];
		HashMap<Integer,ArrayList<Integer>> order=new HashMap<Integer,ArrayList<Integer>>();
		//先要将HashMap初始化
		for(int k=0;k<array[0].length;k++)
		{
			//ArrayList<Integer> a=new ArrayList<Integer>();
			
			order.put(k, new ArrayList<Integer>());
		}	
		//初始化
		for(int i=0;i<array[0].length;i++)
		{
			sum[i]=array[0][i];
			//order.get(i).add(i);
		}
		
		for(int L=1;L<array.length;L++)
		{	
			double[] new_sum=sum.clone();//用备份的数组进行运算
			for(int i=0;i<array[0].length;i++)//遍历新的一行中的每一列
			{
				int index=0;			
				double local_sum=0;
				
				for(int j=0;j<array[0].length;j++)//针对新一行中的前一列
				{				
					double value=array[L][i]+sum[j];
					if(local_sum<value&&!order.get(i).contains(j))
					//if(local_sum<value)
					{
						local_sum=value;
						index=j;//即回溯中，前一列中的下标
					}
				}
				new_sum[i]=local_sum;	//存储当前的最大值
				order.get(i).add(index); //存储当前最大值对应的前一列的下标
			}
			sum=new_sum.clone();//这样sum的值更改不会对后续结果产生影响,特别是第二列 
		}
		double max_Value=0;
		int last_index=0;
		for(int i=0;i<array[0].length;i++)
		{
			if(max_Value<sum[i]&&!order.get(i).contains(i))
			{
				max_Value=sum[i];
				last_index=i;
			}
		}
		//回溯
		ArrayList<Integer> backtrack=new ArrayList<Integer>();
		backtrack.add(last_index);
		int index=last_index;
		for(int L=array.length-2;L>=0;L--)
		{
			index=order.get(index).get(L);
			backtrack.add(index);
		}
		//将回溯的顺序到过来
		for(int i=backtrack.size()-1;i>=0;i--)
		{
			global_index.add(backtrack.get(i));
		}							
		return global_index;
	}
	
	/*public ArrayList<Integer> Beam_Search(double [][]array,int n)//这里是升级版的动态规划，而不是贪心
	{
		//以列为统计个数自顶向下进行累加,但需要保留几种备选方案，取最大的一种。当它逼近的方案个数等于矩阵的秩时，则是全局最优
		double global_sum=0;
		ArrayList<Integer> global_index=new ArrayList<Integer>();		//存储全局最优时每一行对应的下标
		double sum[][]=new double[n][array[0].length];			//初始化每一列的开头的值
		//HashMap<Integer,ArrayList<Integer>> order=new HashMap<Integer,ArrayList<Integer>>();//便于确定是否存在下标重复的情况
		//HashMap<Integer,ArrayList<Integer>> []order=new HashMap[n];	//Hasmap数组初始化的方法
		HashMap<Integer,ArrayList<Integer>[]> order=new HashMap<Integer,ArrayList<Integer>[]>();	
		//先要将HashMap初始化
		for(int i=0;i<array[0].length;i++)	//根据已有的大小n来确定候选的Map的个数
		{
			ArrayList<Integer> a[]=new ArrayList[n];
			for(int j=0;j<n;j++)
				a[j]= new ArrayList<Integer>();
			order.put(i, a);
		}	
		//ArrayList<Integer> a[]=new ArrayList[n];	//链表数组初始化的方法
		sum[0]=array[0].clone();
		for(int i=0;i<n;i++)//这里的需要假设初始化时前n项中是前k个候选值是相等的
		{
			for(int k=0;k<array[0].length;k++)//为了保证其不重复，应该只需初始化第一行
			{
				sum[i][k]=array[0][k];
			}
		}	
		//存在一个大小排序问题,应该是先排序再存储
		for(int L=1;L<array.length;L++)
		{
			double	[][]new_sum=new double[n][array[0].length];
			for(int w=0;w<n;w++)
			{
					new_sum[w]=sum[w].clone();//用备份的数组进行运算,二维数组的拷贝与1维数组不同	
			}
			for(int i=0;i<array[0].length;i++)//遍历新的一行中的每一列
			{
				int index=0;			
				double local_sum=0;
				int new_candidate[]=new int[n];
				double local_sum[]=new double[n];
				int index[]=new int[n];
				for(int C=0;C<n;C++)  	
				{
					index[C]=-1;
					local_sum[C]=-C-1;	//这样默认的降序排序就可以省略排序的过程									
				}
				//ArrayList<Integer>[]index=order.get(i).clone();//将第i的存储的index进行克隆
				//将local_sum做降序排序（由于对应的下标都是0，因此这一步可以省略）
				//sort(local_sum,0,n-1);
				for(int j=0;j<array[0].length;j++)//针对新一行中的每一列进行计算
				{	
					for(int candidate_num=0;candidate_num<n;candidate_num++)
					{
						double value=array[L][i]+sum[candidate_num][j];	
						System.out.println(order.get(i)[candidate_num].size());
						System.out.println(order.get(i)[candidate_num].contains(j));
						for(int t=0;t<order.get(i)[candidate_num].size();t++)
							System.out.println(order.get(i)[candidate_num].get(t));
						//默认是降序的，因此需要跟最小的那个进行比较
						if(value>local_sum[n-1]&&!order.get(i)[candidate_num].contains(j))//因为是降序，只需要跟最后一个值进行比较
						{
							
							int change_index=Change_local_value(local_sum,index,value,j);
							int change_index=Change_local_value(local_sum,value);//索引
							index[change_index]=j;			//也需要修改			
							index[candidate_num].remove(change_index);
							index[candidate_num].add(change_index, j);//这里并没有insert							
						}							
					}
				}
				System.out.println("*******************************");
				for(int candidate_num=0;candidate_num<n;candidate_num++)  
				{
					new_sum[candidate_num][i]=local_sum[candidate_num];	//将升序的结果进行备份
					order.get(i)[candidate_num].add(index[candidate_num]);
				}
			}
			sum=new_sum.clone();
		}
			
		//还需要一个函数来找到sum中的最大值
		double max_Value=0;
		int last_index=0;
		int last_candidate=0;
		for(int k=0;k<n;k++)
		{
			for(int i=0;i<array[0].length;i++)
			{
				if(max_Value<sum[k][i]&&!order.get(i)[k].contains(i))
				{
					max_Value=sum[k][i];
					last_index=i;
					last_candidate=k;
				}
			}
		}
		//回溯
		ArrayList<Integer> backtrack=new ArrayList<Integer>();
		backtrack.add(last_index);
		int index=last_index;
		for(int L=array.length-2;L>=0;L--)//回溯的对象不同了,找到最大的那个index下的坐标即可
		{
			index=order.get(index)[last_candidate].get(L);
			backtrack.add(index);
		}
		//将回溯的顺序到过来
		for(int i=backtrack.size()-1;i>=0;i--)
		{
			global_index.add(backtrack.get(i));
		}							
		return global_index;
	}*/
	
	public ArrayList<Integer> Beam_Search(double [][]array,int n)//这里是升级版的Beam_Search
	{
		//以列为统计个数自顶向下进行累加,但需要保留几种备选方案，取最大的一种。当它逼近的方案个数等于矩阵的秩时，则是全局最优	
		ArrayList<Integer> global_index=new ArrayList<Integer>();		//存储全局最优时每一行对应的下标		
		int m=array[0].length;  //m为数组的列
		class Node
		{
			double value;
			int index1;		//产生对应该value值新一行的第几列
			int index2;		//产生对应该value值原始链表中的第几种情况		
		}
		
		//先要将HashMap初始化
		HashMap<Integer,ArrayList<Integer>[]> order=new HashMap<Integer,ArrayList<Integer>[]>();	
		for(int i=0;i<array[0].length;i++)	//根据已有的大小n来确定候选的Map的个数
		{
			ArrayList<Integer> a[]=new ArrayList[n];
			for(int j=0;j<n;j++)
				a[j]= new ArrayList<Integer>();
			order.put(i, a);
		}	
		
		for(int k=0;k<array[0].length;k++)//默认将每一列的第一个写入Map
		{
			for(int i=0;i<n;i++)
			{
				order.get(k)[i].add(k);
			}
		}
		
		Node node[][]=new Node[m][n];  	//一共有m列，每列存储前n种情况
		//第一行需要做特殊的处理
		for(int i=0;i<m;i++)
		{
			for(int j=0;j<n;j++)
			{
				node[i][j] = new Node();
				if(j==0)
					node[i][j].value=array[0][i];			
				else
					node[i][j].value=0;
				node[i][j].index1=i;//产生对应该value值新一行的第几列
				node[i][j].index2=j;//产生对应该value值原始链表中的第几种情况
			}
		}	
		//通过链表存储的顺序得到Node中的Value
		//将Value与这新的这一层相加得到新的Value值
		//index1对应记录k个最大的情况的当层节点
		//index2对应记录对应的最大情况中的原始链表的顺序
		//这里必须要新定义一个MAP来存储新的顺序值，而不能从末尾添加！
		//再更新Map中的顺序
		//最后是基于所有的Node来进行的一个对比，取Value最大的那个点生成的那个顺序，即为所求！
			
		for(int i=0;i<m;i++)  //新的每一行的每一列
		{
			double local_sum[]=new double[n];
			int index1[]=new int[n];
			int index2[]=new int[n];
			for(int K=0;K<n;K++)  	
			{
				index1[K]=-1;	
				index2[K]=-1;
				local_sum[K]=-K-1;	//这样默认的降序排序就可以省略排序的过程									
			}			
			
			for(int j=0;j<m;j++)	//遍历当前列的n个结点
			{
				double value=node[i][0].value+array[1][j];//
				if(value>local_sum[n-1]&&!order.get(i)[0].contains(j))//因为是降序，只需要跟最后一个值进行比较
				{	
					//满足条件，进行插值
					Change_local_value(local_sum,index1,index2,value,j,0);
				}
			}		
			//更新Node节点,Map 链表
			ArrayList<Integer> copy[]=new ArrayList[n];
			for(int t=0;t<n;t++)
				copy[t]= new ArrayList<Integer>();
			for(int K=0;K<n;K++)  	
			{
				//更新节点
				ArrayList<Integer> original[]=new ArrayList[n];
				for(int t=0;t<n;t++)
					original[t]= new ArrayList<Integer>();
				for(int r=0;r<n;r++)
					original[r]=(ArrayList<Integer>) order.get(i)[r].clone();
				node[i][K].value=local_sum[K];
				node[i][K].index1=index1[K];//产生对应该value值新一行的第几列
				node[i][K].index2=index2[K];//产生对应该value值原始链表中的第几种情况
				//用节点的所有去更新链表	
				original[index2[K]].add(index1[K]);	
				copy[K]=original[index2[K]];	
			}	
			for(int K=0;K<n;K++)
				order.get(i)[K]=copy[K];
		}
			
		//正式计算
		for(int L=2;L<array.length;L++)
		{		
			for(int i=0;i<m;i++)  //新的每一行的每一列
			{
				double local_sum[]=new double[n];
				int index1[]=new int[n];
				int index2[]=new int[n];
				for(int K=0;K<n;K++)  	
				{
					index1[K]=-1;	
					index2[K]=-1;
					local_sum[K]=-K-1;	//这样默认的降序排序就可以省略排序的过程									
				}			
				for(int c=0;c<n;c++)	//对上一列的原始node的值前n种进行遍历一下
				{
					for(int j=0;j<m;j++)	//遍历当前列的n个结点
					{
						double value=node[i][c].value+array[L][j];
						if(value>local_sum[n-1]&&!order.get(i)[c].contains(j))//因为是降序，只需要跟最后一个值进行比较
						{	
							//满足条件，进行插值
							Change_local_value(local_sum,index1,index2,value,j,c);
							//index2[c]=c;//存储来自第几种备选方案的信息
						}
					}
				}
				//更新Node节点,Map 链表
				
				ArrayList<Integer> copy[]=new ArrayList[n];
				for(int t=0;t<n;t++)
					copy[t]= new ArrayList<Integer>();
				for(int K=0;K<n;K++)  	
				{
					//更新节点
					ArrayList<Integer> original[]=new ArrayList[n];
					for(int t=0;t<n;t++)
						original[t]= new ArrayList<Integer>();
					for(int r=0;r<n;r++)
						original[r]=(ArrayList<Integer>) order.get(i)[r].clone();
					node[i][K].value=local_sum[K];
					node[i][K].index1=index1[K];//产生对应该value值新一行的第几列
					node[i][K].index2=index2[K];//产生对应该value值原始链表中的第几种情况
					//用节点的所有去更新链表	
					original[index2[K]].add(index1[K]);	
					copy[K]=original[index2[K]];	
				}	
				for(int K=0;K<n;K++)
					order.get(i)[K]=copy[K];
			}
		}	
		double global_sum=0;
		int last_index=0;
		for(int i=0;i<m;i++)
		{
			if(global_sum<node[i][0].value)
			{
				global_sum=node[i][0].value;
				last_index=i;
			}
		}
		global_index=order.get(last_index)[0];
		System.out.println("全局最优值为:"+global_sum);
		return global_index;
	}
	
	
	
	public int find_index(ArrayList<String> tokens,String concept)
	{
		//int index=0;	
		for(int i=0;i<tokens.size();i++)
		{
			if(tokens.get(i).equals(concept))
			{
				return i;
			}
		}
		return 0;
	}
	
	
	//快排	
	public void sort(double[] a, int left, int right) {
		if (right > left) {
			int i = left;
			double p = a[left];
			for (int j = 1; j <= right - left; j++) {
				if (a[j+left] > p) {
					double tmp_j=a[left+j];
					for(int k=left+j;k>i;k--){
						a[k]=a[k-1];
					}
					a[i]=tmp_j;
					i++;
				}

			}
			if (left + 1 < i){
				sort(a, left, i - 1);
			}
			if (i < right - 1){
				sort(a, i + 1, right);
			}
		}
	}
	
	//插值排序时，对应的值进行更改
	public void Change_local_value(double a[],int b[],int c[],double value,int m,int n)
	{
		//local_sum,index1,index2,value,j,c
		int k=0;//记录比大小时满足条件的信息
		
		for(int i=0;i<a.length;i++)
		{
			if(a[i]<value)
			{
				k=i;
				break;
			}				
		}
		//插值后更新值（降序）	
		for(int i=a.length-1;i>k;i--)
		{
			a[i]=a[i-1];
		}
		a[k]=value;
		//插值后更新index1，即那一列的影响
		for(int i=b.length-1;i>k;i--)
		{
			b[i]=b[i-1];
		}
		b[k]=m;
		//插值后更新index2，即那一列的第几种情况的影响
		for(int i=c.length-1;i>k;i--)
		{
			c[i]=c[i-1];
		}
		c[k]=n;
	}
}

	 

