package android.ext;

import android.app.AlertDialog;
import android.content.DialogInterface.OnShowListener;
import android.content.DialogInterface;
import android.fix.LayoutInflater;
import android.os.Handler;
import android.view.View.OnClickListener;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class BusyDialog implements DialogInterface.OnShowListener, View.OnClickListener {
    final TextView busyFoundCount;
    final TextView busyMessage;
    final ProgressBar busyProgress;
    final ProgressBar busyProgressStage;
    final View busyProgressStageLayout;
    final TextView busyProgressStageText;
    final TextView busyProgressText;
    final TextView busyTime;
    volatile AlertDialog mBusyDialog;
    final Runnable shower;
    long startProgress;
    long startProgressMax;
    long startTime;
    final View view;

    public BusyDialog() {
        this.startTime = -1L;
        this.startProgress = -1L;
        this.startProgressMax = -1L;
        this.shower = new Runnable() {
            @Override
            public void run() {
                AlertDialog dialog = BusyDialog.this.mBusyDialog;
                if(dialog != null) {
                    Alert.show(dialog);
                }
            }
        };
        this.view = LayoutInflater.inflateStatic(0x7F040019, null);  // layout:service_busy_dialog
        this.busyMessage = (TextView)this.view.findViewById(0x7F0B000E);  // id:message
        this.busyProgress = (ProgressBar)this.view.findViewById(0x7F0B0024);  // id:progress_bar
        this.busyProgressText = (TextView)this.view.findViewById(0x7F0B0023);  // id:progress_bar_text
        this.busyProgressStageLayout = this.view.findViewById(0x7F0B0077);  // id:stage_layout
        this.busyProgressStage = (ProgressBar)this.view.findViewById(0x7F0B0025);  // id:progress_bar_stage
        this.busyProgressStageText = (TextView)this.view.findViewById(0x7F0B0078);  // id:progress_bar_stage_text
        this.busyFoundCount = (TextView)this.view.findViewById(0x7F0B0079);  // id:found_count
        this.busyTime = (TextView)this.view.findViewById(0x7F0B007A);  // id:time
    }

    public void dismiss(boolean force) {
        AlertDialog dialog = this.mBusyDialog;
        if(dialog != null) {
            ThreadManager.getHandlerUiThread().removeCallbacks(this.shower);
            Tools.dismiss(dialog);
            if(force) {
                this.mBusyDialog = null;
            }
        }
    }

    public boolean isVisible() {
        return this.mBusyDialog != null;
    }

    @Override  // android.view.View$OnClickListener
    public void onClick(View v) {
        ConfigListAdapter.showHelp(0x7F070210);  // string:help_speed_up "* How to speed up search:\n\t0. If you are use an Android 
                                                 // emulator on a PC (BlueStacks, Droid4X, Andy, NOX, Memu, AMIDuOS, Windroy, AVD, Genymotion, 
                                                 // Koplayer, Leapdroid etc.), check that virtualization (VT Technology, Vanderpool 
                                                 // Technology, Virtualization Technology, VTx, VT-d, VMX, Virtual Technology, Intel-VT, 
                                                 // AMD-V, SVM, IOMMU) is enabled in the PC BIOS.\n\t1. If you have a lot of RAM, then 
                                                 // you can store the data there. This greatly speeds up the search. Set the setting 
                                                 // \"__data_in_ram__\" to \"__yes__\". In an Android emulator on a PC, this can slow 
                                                 // down the search.\n\t2. If you have little RAM, the data will be stored on the memory 
                                                 // card. Use a memory card with a high write and read speed.\n\t3. You can also specify 
                                                 // the storage of data in the internal memory of the device, if it faster than the 
                                                 // memory card. Set the setting \"__set_path__\" to the desired path.\n\t4. On search, 
                                                 // select only the desired regions. Avoid slow (\"__slow__\") and dangerous (\"__dangerous__\") 
                                                 // regions.\n\t5. You can also specify the limits of the memory range, for search, 
                                                 // if you know them. Click the button \"__more__\" in the search dialog and enter the 
                                                 // limits.\n\t6. Search for only the desired types of data. The data type \"__type_auto__\" 
                                                 // is slow.\n\t7. Disable all possible \"__hide_from_game__\" options.\n\t8. Try using 
                                                 // \"__experimental__\" or \"__extended__\" in \"__memory_access__\". These options 
                                                 // can lead to unstable work.\n    "
    }

    public void onProgress(CharSequence message, long progress, long progressMax, int stage, int stageMax, long found, String progressText) {
        if(!this.isVisible()) {
            return;
        }
        if(ThreadManager.isInUiThread()) {
            this.onProgressUi(message, progress, progressMax, stage, stageMax, found, progressText);
            return;
        }
        ThreadManager.runOnUiThread(() -> {
            if(this.val$message != null) {
                BusyDialog.this.busyMessage.setText(this.val$message);
            }
            if(this.val$progress >= 0L && this.val$progressMax != 0L) {
                BusyDialog.this.busyProgress.setIndeterminate(false);
                BusyDialog.this.busyProgress.setMax(((int)this.val$progressMax));
                BusyDialog.this.busyProgress.setProgress(((int)this.val$progress));
                BusyDialog.this.busyProgressText.setText(this.val$progressText);
            }
            else {
                BusyDialog.this.busyProgressText.setText(this.val$progressText);
                BusyDialog.this.busyProgress.setIndeterminate(true);
            }
            if(this.val$stage >= 0) {
                if(this.val$stageMax <= 1) {
                    BusyDialog.this.busyProgressStageLayout.setVisibility(8);
                }
                else {
                    BusyDialog.this.busyProgressStage.setMax(this.val$stageMax);
                    BusyDialog.this.busyProgressStage.setProgress(this.val$stage);
                    String s1 = Tools.stringFormat(Re.s(0x7F0700C3), new Object[]{this.val$stage, this.val$stageMax});  // string:from "Completed __d__ from __d__ stages"
                    BusyDialog.this.busyProgressStageText.setText(s1);
                    BusyDialog.this.busyProgressStageLayout.setVisibility(0);
                }
            }
            String s2 = Tools.stringFormat(Re.s(0x7F0700C9), new Object[]{this.val$found});  // string:found_count "Found: __d__"
            BusyDialog.this.busyFoundCount.setText(s2);
            long v5 = System.currentTimeMillis();
            if(this.val$progress < 0L) {
                BusyDialog.this.startTime = -1L;
                BusyDialog.this.startProgress = -1L;
                BusyDialog.this.startProgressMax = -1L;
            }
            else if(BusyDialog.this.startProgressMax != this.val$progressMax) {
                BusyDialog.this.startTime = v5;
                BusyDialog.this.startProgress = this.val$progress;
                BusyDialog.this.startProgressMax = this.val$progressMax;
            }
            long time = BusyDialog.this.startTime >= 0L && v5 - BusyDialog.this.startTime >= 2000L && this.val$progress - BusyDialog.this.startProgress != 0L ? Math.round(((double)(v5 - BusyDialog.this.startTime)) * ((double)(this.val$progressMax - this.val$progress)) / ((double)(this.val$progress - BusyDialog.this.startProgress)) / 1000.0) : -1L;
            if(time <= 0L) {
                BusyDialog.this.busyTime.setText("");
                return;
            }
            String s3 = Tools.stripColon(0x7F070209) + ": " + Tools.longToTime(time);  // string:time_remaining "Time remaining:"
            BusyDialog.this.busyTime.setText(s3);
        });

        class android.ext.BusyDialog.3 implements Runnable {
            android.ext.BusyDialog.3(CharSequence charSequence0, long v, long v1, int v2, int v3, long v4, String s) {
            }

            @Override
            public void run() {
                BusyDialog.this.onProgressUi(this.val$message, this.val$progress, this.val$progressMax, this.val$stage, this.val$stageMax, this.val$found, this.val$progressText);
            }
        }

    }

    // 检测为 Lambda 实现
    void onProgressUi(CharSequence message, long progress, long progressMax, int stage, int stageMax, long found, String progressText) [...]

    @Override  // android.content.DialogInterface$OnShowListener
    public void onShow(DialogInterface dialog) {
        Tools.setButtonListener(dialog, -1, 0x7F070216, MainService.instance);  // string:hide "Hide"
        Tools.setButtonListener(dialog, -2, 0x7F0B0034, MainService.instance);  // id:abort_button
    }

    public boolean show(boolean force) {
        AlertDialog dialog = this.mBusyDialog;
        if(dialog != null) {
            Alert.show(dialog);
            if(force) {
                Log.d("Show busy dialog not first time", new RuntimeException());
                return true;
            }
        }
        else if(force) {
            ThreadManager.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    BusyDialog.this.startTime = -1L;
                    BusyDialog.this.mBusyDialog = Alert.create().setView(Tools.getViewForAttach(BusyDialog.this.view)).setCancelable(false).setPositiveButton(0x7F070216, null).setNegativeButton(0x7F0700A1, null).create();  // string:hide "Hide"
                    BusyDialog.this.onProgress("", -1L, 1L, 0, 1, 0L, "");
                    Alert.setOnShowListener(BusyDialog.this.mBusyDialog, BusyDialog.this);
                    if(MainService.instance.mainDialog != null) {
                        Handler handler0 = ThreadManager.getHandlerUiThread();
                        handler0.removeCallbacks(BusyDialog.this.shower);
                        handler0.postDelayed(BusyDialog.this.shower, 100L);
                    }
                }
            });
            return true;
        }
        return true;
    }
}

