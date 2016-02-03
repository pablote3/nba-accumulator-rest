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
	private PropertyResourceLoader resourceLoader;

	@Test
	public void getResourceTest_Found() throws IOException {
		Properties properties = resourceLoader.getProperties();
		Assert.assertEquals("testToken", properties.getProperty("xmlstats.accessToken"));
		Assert.assertEquals("testAgent", properties.getProperty("xmlstats.userAgent"));
	}

	@Test
	public void getResourceTest_PropertyNotFound() throws IOException {
		Properties properties = resourceLoader.getProperties();
		properties.getProperty("xmlstats.accessToken");
	}
}
