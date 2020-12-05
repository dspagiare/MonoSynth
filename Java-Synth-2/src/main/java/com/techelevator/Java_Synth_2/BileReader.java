package com.techelevator.Java_Synth_2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import java.io.FileReader;
import java.io.IOException;



public class BileReader {

	
	public static void main(String[] args) throws IOException {
		String line = "";
		File sinePatch = new File("sinePatch.csv");
		BufferedReader br = new BufferedReader(new FileReader(sinePatch));
		
		Map<String, String> map = new HashMap<>();
		
		while((line = br.readLine()) != null) {
			String[] value = line.split(",");
			
			map.put((value[0]), (value[1]));
			
			
		
			
		}
		
		System.out.println(map);
	}

}
