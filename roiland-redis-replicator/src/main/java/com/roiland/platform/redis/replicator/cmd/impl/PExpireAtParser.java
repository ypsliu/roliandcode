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

/**
 * Created by leon on 8/14/16.
 */
public class PExpireAtParser implements CommandParser<PExpireAtParser.PExpireAtCommand> {
    @Override
    public PExpireAtCommand parse(CommandName cmdName, Object[] params) {
        int idx = 0;
        String key = (String) params[idx++];
        long ex = Long.parseLong((String) params[idx++]);
        return new PExpireAtCommand(key, ex);
    }

    public static class PExpireAtCommand implements Command {
        public final String key;
        public final long ex;

        public PExpireAtCommand(String key, long ex) {
            this.key = key;
            this.ex = ex;
        }

        @Override
        public String toString() {
            return "PExpireAtCommand{" +
                    "key='" + key + '\'' +
                    ", ex=" + ex +
                    '}';
        }

        @Override
        public CommandName name() {
            return CommandName.name("PEXPIREAT");
        }
    }
}
