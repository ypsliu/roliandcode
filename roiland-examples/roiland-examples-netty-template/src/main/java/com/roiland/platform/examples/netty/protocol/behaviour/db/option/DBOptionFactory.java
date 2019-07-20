package com.roiland.platform.examples.netty.protocol.behaviour.db.option;

import com.roiland.platform.examples.netty.protocol.IBehaviour;
import com.roiland.platform.examples.netty.protocol.behaviour.db.DBBiz;
import com.roiland.platform.examples.netty.protocol.behaviour.db.option.impl.*;

/**
 * <p></p>
 *
 * @author 杨昆
 * @since 16/8/9
 */
public class DBOptionFactory {

    public IBehaviour getOptionBehaviour(DBBiz dbBiz) {
        final String type = dbBiz.getDBBean().getType();
        final String option = dbBiz.getDBOptionBean().getOption();
        if ("mysql".equalsIgnoreCase(type) && "insert".equalsIgnoreCase(option)) {
            return new MySQLInsertBehaviour(dbBiz);
        } else if ("mysql".equalsIgnoreCase(type) && "select".equalsIgnoreCase(option)) {
            return new MySQLSelectBehaviour(dbBiz);
        } else if ("redis".equalsIgnoreCase(type) && "insert".equalsIgnoreCase(option)) {
            return new RedisInsertBehaviour(dbBiz);
        } else if ("redis".equalsIgnoreCase(type) && "select".equalsIgnoreCase(option)) {
            return new RedisSelectBehaviour(dbBiz);
        } else if ("cache".equalsIgnoreCase(type) && "insert".equalsIgnoreCase(option)) {
            return new CacheInsertBehaviour(dbBiz);
        } else if ("cache".equalsIgnoreCase(type) && "select".equalsIgnoreCase(option)) {
            return new CacheSelectBehaviour(dbBiz);
        } else {
            throw new IllegalArgumentException("unknown database `type` or `option`.");
        }
    }
}
