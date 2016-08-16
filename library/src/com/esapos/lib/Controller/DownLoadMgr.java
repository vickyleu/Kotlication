package com.esapos.lib.Controller;

import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.esapos.lib.Utils.SDCardUtils;
import com.esapos.lib.View.Dialog.MDialog;
import com.esapos.lib.model.Component.Http.DownParser;
import com.esapos.lib.model.Component.HttpLibrary.HttpCacheApp;
import com.esapos.lib.model.Component.RxJava.ResultCall;
import com.esapos.lib.model.Component.RxJava.RxCall;
import com.esapos.lib.model.Component.RxJava.RxHandler;
import com.esapos.lib.model.Component.RxJava.RxTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by VickyLeu on 2016/8/9.
 *
 * @Author Vickyleu
 * @Company Esapos
 * @Class
 */
public class DownLoadMgr extends Service implements DownParser {


    public static final String DOWNLOAD_ADDR = "downloadAddr";

//    public static final String ACTION = "com.esapos.lib.Controller.DownLoadMgr";
    private static final String TAG = DownLoadMgr.class.getSimpleName();
    private static ProgressUpd mUpd;
    private RxHandler mHandler = new RxHandler();


    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "onCreate: ");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand: ");
        if (intent == null) {
            return super.onStartCommand(intent, flags, startId);
        }
        load(intent.getStringExtra(DOWNLOAD_ADDR));
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void load(String urlDownload) {

        RxCall call = new RxCall(mHandler) {
            @Override
            public Boolean call() throws Exception {

                try {
                    File newFilename = FileName(urlDownload);
                    URL url = new URL(urlDownload);
                    // 打开连接
                    URLConnection con = url.openConnection();
                    // 输入流
                    InputStream is = con.getInputStream();
                    int progress = 0;
                    int length = con.getContentLength();
                    // 输出的文件流
                    FileOutputStream os = new FileOutputStream(newFilename);

                    // 1K的数据缓冲
                    byte[] buf = new byte[1024];

                    int ch = -1;
                    int count = 0;
                    if (length > 0)
                        do {
                            ch = is.read(buf);
                            if (ch != -1) {
                                count += ch;
                            }
                            progress = (int) (((float) count / length) * 100);
                            if (progress % 10 == 0) {
                                sendProgress(progress);
                            }
                            if (ch == -1) {
                                os.close();
                                is.close();
                                sendComplete(newFilename);
                                return true;
                            }
                            os.write(buf, 0, ch);
                        } while (flag);
                    // 完毕，关闭所有链接
                    os.close();
                    is.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    sendComplete(false);
                }
                sendComplete(!flag);
                return true;
            }

            @Override
            public void Result(Object result) throws Exception {
                super.Result(result);
            }
        };
        postTask("下载更新", null, call, null, null, 60 * 5, null, null);

    }

    public void setFileLimits(File file) {
        String[] command = {"chmod", "777", file.getPath()};
        ProcessBuilder builder = new ProcessBuilder(command);
        try {
            builder.start();
            Log.e(TAG, "setFileLimits: ");
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    private void sendComplete(Object object) {
        mHandler.post(() -> {
            if (mUpd != null) mUpd.Complete(object);
            stop();
        });

    }

    @NonNull
    private File FileName(String urlDownload) {
        // 获得存储卡路径，构成 保存文件的目标路径
        String dirName = "";
        if (SDCardUtils.isSDCardMounted()) {
            dirName = Environment.getExternalStorageDirectory() + "/MyDownload/";
        } else {
            dirName = this.getFilesDir() + "/MyDownload/";
        }
        //准备拼接新的文件名（保存在存储卡后的文件名）
        String filename = new Md5FileNameGenerator().generate(urlDownload);
        File file = new File(dirName, filename);
        setFileLimits(file);
        //如果目标文件已经存在，则删除。产生覆盖旧文件的效果


        if (!file.exists()) file.mkdirs();
        if (file.exists()) file.delete();

        Log.e(TAG, "FileName: " + file);
        return file;
    }

    public static <T, B> void postTask(String name, Activity activity, RxCall call, ResultCall<T, B> finalCall,
                                       String tips, int timeout, String successText, String errorText) {
        ObservableController.getInstance()
                .addTask(new RxTask(name, activity, tips, timeout, call, finalCall, successText, errorText));
    }

    private void sendProgress(int progress) {
        mHandler.post(() -> {
            if (mUpd != null) mUpd.update(progress);
        });

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    ProgressUpd upd;

    public static void start(Context context, String downloadAddr, ProgressUpd upd) {
        mUpd = upd;
        Intent intent = new Intent(context,DownLoadMgr.class);
        intent.putExtra(DOWNLOAD_ADDR, downloadAddr);
        Log.e(TAG, "start: ");
        ComponentName a = HttpCacheApp.getInstance().startService(intent);
        Log.e(TAG, "start: " + a.getPackageName());
    }

    private void stop() {
        stopSelf();
    }

    public interface ProgressUpd {


        void update(int progress);

        void Complete(Object object);
    }

    public static interface CompleteInstall{
        void DownloadComplete(File file);
        void DownloadError(int flag, Object obj, MDialog dialog);
    }

}
