package com.jem.util;

import java.util.Hashtable;
import java.util.concurrent.Semaphore;

public class ConnectionManager extends Thread{
    private static final int MAX_TIMEOUT = 10000;
   private static Hashtable<String, Boolean> users = new Hashtable<String, Boolean>();
   private static Hashtable<String, String> lastPing = new Hashtable<String, String>();
   private Semaphore sem = new Semaphore(1);
   public void addUser(String ip, String timestamp, boolean isMobile) {
       try {
	   sem.acquire();
	   if(users.keySet().contains(ip)) return;
	   else {
	       
	   }
       }catch(Exception e) {
	   
       }finally {
	   sem.release();
       }
   }
   public void pingAccept(String ip, String timestamp) {
       
   }
   @Override public void run() {
       
   }

}
