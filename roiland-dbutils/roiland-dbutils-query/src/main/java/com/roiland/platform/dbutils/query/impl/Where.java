package com.roiland.platform.dbutils.query.impl;

import com.roiland.platform.dbutils.query.Query;
import com.roiland.platform.dbutils.query.field.IField;

/**
 * Created by jeffy.yang on 2016/1/10.
 */
public class Where extends Query {

    public Where(IField column, ColumnOption option, Object...values) {
        super.where(column, option, values);
    }
}
