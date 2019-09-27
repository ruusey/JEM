package com.jem.search;
//------------------------------------------------------------------------------------------------------------------------------------------
//
//N2ANALYTICS, LLC ("COMPANY") CONFIDENTIAL
//Unpublished Copyright (c) 2013-2016 N2ANALYTICS, LLC, All Rights Reserved.
//
//NOTICE:  All information contained herein is, and remains the property of COMPANY. The intellectual and technical concepts contained
//herein are proprietary to COMPANY and may be covered by U.S. and Foreign Patents, patents in process, and are protected by trade secret or copyright law.
//Dissemination of this information or reproduction of this material is strictly forbidden unless prior written permission is obtained
//from COMPANY.  Access to the source code contained herein is hereby forbidden to anyone except current COMPANY employees, managers or contractors who have executed
//Confidentiality and Non-disclosure agreements explicitly covering such access.
//
//The copyright notice above does not evidence any actual or intended publication or disclosure of this source code, which includes  
//information that is confidential and/or proprietary, and is a trade secret, of COMPANY.  ANY REPRODUCTION, MODIFICATION, DISTRIBUTION, PUBLIC  PERFORMANCE,
//OR PUBLIC DISPLAY OF OR THROUGH USE OF THIS SOURCE CODE WITHOUT THE EXPRESS WRITTEN CONSENT OF COMPANY IS STRICTLY PROHIBITED, AND IN VIOLATION OF APPLICABLE
//LAWS AND INTERNATIONAL TREATIES.  THE RECEIPT OR POSSESSION OF THIS SOURCE CODE AND/OR RELATED INFORMATION DOES NOT CONVEY OR IMPLY ANY RIGHTS  
//TO REPRODUCE, DISCLOSE OR DISTRIBUTE ITS CONTENTS, OR TO MANUFACTURE, USE, OR SELL ANYTHING THAT IT  MAY DESCRIBE, IN WHOLE OR IN PART.                
//
//------------------------------------------------------------------------------------------------------------------------------------------

import java.util.HashMap;

import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.concurrent.Semaphore;

import com.jem.models.JobRequest;

public abstract class IndexInteger {
	
	HashMap<String,HashMap<Integer,Integer>> tokenMap= new HashMap<String,HashMap<Integer,Integer>>(20000);
	HashMap<Integer,Object> objectMap= new HashMap<Integer,Object>(10000);
	static int findMatchLimit=100;
	static String REGEX="\t\n\r\f,. :<@!>()$+'-=/[]_\"\\/";
	Semaphore sem = new Semaphore(1);
	
	protected void index(int id,String expression){
		
		//System.out.println("TOKENMAP="+tokenMap.size());
		int tokenCount=0;
		try {
			//sem.acquire();
			StringTokenizer st = new StringTokenizer(expression.toLowerCase(),REGEX,false);
			Vector <String>tokens = new Vector<String>(st.countTokens());
			HashMap<String,String> uniqueTokens = new HashMap<String,String>(st.countTokens());
			
		    tokenCount=0;
		    //System.out.println(expression.substring(0,20)+" "+expression.length()+" - "+st.countTokens());
			while (st.hasMoreTokens()) {
		    	tokenCount++;
		    	String oToken =st.nextToken().toLowerCase();
		      	if ( uniqueTokens.get(oToken)==null){
		    		uniqueTokens.put(oToken, oToken);
		    		tokens.add(oToken);
		    	}
		    }
	
		    String token="";
		    for (int l=0;l<tokens.size();l++){
		    	token = (String)tokens.get(l);
		    	//DO PHRASES PARTIAL
	    		String subString ="";
	    		for (int l2=1;l2<=token.length();l2++){
	    			try {
	    				subString = new String(token.substring(0,l2));
	    			}catch (Exception e){
	    				e.printStackTrace();	
	    			}
	    			//HashMap of matching Objects with this Substring
	    			HashMap<Integer,Integer> matches = tokenMap.get(subString);
	    			if(matches==null){
	    				matches=new HashMap<Integer,Integer>(1000);
	    				tokenMap.put(subString,matches);
	    				}
	    			matches.put(id, id);
			    	//tokenMap.put(subString,matches);
	    		}
	 	    	if (l==tokenCount-1)break;
		    }
		} catch (Exception e){
			e.printStackTrace();
		}finally {
			//sem.release();
		}
	}
	
	public Vector<Object> search(String expression,Integer maxResults){
		
		findMatchLimit=maxResults;
		Vector<Object> results = new Vector<Object>(maxResults);
		HashMap<Integer,Integer> currentMatches=null;
		HashMap<Integer,Integer> newMatches=null;
		HashMap<Integer,Integer> prevMatches=null;
		StringTokenizer st = new StringTokenizer(expression.toLowerCase(),REGEX,false);		
	    try {
			//sem.acquire();
			while (st.hasMoreTokens()) {
		    	String token =st.nextToken().toLowerCase();
				currentMatches=tokenMap.get(token);
				
				if (prevMatches==null){prevMatches=currentMatches;}				
				if (currentMatches==null){return results;}
				
				newMatches= new HashMap<Integer,Integer> (currentMatches.size());
				
				for (Integer id : currentMatches.keySet()) {
					Integer testPrev =prevMatches.get(id);
				    if (testPrev!=null)newMatches.put(id, id);
				}
				prevMatches=newMatches;
		    }

	    } catch (Exception e){
	    	e.printStackTrace();
	    }finally {
	    	//sem.release();
	    }
	    int matchCount=0;
	    for (Integer id : prevMatches.values()) {
	    	Object category = objectMap.get(id);
			if (category!=null){
				results.add(objectMap.get(id));
				matchCount++;
				if (matchCount>findMatchLimit){break;}
			}
	    }
	    return results;
	}
	abstract public void buildJobIndex(List<JobRequest> objectList);
}
