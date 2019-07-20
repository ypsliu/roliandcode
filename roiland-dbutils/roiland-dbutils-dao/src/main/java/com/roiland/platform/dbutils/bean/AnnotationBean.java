package com.roiland.platform.dbutils.bean;

import com.roiland.platform.dbutils.annotation.Column;
import com.roiland.platform.dbutils.annotation.Table;
import com.roiland.platform.dbutils.helper.SQLHelper;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * <p>类名：AnnotationBean
 * <p>描述：反射泛型Bean，初始化数据库相关信息
 * <p>版本：0.0.1
 * <p>创建者：杨昆
 * <p>历史
 * <table>
 * <tr><th>版本</th><th>时间</th><th>更新者</th></tr>
 * <tr><td>0.0.1</td><td>2015/9/10</td><td>杨昆</td></tr>
 * </table>
 */
public class AnnotationBean {

    private String schema;
    private String table;
    private String[] columns;

    public AnnotationBean(final Type type) {
        Type[] params = ((ParameterizedType) type).getActualTypeArguments();
        Class<?> entity = (Class<?>) params[0];
        Table clazzTable = entity.getAnnotation(Table.class);
        final Field[] fields = entity.getDeclaredFields();
        final String[] columns = new String[fields.length];

        for (int i = 0; i < fields.length; i++ ) {
            Field field = fields[i];
            String property = field.getName();

            String column = null;
            if (field.isAnnotationPresent(Column.class)) {
                column = field.getAnnotation(Column.class).name();
            }

            if (column == null || "".equals(column.trim())) {
                column = SQLHelper.decode(property, '_');
            }
            columns[i] = column;
        }
        this.schema = clazzTable.schema();
        this.table = clazzTable.name();
        this.columns = columns;
    }

    /**
     * 获取数据库库名
     * @return 库名
     */
    public String getSchema() {
        return schema;
    }

    /**
     * 获取数据库表名
     * @return 表名
     */
    public String getTable() {
        return table;
    }

    /**
     * 获取字段列表
     * @return 字段列表
     */
    public String[] getColumns() {
        return columns;
    }
}
