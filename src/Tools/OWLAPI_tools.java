package Tools;
import java.io.File;
import java.util.ArrayList;
import java.util.Set;


import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.ConsoleProgressMonitor;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerConfiguration;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.SimpleConfiguration;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.reasoner.Reasoner;

public class OWLAPI_tools {	
		 OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		 OWLOntology onto;
		 String OntoID;
		 //String IndividualID="";
		 //String IndividualID="http://xmlns.com/foaf/0.1";
		 
		 public void readOnto(String path) 
		 {
			 try {
				 File file = new File(path);			
			     onto = manager.loadOntologyFromOntologyDocument(file);
			     OWLOntologyID ontologyIRI = onto.getOntologyID();
			     OntoID = ontologyIRI.getOntologyIRI().toString();
			     System.out.println("Load ontology sucessfully!");
			     IRI documentIRI = manager.getOntologyDocumentIRI(onto);
			     System.out.println("The path comes from " + documentIRI);// 获取文件的相对路径
			     System.out.println("The OntoID is " + OntoID);
				} catch (OWLOntologyCreationException e) 
				{
					// TODO Auto-generated catch block
					System.out.println("cuowu");
					e.printStackTrace();
				} 		
		 }
    
	    public void Isconsistent()  //嵌入推理机来判断是否一致
	    {
	    	 OWLReasonerFactory reasonerFactory = new StructuralReasonerFactory();
		     ConsoleProgressMonitor progressMonitor = new ConsoleProgressMonitor();
		     OWLReasonerConfiguration config = new SimpleConfiguration(progressMonitor);
		     OWLReasoner reasoner = reasonerFactory.createReasoner(onto, config);
		     reasoner.precomputeInferences();
			 boolean consistent = reasoner.isConsistent();
			 System.out.println("Consistent: " + consistent);
			 System.out.println("\n");
	    }
	    
	    public ArrayList<String> GetAllConcept()
		{
	    	ArrayList<String> classes=new ArrayList<String>();
	        for (OWLClass c : onto.getClassesInSignature()) 
	        {
	        //	System.out.println(c.getIRI().getFragment());
	        	String concept=c.getIRI().getFragment();
	        	if(!concept.equals("Thing"))//Thing的情况就不考虑了
	        		classes.add(concept);
	        }
	        return classes;
		}
	    
	    public ArrayList<String> GetConcept_Children(String concept)
	    {
	    		ArrayList<String> Concept_Instances=new ArrayList<String>();
	    		OWLReasonerFactory reasonerFactory = new StructuralReasonerFactory();
			    ConsoleProgressMonitor progressMonitor = new ConsoleProgressMonitor();
			    OWLReasonerConfiguration config = new SimpleConfiguration(progressMonitor);
			    OWLReasoner reasoner = reasonerFactory.createReasoner(onto, config);    
			    		    
			    OWLDataFactory fac = manager.getOWLDataFactory();
			    String concept_url= OntoID+"#"+concept;
			    System.out.println( concept_url);	   
			    OWLClass concept_name = fac.getOWLClass(IRI.create(concept_url));
			    NodeSet<OWLClass> Children = reasoner.getSubClasses(concept_name, false);//false是使用推理机的情况
			    Set<OWLClass> children = Children.getFlattened();
			    for (OWLClass c : children) 
		        {
			    	String child=c.getIRI().getFragment();
		        	if(!concept.equals("Thing"))//Thing的情况就不考
		        		System.out.println(child);
		        }			 			    
			    return Concept_Instances;    	
	    }
	    
	    public ArrayList<String> GetObjectProperty()
	  	{
	    	ArrayList<String> Properties=new ArrayList<String>();
	        for (OWLObjectProperty op : onto.getObjectPropertiesInSignature()) 
            {
	       // 	System.out.println(op.getIRI().getFragment());
	        	Properties.add(op.getIRI().getFragment());
            }
	        return Properties;
	  	}
	    
	    public ArrayList<String> GetConceptInstance()	    
	    {
	    	ArrayList<String> Concept_Instances=new ArrayList<String>();
	        OWLReasonerFactory reasonerFactory = new StructuralReasonerFactory();
		    ConsoleProgressMonitor progressMonitor = new ConsoleProgressMonitor();
		    OWLReasonerConfiguration config = new SimpleConfiguration(progressMonitor);
		    OWLReasoner reasoner = reasonerFactory.createReasoner(onto, config);
		    String information="";
	    	for (OWLClass c : onto.getClassesInSignature()) 
	        {
	    		
	        	//System.out.println(c.getIRI().getFragment());//可以直接将不带URL的概念名进行输出 
	        	NodeSet<OWLNamedIndividual> individualsNodeSet_c = reasoner.getInstances(c,false );//false是使用推理机的情况
	        	Set<OWLNamedIndividual> individuals_c = individualsNodeSet_c.getFlattened(); 
	        	String concept=c.getIRI().getFragment().replace("-", "_");//考虑下划线问题
	        	if(!concept.equals("Thing")&&individuals_c.size()>0)
	        	{
	        	 information=concept+"--";
	        	// System.out.println("****************************************");	      
	        	// System.out.println("the number of instances of "+c.getIRI().getFragment()+ " is "+individuals_c.size());
	        	 for(OWLNamedIndividual i:individuals_c)
		         {   
		           String individual="";
		           if(i.getIRI().getNamespace().contains(OntoID))
		           {
		        	 individual=i.getIRI().toString().replace(OntoID+"#", "").replace("-", "_");
		           }
		           else
		           {
		        	  individual=i.getIRI().getFragment().replace("-", "_");
		           }
		           //System.out.println(individual + "\t is an instance of\t" + concept);
		           information=information+","+individual;		        		
		         }
		        //	System.out.println(information);
		        	Concept_Instances.add(information.replace("--,", "--"));
		        	information="";
	        	 }
	        	 
	        }   
	    	return Concept_Instances;    	
	    }
	    
	    public ArrayList<String> GetConceptInstance(String concept)
	    {
	    	ArrayList<String> Instances=new ArrayList<String>();
	        		    			
	        OWLReasonerFactory reasonerFactory = new StructuralReasonerFactory();
		    ConsoleProgressMonitor progressMonitor = new ConsoleProgressMonitor();
		    OWLReasonerConfiguration config = new SimpleConfiguration(progressMonitor);
		    OWLReasoner reasoner = reasonerFactory.createReasoner(onto, config);    
		    
		    OWLDataFactory fac = manager.getOWLDataFactory();
		    String concept_url= OntoID+"#"+concept;
		    System.out.println( concept_url);	   
		    OWLClass concept_name = fac.getOWLClass(IRI.create(concept_url));
		    NodeSet<OWLNamedIndividual> individualsNodeSet = reasoner.getInstances(concept_name, true);//false是使用推理机的情况		   
		    Set<OWLNamedIndividual> individuals = individualsNodeSet.getFlattened();
		    System.out.println(concept+"实例的个数为:"+individuals.size());
		    
		    String information=concept.replace("-", "_")+"--";
		    for(OWLNamedIndividual i:individuals)
        	{       
		    	
		    	//String instance=i.getIRI().getFragment().replace("-", "_");//考虑下划线问题;
		    	//IRI instance=i.getIRI();//考虑下划线问题;
		    	//String IndividualID=find_Instances_url();
		    	String instance="";
		    	if(i.getIRI().getNamespace().contains(OntoID))
		    	{
		    		instance=i.getIRI().toString().replace(OntoID+"#", "");
		    		System.out.println(instance+ "\tinstance of\t" + concept.replace("-", "_"));	//考虑下划线问题;   
		    	}
		    	else
		    	{
		    		instance=i.getIRI().getFragment();
		    		System.out.println(instance+ "\tinstance of\t" + concept.replace("-", "_"));	//考虑下划线问题;   	    		
		    	}
		    	information=information+","+instance.replace("-", "_");
	    		
        	}
		    Instances.add(information.replace("--,", "--"));
		    return  Instances;		        
	    }
	    
	    public void Is_concept_Instance(String concept,String instance)//判断保证原始的"-"不变
	    {
	        OWLReasonerFactory reasonerFactory = new StructuralReasonerFactory();
		    ConsoleProgressMonitor progressMonitor = new ConsoleProgressMonitor();
		    OWLReasonerConfiguration config = new SimpleConfiguration(progressMonitor);
		    OWLReasoner reasoner = reasonerFactory.createReasoner(onto, config);    
		    
		    OWLDataFactory fac = manager.getOWLDataFactory();
		    String concept_url= OntoID+"#"+concept;
		    OWLClass concept_name = fac.getOWLClass(IRI.create(concept_url));
		    NodeSet<OWLNamedIndividual> individualsNodeSet = reasoner.getInstances(concept_name, true);//false是使用推理机的情况		   
		    Set<OWLNamedIndividual> individuals = individualsNodeSet.getFlattened();
		    //System.out.println(concept+"实例的个数为:"+individuals.size());
		    boolean flag=false;
		    for(OWLNamedIndividual i:individuals)
        	{       
		    	if(i.getIRI().getFragment().equals(instance)||i.getIRI().toString().replace(OntoID+"#", "").equals(instance))
		    	{
		    	  flag =true;
		    	  break;
		    	}     		
        	}	  
		    System.out.println(flag);	 	        
	    }
	    
	  
	    
	    public ArrayList<String> GetRelationInstance()
	    {
	    	 ArrayList<String> Relations=new ArrayList<String>();
	    	 
	    	 OWLReasonerFactory reasonerFactory = new StructuralReasonerFactory();
			 ConsoleProgressMonitor progressMonitor = new ConsoleProgressMonitor();
			 OWLReasonerConfiguration config = new SimpleConfiguration(progressMonitor);
			 OWLReasoner reasoner = reasonerFactory.createReasoner(onto, config);  
			 
	    	 for (OWLClass c : onto.getClassesInSignature()) 
		     {
		        NodeSet<OWLNamedIndividual> instances = reasoner.getInstances(c, false);
		        for (OWLNamedIndividual i : instances.getFlattened())
		        {
		        	String instance=i.getIRI().getFragment();
		            for (OWLObjectProperty op : onto.getObjectPropertiesInSignature()) 
		            {
		              String objectpropety=op.getIRI().getFragment();
		              NodeSet<OWLNamedIndividual> petValuesNodeSet = reasoner.getObjectPropertyValues(i, op);
		              for (OWLNamedIndividual value : petValuesNodeSet.getFlattened()) 
		              {
		            	 String instance_value= value.getIRI().getFragment();
		                 System.out.println(instance + "\t" + objectpropety + "\t"+ instance_value);
		                 Relations.add(instance.replace("-", "_") + "," + objectpropety.replace("-", "_")+ ","+ instance_value.replace("-", "_"));
		              }
		            }
		        }
		     }
	    	 return Relations;
	    }
	    
	    public  ArrayList<String> GetRelationInstance(String individual,String property)
	    {
	    	 ArrayList<String> Relations=new ArrayList<String>();
	    	 OWLReasonerFactory reasonerFactory = new StructuralReasonerFactory();
			 ConsoleProgressMonitor progressMonitor = new ConsoleProgressMonitor();
			 OWLReasonerConfiguration config = new SimpleConfiguration(progressMonitor);
			 OWLReasoner reasoner = reasonerFactory.createReasoner(onto, config);  
			 
			 OWLDataFactory fac = manager.getOWLDataFactory();
			 
			 String individual_url=find_Instances_url()+individual;//url不一定是本体的，可能是"http://xmlns.com/foaf/0.1#"
			 String property_url=OntoID+"#"+property;
			 
	    	 OWLNamedIndividual Individual = fac.getOWLNamedIndividual(IRI.create(individual_url));
		     OWLObjectProperty Property = fac.getOWLObjectProperty(IRI.create(property_url));
		       
		     NodeSet<OWLNamedIndividual> petValuesNodeSet = reasoner.getObjectPropertyValues(Individual, Property);
		     Set<OWLNamedIndividual> values = petValuesNodeSet.getFlattened();
		     for (OWLNamedIndividual ind : values) 
		     {
		    	String value=ind.getIRI().getFragment();
		       // System.out.println(individual+","+property+","+value);
		        Relations.add(individual+","+property+","+value);
		     }	
		     return Relations;		     
	    }
	    
	    public String find_Instances_url()
	    {
	    	String IndividualID=OntoID;
	    	OWLReasonerFactory reasonerFactory = new StructuralReasonerFactory();
		    ConsoleProgressMonitor progressMonitor = new ConsoleProgressMonitor();
		    OWLReasonerConfiguration config = new SimpleConfiguration(progressMonitor);
		    OWLReasoner reasoner = reasonerFactory.createReasoner(onto, config);
		    
	    	for (OWLClass c : onto.getClassesInSignature()) 
	        {
	        	NodeSet<OWLNamedIndividual> individualsNodeSet_c = reasoner.getInstances(c,true);//false是使用推理机的情况
	        	Set<OWLNamedIndividual> individuals_c = individualsNodeSet_c.getFlattened();
	        	if(individuals_c.size()>0)
	        	{
	        		for(OWLNamedIndividual i:individuals_c)
	        	   {   
	        		  String URL=i.getIRI().getNamespace();//考虑下划线问题;
	        		  if(!URL.contains(OntoID))
	        		  {
	        			  IndividualID=URL;
	        			  //System.out.println(IndividualID);
	        		  	  return IndividualID;
	        		  }
	        	   }	   
	        	}        		        	
	        }
	    	return IndividualID;
	    }
	    
}


