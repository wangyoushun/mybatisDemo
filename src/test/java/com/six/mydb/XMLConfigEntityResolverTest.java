package com.six.mydb;

import java.io.IOException;
import java.io.InputStream;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * 重写解析 将远程dtd校验转为本地校验
 */
public class XMLConfigEntityResolverTest implements EntityResolver {

	@Override
	public InputSource resolveEntity(String publicId, String systemId)
			throws SAXException, IOException {

		System.out.println("XMLConfigEntityResolver=====");
		String path = getClass().getResource("").getPath();
		systemId="file://"+path+systemId.substring(systemId.lastIndexOf("/")+1);
		InputStream resourceAsStream = getClass().getClassLoader()
				.getResourceAsStream(systemId);
		InputSource inputSource = new InputSource(resourceAsStream);
		inputSource.setSystemId(systemId);
		return inputSource;
	}

}
