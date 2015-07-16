package PostProcessing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Tools.Result_Optimization;


public class Stable_Mapping {
	
	public static void main(String[] args) throws IOException
	{	
		BufferedWriter Stable_Mapping1 = null;
		BufferedWriter Stable_Mapping2 = null;
		String Store__Path1 = "Results/Test_Data/"+"Stable_Mapping1.txt";
		String Store__Path2 = "Results/Test_Data/"+"Stable_Mapping2.txt";
		try 
		{		
			Stable_Mapping1 = new BufferedWriter(new FileWriter(Store__Path1));
			Stable_Mapping2 = new BufferedWriter(new FileWriter(Store__Path2));
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
			
		String model="SPN";
		//String path="Results/"+model+"/resultpredict.txt";
		String path="Results/Test_Data/Test2.txt";
		BufferedReader Result = new BufferedReader(new FileReader(new File(path)));
		ArrayList<String>  result= new ArrayList<String>();
		String lineTxt = null;
		while ((lineTxt = Result.readLine()) != null) {
			String line = lineTxt.trim(); // 去掉字符串首位的空格，避免其空格造成的错误
			line=line.toLowerCase();//全部变成小写
			result.add(line);
		}
		
		//得到各自本体中唯一的概念
		ArrayList<String>  tokens1= new ArrayList<String>();
		ArrayList<String>  tokens2= new ArrayList<String>();
		for(int i=0;i<result.size();i++)
		{
			String part[]=result.get(i).split(",");
			String concept1=part[0];
			String concept2=part[1];			
			if(!tokens1.contains(concept1))
				tokens1.add(concept1);
			if(!tokens2.contains(concept2))
				tokens2.add(concept2);
		}
		
		//将其转换成字符串数组的形式
		String Token1="";
		for(int i=0;i<tokens1.size();i++)
			Token1=Token1+tokens1.get(i)+" ";
		Token1=Token1+"#";
		Token1=Token1.replace(" #", "");
		String Token2="";
		for(int i=0;i<tokens2.size();i++)
			Token2=Token2+tokens2.get(i)+" ";
		Token2=Token2.replace(" #", "");
		
		//通过计算相似度来进行计算
		Result_Optimization opt=new Result_Optimization();
		if(tokens1.size()<=tokens2.size())
		{
			double[][] similarity=new double[tokens1.size()][tokens2.size()];
			for(int i=0;i<result.size();i++)
			{
				String part[]=result.get(i).split(",");
				String concept1=part[0];
				String concept2=part[1];			
				int index1=find_index(tokens1,concept1);
				int index2=find_index(tokens2,concept2);
				similarity[index1][index2]=Double.parseDouble(part[2]);		
			}
			List<Map.Entry<Integer,Double>> value = new ArrayList<Map.Entry<Integer,Double>>();
			HashMap<Integer,Double> map=new HashMap<Integer,Double>();
			
			/*System.out.println("由Naive Greedy problem 得到的匹配对为:");		
			int Greedy_pair[]=opt.FindGreedySim_Pair(similarity,Token1.split(" "),Token2.split(" "));				
			//假设行比列短			
			ArrayList<String>  final_match= new ArrayList<String>();
			for(int i=0;i<Greedy_pair.length;i++)
			{
				String pair=tokens1.get(i)+","+tokens2.get(Greedy_pair[i])+","+similarity[i][Greedy_pair[i]];
				final_match.add(pair);
				map.put(i, similarity[i][Greedy_pair[i]]);
			}			
			value.addAll(map.entrySet());
			Collections.sort(value, new Comparator<Map.Entry<Integer, Double>>() {   
				public int compare(Map.Entry<Integer, Double> o1, Map.Entry<Integer, Double> o2) {      
					//return (o2.getValue() - o1.getValue()); 
					return (o2.getValue()).compareTo(o1.getValue());
				}
			});
			//输出结果		
			for(int i=0;i<final_match.size();i++)
			{
				int index=value.get(i).getKey();
				System.out.println(final_match.get(index));
				
			}*/
			
			System.out.println("由Stable Marriage策略得到的序列对为:");
			ArrayList<String> Stable_pair=opt.Stable_Marriage(similarity);
			for(int i=0;i<Stable_pair.size();i++)
			{
				String part[]=Stable_pair.get(i).split(",");
				int row=Integer.parseInt(part[0]);
				int column=Integer.parseInt(part[1]);
				//方便验证是否匹配
				System.out.println(tokens1.get(row)+","+tokens2.get(column)+","+similarity[row][column]);
				Stable_Mapping1.append(tokens1.get(row)+","+tokens2.get(column)+","+similarity[row][column]+"\n");
			}
			
			/*System.out.println("由Dynamic_Programming策略得到的序列对为:");
			map.clear();
			value.clear();
			ArrayList<String>  final_match2= new ArrayList<String>();
			ArrayList<Integer> DP_pair=opt.Dynamic_Programming(similarity);
			for(int i=0;i<DP_pair.size();i++)
			{			
				String pair=tokens1.get(i)+","+tokens2.get(DP_pair.get(i))+","+similarity[i][DP_pair.get(i)];
				final_match2.add(pair);
				map.put(i, similarity[i][DP_pair.get(i)]);	
			}					
			value.addAll(map.entrySet());
			Collections.sort(value, new Comparator<Map.Entry<Integer, Double>>() {   
				public int compare(Map.Entry<Integer, Double> o1, Map.Entry<Integer, Double> o2) {      
					//return (o2.getValue() - o1.getValue()); 
					return (o2.getValue()).compareTo(o1.getValue());
				}
			});
			//输出结果		
			for(int i=0;i<final_match2.size();i++)
			{
				int index=value.get(i).getKey();
				System.out.println(final_match2.get(index));
			}		*/
			
			System.out.println("由Beam_Search策略得到的序列对为:");
			map.clear();
			value.clear();
			ArrayList<String>  final_match3= new ArrayList<String>();
			int Backtracking_num=2;
			ArrayList<Integer> BS_pair=opt.Beam_Search(similarity,Backtracking_num);
			for(int i=0;i<BS_pair.size();i++)
			{			
				String pair=tokens1.get(i)+","+tokens2.get(BS_pair.get(i))+","+similarity[i][BS_pair.get(i)];
				final_match3.add(pair);
				map.put(i, similarity[i][BS_pair.get(i)]);	
			}					
			value.addAll(map.entrySet());
			Collections.sort(value, new Comparator<Map.Entry<Integer, Double>>() {   
				public int compare(Map.Entry<Integer, Double> o1, Map.Entry<Integer, Double> o2) {      
					//return (o2.getValue() - o1.getValue()); 
					return (o2.getValue()).compareTo(o1.getValue());
				}
			});
			//输出结果		
			for(int i=0;i<final_match3.size();i++)
			{
				int index=value.get(i).getKey();
				System.out.println(final_match3.get(index));
				Stable_Mapping2.append(final_match3.get(index)+"\n");
				//Stable_Mapping2
			}	
		}
		/********************************************************************************/
		else
		{
			double[][] similarity=new double[tokens2.size()][tokens1.size()];
			for(int i=0;i<result.size();i++)
			{
				String part[]=result.get(i).split(",");
				String concept1=part[0];
				String concept2=part[1];			
				int index1=find_index(tokens1,concept1);
				int index2=find_index(tokens2,concept2);
				similarity[index2][index1]=Double.parseDouble(part[2]);		
			}
	
			List<Map.Entry<Integer,Double>> value = new ArrayList<Map.Entry<Integer,Double>>();
			HashMap<Integer,Double> map=new HashMap<Integer,Double>();
			/*System.out.println("由Naive Greedy problem 得到的匹配对为:");		
			//value 和 map均是排序引入的变量
			List<Map.Entry<Integer,Double>> value = new ArrayList<Map.Entry<Integer,Double>>();
			HashMap<Integer,Double> map=new HashMap<Integer,Double>();
			
			int stable_pair[]=opt.FindGreedySim_Pair(similarity,Token2.split(" "),Token1.split(" "));		
			ArrayList<String>  final_match= new ArrayList<String>();
			
			for(int i=0;i<stable_pair.length;i++)
			{
				String pair=tokens1.get(stable_pair[i])+","+tokens2.get(i)+","+similarity[i][stable_pair[i]];

				final_match.add(pair);
				map.put(i, similarity[i][stable_pair[i]]);
			}
			value.addAll(map.entrySet());
			
			Collections.sort(value, new Comparator<Map.Entry<Integer, Double>>() {   
				public int compare(Map.Entry<Integer, Double> o1, Map.Entry<Integer, Double> o2) {      
					//return (o2.getValue() - o1.getValue()); 
					return (o2.getValue()).compareTo(o1.getValue());
				}
			});			
			//输出结果
			for(int i=0;i<final_match.size();i++)
			{
				int index=value.get(i).getKey();
				System.out.println(final_match.get(index));
			}*/
			
			System.out.println("由Stable Marriage策略得到的序列对为:");
			ArrayList<String> Stable_pair=opt.Stable_Marriage(similarity);
			for(int i=0;i<Stable_pair.size();i++)
			{
				String part[]=Stable_pair.get(i).split(",");
				int row=Integer.parseInt(part[0]);
				int column=Integer.parseInt(part[1]);
				//方便验证是否匹配
				System.out.println(tokens1.get(column)+","+tokens2.get(row)+","+similarity[row][column]);
				Stable_Mapping1.append(tokens1.get(column)+","+tokens2.get(row)+","+similarity[row][column]+"\n");
				//System.out.println(tokens1.get(row)+","+tokens2.get(column)+","+similarity[row][column]);
			}
			
	/*		System.out.println("由Dynamic_Programming策略得到的序列对为:");
			map.clear();
			value.clear();
			ArrayList<String>  final_match2= new ArrayList<String>();
			ArrayList<Integer> DP_pair=opt.Dynamic_Programming(similarity);
			for(int i=0;i<DP_pair.size();i++)
			{		
				//String pair=tokens1.get(i)+","+tokens2.get(DP_pair.get(i))+","+similarity[i][DP_pair.get(i)];
				String pair=tokens1.get(DP_pair.get(i))+","+tokens2.get(i)+","+similarity[i][DP_pair.get(i)];
				final_match2.add(pair);
				map.put(i, similarity[i][DP_pair.get(i)]);	
			}					
			value.addAll(map.entrySet());
			Collections.sort(value, new Comparator<Map.Entry<Integer, Double>>() {   
				public int compare(Map.Entry<Integer, Double> o1, Map.Entry<Integer, Double> o2) {      
					//return (o2.getValue() - o1.getValue()); 
					return (o2.getValue()).compareTo(o1.getValue());
				}
			});
			//输出结果		
			for(int i=0;i<final_match2.size();i++)
			{
				int index=value.get(i).getKey();
				System.out.println(final_match2.get(index));
			}	*/	
			System.out.println("由Beam_Search策略得到的序列对为:");
			map.clear();
			value.clear();
			ArrayList<String>  final_match3= new ArrayList<String>();
			int Backtracking_num=2;
			ArrayList<Integer> BS_pair=opt.Beam_Search(similarity,Backtracking_num);
			for(int i=0;i<BS_pair.size();i++)
			{			
				//tokens是不变的，只是相似度变了
				String pair=tokens1.get(BS_pair.get(i))+","+tokens2.get(i)+","+similarity[i][BS_pair.get(i)];
				final_match3.add(pair);
				map.put(i, similarity[i][BS_pair.get(i)]);	
			}					
			value.addAll(map.entrySet());
			Collections.sort(value, new Comparator<Map.Entry<Integer, Double>>() {   
				public int compare(Map.Entry<Integer, Double> o1, Map.Entry<Integer, Double> o2) {      
					//return (o2.getValue() - o1.getValue()); 
					return (o2.getValue()).compareTo(o1.getValue());
				}
			});
			//输出结果		
			for(int i=0;i<final_match3.size();i++)
			{
				int index=value.get(i).getKey();
				System.out.println(final_match3.get(index));
				Stable_Mapping2.append(final_match3.get(index)+"\n");
			}	
						
		}
		Stable_Mapping1.close();
		Stable_Mapping2.close();
	}
	
	public static int find_index(ArrayList<String> tokens,String concept)
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
}
	
	