package com.six;

import static org.junit.Assert.*;

import org.junit.Test;

import com.six.mydb.utils.StringHelp;

public class StringHelpTest {
	@Test
	public void testSql() throws Exception {
		String sql = "'' or 1=1 --'";
		
		String transactSQLInjection = StringHelp.transactSQLInjection(sql);
		System.out.println(transactSQLInjection);
		
	}
}
