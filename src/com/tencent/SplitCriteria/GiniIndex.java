package com.tencent.SplitCriteria;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.hadoop.io.Text;

/**
 *@Package: com.tencent.SplitCriteria
 *@Title: GiniIndex.java
 *@author: wenshifeng
 *@date: 2014-8-3����2:15:53
 */
public class GiniIndex {
	

	public static double SMALL = 1e-6;
	private ArrayList<Double> sequence = new ArrayList<Double>();
	private ArrayList<String> AttValue = new ArrayList<String>();
	private double gini = 0;    
	public StringBuffer ImpCliCTR = new StringBuffer();
	private double ActuralTotal =0; 
	
	public GiniIndex(Text sequence,ArrayList<Double> LabelInfor) {		
		String [] TempValue=sequence.toString().split(",");	
		for(int i=0; i< TempValue.length-1; i++) {
			if(i%4==0 ) 	{
				this.AttValue.add(TempValue[i]);
			}else {
				this.sequence.add(Double.parseDouble(TempValue[i]));
			}			
		}//end for
		this.sequence.add(Double.parseDouble(TempValue[TempValue.length-1]));	
		this.ActuralTotal = LabelInfor.get(2);
	}//end public
	
	
	public double ComputeGini() {
		
		double p=0;
		double x1,x2,x3;
		double all = this.sequence.get(this.sequence.size()-1);
		for(int i=0; i< (this.sequence.size()-1)/3; i++) {
			
			x1= this.sequence.get(i*3);
			x2= this.sequence.get(i*3+1);
			x3= this.sequence.get(i*3+2);	
			
			//p=2* (x1*x2/Math.pow(x3,2));		
			p=2* (x1/x3)*(x2/x3);	
			this.gini+= x3/all*p;
			
		    ImpCliCTR.append(this.AttValue.get(i) + ",");
		    ImpCliCTR.append(Integer.toString((int)x3) + ",");
		    ImpCliCTR.append(Integer.toString((int)x1) + ",");
		    ImpCliCTR.append(Double.toString(x1/x3) + ",");
		    }
		ImpCliCTR.deleteCharAt(ImpCliCTR.length()-1);
		this.sequence.clear();
		this.AttValue.clear();
		//return this.ActuralTotal/all*this.gini;
		return this.gini;
		
	}
	
	public double OptimalGini() {
		

		double p1= 0,p2= 0;
		int index = 0;
		double  GN, minGini= 1;
		double Limpression=0, Lclick=0, Rimpression=0, Rclick=0;
		double all= this.sequence.get(this.sequence.size()-1);
		if(this.sequence.size()==4) {
			
			 Lclick = this.sequence.get(0);
			 Limpression = this.sequence.get(2);
			 p1=2*Lclick/Limpression*(1-Lclick/Limpression);
			 //minGini=this.ActuralTotal/all*(Limpression/all*p1);
			 minGini= (Limpression/all*p1);
			
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
				
				//p1= 2*Lpos*Lneg/Math.pow(Lall, 2);
				p1= 2*(Lpos/Lall)*(Lneg/Lall);
				//p2= 2* Rpos*Rneg/Math.pow(Rall, 2); 
				p2= 2* (Rpos/Rall)*(Rneg/Rall); 
				//GN = this.ActuralTotal/all*(Lall/all*p1 + Rall/all*p2);	
				GN = (Lall/all*p1 + Rall/all*p2);		

				if( GN < minGini) {
					minGini = GN;
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
		return minGini;
		
		
	}


}

