package com.roiland.platform.redis.replicator.rdb;

import com.roiland.platform.redis.replicator.Constants;
import com.roiland.platform.redis.replicator.Replicator;
import com.roiland.platform.redis.replicator.io.RedisInputStream;
import com.roiland.platform.redis.replicator.rdb.datatype.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.*;

/**
 * Created by leon on 8/20/16.
 */
public class Rdb7Parser extends AbstractRdbParser {

    public Rdb7Parser(RedisInputStream in, Replicator replicator) {
        super(in, replicator);
    }

    /**
     * ----------------------------# RDB is a binary format. There are no new lines or spaces in the file.
     * 52 45 44 49 53              # Magic String "REDIS"
     * 30 30 30 33                 # RDB Version Number in big endian. In this case, version = 0003 = 3
     * ----------------------------
     * FE 00                       # FE = code that indicates database selector. db number = 00
     * ----------------------------# Key-Value pair starts
     * FD $unsigned int            # FD indicates "expiry time in seconds". After that, expiry time is read as a 4 byte unsigned int
     * $value-type                 # 1 byte flag indicating the type of value - set, map, sorted set etc.
     * $string-encoded-name         # The name, encoded as a redis string
     * $encoded-value              # The value. Encoding depends on $value-type
     * ----------------------------
     * FC $unsigned long           # FC indicates "expiry time in ms". After that, expiry time is read as a 8 byte unsigned long
     * $value-type                 # 1 byte flag indicating the type of value - set, map, sorted set etc.
     * $string-encoded-name         # The name, encoded as a redis string
     * $encoded-value              # The value. Encoding depends on $value-type
     * ----------------------------
     * $value-type                 # This name value pair doesn't have an expiry. $value_type guaranteed != to FD, FC, FE and FF
     * $string-encoded-name
     * $encoded-value
     * ----------------------------
     * FE $length-encoding         # Previous db ends, next db starts. Database number read using length encoding.
     * ----------------------------
     * ...                         # Key value pairs for this database, additonal database
     * FF                          ## End of RDB file indicator
     * 8 byte checksum             ## CRC 64 checksum of the entire file.
     *
     * @return read bytes
     * @throws IOException
     */
    public long parse() throws IOException {
        /*
         * ----------------------------
         * 52 45 44 49 53              # Magic String "REDIS"
         * 30 30 30 33                 # RDB Version Number in big endian. In this case, version = 0003 = 3
         * ----------------------------
         */
        String magicString = AbstractRdbParser.StringHelper.str(in, 5);//REDIS
        if (!magicString.equals("REDIS")) {
            logger.error("Can't read MAGIC STRING [REDIS] ,value:" + magicString);
            return in.total();
        }
        int version = Integer.parseInt(AbstractRdbParser.StringHelper.str(in, 4));//0006
        if (version < 1 || version > 7) {
            logger.error("Can't handle RDB format version " + version);
            return in.total();
        }
        return rdbLoad();
    }

    protected long rdbLoad() throws IOException {
        Db db = null;
        /**
         * rdb
         */
        loop:
        while (true) {
            int type = in.read();
            KeyValuePair kv = null;
            switch (type) {
                /*
                 * ----------------------------
                 * FD $unsigned int            # FD indicates "expiry time in seconds". After that, expiry time is read as a 4 byte unsigned int
                 * $value-type                 # 1 byte flag indicating the type of value - set, map, sorted set etc.
                 * $string-encoded-name         # The name, encoded as a redis string
                 * $encoded-value              # The value. Encoding depends on $value-type
                 * ----------------------------
                 */
                case Constants.REDIS_RDB_OPCODE_EXPIRETIME:
                    int expiredSec = rdbLoadTime();
                    int valueType = in.read();
                    String key = rdbLoadEncodedStringObject();
                    kv = rdbLoadObject(valueType);
                    kv.setDb(db);
                    kv.setExpiredSeconds(expiredSec);
                    kv.setKey(key);
                    break;
                /*
                 * ----------------------------
                 * FC $unsigned long           # FC indicates "expiry time in ms". After that, expiry time is read as a 8 byte unsigned long
                 * $value-type                 # 1 byte flag indicating the type of value - set, map, sorted set etc.
                 * $string-encoded-name         # The name, encoded as a redis string
                 * $encoded-value              # The value. Encoding depends on $value-type
                 * ----------------------------
                 */
                case Constants.REDIS_RDB_OPCODE_EXPIRETIME_MS:
                    long expiredMs = rdbLoadMillisecondTime();
                    valueType = in.read();
                    key = rdbLoadEncodedStringObject();
                    kv = rdbLoadObject(valueType);
                    kv.setDb(db);
                    kv.setExpiredMs(expiredMs);
                    kv.setKey(key);
                    break;
                case Constants.REDIS_RDB_OPCODE_AUX:
                    String auxKey = rdbLoadEncodedStringObject();
                    String auxValue = rdbLoadEncodedStringObject();
                    if (auxKey.startsWith("%")) {
                        logger.info("RDB " + auxKey + ": " + auxValue);
                    } else {
                        logger.debug("Unrecognized RDB AUX field: " + auxKey + ",value: " + auxValue);
                    }
                    break;
                case Constants.REDIS_RDB_OPCODE_RESIZEDB:
                    int dbsize = rdbLoadLen().len;
                    int expiresSize = rdbLoadLen().len;
                    db.setDbsize(dbsize);
                    db.setExpires(expiresSize);
                    break;
                /*
                 * ----------------------------
                 * $value-type                 # This name value pair doesn't have an expiry. $value_type guaranteed != to FD, FC, FE and FF
                 * $string-encoded-name
                 * $encoded-value
                 * ----------------------------
                 */
                case Constants.REDIS_RDB_TYPE_STRING:
                case Constants.REDIS_RDB_TYPE_LIST:
                case Constants.REDIS_RDB_TYPE_SET:
                case Constants.REDIS_RDB_TYPE_ZSET:
                case Constants.REDIS_RDB_TYPE_HASH:
                case Constants.REDIS_RDB_TYPE_HASH_ZIPMAP:
                case Constants.REDIS_RDB_TYPE_LIST_ZIPLIST:
                case Constants.REDIS_RDB_TYPE_SET_INTSET:
                case Constants.REDIS_RDB_TYPE_ZSET_ZIPLIST:
                case Constants.REDIS_RDB_TYPE_HASH_ZIPLIST:
                case Constants.REDIS_RDB_TYPE_LIST_QUICKLIST:
                    valueType = type;
                    key = rdbLoadEncodedStringObject();
                    kv = rdbLoadObject(valueType);
                    kv.setDb(db);
                    kv.setKey(key);
                    break;
                /*
                 * ----------------------------
                 * FE $length-encoding         # Previous db ends, next db starts. Database number read using length encoding.
                 * ----------------------------
                 */
                case Constants.REDIS_RDB_OPCODE_SELECTDB:
                    int dbNumber = rdbLoadLen().len;
                    db = new Db(dbNumber);
                    break;
                /*
                 * ----------------------------
                 * ...                         # Key value pairs for this database, additonal database
                 * FF                          ## End of RDB file indicator
                 * 8 byte checksum             ## CRC 64 checksum of the entire file.
                 * ----------------------------
                 */
                case Constants.REDIS_RDB_OPCODE_EOF:
                    byte[] checksum = in.readBytes(8);
                    break loop;
                default:
                    throw new AssertionError("Un-except value-type:" + type);
            }
            if (kv == null) continue;
            if (logger.isDebugEnabled()) logger.debug(kv);
            if (!replicator.doRdbFilter(kv)) continue;
            replicator.doRdbHandler(kv);
        }
        return in.total();
    }

    private KeyValuePair rdbLoadObject(int rdbtype) throws IOException {
        switch (rdbtype) {
            /*
             * |       <content>       |
             * |    string contents    |
             */
            case Constants.REDIS_RDB_TYPE_STRING:
                KeyStringValueString o0 = new KeyStringValueString();
                String val = rdbLoadEncodedStringObject();
                o0.setValueRdbType(rdbtype);
                o0.setValue(val);
                return o0;
            /*
             * |    <len>     |       <content>       |
             * | 1 or 5 bytes |    string contents    |
             */
            case Constants.REDIS_RDB_TYPE_LIST:
                int len = rdbLoadLen().len;
                KeyStringValueList<String> o1 = new KeyStringValueList<>();
                List<String> list = new ArrayList<>();
                for (int i = 0; i < len; i++) {
                    String element = rdbLoadEncodedStringObject();
                    list.add(element);
                }
                o1.setValueRdbType(rdbtype);
                o1.setValue(list);
                return o1;
            /*
             * |    <len>     |       <content>       |
             * | 1 or 5 bytes |    string contents    |
             */
            case Constants.REDIS_RDB_TYPE_SET:
                len = rdbLoadLen().len;
                KeyStringValueSet o2 = new KeyStringValueSet();
                Set<String> set = new LinkedHashSet<>();
                for (int i = 0; i < len; i++) {
                    String element = rdbLoadEncodedStringObject();
                    set.add(element);
                }
                o2.setValueRdbType(rdbtype);
                o2.setValue(set);
                return o2;
            /*
             * |    <len>     |       <content>       |        <score>       |
             * | 1 or 5 bytes |    string contents    |    double content    |
             */
            case Constants.REDIS_RDB_TYPE_ZSET:
                len = rdbLoadLen().len;
                KeyStringValueZSet o3 = new KeyStringValueZSet();
                Set<ZSetEntry> zset = new LinkedHashSet<>();
                while (len > 0) {
                    String element = rdbLoadEncodedStringObject();
                    double score = rdbLoadDoubleValue();
                    zset.add(new ZSetEntry(element, score));
                    len--;
                }
                o3.setValueRdbType(rdbtype);
                o3.setValue(zset);
                return o3;
            /*
             * |    <len>     |       <content>       |
             * | 1 or 5 bytes |    string contents    |
             */
            case Constants.REDIS_RDB_TYPE_HASH:
                len = rdbLoadLen().len;
                KeyStringValueHash o4 = new KeyStringValueHash();
                Map<String, String> map = new LinkedHashMap<>();
                while (len > 0) {
                    String field = rdbLoadEncodedStringObject();
                    String value = rdbLoadEncodedStringObject();
                    map.put(field, value);
                    len--;
                }
                o4.setValueRdbType(rdbtype);
                o4.setValue(map);
                return o4;
            /*
             * |<zmlen> |   <len>     |"foo"    |    <len>   | <free> |   "bar" |<zmend> |
             * | 1 byte | 1 or 5 byte | content |1 or 5 byte | 1 byte | content | 1 byte |
             */
            case Constants.REDIS_RDB_TYPE_HASH_ZIPMAP:
                byte[] aux = rdbLoadRawStringObject();
                RedisInputStream stream = new RedisInputStream(new ByteArrayInputStream(aux));
                KeyStringValueHash o9 = new KeyStringValueHash();
                map = new LinkedHashMap<>();
                int zmlen = AbstractRdbParser.LenHelper.zmlen(stream);
                while (true) {
                    int zmEleLen = AbstractRdbParser.LenHelper.zmElementLen(stream);
                    if (zmEleLen == -1) {
                        break;
                    }
                    String field = AbstractRdbParser.StringHelper.str(stream, zmEleLen);
                    zmEleLen = AbstractRdbParser.LenHelper.zmElementLen(stream);
                    int free = AbstractRdbParser.LenHelper.free(stream);
                    String value = AbstractRdbParser.StringHelper.str(stream, zmEleLen);
                    AbstractRdbParser.StringHelper.skip(stream, free);
                    map.put(field, value);
                }
                int zmend = AbstractRdbParser.LenHelper.zmend(stream);
                if (zmend != 255) {
                    throw new AssertionError("zmend expected 255 but " + zmend);
                }
                o9.setValueRdbType(rdbtype);
                o9.setValue(map);
                return o9;
            /*
             * |<encoding>| <length-of-contents>|              <contents>                           |
             * | 4 bytes  |            4 bytes  | 2 bytes lement| 4 bytes element | 8 bytes element |
             */
            case Constants.REDIS_RDB_TYPE_SET_INTSET:
                aux = rdbLoadRawStringObject();
                stream = new RedisInputStream(new ByteArrayInputStream(aux));
                KeyStringValueSet o11 = new KeyStringValueSet();
                set = new LinkedHashSet<>();
                int encoding = AbstractRdbParser.LenHelper.encoding(stream);
                int lenOfContent = AbstractRdbParser.LenHelper.lenOfContent(stream);
                for (int i = 0; i < lenOfContent; i++) {
                    switch (encoding) {
                        case 2:
                            set.add(String.valueOf(stream.readInt(2)));
                            break;
                        case 4:
                            set.add(String.valueOf(stream.readInt(4)));
                            break;
                        case 8:
                            set.add(String.valueOf(stream.readLong(8)));
                            break;
                        default:
                            throw new AssertionError("Expect encoding [2,4,8] but:" + encoding);
                    }
                }
                o11.setValueRdbType(rdbtype);
                o11.setValue(set);
                return o11;
            /*
             * |<zlbytes>| <zltail>| <zllen>| <entry> ...<entry> | <zlend>|
             * | 4 bytes | 4 bytes | 2bytes | zipListEntry ...   | 1byte  |
             */
            case Constants.REDIS_RDB_TYPE_LIST_ZIPLIST:
                aux = rdbLoadRawStringObject();
                stream = new RedisInputStream(new ByteArrayInputStream(aux));
                KeyStringValueList<String> o10 = new KeyStringValueList<>();
                list = new ArrayList<>();
                int zlbytes = AbstractRdbParser.LenHelper.zlbytes(stream);
                int zltail = AbstractRdbParser.LenHelper.zltail(stream);
                int zllen = AbstractRdbParser.LenHelper.zllen(stream);
                for (int i = 0; i < zllen; i++) {
                    list.add(AbstractRdbParser.StringHelper.zipListEntry(stream));
                }
                int zlend = AbstractRdbParser.LenHelper.zlend(stream);
                if (zlend != 255) {
                    throw new AssertionError("zlend expected 255 but " + zlend);
                }
                o10.setValueRdbType(rdbtype);
                o10.setValue(list);
                return o10;
            /*
             * |<zlbytes>| <zltail>| <zllen>| <entry> ...<entry> | <zlend>|
             * | 4 bytes | 4 bytes | 2bytes | zipListEntry ...   | 1byte  |
             */
            case Constants.REDIS_RDB_TYPE_ZSET_ZIPLIST:
                aux = rdbLoadRawStringObject();
                stream = new RedisInputStream(new ByteArrayInputStream(aux));
                KeyStringValueZSet o12 = new KeyStringValueZSet();
                zset = new LinkedHashSet<>();
                zlbytes = AbstractRdbParser.LenHelper.zlbytes(stream);
                zltail = AbstractRdbParser.LenHelper.zltail(stream);
                zllen = AbstractRdbParser.LenHelper.zllen(stream);
                while (zllen > 0) {
                    String element = AbstractRdbParser.StringHelper.zipListEntry(stream);
                    zllen--;
                    double score = Double.valueOf(AbstractRdbParser.StringHelper.zipListEntry(stream));
                    zllen--;
                    zset.add(new ZSetEntry(element, score));
                }
                zlend = AbstractRdbParser.LenHelper.zlend(stream);
                if (zlend != 255) {
                    throw new AssertionError("zlend expected 255 but " + zlend);
                }
                o12.setValueRdbType(rdbtype);
                o12.setValue(zset);
                return o12;
            /*
             * |<zlbytes>| <zltail>| <zllen>| <entry> ...<entry> | <zlend>|
             * | 4 bytes | 4 bytes | 2bytes | zipListEntry ...   | 1byte  |
             */
            case Constants.REDIS_RDB_TYPE_HASH_ZIPLIST:
                aux = rdbLoadRawStringObject();
                stream = new RedisInputStream(new ByteArrayInputStream(aux));
                KeyStringValueHash o13 = new KeyStringValueHash();
                map = new LinkedHashMap<>();
                zlbytes = AbstractRdbParser.LenHelper.zlbytes(stream);
                zltail = AbstractRdbParser.LenHelper.zltail(stream);
                zllen = AbstractRdbParser.LenHelper.zllen(stream);
                while (zllen > 0) {
                    String field = AbstractRdbParser.StringHelper.zipListEntry(stream);
                    zllen--;
                    String value = AbstractRdbParser.StringHelper.zipListEntry(stream);
                    zllen--;
                    map.put(field, value);
                }
                zlend = AbstractRdbParser.LenHelper.zlend(stream);
                if (zlend != 255) {
                    throw new AssertionError("zlend expected 255 but " + zlend);
                }
                o13.setValueRdbType(rdbtype);
                o13.setValue(map);
                return o13;
            /* rdb version 7*/
            case Constants.REDIS_RDB_TYPE_LIST_QUICKLIST:
                len = rdbLoadLen().len;
                KeyStringValueList<byte[]> o14 = new KeyStringValueList<>();
                List<byte[]> byteList = new ArrayList<>();
                for (int i = 0; i < len; i++) {
                    byte[] element = rdbLoadRawStringObject();
                    byteList.add(element);
                }
                o14.setValueRdbType(rdbtype);
                o14.setValue(byteList);
                return o14;
            default:
                throw new AssertionError("Un-except value-type:" + rdbtype);

        }
    }
}
