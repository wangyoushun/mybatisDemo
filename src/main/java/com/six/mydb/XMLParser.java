package com.six.mydb;

import java.io.InputStream;
import java.util.HashMap;
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
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLParser {

	private XPath xPath;
	private Config config;
	private Logger logger = Logger.getLogger(getClass());
	private DocumentBuilderFactory factory = DocumentBuilderFactory
			.newInstance();

	public XMLParser() {
		this.config = new Config();
		this.xPath = XPathFactory.newInstance().newXPath();
	}

	public Config parserXml(String resources) throws Exception {
		factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = factory.newDocumentBuilder();
		db.setEntityResolver(new XMLConfigEntityResolverTest());

		// 解析dbconfig
		InputStream resourceAsStream = Resources.getResourceAsStream(resources);

		Document document = db.parse(resourceAsStream);
		Node configurationNode = (Node) xPath.evaluate("/configuration",
				document, XPathConstants.NODE);

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

		Node mappersNode = (Node) xPath.evaluate("mappers", configurationNode,
				XPathConstants.NODE);
		if (mappersNode.hasChildNodes()) {
			NodeList childNodes = mappersNode.getChildNodes();
			for (int i = 0; i < childNodes.getLength(); i++) {
				Node item = childNodes.item(i);
				if (item.getNodeType() == Node.ELEMENT_NODE) {
					String resource = item.getAttributes()
							.getNamedItem("resource").getNodeValue();
					parserMapper(resource);
				}
			}

		}
		logger.debug("=========parserMappers============end");
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

	/**
	 * 解析mapper文件中的标签
	 */
	public void parserSelectStatement(Node node) throws Exception {
		Node selectNode = (Node) xPath.evaluate("select", node,
				XPathConstants.NODE);
		parsesSelect(selectNode);
	}

	/**
	 * 解析mapper文件中select标签
	 */
	public void parsesSelect(Node mapper) throws Exception {
		NodeList selectNodes = (NodeList) xPath.evaluate("//select", mapper,
				XPathConstants.NODESET);
		Map<String, MapStatement> map = new HashMap<String, MapStatement>();

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
				mapStatement.setSqlStr(sql);
//				System.out.println(mapStatement);
				// 将解析数据放入map中， 判断map中
				map.put(id, mapStatement);
			}
		}
		config.setSqlMap(map);
		System.out.println(map);
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

	/**
	 * 解析config文件setting标签
	 */
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

	/**
	 * 解析config文件environment标签
	 */
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

	public Config getConfig() {
		return config;
	}

	@Test
	public void testName() throws Exception {
		InputStream resourceAsStream = getClass().getClassLoader()
				.getResourceAsStream("com/six/mydb/UserMapper.xml");
		System.out.println(resourceAsStream);
	}

	public void print(String msg) {
		System.out.println(msg);
	}

}
