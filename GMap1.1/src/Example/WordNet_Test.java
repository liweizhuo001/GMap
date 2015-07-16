package Example;

import JWS.JWS;
import JWS.JiangAndConrath;
import JWS.Lin;
import Tools.Sim_Tools;


public class WordNet_Test{

    /*private String str1;
    private String str2;*/
   // private String dir = "E:/Commonly Application/WordNet/";
    private String dir = "WordNet";
    private JWS    ws = new JWS(dir, "2.1");
    public WordNet_Test(){}
    
/*    public WordNet_similarity(String str1,String str2){
        this.str1=str1;
        this.str2=str2;
    }*/
    
    public double getSimilarity(String str1,String str2){
        String[] strs1 = splitString(str1);
        String[] strs2 = splitString(str2);
        double sum = 0.0;
        for(String s1 : strs1){
            for(String s2: strs2){
                double sc= maxScoreOfLin(s1,s2);
               // double sc= maxScoreOfJcn(s1,s2);
                sum+= sc;
                System.out.println("基于WordNet， "+s1+" VS "+s2+" 的相似度为:"+sc);
            }
        }
        double Similarity = sum /(strs1.length * strs2.length);
        sum=0;
        return Similarity;
    }
    
    private String[] splitString(String str){
        String[] ret = str.split(" ");
        return ret;
    }
    
    private double maxScoreOfLin(String str1,String str2){
        Lin lin = ws.getLin();
        double sc = lin.max(str1, str2, "n");
        if(sc==0){
            sc = lin.max(str1, str2, "v");
        }
        return sc;
    }
    
 /*   private double maxScoreOfJcn(String str1,String str2){
        JiangAndConrath jcn = ws.getJiangAndConrath();
        double sc = jcn.max(str1, str2, "n");
        if(sc==0){
            sc = jcn.max(str1, str2, "v");
        }
        return sc;
    }*/
    
    public static void main(String args[]){
        String s1="part";
        String s2="portion";
       Sim_Tools tools=new Sim_Tools();
       WordNet_Test sm= new WordNet_Test();
       s1=tools.findStem(s1);
       s2=tools.findStem(s2);
        System.out.println(s1);
        System.out.println(s2);
        System.out.println(sm.getSimilarity(s1,s2));
        System.out.println(tools.ISUB(s1, s2));
        int ld = tools.similarityOfDistance(s1,s2);		
		float sim=1 - (float) ld / Math.max(s1.length(), s2.length());
		System.out.println(sim);
/*        s1="a b";
        s2="b c";
        System.out.println(tools.tokeningWord(s1));
        System.out.println(tools.tokeningWord(s2));
        String s3="First_Portion_of_the_Duodenum";
        s3=tools.tokeningWord(s3);
        System.out.println(s3);
        s3=tools.fliter(s3);
        System.out.println(s3);*/
      /*  Sim_Tools tools=new Sim_Tools();
        s1=tools.findStem(s1);
        s2=tools.findStem(s2);
        String str1=s1.toLowerCase().replace("_", "").replace("-", "").replace(" ", "");
        String str2=s2.toLowerCase().replace("_", "").replace("-", "").replace(" ", "");
        float ld=tools.similarityOfDistance(str1, str2);
       
        float sim=1 - (float) ld / Math.max(str1.length(), str2.length());
        System.out.println(sim);*/
        
    }
}
