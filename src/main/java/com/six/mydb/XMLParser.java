package com.six.mydb;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.six.mydb.utils.FileTool;

public class XMLParser {

	private XPath xPath;
	private Config config;
	private Logger logger = Logger.getLogger(getClass());
	private DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

	public XMLParser() {
		this.config = new Config();
		this.xPath = XPathFactory.newInstance().newXPath();
	}

	public Config parserXml(String resources) throws Exception {
		factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = factory.newDocumentBuilder();
		db.setEntityResolver(new XMLConfigEntityResolver());

		// 解析dbconfig
		InputStream resourceAsStream = Resources.getResourceAsStream(resources);

		Document document = db.parse(resourceAsStream);
		Node configurationNode = (Node) xPath.evaluate("/configuration", document, XPathConstants.NODE);

		parserProperties(configurationNode);
		parserSettings(configurationNode);
		parserEnvironments(configurationNode);

		// 解析dbmapper
		parserMappers(configurationNode);

		resourceAsStream.close();
		return config;
	}

	/**
	 * 解析config文件中mapper标签
	 */
	public void parserMappers(Node configurationNode) throws Exception {
		logger.debug("=========parserMappers============start");

		Node mappersNode = (Node) xPath.evaluate("mappers", configurationNode, XPathConstants.NODE);
		if (mappersNode.hasChildNodes()) {
			NodeList childNodes = mappersNode.getChildNodes();
			for (int i = 0; i < childNodes.getLength(); i++) {
				Node item = childNodes.item(i);
				if (item.getNodeType() == Node.ELEMENT_NODE) {
					String resource = item.getAttributes().getNamedItem("resource").getNodeValue();
					parserSqlMapper(resource);
				}
			}

		}
		logger.debug("=========parserMappers============end");
	}

	// 解析sql文件
	public void parserSqlMapper(String path) {
		logger.debug("=========parsersql============start==="+path);
		
		String filePath = Thread.currentThread().getContextClassLoader().getResource(path).getFile();
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
//								System.out.println("1" + trim.substring("paramType=".length()));
								sqlConfig.setParamType(trim.substring("paramType=".length()));
							} else if (trim.startsWith("resultType=")) {
								sqlConfig.setResultType(trim.substring("resultType=".length()));
//								System.out.println("2" + trim.substring("resultType=".length()));
							}
						}
					}
					indexList.add(i + 1);
				}
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
		config.setSqlMap(hashMap);
//		config.setSqlMap(hashMap);
		logger.debug("=========parsersql============end==="+path);
	}

	

	
	public String getAttrValue(Node node, String name) {
		Node namedItem = node.getAttributes().getNamedItem(name);
		if (namedItem == null) {
			return null;
		}
		return namedItem.getNodeValue();
	}

	/**
	 * 解析config文件parserProperties标签
	 */
	public void parserProperties(Node configurationNode) throws Exception {
		logger.debug("=========parserProperties============  start");
		Node propertiesNode = (Node) xPath.evaluate("properties", configurationNode, XPathConstants.NODE);

		Properties properties = new Properties();
		if (propertiesNode.hasChildNodes()) {
			NodeList childNodes = propertiesNode.getChildNodes();
			for (int i = 0; i < childNodes.getLength(); i++) {
				Node item = childNodes.item(i);
				if (item.getNodeType() == Node.ELEMENT_NODE) {
					String name = item.getAttributes().getNamedItem("name").getNodeValue();
					String value = item.getAttributes().getNamedItem("value").getNodeValue();
					properties.put(name, value);
				}
			}
		}

		NamedNodeMap attributes = propertiesNode.getAttributes();
		Node namedItem = attributes.getNamedItem("resource");
		String nodeValue = namedItem.getNodeValue();
		print(nodeValue);

		InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("datasource.properties");
		Properties properties2 = new Properties();
		properties2.load(resourceAsStream);
		properties.putAll(properties2);

		print(properties.toString());
		config.setSetProperties(properties);

		resourceAsStream.close();
		logger.debug("=========parserProperties============  end");

	}

	/**
	 * 解析config文件setting标签
	 */
	public void parserSettings(Node configurationNode) throws Exception {
		logger.debug("=========parserSettings=================start");
		Node settingsNode = (Node) xPath.evaluate("settings", configurationNode, XPathConstants.NODE);
		logger.debug(settingsNode.getNodeName());

		NodeList childNodes = settingsNode.getChildNodes();
		Map<String, String> setMap = new HashMap<String, String>();
		for (int i = 0; i < childNodes.getLength(); i++) {
			Node item = childNodes.item(i);
			if (item.getNodeType() == Node.ELEMENT_NODE) {
				String name = item.getAttributes().getNamedItem("name").getNodeValue();
				String value = item.getAttributes().getNamedItem("value").getNodeValue();
				setMap.put(name, value);
			}
		}
		logger.debug(setMap);
		config.setSetMap(setMap);
		logger.debug("=========parserSettings=================end");

	}

	/**
	 * 解析config文件environment标签
	 */
	public void parserEnvironments(Node configurationNode) throws Exception {
		logger.debug("=========parserEnvironments===============start");
		Node environmentsNode = (Node) xPath.evaluate("environments", configurationNode, XPathConstants.NODE);

		NodeList childNodes = environmentsNode.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {
			Node item = childNodes.item(i);

			if (item.getNodeType() == Node.ELEMENT_NODE) {
				String id = item.getAttributes().getNamedItem("id").getNodeValue();
				Environment environment = new Environment();
				Node transactionManager = (Node) xPath.evaluate("transactionManager", item, XPathConstants.NODE);
				String nodeValue = transactionManager.getAttributes().getNamedItem("autoCmit").getNodeValue();
				environment.setAutoCmit(Boolean.parseBoolean(nodeValue));

				Node dataSource = (Node) xPath.evaluate("dataSource", item, XPathConstants.NODE);
				Map<String, String> childAttr = getChildAttr(dataSource);
				parserValue(childAttr);

				environment.setId(id);
				environment.setDriver(childAttr.get("driver"));
				environment.setUrl(childAttr.get("url"));
				environment.setUsername(childAttr.get("username"));
				environment.setPassword(childAttr.get("password"));
				config.setEnvironment(environment);
			}
		}
		logger.debug("=========parserEnvironments==================end");
	}

	public void parserValue(Map<String, String> childAttr) {
		Set<String> keySet = childAttr.keySet();
		TokenParser tokenParser = new TokenParser(config.getSetProperties());
		for (String key : keySet) {
			String value = childAttr.get(key);
			String parser = tokenParser.parser(value);
			childAttr.put(key, parser);
		}
	}

	public Map<String, String> getChildAttr(Node node) {
		Map<String, String> childAttrMap = new HashMap<String, String>();
		if (node.hasChildNodes()) {
			NodeList childNodes = node.getChildNodes();
			for (int i = 0; i < childNodes.getLength(); i++) {
				Node item = childNodes.item(i);
				if (item.getNodeType() == Node.ELEMENT_NODE) {
					String name = item.getAttributes().getNamedItem("name").getNodeValue();
					String value = item.getAttributes().getNamedItem("value").getNodeValue();
					childAttrMap.put(name, value);
				}
			}
		}
		return childAttrMap;
	}

	public void printNOdeName(Node node) {
		if (!node.hasChildNodes()) {
			print(node.getNodeName());
		} else {
			print(node.getNodeName());
			NodeList childNodes = node.getChildNodes();
			for (int i = 0; i < childNodes.getLength(); i++) {
				printNOdeName(childNodes.item(i));
			}
		}
	}

	public Config getConfig() {
		return config;
	}

	@Test
	public void testName() throws Exception {
		InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("com/six/mydb/UserMapper.xml");
		System.out.println(resourceAsStream);
	}

	public void print(String msg) {
		System.out.println(msg);
	}

}
