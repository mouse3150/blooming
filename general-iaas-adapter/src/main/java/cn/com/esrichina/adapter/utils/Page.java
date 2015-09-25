package cn.com.esrichina.adapter.utils;

import java.util.List;

/**
 * 分页存储
 */
public class Page<T> {

    // 数据
    private List<T> data;

    // 起始页
    private int from = 1;

    // 每页记录数
    private int limit = 10;

    // 总页数
    private int totalPage;

    // 总记录数
    private int totalRecords;

    /**
     * 获取数据
     * 
     * @return 数据
     */
    public List<T> getData() {
        return data;
    }

    /**
     * 设置数据
     * 
     * @param data 数据
     */
    public void setData(List<T> data) {
        this.data = data;
    }

    /**
     * 获取起始页
     * 
     * @return 起始页
     */
    public int getFrom() {
        return from;
    }

    /**
     * 设置起始页
     * 
     * @param from 起始页
     */
    public void setFrom(int from) {
        this.from = from;
    }

    /**
     * 获取每页记录数
     * 
     * @return 每页记录数
     */
    public int getLimit() {
        return limit;
    }

    /**
     * 设置每页记录数
     * 
     * @param limit 每页记录数
     */
    public void setLimit(int limit) {
        this.limit = limit;
    }

    /**
     * 获取总页数
     * 
     * @return 总页数
     */
    public int getTotalPage() {
    	totalPage = totalRecords / limit + (totalRecords % limit == 0 ? 0 : 1);
        return totalPage;
    }

    /**
     * 设置总页数
     * 
     * @param totalPage 总页数
     */
    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    /**
     * 获取总记录数
     * 
     * @return 总记录数
     */
    public int getTotalRecords() {
        return totalRecords;
    }

    /**
     * 设置总记录数
     * 
     * @param totalRecords 总记录数
     */
    public void setTotalRecords(int totalRecords) {
        this.totalRecords = totalRecords;
    }
}

