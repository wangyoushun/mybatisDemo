package com.six.demo;

import java.util.Properties;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

/**
* @ClassName: TimePlugin 
* @Description: mybatis时间插件 
* @author iwantfly 
* @date 2016年12月4日 下午5:01:41 
*
 */
@Intercepts({ @Signature(type = Executor.class, method = "query", args = {
		MappedStatement.class, Object.class,RowBounds.class,ResultHandler.class }) })
public class TimePlugin implements Interceptor {

	public Object intercept(Invocation invocation) throws Throwable {
		System.out.println("interceptor query start------");
		long start = System.currentTimeMillis();
		Object proceed = invocation.proceed();
		long end = System.currentTimeMillis();
		System.out.println("interceptor query end------");
		System.out.println("query cost time " + (end - start));

		return proceed;
	}

	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	public void setProperties(Properties properties) {
		// TODO Auto-generated method stub
	}

}
