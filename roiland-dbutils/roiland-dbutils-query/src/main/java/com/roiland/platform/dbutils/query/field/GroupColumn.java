package com.roiland.platform.dbutils.query.field;

/**
 * Created by jeffy.yang on 2016/1/10.
 */
public class GroupColumn {

    public enum OrderOption {ASC, DESC}

    private String column = null;

    public GroupColumn(String column) {
        this(null, column);
    }

    public GroupColumn(String table, String column) {
        this.column = (table == null || "".equals(table.trim())? " ": " `" + table + "`.")
                + "`" + column + "`";
    }

    public String toString() {
        return column;
    }
}
