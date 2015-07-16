package SPNs;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class Main {
	/*static double[] D = {0.6,0.4};
	static double[] M_I1_D0 = {0.6,0.1,0.3};//I=1=>M=1
	static double[] M_I0_D0 = {0.1,0.5,0.4};//I=0=>M=0
	static double[] M_IUnknown_D0 = {0.2,0.3,0.5};//I=unknown=>M=unknown
	static double[] I_1_2_3_D0 = {0.35,0.2,0.45};//D=0=>I=unknown
	static double[] I_D1 = {0.05,0.8,0.15};//D=1=>I=0
	static double[] M_D1 = {0.05,0.7,0.25};//D=1=>M=0*/
	//针对开放世界假设（D0=1，D1=1）
	//当I=1=>M=1,D=0  当I=0=>M=1,D=1,当I=unknown根据概率分布表，可以得到M=unknown,这是由D=0这个分支得到的!
	//叶子节点需要提前给定，取值根据具体指派来取0和1。
	//节点直接的关联也需要提前给定，因为这并不是按层给出的
	
	
	public static void main(String args[]) throws IOException 
	{
		
		String Read_Path ="Datasets/SPN_data";
		
		BufferedReader Nodes = new BufferedReader(new FileReader(new File(Read_Path + "/Nodes_O.txt")));
		ArrayList<String> SPN_Node_informations = new ArrayList<String>();
		String lineTxt = null;
		while ((lineTxt = Nodes.readLine()) != null)
		{
			String line = lineTxt.trim(); // 去掉字符串首位的空格，避免其空格造成的错误
			SPN_Node_informations.add(line);
		}
		
		BufferedReader Edges = new BufferedReader(new FileReader(new File(Read_Path + "/Edges_O.txt")));
		ArrayList<String> SPN_Edge_informations = new ArrayList<String>();
		lineTxt = null;
		while ((lineTxt = Edges.readLine()) != null)
		{
			String line = lineTxt.trim(); // 去掉字符串首位的空格，避免其空格造成的错误
			SPN_Edge_informations.add(line);
		}
	
		GraphSPN spn=new GraphSPN();
		System.out.println("=====================================");
		spn.buildSPN(SPN_Node_informations, SPN_Edge_informations);
		
		BufferedReader Evidences = new BufferedReader(new FileReader(new File(Read_Path + "/Assignments_O.txt")));
		ArrayList<String> SPN_Evidences = new ArrayList<String>();
		lineTxt = null;
		while ((lineTxt = Evidences.readLine()) != null)
		{
			String line = lineTxt.trim(); // 去掉字符串首位的空格，避免其空格造成的错误
			SPN_Evidences.add(line);
		}
		
		HashMap<String, Integer[]> Assignments=new HashMap<String, Integer[]>();
		for(int i=0;i<SPN_Evidences.size();i++)
		{
			String []Var_Value=SPN_Evidences.get(i).split("--");
			String var=Var_Value[0];
			String Value[]=Var_Value[1].split(",");
			Integer Val[]=new Integer[Value.length];
			for(int j=0;j<Value.length;j++)
			{
				Val[j]=Integer.parseInt(Value[j]);
			}
			Assignments.put(var, Val);
		}
		
		System.out.println("=====================================");	
		boolean valid=spn.isValid();
		System.out.println("=====================================");	
		if(valid)
		{
			spn.bottom_up(Assignments);
			System.out.println("Given the Evidence:");
			for(int i=0;i<SPN_Evidences.size();i++)
			{
				System.out.println(SPN_Evidences.get(i));
			}	
			/*Iterator<Entry <String, Integer[]>> Assign= Assignments.entrySet().iterator(); 
			while(Assign.hasNext())
			{
				Entry <String, Integer[]> assign=Assign.next();
				//获取具体的变量
				String var=assign.getKey();		
				String value="";
				for(int i=0;i<Assignments.get(var).length;i++)
					value=value+" "+Assignments.get(var)[i];
				System.out.println(var+"--"+value);
			}*/
			//System.out.println(Assignments);
			System.out.println("the probability is "+spn.get_root_value());
			System.out.println("the MAP is "+spn.get_root_MAP_value());	
			spn.find_MAP();
		}
		else
		{
			System.out.println("please make the SPN valid!");	
		}
		
		spn.initial();
		Evidences = new BufferedReader(new FileReader(new File(Read_Path + "/Assignments_O1.txt")));
		SPN_Evidences = new ArrayList<String>();
		lineTxt = null;
		while ((lineTxt = Evidences.readLine()) != null)
		{
			String line = lineTxt.trim(); // 去掉字符串首位的空格，避免其空格造成的错误
			SPN_Evidences.add(line);
		}
		
		Assignments=new HashMap<String, Integer[]>();
		for(int i=0;i<SPN_Evidences.size();i++)
		{
			String []Var_Value=SPN_Evidences.get(i).split("--");
			String var=Var_Value[0];
			String Value[]=Var_Value[1].split(",");
			Integer Val[]=new Integer[Value.length];
			for(int j=0;j<Value.length;j++)
			{
				Val[j]=Integer.parseInt(Value[j]);
			}
			Assignments.put(var, Val);
		}
		
		System.out.println("=====================================");	
		valid=spn.isValid();
		System.out.println("=====================================");	
		if(valid)
		{
			spn.bottom_up(Assignments);
			System.out.println("Given the Evidence:");
			for(int i=0;i<SPN_Evidences.size();i++)
			{
				System.out.println(SPN_Evidences.get(i));
			}	
			/*Iterator<Entry <String, Integer[]>> Assign= Assignments.entrySet().iterator(); 
			while(Assign.hasNext())
			{
				Entry <String, Integer[]> assign=Assign.next();
				//获取具体的变量
				String var=assign.getKey();		
				String value="";
				for(int i=0;i<Assignments.get(var).length;i++)
					value=value+" "+Assignments.get(var)[i];
				System.out.println(var+"--"+value);
			}*/
			//System.out.println(Assignments);
			System.out.println("the probability is "+spn.get_root_value());
			System.out.println("the MAP is "+spn.get_root_MAP_value());	
			spn.find_MAP();
		}
		else
		{
			System.out.println("please make the SPN valid!");	
		}
		
		spn.initial();
		Evidences = new BufferedReader(new FileReader(new File(Read_Path + "/Assignments_O2.txt")));
		SPN_Evidences = new ArrayList<String>();
		lineTxt = null;
		while ((lineTxt = Evidences.readLine()) != null)
		{
			String line = lineTxt.trim(); // 去掉字符串首位的空格，避免其空格造成的错误
			SPN_Evidences.add(line);
		}
		
		Assignments=new HashMap<String, Integer[]>();
		for(int i=0;i<SPN_Evidences.size();i++)
		{
			String []Var_Value=SPN_Evidences.get(i).split("--");
			String var=Var_Value[0];
			String Value[]=Var_Value[1].split(",");
			Integer Val[]=new Integer[Value.length];
			for(int j=0;j<Value.length;j++)
			{
				Val[j]=Integer.parseInt(Value[j]);
			}
			Assignments.put(var, Val);
		}
		
		System.out.println("=====================================");	
		valid=spn.isValid();
		System.out.println("=====================================");	
		if(valid)
		{
			spn.bottom_up(Assignments);
			System.out.println("Given the Evidence:");
			for(int i=0;i<SPN_Evidences.size();i++)
			{
				System.out.println(SPN_Evidences.get(i));
			}	
			/*Iterator<Entry <String, Integer[]>> Assign= Assignments.entrySet().iterator(); 
			while(Assign.hasNext())
			{
				Entry <String, Integer[]> assign=Assign.next();
				//获取具体的变量
				String var=assign.getKey();		
				String value="";
				for(int i=0;i<Assignments.get(var).length;i++)
					value=value+" "+Assignments.get(var)[i];
				System.out.println(var+"--"+value);
			}*/
			//System.out.println(Assignments);
			System.out.println("the probability is "+spn.get_root_value());
			System.out.println("the MAP is "+spn.get_root_MAP_value());	
			spn.find_MAP();
		}
		else
		{
			System.out.println("please make the SPN valid!");	
		}
		
		
		
	}
	
}
