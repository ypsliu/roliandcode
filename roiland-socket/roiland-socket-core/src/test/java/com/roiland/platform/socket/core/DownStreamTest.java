package com.roiland.platform.socket.core;

/**
 * Created by jeffy.yang on 16-6-27.
 */
public class DownStreamTest {

    public static void main(String[] args) {

        long start = System.currentTimeMillis();
        for (int i = 0; i < 50000; i++) {
            new DownStream("AW9E011000R250013", "83eebee7811847a7ad67f2f00d0a325b", 0, 0x08);
        }
        long end = System.currentTimeMillis();
        System.out.println("time:" + (end - start) + " total:" + 50000);

        for (int i = 0; i < 500000; i++) {
            new DownStream("AW9E011000R250013", "83eebee7811847a7ad67f2f00d0a325b", 0, 0x08);
        }
        long end2 = System.currentTimeMillis();
        System.out.println("time:" + (end2 - end) + " total:" + 500000);
    }
}
