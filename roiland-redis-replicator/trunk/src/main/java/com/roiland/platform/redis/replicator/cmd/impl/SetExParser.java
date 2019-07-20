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
public class SetExParser implements CommandParser<SetExParser.SetExCommand> {
    @Override
    public SetExCommand parse(CommandName cmdName, Object[] params) {
        int idx = 0;
        String key = (String) params[idx++];
        int ex = Integer.parseInt((String) params[idx++]);
        String value = (String) params[idx++];
        return new SetExCommand(key, ex, value);
    }

    public static class SetExCommand implements Command {
        public final String key;
        public final int ex;
        public final String value;

        public SetExCommand(String key, int ex, String value) {
            this.key = key;
            this.value = value;
            this.ex = ex;
        }

        @Override
        public String toString() {
            return "SetExCommand{" +
                    "key='" + key + '\'' +
                    ", ex=" + ex +
                    ", value='" + value + '\'' +
                    '}';
        }

        @Override
        public CommandName name() {
            return CommandName.name("SETEX");
        }
    }
}
