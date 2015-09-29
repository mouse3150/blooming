package cn.com.esrichina.adapter.utils;



import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import cn.com.esrichina.adapter.IaaSService;
import cn.com.esrichina.adapter.openstack.ProviderContext;
import cn.com.esrichina.adapter.utils.time.Hour;
import cn.com.esrichina.adapter.utils.time.Millisecond;
import cn.com.esrichina.adapter.utils.time.TimePeriod;

/**
 * Implements efficient caching of non-changing resources so that you can minimize the number of API calls being made
 * to a cloud provider. For example, the Dasein Cloud API provides methods for querying the list of regions in the cloud.
 * Chances are this list changes, at most, once every few months and is the same for every single account in the cloud.
 * Thus, repeatedly calling the API to list regions is wildly inefficient. On the other hand, caching in a cross-cloud
 * API with classes that may support multiple accounts in multiple clouds. You also don't want to end up caching so
 * much stuff that you are eating up RAM. This cloud implements centralized caching so that you can minimize API calls
 * with efficient memory usage.
 * <p>
 * Example:
 * </p>
 * <pre>
 *     public Iterable&lt;Region&gt; listRegions() {
 *         Cache&lt;Region&gt; cache = Cache.getInstance(provider, "regions", Region.class, CacheLevel.CLOUD);
 *         Iterable&lt;Region&gt; regions = cache.get(provider.getContext());
 *
 *         if( regions == null ) {
 *             // make API call to load regions
 *             cache.put(provider.getContext(), regions);
 *         }
 *         return regions;
 *     }
 * </pre>
 */
public final class Cache<T> {
    static private final HashMap<String,Cache<?>> caches = new HashMap<String, Cache<?>>();

    static public class CacheDelegate implements CacheMBean {
        @Override
        public void clear(String cacheName) {
            Cache<?> c;

            synchronized( caches ) {
                if( caches.containsKey(cacheName) ) {
                    c = caches.get(cacheName);
                }
                else {
                    return;
                }
            }
            c.clear();
        }

        @Override
        public String[] getCaches() {
            Set<String> names;

            synchronized( caches ) {
                names = caches.keySet();
            }
            return names.toArray(new String[names.size()]);
        }

        @Override
        public CacheLevel getCacheLevel(String cacheName) {
            Cache<?> c;

            synchronized( caches ) {
                if( caches.containsKey(cacheName) ) {
                    c = caches.get(cacheName);
                }
                else {
                    return null;
                }
            }
            if( c.cloudCache != null ) {
                return CacheLevel.CLOUD;
            }
            else if( c.cloudAccountCache != null ) {
                return CacheLevel.CLOUD_ACCOUNT;
            }
            else if( c.regionCache != null ) {
                return CacheLevel.REGION;
            }
            else if( c.regionAccountCache != null ) {
                return CacheLevel.REGION_ACCOUNT;
            }
            return null;
        }

        @Override
        public long getNextTimeout(String cacheName) {
            Cache<?> c;

            synchronized( caches ) {
                if( caches.containsKey(cacheName) ) {
                    c = caches.get(cacheName);
                }
                else {
                    return System.currentTimeMillis();
                }
            }
            return (c.cacheStart + c.cacheTimeout.longValue());
        }

        @Override
        public long getTimeoutInSeconds(String cacheName) {
            Cache<?> c;

            synchronized( caches ) {
                if( caches.containsKey(cacheName) ) {
                    c = caches.get(cacheName);
                }
                else {
                    return 0L;
                }
            }
            return (c.cacheTimeout.longValue()/1000L);
        }

        public void setTimeoutInSeconds(String cacheName, long timeoutInSeconds) {
            Cache<?> c;

            synchronized( caches ) {
                if( caches.containsKey(cacheName) ) {
                    c = caches.get(cacheName);
                }
                else {
                    return;
                }
            }
            c.cacheTimeout = new TimePeriod<Millisecond>(timeoutInSeconds, TimePeriod.MILLISECOND);
        }
    }

    static private class CacheEntry<T> {
        public long lastCacheClear;
        public SoftReference<Iterable<T>> items;
        public String toString() { return ((items == null) ? "--> empty <--" : items.toString()); }
    }



    /**
     * Provides access to a cache for items under the specified name.
     * @param provider the cloud provider object governing the cache
     * @param name the name of the cache
     * @param typeClass the type of object being cached
     * @param level the level at which these objects should be cached
     * @param <X> the type of the object being cached
     * @return a cache containing the context-sensitive cached items
     */
    static public <X> Cache<X> getInstance(IaaSService provider, String name, Class<X> typeClass, CacheLevel level) {
        return getInstance(provider, name, typeClass, level, new TimePeriod<Hour>(1, TimePeriod.HOUR));
    }

    /**
     * Provides access to a cache for items under the specified name.
     * @param provider the cloud provider object governing the cache
     * @param name the name of the cache
     * @param typeClass the type of object being cached
     * @param level the level at which these objects should be cached
     * @param timeout the amount of time before the cache is automatically considered stale and forces you to reload from API
     * @param <X> the type of the object being cached
     * @return a cache containing the context-sensitive cached items
     */
    static public <X> Cache<X> getInstance(IaaSService provider, String name, Class<X> typeClass, CacheLevel level, TimePeriod<?> timeout) {
        Cache<X> c;

        name = provider.getClass().getName() + "." + name;
        synchronized( caches ) {
            if( caches.containsKey(name) ) {
                c = (Cache<X>)caches.get(name);
            }
            else {
                c = new Cache<X>(level, timeout);
                caches.put(name, c);
            }
        }
        return c;
    }

    private HashMap<String,CacheEntry<T>>                         cloudCache;
    private HashMap<String,Map<String,CacheEntry<T>>>             cloudAccountCache;
    private HashMap<String,Map<String,CacheEntry<T>>>             regionCache;
    private HashMap<String,Map<String,Map<String,CacheEntry<T>>>> regionAccountCache;

    private TimePeriod<Millisecond> cacheTimeout;
    private long                    cacheStart;

    private Cache() { }

    private Cache(CacheLevel level, TimePeriod<?> timeout) {
        switch( level ) {
            case CLOUD: cloudCache = new HashMap<String, CacheEntry<T>>(); break;
            case CLOUD_ACCOUNT: cloudAccountCache = new HashMap<String, Map<String, CacheEntry<T>>>(); break;
            case REGION: regionCache = new HashMap<String, Map<String, CacheEntry<T>>>(); break;
            case REGION_ACCOUNT: regionAccountCache = new HashMap<String, Map<String, Map<String, CacheEntry<T>>>>(); break;
        }
        cacheTimeout = (TimePeriod<Millisecond>)timeout.convertTo(TimePeriod.MILLISECOND);
        cacheStart = System.currentTimeMillis();
    }

    /**
     * Clears out the cache across the board, regardless of context.
     */
    public void clear() {
        synchronized( this ) {
            if( cloudCache != null ) {
                cloudCache.clear();
            }
            else if( cloudAccountCache != null ) {
                cloudAccountCache.clear();
            }
            else if( regionCache != null ) {
                regionCache.clear();
            }
            else if( regionAccountCache != null ) {
                regionAccountCache.clear();
            }
            cacheStart = System.currentTimeMillis();
        }
    }

    /**
     * Fetches the items currently cached for the context specified. Depending on the caching level, this
     * method may return different values for different contexts. If the returned value is null, that means
     * nothing is cached and you should refetch and then cache the results.
     * @param ctx the context for the caching
     * @return the items currently in the cache if any are currently cached
     */
    public Iterable<T> get(ProviderContext ctx) {
        synchronized( this ) {
            if( System.currentTimeMillis() > (cacheStart + CalendarWrapper.DAY) ) {
                clear();
                return null;
            }
        }
        CacheEntry<T> entry = null;
        String endpoint = ctx.getEndpoint();

        if( cloudCache != null ) {
            entry = cloudCache.get(endpoint);
        }
        else if( regionCache != null ) {
            Map<String,CacheEntry<T>> map = regionCache.get(endpoint);

            if( map != null ) {
                entry = map.get(ctx.getRegionId());
            }
        }
        else if( cloudAccountCache != null ) {
            Map<String,CacheEntry<T>> map = cloudAccountCache.get(endpoint);

            if( map != null ) {
                entry = map.get(ctx.getAccountNumber());
            }
        }
        else if( regionAccountCache != null ) {
            Map<String,Map<String,CacheEntry<T>>> map = regionAccountCache.get(endpoint);

            if( map != null ) {
                Map<String,CacheEntry<T>> rmap = map.get(ctx.getRegionId());

                if( rmap != null ) {
                    entry = rmap.get(ctx.getAccountNumber());
                }
            }
        }
        if( entry == null ) {
            return null;
        }
        if( entry.lastCacheClear + cacheTimeout.longValue() < System.currentTimeMillis() ) {
            entry.items = null;
            return null;
        }
        return entry.items.get();
    }

    /**
     * Places items into the cache for the specified context.
     * @param ctx the context of the cache
     * @param list the items to be cached
     */
    public void put(ProviderContext ctx, Iterable<T> list) {
        CacheEntry<T> entry = new CacheEntry<T>();
        String endpoint = ctx.getEndpoint();

        entry.items = new SoftReference<Iterable<T>>(list);
        entry.lastCacheClear = System.currentTimeMillis();
        if( cloudCache != null ) {
            cloudCache.put(endpoint, entry);
        }
        else if( regionCache != null ) {
            Map<String,CacheEntry<T>> map = regionCache.get(endpoint);

            if( map == null ) {
                map = new HashMap<String, CacheEntry<T>>();
                regionCache.put(endpoint, map);
            }
            map.put(ctx.getRegionId(), entry);
        }
        else if( cloudAccountCache != null ) {
            Map<String,CacheEntry<T>> map = cloudAccountCache.get(endpoint);

            if( map == null ) {
                map = new HashMap<String, CacheEntry<T>>();
                cloudAccountCache.put(endpoint, map);
            }
            map.put(ctx.getAccountNumber(), entry);
        }
        else if( regionAccountCache != null ) {
            Map<String,Map<String,CacheEntry<T>>> map = regionAccountCache.get(endpoint);

            if( map == null ) {
                map = new HashMap<String, Map<String, CacheEntry<T>>>();
                regionAccountCache.put(endpoint, map);
            }

            Map<String,CacheEntry<T>> rmap = map.get(ctx.getRegionId());

            if( rmap == null ) {
                rmap = new HashMap<String, CacheEntry<T>>();
                map.put(ctx.getRegionId(), rmap);
            }
            rmap.put(ctx.getAccountNumber(), entry);
        }
    }
}
