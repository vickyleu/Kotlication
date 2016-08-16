-optimizationpasses 5         # 指定代码的压缩级别
-dontusemixedcaseclassnames   # 是否使用大小写混合
-dontpreverify                # 混淆时是否做预校验
-verbose                      # 混淆时是否记录日志
-ignorewarnings　             #忽略所有警告
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*  # 混淆时所采用的算法



#gradlew assembleRelease
#保护注解
-keepattributes *Annotation*
-dontwarn
-keep public class * extends android.app.Activity      # 保持哪些类不被混淆
-keep public class * extends android.app.Application   # 保持哪些类不被混淆
-keep public class * extends android.app.Service       # 保持哪些类不被混淆
-keep public class * extends android.content.BroadcastReceiver  # 保持哪些类不被混淆
-keep public class * extends android.content.ContentProvider    # 保持哪些类不被混淆
-keep public class * extends android.app.backup.BackupAgentHelper # 保持哪些类不被混淆
-keep public class * extends android.preference.Preference        # 保持哪些类不被混淆
-keep public class com.android.vending.licensing.ILicensingService    # 保持哪些类不被混淆

#以libaray的形式引用了一个图片加载框架,如果不想混淆 keep 掉
-keep class com.nostra13.universalimageloader.** { *; }
-keep class com.google.gson.** { *; }
-keep class com.google.gson.jpush.** { *; }
-keep class com.google.protobuf.jpush.** { *; }
-keep class android.support.v4.** { *; }
-keep class android.support.v7.** { *; }
-keep class com.j256.ormlite.** { *; }
-keep class org.apache.http.** { *; }
-keep class org.apache.commons.** { *; }
-keep class  com.alipay.security.** { *; }
-keep class   com.loc.bf.** { *; }
-keep class   com.ta.utdid2.** { *; }

-keep class com.iflytek.msc.** { *; }
-keep class com.esapos.lib.api.** { *; }
-keep class com.autonavi.tbt.** { *; }
-keep class com.amap.api.** { *; }
-keep class com.autonavi.amap.** { *; }
-keep class com.umeng.socialize.** { *; }
-keep class okhttp3.** { *; }
-keep class okio.** { *; }
-keep class com.tencent.** { *; }
-keep class  cn.jpush.** { *; }
-keep class    rx.** { *; }
-keep class    com.ut.device.** { *; }

-dontwarn okio.**
-dontwarn rx.**




-keep public class com.umeng.socialize.**
-keep public class  com.android.org.**
-keep public class  com.google.android.**



-keep public class  org.apache.harmony.**

-keep public class  sun.misc.**
-keep public class me.dm7.barcodescannerview.**
-keep public class org.apache.commons.httpclient.**
-keep public class com.amap.api.services.**
-keep public class com.amap.api.**
-keep public class com.google.gson.jpush.**
-keep public class com.tencent.weibo.**
-keep public class org.codehaus.mojo.**
-keep public class com.alipay.tscenter.**

-dontwarn com.tencent.weibo.**
-dontwarn com.google.gson.**
-dontnote com.google.gson.**

-keepattributes Signature
-dontwarn android.net.SSLCertificateSocketFactory
-dontwarn android.app.Notification
-dontwarn com.squareup.**

-dontwarn com.amap.api.**
-dontwarn com.amap.api.services.**

-dontwarn org.apache.commons.httpclient.**
-dontnote **ViewfinderView
-dontnote **SearchBox
-dontnote **SectionListView
-dontnote **SlideBar
-dontnote **CameraPreview




-dontnote com.umeng.socialize.*
-dontwarn com.umeng.socialize.*

-keep class com.autonavi.wtbt.** { *; }

-dontnote com.autonavi.tbt.*
-dontwarn com.autonavi.tbt.*

-dontnote com.amap.api.*
-dontwarn com.amap.api.*
-dontwarn com.google.gson.*
-dontwarn  com.tencent.weibo.*
-dontwarn   com.clubank.device.*

-dontnote android.net.http.*
-dontnote org.apache.commons.codec.**
-dontnote org.apache.http.**
-dontwarn org.apache.http.**
-dontwarn
-keep class com.lsjwzh.widget.materialloadingprogressbar.** { *; }
-keepclasseswithmembernames class * {  # 保持 native 方法不被混淆
    native <methods>;
}
-keepclasseswithmembers class * {   # 保持自定义控件类不被混淆
    public <init>(android.content.Context, android.util.AttributeSet);
}
-keepclasseswithmembers class * {# 保持自定义控件类不被混淆
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclassmembers class * extends android.app.Activity { # 保持自定义控件类不被混淆
    public void *(android.view.View);
}
-keepclassmembers enum * {     # 保持枚举 enum 类不被混淆
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keep class * implements android.os.Parcelable { # 保持 Parcelable 不被混淆
    public static final android.os.Parcelable$Creator *;
}
#不混淆资源类
-keepclassmembers class **.R$* {
    public static <fields>;
}