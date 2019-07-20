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

package com.roiland.platform.redis.replicator.cmd.impl;

import com.roiland.platform.redis.replicator.cmd.Command;
import com.roiland.platform.redis.replicator.cmd.CommandName;
import com.roiland.platform.redis.replicator.cmd.CommandParser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by leon on 8/19/16.
 */
public class ZAddParser implements CommandParser<ZAddParser.ZAddCommand> {

    @Override
    public ZAddCommand parse(CommandName cmdName, Object[] params) {
        int idx = 0;
        Boolean isNx = null, isXx = null, isCh = null, isIncr = null;
        List<ZEntry> list = new ArrayList<>();
        String key = (String) params[idx++];
        while (idx < params.length) {
            String param = (String) params[idx];
            if (param.equalsIgnoreCase("NX")) {
                isNx = true;
            } else if (param.equalsIgnoreCase("XX")) {
                isXx = true;
            } else if (param.equalsIgnoreCase("CH")) {
                isCh = true;
            } else if (param.equalsIgnoreCase("INCR")) {
                isIncr = true;
            } else {
                double score = Double.parseDouble(param);
                idx++;
                String member = (String) params[idx];
                list.add(new ZEntry(score, member));
            }
            idx++;
        }
        ZEntry[] zEntries = new ZEntry[list.size()];
        list.toArray(zEntries);
        return new ZAddCommand(key, isNx, isXx, isCh, isIncr, zEntries);
    }

    public static class ZAddCommand implements Command {
        public final String key;
        public final Boolean isNx;
        public final Boolean isXx;
        public final Boolean isCh;
        public final Boolean isIncr;
        public final ZEntry[] zEntries;

        public ZAddCommand(String key, Boolean isNx, Boolean isXx, Boolean isCh, Boolean isIncr, ZEntry[] zEntries) {
            this.key = key;
            this.isNx = isNx;
            this.isXx = isXx;
            this.isCh = isCh;
            this.isIncr = isIncr;
            this.zEntries = zEntries;
        }

        @Override
        public String toString() {
            return "ZAddCommand{" +
                    "key='" + key + '\'' +
                    ", isNx=" + isNx +
                    ", isXx=" + isXx +
                    ", isCh=" + isCh +
                    ", isIncr=" + isIncr +
                    ", zEntries=" + Arrays.toString(zEntries) +
                    '}';
        }

        @Override
        public CommandName name() {
            return CommandName.name("ZADD");
        }
    }
}
