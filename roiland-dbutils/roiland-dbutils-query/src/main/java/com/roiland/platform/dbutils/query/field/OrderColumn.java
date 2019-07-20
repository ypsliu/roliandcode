package com.roiland.platform.dbutils.query.field;

/**
 * Created by jeffy.yang on 2016/1/10.
 */
public class OrderColumn {

    public enum OrderOption {ASC, DESC}

    private String column = null;

    public OrderColumn(String column) {
        this(null, column, OrderOption.ASC);
    }

    public OrderColumn(String table, String column) {
        this(table, column, OrderOption.ASC);
    }

    public OrderColumn(String table, String column, OrderOption option) {
        this.column = (table == null || "".equals(table.trim())? " ": " `" + table + "`.")
                + "`" + column + "` "
                + (option == null? OrderOption.ASC: option);
    }

    public String toString() {
        return column;
    }
}
