package Tools;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;

public class TreeMap_Tools  {
    /**
     * @param args
     * 如果需要可以重写类toString方法TreeMap可以使用
     */
	 //因为这里方法用到的是m 所有对于全局的
//	static  Map<String, ArrayList<String>> m = new TreeMap<String, ArrayList<String>>();
	Map<String, ArrayList<String>> m = new TreeMap<String, ArrayList<String>>();
/*	static Map<String, ArrayList<String>> Ontology1_hasSub = new TreeMap<String, ArrayList<String>>();
	static Map<String, ArrayList<String>> Ontology2_hasSub = new TreeMap<String, ArrayList<String>>();*/
	
	public  void putAdd(String sr, String[] s) {
		if (!m.containsKey(sr)) 
		{
			m.put(sr, new ArrayList<String>());
		}
		for (int i = 0; i < s.length; i++) 
		{
			if(!m.get(sr).contains(s[i].toLowerCase()))//保证不重复
				m.get(sr).add(s[i].toLowerCase());
		}
	}
	
	public  void putAdd(String sr, String s) 
	{
		if (!m.containsKey(sr)) 
			m.put(sr, new ArrayList<String>());
		if(!m.get(sr).contains(s.toLowerCase()))//保证不重复
			m.get(sr).add(s.toLowerCase());
	}
	
	public  void putAdd(String sr, ArrayList<String> s) //确定主键一定没有的情况下
	{
		/*if (!m.containsKey(sr)) 
			m.put(sr, s);*/
		if (!m.containsKey(sr)) 
		{
			m.put(sr, new ArrayList<String>());
		}
		for(int i=0;i<s.size();i++)
		{
			if(!m.get(sr).contains(s.get(i).toLowerCase()))
					m.get(sr).add(s.get(i).toLowerCase());
		}
	}
	
	
	//这里传3个参数 ，本体中的概念1，本体中的概念2 以及对应需要操作的TreeMap
	public boolean has_relation(String x1,String x2)
	{
		ArrayList<String> Value=m.get(x1);
		if(Value!=null&&Value.contains(x2))
			return true;
		else
			return false;      
	}
	
	//这个函数主要是为了过滤一些预先被判断是Map的Nomap节点(假设Map节点是绝对可信的)
	public boolean Is_Mapped(String concept)
	{
		ArrayList<String> O1_Mapped=GetKey();
		for(int i=0;i<O1_Mapped.size();i++)
		{
			//即使是O2中的概念1对多的情况也在O1中已经体现了
			//判断是否与本体1中已匹配的概念重合了
			if(concept.equals(O1_Mapped.get(i)))
			{
				return true;
			}
			ArrayList<String> O2_Mapped=GetKey_Value(O1_Mapped.get(i));
			for(int j=0;j<O2_Mapped.size();j++)
			{
				//判断是否与本体2中已匹配的概念重合了
				if(concept.equals(O2_Mapped.get(j)))
				{
					return true;
				}			
			}
		}
		//若都不在则返回false
		return false;
	}
	
	//输入要打印的 TreeMap
	public  ArrayList<String> Print_Value()
	{	
		 ArrayList<String> result=new  ArrayList<String>();
		//Print_Value2(m.keySet());
		Iterable<String> iterable=m.keySet();
		Iterator<String> it = iterable.iterator();
		while(it.hasNext())  
		{
		     String key=it.next();
		     ArrayList<String> value=m.get(key);
		     //System.out.printf("(键为:%s,值为:%s)%n",key,value);
		     System.out.printf("%s,%s %n",key,value);
		     result.add(key+","+value);
		}
		return result;
	}
	
	
	public Map<String, ArrayList<String>> GetMap()
	{
		return m;    //获取Map的信息
	}
	
	public int size()
	{
		return m.size();
	}
	
	public  ArrayList<String> GetKey() //获取Map的所有键值，返回的是一个链表
	{
		ArrayList<String> Key=new ArrayList<String>();
		Iterable<String> iterable=m.keySet();
		Iterator<String> it = iterable.iterator();
		while (it.hasNext()) {
			String key = (String) it.next();
			Key.add(key);
		}		
		return Key;    //获取Map的信息
	}
	
	public  ArrayList<String> GetValue() //获取Map的所有键后的value值，返回的是一个链表
	{
		ArrayList<String> Value=new ArrayList<String>();
		Iterable<String> iterable=m.keySet();
		Iterator<String> it = iterable.iterator();
		while (it.hasNext()) {
			String key = (String) it.next();
			ArrayList<String> values=GetKey_Value(key);
			for(int i=0;i<values.size();i++)
			{
				//System.out.println(!Value.contains(values.get(i)));
				if(!Value.contains(values.get(i)))
					Value.add(values.get(i));
			}
		}		
		return Value;    //获取Map的信息
	}
	
	public ArrayList<String> GetKey_Value(String key)
	{
		return m.get(key);
	}
	
	
	public ArrayList<String> GetValue_Key(String value)
	{
		ArrayList<String> key=new ArrayList<String>();
		
		ArrayList<String> Keys=GetKey();
		for(int i=0;i<Keys.size();i++)
		{
			ArrayList<String> Value=GetKey_Value(Keys.get(i));
			if(Value.contains(value))
				key.add(Keys.get(i));
		}
		return key;    //获取Map的信息
	}
	
	public int getNumberOfMap()
	{
		int num=0;
		ArrayList<String> keySet= GetKey();
		for(int i=0;i<keySet.size();i++)
		{
			if(m.get(keySet.get(i)).size()==1)
				num++;
			else
				num=num+m.get(keySet.get(i)).size();
		}
		return num;
	}
	
	public void Modified_Match()
	{
		//ArrayList<String> map=m;
		//定义一个新的TreeMap来存储结构
		Map<String, ArrayList<String>> new_Match = new TreeMap<String, ArrayList<String>>();
		ArrayList<String> Key_medium=new ArrayList<String>();
/*		Key_medium.add("Thing2");
		new_Match.put("Thing1", Key_medium);*///常理上说本体1中的Thing与本体2中的Thing是不满足匹配关系的
		ArrayList<String> Key_set=GetKey();
		for(int i=0;i<Key_set.size();i++)
		{
			String Key=Key_set.get(i);
			ArrayList<String> Value=GetKey_Value(Key);
			ArrayList<String> New_Value=new ArrayList<String>();
			for(int j=0;j<Value.size();j++)
			{
				//System.out.println(Value.get(j));
				//String value=Value.get(j)+'1';
				String value=Value.get(j);//本体2中的概念不用加标签
				New_Value.add(value+"2");
			}
			new_Match.put(Key+"1", New_Value);
		}
		
		m.clear();
		m=new_Match;		
		//测试输出
  		Iterable<String> iterable=m.keySet();
		Iterator<String> it = iterable.iterator();
		while(it.hasNext())  
		{
		     String key=it.next();
		     ArrayList<String> value=m.get(key);
		     System.out.printf("(键为:%s,值为:%s)%n",key,value);
		}
	}
	
	public void Modified_HasSub(String label) //这里的label主要是标注概念属于本体1还是本体2
	{
		//ArrayList<String> map=m;
		//定义一个新的TreeMap来存储结构
		Map<String, ArrayList<String>> new_Match = new TreeMap<String, ArrayList<String>>();
		ArrayList<String> Key_set=GetKey();
		for(int i=0;i<Key_set.size();i++)
		{
			String Key=Key_set.get(i);
			ArrayList<String> Value=GetKey_Value(Key);
			ArrayList<String> New_Value=new ArrayList<String>();
			for(int j=0;j<Value.size();j++)
			{
				//System.out.println(Value.get(j));
				//String value=Value.get(j)+'1';
				String value=Value.get(j)+label;//本体2中的概念不用加标签
				New_Value.add(value);
			}
			new_Match.put(Key+label, New_Value);
		}
		m.clear();
		m=new_Match;
		
		//测试输出
  		Iterable<String> iterable=m.keySet();
		Iterator<String> it = iterable.iterator();
		while(it.hasNext())  
		{
		     String key=it.next();
		     ArrayList<String> value=m.get(key);
		     System.out.printf("(键为:%s,值为:%s)%n",key,value);
		}
	}

	
	
	public TreeMap_Tools(String path) throws IOException
	{	
		BufferedReader  Ontology_match_information = new BufferedReader (new FileReader(new File(path)));
		/*BufferedReader  Ontology1_sub_information = new BufferedReader (new FileReader(new File("Data/Ontology1_HasSub.txt")));
		BufferedReader  Ontology2_sub_information = new BufferedReader (new FileReader(new File("Data/Ontology2_HasSub.txt")));*/
		String lineTxt = null;
		  while ((lineTxt = Ontology_match_information.readLine()) != null) {
				String line = lineTxt.trim(); //去掉字符串首位的空格，避免其空格造成的错误		
				String[] part = line.split("--"); //用"--"来进行key和Value的分割			
				String[] value = part[1].split(",");
				for(int i=0;i<value.length;i++)
				{
					value[i]=value[i].toLowerCase();
				}
				putAdd(part[0].toLowerCase(), value);	
			}
/*		  while ((lineTxt = Ontology1_sub_information.readLine()) != null) {
				String line = lineTxt.trim(); //去掉字符串首位的空格，避免其空格造成的错误		
				String[] part = line.split("--"); //用"--"来进行key和Value的分割			
				String[] value = part[1].split(",");
				putAdd_O1_Sub(part[0], value);	
			} 
		  while ((lineTxt = Ontology2_sub_information.readLine()) != null) {
				String line = lineTxt.trim(); //去掉字符串首位的空格，避免其空格造成的错误		
				String[] part = line.split("--"); //用"--"来进行key和Value的分割			
				String[] value = part[1].split(",");
				putAdd_O2_Sub(part[0], value);	
			} */
		  System.out.println("the TreeMap has been constructed.");
		  
		    //用常规方式打印输出
/*			System.out.println("输出方式2");
			Print_Value(Mapping_information.keySet());*/
			//来判断对应的键中是否包含该元组
/*			String concept1_1="Form";
			String concept1_2="Author_information_form";
			boolean label=Is_subclassof(concept1_1,concept1_2,Mapping_information.keySet()); //判断Map中的某个元素是否存在相应的关系（这里的关系可以连接为包含关系）
			System.out.println(label);*/
	}
	
	public TreeMap_Tools(ArrayList<String> array) 
	{		
		for (int i = 0; i < array.size(); i++) 
		{
			//System.out.println(array);
			String line = array.get(i).trim(); // 去掉字符串首位的空格，避免其空格造成的错误
			String[] part = line.split("--"); // 用"--"来进行key和Value的分割
			String[] value = part[1].split(",");
			/*for (int j = 0; j < value.length; j++) 
			{
				value[j] = value[j].toLowerCase();
			}*/
			putAdd(part[0].toLowerCase(), value);
		}
		  System.out.println("the TreeMap has been constructed.");	
	}
	
	public TreeMap_Tools() 
	{		
		  System.out.println("the TreeMap has been constructed.");	
	}

	public void remove(String object1,String object2)
	{
		ArrayList<String> objectValue=GetKey_Value(object1);
		objectValue.remove(object2);
		if(objectValue.size()==0)
			m.remove(object1);
	}
	
	public void clear()
	{
		m.clear();
	}

	public static void main(String[] args)throws IOException {	
	// BufferedReader  sub_information = new BufferedReader (new FileReader(new File("src/excise/MAP/test.txt")));
	// BufferedReader  match_information = new BufferedReader (new FileReader(new File("src/excise/MAP/Ontology1_match.txt")));
	// BufferedReader  Ontology1_hasSub = new BufferedReader (new FileReader(new File("src/excise/MAP/Ontology1_HasSub.txt")));
      BufferedReader  Ontology2_hasSub = new BufferedReader (new FileReader(new File("Data/Ontology2_HasSub.txt")));
	  String lineTxt = null;
	  TreeMap_Tools a=new TreeMap_Tools("Data/Ontology2_HasSub.txt");
	  while ((lineTxt = Ontology2_hasSub.readLine()) != null) {
			// System.out.println(lineTxt);
			String line = lineTxt.trim(); //去掉字符串首位的空格，避免其空格造成的错误		
			String[] part = line.split("--"); //用"--"来进行key和Value的分割		
			String[] value = part[1].split(",");
			a.putAdd(part[0], value);	//实例化之后即可调用这个类中的函数，并不需要静态定义函数或者数据结构
		}  
	    //用常规方式打印输出
		System.out.println("输出方式2");
		a.Print_Value();
		//来判断对应的键中是否包含该元组
		String concept1_1="Form";
		String concept1_2="Author_information_form";
		boolean label=a.has_relation(concept1_1, concept1_2); //判断Map中的某个元素是否存在相应的关系（这里的关系可以连接为包含关系）
		System.out.println(label);
	
/*	  Iterator<String> it=m.keySet().iterator();//根据hashmap的键值创建迭代器
	   while(it.hasNext()){
	     String key=(String)it.next();
	     ArrayList<String> value=m.get(key);
	     System.out.println(key+"--"+value);
	   }*/
	
    }
}