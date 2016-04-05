package com.rossotti.basketball.util;

import org.junit.Assert;
import org.junit.Test;

public class FormatUtilTest {

	@Test
	public void padString() {
		Assert.assertEquals("Jump      ", FormatUtil.padString("Jump", 10));
	}
}
