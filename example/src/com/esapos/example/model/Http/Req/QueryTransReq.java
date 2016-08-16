package com.esapos.example.model.Http.Req;

/**
 * Created by VickyLeu on 2016/7/22.
 *
 * @Author Vickyleu
 * @Company Esapos
 * @Class
 */
public class QueryTransReq {
    public String startTime;//查询起始时间,批量查询时使用
    public String endTime;//查询结束时间,批量查询时使用
    public int pageSize;//准备获取的每页记录数,如果没有值,则 默认为10条
    public int pageIndex;//访问的页码,如果没有值,则默认为第 1 页
    public String transType;//交易类型 10:余额查询,11:消费，12:消费撤销  若为空则查询所有交易

    /**
     * 完整参数
     *
     * @param startTime
     * @param endTime
     * @param pageSize
     * @param pageIndex
     * @param transType
     */
    public QueryTransReq(String startTime, String endTime,
                         int pageSize, int pageIndex, String transType) {
        this.startTime = startTime;
        this.endTime = endTime;
        if (pageSize == -1) pageSize = 10;
        this.pageSize = pageSize;
        if (pageIndex == -1) pageIndex = 1;
        this.pageIndex = pageIndex;
        this.transType = transType;
    }


    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public class TransType {
        public static final String BALANCE = "10";
        public static final String SALE = "11";
        public static final String UNSALE = "12";
        public static final String ALL = "";
    }
}
