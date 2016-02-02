package com.rossotti.basketball.util;

import java.io.IOException;
import java.util.Properties;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:applicationContext.xml"})
public class ResourceLoaderTest {
	@Autowired
	private CustomResourceLoader resourceLoader;

	@Test
	public void getResourceTest() {
		String accessToken = null;
		try {
			Properties properties = resourceLoader.getProperties();
			accessToken = properties.getProperty("xmlstats.accessToken");
		} catch (IOException e) {
			e.printStackTrace();
		}
		Assert.assertEquals("testToken", accessToken);
	}
}
