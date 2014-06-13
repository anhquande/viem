package de.anhquan.viem.core;

import org.junit.Test;

public class DemoTest {

	@Test
	public void testDemo(){
		String str = "/product/*";
		str = str.replaceFirst("\\*", "109");
		System.out.println(str);
	}
}
