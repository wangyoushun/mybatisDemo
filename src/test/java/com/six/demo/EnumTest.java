package com.six.demo;



import org.junit.Test;

/**
 * 测试枚举的简单实用
* @ClassName: EnumTest 
* @Description: TODO 
* @author iwantfly 
* @date 2016年11月30日 上午10:33:19 
*
 */
public class EnumTest {

	@Test
	@Timer
	public void testName() throws Exception {
		String describe = Season.SPRING.describe();
		System.out.println(describe);
		
		String describe2 = Season.valueOf("AUTUMN").describe();
		System.out.println(describe2);
	}
	
	@Test
	public void test2() throws Exception {
//		EnumNull in = EnumNull.IN;
		TimerUtil timerUtil = new TimerUtil();
		timerUtil.getTime();
	}
}
