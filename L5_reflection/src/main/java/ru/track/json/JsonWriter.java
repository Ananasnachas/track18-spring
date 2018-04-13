package ru.track.json;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * сериализатор в json
 */
public class JsonWriter {

    // В зависимости от типа объекта вызывает соответствующий способ сериализации
    public static String toJson(@Nullable Object object) {
        if (object == null) {
            return "null";
        }

        Class clazz = object.getClass();

        if (clazz.equals(String.class)
                || clazz.equals(Character.class)
                || clazz.isEnum()
                ) {
            return String.format("\"%s\"", object);
        }

        if (object instanceof Boolean || object instanceof Number) {
            return object.toString();
        }

        if (clazz.isArray()) {
            return toJsonArray(object);
        }

        if (object instanceof Collection) {
            return toJsonCollection(object);
        }

        if (object instanceof Map) {
            return toJsonMap(object);
        }

        return toJsonObject(object);
    }

    /**
     * Используется вспомогательный класс {@link Array}, чтобы работать с object instanceof Array
     * <p>
     * То есть чтобы получить i-й элемент массива, нужно вызвать {@link Array#get(Object, int)}, где i - это число от 0 до {@link Array#getLength(Object)}
     *
     * @param object - который Class.isArray()
     * @return строковое представление массива: [item1, item2, ...]
     */
    @NotNull
    private static String toJsonArray(@NotNull Object object) {
        int length = Array.getLength(object);
        // TODO: implement!
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        Object o;
        for (int i = 0; i < length; i++) {
            o = Array.get(object,i);
            if(isNumber(o)){
                sb.append(o);
            }
            else{
                sb.append('"');
                sb.append(o);
                sb.append('"');
            }
            if(i<length-1)
                sb.append(',');
        }
        sb.append(']');
        return new String(sb);
    }

    private static boolean isNumber(Object o) {
        Integer number = null;
        try {
            number = new Integer(o.toString());
        } catch (Exception ignored) {
        }
        return number != null;
    }

    /**
     * В 1 шаг приводится к Collection
     */
    @NotNull
    private static String toJsonCollection(@NotNull Object object) {
        Collection collection = (Collection) object;
        return toJsonArray(collection.toArray());
    }

    /**
     * Сконвертить мап в json. Формат:
     * {key:value, key:value,..}
     * <p>
     * На входе мы проверили, что это Map, можно просто кастовать Map map = (Map) object;
     */
    @NotNull
    private static String toJsonMap(@NotNull Object object) {
        // TODO: implement!
        HashMap map = (HashMap)object;
        Object[] keys = map.keySet().toArray();
        Object[] values = map.values().toArray();
        Map<String,String> stringMap = new LinkedHashMap<>();
        for (int i = 0; i < keys.length; i++) {
            stringMap.put(keys[i].toString(), toJson(values[i]));
        }
        return formatObject(stringMap);
        // Можно воспользоваться этим методом, если сохранить все поля в новой мапе уже в строковом представлении
//        return formatObject(stringMap);
    }

    /**
     * 1) Чтобы распечатать объект, нужно знать его внутреннюю структуру, для этого нужно получить его Class-объект:
     * {@link Class} с помощью {@link Object#getClass()}
     * <p>
     * Получить поля класса можно с помощью {@link Class#getDeclaredFields()}
     * Приватные поля недоступны, нужно изменить в рантайм их accessibility: {@link Field#setAccessible(boolean)}
     * <p>
     * 2) Вторая часть задачи: {@link JsonNullable} и {@link SerializedTo}
     * Нужно проверить, что у класса/поля есть аннотация
     * <p>
     * {@link Class#getAnnotation(Class)} / {@link Field#getAnnotation(Class)}
     * и в зависимости от этого изменить поведение
     * <p>
     * NOTE: Удобно сложить все поля объекта в Map<String, String> то етсь {имя поля -> значение поля в json}
     * и воспользоваться методом {@link #formatObject(Map)}
     */
    @NotNull
    private static String toJsonObject(@NotNull Object object){
        boolean isClassNullable = false;
        Class clazz = object.getClass();
        // TODO: implement!
        Map<String,String> stringMap = new LinkedHashMap<>();
        Field[] fields = clazz.getDeclaredFields();
        String s = "";
        for (Annotation a :clazz.getAnnotations()) {
            if(a.toString().equals("@ru.track.json.JsonNullable()")){
                isClassNullable = true;
            }
            if(a.toString().equals("")){
            }
        }
        for (Field field : fields) {
            boolean isFieldSerialized = false;
            field.setAccessible(true);
            try {
                s = toJson(field.get(object));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            String value = "";
            for (Annotation a :field.getAnnotations()) {
                if(a.toString().startsWith("@com.google.gson.annotations.SerializedName(")){
                    value = a.toString().replaceFirst("@com\\.google\\.gson\\.annotations\\.SerializedName\\(alternate=\\[], value=", "")
                            .replaceFirst("\\)","");
                    isFieldSerialized = true;

                }
            }
            if(!isFieldSerialized) {
                if (isClassNullable) {
                    stringMap.put(field.getName(), s);
                } else {
                    if (!s.equals("null")) {
                        stringMap.put(field.getName(), s);
                    }
                }
            }
            else{
                if (isClassNullable) {
                    stringMap.put(value, s);
                } else {
                    if (!s.equals("null")) {
                        stringMap.put(value, s);
                    }
                }
            }
        }
        return formatObject(stringMap);
    }

    /**
     * Вспомогательный метод для форматирования содержимого Map<K, V>
     *
     * @param map
     * @return "{key:value, key:value,..}"
     */
    @NotNull
    private static String formatObject(@NotNull Map<String, String> map) {
        String r = String.join(",", map.entrySet().stream()
                .map(e -> String.format("\"%s\":%s", e.getKey(), e.getValue()))
                .collect(Collectors.toList())
        );

        return String.format("{%s}", r);
    }

}
