package enjuu.framwork;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

public class AppTest {
	
	public static void main(String[] args) {
		
		if(!new File("/templates").exists()) {
			new File("/templates").mkdirs();
		}
		
		File[] listOfFiles = new File("/templates").listFiles();

		for (File file : listOfFiles) {
		    if (file.isFile()) {
		    	Spark.get(new Route(file.getName().replaceAll(".html", "")) {
					
					@Override
					public Object handle(Request request, Response response) {
						File file = new File("C:\\Users\\pankaj\\Desktop\\test.txt"); 
						  
						  BufferedReader br = null;
						try {
							br = new BufferedReader(new FileReader(file));
						} catch (FileNotFoundException e1) {
							e1.printStackTrace();
						} 
						  
						  String end = null;
						  String st; 
						  try {
							while ((st = br.readLine()) != null) {
								end = end +  st + "\n";
							}
							    
								
						} catch (IOException e) {
							e.printStackTrace();
						} 
						  if(end == null) {
							  //404
							  return "404";
						  }
						return end;
					}
				});
		    }
		}
		
	}

}
