package com.roiland.platform.dbutils.query.impl;

import com.roiland.platform.dbutils.query.Query;
import com.roiland.platform.dbutils.query.field.Table;

/**
 * Created by jeffy.yang on 2016/1/10.
 */
public class Join extends Query {

    public Join(JoinOptions option, Table table) {
        super.join(option, table);
    }
}
