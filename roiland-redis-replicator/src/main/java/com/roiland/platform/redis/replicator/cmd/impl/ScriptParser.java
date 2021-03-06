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
public class ScriptParser implements CommandParser<ScriptParser.ScriptCommand> {
    @Override
    public ScriptCommand parse(CommandName cmdName, Object[] params) {
        int idx = 0;
        String keyWord = (String) params[idx++];
        if (keyWord.equalsIgnoreCase("LOAD")) {
            String script = (String) params[idx++];
            return new ScriptLoadCommand(script);
        } else if (keyWord.equalsIgnoreCase("FLUSH")) {
            return new ScriptFlushCommand();
        }
        throw new AssertionError("SCRIPT " + keyWord);
    }


    public static abstract class ScriptCommand implements Command {
    }

    public static class ScriptLoadCommand extends ScriptCommand {
        public final String script;

        public ScriptLoadCommand(String script) {
            this.script = script;
        }

        @Override
        public String toString() {
            return "ScriptLoadCommand{" +
                    "script='" + script + '\'' +
                    '}';
        }

        @Override
        public CommandName name() {
            return CommandName.name("SCRIPT LOAD");
        }
    }

    public static class ScriptFlushCommand extends ScriptCommand {
        @Override
        public String toString() {
            return "ScriptFlushCommand{}";
        }

        @Override
        public CommandName name() {
            return CommandName.name("SCRIPT FLUSH");
        }
    }
}
