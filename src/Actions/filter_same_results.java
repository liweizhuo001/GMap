package Actions;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class filter_same_results {
	public static void main(String args[]) throws IOException, ClassNotFoundException
	{
		String Read_path1="Results/previous.txt";
		BufferedReader Result1 = new BufferedReader(new FileReader(new File(Read_path1)));
		ArrayList<String> results1= new ArrayList<String>();
		String lineTxt = "";
		while ((lineTxt = Result1.readLine()) != null) {
			String line = lineTxt.trim(); // 去掉字符串首位的空格，避免其空格造成的错误
			//line=line.toLowerCase();//全部变成小写
			String parts[]=line.split(",");
			results1.add(parts[0]+","+ parts[1]);
			
		}
		
		String Read_path2="Results/last.txt";
		BufferedReader Result2 = new BufferedReader(new FileReader(new File(Read_path2)));
		ArrayList<String> results2= new ArrayList<String>();
		lineTxt = "";
		while ((lineTxt = Result2.readLine()) != null) {
			String line = lineTxt.trim(); // 去掉字符串首位的空格，避免其空格造成的错误
			//line=line.toLowerCase();//全部变成小写
			String parts[]=line.split(",");
			results2.add(parts[0]+","+parts[1]);
		}
		
		ArrayList<String> difference= new ArrayList<String>();
		
		for(int j=0;j<results2.size();j++)
		{
			if(results1.contains(results2.get(j)))
				continue;
			else
			{
				System.out.println(results2.get(j));
				difference.add(results2.get(j));
			}
		}
		System.out.println("不同的个数为："+difference.size());
	}

}
