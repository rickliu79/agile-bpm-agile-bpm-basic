package com.dstz.base.core.executor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.dstz.base.api.executor.Executor;
import com.dstz.base.core.util.AppUtil;

/**
 * 执行器的服务类
 *
 * @author aschs
 */
public class ExecutorService {
    private ExecutorService() {

    }

    /**
     * <pre>
     * 初始化map方法
     * </pre>
     */
    private static <T> Map<String, Executor<T>> executorMap(Class<? extends Executor<T>> cls) {
        Map<String, Executor<T>> executorMap = new LinkedHashMap<>();
        Map<String, ? extends Executor<T>> map = AppUtil.getImplInstance(cls);
        List<Executor<T>> list = new ArrayList<>();
        for (Entry<String, ? extends Executor<T>> entry : map.entrySet()) {
            Executor<T> plugin = entry.getValue();
            list.add(plugin);
        }
        Collections.sort(list);
        for (Executor<T> plugin : list) {
            executorMap.put(plugin.getKey(), plugin);
        }
       
        return executorMap;
    }

    /**
     * <pre>
     * 调用指定插件服务
     * </pre>
     *
     * @param cls   要运行的插件类，可以是具体的某个指定插件，也可以是一个抽象的父插件
     * @param param 参数
     */
    public static <T> void execute(Class<? extends Executor<T>> cls, T param) {
        for (Entry<String, Executor<T>> entry : executorMap(cls).entrySet()) {
            Executor<T> executor = entry.getValue();
            executor.execute(param);
        }
    }
}
