package me.tergel.annotation;

public class GlobalConstants {
    @UniqueString
    public static class SharedPrefsFileName {
        public static final String USER_INFO = "user_info";
        public static final String DATA_CACHE = "data_cache";
        @Skip
        public static final String USER_INFO_CONFLICT = "user_info";
    }

    @UniqueString
    public static class UserInfo {
        public static final String KEY_USER_GENDER = "ug";
        public static final String KEY_USER_NICKNAME = "un";
        @Skip
        public static final String KEY_USER_GRADE = "ug";
    }
}
