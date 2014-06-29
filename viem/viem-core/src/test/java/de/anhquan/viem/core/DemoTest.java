package de.anhquan.viem.core;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import com.google.appengine.labs.repackaged.com.google.common.primitives.Ints;
import com.google.common.base.CharMatcher;

public class DemoTest {

	@Test
	public void testDemo(){
		
		String str = "-1 ";
		Integer digit = Ints.tryParse((StringUtils.trimToEmpty(str)));
		System.out.println(digit);
	}
}
