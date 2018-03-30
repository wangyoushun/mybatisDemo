package com.six.mydb.utils;

import java.io.StringWriter;
import java.util.Map;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;

public class FreeMarkerKit {

	public static String parseSql(String templateStr, Map<String, Object> map) {
		String str = "";
		try {
			Configuration e = new Configuration();
			e.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);
			StringTemplateLoader sTmpLoader = new StringTemplateLoader();
			sTmpLoader.putTemplate("template", templateStr);

			e.setTemplateLoader(sTmpLoader);
			e.setDefaultEncoding("UTF-8");
			StringWriter writer = new StringWriter();

			try {
				Template template = e.getTemplate("template");
				template.process(map, writer);
				str = writer.toString();
			} finally {
				writer.close();
			}
			System.out.println(str);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		return str;
	}

}
