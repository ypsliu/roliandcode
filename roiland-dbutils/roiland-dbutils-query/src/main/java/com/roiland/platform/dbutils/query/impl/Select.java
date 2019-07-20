package com.roiland.platform.dbutils.query.impl;

import com.roiland.platform.dbutils.query.Query;
import com.roiland.platform.dbutils.query.field.IField;

/**
 * Created by jeffy.yang on 2016/1/10.
 */
public class Select extends Query {

    public Select(IField... columns) {
        super.select(columns);
    }
    
}
