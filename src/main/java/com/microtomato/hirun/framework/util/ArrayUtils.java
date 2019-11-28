package com.microtomato.hirun.framework.util;

import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 数组型及 LIST 数据的工具类，可以判断数组是否为空及数组型数据的简便操作
 *
 * @author jinnian
 * @date 2018/2/20 23:51
 */
public class ArrayUtils {

    /**
     * 判断数组是否为非空
     *
     * @param objects
     * @return true-非空  false-空
     */
    public static boolean isNotEmpty(Object[] objects) {

        if (null == objects) {
            return false;
        }

        if (0 == objects.length) {
            return false;
        }

        return true;
    }

    /**
     * 判断数组是否为空
     *
     * @param objects
     * @return true-空  false-空
     */
    public static boolean isEmpty(Object[] objects) {
        return !isNotEmpty(objects);
    }

    /**
     * 判断 List 结构是否为非空
     *
     * @param list
     * @return true-非空 false-空
     */
    public static boolean isNotEmpty(List list) {
        if (null == list) {
            return false;
        }
        if (0 == list.size()) {
            return false;
        }

        return true;
    }

    /**
     * 判断List结构是否为空
     *
     * @param list
     * @return true-空 false-非空
     */
    public static boolean isEmpty(List list) {
        return !isNotEmpty(list);
    }

    /**
     * 将一个 List 按类型快速转换成另一个类的 List
     *
     * @param srcObjectList
     * @param target
     * @return
     * @auther Steven
     */
    public static List<?> copyPropertiesList(List<?> srcObjectList, Class target) {

        List<Object> rtn = null;
        if (null != srcObjectList) {
            rtn = new ArrayList(srcObjectList.size());
            for (Object srcObject : srcObjectList) {
                try {
                    Object dstObject = target.newInstance();
                    BeanUtils.copyProperties(srcObject, dstObject);
                    rtn.add(dstObject);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }

        return rtn;
    }

}
