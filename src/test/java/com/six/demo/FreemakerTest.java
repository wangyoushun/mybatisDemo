<<<<<<< HEAD
package com.six.demo;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.six.config.SqlConfig;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;


//
// 读取sql文件 freemaker表达式 测试类
public class FreemakerTest {

	public static void main(String[] args) throws IOException {
		FreemakerTest freemakerTest = new FreemakerTest();
		// File file = new File("User.sql");
		// System.out.println(file.getAbsolutePath());

		String resources = "sql/User.sql";
		// InputStream resourceAsStream =
		// Resources.getResourceAsStream(resources );
		String filePath = Thread.currentThread().getContextClassLoader().getResource(resources).getFile();
		List<String> lines = FileTool.lines(new File(filePath));

		ArrayList<String> sqlNameList = new ArrayList<String>();
		ArrayList<SqlConfig> sqlConfigList = new ArrayList<SqlConfig>();
		ArrayList<Integer> indexList = new ArrayList<Integer>();

		SqlConfig sqlConfig = null;
		for (int i = 0; i < lines.size(); i++) {
			String string = lines.get(i);
			if (string.contains("--start")) {
				String[] split = string.split(":");
				if (split.length > 1) {
					sqlConfig = new SqlConfig();
					String type = split[1];

					// TODO: 参数校验
					sqlConfig.setType(type);
					String[] split2 = split[2].trim().split(";");
					String id = split2[0];
					sqlConfig.setId(id);

					for (String string2 : split2) {
						String trim = string2.trim();
						if (!"".equals(trim)) {
							if (trim.startsWith("paramType=")) {
								System.out.println("1" + trim.substring("paramType=".length()));
								sqlConfig.setParamType(trim.substring("paramType=".length()));
							} else if (trim.startsWith("resultType=")) {
								sqlConfig.setResultType(trim.substring("resultType=".length()));
								System.out.println("2" + trim.substring("resultType=".length()));
							}
						}
					}
					indexList.add(i + 1);
				}
				// System.out.println(string);
			}

			if (string.contains("--end")) {
				String[] split = string.split(":");
				if (split.length > 1) {
					String str = split[2];
					if (sqlConfig.getId().equals(str.trim())) {
						sqlNameList.add(sqlConfig.getId());
						sqlConfigList.add(sqlConfig);
						indexList.add(i + 1);
						sqlConfig = null;
					} else {
						throw new RuntimeException("--sql error --");
					}
					// System.out.println(string);

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
		HashMap<String, SqlConfig> hashMap = new HashMap<String, SqlConfig>();
		int i = 0;

		for (SqlConfig sqlconfig2 : sqlConfigList) {
			StringBuilder sql = new StringBuilder();

			Integer start = indexList.get(i);
			Integer end = indexList.get(i + 1);
			for (int j = start; j < end - 1; j++) {
				sql.append(lines.get(j).trim()).append(" ");
			}
			String string = sql.toString();
			System.out.println(string);
			sqlconfig2.setSql(string);
			hashMap.put(sqlconfig2.getId(), sqlconfig2);
		}

		System.out.println(hashMap);
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

	@Test
	public void testName() throws Exception {
		String str = "--start:select:queryAdmin; paramType=java.util.HashMap; resultType=java.util.HashMap;";
		String[] split = setSqlConfig(str);

		System.out.println(split.length);

	}

	private String[] setSqlConfig(String str) {
		String[] split = str.split(";");
		for (String string : split) {
			String trim = string.trim();
			if (!"".equals(trim)) {
				if (trim.startsWith("paramType=")) {
					String substring = trim.substring("paramType=".length());
					System.out.println(substring);
				} else if (trim.startsWith("resultType=")) {
					String substring = trim.substring("resultType=".length());
					System.out.println(substring);
				}
			}
		}
		return split;
	}
}
=======
package com.six.demo;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.six.config.SqlConfig;

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

		String resources = "sql/User.sql";
		// InputStream resourceAsStream =
		// Resources.getResourceAsStream(resources );
		String filePath = Thread.currentThread().getContextClassLoader().getResource(resources).getFile();
		List<String> lines = FileTool.lines(new File(filePath));

		ArrayList<String> sqlNameList = new ArrayList<String>();
		ArrayList<SqlConfig> sqlConfigList = new ArrayList<SqlConfig>();
		ArrayList<Integer> indexList = new ArrayList<Integer>();

		SqlConfig sqlConfig = null;
		for (int i = 0; i < lines.size(); i++) {
			String string = lines.get(i);
			if (string.contains("--start")) {
				String[] split = string.split(":");
				if (split.length > 1) {
					sqlConfig = new SqlConfig();
					String type = split[1];

					// TODO: 参数校验
					sqlConfig.setType(type);
					String[] split2 = split[2].trim().split(";");
					String id = split2[0];
					sqlConfig.setId(id);

					for (String string2 : split2) {
						String trim = string2.trim();
						if (!"".equals(trim)) {
							if (trim.startsWith("paramType=")) {
								System.out.println("1" + trim.substring("paramType=".length()));
								sqlConfig.setParamType(trim.substring("paramType=".length()));
							} else if (trim.startsWith("resultType=")) {
								sqlConfig.setResultType(trim.substring("resultType=".length()));
								System.out.println("2" + trim.substring("resultType=".length()));
							}
						}
					}
					indexList.add(i + 1);
				}
				// System.out.println(string);
			}

			if (string.contains("--end")) {
				String[] split = string.split(":");
				if (split.length > 1) {
					String str = split[2];
					if (sqlConfig.getId().equals(str.trim())) {
						sqlNameList.add(sqlConfig.getId());
						sqlConfigList.add(sqlConfig);
						indexList.add(i + 1);
						sqlConfig = null;
					} else {
						throw new RuntimeException("--sql error --");
					}
					// System.out.println(string);

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
		HashMap<String, SqlConfig> hashMap = new HashMap<String, SqlConfig>();
		int i = 0;

		for (SqlConfig sqlconfig2 : sqlConfigList) {
			StringBuilder sql = new StringBuilder();

			Integer start = indexList.get(i);
			Integer end = indexList.get(i + 1);
			for (int j = start; j < end - 1; j++) {
				sql.append(lines.get(j).trim()).append(" ");
			}
			String string = sql.toString();
			System.out.println(string);
			sqlconfig2.setSql(string);
			hashMap.put(sqlconfig2.getId(), sqlconfig2);
		}

		System.out.println(hashMap);
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

	@Test
	public void testName() throws Exception {
		String str = "--start:select:queryAdmin; paramType=java.util.HashMap; resultType=java.util.HashMap;";
		String[] split = setSqlConfig(str);

		System.out.println(split.length);

	}

	private String[] setSqlConfig(String str) {
		String[] split = str.split(";");
		for (String string : split) {
			String trim = string.trim();
			if (!"".equals(trim)) {
				if (trim.startsWith("paramType=")) {
					String substring = trim.substring("paramType=".length());
					System.out.println(substring);
				} else if (trim.startsWith("resultType=")) {
					String substring = trim.substring("resultType=".length());
					System.out.println(substring);
				}
			}
		}
		return split;
	}
}
>>>>>>> branch 'master' of https://github.com/wangyoushun/mybatisDemo.git
