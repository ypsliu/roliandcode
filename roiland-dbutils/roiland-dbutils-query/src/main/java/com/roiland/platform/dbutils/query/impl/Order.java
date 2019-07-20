package com.roiland.platform.dbutils.query.impl;

import com.roiland.platform.dbutils.query.Query;
import com.roiland.platform.dbutils.query.field.OrderColumn;

/**
 * Created by jeffy.yang on 2016/1/10.
 */
public class Order extends Query {

    public Order(OrderColumn...fields) {
        super.order(fields);
    }
}
