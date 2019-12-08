package com.microtomato.hirun.framework.util;

import java.io.*;

/**
 * 深度 Clone
 *
 * @author Steven
 * @date 2019-12-02
 */
public class CloneUtils {

    public static final Object deepCopy(Object from) {

        Object rtn = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try (ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(from);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()))) {
            rtn = ois.readObject();
            bos.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return rtn;
    }

}
