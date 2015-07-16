package SPNs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;


public class GraphSPN {
	
	/*public enum Node_Type
	{
		leaf, Sum, Product;
	}
	Node_Type type;*/
	
	double root_value=0;
	double MAP_value=0;
	public HashMap<Integer, Node> MAP = new HashMap<Integer, Node>();//存储所有结点的值
	ArrayList<ListLeaf<Node>> LeafSet= new ArrayList<ListLeaf<Node>>();
	
	public void initial()
	{
		root_value=0;
		MAP_value=0;
		for(int i=0;i<LeafSet.size();i++)//将LeafSet初始化
		{
			ListLeaf<Node> leaf=LeafSet.get(i);
			leaf.Max_index=-1;
			leaf.Max_name="";
			leaf.assigned=false;
		}
		for(int i=0;i<MAP.size();i++)//关系不需要初始化，只要将值初始化就可以了
		{
			Node node=MAP.get(i);
			if(node.type.equals("product"))
			{
				node.value=0;
				node.calculated=false;
				node.MAP_value=0;			
			}
			else if(node.type.equals("sum"))
			{
				node.value=0;
				node.calculated=false;
				node.MAP_value=0;
				((SumNode)node).Max_index=-1;
			}
			else if(node.type.equals("leaf"))
			{
				//node.index=0;
				node.value=0;
				node.calculated=false;
				node.MAP_value=0;
						
			}		
		}
	}
	
	public double get_root_value()//根结点为MAP中的最后一个结点
	{
		int num=MAP.size();
		return MAP.get(num-1).value;
	}
	
	public double get_root_MAP_value()//根结点为MAP中的最后一个结点
	{
		int num=MAP.size();
		return MAP.get(num-1).MAP_value;
	}
	
	public boolean isValid()
	{
		boolean complete=isComplete();
		boolean consistent=isConsistent();
		boolean normalized=isNormalized();
		if(complete&&consistent&&normalized)	
		{
			System.out.println("this SPN is valid.");	
			return true;
		}
		else 
		{
			System.out.println(" so it is invalid.");
			return false;
		}
			
	}
	
	public boolean isComplete()
	{
		for(int i=0;i<MAP.size();i++)
		{
			if(MAP.get(i).type.equals("sum"))
			{
				Node a=MAP.get(i);
				int child_num=((SumNode)a).sum_c.size();
				for(int j=0;j<child_num;j++)//遍历它的所有儿子，看有木有与之范围不等的情况
				{
					int child_index=((SumNode)a).sum_c.get(j).index;
					Node b=MAP.get(child_index);
					if(a.scope.size()==b.scope.size())
					{
						for(int k=0;k<a.scope.size();k++)
						{
							if(a.scope.get(k).equals(b.scope.get(k)))
								continue;
							else
							{
								System.out.println("please check scope of node "+a.name +" and scope of node "+b.name+".");
								System.out.print("this SPN is incomplete,");
								return false;
							}
						}
					}
					else
					{
						System.out.println("please check scope of node "+a.name +" and scope of node "+b.name+".");
						System.out.print("this SPN is incomplete,");
						return false;
					}
				}
			}
		}
		return true;
	}
	
	public boolean isConsistent()
	{
		for(int i=0;i<MAP.size();i++)
		{
			if(MAP.get(i).type.equals("product"))
			{
				Node a=MAP.get(i);
				int child_num=((ProdNode)a).prod_c.size();
				ArrayList<String> child_scope=new ArrayList<String>();
				for(int j=0;j<child_num;j++)//遍历它的所有儿子，将所有的范围加入链表，看有没有，链表包含其中元素的情况
				{
					int child_index=((ProdNode)a).prod_c.get(j).index;
					Node b=MAP.get(child_index);
					for(int k=0;k<b.scope.size();k++)
					{
						if(child_scope.contains(b.scope.get(k)))
						{
							System.out.println("please check scope of node "+a.name +" and scope of node "+b.name+".");
							System.out.print("this SPN is inconsistent,");
							return false;
						}
						else
							child_scope.add(b.scope.get(k));					
					}
				}
				child_scope.clear();
			}
		}	
		return true;
	}
	
	public boolean isNormalized()
	{
		for(int i=0;i<MAP.size();i++)
		{
			if(MAP.get(i).type.equals("sum"))
			{
				Node a=MAP.get(i);
				int child_num=((SumNode)a).sum_c.size();
				int weight_num=((SumNode)a).sum_w.size();
				if(child_num==weight_num)
				{
					double sum_weight=0;
					for(int j=0;j<weight_num;j++)//遍历它的所有儿子，看有木有与之范围不等的情况
					{
						sum_weight=sum_weight+((SumNode)a).sum_w.get(j);
					}
					if(sum_weight==1.0)
						continue;
					else
					{
						System.out.println("please check weight of node "+a.name+".");
						System.out.print("this SPN is unnormalized,");
						return false;
					}
				}
				else
				{
					System.out.println("please check node "+a.name +" . its weight and its children is unbalance!");
					System.out.print("this SPN is unnormalized,");
					return false;
				}
			}
			
		}
		
		return true;
	}
	
	
	public void bottom_up(HashMap<String, Integer[]> Assignments)//类似与边缘推理
	{
		//Assignments.get("")
		/*Set<String> Variables=Assignments.keySet();
		for(String s:Variables)
		{
			Assignments.get(s);
		}*/	
		/*Iterator<String> a=Variables.iterator();
		a.next();*/
		
		Iterator<Entry <String, Integer[]>> Assign= Assignments.entrySet().iterator(); 
		while(Assign.hasNext())
		{
			Entry <String, Integer[]> assign=Assign.next();
			//获取具体的变量
			String var=assign.getKey();
			ListLeaf<Node> n=find_LeafVariable(var);
			//获取变量中的指派
			int times=0;
			for(int i=0;i<n.size();i++)
			{
				((Node)n.get(i)).value=assign.getValue()[i]; //这样就可以将赋值进行传递
				((Node)n.get(i)).MAP_value=assign.getValue()[i]; //这样就可以将赋值进行传递
				((Node)n.get(i)).calculated=true;
				if(((Node)n.get(i)).value==1)
				{
					n.Max_index=((Node)n.get(i)).index;
					n.Max_name=((Node)n.get(i)).name;
					n.assigned=true;
					times++;
				}
			}	
			if(times>1)//因为这些结点是互斥的关系，都为1只可能是缺省的时候
			{
				n.Max_index=-1;
				n.Max_name="";
				n.assigned=false;
			}
		}
		boolean ALL_calculated=false;
		//int calculated_num=0;
		while(true)
		{
			if(ALL_calculated==true)
				break;
			else
			{
				for(int k=0;k<MAP.size();k++)
				{
					if(MAP.get(k).calculated==true)
						continue;
					else if(MAP.get(k).calculated==false&&MAP.get(k).type.equals("product")&&((ProdNode)MAP.get(k)).isCalculated())
					{
						((ProdNode)MAP.get(k)).process();
						//MAP.get(k).calculated=true;				
					}	
					else if(MAP.get(k).calculated==false&&MAP.get(k).type.equals("sum")&&((SumNode)MAP.get(k)).isCalculated())
					{
						((SumNode)MAP.get(k)).process();
						//MAP.get(k).calculated=true;
					}	
						
				}
				ALL_calculated=ALLisCalculated();
			}	
		}
		
		//这里将值进行存储时，就已经将每一个节点的值，SUM结点的(MAP_value,MAP_index)Product结点的(Value)
	}
	
	public ArrayList<String> find_MAP()//类似与MAP的方法
	{
		ArrayList<String> assignmentM=new ArrayList<String>();
		boolean flag=false;
		int num=MAP.size()-1;
		Queue<Integer> order = new LinkedList<Integer>();
		order.offer(num);
		//System.out.print(order.isEmpty());
		while(!order.isEmpty())
		{		
			int clue=order.remove();
			Node clue_Node=MAP.get(clue);
			if(clue_Node.type.equals("product"))
			{
				int clue_num=((ProdNode)clue_Node).prod_c.size();
				for(int i=0;i<clue_num;i++)
				{
					//int sub_clue=0;
					int sub_clue=(((ProdNode)clue_Node).prod_c.get(i)).index;
					order.add(sub_clue);
				}
			}
			else if(clue_Node.type.equals("sum"))
			{
				order.add(((SumNode)clue_Node).Max_index);
			}
			else if(clue_Node.type.equals("leaf"))
			{
				String var=clue_Node.name;
				ListLeaf<Node> n=find_LeafVariable(var);
				if(n.assigned)
					continue;
				else 
				{
					flag=true;
					System.out.printf("the max assignment of "+var.charAt(0)+" is "+clue_Node.name);
					assignmentM.add(clue_Node.name);
					System.out.println();
					//将其赋值
				}
			}		
		}
		if(flag==false)
		{
			System.out.println("All the variables have been assigned.");
		}
		return assignmentM;
	}	
	
	public void buildSPN(ArrayList<String> Node_information,ArrayList<String> Edge_information)
	{
		buildSPN_Node(Node_information);
		bulidSPN_edge(Edge_information);
		System.out.println("the SPN has been build!");
	}
	
	public void buildSPN_Node(ArrayList<String> Node_information)
	{
		for(int i=0;i<Node_information.size();i++)
		{
			String []information=Node_information.get(i).split("--");
			String type=information[1];
				
			if(type.equals("leaf"))
			{
				Node a=new Node();
				a.type=information[1];
				a.name=information[0];
				a.index=i;
				a.value=0;	
				a.scope.add(String.valueOf(a.name.charAt(0)));
				MAP.put(i, a);
				boolean flag=false;
				char var=a.name.charAt(0);//判断是否属于同一个变量
				for(int k=0;k<LeafSet.size();k++)
				{
					if(LeafSet.get(k).Variable==var)
					{
						LeafSet.get(k).add(a);//只需要添加到集合中，变量不需要改变，因为是一致的
						flag=true;
						break;
					}
				}
				if(flag==false)
				{
					ListLeaf<Node> Leaves=new ListLeaf<Node>();
					Leaves.add(a);
					Leaves.Variable=var;
					LeafSet.add(Leaves);
				}
			}	
			else if(type.equals("sum"))
			{
				SumNode b=new SumNode();
				b.type=information[1];
				b.name=information[0];
				b.index=i;
				b.value=0;
				MAP.put(i, b);//将结点加入MAP表中(最后一个结点为Root结点)
			}
			else if(type.equals("product"))
			{
				ProdNode c=new ProdNode();
				c.type=information[1];
				c.name=information[0];
				c.index=i;
				c.value=0;
				MAP.put(i, c);//将结点加入MAP表中(最后一个结点为Root结点)
			}
		}
		System.out.println("the nodes of SPN have been creates!");
	}
	
	public void bulidSPN_edge(ArrayList<String> Edge_information)
	{
		for(int i=0;i<Edge_information.size();i++)
		{
			String []information=Edge_information.get(i).split("--");
			String Child_name=information[0];
			String []Father_informations=information[1].split(",");//可能不止一个父亲
			Node Child=search_Node(Child_name);
			Child.Fathers=new Node[Father_informations.length];
			for(int j=0;j<Father_informations.length;j++)
			{
				String Father_name=Father_informations[j].split("#")[0];
				Node Father=search_Node(Father_name);
				double weight=Double.parseDouble(Father_informations[j].split("#")[1]);
				Child.Fathers[j]=Father;
				if(Father.type.equals("product"))//如果父亲是Prod_Node，只需要对将该结点的儿子进行指派
				{
					((ProdNode)Father).prod_c.add(Child);
					for(int s=0;s<Child.scope.size();s++)
					{
						Father.scope.add(Child.scope.get(s));
					}
				}
				else//如果父亲是Sum_Node，除了对将该结点的儿子进行指派，并要将相关的权值进行存储
				{
					((SumNode)Father).sum_c.add(Child);
					((SumNode)Father).sum_w.add(weight);				
					for(int s=0;s<Child.scope.size();s++)
					{
						if(Father.scope.contains(Child.scope.get(s)))
							continue;
						else
							Father.scope.add(Child.scope.get(s));
					}		
				}
			}	
		}
		System.out.println("the edges of SPN have been build!");
	}
	
	public Node search_Node(String name)
	{
		//Node a=new Node();
		//boolean flag=false;
		for(int i=0;i<MAP.size();i++)//查找name下面对应的结点
		{
			if(name.equals(MAP.get(i).name))
			{
				if(MAP.get(i).type.equals("leaf"))
				{
					Node a=new Node();
					a=MAP.get(i);
					return a;
				}
				else if(MAP.get(i).type.equals("sum"))
				{
					SumNode a=new SumNode();
					a=(SumNode)MAP.get(i);
					return a;
				}
				else if(MAP.get(i).type.equals("product"))
				{
					ProdNode a=new ProdNode();
					a=(ProdNode)MAP.get(i);
					return a;
				}		
			}				
		}
	
			System.out.println("无法找到"+name+"的父亲！");
		
		return null;
	}
	
	public ListLeaf<Node> find_LeafVariable(String var)
	{
		ListLeaf<Node> a=new ListLeaf<Node>();
		for(int i=0;i<LeafSet.size();i++)
		{
			if(LeafSet.get(i).Variable==var.charAt(0))
			{
				a=LeafSet.get(i);//只需要添加到集合中，变量不需要改变，因为是一致的
				return a;
			}
		}
		return a;
	}
	
	public boolean ALLisCalculated()//判断是否所有的儿子结点都可以计算
	{
		int right_num=0;
		for(int i=0;i<MAP.size();i++)
		{
			if(MAP.get(i).calculated==true)
				right_num++;				
		}
		if(right_num==MAP.size())
			return true;
		else
			return false;
	}
}
