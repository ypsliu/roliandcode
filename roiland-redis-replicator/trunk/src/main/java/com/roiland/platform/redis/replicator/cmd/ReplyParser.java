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

package com.roiland.platform.redis.replicator.cmd;

import com.roiland.platform.redis.replicator.io.RedisInputStream;
import com.roiland.platform.redis.replicator.util.ByteBuilder;

import java.io.IOException;

import static com.roiland.platform.redis.replicator.Constants.*;

/**
 * Created by leon on 8/13/16.
 */
public class ReplyParser {
    private final RedisInputStream in;

    public ReplyParser(RedisInputStream in) {
        this.in = in;
    }

    public Object parse() throws IOException {
        return parse(new BulkReplyHandler.SimpleBulkReplyHandler());
    }

    /**
     * @param handler
     * @return return Object[] or String or Long
     * @throws IOException
     */
    public Object parse(BulkReplyHandler handler) throws IOException {
        int c = in.read();
        switch (c) {
            case DOLLAR:
                //RESP Bulk Strings
                ByteBuilder builder = ByteBuilder.allocate(128);
                while (true) {
                    while ((c = in.read()) != '\r') {
                        builder.put((byte) c);
                    }
                    if ((c = in.read()) == '\n') {
                        break;
                    } else {
                        builder.put((byte) c);
                    }
                }
                long len = Long.parseLong(builder.toString());
                // $-1\r\n. this is called null string.
                // see http://redis.io/topics/protocol
                if (len == -1) return null;
                if (handler != null) return handler.handle(len, in);
                throw new AssertionError("callback is null");
            case COLON:
                // RESP Integers
                builder = ByteBuilder.allocate(128);
                while (true) {
                    while ((c = in.read()) != '\r') {
                        builder.put((byte) c);
                    }
                    if ((c = in.read()) == '\n') {
                        break;
                    } else {
                        builder.put((byte) c);
                    }
                }
                //as integer
                return Long.parseLong(builder.toString());
            case STAR:
                // RESP Arrays
                builder = ByteBuilder.allocate(128);
                while (true) {
                    while ((c = in.read()) != '\r') {
                        builder.put((byte) c);
                    }
                    if ((c = in.read()) == '\n') {
                        break;
                    } else {
                        builder.put((byte) c);
                    }
                }
                len = Long.parseLong(builder.toString());
                if (len == -1) return null;
                Object[] ary = new Object[(int) len];
                for (int i = 0; i < len; i++) {
                    Object obj = parse();
                    ary[i] = obj;
                }
                return ary;
            case PLUS:
                // RESP Simple Strings
                builder = ByteBuilder.allocate(128);
                while (true) {
                    while ((c = in.read()) != '\r') {
                        builder.put((byte) c);
                    }
                    if ((c = in.read()) == '\n') {
                        return builder.toString();
                    } else {
                        builder.put((byte) c);
                    }
                }
            case MINUS:
                // RESP Errors
                builder = ByteBuilder.allocate(128);
                while (true) {
                    while ((c = in.read()) != '\r') {
                        builder.put((byte) c);
                    }
                    if ((c = in.read()) == '\n') {
                        return builder.toString();
                    } else {
                        builder.put((byte) c);
                    }
                }
            default:
                throw new AssertionError("Expect [$,:,*,+,-] but: " + (char) c);

        }
    }
}
