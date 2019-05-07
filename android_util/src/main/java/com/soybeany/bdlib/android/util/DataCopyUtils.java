package com.soybeany.bdlib.android.util;


import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * 数据拷贝工具类
 */
public class DataCopyUtils {

    /**
     * 使指定字段忽略赋值
     */
    public static final String IGNORE = null;

    private static final String TAG = DataCopyUtils.class.getSimpleName();

    /**
     * 拷贝公开的值
     *
     * @param source 源对象
     * @param target 目标对象
     */
    public static void copyPublic(ICopyable source, Object target) {
        copyPublic(source, target, null);
    }

    /**
     * 拷贝公开的值
     *
     * @param source 源对象
     * @param target 目标对象
     * @param keyMap 名字不同的键的映射，键：源； 值：目标
     */
    public static void copyPublic(ICopyable source, Object target, Map<String, String> keyMap) {
        ICopyable sourceCopy = source.copy();
        Class sourceClazz = source.getClass();
        Class targetClazz = target.getClass();
        Field[] sourceFields = sourceClazz.getFields();

        if (null == keyMap) {
            keyMap = new HashMap<>();
        }

        String sourceKey; // 源键
        String targetKey; // 目标键
        Object value = null; // 目标值
        Field targetField; // 目标变量

        for (Field sourceField : sourceFields) {
            // 获得相关键
            sourceKey = sourceField.getName();
            targetKey = keyMap.get(sourceKey);
            if (null == targetKey) {
                targetKey = sourceKey;
            }

            try {
                // 设置目标键
                targetField = targetClazz.getField(targetKey);

                // 设置可访问性
                sourceField.setAccessible(true);
                targetField.setAccessible(true);

                // 设置目标值
                value = sourceField.get(sourceCopy);
                targetField.set(target, value);
            } catch (NoSuchFieldException e) {
                LogUtils.w(TAG, targetClazz.getSimpleName() + "没有这个属性：" + sourceKey + ", value = " + value + "（忽略，继续下一个属性）");
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                LogUtils.w(TAG, "非法访问：" + e.getMessage());
            }
        }

    }

    /**
     * 可复制接口，自主决定使用浅复制还是深复制
     */
    public interface ICopyable extends Cloneable {
        /**
         * 复制
         */
        ICopyable copy();
    }

}
