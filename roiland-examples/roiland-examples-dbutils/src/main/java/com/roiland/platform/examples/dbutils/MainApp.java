package com.roiland.platform.examples.dbutils;

import com.roiland.platform.dbutils.mysql.DBOption;
import com.roiland.platform.dbutils.mysql.RoilandDataSource;
import com.roiland.platform.lang.StringUtils;
import com.roiland.platform.zookeeper.RoilandProperties;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * <p></p>
 *
 * @author 杨昆
 * @since 2016/10/26
 */
public class MainApp {

    DBOption dbOption = null;
    String module;
    String schema;

    public MainApp(String module, String schema) {
        this.module = module;
        this.schema = schema;
        RoilandProperties properties = new RoilandProperties("roiland-examples-dbutils");
        String url = properties.getProperty("mysql." + schema + ".url");
        String username = properties.getProperty("mysql." + schema + ".username");
        String password = properties.getProperty("mysql." + schema + ".password");
        dbOption = new DBOption("information_schema", new RoilandDataSource(url, username, password));
    }


    public static void main(String[] args) {
        String dir = "/home/roy/tmp/ttt/";

        String daoModule = "common";
        String schema = "dcs-common";

        daoModule = "center";
        schema = "user-center";

        daoModule = "business";
        schema = "dcs-business";

        String table = "";
        MainApp app = new MainApp(daoModule, schema);
        if (StringUtils.isEmpty(table)) {
            List<Map<String, Object>> tables = app.findTables();
            for (Map<String, Object> params : tables) {
                String name = params.get("TABLE_NAME").toString();
                String comment = params.get("TABLE_COMMENT").toString();

                List<Map<String, Object>> columns = app.findColumns(name);
                new TableBeanCreator(dir).build(daoModule, name, comment, columns);
                new TableColumnConstCreator(dir).build(daoModule, name, comment, columns);
            }
        } else {
            Map<String, Object> params = app.findTable(table);

            String name = params.get("TABLE_NAME").toString();
            String comment = params.get("TABLE_COMMENT").toString();

            List<Map<String, Object>> columns = app.findColumns(name);
            new TableBeanCreator(dir).build(daoModule, name, comment, columns);
            new TableColumnConstCreator(dir).build(daoModule, name, comment, columns);
        }

        List<Map<String, Object>> tables = app.findTables();
        new TableConstCreator(dir).build(daoModule, "table", "", tables);

    }


    protected Map<String, Object> findTable(String table) {
        return dbOption.selectOne(null
                , "SELECT\n" +
                        "\tTABLE_NAME ,\n" +
                        "\tTABLE_COMMENT\n" +
                        "FROM\n" +
                        "\t`TABLES`\n" +
                        "WHERE\n" +
                        "\tTABLE_SCHEMA = ? AND TABLE_NAME = ?\n" +
                        "AND TABLE_NAME NOT LIKE 'bak_%'\n" +
                        "ORDER BY\n" +
                        "\tTABLE_NAME"
                , schema, table);
    }

    protected List<Map<String, Object>> findTables() {
        return dbOption.select(null
                , "SELECT\n" +
                        "\tTABLE_NAME ,\n" +
                        "\tTABLE_COMMENT\n" +
                        "FROM\n" +
                        "\t`TABLES`\n" +
                        "WHERE\n" +
                        "\tTABLE_SCHEMA = ?\n" +
                        "AND TABLE_NAME NOT LIKE 'bak_%'\n" +
                        "ORDER BY\n" +
                        "\tTABLE_NAME"
                , schema);
    }

    protected List<Map<String, Object>> findColumns(String table) {
        return dbOption.select(null,
                "SELECT\n" +
                        "\t`COLUMNS`.`COLUMN_NAME` ,\n" +
                        "\t`COLUMNS`.`DATA_TYPE` ,\n" +
                        "\t`COLUMNS`.`COLUMN_COMMENT`\n" +
                        "FROM\n" +
                        "\t`COLUMNS`\n" +
                        "WHERE\n" +
                        "\tTABLE_SCHEMA = ?\n" +
                        "AND TABLE_NAME = ?\n" +
                        "ORDER BY\n" +
                        "\t`COLUMNS`.`COLUMN_NAME`", schema, table);
    }


}


