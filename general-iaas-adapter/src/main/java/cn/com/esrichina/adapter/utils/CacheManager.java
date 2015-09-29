package cn.com.esrichina.adapter.utils;




/**
 * Provides JMX access into cache management functionality. 
 * Through this bean, you can fetch a list of active caches, get
 * the meta-data around them, and clear them out.
 */
public class CacheManager implements CacheMBean {
    private Cache.CacheDelegate collections         = new Cache.CacheDelegate();
    private SingletonCache.CacheDelegate singletons = new SingletonCache.CacheDelegate();

    @Override
    public void clear(String cacheName) {
        collections.clear(cacheName);
    }

    @Override
    public String[] getCaches() {
        String[] s = singletons.getCaches();
        String[] c = collections.getCaches();
        String[] names = new String[s.length + c.length];
        int i = 0;

        for( String str : s ) {
            names[i++] = str;
        }
        for( String str : c ) {
            names[i++] = str;
        }
        return names;
    }

    @Override
    public CacheLevel getCacheLevel(String cacheName) {
        CacheLevel l = collections.getCacheLevel(cacheName);

        if( l == null ) {
            l = singletons.getCacheLevel(cacheName);
        }
        return l;
    }

    @Override
    public long getNextTimeout(String cacheName) {
        CacheLevel l = collections.getCacheLevel(cacheName);

        if( l == null ) {
            return singletons.getNextTimeout(cacheName);
        }
        return collections.getNextTimeout(cacheName);
    }

    @Override
    public long getTimeoutInSeconds(String cacheName) {
        CacheLevel l = collections.getCacheLevel(cacheName);

        if( l == null ) {
            return singletons.getTimeoutInSeconds(cacheName);
        }
        return collections.getTimeoutInSeconds(cacheName);
    }

    @Override
    public void setTimeoutInSeconds(String cacheName, long timeoutInSeconds) {
        CacheLevel l = collections.getCacheLevel(cacheName);

        if( l == null ) {
            singletons.setTimeoutInSeconds(cacheName, timeoutInSeconds);
        }
        else {
            collections.setTimeoutInSeconds(cacheName, timeoutInSeconds);
        }
    }
}
