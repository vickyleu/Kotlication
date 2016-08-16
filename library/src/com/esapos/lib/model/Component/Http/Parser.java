package com.esapos.lib.model.Component.Http;

/**
 * Created by VickyLeu on 2016/7/22.
 *
 * @Author Vickyleu
 * @Company Esapos
 * @Class
 */
public interface Parser<D> {
    public  Parser parser(String result, D d) throws Exception;
}
