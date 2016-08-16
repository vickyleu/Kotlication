package com.esapos.lib.Utils;

/**
 * Created by VickyLeu on 2016/8/8.
 * log日志统计保存
 *
 * @Author Vickyleu
 * @Company Esapos
 * @Class
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogcatHelper {

    private static LogcatHelper INSTANCE = null;
    private static String PATH_LOGCAT;
    private LogDumper mLogDumper = null;
    private int mPId;
    PrintLine printer;

    /**
     * 初始化目录
     */
    public void init(Context context) {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {// 优先保存到SD卡中
            PATH_LOGCAT = Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + File.separator + "lib_logcat";
        } else {// 如果SD卡不存在，就保存到本应用的目录下
            PATH_LOGCAT = context.getFilesDir().getAbsolutePath()
                    + File.separator + "lib_logcat";
        }
        File file = new File(PATH_LOGCAT);
        if (!file.exists()) {
            //noinspection ResultOfMethodCallIgnored
            file.mkdirs();
        }
    }

    public static LogcatHelper getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new LogcatHelper(context);
        }
        return INSTANCE;
    }

    private LogcatHelper(Context context) {
        init(context);
        mPId = android.os.Process.myPid();
    }

    public void start() {
        if (mLogDumper == null)
            mLogDumper = new LogDumper(String.valueOf(mPId), PATH_LOGCAT);
        mLogDumper.start();
    }

    public void stop() {
        if (mLogDumper != null) {
            mLogDumper.stopLogs();
            mLogDumper = null;
        }
    }

    private class LogDumper extends Thread {

        private Process logcatProc;
        private BufferedReader mReader = null;
        private boolean mRunning = true;
        String cmds = null;
        private String mPID;
        private FileOutputStream out = null;

        public LogDumper(String pid, String dir) {
            mPID = pid;
            try {
                out = new FileOutputStream(new File(dir, "logcat-"
                        + getFileName() + ".txt"));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            /**
             *
             * 日志等级：*:v , *:d , *:w , *:e , *:f , *:s
             *
             * 显示当前mPID程序的 E和W等级的日志.
             *
             * */

            // cmds = "logcat *:e *:w | grep \"(" + mPID + ")\"";
            // cmds = "logcat  | grep \"(" + mPID + ")\"";//打印所有日志信息
            // cmds = "logcat -s way";//打印标签过滤信息
//            cmds = "logcat *:e *:i | grep \"(" + mPID + ")\"";
            cmds = "logcat *:e  | grep \"(" + mPID + ")\" ";

        }

        public void stopLogs() {
            mRunning = false;
        }

        @Override
        public void run() {
            try {
                logcatProc = Runtime.getRuntime().exec(cmds);
                mReader = new BufferedReader(new InputStreamReader(
                        logcatProc.getInputStream()), 1024);
                String line = null;
                while (mRunning && (line = mReader.readLine()) != null) {
                    if (!mRunning) {
                        break;
                    }
                    if (line.length() == 0) {
                        continue;
                    }
                    if (out != null && line.contains(mPID)) {

                        final String finalLine = "  " + line + "\n";
                        new Handler(Looper.getMainLooper()).post(() -> {
                            if (printer != null) printer.LogInfo(finalLine);
                        });

                        out.write((getDateEN() + "  " + line + "\n")
                                .getBytes());
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (logcatProc != null) {
                    logcatProc.destroy();
                    logcatProc = null;
                }
                if (mReader != null) {
                    try {
                        mReader.close();
                        mReader = null;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    out = null;
                }

            }

        }

    }

    public void registerPrinter(PrintLine printer) {
        this.printer = printer;
    }

    public static String getFileName() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(new Date(System.currentTimeMillis()));// 2012年10月03日 23:41:31
    }

    public static String getDateEN() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format1.format(new Date(System.currentTimeMillis()));// 2012-10-03 23:41:31


    }

    public static interface PrintLine {
        void LogInfo(String logcat);
    }

}
