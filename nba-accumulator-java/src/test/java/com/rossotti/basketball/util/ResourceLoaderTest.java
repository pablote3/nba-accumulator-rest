package com.rossotti.basketball.util;

import org.junit.Assert;
import org.junit.Test;

public class ResourceLoaderTest {

	@Test
	public void getResourceTest_Found() {
		Assert.assertNotNull(ResourceLoader.getInstance().getProperties().getProperty("xmlstats.accessToken"));
		Assert.assertEquals("pablote/2.0 (rossotti.paul@gmail.com)", ResourceLoader.getInstance().getProperties().getProperty("xmlstats.userAgent"));
	}

	@Test
	public void getResourceTest_PropertyNotFound() {
		Assert.assertNull(ResourceLoader.getInstance().getProperties().getProperty("xmlstats.wrongToken"));
	}
}