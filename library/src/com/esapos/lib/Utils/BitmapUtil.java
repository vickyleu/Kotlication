package com.esapos.lib.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by VickyLeu on 2016/7/29.
 *
 * @Author Vickyleu
 * @Company Esapos
 * @Class
 */
public class BitmapUtil {


    public static void ConvertRGBBmp(ImageView to, View from) throws Exception {
        from.setDrawingCacheEnabled(true);
        from.buildDrawingCache();
        Bitmap fromDrawingCache = from.getDrawingCache();

        if (!fromDrawingCache.isRecycled()) {
            fromDrawingCache = convertToBlackWhite(fromDrawingCache);
            Bitmap bmp = Bitmap.createBitmap(fromDrawingCache, 0, 0, from.getWidth(), from.getHeight());
            to.setImageBitmap(bmp);
        }
        from.setDrawingCacheEnabled(false);
        fromDrawingCache.recycle();
    }

    public static void ConvertRGBBmp(ImageView to, View from, int color) throws Exception {
        from.setDrawingCacheEnabled(true);
        from.buildDrawingCache();
        Bitmap fromDrawingCache = from.getDrawingCache();

        if (!fromDrawingCache.isRecycled()) {
            fromDrawingCache = createRGBImage(fromDrawingCache, color);
            Bitmap bmp = Bitmap.createBitmap(fromDrawingCache, 0, 0, from.getWidth(), from.getHeight());
            to.setImageBitmap(bmp);
        }
        from.setDrawingCacheEnabled(false);
        fromDrawingCache.recycle();
    }

    /**
     * 将彩色图转换为黑白图
     *
     * @param bmp
     * @return 返回转换好的位图
     */
    private static Bitmap convertToBlackWhite(Bitmap bmp) {
        int width = bmp.getWidth(); // 获取位图的宽
        int height = bmp.getHeight(); // 获取位图的高
        int[] pixels = new int[width * height]; // 通过位图的大小创建像素点数组

        bmp.getPixels(pixels, 0, width, 0, 0, width, height);
        int alpha = 0xFF << 24;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int grey = pixels[width * i + j];

                int red = ((grey & 0x00FF0000) >> 16);
                int green = ((grey & 0x0000FF00) >> 8);
                int blue = (grey & 0x000000FF);

                grey = (int) (red * 0.3 + green * 0.59 + blue * 0.11);
                grey = alpha | (grey << 16) | (grey << 8) | grey;
                pixels[width * i + j] = grey;
            }
        }
        Bitmap newBmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

        newBmp.setPixels(pixels, 0, width, 0, 0, width, height);

        return ThumbnailUtils.extractThumbnail(newBmp, width, height);
    }


    private static Bitmap createRGBImage(Bitmap bitmap, int color) {
        int bitmap_w = bitmap.getWidth();
        int bitmap_h = bitmap.getHeight();
        int[] arrayColor = new int[bitmap_w * bitmap_h];
        int count = 0;
        for (int i = 0; i < bitmap_h; i++) {
            for (int j = 0; j < bitmap_w; j++) {
                int color1 = bitmap.getPixel(j, i);
                //这里也可以取出 R G B 可以扩展一下 做更多的处理，
                //暂时我只是要处理除了透明的颜色，改变其他的颜色
                if (color1 == 0) {

                } else {
                    color1 = color;
                }
                arrayColor[count] = color;
                count++;
            }
        }
        bitmap = Bitmap.createBitmap(arrayColor, bitmap_w, bitmap_h, Bitmap.Config.ARGB_4444);
        return bitmap;
    }

    public static String GenerateBase64(View view) throws Exception {
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap fromDrawingCache = view.getDrawingCache();
        return GenerateBase64(fromDrawingCache);
    }

    public static String GenerateBase64(Bitmap bm)throws Exception {
        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bm != null) {
                baos = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                baos.flush();
                baos.close();

                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static Bitmap GenerateBmp(String imgStr) {
        //对字节数组字符串进行Base64解码并生成图片
        if (imgStr == null) //图像数据为空
            return null;
        try {
            //Base64解码
            byte[] bytes = Base64.decode(imgStr, Base64.DEFAULT);
            for (int i = 0; i < bytes.length; ++i) {
                if (bytes[i] < 0) {//调整异常数据
                    bytes[i] += 256;
                }
            }
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        } catch (Exception e) {
            return null;
        }
    }
}
