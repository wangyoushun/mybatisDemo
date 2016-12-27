package com.six.mydb;

import static org.junit.Assert.*;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XmlParserTest {

	private XPath xPath;
	private Config config;
	private Logger logger = Logger.getLogger(getClass());
	private DocumentBuilderFactory factory = DocumentBuilderFactory
			.newInstance();

	Document document;
	private String resources = "com/six/mydb/UserMapper.xml";

	@Before
	public void init() throws Exception {
		this.config = new Config();
		this.xPath = XPathFactory.newInstance().newXPath();
		factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = factory.newDocumentBuilder();
		db.setEntityResolver(new XMLConfigEntityResolverTest());

		InputStream resourceAsStream = Resources.getResourceAsStream(resources);
		document = db.parse(resourceAsStream);
	}

	@Test
	public void testParserMapxml() throws Exception {
		Node mapper = (Node) xPath.evaluate("/mapper", document,
				XPathConstants.NODE);
		String nameSpace = mapper.getAttributes().getNamedItem("namespace")
				.getNodeValue();
		System.out.println(nameSpace);
	}

	//测试获取mappe.xml数据
	@Test
	public void getAttrValue() throws Exception {
		Node mapper = (Node) xPath.evaluate("/mapper", document,
				XPathConstants.NODE);
		NodeList selectNodes = (NodeList) xPath.evaluate("//select", mapper,
				XPathConstants.NODESET);
		Map<String,MapStatement> map = new HashMap<String,MapStatement>();
		TokenParser parser = new TokenParser();
		
		for (int i = 0; i < selectNodes.getLength(); i++) {
			Node selectNode = selectNodes.item(i);
			if (selectNode.getNodeType() == Node.ELEMENT_NODE) {
				String id = getAttrValue(selectNode, "id");
				String parameterType = getAttrValue(selectNode, "parameterType");
				String resultType = getAttrValue(selectNode, "resultType");
				NodeList childNodes = selectNode.getChildNodes();
				String sql = "";

				for (int j = 0; j < childNodes.getLength(); j++) {
					String nodeName = childNodes.item(j).getNodeName();
					String nodeValue = childNodes.item(j).getNodeValue();
					if ("#text".equals(nodeName)) {
						nodeValue = nodeValue.trim();
						sql += nodeValue;
					}
				}
				
				MapStatement mapStatement = new MapStatement();
				mapStatement.setId(id);
				mapStatement.setParameterType(parameterType);
				mapStatement.setResultType(resultType);
				mapStatement.setSqlStr(parser.parserSql(sql));
				System.out.println(mapStatement);
				//将解析数据放入map中， 判断map中
				map.put(id, mapStatement);
			}
		}
		
		System.out.println(map);
	}
	
//	@Test
	public void testsql(Map<String,MapStatement> map) throws Exception {
		MapStatement mapStatement = map.get("selectAllUser");
		String parameterType = mapStatement.getParameterType();
		
		if(parameterType==null || "".equals(parameterType))
			return;
		
	}
	

	@Test
	public void testParam(){
		String sql = "select * from user  t where t.id=${id} and t.name=${name}";
//		Properties properties = new Properties();
//		properties.setProperty("id", "123");
//		//解析传入参数，
//		TokenParser tokenParser = new TokenParser(properties);
		TokenParser tokenParser = new TokenParser();
		String parser = tokenParser.parserSql(sql);
		
		System.out.println(parser);
	}
	
	//测试反射
	@Test
	public void testReflect(){
		String path = "com.six.domain.User";
		Class<?> clazz;
		try {
			clazz = Class.forName(path);
			Field[] declaredFields = clazz.getDeclaredFields();
			for (Field field : declaredFields) {
				System.out.println(field.getName()+"--"+field.getType());
			}
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	//get
	@Test
	public void testReflectGet() throws Exception{
		String path = "com.six.domain.User";
		Class<?> clazz;
		try {
			clazz = Class.forName(path);
			Object object = clazz.newInstance();
			Method[] methods = clazz.getMethods();
			for (Method method : methods) {
				String name = method.getName();
				if(name!=null && name.length()>3 && "get".equals(name.substring(0, 3)) && !"getClass".equals(name)){
					System.out.println(name);
					Object invoke = method.invoke(object);
					System.out.println(invoke);
				}
			}
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	//获取sql中传入参数名
	@Test
	public void testParamKey(){
		String str="select * from user  t where t.id=${id} and name=${name}";
		List<String> paramKeyList = new ArrayList<String>();
		String paramKey="";
		while (str.contains("${") && str.contains("}")) {
			int startIndex = str.indexOf("${");
			int endIndex = str.indexOf("}");
			paramKey = str.substring(startIndex + "${".length(), endIndex);
			paramKeyList.add(paramKey);
			if(endIndex<str.length())
				str=str.substring(endIndex+1);
		}
		System.out.println(paramKeyList);
	}
	
	@Test
	public void testUnlow() throws Exception {
		String str="getAge";
//		String substring = str.substring(3);
//		String substring2 = substring.substring(0, 1);
//		System.out.println(substring2.toLowerCase()+substring.substring(1));
		System.out.println(str.substring(3, 4).toLowerCase()+str.substring(4));
	}
	
	public String getAttrValue(Node node, String name) {
		Node namedItem = node.getAttributes().getNamedItem(name);
		if (namedItem == null) {
			return null;
		}
		return namedItem.getNodeValue();
	}

	/**
	 * 解析mapper文件
	 */
	public void parserMapper(String path) throws Exception {
		logger.debug("=========parserMapper============start");

		DocumentBuilder db = factory.newDocumentBuilder();
		db.setEntityResolver(new XMLConfigEntityResolverTest());
		InputStream resourceAsStream = getClass().getClassLoader()
				.getResourceAsStream(path);
		Document document = db.parse(resourceAsStream);
		Node mapper = (Node) xPath.evaluate("/mapper", document,
				XPathConstants.NODE);
		String nameSpace = mapper.getAttributes().getNamedItem("namespace")
				.getNodeValue();

		parserSelectStatement(mapper);

		System.out.println(nameSpace);
		logger.debug("=========parserMapper============end");

	}

	public void parserSelectStatement(Node node)
			throws XPathExpressionException {
		Node selectNode = (Node) xPath.evaluate("select", node,
				XPathConstants.NODE);

		String id = selectNode.getAttributes().getNamedItem("id").getNodeName();
		String parameterType = selectNode.getAttributes()
				.getNamedItem("parameterType").getNodeName();
		String resultType = selectNode.getAttributes()
				.getNamedItem("resultType").getNodeName();
		System.out.println(id + "--" + parameterType + "--" + resultType);

	}

	public void parserProperties(Node configurationNode) throws Exception {
		logger.debug("=========parserProperties============  start");
		Node propertiesNode = (Node) xPath.evaluate("properties",
				configurationNode, XPathConstants.NODE);

		Properties properties = new Properties();
		if (propertiesNode.hasChildNodes()) {
			NodeList childNodes = propertiesNode.getChildNodes();
			for (int i = 0; i < childNodes.getLength(); i++) {
				Node item = childNodes.item(i);
				if (item.getNodeType() == Node.ELEMENT_NODE) {
					String name = item.getAttributes().getNamedItem("name")
							.getNodeValue();
					String value = item.getAttributes().getNamedItem("value")
							.getNodeValue();
					properties.put(name, value);
				}
			}
		}

		NamedNodeMap attributes = propertiesNode.getAttributes();
		Node namedItem = attributes.getNamedItem("resource");
		String nodeValue = namedItem.getNodeValue();
		print(nodeValue);

		InputStream resourceAsStream = getClass().getClassLoader()
				.getResourceAsStream("datasource.properties");
		Properties properties2 = new Properties();
		properties2.load(resourceAsStream);
		properties.putAll(properties2);

		print(properties.toString());
		config.setSetProperties(properties);

		resourceAsStream.close();
		logger.debug("=========parserProperties============  end");

	}

	public void parserSettings(Node configurationNode) throws Exception {
		logger.debug("=========parserSettings=================start");
		Node settingsNode = (Node) xPath.evaluate("settings",
				configurationNode, XPathConstants.NODE);
		logger.debug(settingsNode.getNodeName());

		NodeList childNodes = settingsNode.getChildNodes();
		Map<String, String> setMap = new HashMap<String, String>();
		for (int i = 0; i < childNodes.getLength(); i++) {
			Node item = childNodes.item(i);
			if (item.getNodeType() == Node.ELEMENT_NODE) {
				String name = item.getAttributes().getNamedItem("name")
						.getNodeValue();
				String value = item.getAttributes().getNamedItem("value")
						.getNodeValue();
				setMap.put(name, value);
			}
		}
		logger.debug(setMap);
		config.setSetMap(setMap);
		logger.debug("=========parserSettings=================end");

	}

	public void parserEnvironments(Node configurationNode) throws Exception {
		logger.debug("=========parserEnvironments===============start");
		Node environmentsNode = (Node) xPath.evaluate("environments",
				configurationNode, XPathConstants.NODE);

		NodeList childNodes = environmentsNode.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {
			Node item = childNodes.item(i);

			if (item.getNodeType() == Node.ELEMENT_NODE) {
				String id = item.getAttributes().getNamedItem("id")
						.getNodeValue();
				Environment environment = new Environment();
				Node transactionManager = (Node) xPath.evaluate(
						"transactionManager", item, XPathConstants.NODE);
				String nodeValue = transactionManager.getAttributes()
						.getNamedItem("autoCmit").getNodeValue();
				environment.setAutoCmit(Boolean.parseBoolean(nodeValue));

				Node dataSource = (Node) xPath.evaluate("dataSource", item,
						XPathConstants.NODE);
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
					String name = item.getAttributes().getNamedItem("name")
							.getNodeValue();
					String value = item.getAttributes().getNamedItem("value")
							.getNodeValue();
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

	@Test
	public void testName() throws Exception {
		InputStream resourceAsStream = getClass().getClassLoader()
				.getResourceAsStream("com/six/mydb/UserMapper.xml");
		System.out.println(resourceAsStream);
	}

	public Config getConfig() {
		return config;
	}

	public void print(String msg) {
		System.out.println(msg);
	}

}
