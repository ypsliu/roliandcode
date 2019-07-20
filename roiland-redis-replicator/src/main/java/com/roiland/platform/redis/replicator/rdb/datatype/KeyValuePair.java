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

package com.roiland.platform.redis.replicator.rdb.datatype;

/**
 * Created by leon on 8/13/16.
 */
public class KeyValuePair<T> {
    protected Db db;
    protected int valueRdbType;
    protected Integer expiredSeconds;
    protected Long expiredMs;
    protected String key;
    protected T value;

    public int getValueRdbType() {
        return valueRdbType;
    }

    public void setValueRdbType(int valueRdbType) {
        this.valueRdbType = valueRdbType;
    }

    public Integer getExpiredSeconds() {
        return expiredSeconds;
    }

    public void setExpiredSeconds(Integer expiredSeconds) {
        this.expiredSeconds = expiredSeconds;
    }

    public Long getExpiredMs() {
        return expiredMs;
    }

    public void setExpiredMs(Long expiredMs) {
        this.expiredMs = expiredMs;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public Db getDb() {
        return db;
    }

    public void setDb(Db db) {
        this.db = db;
    }

    @Override
    public String toString() {
        return "KeyValuePair{" +
                "db=" + db +
                ", valueRdbType=" + valueRdbType +
                ", expiredSeconds=" + expiredSeconds +
                ", expiredMs=" + expiredMs +
                ", key='" + key + '\'' +
                ", value=" + value +
                '}';
    }
}
