package com.jem.util;

import java.util.Hashtable;
import java.util.concurrent.Semaphore;

public class ConnectionManager extends Thread{
    private static final int MAX_TIMEOUT = 10000;
   
   private static Hashtable<String, String> lastPing = new Hashtable<String, String>();
   private Semaphore sem = new Semaphore(1);
   public boolean addNewUser(String ip, String timestamp) {
       try {
	   sem.acquire();
	   if(lastPing.keySet().contains(ip)) return false;
	   else {  
	       return true;
	   }
       }catch(Exception e) {
	   return false;
       }finally {
	   sem.release();
       }
   }
   public void pingAccept(String ip, String timestamp) {
       if(addNewUser(ip,timestamp)) {
	   lastPing.put(ip, timestamp);
       }else {
	   lastPing.remove(ip);
	   lastPing.put(ip, timestamp);  
       }
       
   }
   @Override public void run() {
       while(true) {
	   
       }
   }

}
