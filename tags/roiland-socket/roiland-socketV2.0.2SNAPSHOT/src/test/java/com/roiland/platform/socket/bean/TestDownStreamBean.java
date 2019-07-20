package com.roiland.platform.socket.bean;

import org.junit.Test;

public class TestDownStreamBean {

	@Test
	public void testDownStreamBean() {
		DownStreamBean stream = new DownStreamBean("0", "0", 0, new byte[]{0x01, 0x01, 0x03, 0x02});
		System.out.println(stream.toString());
	}
}
