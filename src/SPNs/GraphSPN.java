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
	public HashMap<Integer, Node> MAP = new HashMap<Integer, Node>();//�洢���н���ֵ
	ArrayList<ListLeaf<Node>> LeafSet= new ArrayList<ListLeaf<Node>>();
	
	public void initial()
	{
		root_value=0;
		MAP_value=0;
		for(int i=0;i<LeafSet.size();i++)//��LeafSet��ʼ��
		{
			ListLeaf<Node> leaf=LeafSet.get(i);
			leaf.Max_index=-1;
			leaf.Max_name="";
			leaf.assigned=false;
		}
		for(int i=0;i<MAP.size();i++)//��ϵ����Ҫ��ʼ����ֻҪ��ֵ��ʼ���Ϳ�����
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
	
	public double get_root_value()//�����ΪMAP�е����һ�����
	{
		int num=MAP.size();
		return MAP.get(num-1).value;
	}
	
	public double get_root_MAP_value()//�����ΪMAP�е����һ�����
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
				for(int j=0;j<child_num;j++)//�����������ж��ӣ�����ľ����֮��Χ���ȵ����
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
				for(int j=0;j<child_num;j++)//�����������ж��ӣ������еķ�Χ������������û�У������������Ԫ�ص����
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
					for(int j=0;j<weight_num;j++)//�����������ж��ӣ�����ľ����֮��Χ���ȵ����
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
	
	
	public void bottom_up(HashMap<String, Integer[]> Assignments)//�������Ե����
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
			//��ȡ����ı���
			String var=assign.getKey();
			ListLeaf<Node> n=find_LeafVariable(var);
			//��ȡ�����е�ָ��
			int times=0;
			for(int i=0;i<n.size();i++)
			{
				((Node)n.get(i)).value=assign.getValue()[i]; //�����Ϳ��Խ���ֵ���д���
				((Node)n.get(i)).MAP_value=assign.getValue()[i]; //�����Ϳ��Խ���ֵ���д���
				((Node)n.get(i)).calculated=true;
				if(((Node)n.get(i)).value==1)
				{
					n.Max_index=((Node)n.get(i)).index;
					n.Max_name=((Node)n.get(i)).name;
					n.assigned=true;
					times++;
				}
			}	
			if(times>1)//��Ϊ��Щ����ǻ���Ĺ�ϵ����Ϊ1ֻ������ȱʡ��ʱ��
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
		
		//���ｫֵ���д洢ʱ�����Ѿ���ÿһ���ڵ��ֵ��SUM����(MAP_value,MAP_index)Product����(Value)
	}
	
	public ArrayList<String> find_MAP()//������MAP�ķ���
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
					//���丳ֵ
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
				char var=a.name.charAt(0);//�ж��Ƿ�����ͬһ������
				for(int k=0;k<LeafSet.size();k++)
				{
					if(LeafSet.get(k).Variable==var)
					{
						LeafSet.get(k).add(a);//ֻ��Ҫ��ӵ������У���������Ҫ�ı䣬��Ϊ��һ�µ�
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
				MAP.put(i, b);//��������MAP����(���һ�����ΪRoot���)
			}
			else if(type.equals("product"))
			{
				ProdNode c=new ProdNode();
				c.type=information[1];
				c.name=information[0];
				c.index=i;
				c.value=0;
				MAP.put(i, c);//��������MAP����(���һ�����ΪRoot���)
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
			String []Father_informations=information[1].split(",");//���ܲ�ֹһ������
			Node Child=search_Node(Child_name);
			Child.Fathers=new Node[Father_informations.length];
			for(int j=0;j<Father_informations.length;j++)
			{
				String Father_name=Father_informations[j].split("#")[0];
				Node Father=search_Node(Father_name);
				double weight=Double.parseDouble(Father_informations[j].split("#")[1]);
				Child.Fathers[j]=Father;
				if(Father.type.equals("product"))//���������Prod_Node��ֻ��Ҫ�Խ��ý��Ķ��ӽ���ָ��
				{
					((ProdNode)Father).prod_c.add(Child);
					for(int s=0;s<Child.scope.size();s++)
					{
						Father.scope.add(Child.scope.get(s));
					}
				}
				else//���������Sum_Node�����˶Խ��ý��Ķ��ӽ���ָ�ɣ���Ҫ����ص�Ȩֵ���д洢
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
		for(int i=0;i<MAP.size();i++)//����name�����Ӧ�Ľ��
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
	
			System.out.println("�޷��ҵ�"+name+"�ĸ��ף�");
		
		return null;
	}
	
	public ListLeaf<Node> find_LeafVariable(String var)
	{
		ListLeaf<Node> a=new ListLeaf<Node>();
		for(int i=0;i<LeafSet.size();i++)
		{
			if(LeafSet.get(i).Variable==var.charAt(0))
			{
				a=LeafSet.get(i);//ֻ��Ҫ��ӵ������У���������Ҫ�ı䣬��Ϊ��һ�µ�
				return a;
			}
		}
		return a;
	}
	
	public boolean ALLisCalculated()//�ж��Ƿ����еĶ��ӽ�㶼���Լ���
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
