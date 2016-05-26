package com.tencent.SplitCriteria;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.hadoop.io.Text;

/**
 *@Package: com.tencent.SplitCriteria
 *@Title: InforGain.java
 *@author: wenshifeng
 *@date: 2014-8-1����3:42:40
 */
public class InfoGainRatio  {
	
	private static double log2 = Math.log(2);
	private static double SMALL = 1e-6;
	private ArrayList<Double> sequence = new ArrayList<Double>();
	private ArrayList<String> AttValue = new ArrayList<String>();
	private double entropy;
	public StringBuffer ImpCliCTR = new StringBuffer();
	private double ActualTotal =0;	
	
	public InfoGainRatio(Text sequence,ArrayList<Double> LabelInfor) {			
		String [] TempValue=sequence.toString().split(",");	
		for(int i=0; i< TempValue.length-1; i++) {		
			
			if(i%4==0 ) 	{
				this.AttValue.add(TempValue[i]);
			}else {
				this.sequence.add(Double.parseDouble(TempValue[i]));
			}
		}
		this.sequence.add(Double.parseDouble(TempValue[TempValue.length-1]));	
		
		double pos = 0, neg = 0;
		double posneg = this.sequence.get(this.sequence.size()-1);
		for(int i=0; i< (this.sequence.size()-1)/3; i++) {			
			pos += this.sequence.get(i*3);
			neg += this.sequence.get(i*3+1);
		}	
				
		this.ActualTotal = LabelInfor.get(2);
		this.entropy =-1* ((pos/posneg)*Math.log((pos+SMALL)/(posneg+SMALL))/log2 + (neg/posneg)*Math.log((neg+SMALL)/(posneg+SMALL))/log2);
		
	}
	
	public double ComputeIGR() {
		
		double p1=0,p2=0,sum=0;
		double x1=0,x2=0,x3=0;
		double IGRIV = 0;
		double all= this.sequence.get(this.sequence.size()-1);
		for(int i=0; i< (this.sequence.size()-1)/3; i++) {
			
			x1= this.sequence.get(i*3);
			x2= this.sequence.get(i*3+1);
			x3= this.sequence.get(i*3+2);	
			
			p1= (x1/x3)*Math.log((x1+SMALL)/(x3+SMALL))/log2;
			p2= (x2/x3)*Math.log((x2+SMALL)/(x3+SMALL))/log2;
			
			sum+= -1*x3/all*(p1+p2);			
			IGRIV += -1*((x3+SMALL)/(all+SMALL))*Math.log((x3+SMALL)/(all+SMALL))/log2;
			
		    ImpCliCTR.append(this.AttValue.get(i) + ",");
		    ImpCliCTR.append(Integer.toString((int)x3) + ",");
		    ImpCliCTR.append(Integer.toString((int)x1) + ",");
		    ImpCliCTR.append(Double.toString(x1/x3) + ",");
		    
		}
		ImpCliCTR.deleteCharAt(ImpCliCTR.length()-1);
		this.sequence.clear();
		this.AttValue.clear();
		
		return (this.entropy-sum)/(IGRIV + SMALL);
		
	}	
	
	
	public double OptimalIGR() {
		
		double p1= 0,p2= 0;
		double Limpression=0, Lclick=0, Rimpression=0, Rclick=0;
		int index = 0;
		double IGL, IGR, IGRatio, maxIGRatio= 0;
		double IGRIV = 0;
		double all= this.sequence.get(this.sequence.size()-1);
		if(this.sequence.size()==4) {	
			
			double neg = this.sequence.get(1);
			Lclick = this.sequence.get(0);
			Limpression = this.sequence.get(2);
			p1 = (Lclick/Limpression)*Math.log((Lclick+SMALL)/(Limpression+SMALL))/log2;
			p2 = (neg/Limpression)*Math.log((neg+SMALL)/(Limpression+SMALL))/log2;
			IGL=-1*Limpression/all*(p1+p2);
			//maxIGRatio=all/this.ActualTotal*(this.entropy-IGL)/(this.entropy+SMALL);
			//maxIGRatio=(this.entropy-IGL)/(this.IGRDenominator+SMALL);
			IGRIV = -1 * (Limpression/all)*Math.log((Limpression+SMALL)/(all+SMALL))/log2;
			maxIGRatio=(this.entropy-IGL)/(IGRIV+SMALL);
			
		} else {
			for(int i= 0; i< (this.sequence.size()-1)/3-1; i++) {
				double Lpos= 0,Lneg= 0,Lall= 0,Rpos= 0,Rneg= 0,Rall= 0;
				
				for(int k=0; k<=i; k++) {
					Lpos += this.sequence.get(k*3);
					Lneg += this.sequence.get(k*3+1);
					Lall += this.sequence.get(k*3+2);
				}
				
				for(int j=i+1; j< (this.sequence.size()-1)/3; j++) {
					Rpos += this.sequence.get(j*3);
					Rneg += this.sequence.get(j*3+1);
					Rall += this.sequence.get(j*3+2);				
				}
				
				p1= (Lpos/Lall)*Math.log((Lpos+SMALL)/(Lall+SMALL))/log2;
				p2= (Lneg/Lall)*Math.log((Lneg+SMALL)/(Lall+SMALL))/log2;
				IGL= -1*Lall/all*(p1+p2);
				p1= (Rpos/Rall)*Math.log((Rpos+SMALL)/(Rall+SMALL))/log2;
				p2= (Rneg/Rall)*Math.log((Rneg+SMALL)/(Rall+SMALL))/log2;
				IGR= -1*Rall/all*(p1+p2);	
				//IGRatio= all/this.ActualTotal*(this.entropy- IGL- IGR)/(this.entropy + SMALL);
				IGRIV = -1* (Lall/all*Math.log((Lall+SMALL)/(all+SMALL))/log2 + Rall/all*Math.log((Rall+SMALL)/(all+SMALL))/log2);
				IGRatio= (this.entropy- IGL- IGR)/(IGRIV + SMALL);
				if( IGRatio >= maxIGRatio) {
					maxIGRatio = IGRatio;
					index = i;
					Limpression = Lall;
					Lclick = Lpos;
					Rimpression = Rall;
					Rclick = Rpos;
				}
			}
			
		}	
		ImpCliCTR.append(this.AttValue.get(index) + ",");
	    ImpCliCTR.append(Integer.toString((int)Limpression) + ",");
	    ImpCliCTR.append(Integer.toString((int)Lclick) + ",");
	    ImpCliCTR.append(Double.toString(Lclick/Limpression) + ",");
	    ImpCliCTR.append(Integer.toString((int)Rimpression) + ",");
	    ImpCliCTR.append(Integer.toString((int)Rclick) + ",");
	    ImpCliCTR.append(Double.toString(Rclick/(Rimpression+SMALL))); 
		this.sequence.clear();
		this.AttValue.clear();
		return maxIGRatio;
		
	}
	

}
