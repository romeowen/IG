package com.tencent.Header;

import java.util.ArrayList;

/**
 *@Package: com.tencent.Header
 *@Title: header.java
 *@author: wenshifeng
 *@date: 2014-8-1����3:31:39
 */
public class Header {
	
	public static final int IG = 1;      
	
	public static final int gini = 2;
	
	public static final int mutual = 3;
	
	public static final int SU = 4;
	
	public static final int all = 0;            // index for computing all of the criteria;
	
	public static final int NumCriteria =4;  	//number of criterion	
	// four criterion 
	public static final String [] Criterias= {"IGR","GINI","MU","SU"};
		// four paths for saving the output of Class FilesRead 
	public static final String [] kinds =  {"/IGR/0","/IGR/1", "/GINI/0","/GINI/1","/MU/0","/MU/1","/SU/0","/SU/1"};
	
	public static final String Spacesplit = "\\s+";
	
	public static final String Commasplit = ",+";  
	
	public static final String Dashsplit ="_";  //not change this;
	
	public static final String Poslabel = "1";
	
	public static final String Neglabel = "0";
	
	public static final String [] location = {"result1/", "result2/", "result3/"}; //  output paths of three mapreduce; 
    
    public static final String I = "I";     // ignored
    
    public static final String N = "N";     //numerical
    
    public static final String C = "C";     //category
    
    public static final String S = "S";    //string
     
    public static final String L = "L";    //label
    
    public static final String TD = "TD";    //threshold  divided
    
    public static final String ND = "ND";    // number divided   
    
    public static final int Numdivide = 100;    //default NumDivide for N and ND  not used
    
    public static final String SecondCombName = "Second.txt";
    
    public static final String Tempresult ="TempFeature";   // the output file name of Class CriteriaCombine
    
    public static final String Finalresult = "feature.txt";     // the final saved file name;

    public static ArrayList<String>  AttributeName = new ArrayList<String>();   // used for storing attribute names
    
    public static ArrayList<String>  AttributeClass = new ArrayList<String>();  // N S C TD ND L I
    
    public static StringBuffer AttributeThreshold = new StringBuffer();          // threshold listed
    
    public static ArrayList<Integer>  AttributesND = new ArrayList<Integer>();   // segmented regions ND
    
    public static ArrayList<Integer>  AttributeN = new ArrayList<Integer>();  //optimal threshold divided into two parts  N
    
    public static ArrayList<Integer>  AttributeIndex = new ArrayList<Integer>();  //contain the actual column number of in-consideration attribute
    
    public static boolean  UpSort= false;   //default is decent! for the GINI index,it should be true,from lower to upper; 

    public static int NumAttr = 200;        // the attributes in calculation
    
    public static  int InputCriteria = 0;   // default calculation criteria
    
    public static String output = null;     // the path to final output fold
    
    public static String AttrPath = null;   // the path to the Header file
    
    public static int PosNum =0;
    
    public static int NegNum =0;
    
    public static int PosNeg =0;
    
    public static int LableIndex =0;
    
    public static String str_null = "\\N";
}

	
