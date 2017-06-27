package com.softwarexpressllc.service.stat;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class TestCreateStatDataInput {

	public TestCreateStatDataInput () {
		StatDataInput input = new StatDataInput();
		input.setRequestId("req-1");
		float numbers [] = new float[]{2,4,6};
		input.setNumbers(numbers);
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.enable(SerializationFeature.INDENT_OUTPUT);
		
		try {
			System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(input));
		} catch (IOException e) {			
			e.printStackTrace();
		}		
	}
	
	
	public static void main(String[] args) {
		new TestCreateStatDataInput();
	}

}
