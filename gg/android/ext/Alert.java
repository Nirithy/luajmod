package android.ext;

import android.annotation.TargetApi;
import android.app.AlertDialog.Builder;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnShowListener;
import android.content.DialogInterface;
import android.os.Build.VERSION;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.BadTokenException;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class Alert {
    static class DialogDismissHolder {
        public WeakReference dialog;
        public WeakReference listener;

        public DialogDismissHolder(AlertDialog dialog, DialogDismissListener listener) {
            this.dialog = new WeakReference(dialog);
            this.listener = new WeakReference(listener);
        }
    }

    static class DialogDismissListener implements DialogInterface.OnDismissListener {
        private final List listeners;

        private DialogDismissListener() {
            this.listeners = new ArrayList();
        }

        DialogDismissListener(DialogDismissListener alert$DialogDismissListener0) {
        }

        public void add(DialogInterface.OnDismissListener listener) {
            if(listener != null && !this.listeners.contains(listener)) {
                this.listeners.add(listener);
            }
        }

        @Override  // android.content.DialogInterface$OnDismissListener
        public void onDismiss(DialogInterface dialog) {
            Alert.hideHint();
            for(Object object0: this.listeners) {
                DialogInterface.OnDismissListener listener = (DialogInterface.OnDismissListener)object0;
                if(listener != null) {
                    listener.onDismiss(dialog);
                }
            }
        }
    }

    static class DialogShowHolder {
        public WeakReference dialog;
        public WeakReference listener;

        public DialogShowHolder(AlertDialog dialog, DialogShowListener listener) {
            this.dialog = new WeakReference(dialog);
            this.listener = new WeakReference(listener);
        }
    }

    static class DialogShowListener implements DialogInterface.OnShowListener {
        private final List listeners;

        private DialogShowListener() {
            this.listeners = new ArrayList();
        }

        DialogShowListener(DialogShowListener alert$DialogShowListener0) {
        }

        public void add(DialogInterface.OnShowListener listener) {
            if(listener != null && !this.listeners.contains(listener)) {
                this.listeners.add(listener);
            }
        }

        @Override  // android.content.DialogInterface$OnShowListener
        public void onShow(DialogInterface dialog) {
            Tools.fixDialogButtonsSize(((AlertDialog)dialog));
            for(Object object0: this.listeners) {
                DialogInterface.OnShowListener listener = (DialogInterface.OnShowListener)object0;
                if(listener != null) {
                    listener.onShow(dialog);
                }
            }
        }
    }

    static class OnItemClickListenerWrapper implements AdapterView.OnItemClickListener {
        final AlertDialog dialog;
        final AdapterView.OnItemClickListener old;

        private OnItemClickListenerWrapper(AlertDialog dialog, AdapterView.OnItemClickListener base) {
            this.old = base;
            this.dialog = dialog;
        }

        OnItemClickListenerWrapper(AlertDialog alertDialog0, AdapterView.OnItemClickListener adapterView$OnItemClickListener0, OnItemClickListenerWrapper alert$OnItemClickListenerWrapper0) {
            this(alertDialog0, adapterView$OnItemClickListener0);
        }

        @Override  // android.widget.AdapterView$OnItemClickListener
        public void onItemClick(AdapterView adapterView0, View view, int position, long id) {
            try {
                this.old.onItemClick(adapterView0, view, position, id);
            }
            catch(Throwable e) {
                Log.badImplementation(e);
                Tools.dismiss(this.dialog);
            }
        }
    }

    private static volatile int counter;
    private static final List dialogDismissHolder;
    private static final List dialogShowHolder;
    private static ArrayList dialogs;
    static final Runnable shower;

    static {
        Alert.shower = () -> if(MainService.instance != null) {
            MainService.instance.showApp.update();
            if(MainService.instance.selectedTab != MainService.instance.mConfigPage) {
                MainService.instance.showApp.showHint();
            }
        };
        Alert.counter = 0;
        Alert.dialogShowHolder = new ArrayList();
        Alert.dialogDismissHolder = new ArrayList();
        Alert.dialogs = new ArrayList();
    }

    public static AlertDialog.Builder create() {
        return Alert.create(Tools.getContext());
    }

    public static AlertDialog.Builder create(Context context) {
        return new AlertDialog.Builder(context);
    }

    public static void dismissAll() {
        for(Object object0: Alert.dialogs) {
            AlertDialog d = (AlertDialog)((WeakReference)object0).get();
            if(d != null) {
                try {
                    if(!d.isShowing()) {
                        continue;
                    }
                    Tools.dismiss(d);
                }
                catch(Throwable e) {
                    Log.w(("Failed dismiss dialog: " + d), e);
                }
            }
        }
        if(MainService.instance != null) {
            MainService.instance.dismissDialog();
        }
    }

    @TargetApi(11)
    private static void fixDialog(AlertDialog dialog) {
        Window window0 = dialog.getWindow();
        if(Build.VERSION.SDK_INT >= 11) {
            window0.setCallback(new WindowCallbackWrapper(window0.getCallback()));
        }
        window0.clearFlags(0x1000000);
    }

    private static void fixListView(AlertDialog dialog) {
        ListView list;
        try {
            list = dialog.getListView();
        }
        catch(Throwable e) {
            list = null;
            Log.badImplementation(e);
        }
        if(list == null) {
            return;
        }
        else {
            try {
                AdapterView.OnItemClickListener adapterView$OnItemClickListener0 = list.getOnItemClickListener();
                if(adapterView$OnItemClickListener0 != null && !(adapterView$OnItemClickListener0 instanceof OnItemClickListenerWrapper)) {
                    list.setOnItemClickListener(new OnItemClickListenerWrapper(dialog, adapterView$OnItemClickListener0, null));
                    return;
                }
                return;
            }
            catch(Throwable e) {
            }
        }
        Log.badImplementation(e);
    }

    static void hideHint() {
        if(MainService.instance != null) {
            --Alert.counter;
            if(Alert.counter <= 0) {
                ThreadManager.getHandlerUiThread().removeCallbacks(Alert.shower);
                MainService.instance.showApp.hideHint();
                Alert.counter = 0;
            }
        }
    }

    private static boolean inInstallMode() [...] // 潜在的解密器

    public static void register(AlertDialog dialog) {
        if(dialog.getWindow().getAttributes().type < 2000) {
            return;
        }
        ArrayList oldDialogs = Alert.dialogs;
        ArrayList newDialogs = new ArrayList();
        for(Object object0: oldDialogs) {
            WeakReference weak = (WeakReference)object0;
            AlertDialog d = (AlertDialog)weak.get();
            if(d != null && d != dialog) {
                newDialogs.add(weak);
            }
        }
        newDialogs.add(new WeakReference(dialog));
        Alert.dialogs = newDialogs;
        oldDialogs.clear();
        oldDialogs.trimToSize();
    }

    public static void setOnDismissListener(AlertDialog dialog, DialogInterface.OnDismissListener listener) {
        DialogDismissHolder holder = null;
        for(int i = 0; i < Alert.dialogDismissHolder.size(); ++i) {
            DialogDismissHolder hold = (DialogDismissHolder)Alert.dialogDismissHolder.get(i);
            if(hold == null) {
                Alert.dialogDismissHolder.remove(i);
                --i;
            }
            else {
                AlertDialog d = (AlertDialog)hold.dialog.get();
                if(d == null) {
                    Alert.dialogDismissHolder.remove(i);
                    --i;
                }
                else if(d == dialog) {
                    holder = hold;
                }
            }
        }
        if(holder == null) {
            DialogDismissListener alert$DialogDismissListener0 = new DialogDismissListener(null);
            dialog.setOnDismissListener(alert$DialogDismissListener0);
            holder = new DialogDismissHolder(dialog, alert$DialogDismissListener0);
            Alert.dialogDismissHolder.add(holder);
        }
        ((DialogDismissListener)holder.listener.get()).add(listener);
    }

    public static void setOnShowListener(AlertDialog dialog, DialogInterface.OnShowListener listener) {
        DialogShowHolder holder = null;
        for(int i = 0; i < Alert.dialogShowHolder.size(); ++i) {
            DialogShowHolder hold = (DialogShowHolder)Alert.dialogShowHolder.get(i);
            if(hold == null) {
                Alert.dialogShowHolder.remove(i);
                --i;
            }
            else {
                AlertDialog d = (AlertDialog)hold.dialog.get();
                if(d == null) {
                    Alert.dialogShowHolder.remove(i);
                    --i;
                }
                else if(d == dialog) {
                    holder = hold;
                }
            }
        }
        if(holder == null) {
            DialogShowListener alert$DialogShowListener0 = new DialogShowListener(null);
            dialog.setOnShowListener(alert$DialogShowListener0);
            holder = new DialogShowHolder(dialog, alert$DialogShowListener0);
            Alert.dialogShowHolder.add(holder);
        }
        ((DialogShowListener)holder.listener.get()).add(listener);
    }

    public static AlertDialog show(AlertDialog dialog) {
        Alert.show(dialog, null);
        return dialog;
    }

    // 检测为 Lambda 实现
    public static AlertDialog show(AlertDialog dialog, EditText edit) [...]

    public static void show(AlertDialog.Builder builder) {
        Alert.show(builder, null);
    }

    public static void show(AlertDialog.Builder builder, Context context) {
        Alert.show(builder, null);
    }

    // 检测为 Lambda 实现
    public static void show(AlertDialog.Builder builder, android.ext.EditText edit) [...]

    public static void show(AlertDialog dialog, Runnable callback, boolean dismiss) {
        Alert.setOnShowListener(dialog, new DialogInterface.OnShowListener() {
            @Override  // android.content.DialogInterface$OnShowListener
            public void onShow(DialogInterface dialog) {
                Log.d((callback + ": 30_"));
                ThreadManager.getHandlerUiThread().removeCallbacks(callback);
            }
        });
        if(dismiss) {
            Alert.setOnDismissListener(dialog, new DialogInterface.OnDismissListener() {
                @Override  // android.content.DialogInterface$OnDismissListener
                public void onDismiss(DialogInterface dialog) {
                    Log.d((callback + ": 40_"));
                    ThreadManager.getHandlerUiThread().postDelayed(callback, 5000L);
                }
            });
        }
        ThreadManager.getHandlerUiThread().postDelayed(callback, 3000L);
        Log.d((callback + ": 10_"));
        Alert.show(dialog);
        Log.d((callback + ": 20_"));
    }

    public static void showAfterDaemonStart(AlertDialog.Builder builder) {
        DaemonManager.runAfterDaemonStart(() -> {
            ThreadManager.runOnUiThread(() -> {
                if(this.val$edit != null) {
                    this.val$edit.setOnEditorActionListener(new EditorActionListener(this.val$builder.create()));
                }
                Alert.fixDialog(this.val$builder.create());
                ThreadManager.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Alert.showAndFix(this.val$builder.create());
                        }
                        catch(Throwable e) {
                            Log.e("Failed show dialog on service", e);
                        }
                    }
                });
                return this.val$builder.create();
            });

            class android.ext.Alert.6 implements Runnable {
                android.ext.Alert.6(AlertDialog.Builder alertDialog$Builder0, android.ext.EditText editText0) {
                }

                @Override
                public void run() {
                    Alert.show(this.val$builder.create(), this.val$edit);
                }
            }

        });

        class android.ext.Alert.5 implements Runnable {
            android.ext.Alert.5(AlertDialog.Builder alertDialog$Builder0) {
            }

            @Override
            public void run() {
                Alert.show(this.val$builder, null);
            }
        }

    }

    static void showAndFix(AlertDialog dialog) {
        Alert.setOnShowListener(dialog, null);
        dialog.show();
    }

    // 检测为 Lambda 实现
    static void showHint() [...]

    public static void tryShow(AlertDialog dialog) {
        try {
            dialog.show();
        }
        catch(WindowManager.BadTokenException unused_ex) {
            Alert.show(dialog);
        }
    }

    class android.ext.Alert.1 implements Runnable {
        @Override
        public void run() {
            Alert.showHint();
        }
    }

}

