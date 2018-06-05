package com.dstz.sys.scheduler;

import com.dstz.base.core.util.AppUtil;
import com.dstz.sys.core.model.SysScheduleJob;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;
import org.springframework.util.PatternMatchUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 任务执行工具
 *
 * @author didi
 */
class JobInvokeUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobInvokeUtil.class);

    /**
     * 执行方法
     *
     * @param sysScheduleJob 系统任务
     */
    public static void invokeMethod(SysScheduleJob sysScheduleJob) throws Exception {
        final String invokeTarget = sysScheduleJob.getInvokeTarget();
        Assert.notNull(invokeTarget, "执行目标方法为空");
        //如果类名超过两个调用视为静态方法
        if (StringUtils.countMatches(invokeTarget, ".") > 2) {
            invokeStaticMethod(invokeTarget);
        } else {
            invokeSpringBean(invokeTarget);
        }
    }

    /**
     * 调用spring bean方法
     *
     * @param invokeTarget 目标调用
     */
    private static void invokeSpringBean(String invokeTarget) throws IllegalAccessException, InvocationTargetException {
        final String beanId = invokeTarget.substring(0, invokeTarget.indexOf("."));
        final String methodName = invokeTarget.substring(beanId.length() + 1);
        Object bean = AppUtil.getApplicaitonContext().getBean(beanId);
        Method method = BeanUtils.findDeclaredMethodWithMinimalParameters(bean.getClass(), methodName);
        method.invoke(bean);
    }


    /**
     * 调用静态方法
     *
     * @param invokeTarget 调用目标
     */
    private static void invokeStaticMethod(String invokeTarget) throws ClassNotFoundException, IllegalAccessException, InvocationTargetException {
        String className = invokeTarget.substring(0, invokeTarget.lastIndexOf("."));
        String methodName = invokeTarget.substring(className.length() + 1);
        Class<?> clazz = Class.forName(className);
        Method method = BeanUtils.findDeclaredMethodWithMinimalParameters(clazz, methodName);
        method.invoke(null);
    }
}
