package com.six.demo;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

public class FindFile {

	
	
	
	@Test
	public void testName() throws Exception {
		String path = "D:\\programfile\\apache-maven-3.3.9-bin\\reposity";
		findFile(new File(path));
		
	}
	
	public void findFile(File file){
		if(!file.exists()){
			System.out.println("path is error");
		}else{
			if(file.isFile()){
				if(file.getName().endsWith("lastUpdated")){
					print(file.getAbsolutePath());
				}
				
			}else if(file.isDirectory()){
				File[] listFiles = file.listFiles();
				for (File file2 : listFiles) {
					findFile(file2);
				}
			}
		}
	}
	
	public void print(String msg){
		System.out.println("==  "+msg);
	}
	
}
