package Tools;

import java.io.IOException;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class PosTagger {
	MaxentTagger tagger;
	
	public PosTagger() throws IOException{
		try {
			tagger = new MaxentTagger("models/bidirectional-distsim-wsj-0-18.tagger");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String findPOS(String token) 
	{	  
		String POS=tagger.tagString(token).replace(token+"/", "");//∑µªÿ «written/VBN 
		return POS.trim();
	}

}
