package cmd.util;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckUtil {
    static Pattern pattern = Pattern.compile("http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?");

    public CheckUtil() {
    }

    public static boolean checkUrl(String url) {
        if (url == null) {
            return false;
        } else {
            Matcher matcher = pattern.matcher(url);
            return matcher.matches();
        }
    }

    public static boolean equals(String str1, String str2) {
        if (str1 == null && str2 == null) {
            return true;
        } else {
            return str1 != null && str1.equals(str2);
        }
    }

    public static boolean isBlank(String str) {
        if (str == null) {
            return true;
        } else {
            return str.trim().length() == 0;
        }
    }

    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    public static boolean anyMatchBlank(Object... targets) {
        return !noneMatchBlank(targets);
    }

    public static boolean allMatchBlank(Object... targets) {
        return !anyMatchNotBlank(targets);
    }

    public static boolean noneMatchBlank(Object... targets) {
        if (targets != null && targets.length != 0) {
            Object[] var1 = targets;
            int var2 = targets.length;

            for(int var3 = 0; var3 < var2; ++var3) {
                Object target = var1[var3];
                if (null == target) {
                    return false;
                }

                if (target instanceof String) {
                    if (isBlank((String)target)) {
                        return false;
                    }
                } else {
                    Class<?> clazz = target.getClass();
                    if (clazz.isArray()) {
                        int l = Array.getLength(target);
                        if (l == 0) {
                            return false;
                        }

                        Object firstOne = Array.get(target, 0);
                        if (firstOne != null && firstOne.getClass().isArray()) {
                            for(int i = 0; i < l; ++i) {
                                if (!noneMatchBlank((Object[])((Object[])Array.get(target, 0)))) {
                                    return false;
                                }
                            }
                        } else {
                            Class<?> componentType = clazz.getComponentType();
                            if (componentType.isPrimitive()) {
                                for(int i = 0; i < l; ++i) {
                                    if (null == Array.get(target, i)) {
                                        return false;
                                    }
                                }
                            } else if (!noneMatchBlank((Object[])((Object[])target))) {
                                return false;
                            }
                        }
                    } else if (Collection.class.isAssignableFrom(clazz)) {
                        if (!noneMatchBlank(((Collection)target).toArray())) {
                            return false;
                        }
                    } else if (!(target instanceof Number)) {
                        return false;
                    }
                }
            }

            return true;
        } else {
            return false;
        }
    }

    public static boolean anyMatchNotBlank(Object... targets) {
        if (targets != null && targets.length != 0) {
            Object[] var1 = targets;
            int var2 = targets.length;

            for(int var3 = 0; var3 < var2; ++var3) {
                Object target = var1[var3];
                if (null != target) {
                    if (target instanceof String) {
                        if (!isBlank((String)target)) {
                            return true;
                        }
                    } else {
                        Class<?> clazz = target.getClass();
                        if (Collection.class.isAssignableFrom(clazz)) {
                            if (anyMatchNotBlank(((Collection)target).toArray())) {
                                return true;
                            }
                        } else if (clazz.isArray()) {
                            int l = Array.getLength(target);
                            if (l != 0) {
                                Object firstOne = Array.get(target, 0);
                                if (firstOne != null && firstOne.getClass().isArray()) {
                                    for(int i = 0; i < l; ++i) {
                                        if (anyMatchNotBlank((Object[])((Object[])Array.get(target, 0)))) {
                                            return true;
                                        }
                                    }
                                } else {
                                    Class<?> componentType = clazz.getComponentType();
                                    if (componentType.isPrimitive()) {
                                        for(int i = 0; i < l; ++i) {
                                            if (null != Array.get(target, i)) {
                                                return true;
                                            }
                                        }
                                    }

                                    if (anyMatchNotBlank((Object[])((Object[])target))) {
                                        return true;
                                    }
                                }
                            }
                        } else if (target instanceof Number) {
                            return true;
                        }
                    }
                }
            }

            return false;
        } else {
            return false;
        }
    }
}
