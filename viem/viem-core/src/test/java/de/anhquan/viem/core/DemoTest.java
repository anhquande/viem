package de.anhquan.viem.core;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import com.google.common.primitives.Ints;

public class DemoTest {

	@Test
	public void testDemo(){
		
		String str = "-1 ";
		Integer digit = Ints.tryParse((StringUtils.trimToEmpty(str)));
		System.out.println(digit);
	}
}
