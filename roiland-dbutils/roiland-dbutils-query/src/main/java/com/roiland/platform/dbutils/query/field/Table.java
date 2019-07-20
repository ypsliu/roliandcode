package com.roiland.platform.dbutils.query.field;

/**
 * Created by jeffy.yang on 2016/1/10.
 */
public class Table {

    private String table = null;

    public Table(String table) {
        this.table = table == null || "".equals(table.trim())? " ": " `" + table + "`";
    }

    public Table(String table, String alias) {
        this.table = (table == null || "".equals(table.trim())? " ": " `" + table + "`") + (alias == null || "".equals(alias.trim())? "": " AS `" + alias + "`");
    }

    public String toString() {
        return table;
    }
}
