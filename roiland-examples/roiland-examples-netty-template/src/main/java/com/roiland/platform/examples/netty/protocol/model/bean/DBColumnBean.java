package com.roiland.platform.examples.netty.protocol.model.bean;

/**
 * <p></p>
 *
 * @author 杨昆
 * @since 16/8/5
 */
public class DBColumnBean {

    private String name;
    private String column;
    private String type;
    private String prefix;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DBColumnBean that = (DBColumnBean) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (column != null ? !column.equals(that.column) : that.column != null) return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;
        return !(prefix != null ? !prefix.equals(that.prefix) : that.prefix != null);

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (column != null ? column.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (prefix != null ? prefix.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "DBColumnBean [" +
                "name='" + name + '\'' +
                ", column='" + column + '\'' +
                (type == null? "": ", type='" + type + '\'') +
                (prefix == null? "": ", prefix='" + prefix + '\'') +
                ']';
    }
}
