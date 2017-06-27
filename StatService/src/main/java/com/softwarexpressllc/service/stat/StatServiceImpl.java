package com.softwarexpressllc.service.stat;

import java.util.Arrays;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/api/statistics")
public class StatServiceImpl {
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Secured ({Role.ROLE_1})
	@Path("/coreStat")
	public StatDataOutput getCoreStats(StatDataInput input) {				
		String requestId = input.getRequestId();
		float[] numbers = input.getNumbers();
		float minValue = min(numbers);
		float avgValue = avg(numbers);
		float maxValue = max(numbers);
				
		StatDataOutput statData = new StatDataOutput();
		statData.setRequestId(requestId);
		statData.setMin(minValue);
		statData.setAvg(avgValue);
		statData.setMax(maxValue);
		return statData;
	}

	private float min(float[] numbers) {
		Arrays.sort(numbers);
		return numbers[0];
	}

	private float max(float[] numbers) {
		Arrays.sort(numbers);
		return numbers[numbers.length - 1];
	}
	
	private float avg(float[] numbers) {
		float total = 0;
		for(int i = 0; i < numbers.length; i++) {
			total += numbers[i];
		}		
		return total/numbers.length;
	}
}
