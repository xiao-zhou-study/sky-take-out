package com.sky.handler;

import com.sky.constant.AutoFillConstant;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class AutoFillHandler {

    private static final Map<Class<?>, Map<String, Method>> METHOD_CACHE = new ConcurrentHashMap<>();

    /**
     * 填充插入操作需要的字段
     */
    public static void fillInsertFields(Object entity, Long currentUserId, LocalDateTime currentTime) {
        if (entity == null) {
            return;
        }

        Class<?> clazz = entity.getClass();
        Map<String, Method> methodMap = getMethodMap(clazz);

        invokeMethod(methodMap, entity, AutoFillConstant.SET_CREATE_TIME, currentTime);
        invokeMethod(methodMap, entity, AutoFillConstant.SET_CREATE_USER, currentUserId);
        invokeMethod(methodMap, entity, AutoFillConstant.SET_UPDATE_TIME, currentTime);
        invokeMethod(methodMap, entity, AutoFillConstant.SET_UPDATE_USER, currentUserId);
    }

    /**
     * 填充更新操作需要的字段
     */
    public static void fillUpdateFields(Object entity, Long currentUserId, LocalDateTime currentTime) {
        if (entity == null) {
            return;
        }

        Class<?> clazz = entity.getClass();
        Map<String, Method> methodMap = getMethodMap(clazz);

        invokeMethod(methodMap, entity, AutoFillConstant.SET_UPDATE_TIME, currentTime);
        invokeMethod(methodMap, entity, AutoFillConstant.SET_UPDATE_USER, currentUserId);
    }

    /**
     * 获取类的方法映射（带缓存）
     */
    private static Map<String, Method> getMethodMap(Class<?> clazz) {
        return METHOD_CACHE.computeIfAbsent(clazz, c -> {
            // 内部 Map 也使用 ConcurrentHashMap
            Map<String, Method> map = new ConcurrentHashMap<>();

            // 收集常用的自动填充方法
            collectMethod(map, c, AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
            collectMethod(map, c, AutoFillConstant.SET_CREATE_USER, Long.class);
            collectMethod(map, c, AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
            collectMethod(map, c, AutoFillConstant.SET_UPDATE_USER, Long.class);

            return map;
        });
    }

    /**
     * 收集单个方法到映射中
     */
    private static void collectMethod(Map<String, Method> map, Class<?> clazz, String methodName, Class<?>... parameterTypes) {
        try {
            Method method = clazz.getDeclaredMethod(methodName, parameterTypes);
            method.setAccessible(true);
            map.put(methodName, method);
        } catch (Exception e) {
            log.debug("类 {} 中未找到方法 {}", clazz.getName(), methodName);
        }
    }

    /**
     * 调用方法（如果存在）
     */
    private static void invokeMethod(Map<String, Method> methodMap, Object entity, String methodName, Object value) {
        Method method = methodMap.get(methodName);
        if (method != null) {
            try {
                method.invoke(entity, value);
                log.trace("成功调用 {} 的 {} 方法", entity.getClass().getSimpleName(), methodName);
            } catch (Exception e) {
                log.error("调用 {} 的 {} 方法失败", entity.getClass().getSimpleName(), methodName, e);
            }
        } else {
            log.debug("{} 方法不存在，跳过填充", methodName);
        }
    }
}