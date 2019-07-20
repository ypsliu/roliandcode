package com.roiland.platform.template.core.support;

import junit.framework.TestCase;

public class TypeSupportTest extends TestCase {
    public void testGetType() {

        String type = "java.util.List<String>";
        assertEquals("[collection type; class java.util.List, contains [simple type, class java.lang.String]]", TypeSupport.getJavaType(type).toString());

        type = "List<String>";
        assertEquals("[collection type; class java.util.List, contains [simple type, class java.lang.String]]", TypeSupport.getJavaType(type).toString());

        type = "HashMap<String,Object>";
        assertEquals("[map type; class java.util.HashMap, [simple type, class java.lang.String] -> [simple type, class java.lang.Object]]", TypeSupport.getJavaType(type).toString());

        //FIXME
        type = "HashMap";
        try {
            TypeSupport.getJavaType(type);
            fail();
        } catch (Exception ignored) {
        }

        type = "HashMap<HashMap<String,Map<String,List<String>>>,HashMap<String,Map<String,List<String>>>>";
        assertEquals("[map type; class java.util.HashMap, [map type; class java.util.HashMap, [simple type, class java.lang.String] -> [map type; class java.util.Map, [simple type, class java.lang.String] -> [collection type; class java.util.List, contains [simple type, class java.lang.String]]]] -> [map type; class java.util.HashMap, [simple type, class java.lang.String] -> [map type; class java.util.Map, [simple type, class java.lang.String] -> [collection type; class java.util.List, contains [simple type, class java.lang.String]]]]]", TypeSupport.getJavaType(type).toString());

        type = "Array<Array<int>>";
        assertEquals("[array type, component type: [array type, component type: [simple type, class int]]]", TypeSupport.getJavaType(type).toString());

        type = "Array<Map<String,Integer>>";
        assertEquals("[array type, component type: [map type; class java.util.Map, [simple type, class java.lang.String] -> [simple type, class java.lang.Integer]]]", TypeSupport.getJavaType(type).toString());

        //FIXME
        type = "LinkedHashMap<String,int>";
        assertEquals("[map type; class java.util.LinkedHashMap, [simple type, class java.lang.String] -> [simple type, class int]]", TypeSupport.getJavaType(type).toString());

        type = "com.roiland.platform.template.support.Bean3";
        try {
            TypeSupport.getJavaType(type);
            fail();
        } catch (Exception ignored) {
        }

        type = "com.roiland.platform.template.core.support.Bean1";
        assertEquals("[simple type, class com.roiland.platform.template.core.support.Bean1]", TypeSupport.getJavaType(type).toString());

        type = "com.roiland.platform.template.core.support.Bean2";
        assertEquals("[simple type, class com.roiland.platform.template.core.support.Bean2]", TypeSupport.getJavaType(type).toString());

        //FIXME
        type = "List<? extends Object>";
        try {
            TypeSupport.getJavaType(type);
            fail();
        } catch (Exception ignored) {
        }

        //FIXME
        type = "List<? super com.roiland.platform.template.core.support.Bean1>";
        try {
            TypeSupport.getJavaType(type);
            fail();
        } catch (Exception ignored) {
        }

        type = "List<?>";
        assertEquals("[collection type; class java.util.List, contains [simple type, class java.lang.Object]]",TypeSupport.getJavaType(type).toString());
    }

    public void testNormalize(){
        String type = "Map<String,List<Array<List<Array<int>>>>>";
        assertEquals("java.util.Map<java.lang.String,java.util.List<java.util.List<int[]>[]>>", TypeSupport.normalize(type));

        type = "Array<Array<Map<String,List<Array<List<Array<int>>>>>>>";
        assertEquals("java.util.Map<java.lang.String,java.util.List<java.util.List<int[]>[]>>[][]",TypeSupport.normalize(type));
    }

}