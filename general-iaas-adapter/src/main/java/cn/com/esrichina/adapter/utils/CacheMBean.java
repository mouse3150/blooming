package cn.com.esrichina.adapter.utils;

public interface CacheMBean {
    public void clear(String cacheName);

    public CacheLevel getCacheLevel(String cacheName);

    public String[] getCaches();

    public long getNextTimeout(String cacheName);

    public long getTimeoutInSeconds(String cacheName);

    public void setTimeoutInSeconds(String cacheName, long timeoutInSeconds);
}
