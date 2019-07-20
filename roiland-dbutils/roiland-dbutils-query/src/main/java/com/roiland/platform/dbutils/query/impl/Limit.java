package com.roiland.platform.dbutils.query.impl;

import com.roiland.platform.dbutils.query.Query;

/**
 * Created by jeffy.yang on 2016/1/10.
 */
public class Limit extends Query {

    public Limit(Integer start, Integer limit) {
        super.limit(start, limit);
    }
}
