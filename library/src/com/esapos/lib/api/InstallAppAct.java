package com.esapos.lib.api;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.IPackageInstallObserver;
import android.content.pm.PackageManager;
import android.content.pm.VerificationParams;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.esapos.lib.R;

import java.io.File;

@SuppressWarnings("ALL")
public class InstallAppAct extends Activity {

    public static String RECEIVE_ACTION = "com.esapos.qb.appstore.ui.InstallAppAct"; //action名称
    private TextView tvMsg;
    private View rl;

    private static final int FLAG_HOMEKEY_DISPATCHED = 0x80000000;
    private String pkgName;
    private String apkPath;
    private String appName;
    private SilenceInstallService.InstallAppReceiver appReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBoardCaseReceived();
        getWindow().setFlags(FLAG_HOMEKEY_DISPATCHED, FLAG_HOMEKEY_DISPATCHED);
        setContentView(R.layout.mk_installtip);
        rl = findViewById(R.id.rl);
        pkgName = getIntent().getStringExtra(SilenceInstallService.KEY_PKG_NAME);
        apkPath = getIntent().getStringExtra(SilenceInstallService.KEY_APK_PATH);
        appName = getIntent().getStringExtra(SilenceInstallService.KEY_APP_NAME);

        tvMsg = (TextView) findViewById(R.id.tv_msg);
        tvMsg.setText(getString(R.string.installapp_tv, appName));
        boolean permission = getPackageManager().checkPermission("android.permission.INSTALL_PACKAGES", "packageName") == 0;
        if (permission) {
            new Thread() {
                public void run() {
                    try {
                        ubankInstall(new File(apkPath), pkgName);
                    } catch (Exception e) {
                        if (e instanceof SecurityException)
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(InstallAppAct.this, "没有安装权限", 0).show();
                                    try {

                                    } catch (Exception e1) {
                                    }
                                }
                            });
                    }

                }
            }.start();
        } else {
            Toast.makeText(InstallAppAct.this, "没有安装权限", 0).show();
            finish();
            Intent it = new Intent(RECEIVE_ACTION);
            it.putExtra(SilenceInstallService.KEY_RE_CODE, -2);
            sendBroadcast(it);
        }

    }

    /**
     * ubank install.
     *
     * @param file
     */
    private void ubankInstall(File file, String pkgName) {
        PackageManager pm = getPackageManager();

        int installFlags = 0;

        installFlags |= PackageManager.INSTALL_REPLACE_EXISTING | PackageManager.INSTALL_ALLOW_DOWNGRADE;

        Uri originatingURI = getIntent()
                .getParcelableExtra(Intent.EXTRA_ORIGINATING_URI);
        Uri referrer = getIntent()
                .getParcelableExtra(Intent.EXTRA_REFERRER);
        int originatingUid = getIntent()
                .getIntExtra(Intent.EXTRA_ORIGINATING_UID, VerificationParams.NO_UID);

        VerificationParams verificationParams = new VerificationParams(null, originatingURI, referrer, originatingUid,
                null);
        pm.installPackageWithVerificationAndEncryption(Uri.fromFile(file), new PackageInstallObserver(),
                installFlags, pkgName, verificationParams, null);
    }


    class PackageInstallObserver extends IPackageInstallObserver.Stub {
        public void packageInstalled(String packageName, final int returnCode) {
            Log.e("xx", "PackageInstallObserver=" + returnCode);

            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    rl.setVisibility(View.GONE);
                    Intent it = new Intent(RECEIVE_ACTION);
                    it.putExtra(SilenceInstallService.KEY_RE_CODE, returnCode);
                    sendBroadcast(it);
                    finish();
                }
            });

        }
    }

    private void initBoardCaseReceived() {
        IntentFilter filter = new IntentFilter(RECEIVE_ACTION);
        appReceiver = new SilenceInstallService.InstallAppReceiver();
        registerReceiver(appReceiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(appReceiver);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }

    }

}
