package com.roiland.platform.examples.dbutils;

import com.google.common.base.Preconditions;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * <p></p>
 *
 * @author 杨昆
 * @since 2016/10/26
 */
public abstract class TableBasicCreator {

    private List<String> imports = new ArrayList<String>();

    protected String dir;

    public TableBasicCreator(String dir) {
        this.dir = dir;
    }

    public void build(String module, String table, String comment, List<Map<String, Object>> listParams) {
        String pkg = "package com.roiland.platform.data.mysql." + module + "." + pkgSuffix().toLowerCase() + ";";

        List<String> lines = new ArrayList<String>();
        lines.add(pkg);
        lines.add("");
        lines.add("");
        lines.addAll(beginClass(table, comment));
        lines.add("");
        for (Map<String, Object> params : listParams) {
            lines.addAll(buildProperty(params));
            lines.add("");
        }

        for (Map<String, Object> params : listParams) {
            List<String> options = buildOption(params);
            if (options != null) {
                lines.addAll(options);
                lines.add("");
            }
        }
        lines.add(endClass());

        if ("bean".equalsIgnoreCase(suffix())) {
            buildImport("com.roiland.platform.data.mysql." + module + ".constants.TableConst");
        }
        Collections.sort(imports);
        lines.addAll(2, imports);

        writeFile(module, table, lines);
    }

    protected abstract void writeFile(String module, String table, List<String> lines);

    protected void output(String module, String table, String fleName, List<String> lines) {
        try {
            FileUtils.writeLines(new File(fleName), lines);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected abstract String suffix();

    protected abstract String pkgSuffix();

    protected List<String> beginClass(String table, String comment) {
        List<String> lines = new ArrayList<String>();
        lines.add(
                "/**\n" +
                        " * <p>" + comment + "</p>\n" +
                        " */");
        if ("bean".equalsIgnoreCase(suffix())) {
            buildImport("com.roiland.platform.dbutils.annotation.Table");
            lines.add(String.format("@Table(TableConst.%s)", table.substring((table.indexOf('_') > 0 ? table.indexOf('_') + 1 : 0)).toUpperCase()));
        }
        lines.add(String.format("public class %s {", buildTable(table)));
        return lines;
    }

    protected String endClass() {
        return "}";
    }

    protected abstract List<String> buildProperty(Map<String, Object> params);

    protected abstract List<String> buildOption(Map<String, Object> params);

    protected void buildImport(String classname) {
        String imp = "import " + classname + ";";
        if (!imports.contains(imp)) {
            imports.add(imp);
        }
    }

    protected String buildTable(String table) {
        table = table.substring((table.indexOf('_') > 0 ? table.indexOf('_') + 1 : 0)).toLowerCase();
        System.out.println(table);
        if (table.charAt(0) >= '0' && table.charAt(0) <= '9') table = "T" + table;

        char[] chars = table.toCharArray();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < chars.length; i++) {
            if (i == 0) {
                builder.append(Character.toUpperCase(chars[i]));
            } else if (chars[i] == '_') {
                builder.append(Character.toUpperCase(chars[++i]));
            } else {
                builder.append(chars[i]);
            }
        }
        return builder.toString() + suffix();
    }

    protected String buildColumn(String column) {
        char[] chars = column.toCharArray();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < chars.length; i++) {
            if (i == 0 && chars[i] >= '0' && chars[i] <= '9') {
                builder.append("c");
            }
            if (chars[i] == '_') {
                builder.append(Character.toUpperCase(chars[++i]));
            } else {
                builder.append(chars[i]);
            }
        }
        return builder.toString();
    }

    protected String buildType(String type) {
        String result = null;
        if ("DATE".equalsIgnoreCase(type) || "TIME".equalsIgnoreCase(type) || "DATETIME".equalsIgnoreCase(type) || "TIMESTAMP".equalsIgnoreCase(type) || "YEAR".equalsIgnoreCase(type)) {
            result = "java.util.Date";
        } else if ("CHAR".equalsIgnoreCase(type) || "VARCHAR".equalsIgnoreCase(type) || "TEXT".equalsIgnoreCase(type)) {
            result = "String";
        } else if ("BLOB".equalsIgnoreCase(type)) {
            result = "byte[]";
        } else if ("INTEGER".equalsIgnoreCase(type) || "INT".equalsIgnoreCase(type)) {
            result = "Long";
        } else if ("TINYINT".equalsIgnoreCase(type) || "SMALLINT".equalsIgnoreCase(type) || "MEDIUMINT".equalsIgnoreCase(type)) {
            result = "Integer";
        } else if ("BIT".equalsIgnoreCase(type) || "BOOLEAN".equalsIgnoreCase(type)) {
            result = "Boolean";
        } else if ("BIGINT".equalsIgnoreCase(type)) {
            result = "java.math.BigInteger";
        } else if ("FLOAT".equalsIgnoreCase(type)) {
            result = "Float";
        } else if ("DOUBLE".equalsIgnoreCase(type)) {
            result = "Double";
        } else if ("DECIMAL".equalsIgnoreCase(type)) {
            result = "java.math.BigDecimal";
        }

        Preconditions.checkNotNull(result, "unknown type '%s'", type);
        return result;
    }
}
