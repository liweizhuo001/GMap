package Example;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

import Tools.Pellet_tools;
import Tools.PosTagger;
import Tools.Refine_Tools;
import Tools.Sim_Tools;
import Tools.TreeMap_Tools;

public class Test2 {
	public static void main(String args[]) throws IOException, ClassNotFoundException
	{
		//Refine_Tools refineTools=new Refine_Tools();
		/*ArrayList<String> maps=new ArrayList<String>();
		maps.add("a1,d2,0.8");
		maps.add("c1,b2,0.9");
		maps.add("c1,a2,0.7");
		maps.add("a1,b2,0.95");
		maps.add("a1,d2,0.8");
		maps.add("c1,a2,0.9");
		
	
		ArrayList<String> superclasses1=new ArrayList<String>();
		superclasses1.add("b1--a1");
		superclasses1.add("c1--a1");
		ArrayList<String> superclasses2=new ArrayList<String>();
		superclasses2.add("d2--b2,a2");
		superclasses2.add("b2--a2");
		superclasses2.add("c2--a2");
		
		ArrayList<String> refinedMap=new ArrayList<String>();
		refinedMap=refineTools.removeCrissCross(maps, superclasses1, superclasses2);
		for(int i=0;i<refinedMap.size();i++)
		{
			System.out.println(refinedMap.get(i));
		}*/
		/*String path="Results/Local_optimum/Test2.txt";
		BufferedReader Result = new BufferedReader(new FileReader(new File(path)));
		ArrayList<String>  result= new ArrayList<String>();
		String lineTxt = null;
		while ((lineTxt = Result.readLine()) != null) {
			String line = lineTxt.trim(); // 去掉字符串首位的空格，避免其空格造成的错误
			line=line.toLowerCase();//全部变成小写
			result.add(line);
		}
		refineTools.keepOneToOneAlignment(result);*/
		
		/*ArrayList<String> maps=new ArrayList<String>();
		maps.add("a1--d2");
		maps.add("c1--b2");
		maps.add("c1--a2");
		maps.add("d1--a2");
		for(int i=0;i<maps.size();i++)
		{
			System.out.println(maps.get(i));
		}
		System.out.println("***********************");
		maps.remove("c1--a2");
		for(int i=0;i<maps.size();i++)
		{
			System.out.println(maps.get(i));
		}
		//maps.add("a1--b2");
		TreeMap_Tools Match_Class=new TreeMap_Tools(maps);
		Match_Class.Print_Value();
		System.out.println("*****************************");
		Match_Class.remove("a1", "d2");
		Match_Class.Print_Value();*/
		/*boolean a=false;
		boolean b=true;
		boolean c=a&&b;
		System.out.println(c);*/
		/*String a="0";
		String b="*";
		System.out.println(a.equals("0"));
		System.out.println(b.equals("*"));*/
/*		double n=1;
		double M2=1/(1+Math.exp(-n));
	    System.out.println(M2);
	    double c=0.45;
	    double b=Math.pow(1-c, M2);
	    System.out.println(b);
	    System.out.println(1-0.4*b);
	    String a= "Program_committee";
	    Sim_Tools tools=new Sim_Tools();
	    String p=tools.tokeningWord(a);
	    System.out.println(p);
		PosTagger tagger = new PosTagger();
		System.out.println(tagger.findPOS("11")); 
		String normalized="11 thoracic vertebra";
		String parts[]=normalized.split(" ");
		String pos= tagger.findPOS(parts[0]);
		if(pos.equals("CD")||pos.equals("NNP"))//考虑到首字母缩写的问题
		{
			String abbr_letter = parts[1].charAt(0)+parts[0];
			normalized=normalized.replace(parts[0], abbr_letter).replace(parts[1]+" ", "");
		}
		System.out.println(normalized);*/
		double d=((float)2-(float)1)/2;
		System.out.print(d);
		for(int i=0;i<5;i++)
		{
			System.out.println(i);
		}
		
		/*String a="a#a";
		long tic=0;
		long toc=0;*/
		
		/*for(int i=0;i<30000;i++)
		{
			for(int j=0;j<3000;j++)
			{
				String s[]=a.split("#");
				Double.parseDouble("1");
			}
		}
		HashMap<Integer,Integer> test1=new HashMap<Integer,Integer>();
		ArrayList<Integer> test2=new ArrayList<Integer>();
		for(int i=0;i<100;i++)
		{
			test1.put(i, i);
			test2.add(i);
		}*/
		
		/*Random r=new Random();
		tic=System.currentTimeMillis();
		for(int j=0;j<100000;j++)
		{
			int k=r.nextInt(100);
			test1.containsKey(k);
		}
		
		toc=System.currentTimeMillis();
		System.out.println((toc-tic));
	
		tic=System.currentTimeMillis();
		for(int j=0;j<100000;j++)
		{
			int k=r.nextInt(100);
			find_index(test2,k);
			//test2.contains(k);
		}
		
		toc=System.currentTimeMillis();
		System.out.println((toc-tic));*/
		
		
		/*ArrayList<String> Based_Father_Pairs=new ArrayList<String>();//基于父亲匹配的概念对	
		Based_Father_Pairs.add("1");
		Based_Father_Pairs.add("2");
		Based_Father_Pairs.clear();
		System.out.println("**************");
		System.out.println(Based_Father_Pairs);
		System.out.println("**************");*/
		
		
	/*	long tic=0;
		long toc=0;
		HashSet<String> test1=new HashSet<String>();
		ArrayList<String> test2=new ArrayList<String>();
		for(int i=0;i<100;i++)
		{
			test1.add(i+"a");
			test2.add(i+"a");
		}
		
		Random r=new Random();
		tic=System.currentTimeMillis();
		for(int j=0;j<1000000;j++)
		{
			int k=r.nextInt(1000000);
			test1.contains(k+"a");
		}
		
		toc=System.currentTimeMillis();
		System.out.println((toc-tic));
	
		tic=System.currentTimeMillis();
		for(int j=0;j<1000000;j++)
		{
			int k=r.nextInt(1000000);
			//find_index(test2,k);
			test2.contains(k+"a");
		}
		
		toc=System.currentTimeMillis();
		System.out.println((toc-tic));*/
	  /*  Pellet_tools a=new Pellet_tools();
	    ArrayList<String> test1=new ArrayList<String>();
	    ArrayList<String> sub=new ArrayList<String>();
	    test1.add("A,part_of,some,B");
	    test1.add("A,part_of,some,E");
	    test1.add("B,part_of,some,C");
	    test1.add("E,part_of,some,F");
	    test1.add("P,part_of,some,Q");
	    test1.add("P,part_of,some,D");
	    sub.add("A--Q,D");
	   // sub.add("B--J");
	    a.transformToPartOf(test1,sub);
	    a.transformToHaspart(test1,sub);*/
	}
	
	public static int find_index(ArrayList<Integer> tokens,Integer concept)
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
