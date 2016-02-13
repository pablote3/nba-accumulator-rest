package com.rossotti.basketball.util;

import java.util.Properties;

import org.junit.Assert;
import org.junit.Test;

public class ResourceLoaderTest {
	private Properties properties = ResourceLoader.getProperties();

	@Test
	public void getResourceTest_Found() {
		Assert.assertNotNull(properties.getProperty("xmlstats.accessToken"));
		Assert.assertEquals("pablote/2.0 (rossotti.paul@gmail.com)", properties.getProperty("xmlstats.userAgent"));
	}

	@Test
	public void getResourceTest_PropertyNotFound() {
		Assert.assertNull(properties.getProperty("xmlstats.wrongToken"));
	}
}