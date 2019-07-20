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
public class TableColumnConstCreator extends TableBasicCreator {

    public TableColumnConstCreator(String dir) {
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
        String fieldName = params.get("COLUMN_NAME").toString();
        String fieldComment = params.get("COLUMN_COMMENT").toString().trim();

        properties.add(String.format("\t//%s", fieldComment));
        String name;
        if (fieldName.charAt(0) >= '0' && fieldName.charAt(0) <= '9') {
            name = "C_" + fieldName.toUpperCase();
        } else {
            name = fieldName.toUpperCase();
        }
        properties.add(String.format("\tpublic static final String %s = \"%s\";", name, fieldName));
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
