package com.roiland.platform.lang;

import org.junit.Assert;
import org.junit.Test;

public class IPUtilsTest {

	@Test
	public void testIsIP() {
		Assert.assertTrue(IPUtils.isIp("192.168.33.70"));
		// 位数大于4
		Assert.assertFalse(IPUtils.isIp("92.18.35.70.21"));
		// 位数大于255
		Assert.assertFalse(IPUtils.isIp("1920.18.35.70"));
	}

}
