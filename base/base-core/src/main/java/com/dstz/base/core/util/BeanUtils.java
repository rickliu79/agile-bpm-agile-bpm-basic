package com.dstz.base.core.util;

import com.dstz.base.api.model.Tree;
import com.dstz.base.api.query.QueryOP;
import com.dstz.base.core.util.time.DateFormatUtil;
import org.apache.commons.beanutils.*;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.beanutils.converters.LongConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ReflectionUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * BeanUtils的等价类，只是将check exception改为uncheck exception
 *
 * @author badqiu
 */
public class BeanUtils {
    private static Logger logger = LoggerFactory.getLogger(BeanUtils.class);
    /**
     * BeanUtil类型转换器
     */
    public static ConvertUtilsBean convertUtilsBean = new ConvertUtilsBean();

    private static BeanUtilsBean beanUtilsBean = new BeanUtilsBean(
            convertUtilsBean, new PropertyUtilsBean());

    static {
        convertUtilsBean.register(new DateConverter(), Date.class);
        convertUtilsBean.register(new LongConverter(null), Long.class);
    }

    /**
     * 可以用于判断 Map,Collection,String,Array,Long是否为空
     *
     * @param o java.lang.Object.
     * @return boolean.
     */
    @SuppressWarnings("unused")
    public static boolean isEmpty(Object o) {
        if (o == null)
            return true;
        if (o instanceof String) {
            if (((String) o).trim().length() == 0)
                return true;
        } else if (o instanceof Collection) {
            if (((Collection<?>) o).size() == 0)
                return true;
        } else if (o.getClass().isArray()) {
            if (((Object[]) o).length == 0)
                return true;
        } else if (o instanceof Map) {
            if (((Map<?, ?>) o).size() == 0)
                return true;
        }
        return false;

    }

    /**
     * 可以用于判断 Map,Collection,String,Array是否不为空
     *
     * @param c
     * @return
     */
    public static boolean isNotEmpty(Object o) {
        return !isEmpty(o);
    }

    /**
     * 判断对象是否为数字
     *
     * @param o
     * @return
     */
    public static boolean isNumber(Object o) {
        if (o == null)
            return false;
        if (o instanceof Number)
            return true;
        if (o instanceof String) {
            try {
                Double.parseDouble((String) o);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return false;
    }

    /**
     * 封装
     *
     * @param map
     * @param entity
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static Object populateEntity(Map<?, ?> map, Object entity)
            throws IllegalAccessException, InvocationTargetException {
        beanUtilsBean.populate(entity, map);
        return entity;
    }

    /**
     * 根据指定的类名判定指定的类是否存在。
     *
     * @param className
     * @return
     */
    public static boolean validClass(String className) {
        try {
            Class.forName(className);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    /**
     * 判定类是否继承自父类
     *
     * @param cls         子类
     * @param parentClass 父类
     * @return
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static boolean isInherit(Class cls, Class parentClass) {
        return parentClass.isAssignableFrom(cls);
    }

    /**
     * 克隆对象
     *
     * @param bean
     * @return
     */
    public static Object cloneBean(Object bean) {
        try {
            return beanUtilsBean.cloneBean(bean);
        } catch (Exception e) {
            handleReflectionException(e);
            return null;
        }
    }


    /**
     * 输入基类包名，扫描其下的类，返回类的全路径
     *
     * @param basePackages 如：com.dstz
     * @return
     * @throws IllegalArgumentException
     */
    @SuppressWarnings("all")
    public static List<String> scanPackages(String basePackages)
            throws IllegalArgumentException {

        ResourcePatternResolver rl = new PathMatchingResourcePatternResolver();
        MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(
                rl);
        List result = new ArrayList();
        String[] arrayPackages = basePackages.split(",");
        try {
            for (int j = 0; j < arrayPackages.length; j++) {
                String packageToScan = arrayPackages[j];
                String packagePart = packageToScan.replace('.', '/');
                String classPattern = "classpath*:/" + packagePart
                        + "/**/*.class";
                Resource[] resources = rl.getResources(classPattern);
                for (int i = 0; i < resources.length; i++) {
                    Resource resource = resources[i];
                    MetadataReader metadataReader = metadataReaderFactory
                            .getMetadataReader(resource);
                    String className = metadataReader.getClassMetadata()
                            .getClassName();
                    result.add(className);
                }
            }
        } catch (Exception e) {
            new IllegalArgumentException("scan pakcage class error,pakcages:"
                    + basePackages);
        }

        return result;
    }

    /**
     * 拷贝一个bean中的非空属性于另一个bean中
     *
     * @param dest 目标对象
     * @param orig 源对象
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static void copyNotNullProperties(Object dest, Object orig) {

        // Validate existence of the specified beans
        if (dest == null) {
            logger.error("No destination bean specified");
            return;
        }
        if (orig == null) {
            logger.error("No origin bean specified");
            return;
        }

        try {
            // Copy the properties, converting as necessary
            if (orig instanceof DynaBean) {
                DynaProperty[] origDescriptors = ((DynaBean) orig)
                        .getDynaClass().getDynaProperties();
                for (int i = 0; i < origDescriptors.length; i++) {
                    String name = origDescriptors[i].getName();
                    if (beanUtilsBean.getPropertyUtils().isReadable(orig, name)
                            && beanUtilsBean.getPropertyUtils().isWriteable(
                            dest, name)) {
                        Object value = ((DynaBean) orig).get(name);
                        beanUtilsBean.copyProperty(dest, name, value);
                    }
                }
            } else if (orig instanceof Map) {
                Iterator<?> entries = ((Map<?, ?>) orig).entrySet().iterator();
                while (entries.hasNext()) {
                    @SuppressWarnings("rawtypes")
                    Map.Entry entry = (Map.Entry) entries.next();
                    String name = (String) entry.getKey();
                    if (beanUtilsBean.getPropertyUtils()
                            .isWriteable(dest, name)) {
                        beanUtilsBean
                                .copyProperty(dest, name, entry.getValue());
                    }
                }
            } else /* if (orig is a standard JavaBean) */ {
                PropertyDescriptor[] origDescriptors = beanUtilsBean
                        .getPropertyUtils().getPropertyDescriptors(orig);
                for (int i = 0; i < origDescriptors.length; i++) {
                    String name = origDescriptors[i].getName();
                    if ("class".equals(name)) {
                        continue; // No point in trying to set an object's class
                    }
                    if (beanUtilsBean.getPropertyUtils().isReadable(orig, name)
                            && beanUtilsBean.getPropertyUtils().isWriteable(
                            dest, name)) {
                        try {
                            Object value = beanUtilsBean.getPropertyUtils()
                                    .getSimpleProperty(orig, name);
                            if (value != null) {
                                beanUtilsBean.copyProperty(dest, name, value);
                            }
                        } catch (NoSuchMethodException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } catch (Exception ex) {
            handleReflectionException(ex);
        }

    }

    @SuppressWarnings("unchecked")
    public static <T> T copyProperties(Class<T> destClass, Object orig) {
        Object target = null;
        try {
            target = destClass.newInstance();
            copyProperties((Object) target, orig);
            return (T) target;
        } catch (Exception e) {
            handleReflectionException(e);
            return null;
        }
    }

    public static void copyProperties(Object dest, Object orig) {
        try {
            beanUtilsBean.copyProperties(dest, orig);
        } catch (Exception e) {
            handleReflectionException(e);
        }
    }

    public static void copyProperty(Object bean, String name, Object value) {
        try {
            beanUtilsBean.copyProperty(bean, name, value);
        } catch (Exception e) {
            handleReflectionException(e);
        }
    }

    public static Map<?, ?> describe(Object bean) {
        try {
            return beanUtilsBean.describe(bean);
        } catch (Exception e) {
            handleReflectionException(e);
            return null;
        }
    }

    public static String[] getArrayProperty(Object bean, String name) {
        try {
            return beanUtilsBean.getArrayProperty(bean, name);
        } catch (Exception e) {
            handleReflectionException(e);
            return null;
        }
    }

    public static ConvertUtilsBean getConvertUtils() {
        return beanUtilsBean.getConvertUtils();
    }

    public static String getIndexedProperty(Object bean, String name, int index) {
        try {
            return beanUtilsBean.getIndexedProperty(bean, name, index);
        } catch (Exception e) {
            handleReflectionException(e);
            return null;
        }
    }

    public static String getIndexedProperty(Object bean, String name) {
        try {
            return beanUtilsBean.getIndexedProperty(bean, name);
        } catch (Exception e) {
            handleReflectionException(e);
            return null;
        }
    }

    public static String getMappedProperty(Object bean, String name, String key) {
        try {
            return beanUtilsBean.getMappedProperty(bean, name, key);
        } catch (Exception e) {
            handleReflectionException(e);
            return null;
        }
    }

    public static String getMappedProperty(Object bean, String name) {
        try {
            return beanUtilsBean.getMappedProperty(bean, name);
        } catch (Exception e) {
            handleReflectionException(e);
            return null;
        }
    }

    public static String getNestedProperty(Object bean, String name) {
        try {
            return beanUtilsBean.getNestedProperty(bean, name);
        } catch (Exception e) {
            handleReflectionException(e);
            return null;
        }
    }

    public static String getProperty(Object bean, String name) {
        try {
            return beanUtilsBean.getProperty(bean, name);
        } catch (Exception e) {
            handleReflectionException(e);
            return null;
        }
    }

    public static PropertyUtilsBean getPropertyUtils() {
        try {
            return beanUtilsBean.getPropertyUtils();
        } catch (Exception e) {
            handleReflectionException(e);
            return null;
        }
    }

    public static String getSimpleProperty(Object bean, String name) {
        try {
            return beanUtilsBean.getSimpleProperty(bean, name);
        } catch (Exception e) {
            handleReflectionException(e);
            return null;
        }
    }

    public static void populate(Object bean, Map<?, ?> properties) {
        try {
            beanUtilsBean.populate(bean, properties);
        } catch (Exception e) {
            handleReflectionException(e);
        }
    }

    /**
     * 通过反射设置对象的值。
     *
     * @param bean
     * @param name
     * @param value void
     */
    public static void setProperty(Object bean, String name, Object value) {
        try {
            beanUtilsBean.setProperty(bean, name, value);
        } catch (Exception e) {
            handleReflectionException(e);
        }
    }

    private static void handleReflectionException(Exception e) {
        ReflectionUtils.handleReflectionException(e);
    }

    /**
     * java反射访问私有成员变量的值。
     *
     * @param instance
     * @param fieldName
     * @return
     * @throws IllegalAccessException
     * @throws NoSuchFieldException   Object
     */
    public static Object getValue(Object instance, String fieldName)
            throws IllegalAccessException, NoSuchFieldException {

        Field field = getField(instance.getClass(), fieldName);
        // 参数值为true，禁用访问控制检查
        field.setAccessible(true);
        return field.get(instance);
    }

    /**
     * 将字符串数据按照指定的类型进行转换。
     *
     * @param typeName 实际的数据类型
     * @param valStr   字符串值。
     * @return Object
     */
    public static Object getValue(String typeName, String valStr) {
        Object o = null;
        if (typeName.equals("int")) {
            o = Integer.parseInt(valStr);
        } else if (typeName.equals("short")) {
            o = Short.parseShort(valStr);
        } else if (typeName.equals("long")) {
            o = Long.parseLong(valStr);
        } else if (typeName.equals("float")) {
            o = Float.parseFloat(valStr);
        } else if (typeName.equals("double")) {
            o = Double.parseDouble(valStr);
        } else if (typeName.equals("boolean")) {
            o = Boolean.parseBoolean(valStr);
        } else if (typeName.equals("java.lang.String")) {
            o = valStr;
        } else {
            o = valStr;
        }
        return o;
    }

    /**
     * <pre>
     * 根据字段类型，条件，把字符串的valStr转成真正的val
     * </pre>
     *
     * @param columnType 字段类型
     * @param queryOP    条件
     * @param valStr     字符串值
     * @return
     */
    public static Object getValue(String columnType, QueryOP queryOP, String valStr) {
        Object value = null;
        if ("varchar".equals(columnType)) {
            value = valStr;
        } else if ("number".equals(columnType)) {
            value = Double.parseDouble(valStr);
        } else if ("date".equals(columnType)) {
            try {
                value = DateFormatUtil.parse(valStr);
            } catch (Exception e) {

            }
        }
        return value;
    }

    /**
     * 根据类和成员变量名称获取成员变量。
     *
     * @param thisClass
     * @param fieldName
     * @return
     * @throws NoSuchFieldException Field
     */
    public static Field getField(Class thisClass, String fieldName)
            throws NoSuchFieldException {

        if (fieldName == null) {
            throw new NoSuchFieldException("Error field !");
        }

        Field field = thisClass.getDeclaredField(fieldName);
        return field;
    }

    /**
     * 合并两个对象。
     *
     * @param srcObj
     * @param desObj void
     */
    public static void mergeObject(Object srcObj, Object desObj) {
        if (srcObj == null || desObj == null) return;

        Field[] fs1 = srcObj.getClass().getDeclaredFields();
        Field[] fs2 = desObj.getClass().getDeclaredFields();
        for (int i = 0; i < fs1.length; i++) {
            try {
                fs1[i].setAccessible(true);
                Object value = fs1[i].get(srcObj);
                fs1[i].setAccessible(false);
                if (null != value) {
                    fs2[i].setAccessible(true);
                    fs2[i].set(desObj, value);
                    fs2[i].setAccessible(false);
                }
            } catch (Exception e) {
                logger.error("mergeObject" + e.getMessage());
            }
        }
    }

    /**
     * @描述 list数据转Tree，大多使用在前台json中。
     * @说明 实现接口 Tree即可
     * @扩展 可通过反射获取id, pid，目前只提供Tree接口排序的实现
     * @author jeff
     */
    public static <T> List<T> listToTree(List<T> list) {
        Map<String, Tree> tempMap = new LinkedHashMap<String, Tree>();
        if (BeanUtils.isEmpty(list)) return Collections.emptyList();
        if (!(list.get(0) instanceof Tree)) {
            throw new RuntimeException("树形转换出现异常。数据必须实现Tree接口！");
        }

        List<T> returnList = new ArrayList<T>();
        for (Tree tree : (List<Tree>) list) {
            tempMap.put(tree.getId(), tree);
        }

        for (Tree obj : (List<Tree>) list) {
            String parentId = obj.getParentId();
            if (tempMap.containsKey(parentId) && !obj.getId().equals(parentId)) {
                if (tempMap.get(parentId).getChildren() == null) {
                    tempMap.get(parentId).setChildren(new ArrayList());
                }
                tempMap.get(parentId).getChildren().add(obj);
            } else {
                returnList.add((T) obj);
            }
        }

        return returnList;
    }

}
