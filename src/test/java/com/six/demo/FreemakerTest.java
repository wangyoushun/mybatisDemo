package com.six.demo;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;

// 读取sql文件 freemaker表达式 测试类
public class FreemakerTest {

	public static void main(String[] args) throws IOException {
		FreemakerTest freemakerTest = new FreemakerTest();
		// File file = new File("User.sql");
		// System.out.println(file.getAbsolutePath());

		String resources = "User.sql";
		// InputStream resourceAsStream =
		// Resources.getResourceAsStream(resources );
		String filePath = Thread.currentThread().getContextClassLoader()
				.getResource(resources).getFile();
		List<String> lines = FileTool.lines(new File(filePath));

		ArrayList<String> sqlNameList = new ArrayList<String>();
		ArrayList<Integer> indexList = new ArrayList<Integer>();

		//
		String curName = "";

		for (int i = 0; i < lines.size(); i++) {
			String string = lines.get(i);
			if (string.contains("--start")) {
				String[] split = string.split(":");
				if (split.length > 1) {
					String string2 = split[2];
					curName = string2.trim();
					indexList.add(i + 1);
				}
			}

			if (string.contains("--end")) {
				String[] split = string.split(":");
				if (split.length > 1) {
					String string2 = split[2];
					if (curName.equals(string2.trim())) {
						sqlNameList.add(curName);
						indexList.add(i + 1);
					} else {
						throw new RuntimeException("--sql error --");
					}
				}
			}

		}

		if (sqlNameList.size() == 0) {
			return;
		}

		// 检查是否有重复sql
		ArrayList<String> sqlNameListNew = new ArrayList<String>();
		for (String string : sqlNameList) {
			if (sqlNameListNew.contains(string)) {
				throw new RuntimeException("--sql already exist --" + string);
			} else {
				sqlNameListNew.add(string);
			}
		}

		// 检查sql名称和位置是否对应
		if (sqlNameList.size() * 2 != indexList.size()) {
			throw new RuntimeException("--sql -- error--");
		}

		// 读取sql
		HashMap<String, String> hashMap = new HashMap<String, String>();
		int i = 0;
		for (String string : sqlNameList) {
			StringBuilder sql = new StringBuilder();

			Integer start = indexList.get(i);
			Integer end = indexList.get(i + 1);
			for (int j = start; j < end - 1; j++) {
				sql.append(lines.get(j).trim()).append(" ");
			}

			hashMap.put(string, sql.toString());
		}

		System.out.println(hashMap);

		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("type", 4);

		Set<String> keySet = hashMap.keySet();
		for (String string : keySet) {
			String string2 = hashMap.get(string);
			System.out.println(string2);
			String compileString = freemakerTest.compileString(string2,
					paramMap);
			System.out.println(compileString);
		}
	}

	public String compileString(String name, Map<String, Object> root) {
		try {
			Configuration e = new Configuration();
			e.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);
			StringTemplateLoader templateLoader = new StringTemplateLoader();
			templateLoader.putTemplate("temp", name);
			e.setTemplateLoader(templateLoader);
			e.setDefaultEncoding("UTF-8");
			StringWriter writer = new StringWriter();

			String var6;
			try {
				Template template = e.getTemplate("temp");
				template.process(root, writer);
				var6 = writer.toString();
			} finally {
				writer.close();
			}

			return var6;
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
}
