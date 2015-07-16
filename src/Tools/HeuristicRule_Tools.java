package Tools;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class HeuristicRule_Tools {
	TreeMap_Tools Match_Class;
	TreeMap_Tools Match_Property;
	ArrayList<String> Based_Father_Pairs=new ArrayList<String>();//基于父亲匹配的概念对		
	ArrayList<String> Based_Children_Pairs=new ArrayList<String>();//基于儿子匹配的概念对
	ArrayList<String> Based_Siblings_Pairs=new ArrayList<String>();//基于兄弟姐妹匹配的概念对
	ArrayList<String> Based_hasPart_Pairs=new ArrayList<String> ();
	ArrayList<String> Based_PartOf_Pairs=new ArrayList<String>();//基于兄弟姐妹匹配的概念对
	ArrayList<String> Based_Domain_Pairs=new ArrayList<String>();//基于定义域匹配的概念对
	ArrayList<String> Based_Range_Pairs=new ArrayList<String>();//基于值域匹配的概念对
	ArrayList<String> Based_DataType_Pairs=new ArrayList<String>();//基于DataType匹配的概念对
	ArrayList<String> Based_Disjoint_Pairs=new ArrayList<String>();//基于DataType匹配的概念对
	ArrayList<String> Based_OP_Pairs=new ArrayList<String>();//基于DataType匹配的概念对
	ArrayList<String> Based_DP_Pairs=new ArrayList<String>();//基于DataType匹配的概念对	
	
	public HeuristicRule_Tools(String classPath,String propertyPath) throws IOException
	{
		//初始化的时候已经全部把字母转换为小写了。
		Match_Class=new TreeMap_Tools(classPath);
		Match_Property=new TreeMap_Tools(propertyPath);
	}
	
	public HeuristicRule_Tools(ArrayList<String> classes,ArrayList<String> properties)
	{
		//初始化的时候已经全部把字母转换为小写了。
		Match_Class=new TreeMap_Tools(classes);
		Match_Property=new TreeMap_Tools(properties);	
	}
	/**
	 * this function is used to calculate the number of pairs satisfied father rule
	 * @param classes1
	 * @param classes2
	 * @param subclasses1
	 * @param subclasses2
	 * @return class1,class2--satisfied number
	 */
	public ArrayList<String> fatherRule(ArrayList<String> classes1, ArrayList<String> classes2,ArrayList<String> superclasses1,ArrayList<String> superclasses2 )
	{
		
		int Map_father_number=0;
		int number_father1=0;
		int number_father2=0;	
		boolean father_flag=false;
		
		TreeMap_Tools Ontology1_super=new TreeMap_Tools(superclasses1);
		TreeMap_Tools Ontology2_super=new TreeMap_Tools(superclasses2);
		//对两个本体中概念的父亲的匹配情况来统计
		for(int i=0;i<classes1.size();i++)
		{
			String concept1=classes1.get(i).toLowerCase();		
			ArrayList<String> Father1=Ontology1_super.GetKey_Value(concept1);//利用孩子来找寻他的父亲	
			
			//if(Father1.size()<=1||Father1==null)	//只有Thing的情况size为1
			if(Father1==null)	//只有Thing的情况size为1
			{
				number_father1=0;
				continue;
			}	
			//这里需要排除一下Thing的情况
			else if(Father1.get(0).equals("Thing")&&Father1.size()==1)	
			{
				System.out.println("Nothing!");	
				System.out.println("Based_father finished!");
				number_father1=0;
				return Based_Father_Pairs;
			}
			else
			{
				number_father1=Father1.size();
			}
			for(int j=0;j<classes2.size();j++)
			{
				String concept2=classes2.get(j).toLowerCase();
				ArrayList<String> Father2=Ontology2_super.GetKey_Value(concept2);//利用孩子来找寻他的父亲
				//System.out.println(Father2==null);	
				//
			   // if(Father2==null||Father2.size()<=1) //只要Thing的情况Size为1
			    if(Father2==null) //只要Thing的情况Size为1
				{
					number_father2=0;
					continue;				
				}	
				//这里需要排除一下Thing的情况
				else if(Father2.get(0).equals("Thing"))	
				{
					number_father2=0;
				}
				else
				{
					number_father2=Father2.size();
				}
				//判断本体1，2中的概念是否已经出现在first-liner中||或者伪根节点（即父亲为"Thing"）
				if(number_father1!=0&&number_father2!=0)
				{
					//因此Chair这个节点没有考虑进来。
					Map_father_number=getMapofFather(Father1,Father2);
				}
				else
				{
					Map_father_number=0;
				}
				if(Map_father_number>0)
				{
					father_flag=true;
					System.out.println(concept1+"与"+concept2+"基于父亲匹配的个数为:"+Map_father_number);
					Based_Father_Pairs.add(concept1+","+concept2+"--"+Map_father_number);
				}
				Map_father_number=0;//保证初始化
			}
		}
		if(father_flag==false)
			System.out.println("Nothing!");	
		System.out.println("Based_father finished!");
		return Based_Father_Pairs;
	}
	
	
	public ArrayList<String> fatherRule2(ArrayList<String> subclasses1,ArrayList<String> subclasses2 )
	{
		
		/*int Map_father_number=0;
		int number_father1=0;
		int number_father2=0;	
		boolean father_flag=false;*/
		
		TreeMap_Tools Ontology1_sub=new TreeMap_Tools(subclasses1);
		TreeMap_Tools Ontology2_sub=new TreeMap_Tools(subclasses2);
		//对两个本体中概念的父亲的匹配情况来统计
		
		ArrayList<String> mapset=Match_Class.GetKey();
		for(int i=0;i<mapset.size();i++)
		{
			String map1=mapset.get(i);
			String map2=Match_Class.GetKey_Value(map1).get(0);
			ArrayList<String> sub1=Ontology1_sub.GetKey_Value(map1);
			ArrayList<String> sub2=Ontology2_sub.GetKey_Value(map2);
			if(sub1!=null&&sub2!=null)
			{
				for(int n=0;n<sub1.size();n++)
				{
					String concetp1=sub1.get(n);
					for(int m=0;m<sub2.size();m++)
					{
						String concept2=sub2.get(m);
						Based_Father_Pairs.add(concetp1+","+concept2+","+1);
					}
				}
			}		
		}
		Based_Father_Pairs=Statistic(Based_Father_Pairs);
		if(Based_Father_Pairs.size()==0)
			System.out.println("Based_father rule has no effect!");	
		/*for(int i=0;i<Based_Father_Pairs.size();i++)
		{
			System.out.println(Based_Father_Pairs.get(i));
		}*/
		System.out.println("Based_father rule was finished!");
		return Based_Father_Pairs;
	}
	
	public  int getMapofFather(ArrayList<String> father1,ArrayList<String> father2)
	{
		//抽取本体1中概念的父亲，然后再用first-liner找到本体2中对应的概念，然后扫描看是否在该概念的父亲
		//同时需要事先确定这个孩子的父亲双方并不是在first-liner出现过！
		//ArrayList<String> map_concept=Match_Class.GetKey();
		HashSet<String> objectSet=new HashSet<String>();//考虑用一个集合来判断
		for(int i=0;i<father2.size();i++)
		{
			objectSet.add(father2.get(i));
		}
		int mapping_number=0;
		for(int i=0;i<father1.size();i++)
		{
			String parent1=father1.get(i);
			ArrayList<String> Map_father=Match_Class.GetValue_Key(parent1);//利用本体1的父亲找到本体2中的父亲
			//其实parent2只有1个
			if(Map_father!=null)
			{
				for(int j=0;j<Map_father.size();j++)
				{
					String parent2=Map_father.get(j);
					if(objectSet.contains(parent2))
					{
						mapping_number++;
						break;
					}				
				}
			}
		}
		return mapping_number;
	}	
	
	/**
	 * this function is used to calculate the number of pairs satisfied children rule
	 * @param classes1
	 * @param classes2
	 * @param subclasses1
	 * @param subclasses2
	 * @return	class1,class2--satisfied number
	 */
	public ArrayList<String> childrenRule(ArrayList<String> classes1, ArrayList<String> classes2,ArrayList<String> subclasses1,ArrayList<String> subclasses2 )
	{
		
		TreeMap_Tools Ontology1_sub=new TreeMap_Tools(subclasses1);
		TreeMap_Tools Ontology2_sub=new TreeMap_Tools(subclasses2);
		int Map_child_number=0;
		int number_children1=0;
		int number_children2=0;	
		boolean children_flag=false;

		//对两个本体中概念的儿子的匹配情况来统计
		for(int i=0;i<classes1.size();i++)
		{
			String concept1=classes1.get(i).toLowerCase();		
			ArrayList<String> Children1=Ontology1_sub.GetKey_Value(concept1);//利用父亲找到他的孩子
			if(Children1==null)	
			{
				number_children1=0;
				continue;
			}			
			else
			{
				number_children1=Children1.size();//统计孩子的个数
			}
			for(int j=0;j<classes2.size();j++)
			{
				String concept2=classes2.get(j).toLowerCase();
				ArrayList<String> Children2=Ontology2_sub.GetKey_Value(concept2);//利用父亲找到所以的孩子
				if(Children2==null)	
				{
					number_children2=0;
					continue;
				}			
				else
				{
					number_children2=Children2.size();//统计孩子的个数
				}
				//判断本体1，2中的概念是否已经出现在first-liner中||或者是叶子节点（即找不到儿子）
				if(number_children1!=0&&number_children2!=0)
				{
					Map_child_number=getMapofChildren(Children1,Children2);
				}
				else
				{
					Map_child_number=0;
				}
				if(Map_child_number>0)
				{
					children_flag=true;				
					//这样计算可以考虑各自子类个数的影响
					//double Map_chilid_value=Map_chilid_number/Math.min(number_children1, number_children2);
					//System.out.println(concept1+"与"+concept2+"基于儿子匹配的值为:"+Map_child_number);
					Based_Children_Pairs.add(concept1+","+concept2+"--"+Map_child_number);
				}
				Map_child_number=0;//保证初始化
			 }
		}
		if(children_flag==false)
			System.out.println("Nothing!");
		System.out.println("Based_children finished!");	
		return Based_Children_Pairs;
	}
	
	public ArrayList<String> childrenRule2(ArrayList<String> superclasses1,ArrayList<String> superclasses2 )
	{
		
		TreeMap_Tools Ontology1_super=new TreeMap_Tools(superclasses1);
		TreeMap_Tools Ontology2_super=new TreeMap_Tools(superclasses2);
		//对两个本体中概念的父亲的匹配情况来统计
		
		ArrayList<String> mapset=Match_Class.GetKey();
		for(int i=0;i<mapset.size();i++)
		{
			String map1=mapset.get(i);
			String map2=Match_Class.GetKey_Value(map1).get(0);
			ArrayList<String> super1=Ontology1_super.GetKey_Value(map1);
			ArrayList<String> super2=Ontology2_super.GetKey_Value(map2);
			if(super1!=null&&super2!=null)
			{
				for(int n=0;n<super1.size();n++)
				{
					String concetp1=super1.get(n);
					for(int m=0;m<super2.size();m++)
					{
						String concept2=super2.get(m);
						Based_Children_Pairs.add(concetp1+","+concept2+","+1);
					}
				}
			}		
		}
		Based_Children_Pairs=Statistic(Based_Children_Pairs);
		if(Based_Children_Pairs.size()==0)
			System.out.println("Based_children rule has no effect!");	
		System.out.println("Based_children rule was finished!");	
		return Based_Children_Pairs;
	}
	
	public int getMapofChildren(ArrayList<String> children1,ArrayList<String> children2)
	{
		//抽取本体1中概念的一个孩子，然后再用first-liner找到本体2中对应的概念，然后扫描看是否在该概念的孩子中
		//同时需要事先确定这个孩子的父亲双方并不是在first-liner出现过！
		//ArrayList<String> map_concept=Match_Class.GetKey();
		int mapping_number=0;
		for(int i=0;i<children1.size();i++)
		{
			String child1=children1.get(i);
			ArrayList<String> Map_child=Match_Class.GetKey_Value(child1);//利用本体1中的儿子找到本体2中的儿子
			//其实child2只有1个
			if(Map_child!=null)
			{
				for(int j=0;j<Map_child.size();j++)
				{
					//children2是一个链表，而child2是一个字符串
					String child2=Map_child.get(j);
					if(children2.contains(child2))
					{
						mapping_number++;
						break;
					}				
				}
			}
		}
		return mapping_number;
	}
	
	public boolean MapBetweenObject(ArrayList<String> object1,ArrayList<String> object2)
	{
		//抽取本体1中概念的一个孩子，然后再用first-liner找到本体2中对应的概念，然后扫描看是否在该概念的孩子中
		//同时需要事先确定这个孩子的父亲双方并不是在first-liner出现过！		
		HashSet<String> objectSet=new HashSet<String>();//考虑用一个集合来判断
		for(int i=0;i<object2.size();i++)
		{
			objectSet.add(object2.get(i));
		}
		for(int i=0;i<object1.size();i++)
		{
			String concept1=object1.get(i);
			ArrayList<String> mapConcept=Match_Class.GetKey_Value(concept1);//利用本体1中的儿子找到本体2中的儿子
			//其实child2只有1个
			if(mapConcept!=null)
			{
				for(int j=0;j<mapConcept.size();j++)
				{
					//children2是一个链表，而child2是一个字符串
					String child2=mapConcept.get(j);
					if(objectSet.contains(child2))//儿子中有匹配的
					{
						return true;
					}				
				}
			}
		}
		return false;
	}
	
	/**
	 * this function is used to calculate the number of pairs satisfied siblings rule
	 * @param classes1
	 * @param classes2
	 * @param siblings1
	 * @param siblings2
	 * @return class1,class2--satisfied number
	 */
	public ArrayList<String> siblingsRule(ArrayList<String> classes1, ArrayList<String> classes2,ArrayList<String> siblings1,ArrayList<String> siblings2 )
	{
		
		//System.out.println(Relation2.contains("reviewWrittenBy"));	
		double Map_Siblings_number=0;
		double number_siblings1=0;
		double number_siblings2=0;	
		boolean Siblings_flag=false;
		
		TreeMap_Tools Ontology1_sibling=new TreeMap_Tools(siblings1);
		TreeMap_Tools Ontology2_sibling=new TreeMap_Tools(siblings2);

		//对两个本体中概念的兄弟姐妹的匹配情况来统计
		for(int i=0;i<classes1.size();i++)
		{
			String concept1=classes1.get(i).toLowerCase();		
			//String concept1="author";
			ArrayList<String> Siblings1=Ontology1_sibling.GetKey_Value(concept1);//利用父亲找到他的孩子
			if(Siblings1==null)	
			{
				number_siblings1=0;
				continue;
			}			
			else
			{
				number_siblings1=Siblings1.size();//统计孩子的个数
			}
			for(int j=0;j<classes2.size();j++)
			{
				String concept2=classes2.get(j).toLowerCase();
				//String concept2="conference_contributor";
				ArrayList<String> Siblings2=Ontology2_sibling.GetKey_Value(concept2);//利用父亲找到所以的孩子
				if(Siblings2==null)	
				{
					number_siblings2=0;
					continue;
				}			
				else
				{
					number_siblings2=Siblings2.size();//统计孩子的个数
				}
				//判断本体1，2中的概念是否已经出现在first-liner中||或者是叶子节点（即找不到儿子）
				if(number_siblings1!=0&&number_siblings2!=0)
				{
					Map_Siblings_number=GetMapofSiblings(Siblings1,Siblings2);
				}
				else
				{
					Map_Siblings_number=0;
				}
				if(Map_Siblings_number>0)
				{
					Siblings_flag=true;				
					//这样计算可以考虑各自子类个数的影响
					//double Map_chilid_value=Map_chilid_number/Math.min(number_children1, number_children2);
					//System.out.println(concept1+"与"+concept2+"基于兄弟姐妹匹配的值为:"+Map_Siblings_number);
					//if(Based_Siblings_Pairs.contains(o))
					Based_Siblings_Pairs.add(concept1+","+concept2+"--"+Map_Siblings_number);
				}
				Map_Siblings_number=0;//保证初始化
			 }
		}
		if(Siblings_flag==false)
			System.out.println("Based_Siblings rule has no effect!!");
		System.out.println("Based_Siblings rule was finished!");	
		return Based_Siblings_Pairs;
	}
	
	public ArrayList<String> siblingsRule2(ArrayList<String> siblings1,ArrayList<String> siblings2 )
	{
		
		TreeMap_Tools Ontology1_sibling=new TreeMap_Tools(siblings1);
		TreeMap_Tools Ontology2_sibling=new TreeMap_Tools(siblings2);
		//对两个本体中概念的父亲的匹配情况来统计
		
		ArrayList<String> mapset=Match_Class.GetKey();
		for(int i=0;i<mapset.size();i++)
		{
			String map1=mapset.get(i);
			String map2=Match_Class.GetKey_Value(map1).get(0);
			ArrayList<String> sibling1=Ontology1_sibling.GetKey_Value(map1);
			ArrayList<String> sibling2=Ontology2_sibling.GetKey_Value(map2);
			if(sibling1!=null&&sibling2!=null)
			{
				for(int n=0;n<sibling1.size();n++)
				{
					String concetp1=sibling1.get(n);
					for(int m=0;m<sibling2.size();m++)
					{
						String concept2=sibling2.get(m);
						Based_Siblings_Pairs.add(concetp1+","+concept2+","+1);
					}
				}
			}		
		}
		Based_Siblings_Pairs=Statistic(Based_Siblings_Pairs);
		if(Based_Siblings_Pairs.size()==0)
			System.out.println("Based_Siblings rule has no effect!");	
		System.out.println("Based_Siblings_Pairs rule was finished!");	
		return Based_Siblings_Pairs;
	}
	
	public double GetMapofSiblings(ArrayList<String> Siblings1,ArrayList<String> Siblings2)
	{
		//抽取本体1中概念的一个孩子，然后再用first-liner找到本体2中对应的概念，然后扫描看是否在该概念的孩子中
		//同时需要事先确定这个孩子的父亲双方并不是在first-liner出现过！
		//ArrayList<String> map_concept=Match_Class.GetKey();
		double mapping_number=0;
		for(int i=0;i<Siblings1.size();i++)
		{
			String siblings1=Siblings1.get(i);
			ArrayList<String> Map_siblings=Match_Class.GetKey_Value(siblings1);//利用本体1中的儿子找到本体2中的儿子
			//其实siblings2只有1个
			if(Map_siblings!=null)
			{
				for(int j=0;j<Map_siblings.size();j++)
				{
					//children2是一个链表，而child2是一个字符串
					String siblings2=Map_siblings.get(j);
					if(Siblings2.contains(siblings2))
					{
						mapping_number++;
						break;
					}				
				}
			}
		}
		return mapping_number;
	}
	
	public ArrayList<String> partOfRule(ArrayList<String> classes1, ArrayList<String> classes2,TreeMap_Tools Ontology1_part,TreeMap_Tools Ontology2_part )
	{
		
		int Map_partOf_number=0;
		int number_part1=0;
		int number_part2=0;	
		boolean father_flag=false;
		
		/*TreeMap_Tools Ontology1_part=new TreeMap_Tools(partOf1);
		TreeMap_Tools Ontology2_part=new TreeMap_Tools(partOf2);*/
		//对两个本体中概念的父亲的匹配情况来统计
		for(int i=0;i<classes1.size();i++)
		{
			String concept1=classes1.get(i).toLowerCase();		
			ArrayList<String> Part1=Ontology1_part.GetKey_Value(concept1);//利用孩子来找寻他的父亲	
			//if(Father1.size()<=1||Father1==null)	//只有Thing的情况size为1
			if(Part1==null)	//只有Thing的情况size为1
			{
				number_part1=0;
				continue;
			}	
			//这里需要排除一下Thing的情况
			/*else if(Father1.get(0).equals("Thing")&&Father1.size()==1)	
			{
				System.out.println("Nothing!");	
				System.out.println("Based_father finished!");
				number_father1=0;
				return Based_Father_Pairs;
			}*/
			else
			{
				number_part1=Part1.size();
			}
			for(int j=0;j<classes2.size();j++)
			{
				String concept2=classes2.get(j).toLowerCase();
				ArrayList<String> Part2=Ontology2_part.GetKey_Value(concept2);//利用孩子来找寻他的父亲
				//System.out.println(Father2==null);	
				//
			   // if(Father2==null||Father2.size()<=1) //只要Thing的情况Size为1
			    if(Part2==null) //只要Thing的情况Size为1
				{
			    	number_part2=0;
					continue;				
				}	
				/*//这里需要排除一下Thing的情况
				else if(Father2.get(0).equals("Thing"))	
				{
					number_father2=0;
				}*/
				else
				{
					number_part2=Part2.size();
				}
				//判断本体1，2中的概念是否已经出现在first-liner中||或者伪根节点（即父亲为"Thing"）
				if(number_part1!=0&&number_part2!=0)
				{
					//因此Chair这个节点没有考虑进来。
					Map_partOf_number=getMapofFather(Part1,Part2);
				}
				/*else
				{
					Map_father_number=0;
				}*/
				if(Map_partOf_number>0)
				{
					father_flag=true;
					//System.out.println(concept1+"与"+concept2+"基于父亲匹配的个数为:"+Map_father_number);
					Based_PartOf_Pairs.add(concept1+","+concept2+"--"+Map_partOf_number);
				}
				Map_partOf_number=0;//保证初始化
			}
		}
		if(father_flag==false)
			System.out.println("Nothing!");	
		System.out.println("Based_partOf finished!");
		return Based_PartOf_Pairs;
	}
	
	public ArrayList<String> hasPartRule2(TreeMap_Tools Ontology1_hasPart,TreeMap_Tools Ontology2_hasPart )
	{
		/*TreeMap_Tools Ontology1_sibling=new TreeMap_Tools(siblings1);
		TreeMap_Tools Ontology2_sibling=new TreeMap_Tools(siblings2);*/
		//对两个本体中概念的父亲的匹配情况来统计
		
		ArrayList<String> mapset=Match_Class.GetKey();
		for(int i=0;i<mapset.size();i++)
		{
			String map1=mapset.get(i);
			String map2=Match_Class.GetKey_Value(map1).get(0);
			ArrayList<String> hasPart1=Ontology1_hasPart.GetKey_Value(map1);
			ArrayList<String> hasPart2=Ontology2_hasPart.GetKey_Value(map2);
			if(hasPart1!=null&&hasPart2!=null)
			{
				for(int n=0;n<hasPart1.size();n++)
				{
					String concetp1=hasPart1.get(n);
					for(int m=0;m<hasPart2.size();m++)
					{
						String concept2=hasPart2.get(m);
						Based_hasPart_Pairs.add(concetp1+","+concept2+","+1);
					}
				}
			}		
		}
		Based_hasPart_Pairs=Statistic(Based_hasPart_Pairs);
		if(Based_hasPart_Pairs.size()==0)
			System.out.println("Based_hasPart rule has no effect!");	
		System.out.println("Based_hasPart rule was finished!");	
		return Based_hasPart_Pairs;
	}
	
	public ArrayList<String> partOfRule2(TreeMap_Tools Ontology1_partOf,TreeMap_Tools Ontology2_partOf )
	{
		/*TreeMap_Tools Ontology1_sibling=new TreeMap_Tools(siblings1);
		TreeMap_Tools Ontology2_sibling=new TreeMap_Tools(siblings2);*/
		//对两个本体中概念的父亲的匹配情况来统计
		
		ArrayList<String> mapset=Match_Class.GetKey();
		for(int i=0;i<mapset.size();i++)
		{
			String map1=mapset.get(i);
			String map2=Match_Class.GetKey_Value(map1).get(0);
			ArrayList<String> partOf1=Ontology1_partOf.GetKey_Value(map1);
			ArrayList<String> partOf2=Ontology2_partOf.GetKey_Value(map2);
			if(partOf1!=null&&partOf2!=null)
			{
				for(int n=0;n<partOf1.size();n++)
				{
					String concetp1=partOf1.get(n);
					for(int m=0;m<partOf2.size();m++)
					{
						String concept2=partOf2.get(m);
						Based_PartOf_Pairs.add(concetp1+","+concept2+","+1);
					}
				}
			}		
		}
		Based_PartOf_Pairs=Statistic(Based_PartOf_Pairs);
		if(Based_PartOf_Pairs.size()==0)
			System.out.println("Based_PartOf rule has no effect!!");	
		System.out.println("Based_PartOf rule was finished!");	
		return Based_PartOf_Pairs;
	}
	
	/**
	 * this function is used to calculate the number of pairs satisfied domain rule
	 * @param classes1
	 * @param classes2
	 * @param relations1
	 * @param relations2
	 * @return class1,class2--satisfied number
	 */
	public ArrayList<String> domainRule(ArrayList<String> classes1, ArrayList<String> classes2,ArrayList<String> relations1,ArrayList<String> relations2 )
	{
		
		
		int Map_Domain_number=0;
		boolean domain_flag=false;
		//System.out.println(Relation2.size());
		//cmt-ekaw edas-ekaw
		//对两个本体中概念的定义域、值域的匹配情况来统计
			for(int i=0;i<relations1.size();i++)
			{
				String triple1=relations1.get(i).toLowerCase();
				String[] part1 = triple1.split(",");
				//标签是一致的，所有很容易定位part1[0]表示Subject part1[2]表示Object
				String Subject1=part1[0];
				String property1=part1[1];
				String Object1=part1[2];			
	/*			String Subject1="Review";
				String property1="writtenBy";
				String Object1="Reviewer";*/						
				for(int j=0;j<relations2.size();j++)
				{
					String triple2=relations2.get(j).toLowerCase();
					String[] part2 = triple2.split(",");
					String Subject2=part2[0];
					String property2=part2[1];
					String Object2=part2[2];		
					//System.out.println(part2[1]);			
					if(Match_Property.has_relation(property1, property2))//间接判定了为空的情况
					{
						//这里从ObjectProperty的角度出发
						//定义域Matching，那么值域概念对匹配的数量+1
						if(classes1.contains(Object1)&&classes2.contains(Object2)&&Match_Class.has_relation(Subject1, Subject2))//标记为概念的范畴
						{	
							//这里其实可以考虑其定义域或者值域的子类，因为其定义范围较广
							Map_Domain_number=1;
							domain_flag=true;
						}				
					}
					if(domain_flag==true)
					{
						//domain_flag=true;
						/*System.out.println(Object1+"与"+Object2+"基于匹配的缘由如下:");	
						System.out.println("属性的匹配对为："+property1+","+property2);
						System.out.println("定义域的匹配对为："+Subject1+","+Subject2);*/
						Based_Domain_Pairs.add(Object1+","+Object2+","+Map_Domain_number);
					}	
					domain_flag=false;
					Map_Domain_number=0;
				}//for
			}
			if(Based_Domain_Pairs.size()==0)
				System.out.println("Based_Domain rule has no effect!");
			System.out.println("Based_Domain rule was finished!");
			//将次数进行统计
			Based_Domain_Pairs=Statistic(Based_Domain_Pairs);
			/*for(int i=0;i<Based_Domain_Pairs.size();i++)
			{
				System.out.println(Based_Domain_Pairs.get(i));
			}*/
			return Based_Domain_Pairs;
	}
	
	/**
	 * this function is used to calculate the number of pairs satisfied range rule
	 * @param classes1
	 * @param classes2
	 * @param relations1
	 * @param relations2
	 * @return classes1,classes2--satisfied number
	 */
	public ArrayList<String> rangeRule(ArrayList<String> classes1, ArrayList<String> classes2,ArrayList<String> relations1,ArrayList<String> relations2 )
	{
		
		
		int Map_Range_number=0;	
		boolean range_flag=false;
		//System.out.println(Relation2.size());
		//cmt-ekaw edas-ekaw
		//对两个本体中概念的定义域、值域的匹配情况来统计
			for(int i=0;i<relations1.size();i++)
			{
				String triple1=relations1.get(i).toLowerCase();
				String[] part1 = triple1.split(",");
				//标签是一致的，所有很容易定位part1[0]表示Subject part1[2]表示Object
				String Subject1=part1[0];
				String property1=part1[1];
				String Object1=part1[2];			
	/*			String Subject1="Review";
				String property1="writtenBy";
				String Object1="Reviewer";*/						
				for(int j=0;j<relations2.size();j++)
				{
					String triple2=relations2.get(j).toLowerCase();
					String[] part2 = triple2.split(",");
					String Subject2=part2[0];
					String property2=part2[1];
					String Object2=part2[2];		
					//System.out.println(part2[1]);			
					if(Match_Property.has_relation(property1, property2))//间接判定了为空的情况
					{
							
						//值域Matching，那么定义域概念对匹配的数量+1
						if(classes1.contains(Subject1)&&classes2.contains(Subject2)&&Match_Class.has_relation(Object1, Object2))
						{
							//这里其实可以考虑其定义域或者值域的子类，因为其定义范围较广
							Map_Range_number=1;
							range_flag=true;
						}
					}
					if(range_flag==true)
					{
						//range_flag=true;
						/*System.out.println(Subject1+"与"+Subject2+"基于匹配的缘由如下:");	
						System.out.println("属性的匹配对为："+property1+","+property2);
						System.out.println("值域的匹配对为："+Object1+","+Object2);*/
						Based_Range_Pairs.add(Subject1+","+Subject2+","+Map_Range_number);
					}
					range_flag=false;
					Map_Range_number=0;
				}//for
			}
			if(Based_Range_Pairs.size()==0)
				System.out.println("Based_Range rule has no effect!!");
			System.out.println("Based_Range rule was finished!");
				
			Based_Range_Pairs=Statistic(Based_Range_Pairs);
		/*	for(int i=0;i<Based_Range_Pairs.size();i++)
			{
				System.out.println(Based_Range_Pairs.get(i));
			}*/		
			return Based_Range_Pairs;
	}
	
	/**
	 * this function is used to calculate the number of pairs satisfied datatype rule
	 * @param classes1
	 * @param classes2
	 * @param relations1
	 * @param relations2
	 * @return classes1,classes2--satisfied number
	 */
	public ArrayList<String> dataTypeRule(ArrayList<String> classes1, ArrayList<String> classes2,ArrayList<String> relations1,ArrayList<String> relations2 )
	{
		
		
		int Map_DataType=0;
		boolean datatype_flag=false;
		//System.out.println(Relation2.size());
		//cmt-ekaw edas-ekaw
		//对两个本体中概念的定义域、值域的匹配情况来统计
			for(int i=0;i<relations1.size();i++)
			{
				String triple1=relations1.get(i).toLowerCase();
				String[] part1 = triple1.split(",");
				//标签是一致的，所有很容易定位part1[0]表示Subject part1[2]表示Object
				String Subject1=part1[0];
				String property1=part1[1];
				String Object1=part1[2];			
	/*			String Subject1="Review";
				String property1="writtenBy";
				String Object1="Reviewer";*/						
				for(int j=0;j<relations2.size();j++)
				{
					String triple2=relations2.get(j).toLowerCase();
					String[] part2 = triple2.split(",");
					String Subject2=part2[0];
					String property2=part2[1];
					String Object2=part2[2];		
					//System.out.println(part2[1]);			
					if(Match_Property.has_relation(property1, property2))//间接判定了为空的情况
					{
						//这里从ObjectProperty的角度出发
						//定义域Matching，那么值域概念对匹配的数量+1
						//这里从DataProperty出发,属性匹配值域的类型相等
						if(Object1.equals(Object2)&&!Object1.equals("null"))
						//if(!classes1.contains(Object1)&&!classes2.contains(Object2)&&Object1.equals(Object2)&&!Object1.equals("null"))
						{
							Map_DataType=1;
							datatype_flag=true;
						}
					}
					if(datatype_flag==true)
					{
						//range_flag=true;
						/*System.out.println(Subject1+"与"+Subject2+"基于匹配的缘由如下:");	
						System.out.println("Datatype属性的匹配对为："+property1+","+property2);
						System.out.println("Datatype value的匹配对为："+Object1+","+Object2);*/
						Based_DataType_Pairs.add(Subject1+","+Subject2+","+Map_DataType);
					}
					datatype_flag=false;
					Map_DataType=0;
				}//for
			}
			if(Based_DataType_Pairs.size()==0)
				System.out.println("Based_Datatype rule has no effect!");
			System.out.println("Based_Datatype rule was finished!");
			//long tic = System.currentTimeMillis();				
			//将次数进行统计
			Based_DataType_Pairs=Statistic(Based_DataType_Pairs);
			//Based_DataType_Pairs=New_Statistic(Based_DataType_Pairs);
			/*for(int i=0;i<Based_DataType_Pairs.size();i++)
			{
				System.out.println(Based_DataType_Pairs.get(i));			
			}*/
			//long toc = System.currentTimeMillis();
			return Based_DataType_Pairs;
	}
	
	public ArrayList<String> objectPropertyRule(ArrayList<String> properties1, ArrayList<String> properties2, ArrayList<String> relations1,ArrayList<String> relations2 )
	{
		
		int Map_number=0;
		boolean OP_flag=false;
		//System.out.println(Relation2.size());
		//cmt-ekaw edas-ekaw
		//对两个本体中概念的定义域、值域的匹配情况来统计
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
				if(Match_Class.has_relation(Subject1, Subject2)&&Match_Class.has_relation(Object1, Object2))//间接判定了为空的情况
				{			
					Map_number=1;
					OP_flag=true;
				}
				if(OP_flag==true)
				{
					//range_flag=true;
				/*	System.out.println(property1+"与"+property2+"基于匹配的缘由如下:");	
					System.out.println("ObjectProperty基于定义域的匹配对为："+Subject1+","+Subject2);
					System.out.println("ObjectProperty基于值域的匹配对为："+Object1+","+Object2);*/
					Based_OP_Pairs.add(property1+","+property2+","+Map_number);
				}
				Map_number=0;
				OP_flag=false;

			}//for
		}
		if(Based_OP_Pairs.size()==0)
			System.out.println("Based_ObjectProperty rule has no effect!");
		System.out.println("Based_ObjectProperty rule was finished!");

		//将次数进行统计
		Based_OP_Pairs=Statistic(Based_OP_Pairs);

		for(int i=0;i<Based_OP_Pairs.size();i++)
		{
			System.out.println(Based_OP_Pairs.get(i));

		}
		return Based_OP_Pairs;
	}
		
	
	/**
	 * this function is used to calculate the number of pairs satisfied datatype rule
	 * @param properties1
	 * @param properties2
	 * @param relations1
	 * @param relations2
	 * @return properties1，properties2，satisfied number
	 */
	public ArrayList<String> dataPropertyRule(ArrayList<String> properties1, ArrayList<String> properties2, ArrayList<String> relations1,ArrayList<String> relations2 )
	{
		
		int Map_number=0;
		boolean DP_flag=false;
		//System.out.println(Relation2.size());
		//cmt-ekaw edas-ekaw
		//对两个本体中概念的定义域、值域的匹配情况来统计
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
				//为了跟objectproperty区别，在Object匹配时进行了限制，更多的是一种type形式的匹配。
				if(Match_Class.has_relation(Subject1, Subject2)&&Object1.equals(Object2)&&!Object1.equals("null"))//间接判定了为空的情况
				//if(Match_Class.has_relation(Subject1, Subject2)&&!Match_Class.has_relation(Object1, Object2)&&!Object1.equals("null")&&Object1.equals(Object2))//间接判定了为空的情况
				{
						Map_number=1;
						DP_flag=true;
				}
				if(DP_flag==true)
				{
					//range_flag=true;
					/*System.out.println(property1+"与"+property2+"基于匹配的缘由如下:");	
					System.out.println("DataProperty基于定义域的匹配对为："+Subject1+","+Subject2);
					System.out.println("DataProperty基于值域的匹配对为："+Object1+","+Object2);*/
					Based_DP_Pairs.add(property1+","+property2+","+Map_number);
				}
				Map_number=0;
				DP_flag=false;			
			}//for
		}
		if(Based_DP_Pairs.size()==0)
			System.out.println("Based_DataProperty rule has no effect!");
		System.out.println("Based_DataProperty was finished!");

		//将次数进行统计
		Based_DP_Pairs=Statistic(Based_DP_Pairs);

		for(int i=0;i<Based_DP_Pairs.size();i++)
		{
			System.out.println(Based_DP_Pairs.get(i));

		}
		return Based_DP_Pairs;
	}
	
/*	public ArrayList<String> Statistic(ArrayList<String> original)
	{
		ArrayList<String> new_pair=new ArrayList<String>();
		ArrayList<String> tokens=new ArrayList<String>();
		for(int i=0;i<original.size();i++)
		{
			String triple = original.get(i);
			String[] part = triple.split(",");
			String pair=part[0]+","+part[1];
			if(!tokens.contains(pair))
				tokens.add(pair);			
		}
		
		double []times=new double[tokens.size()];//这样能得到总共的匹配数
		for(int i=0;i<tokens.size();i++)
		{
			for(int j=0;j<original.size();j++)
			{
				String triple = original.get(j);
				String[] part = triple.split(",");
				String pair=part[0]+","+part[1];
				if(tokens.get(i).equals(pair))
					times[i]++;	
			}
		}	
		
		for(int i=0;i<tokens.size();i++)
		{
			String pair_value=tokens.get(i)+"--"+times[i];
			new_pair.add(pair_value);
		}
		return new_pair;
	}*/
	
	public ArrayList<String> Statistic(ArrayList<String> original)
	{
		ArrayList<String> new_pair=new ArrayList<String>();
		ArrayList<String> tokens=new ArrayList<String>();
		HashMap<String,Integer> tokensMap=new HashMap<String,Integer>();
		//ArrayList<Integer> times=new ArrayList<Integer>();
		int m=0;
		for(int i=0;i<original.size();i++)
		{
			String triple = original.get(i);
			String[] part = triple.split(",");
			String pair=part[0]+","+part[1];
			if(!tokens.contains(pair))
			{
				tokens.add(pair);		
			}		
			if(!tokensMap.containsKey(pair))
			{
				tokensMap.put(part[0]+","+part[1], m++);	
			}				
		}
		
		double []times=new double[tokensMap.size()];//这样能得到总共的匹配数
	    for(int j=0;j<original.size();j++)
	    {
	    	String triple = original.get(j);
	    	String[] part = triple.split(",");
	    	String pair=part[0]+","+part[1];
	    	//int index=findIndex(tokens,pair);
	    	int index=tokensMap.get(pair);
	    	times[index]++;
	    	/*if(tokens.get(i).equals(pair))
	    		times[i]++;	*/
	    }	
	    
		for(int i=0;i<tokens.size();i++)
		{
			String pair_value=tokens.get(i)+"--"+times[i];
			new_pair.add(pair_value);
		}
		return new_pair;
	}
	
	public int findIndex(ArrayList<String> tokens,String index)
	{
		//ArrayList<String> Triples=new ArrayList<String>();
		for(int i=0;i<tokens.size();i++)
		{
			//String parts[]=tokens.get(i).split(",");
			if(index.equals(tokens.get(i)))
			{
				return i;
			}
		}
		return -1;
	}
	
	public ArrayList<String> disjointRule(ArrayList<String> classes1, ArrayList<String> classes2, ArrayList<String> subclasses1, ArrayList<String> subclasses2,
			ArrayList<String> superclasses1, ArrayList<String> superclasses2,ArrayList<String> disjoint1,ArrayList<String> disjoint2)
	/*public ArrayList<String> disjointRule(ArrayList<String> classes1, ArrayList<String> classes2, ArrayList<String> subclasses1, ArrayList<String> subclasses2,
			ArrayList<String> disjoint1,ArrayList<String> disjoint2)  */
	{
		
		TreeMap_Tools Ontology1_sub=new TreeMap_Tools(subclasses1);
		TreeMap_Tools Ontology2_sub=new TreeMap_Tools(subclasses2);
		TreeMap_Tools Ontology1_super=new TreeMap_Tools(superclasses1);
		TreeMap_Tools Ontology2_super=new TreeMap_Tools(superclasses2);
		TreeMap_Tools Ontology1_Disjoint=new TreeMap_Tools(disjoint1);
		TreeMap_Tools Ontology2_Disjoint=new TreeMap_Tools(disjoint2);
		
		int D1=0;//记录满足Disjointwith这种条件的总个数
		int D0=0;//记录满足Disjointwith这种条件的总个数
		int D=0;
			
		char Disjoint='*';//默认是未知的
		for(int i=0;i<classes1.size();i++)
		{
			//String concept1="person";
			String concept1=classes1.get(i);
			ArrayList<String> Children1=Ontology1_sub.GetKey_Value(concept1);
			ArrayList<String> Father1=Ontology1_super.GetKey_Value(concept1);
			ArrayList<String> Disjoint_Class1=Ontology1_Disjoint.GetKey_Value(concept1);
			for(int j=0;j<classes2.size();j++)
			{
				//String concept2="paper";
				String concept2=classes2.get(j);				
				ArrayList<String> Children2=Ontology2_sub.GetKey_Value(concept2);				
				ArrayList<String> Father2=Ontology2_super.GetKey_Value(concept2);				
				ArrayList<String> Disjoint_Class2=Ontology2_Disjoint.GetKey_Value(concept2);
				if(Match_Class.has_relation(concept1, concept2))//如果两个概念本体来就是匹配的，那肯定相交
				{
					Disjoint='0';
					Based_Disjoint_Pairs.add(concept1+","+concept2+","+Disjoint);
					D0++;
				}
				else if(Children1!=null&&Children2!=null&&MapBetweenObject(Children1,Children2)==true) //相交情况的判定
				{
					Disjoint='0';
					Based_Disjoint_Pairs.add(concept1+","+concept2+","+Disjoint);
					D0++;
				}
				//这里说明的是：本体1中概念Y1的父类X1与本体2中的概念X2匹配且X2与Y2不相交，则Y1与Y2不相交
				else if(Disjoint_Class2!=null&&Father1!=null&&MapBetweenObject(Father1,Disjoint_Class2)==true)
				{
					Disjoint='1';
					Based_Disjoint_Pairs.add(concept1+","+concept2+","+Disjoint);
					D1++;				
				}
				//这里说明的是：本体1中概念Y2的父类X2与本体1中的概念X1匹配且X1与Y1不相交，则Y1与Y2不相交
				else if(Disjoint_Class1!=null&&Father2!=null&&MapBetweenObject(Father2,Disjoint_Class1)==true)
				{	
					Disjoint='1';
					Based_Disjoint_Pairs.add(concept1+","+concept2+","+Disjoint);
					D1++;	
				}
				else//开放世界假设的情况
				{
					Disjoint='*';
					Based_Disjoint_Pairs.add(concept1+","+concept2+","+Disjoint);
					D++;
					//bfw_Disjoint_Similarities.append(Disjoint+"\n");
				}
				//下一轮计算之前初始化
				Disjoint='*';
			}			
		}
		
		/*System.out.println("发现不相交概念对的个数为:"+D1);
		System.out.println("发现相交概念对的个数为:"+D0);
		System.out.println("无法判断的个数为:"+D);*/
		if(Based_Disjoint_Pairs.size()==0)
			System.out.println("Based_Disjoint rule has no effect!");
		System.out.println("Based_Disjoint rule was finished!");
		return Based_Disjoint_Pairs;
	}
	
	public ArrayList<String> New_disjointRule(ArrayList<String> classes1, ArrayList<String> classes2, ArrayList<String> subclasses1, ArrayList<String> subclasses2,
			ArrayList<String> superclasses1, ArrayList<String> superclasses2,ArrayList<String> disjoint1,ArrayList<String> disjoint2)
	/*public ArrayList<String> disjointRule(ArrayList<String> classes1, ArrayList<String> classes2, ArrayList<String> subclasses1, ArrayList<String> subclasses2,
			ArrayList<String> disjoint1,ArrayList<String> disjoint2)  */
	{
		ArrayList<String> Based_Disjoint_Pairs=new ArrayList<String>();//基于DataType匹配的概念对
		TreeMap_Tools Ontology1_sub=new TreeMap_Tools(subclasses1);
		TreeMap_Tools Ontology2_sub=new TreeMap_Tools(subclasses2);
		TreeMap_Tools Ontology1_super=new TreeMap_Tools(superclasses1);
		TreeMap_Tools Ontology2_super=new TreeMap_Tools(superclasses2);
		TreeMap_Tools Ontology1_Disjoint=new TreeMap_Tools(disjoint1);
		TreeMap_Tools Ontology2_Disjoint=new TreeMap_Tools(disjoint2);
		
		int D1=0;//记录满足Disjointwith这种条件的总个数
		int D0=0;//记录满足Disjointwith这种条件的总个数
		int D=0;
			
		char Disjoint='*';//默认是未知的
		for(int i=0;i<classes1.size();i++)
		{
			//String concept1="person";
			String concept1=classes1.get(i);
			ArrayList<String> Children1=Ontology1_sub.GetKey_Value(concept1);
			ArrayList<String> Father1=Ontology1_super.GetKey_Value(concept1);
			Father1.add(concept1);//其实也可以顺带把自己带进去判断
			ArrayList<String> Disjoint_Class1=Ontology1_Disjoint.GetKey_Value(concept1);
			for(int j=0;j<classes2.size();j++)
			{
				//String concept2="paper";
				String concept2=classes2.get(j);				
				ArrayList<String> Children2=Ontology2_sub.GetKey_Value(concept2);				
				ArrayList<String> Father2=Ontology2_super.GetKey_Value(concept2);
				try
				{
					Father2.add(concept2);//其实也可以顺带把自己带进去判断
				}
				catch(Exception e)
				{
					System.out.println();
				}
				ArrayList<String> Disjoint_Class2=Ontology2_Disjoint.GetKey_Value(concept2);
				if(Match_Class.has_relation(concept1, concept2))//如果两个概念本体来就是匹配的，那肯定相交
				{
					Disjoint='0';
					Based_Disjoint_Pairs.add(concept1+","+concept2+","+Disjoint);
					D0++;
				}
				else if(Children1!=null&&Children2!=null&&MapBetweenObject(Children1,Children2)==true) //相交情况的判定
				{
					Disjoint='0';
					Based_Disjoint_Pairs.add(concept1+","+concept2+","+Disjoint);
					D0++;
				}
				//这里说明的是：本体1中概念Y1的父类X1与本体2中的概念X2匹配且X2与Y2不相交，则Y1与Y2不相交
				else if(Disjoint_Class2!=null&&MapBetweenObject(Father1,Disjoint_Class2)==true)
				{
					Disjoint='1';
					Based_Disjoint_Pairs.add(concept1+","+concept2+","+Disjoint);
					D1++;				
				}
				//这里说明的是：本体1中概念Y2的父类X2与本体1中的概念X1匹配且X1与Y1不相交，则Y1与Y2不相交
				else if(Disjoint_Class1!=null&&MapBetweenObject(Father2,Disjoint_Class1)==true)
				{	
					Disjoint='1';
					Based_Disjoint_Pairs.add(concept1+","+concept2+","+Disjoint);
					D1++;	
				}
				else//开放世界假设的情况
				{
					Disjoint='*';
					Based_Disjoint_Pairs.add(concept1+","+concept2+","+Disjoint);
					D++;
					//bfw_Disjoint_Similarities.append(Disjoint+"\n");
				}
				//下一轮计算之前初始化
				Disjoint='*';
			}			
		}
		
		System.out.println("发现不相交概念对的个数为:"+D1);
		System.out.println("发现相交概念对的个数为:"+D0);
		System.out.println("无法判断的个数为:"+D);
		if(Based_Disjoint_Pairs.size()==0)
			System.out.println("Nothing!");
		System.out.println("Based_Disjoint finished!");
		return Based_Disjoint_Pairs;
	}
	
	/*
	 * refresh the Maps in each iteration
	 */
	public void refreshClassMaps(ArrayList<String> newClassMaps)
	{
		Match_Class.clear();
		Match_Class=new TreeMap_Tools(newClassMaps);
		//Match_Property=new TreeMap_Tools(newPropertyMaps);
	}
	public void refreshAllMaps(ArrayList<String> newClassMaps,ArrayList<String> newPropertyMaps)
	{
		Match_Class.clear();
		Match_Property.clear();
		Match_Class=new TreeMap_Tools(newClassMaps);
		Match_Property=new TreeMap_Tools(newPropertyMaps);
		Based_Father_Pairs.clear();//基于父亲匹配的概念对		
		Based_Children_Pairs.clear();//基于儿子匹配的概念对
		Based_Siblings_Pairs.clear();//基于兄弟姐妹匹配的概念对
		Based_hasPart_Pairs.clear();
		Based_PartOf_Pairs.clear();//基于partOf的匹配对
		Based_Domain_Pairs.clear();//基于定义域匹配的概念对
		Based_Range_Pairs.clear();//基于值域匹配的概念对
		Based_DataType_Pairs.clear();//基于DataType匹配的概念对
		Based_Disjoint_Pairs.clear();//基于Disjoint匹配的概念对
		Based_OP_Pairs.clear();//基于ObjectProperty匹配的概念对
		Based_DP_Pairs.clear();//基于DataProperty匹配的概念对	
	}
}
