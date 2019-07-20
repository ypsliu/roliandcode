package com.roiland.platform.dbutils.query.field;

/**
 * Created by jeffy.yang on 2016/1/11.
 */
public class CountColumn implements IField {

    private String column = null;

    public CountColumn() {
        this.column = " COUNT(1) AS COUNT ";
    }

    public String toString() {
        return column;
    }
}
