package android.ext;

import android.app.AlertDialog.Builder;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.DialogInterface.OnShowListener;
import android.content.DialogInterface;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.fix.LayoutInflater;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Build;
import android.sup.ContainerHelpers;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.DisplayMetrics;
import android.view.View.OnClickListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.SeekBar;
import android.widget.TextView;
import com.ggdqo.ActivityMain;
import com.ggdqo.MainActivity;
import java.io.FileInputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;

public class ConfigListAdapter extends BaseAdapter implements AdapterView.OnItemClickListener {
    public static final String DEVS = null;
    private static final String HIDE_ICON = "hide-icon";
    private static final int HW = 2;
    static final int RESTORE = 3;
    private static final int ROW_SIZE = 3;
    public static final String SP_USAGE = "mc";
    private static final int SW = 1;
    public static long USAGE;
    static final int[] helpConfig;
    static final String[] helpVideos;
    private WeakReference mConfigIcon;
    private WeakReference mConfigIconTextView;
    private WeakReference mGameSection;
    private View[] mViewList;
    private ScrollPos scrollPos;
    private boolean updated;

    static {
        ConfigListAdapter.DEVS = "\td2dyno - __owner__, __design__, __pm__.\n\tEnyby - __code__, __pm__.\n\tTrasd - __tech__, __pm__.\n\tAqua - __creator__.\n";
        ConfigListAdapter.USAGE = 0L;
        ConfigListAdapter.helpConfig = new int[]{0x7F0701BA, 0x7F070032, -1, 0x7F070294, 0x7F070295, -1, 0x7F07017F, 0x7F070180, 2, 0x7F07012C, 0x7F070067, -1, 0x7F0702CB, 0x7F0702CC, 21, 0x7F07012D, 0x7F070124, -1, 0x7F07012E, 0x7F070125, 1, 0x7F07012F, 0x7F070065, 6, 0x7F07018B, 0x7F07018C, 5, 0x7F0701B8, 0x7F0701B9, 7, 0x7F070101, 0x7F070126, 0, 0x7F0701FF, 0x7F070200, 8, 0x7F07020A, 0x7F07020B, 9, 0x7F070229, 0x7F07031F, 12, 0x7F070225, 0x7F070226, 11, 0x7F07028E, 0x7F07028F, 20, 0x7F070263, 0x7F070264, 16, 0x7F070130, 0x7F070128, 4, 0x7F070131, 0x7F070129, 3, 0x7F07020F, 0x7F070210, 10, 0x7F07026F, 0x7F07006D, 14, 0x7F070344, 0x7F070325, 24, 0x7F070132, 0x7F07003B, -1, 0x7F070240, 0x7F070241, -1, 0x7F07025A, 0x7F07025B, 17, 0x7F07030B, 0x7F07030C, 22, 0x7F07030D, 0x7F07030E, 23, 0x7F070253, 0x7F070254, 13, 0x7F07027C, 0x7F07027E, 18, 0x7F070047, 0x7F070282, 15, 0x7F070271, 0x7F07007A, -1, 0x7F070289, 0x7F07028A, 19, 0x7F07028B, 0x7F0702BC, -1, 0x7F0702D7, 0x7F070181, -1, 0x7F070342, 0x7F070343, 25};  // string:help_faq_title "Frequently asked questions"
        ConfigListAdapter.helpVideos = new String[]{"kwws=22jdphjxdugldq1qhw2iruxp2jdoohu|2lpdjh24950vhdufk0xqnqrz0zlwk0nqrzq0fkdqjhv0jdphjxdugldq2", "kwws=22jdphjxdugldq1qhw2iruxp2jdoohu|2lpdjh25570vshhg0kdfn0dqg0wlph0mxps0jdphjxdugldq2", "kwws=22jdphjxdugldq1qhw2iruxp2jdoohu|2lpdjh248;0krz0xvh0h|h0lfrq0jdphjxdugldq2", "kwws=22jdphjxdugldq1qhw2iruxp2jdoohu|2lpdjh248<0krz0vhdufk0hqfu|swhg0ydoxh0jdphjxdugldq2\r\rkwws=22jdphjxdugldq1qhw2iruxp2jdoohu|2lpdjh24970frpsduh0phwkrgv0jdphjxdugldq2", "kwws=22jdphjxdugldq1qhw2iruxp2jdoohu|2lpdjh24<40klvwru|0xvdjh0jdphjxdugldq2", "kwws=22jdphjxdugldq1qhw2iruxp2jdoohu|2lpdjh25550h{dpsoh0ri0udqjh0vhdufk0jdphjxdugldq2\r\rkwws=22jdphjxdugldq1qhw2iruxp2jdoohu|2lpdjh25590krz0wr0vhdufk0urxqghg0ydoxh0jdphjxdugldq2", "kwws=22jdphjxdugldq1qhw2iruxp2jdoohu|2lpdjh255<0krz0wr0xvh0rughuhg0jurxs0vhdufk0jdphjxdugldq2", "kwws=22jdphjxdugldq1qhw2iruxp2jdoohu|2lpdjh248<0krz0vhdufk0hqfu|swhg0ydoxh0jdphjxdugldq2", "kwws=22jdphjxdugldq1qhw2iruxp2jdoohu|2lpdjh25960h{dpsoh0ri0wkh0pdvn0vhdufk0jdphjxdugldq2", "kwws=22jdphjxdugldq1qhw2iruxp2jdoohu|2lpdjh255:0krz0wr0xvh0iloo0jdphjxdugldq2", "kwws=22jdphjxdugldq1qhw2iruxp2jdoohu|2lpdjh24<50krz0wr0vshhg0xs0vhdufk0jdphjxdugldq2", "kwws=22jdphjxdugldq1qhw2iruxp2jdoohu|2lpdjh25;60{ru0vhdufk0jxlgh0jdphjxdugldq2", "kwws=22jdphjxdugldq1qhw2iruxp2jdoohu|2lpdjh28:80;330wh{w0vwulqj0kh{0dre0vhdufk0jdphjxdugldq2", "kwws=22jdphjxdugldq1qhw2iruxp2jdoohu|2lpdjh26690rswlrq0dgg0wr0ydoxh0gr0qrw0uhsodfh0jdphjxdugldq2", "kwws=22jdphjxdugldq1qhw2iruxp2jdoohu|2lpdjh26860lpsorvlrq0qhyhu0orvh0krsh0kdfn0ohyho0klgh0iurp0wkh0jdph0jdphjxdugldq2\r\rkwws=22jdphjxdugldq1qhw2iruxp2jdoohu|2lpdjh26870lpsorvlrq0qhyhu0orvh0krsh0kdfn0fuhglwv0klgh0iurp0wkh0jdph0jdphjxdugldq2\r\rkwws=22jdphjxdugldq1qhw2iruxp2jdoohu|2lpdjh26930lpsorvlrq0qhyhu0orvh0krsh0kdfn0fuhglwv0uhvwduw0zlwkrxw0surwhfwlrq0jdphjxdugldq2\r\rkwws=22jdphjxdugldq1qhw2iruxp2jdoohu|2lpdjh26950lpsorvlrq0qhyhu0orvh0krsh0kdfn0fuhglwv0glvdeoh0surwhfwlrq0jdphjxdugldq2\r\rkwws=22jdphjxdugldq1qhw2iruxp2jdoohu|2lpdjh28680:530xvh0urrw0iurp0yluwxdo0vsdfh0iru0klgh0jdphjxdugldq0yluwxdo{srvhg0jdphjxdugldq2\r\rkwws=22jdphjxdugldq1qhw2iruxp2jdoohu|2lpdjh28;30iuhh0iluh0dyrlg0ghwhfwlrq0urrw0iurp0yluwxdo0vsdfh0yluwxdo{srvhg0jdphjxdugldq2\r\rkwws=22jdphjxdugldq1qhw2iruxp2jdoohu|2lpdjh28;40dyrlg0ghwhfwlrq0urrw0iurp0yluwxdo0vsdfh0yluwxdo{srvhg0jdphjxdugldq2\r\rkwws=22jdphjxdugldq1qhw2iruxp2wdjv2dss(53klgh2\r\rkwws=22jdphjxdugldq1qhw2iruxp2jdoohu|2lpdjh28;80dyrlg0ghwhfwlrq0urrw0iurp0yluwxdo0vsdfh0pxowlsoh0dffrxqwv0jdphjxdugldq2", "kwws=22jdphjxdugldq1qhw2iruxp2jdoohu|2lpdjh26;80uhvwduw0zlwkrxw0surwhfwlrq0uhsdlu0wkh0uherrw0uxq0jdphjxdugldq0jdphjxdugldq2\r\rkwws=22jdphjxdugldq1qhw2iruxp2jdoohu|2lpdjh26;70uhvwduw0zlwkrxw0surwhfwlrq0uhsdlu0wkh0uherrw0uherrw0ghylfh0jdphjxdugldq2", "kwws=22jdphjxdugldq1qhw2iruxp2jdoohu|2lpdjh267:0krz0wr0vhdufk0ilowhu0lq0wkh0phpru|0hglwru0jdphjxdugldq2", "kwws=22jdphjxdugldq1qhw2iruxp2jdoohu|2lpdjh26770krz0wr0txlfno|0vhdufk0iru0srlqwhuv0jdphjxdugldq2", "kwws=22jdphjxdugldq1qhw2iruxp2jdoohu|2lpdjh26990xqudqgrpl}hu0jdphjxdugldq2\r\rkwws=22jdphjxdugldq1qhw2iruxp2jdoohu|2lpdjh26;40uxvvldq0vorwv0kdfn0vwruh0xqudqgrpl}hu0jdphjxdugldq2", "kwws=22jdphjxdugldq1qhw2qrurrw", "kwws=22jdphjxdugldq1qhw2iruxp2jdoohu|2lpdjh27480ilowhuv0jdphjxdugldq2", "kwws=22jdphjxdugldq1qhw2iruxp2ilohv2fdwhjru|290oxd0vfulswv2\r\rkwws=22jdphjxdugldq1qhw2khos2", "kwws=22jdphjxdugldq1qhw2iruxp2ilohv2iloh2:970xqdoljqhg0vhdufk2", "kwws=22jdphjxdugldq1qhw2iruxp2jdoohu|2lpdjh286:0:630irupxod0fdofxodwru0jdphjxdugldq2\r\rkwws=22jdphjxdugldq1qhw2iruxp2jdoohu|2lpdjh286;0jxq0zdu0vkrrwlqj0jdphv0kdfn0sulfh0irupxod0fdofxodwru0jdphjxdugldq2", "kwws=22jdphjxdugldq1qhw2iruxp2jdoohu|2lpdjh28680:530xvh0urrw0iurp0yluwxdo0vsdfh0iru0klgh0jdphjxdugldq0yluwxdo{srvhg0jdphjxdugldq2\r\rkwws=22jdphjxdugldq1qhw2iruxp2jdoohu|2lpdjh28;30iuhh0iluh0dyrlg0ghwhfwlrq0urrw0iurp0yluwxdo0vsdfh0yluwxdo{srvhg0jdphjxdugldq2\r\rkwws=22jdphjxdugldq1qhw2iruxp2jdoohu|2lpdjh28;40dyrlg0ghwhfwlrq0urrw0iurp0yluwxdo0vsdfh0yluwxdo{srvhg0jdphjxdugldq2\r\rkwws=22jdphjxdugldq1qhw2iruxp2wdjv2dss(53klgh2\r\rkwws=22jdphjxdugldq1qhw2iruxp2jdoohu|2lpdjh28;80dyrlg0ghwhfwlrq0urrw0iurp0yluwxdo0vsdfh0pxowlsoh0dffrxqwv0jdphjxdugldq2", "kwws=22jdphjxdugldq1qhw2iruxp2jdoohu|2lpdjh29470il{lqj0d0vfulsw0xvlqj0dvvhpeohu0jdphjxdugldq2"};
    }

    public ConfigListAdapter() {
        this.mGameSection = new WeakReference(null);
        this.mConfigIconTextView = new WeakReference(null);
        this.mConfigIcon = new WeakReference(null);
        this.updated = false;
        this.scrollPos = null;
        this.mViewList = ContainerHelpers.EMPTY_VIEWS;
        HotPoint hotPoint = MainService.instance.hotPoint;
        this.updateConfigIcon(hotPoint.getLayoutAlpha(), hotPoint.getVanishingTime(), null, null, hotPoint.getSize());
        this.updateData();
    }

    public static void changePath(String path) {
        Log.d("mConfigTempPath onClick");
        View view0 = LayoutInflater.inflateStatic(0x7F040028, null);  // layout:temp_path_config
        EditTextPath pathEdit = (EditTextPath)view0.findViewById(0x7F0B0143);  // id:temp_file_path
        SharedPreferences sharedPreferences0 = Tools.getSharedPreferences();
        if(path == null) {
            path = sharedPreferences0.getString("temp-path", "");
        }
        pathEdit.setText(path);
        pathEdit.setDataType(4);
        pathEdit.setPathType(0);
        ((TextView)view0.findViewById(0x7F0B0142)).setText(Re.s(0x7F07009F) + '\n' + Re.s(0x7F070163));  // id:temp_file_desc
        view0.findViewById(0x7F0B000B).setTag(pathEdit);  // id:path_selector
        AlertDialog alertDialog0 = Alert.create().setView(InternalKeyboard.getView(view0, false)).setPositiveButton(Re.s(0x7F0700A0), null).setNegativeButton(Re.s(0x7F0700A1), null).create();  // string:confirm "Confirm"
        android.ext.ConfigListAdapter.20 listener = new View.OnClickListener() {
            @Override  // android.view.View$OnClickListener
            public void onClick(View v) {
                String s = Tools.trimDirPath(pathEdit.getText().toString().trim());
                if(s.length() > 0) {
                    if(!Tools.isPath(s)) {
                        return;
                    }
                    History.add(s, 4);
                }
                Log.d(("save path: " + s));
                new SPEditor().putString("temp-path", s, "").commit();
                MainService service = MainService.instance;
                service.mDaemonManager.setPath(service.getTempPatches());
                service.clear(true);
                Tools.dismiss(alertDialog0);
            }
        };
        TypicalValues.setCacheDirs(((Button)view0.findViewById(0x7F0B000C)), pathEdit);  // id:typical_values
        ConfigListAdapter.showChange(alertDialog0, listener, pathEdit);
    }

    public static void changeSu() {
        if(!Main.isRootMode()) {
            return;
        }
        View view0 = LayoutInflater.inflateStatic(0x7F040028, null);  // layout:temp_path_config
        EditTextPath pathEdit = (EditTextPath)view0.findViewById(0x7F0B0143);  // id:temp_file_path
        SharedPreferences sharedPreferences0 = Tools.getSharedPreferences();
        pathEdit.setText(sharedPreferences0.getString("su", ""));
        pathEdit.setDataType(4);
        pathEdit.setPathType(1);
        pathEdit.setHint("su");
        ((TextView)view0.findViewById(0x7F0B0142)).setText(Re.s(0x7F07025F));  // id:temp_file_desc
        CheckBox useSh = (CheckBox)view0.findViewById(0x7F0B0144);  // id:use_sh
        useSh.setChecked(sharedPreferences0.getBoolean("sh", false));
        useSh.setVisibility(0);
        view0.findViewById(0x7F0B000B).setTag(pathEdit);  // id:path_selector
        AlertDialog alertDialog0 = Alert.create().setView(InternalKeyboard.getView(view0, false)).setPositiveButton(Re.s(0x7F0700A0), null).setNegativeButton(Re.s(0x7F0700A1), null).create();  // string:confirm "Confirm"
        android.ext.ConfigListAdapter.23 listener = new View.OnClickListener() {
            void alert(String msg) {
                ThreadManager.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        this.val$dialog.show();
                    }
                });
                Tools.alertBigText((msg + "\n\n" + ""), 0);
            }

            void hide() {
                ThreadManager.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(MainService.instance != null) {
                            MainService.instance.dismissDialog();
                        }
                        this.val$dialog.hide();
                    }
                });
            }

            @Override  // android.view.View$OnClickListener
            public void onClick(View v) {
                String s = pathEdit.getText().toString().trim();
                if(s.length() > 0) {
                    new ThreadEx(new Runnable() {
                        @Override
                        public void run() {
                            android.ext.ConfigListAdapter.23.this.hide();
                            try {
                                String s = RootDetector.runCmd("exec id", 10, s);
                                if(s.contains("uid=0")) {
                                    ThreadManager.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            android.ext.ConfigListAdapter.23 configListAdapter$230 = android.ext.ConfigListAdapter.23.this;
                                            boolean z = this.val$useSh.isChecked();
                                            configListAdapter$230.save(this.val$su, z);
                                        }
                                    });
                                }
                                else {
                                    android.ext.ConfigListAdapter.23.this.alert(s);
                                }
                            }
                            catch(Throwable e) {
                                android.ext.ConfigListAdapter.23.this.alert(e.getMessage());
                            }
                            RootDetector.debug = "";
                        }
                    }, "CheckSu: " + s).start();
                    return;
                }
                this.save(s, useSh.isChecked());
            }

            void save(String su, boolean sh) {
                Log.d(("su path: " + su + "; sh: " + sh));
                new SPEditor().putString("su", su, "").putBoolean("sh", sh, false).commit();
                if(su.length() != 0) {
                    History.add(su, 4);
                }
                Tools.dismiss(alertDialog0);
                ConfigListAdapter.needRestart();
            }
        };
        Button btn = (Button)view0.findViewById(0x7F0B000C);  // id:typical_values
        String[] arr_s = {"su", "ku.sud", "/system/bin/su", "/system/xbin/su", "/su/bin/su", "/system/xbin/bstk/su", "/sbin/su", "/magisk/.core/bin/su", "/magisk/phh/bin/su"};
        btn.setOnClickListener(new TypicalValues(arr_s, arr_s));
        btn.setTag(pathEdit);
        ConfigListAdapter.showChange(alertDialog0, listener, pathEdit);
    }

    static void changeToolbarButtons() {
        String[] stringValues = new String[MainService.menuItems.length * 2];
        stringValues[0] = Re.s(0x7F07008B) + ", " + Re.s(0x7F07023A);  // string:search "Search"
        stringValues[1] = Re.s(0x7F07008B) + ", " + Re.s(0x7F070239);  // string:search "Search"
        stringValues[2] = Re.s(0x7F07019D) + ", " + Re.s(0x7F07023A);  // string:saved_list "Saved list"
        stringValues[3] = Re.s(0x7F07019D) + ", " + Re.s(0x7F070239);  // string:saved_list "Saved list"
        stringValues[4] = Re.s(0x7F07019E) + ", " + Re.s(0x7F07023A);  // string:memory_editor "Memory editor"
        stringValues[5] = Re.s(0x7F07019E) + ", " + Re.s(0x7F070239);  // string:memory_editor "Memory editor"
        Alert.show(Alert.create().setItems(stringValues, new DialogInterface.OnClickListener() {
            @Override  // android.content.DialogInterface$OnClickListener
            public void onClick(DialogInterface d, int which) {
                MenuItem[] items = MainService.menuItems[which / 2];
                CharSequence[] list = new CharSequence[items.length];
                Drawable[] icons = new Drawable[items.length];
                boolean[] checked = new boolean[list.length];
                long set = Config.toolbarButtons[which];
                for(int i = 0; i < list.length; ++i) {
                    CharSequence charSequence0 = Build.VERSION.SDK_INT < 11 ? Tools.colorize(items[i].toString(), -1) : items[i].toString();
                    list[i] = charSequence0;
                    checked[i] = (1L << i & set) != 0L;
                    icons[i] = items[i].getDrawable();
                }
                android.ext.ConfigListAdapter.16.1 configListAdapter$16$10 = new DialogInterface.OnClickListener() {
                    @Override  // android.content.DialogInterface$OnClickListener
                    public void onClick(DialogInterface dialog, int which) {
                        long set = -1L;
                        if(which == -3 || which == -1) {
                            if(which == -1) {
                                for(int i = 0; i < checked.length; ++i) {
                                    set = checked[i] ? set | ((long)(1 << i)) : set & ((long)(~(1 << i)));
                                }
                            }
                            if((((long)((1 << checked.length) - 1)) & set) == 0L) {
                                set |= 1L;
                            }
                            Config.toolbarButtons[which] = set;
                            Config.save();
                        }
                        ConfigListAdapter.changeToolbarButtons();
                    }
                };
                AlertDialog alertDialog0 = Alert.create(Tools.getDarkContext()).setCustomTitle(Tools.getCustomTitle(stringValues[which])).setMultiChoiceItems(list, checked, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override  // android.content.DialogInterface$OnMultiChoiceClickListener
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        checked[which] = isChecked;
                    }
                }).setPositiveButton(Re.s(0x7F07008C), configListAdapter$16$10).setNeutralButton(Re.s(0x7F07023F), configListAdapter$16$10).setNegativeButton(Re.s(0x7F0700A1), configListAdapter$16$10).create();  // string:save "Save"
                ListView listView0 = alertDialog0.getListView();
                if(listView0 != null) {
                    Tools.addIconsToListViewItems(listView0, list, icons, Config.getIconSize(), 0x7F090002);  // style:SmallText
                }
                Alert.show(alertDialog0);
            }
        }).setNegativeButton(Re.s(0x7F0700A1), null));  // string:cancel "Cancel"
    }

    static void configLanguage() {
        Tools.showToast(0x7F0700C8, 0);  // string:loading "Loading..."
        ThreadManager.runOnMainThread(() -> {
            MainService service = MainService.instance;
            if(service == null) {
                return;
            }
            ArrayList locales = new ArrayList();
            ArrayList localesList = new ArrayList();
            HashMap index = new HashMap();
            DisplayMetrics metrics = new DisplayMetrics();
            service.mWindowManager.getDefaultDisplay().getMetrics(metrics);
            Resources resources0 = MainService.context.getResources();
            Configuration configuration0 = resources0.getConfiguration();
            AssetManager assetManager0 = resources0.getAssets();
            String[] arr_s = AppLocale.getLocales();
            Arrays.sort(arr_s, new Comparator() {
                @Override
                public int compare(Object object0, Object object1) {
                    return this.compare(((String)object0), ((String)object1));
                }

                public int compare(String lhs, String rhs) {
                    return (lhs + "_zzz").replace('-', '_').compareTo((rhs + "_zzz").replace('-', '_'));
                }
            });
            String s = Re.s(0x7F070083);  // string:lang_code "~~~en_US~~~"
            int currentLocale = -1;
            long last = System.currentTimeMillis();
            int i = 0;
            for(int v3 = 0; v3 < arr_s.length; ++v3) {
                String localeName = arr_s[v3];
                ++i;
                if(localeName != null) {
                    try {
                        long v4 = System.currentTimeMillis();
                        if(v4 - last > 2000L) {
                            last = v4;
                            Tools.showToast((i * 100 / arr_s.length + "%"), 0);
                        }
                        if(localeName.length() == 0) {
                            localeName = "en_US";
                        }
                        Locale locale0 = AppLocale.getLocaleObject(localeName);
                        String s2 = ConfigListAdapter.getLangCode(configuration0, locale0, assetManager0, metrics);
                        if(index.get(s2) == null) {
                            if(s2.startsWith(s) && currentLocale == -1) {
                                currentLocale = locales.size();
                            }
                            String num = String.format(locale0, "\n[%,.1f | %.2e]", -1234.0, -0.0);
                            index.put(s2, localeName);
                            locales.add(localeName);
                            try {
                                SpannableStringBuilder builder = new SpannableStringBuilder();
                                builder.append(locale0.getDisplayName(locale0));
                                builder.setSpan(new ForegroundColorSpan(ProcessInfo.colorName), 0, builder.length(), 33);
                                int v5 = builder.length();
                                builder.append(" (");
                                builder.append(localeName);
                                builder.append(')');
                                builder.setSpan(new ForegroundColorSpan(ProcessInfo.colorPid), v5, builder.length(), 33);
                                int len = builder.length();
                                builder.append(num);
                                builder.setSpan(new RelativeSizeSpan(0.6f), len, builder.length(), 33);
                                builder.setSpan(new ForegroundColorSpan(ProcessInfo.colorSize), len, builder.length(), 33);
                                localesList.add(builder);
                            }
                            catch(Throwable e) {
                                Log.badImplementation(e);
                                localesList.add(locale0.getDisplayName(locale0) + " (" + localeName + ')' + num);
                            }
                        }
                    }
                    catch(Throwable e) {
                        Log.e(("Failed check locale: " + localeName), e);
                    }
                }
            }
            ConfigListAdapter.getLangCode(configuration0, AppLocale.getLocaleObject(AppLocale.getLocale()), assetManager0, metrics);
            AppLocale.loadLocale();
            ThreadManager.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AlertDialog.Builder alertDialog$Builder0 = Alert.create(Tools.getDarkContext()).setCustomTitle(Tools.getCustomTitle(0x7F0700DD));  // string:select_language "Select language"
                    android.ext.ConfigListAdapter.26.1 configListAdapter$26$10 = new DialogInterface.OnClickListener() {
                        @Override  // android.content.DialogInterface$OnClickListener
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                AppLocale.saveLocale(((String)this.val$locales.get(which)));
                                ConfigListAdapter.needRestart();
                            }
                            catch(IndexOutOfBoundsException e) {
                                Log.e(("Failed get locale for index: " + which), e);
                            }
                            Tools.dismiss(dialog);
                        }
                    };
                    Alert.show(alertDialog$Builder0.setSingleChoiceItems(new ArrayAdapter(MainService.context, localesList), currentLocale, configListAdapter$26$10));
                }
            });
        });

        class android.ext.ConfigListAdapter.24 implements Runnable {
            @Override
            public void run() {
                ConfigListAdapter.configLanguage_();
            }
        }

    }

    // 检测为 Lambda 实现
    static void configLanguage_() [...]

    @Override  // android.widget.Adapter
    public int getCount() {
        return this.mViewList.length;
    }

    @Override  // android.widget.Adapter
    public Object getItem(int position) {
        return null;
    }

    @Override  // android.widget.Adapter
    public long getItemId(int position) {
        return (long)position;
    }

    private static String getLangCode(Configuration c, Locale locale, AssetManager assets, DisplayMetrics metrics) {
        Configuration config = new Configuration(c);
        ConfigListAdapter.setLocale(config, locale);
        Resources res = null;
        if(Build.VERSION.SDK_INT >= 17) {
            try {
                res = MainService.context.createConfigurationContext(config).getResources();
            }
            catch(Throwable e) {
                Log.badImplementation(e);
            }
        }
        if(res == null) {
            new Resources(assets, metrics, config);
        }
        return "~~~en_US~~~" + String.format(locale, "%,d;%,d;%e;%e;%e;%e;", 0x499602D2, 0xB669FD2E, 1.234560E+300, -1.234560E+300, 1.234560E-300, -0.0);
    }

    public static DialogInterface.OnClickListener getShowHelpListener() {
        return (/* 缺少LAMBDA参数 */, /* 缺少LAMBDA参数 */) -> ConfigListAdapter.showHelp(0);

        class android.ext.ConfigListAdapter.18 implements DialogInterface.OnClickListener {
            @Override  // android.content.DialogInterface$OnClickListener
            public void onClick(DialogInterface d, int which) {
                ConfigListAdapter.showHelp();
            }
        }

    }

    public static String getUsage() {
        SharedPreferences sharedPreferences0 = Tools.getSharedPreferences();
        SharedPreferences.Editor sharedPreferences$Editor0 = sharedPreferences0.edit();
        StringBuilder out = new StringBuilder();
        long v = sharedPreferences0.getLong("mc", 0L);
        sharedPreferences$Editor0.remove("mc");
        if(v != 0L) {
            out.append("&mc=");
            out.append('=');
            out.append(Long.toHexString(v));
        }
        sharedPreferences$Editor0.commit();
        return out.toString();
    }

    @Override  // android.widget.Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        return position < this.mViewList.length ? this.mViewList[position] : new View(Tools.getContext());
    }

    // 检测为 Lambda 实现
    static void hideIcon(int w) [...]

    public static boolean isClient64() {
        boolean ret = false;
        try {
            if(Build.VERSION.SDK_INT < 21) {
                return ret;
            }
            if(Build.SUPPORTED_64_BIT_ABIS != null && Build.SUPPORTED_64_BIT_ABIS.length > 0) {
                ret = true;
            }
            FileInputStream fileInputStream0 = new FileInputStream("/proc/self/auxv");
            byte[] buf = new byte[0x1000];
            int v = fileInputStream0.read(buf);
            fileInputStream0.close();
            if(v <= 0) {
                return ret;
            }
            if(v % 16 == 8) {
                return false;
            }
            for(int i = 0; true; i += 16) {
                if(i >= v) {
                    return true;
                }
                if(buf[i + 4] != 0 || buf[i + 5] != 0 || buf[i + 6] != 0 || buf[i + 7] != 0) {
                    break;
                }
            }
            return false;
        }
        catch(Throwable e) {
            Log.w(("Failed check 64 client. Used: " + ret), e);
            return ret;
        }
    }

    public void load(boolean selected) {
        if(selected) {
            if(this.mViewList.length <= 0) {
                ViewGroup parent = (ViewGroup)LayoutInflater.inflateStatic(0x7F04001A, null);  // layout:service_config
                this.mGameSection = new WeakReference(((TextView)parent.findViewById(0x7F0B0080)));  // id:game_section
                this.mConfigIconTextView = new WeakReference(((TextView)parent.findViewById(0x7F0B00AB)));  // id:config_icon_text
                this.mConfigIcon = new WeakReference(((ImageView)parent.findViewById(0x7F0B00AC)));  // id:config_icon_example
                ArrayList mViewList = new ArrayList();
                int i = 0;
                while(i < parent.getChildCount()) {
                    try {
                        View view0 = parent.getChildAt(i);
                        if(view0.getVisibility() != 8) {
                            if(view0 instanceof TextView) {
                                ((TextView)view0).setText(Re.s(((TextView)view0).getText().toString().trim()));
                                this.setIcon(((TextView)view0));
                            }
                            view0.setLayoutParams(new AbsListView.LayoutParams(-1, -2, 0));
                            mViewList.add(view0);
                        }
                        ++i;
                        continue;
                    }
                    catch(ArrayIndexOutOfBoundsException unused_ex) {
                    }
                    ++i;
                }
                for(Object object0: mViewList) {
                    Tools.getViewForAttach(((View)object0));
                }
                this.mViewList = (View[])mViewList.toArray(new View[mViewList.size()]);
                this.notifyDataSetChanged();
                MainService service = MainService.instance;
                service.mConfigListView.setAdapter(this);
                HotPoint hotPoint = service.hotPoint;
                this.setGameName();
                this.updateConfigIcon(hotPoint.getLayoutAlpha(), hotPoint.getVanishingTime(), null, null, hotPoint.getSize());
                ScrollPos scrollPos = this.scrollPos;
                if(scrollPos != null) {
                    Tools.setScrollPos(MainService.instance.mConfigListView, scrollPos);
                    MainService.instance.mConfigListView.post(new Runnable() {
                        @Override
                        public void run() {
                            Tools.setScrollPos(MainService.instance.mConfigListView, scrollPos);
                        }
                    });
                }
            }
        }
        else if(this.mViewList.length != 0) {
            ListView list = MainService.instance.mConfigListView;
            this.scrollPos = Tools.getScrollPos(list);
            this.mViewList = ContainerHelpers.EMPTY_VIEWS;
            this.notifyDataSetChanged();
            list.setAdapter(null);
        }
    }

    static void needRestart() {
        Alert.show(Alert.create().setCustomTitle(Tools.getCustomTitle(0x7F070001)).setMessage(Re.s(0x7F0700DE)).setPositiveButton(Re.s(0x7F0700F2), new ExitListener(200, true)).setNegativeButton(Re.s(0x7F0700B9), null));  // string:app_name "GameGuardian"
    }

    @Override  // android.widget.BaseAdapter
    public void notifyDataSetChanged() {
        if(!this.updated) {
            this.updated = true;
            this.updateData();
            this.updated = false;
        }
        super.notifyDataSetChanged();
    }

    @Override  // android.widget.AdapterView$OnItemClickListener
    public void onItemClick(AdapterView adapterView0, View view, int position, long id) {
        View v;
        if(view != null) {
            int v2 = view.getId();
            if(v2 != -1) {
                long bit = 1L << position % 0x3F;
                if((ConfigListAdapter.USAGE & bit) == 0L) {
                    ConfigListAdapter.USAGE |= bit;
                    ConfigListAdapter.saveUsage();
                }
                MainService service = MainService.instance;
                Option config$Option0 = Config.get(v2);
                if(config$Option0 != null) {
                    switch(v2) {
                        case 0x7F0B009F: {  // id:config_keyboard
                            config$Option0.setOnChangeListner(new OnChangeListener() {
                                @Override  // android.ext.Config$Option$OnChangeListener
                                public int onChange(DialogInterface dialog, int which) {
                                    if(which == 1 && !InternalKeyboard.allowUsage()) {
                                        which = 0;
                                        MainService.reportBadFirmware();
                                    }
                                    return which;
                                }
                            });
                            break;
                        }
                        case 0x7F0B00A9: {  // id:config_icons_size
                            config$Option0.setOnChangeListner(new OnChangeListener() {
                                @Override  // android.ext.Config$Option$OnChangeListener
                                public int onChange(DialogInterface dialog, int which) {
                                    if(which != Config.iconsSize) {
                                        ConfigListAdapter.needRestart();
                                    }
                                    return which;
                                }
                            });
                        }
                    }
                    config$Option0.change();
                    return;
                }
                switch(v2) {
                    case 0x7F0B0013: {  // id:help
                        ConfigListAdapter.showHelp();
                        return;
                    }
                    case 0x7F0B007D: {  // id:config_reset
                        service.detectApp(true);
                        return;
                    }
                    case 0x7F0B007E: {  // id:config_exit
                        Alert.show(Alert.create().setCustomTitle(Tools.getCustomTitle(0x7F0700B8)).setPositiveButton(Re.s(0x7F07009B), new ExitListener(300)).setNegativeButton(Re.s(0x7F07009C), null));  // string:exit "Exit"
                        return;
                    }
                    case 0x7F0B007F: {  // id:config_kill_game
                        Alert.show(Alert.create().setCustomTitle(Tools.getCustomTitle(0x7F0701AF)).setMessage(Re.s(0x7F070031)).setPositiveButton(Re.s(0x7F07009B), new DialogInterface.OnClickListener() {  // string:kill_game "Kill the game"
                            @Override  // android.content.DialogInterface$OnClickListener
                            public void onClick(DialogInterface dialog, int which) {
                                service.processKill();
                            }
                        }).setNegativeButton(Re.s(0x7F07009C), null));  // string:no "No"
                        return;
                    }
                    case 0x7F0B0083: {  // id:config_speedhack_functions
                        MainService.instance.timersEditor.show();
                        return;
                    }
                    case 0x7F0B0086: {  // id:config_unrandomizer
                        MainService.instance.randEditor.show();
                        return;
                    }
                    case 0x7F0B008F: {  // id:config_speeds
                        try {
                            v = LayoutInflater.inflateStatic(0x7F040007, null);  // layout:list_speeds_config
                        }
                        catch(IndexOutOfBoundsException e) {
                            v = null;
                            Log.badImplementation(e);
                            MainService.reportBadFirmware();
                        }
                        if(v != null) {
                            TextView hint = (TextView)v.findViewById(0x7F0B002C);  // id:hint
                            TextView text = (TextView)v.findViewById(0x7F0B000E);  // id:message
                            text.setText(Re.s(text.getText().toString().trim()));
                            EditTextSpeeds edit = (EditTextSpeeds)v.findViewById(0x7F0B002B);  // id:speeds
                            TextView default_ = (TextView)v.findViewById(0x7F0B002D);  // id:default_
                            default_.setText(Tools.stripColon(0x7F07023E) + ": " + "0.000000001;0.00000001;0.0000001;0.000001;0.00001;0.0001;0.001;0.002;0.005;0.01;0.02;0.05;0.1;0.2;0.5;0.6;0.75;0.9;1;1.2;1.3;1.5;2;3;4;5;6;9;12;15;20;30;60;120;180;300;600;1200;2400;3600");  // string:default_ "Default:"
                            default_.setOnClickListener(new View.OnClickListener() {
                                // 去混淆评级： 低(20)
                                @Override  // android.view.View$OnClickListener
                                public void onClick(View v) {
                                    edit.setText("0.000000001;0.00000001;0.0000001;0.000001;0.00001;0.0001;0.001;0.002;0.005;0.01;0.02;0.05;0.1;0.2;0.5;0.6;0.75;0.9;1;1.2;1.3;1.5;2;3;4;5;6;9;12;15;20;30;60;120;180;300;600;1200;2400;3600");
                                }
                            });
                            edit.setText(MainService.instance.listSpeed.get());
                            edit.setDataType(16);
                            edit.setOnChangedListener(new OnChangedListener() {
                                @Override  // android.ext.EditTextSpeeds$OnChangedListener
                                public void onSelectionChanged(EditText obj, int selStart, int selEnd) {
                                    this.updateHint();
                                }

                                @Override  // android.ext.EditTextSpeeds$OnChangedListener
                                public void onTextChanged(EditText obj, CharSequence text, int start, int lengthBefore, int lengthAfter) {
                                    this.updateHint();
                                }

                                public void updateHint() {
                                    String text;
                                    if(edit != null && hint != null) {
                                        try {
                                            String s1 = edit.getText().toString().trim();
                                            int pos = edit.getSelectionStart();
                                            if(pos < 0) {
                                                pos = 0;
                                            }
                                            if(pos >= s1.length()) {
                                                pos = s1.length() - 1;
                                            }
                                            int start = pos > 0 ? s1.lastIndexOf(59, pos - 1) : -1;
                                            if(start < 0) {
                                                start = -1;
                                            }
                                            double f = Tools.parseTime(s1.substring(start + 1, (s1.indexOf(59, pos) >= 0 ? s1.indexOf(59, pos) : s1.length())));
                                            if(f > 0.0) {
                                                MN listSpeed$MN0 = ListSpeed.getMN(f);
                                                text = Tools.stringFormat(Re.s(0x7F070177), new Object[]{Tools.longToTime(listSpeed$MN0.N), Tools.longToTime(listSpeed$MN0.M)});  // string:speed_info "__s__ in real time = __s__ in game time"
                                            }
                                            else {
                                                text = "??";
                                            }
                                        }
                                        catch(NumberFormatException unused_ex) {
                                            text = "???";
                                        }
                                    }
                                    else {
                                        text = "?";
                                    }
                                    hint.setText(text);
                                }
                            });
                            Alert.show(Alert.create().setView(InternalKeyboard.getView(v)).setPositiveButton(Re.s(0x7F0700A0), new DialogInterface.OnClickListener() {  // string:confirm "Confirm"
                                @Override  // android.content.DialogInterface$OnClickListener
                                public void onClick(DialogInterface dialog, int which) {
                                    String s = edit.getText().toString().trim();
                                    History.add(s, 16);
                                    MainService.instance.listSpeed.set(s);
                                }
                            }).setNegativeButton(Re.s(0x7F0700A1), null), edit);  // string:cancel "Cancel"
                            return;
                        }
                        break;
                    }
                    case 0x7F0B0098: {  // id:config_path
                        ConfigListAdapter.changePath(null);
                        return;
                    }
                    case 0x7F0B0099: {  // id:config_su
                        ConfigListAdapter.changeSu();
                        return;
                    }
                    case 0x7F0B00A7: {  // id:config_toolbar_buttons
                        ConfigListAdapter.changeToolbarButtons();
                        return;
                    }
                    case 0x7F0B00AA: {  // id:config_icon
                        View view1 = LayoutInflater.inflateStatic(0x7F04001C, null);  // layout:service_config_icon
                        SeekBar transparencyBar = (SeekBar)view1.findViewById(0x7F0B00C3);  // id:transparency
                        SeekBar hideBar = (SeekBar)view1.findViewById(0x7F0B00C4);  // id:hide
                        SeekBar sizeBar = (SeekBar)view1.findViewById(0x7F0B0053);  // id:size
                        TextView text = (TextView)view1.findViewById(0x7F0B00AB);  // id:config_icon_text
                        ImageView icon = (ImageView)view1.findViewById(0x7F0B00AC);  // id:config_icon_example
                        float f = service.hotPoint.getLayoutAlpha();
                        int v4 = service.hotPoint.getVanishingTime();
                        int v5 = service.hotPoint.getSize();
                        transparencyBar.setProgress(10 - ((int)(10.0f * f)));
                        hideBar.setProgress((v4 >= 0 ? v4 : hideBar.getMax()));
                        sizeBar.setMax(12);
                        sizeBar.setProgress(v5);
                        this.updateConfigIcon(f, v4, text, icon, v5);
                        android.ext.ConfigListAdapter.4 configListAdapter$40 = new SeekBar.OnSeekBarChangeListener() {
                            @Override  // android.widget.SeekBar$OnSeekBarChangeListener
                            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                int v1 = transparencyBar.getProgress();
                                int hide = hideBar.getMax() == hideBar.getProgress() ? -1 : hideBar.getProgress();
                                int v3 = sizeBar.getProgress();
                                ConfigListAdapter.this.updateConfigIcon(((float)(10 - v1)) / 10.0f, hide, text, icon, v3);
                            }

                            @Override  // android.widget.SeekBar$OnSeekBarChangeListener
                            public void onStartTrackingTouch(SeekBar seekBar) {
                            }

                            @Override  // android.widget.SeekBar$OnSeekBarChangeListener
                            public void onStopTrackingTouch(SeekBar seekBar) {
                            }
                        };
                        transparencyBar.setOnSeekBarChangeListener(configListAdapter$40);
                        hideBar.setOnSeekBarChangeListener(configListAdapter$40);
                        sizeBar.setOnSeekBarChangeListener(configListAdapter$40);
                        Alert.show(Alert.create().setPositiveButton(Re.s(0x7F07009D), null).setView(view1));  // string:ok "OK"
                        return;
                    }
                    case 0x7F0B00AD: {  // id:hide_icons
                        CharSequence[] arr_charSequence = {Re.s(0x7F07017C) + " (" + Re.s(0x7F07023D) + ')', Re.s(0x7F070003), Re.s(0x7F070002)};  // string:nothing "Nothing"
                        AlertDialog alertDialog0 = Alert.create().setCustomTitle(Tools.getCustomTitle(0x7F07022B)).setItems(arr_charSequence, (/* 缺少LAMBDA参数 */, int w) -> try {
                            int v1 = 10;
                            int which = w == 3 ? Tools.getSharedPreferences().getInt("hide-icon", 0) : w;
                            if((which != 1 || !(BaseActivity.instance instanceof MainActivity)) && (which != 2 || !(BaseActivity.instance instanceof ActivityMain))) {
                                String s = Tools.getPackageName();
                                PackageManager packageManager0 = Tools.getPackageManager();
                                ComponentName componentName0 = new ComponentName(s, "com.ggdqo.ActivityMain");
                                ComponentName componentName1 = new ComponentName(s, "com.ggdqo.MainActivity");
                                Log.d(("hide A: " + which + "; " + componentName1 + ": " + packageManager0.getComponentEnabledSetting(componentName1) + "; " + componentName0 + ": " + packageManager0.getComponentEnabledSetting(componentName0) + ';'));
                                if(w == 3) {
                                    int v3 = packageManager0.getComponentEnabledSetting(componentName1) == 2 ? 1 : 0;
                                    int v4 = packageManager0.getComponentEnabledSetting(componentName0) == 2 ? 10 : 0;
                                    if(which == 1) {
                                        v1 = 1;
                                    }
                                    else if(which != 2) {
                                        v1 = 0;
                                    }
                                    if(v3 + v4 != v1) {
                                        goto label_20;
                                    }
                                }
                                else {
                                label_20:
                                    if(which != 1) {
                                        Tools.setComponentEnabledSetting(1, componentName1, 1);
                                    }
                                    if(which != 2) {
                                        Tools.setComponentEnabledSetting(3, componentName0, 1);
                                    }
                                    if(which == 1) {
                                        Tools.setComponentEnabledSetting(5, componentName1, 2);
                                    }
                                    if(which == 2) {
                                        Tools.setComponentEnabledSetting(7, componentName0, 2);
                                    }
                                    if(which != 1) {
                                        Tools.setComponentEnabledSetting(9, componentName1, 1);
                                    }
                                    if(which != 2) {
                                        Tools.setComponentEnabledSetting(11, componentName0, 1);
                                    }
                                    Log.d(("hide Z: " + which + "; " + componentName1 + ": " + packageManager0.getComponentEnabledSetting(componentName1) + "; " + componentName0 + ": " + packageManager0.getComponentEnabledSetting(componentName0) + ';'));
                                    if(w != 3) {
                                        new SPEditor().putInt("hide-icon", which, 0).commit();
                                    }
                                }
                            }
                            else if(w != 3) {
                                Alert.show(Alert.create().setCustomTitle(Tools.getCustomTitle(0x7F07022B)).setMessage(Re.s(0x7F070315)).setNegativeButton(Re.s(0x7F07009D), null));  // string:hide_icons "Hide launcher icons"
                            }
                        }
                        catch(Throwable e) {
                            Log.e(("Failed disable icon " + w), e);
                        }).create();
                        ListView listView0 = alertDialog0.getListView();
                        if(listView0 != null) {
                            Drawable[] arr_drawable = new Drawable[3];
                            arr_drawable[1] = Tools.getDrawable(0x7F030007);  // mipmap:ic_sw_48dp
                            arr_drawable[2] = Tools.getDrawable(0x7F030004);  // mipmap:ic_hw_48dp
                            Tools.addIconsToListViewItems(listView0, arr_charSequence, arr_drawable, 0x30, 0);
                        }
                        Alert.show(alertDialog0);
                        return;
                    }
                    case 0x7F0B00B0: {  // id:config_language
                        ConfigListAdapter.configLanguage();
                        return;
                    }
                    case 0x7F0B00B2: {  // id:config_disable_protection
                        if(Main.isRootMode()) {
                            Alert.show(Alert.create().setCustomTitle(Tools.getCustomTitle(0x7F070271)).setMessage(Re.s(0x7F07007A)).setPositiveButton(Re.s(0x7F07009D), new DialogInterface.OnClickListener() {  // string:disable_protection "Disable protection for all applications (until reboot)"
                                @Override  // android.content.DialogInterface$OnClickListener
                                public void onClick(DialogInterface dialog, int which) {
                                    service.mDaemonManager.disableProtection();
                                }
                            }).setNegativeButton(Re.s(0x7F0700A1), null));  // string:cancel "Cancel"
                            return;
                        }
                        break;
                    }
                    case 0x7F0B00B3: {  // id:config_clear_history
                        Alert.show(Alert.create().setCustomTitle(Tools.getCustomTitle(0x7F070284)).setMessage(Re.s(0x7F070285)).setPositiveButton(Re.s(0x7F07009D), new DialogInterface.OnClickListener() {  // string:clear_history "Clear input history"
                            @Override  // android.content.DialogInterface$OnClickListener
                            public void onClick(DialogInterface dialog, int which) {
                                History.clear();
                            }
                        }).setNegativeButton(Re.s(0x7F0700A1), null));  // string:cancel "Cancel"
                        return;
                    }
                    case 0x7F0B00B4: {  // id:config_clear_defaults
                        Alert.show(Alert.create().setMessage(Re.s(0x7F0702DB)).setPositiveButton(Re.s(0x7F0700A0), new DialogInterface.OnClickListener() {  // string:reset_ignore "Reset all ignore for all dialogs"
                            @Override  // android.content.DialogInterface$OnClickListener
                            public void onClick(DialogInterface dialog, int which) {
                                Config.ignore = 0L;
                                Config.save();
                            }
                        }).setNegativeButton(Re.s(0x7F0700A1), null));  // string:cancel "Cancel"
                        return;
                    }
                    case 0x7F0B00B5: {  // id:config_last_stats
                        View view2 = LayoutInflater.inflateStatic(0x7F04000A, null);  // layout:main_new_version
                        ((TextView)view2.findViewById(0x7F0B003C)).setText("- none -");  // id:changelog
                        Alert.show(Alert.create().setCustomTitle(Tools.getCustomTitle(0x7F07014D)).setView(view2).setNegativeButton(Re.s(0x7F07009D), null));  // string:last_stats "Stats for your last search"
                        return;
                    }
                    case 0x7F0B00B6: {  // id:show_logcat
                        this.showLogcat();
                        return;
                    }
                    case 0x7F0B00B7: {  // id:config_region_log
                        Debug.runCollectRegionLog();
                        return;
                    }
                    case 0x7F0B00B8: {  // id:screenshot
                        ConfigListAdapter.takeScreenshot();
                        return;
                    }
                    case 0x7F0B00B9: {  // id:about
                        Alert.show(Alert.create().setCustomTitle(Tools.getCustomTitle(0x7F0700D3)).setMessage(Re.s(("__version__ " + 96.0f + " (" + 0x3E79 + ")" + " - Mod" + "\n\n__developers__\n\t酔月 - __pm__.\n\t妒猫 - __tech__.\n\tEvening - __tech__.\n" + "\n__special_thanks__\n\tRL,\n\t曦轩,\n\t文艺,\n\t极致,\n\t抹布,\n\t绝代,\n\t霸天,\n\tAy,\n\t星辰,\n\t苏辞,\n\t执念,\n\t在劫难逃."))).setPositiveButton(Re.s(0x7F07009D), null));  // string:about "About"
                        return;
                    }
                    default: {
                        Log.w(("Unknown id in config: " + v2));
                    }
                }
            }
        }

        class android.ext.ConfigListAdapter.12 implements DialogInterface.OnClickListener {
            @Override  // android.content.DialogInterface$OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                ConfigListAdapter.hideIcon(which);
            }
        }

    }

    private static void saveUsage() {
        new SPEditor().putLong("mc", ConfigListAdapter.USAGE, 0L).commit();
    }

    void setGameName() {
        TextView textView = (TextView)this.mGameSection.get();
        if(textView == null) {
            return;
        }
        String text = Re.s(0x7F070268);  // string:game_settings "Settings for the game:"
        ProcessInfo pi = MainService.instance.processInfo;
        String name = pi == null ? null : pi.name;
        if(name != null) {
            text = Tools.stripColon(text) + " \"" + name + "\":";
        }
        textView.setText(text);
    }

    private void setIcon(TextView tv) {
        int icon = 0;
        switch(tv.getId()) {
            case 0x7F0B0013: {  // id:help
                icon = 0x7F02002F;  // drawable:ic_help_circle_outline_white_24dp
                break;
            }
            case 0x7F0B0084: {  // id:config_time_jump_panel
                icon = 0x7F020021;  // drawable:ic_fast_forward_white_24dp
                break;
            }
            case 0x7F0B0086: {  // id:config_unrandomizer
                icon = 0x7F02001C;  // drawable:ic_dice_multiple_white_24dp
                break;
            }
            case 0x7F0B008C: {  // id:config_autopause
                icon = 0x7F02003A;  // drawable:ic_pause_white_18dp
                break;
            }
            case 0x7F0B008E: {  // id:config_saved_list_updates_interval
                icon = 0x7F020018;  // drawable:ic_content_save_white_24dp
                break;
            }
            case 0x7F0B008F: {  // id:config_speeds
                icon = 0x7F02004D;  // drawable:ic_speedometer_white_24dp
                break;
            }
            case 0x7F0B009E: {  // id:config_history_limit
                icon = 0x7F020032;  // drawable:ic_history_white_24dp
                break;
            }
            case 0x7F0B00A4: {  // id:config_use_small_list_items
                icon = 0x7F020029;  // drawable:ic_format_list_bulleted_white_24dp
                break;
            }
            case 0x7F0B00A9: {  // id:config_icons_size
                icon = 0x7F02004F;  // drawable:ic_tooltip_edit_white_24dp
                break;
            }
            case 0x7F0B00B8: {  // id:screenshot
                icon = 0x7F020012;  // drawable:ic_cellphone_screenshot_white_24dp
            }
        }
        if(icon != 0) {
            Tools.addIconToTextView(tv, Tools.getDrawable(icon), Config.getIconSize());
        }
    }

    public static void setLocale(Configuration config, Locale locale) {
        if(Build.VERSION.SDK_INT >= 17) {
            try {
                config.setLocale(locale);
            }
            catch(Throwable e) {
                Log.badImplementation(e);
            }
            try {
                config.setLayoutDirection(locale);
            }
            catch(Throwable e) {
                Log.badImplementation(e);
            }
        }
        config.locale = locale;
    }

    private static void showChange(AlertDialog dialog, View.OnClickListener listener, android.widget.EditText pathEdit) {
        dialog.setButton(-1, Re.s(0x7F0700A0), new DialogInterface.OnClickListener() {  // string:confirm "Confirm"
            @Override  // android.content.DialogInterface$OnClickListener
            public void onClick(DialogInterface d, int which) {
                Button button0 = dialog.getButton(-1);
                listener.onClick(button0);
            }
        });
        Alert.setOnShowListener(dialog, new DialogInterface.OnShowListener() {
            @Override  // android.content.DialogInterface$OnShowListener
            public void onShow(DialogInterface d) {
                Tools.setButtonListener(d, -1, null, listener);
            }
        });
        Alert.show(dialog, pathEdit);
    }

    // 检测为 Lambda 实现
    public static void showHelp() [...]

    public static void showHelp(int id) {
        int open = 0;
        String[] topics = new String[ConfigListAdapter.helpConfig.length / 3 + 1];
        for(int i = 0; i < ConfigListAdapter.helpConfig.length; ++i) {
            if(i % 3 == 0) {
                int index = i / 3 + 1;
                if(id == ConfigListAdapter.helpConfig[i] || id == ConfigListAdapter.helpConfig[i + 1]) {
                    open = index;
                }
                topics[index] = Re.s(ConfigListAdapter.helpConfig[i]);
            }
        }
        topics[0] = Re.s(0x7F070198);  // string:video_tutorials "Video tutorials and guides how to hack"
        android.ext.ConfigListAdapter.17 listener = new DialogInterface.OnClickListener() {
            @Override  // android.content.DialogInterface$OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                if(which == 0) {
                    new GoOnForum("kwws=22jdphjxdugldq1qhw2iruxp2jdoohu|2fdwhjru|250ylghr0wxwruldov2").onClick(dialog, 0);
                    return;
                }
                String msg = Re.s(ConfigListAdapter.helpConfig[(which - 1) * 3]) + ":\n" + Re.s(ConfigListAdapter.helpConfig[(which - 1) * 3 + 1]);
                int links = ConfigListAdapter.helpConfig[(which - 1) * 3 + 2];
                if(links >= 0) {
                    String msg = msg.trim() + "\n\n";
                    if(msg.charAt(msg.length() - 3) != 58) {
                        msg = msg + Re.s(0x7F07019A) + ":\n";  // string:video "Videos"
                    }
                    msg = msg + Tools.removeNewLinesChars(ConfigListAdapter.helpVideos[links]);
                }
                if(ConfigListAdapter.helpConfig[(which - 1) * 3] == 0x7F07012C) {  // string:help_floating_icon_title "The floating icon"
                    msg = msg.replace(Re.s(0x7F070005), Re.s(0x7F070001)) + "\nhttps://productforums.google.com/forum/#!topic/translate/1Pywh5vI1kE\n\n" + Tools.stripColon(0x7F0702C8) + ":\n" + "http://gameguardian.net/v-240";  // string:s "%s"
                }
                View view0 = LayoutInflater.inflateStatic(0x7F04000A, null);  // layout:main_new_version
                TextView text = (TextView)view0.findViewById(0x7F0B003C);  // id:changelog
                Tools.setClickableText(text, msg);
                Tools.colorizeHelp(text.getText());
                AlertDialog.Builder alertDialog$Builder0 = Alert.create().setView(view0).setNegativeButton(Re.s(0x7F07009D), ConfigListAdapter.getShowHelpListener());  // string:ok "OK"
                if(ConfigListAdapter.helpConfig[(which - 1) * 3] == 0x7F070289) {  // string:work_without_root_title "Work without root"
                    alertDialog$Builder0.setPositiveButton(0x7F070156, new GoOnForum("kwws=22jdphjxdugldq1qhw2iruxp2wrslf24<7540yluwxdo0vsdfhv0wr0uxq0jdphjxdugldq0zlwkrxw0urrw2"));  // string:more "More"
                }
                Alert.show(alertDialog$Builder0);
                if(Config.vSpace && ConfigListAdapter.helpConfig[(which - 1) * 3] == 0x7F07028B) {  // string:help_game_not_listed_title "Why my game is not in the process list?"
                    byte vSpace64 = Config.vSpace64;
                    if(vSpace64 == 0) {
                        vSpace64 = !ConfigListAdapter.isClient64() || !Config.vSpacePkg.contains("64") ? 2 : 1;
                        Config.vSpace64 = vSpace64;
                    }
                    if(vSpace64 == 1) {
                        try {
                            Alert.show(Alert.create().setMessage(Re.s(0x7F0702D1)).setNegativeButton(Re.s(0x7F07009D), null));  // string:vspace64 "You ran __app_name__ in a virtual space that supports 64-bit processes.\n\n__app_name__ 
                                                                                                                                // will not see 32-bit processes running in this space.\n\nInstall the second copy 
                                                                                                                                // of __app_name__ in the \"__for32__\" mode if you want to work in __app_name__ with 
                                                                                                                                // 32-bit processes."
                        }
                        catch(Throwable e) {
                            Log.w(("Failed check vSpace: " + Config.vSpacePkg), e);
                        }
                    }
                }
            }
        };
        if(open == 0) {
            Alert.show(Alert.create().setItems(topics, listener).setNegativeButton(Re.s(0x7F0700A1), null));  // string:cancel "Cancel"
            return;
        }
        listener.onClick(null, open);
    }

    private void showLogcat() {
        new DaemonThread("loadLogcat") {
            @Override
            public void run() {
                Tools.showToast(("Logcat: " + Re.s(0x7F0700C8)), 0);  // string:loading "Loading..."
                Tools.alertBigText(("Wed Mar 04 04:36:14 CST 2026" + '\n' + Debug.getLogcat(true)), 3);
            }
        }.start();
    }

    static void takeScreenshot() {
        Alert.show(Alert.create().setCustomTitle(Tools.getCustomTitle(0x7F070316)).setMessage(Tools.stringFormat(Re.s(0x7F070317), new Object[]{8})).setNegativeButton(Re.s(0x7F0700A1), null).setPositiveButton(Re.s(0x7F07009D), new DialogInterface.OnClickListener() {  // string:screenshot "Take a screenshot"
            @Override  // android.content.DialogInterface$OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                MainService service;
                try {
                    service = MainService.instance;
                    service.dismissDialog();
                    ThreadManager.getHandlerUiThread().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                MainService service = MainService.instance;
                                service.dismissDialog();
                                service.hotPoint.show();
                                service.updateNotification(false);
                            }
                            catch(Throwable e) {
                                Log.e("Failed restore after take screenshot", e);
                            }
                        }
                    }, 8000L);
                    service.updateNotification(true);
                    service.hotPoint.hide();
                    ThreadManager.getHandlerUiThread().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            MainService.instance.mDaemonManager.takeScreenshot();
                        }
                    }, 300L);
                }
                catch(Throwable e) {
                    Log.e("Failed take screenshot", e);
                    try {
                        service.hotPoint.show();
                    }
                    catch(Throwable e) {
                        Log.e("Failed restore after take screenshot", e);
                    }
                }
            }
        }));
    }

    void updateConfigIcon(float alpha, int hide, TextView view, ImageView icon, int size) {
        String text;
        MainService service = MainService.instance;
        String s = hide >= 0 ? Tools.stringFormat(Re.s(0x7F0700F7), new Object[]{hide}) : Re.s(0x7F0700F8);  // string:icon_hide_after "after __d__ seconds"
        try {
            text = Tools.stringFormat(Re.s(0x7F0700F6), new Object[]{((int)(100 - ((int)(100.0f * alpha)))), s});  // string:config_icon_text "Icon: transparency = __d__%%, hide - __s__"
        }
        catch(Throwable e) {
            text = "fail";
            Log.e("Fail setup string for icon config", e);
        }
        TextView textView = (TextView)this.mConfigIconTextView.get();
        if(textView != null) {
            textView.setText(text);
        }
        if(view != null) {
            view.setText(text);
        }
        service.hotPoint.setSize(size);
        int v2 = service.hotPoint.getSizePx();
        TimeJumpPanel panel = service.timeJumpPanel;
        if(panel != null) {
            panel.setMinSize(v2);
        }
        ImageView imageView = (ImageView)this.mConfigIcon.get();
        if(imageView != null) {
            Tools.setImageAlpha(imageView, 255.0f * alpha);
            Config.setIconSizeEx(imageView, v2);
        }
        if(icon != null) {
            Tools.setImageAlpha(icon, 255.0f * alpha);
            Config.setIconSizeEx(icon, v2);
        }
        service.hotPoint.setLayoutAlpha(alpha);
        service.hotPoint.setVanishingTime(hide);
        service.notifyDataSetChanged(service.mConfigListView.getAdapter());
    }

    public void updateData() {
        View[] arr_view = this.mViewList;
        for(int v = 0; v < arr_view.length; ++v) {
            View view = arr_view[v];
            if(view instanceof TextView) {
                Option config$Option0 = Config.get(view.getId());
                if(config$Option0 != null) {
                    ((TextView)view).setText(config$Option0.toString());
                }
            }
        }
    }
}

