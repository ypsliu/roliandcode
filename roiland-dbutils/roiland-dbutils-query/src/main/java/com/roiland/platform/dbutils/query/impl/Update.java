package com.roiland.platform.dbutils.query.impl;

import com.roiland.platform.dbutils.query.Query;
import com.roiland.platform.dbutils.query.field.Table;

/**
 * Created by jeffy.yang on 2016/1/10.
 */
public class Update extends Query {

    public Update(Table table) {
        super.update(table);
    }
    
}
