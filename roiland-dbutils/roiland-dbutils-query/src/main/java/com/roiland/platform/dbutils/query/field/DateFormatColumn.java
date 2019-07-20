package com.roiland.platform.dbutils.query.field;

/**
 * Created by jeffy.yang on 2016/1/11.
 */
public class DateFormatColumn implements IField {

    private String column = null;

    public DateFormatColumn(String table, String column, String format, String alias) {
        this.column = " DATE_FORMAT(" + (table == null || "".equals(table.trim())? " ": " `" + table + "`.")
                + "`" + column + "`" + ", '" + format + "')"
                + (alias == null || "".equals(alias.trim())? "": " AS `" + alias + "`");
    }

    public String toString() {
        return column;
    }
}
