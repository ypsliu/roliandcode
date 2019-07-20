package com.roiland.platform.examples.temp;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: test
 * Date: 12-5-24
 * Time: 下午5:37
 * To change this template use File | Settings | File Templates.
 */
public class ConsistencyHash<T extends Object> {

    private TreeMap<Long, T> nodes = new TreeMap<>();
    private final int VIRTUAL_NUM = 4;                        // 虚拟节点
    private final Random random = new Random();

    public synchronized void add(T node) {
        final String key = node.toString();
        for (int j = 0; j < VIRTUAL_NUM; j++) {
            nodes.put(hash(compute(key + j), j), node);
        }
    }

    public synchronized void remove(T node) {
        final String key = node.toString();
        for (int j = 0; j < VIRTUAL_NUM; j++) {
            nodes.remove(hash(compute(key + j), j));
        }
    }

    /**
     * 根据key的hash值取得服务器节点信息
     *
     * @param key
     * @return
     */
    public T get(String key) {
        long node = hash(compute(key), random.nextInt(VIRTUAL_NUM));
        SortedMap<Long, T> tailMap = nodes.tailMap(node);
        if (tailMap.isEmpty()) {
            node = nodes.firstKey();
        } else {
            node = tailMap.firstKey();
        }
        return nodes.get(node);
    }

    /**
     * 根据2^32把节点分布到圆环上面。
     *
     * @param digest
     * @param nTime
     * @return
     */
    private long hash(byte[] digest, int nTime) {
        long rv = ((long) (digest[3 + nTime * 4] & 0xFF) << 24)
                | ((long) (digest[2 + nTime * 4] & 0xFF) << 16)
                | ((long) (digest[1 + nTime * 4] & 0xFF) << 8)
                | (digest[0 + nTime * 4] & 0xFF);

        return rv & 0xffffffffL; /* Truncate to 32-bits */
    }

    /**
     * Get the md5 of the given key.
     * 计算MD5值
     */
    private byte[] compute(String k) {
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 not supported", e);
        }
        md5.reset();
        byte[] keyBytes;
        try {
            keyBytes = k.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Unknown string :" + k, e);
        }

        md5.update(keyBytes);
        return md5.digest();
    }

    public static void main(String[] args) {
        ConsistencyHash<String> hash = new ConsistencyHash<>();
        hash.add("192.168.33.35:7000");
        hash.add("192.168.33.36:7000");
        hash.add("192.168.33.37:7000");
        hash.add("192.168.33.38:7000");

        for (int i = 0; i < 50; i++) {
            System.out.println("i=" + i + ", node:" + hash.get(String.valueOf(i)));
        }

        hash.remove("192.168.33.37:7000");
        for (int i = 0; i < 50; i++) {
            System.out.println("i=" + i + ", node:" + hash.get(String.valueOf(i)));
        }
    }
}
