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
        Object obj = null;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bos);
            out.writeObject(from);
            out.flush();
            out.close();

            ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()));
            obj = in.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e2) {
            e2.printStackTrace();
        }
        return obj;
    }

}
