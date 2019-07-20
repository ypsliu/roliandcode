package com.roiland.platform.zookeeper;

/**
 * @author leon.chen
 * @since 2016/6/2
 */
public interface PropertyListener {

    /**
     * 当设置有效属性的时候会notify此方法
     *
     * @param properties 扩展用
     * @param key
     * @param value
     * @param from
     */
    public void onPropertySet(RoilandProperties properties, String key, String value, String from);

    /**
     * 当所有属性设置完毕的时候会notify此方法
     */
    public void onPropertyDone(RoilandProperties properties);

    public static class Adaptor implements PropertyListener {

        @Override
        public void onPropertySet(RoilandProperties properties, String key, String value, String from) {

        }

        @Override
        public void onPropertyDone(RoilandProperties properties) {

        }
    }
}
