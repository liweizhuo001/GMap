package SPNs;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class SPNProcessing {
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
	
	public ArrayList<String> SPN_Node_informations;
	public ArrayList<String> SPN_Edge_informations;
	public GraphSPN spn=new GraphSPN();
	public boolean valid=false;
	
	public  SPNProcessing() throws IOException
	{
		
		String Read_Path ="Datasets/SPN_data";
		BufferedReader Nodes = new BufferedReader(new FileReader(new File(Read_Path + "/Nodes_O.txt")));
		SPN_Node_informations = new ArrayList<String>();
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
		System.out.println("=====================================");
		spn.buildSPN(SPN_Node_informations, SPN_Edge_informations);
		valid=spn.isValid();
	}
	
	public ArrayList<String> process(HashMap<String, Integer[]> Assignments)
	{	
		ArrayList<String> assignM=new ArrayList<String>();
		System.out.println("=====================================");	
		spn.initial();//保证其初始化
		if(valid)
		{
			spn.bottom_up(Assignments);
			/*System.out.println("Given the Evidence:");	
			//只能迭代输出，不能直接输出，如果是数组的话
			Iterator<Entry <String, Integer[]>> Assign= Assignments.entrySet().iterator(); 
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
			
			/*System.out.println("the probability is "+spn.get_root_value());
			System.out.println("the MAP is "+spn.get_root_MAP_value());	*/
			assignM=spn.find_MAP();
		}
		else
		{
			System.out.println("please make the SPN valid!");	
		}
		return assignM;
	}
	
	
}
