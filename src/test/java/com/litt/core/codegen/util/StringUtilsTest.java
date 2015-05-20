package com.litt.core.codegen.util;

import org.junit.Assert;
import org.junit.Test;

public class StringUtilsTest {

	@Test
	public void test_toUnicode()
	{
		String text = "中文";
		String ret = StringUtils.toUnicode(text);
		
		Assert.assertEquals("\\u4e2d\\u6587", ret);
	}
	
}
