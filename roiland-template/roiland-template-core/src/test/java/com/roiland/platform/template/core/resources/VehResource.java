package com.roiland.platform.template.core.resources;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author leon.chen
 * @since 2016/4/8
 */
public class VehResource implements IVehService {

    @Override
    public Map<String, Object> getVehInfo(String bid, String cnum, String ttt, String[] field1, HashMap<String, String> field2) {
        Map<String, Object> rs = new HashMap<>();
        rs.put("bid", bid);
        rs.put("cnum", cnum);
        rs.put("ttt", ttt);
        rs.putAll(field2);
        rs.put("fields", Arrays.asList(field1));
        return rs;
    }
}
