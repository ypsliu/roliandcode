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
 * Created by leon on 8/19/16.
 */
public class ZUnionStoreParser implements CommandParser<ZUnionStoreParser.ZUnionStoreCommand> {
    @Override
    public ZUnionStoreCommand parse(CommandName cmdName, Object[] params) {
        int idx = 0;
        Boolean isSum = null, isMin = null, isMax = null;
        String destination = (String) params[idx++];
        int numkeys = Integer.parseInt((String) params[idx++]);
        String[] keys = new String[numkeys];
        for (int i = 0; i < numkeys; i++) {
            keys[i] = (String) params[idx++];
        }
        double[] weights = null;
        if (idx < params.length) {
            String param = (String) params[idx];
            if (param.equalsIgnoreCase("WEIGHTS")) {
                idx++;
                weights = new double[numkeys];
                for (int i = 0; i < numkeys; i++) {
                    weights[i] = Double.parseDouble((String) params[idx++]);
                }
            } else if (param.equalsIgnoreCase("AGGREGATE")) {
                idx++;
                String next = (String) params[idx];
                if (next.equalsIgnoreCase("SUM")) {
                    isSum = true;
                } else if (next.equalsIgnoreCase("MIN")) {
                    isMin = true;
                } else if (next.equalsIgnoreCase("MAX")) {
                    isMax = true;
                }
            }
        }
        return new ZUnionStoreCommand(destination, numkeys, keys, weights, isSum, isMin, isMax);
    }

    public static class ZUnionStoreCommand implements Command {
        private final String destination;
        private final int numkeys;
        private final String[] keys;
        private final double[] weights;
        private final Boolean isSum;
        private final Boolean isMin;
        private final Boolean isMax;

        public ZUnionStoreCommand(String destination, int numkeys, String[] keys, double[] weights, Boolean isSum, Boolean isMin, Boolean isMax) {
            this.destination = destination;
            this.numkeys = numkeys;
            this.keys = keys;
            this.weights = weights;
            this.isSum = isSum;
            this.isMin = isMin;
            this.isMax = isMax;
        }

        @Override
        public String toString() {
            return "ZUnionStoreCommand{" +
                    "destination='" + destination + '\'' +
                    ", numkeys=" + numkeys +
                    ", keys=" + Arrays.toString(keys) +
                    ", weights=" + Arrays.toString(weights) +
                    ", isSum=" + isSum +
                    ", isMin=" + isMin +
                    ", isMax=" + isMax +
                    '}';
        }

        @Override
        public CommandName name() {
            return CommandName.name("ZUNIONSTORE");
        }
    }
}
