package com.tencent.ThirdMR;

import java.util.ArrayList;

import org.apache.hadoop.io.Text;

import com.tencent.Header.Header;
import com.tencent.SplitCriteria.GiniIndex;
import com.tencent.SplitCriteria.InfoGainRatio;
import com.tencent.SplitCriteria.MutualInformation;
import com.tencent.SplitCriteria.SymmetryUncertainty;

/**
 *@Package: com.tencent.Sort
 *@Title: InforCompute.java
 *@author: wenshifeng
 *@date: 2014-8-14ÏÂÎç2:36:26
 */
public class InforCompute {
	
	private Text tuples = new Text();
	private int InputCriteria= 0;
	
	public InforCompute(Text tuples,int InputCriteria) {		
		
		this.tuples.set(tuples);
		this.InputCriteria = InputCriteria;
	}
	
	public String RankingComp(String CurrentClass,ArrayList<Double> LabelInfor) {
		
		if (InputCriteria == Header.all ) {
			
      	    InfoGainRatio IG= new InfoGainRatio(tuples,LabelInfor);
    	    GiniIndex gini= new GiniIndex(tuples,LabelInfor);
    	    MutualInformation Mutual =new MutualInformation(tuples,LabelInfor);
    	    SymmetryUncertainty SU =new SymmetryUncertainty(tuples,LabelInfor);
    	    if( !CurrentClass.endsWith(Header.N) ) {
	    	    return Double.toString(IG.ComputeIGR()) + "," + Double.toString(gini.ComputeGini())+
	    	    		 "," + Double.toString(Mutual.ComputeMutual()) + ","  + Double.toString(SU.ComputeSU())
	    	    		+ "\t" + IG.ImpCliCTR.toString();	    	    	
    	    }else {
	    	    return Double.toString(IG.OptimalIGR()) + "," + Double.toString(gini.OptimalGini())+
	    	    		 "," + Double.toString(Mutual.OptimalMutual()) + "," + Double.toString(SU.OptimalSU())
	    	    		 + "\t" + IG.ImpCliCTR.toString() + "\t" + gini.ImpCliCTR.toString() + "\t" + 
	    	    		 Mutual.ImpCliCTR.toString() + "\t" + SU.ImpCliCTR.toString();
    	    	
    	    }
	   }    
			return "if you see this string, something must be wrong in somewhere!";
		
	}

}
