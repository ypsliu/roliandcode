/*
 * Copyright 2016 leon chen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.roiland.platform.redis.replicator;

/**
 * @author leon.chen
 * @since 2016/8/11
 */
public class Constants {

    /**
     * len type
     */
    public static final int REDIS_RDB_6BITLEN = 0;
    public static final int REDIS_RDB_14BITLEN = 1;
    public static final int REDIS_RDB_32BITLEN = 2;
    public static final int REDIS_RDB_ENCVAL = 3;

    /**
     * string encoding
     */
    public static final int REDIS_RDB_ENC_INT8 = 0;
    public static final int REDIS_RDB_ENC_INT16 = 1;
    public static final int REDIS_RDB_ENC_INT32 = 2;
    public static final int REDIS_RDB_ENC_LZF = 3;

    /**
     * rdb object encoding
     */
    public static final int REDIS_RDB_TYPE_STRING = 0;
    public static final int REDIS_RDB_TYPE_LIST = 1;
    public static final int REDIS_RDB_TYPE_SET = 2;
    public static final int REDIS_RDB_TYPE_ZSET = 3;
    public static final int REDIS_RDB_TYPE_HASH = 4;
    public static final int REDIS_RDB_TYPE_HASH_ZIPMAP = 9;
    public static final int REDIS_RDB_TYPE_LIST_ZIPLIST = 10;
    public static final int REDIS_RDB_TYPE_SET_INTSET = 11;
    public static final int REDIS_RDB_TYPE_ZSET_ZIPLIST = 12;
    public static final int REDIS_RDB_TYPE_HASH_ZIPLIST = 13;
    /* rdb version 7 start */
    public static final int REDIS_RDB_TYPE_LIST_QUICKLIST = 14;
    /* rdb version 7 end */

    /**
     * zip entry
     */
    public static final int ZIP_INT_8B = 0xfe; /*11111110*/
    public static final int ZIP_INT_16B = 0xc0 | 0 << 4; /* 11000000*/
    public static final int ZIP_INT_24B = 0xc0 | 3 << 4; /* 11110000*/
    public static final int ZIP_INT_32B = 0xc0 | 1 << 4; /* 11010000*/
    public static final int ZIP_INT_64B = 0xc0 | 2 << 4; /* 11100000*/

    /**
     * rdb protocol
     */
    /* rdb version 7 start */
    public static final int REDIS_RDB_OPCODE_AUX = 0xfa; /*250*/
    public static final int REDIS_RDB_OPCODE_RESIZEDB = 0xfb; /*251*/
    /* rdb version 7 end */
    public static final int REDIS_RDB_OPCODE_EXPIRETIME_MS = 0xfc;/* 252 */
    public static final int REDIS_RDB_OPCODE_EXPIRETIME = 0xfd; /* 253 */
    public static final int REDIS_RDB_OPCODE_SELECTDB = 0xfe; /* 254 */
    public static final int REDIS_RDB_OPCODE_EOF = 0xff; /* 255 */

    /**
     * transfer protocol
     */
    public static final byte DOLLAR = '$';
    public static final byte STAR = '*';
    public static final byte PLUS = '+';
    public static final byte MINUS = '-';
    public static final byte COLON = ':';

}
