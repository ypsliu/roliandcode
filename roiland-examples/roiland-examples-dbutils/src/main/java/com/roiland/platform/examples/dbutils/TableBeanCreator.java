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
public class TableBeanCreator extends TableBasicCreator {

    public TableBeanCreator(String dir) {
        super(dir);
    }

    @Override
    protected String suffix() {
        return "Bean";
    }

    @Override
    protected String pkgSuffix() {
        return "bean";
    }

    @Override
    protected List<String> buildProperty(Map<String, Object> params) {
        List<String> lines = new ArrayList<String>();
        String fieldName = params.get("COLUMN_NAME").toString().trim();
        String fieldType = params.get("DATA_TYPE").toString().trim();
        String fieldComment = params.get("COLUMN_COMMENT").toString().trim();


        String type = super.buildType(fieldType);
        int index = type.lastIndexOf('.') > 0 ? type.lastIndexOf('.') + 1 : 0;
        if (index > 0) {
            buildImport(type);
            type = type.substring(index);
        }

        String name = super.buildColumn(fieldName);
        buildImport("com.roiland.platform.dbutils.annotation.Column");
        lines.add(String.format("\t//%s", fieldComment));
        lines.add(String.format("\t@Column(\"%s\")", fieldName));
        lines.add(String.format("\tprivate %s %s;", type, name));
        return lines;
    }

    @Override
    protected List<String> buildOption(Map<String, Object> params) {
        List<String> options = new ArrayList<String>();
        String fieldName = params.get("COLUMN_NAME").toString();
        String fieldType = params.get("DATA_TYPE").toString();
        String fieldComment = params.get("COLUMN_COMMENT").toString().trim();
        String type = super.buildType(fieldType);
        String name = super.buildColumn(fieldName);
        options.add(String.format(
                "\t/**\n" +
                        "\t * 设置%s.\n" +
                        "\t *\n" +
                        "\t * @param %s %s\n" +
                        "\t */\n" +
                        "\tpublic void set%s(%s %s) {\n" +
                        "\t    this.%s = %s;\n" +
                        "\t}\n", fieldComment, name, fieldComment, Character.toUpperCase(name.toCharArray()[0]) + name.substring(1), type, name, name, name));

        options.add(String.format(
                "\t/**\n" +
                        "\t * 获取%s.\n" +
                        "\t *\n" +
                        "\t * @return %s\n" +
                        "\t */\n" +
                        "\tpublic %s get%s() {\n" +
                        "\t    return this.%s;\n" +
                        "\t}", fieldComment, fieldComment, type, Character.toUpperCase(name.toCharArray()[0]) + name.substring(1), name));

        return options;
    }

    protected void writeFile(String module, String table, List<String> lines) {
        String fileName = dir + "/" + module + "/bean/" + buildTable(table) + ".java";
        output(module, table, fileName, lines);
    }
}
