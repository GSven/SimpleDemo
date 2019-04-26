package com.gsven.library.utils.praserutils;

import android.database.Cursor;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Gsven
 * @Date 2019/4/26 14:41
 * @Desc Json解析类，
 * 依靠注解，关联实体类属性和服务器数据的字段名。基于行情的Json解析自动打包开发而成。
 * Announcements：使用此类时，实体类中的各个属性必须加上注解，注解必须和该属性对应的服务器返回字段一致
 */
public class JsonParseUtils {

    public static <T> T createBean(Class<T> tClass, JSONObject jsonObject) {
        return BeanUtils.createBean(getJsonParamsByAnnotation(tClass), tClass, jsonObject);
    }

    public static <T> ArrayList<T> createBeanList(Class<T> tClass, JSONArray jsonArray) {
        ArrayList<T> resultList = new ArrayList<T>();
        HashMap<String, String> jsonParamsHashMap = getJsonParamsByAnnotation(tClass);
        int jsonArrayLength = jsonArray.length();
        JSONObject tempJsonObject;
        try {
            for (int i = 0; i < jsonArrayLength; i++) {
                tempJsonObject = jsonArray.getJSONObject(i);
                resultList.add(BeanUtils.createBean(jsonParamsHashMap, tClass, tempJsonObject));
            }
        } catch (JSONException je) {
            je.printStackTrace();
        }
        return resultList;
    }

    public static <T> HashMap<String, String> getJsonParamsByAnnotation(Class<T> tClass) {
        Field[] fields = tClass.getDeclaredFields();
        HashMap<String, String> resultHashMap = new HashMap<String, String>();
        for (Field field : fields) {
            if (field.isAnnotationPresent(JsonKey.class)) {
                JsonKey jsonKeyAnnotation = field.getAnnotation(JsonKey.class);
                resultHashMap.put(field.getName(), jsonKeyAnnotation.value());
            }
        }
        return resultHashMap;
    }

    /**
     * list转jsonarray
     *
     * @param list
     * @return
     */
    public static JSONArray parseListToJsonArray(List<?> list) {
        JSONArray jsonArray = new JSONArray();
        Gson gson = new Gson();
        try {
            jsonArray = new JSONArray(gson.toJson(list));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonArray;
    }
}

/**
 * 根据一个装有前端实体类属性名和服务器json数据字段key的对应关系的map，来解析Json，获取实体类对象或实体类List
 * 来自行情模块
 */
class BeanUtils {

    /**
     * 创建一个豆子
     *
     * @param mapper 豆子与jsonObject中的key对应关系,豆子属性名作为Key,Json中的Key作为Value
     * @param clz    豆子的class对象
     * @param cursor 需要打包的JsonObject对象
     * @return
     */
    public static <T> T createBean(Map mapper, Class<T> clz, Cursor cursor) {
        Set keySet = mapper.keySet();
//        List<T> resultList = new ArrayList<T>();
        Object obj = null;
        try {
            obj = clz.newInstance();
            Iterator<String> it = keySet.iterator();
            while (it.hasNext()) {
                String name = it.next();

                Field f = null;
                try {
                    f = getField(clz, name, 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (f != null) {
                    f.setAccessible(true);
                    Object value = cursor.getString(cursor.getColumnIndex((String) mapper.get(name)));
                    setDataToField(f, obj, value);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (T) obj;
    }

    /**
     * 创建一个豆子
     *
     * @param mapper     豆子与jsonObject中的key对应关系,豆子属性名作为Key,Json中的Key作为Value
     * @param clz        豆子的class对象
     * @param jsonObject 需要打包的JsonObject对象
     * @return
     */
    public static <T> T createBean(Map mapper, Class<T> clz, JSONObject jsonObject) {
        Set keySet = mapper.keySet();
//        List<T> resultList = new ArrayList<T>();
        Object obj = null;
        try {
            obj = clz.newInstance();
            Iterator<String> it = keySet.iterator();
            while (it.hasNext()) {
                String name = it.next();

                Field f = null;
                try {
                    f = getField(clz, name, 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (f != null) {
                    f.setAccessible(true);
                    String arrayTest = "";
                    try {
                        arrayTest = String.valueOf(mapper.get(name));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (arrayTest.split(",").length > 1) {//集合对象赋值
                        String[] array = arrayTest.split(",");
                        if (f.getType().isAssignableFrom(Map.class)) {//如果是Map进行Map赋值,array单元规范 key:jsonKey,key:jsonKey,...
                            HashMap map = new HashMap();
                            for (int i = 0; i < array.length; i++) {
                                try {
                                    String key = array[i].split(":")[0];
                                    String jsonKey = array[i].split(":")[1];
                                    Object value = jsonObject.get(jsonKey);
                                    map.put(key, value);
                                } catch (Exception e) {
                                }
                            }
                            setDataToField(f, obj, map);
                        } else if (f.getType().isAssignableFrom(List.class)) {//如果是List进行List集合赋值,array单元规范 jsonKey,jsonKey,...
                            ArrayList list = new ArrayList();
                            for (int i = 0; i < array.length; i++) {
                                try {
                                    Object value = jsonObject.get(array[i]);
                                    list.add(value);
                                } catch (Exception e) {
                                }
                            }
                            setDataToField(f, obj, list);
                        }
                    } else {//普通对象赋值
                        try {
                            Object value = jsonObject.get((String) mapper.get(name));
                            setDataToField(f, obj, value);
                        } catch (Exception e) {
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (T) obj;
    }

    /**
     * 创建一个豆子
     *
     * @param mapper    豆子与jsonArray中的key对应关系,豆子属性名作为Key,Json中的下标作为Value
     * @param clz       豆子的class对象
     * @param jsonArray 需要打包的jsonArray对象
     * @return
     */
    public static <T> T createBean(Map mapper, Class<T> clz, JSONArray jsonArray) {
        Set keySet = mapper.keySet();
//        List<T> resultList = new ArrayList<T>();
        Object obj = null;
        try {
            obj = clz.newInstance();
            Iterator<String> it = keySet.iterator();
            while (it.hasNext()) {
                String name = it.next();
                Field f = null;
                try {
                    f = getField(clz, name, 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (f != null) {
                    f.setAccessible(true);
                    String arrayTest = "";
                    try {
                        arrayTest = String.valueOf(mapper.get(name));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    String[] array = arrayTest.split(",");
                    if (f.getType().isAssignableFrom(Map.class)) {//如果是Map进行Map赋值,array单元规范 key:下标,key:下标,...
                        HashMap map = new HashMap();
                        String key;
                        String index;
                        for (int i = 0; i < array.length; i++) {
                            try {
                                key = array[i].split(":")[0];
                                index = array[i].split(":")[1];
                                Object value = jsonArray.get(Integer.parseInt(index));
                                map.put(key, value);
                            } catch (Exception e) {
                            }
                        }
                        setDataToField(f, obj, map);
                    } else if (f.getType().isAssignableFrom(List.class)) {//如果是List进行List集合赋值,array单元规范 下标,下标,...
                        ArrayList list = new ArrayList();
                        for (int i = 0; i < array.length; i++) {
                            try {
                                Object value = jsonArray.get(Integer.parseInt(array[i]));
                                list.add(value);
                            } catch (Exception e) {

                            }
                        }
                        setDataToField(f, obj, list);
                    } else {//普通对象赋值
                        try {
                            Object value = jsonArray.get((Integer) mapper.get(name));
                            setDataToField(f, obj, value);
                        } catch (Exception e) {
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (T) obj;
    }

    /**
     * 生成一堆豆子
     *
     * @param mapper 豆子与jsonObject中的key对应关系,豆子属性名作为Key,Json中的下标作为Value
     * @param clz    豆子的class对象
     * @param Array  jsonArray数组,包含JsonObject和JsonArray的
     * @return
     */
    public static <T> ArrayList<T> createBeanList(Map mapper, Class<T> clz, JSONArray Array) {
        ArrayList<T> result = new ArrayList<T>();
        try {
            for (int i = 0; i < Array.length(); i++) {
                Object json = Array.get(i);
                if (json instanceof JSONArray) {
                    JSONArray jsonArray = (JSONArray) json;
                    result.add(createBean(mapper, clz, jsonArray));
                } else if (json instanceof JSONObject) {
                    JSONObject jsonObject = (JSONObject) json;
                    result.add(createBean(mapper, clz, jsonObject));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static Field getField(Class clz, String name, int deep) throws RuntimeException {
        if (deep > 5) {
            throw new RuntimeException(clz.getSimpleName() + " is too deep ,can't get field , please shallower and try again");
        }
        Field f;
        try {
            f = clz.getDeclaredField(name);
        } catch (NoSuchFieldException e) {
            f = getField(clz.getSuperclass(), name, deep++);
        }
        return f;
    }

    private static void setDataToField(Field f, Object obj, Object value) {
        try {
            String value_str = String.valueOf(value);
            if ("String".equals(f.getType().getSimpleName())) {
                try {
                    f.set(obj, value_str);
                } catch (IllegalArgumentException e) {
                    f.set(obj, "");
                }
            } else if ("Double".equalsIgnoreCase(f.getType().getSimpleName())) {
                try {
                    f.setDouble(obj, Double.parseDouble(value_str));
                } catch (IllegalArgumentException e) {
                    f.setDouble(obj, 0D);
                }
            } else if ("Integer".equals(f.getType().getSimpleName()) || "int".equals(f.getType().getSimpleName())) {
                try {
                    f.setInt(obj, Integer.parseInt(value_str));
                } catch (IllegalArgumentException e) {
                    f.setInt(obj, 0);
                }
            } else if ("Char".equalsIgnoreCase(f.getType().getSimpleName())) {
                try {
                    f.setChar(obj, value_str.charAt(0));
                } catch (IllegalArgumentException e) {
                    f.setChar(obj, '0');
                }
            } else if ("Float".equalsIgnoreCase(f.getType().getSimpleName())) {
                try {
                    f.setFloat(obj, Float.parseFloat(value_str));
                } catch (IllegalArgumentException e) {
                    f.setFloat(obj, 0F);
                }
            } else if ("Boolean".equalsIgnoreCase(f.getType().getSimpleName())) {
                try {
                    f.setBoolean(obj, Boolean.parseBoolean(value_str));
                } catch (IllegalArgumentException e) {
                    f.setBoolean(obj, false);
                }
            } else {
                f.set(obj, value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
