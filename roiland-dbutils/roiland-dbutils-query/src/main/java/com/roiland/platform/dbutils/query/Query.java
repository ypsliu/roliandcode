package com.roiland.platform.dbutils.query;

import com.roiland.platform.dbutils.exception.IllegalMethodCalledException;
import com.roiland.platform.dbutils.query.field.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 支持SELECT、FROM、JOIN、WHERE、ORDER、LIMIT、GROUP等SQL生成
 */
public abstract class Query {

    public enum Options { SELECT, UPDATE, FROM, JOIN, WHERE, LIMIT, ORDER, GROUP }
    public enum JoinOptions { INNER, LEFT, RIGHT }
    public enum ColumnOption { EQ, NE, IN, NIN, LT, LTE, GT, GTE, LIKE, PRE_LIKE, SUF_LIKE, NOT_NULL, IS_NULL, SET_NOW }
    public enum LinkOption { AND, OR}

    private StringBuilder builder = null;
    private List<Object> params = null;

    private Options latestOption = null;
    private LinkOption latestLinkOption = null;

    public Query(){
        this.builder = new StringBuilder();
        this.params = new ArrayList<Object>();
    };

    /**
     *
     * @param columns
     * @return
     */
    public Query select(IField... columns) {
        this.latestOption = Options.SELECT;
        if (columns.length == 0) {
            this.builder.append(" SELECT *");
        } else {
            this.builder.append(" SELECT" + columns[0].toString());
            for (int i = 1; i < columns.length; i++) {
                this.builder.append("," + columns[i].toString());
            }
        }
        this.builder.append(" ");
        return this;
    }
    /**
    *
    * @param columns
    * @return
    */
   public Query update(Table table) {
       this.latestOption = Options.UPDATE;
       this.builder.append("UPDATE" + table.toString() + " SET");
       return this;
   }
    
    public Query from(Table table) {
        if (latestOption == null || latestOption == Options.SELECT) {
            this.latestOption = Options.FROM;
            this.builder.append(" FROM" + table.toString());
            return this;
        } else {
            throw new IllegalMethodCalledException("无法创建FROM语句，前置关键字为SELECT");
        }
    }

    public Query join(JoinOptions option, Table table) {
        if (latestOption == null || latestOption == Options.FROM || (latestOption == Options.JOIN && this.latestLinkOption != null)) {
            this.latestLinkOption = null;
            this.latestOption = Options.JOIN;
            this.builder.append(" " + option + " JOIN" + table.toString() + " ON");
            return this;
        } else {
            throw new IllegalMethodCalledException("无法创建JOIN语句，前置关键字为FROM、JOIN或JOIN后无条件");
        }
    }

    public Query where(IField column, ColumnOption option, Object...values) {
        if (latestOption == null || latestOption == Options.FROM || latestOption == Options.UPDATE ||(latestOption == Options.JOIN && this.latestLinkOption != null)) {
            this.latestLinkOption = null;
            this.latestOption = Options.WHERE;
            this.builder.append(" WHERE");
            return this.and(column, option, values);
        } else {
            throw new IllegalMethodCalledException("无法创建WHERE语句，前置关键字为FROM、JOIN或JOIN后无条件");
        }
    }

    public Query order(OrderColumn...fields) {
        this.latestOption = Options.ORDER;
        if (fields.length == 1) {
            this.builder.append(" ORDER BY" + fields[0].toString());
        } else if (fields.length > 1) {
            this.builder.append(" ORDER BY" + fields[0].toString());
            for (int i = 1; i < fields.length; i++) {
                this.builder.append("," + fields[i].toString());
            }
        }
        this.builder.append(" ");
        return this;
    }

    public Query limit(int start, int limit) {
        if (start >= 0 && limit > 0) {
            this.latestOption = Options.LIMIT;
            this.builder.append(" LIMIT ?, ?");
            this.params.add(start);
            this.params.add(limit);
            return this;
        } else {
            throw new IllegalArgumentException("无法创建LIMIT语句，起始位置大于等于0并且限制长度大于0");
        }
    }

    public Query group(GroupColumn...fields) {
        this.latestOption = Options.GROUP;
        if (fields.length == 1) {
            this.builder.append(" GROUP BY" + fields[0].toString());
        } else if (fields.length > 1) {
            this.builder.append(" GROUP BY" + fields[0].toString() );
            for (int i = 1; i < fields.length; i++) {
                this.builder.append("," + fields[i].toString());
            }
        }
        return this;
    }

    public Query and() {
    	this.builder.append(latestLinkOption == null? "": " AND");
        this.latestLinkOption = LinkOption.AND;
        return this;
    }

    public Query and(IField column, ColumnOption option, Object...values) {
        return this.and("AND", column, option, values);
    }

    public Query and(String split, IField column, ColumnOption option, Object...values) {
        this.builder.append(latestLinkOption == null? "": " " + split).append(column.toString() + option(option, values));
        this.latestLinkOption = LinkOption.AND;
        return this;
    }
    
    public Query and(IField column, IField other) {
        this.builder.append(latestLinkOption == null? "": " AND").append(column.toString() + "=" + other.toString());
        this.latestLinkOption = LinkOption.AND;
        return this;
    }

    public Query or() {
        this.builder.append(latestLinkOption == null? "": " OR");
        this.latestLinkOption = LinkOption.OR;
        return this;
    }

    public Query or(IField column, ColumnOption option, Object...values) {
        this.builder.append(latestLinkOption == null? "": " OR").append(column.toString() + option(option, values));
        this.latestLinkOption = LinkOption.OR;
        return this;
    }

    public Query begin(){
        this.latestLinkOption = null;
        this.builder.append(" (");
        return this;
    }

    public Query end(){
        this.builder.append(")");
        return this;
    }

    public Query append(Query query) {
        if (query != null || query != this) {
            this.builder.append(query.toString());
            this.params.addAll(query.params);
        }
        return this;
    }

    private String option(ColumnOption option, Object...values) {
        String result = null;
        switch (option) {
            case EQ:
                if (values.length == 1) {
                    result = " = ?";
                    params.add(values[0]);
                }
                break;
            case NE:
                if (values.length == 1) {
                    result = " <> ?";
                    params.add(values[0]);
                }
                break;
            case IN:
                if (values.length >= 1) {
                    StringBuilder temp = new StringBuilder(" in (?");
                    params.add(values[0]);
                    for (int i = 1; i < values.length; i++) {
                        temp.append(", ?");
                        params.add(values[i]);
                    }
                    result = temp.append(")").toString();
                }
                break;
            case NIN:
                if (values.length >=1) {
                    StringBuilder temp = new StringBuilder(" not in (?");
                    params.add(values[0]);
                    for (int i = 1; i < values.length; i++) {
                        temp.append(", ?");
                        params.add(values[i]);
                    }
                    result = temp.append(")").toString();
                }
                break;
            case LT:
                if (values.length == 1) {
                    result = " < ?";
                    params.add(values[0]);
                }
                break;
            case LTE:
                if (values.length == 1) {
                    result = " <= ?";
                    params.add(values[0]);
                }
                break;
            case GT:
                if (values.length == 1) {
                    result = " > ?";
                    params.add(values[0]);
                }
                break;
            case GTE:
                if (values.length == 1) {
                    result = " >= ?";
                    params.add(values[0]);
                }
                break;
            case LIKE:
                if (values.length == 1) {
                    result = " like CONCAT('%', ?, '%')";
                    params.add(values[0]);
                }
                break;
            case PRE_LIKE:
                if (values.length == 1) {
                    result = " like CONCAT('%', ?)";
                    params.add(values[0]);
                }
                break;
            case SUF_LIKE:
                if (values.length == 1) {
                    result = " like CONCAT(?, '%')";
                    params.add(values[0]);
                }
                break;
            case IS_NULL:
                result = " is null";
                break;
            case NOT_NULL:
                result = " is not null";
                break;
            case SET_NOW:
                result = " = NOW()";
                break;
        }
        return result;
    }

    public String toString() {
        return this.builder.toString();
    }

    public Object[] toParams() {
        return params.toArray();
    }
}
