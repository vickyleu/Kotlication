package com.esapos.lib.model.Component;

/**
 * Created by VickyLeu on 2016/8/10.
 *
 * @Author Vickyleu
 * @Company Esapos
 * @Class
 */
public interface FileNameGenerator {

    /** Generates unique file name for image defined by URI */
    String generate(String imageUri);
}
