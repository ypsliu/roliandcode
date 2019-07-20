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

import com.roiland.platform.redis.replicator.io.RedisInputStream;
import com.roiland.platform.redis.replicator.rdb.RdbParser;

import java.io.*;

/**
 * Created by leon on 8/13/16.
 */
public class RedisFileReplicator extends AbstractReplicator {

    public RedisFileReplicator(File file) throws FileNotFoundException {
        this.inputStream = new RedisInputStream(new FileInputStream(file));
    }

    public RedisFileReplicator(InputStream in) {
        this.inputStream = new RedisInputStream(in);
    }

    @Override
    public void open() throws IOException {
        RdbParser parser = new RdbParser(inputStream, this);
        parser.parse();
    }

    @Override
    public void close() throws IOException {
        inputStream.close();
    }
}
