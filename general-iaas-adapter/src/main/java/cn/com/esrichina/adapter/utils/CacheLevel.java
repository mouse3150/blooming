package cn.com.esrichina.adapter.utils;


/**
 * Defines what level data gets cached at.
 */
public enum CacheLevel {
    /**
     * Cached data should be shared across all accounts and regions in the same cloud
     */
    CLOUD,
    /**
     * Cached data should be shared across all accounts in the same region
     */
    REGION,
    /**
     * Cached data should be shared across all regions for a specific account (not shared with other accounts)
     */
    CLOUD_ACCOUNT,
    /**
     * Cached data should not be shared with other accounts or regions
     */
    REGION_ACCOUNT
}
