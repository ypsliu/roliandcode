package com.roiland.platform.template.core.resources;

import java.util.HashMap;
import java.util.Map;

/**
 * @author leon.chen
 * @since 2016/4/8
 */
public interface IVehService {
    Map<String,Object> getVehInfo(String bid, String cnum, String ttt, String[] field1, HashMap<String, String> field2);
}
