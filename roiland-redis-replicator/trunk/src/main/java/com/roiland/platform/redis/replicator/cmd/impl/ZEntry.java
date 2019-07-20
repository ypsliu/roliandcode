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

/**
 * Created by leon on 8/19/16.
 */
public class ZEntry {
    public final double score;
    public final String member;

    public ZEntry(double score, String member) {
        this.score = score;
        this.member = member;
    }

    @Override
    public String toString() {
        return "ZEntry{" +
                "score=" + score +
                ", member='" + member + '\'' +
                '}';
    }
}