package com.roiland.platform.dbutils.bean;

import com.roiland.platform.dbutils.query.Query;
import com.roiland.platform.dbutils.query.Query.ColumnOption;
import com.roiland.platform.dbutils.query.field.Column;
import com.roiland.platform.dbutils.query.field.Table;
import com.roiland.platform.dbutils.query.impl.Update;
import junit.framework.TestCase;

public class QueryTest extends TestCase {

    public void testWhere() throws Exception {
	
//        Query query = new Select(new Column("ei_info", "ei", "ei"), new Column("ei_info", "imsi", "im"));
//        query.from(new Table("t_ei_info", "ei_info"));
//        query.join(Query.JoinOptions.INNER, new Table("t_ei_info", "temp"));
//        query.begin().and(new Column("temp", "ei"), Query.ColumnOption.EQ, "1952DTEST00000001");
//        query.and(new Column("temp", "ei"), Query.ColumnOption.NIN, "1952DTEST00000002", "1952DTEST00000003").end();
//        System.out.println(query.toString());
//        System.out.println(Arrays.deepToString(query.toParams()));
//
//        Query where = new Where(new Column("ei_info", "ei"), Query.ColumnOption.EQ, "1952DTEST00000004");
//        query.append(where);
//        System.out.println(where.toString());
//        System.out.println(Arrays.deepToString(where.toParams()));
//
//        query.append(new Limit(0, 1000));
//        System.out.println(query.toString());
//        System.out.println(Arrays.deepToString(query.toParams()));
//
//        query.append(new Order(new OrderColumn("ei_info", "ei", OrderOption.DESC)));
        
        Query update = new Update(new Table("t_usr_login"));
        update.and(",", new Column("username"), ColumnOption.EQ, "jeffy.yang");
        update.where(new Column("uaid"), ColumnOption.EQ, "1111");
        System.out.println(update.toString());
//        AbstractQuery query = new AbstractQuery("t_auth_black_list","field1,field2,field3").where("cnum").is("100012312V1231").limit(1000, 10);
//        System.out.println(query.toSql());
//        System.out.println(query.getValueArray().length);
//        System.out.println(Arrays.deepToString(query.getValueArray()));
    }

    public void testAnd() throws Exception {
//        AbstractQuery query = new AbstractQuery("t_auth_black_list","field1,field2,field3").where("cnum").is("100012312V1231").and("ei").is(21312321).group("field1,field2").orderAsc("field3").orderDesc("field2");
//        System.out.println(query.toSql());
//        System.out.println(query.getValueArray().length);
//        System.out.println(Arrays.deepToString(query.getValueArray()));
    }

    public void testIs() throws Exception {
//        AbstractQuery query = new AbstractQuery("t_auth_black_list","field1,field2,field3").where("cnum").is("100012312V1231").and("ei").is(21312321).group("field1,field2").orderAsc("field3").orderDesc("field2");
//        System.out.println(query.toSql());
//        System.out.println(query.getValueArray().length);
//        System.out.println(Arrays.deepToString(query.getValueArray()));
    }

    public void testNe() throws Exception {
//        AbstractQuery query = new AbstractQuery("t_auth_black_list","field1,field2,field3").where("cnum").ne("100012312V1231").and("ei").lte(21312321).group("field1,field2").orderAsc("field3").orderDesc("field2");
//        System.out.println(query.toSql());
//        System.out.println(query.getValueArray().length);
//        System.out.println(Arrays.deepToString(query.getValueArray()));
    }

    public void testLt() throws Exception {
//        AbstractQuery query = new AbstractQuery("t_auth_black_list","field1,field2,field3").group("field1,field2").orderAsc("field3").orderDesc("field2").limit(100);
//        System.out.println(query.toSql());
//        System.out.println(query.getValueArray().length);
//        System.out.println(Arrays.deepToString(query.getValueArray()));
    }

    public void testLte() throws Exception {

    }

    public void testGt() throws Exception {

    }

    public void testGte() throws Exception {

    }

    public void testIn() throws Exception {

    }

    public void testNin() throws Exception {

    }

    public void testLike() throws Exception {
//        AbstractQuery query = new AbstractQuery("t_auth_black_list","field1,field2,field3").where("cnum").like("%1231%");
//        System.out.println(query.toSql());
//        System.out.println(query.getValueArray().length);
//        System.out.println(Arrays.deepToString(query.getValueArray()));
    }

    public void testGroup() throws Exception {

    }

    public void testLimit() throws Exception {
//        AbstractQuery query = new AbstractQuery("t_auth_black_list","field1,field2,field3").where("cnum").like("%1231%").limit(100);
//        System.out.println(query.toSql());
//        System.out.println(query.getValueArray().length);
//        System.out.println(Arrays.deepToString(query.getValueArray()));
    }

    public void testLimit1() throws Exception {

    }

    public void testOrderAsc() throws Exception {

    }

    public void testOrderDesc() throws Exception {

    }

    public void testToSql() throws Exception {

    }

    public void testGetValueArray() throws Exception {

    }
}