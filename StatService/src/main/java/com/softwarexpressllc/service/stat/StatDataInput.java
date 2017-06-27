package com.softwarexpressllc.service.stat;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "StatDataInput")
public class StatDataInput {
	String requestId;
	float numbers [];
	
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	public float[] getNumbers() {
		return numbers;
	}
	public void setNumbers(float[] numbers) {
		this.numbers = numbers;
	}
}
