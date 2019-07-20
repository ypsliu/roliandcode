package com.roiland.platform.dbutils.helper;

/**
 * 类名：MySQLUtils<br/>
 * 作用：MySQL工具，包含SQL格式化（format）<br/>
 *
 * @author 杨昆
 * @since 2015/6/1
 */
public final class SQLHelper {

    public static String schema(String schema, String key) {
        return schema + (key != null? Math.abs(key.hashCode() % 1024): "");
    }
    
    /**
     * 将名称中大写字母替换成对应分隔符加小写形式。<br>
     * {@code VehCurrTraffic => veh_curr_traffic}
     * @param name 转换前名称
     * @param c 分隔符
     * @return 转换后名称
     */
	public static String decode(String name, char c) {
		StringBuilder builder = new StringBuilder();
		char[] charName = name.toCharArray();
		char curr;
		for (int i = 0, size = charName.length; i < size; i++ ) {
			curr = charName[i];
			if (i == 0 && curr >= 'A' && curr <= 'Z') {
				builder.append(Character.toLowerCase(curr));
			} else if (curr >= 'A' && curr <= 'Z') {
				builder.append(c).append(Character.toLowerCase(curr));
			} else {
				builder.append(curr);
			}
		}
		return builder.toString();
	}
	
    /**
     * 将名称中分隔符加小写形式替换成对应大写字母。<br>
     * {@code veh_curr_traffic => VehCurrTraffic}
     * @param name 转换前名称
     * @param c 分隔符
     * @return 转换后名称
     */
	public static String encode(String name, char c) {
		StringBuilder builder = new StringBuilder();
		char[] charName = name.toCharArray();
		char curr;
		for (int i = 0, size = charName.length; i < size; i++ ) {
			curr = charName[i];
			if (i == 0 && curr >= 'a' && curr <= 'z') {
				builder.append(Character.toUpperCase(curr));
			} else if (i > 0 && curr == c) {
				builder.append(Character.toUpperCase(charName[++i]));
			} else {
				builder.append(curr);
			}
		}
		return builder.toString();
	}
}
