package Tools;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;


import java.util.Iterator;


import java.util.Set;

import com.hp.hpl.jena.graph.FrontsNode;
import com.hp.hpl.jena.ontology.AllValuesFromRestriction;
import com.hp.hpl.jena.ontology.CardinalityRestriction;
import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.HasValueRestriction;
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

public class Ontology_tools {
	
/*	 Reasoner reasoner = PelletReasonerFactory.theInstance().create();
	    // create an empty model
	     Model rawModel = ModelFactory.createDefaultModel( );        
	       // create an inferencing model using Pellet reasoner
	     RDF_Model = ModelFactory.createInfModel( reasoner, emptyModel );
	     OntModel model = ModelFactory.createOntologyModel(
	    		 PelletReasonerFactory.THE_SPEC, rawModel);*/
	
/*	Reasoner reasoner = PelletReasonerFactory.theInstance().create();
	Model rawModel = ModelFactory.createDefaultModel( );
	OntModel ontology = ModelFactory.createOntologyModel(PelletReasonerFactory.THE_SPEC, rawModel);*/
	
	OntModel ontology = ModelFactory.createOntologyModel();
	String OntoID="";
	
	
	//�жϱ����Ƿ���سɹ�
	public void readOnto(String f) 
	{
		try {
			ontology.read(new FileInputStream(f), "");
			System.out.println("loadOntology");
			String concept="";
			OntClass temp=null;
			ExtendedIterator<OntClass> classlist = ontology.listNamedClasses();
			while(classlist.hasNext()){			//ǰ�����ѭ����Ҫ��Ϊ�˶�λ������
				temp=classlist.next();
				concept=temp.getLocalName().toString();	
				OntoID=temp.getURI().replace(concept, "");
				System.out.println("URL is:"+OntoID);
				break;
			}
		} catch (FileNotFoundException ex) {
			System.out.println("cuowu");
		}
	}	
	
	public ArrayList<String> GetAllConcept()
	{
		//ע������Ҫ��ִ��readOnto��ȷ���ļ��Ķ���
		ArrayList<String> classes=new ArrayList<String>();
		ExtendedIterator<OntClass> classlist = ontology.listNamedClasses(); //�����ı��������������ʽ����
		OntClass temp ;
		//ArrayList<String> class1=new ArrayList<String>();  
		while(classlist.hasNext()){
			temp=classlist.next();
			String a=temp.getLocalName().toString();
			//a=a.replace("-", "_");//Ϊ�˻�ͼ���㣬��'-'�滻��'_'
			classes.add(a);
			//classes.add(temp.getLocalName().toString());
			}
		return classes;
	}
	
	public ArrayList<String> GetAllProperty()
	{
		ArrayList<String> Properties=new ArrayList<String>();
		ExtendedIterator<ObjectProperty> propertylist1 = ontology.listObjectProperties(); //�����ı��������������ʽ����
		ObjectProperty temp1 ;
		//int count=0;
		while(propertylist1.hasNext()){
			temp1=propertylist1.next();
			String property=temp1.getLocalName().toString();
			Properties.add(property);
		}
		ExtendedIterator<DatatypeProperty> propertylist2 = ontology.listDatatypeProperties(); //�����ı��������������ʽ����
		DatatypeProperty temp2 ;
		//int count=0;
		while(propertylist2.hasNext()){
			temp2=propertylist2.next();
			String property=temp2.getLocalName().toString();
			Properties.add(property);
		}
		return Properties;
	}
	
	
	public ArrayList<String> GetAllRelations()//����ֻ��ȡobjectproperty�Ĺ�ϵ
	{
		ArrayList<String> Relations=new ArrayList<String>();
		ExtendedIterator<ObjectProperty> propertylist = ontology.listObjectProperties(); //�����ı��������������ʽ����
		ObjectProperty temp ;
		while(propertylist.hasNext()){
			temp=propertylist.next();
			
			String property=temp.getLocalName().toString();
			String subject="";
			String object="";
		//	System.out.println("************************");
		//	System.out.println(property);		
            /*ExtendedIterator<? extends OntResource> Domains=temp.listDomain();
			ExtendedIterator<? extends OntResource> Ranges=temp.listRange();*/
        	//System.out.println(temp.getDomain().isAnon());//�жϸýڵ��Ƿ���һ��RDF Node
			//����ȡʽ�������صĺ������
			// ExtendedIterator<? extends OntClass> PropertyRanges = temp.getRange().asClass().asUnionClass().listOperands();	
			
		//	subject=temp.getDomain().getLocalName();
		//	object=temp.getRange().getLocalName();
		//	System.out.println(temp.getDomain());
		//	System.out.println(temp.getRange());
			//������ֵ�򶼲�Ϊ��
			if(temp.getDomain()!=null&&temp.getRange()!=null)
			{
			 if(!temp.getDomain().isAnon()&&!temp.getRange().isAnon())
			 {
				subject=temp.getDomain().getLocalName();
				object=temp.getRange().getLocalName();
				String Triple=subject+","+property+","+object;
				System.out.println(Triple);
				Relations.add(Triple);			 
			 }
			 	
			 else if(temp.getDomain().isAnon()&&!temp.getRange().isAnon())
			{
				 object=temp.getRange().getLocalName();
				 //����������������ȡ��or�����ڵ����
				 ExtendedIterator<? extends OntClass> PropertyDomains = temp.getDomain().asClass().asUnionClass().listOperands();				 
				 while (PropertyDomains.hasNext()) 
				 {					
					subject=PropertyDomains.next().getLocalName();		
				//	System.out.println(subject);				
					String Triple=subject+","+property+","+object;
					System.out.println(Triple);
					Relations.add(Triple);
				 }
			}
			 
			 else if(!temp.getDomain().isAnon()&&temp.getRange().isAnon())
			{
				 subject=temp.getDomain().getLocalName();
				 //���ֵ���������ȡ��or�����ڵ����
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
			 
			 else if(temp.getDomain().isAnon()&&temp.getRange().isAnon()) //��������д�����������
				{		
					//���������ֵ�򶼴�����ȡ��or�����ڵ����
					/*Property prop =temp; 	//temp ��һ������
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
							Relations.add(Triple);
						}			
					 }
				}
			 			 
			}
			
			//������Ϊ�ա�ֵ��Ϊ��
			 if(temp.getDomain()!=null&&temp.getRange()==null)
			{
				 if(!temp.getDomain().isAnon())
				 {
					subject=temp.getDomain().getLocalName();
					object=null;
					String Triple=subject+","+property+","+object;
					System.out.println(Triple);
					Relations.add(Triple);			 
				 }
				 	
				 else if(temp.getDomain().isAnon())
				{
					 object=null;
					 //����������������ȡ��or�����ڵ����
					 ExtendedIterator<? extends OntClass> PropertyDomains = temp.getDomain().asClass().asUnionClass().listOperands();				 
					 while (PropertyDomains.hasNext()) 
					 {					
						subject=PropertyDomains.next().getLocalName();		
					//	System.out.println(subject);				
						String Triple=subject+","+property+","+object;
						System.out.println(Triple);
						Relations.add(Triple);
					 }
				}			
			}
			 
				//������Ϊ�ա�ֵ��Ϊ��
			 if(temp.getDomain()==null&&temp.getRange()!=null)
			{
				 if(!temp.getRange().isAnon())
				 {
					subject=null;
					object=temp.getRange().getLocalName();
					String Triple=subject+","+property+","+object;
					System.out.println(Triple);
					Relations.add(Triple);			 
				 }
				 	
				 else if(temp.getRange().isAnon())
					{
						 subject=null;
						 //���ֵ���������ȡ��or�����ڵ����
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
			 
		}	//while propertylist.hasNext() �����Ƕ�Objectproperties
		
		System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++");		
/*		ExtendedIterator<DatatypeProperty> propertylist2 = ontology.listDatatypeProperties(); //�����ı��������������ʽ����
		DatatypeProperty temp2 ;
		while(propertylist2.hasNext()){
			temp2=propertylist2.next();
			String property=temp2.getLocalName().toString();
			System.out.println(property);
		}*/
		
		ExtendedIterator<DatatypeProperty> propertylist2 = ontology.listDatatypeProperties(); //�����ı��������������ʽ����
		DatatypeProperty temp2;
		while(propertylist2.hasNext()){
			temp2=propertylist2.next();
			String property=temp2.getLocalName().toString();
			String subject="";
			String object="";
			System.out.println("####################################");
			System.out.println(property);	
			
			
			if(temp2.getDomain()!=null&&temp2.getRange()!=null)
			{
			 if(!temp2.getDomain().isAnon()&&!temp2.getRange().isAnon())
			 {
				subject=temp2.getDomain().getLocalName();
				object=temp2.getRange().getLocalName();
				String Triple=subject+","+property+","+object;
				System.out.println(Triple);
				Relations.add(Triple);			 
			 }
			 	
			 else if(temp2.getDomain().isAnon()&&!temp2.getRange().isAnon())
			{
				 object=temp2.getRange().getLocalName();
				 //����������������ȡ��or�����ڵ����
				 ExtendedIterator<? extends OntClass> PropertyDomains = temp2.getDomain().asClass().asUnionClass().listOperands();				 
				 while (PropertyDomains.hasNext()) 
				 {					
					subject=PropertyDomains.next().getLocalName();		
				//	System.out.println(subject);				
					String Triple=subject+","+property+","+object;
					System.out.println(Triple);
					Relations.add(Triple);
				 }
			}
			 
			 else if(!temp2.getDomain().isAnon()&&temp2.getRange().isAnon()&&temp2.getRange().asClass().isUnionClass())
			{
				 subject=temp2.getDomain().getLocalName();
				 //���ֵ���������ȡ��or�����ڵ����
				 ExtendedIterator<? extends OntClass> PropertyRanges = temp2.getRange().asClass().asUnionClass().listOperands();				 
				 while (PropertyRanges.hasNext()) 
				 {					
					object=PropertyRanges.next().getLocalName();		
				//	System.out.println(subject);				
					String Triple=subject+","+property+","+object;
					System.out.println(Triple);
					Relations.add(Triple);
				 }
			}
			 
			 else if(temp2.getDomain().isAnon()&&temp2.getRange().isAnon()&&temp2.getRange().asClass().isUnionClass()) //��������д�����������
				{		
					//���������ֵ�򶼴�����ȡ��or�����ڵ����
				//	Property prop =temp2; 	//temp ��һ������
				//	ExtendedIterator<? extends OntClass> ConcetpDomains = ((OntProperty)prop).getDomain().asClass().asUnionClass().listOperands();
					ExtendedIterator<? extends OntClass> PropertyDomains = temp2.getDomain().asClass().asUnionClass().listOperands();
					ExtendedIterator<? extends OntClass> PropertyRanges = temp2.getRange().asClass().asUnionClass().listOperands();
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
							Relations.add(Triple);
						}			
					 }
				}		 			 
			}
		}
		
		return Relations;
	}
	
	
	
	public ArrayList<String> GetConcept_Instances()
	{
		ArrayList<String> Concept_Instances=new ArrayList<String>();
		ExtendedIterator<OntClass> classlist = ontology.listNamedClasses(); //�����ı��������������ʽ����
		OntClass temp ;
		while(classlist.hasNext()){	
			temp=classlist.next();
			String Concept_Members="";
			String name=temp.getLocalName().toString();
			//System.out.println(name);
			//name=name.replace("-", "_");//Ϊ�˻�ͼ���㣬��'-'�滻��'_'
			String members="";
			String instance="";
			if(name.equals("Thing")||name.equals("Nothing"))
				continue;
			ExtendedIterator<? extends OntResource> Instance=temp.listInstances(false);
			while (Instance.hasNext()) {
				OntResource member=Instance.next(); //���������ת��ֻ����OntResource
				instance=member.getLocalName();
			//	System.out.println(instance);
				members=members+","+instance;					
			}	
			//System.out.println(!(instance==null));
			
			if(!(instance==null)&&!instance.equals(""))
			{
				Concept_Members=name+"--"+members;	
			//	System.out.println(Concept_Members);
				Concept_Instances.add(Concept_Members);
			}
		}	
		return Concept_Instances;
	}
	
	public ArrayList<String> GetSubclass()
	{
		ArrayList<String> Subclasses=new ArrayList<String>();
		ExtendedIterator<OntClass> classlist = ontology.listNamedClasses();
		String sub_information=null;
		OntClass temp ;
		while(classlist.hasNext()){
			temp=classlist.next();
			ExtendedIterator<OntClass> classlist1 = temp.listSubClasses(false);//false��ʾ�г����е����࣬true��ʾֻ�г�ֱ������
			if(classlist1.hasNext()){
				String a=temp.getLocalName().toString();
				//a=a.replace("-", "_");//Ϊ�˻�ͼ���㣬��'-'�滻��'_'
				sub_information=a+"--";
				//Subclasses.add(a+"--");
				//sub_information=temp.getLocalName().toString()+"--";
				//Subclasses.add(temp.getLocalName().toString()+"--");
		//	bfw.write(temp.getLocalName().toString()+"--");//The structure of the data
			OntClass temp1;
			while(classlist1.hasNext()){
				temp1=classlist1.next();
				String b=temp1.getLocalName().toString();
				//b=b.replace("-", "_");//Ϊ�˻�ͼ���㣬��'-'�滻��'_'
				sub_information=sub_information+","+b;				
				//sub_information=sub_information+","+temp1.getLocalName().toString();				
				//bfw.write(","+temp1.getLocalName().toString());
			 }
			Subclasses.add(sub_information);
			//��д���ļ�ʱ����ý�"--,"�滻��"--"
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
			//����thing��ֱ��������г���
			ExtendedIterator<OntClass> classlist1 = temp.listSubClasses(true);//false��ʾ�г����е����࣬true��ʾֻ�г�ֱ������
			if(classlist1.hasNext()){
				String a=temp.getLocalName().toString();
				//a=a.replace("-", "_");//Ϊ�˻�ͼ���㣬��'-'�滻��'_'
				sub_information=a+"--";
				//sub_information=temp.getLocalName().toString()+"--";
			OntClass temp1;
			while(classlist1.hasNext()){
				temp1=classlist1.next();
				String b=temp1.getLocalName().toString();
				//b=b.replace("-", "_");//Ϊ�˻�ͼ���㣬��'-'�滻��'_'
				sub_information=sub_information+","+b;		
				//sub_information=sub_information+","+temp1.getLocalName().toString();				
			 }
			Subclasses.add(sub_information);//��д���ļ�֮ǰ����ý�"--,"�滻��"--"		
			}	
	/*		//����thing��ֱ����ص������г���
		//	System.out.println(temp.getLocalName().toString());
			ExtendedIterator<OntClass> classlist2 = temp.listSuperClasses(true);//false��ʾ�г����е����࣬true��ʾֻ�г�ֱ������
		//	System.out.println(classlist2.hasNext());
			if(!classlist2.hasNext())    //��һ������ǿ��ܸ���Ϊ�յ����
			{
				String a=temp.getLocalName().toString();
				//a=a.replace("-", "_");//Ϊ�˻�ͼ���㣬��'-'�滻��'_'
				sub_information="Thing"+"--"+a;
				//sub_information="Thing"+"--"+temp.getLocalName().toString();	
				Subclasses.add(sub_information)	;		
			 }
			while(classlist2.hasNext()) //�ڶ�������ǿ��ܸ���ΪResource�����
			{
				OntClass temp2;
				temp2=classlist2.next();
				if(temp2.isRestriction()||(temp2.getLocalName()==null)) //����������������
				{
					continue;
				}
			//	System.out.println(temp2.isRestriction());
			//	System.out.println(temp2.getLocalName().toString());
				//System.out.println("***********");
				String father=temp2.getLocalName().toString();
			//	father=father.replace("-", "_");//Ϊ�˻�ͼ���㣬��'-'�滻��'_'
				if(father.equals("Resource")||father.equals("Thing"))
				{	
					String a=temp.getLocalName().toString();
				   // a=a.replace("-", "_");//Ϊ�˻�ͼ���㣬��'-'�滻��'_'		
				    sub_information="Thing"+"--"+a;
					//sub_information="Thing"+"--"+temp.getLocalName().toString();	
					Subclasses.add(sub_information)	;	
					break;
				}
			}*/
			//��д���ļ�֮ǰ����ý�"--,"�滻��"--"		
		}
		return Subclasses;
	}
	
	public ArrayList<String> GetSuperclass()
	{
		ArrayList<String> Superclasses=new ArrayList<String>();
		ExtendedIterator<OntClass> classlist = ontology.listNamedClasses();
		String super_information=null;
		OntClass temp ;
		while(classlist.hasNext()){
			temp=classlist.next();
			String a=temp.getLocalName().toString();
			//a=a.replace("-", "_");//Ϊ�˻�ͼ���㣬��'-'�滻��'_'
			super_information=a+"--Thing";		//�κν��Ķ���Thing��Ϊ�����Ƚ��
			ExtendedIterator<OntClass> classlist1 = temp.listSuperClasses(false);//false��ʾ�г����еĸ��࣬true��ʾֻ�г�ֱ�Ӹ���
			if(!classlist1.hasNext())    //��һ������ǿ��ܸ���Ϊ�յ����
			{
				//continue;
			}
			while(classlist1.hasNext()) //�ڶ�������ǿ��ܸ���ΪResource�����
			{
				OntClass temp2;
				temp2=classlist1.next();
				if(temp2.isRestriction()||(temp2.getLocalName()==null)) //����������������
				{
					continue;
				}
				//�ų�������������father�Ͳ���һ�������࣬������������ķ�ʽ��ȡ����
				String father=temp2.getLocalName().toString();
				if(father.equals("Resource")||father.equals("Thing"))
				{
					continue; //����������Ŀ����Ҫ�������Ჹ���������
				}
				else
				{
					super_information=super_information+","+father;
				}
			}
			Superclasses.add(super_information)	;	
		}
		return Superclasses;
	}
	
	public ArrayList<String> GetSupclass_Direct()
	{
		ArrayList<String> Superclasses=new ArrayList<String>();
		ExtendedIterator<OntClass> classlist = ontology.listNamedClasses();
		String super_information=null;
		OntClass temp ;
		while(classlist.hasNext()){
			temp=classlist.next();
			String a=temp.getLocalName().toString();
			//a=a.replace("-", "_");//Ϊ�˻�ͼ���㣬��'-'�滻��'_'
			super_information=a+"--Thing";		//�κν��Ķ���Thing��Ϊ�����Ƚ��
			ExtendedIterator<OntClass> classlist1 = temp.listSuperClasses(true);//false��ʾ�г����еĸ��࣬true��ʾֻ�г�ֱ�Ӹ���
			if(!classlist1.hasNext())    //��һ������ǿ��ܸ���Ϊ�յ����
			{
				//continue;
			}
			while(classlist1.hasNext()) //�ڶ�������ǿ��ܸ���ΪResource�����
			{
				OntClass temp2;
				temp2=classlist1.next();
				if(temp2.isRestriction()||(temp2.getLocalName()==null)) //����������������
				{
					continue;
				}
				//�ų�������������father�Ͳ���һ�������࣬������������ķ�ʽ��ȡ����
				String father=temp2.getLocalName().toString();
				if(father.equals("Resource")||father.equals("Thing"))
				{
					continue; //����������Ŀ����Ҫ�������Ჹ���������
				}
				else
				{
					super_information=super_information+","+father;
				}
			}
			Superclasses.add(super_information)	;	
		}
		return Superclasses;
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
						sibling_information=children[j].toString()+"--";
						for(int k=0;k<children.length;k++)
						{
							if(k!=j)
							{
								sibling_information=sibling_information+","+children[k].toString();
							}
						}
						Sibling.add(sibling_information);
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
			ExtendedIterator<OntClass> disjointionlist = temp.listDisjointWith();//false��ʾ�г����е����࣬true��ʾֻ�г�ֱ������
			if(disjointionlist.hasNext()){
				String a=temp.getLocalName().toString();
				//a=a.replace("-", "_");//Ϊ�˻�ͼ���㣬��'-'�滻��'_'
				sub_information=a+"--";
				OntClass temp1;
				while(disjointionlist.hasNext())
				{
					temp1=disjointionlist.next();
					String b=temp1.getLocalName().toString();
					//b=b.replace("-", "_");//Ϊ�˻�ͼ���㣬��'-'�滻��'_'
					sub_information=sub_information+","+b;				
				}
			Disjointion.add(sub_information);
			//��д���ļ�ʱ����ý�"--,"�滻��"--"
			}		
		}
		return Disjointion;
	}
	
	public ArrayList<String> GetLeaves()
	{
		//��������Ҷ��������������ʽ���з���
		ArrayList<String> leaf=new ArrayList<String>();
		ExtendedIterator<OntClass> classlist = ontology.listNamedClasses();
		OntClass temp ;
		while(classlist.hasNext()){
			temp=classlist.next();	
			ExtendedIterator<OntClass> classlist1 = temp.listSubClasses();	
			if(classlist1.hasNext()==false )
			{
				String a=temp.getLocalName().toString();
				//a=a.replace("-","_");//Ϊ�˻�ͼ���㣬��'-'�滻��'_'		
				leaf.add(a);
				//leaf.add(temp.getLocalName().toString());
			}
		}
		return leaf;
	}
	
	public ArrayList<String> get_reference(String alignmentFile)
	{
	    ArrayList<String> references=new ArrayList<String>();  
	    Model model = ModelFactory.createDefaultModel();
		InputStream in = FileManager.get().open( alignmentFile );
        if (in == null) {
        	System.out.println("File: " + alignmentFile + " not found!");
            throw new IllegalArgumentException( "File: " + alignmentFile + " not found");
        }
        model.read(in,"");
		//model.read(in,null);
        //������ʽ1(���YAM++)
		/*Property alignmententity1, alignmententity2;
		alignmententity1 = model.getProperty("http://knowledgeweb.semanticweb.org/heterogeneity/alignment#entity1");
		alignmententity2 = model.getProperty("http://knowledgeweb.semanticweb.org/heterogeneity/alignment#entity2");
		Property value = model.getProperty("http://knowledgeweb.semanticweb.org/heterogeneity/alignment#measure");
		Property relation = model.getProperty("http://knowledgeweb.semanticweb.org/heterogeneity/alignment#relation");*/
		//OntClass temp;
        //���ͷ�ʽ2(���AML++,reference alignments)
	    Property alignmententity1 = model.getProperty("http://knowledgeweb.semanticweb.org/heterogeneity/alignmententity1");
		Property alignmententity2 = model.getProperty("http://knowledgeweb.semanticweb.org/heterogeneity/alignmententity2");//alignment
		Property value = model.getProperty("http://knowledgeweb.semanticweb.org/heterogeneity/alignmentmeasure");
		Property relation = model.getProperty("http://knowledgeweb.semanticweb.org/heterogeneity/alignmentrelation");

        ResIterator resstmt = model.listSubjectsWithProperty(alignmententity1);	//���߷�����һ����
	  
		//Answer an iterator [with no duplicates] over all the resources in this model that have property p
      //  ResIterator resstmt = model.listResourcesWithProperty(alignmententity1);
		//StmtIterator resstmt = model.listStatements();
	//	int id1,id2;
	    Resource temp;
	    //Resource temp,entity1,entity2;
	//	ResIterator resstmt = model.listSubjectsWithProperty(alignmententity1);
		//System.out.print(resstmt.hasNext());
		while(resstmt.hasNext()){
			temp = resstmt.next();//temp����Ԫ��
			String entity1 = temp.getPropertyResourceValue(alignmententity1).getLocalName();//��ȡ����1�ı���
			String entity2 = temp.getPropertyResourceValue(alignmententity2).getLocalName();//��ȡ����2�ı���
			String Confidence=temp.getProperty(value).getObject().asLiteral().getString();//��ȡ����ֵ
			String Relation=temp.getProperty(relation).getObject().toString();//��ȡƥ��Ĺ�ϵ
			
			//�Ƚϱ��ķ���
			/*String Confidence=temp.getProperty(value).getLiteral().toString();
			Confidence=Confidence.replace("^^xsd:float", "").replace("^^http://www.w3.org/2001/XMLSchema#float", "");
			System.out.println(Confidence);*/
			
			
		//	System.out.println(Relation);
		//	System.out.println(entity1+" "+entity2);
		//	System.out.println(entity1+" "+entity2+" "+Relation+" "+Confidence);
			
			//������е���Ԫ��
	/*		StmtIterator stmt = model.listStatements();
			while(stmt.hasNext()){
				System.out.println(stmt.next());
			}*/
			//entity1=entity1.replace("-", "_");//Ϊ�˻�ͼ���㣬��'-'�滻��'_'		
			//entity2=entity2.replace("-", "_");//Ϊ�˻�ͼ���㣬��'-'�滻��'_'		
			references.add(entity1+"--"+entity2);//ͳһת����Сд
		}
		return references;
	}
	
	public ArrayList<String> generate_reference(String alignmentFile)
	{
		
	    ArrayList<String> references=new ArrayList<String>();  
	    Model model = ModelFactory.createDefaultModel();
		InputStream in = FileManager.get().open( alignmentFile );
        if (in == null) {
        	System.out.println("File: " + alignmentFile + " not found!");
            throw new IllegalArgumentException( "File: " + alignmentFile + " not found");
        }
        model.read(in,"");
		//model.read(in,null);
        //������ʽ1(���YAM++)
		/*Property alignmententity1, alignmententity2;
		alignmententity1 = model.getProperty("http://knowledgeweb.semanticweb.org/heterogeneity/alignment#entity1");
		alignmententity2 = model.getProperty("http://knowledgeweb.semanticweb.org/heterogeneity/alignment#entity2");
		Property value = model.getProperty("http://knowledgeweb.semanticweb.org/heterogeneity/alignment#measure");
		Property relation = model.getProperty("http://knowledgeweb.semanticweb.org/heterogeneity/alignment#relation");*/
		//OntClass temp;
        //���ͷ�ʽ2(���AML++,reference alignments)
	    Property alignmententity1 = model.getProperty("http://knowledgeweb.semanticweb.org/heterogeneity/alignmententity1");
		Property alignmententity2 = model.getProperty("http://knowledgeweb.semanticweb.org/heterogeneity/alignmententity2");//alignment
		Property value = model.getProperty("http://knowledgeweb.semanticweb.org/heterogeneity/alignmentmeasure");
		Property relation = model.getProperty("http://knowledgeweb.semanticweb.org/heterogeneity/alignmentrelation");

        ResIterator resstmt = model.listSubjectsWithProperty(alignmententity1);	//���߷�����һ����
	  
		//Answer an iterator [with no duplicates] over all the resources in this model that have property p
      //  ResIterator resstmt = model.listResourcesWithProperty(alignmententity1);
		//StmtIterator resstmt = model.listStatements();
	//	int id1,id2;
	    Resource temp;
	    //Resource temp,entity1,entity2;
	//	ResIterator resstmt = model.listSubjectsWithProperty(alignmententity1);
		//System.out.print(resstmt.hasNext());
		while(resstmt.hasNext()){
			temp = resstmt.next();//temp����Ԫ��
			String entity1 = temp.getPropertyResourceValue(alignmententity1).getLocalName();//��ȡ����1�ı���
			String entity2 = temp.getPropertyResourceValue(alignmententity2).getLocalName();//��ȡ����2�ı���
			String Confidence=temp.getProperty(value).getObject().asLiteral().getString();//��ȡ����ֵ
			String Relation=temp.getProperty(relation).getObject().toString();//��ȡƥ��Ĺ�ϵ
			
			//�Ƚϱ��ķ���
			/*String Confidence=temp.getProperty(value).getLiteral().toString();
			Confidence=Confidence.replace("^^xsd:float", "").replace("^^http://www.w3.org/2001/XMLSchema#float", "");
			System.out.println(Confidence);*/
			
			
		//	System.out.println(Relation);
		//	System.out.println(entity1+" "+entity2);
		//	System.out.println(entity1+" "+entity2+" "+Relation+" "+Confidence);
			
			//������е���Ԫ��
	/*		StmtIterator stmt = model.listStatements();
			while(stmt.hasNext()){
				System.out.println(stmt.next());
			}*/
			//entity1=entity1.replace("-", "_");//Ϊ�˻�ͼ���㣬��'-'�滻��'_'		
			//entity2=entity2.replace("-", "_");//Ϊ�˻�ͼ���㣬��'-'�滻��'_'		
			references.add(entity1+"--"+entity2);//ͳһת����Сд
		}
		return references;
	}
	

	
	public ArrayList<String> keep_class(ArrayList<String> reference,ArrayList<String> concept1)
	{
		ArrayList<String> Class_Match=new ArrayList<String>();
		for(int i=0;i<reference.size();i++)
		{
			String[] Match_pairs = reference.get(i).split("--");//ֻҪȡǰ��һ���ּ�����1�еĸ���Աȼ���
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
				//��ʱ��������ֶ�һ��"_"����ƥ������
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
		//��reference�а������Ե�ƥ��Խ��й���
		return Class_Match;
	}
	
	public ArrayList<String> new_keep_class(ArrayList<String> reference,ArrayList<String> classes1,ArrayList<String> classes2)
	{
		ArrayList<String> Class_Match=new ArrayList<String>();
		for(int i=0;i<reference.size();i++)
		{
			String[] Match_pairs = reference.get(i).split("--");//ֻҪȡǰ��һ���ּ�����1�еĸ���Աȼ���
			//System.out.println(Match_pairs[0]);
			String concept1=Match_pairs[0].replace("_", "");//��ʱ��������ֶ�һ��"_"����ƥ������
			String concept2=Match_pairs[1].replace("_", "");			
			if(classes1.contains(concept1)&&classes2.contains(concept2))
				Class_Match.add(reference.get(i));
			else if(classes1.contains(Match_pairs[0])&&classes2.contains(concept2))
				Class_Match.add(reference.get(i));
			else if(classes1.contains(concept1)&&classes2.contains(Match_pairs[1]))
				Class_Match.add(reference.get(i));
			else if(classes1.contains(Match_pairs[0])&&classes2.contains(Match_pairs[1]))
				Class_Match.add(reference.get(i));
			
		}		
		//��reference�а������Ե�ƥ��Խ��й���
		return Class_Match;
	}
	
	public ArrayList<String> keep_property(ArrayList<String> reference,ArrayList<String> property)
	{
		ArrayList<String> Property_Match=new ArrayList<String>();
		for(int i=0;i<reference.size();i++)
		{
			String[] Match_pairs = reference.get(i).split("--");//ֻҪȡǰ��һ���ּ�����1�еĸ���Աȼ���
			//System.out.println(Match_pairs[0]);
			for(int j=0;j<property.size();j++)
			{
				//Session_Chair
				//SessionChair
			//	System.out.println(concept1.get(j));
				if(Match_pairs[0].equals(property.get(j)))
				{
					Property_Match.add(reference.get(i));
					break;
				}
				//��ʱ��������ֶ�һ��"_"����ƥ������
				else if(Match_pairs[0].replace("_", "").equals(property.get(j)))
				{
					Property_Match.add(reference.get(i));
					break;
				}
				else if(Match_pairs[0].equals(property.get(j).replace("_", "")))
				{
					Property_Match.add(reference.get(i));
					break;
				}
			}
		}		
		//��reference�а������Ե�ƥ��Խ��й���
		return Property_Match;
	}
	
	public ArrayList<String> filter_class(ArrayList<String> reference,ArrayList<String> class_match)
	{
		ArrayList<String> Property_Match=new ArrayList<String>();
		boolean label[]=new boolean[reference.size()];
		for(int i=0;i<reference.size();i++)
		{
			//String[] Match_pairs = reference.get(i).split("--");//ֻҪȡǰ��һ���ּ�����1�еĸ���Աȼ���
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
		//ƥ�������Щ���Ǹ����ƥ��ԣ�Ĭ�Ͻ��俴�����Ե�ƥ��ԡ�
		for(int k=0;k<reference.size();k++)
		{
			if(label[k]!=true)
			{
				Property_Match.add(reference.get(k));
			}
		}
		//��reference�а������Ե�ƥ��Խ��й���
		return Property_Match;
	}
	
	
	
	
    //��ȡֻ���������ƥ��
	public ArrayList<String> Get_haschild_match(ArrayList<String> Match,ArrayList<String> Leaf1,ArrayList<String> Leaf2) //��ȡ˫�����к��ӵ�ƥ���
	{
		ArrayList<String> constrained_match=new ArrayList<String>();
        boolean flag1=false,flag2=false;
		for(int i=0;i<Match.size();i++)
		{
			
			//��ƥ��Խ����з��ж�
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
				//��ƥ��Եĵ�һ�����Ƿ���ڱ���1��Ҷ�ӽڵ�
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
				//��ƥ��Եĵڶ������Ƿ���ڱ���2��Ҷ�ӽڵ�
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
	
	
	public ArrayList<String> FindConcept_superclasses(String concept )//�ҵ�ĳһ���������������
	{
		ArrayList<String> superclasses=new ArrayList<String>();
		ExtendedIterator<OntClass> classlist = ontology.listNamedClasses(); //�����ı��������������ʽ����
		OntClass temp = null; 
		while(classlist.hasNext())
		{
			temp=classlist.next();	
		    //��ӡ������һ�ַ�ʽ
/*		    String b=temp.getModel().getGraph().getPrefixMapping().shortForm(temp.getURI());
		    b=b.replace(":", "");*/
			if(temp.getLocalName().equals(concept))
			{
				System.out.println(concept);
				ExtendedIterator<OntClass> superclass=temp.listSuperClasses(false);
				while(superclass.hasNext())
				{
					OntClass Restriction=superclass.next();		
					////���ӹ�ϵʽ����Ҫ�ˣ�ֻ��Ҫ��ȡ�����������
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
							String constraint=relation+","+restriction+","+type;
							System.out.println(constraint);
							superclasses.add(constraint);
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
								else
								{
									
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
								else
								{
									
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
								else
								{
									
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
				}
			}			
		}		
		//temp.getSuperClass();


		return superclasses;
	}
	
	public ArrayList<String> GetAllRestrictions()//�ҵ�ĳ���������и������������
	{
		ArrayList<String> Restrictions=new ArrayList<String>();
		ExtendedIterator<OntClass> classlist = ontology.listNamedClasses(); //�����ı��������������ʽ����
	    OntClass temp = null; 
		while(classlist.hasNext())
		{
			temp=classlist.next();	
			String concept=temp.getLocalName();
		    //��ӡ������һ�ַ�ʽ
			System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
			System.out.println(concept);
			ExtendedIterator<OntClass> superclass=temp.listSuperClasses(false);
			while(superclass.hasNext())
			{
				OntClass Restriction=superclass.next();		
				////���ӹ�ϵʽ����Ҫ�ˣ�ֻ��Ҫ��ȡ�����������
				if(Restriction.isRestriction())
				{							
					System.out.println("****************************");
					// System.out.println(Restriction.isRestriction());
					// System.out.println(Restriction);
					Restriction res = Restriction.asRestriction();
					// System.out.println(res.isMinCardinalityRestriction());
					String relation = "";
					String restriction = "";
					String type = "";
					if (res.isAllValuesFromRestriction()) 
					{
						System.out.println("Only!");
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
								System.out.println(constraint);
								Restrictions.add(constraint);
							}
						} 
						else 
						{
							String constraint = concept+","+relation + "," + restriction
									+ "," + type;
							System.out.println(constraint);
							Restrictions.add(constraint);
						}
					} 
					else if (res.isSomeValuesFromRestriction()) 
					{
						System.out.println("some!");
						SomeValuesFromRestriction res_some = res
								.asSomeValuesFromRestriction();
						relation = res_some.getOnProperty().getLocalName();
						// System.out.println(res_some.getOnProperty().
						// getLocalName());
						OntClass a = (OntClass) res_some.getSomeValuesFrom();
						restriction = "some";
						type = a.getLocalName();
						String constraint = concept+","+relation + "," + restriction + ","
								+ type;
						System.out.println(constraint);
						Restrictions.add(constraint);
					} 
					else if (res.isHasValueRestriction()) 
					{
						System.out.println("hasvalue!!");
						HasValueRestriction res_hasvalue = res
								.asHasValueRestriction();
						relation = res_hasvalue.getOnProperty().getLocalName();
						// System.out.println(res_hasvalue.getOnProperty().
						// getLocalName());
						restriction = "value";
						type = res_hasvalue.getHasValue().toString();
						String constraint = concept+","+relation + "," + restriction + ","
								+ type;
						System.out.println(constraint);
						Restrictions.add(constraint);
					} 
					else if (res.isCardinalityRestriction()) 
					{
						System.out.println("exactly!");
						CardinalityRestriction res_exact = res
								.asCardinalityRestriction();
						relation = res_exact.getOnProperty().getLocalName();
						restriction = "exactly," + res_exact.getCardinality();
						NodeIterator value = res_exact.listPropertyValues(null);
						while (value.hasNext()) {
							RDFNode a = value.next();
							// System.out.println();
							if (a.isLiteral()) {
								type = "Literal";
								break;
							} else {

							}
						}
						String constraint = concept+","+relation + "," + restriction + ","
								+ type;
						System.out.println(constraint);
						Restrictions.add(constraint);
					} 
					else if (res.isMaxCardinalityRestriction()) 
					{
						System.out.println("Max!");
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
							} else {

							}
						}
						String constraint = concept+","+relation + "," + restriction + ","
								+ type;
						System.out.println(constraint);
						Restrictions.add(constraint);
					} 
					else if (res.isMinCardinalityRestriction())
					{
						System.out.println("Min!");
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
							} else {

							}
						}
						String constraint = concept+","+relation + "," + restriction + ","
								+ type;
						System.out.println(constraint);
						Restrictions.add(constraint);
					} 
					else 
					{
						System.out.println("None restriction!");
					}					
				}
				}
			}			
				
		//temp.getSuperClass();
		return Restrictions;
	}
	
	public int GetConcept_Path(String concept)
	{	
		ExtendedIterator<OntClass> classlist = ontology.listNamedClasses();
		String a="";
		String b="";
		/*OntClass temp0=null;
		while(classlist.hasNext()){			//ǰ�����ѭ����Ҫ��Ϊ�˶�λ������
			temp0=classlist.next();
			a=temp0.getLocalName().toString();	
			if(a.equals(concept))
			{
				//System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
				System.out.println(a);
				break;
			}
		}*/
		//System.out.println("����Ϊ:"+concept);
		OntClass temp=ontology.getOntClass(OntoID+concept);
		if(temp==null) //�п����滻��ʱ�򱨴���
			 temp=ontology.getOntClass(OntoID+concept.replace("_", "-"));
		//System.out.println(OntoID+concept);
		
		int depth=0;
		boolean flag=false;
			ExtendedIterator<OntClass> classlist1 = temp.listSuperClasses(false);//false��ʾ�г����е����࣬true��ʾֻ�г�ֱ������
			if(classlist1.hasNext()){				
				OntClass temp1;
				while(classlist1.hasNext())
				{
					temp1=classlist1.next();
					if(temp1.isRestriction()||(temp1.getLocalName()==null))
					{
						continue;
					}
					else if(temp1.getLocalName().toString().equals("Thing")||temp1.getLocalName().toString().equals("Resource"))//�����丸����Resource����Thing������
					{
						continue;
					}
					else 
					{
						b=temp1.getLocalName().toString();
					//	System.out.print(b);	
						depth++;
					}								
				}
			}
			//System.out.println();
			depth=depth+1;
		return depth;
	}
}
