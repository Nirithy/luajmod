package android.ext;

import android.app.ActivityManager;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;

class AppDetector {
    volatile boolean appSelectByUser;
    volatile Runnable callback;
    private boolean force;
    private volatile boolean inDetectApp;
    private String prefferedPkg;
    private ProcessInfo processInfo;
    public static volatile Boolean processListWindow;

    public AppDetector(ActivityManager am, PackageManager pm) {
        this.appSelectByUser = false;
        this.callback = null;
        this.inDetectApp = false;
        this.force = false;
        this.prefferedPkg = null;
        new ProcessList(am, pm);
        AppDetector.processListWindow = Boolean.FALSE;
    }

    public boolean checkAppSelect(boolean soft, Runnable callback) {
        this.callback = null;
        if(!this.appSelectByUser) {
            this.detectApp(true);
            this.callback = callback;
            AppDetector.processListWindow = Boolean.TRUE;
            return false;
        }
        if(soft) {
            this.detectApp(false);
        }
        return true;
    }

    public void detectApp(boolean force) {
        this.detectApp(force, null);
    }

    public void detectApp(boolean force, String prefferedPkg) {
        if(this.inDetectApp) {
            if(force && MainService.instance.mDaemonManager.isDaemonRun() && Boolean.TRUE.equals(AppDetector.processListWindow)) {
                Tools.showToast(0x7F070160, 0);  // string:data_collecting_in_progress "Data collection in progress"
                return;
            }
            return;
        }
        this.inDetectApp = true;
        this.force = force;
        this.prefferedPkg = prefferedPkg;
        if(force && Boolean.TRUE.equals(AppDetector.processListWindow)) {
            Tools.showToast(0x7F07015F, 0);  // string:start_collecting_data "Start collecting data"
        }
        AppDetector.staticPost();
    }

    public void detectAppResume(List list0) {
        boolean found = false;
        if(this.inDetectApp) {
            try {
                if(list0.size() == 0) {
                    Log.w("listProcesses empty");
                    if(this.force || this.processInfo == null) {
                        Tools.showToast(0x7F07009E);  // string:failed_find_package_name "Failed to find the application package name!"
                    }
                }
                else {
                    if(this.processInfo != null) {
                        for(Object object0: list0) {
                            if(((ProcessInfo)object0).pid == this.processInfo.pid) {
                                found = true;
                                break;
                            }
                            if(false) {
                                break;
                            }
                        }
                    }
                    if(this.prefferedPkg != null) {
                        boolean cnt = false;
                        for(Object object1: list0) {
                            ProcessInfo ps = (ProcessInfo)object1;
                            if(ps.packageName.equals(this.prefferedPkg)) {
                                cnt = true;
                                this.setAppInfo(ps);
                                found = true;
                                break;
                            }
                            if(false) {
                                break;
                            }
                        }
                        if(cnt) {
                            this.force = false;
                        }
                    }
                    if(!found) {
                        this.setAppInfo(((ProcessInfo)list0.get(0)));
                    }
                    if(Config.vSpace && Config.vSpace64 == 0) {
                        Config.vSpace64 = !((ProcessInfo)list0.get(0)).x64 || !Config.vSpacePkg.contains("64") ? 2 : 1;
                    }
                    if(this.force) {
                        android.ext.AppDetector.2 appDetector$20 = new ArrayAdapter(MainService.context, list0) {
                            @Override  // android.ext.ArrayAdapter
                            public View getView(int position, View convertView, ViewGroup parent) {
                                View view1 = super.getView(position, convertView, parent);
                                TextView tv = (TextView)view1.findViewById(0x1020014);
                                if(tv != null) {
                                    ProcessInfo pi = (ProcessInfo)list0.get(position);
                                    if(pi != null) {
                                        try {
                                            tv.setText(pi.toCharSequence());
                                        }
                                        catch(Throwable e) {
                                            Log.badImplementation(e);
                                        }
                                        tv.setTag(pi);
                                        pi.loadIcon(tv);
                                    }
                                    Tools.setTextAppearance(tv, 0x7F090007);  // style:ListItemText
                                }
                                return view1;
                            }
                        };
                        AlertDialog.Builder alertDialog$Builder0 = Alert.create(Tools.getDarkContext()).setCustomTitle(Tools.getCustomTitle(0x7F070089)).setAdapter(appDetector$20, new DialogInterface.OnClickListener() {  // string:reset "Select process"
                            @Override  // android.content.DialogInterface$OnClickListener
                            public void onClick(DialogInterface dialog, int which) {
                                if(which == list0.size()) {
                                    ConfigListAdapter.showHelp(0x7F07028B);  // string:help_game_not_listed_title "Why my game is not in the process list?"
                                    return;
                                }
                                ProcessInfo processList$ProcessInfo0 = (ProcessInfo)list0.get(which);
                                AppDetector.this.setAppInfo(processList$ProcessInfo0);
                                AppDetector.this.appSelectByUser = true;
                                Tools.dismiss(dialog);
                                Runnable call = AppDetector.this.callback;
                                if(call != null) {
                                    AppDetector.this.callback = null;
                                    call.run();
                                }
                            }
                        });
                        Script.processList = list0;
                        if(Boolean.TRUE.equals(AppDetector.processListWindow)) {
                            Alert.show(alertDialog$Builder0);
                        }
                        AppDetector.processListWindow = Boolean.TRUE;
                    }
                }
            }
            finally {
                this.inDetectApp = false;
            }
        }
    }

    public boolean isAppSelectByUser() {
        return this.appSelectByUser;
    }

    void setAppInfo(ProcessInfo info) {
        info.loadIcon();
        info.resolveLibs();
        this.processInfo = info;
        Log.i(("used: " + info.dump()));
        MainService.instance.setProcessInfo(info);
    }

    public void setAppSelectByAuto() {
        this.appSelectByUser = false;
    }

    private static void staticPost() {
        ThreadManager.getHandlerUiThread().post(new Runnable() {
            @Override
            public void run() {
                MainService.instance.mDaemonManager.getProcessList();
            }
        });
    }
}

