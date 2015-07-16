package Tools;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Refine_Tools {
	/**
	 * 
	 * @param map
	 * @param superclasses1
	 * @param superclasses2
	 * @return classes map without CrissCross
	 */
	public ArrayList<String> removeCrissCross(ArrayList<String> map, ArrayList<String> superclasses1,ArrayList<String> superclasses2)
	{
		//ArrayList<String> refinedMap=new ArrayList<String>();
		TreeMap_Tools Ontology1_super=new TreeMap_Tools(superclasses1);
		TreeMap_Tools Ontology2_super=new TreeMap_Tools(superclasses2);
		for(int i=0;i<map.size();i++)
		{
			String part1[]=map.get(i).split(",");
			String map1_Concept1=part1[0];
			String map1_Concept2=part1[1];
			double map1_value=Double.parseDouble(part1[2]);
			System.out.println(map1_Concept2);	
			for(int j=i+1;j<map.size();j++)
			{
				String part2[]=map.get(j).split(",");
				String map2_Concept1=part2[0];
				String map2_Concept2=part2[1];
				double map2_value=Double.parseDouble(part2[2]);
				ArrayList<String> Father1=Ontology1_super.GetKey_Value(map2_Concept1);
				ArrayList<String> Father2=Ontology2_super.GetKey_Value(map1_Concept2);			
				if(Father1!=null&&Father2!=null)
				{
					if(Father1.contains(map1_Concept1)&&Father2.contains(map2_Concept2))
					{
						if(map1_value>map2_value)
						{
							map.remove(j);
							j--;
							continue;
						}
						else
						{							
							map.remove(i);
							i--;
							break;
						}
						//map的长度减少了一位所以要保证扫描时的长度一定要一直
					}	
				}
				Father1=Ontology1_super.GetKey_Value(map1_Concept1);
				Father2=Ontology2_super.GetKey_Value(map2_Concept2);			
				if(Father1!=null&&Father2!=null)
				{
					if(Father1.contains(map2_Concept1)&&Father2.contains(map1_Concept2))
					{
						if(map1_value>map2_value)
						{
							map.remove(j);
							j--;
							continue;
						}
						else
						{							
							map.remove(i);
							i--;
							break;
						}
						//map的长度减少了一位所以要保证扫描时的长度一定要一直
					}	
				}
			}		
		}	
		return map;
	}
	
	public ArrayList<String> removeCrissCrossInProperty(ArrayList<String> PropertyMap, ArrayList<String> classesMap,ArrayList<String> relations1,ArrayList<String> relations2)	
	//public ArrayList<String> removeCrissCrossInProperty(ArrayList<String> PropertyMap, String classesMap,ArrayList<String> relations1,ArrayList<String> relations2) throws IOException
	{		
		ArrayList<String> refinedProperty=new ArrayList<String>();
		//ArrayList<String> CMap=transformToMap(classesMap);
		ArrayList<String> PMap=transformToMap(PropertyMap);
		TreeMap_Tools Match_Class=new TreeMap_Tools(classesMap);
		TreeMap_Tools Match_Property=new TreeMap_Tools(PMap);
		for(int i=0;i<relations1.size();i++)
		{
			String triple1=relations1.get(i).toLowerCase();
			String[] part1 = triple1.split(",");
			//标签是一致的，所有很容易定位part1[0]表示Subject part1[2]表示Object
			String Subject1=part1[0];
			String property1=part1[1];
			String Object1=part1[2];								
			for(int j=0;j<relations2.size();j++)
			{
				String triple2=relations2.get(j).toLowerCase();
				String[] part2 = triple2.split(",");
				String Subject2=part2[0];
				String property2=part2[1];
				String Object2=part2[2];	
				//三元组都满足匹配条件，但是两者的关系定义域与值域正好相反
				if(Match_Class.has_relation(Subject1, Object2)&&Match_Class.has_relation(Subject2, Object1)
				   &&!Subject1.equals(Subject2)&&!Object1.equals(Object2)&&Match_Property.has_relation(property1, property2))
				{					
					System.out.println(property1+"与"+property2+"被移除的缘由如下:");	
					System.out.println("本体1的三元组为："+relations1.get(i).toLowerCase());
					System.out.println("本体2的三元组为："+relations2.get(j).toLowerCase());
					System.out.println("十字概念匹配对分别为："+Subject1+","+Object2+"	"+Subject2+","+Object1);
					Match_Property.remove(property1, property2);			
					//Match_Property
				}
				
			}	
		}
		for(int i=0;i<PropertyMap.size();i++)
		{
			String part[]=PropertyMap.get(i).toLowerCase().split(",");
			if(Match_Property.has_relation(part[0], part[1]))
			{
				refinedProperty.add(PropertyMap.get(i));
			}
		}
		//还需要把MAP变成链表的形式进行返回
		return refinedProperty;
	}
	
	public ArrayList<String> transformToMap(ArrayList<String> originalMap)
	{
		ArrayList<String> standardMap=new ArrayList<String>();
		for(int i=0;i<originalMap.size();i++)
		{
			String part[]=originalMap.get(i).split(",");
			standardMap.add(part[0]+"--"+part[1]);
		}
		return standardMap;
	}
	
	public HashMap<String,Integer> transformToHashMap(ArrayList<String> originalMap)
	{
		HashMap<String,Integer> standardMap=new HashMap<String,Integer>();
		for(int i=0;i<originalMap.size();i++)
		{
			String part[]=originalMap.get(i).split(",");
			standardMap.put(part[0]+"--"+part[1], i);
			//standardMap.add();
		}
		return standardMap;
	}
	
	public ArrayList<String> removeStemConflict(ArrayList<String> refinedPropertyMap1,ArrayList<String> propertiesInverse1,ArrayList<String> propertiesInverse2)
	{
		//Sim_Tools tools=new Sim_Tools();
		//ArrayList<String> refinedProperty=new ArrayList<String>();
		TreeMap_Tools inversrPair1=new TreeMap_Tools(propertiesInverse1);
		TreeMap_Tools inversrPair2=new TreeMap_Tools(propertiesInverse2);
		for(int i=0;i<refinedPropertyMap1.size();i++)
		{
			String part[]=refinedPropertyMap1.get(i).split(",");
			/*boolean isStemed1=Boolean.parseBoolean(part[3]);
			boolean isStemed2=Boolean.parseBoolean(part[4]);*/
			String tense1=part[3];
			String tense2=part[4];
			ArrayList<String> inverseProperty1=inversrPair1.GetKey_Value(part[0].toLowerCase());
			ArrayList<String> inverseProperty2=inversrPair2.GetKey_Value(part[1].toLowerCase());
			 			
			String stemProperty1=part[5];
			String stemProperty2=part[6];
			
			//主被动之间的转换情况，往往这种情况的概率是一样的。
			//前者是被动，后者是主动,而且这种被动形式是有主动属性存在的
			if(tense1.equals("*")&&tense2.equals("1")&&stemProperty1.equals(stemProperty2))//前者是被动，后者是主动
			{
				refinedPropertyMap1.remove(i);
				i--;
			}
			else if(tense1.equals("1")&&tense2.equals("*")&&stemProperty1.equals(stemProperty2))//前者是主动，后者是被动
			{
				refinedPropertyMap1.remove(i);
				i--;
			}
			else if(tense1.equals("*")&&tense2.equals("0")&&inverseProperty1!=null&&stemProperty1.equals(stemProperty2))//前者是被动，后者是主动，且前者有逆数属性
			{
				refinedPropertyMap1.remove(i);
				i--;
			}
			
			//前者是主动，后者是被动，而且这种被动形式是有主动属性存在的
			else if(tense1.equals("0")&&tense2.equals("*")&&inverseProperty2!=null&&stemProperty1.equals(stemProperty2))
			{
				refinedPropertyMap1.remove(i);
				i--;
			}
		}
		return refinedPropertyMap1;
	}
	
	public ArrayList<String> keepOneToOneAlignment(ArrayList<String> map)
	{
		ArrayList<String> refinedMap= new ArrayList<String>();
		HashMap<String, Integer> Maps=transformToHashMap(map);
		//ArrayList<String> Maps=transformToMap(map);
		HashMap<String,Integer> tokensMap1= new HashMap<String,Integer>();
		HashMap<String,Integer> tokensMap2= new HashMap<String,Integer>();
		ArrayList<String> tokens1=new ArrayList<String>();
		ArrayList<String> tokens2=new ArrayList<String>();
		int mapTupleSize=0;
		int m=0,n=0;
		for(int i=0;i<map.size();i++)
		{
			String part[]=map.get(i).split(",");
			mapTupleSize=map.get(0).split(",").length;
			String concept1=part[0];
			String concept2=part[1];			
			if(!tokensMap1.containsKey(concept1))
				tokensMap1.put(concept1, m++);
			if(!tokens1.contains(concept1))
				tokens1.add(concept1);		
			if(!tokensMap2.containsKey(concept2))
				tokensMap2.put(concept2, n++);
			if(!tokens2.contains(concept2))
				tokens2.add(concept2);
		}
		if(tokens1.size()<=tokens2.size())
		{
			double[][] similarity=new double[tokens1.size()][tokens2.size()];
			for(int i=0;i<map.size();i++)
			{
				String part[]=map.get(i).split(",");
				String concept1=part[0];
				String concept2=part[1];			
				//int index1=find_index(tokens1,concept1);
				//int index2=find_index(tokens2,concept2);
				int index1=tokensMap1.get(concept1);
				int index2=tokensMap2.get(concept2);
				try
				{
				if(concept1.equals(concept2))//对于名字相同的匹配对，由于其语义相似度无法区别，因此在此加一个微弱的波动
					similarity[index1][index2]=Double.parseDouble(part[2])+0.0000001;	//Double.MIN_VALUE
				else
					similarity[index1][index2]=Double.parseDouble(part[2]);	
				}
				catch(Exception e)
				{
					System.out.println();
				}
			}
			//System.out.println("由Stable Marriage策略得到的序列对为:");
			ArrayList<String> Stable_pair=Stable_Marriage(similarity);
			for(int i=0;i<Stable_pair.size();i++)
			{
				String part[]=Stable_pair.get(i).split(",");
				int row=Integer.parseInt(part[0]);
				int column=Integer.parseInt(part[1]);
				//方便验证是否匹配
				//System.out.println(tokens1.get(row)+","+tokens2.get(column)+","+similarity[row][column]);
				if(mapTupleSize==7||mapTupleSize==5)
				{
					String concept=tokens1.get(row)+"--"+tokens2.get(column);			
					//int index=find_index(Maps, concept);
					Integer index=Maps.get(concept);
					if(index!=null)//因为后面极有可能有空
						refinedMap.add(map.get(index));				
				}				
				else
				{
					refinedMap.add(tokens1.get(row)+","+tokens2.get(column)+","+similarity[row][column]);
				}
			}
			
			/*System.out.println("由Beam_Search策略得到的序列对为:");
			ArrayList<String>  final_match3= new ArrayList<String>();
			int Backtracking_num=2;
			ArrayList<Integer> BS_pair=Beam_Search(similarity,Backtracking_num);
			for(int i=0;i<BS_pair.size();i++)
			{			
				String pair=tokens1.get(i)+","+tokens2.get(BS_pair.get(i))+","+similarity[i][BS_pair.get(i)];
				refinedMap.add(tokens1.get(row)+","+tokens2.get(column)+","+similarity[row][column]);
				System.out.println(pair);
			}*/
			
		}
		else
		{
			double[][] similarity=new double[tokens2.size()][tokens1.size()];
			for(int i=0;i<map.size();i++)
			{
				String part[]=map.get(i).split(",");
				String concept1=part[0];
				String concept2=part[1];			
				//int index1=find_index(tokens1,concept1);
				//int index2=find_index(tokens2,concept2);
				int index1=tokensMap1.get(concept1);
				int index2=tokensMap2.get(concept2);
				if(concept1.equals(concept2))//对于名字相同的语义对，由于其语义相似度无法区别，因此在此加一个微弱的波动
					similarity[index2][index1]=Double.parseDouble(part[2])+0.0000001;	
				else
					similarity[index2][index1]=Double.parseDouble(part[2]);	
			}
			//System.out.println("由Stable Marriage策略得到的序列对为:");
			ArrayList<String> Stable_pair=Stable_Marriage(similarity);
			for(int i=0;i<Stable_pair.size();i++)
			{
				String part[]=Stable_pair.get(i).split(",");
				int row=Integer.parseInt(part[0]);
				int column=Integer.parseInt(part[1]);
				//方便验证是否匹配
				if(mapTupleSize==7||mapTupleSize==5)
				{
					String concept=tokens1.get(column)+"--"+tokens2.get(row);	
					//int index=find_index(Maps, concept);
					Integer index=Maps.get(concept);
					if(index!=null)
						refinedMap.add(map.get(index));
				}				
				else
				{
					refinedMap.add(tokens1.get(column)+","+tokens2.get(row)+","+similarity[row][column]);
				}
				//System.out.println(tokens1.get(column)+","+tokens2.get(row)+","+similarity[row][column]);
				//refinedMap.add(tokens1.get(column)+","+tokens2.get(row)+","+similarity[row][column]);
			}
			
			/*System.out.println("由Beam_Search策略得到的序列对为:");
			int Backtracking_num=2;
			ArrayList<Integer> BS_pair=Beam_Search(similarity,Backtracking_num);
			for(int i=0;i<BS_pair.size();i++)
			{			
				//tokens是不变的，只是相似度变了
				String pair=tokens1.get(BS_pair.get(i))+","+tokens2.get(i)+","+similarity[i][BS_pair.get(i)];
				refinedMap.add(tokens1.get(row)+","+tokens2.get(column)+","+similarity[row][column]);
				System.out.println(pair);
			}			*/
		}
		return refinedMap;
	}
	
	
	/**
	 * Stable_Marriage Strategy(local)
	 * @param maps
	 * @return one to one alignments
	 */
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
		ArrayList<String> pair=new ArrayList<String>();
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
				pair.add(row_index+","+column_index+","+value.get(i).getValue());
			}
			else if(Stable_value.size()>array.length)
				break;
		}
		
		//将值、行列下标以列表的形式加入到链表中
		/*double global_sum=0;
		for(int i=0;i<Stable_value.size();i++)
		{
			String used_index=Used_row_index.get(i)+","+Used_column_index.get(i);
			double similarity=Stable_value.get(i);
			pair.add(used_index+","+similarity);
			global_sum=global_sum+similarity;
		}*/	
		//System.out.println("Stable的最优值为:"+global_sum);
    	return pair;
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
	
	/*public int find_index(HashMap<String,Integer> tokens,String concept)
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
		return 
	}*/
	
	/**
	 * Beam_Search Strategy(global)
	 * @param array
	 * @param n backtrack number
	 * @return one to one alignments
	 */
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
