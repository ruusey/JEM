package com.jem.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class DBCopy {
    public static void main(String[] args) {
	// saveTables();

	saveTables("lb","root","curr_dump.sql");

    }

    public static boolean saveTables(String dbName, String user, String outFile) {
		boolean success = false;
	    if(!outFile.contains(".sql")) {
		System.out.println("Output file must be [filename].sql\nEnter a valid file name.");
		return success;
	    }
	    String executeCmd = "mysqldump";
	    String userArg = "-u"+user;
	    String dbArg = dbName;
	    String outArg = "-r"+outFile;
	    
	    try {
		success=saveTable2(executeCmd,userArg,dbArg,outArg);
	    } catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	    return success;
    }

    public static boolean saveTable2(String run, String arg1, String arg2, String arg3) throws Exception {
	ProcessBuilder ps = new ProcessBuilder(run, arg1, arg2, arg3);

	// From the DOC: Initially, this property is false, meaning that the
	// standard output and error output of a subprocess are sent to two
	// separate streams
	ps.redirectErrorStream(true);

	Process pr = ps.start();

	BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream()));
	String line;
	while ((line = in.readLine()) != null) {
	    System.out.println(line);
	}
	pr.waitFor();
	in.close();
	if(pr.exitValue()==0) {
	    System.out.println("Succesfully backed up '"+arg2+"' to file '"+arg3.replace("-r", "")+"'");
	}else {
	    System.out.println("Error backing up '"+arg2+"' to file '"+arg3.replace("-r", "")+"'");
	}
	return true;
    }

}
