package com.roiland.platform.lang;

import java.security.SecureRandom;
import java.util.UUID;

/**
 * UUID处理类
 *
 * @author leon.chen
 * @see [https://en.wikipedia.org/wiki/Universally_unique_identifier]
 * @see [http://www.ietf.org/rfc/rfc4122.txt]
 * @since 2016/3/25
 */
public class UUIDUtils {

    private static final SecureRandom numberGenerator = new SecureRandom();

    /**
     * 0x00 version 0 reserved
     * 0x10 version 1 date-time & MAC address (mysql default)
     * 0x20 version 2 DCE Security
     * 0x30 version 3 MD5 hash & namespace (java)
     * 0x40 version 4 random (java)
     * 0x50 version 5 SHA-1 hash & namespace
     * 0x60 version 6 user-defined
     * 0x70 version 7 user-defined
     * 0x80 version 8 user-defined
     * 0x90 version 9 user-defined
     * 0xa0 version a user-defined
     * 0xb0 version b user-defined
     * 0xc0 version c user-defined
     * 0xd0 version d user-defined
     * 0xe0 version e user-defined
     * 0xf0 version f user-defined
     *
     * @param version uuid version
     * @return
     */
    public static UUID randomUUID(byte version) {
        byte[] randomBytes = new byte[16];
        numberGenerator.nextBytes(randomBytes);
        randomBytes[6] &= 0x0f;     /* clear version        */
        randomBytes[6] |= version;  /* set to version       */
        randomBytes[8] &= 0x3f;     /* clear variant        */
        randomBytes[8] |= 0x80;     /* set to IETF variant  */
        return uuid(randomBytes);
    }

    /**
     * version 4
     *
     * @return
     */
    public static UUID randomUUID() {
        return UUID.randomUUID();
    }

    /**
     * version 3
     *
     * @param name
     * @return
     */
    public static UUID nameUUIDFromBytes(byte[] name) {
        return UUID.nameUUIDFromBytes(name);
    }

    public static UUID fromString(String name) {
        return UUID.fromString(name);
    }

    public static String toString(UUID uuid) {
        final long mostSigBits = uuid.getMostSignificantBits();
        final long leastSigBits = uuid.getLeastSignificantBits();
        return digits(mostSigBits >> 32, 8) + digits(mostSigBits >> 16, 4) + digits(mostSigBits, 4) + digits(leastSigBits >> 48, 4) + digits(leastSigBits, 12);
    }

    private static String digits(long val, int digits) {
        long hi = 1L << (digits * 4);
        return Long.toHexString(hi | (val & (hi - 1))).substring(1);
    }

    private static UUID uuid(long mostSigBits, long leastSigBits) {
        return new UUID(mostSigBits, leastSigBits);
    }

    private static UUID uuid(byte[] data) {
        long msb = 0;
        long lsb = 0;
        assert data.length == 16 : "data must be 16 bytes in length";
        for (int i = 0; i < 8; i++)
            msb = (msb << 8) | (data[i] & 0xff);
        for (int i = 8; i < 16; i++)
            lsb = (lsb << 8) | (data[i] & 0xff);
        return new UUID(msb, lsb);
    }
}
