package com.roiland.platform.redis.replicator.rdb;

import com.roiland.platform.redis.replicator.Constants;
import com.roiland.platform.redis.replicator.Replicator;
import com.roiland.platform.redis.replicator.io.RedisInputStream;
import com.roiland.platform.redis.replicator.util.Lzf;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;

/**
 * Created by leon on 8/20/16.
 */
public abstract class AbstractRdbParser {
    protected static final Log logger = LogFactory.getLog(RdbParser.class);

    protected final RedisInputStream in;

    protected final Replicator replicator;

    public AbstractRdbParser(RedisInputStream in, Replicator replicator) {
        this.in = in;
        this.replicator = replicator;
    }

    protected long rdbLoad() throws IOException {
        throw new UnsupportedOperationException("rdbLoad()");
    }

    /**
     * "expiry time in seconds". After that, expiry time is read as a 4 byte unsigned int
     *
     * @return seconds
     * @throws IOException
     */
    protected int rdbLoadTime() throws IOException {
        return in.readInt(4);
    }

    /**
     * "expiry time in ms". After that, expiry time is read as a 8 byte unsigned long
     *
     * @return millisecond
     * @throws IOException
     */
    protected long rdbLoadMillisecondTime() throws IOException {
        return in.readLong(8);
    }

    /**
     * read bytes 1 or 2 or 5
     * 1. |00xxxxxx| remaining 6 bits represent the length
     * 2. |01xxxxxx|xxxxxxxx| the combined 14 bits represent the length
     * 3. |10xxxxxx|xxxxxxxx|xxxxxxxx|xxxxxxxx|xxxxxxxx| the remaining 6 bits are discarded.Additional 4 bytes represent the length(big endian in version6)
     * 4. |11xxxxxx| the remaining 6 bits are read.and then the next object is encoded in a special format.so we set isencoded = true
     *
     * @return tuple(len, isencoded)
     * @throws IOException
     * @see #rdbLoadIntegerObject
     * @see #rdbLoadLzfStringObject
     */
    protected Len rdbLoadLen() throws IOException {
        boolean isencoded = false;
        int rawByte = in.read();
        int type = (rawByte & 0xc0) >> 6;
        int value;
        switch (type) {
            case Constants.REDIS_RDB_ENCVAL:
                isencoded = true;
                value = rawByte & 0x3f;
                break;
            case Constants.REDIS_RDB_6BITLEN:
                value = rawByte & 0x3f;
                break;
            case Constants.REDIS_RDB_14BITLEN:
                value = ((rawByte & 0x3F) << 8) | in.read();
                break;
            case Constants.REDIS_RDB_32BITLEN:
                value = in.readInt(4, false);
                break;
            default:
                throw new AssertionError("Un-except len-type:" + type);

        }
        return new Len(value, isencoded);
    }

    /**
     * @param enctype 0,1,2
     * @param encode  true: encoded string.false:raw bytes
     * @return String rdb object
     * @throws IOException
     */
    protected Object rdbLoadIntegerObject(int enctype, boolean encode) throws IOException {
        byte[] value;
        switch (enctype) {
            case Constants.REDIS_RDB_ENC_INT8:
                value = in.readBytes(1);
                break;
            case Constants.REDIS_RDB_ENC_INT16:
                value = in.readBytes(2);
                break;
            case Constants.REDIS_RDB_ENC_INT32:
                value = in.readBytes(4);
                break;
            default:
                value = new byte[]{0x00};
                break;
        }
        return encode ? String.valueOf(in.readInt(value)) : value;
    }

    /**
     * |11xxxxxx| remaining 6bit is 3,then lzf compressed string follows
     * lzf format
     * |lzf flag|clen:1 or 2 or 5 bytes|len:1 or 2 or 5 bytes |       lzf compressed bytes           |
     * |11xxxxxx|xxxxxxxx|....|xxxxxxxx|xxxxxxxx|....|xxxxxxxx|xxxxxxxx|xxxxxxxx|............xxxxxxxx|
     *
     * @param encode true: encoded string.false:raw bytes
     * @return String rdb object
     * @throws IOException
     * @see #rdbLoadLen
     */
    protected Object rdbLoadLzfStringObject(boolean encode) throws IOException {
        int clen = rdbLoadLen().len;
        int len = rdbLoadLen().len;
        byte[] inBytes = in.readBytes(clen);
        byte[] outBytes = Lzf.decode(inBytes, len);
        return encode ? new String(outBytes, "UTF-8") : outBytes;
    }

    /**
     * 1.|11xxxxxx|xxxxxxxx| remaining 6bit is 0, then an 8 bit integer follows
     * 2.|11xxxxxx|xxxxxxxx|xxxxxxxx| remaining 6bit is 1, then an 16 bit integer follows
     * 3.|11xxxxxx|xxxxxxxx|xxxxxxxx|xxxxxxxx|xxxxxxxx| remaining 6bit is 2, then an 32 bit integer follows
     * 4.|11xxxxxx| remaining 6bit is 3,then lzf compressed string follows
     *
     * @param encode true: encoded string.false:raw bytes
     * @return String rdb object
     * @throws IOException
     * @see #rdbLoadIntegerObject
     * @see #rdbLoadLzfStringObject
     */
    protected Object rdbGenericLoadStringObject(boolean encode) throws IOException {
        Len lenObj = rdbLoadLen();
        int len = lenObj.len;
        boolean isencoded = lenObj.isencoded;
        if (isencoded) {
            switch (len) {
                case Constants.REDIS_RDB_ENC_INT8:
                case Constants.REDIS_RDB_ENC_INT16:
                case Constants.REDIS_RDB_ENC_INT32:
                    return rdbLoadIntegerObject(len, encode);
                case Constants.REDIS_RDB_ENC_LZF:
                    return rdbLoadLzfStringObject(encode);
                default:
                    throw new AssertionError("Unknown RdbParser encoding type:" + len);
            }
        }
        return encode ? StringHelper.str(in, len) : in.readBytes(len);
    }

    /**
     * @return String rdb object with raw bytes
     * @throws IOException
     */
    protected byte[] rdbLoadRawStringObject() throws IOException {
        return (byte[]) rdbGenericLoadStringObject(false);
    }

    /**
     * @return String rdb object with UTF-8 string
     * @throws IOException
     */
    protected String rdbLoadEncodedStringObject() throws IOException {
        return (String) rdbGenericLoadStringObject(true);
    }

    protected double rdbLoadDoubleValue() throws IOException {
        int len = in.read();
        switch (len) {
            case 255:
                return Double.NEGATIVE_INFINITY;
            case 254:
                return Double.POSITIVE_INFINITY;
            case 253:
                return Double.NaN;
            default:
                byte[] bytes = in.readBytes(len);
                return Double.valueOf(new String(bytes));
        }
    }

    /**
     * @see #rdbLoadLen
     */
    protected static class Len {
        public final int len;
        public final boolean isencoded;

        private Len(int len, boolean isencoded) {
            this.len = len;
            this.isencoded = isencoded;
        }
    }

    protected static class StringHelper {
        private StringHelper() {
        }

        public static String str(RedisInputStream in, int len) throws IOException {
            return in.readString(len);
        }

        public static long skip(RedisInputStream in, long len) throws IOException {
            return in.skip(len);
        }

        /**
         * <length-prev-entry><special-flag><raw-bytes-of-entry>
         * <length-prev-entry> format:
         * |xxxxxxxx| if first byte value < 254. then 1 byte as prev len.
         * |xxxxxxxx|xxxxxxxx|xxxxxxxx|xxxxxxxx|xxxxxxxx| if first byte >=254 then next 4 byte as prev len.
         * <p>
         * <special-flag>:
         * |00xxxxxx| remaining 6 bit as string len.
         * |01xxxxxx|xxxxxxxx| combined 14 bit as string len.
         * |10xxxxxx|xxxxxxxx|xxxxxxxx|xxxxxxxx|xxxxxxxx| next 4 byte as string len.
         * <p>
         * |11111110|xxxxxxxx| next 1 byte as 8bit int
         * |11000000|xxxxxxxx|xxxxxxxx| next 2 bytes as 16bit int
         * |11110000|xxxxxxxx|xxxxxxxx|xxxxxxxx| next 3 bytes as 24bit int
         * |11010000|xxxxxxxx|xxxxxxxx|xxxxxxxx|xxxxxxxx| next 4 bytes as 32bit int
         * |11100000|xxxxxxxx|xxxxxxxx|xxxxxxxx|xxxxxxxx|xxxxxxxx|xxxxxxxx|xxxxxxxx|xxxxxxxx| next 8 bytes as 64bit long
         * |11xxxxxx| next 6 bit value as int value
         *
         * @param in ziplist raw bytes stream
         * @return encoded string
         * @throws IOException
         */
        public static String zipListEntry(RedisInputStream in) throws IOException {
            int prevlen = in.read();
            if (prevlen >= 254) {
                prevlen = in.readInt(4);
            }
            int special = in.read();
            switch (special >> 6) {
                case 0:
                    int len = special & 0x3f;
                    return StringHelper.str(in, len);
                case 1:
                    len = ((special & 0x3f) << 8) | in.read();
                    return StringHelper.str(in, len);
                case 2:
                    //bigEndian
                    len = in.readInt(4, false);
                    return StringHelper.str(in, len);
                default:
                    break;
            }
            switch (special) {
                case Constants.ZIP_INT_8B:
                    return String.valueOf(in.readInt(1));
                case Constants.ZIP_INT_16B:
                    return String.valueOf(in.readInt(2));
                case Constants.ZIP_INT_24B:
                    return String.valueOf(in.readInt(3));
                case Constants.ZIP_INT_32B:
                    return String.valueOf(in.readInt(4));
                case Constants.ZIP_INT_64B:
                    return String.valueOf(in.readLong(8));
                default:
                    //6BIT
                    return String.valueOf(special - 0xf1);
            }
        }
    }

    protected static class LenHelper {
        private LenHelper() {
        }

        //zip hash
        public static int zmlen(RedisInputStream in) throws IOException {
            return in.read();
        }

        public static int zmend(RedisInputStream in) throws IOException {
            return in.read();
        }

        public static int free(RedisInputStream in) throws IOException {
            return in.read();
        }

        public static int zmElementLen(RedisInputStream in) throws IOException {
            int len = in.read();
            if (len >= 0 && len <= 253) {
                return len;
            } else if (len == 254) {
                return in.readInt(4, false);
            } else {
                return -1;
            }
        }

        //zip list
        public static int zlbytes(RedisInputStream in) throws IOException {
            return in.readInt(4);
        }

        public static int zlend(RedisInputStream in) throws IOException {
            return in.read();
        }

        public static int zltail(RedisInputStream in) throws IOException {
            return in.readInt(4);
        }

        public static int zllen(RedisInputStream in) throws IOException {
            return in.readInt(2);
        }

        //int set
        public static int encoding(RedisInputStream in) throws IOException {
            return in.readInt(4);
        }

        public static int lenOfContent(RedisInputStream in) throws IOException {
            return in.readInt(4);
        }
    }
}
