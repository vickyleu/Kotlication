package com.esapos.lib.Controller.db;

import java.util.List;

/**
 * Created by VickyLeu on 2016/7/26.
 *
 * @Author Vickyleu
 * @Company Esapos
 * @Class
 */
public interface DaoHelperInterface {
    public <T> void addData(T t);
    public void deleteData(String id);
    public <T> T getDataById(String id);
    public List getAllData();
    public boolean hasKey(String id);
    public long getTotalCount();
    public void deleteAll();
}
