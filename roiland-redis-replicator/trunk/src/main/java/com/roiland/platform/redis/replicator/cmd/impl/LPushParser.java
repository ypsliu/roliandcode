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

import java.util.Arrays;

/**
 * Created by leon on 8/14/16.
 */
public class LPushParser implements CommandParser<LPushParser.LPushCommand> {


    @Override
    public LPushCommand parse(CommandName cmdName, Object[] params) {
        int idx = 0, newIdx = 0;
        String key = (String) params[idx++];
        String[] values = new String[params.length - 1];
        while (idx < params.length) {
            values[newIdx++] = (String) params[idx++];
        }
        return new LPushCommand(key, values);
    }

    public static class LPushCommand implements Command {
        public final String key;
        public final String[] values;

        public LPushCommand(String key, String... values) {
            this.key = key;
            this.values = values;
        }

        @Override
        public String toString() {
            return "LPushCommand{" +
                    "key='" + key + '\'' +
                    ", values=" + Arrays.toString(values) +
                    '}';
        }

        @Override
        public CommandName name() {
            return CommandName.name("LPUSH");
        }
    }
}
