package com.roiland.platform.rest.filter.auth;

import com.roiland.platform.rest.model.dao.AuthBlackListDao;
import com.roiland.platform.rest.model.dao.AuthWhiteListDao;
import com.roiland.platform.rest.task.AbstractTimerTask;
import com.roiland.platform.rest.util.Constants;

import java.util.*;

/**
 * Created by jeffy.yang on 2015/12/6.
 */
public class AuthorizationSingleton {

    private static AuthorizationSingleton INSTANCE = new AuthorizationSingleton();

    private final Set<Object[]> BLACK_LIST;

    private final Map<String, Set<String>> WHITE_LIST;

    private AuthorizationSingleton() {
        BLACK_LIST = new HashSet<>();
        WHITE_LIST = new HashMap<>();
        this.loadBlackAndWhiteData();
        new Timer().schedule(new BlackAndWhiteTask("BLACK_WHITE_LIST_SYNC_TASK"), Constants.BLACK_WHITE_LIST_SYNC_TASK_DELAY, Constants.BLACK_WHITE_LIST_SYNC_TASK_PERIOD);
    }

    public static AuthorizationSingleton instance() {
        return INSTANCE;
    }

    public boolean containsInBlack(String strPath, String strAppID) {
        return BLACK_LIST.contains(new String[]{strPath, strAppID});
    }

    public boolean containsInWhite(String strPath, String strAppID) {
        Set<String> strAppIDs = WHITE_LIST.get(strPath);
        if (strAppIDs == null || strAppIDs.size() == 0) {
            return true;
        }
        return strAppIDs.contains(strAppID);
    }

    private void loadBlackAndWhiteData() {
        final List<Map<String, Object>> blacks = AuthBlackListDao.instance().findAll();
        for (Map<String, Object> black : blacks) {
            final String strAppID = black.get("source").toString();
            final String strPath = black.get("command").toString();
            Integer objDeleteFlag = (Integer) black.get("delete_flag");

            String[] ary = new String[]{strPath, strAppID};
            if (objDeleteFlag == 0) {
                if (!BLACK_LIST.contains(ary)) {
                    BLACK_LIST.add(ary);
                }
            } else {
                BLACK_LIST.remove(ary);
            }
        }

        final List<Map<String, Object>> whites = AuthWhiteListDao.instance().findAll();
        for (Map<String, Object> white : whites) {
            final String strAppID = white.get("source").toString();
            final String strPath = white.get("command").toString();
            Integer objDeleteFlag = (Integer) white.get("delete_flag");

            if (objDeleteFlag == 0) {
                if (WHITE_LIST.containsKey(strPath)) {
                    WHITE_LIST.get(strPath).add(strAppID);
                } else {
                    WHITE_LIST.put(strPath, new HashSet<>(Arrays.asList(strAppID)));
                }
            } else {
                if (WHITE_LIST.containsKey(strPath)) {
                    WHITE_LIST.get(strPath).remove(strAppID);
                }
            }
        }
    }

    private class BlackAndWhiteTask extends AbstractTimerTask {

        public BlackAndWhiteTask(String taskName) {
            super(taskName);
        }

        @Override
        protected void execute() throws Exception {
            loadBlackAndWhiteData();
        }
    }

}
