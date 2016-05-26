package com.tencent.SecondRW;

import java.util.ArrayList;

public class IGRDenominator {
	private static double log2 = Math.log(2);
	private static double SMALL = 1e-6;
	private int CorSNum=0;
	private ArrayList<String> sequence = new ArrayList<String>();
	private String [] ValuePair = null;
	
	public IGRDenominator(StringBuffer ValueAll, int totalnum) {
		
		this.ValuePair = ValueAll.toString().split("\\s+");
		this.CorSNum = totalnum;
	}
	
	public double IGRFV() {
		double finalvalue = 0;
		for (int i=0;i<this.ValuePair.length;i++) {
			String [] tempValue = this.ValuePair[i].trim().split(",");
			if(tempValue.length<4)
				continue;			
			//double pos = Double.parseDouble(tempValue[1]);
			//double neg = Double.parseDouble(tempValue[2]);
			double posneg = Double.parseDouble(tempValue[3]);
			//finalvalue += posneg/this.CorSNum*(pos/posneg*Math.log((pos+SMALL)/
			//		(posneg+SMALL))/log2 + neg/posneg*Math.log((neg+SMALL)/(posneg+SMALL))/log2);
			finalvalue += -1*(posneg/this.CorSNum * Math.log((posneg+SMALL)/this.CorSNum)/log2);
		}		
		return finalvalue;
	}

}
