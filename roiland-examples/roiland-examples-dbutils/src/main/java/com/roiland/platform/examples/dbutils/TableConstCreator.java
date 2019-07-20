package com.roiland.platform.examples.dbutils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p></p>
 *
 * @author 杨昆
 * @since 2016/10/26
 */
public class TableConstCreator extends TableBasicCreator {

    public TableConstCreator(String dir) {
        super(dir);
    }


    @Override
    protected String suffix() {
        return "Const";
    }

    @Override
    protected String pkgSuffix() {
        return "constants";
    }

    @Override
    protected List<String> buildProperty(Map<String, Object> params) {
        List<String> properties = new ArrayList<String>();
        String tableName = params.get("TABLE_NAME").toString();
        String tableComment = params.get("TABLE_COMMENT").toString().trim();

        properties.add(String.format("\t\t//%s", tableComment));
        properties.add(String.format("\t\tpublic static final String %s = \"%s\";", tableName.substring((tableName.indexOf('_') > 0 ? tableName.indexOf('_') + 1 : 0)).toUpperCase(), tableName));
        return properties;
    }

    @Override
    protected List<String> buildOption(Map<String, Object> params) {
        return null;
    }

    protected void writeFile(String module, String table, List<String> lines) {
        String fileName = dir + "/" + module + "/constants/" + buildTable(table) + ".java";
        output(module, table, fileName, lines);
    }
}
