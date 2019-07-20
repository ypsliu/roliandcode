package com.roiland.platform.template.core.support;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.*;

/**
 * @author leon.chen
 * @since 2016/4/5
 */
public class TypeSupport {

    private static final Log LOGGER = LogFactory.getLog(TypeSupport.class);

    private static final Map<String, Class<?>> buildInBasic = new HashMap<>();

    private static final Map<String, Class<?>> buildInCollection = new HashMap<>();

    private static final Map<String, Class<?>> buildInMap = new HashMap<>();

    private static final Map<String, Class<?>> buildInArray = new HashMap<>();

    //context
    static {
        //内置类型
        buildInBasic.put("String", String.class);
        buildInBasic.put("Integer", Integer.class);
        buildInBasic.put("Long", Long.class);
        buildInBasic.put("Double", Double.class);
        buildInBasic.put("Float", Float.class);
        buildInBasic.put("Short", Short.class);
        buildInBasic.put("Char", Character.class);
        buildInBasic.put("Byte", Byte.class);
        buildInBasic.put("Boolean", Boolean.class);
        buildInBasic.put("BigDecimal", BigDecimal.class);
        buildInBasic.put("BigInteger", BigInteger.class);
        buildInBasic.put("Date", Date.class);
        buildInBasic.put("Timestamp", Timestamp.class);
        buildInBasic.put("Object", Object.class);
        buildInBasic.put("Class", Class.class);
        //特殊
        buildInBasic.put("?", Object.class);
        buildInBasic.put("void", void.class);

        buildInBasic.put("int", int.class);
        buildInBasic.put("long", long.class);
        buildInBasic.put("double", double.class);
        buildInBasic.put("float", float.class);
        buildInBasic.put("short", short.class);
        buildInBasic.put("char", char.class);
        buildInBasic.put("byte", byte.class);
        buildInBasic.put("boolean", boolean.class);

        //內置Map类型
        buildInMap.put("Map", Map.class);
        buildInMap.put("LinkedHashMap", LinkedHashMap.class);
        buildInMap.put("HashMap", HashMap.class);

        //内置集合类型
        buildInCollection.put("Set", Set.class);
        buildInCollection.put("List", List.class);
        buildInCollection.put("Collection", Collection.class);
        buildInCollection.put("HashSet", HashSet.class);
        buildInCollection.put("LinkedHashSet", LinkedHashSet.class);
        buildInCollection.put("ArrayList", ArrayList.class);
        buildInCollection.put("LinkedList", LinkedList.class);

        //内置类型数组
        buildInArray.put("Array", null);
    }

    private static Type getBasicType(String type) {
        for (Map.Entry<String, Class<?>> entry : buildInBasic.entrySet()) {
            if (type.equals(entry.getKey())) {
                return Type.type(entry.getValue().getCanonicalName(), entry.getValue(), TypeEnum.SIMPLE);
            }
        }
        return Type.type(type, null, TypeEnum.SIMPLE);
    }

    private static Type getArrayType(String type) {
        return Type.type(null, null, TypeEnum.ARRAY, getType(type));
    }

    private static Type getCollectionType(Class<?> clazz, String type) {
        return Type.type(clazz.getCanonicalName(), clazz, TypeEnum.COLLECTION, getType(type));
    }

    private static Type getMapType(Class<?> clazz, String type) {
        List<String> list = split(type);
        if (list.size() != 2) {
            throw new UnsupportedOperationException("Generic Type Parse Error");
        }
        String keyType = list.get(0).trim();
        String valueType = list.get(1).trim();
        return Type.type(clazz.getCanonicalName(), clazz, TypeEnum.MAP, getType(keyType), getType(valueType));
    }

    private static Type getType(String type) {
        type = type.trim();
        if (type.indexOf('<') == -1 || type.indexOf('>') == -1) {
            return getBasicType(type);
        }
        String prefix = type.substring(0, type.indexOf('<'));
        int start = type.indexOf('<') + 1;
        int end = type.lastIndexOf('>');
        type = type.substring(start, end).trim();

        if (buildInArray.containsKey(prefix)) {
            return getArrayType(type);
        } else if (buildInCollection.containsKey(prefix)) {
            return getCollectionType(buildInCollection.get(prefix), type);
        } else if (buildInMap.containsKey(prefix)) {
            return getMapType(buildInMap.get(prefix), type);
        } else {
            List<String> list = split(type);
            Type[] generics = new Type[list.size()];
            for (int i = 0; i < list.size(); i++) {
                generics[i] = getType(list.get(i));
            }
            return Type.type(prefix, null, TypeEnum.SIMPLE, generics);
        }
    }

    public static JavaType getJavaType(String type) {
        return getType(type).toJavaType();
    }

    /**
     * @param type example : "A,B<C,D>,E"
     * @return new String[]{"A","B<C,D>","E"}
     */
    private static List<String> split(String type) {
        char[] ary = type.toCharArray();
        int balance = 0;
        List<String> list = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        for (char c : ary) {
            switch (c) {
                case '<':
                    balance++;
                    sb.append(c);
                    break;
                case '>':
                    balance--;
                    sb.append(c);
                    break;
                case ',':
                    if (balance == 0) {
                        list.add(sb.toString());
                        sb = new StringBuilder();
                    } else {
                        sb.append(c);
                    }
                    break;
                default:
                    sb.append(c);

            }
        }
        if (balance != 0) {
            throw new UnsupportedOperationException("Parse Generic Exception " + type);
        }
        list.add(sb.toString());
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(list);
        }
        return list;
    }

    public static String normalize(String type) {
        return getType(type).normalize();
    }

    private enum TypeEnum {
        ARRAY, MAP, COLLECTION, SIMPLE
    }

    private static class Type {

        //factory
        public static Type type(String typeName, Class<?> clazz, TypeEnum typeEnum, Type... generic) {
            return new Type(typeName, clazz, typeEnum, generic);
        }

        public String typeName;

        public TypeEnum typeEnum;

        public Class<?> clazz;

        public Type[] generic = new Type[0];

        public Type(String typeName, Class<?> clazz, TypeEnum typeEnum, Type... generic) {
            if (typeName != null && (typeName.indexOf('<') >= 0 || typeName.indexOf('>') >= 0 || typeName.indexOf(',') >= 0)) {
                throw new UnsupportedOperationException("Type parser error " + typeName);
            }
            this.typeName = typeName;
            this.typeEnum = typeEnum;
            this.clazz = clazz;
            if (generic.length != 0) {
                this.generic = generic;
            }
        }

        public boolean hasGeneric() {
            return generic != null && generic.length > 0;
        }

        public String normalize() {
            StringBuilder builder = new StringBuilder();
            normalize(this, builder);
            return builder.toString();
        }

        public JavaType toJavaType() {
            try {
                switch (typeEnum) {
                    case SIMPLE:
                        //带泛型
                        if (hasGeneric()) {
                            JavaType[] typeAry = new JavaType[generic.length];
                            for (int i = 0; i < generic.length; i++) {
                                typeAry[i] = generic[i].toJavaType();
                            }
                            return TypeFactory.defaultInstance().constructParametricType(getTypeClass(), typeAry);
                        } else {
                            //不带泛型
                            return TypeFactory.defaultInstance().constructType(getTypeClass());
                        }
                    case ARRAY:
                        return ArrayType.construct(generic[0].toJavaType(), null);
                    case MAP:
                        return TypeFactory.defaultInstance().constructMapLikeType(getTypeClass(), generic[0].toJavaType(), generic[1].toJavaType());
                    case COLLECTION:
                        return TypeFactory.defaultInstance().constructCollectionLikeType(getTypeClass(), generic[0].toJavaType());
                    default:
                        throw new AssertionError("Un-reachable");
                }
            } catch (ClassNotFoundException e) {
                throw new UnsupportedOperationException(e);
            }
        }

        public Class<?> getTypeClass() throws ClassNotFoundException {
            if (clazz != null) {
                return clazz;
            }
            return Class.forName(typeName, false, TypeSupport.class.getClassLoader());
        }

        public void normalize(Type type, StringBuilder builder) {
            if (type.typeEnum == TypeEnum.ARRAY) {
                for (Type generic : type.generic) {
                    normalize(generic, builder);
                }
                builder.append("[]");
            } else {
                builder.append(type.typeName);
                if (type.hasGeneric()) {
                    builder.append("<");
                    Type[] generics = type.generic;
                    for (int i = 0; i < generics.length; ) {
                        normalize(generics[i], builder);
                        if (++i < generics.length) {
                            builder.append(",");
                        }
                    }
                    builder.append(">");
                }
            }
        }
    }
}
