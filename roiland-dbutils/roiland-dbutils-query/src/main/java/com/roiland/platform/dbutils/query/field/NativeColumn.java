package com.roiland.platform.dbutils.query.field;

/**
 * Created by jeffy.yang on 2016/1/11.
 */
public class NativeColumn implements IField {

    private String column = null;

    public NativeColumn(String column) {
        this.column = column;
    }

    public String toString() {
        return column;
    }
}
