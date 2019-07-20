package com.roiland.platform.dbutils.query.impl;

import com.roiland.platform.dbutils.query.Query;
import com.roiland.platform.dbutils.query.field.GroupColumn;

/**
 * Created by jeffy.yang on 2016/1/10.
 */
public class Group extends Query {

    public Group(GroupColumn...fields) {
        super.group(fields);
    }
}
