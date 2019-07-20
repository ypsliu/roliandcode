package com.roiland.platform.dbutils.query.field;

/**
 * Created by jeffy.yang on 2016/1/10.
 */
public class Column implements IField {

    private String column = null;
    
    public Column(String column) {
        this(null, column, null);
    }

    public Column(String table, String column) {
        this(table, column, null);
    }

    public Column(String table, String column, String alias) {
        this.column = (table == null || "".equals(table.trim())? " ": " `" + table + "`.")
                + "`" + column + "`"
                + (alias == null || "".equals(alias.trim())? "": " AS `" + alias + "`");
    }

    public String toString() {
        return column;
    }
}
