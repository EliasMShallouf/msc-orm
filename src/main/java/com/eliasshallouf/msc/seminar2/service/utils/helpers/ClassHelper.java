package com.eliasshallouf.msc.seminar2.service.utils.helpers;

import java.lang.reflect.Array;
import java.sql.Blob;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

public class ClassHelper {
    public static <E> E createObject(Class<E> clazz) {
        try {
            return clazz.getConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static <E> boolean isPrimitive(Class<E> clazz) {
        return Arrays.asList(
            Integer.class,
            Double.class,
            Long.class,
            Boolean.class,
            Date.class,
            LocalDate.class,
            LocalDateTime.class,
            LocalTime.class,
            Float.class,
            String.class,
            Byte.class,
            Short.class,
            byte[].class,
            Character.class,
            int.class,
            short.class,
            long.class,
            double.class,
            float.class,
            char.class,
            boolean.class,
            byte.class,
            UUID.class
        ).contains(clazz);
    }

    public static boolean isImplements(Class<?> c, Class<?> i) {
        for(Class<?> tmp : c.getInterfaces())
            if(tmp.equals(i))
                return true;

        return false;
    }
}
