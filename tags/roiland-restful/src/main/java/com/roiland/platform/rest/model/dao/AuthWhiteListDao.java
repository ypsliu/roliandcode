package com.roiland.platform.rest.model.dao;

import com.roiland.platform.dbutils.DBConn;
import com.roiland.platform.rest.util.Constants;
import org.skife.jdbi.v2.Handle;

import java.util.List;
import java.util.Map;

/**
 * Created by jeffy.yang on 2015/12/6.
 */
public class AuthWhiteListDao {

    private static final AuthWhiteListDao INSTANCE = new AuthWhiteListDao();

    private static final String WHITE_LIST_SELECT_ALL_SQL = "select source,command,delete_flag from t_usr_auth_ctrl_whitelist where application = :application";

    private AuthWhiteListDao(){
        //单例模式，方便测试用例注入
    }

    public static AuthWhiteListDao instance(){
        return INSTANCE;
    }

    public List<Map<String, Object>> findAll() {
        Handle handle = null;
        try {
            handle = DBConn.handle(Constants.RESTFUL_DB_SCHEMA);
            return handle.createQuery(WHITE_LIST_SELECT_ALL_SQL).bind("application",Constants.APPLICATION).list();
        } finally {
            if (handle != null) {
                handle.close();
            }
        }
    }
}
