package android.ext;

import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.fix.Activity;
import android.fix.ContextWrapper;
import android.fix.KeyboardView;
import android.fix.LayoutInflater;
import android.lang.ProcessImpl;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Parcelable;
import android.os.Process;
import android.toast.Toaster;
import android.view.KeyEvent;
import android.view.View.OnClickListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.ggdqo.MainActivity;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel.MapMode;
import java.util.ArrayList;
import java.util.Arrays;
import luaj.LuaError;
import luaj.LuaFunction;
import luaj.lib.ModLib;

public class BaseActivity extends Activity implements View.OnClickListener {
    public static class GoOnForum implements DialogInterface.OnClickListener, View.OnClickListener {
        public static final String S1 = "kwws=22jdphjxdugldq1qhw2grzqordg";
        public static final String S10 = "kwws=22jdphjxdugldq1qhw2iruxp2wrslf24<7540yluwxdo0vsdfhv0wr0uxq0jdphjxdugldq0zlwkrxw0urrw2";
        public static final String S11 = "kwws=22jdphjxdugldq1qhw2iruxp2wrslf2:6<;0jdwkhulqj0lqirupdwlrq0derxw0jj0huuruv2";
        public static final String S12 = "kwws=22jdphjxdugldq1qhw2iruxp2wrslf2594440elqdu|0vfulswv0zlwk0fruuxswhg0ru0lqydolg0khdghuv2";
        public static final String S2 = "kwws=22jdphjxdugldq1qhw2grqdwh";
        public static final String S3 = "kwws=22jdphjxdugldq1qhw2iruxp2wrslf2:7360nqrzq0sureohpv2Bgr@ilqgFrpphqw)frpphqw@76<88";
        public static final String S4 = "kwws=22jdphjxdugldq1qhw2iruxp2wrslf2:8770li0|rx0zdqw0wr0dgg0d0qhz0wudqvodwlrq0ru0lpsuryh0dq0h{lvwlqj2";
        public static final String S5 = "kwws=22jdphjxdugldq1qhw2iruxp2jdoohu|2fdwhjru|250ylghr0wxwruldov2";
        public static final String S6 = "kwws=22jdphjxdugldq1qhw2iruxp2ilohv2fdwhjru|290oxd0vfulswv2";
        public static final String S7 = "kwws=22jdphjxdugldq1qhw2khos2";
        public static final String S8 = "kwws=22jdphjxdugldq1qhw2iruxp2jdoohu|2lpdjh278:0krz0wr0uhsodfh0dup0oleudulhv0zlwk0{;90oleudulhv0{0soruh2";
        public static final String S9 = "kwws=22jdphjxdugldq1qhw2iruxp2jdoohu|2lpdjh25<70krz0wr0uxq0lq0skrhql{rv0jdphjxdugldq2";
        private String url;

        public GoOnForum(String url) {
            if(url == null) {
                url = "kwws=22jdphjxdugldq1qhw2grzqordg";
            }
            this.url = url;
        }

        @Override  // android.content.DialogInterface$OnClickListener
        public void onClick(DialogInterface dialog, int which) {
            try {
                Tools.getContext().startActivity(new Intent("android.intent.action.VIEW", Uri.parse(Tools.removeNewLinesChars(this.url))));
                Alert.dismissAll();
            }
            catch(Throwable e) {
                Log.w("Failed call activity", e);
            }
        }

        @Override  // android.view.View$OnClickListener
        public void onClick(View v) {
            this.onClick(null, 0);
        }
    }

    private static final String BUNDLE_HW_KEY = "hw";
    private static final String BUNDLE_REMOVE_INSTALLER = "reminst";
    private static final String BUNDLE_UID_KEY = "uid";
    private static final String BUNDLE_V_SPACE_KEY = "vspace";
    private static final String INSTALLER_STATE = "installer-state";
    public static volatile Context appContext;
    public static volatile Context context;
    public static Exception err_msg;
    public static volatile boolean exitOnDestroy;
    public static volatile boolean hw;
    volatile boolean installMode;
    public static volatile BaseActivity instance;
    private static volatile boolean mntExpand;
    private Handler myhandler;
    private volatile boolean onTop;
    public static Post post;
    public static volatile boolean removeInstaller;
    private WeakReference textCache;
    private StringBuilder vSpaceLog;
    public static volatile boolean waitExit;
    private WeakReference weakRoot;
    private WeakReference weakTextArea;

    static {
        BaseActivity.err_msg = null;
        BaseActivity.waitExit = false;
        BaseActivity.hw = true;
        BaseActivity.removeInstaller = false;
        BaseActivity.exitOnDestroy = true;
        Log.d("BaseActivity clinit");
        ExceptionHandler.install();
        BaseActivity.mntExpand = false;
        BaseActivity.post = new Post();
    }

    public BaseActivity() {
        boolean z = false;
        super();
        this.installMode = true;
        this.onTop = false;
        this.weakRoot = new WeakReference(null);
        this.weakTextArea = new WeakReference(null);
        this.textCache = new WeakReference(null);
        this.vSpaceLog = new StringBuilder();
        if(this instanceof MainActivity || BaseActivity.instance == null) {
            BaseActivity.instance = this;
        }
        if(this instanceof MainActivity || BaseActivity.context == null) {
            BaseActivity.context = this;
        }
        if(!(this instanceof MainActivity)) {
            z = true;
        }
        BaseActivity.hw = z;
        Log.d(("BaseActivity init: " + this));
        ExceptionHandler.install();
        this.myhandler = new Handler(Looper.getMainLooper()) {
            {
                Looper looper0 = Looper.getMainLooper();  // 捕获的参数 （可能与外部方法变量命名冲突；考虑手动重命名）
                BaseActivity.this = baseActivity0;
            }

            static BaseActivity access$0(android.ext.BaseActivity.12 baseActivity$120) {
                return BaseActivity.this;
            }

            @Override  // android.os.Handler
            public void handleMessage(Message message0) {
                LuaFunction luaFunction0 = (LuaFunction)message0.obj;
                if(message0.what != 0x400 || luaFunction0 == null || luaFunction0.isnil()) {
                    BaseActivity.err_msg = new LuaError("函数为空");
                    return;
                }
                try {
                    luaFunction0.call(ModLib._G);
                }
                catch(Exception exception0) {
                    BaseActivity.err_msg = exception0;
                    MainService.instance.currentScript.appendLog(BaseActivity.err_msg.toString());
                    try {
                        MainService.instance.interruptScript();
                    }
                    catch(Exception unused_ex) {
                    }
                }
            }
        };
    }

    static void access$1() {
        BaseActivity.checkExpand();
    }

    private static void checkExpand() {
        if(!BaseActivity.mntExpand) {
            return;
        }
        Alert.show(Alert.create(BaseActivity.context).setMessage(Re.s(0x7F0702BB)).setNeutralButton(Re.s(0x7F07009D), null).setPositiveButton(Re.s(0x7F070164), new DialogInterface.OnClickListener() {  // string:mnt_expand "__app_name__ is installed on a memory card that is used as \"Internal 
                                                                                                                                                                                                         // storage\".\nThis can interfere with the operation of virtual spaces.\nIf this happens, 
                                                                                                                                                                                                         // install __app_name__ in the internal memory of the device.\nYou may need to reformat 
                                                                                                                                                                                                         // the memory card as \"Portable storage\"."
            @Override  // android.content.DialogInterface$OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                try {
                    BaseActivity.context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://support.google.com/android/answer/6088895")));
                }
                catch(Throwable e) {
                    Log.w("Failed call activity", e);
                }
            }
        }));
    }

    private static void checkMaps(StringBuilder dbg, String name) throws IOException {
        BufferedReader bufferedReader0 = new BufferedReader(new FileReader("/proc/self/maps"));
        String s1;
        while((s1 = bufferedReader0.readLine()) != null) {
            int v = s1.indexOf(name);
            if(v >= 0) {
                int start;
                for(start = v; start > 0 && s1.charAt(start) > 0x20; --start) {
                }
                if(start < v) {
                    dbg.append("\n\'");
                    dbg.append(s1.substring(start, s1.length()).trim());
                    dbg.append('\'');
                }
            }
        }
        bufferedReader0.close();
    }

    public void checkPermissions() {
        if(!Config.vSpaceReal) {
            StringBuilder report = new StringBuilder();
            try {
                report.append("INTERNET: 0~");
                report.append(((char)(Config.vSpaceReal ? 36 : 35)));
                report.append(";1~");
                Context context0 = Tools.getContext();
                int v1 = context0.checkPermission("android.permission.INTERNET", Process.myPid(), Process.myUid());
                report.append(v1);
                report.append(";2~");
                report.append(context0.checkCallingOrSelfPermission("android.permission.INTERNET"));
                report.append(";3~");
                report.append(context0.checkCallingPermission("android.permission.INTERNET"));
                Log.d(report.toString());
                if(v1 != 0) {
                    ExceptionHandler.sendException(Thread.currentThread(), new RuntimeException(report.toString()), false);
                }
            }
            catch(Throwable e) {
                RuntimeException runtimeException0 = new RuntimeException(report.toString(), e);
                Log.badImplementation(runtimeException0);
                ExceptionHandler.sendException(Thread.currentThread(), runtimeException0, false);
            }
        }
        if(Build.VERSION.SDK_INT >= 23) {
            try {
                Context context1 = Tools.getContext();
                PackageInfo packageInfo0 = Tools.getPackageInfo(Tools.getPackageName(), 0x1000);
                if(packageInfo0 != null && packageInfo0.requestedPermissions != null) {
                    ArrayList list = new ArrayList();
                    PackageManager packageManager0 = Tools.getPackageManager();
                    String[] arr_s = packageInfo0.requestedPermissions;
                    for(int v = 0; v < arr_s.length; ++v) {
                        String permission = arr_s[v];
                        if(permission != null && (context1.checkSelfPermission(permission) != 0 || Config.vSpaceReal && packageManager0.checkPermission(permission, Config.vSpacePkg) != 0)) {
                            list.add(permission);
                        }
                    }
                    if(list.size() > 0) {
                        String[] perms = (String[])list.toArray(new String[list.size()]);
                        Log.d(("requestPermissions: " + Arrays.toString(perms)));
                        this.requestPermissions(perms, 0);
                        return;
                    }
                }
                return;
            }
            catch(Throwable e) {
            }
        }
        else {
            return;
        }
        Log.badImplementation(e);
    }

    private void clearText() {
        TextView text = (TextView)this.weakTextArea.get();
        if(text != null) {
            text.setText("");
        }
        this.load(false);
    }

    private static void dbgAppend(StringBuilder dbg, int code) {
        Log.d(("START: " + code));
        dbg.append('\n');
        dbg.append(code);
        dbg.append(": ");
    }

    private void detectVSpace() {
        StringBuilder log;
        try {
            log = this.vSpaceLog;
            File file0 = this.getFilesDir();
            String s = file0.getAbsolutePath();
            log.append(s);
            log.append('\n');
            log.append(Apk.PACKAGE);
            log.append('\n');
            this.setVSpace(s);
            if(!Config.vSpace && (BaseActivity.instance.checkCallingOrSelfPermission("android.permission.READ_SMS") == 0 || true)) {
                log.append("? ");
                File check = new File(file0, "1646161376.txt");
                FileOutputStream os = new FileOutputStream(check);
                os.write(new byte[0x1000]);
                os.flush();
                os.close();
                String s1 = Tools.getNativePath(check.getAbsolutePath()).replace("/1646161376.txt", "");
                check.delete();
                log.append(s1);
                log.append('\n');
                this.setVSpace(s1);
                if(Config.vSpace) {
                    String s3 = "/" + file0.getName();
                    String s4 = s1.replace(s3, "");
                    String s5 = s.replace(s3, "");
                    String s6 = "F2R: [" + s4.length() + "] " + s5 + " -> " + s4;
                    Log.w(s6);
                    log.append(s6);
                    log.append('\n');
                    Tools.fakeToReal = new String[]{s5, s4};
                }
                else {
                    String s2 = "Odd vSpace: " + s + "; " + s1;
                    Log.w(s2);
                    log.append(s2);
                    log.append('\n');
                    int v = Tools.getSharedPreferences().getInt("odd-vspace", 0);
                    log.append(v);
                    log.append('\n');
                    if(v == 1) {
                        Config.vSpace = true;
                    }
                    else if(v == 0) {
                        android.ext.BaseActivity.6 baseActivity$60 = new DialogInterface.OnClickListener() {
                            @Override  // android.content.DialogInterface$OnClickListener
                            public void onClick(DialogInterface dialog, int which) {
                                int v1 = 1;
                                SPEditor sPEditor0 = new SPEditor();
                                if(which != -1) {
                                    v1 = 2;
                                }
                                sPEditor0.putInt("odd-vspace", v1).commit();
                                if(which == -1) {
                                    ConfigListAdapter.needRestart();
                                }
                            }
                        };
                        Alert.show(Alert.create(BaseActivity.context).setMessage(Re.s(0x7F0702E3)).setCancelable(false).setPositiveButton(Re.s(0x7F07009B), baseActivity$60).setNegativeButton(Re.s(0x7F07009C), baseActivity$60));  // string:odd_vspace "__app_name__ launched in the virtual space?"
                    }
                }
            }
            String s7 = "Files: " + s + ' ' + Apk.PACKAGE;
            Log.d(s7);
            log.append(s7);
            log.append('\n');
        }
        catch(Throwable e) {
            Log.badImplementation(e);
            log.append(e.toString());
            log.append('\n');
        }
        Config.vSpaceReal = Config.vSpace;
        if(Config.vSpace && (Config.configClient & 0x200) != 0) {
            Config.vSpace = false;
        }
    }

    @Override  // android.app.Activity
    public boolean dispatchKeyEvent(KeyEvent event) {
        try {
            return super.dispatchKeyEvent(event);
        }
        catch(IllegalStateException e) {
            Log.badImplementation(e);
            return false;
        }
    }

    private static void fixDurationScale() {
        if(Build.VERSION.SDK_INT >= 11) {
            try {
                Field field0 = ValueAnimator.class.getDeclaredField("sDurationScale");
                field0.setAccessible(true);
                Object object0 = field0.get(null);
                if(object0 instanceof Float && 0.0f.equals(object0)) {
                    field0.set(null, 1.0f);
                    Log.d("Fix sDurationScale");
                    return;
                }
                return;
            }
            catch(Throwable e) {
            }
        }
        else {
            return;
        }
        Log.w("failed fix sDurationScale", e);
    }

    // 去混淆评级： 中等(60)
    public static String getClassName() [...] // 潜在的解密器

    public Handler getHandler() {
        return this.myhandler;
    }

    public static Bundle getHwBundle() {
        Bundle params = new Bundle();
        params.putBoolean("hw", BaseActivity.hw);
        params.putInt("uid", Process.myUid());
        params.putBoolean("vspace", Config.vSpace);
        if(BaseActivity.removeInstaller) {
            Log.d("removeInstaller: to bundle");
            params.putBoolean("reminst", true);
        }
        return params;
    }

    static String getLogcatFilename() {
        String s = Tools.getSdcardPath();
        StringBuilder stringBuilder0 = new StringBuilder("GG_logcat_");
        if(Config.vSpace) {
            return new File(s, stringBuilder0.append("v").append(96.0f).append('_').append(0x3E79).append('_').append(Build.VERSION.RELEASE).append('_').append(Build.VERSION.SDK_INT).append(".log").toString()).getAbsolutePath();
        }
        return Config.vSpaceReal ? new File(s, stringBuilder0.append("R").append(96.0f).append('_').append(0x3E79).append('_').append(Build.VERSION.RELEASE).append('_').append(Build.VERSION.SDK_INT).append(".log").toString()).getAbsolutePath() : new File(s, stringBuilder0.append("r").append(96.0f).append('_').append(0x3E79).append('_').append(Build.VERSION.RELEASE).append('_').append(Build.VERSION.SDK_INT).append(".log").toString()).getAbsolutePath();
    }

    private String getVSpaceLog() {
        StringBuilder log = this.vSpaceLog;
        this.vSpaceLog = null;
        return log == null ? "" : log.toString();
    }

    public boolean isOnTop() {
        return this.onTop;
    }

    private void load(boolean selected) {
        View root = (View)this.weakRoot.get();
        if(root != null) {
            View view1 = root.findViewById(0x7F0B0035);  // id:starter
            if(selected) {
                if(view1 == null) {
                    if(Tools.isLandscape() == 1) {
                        ((ViewGroup)root).addView(Tools.getViewForAttach(((ViewGroup)LayoutInflater.inflateStatic(0x7F040009, null)).findViewById(0x7F0B0035)));  // layout:main_land
                    }
                    else {
                        ((ViewGroup)root).addView(Tools.getViewForAttach(((ViewGroup)LayoutInflater.inflateStatic(0x7F040033, null)).findViewById(0x7F0B0035)));  // layout:main_port
                    }
                }
                TextView textArea = (TextView)root.findViewById(0x7F0B0036);  // id:front_text
                this.weakTextArea = new WeakReference(textArea);
                for(int v = 0; v < 6; ++v) {
                    View view2 = root.findViewById(new int[]{0x7F0B0039, 0x7F0B003B, 0x7F0B003A, 0x7F0B0038, 0x7F0B0037, 0x7F0B0013}[v]);  // id:btn_stop_service
                    if(view2 instanceof TextView) {
                        ((TextView)view2).setText(Re.s(((TextView)view2).getText().toString()));
                    }
                    view2.setOnClickListener(this);
                }
                Tools.fixViews(textArea);
                return;
            }
            if(view1 != null) {
                TextView text = (TextView)this.weakTextArea.get();
                if(text != null) {
                    Tools.fixLeak(text);
                    Tools.getViewForAttach(text);
                }
                Tools.getViewForAttach(view1);
            }
        }
    }

    private void loadMainScreen() {
        BaseActivity.fixDurationScale();
        if(Tools.isLandscape() == 1) {
            this.setContentView(0x7F040009);  // layout:main_land
        }
        else {
            this.setContentView(0x7F040033);  // layout:main_port
        }
        View view0 = this.findViewById(0x7F0B0030);  // id:root
        this.weakRoot = new WeakReference(view0);
        TextView installMsg = (TextView)view0.findViewById(0x7F0B0033);  // id:installing
        installMsg.setText(Re.s(0x7F070152));  // string:installing "__app_name__ performs a reinstallation of itself with a random 
                                               // name to exclude detection by games. Wait."
        Installer.weakBar = new WeakReference(((ProgressBar)view0.findViewById(0x7F0B0024)));  // id:progress_bar
        Installer.weakBarText = new WeakReference(((TextView)view0.findViewById(0x7F0B0023)));  // id:progress_bar_text
        Installer.weakMsg = new WeakReference(installMsg);
        Installer.weakCancel = new WeakReference(((Button)view0.findViewById(0x7F0B0034)));  // id:abort_button
        this.setInstallerMode(0);
        this.load(true);
    }

    private void nothing() {
    }

    @Override  // android.app.Activity
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(((this.installMode ? "Installer: " : "") + "onActivityResult()"));
        super.onActivityResult(requestCode, resultCode, data);
        if(this.installMode) {
            Installer.onActivityResult(requestCode, resultCode, data);
            return;
        }
        CheckFloatingWindow.checkAPI();
    }

    @Override  // android.app.Activity
    public void onBackPressed() {
        try {
            super.onBackPressed();
        }
        catch(IllegalStateException e) {
            Log.badImplementation(e);
        }
    }

    @Override  // android.view.View$OnClickListener
    public void onClick(View v) {
        if(v == null) {
            return;
        }
        switch(v.getId()) {
            case 0x7F0B0013: {  // id:help
                ConfigListAdapter.showHelp();
                return;
            }
            case 0x7F0B0037: {  // id:language
                ConfigListAdapter.configLanguage();
                return;
            }
            case 0x7F0B0038: {  // id:fix_it
                try {
                    this.showFixIt(BaseActivity.context).show();
                }
                catch(Throwable e) {
                    Log.badImplementation(e);
                    Alert.show(this.showFixIt(Tools.getContext()));
                }
                return;
            }
            case 0x7F0B0039: {  // id:btn_stop_service
                Log.d("Pushed exit");
                this.finish();
                new ExitListener(100).onClick(null, 0);
                return;
            }
            case 0x7F0B003A: {  // id:check
                new CheckFloatingWindow(this).doCheck();
                return;
            }
            case 0x7F0B003B: {  // id:btn_start_usage
                if(SystemConstants.useFloatWindows) {
                    try {
                        if(!this.moveTaskToBack(true)) {
                            this.finish();
                        }
                    }
                    catch(Throwable e) {
                        Log.badImplementation(e);
                    }
                }
                MainService.instance.startHotkeyDetection();
            }
        }
    }

    @Override  // android.fix.Activity
    public void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(1);
        super.onCreate(savedInstanceState);
        Toaster.init(this.getApplication());
        ExceptionHandler.install();
        if(this instanceof MainActivity || BaseActivity.instance == null) {
            BaseActivity.instance = this;
        }
        if(this instanceof MainActivity || !(BaseActivity.context instanceof ContextWrapper)) {
            BaseActivity.context = ContextWrapper.wrap(this);
        }
        if(this instanceof MainActivity || BaseActivity.appContext == null) {
            BaseActivity.appContext = ContextWrapper.wrap(this.getApplicationContext());
        }
        try {
            android.fix.Button btn = new android.fix.Button(this);
            btn.setText("Fix it");
            btn.setId(0x7F0B0038);  // id:fix_it
            btn.setOnClickListener(this);
            this.setContentView(btn);
        }
        catch(Throwable e) {
            Log.badImplementation(e);
        }
        this.detectVSpace();
        KeyboardView.inEditMode = false;
        Tools.init(this);
        Window window0 = this.getWindow();
        if(window0 != null) {
            if(BaseActivity.hw) {
                window0.addFlags(1);
            }
            else {
                window0.clearFlags(1);
            }
        }
        ContextWrapper.checkFix(BaseActivity.context);
        AppLocale.loadLocale();
        this.startRecordLogcat();
        this.loadMainScreen();
        Tools.checkForBadContext();
        android.ext.BaseActivity.7 callback = new Runnable() {
            private boolean called;

            {
                Bundle bundle0 = savedInstanceState;  // 捕获的参数 （可能与外部方法变量命名冲突；考虑手动重命名）
                this.called = false;
            }

            @Override
            public void run() {
                Log.d((this + ": 200"));
                if(this.called) {
                    Log.d((this + ": already"));
                    return;
                }
                this.called = true;
                BaseActivity.this.onCreate2(savedInstanceState);
            }
        };
        Log.d((callback + ": 10"));
        Installer.removeInstaller(callback);
        Log.d((callback + ": 20"));
    }

    void onCreate2(Bundle savedInstanceState) {
        boolean work = true;
        if(!Config.vSpaceReal && !Installer.needInstall() && Tools.getSharedPreferences().getInt("root-ok", 0) != 0x3E79) {
            try {
                if(RootDetector.runCmd("exec id", 10).contains("uid=0")) {
                    new SPEditor().putInt("root-ok", 0x3E79).commit();
                }
                else {
                    work = false;
                }
            }
            catch(Throwable unused_ex) {
                work = false;
            }
        }
        BaseActivity.useUID(work);
        this.onCreate3(savedInstanceState);
    }

    void onCreate3(Bundle savedInstanceState) {
        if(savedInstanceState != null) {
            Parcelable parcelable0 = savedInstanceState.getParcelable("installer-state");
            if(parcelable0 instanceof State) {
                Installer.setState(((State)parcelable0));
            }
        }
        if(Main.loaded) {
            this.setInstallerMode(2);
            return;
        }
        Main.start();
    }

    @Override  // android.fix.Activity
    public void onDestroy() {
        this.setOnTop(false);
        this.clearText();
        super.onDestroy();
        if(!this.installMode && BaseActivity.exitOnDestroy) {
            Log.i("onDestroy(exit)");
            Main.exit();
            return;
        }
        Log.i(((this.installMode ? "Installer: " : "") + "onDestroy()"));
    }

    @Override  // android.app.Activity
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        try {
            return super.onKeyUp(keyCode, event);
        }
        catch(IllegalStateException e) {
            Log.badImplementation(e);
            return false;
        }
    }

    @Override  // android.fix.Activity
    public void onPause() {
        Log.i(((this.installMode ? "Installer: " : "") + "onPause()"));
        this.setOnTop(false);
        if(!this.installMode && MainService.instance != null && SystemConstants.useFloatWindows) {
            MainService.instance.startHotkeyDetection();
        }
        super.onPause();
    }

    @Override  // android.fix.Activity
    protected void onPostCreate(Bundle savedInstanceState) {
        Log.i(((this.installMode ? "Installer: " : "") + "onPostCreate()"));
        super.onPostCreate(savedInstanceState);
    }

    @Override  // android.fix.Activity
    protected void onPostResume() {
        Log.i(((this.installMode ? "Installer: " : "") + "onPostResume()"));
        this.setOnTop(true);
        this.load(true);
        super.onPostResume();
    }

    @Override  // android.app.Activity
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if(permissions != null && grantResults != null) {
            int v1 = Math.min(permissions.length, grantResults.length);
            for(int i = 0; i < v1; ++i) {
                String permission = permissions[i];
                if(permission != null) {
                    Log.d((permission + ": " + grantResults[i]));
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override  // android.fix.Activity
    protected void onRestart() {
        Log.i(((this.installMode ? "Installer: " : "") + "onRestart()"));
        this.setOnTop(true);
        this.load(true);
        super.onRestart();
    }

    @Override  // android.fix.Activity
    public void onResume() {
        Log.i(((this.installMode ? "Installer: " : "") + "onResume()"));
        this.setOnTop(true);
        this.load(true);
        super.onResume();
        if(!this.installMode && MainService.instance != null) {
            if(SystemConstants.useFloatWindows) {
                MainService.instance.stopHotkeyDetection();
                return;
            }
            if(MainService.instance.mainDialog == null) {
                BaseActivity.staticPost();
            }
        }
    }

    @Override  // android.app.Activity
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("installer-state", Installer.getState());
        super.onSaveInstanceState(outState);
    }

    @Override  // android.fix.Activity
    protected void onStart() {
        Log.i(((this.installMode ? "Installer: " : "") + "onStart()"));
        this.setOnTop(true);
        this.load(true);
        super.onStart();
    }

    @Override  // android.fix.Activity
    public void onStop() {
        Log.i(((this.installMode ? "Installer: " : "") + "onStop()"));
        this.setOnTop(false);
        this.clearText();
        super.onStop();
    }

    public void postFunc(LuaFunction luaFunction0) {
        BaseActivity.err_msg = null;
        Post.trd = null;
        BaseActivity.post.postFunc(luaFunction0);
        if(BaseActivity.err_msg != null) {
            MainService.instance.currentScript.appendLog(BaseActivity.err_msg.toString());
            Post.flags = true;
            this.myhandler.removeMessages(0x400);
            try {
                Post.trd.interrupt();
            }
            catch(Exception unused_ex) {
            }
            try {
                MainService.instance.interruptScript();
            }
            catch(Exception unused_ex) {
            }
        }
    }

    static void recordLogcat() {
        String s = BaseActivity.getLogcatFilename();
        Alert.show(Alert.create(BaseActivity.context).setMessage(Tools.stringFormat(Re.s(0x7F0702A6), new Object[]{s})).setPositiveButton(Re.s(0x7F07009D), new DialogInterface.OnClickListener() {  // string:record_logcat_info "__app_name__ will be restarted with recording __logcat__ 
                                                                                                                                                                                                     // into the file:\n\"__s__\".\n\nTo end the recording, exit from __app_name__.\nAfter 
                                                                                                                                                                                                     // that, you can send the log file to where you need it."
            @Override  // android.content.DialogInterface$OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                Config.get(0x7F0B00C2).value = 0x3E79;  // id:record_logcat
                Config.save();
                new ExitListener(1400, true).onClick(null, 0);
            }
        }).setNegativeButton(Re.s(0x7F0700A1), null));  // string:cancel "Cancel"
    }

    public static void restartApp() {
        try {
            Main.doRestart = 2;
            Main.exit();
        }
        catch(Throwable e) {
            throw new RuntimeException("Failed restart", e);
        }
    }

    public static void setHwBundle(Bundle params) {
        if(params != null) {
            if(params.containsKey("reminst")) {
                Log.d("removeInstaller: from bundle");
                BaseActivity.removeInstaller = true;
            }
            if(params.containsKey("hw")) {
                BaseActivity.hw = params.getBoolean("hw");
            }
            if(params.containsKey("uid")) {
                int v = params.getInt("uid");
                int v1 = Process.myUid();
                if(params.getBoolean("vspace") && v1 != v) {
                    Log.d(("Kill by diff uids: " + v1 + " != " + v));
                    Main.die();
                }
            }
        }
    }

    void setInstallerMode(int isInstallerMode) {
        ThreadManager.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int v = 0;
                View root = (View)BaseActivity.this.weakRoot.get();
                if(root != null) {
                    View view1 = root.findViewById(0x7F0B0031);  // id:loader
                    if(view1 != null) {
                        view1.setVisibility((isInstallerMode == 0 ? 0 : 8));
                    }
                    View view2 = root.findViewById(0x7F0B0032);  // id:installer
                    if(view2 != null) {
                        view2.setVisibility((isInstallerMode == 1 ? 0 : 8));
                    }
                    View view3 = root.findViewById(0x7F0B0035);  // id:starter
                    if(view3 != null) {
                        if(isInstallerMode != 2) {
                            v = 8;
                        }
                        view3.setVisibility(v);
                    }
                    if(isInstallerMode == 2) {
                        if(view1 != null) {
                            Tools.getViewForAttach(view1);
                        }
                        if(view2 != null) {
                            Tools.getViewForAttach(view2);
                        }
                    }
                }
            }
        });
    }

    public void setOnTop(boolean value) {
        boolean old = this.onTop;
        this.onTop = value;
        if(old != value && MainService.instance != null) {
            MainService.instance.updateNotification();
        }
    }

    private void setVSpace(String files) {
        boolean z = true;
        StringBuilder log = this.vSpaceLog;
        String vpkg = null;
        if(files.startsWith("/mnt/expand/")) {
            files = files.replace("/mnt/expand/" + files.split("/", -1)[3], "/data");
            BaseActivity.mntExpand = true;
            log.append("1\n");
        }
        if(files.startsWith("/data/data/")) {
            vpkg = files.split("/", -1)[3];
            log.append("2\n");
        }
        else if(files.startsWith("/data/user/")) {
            vpkg = files.split("/", -1)[4];
            log.append("3\n");
        }
        Config.vSpaceName = "";
        if(vpkg != null) {
            log.append(vpkg);
            log.append("\n4\n");
            if(vpkg.startsWith(Apk.PACKAGE)) {
                z = false;
            }
            try {
                Config.vSpace = z;
                Config.vSpacePkg = vpkg;
                Config.vSpaceGuest = vpkg.contains("vmos");
                Config.vSpaceName = Tools.getApplicationLabel(Tools.getApplicationInfo(vpkg));
            }
            catch(Throwable e) {
                Log.w(("Failed get vSpace name for " + vpkg), e);
            }
        }
        if(Config.vSpaceName.length() == 0) {
            Config.vSpaceName = Re.s(0x7F0702C5);  // string:virtual_space "Virtual Space App"
        }
        log.append(Config.vSpace);
        log.append('\n');
    }

    static void showDebugInfo() {
        StringBuilder dbg = new StringBuilder();
        BaseActivity.dbgAppend(dbg, 1);
        try {
            dbg.append(BaseActivity.instance.getFilesDir());
        }
        catch(Throwable e) {
            dbg.append(e);
        }
        BaseActivity.dbgAppend(dbg, 2);
        int v = Process.myUid();
        try {
            dbg.append(v);
        }
        catch(Throwable e) {
            dbg.append(e);
        }
        BaseActivity.dbgAppend(dbg, 3);
        try {
            dbg.append(Arrays.toString(BaseActivity.instance.getPackageManager().getPackagesForUid(v)));
        }
        catch(Throwable e) {
            dbg.append(e);
        }
        BaseActivity.dbgAppend(dbg, 4);
        try {
            dbg.append(Arrays.toString(BaseActivity.instance.getPackageManager().getPackageGids("com.ggdqo")));
        }
        catch(Throwable e) {
            dbg.append(e);
        }
        BaseActivity.dbgAppend(dbg, 5);
        try {
            dbg.append(BaseActivity.instance.getPackageManager().getPackageUid("com.ggdqo", 0));
        }
        catch(Throwable e) {
            dbg.append(e);
        }
        BaseActivity.dbgAppend(dbg, 6);
        try {
            dbg.append(BaseActivity.instance.checkCallingOrSelfPermission("android.permission.READ_SMS"));
        }
        catch(Throwable e) {
            dbg.append(e);
        }
        dbg.append(' ');
        dbg.append(Tools.getSharedPreferences().getInt("odd-vspace", 0));
        BaseActivity.dbgAppend(dbg, 7);
        try {
            dbg.append(Build.VERSION.SDK_INT);
        }
        catch(Throwable e) {
            dbg.append(e);
        }
        BaseActivity.dbgAppend(dbg, 8);
        try {
            File file0 = new File(BaseActivity.instance.getFilesDir(), "-1863902840.txt");
            try {
                FileOutputStream fileOutputStream0 = new FileOutputStream(file0);
                fileOutputStream0.write(new byte[0x1000]);
                fileOutputStream0.flush();
                File file1 = new File("/proc/self/fd/" + ProcessImpl.getFD(fileOutputStream0.getFD()));
                dbg.append(file1.getPath());
                dbg.append(" -> ");
                dbg.append(file1.getCanonicalPath());
                try {
                    FileInputStream fileInputStream0 = new FileInputStream(file0);
                    MappedByteBuffer mappedByteBuffer0 = fileInputStream0.getChannel().map(FileChannel.MapMode.READ_ONLY, 0L, 0x1000L);
                    BaseActivity.checkMaps(dbg, "-1863902840.txt");
                    mappedByteBuffer0.capacity();
                    fileInputStream0.close();
                }
                catch(Throwable e) {
                    dbg.append('\n');
                    dbg.append(e);
                }
                dbg.append("\nN: \'");
                dbg.append(Tools.getNativePath(file0.getAbsolutePath()));
                dbg.append('\'');
                fileOutputStream0.close();
            }
            finally {
                file0.delete();
            }
        }
        catch(Throwable e) {
            dbg.append(e);
        }
        BaseActivity.dbgAppend(dbg, 9);
        try {
            BaseActivity.checkMaps(dbg, "com.ggdqo");
        }
        catch(Throwable e) {
            dbg.append(e);
        }
        BaseActivity.dbgAppend(dbg, 10);
        try {
            dbg.append(ConfigListAdapter.isClient64());
        }
        catch(Throwable e) {
            dbg.append(e);
        }
        BaseActivity.dbgAppend(dbg, 11);
        try {
            File file2 = new File("/proc/self/exe");
            dbg.append("/proc/self/exe");
            dbg.append(" -> ");
            dbg.append(file2.getCanonicalPath());
        }
        catch(Throwable e) {
            dbg.append(e);
        }
        BaseActivity.dbgAppend(dbg, 12);
        try {
            dbg.append(Process.myPid());
        }
        catch(Throwable e) {
            dbg.append(e);
        }
        BaseActivity.dbgAppend(dbg, 13);
        try {
            FileInputStream fileInputStream1 = new FileInputStream("/proc/self/status");
            byte[] bytes = new byte[0x10000];
            int v2 = fileInputStream1.read(bytes);
            dbg.append(v2);
            dbg.append(" -> ");
            if(v2 > 0) {
                dbg.append(new String(bytes, 0, v2));
            }
            fileInputStream1.close();
        }
        catch(Throwable e) {
            dbg.append(e);
        }
        Log.d("FIN");
        Tools.alertBigText(dbg.toString().trim(), 0);
    }

    // 去混淆评级： 低(20)
    private AlertDialog showFixIt(Context context) {
        return Alert.create(Tools.getDarkContext()).setCustomTitle(Tools.getCustomTitle(0x7F070164)).setItems(new String[]{Re.s((SystemConstants.useFloatWindows ? 0x7F070188 : 0x7F070189)), Re.s(((Config.configDaemon & 4) == 0 ? 0x7F0701F6 : 0x7F0701F5)), Re.s(0x7F0701FB), Re.s(0x7F0701FC), Config.get(0x7F0B009C).toString(), Config.get(0x7F0B009D).toString(), Config.get(0x7F0B00BC).toString(), Re.s(0x7F0702DB), Re.s(0x7F07025F), Re.s(0x7F0702A5), Re.s(0x7F0702BA), Config.get(0x7F0B009B).toString(), Config.get(0x7F0B00AE).toString(), Config.get(0x7F0B009A).toString(), Config.get(0x7F0B00B1).toString()}, new DialogInterface.OnClickListener() {  // string:fix_it "Fix it"
            @Override  // android.content.DialogInterface$OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                int v1 = 0;
                switch(which) {
                    case 0: {
                        FloatCheck.setPref(SystemConstants.useFloatWindows);
                        break;
                    }
                    case 1: {
                        Option config$Option0 = Config.get(0x7F0B00BA);  // id:selinux
                        if((Config.configDaemon & 4) == 0) {
                            v1 = 1;
                        }
                        config$Option0.value = v1;
                        Config.save();
                        break;
                    }
                    case 2: {
                        new CheckFloatingWindow(BaseActivity.this).showDialog();
                        return;
                    }
                    case 3: {
                        Config.get(0x7F0B00BD).value = -1;  // id:config_float_type
                        Config.get(0x7F0B00BE).value = -1;  // id:config_float_flags
                        Config.save();
                        break;
                    }
                    case 4: {
                        Option config$Option1 = Config.get(0x7F0B009C);  // id:config_use_notification
                        if((Config.configClient & 0x100) == 0) {
                            v1 = 1;
                        }
                        config$Option1.value = v1;
                        Config.save();
                        break;
                    }
                    case 5: {
                        Config.get(0x7F0B009D).change();  // id:config_hot_key
                        return;
                    }
                    case 6: {
                        BaseActivity.this.switchContext();
                        return;
                    }
                    case 7: {
                        Config.ignore = 0L;
                        Config.save();
                        break;
                    }
                    case 8: {
                        ConfigListAdapter.changeSu();
                        return;
                    }
                    case 9: {
                        BaseActivity.recordLogcat();
                        return;
                    }
                    case 10: {
                        BaseActivity.showDebugInfo();
                        return;
                    }
                    case 11: {
                        Config.get(0x7F0B009B).change();  // id:config_prevent_unload
                        return;
                    }
                    case 12: {
                        Config.get(0x7F0B00AE).change();  // id:config_acceleration
                        return;
                    }
                    case 13: {
                        Config.get(0x7F0B009A).change();  // id:config_vspace_root
                        return;
                    }
                    case 14: {
                        Config.get(0x7F0B00B1).change();  // id:config_number_locale
                        return;
                    }
                }
                BaseActivity.restartApp();
            }
        }).setNegativeButton(Re.s(0x7F0700A1), null).create();  // string:cancel "Cancel"
    }

    void startRecordLogcat() {
        String s = this.getVSpaceLog();
        if(Config.get(0x7F0B00C2).value != 0x3E79) {  // id:record_logcat
            return;
        }
        Config.get(0x7F0B00C2).value = 0;  // id:record_logcat
        Config.save();
        new DaemonThread(new Runnable() {
            @Override
            public void run() {
                try {
                    String s = BaseActivity.getLogcatFilename();
                    File file = new File(s);
                    FileOutputStream os = new FileOutputStream(file);
                    BaseActivity.writeLogcatHeader(os, s, s);
                    Tools.chmod(file, 420);
                    java.lang.Process process0 = RootDetector.tryRoot((s.contains("/emulated/0") ? "chmod 0644 " + s.replace("/emulated/0", "/emulated/legacy") + " 2>&1 ; " + ("chmod 0644 " + s + " 2>&1 ; logcat -v threadtime -b main -b system *:V 2>&1 ; exit\n") : "chmod 0644 " + s + " 2>&1 ; logcat -v threadtime -b main -b system *:V 2>&1 ; exit\n"));
                    InputStream inputStream0 = process0.getInputStream();
                    byte[] buf = new byte[0x2000];
                    if(Tools.isExited(process0)) {
                        Tools.showToast((Re.s(0x7F0702A5) + ": " + Re.s(0x7F0700AE)));  // string:record_logcat "Record __logcat__"
                    }
                    else {
                        Tools.showToast((Re.s(0x7F0702A5) + ": " + s));  // string:record_logcat "Record __logcat__"
                    }
                    while(!Thread.interrupted()) {
                        int v = inputStream0.read(buf);
                        if(v > 0) {
                            os.write(buf, 0, v);
                            os.flush();
                        }
                        else if(v == -1 && Tools.isExited(process0)) {
                            break;
                        }
                    }
                    os.close();
                    Tools.showToast((Re.s(0x7F0702A5) + ": " + Re.s(0x7F0700B8)));  // string:record_logcat "Record __logcat__"
                }
                catch(Throwable e) {
                    Log.badImplementation(e);
                    Tools.showToast((Re.s(0x7F0702A5) + "> " + Re.s(0x7F0700AE)));  // string:record_logcat "Record __logcat__"
                }
            }
        }, "RecordLogcat").start();
    }

    private static void staticPost() {
        ThreadManager.getHandlerUiThread().post(new Runnable() {
            @Override
            public void run() {
                MainService.instance.startHotkeyDetection();
            }
        });
    }

    public void stopFunc() {
        BaseActivity.post.unlock();
    }

    void switchContext() {
        Option config$Option0 = Config.get(0x7F0B00BC);  // id:config_context_source
        config$Option0.setOnChangedListner(new OnChangedListener() {
            @Override  // android.ext.Config$Option$OnChangedListener
            public void onChanged(int value) {
                config$Option0.setOnChangedListner(null);
                BaseActivity.restartApp();
            }
        });
        config$Option0.change();
    }

    public static void useUID(boolean z) {
        boolean z1 = false;
        if(z) {
            try {
                if(RootDetector.runCmd("exec id", 10).contains("uid=0")) {
                    z1 = true;
                    goto label_4;
                }
                else {
                    Config.vSpace = true;
                    Config.vSpaceReal = true;
                    return;
                }
                return;
            }
            catch(Throwable unused_ex) {
            }
        label_4:
            if(!z1) {
                Config.vSpace = true;
                Config.vSpaceReal = true;
            }
            return;
        }
        Config.vSpace = true;
        Config.vSpaceReal = true;
    }

    static void writeLogcatHeader(OutputStream os, String logFilename, String vSpaceLog) {
        try {
            os.write(("\n\nWed Mar 04 04:36:01 CST 2026" + '\n' + logFilename + '\n' + Debug.getInfo() + '\n' + "su" + '\n' + Tools.getContext().getFilesDir().getAbsolutePath() + '\n' + vSpaceLog + "\n\n").getBytes());
            os.flush();
        }
        catch(Throwable e) {
            Log.w("Fail write logcat header", e);
        }
    }
}

