package Tools;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import org.mindswap.pellet.jena.PelletReasonerFactory;

import com.hp.hpl.jena.graph.FrontsNode;
import com.hp.hpl.jena.ontology.AllValuesFromRestriction;
import com.hp.hpl.jena.ontology.CardinalityRestriction;
import com.hp.hpl.jena.ontology.ComplementClass;
import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.HasValueRestriction;
import com.hp.hpl.jena.ontology.IntersectionClass;
import com.hp.hpl.jena.ontology.MaxCardinalityRestriction;
import com.hp.hpl.jena.ontology.MinCardinalityRestriction;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.ontology.Restriction;
import com.hp.hpl.jena.ontology.SomeValuesFromRestriction;
import com.hp.hpl.jena.ontology.UnionClass;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.NodeIterator;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFList;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.ResIterator;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

public class Pellet_tools {
	
/*	 Reasoner reasoner = PelletReasonerFactory.theInstance().create();
	    // create an empty model
	     Model rawModel = ModelFactory.createDefaultModel( );        
	       // create an inferencing model using Pellet reasoner
	     RDF_Model = ModelFactory.createInfModel( reasoner, emptyModel );
	     OntModel model = ModelFactory.createOntologyModel(
	    		 PelletReasonerFactory.THE_SPEC, rawModel);*/
	String OntoID="";
	Reasoner reasoner = PelletReasonerFactory.theInstance().create();
	Model rawModel = ModelFactory.createDefaultModel( );
	OntModel ontology = ModelFactory.createOntologyModel(PelletReasonerFactory.THE_SPEC, rawModel);
	
	//OntModel ontology = ModelFactory.createOntologyModel();
	
	
	//判断本体是否加载成功
	public void readOnto(String f)  
	{
		try {
			ontology.read(new FileInputStream(f), "");
			System.out.println("loadOntology");
			String concept="";
			OntClass temp=null;
			ExtendedIterator<OntClass> classlist;
			try
			{
				classlist = ontology.listNamedClasses();
			}
			catch(Exception ex) //this method can be used for loading the information of Website
			{
				System.out.println("this ontology can not be analyzed once by pellet!");
			}
			classlist = ontology.listNamedClasses();
			while(classlist.hasNext()){			//前面这个循环主要是为了定位到概念
				temp=classlist.next();
				concept=temp.getLocalName().toString();	
				OntoID=temp.getURI().replace(concept, "");
				System.out.println("URL is:"+OntoID);
				break;
			}
		} 
		catch (FileNotFoundException ex) 
		{
			System.out.println("file error!");
		}	
	}
	
	
	public ArrayList<String> GetAllConcept()
	{
		//注意这里要先执行readOnto来确保文件的读入
		ArrayList<String> classes=new ArrayList<String>();
		ExtendedIterator<OntClass> classlist = ontology.listNamedClasses(); //反馈的本体是以链表的形式返回
		OntClass temp ;
		//ArrayList<String> class1=new ArrayList<String>();  
		while(classlist.hasNext()){
			temp=classlist.next();
			String a=temp.getLocalName().toString();
			if(a.charAt(0)=='_')
				a=a.replaceFirst("_", "");
			System.out.println(a);
			if(a.equals("Nothing")||a.equals("Thing"))//常规本体
				continue;
			//医学本体
			else if(a.equals("DbXref")||a.equals("Subset")||a.equals("Synonym")||a.equals("ObsoleteClass")||a.equals("SynonymType")||a.equals("Definition"))
				continue;
			//a=a.replace("-", "_");//为了画图方便，将'-'替换成'_'
			else
				classes.add(a);
			}
		return classes;
	}
	
	/*public ArrayList<String> GetAllConceptLabel()
	{
		//注意这里要先执行readOnto来确保文件的读入
		ArrayList<String> classes_labels=new ArrayList<String>();
		ArrayList<String> classes=new ArrayList<String>();
		ExtendedIterator<OntClass> classlist = ontology.listNamedClasses(); //反馈的本体是以链表的形式返回
		OntClass temp ;
		int num=0;//统计null的个数
		//ArrayList<String> class1=new ArrayList<String>();  
		while(classlist.hasNext())
	   {
			temp=classlist.next();
			String a=temp.getLocalName().toString();
			String label=temp.getLabel(null);
			if(a.charAt(0)=='_')
				a=a.replaceFirst("_", "");
			//System.out.println(a);
			System.out.println(label);
			if(a.equals("Nothing")||a.equals("Thing"))//常规本体
				continue;
			//医学本体
			else if(a.equals("DbXref")||a.equals("Subset")||a.equals("Synonym")||a.equals("ObsoleteClass")||a.equals("SynonymType")||a.equals("Definition"))
				continue;
			//a=a.replace("-", "_");//为了画图方便，将'-'替换成'_'
			if(label!=null&&!label.equals(a))//存在自己等于自己的label
			//if(label!=null)//存在自己等于自己的label
				classes_labels.add(label);
			else 
			{
				classes_labels.add(a);
				num++;
			}
			
		}
		if(num<=0.5*classes_labels.size())
			return classes_labels;
		else
			return classes;
	}*/
	
	public ArrayList<String> GetAllConceptLabel()
	{
		//注意这里要先执行readOnto来确保文件的读入
		ArrayList<String> classes_labels=new ArrayList<String>();
		//ArrayList<String> classes=new ArrayList<String>();
		ExtendedIterator<OntClass> classlist = ontology.listNamedClasses(); //反馈的本体是以链表的形式返回
		OntClass temp ;
		//int num=0;//统计null的个数
		//ArrayList<String> class1=new ArrayList<String>();  
		while(classlist.hasNext())
	   {
			temp=classlist.next();
			String a=temp.getLocalName().toString();
			String label=temp.getLabel(null);
			String comment=temp.getComment(null);
			if(a.charAt(0)=='_')
				a=a.replaceFirst("_", "");
			//System.out.println(a);
			System.out.println(label);
			if(a.equals("Nothing")||a.equals("Thing"))//常规本体
				continue;
			//医学本体
			else if(a.equals("DbXref")||a.equals("Subset")||a.equals("Synonym")||a.equals("ObsoleteClass")||a.equals("SynonymType")||a.equals("Definition"))
				continue;
			//a=a.replace("-", "_");//为了画图方便，将'-'替换成'_'
			if(label!=null&&!label.equals(a))//存在自己等于自己的label
				classes_labels.add(a+"--"+label);	
			else if(label!=null&&comment!=null&&!comment.equals(""))  
			{
				classes_labels.add(a+"--"+comment.trim());	
			}
	   }
		return classes_labels;
		
	}
	
	public ArrayList<String> GetAllProperty()
	{
		ArrayList<String> Properties=new ArrayList<String>();
		ExtendedIterator<ObjectProperty> propertylist1 = ontology.listObjectProperties(); //反馈的本体是以链表的形式返回
		ObjectProperty temp1 ;	
		while(propertylist1.hasNext()){
			temp1=propertylist1.next();		
			String property=temp1.getLocalName().toString();
			if(property.equals("topObjectProperty")||property.equals("bottomObjectProperty"))
				continue;
			Properties.add(property);
		}
		ExtendedIterator<DatatypeProperty> propertylist2 = ontology.listDatatypeProperties(); //反馈的本体是以链表的形式返回
		DatatypeProperty temp2 ;
		while(propertylist2.hasNext()){
			temp2=propertylist2.next();
			String property=temp2.getLocalName().toString();
			if(property.equals("topDataProperty")||property.equals("bottomDataProperty"))
				continue;
			Properties.add(property);
		}
		return Properties;
	}
	
	public ArrayList<String> GetObjectProperty()
	{
		ArrayList<String> Properties=new ArrayList<String>();
		ExtendedIterator<ObjectProperty> propertylist1 = ontology.listObjectProperties(); //反馈的本体是以链表的形式返回
		ObjectProperty temp1 ;	
		while(propertylist1.hasNext()){
			temp1=propertylist1.next();		
			String property=temp1.getLocalName().toString();
			if(property.equals("topObjectProperty")||property.equals("bottomObjectProperty"))//常规本体
				continue;
			//医学本体
			//else if(property.equals("ObsoleteProperty")||property.equals("UNDEFINED")||property.equals("UNDEFINED_part_of"))
			else if(property.equals("ObsoleteProperty")||property.equals("UNDEFINED"))
				continue;
			else
				Properties.add(property);
		}
		return Properties;
	}
	
	public ArrayList<String> GetObjectPropertyLabel()
	{
		ArrayList<String> PropertiesLabel=new ArrayList<String>();
		ExtendedIterator<ObjectProperty> propertylist1 = ontology.listObjectProperties(); //反馈的本体是以链表的形式返回
		ObjectProperty temp1 ;	
		while(propertylist1.hasNext()){
			temp1=propertylist1.next();		
			String property=temp1.getLocalName().toString();
			String label=temp1.getLabel(null);
			String comment=temp1.getComment(null);
			if(property.equals("topObjectProperty")||property.equals("bottomObjectProperty"))//常规本体
				continue;
			//医学本体
			//else if(property.equals("ObsoleteProperty")||property.equals("UNDEFINED")||property.equals("UNDEFINED_part_of"))
			else if(property.equals("ObsoleteProperty")||property.equals("UNDEFINED"))
				continue;
			if(label!=null&&!tokeningWord(label).equals(tokeningWord(property)))
				PropertiesLabel.add(property+"--"+label);
			else if(comment!=null&&!comment.equals(""))  
			{
				PropertiesLabel.add(property+"--"+comment.trim());	
			}
			
		}
		return PropertiesLabel;
	}
	
	public ArrayList<String> GetDataProperty()
	{
		ArrayList<String> Properties=new ArrayList<String>();
		ExtendedIterator<DatatypeProperty> propertylist2 = ontology.listDatatypeProperties(); //反馈的本体是以链表的形式返回
		DatatypeProperty temp2 ;
		while(propertylist2.hasNext()){
			temp2=propertylist2.next();
			String property=temp2.getLocalName().toString();
			if(property.equals("topDataProperty")||property.equals("bottomDataProperty"))
				continue;
			Properties.add(property);
		}
		return Properties;
	}
	
	public ArrayList<String> GetDataPropertyLabel()
	{
		ArrayList<String> PropertiesLabel=new ArrayList<String>();
		ExtendedIterator<DatatypeProperty> propertylist2 = ontology.listDatatypeProperties(); //反馈的本体是以链表的形式返回
		DatatypeProperty temp2 ;
		while(propertylist2.hasNext()){
			temp2=propertylist2.next();
			String property=temp2.getLocalName().toString();
			String label=temp2.getLabel(null);
			String comment=temp2.getComment(null);
			if(property.equals("topDataProperty")||property.equals("bottomDataProperty"))
				continue;
			if(label!=null&&!tokeningWord(label).equals(tokeningWord(property)))
				PropertiesLabel.add(property+"--"+label);
			else if(comment!=null&&!comment.equals(""))  
			{
				PropertiesLabel.add(property+"--"+comment.trim());	
			}
		}
		return PropertiesLabel;
	}
	
	public ArrayList<String> GetPropertyAndInverse()
	{
		ArrayList<String> propertiesAndInverse=new ArrayList<String>();
		ExtendedIterator<ObjectProperty> propertylist1 = ontology.listObjectProperties(); //反馈的本体是以链表的形式返回
		ObjectProperty temp1 ;	
		while(propertylist1.hasNext()){
			temp1=propertylist1.next();		
			String property=temp1.getLocalName().toString();
			if(temp1.getInverse()!=null)
			{
				String inverseProperty=temp1.getInverse().getLocalName().toString();
				propertiesAndInverse.add(property+"--"+inverseProperty);
			}
		}
		//DataProperty不存在Inverse
		/*ExtendedIterator<DatatypeProperty> propertylist2 = ontology.listDatatypeProperties(); //反馈的本体是以链表的形式返回
		DatatypeProperty temp2 ;
		while(propertylist2.hasNext()){
			temp2=propertylist2.next();
			String property=temp2.getLocalName().toString();
			if(temp2.getInverse()!=null)
			{
				String inverseProperty=temp2.getLocalName().toString();
				propertiesAndInverse.add(property+"--"+inverseProperty);
			}
		}*/
		return propertiesAndInverse;
	}
	
	public ArrayList<String> GetAllRelations()//这里只获取objectproperty的关系
	{
		ArrayList<String> Relations=new ArrayList<String>();
		ExtendedIterator<ObjectProperty> propertylist = ontology.listObjectProperties(); //反馈的本体是以链表的形式返回
		ObjectProperty temp ;
		while(propertylist.hasNext()){
			temp=propertylist.next();			
			String property=temp.getLocalName().toString();
			if(property.equals("topObjectProperty")||property.equals("bottomObjectProperty"))
				continue;
			String subject="";
			String object="";
		//	System.out.println("************************");
		//	System.out.println(property);		
            /*ExtendedIterator<? extends OntResource> Domains=temp.listDomain();
			ExtendedIterator<? extends OntResource> Ranges=temp.listRange();*/
        	//System.out.println(temp.getDomain().isAnon());//判断该节点是否是一个RDF Node
			//将析取式用链表返回的核心语句
			// ExtendedIterator<? extends OntClass> PropertyRanges = temp.getRange().asClass().asUnionClass().listOperands();	
			//定义域、值域都不为空
			if(temp.getDomain()!=null&&temp.getRange()!=null)
			{
				if(!temp.getDomain().isAnon()&&!temp.getRange().isAnon())
				{
					subject=temp.getDomain().getLocalName();
					object=temp.getRange().getLocalName();
					String Triple=subject+","+property+","+object;
					System.out.println(Triple);
					if(!Relations.contains(Triple))
						Relations.add(Triple);			 
				}

				else if(temp.getDomain().isAnon()&&!temp.getRange().isAnon())
				{
					object=temp.getRange().getLocalName();
					//如果定义域存在有析取（or）存在的情况
					ExtendedIterator<? extends OntClass> PropertyDomains = temp.getDomain().asClass().asUnionClass().listOperands();				 
					while (PropertyDomains.hasNext()) 
					{					
						subject=PropertyDomains.next().getLocalName();		
						//	System.out.println(subject);				
						String Triple=subject+","+property+","+object;
						System.out.println(Triple);
						if(!Relations.contains(Triple))
							Relations.add(Triple);
					}
				}

				else if(!temp.getDomain().isAnon()&&temp.getRange().isAnon())
				{
					subject=temp.getDomain().getLocalName();
					//如果值域存在有析取（or）存在的情况
					ExtendedIterator<? extends OntClass> PropertyRanges = temp.getRange().asClass().asUnionClass().listOperands();				 
					while (PropertyRanges.hasNext()) 
					{					
						object=PropertyRanges.next().getLocalName();		
						//	System.out.println(subject);				
						String Triple=subject+","+property+","+object;
						System.out.println(Triple);
						if(!Relations.contains(Triple))
							Relations.add(Triple);
					}
				}

				else if(temp.getDomain().isAnon()&&temp.getRange().isAnon()) //解决属性中存在匿名本体
				{		
					//如果定义域、值域都存在析取（or）存在的情况
					/*Property prop =temp; 	//temp 是一个属性
					ExtendedIterator<? extends OntClass> ConcetpDomains = ((OntProperty)prop).getDomain().asClass().asUnionClass().listOperands();*/
					ExtendedIterator<? extends OntClass> PropertyDomains = temp.getDomain().asClass().asUnionClass().listOperands();
					ExtendedIterator<? extends OntClass> PropertyRanges = temp.getRange().asClass().asUnionClass().listOperands();
					while (PropertyDomains.hasNext()) 
					{		
						subject=PropertyDomains.next().getLocalName();		
						//		System.out.println(subject);
						while(PropertyRanges.hasNext())
						{
							object=PropertyRanges.next().getLocalName();
							//			System.out.println(object);
							String Triple=subject+","+property+","+object;
							System.out.println(Triple);
							if(!Relations.contains(Triple))
								Relations.add(Triple);
						}			
					}
				}		 			 
			}			
			//定义域不为空、值域为空
			 if(temp.getDomain()!=null&&temp.getRange()==null)
			{
				 if(!temp.getDomain().isAnon())
				 {
					subject=temp.getDomain().getLocalName();
					object=null;
					String Triple=subject+","+property+","+object;
					System.out.println(Triple);
					if(!Relations.contains(Triple))
						Relations.add(Triple);			 
				 }
				 	
				 else if(temp.getDomain().isAnon())
				{
					 object=null;
					 //如果定义域存在有析取（or）存在的情况
					 ExtendedIterator<? extends OntClass> PropertyDomains = temp.getDomain().asClass().asUnionClass().listOperands();				 
					 while (PropertyDomains.hasNext()) 
					 {					
						subject=PropertyDomains.next().getLocalName();		
					//	System.out.println(subject);				
						String Triple=subject+","+property+","+object;
						System.out.println(Triple);
						if(!Relations.contains(Triple))
							Relations.add(Triple);
					 }
				}			
			}			 
				//定义域为空、值域不为空
			 if(temp.getDomain()==null&&temp.getRange()!=null)
			{
				 if(!temp.getRange().isAnon())
				 {
					subject=null;
					object=temp.getRange().getLocalName();
					String Triple=subject+","+property+","+object;
					System.out.println(Triple);
					if(!Relations.contains(Triple))
						Relations.add(Triple);			 
				 }
				 	
				 else if(temp.getRange().isAnon())
					{
						 subject=null;
						 //如果值域存在有析取（or）存在的情况
						 ExtendedIterator<? extends OntClass> PropertyRanges = temp.getRange().asClass().asUnionClass().listOperands();				 
						 while (PropertyRanges.hasNext()) 
						 {					
							object=PropertyRanges.next().getLocalName();		
						//	System.out.println(subject);				
							String Triple=subject+","+property+","+object;
							System.out.println(Triple);
							Relations.add(Triple);
						 }
					}			
			}
			 
		}	//while propertylist.hasNext() 这里是对Objectproperties
		
		System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++");		
/*		ExtendedIterator<DatatypeProperty> propertylist2 = ontology.listDatatypeProperties(); //反馈的本体是以链表的形式返回
		DatatypeProperty temp2 ;
		while(propertylist2.hasNext()){
			temp2=propertylist2.next();
			String property=temp2.getLocalName().toString();
			System.out.println(property);
		}*/
		
		ExtendedIterator<DatatypeProperty> propertylist2 = ontology.listDatatypeProperties(); //反馈的本体是以链表的形式返回
		DatatypeProperty temp2;
		while(propertylist2.hasNext()){
			temp2=propertylist2.next();
			String property=temp2.getLocalName().toString();
			if(property.equals("topDataProperty")||property.equals("bottomDataProperty"))
				continue;
			String subject="";
			String object="";
			System.out.println("####################################");
			//System.out.println(property);			
			if(temp2.getDomain()!=null&&temp2.getRange()!=null)
			{
				if(!temp2.getDomain().isAnon()&&!temp2.getRange().isAnon())
				{
					subject=temp2.getDomain().getLocalName();
					object=temp2.getRange().getLocalName();
					String Triple=subject+","+property+","+object;
					System.out.println(Triple);
					if(!Relations.contains(Triple))
						Relations.add(Triple);			 
				}

				else if(temp2.getDomain().isAnon()&&!temp2.getRange().isAnon())
				{
					object=temp2.getRange().getLocalName();
					//如果定义域存在有析取（or）存在的情况
					ExtendedIterator<? extends OntClass> PropertyDomains = temp2.getDomain().asClass().asUnionClass().listOperands();				 
					while (PropertyDomains.hasNext()) 
					{					
						subject=PropertyDomains.next().getLocalName();		
						//	System.out.println(subject);				
						String Triple=subject+","+property+","+object;
						System.out.println(Triple);
						if(!Relations.contains(Triple))
							Relations.add(Triple);
					}
				}
			}		 
		}	
		return Relations;
	}
	
	public ArrayList<String> GetAll_Infered_Relations()//这里只获取两种关系都获取，但是最保守的方法
	{
		ArrayList<String> Relations=new ArrayList<String>();
		ExtendedIterator<ObjectProperty> propertylist = ontology.listObjectProperties(); //反馈的本体是以链表的形式返回
		ObjectProperty temp ;
		while(propertylist.hasNext()){
			temp=propertylist.next();			
			String property=temp.getLocalName().toString();
			//列出它的定义域和值域的中非匿名类的子类，并进行三元组的加入
			if(property.equals("topObjectProperty")||property.equals("bottomObjectProperty"))
				continue;
			String subject="";
			String object="";
			if(temp.getDomain()!=null&&temp.getRange()!=null)
			{
			 if(!temp.getDomain().isAnon()&&!temp.getRange().isAnon())
			 {
				subject=temp.getDomain().getLocalName();
				object=temp.getRange().getLocalName();
				String Triple=subject+","+property+","+object;
				//System.out.println(subject+","+property+","+object);
				if(!Relations.contains(Triple))
					Relations.add(Triple);				
				//对于定义域与值域的扩展
				//ArrayList<String> subject_children=new ArrayList<String>();
				/*ArrayList<String> subject_children=FindConcept_children(subject);
				ArrayList<String> object_children=FindConcept_children(object);			
				for(String a :subject_children)
				{
					for(String b:object_children)
					{
						String Triple=a+","+property+","+b;
						//System.out.println(Triple);
						Relations.add(Triple);
					}
				}*/					 
			 }		 	
			 else if(temp.getDomain().isAnon()&&!temp.getRange().isAnon())
			{
				 object=temp.getRange().getLocalName();
				 //如果定义域存在有析取（or）存在的情况
				 ExtendedIterator<? extends OntClass> PropertyDomains = temp.getDomain().asClass().asUnionClass().listOperands();				 
				 while (PropertyDomains.hasNext()) 
				 {					
					subject=PropertyDomains.next().getLocalName();		
				//	System.out.println(subject);
					String Triple=subject+","+property+","+object;
					//System.out.println(Triple);
					if(!Relations.contains(Triple))
						Relations.add(Triple);
					//对于定义域与值域的扩展
					/*ArrayList<String> subject_children=FindConcept_children(subject);
					ArrayList<String> object_children=FindConcept_children(object);
					subject_children.add(subject);
					object_children.add(object);
					for(String a :subject_children)
					{
						for(String b:object_children)
						{
							String Triple=a+","+property+","+b;
							//System.out.println(Triple);
							Relations.add(Triple);
						}
					}*/
				 }
			}			 
			 else if(!temp.getDomain().isAnon()&&temp.getRange().isAnon())
			{
				 subject=temp.getDomain().getLocalName();
				 //如果值域存在有析取（or）存在的情况
				 ExtendedIterator<? extends OntClass> PropertyRanges = temp.getRange().asClass().asUnionClass().listOperands();				 
				 while (PropertyRanges.hasNext()) 
				 {					
					object=PropertyRanges.next().getLocalName();		
				//	System.out.println(subject);				
					String Triple=subject+","+property+","+object;
					//System.out.println(Triple);
					if(!Relations.contains(Triple))
						Relations.add(Triple);
					//对于定义域与值域的扩展
					/*ArrayList<String> subject_children=FindConcept_children(subject);
					ArrayList<String> object_children=FindConcept_children(object);
					subject_children.add(subject);
					object_children.add(object);
					for(String a :subject_children)
					{
						for(String b:object_children)
						{
							String Triple=a+","+property+","+b;
							//System.out.println(Triple);
							Relations.add(Triple);
						}
					}*/
				 }
			}
			 
			 else if(temp.getDomain().isAnon()&&temp.getRange().isAnon()) //解决属性中存在匿名本体
				{		
					//如果定义域、值域都存在析取（or）存在的情况
					/*Property prop =temp; 	//temp 是一个属性
					ExtendedIterator<? extends OntClass> ConcetpDomains = ((OntProperty)prop).getDomain().asClass().asUnionClass().listOperands();*/
					ExtendedIterator<? extends OntClass> PropertyDomains = temp.getDomain().asClass().asUnionClass().listOperands();
					ExtendedIterator<? extends OntClass> PropertyRanges = temp.getRange().asClass().asUnionClass().listOperands();
					 while (PropertyDomains.hasNext()) 
					 {		
						subject=PropertyDomains.next().getLocalName();		
				//		System.out.println(subject);
						while(PropertyRanges.hasNext())
						{
							object=PropertyRanges.next().getLocalName();
				//			System.out.println(object);
							String Triple=subject+","+property+","+object;
							//System.out.println(Triple);
							if(!Relations.contains(Triple))
								Relations.add(Triple);
							//对于定义域与值域的扩展
							/*ArrayList<String> subject_children=FindConcept_children(subject);
							ArrayList<String> object_children=FindConcept_children(object);
							subject_children.add(subject);
							object_children.add(object);
							for(String a :subject_children)
							{
								for(String b:object_children)
								{
									String Triple=a+","+property+","+b;
									//System.out.println(Triple);
									Relations.add(Triple);
								}
							}*/
						}			
					 }
				}		 			 
			}						 
		}	//while propertylist.hasNext() 这里是对Objectproperties
		
		//System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++");		
		
		ExtendedIterator<DatatypeProperty> propertylist2 = ontology.listDatatypeProperties(); //反馈的本体是以链表的形式返回
		DatatypeProperty temp2;
		while(propertylist2.hasNext()){
			temp2=propertylist2.next();
			String property=temp2.getLocalName().toString();
			if(property.equals("topDataProperty")||property.equals("bottomDataProperty"))
				continue;
			String subject="";
			String object="";
			//System.out.println("####################################");
			//System.out.println(property);			
			if(temp2.getDomain()!=null&&temp2.getRange()!=null)
			{
				if(!temp2.getDomain().isAnon()&&!temp2.getRange().isAnon())
				{
					subject=temp2.getDomain().getLocalName();
					object=temp2.getRange().getLocalName();
					/*String Triple=subject+","+property+","+object;
				System.out.println(Triple);
				Relations.add(Triple);	*/		
					//对于定义域与值域的扩展
					ArrayList<String> subject_children=findConceptChildrenforDatatype(subject);
					//subject_children.add(subject);
					for(String a :subject_children)
					{
						String Triple=a+","+property+","+object;
						//System.out.println(Triple);
						if(!Relations.contains(Triple))
							Relations.add(Triple);
					}
				}

				else if(temp2.getDomain().isAnon()&&!temp2.getRange().isAnon())
				{
					object=temp2.getRange().getLocalName();
					//如果定义域存在有析取（or）存在的情况
					ExtendedIterator<? extends OntClass> PropertyDomains = temp2.getDomain().asClass().asUnionClass().listOperands();				 
					while (PropertyDomains.hasNext()) 
					{					
						subject=PropertyDomains.next().getLocalName();		
						//	System.out.println(subject);				
						/*String Triple=subject+","+property+","+object;
					System.out.println(Triple);
					Relations.add(Triple);*/
						//对于定义域与值域的扩展
						ArrayList<String> subject_children=findConceptChildrenforDatatype(subject);
						//subject_children.add(subject);
						for(String a :subject_children)
						{
							String Triple=a+","+property+","+object;
							//System.out.println(Triple);
							if(!Relations.contains(Triple))
								Relations.add(Triple);
						}
					}
				}	 			 
			}
		}		
		//貌似pellel以及包含了这部分功能了。
		/*ArrayList<String> enhancedRelations=enhancedInverseRelations(Relations);
		return enhancedRelations;*/
		return Relations;
	}
	
	public ArrayList<String> GetDataPropertyRelations()//这里只获取dataproperty的关系
	{
		ArrayList<String> Relations=new ArrayList<String>();
		ExtendedIterator<DatatypeProperty> propertylist2 = ontology.listDatatypeProperties(); //反馈的本体是以链表的形式返回
		DatatypeProperty temp2;
		while(propertylist2.hasNext()){
			temp2=propertylist2.next();
			String property=temp2.getLocalName().toString();
			if(property.equals("topDataProperty")||property.equals("bottomDataProperty"))
				continue;
			String subject="";
			String object="";
			//System.out.println("####################################");
			//System.out.println(property);			
			if(temp2.getDomain()!=null&&temp2.getRange()!=null)
			{
				//定义域值域都是非匿名，
				if(!temp2.getDomain().isAnon()&&!temp2.getRange().isAnon())
				{
					subject=temp2.getDomain().getLocalName();
					object=temp2.getRange().getLocalName();
					ArrayList<String> subject_children=findConceptChildrenforDatatype(subject);//对于定义域与值域的扩展
					subject_children.add(subject);
					for(String a :subject_children)
					{
						String Triple=a+","+property+","+object;
						//System.out.println(Triple);
						if(!Relations.contains(Triple))
							Relations.add(Triple);
					}					
				}
				else if(temp2.getDomain().isAnon()&&!temp2.getRange().isAnon())
				{
					object=temp2.getRange().getLocalName();
					//如果定义域存在有析取（or）存在的情况
					ExtendedIterator<? extends OntClass> PropertyDomains = temp2.getDomain().asClass().asUnionClass().listOperands();				 
					while (PropertyDomains.hasNext()) 
					{							
							subject=PropertyDomains.next().getLocalName();		
							ArrayList<String> subject_children=findConceptChildrenforDatatype(subject);//对于定义域与值域的扩展
							subject_children.add(subject);
							for(String a :subject_children)
							{
								String Triple=a+","+property+","+object;
								if(!Relations.contains(Triple))
									Relations.add(Triple);
							}
						
					}
				}			 
			}
		}		
		//貌似pellel以及包含了这部分功能了。
		/*ArrayList<String> enhancedRelations=enhancedInverseRelations(Relations);
		return enhancedRelations;*/
		return Relations;
	}
	
	public ArrayList<String> GetObjectRelations()//这里只获取objectproperty的关系
	{
		ArrayList<String> Relations=new ArrayList<String>();
		ExtendedIterator<ObjectProperty> propertylist = ontology.listObjectProperties(); //反馈的本体是以链表的形式返回
		ObjectProperty temp ;
		while(propertylist.hasNext()){
			temp=propertylist.next();			
			String property=temp.getLocalName().toString();
			//列出它的定义域和值域的中非匿名类的子类，并进行三元组的加入
			if(property.equals("topObjectProperty")||property.equals("bottomObjectProperty"))
				continue;
			String subject="";
			String object="";
			if(temp.getDomain()!=null&&temp.getRange()!=null)
			{
				if(!temp.getDomain().isAnon()&&!temp.getRange().isAnon())
				{
					subject=temp.getDomain().getLocalName();
					object=temp.getRange().getLocalName();
					String Triple=subject+","+property+","+object;
					//System.out.println(subject+","+property+","+object);
					if(!Relations.contains(Triple))
						Relations.add(Triple);					 
				}		 	
				else if(temp.getDomain().isAnon()&&!temp.getRange().isAnon())
				{
					object=temp.getRange().getLocalName();
					//如果定义域存在有析取（or）存在的情况
					ExtendedIterator<? extends OntClass> PropertyDomains = temp.getDomain().asClass().asUnionClass().listOperands();				 
					while (PropertyDomains.hasNext()) 
					{					
						subject=PropertyDomains.next().getLocalName();		
						String Triple=subject+","+property+","+object;
						//System.out.println(Triple);
						if(!Relations.contains(Triple))
							Relations.add(Triple);
					}
				}			 
				else if(!temp.getDomain().isAnon()&&temp.getRange().isAnon())
				{
					subject=temp.getDomain().getLocalName();
					//如果值域存在有析取（or）存在的情况
					ExtendedIterator<? extends OntClass> PropertyRanges = temp.getRange().asClass().asUnionClass().listOperands();				 
					while (PropertyRanges.hasNext()) 
					{					
						object=PropertyRanges.next().getLocalName();						
						String Triple=subject+","+property+","+object;
						//System.out.println(Triple);
						if(!Relations.contains(Triple))
							Relations.add(Triple);
					}
				}

				else if(temp.getDomain().isAnon()&&temp.getRange().isAnon()) //解决属性中存在匿名本体
				{		
					//如果定义域、值域都存在析取（or）存在的情况
					/*Property prop =temp; 	//temp 是一个属性
					ExtendedIterator<? extends OntClass> ConcetpDomains = ((OntProperty)prop).getDomain().asClass().asUnionClass().listOperands();*/
					ExtendedIterator<? extends OntClass> PropertyDomains = temp.getDomain().asClass().asUnionClass().listOperands();
					ExtendedIterator<? extends OntClass> PropertyRanges = temp.getRange().asClass().asUnionClass().listOperands();
					while (PropertyDomains.hasNext()) 
					{		
						subject=PropertyDomains.next().getLocalName();		
						//		System.out.println(subject);
						while(PropertyRanges.hasNext())
						{
							object=PropertyRanges.next().getLocalName();
							String Triple=subject+","+property+","+object;
							System.out.println(Triple);
							if(!Relations.contains(Triple))
								Relations.add(Triple);
						}			
					}
				}		 			 
			}
			else if(temp.getDomain()==null&&temp.getRange()!=null)//潜在的关系，或许能够利用等价关系进行填充
			{
				if(temp.getRange().isAnon())//值域是匿名类
				{
					 subject=null;
					 //如果值域存在有析取（or）存在的情况
					 ExtendedIterator<? extends OntClass> PropertyRanges = temp.getRange().asClass().asUnionClass().listOperands();				 
					 while (PropertyRanges.hasNext()) 
					 {					
						object=PropertyRanges.next().getLocalName();
						/*ArrayList<String> subject_children=findConceptChildrenforObjcet(subject);//对于定义域与值域的扩展
						//证明作者是较为仔细的考虑过值域
						subject_children.add(subject);
						for(String a :subject_children)
						{
							String Triple=a+","+property+","+object;
							//System.out.println(Triple);
							Relations.add(Triple);
						}*/					
						String Triple=subject+","+property+","+object;
						//System.out.println(Triple);
						if(!Relations.contains(Triple))
							Relations.add(Triple);
					 }
				}
				else //值域是单个类
				{
					subject=null;
					object=temp.getRange().getLocalName();
					/*ArrayList<String> object_children=findConceptChildrenforObjcet(object);//对于定义域与值域的扩展
					object_children.add(object);
					for(String a :object_children)
					{
						String Triple=subject+","+property+","+a;
						//System.out.println(Triple);
						if(!Relations.contains(Triple))
							Relations.add(Triple);
					}*/
					String Triple=subject+","+property+","+object;
					if(!Relations.contains(Triple))
						Relations.add(Triple);
				}
			}
		}	//while propertylist.hasNext() 这里是对Objectproperties
		return Relations;
	}
	
	public ArrayList<String> New_GetObjectRelations()//这里只获取objectproperty的关系
	{
		ArrayList<String> Relations=new ArrayList<String>();
		ExtendedIterator<ObjectProperty> propertylist = ontology.listObjectProperties(); //反馈的本体是以链表的形式返回
		ObjectProperty temp ;
		while(propertylist.hasNext()){
			temp=propertylist.next();			
			String property=temp.getLocalName().toString();
			System.out.println(property);
			//列出它的定义域和值域的中非匿名类的子类，并进行三元组的加入
			if(property.equals("topObjectProperty")||property.equals("bottomObjectProperty"))
				continue;
			String subject="";
			String object="";
			if(temp.getDomain()!=null&&temp.getRange()!=null)
			{
			 if(!temp.getDomain().isAnon()&&!temp.getRange().isAnon())
			 {
				subject=temp.getDomain().getLocalName();
				object=temp.getRange().getLocalName();
				ArrayList<String> subject_children=findConceptChildrenforObjcet(subject);//对于定义域与值域的扩展
				subject_children.add(subject);
				for(String a :subject_children)
				{
					String Triple=a+","+property+","+object;
					//System.out.println(Triple);
					if(!Relations.contains(Triple))
						Relations.add(Triple);
				}
				ArrayList<String> object_children=findConceptChildrenforObjcet(object);//对于定义域与值域的扩展
				object_children.add(object);
				for(String a :object_children)
				{
					String Triple=subject+","+property+","+a;
					//System.out.println(Triple);
					if(!Relations.contains(Triple))
						Relations.add(Triple);
				}
				/*String Triple=subject+","+property+","+object;
				//System.out.println(subject+","+property+","+object);
				Relations.add(Triple);	*/				 
			 }		 	
			 else if(temp.getDomain().isAnon()&&!temp.getRange().isAnon())
			{
				 object=temp.getRange().getLocalName();
				 //如果定义域存在有析取（or）存在的情况
				 ExtendedIterator<? extends OntClass> PropertyDomains = temp.getDomain().asClass().asUnionClass().listOperands();				 
				 while (PropertyDomains.hasNext()) 
				 {					
					subject=PropertyDomains.next().getLocalName();	
					//证明作者是较为仔细的考虑过定义域
					ArrayList<String> object_children=findConceptChildrenforObjcet(object);//对于定义域与值域的扩展
					object_children.add(object);
					for(String a :object_children)
					{
						String Triple=subject+","+property+","+a;
						if(!Relations.contains(Triple))
							Relations.add(Triple);
						//System.out.println(Triple);
					}
					
					/*String Triple=subject+","+property+","+object;
					//System.out.println(Triple);
					Relations.add(Triple);*/
				 }
			}			 
			 else if(!temp.getDomain().isAnon()&&temp.getRange().isAnon())
			{
				 subject=temp.getDomain().getLocalName();
				 //如果值域存在有析取（or）存在的情况
				 ExtendedIterator<? extends OntClass> PropertyRanges = temp.getRange().asClass().asUnionClass().listOperands();				 
				 while (PropertyRanges.hasNext()) 
				 {					
					object=PropertyRanges.next().getLocalName();
					ArrayList<String> subject_children=findConceptChildrenforObjcet(subject);//对于定义域与值域的扩展
					//证明作者是较为仔细的考虑过值域
					subject_children.add(subject);
					for(String a :subject_children)
					{
						String Triple=a+","+property+","+object;
						//System.out.println(Triple);
						if(!Relations.contains(Triple))
							Relations.add(Triple);
					}
					
					/*String Triple=subject+","+property+","+object;
					//System.out.println(Triple);
					Relations.add(Triple);*/
				 }
			}
			 
			 else if(temp.getDomain().isAnon()&&temp.getRange().isAnon()) //解决属性中存在匿名本体
				{		
					//如果定义域、值域都存在析取（or）存在的情况
					/*Property prop =temp; 	//temp 是一个属性
					ExtendedIterator<? extends OntClass> ConcetpDomains = ((OntProperty)prop).getDomain().asClass().asUnionClass().listOperands();*/
					ExtendedIterator<? extends OntClass> PropertyDomains = temp.getDomain().asClass().asUnionClass().listOperands();
					ExtendedIterator<? extends OntClass> PropertyRanges = temp.getRange().asClass().asUnionClass().listOperands();
					 while (PropertyDomains.hasNext()) 
					 {		
						subject=PropertyDomains.next().getLocalName();		
				//		System.out.println(subject);
						while(PropertyRanges.hasNext())
						{
							object=PropertyRanges.next().getLocalName();
							String Triple=subject+","+property+","+object;
							System.out.println(Triple);
							if(!Relations.contains(Triple))
								Relations.add(Triple);
						}			
					 }
				}		 			 
			}
			else if(temp.getDomain()==null&&temp.getRange()!=null)//潜在的关系，或许能够利用等价关系进行填充
			{
				if(temp.getRange().isAnon())//值域是匿名类
				{
					 subject=null;
					 //如果值域存在有析取（or）存在的情况
					 ExtendedIterator<? extends OntClass> PropertyRanges = temp.getRange().asClass().asUnionClass().listOperands();				 
					 while (PropertyRanges.hasNext()) 
					 {					
						object=PropertyRanges.next().getLocalName();
						/*ArrayList<String> subject_children=findConceptChildrenforObjcet(subject);//对于定义域与值域的扩展
						//证明作者是较为仔细的考虑过值域
						subject_children.add(subject);
						for(String a :subject_children)
						{
							String Triple=a+","+property+","+object;
							//System.out.println(Triple);
							Relations.add(Triple);
						}*/					
						String Triple=subject+","+property+","+object;
						//System.out.println(Triple);
						if(!Relations.contains(Triple))
							Relations.add(Triple);
					 }
				}
				else //值域是单个类
				{
					subject=null;
					object=temp.getRange().getLocalName();
					ArrayList<String> object_children=findConceptChildrenforObjcet(object);//对于定义域与值域的扩展
					object_children.add(object);
					for(String a :object_children)
					{
						String Triple=subject+","+property+","+a;
						//System.out.println(Triple);
						if(!Relations.contains(Triple))
							Relations.add(Triple);
					}
				}
			}
		}	//while propertylist.hasNext() 这里是对Objectproperties
		return Relations;
	}
	//利用属性中存在可逆的pair来进行计算
	public  ArrayList<String> enhancedInverseRelations(ArrayList<String> Relations)
	{
		System.out.println("*********************************");
		int m=0;
		ArrayList<String> propertiesInverse=GetPropertyAndInverse();
		//TreeMap_Tools inversrPair=new TreeMap_Tools(propertiesInverse);
		for(int i=0;i<propertiesInverse.size();i++)
		{
			String pairs[]=propertiesInverse.get(i).split("--");
			ArrayList<String> Triples =findIndexOfRelation(Relations,pairs[0]);//其实3元祖后面包含的坐标
			for(int j=0;j<Triples.size();j++)
			{
				String part[]=Triples.get(j).split(",");
				String inverseTriple=part[2]+","+pairs[1]+","+part[0];
				//System.out.println(Relations.contains(inverseTriple));
				if(!Relations.contains(inverseTriple))
				{
					Relations.add(inverseTriple);
					System.out.println(inverseTriple);
					m++;
				}
			}
		}
		System.out.println("新添加的三元组的总数为: "+m);
		return Relations;
	}
	
	public  ArrayList<String> enhancedSubClasses(ArrayList<String> Subclasses,ArrayList<String> EquivalentClass)
	{
		System.out.println("*********************************");
		int m=0,n=0;
		for(int i=0;i<EquivalentClass.size();i++)
		{
			String pairs[]=EquivalentClass.get(i).split(",");
			if(pairs.length==3&&pairs[2].equals("sub"))  //parts[0]是parts[1]的儿子
			{
				int index =findIndexClass(Subclasses,pairs[1]);
				if(index!=-1)//删除原来的，添加新的
				{
					String parts1[]=Subclasses.get(index).split("--");
					String parts2[]=parts1[1].split(",");
					boolean flag=false;
					for(int k=0;k<parts2.length;k++)
					{
						if(parts2[k].equals(pairs[0]))
						{
							flag=true;
							break;
						}
							
					}
					if(flag==false)
					{
						String newDisjointPair=Subclasses.get(index)+","+pairs[0];
						//System.out.println(Classes_Disjoint.get(index));
						//System.out.println(newDisjointPair);
						Subclasses.remove(index);
						Subclasses.add(newDisjointPair);
						m++;
					}			
				}
				else  //直接添加新的，但如果有子类的话，还需要将对应的子类进行添加
				{
					String sub=pairs[1]+"--"+pairs[0];
					Subclasses.add(sub);
					n++;
				}			
			}		
		}
		System.out.println("更改父子关系的总数为: "+m);
		System.out.println("新添加的父子关系的总数为: "+n);
		return Subclasses;
	}
	
	public  ArrayList<String> enhancedSuperClasses(ArrayList<String> Superclasses,ArrayList<String> EquivalentClass)
	{
		System.out.println("*********************************");
		int m=0,n=0;
		for(int i=0;i<EquivalentClass.size();i++)
		{
			String pairs[]=EquivalentClass.get(i).split(",");
			if(pairs.length==3&&pairs[2].equals("sub"))  //parts[0]是parts[1]的儿子
			{
				int index =findIndexClass(Superclasses,pairs[0]);
				if(index!=-1)//删除原来的，添加新的
				{
					String parts1[]=Superclasses.get(index).split("--");
					String parts2[]=parts1[1].split(",");
					boolean flag=false;
					for(int k=0;k<parts2.length;k++)
					{
						if(parts2[k].equals(pairs[1]))
						{
							flag=true;
							break;
						}
							
					}
					if(flag==false)
					{
						String newDisjointPair=Superclasses.get(index)+","+pairs[1];
						//System.out.println(Classes_Disjoint.get(index));
						//System.out.println(newDisjointPair);
						Superclasses.remove(index);
						Superclasses.add(newDisjointPair);
						m++;
					}			
				}
				else  //直接添加新的，但如果有子类的话，还需要将对应的子类进行添加
				{
					String sub=pairs[0]+"--"+pairs[1];
					Superclasses.add(sub);
					n++;
				}			
			}		
		}
		System.out.println("更改子父关系的总数为: "+m);
		System.out.println("新添加的子父关系的总数为: "+n);
		return Superclasses;
	}
	
	public  ArrayList<String> enhancedClassesDisjoint(ArrayList<String> Classes_Disjoint,ArrayList<String> Subclasses,ArrayList<String> EquivalentClass)
	{
		System.out.println("*********************************");
		int m=0,n=0;
		for(int i=0;i<EquivalentClass.size();i++)
		{
			String pairs[]=EquivalentClass.get(i).split(",");
			if(pairs.length==3&&pairs[2].equals("not"))  //parts[0]是parts[1]的儿子
			{
				int index =findIndexClass(Classes_Disjoint,pairs[0]);
				if(index!=-1)//删除原来的，添加新的
				{
					String parts1[]=Classes_Disjoint.get(index).split("--");
					String parts2[]=parts1[1].split(",");
					boolean flag=false;
					for(int k=0;k<parts2.length;k++)
					{
						if(parts2[k].equals(pairs[1]))
						{
							flag=true;
							break;
						}
							
					}
					if(flag==false)
					{
						String newDisjointPair=Classes_Disjoint.get(index)+","+pairs[1];
						//System.out.println(Classes_Disjoint.get(index));
						//System.out.println(newDisjointPair);
						Classes_Disjoint.remove(index);
						Classes_Disjoint.add(newDisjointPair);
						m++;
					}		
				}
				else  //直接添加新的，但如果有子类的话，还需要将对应的子类进行添加
				{
					int indexSub1 =findIndexClass(Subclasses,pairs[0]);
					int indexSub2 =findIndexClass(Subclasses,pairs[1]);
					if(indexSub2!=-1)	//pairs[1]非叶子结点
					{
						String disjoint2=Subclasses.get(indexSub2).replace("--", ",");//直接可以用pairs[1]中subClass的信息加上pairs[1]，自己就是pairs[0]的disjoint信息了。
						String newdisjoint1=pairs[0]+"--"+disjoint2;
						Classes_Disjoint.add(newdisjoint1);
					}
					else  //pairs[1]是叶子结点，即无子类的延生了
					{
						String newdisjoint1=pairs[0]+"--"+pairs[1];
						Classes_Disjoint.add(newdisjoint1);
					}
					if(indexSub1!=-1) //pairs[0]非叶子结点
					{
						String disjoint1=Subclasses.get(indexSub1).replace("--", ",");
						String newdisjoint2=pairs[1]+"--"+disjoint1;
						Classes_Disjoint.add(newdisjoint2);
					}
					else  //pairs[0]是叶子结点，即无子类的延生了
					{
						String newdisjoint2=pairs[1]+"--"+pairs[0];
						Classes_Disjoint.add(newdisjoint2);
					}
					n++;
				}
			
			}			
		}
		System.out.println("更改不想交的总数为: "+m);
		System.out.println("新添加的不想交的总数为: "+n);
		return Classes_Disjoint;
	}
	
	public  ArrayList<String> enhancedRelation(ArrayList<String> ObjectRelations,ArrayList<String> EquivalentClass)
	{
		System.out.println("*********************************");
		int m=0,n=0;
		for(int i=0;i<EquivalentClass.size();i++)
		{
			String pairs[]=EquivalentClass.get(i).split(",");
			if(pairs.length==4&&pairs[2].equals("some"))  //parts[0] ObjectProperty some parts[1]
			{
				ArrayList<String> triple=new ArrayList<String>();
				triple =findIndexOfRelation(ObjectRelations,pairs[1]);//定位属性,且三元组可以不止一个，但只有定义域为null或者不为null两种情况
				//boolean flag1=false;//用来标记三元组的定义域为空的情况
				boolean flag2=false;//用来标记三元组的定义域不为空，但不相等的情况
				for(int j=0;j<triple.size();j++)
				{
					String parts1[]=triple.get(j).split(",");
					/*for(String a:parts1)
					{
						System.out.println(a+" ");
					}
					System.out.println();*/
					
					int index=Integer.parseInt(parts1[3]);
					String domain=parts1[0];
					if(domain.equals("null"))//定义域为空的三元组关系
					{
						String orginalRelation[]=ObjectRelations.get(index).split(",");
						String newRelation=pairs[0]+","+orginalRelation[1]+","+orginalRelation[2];
						ObjectRelations.remove(index);
						ObjectRelations.add(newRelation);
						if(!ObjectRelations.contains(newRelation))
						{
							System.out.println("修改的三元组为: "+newRelation);
							m++;
						}
						//flag1=true;//确实有为空的情况
					}
					else if(!domain.equals("null")&&pairs[0].equals(domain))
					{
						flag2=true;//存在相等的情况
						break;
					}
					
				}
				if(flag2==false)//确实不相等且domain不为0
				{
					triple =findIndexOfRelation(ObjectRelations,pairs[1]);//重新检索一次
					for(int j=0;j<triple.size();j++)
					{
						String parts2[]=triple.get(j).split(",");
						//int index=Integer.parseInt(parts2[3]);
						//String range=parts2[2];
						//String orginalRelation[]=ObjectRelations.get(index).split(",");
						String newRelation=pairs[0]+","+parts2[1]+","+parts2[2];		
						if(!ObjectRelations.contains(newRelation))
						{
							System.out.println("添加的三元组为: "+newRelation);
							ObjectRelations.add(newRelation);
							n++;
						}
					}
				}	
				flag2=false;
			}
		
		}			
		System.out.println("更改关系的总数为: "+m);
		System.out.println("添加关系的总数为: "+n);
		return ObjectRelations;
	}
	
	public  ArrayList<String> transformToRelation(ArrayList<String> ObjectRelations,ArrayList<String> Restriction,ArrayList<String> subclasses)
	{
		System.out.println("*********************************");
		ArrayList<String> partOf=new ArrayList<String>();
		HashMap<String,String> partOfMap=new HashMap<String,String>();
		int n=0;
		String relation="";
		for(int i=0;i<Restriction.size();i++)
		{
			String pairs[]=Restriction.get(i).split(",");
			relation=pairs[1];
			if(pairs.length==4&&pairs[2].equals("some"))  //parts[0] ObjectProperty some parts[1]
			{
				String newRelation=pairs[0]+"--"+pairs[3];		
				if(!partOf.contains(newRelation))
				{
					partOf.add(newRelation.toLowerCase());
					partOfMap.put(pairs[0].toLowerCase(), pairs[3].toLowerCase());
				}
			}	
		}	
		
		TreeMap_Tools partOfRelation=new TreeMap_Tools(partOf);
		for(int i=0;i<partOf.size();i++)
		{
			String part[]=partOf.get(i).split("--");
			String index=part[0];
			while(partOfMap.get(index)!=null)
			{
				String object=partOfMap.get(index);//找到原始的partof关系的object
				if(!partOfMap.get(index).contains(object))//最初肯定是包含的
				{
					partOfRelation.putAdd(part[0], object);
				}
				index=object;//更新索引
			}		
		}
		
		TreeMap_Tools subclass=new TreeMap_Tools(subclasses);
		for(int i=0;i<partOf.size();i++)
		{
			String part[]=partOf.get(i).split("--");
			String index=part[0];
			ArrayList<String> subjects=subclass.GetKey_Value(index);//根据索引找到他的子类
			if(subjects!=null)
			{		
					ArrayList<String> objects=partOfRelation.GetKey_Value(index);//根据索引找到原有partof的Objects
					for(int j=0;j<subjects.size();j++)
					{
						String subject=subjects.get(j);
						partOfRelation.putAdd(subject, objects);
					}

			}
		}
		ArrayList<String> keySet=partOfRelation.GetKey();
		for(int i=0;i<partOfRelation.size();i++)
		{
			String subject=keySet.get(i);
			ArrayList<String> objects=partOfRelation.GetKey_Value(subject);
			if(objects!=null)
				for(int j=0;j<objects.size();j++)
				{
					ObjectRelations.add(subject.toUpperCase()+","+relation+","+objects.get(j).toUpperCase());
					n++;
				}
		}
		System.out.println("Restriciton转换为三元组的总数为: "+n);
		//后续可以继续扩充is-a关系
		return ObjectRelations;
	}
	
	/*public  TreeMap_Tools transformToPartOf(ArrayList<String> Restriction,ArrayList<String> subclasses)
	{
		System.out.println("*********************************");
		//ArrayList<String> partOf=new ArrayList<String>();
		ArrayList<String> partOf=new ArrayList<String>();
		HashMap<String,String> partOfMap=new HashMap<String,String>();
		//int n=0;
		String relation="";
		for(int i=0;i<Restriction.size();i++)
		{
			String pairs[]=Restriction.get(i).split(",");
			relation=pairs[1];
			if(pairs.length==4&&pairs[2].equals("some"))  //parts[0] ObjectProperty some parts[1]
			{
				String newRelation=pairs[0]+"--"+pairs[3];		
				if(!partOf.contains(newRelation))
				{
					partOf.add(newRelation.toLowerCase());
					partOfMap.put(pairs[0].toLowerCase(), pairs[3].toLowerCase());
				}
			}	
		}	
		
		TreeMap_Tools partOfRelation=new TreeMap_Tools(partOf);
		for(int i=0;i<partOf.size();i++)
		{
			String part[]=partOf.get(i).split("--");
			String index=part[0];
			while(partOfMap.get(index)!=null)
			{
				String object=partOfMap.get(index);//找到原始的partof关系的object
				if(!partOfRelation.GetKey_Value(index).contains(object))//最初肯定是包含的
				//if(!partOfMap.get(index).contains(object))//最初肯定是包含的
				{
					partOfRelation.putAdd(part[0], object);
				}
				index=object;//更新索引
			}		
		}
		
		TreeMap_Tools subclass=new TreeMap_Tools(subclasses);
		for(int i=0;i<partOf.size();i++)
		{
			String part[]=partOf.get(i).split("--");
			String index=part[0];
			ArrayList<String> subjects=subclass.GetKey_Value(index);//根据索引找到他的子类
			if(subjects!=null)
			{		
					ArrayList<String> objects=partOfRelation.GetKey_Value(index);//根据索引找到原有partof的Objects
					for(int j=0;j<subjects.size();j++)
					{
						String subject=subjects.get(j);
						partOfRelation.putAdd(subject, objects);
					}
			}
		}
		ArrayList<String> keySet=partOfRelation.GetKey();
		partOf.clear();
		for(int i=0;i<partOfRelation.size();i++)
		{
			String subject=keySet.get(i);
			ArrayList<String> objects=partOfRelation.GetKey_Value(subject);
			if(objects!=null)
				partOf.add(subject)
				for(int j=0;j<objects.size();j++)
				{
					partOf.add(subject.toUpperCase()+","+relation+","+objects.get(j).toUpperCase());
					n++;
				}
		}
		System.out.println("Restriciton转换为partOf的总数为: "+partOfRelation.getNumberOfMap());
		//后续可以继续扩充is-a关系
		return partOfRelation;
	}
	
	public  TreeMap_Tools transformToHaspart(ArrayList<String> Restriction,ArrayList<String> subclasses)
	{
		System.out.println("*********************************");
		ArrayList<String> hasPart=new ArrayList<String>();
		HashMap<String,String> hasPartOfMap=new HashMap<String,String>();
		for(int i=0;i<Restriction.size();i++)
		{
			String pairs[]=Restriction.get(i).split(",");
			if(pairs.length==4&&pairs[2].equals("some"))  //parts[0] ObjectProperty some parts[1]
			{
				String newRelation=pairs[3]+"--"+pairs[0];		
				if(!hasPart.contains(newRelation))
				{
					hasPart.add(newRelation.toLowerCase());
					hasPartOfMap.put(pairs[3].toLowerCase(), pairs[0].toLowerCase());
				}
			}	
		}	
		
		
		TreeMap_Tools hasPartRelation=new TreeMap_Tools(hasPart);
		
		for(int i=0;i<hasPart.size();i++)
		{
			String part[]=hasPart.get(i).split("--");
			String index=part[0];
			while(hasPartOfMap.get(index)!=null)
			{
				String object=hasPartOfMap.get(index);//找到原始的hasPart索引
				if(!hasPartRelation.GetKey_Value(index).contains(object))//最初肯定是包含的
				{
					hasPartRelation.putAdd(part[0], object);
				}
				index=object;//更新索引
			}		
		}
		
		TreeMap_Tools subclass=new TreeMap_Tools(subclasses);
		for(int i=0;i<hasPart.size();i++)
		{
			String part[]=hasPart.get(i).split("--");
			String index=part[1];//应该是找hasPart中Object的部分
			ArrayList<String> objects=subclass.GetKey_Value(index);//根据索引找到他的子类
			if(objects!=null)
			{		
					//ArrayList<String> objects=hasPartRelation.GetKey_Value(index);//根据索引找到原有partof的Objects
					for(int j=0;j<objects.size();j++)
					{
						String object=objects.get(j);
						hasPartRelation.putAdd(index, object);
					}
			}
		}
		ArrayList<String> keySet=partOfRelation.GetKey();
		partOf.clear();
		for(int i=0;i<partOfRelation.size();i++)
		{
			String subject=keySet.get(i);
			ArrayList<String> objects=partOfRelation.GetKey_Value(subject);
			if(objects!=null)
				partOf.add(subject)
				for(int j=0;j<objects.size();j++)
				{
					partOf.add(subject.toUpperCase()+","+relation+","+objects.get(j).toUpperCase());
					n++;
				}
		}
		System.out.println("Restriciton转换为hasPart的总数为: "+hasPartRelation.getNumberOfMap());
		//后续可以继续扩充is-a关系
		return hasPartRelation;
	}*/
	
	public  TreeMap_Tools transformToPartOf(ArrayList<String> Restriction,ArrayList<String> subclasses)
	{
		System.out.println("*********************************");
		//ArrayList<String> partOf=new ArrayList<String>();
		ArrayList<String> partOf=new ArrayList<String>();
		//HashMap<String,String> partOfMap=new HashMap<String,String>();
		HashMap<String, HashSet<String>> partOfMap=new HashMap<String, HashSet<String>>();
		for(int i=0;i<Restriction.size();i++)
		{
			String pairs[]=Restriction.get(i).split(",");
			if(pairs.length==4&&pairs[2].equals("some"))  //parts[0] ObjectProperty some parts[1]
			{
				String newRelation=pairs[0]+"--"+pairs[3];		
				if(!partOf.contains(newRelation))
				{
					partOf.add(newRelation.toLowerCase());
					HashSet<String> a=new HashSet<String>();
					a.add(pairs[3].toLowerCase());
					if(partOfMap.containsKey(pairs[0].toLowerCase()))
					{
						partOfMap.get(pairs[0].toLowerCase()).add(pairs[3].toLowerCase());
					}
					else
						partOfMap.put(pairs[0].toLowerCase(), a);
					//partOfMap.put(pairs[0].toLowerCase(), pairs[3].toLowerCase());
				}
			}	
		}	
		
		System.out.println(partOf.size());
		System.out.println(partOfMap.size());
		
		int num1=0;
		Set<String> keyset1=partOfMap.keySet();
		for(String a:keyset1)
		{
			if(partOfMap.get(a).size()==1)
				num1++;
			else
				num1=num1+partOfMap.get(a).size();
			
		}
		System.out.println("Restriciton转换为partOf的总数为: "+num1);
		//System.out.println(partOfMap.getNumberOfMap());
		
		//ArrayList<String> newPartOfRelation=new ArrayList<String>();

		//TreeMap_Tools partOfRelation=new TreeMap_Tools(partOf);
		TreeMap_Tools partOfRelation=new TreeMap_Tools();
		Queue<String> order = new LinkedList<String>();
		for(int i=0;i<partOf.size();i++)
		{
			String part[]=partOf.get(i).split("--");
			String index=part[0];		//将part of中的上位作为索引
			while(partOfMap.get(index)!=null||!order.isEmpty())//如果根据索引找出来为空，或者序列中没有值了
			{
						
				if(partOfMap.get(index)!=null)
				{
					HashSet<String> b=new HashSet<String>();	
					b.addAll(partOfMap.get(index));
					for(String a: b)
					{
						order.offer(a);
						if(partOfMap.get(a)!=null)
							partOfMap.get(part[0]).addAll(partOfMap.get(a));
					}
					index=order.remove();
				}
				else
				{
					index=order.remove();
				}			
			}
		}
		
		/*TreeMap_Tools subclass=new TreeMap_Tools(subclasses);
		Set<String> keySet= new HashSet<String>();
		keySet.addAll(partOfMap.keySet());
		for(String a:keySet)
		{
			ArrayList<String> objects=subclass.GetKey_Value(a);
			if(objects!=null)
			{
				for(int i=0;i<objects.size();i++)
				{
					String object=objects.get(i);
					if(!partOfMap.containsKey(object))
					{
						partOfMap.put(object,partOfMap.get(a));
						//partOfMap.get(pairs[0].toLowerCase()).add(pairs[3].toLowerCase());
					}
					else
					{
						partOfMap.get(object).addAll(partOfMap.get(a));
					}
				}
			}
		}
		
		int num2=0;
		Set<String> keyset2=partOfMap.keySet();
		for(String a:keyset2)
		{
			if(partOfMap.get(a).size()==1)
				num2++;
			else
				num2=num2+partOfMap.get(a).size();
			
		}
		System.out.println("Restriciton转换为partOf的总数为: "+num2);*/
				
	/*	int num=0;
		Set<String> keyset=partOfMap.keySet();
		for(String a:keyset)
		{
			if(partOfMap.get(a).size()==1)
				num++;
			else
				num=num+partOfMap.get(a).size();
			
		}
		System.out.println("Restriciton转换为partOf的总数为: "+num);*/
		Set<String> keyset=partOfMap.keySet();
		for(String key:keyset)
		{
			HashSet<String>  a=partOfMap.get(key);
			ArrayList<String> values=new ArrayList<String>();
			for(String value:a)
			{
				values.add(value);
			}
			partOfRelation.putAdd(key, values);	
		}
		System.out.println("Restriciton转换为partOf的总数为: "+partOfRelation.getNumberOfMap());
		
		return partOfRelation;
	}
	
	public  TreeMap_Tools transformToHaspart(ArrayList<String> Restriction,ArrayList<String> subclasses)
	{
		System.out.println("*********************************");
		ArrayList<String> hasPart=new ArrayList<String>();
		//HashMap<String,String> hasPartOfMap=new HashMap<String,String>();
		HashMap<String, HashSet<String>> hasPartOfMap=new HashMap<String, HashSet<String>>();
		for(int i=0;i<Restriction.size();i++)
		{
			String pairs[]=Restriction.get(i).split(",");
			if(pairs.length==4&&pairs[2].equals("some"))  //parts[0] ObjectProperty some parts[1]
			{
				String newRelation=pairs[3]+"--"+pairs[0];		
				if(!hasPart.contains(newRelation))
				{
					hasPart.add(newRelation.toLowerCase());
					HashSet<String> a=new HashSet<String>();
					a.add(pairs[0].toLowerCase());
					if(hasPartOfMap.containsKey(pairs[3].toLowerCase()))
					{
						hasPartOfMap.get(pairs[3].toLowerCase()).add(pairs[0].toLowerCase());
					}
					else
						hasPartOfMap.put(pairs[3].toLowerCase(), a);
				//	hasPartOfMap.put(pairs[3].toLowerCase(), pairs[0].toLowerCase());
				}
			}	
		}	
			
		System.out.println(hasPart.size());
		System.out.println(hasPartOfMap.size());
		int num1=0;
		Set<String> keyset1=hasPartOfMap.keySet();
		for(String a:keyset1)
		{
			if(hasPartOfMap.get(a).size()==1)
				num1++;
			else
				num1=num1+hasPartOfMap.get(a).size();
			
		}
		System.out.println("Restriciton转换为hasPart的总数为: "+num1);
		

		
		//TreeMap_Tools hasPartRelation=new TreeMap_Tools(hasPart);	
		TreeMap_Tools hasPartRelation=new TreeMap_Tools();	
		Queue<String> order = new LinkedList<String>();
		for(int i=0;i<hasPart.size();i++)
		{
			String part[]=hasPart.get(i).split("--");
			String index=part[0];		//将part of中的上位作为索引
			while(hasPartOfMap.get(index)!=null||!order.isEmpty())//如果根据索引找出来为空，或者序列中没有值了
			{				
				if(hasPartOfMap.get(index)!=null)
				{				
					HashSet<String> b=new HashSet<String>();
					b.addAll(hasPartOfMap.get(index));
					for(String a: b)
					{
						order.offer(a);
						if(hasPartOfMap.get(a)!=null)
							hasPartOfMap.get(part[0]).addAll(hasPartOfMap.get(a));
					}
					index=order.remove();
				}
				else
				{
					index=order.remove();
				}			
			}
		}
	
		/*TreeMap_Tools subclass=new TreeMap_Tools(subclasses);
		Set<String> keySet= new HashSet<String>();
		keySet.addAll(hasPartOfMap.keySet());
		for(String a:keySet)
		{
			HashSet<String> b=new HashSet<String>();
			b.addAll(hasPartOfMap.get(a));//需要是hasPart中关系的后面部分
			for(String c:b)
			{
				ArrayList<String> objects=subclass.GetKey_Value(c);//通过他们来找到子集的子类
				if(objects!=null)
				{
					hasPartOfMap.get(a).addAll(objects);
					for(int i=0;i<objects.size();i++)//同事子类中可能还有hasPart关系，应进行迭代的添加
					{
						String object=objects.get(i);
						if(hasPartOfMap.get(object)!=null)
							hasPartOfMap.get(a).addAll(hasPartOfMap.get(object));
					}
				}
			}
		}
		
		int num2=0;
		Set<String> keyset2=hasPartOfMap.keySet();
		for(String a:keyset2)
		{
			if(hasPartOfMap.get(a).size()==1)
				num2++;
			else
				num2=num2+hasPartOfMap.get(a).size();
			
		}
		System.out.println("Restriciton转换为hasPart的总数为: "+num2);*/
	/*	int num=0;
		Set<String> keyset=hasPartOfMap.keySet();
		for(String a:keyset)
		{
			if(hasPartOfMap.get(a).size()==1)
				num++;
			else
				num=num+hasPartOfMap.get(a).size();		
		}
		System.out.println("Restriciton转换为hasPart的总数为: "+num);*/
		Set<String> keyset=hasPartOfMap.keySet();
		for(String key:keyset)
		{
			HashSet<String>  a=hasPartOfMap.get(key);
			ArrayList<String> values=new ArrayList<String>();
			for(String value:a)
			{
				values.add(value);
			}
			hasPartRelation.putAdd(key, values);	
		}
		System.out.println("Restriciton转换为hasPart的总数为: "+hasPartRelation.getNumberOfMap());
		
	
		return hasPartRelation;
	}
	
	
	
	public ArrayList<String> findIndexOfRelation(ArrayList<String> Relations,String index)
	{
		ArrayList<String> Triples=new ArrayList<String>();
		for(int i=0;i<Relations.size();i++)
		{
			String parts[]=Relations.get(i).split(",");
			if(index.equals(parts[1]))
			{
				Triples.add(Relations.get(i)+","+i);//同时记录索引的值
			}
		}
		return Triples;
	}
	
	public String findIndexOfPair(ArrayList<String> pair,String index)
	{
		int  num=findIndexClass(pair,index);
		if(num!=-1)
		{
			String parts[]=pair.get(num).split("--");
			return parts[1];	
		}
		else
		{
			return null;
		}
	}
	
	
	
	public int findIndexClass(ArrayList<String> Class,String index)
	{
		//ArrayList<String> Triples=new ArrayList<String>();
		for(int i=0;i<Class.size();i++)
		{
			String parts[]=Class.get(i).split("--");
			if(index.equals(parts[0]))
			{
				return i;
			}
		}
		return -1;
	}
	
	public ArrayList<String> findIndexProperty(ArrayList<String> Relation,String index)
	{
		ArrayList<String> Triples=new ArrayList<String>();
		for(int i=0;i<Relation.size();i++)
		{
			String parts[]=Relation.get(i).split(",");
			if(index.equals(parts[1]))	//为空的情况一定是唯一的
			{
				Triples.add(Relation.get(i));
			}
		}
		return Triples;
	}
	
	public ArrayList<String> GetConcept_Instances()
	{
		ArrayList<String> Concept_Instances=new ArrayList<String>();
		ExtendedIterator<OntClass> classlist = ontology.listNamedClasses(); //反馈的本体是以链表的形式返回
		OntClass temp ;
		while(classlist.hasNext()){		
			temp=classlist.next();
			String Concept_Members="";
			String name=temp.getLocalName().toString();
			//System.out.println(name);
			//name=name.replace("-", "_");//为了画图方便，将'-'替换成'_'
			String members="";
			String instance="";
			if(name.equals("Thing")||name.equals("Nothing"))
				continue;
			ExtendedIterator<? extends OntResource> Instance=temp.listInstances(false);
			while (Instance.hasNext()) {
				OntResource member=Instance.next(); //这里的类型转换只能用OntResource
				instance=member.getLocalName();
				if(instance!=null&&!instance.equals("nil"))
				{
					instance=NLP(instance);//有待改善
					members=members+","+instance;		
				}
			}	
			//System.out.println(!(instance==null));
			
			if(instance!=null&&!members.equals(""))
			{
				Concept_Members=name+"--"+members;	
			//	System.out.println(Concept_Members);
				Concept_Instances.add(Concept_Members);
			}
		}	
		return Concept_Instances;
	}
	
	public String NLP(String name) //对实例进行一定处理
	{
		String tokensWord=tokeningWord(name);
		String tokens[]=tokensWord.split(" ");
		if(tokens.length==1)
			return name;	
		else //用到一些自然处理的技术,待添加,命名实体识别,这里需要将他们标准化
		{
			/*for(int i=0;i<tokens.length;i++)
			{
				if(tokens[i].equals("of"))
				{
					tokens=exchange();//交换两者的位置
				}
			}*/
			return tokensWord;
			//人名的简称复原。
			//日期需要标准化
			//电话号码的识别(这里用到命名实体识别)
			//时态分析
		}		
	}
	
	public String tokeningWord(String str)
	{
		String s1=str;
		//s1="Registration_SIGMOD_Member";
		String ss = "";
		for(int i=0;i<s1.length()-1;i++){
			char aa=s1.charAt(i+1);
			char a=s1.charAt(i);
			if(Character.isUpperCase(a) && i==0)//如果首字母是大写则直接添加
			{
				ss=ss+String.valueOf(a);
			}
			else if(Character.isUpperCase(a) &&Character.isLowerCase(aa)&& i!=0)//如果非字母是大写则需要插入分隔符
			{
				ss=ss+" "+String.valueOf(a);
			}	
			else if((a=='-'||a=='_'||a=='.'))//当出现字符"-","_" 而且后面aa是大写，则不做操作
			{
				//continue;
				ss=ss+" ";//等于间接将'_','-'进行了替换
			}
			else if(Character.isUpperCase(a)&&Character.isUpperCase(aa))
			{
				ss=ss+String.valueOf(a);
			}	
			else if(Character.isLowerCase(a)&&Character.isUpperCase(aa))//前面小写后面接大写
			{
				ss=ss+String.valueOf(a)+" ";
			}	
			else  //其实情况正常添加
			{             
				ss=ss+String.valueOf(a);
			}
		}
		ss=ss+s1.charAt(s1.length()-1);
		ss=ss.replace("  ", " ").trim();
		return ss.toLowerCase().replaceAll("_|-","");		
	}
	
	public ArrayList<String> GetSubclass()
	{
		/*ArrayList<String> Subclasses=new ArrayList<String>();
		ExtendedIterator<OntClass> classlist = ontology.listNamedClasses();
		//String sub_information=null;
		OntClass temp ;
		while(classlist.hasNext())
		{
			temp=classlist.next();
			String a=temp.getLocalName();
			System.out.println(a);
			ExtendedIterator<OntClass> subClasses = temp.listSubClasses(true);		
			if(subClasses.hasNext())
			{
				OntClass temp1=subClasses.next();
				System.out.print(temp1.getLocalName()+" ");
			}
			System.out.println("\n********************");
		}
		return Subclasses;*/
		ArrayList<String> Subclasses=new ArrayList<String>();
		ExtendedIterator<OntClass> classlist = ontology.listNamedClasses();
		String sub_information=null;
		OntClass temp ;
		while(classlist.hasNext()){
			temp=classlist.next();
			System.out.println("***********************");
			System.out.println(temp.getLocalName().toString());
			ExtendedIterator<OntClass> classlist1 = temp.listSubClasses(false);//false表示列出所有的子类，true表示只列出直接子类
			if(classlist1.hasNext())
			{
				String a=temp.getLocalName().toString();
				if(a.charAt(0)=='_')
					a=a.replaceFirst("_", "");
				//a=a.replace("-", "_");//为了画图方便，将'-'替换成'_'
				if(a.equals("Thing")||a.equals("Nothing"))
					continue;
				sub_information=a+"--";
				OntClass temp1;
				while(classlist1.hasNext())
				{
					temp1=classlist1.next();
					String b=temp1.getLocalName().toString();
					sub_information=sub_information+","+b;								
				}
				
				if(sub_information.replace(a, "").equals("--,Nothing"))
					continue;
				else
				{
					sub_information=sub_information.replace(",Nothing","");
					sub_information=sub_information.replace("--,","--");	//考虑可能出先Nothing的情况
					Subclasses.add(sub_information);
				}
			//再写入文件时，最好将"--,"替换成"--"
			}			
		}
		return Subclasses;		
	}
	
	
	public ArrayList<String> GetSubclass_Direct()
	{
		ArrayList<String> Subclasses=new ArrayList<String>();
		ExtendedIterator<OntClass> classlist = ontology.listNamedClasses();
		String sub_information=null;
		OntClass temp ;
		while(classlist.hasNext()){
			temp=classlist.next();
			//将非thing的直接子类给列出来
			ExtendedIterator<OntClass> classlist1 = temp.listSubClasses(true);//false表示列出所有的子类，true表示只列出直接子类
			if(classlist1.hasNext()){
				String a=temp.getLocalName().toString();
				if(a.charAt(0)=='_')
					a=a.replaceFirst("_", "");
				//a=a.replace("-", "_");//为了画图方便，将'-'替换成'_'
				sub_information=a+"--";
				//sub_information=temp.getLocalName().toString()+"--";
			OntClass temp1;
			while(classlist1.hasNext())
			{
				temp1=classlist1.next();
				String b=temp1.getLocalName().toString();
				//b=b.replace("-", "_");//为了画图方便，将'-'替换成'_'
				sub_information=sub_information+","+b;		
				//sub_information=sub_information+","+temp1.getLocalName().toString();				
			 }
			sub_information=sub_information.replace("--,","--");	//考虑可能出先Nothing的情况
			if(!sub_information.replace(a, "").equals("--Nothing"))
				Subclasses.add(sub_information);	
			}	
			/*//将与thing的直接相关的子类列出来
		//	System.out.println(temp.getLocalName().toString());
			ExtendedIterator<OntClass> classlist2 = temp.listSuperClasses(true);//false表示列出所有的子类，true表示只列出直接子类
		//	System.out.println(classlist2.hasNext());
			if(!classlist2.hasNext())    //第一种情况是可能父亲为空的情况
			{
				String a=temp.getLocalName().toString();
				a=a.replace("-", "_");//为了画图方便，将'-'替换成'_'
				sub_information="Thing"+"--"+a;
				//sub_information="Thing"+"--"+temp.getLocalName().toString();	
				Subclasses.add(sub_information)	;		
			 }
			while(classlist2.hasNext()) //第二种情况是可能父亲为Resource的情况
			{
				OntClass temp2;
				temp2=classlist2.next();
				if(temp2.isRestriction()||(temp2.getLocalName()==null)) //如果是匿名类就跳过
				{
					continue;
				}
			//	System.out.println(temp2.isRestriction());
			//	System.out.println(temp2.getLocalName().toString());
				//System.out.println("***********");
				String father=temp2.getLocalName().toString();
			//	father=father.replace("-", "_");//为了画图方便，将'-'替换成'_'
				if(father.equals("Resource")||father.equals("Thing"))
				{	
					String a=temp.getLocalName().toString();
				    a=a.replace("-", "_");//为了画图方便，将'-'替换成'_'		
				    sub_information="Thing"+"--"+a;
					//sub_information="Thing"+"--"+temp.getLocalName().toString();	
					Subclasses.add(sub_information)	;	
					break;
				}
			}*/
			//再写入文件之前，最好将"--,"替换成"--"		
		}
		return Subclasses;
	}
	
	
	/*public ArrayList<String> GetSuperclass()
	{
		ArrayList<String> Superclasses=new ArrayList<String>();
		ExtendedIterator<OntClass> classlist = ontology.listNamedClasses();
		String super_information=null;
		OntClass temp ;
		while(classlist.hasNext())
		{
			temp=classlist.next();
			String a=temp.getLocalName().toString();
			if(a.charAt(0)=='_')
				a=a.replaceFirst("_", "");
			if(a.equals("Thing")||a.equals("Nothing"))
				continue;
			else
			{
				//a=a.replace("-", "_");//为了画图方便，将'-'替换成'_'
				super_information=a+"--Thing";		//任何结点的都有Thing作为其祖先结点
				ExtendedIterator<OntClass> classlist1 = temp.listSuperClasses(false);//false表示列出所有的父类，true表示只列出直接父类
				if(!classlist1.hasNext())    //第一种情况是可能父亲为空的情况
				{
					//continue;
				}
				while(classlist1.hasNext()) //第二种情况是可能父亲为Resource的情况
				{
					OntClass temp2;
					temp2=classlist1.next();
					if(temp2.isRestriction()||(temp2.getLocalName()==null)) //如果是匿名类就跳过
					{
						continue;
					}
					//排除的上面的情况，father就不是一个匿名类，即可以用下面的方式读取出来
					String father=temp2.getLocalName().toString();
					if(father.equals("Resource")||father.equals("Thing"))
					{
						continue; //这里跳过的目的主要是在最后会补上这种情况
					}
					else
					{
						super_information=super_information+","+father;
					}
				}
				Superclasses.add(super_information)	;	
			}
		}
		return Superclasses;
	}*/
	
	public ArrayList<String> GetSuperclass()
	{
		ArrayList<String> Superclasses=new ArrayList<String>();
		ExtendedIterator<OntClass> classlist = ontology.listNamedClasses();
		String super_information="";
		OntClass temp ;
		while(classlist.hasNext())
		{
			boolean flag=false;
			temp=classlist.next();
			String a=temp.getLocalName().toString();
			if(a.charAt(0)=='_')
				a=a.replaceFirst("_", "");
			if(a.equals("Thing")||a.equals("Nothing"))
				continue;
			else
			{
				//a=a.replace("-", "_");//为了画图方便，将'-'替换成'_'
				super_information=a+"--";		//任何结点的都有Thing作为其祖先结点
				
				ExtendedIterator<OntClass> classlist1 = temp.listSuperClasses(false);//false表示列出所有的父类，true表示只列出直接父类
				while(classlist1.hasNext()) //第二种情况是可能父亲为Resource的情况
				{
					OntClass temp2;
					temp2=classlist1.next();
					//不是一个匿名类，且不是Resource或者Thing
					if(!temp2.isAnon()&&!temp2.getLocalName().equals("Resource")&&!temp2.getLocalName().equals("Thing"))
					{
					//排除的上面的情况，father就不是一个匿名类，即可以用下面的方式读取出来
						flag=true;
						String father=temp2.getLocalName().toString();
						super_information=super_information+","+father;
					}
				}
				if(flag==true)
					Superclasses.add(super_information)	;	
			}
			//flag=false;
		}
		return Superclasses;
	}
	
	/*public ArrayList<String> GetSupclass_Direct()
	{
		ArrayList<String> Superclasses=new ArrayList<String>();
		ExtendedIterator<OntClass> classlist = ontology.listNamedClasses();
		String super_information=null;
		OntClass temp ;
		while(classlist.hasNext()){
			temp=classlist.next();
			String a=temp.getLocalName().toString();
			if(a.charAt(0)=='_')
				a=a.replaceFirst("_", "");
			//a=a.replace("-", "_");//为了画图方便，将'-'替换成'_'
			if(a.equals("Thing")||a.equals("Nothing"))
				continue;
			else
			{
			super_information=a+"--Thing";		//任何结点的都有Thing作为其祖先结点
			ExtendedIterator<OntClass> classlist1 = temp.listSuperClasses(true);//false表示列出所有的父类，true表示只列出直接父类
			if(!classlist1.hasNext())    //第一种情况是可能父亲为空的情况
			{
				//continue;
			}
			while(classlist1.hasNext()) //第二种情况是可能父亲为Resource的情况
			{
				OntClass temp2;
				temp2=classlist1.next();
				if(temp2.isRestriction()||(temp2.getLocalName()==null)) //如果是匿名类就跳过
				{
					continue;
				}
				//排除的上面的情况，father就不是一个匿名类，即可以用下面的方式读取出来
				String father=temp2.getLocalName().toString();
				if(father.equals("Resource")||father.equals("Thing"))
				{
					continue; //这里跳过的目的主要是在最后会补上这种情况
				}
				else
				{
					super_information=super_information+","+father;
				}
			}
			Superclasses.add(super_information)	;	
		}
		}
		return Superclasses;
	}*/
	
	public ArrayList<String> GetSupclass_Direct()
	{
		ArrayList<String> Superclasses=new ArrayList<String>();
		ExtendedIterator<OntClass> classlist = ontology.listNamedClasses();
		String super_information="";
		OntClass temp ;
		while(classlist.hasNext())
		{
			boolean flag=false;
			temp=classlist.next();
			String a=temp.getLocalName().toString();
			if(a.charAt(0)=='_')
				a=a.replaceFirst("_", "");
			if(a.equals("Thing")||a.equals("Nothing"))
				continue;
			else
			{
				//a=a.replace("-", "_");//为了画图方便，将'-'替换成'_'
				super_information=a+"--";		//任何结点的都有Thing作为其祖先结点		
				ExtendedIterator<OntClass> classlist1 = temp.listSuperClasses(true);//false表示列出所有的父类，true表示只列出直接父类
				while(classlist1.hasNext()) //第二种情况是可能父亲为Resource的情况
				{
					OntClass temp2;
					temp2=classlist1.next();
					//不是一个匿名类，且不是Resource或者Thing
					if(!temp2.isAnon()&&!temp2.getLocalName().equals("Resource")&&!temp2.getLocalName().equals("Thing"))
					{
					//排除的上面的情况，father就不是一个匿名类，即可以用下面的方式读取出来
						flag=true;
						String father=temp2.getLocalName().toString();
						super_information=super_information+","+father;
					}
				}
				if(flag==true)
					Superclasses.add(super_information)	;	
			}
			//flag=false;
		}
		return Superclasses;
	}
	
	public ArrayList<String> GetEquivalentClass()
	{
		ArrayList<String> EquivalentClass=new ArrayList<String>();
		ExtendedIterator<OntClass> classlist = ontology.listNamedClasses();
		//String equivalentInformation=null;
		OntClass temp ;
		while(classlist.hasNext())
		{
			temp=classlist.next();
			String concept=temp.getLocalName().toString();
			System.out.println(concept);
			if(concept.charAt(0)=='_')
				concept=concept.replaceFirst("_", "");
			//a=a.replace("-", "_");//为了画图方便，将'-'替换成'_'
			if(concept.equals("Thing")||concept.equals("Nothing"))
				continue;
			else
			{	
				ExtendedIterator<OntClass> equivaluentClass = temp.listEquivalentClasses();
				while(equivaluentClass.hasNext()) //第二种情况是可能父亲为Resource的情况
				{
					OntClass equivalent=equivaluentClass.next();
					if(!equivalent.isAnon())//直接存在等价的情况
					{
						String equivalentConcept=equivalent.getLocalName().toString();
						if(!equivalentConcept.equals(concept)&&!equivalentConcept.equals("Nothing"))
							EquivalentClass.add(concept+","+equivalentConcept+","+"Equal");
					}
					else
					{
						if(equivalent.isRestriction())						
						{
							Restriction res = equivalent.asRestriction();
							String relation = "";
							String restriction = "";
							String type = "";
							if (res.isSomeValuesFromRestriction()) //OP some Class
							{
								System.out.println("some!");
								SomeValuesFromRestriction res_some = res.asSomeValuesFromRestriction();
								relation = res_some.getOnProperty().getLocalName();
								OntClass a = (OntClass) res_some.getSomeValuesFrom();
								restriction = "some";
								type = a.getLocalName();
								if (a.isUnionClass()) 
								{
									UnionClass uc = a.asUnionClass();
									ExtendedIterator<? extends OntClass> uco = uc.listOperands();
									while (uco.hasNext()) 
									{
										type = uco.next().getLocalName();
										String constraint = concept+","+relation + ","+ restriction + "," + type;
										System.out.println(constraint);
										EquivalentClass.add(constraint);
									}
								}
								else 
								{
									String constraint = concept+","+relation + "," + restriction+ "," + type;
									System.out.println(constraint);
									EquivalentClass.add(constraint);
								}
							}					
						}//这是has some class的情况
						else if(equivalent.isUnionClass())//or的情况,但是推理机已经能做到了
						{
							UnionClass uc = equivalent.asUnionClass();
							ExtendedIterator<? extends OntResource> uco = uc.listOperands();
							while (uco.hasNext()) {
								OntResource subConcept=uco.next();
								//String subConcept = uco.next();
								if(!subConcept.isAnon())
								{							
									EquivalentClass.add(subConcept.getLocalName()+","+concept+","+"sub");
									System.out.println(subConcept.getLocalName()+","+concept+","+"sub");
								}				
							}
						}
						else if(equivalent.isIntersectionClass())//and 的情况,其实只需要考虑not的情况
						{
							IntersectionClass uc = equivalent.asIntersectionClass();
							ExtendedIterator<? extends OntResource> uco = uc.listOperands();
							while (uco.hasNext()) {
								OntResource subConcept=uco.next();
								if(!subConcept.isAnon())//意义不大，因为父子关系已经用and体现出来了
								{
									EquivalentClass.add(concept+","+subConcept.getLocalName()+","+"sub");
									System.out.println(concept+","+subConcept.getLocalName()+","+"sub");
								}
								if(subConcept.asClass().isComplementClass())//可以适当的补充
								{
									ComplementClass uc1 = subConcept.asClass().asComplementClass();
									ExtendedIterator<? extends OntClass> uco1 = uc1.listOperands();
									while (uco1.hasNext()) {
										OntResource disjointConcept=uco1.next();
										if(!disjointConcept.isAnon()&&!disjointConcept.getLocalName().equals("Nothing"))
										{
											EquivalentClass.add(concept+","+disjointConcept.getLocalName()+","+"not");
											EquivalentClass.add(disjointConcept.getLocalName()+","+concept+","+"not");
											System.out.println(concept+","+disjointConcept.getLocalName()+","+"not");
										}				
									}
								}
							}
						}
						else if(equivalent.isComplementClass())//not 的情况
						{
							ComplementClass uc = equivalent.asComplementClass();
							ExtendedIterator<? extends OntClass> uco = uc.listOperands();
							while (uco.hasNext()) {
								OntResource disjointConcept=uco.next();
								if(!disjointConcept.isAnon()&&!disjointConcept.getLocalName().equals("Nothing"))
								{
									EquivalentClass.add(concept+","+disjointConcept.getLocalName()+","+"not");
									EquivalentClass.add(disjointConcept.getLocalName()+","+concept+","+"not");
									System.out.println(concept+","+disjointConcept.getLocalName()+","+"not");
								}				
							}
						}
					}			
				}
			}
		}
		return EquivalentClass;
	}
	
	public  ArrayList<String> GetEquivalentProperty()
	{
		ArrayList<String> EquivalentProperties=new ArrayList<String>();
		ExtendedIterator<ObjectProperty> propertylist = ontology.listObjectProperties(); //反馈的本体是以链表的形式返回
		ObjectProperty temp ;	
		while(propertylist.hasNext())
		{
			temp=propertylist.next();		
			String property=temp.getLocalName().toString();
			if(property.equals("topObjectProperty")||property.equals("bottomObjectProperty"))
				continue;
			else
			{	
				ExtendedIterator<? extends OntProperty> equivalentProperties = temp.listEquivalentProperties();
				while(equivalentProperties.hasNext()) //第二种情况是可能父亲为Resource的情况
				{
					OntProperty equivalent=equivalentProperties.next();
					if(!equivalent.isAnon())//直接存在等价的情况
					{
						String equivalentProperty=equivalent.getLocalName().toString();
						if(!equivalentProperty.equals(property))
							EquivalentProperties.add(property+","+equivalentProperty+","+"Equal");
					}
				}
			}
		}
		return EquivalentProperties;
	}
	
	public ArrayList<String> GetSibling(ArrayList<String> Subclasses_Direct)
	{
		ArrayList<String> Sibling=new ArrayList<String>();
		for(int i=0;i<Subclasses_Direct.size();i++)
		{
			String father_children[]=Subclasses_Direct.get(i).split("--");
			if(father_children[0].equals("Thing"))
				continue;
			else
			{
				String children[]=father_children[1].split(",");
				if(children.length>1)
				{
					String sibling_information="";
					for(int j=0;j<children.length;j++)
					{
						//考虑添加的位置可能是最后一位，也可以是第一位
						String others=father_children[1].replace(children[j]+",", "").replace(","+children[j], "");
						sibling_information=children[j].toString()+"--"+others;
					/*	if(!Sibling.contains(sibling_information))
							Sibling.add(sibling_information);	*/				
						/*for(int k=0;k<children.length;k++)
						{
							if(k!=j)
							{
								sibling_information=sibling_information+","+children[k].toString();
							}
						}
						if(!Sibling.contains(sibling_information))
							Sibling.add(sibling_information);*/
					}		
				}
			}		
		}	
		return Sibling;
	}
	
	public ArrayList<String> GetDisjointwith()
	{
		ArrayList<String> Disjointion=new ArrayList<String>();
		ExtendedIterator<OntClass> classlist = ontology.listNamedClasses();
		String sub_information=null;
		OntClass temp ;
		while(classlist.hasNext()){
			temp=classlist.next();
			String a=temp.getLocalName().toString();
			if(a.equals("Thing")||a.equals("Nothing"))
				continue;
			else
			{
			ExtendedIterator<OntClass> disjointionlist = temp.listDisjointWith();//false表示列出所有的子类，true表示只列出直接子类
			if(disjointionlist.hasNext()){
			//	String a=temp.getLocalName().toString();
			//	a=a.replace("-", "_");//为了画图方便，将'-'替换成'_'
				sub_information=a+"--";
				OntClass temp1;
				while(disjointionlist.hasNext())
				{
					temp1=disjointionlist.next();
					String b=temp1.getLocalName().toString();
					//if(!b.equals("Nothing"))
						sub_information=sub_information+","+b;	
					//b=b.replace("-", "_");//为了画图方便，将'-'替换成'_'
								
				}
				sub_information=sub_information.replace("--,","--");	//考虑可能出先Nothing的情况
				if(sub_information.replace(a, "").equals("--Nothing"))
					continue;
				else
				Disjointion.add(sub_information.replace("--,", "--").replace(",Nothing", ""));
			//再写入文件时，最好将"--,"替换成"--"
			}		
		}
		}
		return Disjointion;
	}
	
	public ArrayList<String> GetLeaves()
	{
		//将概念是叶子情况用链表的形式进行反馈
		ArrayList<String> leaf=new ArrayList<String>();
		ExtendedIterator<OntClass> classlist = ontology.listNamedClasses();
		String sub_information="";
		OntClass temp ;
		while(classlist.hasNext()){
			temp=classlist.next();
			String a=temp.getLocalName().toString();
			if(a.charAt(0)=='_')
				a=a.replaceFirst("_", "");
			//System.out.println(a);
			ExtendedIterator<OntClass> classlist1 = temp.listSubClasses(true);	
			while(classlist1.hasNext())
			{
				OntClass temp1=classlist1.next();
				String b=temp1.getLocalName().toString();
			//	a=a.replace("-","_");//为了画图方便，将'-'替换成'_'		
				sub_information=b+sub_information;
				//leaf.add(temp.getLocalName().toString());
			}
			if(sub_information.equals("Nothing"))
				leaf.add(a);	
			sub_information="";
		}
		return leaf;
	}
	
	public ArrayList<String> get_reference(String alignmentFile)
	{
	    ArrayList<String> references=new ArrayList<String>();  
	    Model model = ModelFactory.createDefaultModel();
		InputStream in = FileManager.get().open( alignmentFile );
        if (in == null) {
            throw new IllegalArgumentException( "File: " + alignmentFile + " not found");
        }
        model.read(in,"");
		//model.read(in,null);
        //解析方式1(针对YAM++)
/*		Property alignmententity1, alignmententity2;
		alignmententity1 = model.getProperty("http://knowledgeweb.semanticweb.org/heterogeneity/alignment#entity1");
		alignmententity2 = model.getProperty("http://knowledgeweb.semanticweb.org/heterogeneity/alignment#entity2");
		Property value = model.getProperty("http://knowledgeweb.semanticweb.org/heterogeneity/alignment#measure");
		Property relation = model.getProperty("http://knowledgeweb.semanticweb.org/heterogeneity/alignment#relation");*/
		//OntClass temp;
        //解释方式2(针对AML++,reference alignments)
	    Property alignmententity1 = model.getProperty("http://knowledgeweb.semanticweb.org/heterogeneity/alignmententity1");
		Property alignmententity2 = model.getProperty("http://knowledgeweb.semanticweb.org/heterogeneity/alignmententity2");//alignment
		Property value = model.getProperty("http://knowledgeweb.semanticweb.org/heterogeneity/alignmentmeasure");
		Property relation = model.getProperty("http://knowledgeweb.semanticweb.org/heterogeneity/alignmentrelation");

        ResIterator resstmt = model.listSubjectsWithProperty(alignmententity1);	//两者方法是一样的
	  
		//Answer an iterator [with no duplicates] over all the resources in this model that have property p
      //  ResIterator resstmt = model.listResourcesWithProperty(alignmententity1);
		//StmtIterator resstmt = model.listStatements();
	//	int id1,id2;
	    Resource temp;
	    //Resource temp,entity1,entity2;
	//	ResIterator resstmt = model.listSubjectsWithProperty(alignmententity1);
		//System.out.print(resstmt.hasNext());
		while(resstmt.hasNext()){
			temp = resstmt.next();//temp是三元组
			String entity1 = temp.getPropertyResourceValue(alignmententity1).getLocalName();//获取本体1的本体
			String entity2 = temp.getPropertyResourceValue(alignmententity2).getLocalName();//获取本体2的本体
			String Confidence=temp.getProperty(value).getObject().asLiteral().getString();//获取信念值
			String Relation=temp.getProperty(relation).getObject().toString();//获取匹配的关系
			
			//比较笨的方法
			/*String Confidence=temp.getProperty(value).getLiteral().toString();
			Confidence=Confidence.replace("^^xsd:float", "").replace("^^http://www.w3.org/2001/XMLSchema#float", "");
			System.out.println(Confidence);*/
			
			
		//	System.out.println(Relation);
		//	System.out.println(entity1+" "+entity2);
			System.out.println(entity1+" "+entity2+" "+Relation+" "+Confidence);
			
			//输出所有的三元组
	/*		StmtIterator stmt = model.listStatements();
			while(stmt.hasNext()){
				System.out.println(stmt.next());
			}*/
			entity1=entity1.replace("-", "_");//为了画图方便，将'-'替换成'_'		
			entity2=entity2.replace("-", "_");//为了画图方便，将'-'替换成'_'		
			references.add(entity1+"--"+entity2);//统一转化成小写
		}
		return references;
	}
	

	
	public ArrayList<String> filter_property(ArrayList<String> reference,ArrayList<String> concept1)
	{
		ArrayList<String> Class_Match=new ArrayList<String>();
		for(int i=0;i<reference.size();i++)
		{
			String[] Match_pairs = reference.get(i).split("--");//只要取前面一部分即本体1中的概念对比即可
			//System.out.println(Match_pairs[0]);
			for(int j=0;j<concept1.size();j++)
			{
				//Session_Chair
				//SessionChair
			//	System.out.println(concept1.get(j));
				if(Match_pairs[0].equals(concept1.get(j)))
				{
					Class_Match.add(reference.get(i));
					break;
				}
				//有时候难免出现多一个"_"而不匹配的情况
				else if(Match_pairs[0].replace("_", "").equals(concept1.get(j)))
				{
					Class_Match.add(reference.get(i));
					break;
				}
				else if(Match_pairs[0].equals(concept1.get(j).replace("_", "")))
				{
					Class_Match.add(reference.get(i));
					break;
				}
			}
		}		
		//将reference中包含属性的匹配对进行过滤
		return Class_Match;
	}
	
	public ArrayList<String> filter_class(ArrayList<String> reference,ArrayList<String> class_match)
	{
		ArrayList<String> Property_Match=new ArrayList<String>();
		boolean label[]=new boolean[reference.size()];
		for(int i=0;i<reference.size();i++)
		{
			//String[] Match_pairs = reference.get(i).split("--");//只要取前面一部分即本体1中的概念对比即可
			//System.out.println(Match_pairs[0]);
			
			for(int j=0;j<class_match.size();j++)
			{
				if(reference.get(i).equals(class_match.get(j)))
				{
					label[i]=true;
					break;					
				}			
			}
		}	
		//匹配对中那些不是概念的匹配对，默认将其看做属性的匹配对。
		for(int k=0;k<reference.size();k++)
		{
			if(label[k]!=true)
			{
				Property_Match.add(reference.get(k));
			}
		}
		//将reference中包含属性的匹配对进行过滤
		return Property_Match;
	}
	
	
    //获取只包含子类的匹配
	public ArrayList<String> Get_haschild_match(ArrayList<String> Match,ArrayList<String> Leaf1,ArrayList<String> Leaf2) //获取双方都有孩子的匹配对
	{
		ArrayList<String> constrained_match=new ArrayList<String>();
        boolean flag1=false,flag2=false;
		for(int i=0;i<Match.size();i++)
		{
			
			//将匹配对进行切分判断
			String[] Match_pairs = Match.get(i).split("--");
			if(Leaf1==null)
			{
				flag1=true;
			}
			else
			{
			 for(int j=0;j<Leaf1.size();j++)
			 {
				//System.out.println(Leaf1.get(j));
				//看匹配对的第一部分是否存在本体1中叶子节点
				if(Match_pairs[0].equals(Leaf1.get(j)))
				{
					flag1=false;
					break;
				}
				flag1=true;
			 }
			}
			if(Leaf2==null)
			{		
				flag2=true;
			}
			else
			{
			 for(int L=0;L<Leaf2.size();L++)			
			 {
				//看匹配对的第二部分是否存在本体2中叶子节点
				if(Match_pairs[1].equals(Leaf2.get(L)))
				{
					flag2=false;
					break;
				}
				flag2=true;
			 }
			}
			if(flag1==true&&flag2==true)
			{
				constrained_match.add(Match.get(i));
			}
		}		
		return constrained_match;
	}
	
	public ArrayList<String> findConceptChildrenforObjcet(String concept )//找到某一个概念的领域限制
	{
		ArrayList<String> children = new ArrayList<String>();
		ArrayList<String> new_children = new ArrayList<String>();
		OntClass temp = null;
		temp = ontology.getOntClass(OntoID + concept);
		if(temp==null)
		{
			//children.add(concept);//不需要重复添加
			return children;
		}
		ExtendedIterator<OntClass> classes = temp.listSubClasses(true);//迭代器只要一经转换就会清除
		//System.out.println(concept);
		/*System.out.println(classes.toList().size());
		System.out.println(classes.toList().size()==1);
		System.out.println(classes.toList().size());*/
		int m=0; //有些有Nothing,有些没有Nothing
		while(classes.hasNext())
		{
			OntClass temp1=classes.next();				
			String b=temp1.getLocalName().toString();	
			if(!b.equals("Nothing"))
			{
				children.add(b);	
				m++;
			}
			//只有一个儿子的递归情况,其实这样会导致满足这种关系的可能性越来越低
			/*temp = ontology.getOntClass(OntoID + b);
				classes = temp.listSubClasses(true);*/		
			//再写入文件时，最好将"--,"替换成"--"	
		}
		if(m==1)
		{
			ArrayList<String> oneSubClass=findConceptChildrenforObjcet(children.get(0));
			if(oneSubClass.size()==1)
				children.add(oneSubClass.get(0));
			return children;
		}
		else
			return new_children;
	}
	
	public ArrayList<String> findConceptChildrenforDatatype(String concept )//找到某一个概念的领域限制
	{
		ArrayList<String> children = new ArrayList<String>();
		OntClass temp = null;
		temp = ontology.getOntClass(OntoID + concept);
		if(temp==null)
		{
			//children.add(concept);//不需要重复添加
			return children;
		}
		ExtendedIterator<OntClass> classes = temp.listSubClasses(false);
		//System.out.println(concept);
		if(classes.hasNext())//相当于数据库的思想。父亲包含这个属性，那么儿子肯定有
		{		
			OntClass temp1;
			while(classes.hasNext())
			{
				temp1=classes.next();
				String b=temp1.getLocalName().toString();
				if(b.equals("Nothing"))
					continue;
				children.add(b);						
			}
			
		//再写入文件时，最好将"--,"替换成"--"
		}		
		return children;
	}
	
	
	public ArrayList<String> FindConcept_superclasses(String concept )//找到某一个概念的领域限制
	{
		ArrayList<String> superclasses = new ArrayList<String>();
		OntClass temp = null;
		temp = ontology.getOntClass(OntoID + concept);
		System.out.println(concept);
		ExtendedIterator<OntClass> superclass = temp.listSuperClasses(false);
		while (superclass.hasNext()) {
			OntClass Restriction = superclass.next();
			// System.out.println(Restriction.getLocalName().toString());
			// //父子关系式不需要了，只需要抽取匿名类的属性
			if (Restriction.isRestriction()) {
				System.out.println("****************************");
				// System.out.println(Restriction.isRestriction());
				// System.out.println(Restriction);
				Restriction res = Restriction.asRestriction();
				// System.out.println(res.isMinCardinalityRestriction());
				String relation = "";
				String restriction = "";
				String type = "";
				if (res.isAllValuesFromRestriction()) {
					System.out.println("Only!");
					AllValuesFromRestriction res_all = res
							.asAllValuesFromRestriction();
					relation = res_all.getOnProperty().getLocalName();
					// System.out.println(res_all.getOnProperty().getLocalName());
					OntClass a = (OntClass) res_all.getAllValuesFrom();
					restriction = "only";
					type = a.getLocalName();
					if (a.isUnionClass()) {
						UnionClass uc = a.asUnionClass();
						ExtendedIterator<? extends OntClass> uco = uc
								.listOperands();
						while (uco.hasNext()) {
							type = uco.next().getLocalName();
							String constraint = relation + "," + restriction
									+ "," + type;
							System.out.println(constraint);
							superclasses.add(constraint);
						}
					} else {
						String constraint = relation + "," + restriction + ","
								+ type;
						System.out.println(constraint);
						superclasses.add(constraint);
					}
				} else if (res.isSomeValuesFromRestriction()) {
					System.out.println("some!");
					SomeValuesFromRestriction res_some = res
							.asSomeValuesFromRestriction();
					relation = res_some.getOnProperty().getLocalName();
					// System.out.println(res_some.getOnProperty().getLocalName());
					OntClass a = (OntClass) res_some.getSomeValuesFrom();
					restriction = "some";
					type = a.getLocalName();
					String constraint = relation + "," + restriction + ","
							+ type;
					System.out.println(constraint);
					superclasses.add(constraint);
				} else if (res.isHasValueRestriction()) {
					System.out.println("hasvalue!!");
					HasValueRestriction res_hasvalue = res
							.asHasValueRestriction();
					relation = res_hasvalue.getOnProperty().getLocalName();
					// System.out.println(res_hasvalue.getOnProperty().getLocalName());
					restriction = "value";
					type = res_hasvalue.getHasValue().toString();
					String constraint = relation + "," + restriction + ","
							+ type;
					System.out.println(constraint);
					superclasses.add(constraint);
				} else if (res.isCardinalityRestriction()) {
					System.out.println("exactly!");
					CardinalityRestriction res_exact = res
							.asCardinalityRestriction();
					relation = res_exact.getOnProperty().getLocalName();
					restriction = "exactly " + res_exact.getCardinality();
					NodeIterator value = res_exact.listPropertyValues(null);
					while (value.hasNext()) {
						RDFNode a = value.next();
						// System.out.println();
						if (a.isLiteral()) {
							type = "Literal";
							break;
						} else if(!a.isAnon()&&a.asResource().getLocalName().equals("Thing"))
						{
							type="Thing";
							break;
						}
					}
					String constraint = relation + "," + restriction + ","
							+ type;
					System.out.println(constraint);
					superclasses.add(constraint);
					// System.out.println(res_exact.getOnProperty().getLocalName());
					// System.out.println(res_exact.getCardinality());

					// res_exact.isLiteral();
					// ExtendedIterator<RDFNode>label=res_exact.listPropertyValues(res_exact.getOnProperty());
					// System.out.println(res_exact.getPropertyResourceValue(null));
					/*
					 * NodeIterator value=res_exact.listPropertyValues(null);
					 * System.out.println(value.hasNext()); // int count=0;
					 * while(value.hasNext()) { RDFNode a=value.next(); //
					 * System.out.println(); System.out.println(a.isLiteral());
					 * System.out.println(a); }
					 */

				} else if (res.isMaxCardinalityRestriction()) {
					System.out.println("Max!");
					MaxCardinalityRestriction res_max = res
							.asMaxCardinalityRestriction();
					relation = res_max.getOnProperty().getLocalName();
					restriction = "Max " + res_max.getMaxCardinality();
					// OntProperty pro=res_max.getOnProperty();
					NodeIterator value = res_max.listPropertyValues(null);
					while (value.hasNext()) {
						RDFNode a = value.next();
						// System.out.println();
						if (a.isLiteral()) {
							type = "Literal";
							break;
						} else if(!a.isAnon()&&a.asResource().getLocalName().equals("Thing"))
						{
							type="Thing";
							break;
						}
					}
					String constraint = relation + "," + restriction + ","
							+ type;
					System.out.println(constraint);
					superclasses.add(constraint);
					/*
					 * //
					 * System.out.println(res_max.getOnProperty().getLocalName
					 * ()); System.out.println(res_max.getMaxCardinality()); //
					 * OntClass a=(OntClass) res_min.get(); NodeIterator
					 * value=res_max.listPropertyValues(null);
					 * System.out.println(value.hasNext()); // int count=0;
					 * while(value.hasNext()) { RDFNode a=value.next(); //
					 * System.out.println(); System.out.println(a.isLiteral());
					 * System.out.println("***************");
					 * System.out.println(a); }
					 */
				} else if (res.isMinCardinalityRestriction()) {
					System.out.println("Min!");
					MinCardinalityRestriction res_min = res
							.asMinCardinalityRestriction();
					relation = res_min.getOnProperty().getLocalName();
					restriction = "Min " + res_min.getMinCardinality();
					// OntProperty pro=res_max.getOnProperty();
					NodeIterator value = res_min.listPropertyValues(null);
					while (value.hasNext()) {
						RDFNode a = value.next();
						// System.out.println();
						if (a.isLiteral()) {
							type = "Literal";
							break;
						} 
						else if(!a.isAnon()&&a.asResource().getLocalName().equals("Thing"))
						{
							type="Thing";
							break;
						}
					}
					String constraint = relation + "," + restriction + ","
							+ type;
					System.out.println(constraint);
					superclasses.add(constraint);
					/*
					 * OntProperty pro=res_min.getOnProperty();
					 * System.out.println("&&&&&&&&&&&&&&&&&");
					 * System.out.println
					 * (res_min.getOnProperty().getLocalName());
					 * System.out.println(res_min.getMinCardinality()); //
					 * OntClass a=(OntClass) res_min.get(); NodeIterator
					 * value=res_min.listPropertyValues(null);
					 * System.out.println(value.hasNext()); // int count=0;
					 * while(value.hasNext()) { RDFNode a=value.next(); //
					 * System.out.println(); System.out.println(a.isLiteral());
					 * System.out.println("***************");
					 * System.out.println(a); }
					 */

					/*
					 * System.out.println(res_min.getPropertyValue(pro));
					 * System.out.println(res_min.getLabel(concept));
					 */
				} else {
					System.out.println("None restriction!");
				}

			} 
			else if(Restriction.isUnionClass())
			{
				UnionClass uc = Restriction.asUnionClass();
				ExtendedIterator<? extends OntClass> uco = uc.listOperands();
				while (uco.hasNext()) 
				{
					String U_father = uco.next().getLocalName();
					ArrayList<String> new_Restrictions=FindFather_Restriction(U_father);
					for(String a:new_Restrictions)
					{
						superclasses.add(concept+","+a);
					}
				}
			}
			else if(Restriction.isIntersectionClass())
			{
				IntersectionClass ic=Restriction.asIntersectionClass();
				ExtendedIterator<? extends OntClass> uco = ic.listOperands();
				System.out.print("++++++++++++++++++++++++++");
				while (uco.hasNext()) 
				{
					OntClass Intersection= uco.next();
					if(Intersection.isRestriction())
					{
						Restriction res2 = Intersection.asRestriction();
						// System.out.println(res.isMinCardinalityRestriction());
						String relation = "";
						String restriction = "";
						String type = "";
						if (res2.isAllValuesFromRestriction()) {
							System.out.println("Only!");
							AllValuesFromRestriction res_all = res2
									.asAllValuesFromRestriction();
							relation = res_all.getOnProperty().getLocalName();
							// System.out.println(res_all.getOnProperty().getLocalName());
							OntClass a = (OntClass) res_all.getAllValuesFrom();
							restriction = "only";
							type = a.getLocalName();
							if (a.isUnionClass()) {
								UnionClass uc = a.asUnionClass();
								ExtendedIterator<? extends OntClass> uco2 = uc
										.listOperands();
								while (uco2.hasNext()) {
									type = uco2.next().getLocalName();
									String constraint = relation + "," + restriction
											+ "," + type;
									System.out.println(constraint);
									superclasses.add(constraint);
								}
							} else {
								String constraint = relation + "," + restriction + ","
										+ type;
								System.out.println(constraint);
								superclasses.add(constraint);
							}
						} 
						else if (res2.isSomeValuesFromRestriction()) {
							System.out.println("some!");
							SomeValuesFromRestriction res_some = res2
									.asSomeValuesFromRestriction();
							relation = res_some.getOnProperty().getLocalName();
							// System.out.println(res_some.getOnProperty().getLocalName());
							OntClass a = (OntClass) res_some.getSomeValuesFrom();
							restriction = "some";
							type = a.getLocalName();
							if (a.isUnionClass()) {
								UnionClass uc = a.asUnionClass();
								ExtendedIterator<? extends OntClass> uco2 = uc
										.listOperands();
								while (uco2.hasNext()) {
									type = uco2.next().getLocalName();
									String constraint = relation + "," + restriction
											+ "," + type;
									System.out.println(constraint);
									superclasses.add(constraint);
								}
							} else {
								String constraint = relation + "," + restriction + ","
										+ type;
								System.out.println(constraint);
								superclasses.add(constraint);
							}
						} 
					}
					
					
					//System.out.print(Restriction);
					
					/*ArrayList<String> new_Restrictions=FindFather_Restriction(U_father);
					for(String a:new_Restrictions)
					{
						superclasses.add(concept+","+a);
					}*/
				}
			}
			else if(Restriction!=null)//迭代调用，因为该概念继承其父亲的领域受限
			{
				String Father=Restriction.getLocalName().toString();
				if(!Father.equals("Thing"))
				{
						ArrayList<String> new_Restrictions=FindFather_Restriction(Restriction.getLocalName().toString());
						for(String a:new_Restrictions)
						{
							superclasses.add(a);
						}
				}
			}
		}

		// temp.getSuperClass();

		return superclasses;
	}
	
	
	public ArrayList<String> FindFather_Restriction(String concept )//这样就可以避免可能出现的迭代情况出现
	{
		ArrayList<String> superclasses=new ArrayList<String>();
		//OntClass temp = null;
		OntClass temp ;
		System.out.println(concept);
		temp = ontology.getOntClass(OntoID + concept);
		if(temp==null)
			return null;
				ExtendedIterator<OntClass> superclass=temp.listSuperClasses(false);
				while(superclass.hasNext())
				{
					OntClass Restriction=superclass.next();		
					////父子关系式不需要了，只需要抽取匿名类的属性
					if(Restriction.isRestriction())
					{							
						System.out.println("****************************");
						//System.out.println(Restriction.isRestriction());
						//System.out.println(Restriction);
						Restriction res=Restriction.asRestriction();
						//System.out.println(res.isMinCardinalityRestriction());
						String relation="";
						String restriction="";
						String type="";	
						if(res.isAllValuesFromRestriction())
						{
							System.out.println("Only!");
							AllValuesFromRestriction res_all= res.asAllValuesFromRestriction();
							relation=res_all.getOnProperty().getLocalName();
							//System.out.println(res_all.getOnProperty().getLocalName());
							OntClass a=(OntClass) res_all.getAllValuesFrom();
							restriction="only";
							type=a.getLocalName();
							if(a.isUnionClass())
							{
								UnionClass uc = a.asUnionClass();
								ExtendedIterator<? extends OntClass> uco = uc.listOperands();
								while (uco.hasNext())
								{
									type=uco.next().getLocalName();
									String constraint=relation+","+restriction+","+type;
									System.out.println(constraint);
									superclasses.add(constraint);
								}
							}
							else
							{
								String constraint=relation+","+restriction+","+type;
								System.out.println(constraint);
								superclasses.add(constraint);
							}
						}
						else if(res.isSomeValuesFromRestriction())
						{
							System.out.println("some!");
							SomeValuesFromRestriction res_some= res.asSomeValuesFromRestriction();
							relation=res_some.getOnProperty().getLocalName();
							//System.out.println(res_some.getOnProperty().getLocalName());
							OntClass a=(OntClass) res_some.getSomeValuesFrom();
							restriction="some";
							type=a.getLocalName();
							if(a.isUnionClass())
							{
								UnionClass uc = a.asUnionClass();
								ExtendedIterator<? extends OntClass> uco = uc.listOperands();
								while (uco.hasNext())
								{
									type=uco.next().getLocalName();
									String constraint=relation+","+restriction+","+type;
									System.out.println(constraint);
									superclasses.add(constraint);
								}
							}
							else
							{
								String constraint=relation+","+restriction+","+type;
								System.out.println(constraint);
								superclasses.add(constraint);
							}
						}
						else if(res.isHasValueRestriction())
						{
							System.out.println("hasvalue!!");
							HasValueRestriction res_hasvalue= res.asHasValueRestriction();
							relation=res_hasvalue.getOnProperty().getLocalName();
							//System.out.println(res_hasvalue.getOnProperty().getLocalName());
							restriction="value";
							type=res_hasvalue.getHasValue().toString();
							String constraint=relation+","+restriction+","+type;
							System.out.println(constraint);
							superclasses.add(constraint);
						}
						else if(res.isCardinalityRestriction())
						{
							System.out.println("exactly!");
							CardinalityRestriction res_exact= res.asCardinalityRestriction();
							relation=res_exact.getOnProperty().getLocalName();
							restriction="exactly "+res_exact.getCardinality();
							NodeIterator  value=res_exact.listPropertyValues(null);
						    while(value.hasNext())
						   {
									RDFNode a=value.next();
								//	System.out.println();
								if(a.isLiteral())									
							   {
								  type="Literal";
								  break;
							   }
								else if(!a.isAnon()&&a.asResource().getLocalName().equals("Thing"))
								{
									type="Thing";
									break;
								}																		
							}
						    String constraint=relation+","+restriction+","+type;
							System.out.println(constraint);	
							superclasses.add(constraint);
							//System.out.println(res_exact.getOnProperty().getLocalName());
							//System.out.println(res_exact.getCardinality());
							
							//res_exact.isLiteral();
							//ExtendedIterator<RDFNode>label=res_exact.listPropertyValues(res_exact.getOnProperty());
						//	System.out.println(res_exact.getPropertyResourceValue(null));
	/*					  NodeIterator  value=res_exact.listPropertyValues(null);
						 System.out.println(value.hasNext());
					//	 int count=0;
						 while(value.hasNext())
							{
								RDFNode a=value.next();
							//	System.out.println();
								System.out.println(a.isLiteral());
								System.out.println(a);													
							}*/
							
						}
						else if(res.isMaxCardinalityRestriction())
						{
							System.out.println("Max!");
							MaxCardinalityRestriction res_max= res.asMaxCardinalityRestriction();
							relation=res_max.getOnProperty().getLocalName();
							restriction="Max "+res_max.getMaxCardinality();
							//OntProperty pro=res_max.getOnProperty();							
							NodeIterator  value=res_max.listPropertyValues(null);
						    while(value.hasNext())
						   {
									RDFNode a=value.next();
								//	System.out.println();
								if(a.isLiteral())									
							   {
								  type="Literal";
								  break;
							   }
								else if(!a.isAnon()&&a.asResource().getLocalName().equals("Thing"))
								{
									type="Thing";
									break;
								}																			
							}
						    String constraint=relation+","+restriction+","+type;
							System.out.println(constraint);	
							superclasses.add(constraint);
						/*//	System.out.println(res_max.getOnProperty().getLocalName());
							System.out.println(res_max.getMaxCardinality());							
						//	OntClass a=(OntClass) res_min.get();							
							  NodeIterator  value=res_max.listPropertyValues(null);							  
								 System.out.println(value.hasNext());
							//	 int count=0;
								 while(value.hasNext())
									{
										RDFNode a=value.next();
									//	System.out.println();
										System.out.println(a.isLiteral());
										System.out.println("***************");		
										System.out.println(a);													
									}*/
						}
						else if(res.isMinCardinalityRestriction())
						{
							System.out.println("Min!");
							MinCardinalityRestriction res_min= res.asMinCardinalityRestriction();
							relation=res_min.getOnProperty().getLocalName();
							restriction="Min "+res_min.getMinCardinality();
							//OntProperty pro=res_max.getOnProperty();							
							NodeIterator  value=res_min.listPropertyValues(null);
						    while(value.hasNext())
						   {
									RDFNode a=value.next();
								//	System.out.println();
								if(a.isLiteral())									
							   {
								  type="Literal";
								  break;
							   }
								else if(!a.isAnon()&&a.asResource().getLocalName().equals("Thing"))
								{
									type="Thing";
									break;
								}																		
							}
						    String constraint=relation+","+restriction+","+type;
							System.out.println(constraint);
							superclasses.add(constraint);
							/*OntProperty pro=res_min.getOnProperty();							
							System.out.println("&&&&&&&&&&&&&&&&&");		
							System.out.println(res_min.getOnProperty().getLocalName());
							System.out.println(res_min.getMinCardinality());							
						//	OntClass a=(OntClass) res_min.get();							
							  NodeIterator  value=res_min.listPropertyValues(null);							  
								 System.out.println(value.hasNext());
							//	 int count=0;
								 while(value.hasNext())
									{
										RDFNode a=value.next();
									//	System.out.println();
										System.out.println(a.isLiteral());
										System.out.println("***************");		
										System.out.println(a);													
									}*/
								 								 
							/*System.out.println(res_min.getPropertyValue(pro));
							System.out.println(res_min.getLabel(concept));*/							
						}
						else 
						{
							System.out.println("None restriction!");
						}					
				}
					else if(Restriction.isUnionClass())
					{
						UnionClass uc = Restriction.asUnionClass();
						ExtendedIterator<? extends OntClass> uco = uc.listOperands();
						while (uco.hasNext()) 
						{
							String U_father = uco.next().getLocalName();
							ArrayList<String> new_Restrictions=FindFather_Restriction(U_father);
							for(String a:new_Restrictions)
							{
								superclasses.add(concept+","+a);
							}
						}
					}
					else if(Restriction.isIntersectionClass())
					{
						IntersectionClass ic=Restriction.asIntersectionClass();
						ExtendedIterator<? extends OntClass> uco = ic.listOperands();
						System.out.print("++++++++++++++++++++++++++");
						while (uco.hasNext()) 
						{
							OntClass Intersection= uco.next();
							if(Intersection.isRestriction())
							{
								Restriction res2 = Intersection.asRestriction();
								// System.out.println(res.isMinCardinalityRestriction());
								String relation = "";
								String restriction = "";
								String type = "";
								if (res2.isAllValuesFromRestriction()) {
									System.out.println("Only!");
									AllValuesFromRestriction res_all = res2
											.asAllValuesFromRestriction();
									relation = res_all.getOnProperty().getLocalName();
									// System.out.println(res_all.getOnProperty().getLocalName());
									OntClass a = (OntClass) res_all.getAllValuesFrom();
									restriction = "only";
									type = a.getLocalName();
									if (a.isUnionClass()) {
										UnionClass uc = a.asUnionClass();
										ExtendedIterator<? extends OntClass> uco2 = uc
												.listOperands();
										while (uco2.hasNext()) {
											type = uco2.next().getLocalName();
											String constraint = relation + "," + restriction
													+ "," + type;
											System.out.println(constraint);
											superclasses.add(constraint);
										}
									} else {
										String constraint = relation + "," + restriction + ","
												+ type;
										System.out.println(constraint);
										superclasses.add(constraint);
									}
								} else if (res2.isSomeValuesFromRestriction()) {
									System.out.println("some!");
									SomeValuesFromRestriction res_some = res2
											.asSomeValuesFromRestriction();
									relation = res_some.getOnProperty().getLocalName();
									// System.out.println(res_some.getOnProperty().getLocalName());
									OntClass a = (OntClass) res_some.getSomeValuesFrom();
									restriction = "some";
									type = a.getLocalName();
									if (a.isUnionClass()) {
										UnionClass uc = a.asUnionClass();
										ExtendedIterator<? extends OntClass> uco2 = uc
												.listOperands();
										while (uco2.hasNext()) {
											type = uco2.next().getLocalName();
											String constraint = relation + "," + restriction
													+ "," + type;
											System.out.println(constraint);
											superclasses.add(constraint);
										}
									} else {
										String constraint = relation + "," + restriction + ","
												+ type;
										System.out.println(constraint);
										superclasses.add(constraint);
									}
								} 
							}
						}
					}
			}				
		//temp.getSuperClass();
		return superclasses;
	}
	
	public ArrayList<String> findFatherSomeRestriction(String concept )//这样就可以避免可能出现的迭代情况出现
	{
		ArrayList<String> superclasses=new ArrayList<String>();
		//OntClass temp = null;
		OntClass temp ;
		System.out.println(concept);
		temp = ontology.getOntClass(OntoID + concept);
		if(temp==null)
			return null;
		ExtendedIterator<OntClass> superclass=temp.listSuperClasses(false);
		while(superclass.hasNext())
		{
			OntClass Restriction=superclass.next();		
			////父子关系式不需要了，只需要抽取匿名类的属性
			if(Restriction.isRestriction())
			{							
				System.out.println("****************************");
				//System.out.println(Restriction.isRestriction());
				//System.out.println(Restriction);
				Restriction res=Restriction.asRestriction();
				//System.out.println(res.isMinCardinalityRestriction());
				String relation="";
				String restriction="";
				String type="";	
				if(res.isSomeValuesFromRestriction())
				{
					System.out.println("some!");
					SomeValuesFromRestriction res_some= res.asSomeValuesFromRestriction();
					relation=res_some.getOnProperty().getLocalName();
					//System.out.println(res_some.getOnProperty().getLocalName());
					OntClass a=(OntClass) res_some.getSomeValuesFrom();
					restriction="some";
					type=a.getLocalName();
					if(a.isUnionClass())
					{
						UnionClass uc = a.asUnionClass();
						ExtendedIterator<? extends OntClass> uco = uc.listOperands();
						while (uco.hasNext())
						{
							type=uco.next().getLocalName();
							String constraint=relation+","+restriction+","+type;
							System.out.println(constraint);
							superclasses.add(constraint);
						}
					}
					else
					{
						String constraint=relation+","+restriction+","+type;
						System.out.println(constraint);
						superclasses.add(constraint);
					}
				}					
			}
		}				
		//temp.getSuperClass();
		return superclasses;
	}
	
	public ArrayList<String> GetAllRestrictions()//找到某个本体所有概念的领域限制
	{
		ArrayList<String> Restrictions=new ArrayList<String>();
		ExtendedIterator<OntClass> classlist = ontology.listNamedClasses(); //反馈的本体是以链表的形式返回
	    OntClass temp = null; 
	    
		while(classlist.hasNext())
		{
			temp=classlist.next();	
			String concept=temp.getLocalName();
			if(concept.equals("Nothing")||concept.equals("Thing"))
				continue;
		    //打印输出类的一种方式
			//System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
			//System.out.println(concept);
			ExtendedIterator<OntClass> superclass=temp.listSuperClasses(false);
			while(superclass.hasNext())
			{
				OntClass Restriction=superclass.next();		
				////父子关系式不需要了，只需要抽取匿名类的属性
				if(Restriction.isRestriction())
				{							
					//System.out.println("****************************");
					// System.out.println(Restriction.isRestriction());
					// System.out.println(Restriction);
					Restriction res = Restriction.asRestriction();
					// System.out.println(res.isMinCardinalityRestriction());
					String relation = "";
					String restriction = "";
					String type = "";
					if (res.isAllValuesFromRestriction()) 
					{
						//System.out.println("Only!");
						AllValuesFromRestriction res_all = res
								.asAllValuesFromRestriction();
						relation = res_all.getOnProperty().getLocalName();
						//System.out.println(res_all.getOnProperty().getLocalName
						// ());
						OntClass a = (OntClass) res_all.getAllValuesFrom();
						restriction = "only";
						type = a.getLocalName();
						if (a.isUnionClass()) 
						{
							UnionClass uc = a.asUnionClass();
							ExtendedIterator<? extends OntClass> uco = uc
									.listOperands();
							while (uco.hasNext()) {
								type = uco.next().getLocalName();
								String constraint = concept+","+relation + ","
										+ restriction + "," + type;
								//System.out.println(constraint);
								Restrictions.add(constraint);
							}
						} 
						else 
						{
							String constraint = concept+","+relation + "," + restriction
									+ "," + type;
							//System.out.println(constraint);
							Restrictions.add(constraint);
						}
					} 
					else if (res.isSomeValuesFromRestriction()) 
					{
						//System.out.println("some!");
						SomeValuesFromRestriction res_some = res
								.asSomeValuesFromRestriction();
						relation = res_some.getOnProperty().getLocalName();
						// System.out.println(res_some.getOnProperty().
						// getLocalName());
						OntClass a = (OntClass) res_some.getSomeValuesFrom();
						restriction = "some";
						type = a.getLocalName();
						if (a.isUnionClass()) 
						{
							UnionClass uc = a.asUnionClass();
							ExtendedIterator<? extends OntClass> uco = uc
									.listOperands();
							while (uco.hasNext()) {
								type = uco.next().getLocalName();
								String constraint = concept+","+relation + ","
										+ restriction + "," + type;
								//System.out.println(constraint);
								Restrictions.add(constraint);
							}
						} 
						else 
						{
							String constraint = concept+","+relation + "," + restriction
									+ "," + type;
							//System.out.println(constraint);
							Restrictions.add(constraint);
						}
					} 
					else if (res.isHasValueRestriction()) 
					{
						//System.out.println("hasvalue!!");
						HasValueRestriction res_hasvalue = res
								.asHasValueRestriction();
						relation = res_hasvalue.getOnProperty().getLocalName();
						// System.out.println(res_hasvalue.getOnProperty().
						// getLocalName());
						restriction = "value";
						type = res_hasvalue.getHasValue().toString();						
						String constraint = concept+","+relation + "," + restriction + ","
								+ type;
						//System.out.println(constraint);
						Restrictions.add(constraint);
					} 
					else if (res.isCardinalityRestriction()) 
					{
						//System.out.println("exactly!");
						CardinalityRestriction res_exact = res
								.asCardinalityRestriction();
						relation = res_exact.getOnProperty().getLocalName();
						restriction = "exactly," + res_exact.getCardinality();
						NodeIterator value = res_exact.listPropertyValues(null);
						/*try
						{*/
						while (value.hasNext()) 
						{				
							RDFNode a = value.next();
							System.out.println(a);
							// System.out.println();
							if (a.isLiteral()) {
								type = "Literal";
								break;
							}
							else if(!a.isAnon()&&a.asResource().getLocalName().equals("Thing"))
							{
								type="Thing";
								break;
							}
						}
						/*}
						catch(Exception e)
						{
							System.out.print("11");
						}*/
						
						
						String constraint = concept+","+relation + "," + restriction + ","
								+ type;
						//System.out.println(constraint);
						Restrictions.add(constraint);
					} 
					else if (res.isMaxCardinalityRestriction()) 
					{
						//System.out.println("Max!");
						MaxCardinalityRestriction res_max = res
								.asMaxCardinalityRestriction();
						relation = res_max.getOnProperty().getLocalName();
						restriction = "Max," + res_max.getMaxCardinality();
						// OntProperty pro=res_max.getOnProperty();
						NodeIterator value = res_max.listPropertyValues(null);
						while (value.hasNext()) {
							RDFNode a = value.next();
							// System.out.println();
							if (a.isLiteral()) {
								type = "Literal";
								break;
							} 
							else if(!a.isAnon()&&a.asResource().getLocalName().equals("Thing"))
							{
								type="Thing";
								break;
							}
						}
						String constraint = concept+","+relation + "," + restriction + ","
								+ type;
						//System.out.println(constraint);
						Restrictions.add(constraint);
					} 
					else if (res.isMinCardinalityRestriction())
					{
						//System.out.println("Min!");
						MinCardinalityRestriction res_min = res
								.asMinCardinalityRestriction();
						relation = res_min.getOnProperty().getLocalName();
						restriction = "Min," + res_min.getMinCardinality();
						// OntProperty pro=res_max.getOnProperty();
						NodeIterator value = res_min.listPropertyValues(null);
						while (value.hasNext()) {
							RDFNode a = value.next();
							// System.out.println();
							if (a.isLiteral()) {
								type = "Literal";
								break;
							} 
							else if(!a.isAnon()&&a.asResource().getLocalName().equals("Thing"))
							{
								type="Thing";
								break;
							}
						}
						String constraint = concept+","+relation + "," + restriction + ","
								+ type;
						//System.out.println(constraint);
						Restrictions.add(constraint);
					} 
					else 
					{
						System.out.println("None restriction!");
					}					
				}
				else if(Restriction.isUnionClass())
				{
					UnionClass uc = Restriction.asUnionClass();
					ExtendedIterator<? extends OntClass> uco = uc.listOperands();
					while (uco.hasNext()) 
					{
						String U_father = uco.next().getLocalName();
						ArrayList<String> new_Restrictions=FindFather_Restriction(U_father);
						for(String a:new_Restrictions)
						{
							Restrictions.add(concept+","+a);
						}
					}
				}
				else if(Restriction.isIntersectionClass())
				{
					IntersectionClass ic=Restriction.asIntersectionClass();
					ExtendedIterator<? extends OntClass> uco = ic.listOperands();
					System.out.print("++++++++++++++++++++++++++");
					while (uco.hasNext()) 
					{
						OntClass Intersection= uco.next();
						if(Intersection.isRestriction())
						{
							Restriction res2 = Intersection.asRestriction();
							// System.out.println(res.isMinCardinalityRestriction());
							String relation = "";
							String restriction = "";
							String type = "";
							if (res2.isAllValuesFromRestriction()) {
								System.out.println("Only!");
								AllValuesFromRestriction res_all = res2
										.asAllValuesFromRestriction();
								relation = res_all.getOnProperty().getLocalName();
								// System.out.println(res_all.getOnProperty().getLocalName());
								OntClass a = (OntClass) res_all.getAllValuesFrom();
								restriction = "only";
								type = a.getLocalName();
								if (a.isUnionClass()) {
									UnionClass uc = a.asUnionClass();
									ExtendedIterator<? extends OntClass> uco2 = uc
											.listOperands();
									while (uco2.hasNext()) {
										type = uco2.next().getLocalName();
										String constraint = relation + "," + restriction
												+ "," + type;
										System.out.println(constraint);
										Restrictions.add(constraint);
									}
								} else {
									String constraint = relation + "," + restriction + ","
											+ type;
									System.out.println(constraint);
									Restrictions.add(constraint);
								}
							} else if (res2.isSomeValuesFromRestriction()) {
								System.out.println("some!");
								SomeValuesFromRestriction res_some = res2
										.asSomeValuesFromRestriction();
								relation = res_some.getOnProperty().getLocalName();
								// System.out.println(res_some.getOnProperty().getLocalName());
								OntClass a = (OntClass) res_some.getSomeValuesFrom();
								restriction = "some";
								type = a.getLocalName();
								if (a.isUnionClass()) {
									UnionClass uc = a.asUnionClass();
									ExtendedIterator<? extends OntClass> uco2 = uc
											.listOperands();
									while (uco2.hasNext()) {
										type = uco2.next().getLocalName();
										String constraint = relation + "," + restriction
												+ "," + type;
										System.out.println(constraint);
										Restrictions.add(constraint);
									}
								} else {
									String constraint = relation + "," + restriction + ","
											+ type;
									System.out.println(constraint);
									Restrictions.add(constraint);
								}
							} 
						}
					}
				}
				else if(Restriction!=null)//迭代调用，因为该概念继承其父亲的领域受限
				{
					String Father=Restriction.getLocalName().toString();
					if(!Father.equals("Thing"))
					{
							ArrayList<String> new_Restrictions=FindFather_Restriction(Restriction.getLocalName().toString());
							if(new_Restrictions!=null)//因为有类的扩展表达形式无法获得结果，因此要将这种空指针的情况过滤掉
							{
								for(String a:new_Restrictions)
								{
									Restrictions.add(concept+","+a);
								}
							}
					}				
				}
			}
		}			
				
		//temp.getSuperClass();
		return Restrictions;
	}
	
	public ArrayList<String> GetSomeRestrictions()//找到某个本体所有概念的领域限制
	{
		ArrayList<String> someRestrictions=new ArrayList<String>();
		ExtendedIterator<OntClass> classlist = ontology.listNamedClasses(); //反馈的本体是以链表的形式返回
	    OntClass temp = null; 
	    
		while(classlist.hasNext())
		{
			temp=classlist.next();	
			String concept=temp.getLocalName();
			if(concept.equals("Nothing"))
				continue;
		    //打印输出类的一种方式
			System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
			System.out.println(concept);
			ExtendedIterator<OntClass> superclass=temp.listSuperClasses(false);
			while(superclass.hasNext())
			{
				OntClass Restriction=superclass.next();		
				////父子关系式不需要了，只需要抽取匿名类的属性
				if(Restriction.isRestriction())
				{							
					//System.out.println("****************************");
					// System.out.println(Restriction.isRestriction());
					// System.out.println(Restriction);
					Restriction res = Restriction.asRestriction();
					// System.out.println(res.isMinCardinalityRestriction());
					String relation = "";
					String restriction = "";
					String type = "";
					if (res.isSomeValuesFromRestriction()) 
					{
						//System.out.println("some!");
						SomeValuesFromRestriction res_some = res.asSomeValuesFromRestriction();
						relation = res_some.getOnProperty().getLocalName();
						// System.out.println(res_some.getOnProperty().
						// getLocalName());
						OntClass a = (OntClass) res_some.getSomeValuesFrom();
						restriction = "some";
						type = a.getLocalName();
						if (a.isUnionClass()) 
						{
							UnionClass uc = a.asUnionClass();
							ExtendedIterator<? extends OntClass> uco = uc.listOperands();
							while (uco.hasNext()) {
								type = uco.next().getLocalName();
								String constraint = concept+","+relation + ","+ restriction + "," + type;
								//System.out.println(constraint);
								someRestrictions.add(constraint);
							}
						} 
						else 
						{
							String constraint = concept+","+relation + "," + restriction
									+ "," + type;
							//System.out.println(constraint);
							someRestrictions.add(constraint);
						}
					} 
				else if(!Restriction.isAnon())//迭代调用，因为该概念继承其父亲的领域受限
				{
					String Father=Restriction.getLocalName().toString();
					if(!Father.equals("Thing"))
					{
							ArrayList<String> new_Restrictions=findFatherSomeRestriction(Restriction.getLocalName().toString());
							if(new_Restrictions!=null)//因为有类的扩展表达形式无法获得结果，因此要将这种空指针的情况过滤掉
							{
								for(String a:new_Restrictions)
								{
									someRestrictions.add(concept+","+a);
								}
							}
					}				
				}
			}
		}
	 }			
		//temp.getSuperClass();
		return someRestrictions;
	}
	
	public int GetConcept_Path(String concept)
	{	
		//ExtendedIterator<OntClass> classlist = ontology.listNamedClasses();
		String b="";
		System.out.println("概念为:"+concept);
		OntClass temp=ontology.getOntClass(OntoID+concept);
		int depth=0;
		//boolean flag=false;
			ExtendedIterator<OntClass> classlist1 = temp.listSuperClasses(false);//false表示列出所有的子类，true表示只列出直接子类
			if(classlist1.hasNext()){				
				OntClass temp1;
				while(classlist1.hasNext()){
					temp1=classlist1.next();
					if(temp1.isRestriction()||(temp1.getLocalName()==null))
					{
						continue;
					}
					else
					{
						b=temp1.getLocalName().toString();
						System.out.println(b);				
					}
					if(b.equals("Thing")||b.equals("Resource"))//表明其父类用Resource或者Thing表达出现
					{
						continue;
					}
					else //遇到其他的父亲概念深度加1
					{
						depth++;
					}
			 }
			}
			depth=depth+1;
		return depth;
	}
}
