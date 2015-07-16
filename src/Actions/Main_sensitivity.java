package Actions;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import SPNs.SPNProcessing;
import Tools.HeuristicRule_Tools;
import Tools.OAEIAlignmentOutput;
import Tools.Pellet_tools;
import Tools.Refine_Tools;
import Tools.Sim_Tools;
import Tools.TreeMap_Tools;

public class Main_sensitivity {
	static Sim_Tools tool=new Sim_Tools();
	static BufferedWriter bfw_Result= null;
	static double a=0.6;
	static double contribution=0.3;
	static double threshold=0.9;	
	static double average_f_measure=0;
	//static BufferedWriter bfw_middle= null;
	public static void main(String args[]) throws Exception
	{

		String resultPath="Results/1.txt";		
		//String resultPath2="Results/middle_Result.txt";	
		try
		{			
			bfw_Result=new BufferedWriter(new FileWriter(resultPath));	
			//bfw_middle=new BufferedWriter(new FileWriter(resultPath2));	
		}
		catch(IOException e)
		{
			e.printStackTrace();		
		}
		tool.initialPOS();


		String dictionary1="anatomy";
		String Read_path1="dic/"+dictionary1+".txt";
		BufferedReader Result1 = new BufferedReader(new FileReader(new File(Read_path1)));
		ArrayList<String> dic1= new ArrayList<String>();
		String lineTxt = null;
		while ((lineTxt = Result1.readLine()) != null) {
			String line = lineTxt.trim(); // 去掉字符串首位的空格，避免其空格造成的错误
			//line=line.toLowerCase();//全部变成小写
			dic1.add(line);
		}
		HashMap<String, String> anatomy=transformToHashMap(dic1);


		long tic1 = 0 ;
		long toc1= 0;
		long tic = 0 ;
		long toc= 0;
		tic1=System.currentTimeMillis();


/*		String []Ontology1={"101","103","104","201","201-2","201-4","201-6","201-8","202","202-2","202-4","202-6","202-8",
				   "221","222","223","224","225","228","232","233","236","237","238","239","240","241","246",
				   "247","248","248-2","248-4","248-6","248-8","249","249-2","249-4","249-6","249-8","250","250-2",
				   "250-4","250-6","250-8","251","251-2","251-4","251-6","251-8","252","252-2","252-4","252-6",
				   "252-8","253","253-2","253-4","253-6","253-8","254","254-2","254-4","254-6","254-8","257",
				   "257-2","257-4","257-6","257-8","258","258-2","258-4","258-6","258-8","259","259-2","259-4",
				   "259-6","259-8","260","260-2","260-4","260-6","260-8","261","261-2","261-4","261-6","261-8",
				   "262","262-2","262-4","262-6","262-8","265", "266","301","302","303","304"};	*/
/*		String []Ontology1={"101","201","201-2","201-4","201-6","201-8","202","202-2","202-4","202-6","202-8",
				   "221","222","223","224","225","228","232","233","236","237","238","239","240","241","246",
				   "247","248","248-2","248-4","248-6","248-8","249","249-2","249-4","249-6","249-8","250","250-2",
				   "250-4","250-6","250-8","251","251-2","251-4","251-6","251-8","252","252-2","252-4","252-6",
				   "252-8","253","253-2","253-4","253-6","253-8","254","254-2","254-4","254-6","254-8","257",
				   "257-2","257-4","257-6","257-8","258","258-2","258-4","258-6","258-8","259","259-2","259-4",
				   "259-6","259-8","260","260-2","260-4","260-6","260-8","261","261-2","261-4","261-6","261-8",
				   "262","262-2","262-4","262-6","262-8","265", "266"};
		String []Ontology2={"101"};
		for (int x=0;x<Ontology1.length;x++)
		{
			String ontologyName1 = Ontology1[x]+"onto";
			for(int y=0;y<Ontology2.length;y++)
			{			
				String ontologyName2 = Ontology2[y]+"onto";
		String ontologyName1="261-2onto";
		String ontologyName2="101onto";

		String readPath1="Datasets/benchmarks/"+ontologyName1+".rdf";
		String readPath2="Datasets/benchmarks/"+ontologyName2+".rdf";

		String classAlignmentPath="Results/Benchmark_alignments/"+ontologyName1+"-"+ontologyName2+"_class.txt";	
		String propertyAlignmentPath="Results/Benchmark_alignments/"+ontologyName1+"-"+ontologyName2+"_property.txt";*/


/*		String []Ontology1={"cmt","Conference","confOf","edas","ekaw","iasted","sigkdd"};
		String []Ontology2={"cmt","Conference","confOf","edas","ekaw","iasted","sigkdd"};
		for (int x=0;x<Ontology1.length;x++)
		{
			String ontologyName1 = Ontology1[x];
			for(int y=x+1;y<Ontology2.length;y++)
			{			
				String ontologyName2 = Ontology1[y];
		String ontologyName1="cmt";
		String ontologyName2="Conference";

		String readPath1="Datasets/conference_ontologys/"+ontologyName1+".owl";
		String readPath2="Datasets/conference_ontologys/"+ontologyName2+".owl";

		String classAlignmentPath="Results/Conference_alignments/"+ontologyName1+"-"+ontologyName2+"_class.txt";	
		String propertyAlignmentPath="Results/Conference_alignments/"+ontologyName1+"-"+ontologyName2+"_property.txt";*/

		

		String []Ontology={"animals","hotel","network","people+pets","russia"};
		//String []Ontology={"pefople+pets"};
		for (int x=0;x<Ontology.length;x++)
		{
			String ontologyName1 = Ontology[x]+"A";		
			String ontologyName2 = Ontology[x]+"B";
		/*String ontologyName1="people+petsA";
		String ontologyName2="people+petsB";*/
		String readPath1="Datasets/I3CON_ontologys/"+ontologyName1+".owl";
		String readPath2="Datasets/I3CON_ontologys/"+ontologyName2+".owl";
		String classAlignmentPath="Results/I3CON_alignments/"+ontologyName1+"B"+"_class.txt";	
		String propertyAlignmentPath="Results/I3CON_alignments/"+ontologyName1+"B"+"_property.txt";

		tic = System.currentTimeMillis();
/*		String ontologyName1="mouse";
		String ontologyName2="human";

		String readPath1="Datasets/anatomy/"+ontologyName1+".owl";
		String readPath2="Datasets/anatomy/"+ontologyName2+".owl";

		String classAlignmentPath="Results/Anatomy_alignments/"+ontologyName1+"-"+ontologyName2+"_class.txt";	
		String propertyAlignmentPath="Results/Anatomy_alignments/"+ontologyName1+"-"+ontologyName2+"_property.txt";*/

		Pellet_tools onto1=new Pellet_tools(); 
		Pellet_tools onto2=new Pellet_tools(); 
		//Loading
		onto1.readOnto(readPath1);
		onto2.readOnto(readPath2);

		TreeMap_Tools resultClassMap=new TreeMap_Tools(classAlignmentPath);
		TreeMap_Tools resultPropertyMap=new TreeMap_Tools(propertyAlignmentPath);

		/**
		 * acquire ontology information
		 */
		ArrayList<String> classes1=onto1.GetAllConcept();
		ArrayList<String> classlabel1=onto1.GetAllConceptLabel();
		ArrayList<String> objectProperties1=onto1.GetObjectProperty();
		ArrayList<String> objectPropertieslabel1=onto1.GetObjectPropertyLabel();
		ArrayList<String> dataProperties1=onto1.GetDataProperty();
		ArrayList<String> dataPropertieslabel1=onto1.GetDataPropertyLabel();
		ArrayList<String> propertiesInverse1=onto1.GetPropertyAndInverse();//其实只有objectproperty有这个性质	
		ArrayList<String> objectRelations1=onto1.New_GetObjectRelations();
		//ArrayList<String> objectRelations1=onto1.GetObjectRelations();
		ArrayList<String> dataRelations1=onto1.GetDataPropertyRelations();
		ArrayList<String> instances1=filterCommand(onto1.GetConcept_Instances());
		ArrayList<String> subclasses1=filterCommand(onto1.GetSubclass());
		ArrayList<String> superclasses1=filterCommand(onto1.GetSuperclass());
		ArrayList<String> subclassesDirect1=filterCommand(onto1.GetSubclass_Direct());
		ArrayList<String> siblings1=filterCommand(onto1.GetSibling(subclassesDirect1));
		ArrayList<String> disjoint1=filterCommand(onto1.GetDisjointwith());

		ArrayList<String> EquivalentClass1=onto1.GetEquivalentClass();
		ArrayList<String> EquivalentProperty1=onto1.GetEquivalentProperty();
		ArrayList<String> Restrictions1=onto1.GetSomeRestrictions();
		/*subclasses1=onto1.enhancedSubClasses(subclasses1,EquivalentClass1);
		superclasses1=onto1.enhancedSuperClasses(superclasses1,EquivalentClass1);
		disjoint1=onto1.enhancedClassesDisjoint(disjoint1,subclasses1,EquivalentClass1);*/
		objectRelations1=onto1.enhancedRelation(objectRelations1,EquivalentClass1);
		System.out.println("**********");
		objectRelations1=onto1.enhancedRelation(objectRelations1,Restrictions1);


		ArrayList<String> classes2=onto2.GetAllConcept();
		ArrayList<String> classlabel2=onto2.GetAllConceptLabel();
		ArrayList<String> objectProperties2=onto2.GetObjectProperty();
		ArrayList<String> objectPropertieslabel2=onto2.GetObjectPropertyLabel();
		ArrayList<String> dataProperties2=onto2.GetDataProperty();
		ArrayList<String> dataPropertieslabel2=onto2.GetDataPropertyLabel();
		ArrayList<String> propertiesInverse2=onto2.GetPropertyAndInverse();//其实只有objectproperty有这个性质
		ArrayList<String> objectRelations2=onto2.New_GetObjectRelations();
		//ArrayList<String> objectRelations2=onto2.GetObjectRelations();
		ArrayList<String> dataRelations2=onto2.GetDataPropertyRelations();
		ArrayList<String> instances2=filterCommand(onto2.GetConcept_Instances());
		ArrayList<String> subclasses2=filterCommand(onto2.GetSubclass());
		ArrayList<String> superclasses2=filterCommand(onto2.GetSuperclass());
		ArrayList<String> subclassesDirect2=filterCommand(onto2.GetSubclass_Direct());
		ArrayList<String> siblings2=filterCommand(onto2.GetSibling(subclassesDirect2));
		ArrayList<String> disjoint2=filterCommand(onto2.GetDisjointwith());

		ArrayList<String> EquivalentClass2=onto2.GetEquivalentClass();
		ArrayList<String> EquivalentProperty2=onto2.GetEquivalentProperty();
		ArrayList<String> Restrictions2=onto2.GetSomeRestrictions();
		/*subclasses2=onto2.enhancedSubClasses(subclasses2,EquivalentClass2);
		superclasses2=onto2.enhancedSuperClasses(superclasses2,EquivalentClass2);
		disjoint2=onto2.enhancedClassesDisjoint(disjoint2,subclasses2,EquivalentClass2);*/
		objectRelations2=onto2.enhancedRelation(objectRelations2,EquivalentClass2);
		System.out.println("**********");
		objectRelations2=onto2.enhancedRelation(objectRelations2,Restrictions2);

		/**
		 * calculate the similarities by ontology information
		 */
		/**
		 * Instances
		 */
		toc = System.currentTimeMillis();
		System.out.println(toc-tic);
		//父子中只有一个儿子且不为空的情况，可能导致相似度是一样的（仍需改进空间）
		tic = System.currentTimeMillis();
		System.out.println(toc-tic);
		ArrayList<String> InstanceSim=tool.instancesSim2(classes1,classes2,instances1, instances2);
		System.out.println("*************************************");
		System.out.println(InstanceSim.size());
		toc = System.currentTimeMillis();
		System.out.println(toc-tic);
		
		/**
		 * concepts
		 */
		//类只是一个编号，而真正的名字是label

		/*toc = System.currentTimeMillis();
		System.out.println(toc-tic);*/
		ArrayList<String> editSimClass=new ArrayList<String>();
		ArrayList<String> newEditSimClass=tool.initialClass(classes1, classes2);
		// newEditSimClass=tool.ClassDisjoint(, );
		ArrayList<String> semanticSimClass=new ArrayList<String>();
		ArrayList<String> newSemanticSimClass=tool.initialClass(classes1, classes2);
		ArrayList<String> tfidfSim=new ArrayList<String>();
		TreeMap_Tools partOf1=new TreeMap_Tools();
		TreeMap_Tools partOf2=new TreeMap_Tools();
		TreeMap_Tools hasPart1=new TreeMap_Tools();
		TreeMap_Tools hasPart2=new TreeMap_Tools();
		HashMap<String,String> classLabels1=new HashMap<String,String>();
		HashMap<String,String> classLabels2=new HashMap<String,String>();

		System.out.println(InstanceSim.get(0));
		boolean labelflag=classes1.size()==classlabel1.size()&&classes2.size()==classlabel2.size();//只有在医学本体中才会出现
		if(labelflag==true)
		{
			classLabels1=tool.transformToHashMap(classlabel1);//存储一份对应形式
			classLabels2=tool.transformToHashMap(classlabel2);//存储一份对应形式
			
			classlabel1=tool.keepLabel(classlabel1);
			classlabel2=tool.keepLabel(classlabel2);
			
			tic = System.currentTimeMillis();
			editSimClass=tool.editSimClass(classlabel1, classlabel2);
			System.out.println("*************************************");
			System.out.println(editSimClass.size());
			toc = System.currentTimeMillis();
			System.out.println("编辑距离相似度计算消耗的时间为："+(toc-tic));
			//bfw_Result.append("编辑距离相似度计算消耗的时间为："+(toc-tic)/1000+"s"+"\n");
			
			tic = System.currentTimeMillis();
			//semanticSimClass=tool.semanticSimClass(classlabel1, classlabel2);//这里也词典就利用上面的判断条件引入
			semanticSimClass=tool.NewsemanticSimClass4(classlabel1, classlabel2);//这里也词典就利用上面的判断条件引入
			System.out.println("*************************************");
			System.out.println(semanticSimClass.size());
			toc = System.currentTimeMillis();
			System.out.println("语义相似度计算消耗的时间为："+(toc-tic));
			//bfw_Result.append("语义相似度计算消耗的时间为："+(toc-tic)/1000+"s"+"\n");
			
			classlabel1=Normalize(classlabel1,anatomy);
			classlabel2=Normalize(classlabel2,anatomy);

			tic = System.currentTimeMillis();
			newEditSimClass=tool.editSimClass(classlabel1, classlabel2);
			System.out.println("*************************************");
			System.out.println(editSimClass.size());
			toc = System.currentTimeMillis();
			System.out.println("编辑距离相似度计算消耗的时间为："+(toc-tic));
			//bfw_Result.append("编辑距离相似度计算消耗的时间为："+(toc-tic)/1000+"s"+"\n");
			//这里要将class--label进行一步处理,只保留label
			
			tic = System.currentTimeMillis();
			//semanticSimClass=tool.semanticSimClass(classlabel1, classlabel2);//这里也词典就利用上面的判断条件引入
			newSemanticSimClass=tool.NewsemanticSimClass4(classlabel1, classlabel2);//这里也词典就利用上面的判断条件引入
			System.out.println("*************************************");
			System.out.println(semanticSimClass.size());
			toc = System.currentTimeMillis();
			System.out.println("语义相似度计算消耗的时间为："+(toc-tic));
			//bfw_Result.append("语义相似度计算消耗的时间为："+(toc-tic)/1000+"s"+"\n");

			tic = System.currentTimeMillis();
			tfidfSim=tool.tfidfSim(classlabel1, classlabel2);
			System.out.println("*************************************");
			System.out.println(tfidfSim.size());
			toc = System.currentTimeMillis();
			System.out.println("TFIDF相似度计算消耗的时间为："+(toc-tic));
		 	//bfw_Result.append("TFIDF相似度计算消耗的时间为："+(toc-tic)/1000+"s"+"\n");
			/*objectRelations1=onto1.transformToRelation(objectRelations1,Restrictions1,subclasses1);
			objectRelations2=onto2.transformToRelation(objectRelations2,Restrictions2,subclasses2);*/
			
			partOf1=onto1.transformToPartOf(Restrictions1,subclassesDirect1);
			partOf2=onto2.transformToPartOf(Restrictions2,subclassesDirect2);
			hasPart1=onto1.transformToHaspart(Restrictions1,subclassesDirect1);
			hasPart2=onto2.transformToHaspart(Restrictions2,subclassesDirect2);
			/*partOf1=onto1.transformToPartOf(Restrictions1,subclasses1);
			partOf2=onto2.transformToPartOf(Restrictions2,subclasses2);
			hasPart1=onto1.transformToHaspart(Restrictions1,subclasses1);
			hasPart2=onto2.transformToHaspart(Restrictions2,subclasses2);*/
		}		
		else
		{	
			classlabel1=tool.briefLabel(classlabel1);
			classlabel2=tool.briefLabel(classlabel2);
			tic = System.currentTimeMillis();
			editSimClass=tool.editSimClassWithLabel(classes1, classes2,classlabel1,classlabel2);
			//editSimClass=tool.editSimClass(classlabel1,classlabel2);
			System.out.println("*************************************");
			System.out.println(editSimClass.size());
			toc = System.currentTimeMillis();
			System.out.println(toc-tic);

			//从时间开销来考虑的话，下面2者的计算，只能用label替换来计算
			classlabel1=tool.replaceLabel(classes1,classlabel1);
			classlabel2=tool.replaceLabel(classes2,classlabel2);
			tic = System.currentTimeMillis();
			semanticSimClass=tool.semanticSimClass(classlabel1,classlabel2);//这里也词典就利用上面的判断条件引入
			System.out.println("*************************************");
			System.out.println(semanticSimClass.size());
			toc = System.currentTimeMillis();
			System.out.println(toc-tic);

			tic = System.currentTimeMillis();
			tfidfSim=tool.tfidfSim(classlabel1,classlabel2);
			System.out.println("*************************************");
			System.out.println(tfidfSim.size());
			toc = System.currentTimeMillis();
			System.out.println(toc-tic);
		}

		/**
		 * ObjectProperties
		 */
		ArrayList<String> editSimObjectProperty=new ArrayList<String>();
		ArrayList<String> SemanticSimObjectProperty=new ArrayList<String>();
		boolean objectproperty_lableflag=false;
		objectproperty_lableflag=objectProperties1.size()==objectPropertieslabel1.size()&&objectProperties2.size()==objectPropertieslabel2.size();//只有在医学本体中才会出现
		ArrayList<String> objectPropertyPair=new ArrayList<String>();
		for(int i=0;i<objectProperties1.size();i++)
		{
			String objectproperty1=objectProperties1.get(i);
			for(int j=0;j<objectProperties2.size();j++)
			{
				String objectproperty2=objectProperties2.get(j);
				objectPropertyPair.add(objectproperty1+","+objectproperty2);
			}
		}
		if(objectproperty_lableflag==true)
		{
			//只用label来进行计算,一般这种情况只出现在医学本体中
			objectPropertieslabel1=tool.keepLabel(objectPropertieslabel1);
			objectPropertieslabel2=tool.keepLabel(objectPropertieslabel2);

			editSimObjectProperty=tool.editSimProperty(objectPropertieslabel1, objectPropertieslabel2);
			System.out.println("*************************************");
			System.out.println(editSimObjectProperty.size());

			SemanticSimObjectProperty=tool.semanticSimProperty(objectPropertieslabel1, objectPropertieslabel2);
			System.out.println("*************************************");
			System.out.println(SemanticSimObjectProperty.size());
		}
		else
		{
			//editSimClass=tool.editSimClassWithLabel(classes1, classes2,classlabel1,classlabel2);
			//将label替换来计算编辑距离(分开算代价太高)
			objectPropertieslabel1=tool.briefLabel(objectPropertieslabel1);
			objectPropertieslabel2=tool.briefLabel(objectPropertieslabel2);
			
			editSimObjectProperty=tool.editSimPropertyWithLabel(objectProperties1, objectProperties2,objectPropertieslabel1,objectPropertieslabel2);
			System.out.println("*************************************");
			System.out.println(editSimObjectProperty.size());

			objectPropertieslabel1=tool.replaceLabel(objectProperties1,objectPropertieslabel1);
			objectPropertieslabel2=tool.replaceLabel(objectProperties2,objectPropertieslabel2);

			SemanticSimObjectProperty=tool.semanticSimProperty(objectPropertieslabel1, objectPropertieslabel2);
			System.out.println("*************************************");
			System.out.println(SemanticSimObjectProperty.size());
		}

		/**
		 * datatypeProperties
		 */
		ArrayList<String> editSimDatatypeProperty=new ArrayList<String>();
		ArrayList<String> SemanticSimDatatypeProperty=new ArrayList<String>();	
		boolean dataproperty_lableflag=false;
		dataproperty_lableflag=dataProperties1.size()==dataPropertieslabel1.size()&&dataProperties2.size()==dataPropertieslabel2.size();//只有在医学本体中才会出现
		ArrayList<String> dataPropertyPair=new ArrayList<String>();
		for(int i=0;i<dataProperties1.size();i++)
		{
			String dataProperty1=dataProperties1.get(i);
			for(int j=0;j<dataProperties2.size();j++)
			{
				String dataProperty2=dataProperties2.get(j);
				dataPropertyPair.add(dataProperty1+","+dataProperty2);
			}
		}
		
		if(dataproperty_lableflag==true)
		{
			dataPropertieslabel1=tool.keepLabel(dataPropertieslabel1);
			dataPropertieslabel2=tool.keepLabel(dataPropertieslabel2);
			editSimDatatypeProperty=tool.editSimProperty(dataPropertieslabel1, dataPropertieslabel2);
			System.out.println("*************************************");
			System.out.println(editSimDatatypeProperty.size());

			SemanticSimDatatypeProperty=tool.semanticSimProperty(dataPropertieslabel1, dataPropertieslabel2);
			System.out.println("*************************************");
			System.out.println(SemanticSimDatatypeProperty.size());
		}
		else
		{
			//将label替换来计算编辑距离(分开算代价太高)
			dataPropertieslabel1=tool.briefLabel(dataPropertieslabel1);
			dataPropertieslabel2=tool.briefLabel(dataPropertieslabel2);
			
			editSimDatatypeProperty=tool.editSimPropertyWithLabel(dataProperties1,dataProperties2,dataPropertieslabel1, dataPropertieslabel2);
			System.out.println("*************************************");
			System.out.println(editSimDatatypeProperty.size());

			dataPropertieslabel1=tool.replaceLabel(dataProperties1,dataPropertieslabel1);
			dataPropertieslabel2=tool.replaceLabel(dataProperties2,dataPropertieslabel2);
			SemanticSimDatatypeProperty=tool.semanticSimProperty(dataPropertieslabel1, dataPropertieslabel2);
			System.out.println("*************************************");
			System.out.println(SemanticSimDatatypeProperty.size());
		}

		/*ArrayList<String> editSimDatatypeProperty=tool.editSimProperty(dataProperties1, dataProperties2);
		System.out.println("*************************************");
		System.out.println(editSimDatatypeProperty.size());

		ArrayList<String> SemanticSimDatatypeProperty=tool.semanticSimProperty(dataProperties1, dataProperties2);
		System.out.println("*************************************");
		System.out.println(SemanticSimDatatypeProperty.size());
		System.out.println();*/
		//以后肯定是循环模式(而且应该是ArrayList的格式进行写入)
		/**
		 * statistic the number of each pair that satisfys heuristic rules by ontology information
		 */
		/*String classAlignmentPath="Results/Benchmark_alignments/"+ontologyName1+"-"+ontologyName2+"_class.txt";	
		String propertyAlignmentPath="Results/Benchmark_alignments/"+ontologyName1+"-"+ontologyName2+"_property.txt";*/

		/*String classAlignmentPath="Results/First_line_matcher/"+ontologyName1+"-"+ontologyName2+"_class.txt";	
		String propertyAlignmentPath="Results/First_line_matcher/"+ontologyName1+"-"+ontologyName2+"_property.txt";*/

		ArrayList<String> classesMap=new ArrayList<String>();
		ArrayList<String> propertiesMap=new ArrayList<String>();
		ArrayList<String> objectPropertiesMap=new ArrayList<String>();		
		ArrayList<String> dataPropertiesMap=new ArrayList<String>();
		ArrayList<String> oldClassesMap=new ArrayList<String>();
		ArrayList<String> oldPropertiesMap=new ArrayList<String>();

		ArrayList<String> hiddenClassesMap=new ArrayList<String>();
		ArrayList<String> hiddenObjectPropertiesMap=new ArrayList<String>();
		ArrayList<String> hiddenDataPropertiesMap=new ArrayList<String>();

		Refine_Tools refineTools=new Refine_Tools();
		int iteration=0;
		boolean flag=false;
		boolean needComplementClass=true;
		boolean needComplementProperty=true;
		HashMap<String, Integer[]> Assignments=new HashMap<String, Integer[]>();
		SPNProcessing action=new SPNProcessing();

		ArrayList<String> fathers=new ArrayList<String>();
		ArrayList<String> children=new ArrayList<String>();
		ArrayList<String> siblings=new ArrayList<String>();
		ArrayList<String> hasPart=new ArrayList<String>();
		ArrayList<String> partOf=new ArrayList<String>();
		ArrayList<String> domains=new ArrayList<String>();
		ArrayList<String> ranges=new ArrayList<String>();
		ArrayList<String> datatype=new ArrayList<String>();
		ArrayList<String> disjointSim=new ArrayList<String>();
		disjointSim=tool.ClassDisjoint(classes1,classes2);//开放世界假设,将所有的匹配对初始化为'*'
		ArrayList<String> classesAlignments=new ArrayList<String>();
		ArrayList<String> propertyAlignments=new ArrayList<String>();
		HeuristicRule_Tools ruleTools=new HeuristicRule_Tools(classesAlignments,propertyAlignments);
		long iteration_tic=0;
		long iteration_toc=0;
		do
		{
			iteration_tic=System.currentTimeMillis();
			//进行一轮存储赋值
			oldClassesMap.clear();
			oldPropertiesMap.clear();
			//hiddenClassesMap.clear();
			//hiddenPropertiesMap.clear();
		/*	for(int i=0;i<classesMap.size();i++)
			{
				String parts[]=classesMap.get(i).split(",");
				String label1=classLabels1.get(parts[0]);
				String label2=classLabels2.get(parts[1]);
				System.out.println(label1+","+label2+","+parts[2]);
			}*/
			classesAlignments=changeToAlignments(classesMap);
			propertyAlignments=changeToAlignments(propertiesMap);
			for(int i=0;i<classesAlignments.size();i++)
			{
				oldClassesMap.add(classesAlignments.get(i));
			}
			for(int i=0;i<propertyAlignments.size();i++)
			{
				oldPropertiesMap.add(propertyAlignments.get(i));
			}

			ruleTools.refreshAllMaps(classesAlignments,propertyAlignments);
			//HeuristicRule_Tools ruleTools=new HeuristicRule_Tools(classAlignmentPath,propertyAlignmentPath);
			/**
			 * find candidate maps among classes
			 */
			// bfw_Result.append("当前迭代次数为："+iteration+" 概念匹配个数为："+classesAlignments.size()+" 属性匹配个数为："+propertyAlignments.size()+"\n");
			if(iteration>0)
			{
				tic=System.currentTimeMillis();
				//fathers=ruleTools.fatherRule(classes1,classes2,superclasses1,superclasses2);
				fathers=ruleTools.fatherRule2(subclasses1,subclasses2);
				System.out.println("*************************************");
				System.out.println(fathers.size());
				toc=System.currentTimeMillis();
				//bfw_Result.append("当前迭代次数为："+iteration+" fatherRule计算消耗的时间为："+(toc-tic)+"ms"+"\n");
		 		//bfw_Result.append("当前迭代次数为："+iteration+" fathers的大小为："+fathers.size()+"\n");

				tic=System.currentTimeMillis();
				//children=ruleTools.childrenRule(classes1,classes2,subclasses1,subclasses2);
				children=ruleTools.childrenRule2(superclasses1,superclasses2);
				System.out.println("*************************************");
				System.out.println(children.size());
				toc=System.currentTimeMillis();
			 	//bfw_Result.append("当前迭代次数为："+iteration+" childrenRule计算消耗的时间为："+(toc-tic)+"ms"+"\n");
			 	//bfw_Result.append("当前迭代次数为："+iteration+" children的大小为："+children.size()+"\n");

				tic=System.currentTimeMillis();
				//siblings=ruleTools.siblingsRule(classes1,classes2,siblings1,siblings2);
				siblings=ruleTools.siblingsRule2(siblings1,siblings2);
				System.out.println("*************************************");
				System.out.println(siblings.size());
				toc=System.currentTimeMillis();
		 		//bfw_Result.append("当前迭代次数为："+iteration+" siblingsRule计算消耗的时间为："+(toc-tic)+"ms"+"\n");
		 		//bfw_Result.append("当前迭代次数为："+iteration+" siblings的大小为："+siblings.size()+"\n");

				tic=System.currentTimeMillis();
				//partOf=ruleTools.partOfRule(classes1,classes2,partOf1,partOf2);
				hasPart=ruleTools.hasPartRule2(hasPart1,hasPart2);
				System.out.println("*************************************");
				System.out.println(hasPart.size());
				toc=System.currentTimeMillis();
		 		//bfw_Result.append("当前迭代次数为："+iteration+" hasPart计算消耗的时间为："+(toc-tic)+"ms"+"\n");
		 		//bfw_Result.append("当前迭代次数为："+iteration+" hasPart的大小为："+hasPart.size()+"\n");
				
				tic=System.currentTimeMillis();
				//partOf=ruleTools.partOfRule(classes1,classes2,partOf1,partOf2);
				partOf=ruleTools.partOfRule2(partOf1,partOf2);
				System.out.println("*************************************");
				System.out.println(partOf.size());
				/*for(int t=0;t<partOf.size();t++)
				{
					bfw_partOf.append(partOf.get(t)+"\n");
				}*/
				toc=System.currentTimeMillis();
		 		//bfw_Result.append("当前迭代次数为："+iteration+" partOf计算消耗的时间为："+(toc-tic)+"ms"+"\n");
		 		//bfw_Result.append("当前迭代次数为："+iteration+" partOf的大小为："+partOf.size()+"\n");

				tic=System.currentTimeMillis();
				ArrayList<String> lowerCaseClasses1=changeToLowerCase(classes1);
				ArrayList<String> lowerCaseClasses2=changeToLowerCase(classes2);
				domains=ruleTools.domainRule(lowerCaseClasses1,lowerCaseClasses2,objectRelations1,objectRelations2);
				System.out.println("*************************************");
				System.out.println(domains.size());
				toc=System.currentTimeMillis();
			 	//bfw_Result.append("当前迭代次数为："+iteration+" domainRule的个数大小为："+domains.size()+"\n");
			 	//bfw_Result.append("当前迭代次数为："+iteration+" domainRule计算消耗的时间为："+(toc-tic)+"ms"+"\n");

				tic=System.currentTimeMillis();
				ranges=ruleTools.rangeRule(lowerCaseClasses1,lowerCaseClasses2,objectRelations1,objectRelations2);
				System.out.println("*************************************");
				System.out.println(ranges.size());
				toc=System.currentTimeMillis();
		 		//bfw_Result.append("当前迭代次数为："+iteration+" rangeRule的个数大小为："+ranges.size()+"\n");
		 		//bfw_Result.append("当前迭代次数为："+iteration+" rangeRule计算消耗的时间为："+(toc-tic)+"ms"+"\n");

				tic=System.currentTimeMillis();
				datatype=ruleTools.dataTypeRule(lowerCaseClasses1,lowerCaseClasses2,dataRelations1,dataRelations2);
				System.out.println("*************************************");
				System.out.println(datatype.size());
				toc=System.currentTimeMillis();
		 		//bfw_Result.append("当前迭代次数为："+iteration+" dataTypeRule计算消耗的时间为："+(toc-tic)+"ms"+"\n");

				tic=System.currentTimeMillis();
				disjointSim=ruleTools.disjointRule(lowerCaseClasses1, lowerCaseClasses2, subclasses1, subclasses2, superclasses1, superclasses2, disjoint1, disjoint2);
				//disjointSim=ruleTools.disjointRule(lowerCaseClasses1, lowerCaseClasses2, subclasses1, subclasses2, disjoint1, disjoint2);
				System.out.println("*************************************");
				System.out.println(disjointSim.size());
				toc=System.currentTimeMillis();
		 		//bfw_Result.append("当前迭代次数为："+iteration+" disjointRule计算消耗的时间为："+(toc-tic)+"ms"+"\n");
			}


			//这里要将属性的规则加上,最初是没有的，因为Class与属性的Map均为空
			TreeMap_Tools fatherRule=new TreeMap_Tools(fathers);
			TreeMap_Tools childrenRule=new TreeMap_Tools(children);
			TreeMap_Tools siblingsRule=new TreeMap_Tools(siblings);
			TreeMap_Tools partOfRule=new TreeMap_Tools(partOf);
			TreeMap_Tools hasPartRule=new TreeMap_Tools(hasPart);
			TreeMap_Tools domainsRule=new TreeMap_Tools(domains);
			TreeMap_Tools rangesRule=new TreeMap_Tools(ranges);
			TreeMap_Tools datatypeRule=new TreeMap_Tools(datatype);


			tic=System.currentTimeMillis();
			//SPNProcessing action=new SPNProcessing();
			//HashMap<String, Integer[]> Assignments=new HashMap<String, Integer[]>();//定义为全局的或者省空间
			ArrayList<String> roughMap=new ArrayList<String>();
			int classPairSize=InstanceSim.size();
			for(int i=0;i<classPairSize;i++)
			{
				/**
				 *  combine the lexical similarities of each pair
				 */
				double S0=0;
				//i=600;
				double editSimValue1=getTripleValue(editSimClass.get(i));
				double editSimValue2= getTripleValue(newEditSimClass.get(i));
				double editSimValue=Math.max(editSimValue1, editSimValue2);
				int editSize=getEditValue(editSimClass.get(i));
				double semanticSimValue1=getTripleValue(semanticSimClass.get(i));
				double semanticSimValue2=getTripleValue(newSemanticSimClass.get(i));
				double semanticSimValue=Math.max(semanticSimValue1, semanticSimValue2);
				double tfidfSimValue=getTripleValue(tfidfSim.get(i));
				String conceptPairs[]={};
				if(semanticSimValue==semanticSimValue1)
					conceptPairs=semanticSimClass.get(i).split(",");
				else
					conceptPairs=newSemanticSimClass.get(i).split(",");
				/*String concept1=conceptPairs[3];
				String concept2=conceptPairs[4];
				int length1=tool.tokeningWord(concept1).split(" ").length;
				int length2=tool.tokeningWord(concept2).split(" ").length;*/
				int length1=Integer.parseInt(conceptPairs[3]);
				int length2=Integer.parseInt(conceptPairs[4]);
				
				if(length1==1&&length2==1)
					S0=Math.max(editSimValue, semanticSimValue);
				else if(length1==length2&&length1!=1)//组合词，长度相等的比较
					S0=Math.max(editSimValue, Math.max(semanticSimValue, tfidfSimValue));
				else
					S0=Math.max(semanticSimValue, tfidfSimValue);
				
				if(editSimValue==1||(editSize==1&&semanticSimValue>=0.9))  //部分切词的问题统一进行处理,还有可能出现1个词的情况
					S0=1.0;
				
/*				if(length1==1&&length2==1)//单个词的比较，主要看编辑距离与语义相似度
					S0=0.7*Math.max(editSimValue, semanticSimValue);
				else if(length1==length2&&length1!=1)//组合词，长度相等的比较
				//	S0=0.7*Math.max(semanticSimValue, tfidfSimValue);
					S0=0.7*Math.max(editSimValue, Math.max(semanticSimValue, tfidfSimValue));
				else  if(editSimValue!=1)//长度不相等.如果有词组的比较,由于组合来切词的时候会造成一定相似度的压缩
					S0=Math.min(0.8* Math.max(semanticSimValue, tfidfSimValue), 0.7);//最大也不能超出编辑距离的0.7！
					//S0=0.8* Math.max(semanticSimValue, tfidfSimValue);
					//S0=0.8*Math.max(editSimValue, Math.max(semanticSimValue, tfidfSimValue));				
				if(editSimValue==1||(editSize==1&&semanticSimValue>=1-0.0000001))  //部分切词的问题统一进行处理,还有可能出现1个词的情况
					S0=Math.max(0.7000001, S0+0.0000001);*/
				/**	
				 * Use Instances similarity and Disjoint similarity to calculate the assignment of M in SPN.
				 */

				String pairInstanceValue[]=InstanceSim.get(i).split(",");
				String pairDisjointValue[]=disjointSim.get(i).split(",");
				String instance=pairInstanceValue[2];
				String disjoint=pairDisjointValue[2];
				Assignments.clear();
				Integer assignmentM[]={1,1,1};
				/*System.out.println(instance.equals("1"));
				System.out.println(disjoint.equals("*"));*/
				if(instance.equals("1")&&disjoint.equals("0"))
				{
					Integer assignmentD[]={0,1};
					Integer assignmentI[]={1,0,0};
					Assignments.put("D", assignmentD);
					Assignments.put("I", assignmentI);
				}
				else if(instance.equals("1")&&disjoint.equals("1"))
				{
					Integer assignmentD[]={1,0};
					Integer assignmentI[]={1,0,0};
					Assignments.put("D", assignmentD);
					Assignments.put("I", assignmentI);
				}
				else if(instance.equals("1")&&disjoint.equals("*"))
				{
					Integer assignmentD[]={1,1};
					Integer assignmentI[]={1,0,0};
					Assignments.put("D", assignmentD);
					Assignments.put("I", assignmentI);
				}
				else if(instance.equals("0")&&disjoint.equals("0"))
				{
					Integer assignmentD[]={0,1};
					Integer assignmentI[]={0,1,0};
					Assignments.put("D", assignmentD);
					Assignments.put("I", assignmentI);
				}
				else if(instance.equals("0")&&disjoint.equals("1"))
				{
					Integer assignmentD[]={1,0};
					Integer assignmentI[]={0,1,0};
					Assignments.put("D", assignmentD);
					Assignments.put("I", assignmentI);
				}
				else if(instance.equals("0")&&disjoint.equals("*"))
				{
					Integer assignmentD[]={1,1};
					Integer assignmentI[]={0,1,0};
					Assignments.put("D", assignmentD);
					Assignments.put("I", assignmentI);
				}
				else if(instance.equals("*")&&disjoint.equals("0"))
				{
					Integer assignmentD[]={0,1};
					Integer assignmentI[]={0,0,1};
					Assignments.put("D", assignmentD);
					Assignments.put("I", assignmentI);
				}
				else if(instance.equals("*")&&disjoint.equals("1"))
				{
					Integer assignmentD[]={1,0};
					Integer assignmentI[]={0,0,1};
					Assignments.put("D", assignmentD);
					Assignments.put("I", assignmentI);
				}
				else //全部未知的情况
				{
					Integer assignmentD[]={1,1};
					Integer assignmentI[]={0,0,1};
					Assignments.put("D", assignmentD);
					Assignments.put("I", assignmentI);
				}
				Assignments.put("M", assignmentM);
				//action=new SPNProcessing();//执行怎么将初始值还原
				ArrayList<String> newAssignments=action.process(Assignments);
				String dAssign="",mAssign="";
				if(newAssignments.size()==1)//D是已经指派了情况
				{
					dAssign="D"+Integer.parseInt(disjoint);
					mAssign=newAssignments.get(0);		
				}
				else//D是unknown的情况
				{
					dAssign=newAssignments.get(0);
					mAssign=newAssignments.get(1);
				}
			    double  A=a;
			     //a=0.7;
			    if(length1!=length2&&editSimValue!=1)
			    	A=A+0.1;
/*				if(length1>length2&&editSimValue!=1)
				{
					double offset=((float)length1-(float)length2)*(length1-1)/Math.pow(length1, 2);
					a=a*(1+offset);
				}
				else if(length2>length1&&editSimValue!=1)
				{
					double offset=((float)length2-(float)length1)*(length2-1)/Math.pow(length2, 2);
					a=a*(1+offset);
				}*/

				//SPN的后续操作
				//根据概念长度计算不同的补偿系数
			/*	double offset=Math.abs((float)length1-(float)length2)/Math.max(length1,length2);
				offset=1+Math.pow(offset, 2);
				S0=S0*offset;*/
				
			    S0=Math.min(A*S0,a);
			    
			    
				if(dAssign.equals("D1"))
					S0=0;
				else if(dAssign.equals("D0")&&mAssign.equals("M1"))
				{				
					S0=Math.min(S0+contribution, 1);
				}
				else if(dAssign.equals("D0")&&mAssign.equals("M2"))
					S0=Math.max(S0-contribution, 0);
					
				
		/*		if(length1!=length2) //补偿机制
					S0=S0;*/
				//S0=Math.min(S0,a);

				/**
				 * Use heuristic rules to improve the similarity in Noisy-OR model
				 */
				String pairName=pairInstanceValue[0]+","+pairInstanceValue[1];
				double finalPositive=0;
				if(S0==0)   //如果满足不相交性质，则后面其实无需计算，因为相似度为0
					finalPositive=0;
				else
				{			
					double N1=0;
					if(satisfiedNum(fatherRule,pairName)>0)
						N1=1/(1+Math.exp(-satisfiedNum(fatherRule,pairName)));//这里用sigmoid函数来限制它的上限
					double N2=0;
					if(satisfiedNum(childrenRule,pairName)>0)
						N2=1/(1+Math.exp(-satisfiedNum(childrenRule,pairName)));
					double N3=0;//兄弟结点，往往第一次可信度并不高
					if(satisfiedNum(siblingsRule,pairName)>0)
						N3=1/(1+Math.exp(-satisfiedNum(siblingsRule,pairName)+1));
					double N4=0;
					if(satisfiedNum(domainsRule,pairName)>0)
						N4=1/(1+Math.exp(-satisfiedNum(domainsRule,pairName)));
					double N5=0;
					if(satisfiedNum(rangesRule,pairName)>0)
						N5=1/(1+Math.exp(-satisfiedNum(rangesRule,pairName)));
					double N6=0;
					if(satisfiedNum(datatypeRule,pairName)>0)
						N6=1/(1+Math.exp(-satisfiedNum(datatypeRule,pairName)));
					double N7=0;
					if(satisfiedNum(partOfRule,pairName)>0)
						N7=1/(1+Math.exp(-satisfiedNum(partOfRule,pairName)));
					double N8=0;
					if(satisfiedNum(hasPartRule,pairName)>0)
						N8=1/(1+Math.exp(-satisfiedNum(hasPartRule,pairName)));
					/*double N4=satisfiedNum(domainsRule,pairName);
					double N5=satisfiedNum(rangesRule,pairName);*/
					//1-Math.sin(Math.acos(value));


					//这里的Si指的是对应规则发生的概率（但每个规则都有影响最终匹配结果的上界）
					//注意父对子比子对父要大,1次就帮助0.7的匹配对冲破阈值
					//子对父相当 3次就帮助0.7的匹配对冲破阈值
					//兄弟结点有时候并不可信，多一次会造成很多本质上的区别
					//定义域与值域影响相当，但比上述都小 无法就帮助0.7的匹配对冲破阈值
					//最小则是Datatype，很多都是由于String类型才匹配的 无法就帮助0.7的匹配对冲破阈值
					double S1=0.45,S2=0.35,S3=0.35,S4=0.3,S5=0.3,S6=0.2,S7=0.35,S8=0.45;
					double finalNegative=(1-S0)*Math.pow(1-S1, N1)*Math.pow(1-S2, N2)*Math.pow(1-S3, N3)*
							Math.pow(1-S4, N4)*Math.pow(1-S5, N5)*Math.pow(1-S6, N6)*Math.pow(1-S7, N7)*Math.pow(1-S8, N8);
					finalPositive=1-finalNegative;
				}

				if(finalPositive>=threshold)
				{
					//System.out.println(pairName+","+finalPositive);
					roughMap.add(pairName+","+finalPositive+","+length1+","+length2);
				}
				else if(iteration==0&&finalPositive>=a*0.85&&finalPositive<=a)//很多隐藏的点，由于知识不完备（实例未给出，关系未给出），导致其无法被识别
				{
					/*String label1=classLabels1.get(pairInstanceValue[0].toLowerCase());
					String label2=classLabels2.get(pairInstanceValue[1].toLowerCase());
					System.out.println(label1+","+label2+","+finalPositive);
					bfw_middle.append(label1+","+label2+","+finalPositive+"\n");*/
					hiddenClassesMap.add(pairName+","+finalPositive+","+length1+","+length2);
				}
				/*ArrayList<String> hiddenClassesMap=new ArrayList<String>();
				ArrayList<String> hiddenPropertiesMap=new ArrayList<String>();*/
			}
			toc=System.currentTimeMillis();
		 	//bfw_Result.append("当前迭代次数为："+iteration+" SPN+Noisy_OR模型计算消耗的时间为："+(toc-tic)/1000+"s"+"\n");
			//将原来的补上(false针对的是属性，true针对的是class)
			roughMap=addMaps(classesMap,roughMap); //classesMap加到roughMap中去。一般后者比前者大，主要是一种更新
			//System.out.println("候选匹配对的个数为:"+roughMap.size());
			for(int i=0;i<roughMap.size();i++)
			{
				String parts[]=roughMap.get(i).split(",");
				String label1=classLabels1.get(parts[0]);
				String label2=classLabels2.get(parts[1]);
				//System.out.println(label1+","+label2+","+parts[2]);
				//System.out.println(roughMap.get(i));
			}

			//Refine(十字交叉和1对1的限制)	
			ArrayList<String> refinedMap1=new ArrayList<String>();
			tic=System.currentTimeMillis();
			refinedMap1=refineTools.removeCrissCross(roughMap, superclasses1, superclasses2);	
			//System.out.println("基于十字交叉精炼后的匹配对的个数为:"+refinedMap1.size());
			toc=System.currentTimeMillis();
			//bfw_Result.append("当前迭代次数为："+iteration+" 基于概念十字交叉精炼计算消耗的时间为："+(toc-tic)+"ms"+"\n");
			for(int i=0;i<refinedMap1.size();i++)
			{
				String parts[]=refinedMap1.get(i).split(",");
				String label1=classLabels1.get(parts[0]);
				String label2=classLabels2.get(parts[1]);
				//System.out.println(label1+","+label2+","+parts[2]);
				//System.out.println(refinedMap1.get(i));
			}

			ArrayList<String> refinedMap2=new ArrayList<String>();
			tic=System.currentTimeMillis();
			refinedMap2=refineTools.keepOneToOneAlignment(refinedMap1);	
			//System.out.println("基于1对1的精炼后的匹配对的个数为:"+refinedMap2.size());
			toc=System.currentTimeMillis();
			//bfw_Result.append("当前迭代次数为："+iteration+" 基于One-One精炼计算消耗的时间为："+(toc-tic)+"ms"+"\n");
			classesMap.clear();
			for(int i=0;i<refinedMap2.size();i++)
			{
				String part[]=refinedMap2.get(i).split(",");
				if(Double.parseDouble(part[2])>0)
				{
					/*String parts[]=refinedMap2.get(i).split(",");
					String label1=classLabels1.get(parts[0]);
					String label2=classLabels2.get(parts[1]);
					System.out.println(label1+","+label2+","+parts[2]);*/
					
					//System.out.println(refinedMap2.get(i));
					classesMap.add(refinedMap2.get(i));
				}			
			}

			/**
			 * find candidate maps among properties
			 */
			classesAlignments=changeToAlignments(classesMap);
			ruleTools.refreshClassMaps(classesAlignments);
			ArrayList<String> lowerCaseObjectProperties1=changeToLowerCase(objectProperties1);
			ArrayList<String> lowerCaseObjectProperties2=changeToLowerCase(objectProperties2);
			ArrayList<String> objectProperties=ruleTools.objectPropertyRule(lowerCaseObjectProperties1, lowerCaseObjectProperties2, objectRelations1, objectRelations2);
			//System.out.println("*************************************");
			//System.out.println(objectProperties.size());

			ArrayList<String> lowerCaseDataProperties1=changeToLowerCase(dataProperties1);
			ArrayList<String> lowerCaseDataProperties2=changeToLowerCase(dataProperties2);
			ArrayList<String> dataProperties=ruleTools.dataPropertyRule(lowerCaseDataProperties1, lowerCaseDataProperties2, dataRelations1, dataRelations2);
			//System.out.println("*************************************");
			//System.out.println(dataProperties.size());

			TreeMap_Tools OPrule=new TreeMap_Tools(objectProperties);
			TreeMap_Tools DPrule=new TreeMap_Tools(dataProperties);

			ArrayList<String> roughObjectPropertyMap=new ArrayList<String>();


			/*ArrayList<String> editSimObjectProperty=tool.editSimProperty(objectProperties1, objectProperties2);			
			ArrayList<String> SemanticSimObjectProperty=tool.semanticSimProperty(objectProperties1, objectProperties2);
			ArrayList<String> editSimDatatypeProperty=tool.editSimProperty(objectProperties1, objectProperties2);		
			ArrayList<String> SemanticSimDatatypeProperty=tool.semanticSimProperty(objectProperties1, objectProperties2);*/

			int objectPropertyPairSize=editSimObjectProperty.size();
			for(int i=0;i<objectPropertyPairSize;i++)
			{
				double P0=0;
				double editSimValue=getTripleValue(editSimObjectProperty.get(i));
				double semanticSimValue=getTripleValue(SemanticSimObjectProperty.get(i));			
				P0=0.7*Math.max(editSimValue, semanticSimValue);//pooling的方法更合适一些吧
				if(editSimValue==1)//指得是相同的字符串的优势
					P0=P0+0.0000001;
				/*if(editSimValue==semanticSimValue&&semanticSimValue>0)//指得是相同的字符串的优势
					P0=P0+0.0000001;*/
				//P0=Math.min(Math.max(editSimValue, semanticSimValue), 0.7);//pooling的方法更合适一些吧

				/**
				 * Use heuristic rules to improve the similarity of properties in Noisy-OR model
				 */
				String part[]=editSimObjectProperty.get(i).split(",");
				String pairName=objectPropertyPair.get(i);						

				double M1=0;
				if(satisfiedNum(OPrule,pairName)>0)
					M1=1/(1+Math.exp(-satisfiedNum(OPrule,pairName))); //基于定义域、值域的匹配并用sigmoid函数限制它的上限						
				double S1=0.2;//必须要2次才能冲破阈值0.75。		
				double finalNegative=(1-P0)*Math.pow(1-S1, M1);
				double finalPositive=1-finalNegative;
				if(finalPositive>=0.75)
				{
					//System.out.println(pairName+","+finalPositive);
					roughObjectPropertyMap.add(pairName+","+finalPositive+","+part[3]+","+part[4]+","+part[5]+","+part[6]);//应该保留5维信息
				}	
				else if(finalPositive>0.7&&finalPositive<0.75&&iteration==0)//很多隐藏的点，由于知识不完备（实例未给出，关系未给出），导致其无法被识别
				{
					hiddenObjectPropertiesMap.add(pairName+","+finalPositive+","+part[3]+","+part[4]+","+part[5]+","+part[6]);
				}
			}
			//将原来的补上(false针对的是属性，true针对的是class)
			roughObjectPropertyMap=addMaps(objectPropertiesMap,roughObjectPropertyMap);//objectPropertiesMap加到roughObjectPropertyMap中去。一般后者比前者大，主要是一种更新
			/*System.out.println("候选属性的匹配对个数为:"+roughObjectPropertyMap.size());	
			for(int i=0;i<roughObjectPropertyMap.size();i++)
			{
				String part[]=roughObjectPropertyMap.get(i).split(",");
				System.out.println(part[0]+","+part[1]+","+part[2]);
			}*/
			//基于属性匹配对的一个Refine的过程
			ArrayList<String> refinedObjectPropertyMap1=new ArrayList<String>();

			//用更新后的classMap来精炼propertyMap
			refinedObjectPropertyMap1=refineTools.removeCrissCrossInProperty(roughObjectPropertyMap,classesAlignments,objectRelations1,objectRelations2);	
			//refinedPropertyMap1=refineTools.removeCrissCrossInProperty(roughPropertyMap,classAlignmentPath,relations1,relations2);	
			/*System.out.println("过滤基于定义域与值域反向匹配的个数为:"+refinedObjectPropertyMap1.size());
			for(int i=0;i<refinedObjectPropertyMap1.size();i++)
			{
				String part[]=refinedObjectPropertyMap1.get(i).split(",");
				System.out.println(part[0]+","+part[1]+","+part[2]);
			}*/

			//propertiesInverse1; editSimProperty后两维提供信息来进行过滤
			ArrayList<String> refinedObjectPropertyMap2=new ArrayList<String>();

			//stemObjectPropery=tool.findStemPair(refinedObjectPropertyMap1);
			refinedObjectPropertyMap2=refineTools.removeStemConflict(refinedObjectPropertyMap1,propertiesInverse1,propertiesInverse2);	
			/*System.out.println("过滤基于inverse情况的匹配:"+refinedObjectPropertyMap2.size());
			for(int i=0;i<refinedObjectPropertyMap2.size();i++)
			{
				System.out.println(refinedObjectPropertyMap2.get(i));
			}*/

			ArrayList<String> refinedObjectPropertyMap3=new ArrayList<String>();
			refinedObjectPropertyMap3=refineTools.keepOneToOneAlignment(refinedObjectPropertyMap2);	
			//System.out.println("基于1对1的精炼后的匹配对的个数为:"+refinedObjectPropertyMap3.size());
			propertiesMap.clear();
			objectPropertiesMap.clear();
			for(int i=0;i<refinedObjectPropertyMap3.size();i++)
			{
				String part[]=refinedObjectPropertyMap3.get(i).split(",");
				if(Double.parseDouble(part[2])>0)
				{
					/*System.out.println(refinedObjectPropertyMap3.get(i));
					String propertyMapPair=part[0]+"--"+part[1];
					objectPropertiesMap.add(propertyMapPair);
					propertiesMap.add(propertyMapPair);	*/	
					objectPropertiesMap.add(refinedObjectPropertyMap3.get(i));
					propertiesMap.add(refinedObjectPropertyMap3.get(i));				
				}
			}

			ArrayList<String> roughDataPropertyMap=new ArrayList<String>();
			int dataPropertyPairSize=editSimDatatypeProperty.size();
			for(int i=0;i<dataPropertyPairSize;i++)
			{
				double P0=0;
				double editSimValue=getTripleValue(editSimDatatypeProperty.get(i));
				double semanticSimValue=getTripleValue(SemanticSimDatatypeProperty.get(i));			
				P0=0.7*Math.max(editSimValue, semanticSimValue);//pooling的方法更合适一些吧
				if(editSimValue==1)//指得是相同的字符串的优势
					P0=P0+0.0000001;
				/*if(editSimValue==semanticSimValue&&semanticSimValue>0)//指得是相同的字符串的优势
					P0=P0+0.0000001;*/
				//P0=Math.min(Math.max(editSimValue, semanticSimValue), 0.7);//pooling的方法更合适一些吧

				/**
				 * Use heuristic rules to improve the similarity of properties in Noisy-OR model
				 */
				String part[]=editSimDatatypeProperty.get(i).split(",");
				String pairName=dataPropertyPair.get(i);						

				double M2=0;
				if(satisfiedNum(DPrule,pairName)>0)
					M2=1/(1+Math.exp(-satisfiedNum(DPrule,pairName)));//基于DataType的匹配，并用sigmoid函数限制它的上限

				double S2=0.18;
				//必须要3次才能冲破阈值0.75。	
				double finalNegative=(1-P0)*Math.pow(1-S2, M2);
				double finalPositive=1-finalNegative;
				if(finalPositive>=0.75)
				{
					//System.out.println(pairName+","+finalPositive);
					roughDataPropertyMap.add(pairName+","+finalPositive+","+part[3]+","+part[4]+","+part[5]+","+part[6]);//应该保留5维信息
				}	
				else if(finalPositive>0.7&&finalPositive<0.75&&iteration==0)//很多隐藏的点，由于知识不完备（实例未给出，关系未给出），导致其无法被识别
				{
					hiddenDataPropertiesMap.add(pairName+","+finalPositive+","+part[3]+","+part[4]+","+part[5]+","+part[6]);
				}
			}
			//将原来的补上(false针对的是属性，true针对的是class)
			roughDataPropertyMap=addMaps(dataPropertiesMap,roughDataPropertyMap);//dataPropertiesMap加到roughDataPropertyMap中去。一般后者比前者大，主要是一种更新
			/*System.out.println("候选属性的匹配对个数为:"+roughDataPropertyMap.size());	
			for(int i=0;i<roughDataPropertyMap.size();i++)
			{
				String part[]=roughDataPropertyMap.get(i).split(",");
				System.out.println(part[0]+","+part[1]+","+part[2]);
			}*/
			//基于数值属性匹配对的一个Refine的过程		
			ArrayList<String> refinedPropertyMap=new ArrayList<String>();
			refinedPropertyMap=refineTools.keepOneToOneAlignment(roughDataPropertyMap);	
			//System.out.println("基于1对1的精炼后的匹配对的个数为:"+refinedPropertyMap.size());
			dataPropertiesMap.clear();
			for(int i=0;i<refinedPropertyMap.size();i++)
			{
				String part[]=refinedPropertyMap.get(i).split(",");
				if(Double.parseDouble(part[2])>0)
				{
					/*System.out.println(refinedPropertyMap.get(i));
					String propertyMapPair=part[0]+"--"+part[1];
					dataPropertiesMap.add(propertyMapPair);
					propertiesMap.add(propertyMapPair);	*/	
					dataPropertiesMap.add(refinedPropertyMap.get(i));
					propertiesMap.add(refinedPropertyMap.get(i));
				}
			}

			//如果概念的匹配对，与属性的匹配对不再发生变化，或者迭代次数已经有5次，跳出循环。	
			propertyAlignments=changeToAlignments(propertiesMap);
			flag=unChange(classesAlignments,propertyAlignments,oldClassesMap,oldPropertiesMap);
			////如果为空的话，需要将一些看起来十分合适的点进行导入，这相当于人工指定
			if(classesMap.size()==0)
			{
				tic=System.currentTimeMillis();
				needComplementClass=true;		
				String type="class";
				classesMap=complementMaps(classesMap,hiddenClassesMap,type);
				//classesMap=complementMaps(classesMap,hiddenClassesMap,type,needComplementClass);
				needComplementClass=false;
				toc=System.currentTimeMillis();
				//bfw_Result.append("当前迭代次数为："+iteration+" 补充缺省first-liner_class消耗的时间为："+(toc-tic)/1000+"s"+"\n");
			}
			if(propertiesMap.size()==0)
			{
				tic=System.currentTimeMillis();
				String type="property";
				//needComplementProperty=true;
				objectPropertiesMap=complementMaps(objectPropertiesMap,hiddenObjectPropertiesMap,type);
				objectPropertiesMap=refineTools.removeStemConflict(objectPropertiesMap,propertiesInverse1,propertiesInverse2);
				objectPropertiesMap=refineTools.keepOneToOneAlignment(objectPropertiesMap);	
				propertiesMap=addMaps(propertiesMap,objectPropertiesMap);//前面是老的，后面是新的

				dataPropertiesMap=complementMaps(dataPropertiesMap,hiddenDataPropertiesMap,type);
				dataPropertiesMap=refineTools.keepOneToOneAlignment(dataPropertiesMap);
				propertiesMap=addMaps(propertiesMap,dataPropertiesMap);//前面是老的，后面是新的
				needComplementProperty=false;
				toc=System.currentTimeMillis();
				//bfw_Result.append("当前迭代次数为："+iteration+" 补充缺省first-liner_property消耗的时间为："+(toc-tic)/1000+"s"+"\n");
			}
			iteration++;
			Boolean jump=!(iteration<4&&flag==false);
			if(jump==true)
			{
				if(needComplementClass==true)
				{
					classesMap=complementMaps(classesMap,hiddenClassesMap,"class");
					classesMap=refineTools.removeCrissCross(classesMap, superclasses1, superclasses2);
				}
				else//这里的refine只是剪枝，不会增加,因为已经添加过了，所以进行了1对1的限制操作
					classesMap=newRefineClass(classesMap);	
					//classesMap=refineClass(classesMap,classes1,classes2);	
				//如果一开始没有补充，则为在循环内就会补充好,needComplementProperty的值则会变为false,这里则是之前被激活，而一直没补充的情况
				if(needComplementProperty==true)
				{
					propertiesMap=complementMaps(propertiesMap,hiddenObjectPropertiesMap,"property");
					classesAlignments=changeToAlignments(classesMap);
					propertiesMap=refineTools.removeCrissCrossInProperty(propertiesMap,classesAlignments,objectRelations1,objectRelations2);
					propertiesMap=refineTools.removeStemConflict(propertiesMap,propertiesInverse1,propertiesInverse2);

					propertiesMap=complementMaps(propertiesMap,hiddenDataPropertiesMap,"property");								
				}
				/*else   //暂时不需要考虑
				propertiesMap=refineProperty(propertiesMap);*/
			}
			iteration_toc=System.currentTimeMillis();
		 	//bfw_Result.append("当前迭代次数为："+(iteration-1)+" 迭代消耗的时间为："+(iteration_toc-iteration_tic)/1000+"s"+"\n");
		}while(iteration<4&&flag==false);
		//最后，也需要进行适当的补充

		//考虑到信息不完备的时候，将这些潜藏的匹配进行补充(这个过程如果能省略也应该能节约不少时间)
		/*if(needComplementClass==true)
			classesMap=complementMaps(classesMap,hiddenClassesMap,"class");
		else
			classesMap=refineClass(classesMap);
		//如果一开始没有补充，则为在循环内就会补充好,needComplementProperty的值则会变为false,这里则是之前被激活，而一直没补充的情况
		if(needComplementProperty==true)
			propertiesMap=complementMaps(propertiesMap,hiddenObjectPropertiesMap,"property");
		else
			propertiesMap=refineProperty(propertiesMap);*/
		/*ArrayList<String> EquivalentClass2=onto2.GetEquivalentClass();
		ArrayList<String> EquivalentProperty2=onto2.GetEquivalentProperty();*/
		classesMap=enhancedMap(classesMap,EquivalentClass1);
		classesMap=enhancedMap(classesMap,EquivalentClass2);
		propertiesMap=enhancedMap(propertiesMap,EquivalentProperty1);
		propertiesMap=enhancedMap(propertiesMap,EquivalentProperty2);

/*		String alignmentPath="Results/"+ontologyName1+"-"+ontologyName2;
		OAEIAlignmentOutput out=new OAEIAlignmentOutput(alignmentPath,ontologyName1,ontologyName2);
		for(int i=0;i<classesMap.size();i++)
		{
			String parts[]=classesMap.get(i).split(",");
			out.addMapping2Output(parts[0],parts[1]);
		}
		for(int i=0;i<propertiesMap.size();i++)
		{
			String parts[]=propertiesMap.get(i).split(",");
			out.addMapping2Output(parts[0],parts[1]);
		}
		out.saveOutputFile();*/
		

	/*	System.out.println("最终的概念匹配对为：");
		for(int i=0;i<classesMap.size();i++)
		{
			System.out.println(classesMap.get(i));
		}
		System.out.println("找到概念匹配对的个数为："+classesMap.size());*/

		/*System.out.println("最终的属性匹配对为：");
		for(int i=0;i<propertiesMap.size();i++)
		{
			System.out.println(propertiesMap.get(i));
		}*/
		//System.out.println("找到属性匹配对的个数为："+propertiesMap.size());

		/*ArrayList<String> classAlignments=changeToAlignments(classesMap);
		ArrayList<String> propertyAlignments=changeToAlignments(propertiesMap);*/

		System.out.println("The result of  "+ontologyName1+" and "+ontologyName2+" ：");
		bfw_Result.append("The result of  "+ontologyName1+" and "+ontologyName2+" ："+"\n");
		//printResult(classAlignments,propertyAlignments,resultClassMap,resultPropertyMap);
		if(labelflag==true)
		{
			//只是为打印医学本体提供方法
			printAnatomyResult(classesMap,propertiesMap,classLabels1,classLabels2,resultClassMap,resultPropertyMap);
		}	
		else
		{
			printResult(classesMap,propertiesMap,resultClassMap,resultPropertyMap);
		}
		System.out.println(ontologyName1+" "+ontologyName2+" has been done!");
		bfw_Result.append("\n\n");
		
		}//1层循环I3CON

	/*	 }//2层循环Conference、Benchmark
		}*/
		/*	static double a=0.7;
		 static double contribution=0.2;
		static double threshold=0.8;	*/
		System.out.println("When alpha="+a+" lambda="+contribution +" theta="+threshold+","+"the average of f1-measure："+average_f_measure/5);
		bfw_Result.append("When alpha="+a+" lambda="+contribution +" theta="+threshold+","+"\n");
		bfw_Result.append("The average of f1-measure："+average_f_measure/5+"\n");
		toc1=System.currentTimeMillis();
		System.out.println("Time consumption："+(toc1-tic1)/1000+"s");		
		//bfw_Result.append("The average of f1-measure："+average_f_measure/5+"\n");
		bfw_Result.append("Time consumption："+(toc1-tic1)/1000+"s"+"\n");
		
		bfw_Result.close();	
		//bfw_middle.close();
	}

	/**
	 * these function are just for preproccess
	 */
	public static ArrayList<String> filterCommand(ArrayList<String> maps)
	{
		ArrayList<String> fiteredMaps=new ArrayList<String>();
		for(int i=0;i<maps.size();i++)
		{
			String a=maps.get(i);
			fiteredMaps.add(a.replace("--,", "--"));
		}
		return fiteredMaps;
	}

	public static ArrayList<String> changeToLowerCase(ArrayList<String> things)
	{
		ArrayList<String> lowCaseArrayList=new ArrayList<String>();
		for(int i=0;i<things.size();i++)
		{
			lowCaseArrayList.add(things.get(i).toLowerCase());
		}
		return lowCaseArrayList;
	}

	public static int satisfiedNum(TreeMap_Tools rule,String pair)
	{
		ArrayList<String> value=rule.GetKey_Value(pair.toLowerCase());
		if(value!=null)
		{
			return (int) Double.parseDouble(value.get(0));
		}
		return 0;
	}

	public static double getTripleValue(String triple)
	{
		String part[]=triple.split(",");
		double value=Double.parseDouble(part[2]);
		return value;
	}

	public static int getEditValue(String triple)
	{
		String part[]=triple.split(",");
		int value=Integer.parseInt(part[3]);
		return value;
	}

	public static boolean unChange(ArrayList<String> classesMap,ArrayList<String> propertiesMap,ArrayList<String> oldClassesMap, ArrayList<String> oldPropertiesMap)
	{
		boolean classUnChange=false;
		boolean propertyUnChange=false;
		if(oldClassesMap.size()!=classesMap.size())
		{
			return false;
		}
		if(oldPropertiesMap.size()!=propertiesMap.size())
		{
			return false;
		}			
		for(int i=0;i<classesMap.size();i++)
		{
			if(oldClassesMap.contains(classesMap.get(i)))
				classUnChange=true;
			else
			{
				classUnChange=false;
				break;
			}
		}
		if(propertiesMap.size()==0)
			propertyUnChange=true;
		for(int j=0;j<propertiesMap.size();j++)
		{
			if(oldPropertiesMap.contains(propertiesMap.get(j)))
				propertyUnChange=true;
			else
			{
				propertyUnChange=false;
				break;
			}
		}
		boolean change=classUnChange&&propertyUnChange;
		return change;
	}

	/*public static ArrayList<String> complementMaps(ArrayList<String> maps,ArrayList<String> hiddenmaps,boolean flag)
	{
		Refine_Tools tools=new Refine_Tools();
		ArrayList<String> preciseMaps=new ArrayList<String>();	
		if(flag==false)//这种情况的maps是为空的
		{
			for(int i=0;i<hiddenmaps.size();i++)				
			{
				maps.add(hiddenmaps.get(i));
			}
			preciseMaps=tools.keepOneToOneAlignment(maps);//针对Class的时候还是需要的。极有可能出现1对多的情况
			maps.clear();
			for(int i=0;i<preciseMaps.size();i++)
			{
				String parts[]=preciseMaps.get(i).split(",");
				if(Double.parseDouble(parts[2])>0)
					maps.add(preciseMaps.get(i));
			}
			return maps;	//注意，这里返回的格式仍需要修改
		}
		else	//默认缺省时添加的情况
		{
			//要求1对1的匹配,更相信通过模型计算出来的结果。其他的结果只能作为辅助
			preciseMaps=tools.keepOneToOneAlignment(hiddenmaps);
			for(int i=0;i<preciseMaps.size();i++)
			{
				boolean index=false;
				String parts1[]=preciseMaps.get(i).split(",");
				for(int j=0;j<maps.size();j++)
				{
					String parts2[]=maps.get(j).split(",");
					if(parts2[0].equals(parts1[0])||parts2[1].equals(parts1[1])||Double.parseDouble(parts1[2])==0)//1对1的限制
					{
						index=true;
						break;
					}
				}
				if(index==false)
				{
					System.out.println(parts1[0]+"	"+parts1[1]);
					String concept1=parts1[0];
					String concept2=parts1[1];
					int length1=tool.tokeningWord(concept1).split(" ").length;
					int length2=tool.tokeningWord(concept2).split(" ").length;
					if(length1==length2&&length1==1&&Double.parseDouble(parts1[2])>0.7)//仅名字相同才给予考虑，因为有很多WordNet计算会存在一定错误，导致召回率很低
						maps.add(preciseMaps.get(i));
					if((length1==1&&length2>1)||(length1==1&&length2>1))	//单个词与组合词的情况。
						maps.add(preciseMaps.get(i));
					if(length1>1&&length2>1&&Double.parseDouble(parts1[2])>0.6)	//组合词的token均超过2个。
						maps.add(preciseMaps.get(i));
				}
					//maps.add(parts1[0]+"--"+parts1[1]);
			}
			return maps;
		}	
	}*/

	public static ArrayList<String> complementMaps(ArrayList<String> maps,ArrayList<String> hiddenmaps,String type)
	{
		Refine_Tools tools=new Refine_Tools();
		ArrayList<String> preciseMaps=new ArrayList<String>();	
		if(type.equals("class")&&maps.size()==0)//Class为空时的补充
		{
			for(int i=0;i<hiddenmaps.size();i++)				
			{
				maps.add(hiddenmaps.get(i));
			}
			preciseMaps=tools.keepOneToOneAlignment(maps);//针对Class的时候还是需要的。极有可能出现1对多的情况
			maps.clear();
			for(int i=0;i<preciseMaps.size();i++)
			{
				String parts[]=preciseMaps.get(i).split(",");
				if(Double.parseDouble(parts[2])>0)
					maps.add(preciseMaps.get(i));
			}
			return maps;	//注意，这里返回的格式仍需要修改
		}
		else if(type.equals("class")&&maps.size()!=0)//Class不为空时的补充
		{
			preciseMaps=tools.keepOneToOneAlignment(hiddenmaps);
			for(int i=0;i<preciseMaps.size();i++)
			{
				boolean index=false;
				String parts1[]=preciseMaps.get(i).split(",");
				for(int j=0;j<maps.size();j++)
				{
					String parts2[]=maps.get(j).split(",");
					if(parts2[0].equals(parts1[0])||parts2[1].equals(parts1[1])||Double.parseDouble(parts1[2])==0)//1对1的限制
					{
						index=true;
						break;
					}
				}
				if(index==false)
				{
					System.out.println(parts1[0]+"	"+parts1[1]);
					int length1=Integer.parseInt(parts1[3]);
					int length2=Integer.parseInt(parts1[4]);
					
					/*String concept1=parts1[0];
					String concept2=parts1[1];				
					int length1=tool.tokeningWord(concept1).split(" ").length;
					int length2=tool.tokeningWord(concept2).split(" ").length;*/
					if(length1==length2&&Double.parseDouble(parts1[2])>=a)//仅名字相同才给予考虑，因为有很多WordNet计算会存在一定错误，导致召回率很低
						maps.add(preciseMaps.get(i));	
					else if(length1!=length2&&Double.parseDouble(parts1[2])>=a*0.85)
						maps.add(preciseMaps.get(i));				
/*					if(length1==length2&&length1==1&&Double.parseDouble(parts1[2])>=a)//仅名字相同才给予考虑，因为有很多WordNet计算会存在一定错误，导致召回率很低
						maps.add(preciseMaps.get(i));
					if((length1==1&&length2>1)||(length1>1&&length2==1))	//单个词与组合词的情况。
						maps.add(preciseMaps.get(i));
					if(length1>1&&length2>1&&Double.parseDouble(parts1[2])>a*0.85)	//组合词的token均超过2个。
						maps.add(preciseMaps.get(i));*/
				}
			}
			return maps;
		}
		else if((type.equals("property")&&maps.size()!=0))//属性不为空的情况
		{
			preciseMaps=tools.keepOneToOneAlignment(hiddenmaps);
			for(int i=0;i<preciseMaps.size();i++)
			{
				boolean index=false;
				String parts1[]=preciseMaps.get(i).split(",");
				for(int j=0;j<maps.size();j++)
				{
					String parts2[]=maps.get(j).split(",");
					if(parts2[0].equals(parts1[0])||parts2[1].equals(parts1[1])||Double.parseDouble(parts1[2])==0)//1对1的限制
					{
						index=true;
						break;
					}
				}
				if(index==false)//不冲突的情况添加
					maps.add(preciseMaps.get(i));
			}
			return maps;
		}
		else
			//else if(type.equals("property"))//因为最初肯定为空,后续的过程必须在循环中预先做一次精炼
		{
			for(int i=0;i<hiddenmaps.size();i++)				
			{
				maps.add(hiddenmaps.get(i));
			}
			return maps;	//注意，这里返回的格式仍需要修改
		}
	}

	public static ArrayList<String> addMaps(ArrayList<String> maps,ArrayList<String> newMaps)//前面是老的，后面是新的 classesMap,roughMap
	{
		//Sim_Tools tools=new Sim_Tools();
		ArrayList<String> preciseMaps=new ArrayList<String>();	

		for(int i=0;i<newMaps.size();i++)
		{
			preciseMaps.add(newMaps.get(i));
		}
		ArrayList<String> alignments=changeToAlignments(preciseMaps);
		for(int i=0;i<maps.size();i++)//添加新的Maps要考虑值的大小,新的比原来的值要大
		{
			String parts[]=maps.get(i).split(",");
			String conceptPair=parts[0]+"--"+parts[1];
			if(alignments.contains(conceptPair))//因为新的值一定比前者大，所以可以pass掉
			{
				continue;
			}
			else
				preciseMaps.add(maps.get(i));
		}
		return preciseMaps;	
	}

	public static ArrayList<String> refineClass(ArrayList<String> maps,ArrayList<String>Classes1,ArrayList<String> Classes2)//方便将一些最初不太可靠，但又没有在SPN与Noisy-OR中的加强的匹配对过滤掉
	{
		Refine_Tools tools=new Refine_Tools();
		ArrayList<String> lowerCaseClasses1=changeToLowerCase(Classes1);
		ArrayList<String> lowerCaseClasses2=changeToLowerCase(Classes2);
		ArrayList<String> preciseMaps=new ArrayList<String>();
		for(int i=0;i<maps.size();i++)
		{
			String parts[]=maps.get(i).split(",");	
			//因为最终的匹配都转换成了小写，必须还原之后才知道其真实的长度
			int index1=tools.find_index(lowerCaseClasses1, parts[0]);
			int index2=tools.find_index(lowerCaseClasses2, parts[1]);
			int length1=tool.tokeningWord(Classes1.get(index1)).split(" ").length;
			int length2=tool.tokeningWord(Classes2.get(index2)).split(" ").length;

			if(length1==length2&&length1==1&&Double.parseDouble(parts[2])>=a)//仅名字相同才给予考虑，因为有很多WordNet计算会存在一定错误，导致召回率很低
				preciseMaps.add(maps.get(i));
			else if(length1==length2&&length1!=1&&Double.parseDouble(parts[2])>a*0.85)//仅名字相同才给予考虑，因为有很多WordNet计算会存在一定错误，导致召回率很低
				preciseMaps.add(maps.get(i));
			else if(length1!=length2&&Double.parseDouble(parts[2])>=a*0.85)
				preciseMaps.add(maps.get(i));
		}
		return preciseMaps;
	}
	
	public static ArrayList<String> newRefineClass(ArrayList<String> maps)//方便将一些最初不太可靠，但又没有在SPN与Noisy-OR中的加强的匹配对过滤掉
	{
		//Refine_Tools tools=new Refine_Tools();
		ArrayList<String> preciseMaps=new ArrayList<String>();
		for(int i=0;i<maps.size();i++)
		{
			String parts[]=maps.get(i).split(",");	
			int length1=Integer.parseInt(parts[3]);
			int length2=Integer.parseInt(parts[4]);
			if(length1==length2&&length1==1&&Double.parseDouble(parts[2])>=a)//对于字符长度为1（语义相似度为0.9） 但并无太多增长的情况
				preciseMaps.add(maps.get(i));
			else if(length1==length2&&length1!=1&&Double.parseDouble(parts[2])>a*0.9)//对于字符长度相等，
				preciseMaps.add(maps.get(i));
			else if(length1!=length2&&Double.parseDouble(parts[2])>=a*0.85)
				preciseMaps.add(maps.get(i));
		}
		return preciseMaps;
	}

	/*public static ArrayList<String> refineProperty(ArrayList<String> maps)//方便将一些最初不太可靠，但又没有在SPN与Noisy-OR中的加强的匹配对过滤掉
	{
		ArrayList<String> preciseMaps=new ArrayList<String>();
		for(int i=0;i<maps.size();i++)
		{
			String parts[]=maps.get(i).split(",");
			int length1=tool.tokeningWord(parts[0]).split(" ").length;
			int length2=tool.tokeningWord(parts[1]).split(" ").length;
			if(length1==length2&&length1==1&&Double.parseDouble(parts[2])>0.7)//仅名字相同才给予考虑，因为有很多WordNet计算会存在一定错误，导致召回率很低
				preciseMaps.add(maps.get(i));
			if((length1==1&&length2>1)||(length1>1&&length2==1))	//单个词与组合词的情况。
				preciseMaps.add(maps.get(i));
			if(length1>1&&length2>1&&Double.parseDouble(parts[2])>0.6)	//组合词的token均超过2个。
				preciseMaps.add(maps.get(i));
		}
		return preciseMaps;
	}*/

	public static ArrayList<String> changeToAlignments(ArrayList<String> maps)
	{
		ArrayList<String> alignments=new ArrayList<String>();
		for(int i=0;i<maps.size();i++)
		{
			String parts[]=maps.get(i).split(",");
			alignments.add(parts[0]+"--"+parts[1]);
		}
		return alignments;
	}

	public static ArrayList<String> enhancedMap(ArrayList<String> Map,ArrayList<String> Equivalent)
	{
		for(int i=0;i<Equivalent.size();i++)
		{
			String equivalent[]=Equivalent.get(i).split(",");
			if(equivalent.length==3&&equivalent[2].equals("Equal"))  //parts[0]是parts[1]的儿子
			{
				int index =findIndex(Map,equivalent[0]);
				if(index!=-1)
				{
					String newMap=Map.get(index).replaceFirst(equivalent[0], equivalent[1]);//针对属性
					String newMap2=Map.get(index).replaceFirst(equivalent[0].toLowerCase(), equivalent[1].toLowerCase());//针对类
					if(!Map.contains(newMap))
						Map.add(newMap);
					else if(!Map.contains(newMap2))
					{
						Map.add(newMap2);
					}
				}
			}
		}
		return Map;
	}

	public static int findIndex(ArrayList<String> Map,String index)
	{
		//ArrayList<String> Triples=new ArrayList<String>();
		for(int i=0;i<Map.size();i++)
		{
			String parts[]=Map.get(i).split(",");
			if(index.toLowerCase().equals(parts[0].toLowerCase()))
			{
				return i;
			}
		}
		return -1;
	}

	public static HashMap<String,String> transformToHashMap(ArrayList<String> originalMap)
	{
		HashMap<String,String> standardMap=new HashMap<String,String>();
		for(int i=0;i<originalMap.size();i++)
		{
			String part[]=originalMap.get(i).split("--");
			standardMap.put(part[0],part[1]);
			//standardMap.add();
		}
		return standardMap;
	}

	public static ArrayList<String> Normalize(ArrayList<String> object,HashMap<String,String> dic)
	{
		ArrayList<String> normalizedThings=new ArrayList<String>();
		int num=0;
		for(int i=0;i<object.size();i++)
		{
			//String part[]=object.get(i).split("--");
			String normalized=dic.get(object.get(i));
			if(normalized!=null)
			{
				String parts[]=normalized.split(" ");
				String pos= tool.findPOS(parts[0]);
				if(pos.equals("CD")||pos.equals("NNP"))//考虑到首字母缩写的问题
				{
					String abbr_letter = parts[1].charAt(0)+parts[0];
					normalized=normalized.replace(parts[0], abbr_letter).replace(parts[1]+" ", "");
				}
				normalizedThings.add(normalized);
				num++;
			}
			else
				normalizedThings.add(object.get(i));
			//standardMap.add();
		}
		//String candidate_num[]={"1","2","3","4","5","6","7","8","9"};
		System.out.println("规范化概念的个数为:"+num);
		return normalizedThings;
	}

	public  static void printResult(ArrayList<String> classesMap,ArrayList<String> propertiesMap,TreeMap_Tools resultClassMap,TreeMap_Tools resultPropertyMap) throws IOException
	{		
		double alignmentClassNumbers=resultClassMap.getNumberOfMap();
		double alignmentPropertyNumbers=resultPropertyMap.getNumberOfMap();

		double findClassNumbers=classesMap.size();
		double findPropertyNumbers=propertiesMap.size();

		double rightClassNumber=0,rightPropertyNumber=0;
		double R,P,F1;

		System.out.println("**************************************");
		bfw_Result.append("**************************************"+"\n");
		ArrayList<String> notFoundClassMaps=new ArrayList<String>();//O2的概念
		for(int i=0;i<classesMap.size();i++)
		{
			//String part[]=classesMap.get(i).split("--");
			String part[]=classesMap.get(i).split(",");
			String concept1=part[0].toLowerCase();
			String concept2=part[1].toLowerCase();
			//可能给出的reference alignment是反的
			if(resultClassMap.has_relation(concept1, concept2))
			{
				System.out.println(classesMap.get(i)+" (Right)");
				bfw_Result.append(classesMap.get(i)+" (Right)"+"\n");
				rightClassNumber++;
				resultClassMap.remove(concept1, concept2);
			}
			else if(resultClassMap.has_relation(concept2,concept1))
			{
				System.out.println(classesMap.get(i)+" (Right)");
				bfw_Result.append(classesMap.get(i)+" (Right)"+"\n");
				rightClassNumber++;
				resultClassMap.remove(concept2, concept1);
			}
			else
			{
				System.out.println(classesMap.get(i));
				bfw_Result.append(classesMap.get(i)+"\n");
				notFoundClassMaps.add(classesMap.get(i));
			}
		}

		if(resultClassMap.size()==0)
		{
			System.out.println("All the concept pairs are found! ");
			bfw_Result.append("All the concept pairs are found! "+"\n");
		}
		else
		{
			System.out.println("Concept pairs that was not found：");
			bfw_Result.append("Concept pairs that was not found："+"\n");
			ArrayList<String> result=new ArrayList<String>();
			result=resultClassMap.Print_Value();		
			for(String a:result)
			{
				bfw_Result.append(a+"\n");
			}
		}
		System.out.println("The number of  concept pairs that is right："+rightClassNumber);
		bfw_Result.append("The number of  concept pairs that is right："+rightClassNumber+"\n");
		System.out.println("**************************************");
		bfw_Result.append("**************************************"+"\n");
		ArrayList<String> notFoundPropertyMaps=new ArrayList<String>();//O2的概念
		for(int i=0;i<propertiesMap.size();i++)
		{
			//String part[]=propertiesMap.get(i).split("--");
			String part[]=propertiesMap.get(i).split(",");
			String property1=part[0].toLowerCase();
			String property2=part[1].toLowerCase();
			//System.out.println(property1+","+property2);
			//可能给出的reference alignment是反的
			if(resultPropertyMap.has_relation(property1, property2))
			{
				System.out.println(part[0]+","+part[1]+","+part[2]+" "+" (Right)");
				//System.out.println(propertiesMap.get(i)+" (Right)");
				bfw_Result.append(part[0]+","+part[1]+","+part[2]+" "+" (Right)"+"\n");
				//bfw_Result.append(propertiesMap.get(i)+" (Right)"+"\n");
				rightPropertyNumber++;
				resultPropertyMap.remove(property1, property2);
			}
			else if(resultPropertyMap.has_relation(property2, property1))
			{
				System.out.println(part[0]+","+part[1]+","+part[2]+" "+" (Right)");
				//System.out.println(propertiesMap.get(i)+" (Right)");
				bfw_Result.append(part[0]+","+part[1]+","+part[2]+" "+" (Right)"+"\n");
				//bfw_Result.append(propertiesMap.get(i)+" (Right)"+"\n");
				rightPropertyNumber++;
				resultPropertyMap.remove(property2, property1);
			}
			else
			{
				System.out.println(part[0]+","+part[1]+","+part[2]);
				//System.out.println(propertiesMap.get(i));
				bfw_Result.append(part[0]+","+part[1]+","+part[2]+"\n");
				//bfw_Result.append(propertiesMap.get(i)+"\n");
				notFoundPropertyMaps.add(propertiesMap.get(i));
			}
		}
		if(resultPropertyMap.size()==0)
		{
			System.out.println("All the property pairs are found! ");
			bfw_Result.append("All the property pairs are found! " +"\n");
		}
		else
		{
			System.out.println("Property pairs that was not found：");
			bfw_Result.append("Property pairs that was not found："+"\n");
			ArrayList<String> result=new ArrayList<String>();
			result=resultPropertyMap.Print_Value();	
			for(String a:result)
			{
				bfw_Result.append(a+"\n");
			}
		}
		System.out.println("The number of  property pairs that is right ："+rightPropertyNumber);
		bfw_Result.append("The number of  property pairs that is right ："+rightPropertyNumber+"\n");
		P=(rightClassNumber+rightPropertyNumber)/(findClassNumbers+findPropertyNumbers);
		R=(rightClassNumber+rightPropertyNumber)/(alignmentClassNumbers+alignmentPropertyNumbers);
		R=Math.min(R, 1);
		if(P==0&&R==0)
			F1=0;
		else
			F1=(2*P*R)/(P+R);
		System.out.println("==========================================");
		System.out.println("Precision："+P+" "+"	Recall："+R+" "+"	f1-measure："+F1);
		bfw_Result.append("=========================================="+"\n");
		bfw_Result.append("Precision："+P+" "+"	Recall："+R+" "+"	f1-measure："+F1+"\n");
		average_f_measure=average_f_measure+F1;
	}
	
	public  static void printAnatomyResult(ArrayList<String> classesMap,ArrayList<String> propertiesMap,HashMap<String,String> ClassLabel1,HashMap<String,String> ClassLabel2,TreeMap_Tools resultClassMap,TreeMap_Tools resultPropertyMap) throws IOException
	{		
		double alignmentClassNumbers=resultClassMap.getNumberOfMap();
		double alignmentPropertyNumbers=resultPropertyMap.getNumberOfMap();

		double findClassNumbers=classesMap.size();
		double findPropertyNumbers=propertiesMap.size();

		double rightClassNumber=0,rightPropertyNumber=0;
		double R,P,F1;

		System.out.println("**************************************");
		bfw_Result.append("**************************************"+"\n");
		ArrayList<String> notFoundClassMaps=new ArrayList<String>();//O2的概念
		for(int i=0;i<classesMap.size();i++)
		{
			//String part[]=classesMap.get(i).split("--");
			String part[]=classesMap.get(i).split(",");
			String concept1=part[0].toLowerCase();
			String concept2=part[1].toLowerCase();
			String label1=ClassLabel1.get(concept1);
			String label2=ClassLabel2.get(concept2);
			//可能给出的reference alignment是反的
			if(resultClassMap.has_relation(concept1, concept2))
			{
				/*System.out.println(classesMap.get(i)+" (Right)");
				bfw_Result.append(classesMap.get(i)+" (Right)"+"\n");
				rightClassNumber++;
				resultClassMap.remove(concept1, concept2);*/
				System.out.println(label1+","+label2+","+part[2]+" (Right)");
				bfw_Result.append(label1+","+label2+","+part[2]+" (Right)"+"\n");
				rightClassNumber++;
				resultClassMap.remove(concept1, concept2);
				
			}
			else if(resultClassMap.has_relation(concept2,concept1))
			{
				/*System.out.println(classesMap.get(i)+" (Right)");
				bfw_Result.append(classesMap.get(i)+" (Right)"+"\n");
				rightClassNumber++;
				resultClassMap.remove(concept2, concept1);*/
				System.out.println(label1+","+label2+","+part[2]+" (Right)");
				bfw_Result.append(label1+","+label2+","+part[2]+" (Right)"+"\n");
				rightClassNumber++;
				resultClassMap.remove(concept1, concept2);
			}
			else
			{
				/*System.out.println(classesMap.get(i));
				bfw_Result.append(classesMap.get(i)+"\n");
				notFoundClassMaps.add(classesMap.get(i));*/
				System.out.println(label1+","+label2+","+part[2]);
				bfw_Result.append(label1+","+label2+","+part[2]+"\n");
				notFoundClassMaps.add(classesMap.get(i));
			}
		}

		if(resultClassMap.size()==0)
		{
			System.out.println("All the concept pairs are found! ");
			bfw_Result.append("All the concept pairs are found! "+"\n");
		}
		else
		{
			System.out.println("Concept pairs that was not found：");
			bfw_Result.append("Concept pairs that was not found："+"\n");
			ArrayList<String> keySet=resultClassMap.GetKey();
			for(int i=0;i<keySet.size();i++)
			{
				String concept1=keySet.get(i);
				ArrayList<String> concepts2=resultClassMap.GetKey_Value(concept1);
				String label1=ClassLabel1.get(concept1);
				String label2="";
				for(int j=0;j<concepts2.size();j++)
				{
					label2=label2+ClassLabel2.get(concepts2.get(j))+",";
				}
				label2=label2+"]";
				label2.replace(",]", "]");
				bfw_Result.append(label1+",["+label2+"\n");
			}	
		}
		System.out.println("The number of  concept pairs that is right："+rightClassNumber);
		bfw_Result.append("The number of  concept pairs that is right："+rightClassNumber+"\n");
		System.out.println("**************************************");
		bfw_Result.append("**************************************"+"\n");
		ArrayList<String> notFoundPropertyMaps=new ArrayList<String>();//O2的概念
		for(int i=0;i<propertiesMap.size();i++)
		{
			//String part[]=propertiesMap.get(i).split("--");
			String part[]=propertiesMap.get(i).split(",");
			String property1=part[0].toLowerCase();
			String property2=part[1].toLowerCase();
			//System.out.println(property1+","+property2);
			//可能给出的reference alignment是反的
			if(resultPropertyMap.has_relation(property1, property2))
			{
				System.out.println(part[0]+","+part[1]+","+part[2]+" "+" (Right)");
				//System.out.println(propertiesMap.get(i)+" (Right)");
				bfw_Result.append(part[0]+","+part[1]+","+part[2]+" "+" (Right)"+"\n");
				//bfw_Result.append(propertiesMap.get(i)+" (Right)"+"\n");
				rightPropertyNumber++;
				resultPropertyMap.remove(property1, property2);
			}
			else if(resultPropertyMap.has_relation(property2, property1))
			{
				System.out.println(part[0]+","+part[1]+","+part[2]+" "+" (Right)");
				//System.out.println(propertiesMap.get(i)+" (Right)");
				bfw_Result.append(part[0]+","+part[1]+","+part[2]+" "+" (Right)"+"\n");
				//bfw_Result.append(propertiesMap.get(i)+" (Right)"+"\n");
				rightPropertyNumber++;
				resultPropertyMap.remove(property2, property1);
			}
			else
			{
				System.out.println(part[0]+","+part[1]+","+part[2]);
				//System.out.println(propertiesMap.get(i));
				bfw_Result.append(part[0]+","+part[1]+","+part[2]+"\n");
				//bfw_Result.append(propertiesMap.get(i)+"\n");
				notFoundPropertyMaps.add(propertiesMap.get(i));
			}
		}
		if(resultPropertyMap.size()==0)
		{
			System.out.println("All the property pairs were found!!");
			bfw_Result.append("All the property pairs were found! "+"\n");
		}
		else
		{
			System.out.println("Property pairs that was not found：");
			bfw_Result.append("Property pairs that was not found："+"\n");
			ArrayList<String> result=new ArrayList<String>();
			result=resultPropertyMap.Print_Value();	
			for(String a:result)
			{
				bfw_Result.append(a+"\n");
			}
		}
		System.out.println("The number of  property pairs that is right ："+rightPropertyNumber);
		bfw_Result.append("The number of  property pairs that is right ："+rightPropertyNumber+"\n");
		P=(rightClassNumber+rightPropertyNumber)/(findClassNumbers+findPropertyNumbers);
		R=(rightClassNumber+rightPropertyNumber)/(alignmentClassNumbers+alignmentPropertyNumbers);
		R=Math.min(R, 1);
		if(P==0&&R==0)
			F1=0;
		else
			F1=(2*P*R)/(P+R);
		System.out.println("==========================================");
		System.out.println("Precision："+P+" "+"	Recall："+R+" "+"	f1-measure："+F1);
		bfw_Result.append("=========================================="+"\n");
		bfw_Result.append("Precision："+P+" "+"	Recall："+R+" "+"	f1-measure："+F1+"\n");		
	}

}
