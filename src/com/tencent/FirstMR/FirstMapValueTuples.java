package com.tencent.FirstMR;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;

/**
 *@Package: com.tencent.FeatureSelection
 *@Title: FirstMapValueTuples.java
 *@author: wenshifeng
 *@date: 2014-7-30ÉÏÎç9:58:47
 *@discription: the output of map function, which contains posnum,negnum, and pos+ neg;
 */

 
public class FirstMapValueTuples implements Writable, WritableComparable<FirstMapValueTuples>{
	
    public int postiveNum;
	
    public int negativeNum;	

    public int posAndneg;
	
	//@Override  copy is not a override method.
	public FirstMapValueTuples copy() {
		
		FirstMapValueTuples tuples = new FirstMapValueTuples();		
		tuples.postiveNum = postiveNum;
		tuples.negativeNum = negativeNum;
		tuples.posAndneg = posAndneg;
		return tuples;
	}
	
	//  the methods below are all belonged to WritableComparable. among the first two belong to writable,too.
	@Override
	public void write(DataOutput out) throws IOException {		
		out.writeInt(postiveNum);
		out.writeInt(negativeNum);
		out.writeInt(posAndneg);
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		postiveNum = in.readInt();
		negativeNum = in.readInt();
		posAndneg = in.readInt();
	}
	
	@Override
	public int compareTo(FirstMapValueTuples arg0) {

		if (this.postiveNum > arg0.postiveNum) {
			return 1;
		} else if (this.postiveNum < arg0.postiveNum) {
			return -1;
		}

		if (this.negativeNum > arg0.negativeNum) {
			return 1;
		} else if (this.negativeNum < arg0.negativeNum) {
			return -1;
		}
		
		if (this.posAndneg > arg0.posAndneg) {
			return 1;
		} else if (this.posAndneg < arg0.posAndneg) {
			return -1;
		}
		
		return 0;
	}
	
	@Override
	public int hashCode() {

		int result = 17;		
		result = 37 * result + (int) postiveNum;
		result = 37 * result + (int) negativeNum;
		long toint = 37 * result + (int) posAndneg;		
		result = 37 * result + (int) (toint ^ (toint >>> 32));
		return result;
	}

	@Override
	public String toString() {
		return postiveNum + "," + negativeNum + "," + posAndneg;
	}

	@Override
	public boolean equals(Object o) {

		if (!(o instanceof FirstMapValueTuples))
			return false;

		FirstMapValueTuples tuples = (FirstMapValueTuples) o;

		return 
				tuples.postiveNum == postiveNum			  
				&& tuples.negativeNum == negativeNum
				&& tuples.posAndneg == posAndneg;

	}

}
	
