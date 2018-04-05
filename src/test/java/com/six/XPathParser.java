package com.six;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.six.mydb.exceptions.MyDBExeceptions;

public class XPathParser {

	private Document document;
	private XPath xPath = XPathFactory.newInstance().newXPath();

	public Node evalNode(String expression, Object root) {
		Node node = null;
		try {
			node = (Node) xPath.evaluate(expression, root, XPathConstants.NODE);
		} catch (XPathExpressionException e) {
			throw new MyDBExeceptions("evalNode " + expression);
		}
		return node;
	}

}
