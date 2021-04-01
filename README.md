# Guide
Add Annotation **UniqueString** to the Java class you wanna check.

```java
    @UniqueString
    public static class SharedPrefFileNames {
      public static final String USER_INFO = "user_info";
      public static final String DATA_CACHE = "data_cache";
      @Skip
      public static final String USER_INFO_CONFLICT = "user_info";
    }
```



