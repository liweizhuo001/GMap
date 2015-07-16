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
						//map�ĳ��ȼ�����һλ����Ҫ��֤ɨ��ʱ�ĳ���һ��Ҫһֱ
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
						//map�ĳ��ȼ�����һλ����Ҫ��֤ɨ��ʱ�ĳ���һ��Ҫһֱ
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
			//��ǩ��һ�µģ����к����׶�λpart1[0]��ʾSubject part1[2]��ʾObject
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
				//��Ԫ�鶼����ƥ���������������ߵĹ�ϵ��������ֵ�������෴
				if(Match_Class.has_relation(Subject1, Object2)&&Match_Class.has_relation(Subject2, Object1)
				   &&!Subject1.equals(Subject2)&&!Object1.equals(Object2)&&Match_Property.has_relation(property1, property2))
				{					
					System.out.println(property1+"��"+property2+"���Ƴ���Ե������:");	
					System.out.println("����1����Ԫ��Ϊ��"+relations1.get(i).toLowerCase());
					System.out.println("����2����Ԫ��Ϊ��"+relations2.get(j).toLowerCase());
					System.out.println("ʮ�ָ���ƥ��Էֱ�Ϊ��"+Subject1+","+Object2+"	"+Subject2+","+Object1);
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
		//����Ҫ��MAP����������ʽ���з���
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
			
			//������֮���ת�������������������ĸ�����һ���ġ�
			//ǰ���Ǳ���������������,�������ֱ�����ʽ�����������Դ��ڵ�
			if(tense1.equals("*")&&tense2.equals("1")&&stemProperty1.equals(stemProperty2))//ǰ���Ǳ���������������
			{
				refinedPropertyMap1.remove(i);
				i--;
			}
			else if(tense1.equals("1")&&tense2.equals("*")&&stemProperty1.equals(stemProperty2))//ǰ���������������Ǳ���
			{
				refinedPropertyMap1.remove(i);
				i--;
			}
			else if(tense1.equals("*")&&tense2.equals("0")&&inverseProperty1!=null&&stemProperty1.equals(stemProperty2))//ǰ���Ǳ�������������������ǰ������������
			{
				refinedPropertyMap1.remove(i);
				i--;
			}
			
			//ǰ���������������Ǳ������������ֱ�����ʽ�����������Դ��ڵ�
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
				if(concept1.equals(concept2))//����������ͬ��ƥ��ԣ��������������ƶ��޷���������ڴ˼�һ��΢���Ĳ���
					similarity[index1][index2]=Double.parseDouble(part[2])+0.0000001;	//Double.MIN_VALUE
				else
					similarity[index1][index2]=Double.parseDouble(part[2]);	
				}
				catch(Exception e)
				{
					System.out.println();
				}
			}
			//System.out.println("��Stable Marriage���Եõ������ж�Ϊ:");
			ArrayList<String> Stable_pair=Stable_Marriage(similarity);
			for(int i=0;i<Stable_pair.size();i++)
			{
				String part[]=Stable_pair.get(i).split(",");
				int row=Integer.parseInt(part[0]);
				int column=Integer.parseInt(part[1]);
				//������֤�Ƿ�ƥ��
				//System.out.println(tokens1.get(row)+","+tokens2.get(column)+","+similarity[row][column]);
				if(mapTupleSize==7||mapTupleSize==5)
				{
					String concept=tokens1.get(row)+"--"+tokens2.get(column);			
					//int index=find_index(Maps, concept);
					Integer index=Maps.get(concept);
					if(index!=null)//��Ϊ���漫�п����п�
						refinedMap.add(map.get(index));				
				}				
				else
				{
					refinedMap.add(tokens1.get(row)+","+tokens2.get(column)+","+similarity[row][column]);
				}
			}
			
			/*System.out.println("��Beam_Search���Եõ������ж�Ϊ:");
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
				if(concept1.equals(concept2))//����������ͬ������ԣ��������������ƶ��޷���������ڴ˼�һ��΢���Ĳ���
					similarity[index2][index1]=Double.parseDouble(part[2])+0.0000001;	
				else
					similarity[index2][index1]=Double.parseDouble(part[2]);	
			}
			//System.out.println("��Stable Marriage���Եõ������ж�Ϊ:");
			ArrayList<String> Stable_pair=Stable_Marriage(similarity);
			for(int i=0;i<Stable_pair.size();i++)
			{
				String part[]=Stable_pair.get(i).split(",");
				int row=Integer.parseInt(part[0]);
				int column=Integer.parseInt(part[1]);
				//������֤�Ƿ�ƥ��
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
			
			/*System.out.println("��Beam_Search���Եõ������ж�Ϊ:");
			int Backtracking_num=2;
			ArrayList<Integer> BS_pair=Beam_Search(similarity,Backtracking_num);
			for(int i=0;i<BS_pair.size();i++)
			{			
				//tokens�ǲ���ģ�ֻ�����ƶȱ���
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
		HashMap<Integer,Double> map=new HashMap<Integer,Double>();  //�洢ÿһ��
		int num=0;
		for(int i=0;i<array.length;i++)
		{
			for(int j=0;j<array[0].length;j++)
			{
				map.put(num, array[i][j]);  //����ÿһ�н�������
				num++;
			}			
		}		
		//���ú����������������������(���������������)
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
			int index=value.get(i).getKey();	//������±�Ҳ�Ǵ�0��ʼ��
			int row_index=index/array[0].length;//��λ���ڼ��еڼ���	
			int column_index=index%array[0].length;
			//ֵ�ĸ����϶����ᳬ���������ݵ��У��н�С������֤�����е�������û�б��ù�
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
		
		//��ֵ�������±����б����ʽ���뵽������
		/*double global_sum=0;
		for(int i=0;i<Stable_value.size();i++)
		{
			String used_index=Used_row_index.get(i)+","+Used_column_index.get(i);
			double similarity=Stable_value.get(i);
			pair.add(used_index+","+similarity);
			global_sum=global_sum+similarity;
		}*/	
		//System.out.println("Stable������ֵΪ:"+global_sum);
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
	public ArrayList<Integer> Beam_Search(double [][]array,int n)//�������������Beam_Search
	{
		//����Ϊͳ�Ƹ����Զ����½����ۼ�,����Ҫ�������ֱ�ѡ������ȡ����һ�֡������ƽ��ķ����������ھ������ʱ������ȫ������	
		ArrayList<Integer> global_index=new ArrayList<Integer>();		//�洢ȫ������ʱÿһ�ж�Ӧ���±�		
		int m=array[0].length;  //mΪ�������
		class Node
		{
			double value;
			int index1;		//������Ӧ��valueֵ��һ�еĵڼ���
			int index2;		//������Ӧ��valueֵԭʼ�����еĵڼ������		
		}
		
		//��Ҫ��HashMap��ʼ��
		HashMap<Integer,ArrayList<Integer>[]> order=new HashMap<Integer,ArrayList<Integer>[]>();	
		for(int i=0;i<array[0].length;i++)	//�������еĴ�Сn��ȷ����ѡ��Map�ĸ���
		{
			ArrayList<Integer> a[]=new ArrayList[n];
			for(int j=0;j<n;j++)
				a[j]= new ArrayList<Integer>();
			order.put(i, a);
		}	
		
		for(int k=0;k<array[0].length;k++)//Ĭ�Ͻ�ÿһ�еĵ�һ��д��Map
		{
			for(int i=0;i<n;i++)
			{
				order.get(k)[i].add(k);
			}
		}
		
		Node node[][]=new Node[m][n];  	//һ����m�У�ÿ�д洢ǰn�����
		//��һ����Ҫ������Ĵ���
		for(int i=0;i<m;i++)
		{
			for(int j=0;j<n;j++)
			{
				node[i][j] = new Node();
				if(j==0)
					node[i][j].value=array[0][i];			
				else
					node[i][j].value=0;
				node[i][j].index1=i;//������Ӧ��valueֵ��һ�еĵڼ���
				node[i][j].index2=j;//������Ӧ��valueֵԭʼ�����еĵڼ������
			}
		}	
		//ͨ������洢��˳��õ�Node�е�Value
		//��Value�����µ���һ����ӵõ��µ�Valueֵ
		//index1��Ӧ��¼k����������ĵ���ڵ�
		//index2��Ӧ��¼��Ӧ���������е�ԭʼ�����˳��
		//�������Ҫ�¶���һ��MAP���洢�µ�˳��ֵ�������ܴ�ĩβ��ӣ�
		//�ٸ���Map�е�˳��
		//����ǻ������е�Node�����е�һ���Աȣ�ȡValue�����Ǹ������ɵ��Ǹ�˳�򣬼�Ϊ����
			
		for(int i=0;i<m;i++)  //�µ�ÿһ�е�ÿһ��
		{
			double local_sum[]=new double[n];
			int index1[]=new int[n];
			int index2[]=new int[n];
			for(int K=0;K<n;K++)  	
			{
				index1[K]=-1;	
				index2[K]=-1;
				local_sum[K]=-K-1;	//����Ĭ�ϵĽ�������Ϳ���ʡ������Ĺ���									
			}			
			
			for(int j=0;j<m;j++)	//������ǰ�е�n�����
			{
				double value=node[i][0].value+array[1][j];//
				if(value>local_sum[n-1]&&!order.get(i)[0].contains(j))//��Ϊ�ǽ���ֻ��Ҫ�����һ��ֵ���бȽ�
				{	
					//�������������в�ֵ
					Change_local_value(local_sum,index1,index2,value,j,0);
				}
			}		
			//����Node�ڵ�,Map ����
			ArrayList<Integer> copy[]=new ArrayList[n];
			for(int t=0;t<n;t++)
				copy[t]= new ArrayList<Integer>();
			for(int K=0;K<n;K++)  	
			{
				//���½ڵ�
				ArrayList<Integer> original[]=new ArrayList[n];
				for(int t=0;t<n;t++)
					original[t]= new ArrayList<Integer>();
				for(int r=0;r<n;r++)
					original[r]=(ArrayList<Integer>) order.get(i)[r].clone();
				node[i][K].value=local_sum[K];
				node[i][K].index1=index1[K];//������Ӧ��valueֵ��һ�еĵڼ���
				node[i][K].index2=index2[K];//������Ӧ��valueֵԭʼ�����еĵڼ������
				//�ýڵ������ȥ��������	
				original[index2[K]].add(index1[K]);	
				copy[K]=original[index2[K]];	
			}	
			for(int K=0;K<n;K++)
				order.get(i)[K]=copy[K];
		}
			
		//��ʽ����
		for(int L=2;L<array.length;L++)
		{		
			for(int i=0;i<m;i++)  //�µ�ÿһ�е�ÿһ��
			{
				double local_sum[]=new double[n];
				int index1[]=new int[n];
				int index2[]=new int[n];
				for(int K=0;K<n;K++)  	
				{
					index1[K]=-1;	
					index2[K]=-1;
					local_sum[K]=-K-1;	//����Ĭ�ϵĽ�������Ϳ���ʡ������Ĺ���									
				}			
				for(int c=0;c<n;c++)	//����һ�е�ԭʼnode��ֵǰn�ֽ��б���һ��
				{
					for(int j=0;j<m;j++)	//������ǰ�е�n�����
					{
						double value=node[i][c].value+array[L][j];
						if(value>local_sum[n-1]&&!order.get(i)[c].contains(j))//��Ϊ�ǽ���ֻ��Ҫ�����һ��ֵ���бȽ�
						{	
							//�������������в�ֵ
							Change_local_value(local_sum,index1,index2,value,j,c);
							//index2[c]=c;//�洢���Եڼ��ֱ�ѡ��������Ϣ
						}
					}
				}
				//����Node�ڵ�,Map ����
				
				ArrayList<Integer> copy[]=new ArrayList[n];
				for(int t=0;t<n;t++)
					copy[t]= new ArrayList<Integer>();
				for(int K=0;K<n;K++)  	
				{
					//���½ڵ�
					ArrayList<Integer> original[]=new ArrayList[n];
					for(int t=0;t<n;t++)
						original[t]= new ArrayList<Integer>();
					for(int r=0;r<n;r++)
						original[r]=(ArrayList<Integer>) order.get(i)[r].clone();
					node[i][K].value=local_sum[K];
					node[i][K].index1=index1[K];//������Ӧ��valueֵ��һ�еĵڼ���
					node[i][K].index2=index2[K];//������Ӧ��valueֵԭʼ�����еĵڼ������
					//�ýڵ������ȥ��������	
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
		System.out.println("ȫ������ֵΪ:"+global_sum);
		return global_index;
	}
	
	public void Change_local_value(double a[],int b[],int c[],double value,int m,int n)
	{
		//local_sum,index1,index2,value,j,c
		int k=0;//��¼�ȴ�Сʱ������������Ϣ
		
		for(int i=0;i<a.length;i++)
		{
			if(a[i]<value)
			{
				k=i;
				break;
			}				
		}
		//��ֵ�����ֵ������	
		for(int i=a.length-1;i>k;i--)
		{
			a[i]=a[i-1];
		}
		a[k]=value;
		//��ֵ�����index1������һ�е�Ӱ��
		for(int i=b.length-1;i>k;i--)
		{
			b[i]=b[i-1];
		}
		b[k]=m;
		//��ֵ�����index2������һ�еĵڼ��������Ӱ��
		for(int i=c.length-1;i>k;i--)
		{
			c[i]=c[i-1];
		}
		c[k]=n;
	}
	
}
