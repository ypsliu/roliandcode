package com.roiland.platform.examples.netty.protocol.model.bean;

/**
 * <p></p>
 *
 * @author 杨昆
 * @since 16/8/5
 */
public class DBBean {

    private String uuid;
    private String type;
    private String schema;
    private String table;
    private String shard;
    private String key;
    private String sort;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getShard() {
        return shard;
    }

    public void setShard(String shard) {
        this.shard = shard;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    @Override
    public String toString() {
        return "DBBean [" +
                "uuid='" + uuid + '\'' +
                ", type='" + type + '\'' +
                ", schema='" + schema + '\'' +
                ", table='" + table + '\'' +
                (shard == null? "": ", shard='" + shard + '\'') +
                ", key='" + key + '\'' +
                (sort == null? "": ", sort='" + sort + '\'')+ ']';
    }
}
