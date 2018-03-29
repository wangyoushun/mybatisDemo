package com.six.demo;

import java.io.File;

public class RecursionTest {
	public static void main(String[] args) {
		File file = new File("E:/1");
		run(file);
	}

	private static void run(File file) {
		 File[] listFiles = file.listFiles();
		 for (File file2 : listFiles) {
			 System.out.println(file2.getName());
			run(file2);
		}
		
	}
	
//	public List<String> run(){
//		
//	}
	
	
}
