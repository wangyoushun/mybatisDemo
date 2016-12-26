package com.six.demo;

public enum Season {

	SPRING("spring a"), SUMMER("summer b"),

	AUTUMN("summer c"), WINTER("winter  d");

	public String DESCRIBE;

	Season(String describe) {
		this.DESCRIBE = describe;
	}
	
	public  String describe(){
		return DESCRIBE;
	}

}
