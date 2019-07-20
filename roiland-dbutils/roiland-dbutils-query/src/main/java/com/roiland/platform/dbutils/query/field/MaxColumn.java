package com.roiland.platform.dbutils.query.field;

/**
 * max列
 * @author kevin.gong
 * @version 2016/2/18 13:47
 * @since JDK1.7
 */
public class MaxColumn implements IField {

    private String column = null;

    public MaxColumn(String table, String column) {
        this.column = " MAX(" + (table == null || "".equals(table.trim())? " ": " `" + table + "`.")
                + "`" + column + "`" + ") AS `" + column + "`";
    }

    public String toString() {
        return column;
    }
}
