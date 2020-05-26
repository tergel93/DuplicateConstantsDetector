# HowTo
Add Annotation **UniqueString** to the Java class that contains a bunch of **static final String**s

```java
    @UniqueString
    public static class SharedPrefsFileName {
      public static final String USER_INFO = "user_info";
      public static final String DATA_CACHE = "data_cache";
      @Skip
      public static final String USER_INFO_CONFLICT = "user_info";
    }
```



