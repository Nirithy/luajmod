package android.ext;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.AlertDialog.Builder;
import android.app.AlertDialog;
import android.app.Notification.Action.Builder;
import android.app.Notification.Builder;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentCallbacks2;
import android.content.ComponentCallbacks;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.res.Configuration;
import android.database.DataSetObserver;
import android.fix.ContextWrapper;
import android.fix.LayoutInflater;
import android.fix.SparseArray;
import android.fix.WrapLayout;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.hardware.display.DisplayManager;
import android.os.Build.VERSION;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.sup.ArrayListResults;
import android.sup.LongSparseArrayChecked;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import android.view.Display;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class MainService extends ServiceBase implements View.OnClickListener, View.OnLongClickListener {
    class CalculateOffset extends MenuItem {
        private static final int MAX_ITEMS = 10;

        public CalculateOffset() {
            super(0x7F070237, 0x7F020059);  // string:calculate_offset_xor "Calculate offset, XOR"
        }

        private void calculate() {
            Object object0 = MainService.this.getCheckList();
            long[] items = new long[10];
            long[] values = new long[10];
            int count = 0;
            int total = 0;
            if(object0 instanceof ArrayListResults) {
                AddressItem item = new AddressItem();
                int v2 = ((ArrayListResults)object0).size();
                for(int i = 0; i < v2; ++i) {
                    if(((ArrayListResults)object0).checked(i)) {
                        if(count < 10) {
                            ((ArrayListResults)object0).get(i, item);
                            items[count] = item.address;
                            values[count] = item.data & AddressItem.DATA_MASK[item.getSize()];
                            ++count;
                        }
                        ++total;
                        if(total <= 10) {
                            continue;
                        }
                        break;
                    }
                }
            }
            else if(object0 instanceof LongSparseArrayChecked) {
                int v4 = ((LongSparseArrayChecked)object0).size();
                for(int i = 0; i < v4; ++i) {
                    if(((LongSparseArrayChecked)object0).checkAt(i)) {
                        SavedItem item = (SavedItem)((LongSparseArrayChecked)object0).valueAt(i);
                        if(item instanceof SavedItem) {
                            if(count < 10) {
                                items[count] = item.address;
                                values[count] = item.data & AddressItem.DATA_MASK[item.getSize()];
                                ++count;
                            }
                            ++total;
                            if(total <= 10) {
                                continue;
                            }
                            break;
                        }
                    }
                }
            }
            else if(object0 instanceof boolean[]) {
                MemoryContentAdapter memAdapter = MainService.this.mMemListAdapter;
                int s = ((boolean[])object0).length - 1;
                for(int i = 1; i < s; ++i) {
                    if(((boolean[])object0)[i]) {
                        if(count < 10) {
                            items[count] = memAdapter.getAddr(i);
                            AddressItem item = (AddressItem)memAdapter.getItem(i);
                            values[count] = item.data & AddressItem.DATA_MASK[item.getSize()];
                            ++count;
                        }
                        ++total;
                        if(total <= 10) {
                            continue;
                        }
                        break;
                    }
                }
            }
            if(count < 2) {
                Tools.showToast(Tools.stringFormat(Re.s(0x7F070168), new Object[]{2}));  // string:need_select_more "Need select __d__ or more items."
                return;
            }
            if(total > count) {
                Tools.showToast(Tools.stringFormat(Re.s(0x7F070167), new Object[]{count}));  // string:used_only_first "Used only first __d__ items."
            }
            Alert.show(Alert.create().setAdapter(new ArrayAdapter(MainService.context, new String[]{Tools.stripColon(0x7F070169), "XOR"}), (/* 缺少LAMBDA参数 */, int mode) -> {
                String[] list = new String[(this.val$countItems - 1) * this.val$countItems / 2];
                String[] copy = new String[(this.val$countItems - 1) * this.val$countItems / 2];
                int k = 0;
                for(int i = 0; i < this.val$countItems - 1; ++i) {
                    for(int j = i + 1; j < this.val$countItems; ++j) {
                        long from = this.val$items[i];
                        long to = this.val$items[j];
                        String s = mode == 0 ? Long.toHexString(to - from).toUpperCase(Locale.US) : Tools.stringFormat("%,d", new Object[]{((long)(this.val$values[i] ^ this.val$values[j]))});
                        copy[k] = s;
                        list[k] = ToolsLight.prefixLongHex(8, from) + " - " + ToolsLight.prefixLongHex(8, to) + ": " + s;
                        ++k;
                    }
                }
                Alert.show(Alert.create().setCustomTitle(Tools.getCustomTitle(0x7F070238)).setAdapter(new ArrayAdapter(MainService.context, list), new DialogInterface.OnClickListener() {  // string:select_copy_item "Select an item to copy:"
                    @Override  // android.content.DialogInterface$OnClickListener
                    public void onClick(DialogInterface dialog, int which) {
                        Tools.copyText(copy[which]);
                        Tools.dismiss(dialog);
                    }
                }).setNegativeButton(Re.s(0x7F0700A1), null));  // string:cancel "Cancel"
            }).setNegativeButton(Re.s(0x7F0700A1), null));  // string:cancel "Cancel"

            class android.ext.MainService.CalculateOffset.1 implements DialogInterface.OnClickListener {
                android.ext.MainService.CalculateOffset.1(long[] arr_v, long[] arr_v1, int v) {
                }

                @Override  // android.content.DialogInterface$OnClickListener
                public void onClick(DialogInterface dialog, int which) {
                    CalculateOffset.this.display(this.val$items, this.val$values, this.val$countItems, which);
                }
            }

        }

        // 检测为 Lambda 实现
        void display(long[] items, long[] values, int count, int mode) [...]

        @Override  // android.ext.MenuItem
        public void onClick(View v) {
            this.calculate();
        }
    }

    class ClearSelection extends MenuItem {
        public ClearSelection() {
            super(0x7F070136, 0x7F02004A);  // string:clear_selection "Clear selection"
        }

        @Override  // android.ext.MenuItem
        public void onClick(View v) {
            MainService.this.changeCheckList(0);
        }
    }

    class ExportSelected extends MenuItem {
        int memoryFlags;

        public ExportSelected() {
            super(0x7F0701F9, 0x7F02001E);  // string:export_selected "Export selected"
            this.memoryFlags = 0;
        }

        ExportSelected(int title, int resId) {
            super(title, resId);
            this.memoryFlags = 0;
        }

        void doItems(List list0, Object obj) {
            int v = list0.size();
            SavedItem[] list = new SavedItem[v];
            for(int i = 0; i < v; ++i) {
                AddressItem item = (AddressItem)list0.get(i);
                list[i] = item instanceof SavedItem ? ((SavedItem)item) : new SavedItem(item);
            }
            new ListManager(MainService.this.processInfo, list).fromExport = true;
        }

        void getItems() {
            Object object0 = MainService.this.getCheckList();
            if(Tools.getSelectedCount(object0) == 0) {
                Tools.showToast(0x7F07013C);  // string:nothing_selected "Nothing selected"
                return;
            }
            ArrayList items = new ArrayList();
            if(object0 instanceof boolean[]) {
                if(this.memoryFlags == 0) {
                    SparseIntArray counts = new SparseIntArray();
                    new TypeSelector(AddressItem.getDataForEditAll(MainService.this.mMemListAdapter.getPossibleTypes(((boolean[])object0), counts)), counts, "0", Re.s(0x7F0700EF), new DialogInterface.OnClickListener() {  // string:type_change_request "Select the data type to change"
                        @Override  // android.content.DialogInterface$OnClickListener
                        public void onClick(DialogInterface dialog, int flags) {
                            ExportSelected.this.memoryFlags = flags;
                            ExportSelected.this.getItems();
                        }
                    });
                    return;
                }
                MemoryContentAdapter memAdapter = MainService.this.mMemListAdapter;
                int s = ((boolean[])object0).length - 1;
                for(int i = 1; i < s; ++i) {
                    if(((boolean[])object0)[i]) {
                        Object object1 = memAdapter.getItem(i);
                        if(object1 instanceof AddressItem) {
                            ((AddressItem)object1).flags = this.memoryFlags;
                            if(((AddressItem)object1).isPossibleItem()) {
                                items.add(((AddressItem)object1));
                            }
                        }
                    }
                }
                this.memoryFlags = 0;
            }
            else if(object0 instanceof ArrayListResults) {
                int v2 = ((ArrayListResults)object0).size();
                for(int i = 0; i < v2; ++i) {
                    if(((ArrayListResults)object0).checked(i)) {
                        items.add(((ArrayListResults)object0).get(i, null));
                    }
                }
            }
            else if(object0 instanceof LongSparseArrayChecked) {
                int v4 = ((LongSparseArrayChecked)object0).size();
                for(int i = 0; i < v4; ++i) {
                    if(((LongSparseArrayChecked)object0).checkAt(i)) {
                        SavedItem item = (SavedItem)((LongSparseArrayChecked)object0).valueAt(i);
                        if(item instanceof SavedItem) {
                            items.add(item);
                        }
                    }
                }
            }
            this.doItems(items, object0);
        }

        @Override  // android.ext.MenuItem
        public void onClick(View v) {
            this.start();
        }

        void start() {
            this.memoryFlags = 0;
            this.getItems();
        }
    }

    class InvertSelection extends MenuItem {
        public InvertSelection() {
            super(0x7F070137, 0x7F020049);  // string:invert_selection "Invert selection"
        }

        @Override  // android.ext.MenuItem
        public void onClick(View v) {
            MainService.this.changeCheckList(2);
        }
    }

    class LoadChangedAsResults extends LoadSelectedAsResults {
        public LoadChangedAsResults() {
            super(0x7F070345, 0x7F020045);  // string:changed_as_search_result "Changed as search result"
        }

        @Override  // android.ext.MainService$ExportSelected
        void getItems() {
            AddressItemSet revertData = MainService.this.revertMap;
            if(revertData.size() == 0) {
                Tools.showToast(Re.s(0x7F070346));  // string:changed_not_found "Changed items not found."
                return;
            }
            this.doItems(revertData.getAll(), null);
        }
    }

    class LoadSelectedAsResults extends ExportSelected {
        public LoadSelectedAsResults() {
            super(0x7F0702BF, 0x7F020047);  // string:selected_as_search_result "Selected as search result"
        }

        LoadSelectedAsResults(int title, int resId) {
            super(title, resId);
        }

        @Override  // android.ext.MainService$ExportSelected
        void doItems(List list0, Object obj) {
            if(list0.size() == 0) {
                MainService.this.clear(0);
                return;
            }
            if(!MainService.this.showBusyDialog()) {
                throw new NumberFormatException(Re.s(0x7F070223));  // string:busy_dialog_fail "The progress dialog could not be displayed."
            }
            if(MainService.this.mResultCount != 0L) {
                MainService.this.clear(0);
            }
            MainService.this.usedFuzzy = false;
            MainService.this.mDaemonManager.sendConfig(0);
            MainService.this.lockApp(0);
            MainService.this.showSearchHint = false;
            MainService.this.mDaemonManager.loadResults(0, list0);
            MainService.this.mMainScreen.setCurrentTab(1);
            if(obj instanceof ArrayListResults || obj instanceof LongSparseArrayChecked) {
                Record record = MainService.this.currentRecord;
                if(record != null) {
                    StringBuilder out = new StringBuilder();
                    out.append("\nlocal t = ");
                    if(obj instanceof ArrayListResults) {
                        Converter.recordGetResults(record, true);
                    }
                    else {
                        out.append("gg.getListItems()\n");
                    }
                    out.append("gg.loadResults(t)\n");
                    out.append("t = nil\n");
                }
            }
        }

        @Override  // android.ext.MainService$ExportSelected
        public void onClick(View v) {
            if(MainService.this.mResultCount == 0L) {
                this.start();
                return;
            }
            Alert.show(Alert.create().setMessage(Re.s(0x7F0702C0)).setPositiveButton(Re.s(0x7F07009B), new DialogInterface.OnClickListener() {  // string:clear_results_prompt "This will replace the current search results. Continue?"
                @Override  // android.content.DialogInterface$OnClickListener
                public void onClick(DialogInterface dialog, int which) {
                    LoadSelectedAsResults.this.start();
                }
            }).setNegativeButton(Re.s(0x7F07009C), null));  // string:no "No"
        }
    }

    class Remove extends MenuItem {
        final boolean savedList;

        public Remove(boolean savedList) {
            super(0x7F070193, 0x7F02001B);  // string:remove "Remove"
            this.savedList = savedList;
        }

        @Override  // android.ext.MenuItem
        public void onClick(View v) {
            Object object0 = MainService.this.getCheckList();
            int v = Tools.getSelectedCount(object0);
            String s = " (" + v + ')';
            String[] arr_s = {Re.s(0x7F0702CE), "---", Re.s(0x7F070192) + s, Re.s(0x7F070193) + s};  // string:remove_all2 "Remove all"
            Alert.show(Alert.create().setItems(arr_s, new DialogInterface.OnClickListener() {
                @Override  // android.content.DialogInterface$OnClickListener
                public void onClick(DialogInterface dialog, int which) {
                    if(which > 2 && v == 0) {
                        Tools.showToast(0x7F07013C);  // string:nothing_selected "Nothing selected"
                        return;
                    }
                    switch(which) {
                        case 0: {
                            Alert.show(Alert.create().setCustomTitle(Tools.getCustomTitle(0x7F0702CE)).setPositiveButton(Re.s(0x7F07009B), new DialogInterface.OnClickListener() {  // string:remove_all2 "Remove all"
                                @Override  // android.content.DialogInterface$OnClickListener
                                public void onClick(DialogInterface dialog, int which) {
                                    if(Remove.this.savedList) {
                                        MainService.this.savedList.clear();
                                    }
                                    else {
                                        MainService.this.clear();
                                    }
                                    Record record = MainService.instance.currentRecord;
                                    if(record != null) {
                                        record.write((Remove.this.savedList ? "\ngg.clearList()\n" : "\ngg.clearResults()\n"));
                                    }
                                }
                            }).setNegativeButton(Re.s(0x7F07009C), null));  // string:no "No"
                            return;
                        }
                        case 1: {
                            ThreadManager.getHandlerUiThread().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Alert.show(((AlertDialog)dialog));
                                }
                            }, 1000L);
                            return;
                        }
                        case 2: {
                            MainService.this.revertItems(object0, null);
                            break;
                        }
                        case 3: {
                            break;
                        }
                        default: {
                            return;
                        }
                    }
                    MainService.this.removeItems(object0);
                }
            }).setNegativeButton(Re.s(0x7F0700A1), null));  // string:cancel "Cancel"
        }
    }

    class RevertSelected extends MenuItem {
        int memoryFlags;

        public RevertSelected() {
            super(0x7F070144, 0x7F020052);  // string:revert_selected "Revert selected"
            this.memoryFlags = 0;
        }

        @Override  // android.ext.MenuItem
        public void onClick(View v) {
            this.memoryFlags = 0;
            this.revert();
        }

        void revert() {
            Object object0 = MainService.this.getCheckList();
            AddressItemSet revert = new AddressItemSet();
            AddressItemSet revertData = MainService.this.revertMap;
            Result result = new Result();
            if(!(object0 instanceof boolean[])) {
                MainService.this.revertItems(object0, revert);
            }
            else if(this.memoryFlags == 0) {
                SparseIntArray counts = new SparseIntArray();
                SparseArray sparseArray0 = AddressItem.getDataForEditAll(MainService.this.mMemListAdapter.getPossibleTypes(((boolean[])object0), counts));
                if(counts.size() > 0) {
                    new TypeSelector(sparseArray0, counts, "0", Re.s(0x7F0700EF), new DialogInterface.OnClickListener() {  // string:type_change_request "Select the data type to change"
                        @Override  // android.content.DialogInterface$OnClickListener
                        public void onClick(DialogInterface dialog, int flags) {
                            RevertSelected.this.memoryFlags = flags;
                            RevertSelected.this.revert();
                        }
                    });
                    return;
                }
            }
            else {
                MemoryContentAdapter memAdapter = MainService.this.mMemListAdapter;
                int s = ((boolean[])object0).length - 1;
                for(int i = 1; i < s; ++i) {
                    if(((boolean[])object0)[i]) {
                        AddressItem addressItem0 = new AddressItem(memAdapter.getAddr(i), 0L, this.memoryFlags);
                        if(addressItem0.isPossibleItem()) {
                            revertData.get(addressItem0.address, addressItem0.flags, result);
                            if(result.found) {
                                revert.put(addressItem0.address, addressItem0.flags, result.data);
                            }
                        }
                    }
                }
                this.memoryFlags = 0;
            }
            int v2 = revert.size();
            if(v2 == 0) {
                Tools.showToast(0x7F070145);  // string:nothing_to_revert "Nothing to revert"
                return;
            }
            Alert.show(Alert.create().setCustomTitle(Tools.getCustomTitle(0x7F070144)).setMessage(Tools.stringFormat(Re.s(0x7F070146), new Object[]{v2})).setPositiveButton(Re.s(0x7F07009B), new DialogInterface.OnClickListener() {  // string:revert_selected "Revert selected"
                @Override  // android.content.DialogInterface$OnClickListener
                public void onClick(DialogInterface dialog, int which) {
                    revert.revert();
                    Record record = MainService.instance.currentRecord;
                    if(record != null) {
                        record.write("if revert ~= nil then gg.setValues(revert) end\n");
                    }
                }
            }).setNegativeButton(Re.s(0x7F07009C), null));  // string:no "No"
        }
    }

    class SaveSelected extends MenuItem {
        public SaveSelected() {
            super(0x7F07013B, 0x7F020018);  // string:save_selected "Save selected"
        }

        @Override  // android.ext.MenuItem
        public void onClick(View v) {
            Object object0 = MainService.this.getCheckList();
            int v = Tools.getSelectedCount(object0);
            if(v == 0) {
                Tools.showToast(0x7F07013C);  // string:nothing_selected "Nothing selected"
                return;
            }
            if(object0 instanceof boolean[]) {
                SparseIntArray counts = new SparseIntArray();
                new TypeSelector(AddressItem.getDataForEditAll(MainService.this.mMemListAdapter.getPossibleTypes(((boolean[])object0), counts)), counts, "0", Re.s(0x7F0700EF), new DialogInterface.OnClickListener() {  // string:type_change_request "Select the data type to change"
                    @Override  // android.content.DialogInterface$OnClickListener
                    public void onClick(DialogInterface dialog, int flags) {
                        MemoryContentAdapter memAdapter = MainService.this.mMemListAdapter;
                        boolean[] list = (boolean[])object0;
                        int s = list.length - 1;
                        for(int i = 1; i < s; ++i) {
                            if(list[i]) {
                                long v3 = memAdapter.getAddr(i);
                                SavedItem savedItem0 = new SavedItem(v3, 0L, flags);
                                if(!savedItem0.isPossibleItem()) {
                                    savedItem0.flags = AddressItem.getTypeForAddress(savedItem0.address, true);
                                }
                                SavedItem savedItem1 = MainService.this.savedList.getItemByAddress(v3);
                                if(savedItem1 == null || savedItem1.flags != savedItem0.flags) {
                                    MainService.this.savedList.add(savedItem0, 0, false);
                                }
                            }
                        }
                        MainService.this.savedList.notifyDataSetChanged();
                        MainService.this.savedList.reloadData();
                        Tools.showToast(Tools.stringFormat(Re.s(0x7F07013D), new Object[]{v}));  // string:items_added "Items (__d__) added to the saved list"
                    }
                });
                return;
            }
            if(object0 instanceof ArrayListResults) {
                ArrayList checked = new ArrayList();
                int v1 = ((ArrayListResults)object0).size();
                for(int i = 0; i < v1; ++i) {
                    if(((ArrayListResults)object0).checked(i)) {
                        checked.add(((ArrayListResults)object0).get(i, null));
                    }
                }
                for(Object object1: checked) {
                    AddressItem item = (AddressItem)object1;
                    SavedItem savedItem0 = MainService.this.savedList.getItemByAddress(item.address);
                    if(savedItem0 == null || savedItem0.flags != item.flags) {
                        MainService.this.savedList.add(new SavedItem(item), 0, false);
                    }
                }
                MainService.this.savedList.notifyDataSetChanged();
                MainService.this.savedList.reloadData();
                Tools.showToast(Tools.stringFormat(Re.s(0x7F07013D), new Object[]{v}));  // string:items_added "Items (__d__) added to the saved list"
                Record record = MainService.instance.currentRecord;
                if(record != null) {
                    record.write("\nlocal t = ");
                    Converter.recordGetResults(record, true);
                    record.write("gg.addListItems(t)\n");
                    record.write("t = nil\n\n");
                }
            }
        }
    }

    class SelectAll extends MenuItem {
        public SelectAll() {
            super(0x7F070138, 0x7F020048);  // string:select_all "Select all"
        }

        @Override  // android.ext.MenuItem
        public void onClick(View v) {
            MainService.this.changeCheckList(1);
        }
    }

    public static final boolean[] DEFAULT_RAND = null;
    public static final String DEFAULT_STORAGE = null;
    public static final boolean[] DEFAULT_TIMERS = null;
    public static final String MEM_DEV_CHECK_FAIL = "mem-fail";
    public static final int NOTIFY_ID = 1;
    public static final String OLD_DEFAULT_STORAGE = "DefaultStorage";
    public static final int TAB_MEMORY_EDITOR = 3;
    public static final int TAB_SAVED_LIST = 2;
    public static final int TAB_SEARCH = 1;
    public static final int TAB_SETTINGS = 0;
    public static final String TAG = "AndroidService";
    public static final String TEMP_PATH_KEY = "temp-path";
    public static final int USED_LISTS = 4;
    private boolean alertPaused;
    private static volatile ComponentCallbacks cc;
    private static volatile boolean checkLibs;
    public static volatile Context context;
    public static volatile boolean created;
    Record currentRecord;
    Script currentScript;
    volatile boolean dialogDeadShow;
    FilterButtonListener filterListener;
    private static volatile boolean firstGetAfterSearch;
    FuzzyButtonListener fuzzyListener;
    HotPoint hotPoint;
    public static volatile MainService instance;
    private static int lastFlags;
    private static long lastToastTime;
    static int lastType;
    public ListSpeed listSpeed;
    ListView mAddrListView;
    final ArrayListResults mAddressList;
    AppDetector mAppDetector;
    private ImageView mAppImage;
    TextView mAppName;
    private ImageView mBackButton;
    BusyDialog mBusyDialog;
    ConfigListAdapter mConfigListAdapter;
    ListView mConfigListView;
    View mConfigPage;
    View mCountWait;
    DaemonManager mDaemonManager;
    TextView mFilter;
    TextView mFoundCount;
    private boolean mHasNotLocked;
    View mInfoCnt;
    View mInfoMem;
    View mInfoRow;
    ImageButton mListRefreshButton;
    TabHost mMainScreen;
    View mMemEditorPage;
    MemoryContentAdapter mMemListAdapter;
    ListView mMemListView;
    View mMenuButton;
    ImageView mMenuButtonLand;
    private ImageView mMoreButton;
    TextView mNothingFoundText;
    private ImageButton mProcessPauseButton;
    volatile long mResultCount;
    TextView mSavedFormat;
    ListView mSavedListView;
    View mSavedPage;
    View mSearchPage;
    private ImageView mSmallImage;
    TextView mValueFormat;
    WindowManager mWindowManager;
    volatile MainDialog mainDialog;
    MaskButtonListener maskListener;
    volatile int memFree;
    volatile int memTotal;
    MemoryHistory memoryHistory;
    public static final MenuItem[][] menuItems;
    private boolean needPtraceNotify;
    private boolean noX86;
    private final int[] oldBkg;
    private static volatile boolean onConfigurationChangedSemaphore;
    private volatile ArrayList pendingScriptEnds;
    private boolean processHasBeenPaused;
    public ProcessInfo processInfo;
    boolean processPaused;
    boolean processRealPaused;
    TimersEditor randEditor;
    View recordInterrupt;
    Runnable refreshTabRunner;
    final AddressItemSet revertMap;
    private TextView savedCounter;
    SavedListAdapter savedList;
    private final Runnable savedListUpdate;
    View scriptInterrupt;
    View scriptUiButton;
    boolean scriptUiButtonClicked;
    private TextView searchCounter;
    private static volatile boolean searchHelperRestore;
    SearchButtonListener searchListener;
    View selectedTab;
    ShowApp showApp;
    boolean showSearchHint;
    private SparseBooleanArray showedErrors;
    TextView statusBar;
    public static volatile boolean stopped;
    TimeJumpPanel timeJumpPanel;
    TimersEditor timersEditor;
    View toolbarRow;
    final WrapLayout[] toolbars;
    boolean usedFuzzy;
    private final UsedList[] usedLists;
    MenuItem valueFormat;

    static {
        MainService.DEFAULT_STORAGE = "null_preferences";
        MainService.instance = null;
        MainService.stopped = false;
        MainService.created = false;
        MainService.menuItems = new MenuItem[3][];
        MainService.searchHelperRestore = false;
        MainService.DEFAULT_TIMERS = new boolean[0xB0];
        MainService.DEFAULT_RAND = new boolean[76];
        for(int i = 0; i < MainService.DEFAULT_RAND.length; ++i) {
            MainService.DEFAULT_RAND[i] = i % 4 < 2;
        }
        for(int i = 0; i < MainService.DEFAULT_TIMERS.length; ++i) {
            MainService.DEFAULT_TIMERS[i] = i % 4 < 2;
        }
        for(int v2 = 0; v2 < 3; ++v2) {
            int i = new int[]{40, 43, 44}[v2] - 1;
            MainService.DEFAULT_TIMERS[i * 4] = false;
            MainService.DEFAULT_TIMERS[i * 4 + 1] = false;
            MainService.DEFAULT_TIMERS[i * 4 + 2] = false;
            MainService.DEFAULT_TIMERS[i * 4 + 3] = false;
        }
        MainService.lastFlags = 0x7F;
        MainService.lastType = 0;
        MainService.checkLibs = true;
        MainService.cc = null;
        MainService.onConfigurationChangedSemaphore = false;
        MainService.firstGetAfterSearch = true;
        MainService.lastToastTime = 0L;
    }

    public MainService(Context context) {
        super(context);
        this.currentScript = null;
        this.scriptInterrupt = null;
        this.scriptUiButton = null;
        this.scriptUiButtonClicked = false;
        this.currentRecord = null;
        this.recordInterrupt = null;
        this.timeJumpPanel = null;
        this.mainDialog = null;
        this.processPaused = false;
        this.processRealPaused = false;
        this.processHasBeenPaused = false;
        this.alertPaused = false;
        this.mHasNotLocked = true;
        this.memTotal = 0;
        this.memFree = 0;
        this.mResultCount = 0L;
        this.usedFuzzy = false;
        this.showSearchHint = true;
        this.mAddressList = new ArrayListResults();
        this.savedList = new SavedListAdapter();
        this.revertMap = new AddressItemSet();
        this.processInfo = null;
        this.showedErrors = new SparseBooleanArray();
        this.pendingScriptEnds = new ArrayList();
        this.oldBkg = new int[3];
        this.savedListUpdate = new Runnable() {
            @Override
            public void run() {
                try {
                    if(MainService.this.selectedTab == MainService.this.mSavedPage) {
                        MainService.this.savedList.reloadData();
                    }
                }
                catch(Throwable e) {
                    Log.e("Failed update saved list", e);
                }
                try {
                    int period = Config.savedListUpdatesInterval > 0 ? Config.savedListUpdatesInterval : 1000;
                    Handler handler0 = ThreadManager.getHandlerUiThread();
                    handler0.removeCallbacks(this);
                    handler0.postDelayed(this, ((long)period));
                }
                catch(Throwable e) {
                    Log.w("Failed post timer", e);
                }
            }
        };
        this.refreshTabRunner = null;
        this.needPtraceNotify = true;
        this.noX86 = false;
        this.usedLists = new UsedList[4];
        this.dialogDeadShow = false;
        if(MainService.context == null) {
            MainService.context = ServiceContext.wrap(context);
        }
        MainService.instance = this;
        Tools.checkForBadContext();
        this.mDaemonManager = new DaemonManager();
        this.toolbars = new WrapLayout[3];
    }

    private static void _addUnique(List list0, String data) {
        if(data == null || data.length() == 0) {
            return;
        }
        Iterator iterator0 = list0.iterator();
        do {
            if(!iterator0.hasNext()) {
                list0.add(data);
                return;
            }
            Object object0 = iterator0.next();
        }
        while(!((String)object0).equals(data));
    }

    // 检测为 Lambda 实现
    void _updateStatusBar() [...]

    private static void addUnique(List list0, File dir) {
        if(dir == null) {
            return;
        }
        MainService.addUnique(list0, dir.getAbsolutePath());
    }

    private static void addUnique(List list0, String data) {
        if(data == null || data.length() == 0) {
            return;
        }
        if(data.contains("/emulated/0")) {
            MainService._addUnique(list0, data.replace("/emulated/0", "/emulated/legacy"));
        }
        MainService._addUnique(list0, data);
    }

    void allocatedPage(long page) {
        ThreadManager.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(0xFFFFFFFFFFFFFC00L < page && page <= 0L) {
                    Alert.show(Alert.create().setMessage(Re.s(0x7F0702DC) + ' ' + -page).setNegativeButton(Re.s(0x7F07009D), null).create());  // string:allocate_fail "Could not allocate memory page. Error"
                    return;
                }
                String s = AddressItem.getStringAddress(page, 4) + 'h';
                android.ext.MainService.3.1 listener = new DialogInterface.OnClickListener() {
                    @Override  // android.content.DialogInterface$OnClickListener
                    public void onClick(DialogInterface dialog, int which) {
                        switch(which) {
                            case -3: {
                                Tools.copyText(s);
                                return;
                            }
                            case -1: {
                                MainService mainService0 = MainService.instance;
                                String s = Tools.stripColon(0x7F0702DD);  // string:allocated "Allocated memory page:"
                                mainService0.goToAddress(null, s, s);
                            }
                        }
                    }
                };
                History.add(s, 1);
                Alert.show(Alert.create().setMessage(Tools.stripColon(0x7F0702DD) + ": " + s).setPositiveButton(Re.s(0x7F07008D), listener).setNeutralButton(Re.s(0x7F070161), listener).setNegativeButton(Re.s(0x7F07009D), null).create());  // string:allocated "Allocated memory page:"
            }
        });
    }

    private String appendProcessInfo(String sMessage) {
        return this.processInfo == null ? sMessage : sMessage + "\n\n" + this.processInfo.name + "\n[" + this.processInfo.packageName + "]";
    }

    void changeCheckList(int mode) {
        Tools.changeSelection(this.getCheckList(), mode);
        this.notifyCheckList();
    }

    private void checkAutopause() {
        if(this.mainDialog == null) {
            return;
        }
        if(this.processHasBeenPaused || (Config.configClient & 4) != 0 && this.mAppDetector.isAppSelectByUser()) {
            this.processPause(false);
        }
        this.updatePauseButton();
    }

    void checkLibs() {
        if(!MainService.checkLibs || (Config.configClient & 0x800) == 0 || this.processInfo == null || !DaemonLoader.isX86()) {
            return;
        }
        MainService.checkLibs = false;
        new ThreadEx(new Runnable() {
            @Override
            public void run() {
                Log.d(("doCheckLibs: " + MainService.this.doCheckLibs()));
            }
        }, "CheckLibs").start();
    }

    void clear() {
        this.clear(false);
    }

    void clear(byte seq) {
        this.clear(seq, false);
    }

    void clear(byte seq, boolean force) {
        this.clear(seq, force, true);
    }

    void clear(byte seq, boolean force, boolean notifyDaemon) {
        if(force) {
            this.showSearchHint = true;
        }
        this.mResultCount = 0L;
        if(notifyDaemon) {
            this.mDaemonManager.clear(seq);
        }
        MainService.instance.mDaemonManager.inOut.truncate();
        synchronized(this.mAddressList) {
            this.mAddressList.clear();
            this.mAddressList.trimToSize();
        }
        this.notifyDataSetChanged(this.mAddrListView.getAdapter());
        this.showCounters();
        MainService.searchHelperRestore = false;
    }

    // 检测为 Lambda 实现
    void clear(boolean force) [...]

    void detectApp(boolean force) {
        this.mAppDetector.detectApp(force);
    }

    void dismissBusyDialog() {
        this.hotPoint.onProgress(2, -1L, 1L, 0, 1, "");
        this.mBusyDialog.dismiss(true);
        this.mDaemonManager.clearCountFuzzyEqualRun();
        this.mDaemonManager.getMem();
        this.alertPaused = false;
    }

    void dismissDialog() {
        this.dismissDialog(true);
    }

    void dismissDialog(boolean hideScript) {
        Log.d(("ServiceProxy dismissDialog: " + this.mainDialog));
        if(this.mainDialog == null) {
            return;
        }
        this.mBusyDialog.dismiss(false);
        this.mainDialog.dismiss();
        this.mainDialog = null;
        this.showApp.hide(hideScript);
        this.hotPoint.show();
        this.processHasBeenPaused = this.processPaused;
        boolean realPaused = this.processRealPaused;
        boolean hide_1 = (Config.configDaemon & 0x80) != 0;
        if(this.mBusyDialog.isVisible() && (hide_1 || realPaused)) {
            String msg = Tools.stripColon(0x7F07021D) + ":\n";  // string:paused "The game is paused:"
            if(hide_1) {
                msg = msg + "- " + Tools.stringFormat(Re.s(0x7F07021F), new Object[]{Tools.stripColon(0x7F07014C) + ": 1"}) + ";\n";  // string:by_option "by the option \'__s__\'"
            }
            if(realPaused) {
                msg = (Config.configClient & 4) == 0 ? msg + "- " + Re.s(0x7F07021E) + ";\n" : msg + "- " + Tools.stringFormat(Re.s(0x7F07021F), new Object[]{Tools.stripColon(0x7F070141) + ": " + Re.s(0x7F07009B)}) + ";\n";  // string:by_pause_button "by the pause button"
            }
            String msg = msg.trim();
            if(this.alertPaused) {
                Tools.showToast(msg);
            }
            else {
                this.alertPaused = true;
                Alert.show(Alert.create(BaseActivity.context).setMessage(msg).setNegativeButton(Re.s(0x7F07009D), null).create());  // string:ok "OK"
            }
        }
        this.processResume();
        this.mDaemonManager.getMem();
        this.updateNotification();
    }

    int doCheckLibs() {
        File file;
        try {
            ApplicationInfo applicationInfo0 = Tools.getApplicationInfo(this.processInfo.packageName);
            if(applicationInfo0 == null) {
                return 1;
            }
            String libDir = applicationInfo0.nativeLibraryDir;
            if(libDir == null) {
                return 2;
            }
            boolean armLibs = false;
            byte[] buf = new byte[20];
            File[] arr_file = new File(libDir).listFiles();
            if(arr_file == null) {
                goto label_14;
            }
            int v = 0;
            while(true) {
                if(v < arr_file.length) {
                    file = arr_file[v];
                    if(armLibs) {
                        goto label_14;
                    }
                }
                else {
                label_14:
                    if(!armLibs) {
                        return 3;
                    }
                    String apk = applicationInfo0.sourceDir;
                    if(apk == null) {
                        return 4;
                    }
                    ZipFile zipFile0 = new ZipFile(apk);
                    Enumeration enumeration0 = zipFile0.entries();
                    boolean x86Libs = false;
                    while(enumeration0.hasMoreElements()) {
                        ZipEntry ze = (ZipEntry)enumeration0.nextElement();
                        if(ze.getName().contains("lib/x86/lib")) {
                            x86Libs = true;
                            Log.d(("doCheckLibs: " + ze.getName() + " is x86 lib"));
                            break;
                        }
                        if(false) {
                            break;
                        }
                    }
                    zipFile0.close();
                    if(!x86Libs) {
                        return 5;
                    }
                    AlertDialog.Builder alertDialog$Builder0 = Alert.create().setCustomTitle(Tools.getCustomTitle(Tools.stripColon(0x7F070255))).setMessage(Re.s(0x7F070256));  // string:check_libs "Check the architecture of the game libraries:"
                    String s2 = Re.s(0x7F070164);  // string:fix_it "Fix it"
                    GoOnForum baseActivity$GoOnForum0 = Build.VERSION.SDK_INT >= 21 ? new DialogInterface.OnClickListener() {
                        @Override  // android.content.DialogInterface$OnClickListener
                        public void onClick(DialogInterface dialog, int which) {
                            Alert.show(Alert.create().setMessage(Re.s(0x7F07033E)).setPositiveButton(Re.s(0x7F07009B), new DialogInterface.OnClickListener() {  // string:try_reinstall_x86 "Try reinstalling the game with x86 libraries?\n\nThe game 
                                                                                                                                                                // may stop working after that. If this happens, reinstall the game from the apk located 
                                                                                                                                                                // in __data_app__."
                                @Override  // android.content.DialogInterface$OnClickListener
                                public void onClick(DialogInterface dialog, int which) {
                                    Tools.showToast(0x7F07033F, 0);  // string:reinstall_started "Reinstallation started..."
                                    new ThreadEx(new Runnable() {
                                        @Override
                                        public void run() {
                                            String s;
                                            try {
                                                Process process0 = RootDetector.tryRoot(("exec pm install -r --abi x86 " + this.val$apk));
                                                if(process0 == null) {
                                                    s = Re.s(0x7F070340) + "\nnull";  // string:reinstall_failed "Reinstallation failed:"
                                                }
                                                else {
                                                    int v = process0.waitFor();
                                                    s = v == 0 ? Re.s(0x7F070341) : Re.s(0x7F070340) + "\ncode: " + v;  // string:reinstall_ended "Reinstallation completed."
                                                }
                                            }
                                            catch(InterruptedException e) {
                                                Log.d("Reinstall failed", e);
                                                s = Re.s(0x7F070340) + "\n" + e.getMessage();  // string:reinstall_failed "Reinstallation failed:"
                                            }
                                            Tools.showToast(s, 1);
                                        }
                                    }, "FixLibs").start();
                                }
                            }).setNeutralButton(Re.s(0x7F070224), new GoOnForum("kwws=22jdphjxdugldq1qhw2iruxp2jdoohu|2lpdjh278:0krz0wr0uhsodfh0dup0oleudulhv0zlwk0{;90oleudulhv0{0soruh2")).setNegativeButton(Re.s(0x7F07009C), null));  // string:site "Site"
                        }
                    } : new GoOnForum("kwws=22jdphjxdugldq1qhw2iruxp2jdoohu|2lpdjh278:0krz0wr0uhsodfh0dup0oleudulhv0zlwk0{;90oleudulhv0{0soruh2");
                    Alert.show(alertDialog$Builder0.setPositiveButton(s2, baseActivity$GoOnForum0).setNeutralButton(Re.s(0x7F0700B9), new DialogInterface.OnClickListener() {  // string:skip "Ignore"
                        @Override  // android.content.DialogInterface$OnClickListener
                        public void onClick(DialogInterface dialog, int which) {
                            Config.get(0x7F0B0092).value = 0;  // id:config_check_libs
                            Config.save();
                        }
                    }).setNegativeButton(Re.s(0x7F07009D), null));  // string:ok "OK"
                    return 6;
                }
                if(file != null && file.getName().startsWith("lib") && file.getName().endsWith(".so")) {
                    try {
                        FileInputStream is = new FileInputStream(file);
                        if(is.read(buf) > 13 && buf[0] == 0x7F && buf[1] == 69 && buf[2] == 76 && buf[3] == 70 && buf[18] == 40) {
                            armLibs = true;
                            Log.d(("doCheckLibs: " + file + " is ARM lib"));
                        }
                        is.close();
                    }
                    catch(Throwable e) {
                        Log.e(("Failed check lib " + file), e);
                    }
                }
                ++v;
            }
        }
        catch(Throwable e) {
            Log.e(("Failed check libs for " + this.processInfo), e);
            return 0;
        }
    }

    void doDebug() {
        MainDialog mainDialog = this.mainDialog;
        if(mainDialog == null) {
            Log.d("D: mainDialog == null");
            return;
        }
        View view0 = mainDialog.getWindow().peekDecorView();
        if(view0 == null) {
            Log.d("D: decor == null");
            return;
        }
        this.printView("", view0.getRootView());
    }

    public void doTrimMemory(int level) {
        if(this.mDaemonManager != null && level >= 80) {
            this.mDaemonManager.doOOM();
        }
    }

    public void executeScript(String s, int params, String out) {
        this.interruptScript();
        Script script = s.length() < 2 || s.charAt(0) != 0x2F || !new File(s).isFile() ? new Script(s, params, out) : new Script(new File(s), params, out);
        this.currentScript = script;
        script.start();
    }

    void eye(boolean show) {
        if(this.mainDialog != null) {
            return;
        }
        if(show) {
            this.showApp.show();
            return;
        }
        this.showApp.hide(true);
    }

    Object getCheckList() {
        if(this.selectedTab == this.mSearchPage) {
            return this.mAddressList;
        }
        if(this.selectedTab == this.mSavedPage) {
            return this.savedList == null ? null : this.savedList.getList();
        }
        return this.selectedTab != this.mMemEditorPage || this.mMemListAdapter == null ? null : this.mMemListAdapter.getChecks();
    }

    public static ComponentCallbacks getComponentCallbacks() {
        ComponentCallbacks ret = MainService.cc;
        if(ret == null) {
            ret = Build.VERSION.SDK_INT >= 14 ? new ComponentCallbacks2() {
                @Override  // android.content.ComponentCallbacks
                public void onConfigurationChanged(Configuration newConfig) {
                    Log.d(("ComponentCallbacks2 onConfigurationChanged: " + newConfig));
                    MainService.onConfigurationChanged(ContextWrapper.onConfigurationChanged(newConfig));
                }

                @Override  // android.content.ComponentCallbacks
                public void onLowMemory() {
                    Log.d("ComponentCallbacks2 onLowMemory");
                    MainService.onTrimMemory(-1);
                }

                @Override  // android.content.ComponentCallbacks2
                public void onTrimMemory(int level) {
                    Log.d(("ComponentCallbacks2 onTrimMemory: " + level));
                    MainService.onTrimMemory(level);
                }
            } : new ComponentCallbacks() {
                @Override  // android.content.ComponentCallbacks
                public void onConfigurationChanged(Configuration newConfig) {
                    Log.d(("ComponentCallbacks onConfigurationChanged: " + newConfig));
                    MainService.onConfigurationChanged(ContextWrapper.onConfigurationChanged(newConfig));
                }

                @Override  // android.content.ComponentCallbacks
                public void onLowMemory() {
                    Log.d("ComponentCallbacks onLowMemory");
                    MainService.onTrimMemory(-1);
                }
            };
            MainService.cc = ret;
        }
        return ret;
    }

    public long getContentInAddress(long address, int flags) {
        this.lockApp();
        return this.mDaemonManager.getMemoryContent(address, flags);
    }

    FloatPanel getFloatPanel(String perfName, int defX, int defY, int buttonId, int buttonResId) {
        FloatPanel floatPanel0 = new FloatPanel(MainService.context) {
            @Override  // android.ext.FloatPanel
            protected int getDefX() {
                return defX;
            }

            @Override  // android.ext.FloatPanel
            protected int getDefY() {
                return defY;
            }

            @Override  // android.ext.FloatPanel
            protected String getPrefName() {
                return perfName;
            }

            @Override  // android.ext.FloatPanel
            protected boolean isInternal() {
                return true;
            }
        };
        android.fix.ImageButton imageButton0 = new android.fix.ImageButton(MainService.context);
        imageButton0.setImageResource(buttonResId);
        imageButton0.setKeepScreenOn(true);
        imageButton0.setId(buttonId);
        imageButton0.setOnClickListener(MainService.instance);
        floatPanel0.addView(imageButton0);
        return floatPanel0;
    }

    static int getLastFlags() {
        return MainService.instance.mResultCount == 0L ? 0x7F : MainService.lastFlags;
    }

    static int getLastType() {
        return MainService.instance.mResultCount != 0L && (MainService.lastFlags & MainService.lastType) != MainService.lastType ? MainService.lastFlags : MainService.lastType;
    }

    public Notification getNotification(boolean withIcon, boolean withFix) {
        int v;
        PendingIntent pendingIntent0 = UiToggler.getPendingIntent(0);
        if(!SystemConstants.useFloatWindows) {
            v = this.mainDialog == null || BaseActivity.instance == null || !BaseActivity.instance.isOnTop() ? 0x7F070207 : 0x7F070208;  // string:show_gg "Show __app_name__"
        }
        else if(this.mainDialog != null) {
            v = 0x7F070208;  // string:hide_gg "Hide __app_name__"
        }
        else {
            v = 0x7F070207;  // string:show_gg "Show __app_name__"
        }
        String s = Re.s(v);
        int icon = Build.VERSION.SDK_INT >= 21 ? 0x7F020039 : 0x7F02002C;  // drawable:ic_notify
        Notification notification = null;
        if(Build.VERSION.SDK_INT >= 11) {
            try {
                Context ctx = MainService.context.getApplicationContext();
                if(withFix) {
                    ctx = Config.fixContext(ctx);
                }
                Notification.Builder notification$Builder0 = new Notification.Builder(ctx).setContentTitle(s).setContentText("").setContentIntent(pendingIntent0).setAutoCancel(false).setOngoing(true);
                if(withIcon) {
                    notification$Builder0.setSmallIcon(icon);
                    if(Build.VERSION.SDK_INT >= 16) {
                        int aIcon = Build.VERSION.SDK_INT < 21 ? 0x7F020013 : 0x7F020011;  // drawable:ic_cellphone_screenshot_white_32dp
                        String s1 = Re.s(0x7F070318);  // string:screenshot_action "Screenshot"
                        PendingIntent pendingIntent1 = UiToggler.getPendingIntent(1);
                        if(Build.VERSION.SDK_INT >= 20) {
                            notification$Builder0.addAction((Build.VERSION.SDK_INT < 23 ? new Notification.Action.Builder(aIcon, s1, pendingIntent1) : new Notification.Action.Builder(Icon.createWithResource(Tools.getPackageName(), aIcon), s1, pendingIntent1)).build());
                        }
                        else {
                            notification$Builder0.addAction(aIcon, s1, pendingIntent1);
                        }
                    }
                }
                notification = Build.VERSION.SDK_INT < 16 ? notification$Builder0.getNotification() : notification$Builder0.build();
            }
            catch(Throwable e) {
                Log.badImplementation(e);
            }
        }
        if(notification == null) {
            notification = new Notification();
            if(withIcon) {
                notification.icon = icon;
            }
            try {
                notification.tickerText = s;
                notification.contentIntent = pendingIntent0;
                Notification.class.getMethod("setLatestEventInfo", Context.class, CharSequence.class, CharSequence.class, PendingIntent.class).invoke(notification, MainService.context.getApplicationContext(), s, "", pendingIntent0);
            }
            catch(Throwable e) {
                Log.w("Method not found", e);
            }
        }
        notification.flags = 34;
        return notification;
    }

    public double getSpeed() {
        return this.mDaemonManager.getSpeed();
    }

    public int getTabIndex() {
        return this.mMainScreen.getCurrentTab();
    }

    @SuppressLint({"SdCardPath"})
    @TargetApi(19)
    String getTempPatches() {
        ArrayList list = new ArrayList();
        SharedPreferences sharedPreferences0 = Tools.getSharedPreferences();
        String s = sharedPreferences0.getString("temp-path", null);
        if(s != null) {
            if("/bypass".equals(s)) {
                DaemonManager.bypassDaemonFail = true;
            }
            if(s.length() >= 2 && s.charAt(0) == 0x2F) {
                MainService.addUnique(list, s);
            }
            else {
                sharedPreferences0.edit().remove("temp-path").commit();
                Log.e(("Removed bad path: " + s));
            }
        }
        try {
            MainService.addUnique(list, MainService.context.getExternalFilesDir(null));
        }
        catch(Throwable e) {
            Log.e("Fail get path", e);
        }
        if(Build.VERSION.SDK_INT >= 19) {
            try {
                File[] arr_file = MainService.context.getExternalFilesDirs(null);
            label_19:
                for(int v1 = 0; v1 < arr_file.length; ++v1) {
                    MainService.addUnique(list, arr_file[v1]);
                }
            }
            catch(Throwable e) {
                Log.e("Fail get path", e);
                if(true) {
                    goto label_26;
                }
                goto label_19;
            }
        }
        try {
        label_26:
            MainService.addUnique(list, MainService.context.getExternalCacheDir());
        }
        catch(Throwable e) {
            Log.e("Fail get path", e);
        }
        if(Build.VERSION.SDK_INT >= 19) {
            try {
                File[] arr_file1 = MainService.context.getExternalCacheDirs();
            label_32:
                for(int v = 0; v < arr_file1.length; ++v) {
                    MainService.addUnique(list, arr_file1[v]);
                }
            }
            catch(Throwable e) {
                Log.e("Fail get path", e);
                if(true) {
                    goto label_39;
                }
                goto label_32;
            }
        }
        try {
        label_39:
            MainService.addUnique(list, Environment.getExternalStorageDirectory());
        }
        catch(Throwable e) {
            Log.e("Fail get path", e);
        }
        MainService.addUnique(list, "/sdcard");
        try {
            MainService.addUnique(list, Tools.getFilesDir());
        }
        catch(Throwable e) {
            Log.e("Fail get path", e);
        }
        try {
            MainService.addUnique(list, Tools.getCacheDir());
        }
        catch(Throwable e) {
            Log.e("Fail get path", e);
        }
        MainService.addUnique(list, "/data");
        StringBuilder patches = new StringBuilder();
        for(Object object0: list) {
            String path = (String)object0;
            if(path != null) {
                String path = path.trim();
                if(!"".equals(path)) {
                    if(patches.length() > 0) {
                        patches.append('|');
                    }
                    patches.append(path);
                }
            }
        }
        return patches.toString();
    }

    public void goToAddress(long addr) {
        this.goToAddress(addr, 0);
    }

    public void goToAddress(long addr, int top) {
        this.mMemListAdapter.setBaseAddress(addr, top);
        if(this.selectedTab == this.mMemEditorPage) {
            Tools.click(this.mListRefreshButton);
            return;
        }
        this.mMainScreen.setCurrentTab(3);
    }

    public void goToAddress(Long source, String address, CharSequence description) {
        History.add(address, 1);
        long addr = 0L;
        int start = 0;
        int v2 = address.length();
    label_4:
        while(start < v2) {
            int v3 = address.indexOf(45, start + 1);
            int v4 = address.indexOf(43, start + 1);
            int end = v4 != -1 && (v3 == -1 || v3 >= v4) ? v4 : v3;
            if(end == -1) {
                end = v2;
            }
            String part = address.substring(start, end).trim();
            start = end;
            int v6 = part.length();
            if(v6 > 0) {
                for(Object object0: RegionList.getList()) {
                    Region region = (Region)object0;
                    if(region.name != null && region.name.length() >= v6 && region.name.endsWith(part) && (region.name.length() == part.length() || region.name.charAt(region.name.length() - part.length() - 1) == 0x2F)) {
                        addr += region.start;
                        continue label_4;
                    }
                    if(false) {
                        break;
                    }
                }
            }
            if(part.endsWith("h")) {
                part = part.substring(0, part.length() - 1);
            }
            try {
                addr += ParserNumbers.parseBigLong(part, 16);
            }
            catch(Throwable e) {
                Log.d("Failed parse", e);
            }
        }
        this.saveCurrentPosMemEditor(source, addr);
        this.memoryHistory.add(addr, description);
        this.goToAddress(addr);
    }

    private void initDebug() {
    }

    private void initTab(String name, int contentId, int iconId) {
        TabHost.TabSpec tabHost$TabSpec0 = this.mMainScreen.newTabSpec(name);
        tabHost$TabSpec0.setContent(contentId);
        ImageView imageView0 = Config.setIconSize(((ImageView)this.mMainScreen.findViewById(iconId)));
        if(Build.VERSION.SDK_INT >= 21) {
            tabHost$TabSpec0.setIndicator("", imageView0.getDrawable());
        }
        else {
            try {
                tabHost$TabSpec0.setIndicator(LayoutInflater.inflateStatic(0x7F040027, this.mMainScreen.getTabWidget(), false));  // layout:tab_indicator_holo
            }
            catch(NoSuchMethodError e) {
                Log.badImplementation(e);
                tabHost$TabSpec0.setIndicator("", imageView0.getDrawable());
            }
        }
        try {
            this.mMainScreen.addTab(tabHost$TabSpec0);
        }
        catch(IllegalArgumentException e) {
            Log.badImplementation(e);
            tabHost$TabSpec0.setIndicator("");
            try {
                this.mMainScreen.addTab(tabHost$TabSpec0);
            }
            catch(IllegalArgumentException e) {
                Log.badImplementation(e);
                tabHost$TabSpec0.setIndicator("", imageView0.getDrawable());
                this.mMainScreen.addTab(tabHost$TabSpec0);
            }
        }
    }

    // 检测为 Lambda 实现
    public void interruptRecord() [...]

    // 检测为 Lambda 实现
    public void interruptScript() [...]

    boolean isScreenOn() {
        try {
            Context context0 = Tools.getContext();
            if(Build.VERSION.SDK_INT >= 20) {
                boolean screenOn = false;
                Display[] arr_display = ((DisplayManager)context0.getSystemService("display")).getDisplays();
                for(int v = 0; true; ++v) {
                    if(v >= arr_display.length) {
                        return screenOn;
                    }
                    if(arr_display[v].getState() != 1) {
                        screenOn = true;
                    }
                }
            }
            return ((PowerManager)context0.getSystemService("power")).isScreenOn();
        }
        catch(Throwable e) {
            Log.w("isScreenOn fail", e);
            return true;
        }
    }

    void lockApp() {
        this.lockApp(0);
    }

    void lockApp(byte seq) {
        if(this.mHasNotLocked) {
            this.detectApp(false);
            ProcessInfo processInfo = this.processInfo;
            if(processInfo != null) {
                this.mDaemonManager.setPid(seq, processInfo.pid, processInfo.packageName, processInfo.libsPath);
                this.mHasNotLocked = false;
                this.mSmallImage.setVisibility(0);
                if(!this.statusBar.getText().toString().startsWith(Re.s(0x7F070000) + ' ' + this.mDaemonManager.getStatus())) {  // string:version_number "96.0"
                    ThreadManager.getHandlerUiThread().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                MainService.this.mDaemonManager.signal(2);
                            }
                            catch(Throwable unused_ex) {
                            }
                        }
                    }, ((long)(Tools.RANDOM.nextInt(300000) + 180000)));
                }
            }
        }
    }

    private void logUsed(int[] info) {
        if(info != null && info.length == 2) {
            int type = info[0];
            UsedList[] usedLists = this.usedLists;
            if(type >= 0 && type < 4) {
                UsedList usedList = usedLists[type];
                if(usedList == null) {
                    usedList = new UsedList(type);
                    usedLists[type] = usedList;
                }
                usedList.add(info[1]);
            }
        }
    }

    public void nextSpeed() {
        Log.d(">>");
        this.listSpeed.toNext();
        this.updateSpeed(true);
    }

    void notifyCheckList() {
        if(this.selectedTab == this.mSearchPage) {
            this.notifyDataSetChanged(this.mAddrListView);
            return;
        }
        if(this.selectedTab == this.mSavedPage) {
            this.notifyDataSetChanged(this.mSavedListView);
            return;
        }
        if(this.selectedTab == this.mMemEditorPage) {
            this.notifyDataSetChanged(this.mMemListView);
        }
    }

    void notifyDataSetChanged(ListAdapter listAdapter) {
        if(listAdapter == null) {
            return;
        }
        ThreadManager.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(listAdapter instanceof ConfigListAdapter) {
                    ((ConfigListAdapter)listAdapter).updateData();
                }
                if(listAdapter instanceof BaseAdapter) {
                    try {
                        ((BaseAdapter)listAdapter).notifyDataSetChanged();
                    }
                    catch(NoSuchMethodError e) {
                        Log.badImplementation(e);
                    }
                }
            }
        });
    }

    void notifyDataSetChanged(ListView view) {
        if(view == null) {
            return;
        }
        this.notifyDataSetChanged(view.getAdapter());
    }

    public void notifyScript(byte seq, String reason) {
        if(seq == 0) {
            Log.d(("notifyScript client: " + reason));
            return;
        }
        Script script = this.currentScript;
        if(script != null) {
            Log.d(("notifyScript: " + ((int)seq) + "; " + reason));
            script.callNotify(seq, reason);
            return;
        }
        Log.d(("notifyScript no script: " + ((int)seq) + "; " + reason));
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override  // android.view.View$OnClickListener
    public void onClick(View v) {
        String msg;
        if(v == this.mAppImage || v == this.mSmallImage || v == this.mAppName) {
            this.detectApp(true);
            return;
        }
        if(v == this.mListRefreshButton) {
            this.refreshTab();
            return;
        }
        if(v == this.mBackButton) {
            this.dismissDialog();
            return;
        }
        if(v == this.mProcessPauseButton) {
            this.processToggle(true);
            return;
        }
        if(v == this.statusBar) {
            Config.get(0x7F0B0081).change();  // id:config_ranges
            return;
        }
        if(v != null) {
            switch((v.getTag() instanceof Integer ? ((int)(((Integer)v.getTag()))) : v.getId())) {
                case 0x7F020051: {  // drawable:ic_ui_button_24dp
                    this.scriptUiButtonClicked = true;
                    return;
                }
                case 0x7F070216: {  // string:hide "Hide"
                    this.dismissDialog();
                    return;
                }
                case 0x7F07021A: {  // string:interrupt "Interrupt"
                    Script script = this.currentScript;
                    if(script == null) {
                        msg = "??";
                    }
                    else {
                        Object source = script.source;
                        if(source == null) {
                            msg = "?";
                        }
                        else {
                            msg = source.toString();
                            if(msg.length() > 0xFA) {
                                msg = msg.substring(0, 0xFA) + "...";
                            }
                        }
                    }
                    Alert.show(Alert.create().setMessage(msg).setPositiveButton(Re.s(0x7F07021A), (/* 缺少LAMBDA参数 */, /* 缺少LAMBDA参数 */) -> {
                        Script script = MainService.this.currentScript;
                        if(script != null) {
                            script.interrupt();
                        }
                    }).setNegativeButton(Re.s(0x7F07009C), null));  // string:no "No"
                    return;
                }
                case 0x7F0702E5: {  // string:record "Record"
                    String msg = this.currentRecord == null ? "?" : this.currentRecord.getFile();
                    Alert.show(Alert.create().setCustomTitle(Tools.getCustomTitle(Tools.stripColon(0x7F0702E4))).setMessage(msg).setPositiveButton(Re.s(0x7F0702E8), (/* 缺少LAMBDA参数 */, /* 缺少LAMBDA参数 */) -> {
                        Record record = MainService.this.currentRecord;
                        if(record != null) {
                            record.stop();
                        }
                    }).setNegativeButton(Re.s(0x7F07009C), null));  // string:no "No"
                    return;
                }
                case 0x7F0B0020: {  // id:hot_frame
                    this.onHotKeyDetected();
                    return;
                }
                case 0x7F0B0034: {  // id:abort_button
                    this.mDaemonManager.cancel();
                    this.dismissBusyDialog();
                    return;
                }
                case 0x7F0B00E2: {  // id:more_btn
                    Tools.click(this.mMenuButton);
                }
            }
        }

        class android.ext.MainService.24 implements DialogInterface.OnClickListener {
            @Override  // android.content.DialogInterface$OnClickListener
            public void onClick(DialogInterface d, int w) {
                MainService.this.interruptScript();
            }
        }


        class android.ext.MainService.25 implements DialogInterface.OnClickListener {
            @Override  // android.content.DialogInterface$OnClickListener
            public void onClick(DialogInterface d, int w) {
                MainService.this.interruptRecord();
            }
        }

    }

    public static void onConfigurationChanged(Configuration newConfig) {
        Log.d(("Main onConfigurationChanged: " + newConfig));
        if(MainService.onConfigurationChangedSemaphore) {
            return;
        }
        try {
            MainService.onConfigurationChangedSemaphore = true;
            Tools.updateOrientation(newConfig);
            if(!AppLocale.getLocale().equals(Locale.getDefault().toString())) {
                AppLocale.loadLocale();
            }
            if(MainService.instance != null && MainService.instance.hotPoint != null) {
                MainService.instance.hotPoint.updateLayout();
            }
            if(MainService.created) {
                MainService.instance.updateToolbars();
            }
        }
        finally {
            MainService.onConfigurationChangedSemaphore = false;
        }
    }

    @SuppressLint({"WrongViewCast"})
    public void onCreate() {
        int id;
        Enumeration enumeration0;
        ZipFile zipFile0;
        Log.d("Service onCreate");
        this.timersEditor = new TimersEditor("TE", "timers", 44, MainService.DEFAULT_TIMERS, 0);
        this.randEditor = new TimersEditor("RE", "rand", 19, MainService.DEFAULT_RAND, 1);
        TabHost tabHost0 = (TabHost)LayoutInflater.inflateStatic(0x7F04001D, null);  // layout:service_dialog
        this.mMainScreen = tabHost0;
        tabHost0.setFocusableInTouchMode(true);
        for(int v = 0; v < 1; ++v) {
            TextView text = (TextView)tabHost0.findViewById(new int[]{0x7F0B00EE}[v]);  // id:empty_list_text
            text.setText(Re.s(text.getText().toString().trim()));
        }
        try {
            zipFile0 = null;
            zipFile0 = new ZipFile(MainService.context.getPackageResourcePath());
            new String("res/raw/chunk");
            enumeration0 = zipFile0.entries();
            this.mInfoRow = tabHost0.findViewById(0x7F0B00D4);  // id:info_row
        }
        catch(Throwable unused_ex) {
            goto label_44;
        }
        try {
            while(enumeration0.hasMoreElements()) {
                ZipEntry ze = (ZipEntry)enumeration0.nextElement();
                for(int i = 0; i < 1; ++i) {
                    if(this.mBackButton == null) {
                        this.mBackButton = (ImageView)tabHost0.findViewById(0x7F0B00D3);  // id:back_btn
                    }
                    if(this.mAppImage == null) {
                        this.mAppImage = (ImageView)tabHost0.findViewById(0x7F0B00C6);  // id:app_icon
                    }
                    if(this.mSmallImage == null) {
                        this.mSmallImage = (ImageView)tabHost0.findViewById(0x7F0B00C7);  // id:small_icon
                    }
                    if(this.mFoundCount == null) {
                        this.mFoundCount = (TextView)tabHost0.findViewById(0x7F0B0079);  // id:found_count
                    }
                }
            }
            this.mWindowManager = (WindowManager)this.getSystemService("window");
            this.mAppName = (TextView)tabHost0.findViewById(0x7F0B00D6);  // id:app_name
            this.mAddrListView = (ListView)tabHost0.findViewById(0x7F0B00E4);  // id:addr_list
            this.mAddrListView.setEmptyView(tabHost0.findViewById(0x7F0B00E5));  // id:nothing_found_view
            goto label_39;
        }
        catch(Throwable unused_ex) {
            try {
                this.mAddrListView = (ListView)tabHost0.findViewById(0x7F0B0074);  // id:addr_item_type
                this.mFoundCount = (TextView)tabHost0.findViewById(0x7F0B00AE);  // id:config_acceleration
            label_39:
                ZipEntry ze = (ZipEntry)enumeration0.nextElement();
                this.mAppImage = (ImageView)tabHost0.findViewById(0x7F0B011C);  // id:count_fuzzy_equal_seekbar
                this.mBackButton = (ImageView)tabHost0.findViewById(0x7F0B009F);  // id:config_keyboard
                this.mFoundCount = (TextView)tabHost0.findViewById(0x7F0B0098);  // id:config_path
                goto label_50;
            }
            catch(Throwable unused_ex) {
            }
        }
    label_44:
        this.mNothingFoundText = (TextView)tabHost0.findViewById(0x7F0B00E6);  // id:nothing_found_text
        this.toolbars[0] = (WrapLayout)tabHost0.findViewById(0x7F0B00DF);  // id:search_toolbar
        this.toolbars[1] = (WrapLayout)tabHost0.findViewById(0x7F0B00E0);  // id:saved_toolbar
        this.toolbars[2] = (WrapLayout)tabHost0.findViewById(0x7F0B00E1);  // id:memory_toolbar
        this.toolbarRow = tabHost0.findViewById(0x7F0B00DE);  // id:toolbar_row
        this.mMoreButton = (ImageView)tabHost0.findViewById(0x7F0B00E2);  // id:more_btn
    label_50:
        if(zipFile0 != null) {
            try {
                zipFile0.close();
            }
            catch(Throwable e) {
                Log.w("Failed close apk", e);
            }
        }
        this.mFilter = (TextView)tabHost0.findViewById(0x7F0B0041);  // id:filter
        this.mValueFormat = (TextView)tabHost0.findViewById(0x7F0B00D8);  // id:value_format
        this.mInfoMem = tabHost0.findViewById(0x7F0B00D7);  // id:info_mem
        this.mSavedFormat = (TextView)tabHost0.findViewById(0x7F0B00DB);  // id:saved_format
        this.mCountWait = tabHost0.findViewById(0x7F0B00DA);  // id:count_wait
        this.mInfoCnt = tabHost0.findViewById(0x7F0B00D9);  // id:info_cnt
        Config.load();
        this.updateNotification();
        this.mSearchPage = tabHost0.findViewById(0x7F0B00E3);  // id:search_page
        this.mSavedPage = tabHost0.findViewById(0x7F0B00EB);  // id:saved_page
        this.mMemEditorPage = tabHost0.findViewById(0x7F0B00E7);  // id:memory_editor_page
        this.mConfigPage = tabHost0.findViewById(0x7F0B00E9);  // id:config_page
        this.searchCounter = (TextView)tabHost0.findViewById(0x7F0B00CC);  // id:search_tab_counter
        this.savedCounter = (TextView)tabHost0.findViewById(0x7F0B00CF);  // id:saved_tab_counter
        tabHost0.setup();
        this.initTab("settings", 0x7F0B00E9, 0x7F0B00C9);  // id:config_page
        this.initTab("search", 0x7F0B00E3, 0x7F0B00CB);  // id:search_page
        this.initTab("saved", 0x7F0B00EB, 0x7F0B00CE);  // id:saved_page
        this.initTab("memory", 0x7F0B00E7, 0x7F0B00D1);  // id:memory_editor_page
        TabWidget tabWidget0 = tabHost0.getTabWidget();
        int i = 0;
        while(i < tabWidget0.getTabCount()) {
            View view = tabWidget0.getChildTabViewAt(i);
            switch(i) {
                case 0: {
                    id = 0x7F0B00C8;  // id:config_tab
                    break;
                }
                case 1: {
                    id = 0x7F0B00CA;  // id:search_tab
                    break;
                }
                case 2: {
                    id = 0x7F0B00CD;  // id:saved_tab
                    break;
                }
                case 3: {
                    id = 0x7F0B00D0;  // id:memory_tab
                    break;
                }
                default: {
                    view = null;
                    id = 0;
                }
            }
            if(view instanceof ViewGroup) {
                ViewGroup tab = (ViewGroup)view;
                int j = 0;
                while(j < tab.getChildCount()) {
                    try {
                        View view1 = tab.getChildAt(j);
                        if(view1 != null) {
                            view1.setVisibility(8);
                        }
                        ++j;
                        continue;
                    }
                    catch(ArrayIndexOutOfBoundsException unused_ex) {
                    }
                    ++j;
                }
                if(tab.getLayoutParams() instanceof LinearLayout.LayoutParams) {
                    tab.setLayoutParams(new LinearLayout.LayoutParams(0, -2, 1.0f));
                }
                tab.setMinimumWidth(0);
                tab.setMinimumHeight(0);
                tab.setPadding(0, 0, 0, 0);
                View view2 = tabHost0.findViewById(id);
                tab.addView(Tools.getViewForAttach(view2));
                ViewGroup.LayoutParams l = view2.getLayoutParams();
                if(l instanceof LinearLayout.LayoutParams) {
                    ((LinearLayout.LayoutParams)l).gravity = 17;
                }
                if(l instanceof RelativeLayout.LayoutParams) {
                    ((RelativeLayout.LayoutParams)l).addRule(13, -1);
                }
                view2.setLayoutParams(l);
            }
            ++i;
        }
        tabWidget0.setMinimumWidth(0);
        tabWidget0.setMinimumHeight(0);
        if(Build.VERSION.SDK_INT < 11) {
            ViewGroup.LayoutParams viewGroup$LayoutParams1 = tabWidget0.getLayoutParams();
            if(viewGroup$LayoutParams1 instanceof LinearLayout.LayoutParams) {
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)tabWidget0.getLayoutParams();
                lp.height = Tools.dp2px48();
                tabWidget0.setLayoutParams(viewGroup$LayoutParams1);
            }
        }
        this.mMenuButton = tabHost0.findViewById(0x7F0B00DC);  // id:menu_btn
        this.mMenuButtonLand = (ImageView)tabHost0.findViewById(0x7F0B00D2);  // id:menu_btn_land
        this.mSavedListView = (ListView)tabHost0.findViewById(0x7F0B00EC);  // id:saved_list
        this.mSavedListView.setEmptyView(tabHost0.findViewById(0x7F0B00ED));  // id:empty_list_view
        this.mMemListView = (ListView)tabHost0.findViewById(0x7F0B00E8);  // id:mem_listview
        this.mListRefreshButton = (ImageButton)tabHost0.findViewById(0x7F0B00DD);  // id:list_refresh
        this.mProcessPauseButton = (ImageButton)tabHost0.findViewById(0x7F0B00D5);  // id:process_pause
        this.mConfigListView = (ListView)tabHost0.findViewById(0x7F0B00EA);  // id:config_list
        this.statusBar = (TextView)tabHost0.findViewById(0x7F0B00EF);  // id:status_bar
        this.mHasNotLocked = true;
        this.mSmallImage.setVisibility(4);
        tabHost0.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override  // android.widget.TabHost$OnTabChangeListener
            public void onTabChanged(String tabStringId) {
                int v = 8;
                boolean z = false;
                MainService service = MainService.instance;
                int v1 = service.getTabIndex();
                service.mInfoRow.setVisibility((v1 < 1 ? 8 : 0));
                service.mAppName.setVisibility((v1 == 3 ? 8 : 0));
                service.mInfoCnt.setVisibility((v1 == 1 ? 0 : 8));
                service.mInfoMem.setVisibility((v1 == 3 ? 0 : 8));
                TextView textView0 = service.mSavedFormat;
                if(v1 == 2) {
                    v = 0;
                }
                textView0.setVisibility(v);
                service.updateToolbars();
                service.selectedTab = service.mMainScreen.getCurrentView();
                Tools.click(service.mListRefreshButton);
                service.updateStatusBar();
                ConfigListAdapter adapter = MainService.this.mConfigListAdapter;
                if(adapter != null) {
                    if(v1 == 0) {
                        z = true;
                    }
                    adapter.load(z);
                }
            }
        });
        this.filterListener = new FilterButtonListener(this);
        this.mInfoCnt.setOnClickListener(this.filterListener);
        this.mInfoCnt.setTag(this.filterListener);
        this.valueFormat = new MenuItem(0x7F070133, 0x7F02003F) {  // string:value_format "Value format"
            @Override  // android.ext.MenuItem
            public void onClick(View v) {
                MainService.this.mMemListAdapter.chooseValueFormat();
            }
        };
        this.mValueFormat.setOnClickListener(this.valueFormat);
        this.mSavedFormat.setOnClickListener(this.valueFormat);
        this.statusBar.setOnClickListener(this);
        this.showApp = (ShowApp)LayoutInflater.inflateStatic(0x7F040026, null);  // layout:show_app
        this.mProcessPauseButton.setOnClickListener(this);
        tabHost0.setCurrentTab(1);
        this.mBackButton.setOnClickListener(this);
        ActivityManager am = null;
        try {
            am = (ActivityManager)this.getSystemService("activity");
        }
        catch(Throwable e) {
            Log.e("Failed get service activity", e);
        }
        this.mAppDetector = new AppDetector(am, Tools.getPackageManager());
        this.mDaemonManager.start();
        this.mDaemonManager.getAppList();
        this.hotPoint = HotPoint.getInstance();
        this.listSpeed = new ListSpeed();
        Converter.init(this);
        Config.setIconSize(this.mMenuButtonLand);
        Config.setIconSize(this.mBackButton);
        Config.setIconSize(this.mMoreButton);
        this.mMoreButton.setOnClickListener(this);
        MenuItem[][] listeners = MainService.menuItems;
        this.setupSearchPage(listeners);
        this.setupSavedPage(listeners);
        this.setupMemEditorPage(listeners);
        this.setupConfigPage();
        for(int j = 0; j < this.toolbars.length; ++j) {
            WrapLayout toolbar = this.toolbars[j];
            toolbar.removeAllViews();
            for(int i = 0; i < listeners[j].length; ++i) {
                MenuItem item = listeners[j][i];
                item.setIndex(j, i);
                toolbar.addView(item.getButton(true));
            }
        }
        this.mListRefreshButton.setOnClickListener(this);
        android.ext.MainService.20 mainService$200 = new MenuItem("tmp") {
            @Override  // android.ext.MenuItem
            public void onClick(View v) {
            }
        };
        this.mMenuButton.setTag(mainService$200);
        this.mMenuButtonLand.setTag(mainService$200);
        android.ext.MainService.21 mainService$210 = new View.OnClickListener() {
            private ListAdapter[] adapter;

            {
                MenuItem[][] arr2_menuItem = listeners;  // 捕获的参数 （可能与外部方法变量命名冲突；考虑手动重命名）
                this.adapter = new ListAdapter[arr2_menuItem.length];
            }

            @Override  // android.view.View$OnClickListener
            public void onClick(View v) {
                int index;
                switch(MainService.this.selectedTab.getId()) {
                    case 0x7F0B00E3: {  // id:search_page
                        index = 0;
                        break;
                    }
                    case 0x7F0B00E7: {  // id:memory_editor_page
                        index = 2;
                        break;
                    }
                    case 0x7F0B00EB: {  // id:saved_page
                        index = 1;
                        break;
                    }
                    default: {
                        return;
                    }
                }
                if(this.adapter[index] == null) {
                    ListAdapter[] arr_listAdapter = this.adapter;
                    arr_listAdapter[index] = new ArrayAdapter(MainService.context, listeners[index]) {
                        @Override  // android.ext.ArrayAdapter
                        public View getView(int position, View convertView, ViewGroup parent) {
                            View view1 = super.getView(position, convertView, parent);
                            TextView tv = (TextView)view1.findViewById(0x1020014);
                            if(tv != null) {
                                Tools.setTextAppearance(tv, 0x7F090002);  // style:SmallText
                            }
                            return view1;
                        }
                    };
                }
                Alert.show(Alert.create(Tools.getDarkContext()).setAdapter(this.adapter[index], new DialogInterface.OnClickListener() {
                    @Override  // android.content.DialogInterface$OnClickListener
                    public void onClick(DialogInterface dialog, int which) {
                        this.val$listeners[index][which].onClickFromUI(MainService.this.mMenuButton);
                        Tools.dismiss(dialog);
                    }
                }));
            }
        };
        this.mMenuButton.setOnClickListener(mainService$210);
        this.mMenuButtonLand.setOnClickListener(mainService$210);
        this.mAppImage.setOnClickListener(this);
        this.mSmallImage.setOnClickListener(this);
        this.mAppName.setOnClickListener(this);
        this.registerCallback();
        SharedPreferences sharedPreferences0 = Tools.getSharedPreferences();
        if(sharedPreferences0.contains("mem-fail")) {
            Config.get(0x7F0B0094).value = 0;  // id:config_memory_access
            Config.save();
            sharedPreferences0.edit().remove("mem-fail").commit();
        }
        this.mDaemonManager.setPath(this.getTempPatches());
        android.ext.MainService.22 mainService$220 = new DataSetObserver() {
            @Override  // android.database.DataSetObserver
            public void onChanged() {
                super.onChanged();
                MainService.this.updateCounters();
            }

            @Override  // android.database.DataSetObserver
            public void onInvalidated() {
                super.onInvalidated();
                MainService.this.updateCounters();
            }
        };
        this.mAddrListView.getAdapter().registerDataSetObserver(mainService$220);
        this.mSavedListView.getAdapter().registerDataSetObserver(mainService$220);
        this.mMemListView.getAdapter().registerDataSetObserver(mainService$220);
        this.updateCounters();
        this.updateNotification();
        this.mBusyDialog = new BusyDialog();
        MainService.created = true;
        this.updateToolbars();
    }

    public void onDaemonExit() {
        this.dismissBusyDialog();
        this.stopService();
    }

    public void onDestroy() {
        Log.d("Service onDestroy");
        this.updateNotification(true);
        this.unregisterCallback();
        Main.exit();
    }

    public void onHotKeyDetected() {
        if(!MainService.created) {
            return;
        }
        this.refreshResultList(false);
        this.showDialog();
    }

    public void onKeyCode(int code) {
        if(Config.hotKey != 0 && code == Config.hotKey) {
            ThreadManager.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(MainService.this.mainDialog == null) {
                        MainService.this.onHotKeyDetected();
                    }
                }
            });
        }
    }

    // 检测为 Lambda 实现
    @Override  // android.view.View$OnLongClickListener
    public boolean onLongClick(View v) [...]

    public void onMemoryChanged() {
        this.onMemoryChanged(0);
    }

    public void onMemoryChanged(byte seq) {
        this.mDaemonManager.doWaited(seq);
        this.savedList.reloadData();
        this.refreshResultList(false);
        this.notifyDataSetChanged(this.mMemListView);
        this.notifyDataSetChanged(this.mSavedListView);
    }

    public void onMenuPressed() {
        if(this.selectedTab == this.mSearchPage || this.selectedTab == this.mSavedPage || this.selectedTab == this.mMemEditorPage) {
            Tools.click(this.mMenuButton);
        }
    }

    public void onProgress(CharSequence message) {
        this.onProgress(message, -1L, 1L, 0, 1, 0L);
    }

    public void onProgress(CharSequence message, long progress, long progressMax, int stage, int stageMax, long found) {
        StringBuilder progressText = null;
        if(progress > 0L && progressMax != 0L) {
            progressText = new StringBuilder();
            progressText.append(Tools.stringFormat("%.2f", new Object[]{((double)(100.0 * ((double)progress) / ((double)progressMax)))}));
            progressText.append(" %");
        }
        this.mBusyDialog.onProgress(message, progress, progressMax, stage, stageMax, found, (progressText == null ? "" : progressText.toString()));
        if(progressText != null) {
            progressText.setLength(4);
            progressText.append('%');
        }
        this.hotPoint.onProgress(0, progress, progressMax, stage, stageMax, (progressText == null ? "" : progressText.toString()));
    }

    public void onRecordEnd(Record record) {
        ThreadManager.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MainService.this.recordInterrupt = null;
                MainService.this.showApp.hideRecord();
            }
        });
        if(this.currentRecord == record) {
            this.currentRecord = null;
        }
    }

    public void onReportError(int errorCode) {
        this.dismissBusyDialog();
        int message = 0x7F0700B1;  // string:reset_search "ERROR detected. Reset search list."
        String sMessage = null;
        int title = 0x7F0700B2;  // string:unknown_error "Unknown error"
        switch(errorCode) {
            case 1: {
                title = 0x7F0700B3;  // string:out_of_memory "Out of memory"
                break;
            }
            case 2: {
                title = 0x7F0700B4;  // string:bug_detected "Bug detected"
                break;
            }
            case 3: {
                title = 0x7F0700B5;  // string:disk_full "Disk full"
                message = 0x7F0702EB;  // string:fix_path "Free space or specify a different path for temporary files by clicking 
                                       // \"__fix_it__\"."
                break;
            }
            case 4: {
                title = 0x7F0700B6;  // string:io_error "IO error"
                message = 0x7F0702EB;  // string:fix_path "Free space or specify a different path for temporary files by clicking 
                                       // \"__fix_it__\"."
                break;
            }
            case 9: {
                title = 0x7F0700B6;  // string:io_error "IO error"
                sMessage = this.appendProcessInfo(Re.s(0x7F070300) + "\n\n" + Re.s(0x7F070301));  // string:maps_failed "Could not open \"__maps__\" file."
                break;
            }
            case 10: {
                title = 0x7F0700B6;  // string:io_error "IO error"
                sMessage = Re.s(0x7F070300) + "\n\n" + Re.s(0x7F070304);  // string:maps_failed "Could not open \"__maps__\" file."
                break;
            }
            case 105: {
                title = 0x7F0700C0;  // string:eperm_error "Could not attach to process (PTRACE_ATTACH): Operation not permitted"
                sMessage = Re.s(0x7F0702AD) + '\n' + 1 + ": " + Re.s(0x7F0702B0) + '\n' + 2 + ": " + Re.s(0x7F0702B1);  // string:eperm_error_descr_0 "Possible reasons:"
                break;
            }
            case 106: {
                title = 0x7F0701AD;  // string:eperm_error_defend "Could not attach to process (PTRACE_ATTACH): Game is 
                                     // protected"
                sMessage = this.appendProcessInfo(Re.s(0x7F0702AD) + '\n' + 1 + ": " + Re.s(0x7F0702AE) + '\n' + 2 + ": " + Re.s(0x7F0702AF));  // string:eperm_error_descr_0 "Possible reasons:"
                break;
            }
            case 107: {
                title = 0x7F070112;  // string:speedhack_fail "Speedhack for this process is not supported"
                message = 0x7F070112;  // string:speedhack_fail "Speedhack for this process is not supported"
                break;
            }
            case 108: {
                title = 0x7F0700AE;  // string:error "Error"
                sMessage = Tools.stripColon(0x7F07011F) + ": " + 0x20;  // string:too_many_union_numbers "Too many numbers, more than:"
            }
        }
        if(sMessage == null) {
            sMessage = Re.s(message);
        }
        String sTitle = Re.s(title);
        String s2 = "{" + errorCode + '}';
        if(this.showedErrors.get(errorCode)) {
            long v3 = System.currentTimeMillis();
            if(MainService.lastToastTime < v3) {
                MainService.lastToastTime = v3 + 2000L;
                Tools.showToast((s2 + Re.s(title)), 0);
            }
            if(errorCode <= 100) {
                this.clear(true);
            }
            return;
        }
        this.showedErrors.put(errorCode, true);
        AlertDialog.Builder alertDialog$Builder0 = Alert.create().setCustomTitle(Tools.getCustomTitle(s2)).setMessage(sTitle + "\n\n" + sMessage).setPositiveButton(Re.s(0x7F07009D), (errorCode <= 100 ? (/* 缺少LAMBDA参数 */, /* 缺少LAMBDA参数 */) -> MainService.this.clear(0, true) : null)).setCancelable(false);
        if(message == 0x7F0702EB) {  // string:fix_path "Free space or specify a different path for temporary files by clicking 
                                     // \"__fix_it__\"."
            alertDialog$Builder0.setNeutralButton(Re.s(0x7F070164), new DialogInterface.OnClickListener() {  // string:fix_it "Fix it"
                @Override  // android.content.DialogInterface$OnClickListener
                public void onClick(DialogInterface arg0, int arg1) {
                    ConfigListAdapter.changePath(null);
                }
            });
        }
        else if(title == 0x7F0701AD) {  // string:eperm_error_defend "Could not attach to process (PTRACE_ATTACH): Game is 
                                        // protected"
            alertDialog$Builder0.setNeutralButton(Re.s(0x7F07012B), new DialogInterface.OnClickListener() {  // string:help "Help"
                @Override  // android.content.DialogInterface$OnClickListener
                public void onClick(DialogInterface arg0, int arg1) {
                    MainService.this.clear(true);
                    ConfigListAdapter.showHelp(0x7F07006D);  // string:help_games_protection_ "__help_games_protection__\t__help_games_protection_9__\n\n__help_games_protection_end__\n 
                                                             //    "
                }
            });
        }
        Alert.show(alertDialog$Builder0);

        class android.ext.MainService.33 implements DialogInterface.OnClickListener {
            @Override  // android.content.DialogInterface$OnClickListener
            public void onClick(DialogInterface arg0, int arg1) {
                MainService.this.clear(true);
            }
        }

    }

    public void onResults(BufferReader reader) {
        int used;
        int used;
        int i;
        ScrollPos tools$ScrollPos0 = Tools.getScrollPos(this.mAddrListView);
        int v = reader.getSeq();
        try {
            reader.reset();
            int size = reader.size() - reader.getPosition() - 1;
            reader.readInt();
            ArrayListResults list = this.mAddressList;
            long[] prevAddr = null;
            int[] prevOrd = null;
            int used = 0;
            AddressItem item = new AddressItem();
            synchronized(list) {
                boolean force = MainService.firstGetAfterSearch;
                MainService.firstGetAfterSearch = false;
                if(!force) {
                    try {
                        int v4 = list.size();
                        prevAddr = new long[v4];
                        prevOrd = new int[v4];
                        AddressItem aItem = new AddressItem();
                        i = 0;
                        for(used = 0; true; used = used) {
                            if(i >= v4) {
                                used = used;
                                break;
                            }
                            if(list.checked(i)) {
                                list.get(i, aItem);
                                prevAddr[used] = aItem.address;
                                used = used + 1;
                                prevOrd[used] = AddressItem.getOrder(aItem.flags);
                            }
                            else {
                                used = used;
                            }
                            ++i;
                        }
                    }
                    catch(OutOfMemoryError e) {
                        prevAddr = null;
                        prevOrd = null;
                        Log.e("Failed save checked data", e);
                    }
                }
                list.clear();
                int minimumCapacity = size / (InOut.longSize + 12);
                if(v == 0 && minimumCapacity > 0) {
                    minimumCapacity = 0;
                }
                list.ensureCapacity(minimumCapacity);
                int ch = 0;
                while(size - reader.getPosition() > 10 && (v != 0 || list.size() < 0)) {
                    long v10 = reader.readLong();
                    long v11 = reader.readLongLong();
                    int v12 = reader.readInt();
                    item.address = v10;
                    item.flags = v12;
                    boolean checked = force;
                    if(prevAddr != null) {
                        int v13 = AddressItem.getOrder(v12);
                        while(ch < used) {
                            long prevAddress = prevAddr[ch];
                            if(prevAddress == v10) {
                                int ord = prevOrd[ch];
                                if(ord == v13) {
                                    checked = true;
                                    ++ch;
                                    break;
                                }
                                else if(ord <= v13) {
                                    ++ch;
                                    continue;
                                }
                                else {
                                    break;
                                }
                            }
                            if(!Tools.unsignedLess(prevAddress, v10)) {
                                break;
                            }
                            ++ch;
                        }
                    }
                    list.add(v10, v11, v12, checked);
                }
                list.trimToSize();
            }
        }
        catch(OutOfMemoryError e) {
            this.mAddressList.clear();
            this.mAddressList.trimToSize();
            Tools.showToast(0x7F0700C7);  // string:out_of_memory_error "Out of memory error. Current result list cleared. Please 
                                          // set a smaller value for the number of results to show."
            Log.w(("list.size() = " + this.mAddressList.size() + "; show count = " + 0), e);
        }
        catch(IOException e) {
            Log.e("Failed read", e);
        }
        this.notifyDataSetChanged(this.mAddrListView.getAdapter());
        this.showCounters();
        Tools.setScrollPos(this.mAddrListView, tools$ScrollPos0);
        MainService.instance.updateStatusBar();
        this.notifyScript(((byte)v), null);
    }

    public void onScriptEnd(Script script) {
        ThreadManager.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MainService.this.scriptInterrupt = null;
                MainService.this.scriptUiButton = null;
                MainService.this.showApp.hideScript();
                MainService.this.refreshResultList(true);
            }
        });
        if(this.currentScript == script) {
            this.currentScript = null;
        }
    }

    public void onScriptStart(Script script) {
        ThreadManager.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MainService.this.scriptInterrupt = MainService.this.getFloatPanel("script-interrupt", 0, 0, 0x7F07021A, 0x7F02004E);  // string:interrupt "Interrupt"
                MainService.this.scriptUiButton = null;
                MainService.this.showApp.showScript();
            }
        });
    }

    public void onSearchDone(byte seq, long count, long countAddr, int type) {
        boolean z = true;
        this.mResultCount = count;
        if(type == 0) {
            this.dismissBusyDialog();
            MainService.firstGetAfterSearch = true;
            if(seq == 0) {
                if(Config.searchHelper != 0 && count > 0L) {
                    this.dismissDialog();
                    Tools.showToast((Re.s(0x7F070178) + ' ' + Tools.stringFormat(Re.s(0x7F0700C9), new Object[]{count})));  // string:search_helper "Search helper:"
                    if(Config.searchHelper != 2) {
                        z = false;
                    }
                    MainService.searchHelperRestore = z;
                }
                else {
                    this.refreshResultList(true);
                }
            }
        }
        if(this.mResultCount == 0L) {
            this.clear(0, false, false);
        }
    }

    public void onSendCode(byte seq, int code, int[] info) {
        int msg;
        int v1 = 1;
        switch(code) {
            case 0: {
                Tools.getSharedPreferences().edit().putBoolean("mem-fail", true).commit();
                return;
            }
            case 1: {
                Tools.getSharedPreferences().edit().remove("mem-fail").commit();
                return;
            }
            case 3: {
                this.dismissBusyDialog();
                if(seq == 0) {
                    Tools.showToast(0x7F0701AB, 0);  // string:dump_ended "Dump ended"
                }
                this.notifyScript(seq, null);
                return;
            }
            case 4: {
                this.dismissBusyDialog();
                if(seq == 0) {
                    Tools.showToast(0x7F0701AC);  // string:dump_failed "Dump failed"
                }
                this.notifyScript(seq, Re.s(0x7F0701AC));  // string:dump_failed "Dump failed"
                return;
            }
            case 5: {
                if(seq == 0) {
                    Tools.showToast(0x7F0701B6, 0);  // string:copy_ended "Copy memory ended"
                }
                this.notifyScript(seq, null);
                return;
            }
            case 6: {
                if(seq == 0) {
                    Tools.showToast(0x7F0701B7);  // string:copy_failed "Copy memory failed"
                }
                this.notifyScript(seq, Re.s(0x7F0701B7));  // string:copy_failed "Copy memory failed"
                return;
            }
            case 8: {
                this.processRealPaused = true;
                return;
            }
            case 9: {
                this.processRealPaused = false;
                return;
            }
            case 10: {
                if(seq == 0) {
                    Tools.showToast(0x7F070221, 0);  // string:speedhack_loading "Loading speedhack..."
                    return;
                }
                break;
            }
            case 11: {
                if(seq == 0) {
                    String msg = Re.s(0x7F070222);  // string:speedhack_loaded "Speedhack is loaded."
                    if(this.noX86) {
                        msg = msg + "\n" + Re.s(0x7F07032F);  // string:speedhack_no_x86 "But in this game is no x86 libraries, so maybe it will 
                                                              // not work."
                    }
                    if(!this.noX86) {
                        v1 = 0;
                    }
                    Tools.showToast(msg, v1);
                }
                this.noX86 = false;
                return;
            }
            case 12: {
                if(seq == 0) {
                    Tools.showToast(0x7F070273, 0);  // string:patch_loading "Loading patch..."
                    return;
                }
                break;
            }
            case 13: {
                if(seq == 0) {
                    Tools.showToast(0x7F070274);  // string:patch_loaded "Patch is loaded."
                    Alert.show(Alert.create().setCustomTitle(Tools.getCustomTitle(0x7F070271)).setMessage(Re.s(0x7F070272)).setPositiveButton(Re.s(0x7F07009D), null));  // string:disable_protection "Disable protection for all applications (until reboot)"
                }
                this.notifyScript(seq, null);
                return;
            }
            case 14: {
                if(seq == 0) {
                    Tools.showToast(0x7F070275);  // string:patch_failed "Patch is not loaded."
                }
                this.notifyScript(seq, Re.s(0x7F070275));  // string:patch_failed "Patch is not loaded."
                return;
            }
            case 15: {
                if(seq == 0) {
                    Tools.showToast(0x7F070280, 0);  // string:unrandomizer_loading "Loading unrandomizer..."
                    return;
                }
                break;
            }
            case 16: {
                if(seq == 0) {
                    String msg = Re.s(0x7F070281);  // string:unrandomizer_loaded "Unrandomizer is loaded."
                    if(this.noX86) {
                        msg = msg + "\n" + Re.s(0x7F07032F);  // string:speedhack_no_x86 "But in this game is no x86 libraries, so maybe it will 
                                                              // not work."
                    }
                    if(!this.noX86) {
                        v1 = 0;
                    }
                    Tools.showToast(msg, v1);
                }
                this.noX86 = false;
                return;
            }
            case 17: {
                if(seq == 0) {
                    Alert.show(Alert.create().setCustomTitle(Tools.getCustomTitle(0x7F070296)).setMessage(Re.s(0x7F070297)).setPositiveButton(Re.s(0x7F070292), new DialogInterface.OnClickListener() {  // string:waitpid_mode "__waitpid__ mode:"
                        @Override  // android.content.DialogInterface$OnClickListener
                        public void onClick(DialogInterface dialog, int which) {
                            Config.get(0x7F0B0097).value = 1;  // id:config_waitpid
                            Config.save();
                            ConfigListAdapter.needRestart();
                        }
                    }).setNegativeButton(Re.s(0x7F0700B9), null));  // string:skip "Ignore"
                    return;
                }
                break;
            }
            case 18: {
                this.notifyScript(seq, null);
                return;
            }
            case 19: {
                this.logUsed(info);
                return;
            }
            case 20: {
                Alert.show(Alert.create().setMessage(Re.s(0x7F0702EE) + "\n\n" + Re.s((info[0] == 0 ? 0x7F0702F0 : 0x7F0702EF)) + "\n\n" + Re.s(0x7F0702F1)).setPositiveButton(Re.s(0x7F07009D), null));  // string:ptrace_fail "Failed to change memory through ptrace."
                return;
            }
            case 21: {
                if(info[1] != 0) {
                    msg = 0x7F0702F2;  // string:feature_hide_4 "This feature cannot work if the fourth item is selected in 
                                       // \"__hide_from_game__\"."
                }
                else if(info[2] == 0) {
                    if(this.needPtraceNotify && (Config.configDaemon & 0x20000) != 0) {
                        this.needPtraceNotify = false;
                        Config.get(0x7F0B0088).change();  // id:config_ptrace_bypass_mode
                    }
                    msg = 0x7F0702F4;  // string:feature_protection "This feature cannot work due to ptrace protection in 
                                       // the game."
                }
                else {
                    msg = 0x7F0702F3;  // string:feature_firmware "This feature cannot work without ptrace, which does not 
                                       // work on this firmware."
                }
                Tools.showToast(Re.s(msg), 1);
                return;
            }
            case 22: {
                this.noX86 = true;
            }
        }
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("MainService onStartCommand");
        return 2;
    }

    public void onStatusChanged() {
        this.updateStatusBar();
    }

    public void onTargetDead() {
        this.interruptScript();
        if(this.dialogDeadShow) {
            return;
        }
        this.dialogDeadShow = true;
        String oldPkg = this.processInfo == null ? null : this.processInfo.packageName;
        boolean x64 = this.processInfo == null ? false : this.processInfo.x64;
        this.detectApp(false);
        this.reset();
        this.dismissBusyDialog();
        this.onTargetDeadDialog(oldPkg, x64);
    }

    void onTargetDeadDialog(String oldPkg, boolean x64) {
        String[] arr_s = {Re.s(0x7F0700B8), Re.s(0x7F07019F), Re.s(0x7F070047), Re.s(0x7F07009D)};  // string:exit "Exit"
        Alert.show(Alert.create().setCustomTitle(Tools.getCustomTitle(0x7F0700B7)).setItems(arr_s, new DialogInterface.OnClickListener() {  // string:game_dead "Game dead"
            @Override  // android.content.DialogInterface$OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                int v1 = 0;
                MainService.this.dialogDeadShow = false;
                switch(which) {
                    case 0: {
                        new ExitListener(1100).onClick(dialog, 0);
                        break;
                    }
                    case 1: {
                        Log.d(("Restart: " + oldPkg));
                        boolean started = false;
                        try {
                            Intent intent0 = Tools.getPackageManager().getLaunchIntentForPackage(oldPkg);
                            if(intent0 == null) {
                                intent0 = new Intent("android.intent.action.MAIN");
                                intent0.setPackage(oldPkg);
                            }
                            intent0.setFlags(0x10000000);
                            Tools.getContext().startActivity(intent0);
                            started = true;
                        }
                        catch(Exception e) {
                            Log.e(("Failed restart app: " + oldPkg), e);
                        }
                        Handler handler0 = ThreadManager.getHandlerUiThread();
                        android.ext.MainService.36.1 mainService$36$10 = new Runnable() {
                            @Override
                            public void run() {
                                MainService.this.mAppDetector.detectApp(true, this.val$oldPkg);
                            }
                        };
                        if(started) {
                            v1 = 4000;
                        }
                        handler0.postDelayed(mainService$36$10, ((long)v1));
                        break;
                    }
                    case 2: {
                        if(Main.isRootMode(null, new DialogInterface.OnDismissListener() {
                            @Override  // android.content.DialogInterface$OnDismissListener
                            public void onDismiss(DialogInterface dialog) {
                                MainService.this.onTargetDeadDialog(this.val$oldPkg, this.val$x64);
                            }
                        })) {
                            Alert.show(Alert.create().setCustomTitle(Tools.getCustomTitle(0x7F070047)).setMessage(Re.s(0x7F070282)).setNegativeButton(Re.s(0x7F0700A1), null).setPositiveButton(Re.s(0x7F07009D), new DialogInterface.OnClickListener() {  // string:restart_game_without_protection "__restart_game__ (__without_protection__)"
                                @Override  // android.content.DialogInterface$OnClickListener
                                public void onClick(DialogInterface dialog, int which) {
                                    ThreadManager.runOnMainThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Log.d(("Restart stub: " + this.val$oldPkg));
                                            boolean started = false;
                                            String s = "exec setprop wrap." + (this.val$oldPkg == null || this.val$oldPkg.length() <= 26 ? this.val$oldPkg : this.val$oldPkg.substring(0, 26));
                                            try {
                                                RootDetector.runCmd((s + " LD_PRELOAD=" + (DaemonLoader.getDaemonDir() + DaemonLoader.getStubLib(this.val$x64))), 5);
                                            }
                                            catch(Throwable e) {
                                                Log.e(("Failed stub app: " + this.val$oldPkg), e);
                                            }
                                            try {
                                                if(!RootDetector.runCmd(("exec monkey -p " + this.val$oldPkg + " 1"), 10).contains("Events injected")) {
                                                    Intent intent0 = Tools.getPackageManager().getLaunchIntentForPackage(this.val$oldPkg);
                                                    if(intent0 == null) {
                                                        intent0 = new Intent("android.intent.action.MAIN");
                                                        intent0.setPackage(this.val$oldPkg);
                                                    }
                                                    boolean ok = false;
                                                    if(intent0 != null) {
                                                        ComponentName componentName0 = intent0.getComponent();
                                                        if(componentName0 != null) {
                                                            ok = RootDetector.runCmd(("exec am start -n " + componentName0.flattenToString()), 10).contains("Starting: Intent");
                                                        }
                                                    }
                                                    if(!ok) {
                                                        intent0.setFlags(0x10000000);
                                                        ThreadManager.runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                try {
                                                                    Tools.getContext().startActivity(intent0);
                                                                }
                                                                catch(Exception e) {
                                                                    Log.e(("Failed restart app 2: " + this.val$oldPkg), e);
                                                                }
                                                            }
                                                        });
                                                    }
                                                }
                                                started = true;
                                            }
                                            catch(Throwable e) {
                                                Log.e(("Failed restart app: " + this.val$oldPkg), e);
                                            }
                                            ThreadManager.getHandlerMainThread().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    try {
                                                        RootDetector.runCmd((s + " \'\'"), 5);
                                                    }
                                                    catch(Throwable e) {
                                                        Log.e(("Failed unstub app: " + this.val$oldPkg), e);
                                                    }
                                                }
                                            }, ((long)(started ? 10000 : 0)));
                                            ThreadManager.getHandlerUiThread().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    MainService.this.mAppDetector.detectApp(true, this.val$oldPkg);
                                                }
                                            }, ((long)(started ? 4000 : 0)));
                                        }
                                    });
                                }
                            }));
                        }
                        break;
                    }
                    default: {
                        MainService.this.mAppDetector.setAppSelectByAuto();
                    }
                }
                Tools.dismiss(dialog);
            }
        }).setCancelable(false));
    }

    public static void onTrimMemory(int level) {
        Log.w(("onTrimMemory: " + level));
        if(MainService.instance == null) {
            return;
        }
        Re.clearCache();
        ProcessList.clearCache();
        MainService.instance.doTrimMemory(level);
    }

    public void prevSpeed() {
        Log.d("<<");
        this.listSpeed.toPrev();
        this.updateSpeed(true);
    }

    private void printView(String indent, View view) {
        Log.d(("D: " + indent + view));
        if(view instanceof ViewGroup) {
            int v = ((ViewGroup)view).getChildCount();
            if(v > 0) {
                for(int i = 0; i < v; ++i) {
                    View view1 = ((ViewGroup)view).getChildAt(i);
                    if(view1 != null) {
                        this.printView(indent + "  ", view1);
                    }
                }
            }
        }
    }

    boolean processKill() {
        boolean z = this.processKill(0);
        Record record = MainService.instance.currentRecord;
        if(record != null) {
            record.write("gg.processKill()\n");
        }
        return z;
    }

    boolean processKill(byte seq) {
        ProcessInfo processInfo = this.processInfo;
        if(processInfo == null) {
            Tools.showToast(Re.s(0x7F0700BD));  // string:no_application "No application has been selected."
            return false;
        }
        Log.d("Kill game");
        this.mDaemonManager.setPid(seq, processInfo.pid, processInfo.packageName, processInfo.libsPath);
        this.mDaemonManager.signal(seq, 2);
        return true;
    }

    boolean processPause(byte seq, boolean force) {
        if((force || this.mainDialog != null) && !this.processPaused) {
            this.lockApp();
            this.mDaemonManager.signal(seq, 0);
            this.processPaused = true;
            this.updatePauseButton();
            return true;
        }
        return false;
    }

    boolean processPause(boolean force) {
        boolean z1 = this.processPause(0, force);
        Record record = MainService.instance.currentRecord;
        if(record != null) {
            record.write("gg.processPause()\n");
        }
        return z1;
    }

    boolean processResume() {
        boolean z = this.processResume(0);
        Record record = MainService.instance.currentRecord;
        if(record != null) {
            record.write("gg.processResume()\n");
        }
        return z;
    }

    boolean processResume(byte seq) {
        if(this.processPaused) {
            this.lockApp();
            this.mDaemonManager.signal(seq, 1);
            this.processPaused = false;
            this.updatePauseButton();
            return true;
        }
        return false;
    }

    boolean processToggle(byte seq, boolean force) {
        boolean z1 = false;
        if(!force && this.mainDialog == null) {
            return false;
        }
        this.lockApp();
        this.mDaemonManager.signal(seq, ((byte)(this.processPaused ? 1 : 0)));
        if(!this.processPaused) {
            z1 = true;
        }
        this.processPaused = z1;
        this.updatePauseButton();
        return true;
    }

    boolean processToggle(boolean force) {
        boolean z1 = this.processToggle(0, force);
        Record record = MainService.instance.currentRecord;
        if(record != null) {
            record.write("gg.processToggle()\n");
        }
        return z1;
    }

    void refreshResultList(boolean force) {
        if(force || !this.mBusyDialog.isVisible()) {
            this.mCountWait.setVisibility(0);
            Converter.getResultList();
        }
    }

    // 检测为 Lambda 实现
    public void refreshTab() [...]

    public void refreshTabLazy() {
        Runnable runner = this.refreshTabRunner;
        Handler handler0 = ThreadManager.getHandlerUiThread();
        if(runner == null) {
            runner = () -> {
                if(MainService.this.selectedTab == MainService.this.mSavedPage) {
                    MainService.this.savedList.reloadData();
                    return;
                }
                if(MainService.this.selectedTab == MainService.this.mMemEditorPage) {
                    MainService.this.mMemListAdapter.reloadAll();
                    return;
                }
                MainService.this.refreshResultList(false);
            };
            this.refreshTabRunner = runner;
        }
        else {
            handler0.removeCallbacks(runner);
        }
        handler0.postDelayed(runner, 500L);

        class android.ext.MainService.23 implements Runnable {
            @Override
            public void run() {
                MainService.this.refreshTab();
            }
        }

    }

    @TargetApi(14)
    private void registerCallback() {
        if(Build.VERSION.SDK_INT >= 14) {
            MainService.context.registerComponentCallbacks(MainService.getComponentCallbacks());
        }
    }

    void removeItems(Object obj) {
        if(obj instanceof LongSparseArrayChecked) {
            ArrayList checked = new ArrayList();
            int v = ((LongSparseArrayChecked)obj).size();
            for(int i = 0; i < v; ++i) {
                if(((LongSparseArrayChecked)obj).checkAt(i)) {
                    SavedItem item = (SavedItem)((LongSparseArrayChecked)obj).valueAt(i);
                    if(item instanceof SavedItem) {
                        checked.add(item);
                    }
                }
            }
            this.savedList.remove(checked);
            return;
        }
        if(obj instanceof ArrayListResults) {
            ArrayList checked = new ArrayList();
            int v2 = ((ArrayListResults)obj).size();
            for(int i = 0; i < v2; ++i) {
                if(((ArrayListResults)obj).checked(i)) {
                    checked.add(((ArrayListResults)obj).get(i, null));
                }
            }
            this.mDaemonManager.remove(checked);
            this.refreshResultList(false);
        }
    }

    public static void reportBadFirmware() {
        Alert.show(Alert.create().setMessage(Re.s(0x7F070104)).setNegativeButton(Re.s(0x7F07009D), null));  // string:bad_firmware "Your firmware has errors that prevented the use of this feature."
    }

    private void reset() {
        this.processResume();
        this.clear(true);
        this.revertMap.truncate();
        this.mHasNotLocked = true;
        this.mSmallImage.setVisibility(4);
        this.listSpeed.setSpeedOne();
        this.hotPoint.hidePanel();
        this.showedErrors.clear();
        this.savedList.clear();
        this.mDaemonManager.reset();
    }

    public void resetSpeed() {
        Log.d("***");
        this.listSpeed.setSpeedOne();
        this.updateSpeed(true);
    }

    void revertItems(Object obj, AddressItemSet revert) {
        boolean onlyFill = revert != null;
        if(!onlyFill) {
            revert = new AddressItemSet();
        }
        AddressItemSet revertData = this.revertMap;
        Result result = new Result();
        if(obj instanceof ArrayListResults) {
            AddressItem item = new AddressItem();
            int v = ((ArrayListResults)obj).size();
            for(int i = 0; i < v; ++i) {
                if(((ArrayListResults)obj).checked(i)) {
                    ((ArrayListResults)obj).get(i, item);
                    revertData.get(item.address, item.flags, result);
                    if(result.found) {
                        revert.put(item.address, item.flags, result.data);
                    }
                }
            }
        }
        else if(obj instanceof LongSparseArrayChecked) {
            int v2 = ((LongSparseArrayChecked)obj).size();
            for(int i = 0; i < v2; ++i) {
                if(((LongSparseArrayChecked)obj).checkAt(i)) {
                    SavedItem item = (SavedItem)((LongSparseArrayChecked)obj).valueAt(i);
                    if(item instanceof SavedItem) {
                        revertData.get(item.address, item.flags, result);
                        if(result.found) {
                            revert.put(item.address, item.flags, result.data);
                        }
                    }
                }
            }
        }
        if(!onlyFill) {
            revert.revert();
        }
    }

    private void saveCurrentPosMemEditor(Long source, long addr) {
        if(source == null) {
            Long src = null;
            int pos = this.mMemListView.getFirstVisiblePosition();
            if(this.mMemListView.getChildCount() > 1 && this.mMemListView.getChildAt(0).getTop() < 0) {
                ++pos;
            }
            Object object0 = this.mMemListView.getItemAtPosition(pos);
            if(object0 instanceof GotoAddress) {
                src = ((GotoAddress)object0).getSource();
            }
            if(object0 instanceof AddressItem) {
                src = ((AddressItem)object0).address;
            }
            source = src;
        }
        if(source != null) {
            long v2 = (long)source;
            if(v2 != addr && v2 != this.memoryHistory.getCurrent()) {
                int top = 0;
                int index = this.mMemListAdapter.getPos(v2) - this.mMemListView.getFirstVisiblePosition();
                if(index >= 0 && index < this.mMemListView.getChildCount()) {
                    top = this.mMemListView.getChildAt(index).getTop();
                }
                this.memoryHistory.add(v2, Tools.stripColon(0x7F0702C1), top);  // string:position_in_memory_editor "Position in the memory editor"
            }
        }
    }

    static void setLastFlags(int flags, int seq) {
        MainService.lastFlags = flags & 0x7F;
        if(seq == 0) {
            MainService.lastType = flags & 0x7F;
        }
    }

    void setMem(int total, int free) {
        this.memTotal = total;
        this.memFree = free;
    }

    public void setMemory(BufferReader reader) {
        this.mMemListAdapter.loadData(reader);
    }

    public void setProcessInfo(ProcessInfo info) {
        boolean needReset = this.processInfo == null || info == null || this.processInfo.pid != info.pid;
        if(needReset) {
            MainService.checkLibs = true;
            this.reset();
        }
        Config.save();
        this.processInfo = info;
        Config.load();
        this.mConfigListAdapter.setGameName();
        if(info != null) {
            Drawable drawable0 = Tools.tryCloneIcon(info.icon);
            this.mAppImage.setImageDrawable(drawable0);
            if(info.icon == null) {
                Log.e(("setProcessInfo icon is null: " + info));
            }
            this.mAppName.setText(info.toString());
            Drawable icon = Tools.tryCloneIcon(info.icon);
            this.hotPoint.updateIcon(icon);
            this.mDaemonManager.setPid(info.pid, info.packageName, info.libsPath);
        }
        this.timersEditor.load();
        this.randEditor.load();
        this.checkAutopause();
        if(needReset) {
            this.refreshTab();
        }
    }

    void setScriptUiButton(boolean show) {
        if(show == (this.scriptUiButton != null)) {
            return;
        }
        ThreadManager.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(show) {
                    MainService.this.scriptUiButtonClicked = false;
                }
                MainService.this.scriptUiButton = show ? MainService.this.getFloatPanel("script-ui-button", 0, 50, 0x7F020051, 0x7F020051) : null;  // drawable:ic_ui_button_24dp
                MainService.this.showApp.showScript();
            }
        });
    }

    public void setSpeed(byte seq, double speed) {
        this.setSpeedInternal(seq, speed);
        this.listSpeed.setCloseSpeed(speed);
    }

    public void setSpeed(double speed) {
        this.setSpeed(0, speed);
    }

    private void setSpeedInternal(byte seq, double speed) {
        MN listSpeed$MN0 = ListSpeed.getMN(speed);
        this.mDaemonManager.setSpeed(seq, listSpeed$MN0.M, listSpeed$MN0.N);
        this.hotPoint.updateSpeed(ListSpeed.format(((double)listSpeed$MN0.M) / ((double)listSpeed$MN0.N)));
    }

    public void setTabIndex(int tab) {
        this.mMainScreen.setCurrentTab(tab);
    }

    private void setupConfigPage() {
        ConfigListAdapter configListAdapter0 = new ConfigListAdapter();
        this.mConfigListAdapter = configListAdapter0;
        this.mConfigListView.setAdapter(configListAdapter0);
        this.mConfigListView.setOnItemClickListener(configListAdapter0);
    }

    @TargetApi(11)
    private void setupMemEditorPage(MenuItem[][] listeners) {
        this.memoryHistory = new MemoryHistory();
        MemoryContentAdapter memoryContentAdapter0 = new MemoryContentAdapter();
        this.mMemListAdapter = memoryContentAdapter0;
        MenuItem menuItem0 = memoryContentAdapter0.getFilterMenuItem();
        this.mFilter.setOnClickListener(menuItem0);
        listeners[2] = new MenuItem[]{new EditorListener(), new ExportSelected(this), new SaveSelected(this), new GoToButtonListener(), new MemoryForward(this.memoryHistory), new MemoryBack(this.memoryHistory), new CopySelected(), new LoadSelectedAsResults(this), new RevertSelected(this), new ClearSelection(this), new CalculateOffset(this), new Dump(), new CopyMemory(), new Allocate(), new OffsetCalculator(), new SelectAll(this), new InvertSelection(this), this.valueFormat, menuItem0};
        try {
            this.mMemListView.setAdapter(memoryContentAdapter0);
            this.mMemListView.setOnItemClickListener(new EditorListener(true));
            this.mMemListView.setOnItemLongClickListener(new ItemContextMenu(3));
        }
        catch(OutOfMemoryError e) {
            Log.badImplementation(e);
        }
        FastScrollerFix.setFastScrollEnabled(this.mMemListView, true);
    }

    public void setupSavedListUpdates() {
        try {
            ThreadManager.getHandlerUiThread().post(this.savedListUpdate);
        }
        catch(Throwable e) {
            Log.w("Failed post timer", e);
        }
    }

    private void setupSavedPage(MenuItem[][] listeners) {
        listeners[1] = new MenuItem[]{new MenuItem(0x7F0701A4, 0x7F020058) {  // string:load_saved_list "Load the saved list from the file"
            @Override  // android.ext.MenuItem
            public void onClick(View v) {
                new ListManager(MainService.this.processInfo, null);
            }
        }, new RevertSelected(this), new SelectAll(this), new MenuItem(0x7F0701A5, 0x7F02001F) {  // string:save_saved_list "Save the saved list to the file"
            @Override  // android.ext.MenuItem
            public void onClick(View v) {
                int v = MainService.this.savedList.getCount();
                SavedItem[] list = new SavedItem[v];
                for(int i = 0; i < v; ++i) {
                    list[i] = MainService.this.savedList.getItem(i);
                }
                new ListManager(MainService.this.processInfo, list);
            }
        }, new Remove(this, true), new EditorListener(), new InvertSelection(this), new ExportSelected(this), new LoadSelectedAsResults(this), new CopySelected(), new CalculateOffset(this), new AddressEditor(), new ClearSelection(this), new OffsetCalculator(), this.valueFormat};
        this.mSavedListView.setAdapter(this.savedList);
        this.mSavedListView.setOnItemClickListener(new EditorListener());
        this.mSavedListView.setOnItemLongClickListener(new ItemContextMenu(2));
        new FastScrollerFix(this.mSavedListView);
        SavedListAdapter.updateMask();
        this.setupSavedListUpdates();
    }

    private void setupSearchPage(MenuItem[][] listeners) {
        this.fuzzyListener = new FuzzyButtonListener();
        this.searchListener = new SearchButtonListener();
        this.maskListener = new MaskButtonListener();
        listeners[0] = new MenuItem[]{new ExecuteScript(), this.searchListener, new EditorListener(), new Remove(this, false), this.fuzzyListener, new SaveSelected(this), new TimeJump(), new RevertSelected(this), new SelectAll(this), new CopySelected(), this.maskListener, new InvertSelection(this), new RecordScript(), new ExportSelected(this), new ClearSelection(this), new LoadSelectedAsResults(this), new CalculateOffset(this), new Unrandomizer(), new PointerButtonListener(), new OffsetCalculator(), this.filterListener, new MenuItem(0x7F070109, 0x7F02004D) {  // string:speedhack "Speedhack"
            @Override  // android.ext.MenuItem
            public void onClick(View v) {
                Tools.showToast(0x7F070119);  // string:speedhack_open "You can open speedhack by long pressing on the floating __app_name__ 
                                              // icon."
                MainService.this.dismissDialog();
                MainService.this.onLongClick(null);
            }
        }, new LoadChangedAsResults(this)};
        this.mAddrListView.setAdapter(new AddressArrayAdapter(this.mAddressList));
        this.mAddrListView.setOnItemClickListener(new EditorListener());
        this.mAddrListView.setOnItemLongClickListener(new ItemContextMenu(1));
        new FastScrollerFix(this.mAddrListView);
        this.showCounters();
    }

    boolean showBusyDialog() {
        this.hotPoint.onProgress(1, -1L, 1L, 0, 1, "");
        return this.mBusyDialog.show(true);
    }

    @SuppressLint({"DefaultLocale"})
    public void showCounters() {
        StringBuilder text = null;
        if(this.mResultCount == 0L) {
            if(this.showSearchHint) {
                text = new StringBuilder();
                text.append(Re.s(0x7F0700E3));  // string:search_hint "To search for a known value, press \"__search_known_value__\".\nIf 
                                                // the value is unknown or encrypted - click the \"__search_unknown_value__\" to search.\nSearch 
                                                // for float values by their integer part may be performed by selecting the \'__type_auto__\' 
                                                // search type.\nAlso you can use a group search with \'__semicolon__\' as a separator."
                text.append('\n');
                text.append(Re.s(0x7F070103));  // string:speedhack_hint "To change the speed of the game, perform a long press on 
                                                // the floating __app_name__ icon."
            }
            else {
                text = new StringBuilder();
                text.append(Re.s(0x7F0700E0));  // string:nothing_found "Nothing found. Try to search again."
                if(Config.usedRanges != -1) {
                    text.append('\n');
                    text.append(Re.s(0x7F0700E1));  // string:try_more_ranges "You can also try to choose more ranges in the settings tab."
                }
                if(!this.usedFuzzy) {
                    text.append('\n');
                    text.append(Re.s(0x7F0700E2));  // string:try_fuzzy_search "You can also try to search for an unknown value."
                }
            }
        }
        int v = this.mAddressList.size();
        String s = " (" + DisplayNumbers.longToString(v, 0x20) + (this.mResultCount == ((long)v) ? "" : "/" + DisplayNumbers.longToString(this.mResultCount, 0x20)) + ")";
        String fText = text == null ? null : text.toString();
        CharSequence fCounter = AddressArrayAdapter.showMask == 0 ? s : Tools.concat(new CharSequence[]{s, " ", MemoryContentAdapter.getValueFormat(AddressArrayAdapter.showMask)});
        ThreadManager.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(fText != null) {
                    MainService.this.mNothingFoundText.setText(fText);
                }
                if(fCounter != null) {
                    MainService.this.mFoundCount.setText(fCounter);
                }
                MainService.this.mCountWait.setVisibility(8);
            }
        });
    }

    void showDialog() {
        Log.d(("ServiceProxy showDialog: " + this.mainDialog));
        if(this.mainDialog != null) {
            Log.d("Something going wrong", new RuntimeException("mainDialog != null"));
            this.mainDialog.dismiss();
        }
        this.mainDialog = new MainDialog(MainService.context, this.mMainScreen);
        for(int j = 0; j < this.toolbars.length; ++j) {
            this.toolbars[j].setFill(Config.fillToolbar - 1);
        }
        ThreadManager.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MainService.this.mainDialog.show();
                MainService.this.mBusyDialog.show(false);
            }
        });
        this.showApp.show();
        this.mAppDetector.checkAppSelect(this.mHasNotLocked, null);
        this.hotPoint.hide();
        this.checkAutopause();
        this.mDaemonManager.getMem();
        if(!this.mBusyDialog.isVisible()) {
            this.refreshTab();
            if(MainService.searchHelperRestore) {
                MainService.searchHelperRestore = false;
                if(this.mAddressList.size() == 0 && this.mResultCount != 0L) {
                    this.refreshResultList(false);
                }
                if(DaemonManager.lastSearchFuzzy) {
                    this.fuzzyListener.onClick(this.mMenuButton);
                }
                else {
                    this.searchListener.onClick(this.mMenuButton);
                }
            }
        }
        this.updateNotification();
        this.showScriptEnd(null);
    }

    public void showScriptEnd(Runnable callback) {
        if(callback == null) {
            ArrayList list = this.pendingScriptEnds;
            this.pendingScriptEnds = new ArrayList();
            for(Object object0: list) {
                Runnable run = (Runnable)object0;
                try {
                    run.run();
                }
                catch(Throwable e) {
                    Log.w("Failed showScriptEnd", e);
                }
            }
            return;
        }
        if(this.mainDialog != null) {
            try {
                callback.run();
            }
            catch(Throwable e) {
                Log.w("Failed showScriptEnd", e);
            }
            return;
        }
        this.pendingScriptEnds.add(callback);
    }

    void startHotkeyDetection() {
        Log.d(("startHotkeyDetection: " + this.hotPoint));
        if(this.hotPoint == null) {
            return;
        }
        this.hotPoint.show();
        this.mDaemonManager.getMem();
    }

    public void startRecord(Record record) {
        this.interruptRecord();
        this.currentRecord = record;
        ThreadManager.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MainService.this.recordInterrupt = MainService.this.getFloatPanel("record-interrupt", 0, 0, 0x7F0702E5, 0x7F02004E);  // string:record "Record"
                MainService.this.showApp.showRecord();
            }
        });
    }

    void stopHotkeyDetection() {
        Log.d(("stopHotkeyDetection: " + this.hotPoint));
        if(this.hotPoint == null) {
            return;
        }
        this.hotPoint.hide();
    }

    void stopService() {
        Log.d("stopService");
        if(MainService.stopped) {
            Log.d("Service stopService: already");
            return;
        }
        MainService.stopped = true;
        this.mDaemonManager.stop();
        this.hotPoint.stop();
        this.stopSelf();
    }

    public void toggleDialog() {
        if(SystemConstants.useFloatWindows) {
            if(this.mainDialog == null) {
                this.onHotKeyDetected();
                return;
            }
            this.dismissDialog();
            return;
        }
        try {
            if(this.mainDialog == null || BaseActivity.instance == null || !BaseActivity.instance.isOnTop()) {
                Intent intent = MainService.context.getPackageManager().getLaunchIntentForPackage("com.ggdqo");
                if(intent == null) {
                    Tools.setComponentEnabledSetting(300, new ComponentName("com.ggdqo", "com.ggdqo.ActivityMain"), 1);
                    intent = new Intent("android.intent.action.MAIN").setClassName("com.ggdqo", "com.ggdqo.ActivityMain");
                }
                intent.setFlags(0x20000);
                MainService.context.startActivity(intent);
                if(this.mainDialog == null) {
                    this.onHotKeyDetected();
                }
            }
            else if(this.processInfo != null) {
                String pkg = this.processInfo.packageName;
                Intent intent1 = MainService.context.getPackageManager().getLaunchIntentForPackage(pkg);
                if(intent1 == null) {
                    intent1 = new Intent("android.intent.action.MAIN");
                    intent1.setPackage(pkg);
                }
                intent1.setFlags(0x20000);
                MainService.context.startActivity(intent1);
            }
        }
        catch(Throwable e) {
            Log.w("bring to front fail", e);
        }
    }

    @TargetApi(14)
    private void unregisterCallback() {
        if(Build.VERSION.SDK_INT >= 14) {
            MainService.context.unregisterComponentCallbacks(MainService.getComponentCallbacks());
        }
    }

    void updateCounters() {
        int v = 8;
        int v1 = this.mAddrListView.getAdapter().getCount();
        this.searchCounter.setVisibility((v1 == 0 ? 8 : 0));
        this.searchCounter.setText(Integer.toString(v1));
        int v2 = this.mSavedListView.getAdapter().getCount();
        TextView textView0 = this.savedCounter;
        if(v2 != 0) {
            v = 0;
        }
        textView0.setVisibility(v);
        this.savedCounter.setText(Integer.toString(v2));
        this.updateStatusBar();
    }

    public void updateNotification() {
        this.updateNotification(false);
    }

    public void updateNotification(boolean hideAll) {
        int i;
        Throwable e;
        try {
            if(Main.exit) {
                hideAll = true;
            }
            e = null;
            i = 0;
            while(true) {
            label_4:
                if(i < 2) {
                    NotificationManager nm = (NotificationManager)MainService.context.getApplicationContext().getSystemService("notification");
                    if((hideAll || !BootstrapService.allow) && (Config.configClient & 0x100) == 0) {
                        if(i == 0) {
                            Log.d("cancel notification");
                            if(BootstrapService.instance != null) {
                                BootstrapService.instance.stopForeground(true);
                            }
                        }
                        if(nm != null) {
                            nm.cancel(1);
                            nm.cancelAll();
                            return;
                        }
                        Log.d(("Failed get NotificationManager " + i));
                        break;
                    }
                    else {
                        if(i == 0) {
                            Log.d("add notification");
                        }
                        if(nm == null) {
                            Log.d(("Failed get NotificationManager " + i));
                            break;
                        }
                        else {
                            Notification notification0 = this.getNotification(i % 2 == 0, false);
                            nm.notify(1, notification0);
                            if(BootstrapService.instance != null) {
                                BootstrapService.instance.startForeground(1, notification0);
                                Log.d(("startForeground 2." + i));
                                return;
                            }
                        }
                    }
                }
                return;
            }
        }
        catch(Throwable e2) {
            try {
            label_30:
                if(e != null && Build.VERSION.SDK_INT >= 19) {
                    e2.addSuppressed(e);
                }
                if(i == 1) {
                    Log.w(("Failed startForeground " + 1), e2);
                    ExceptionHandler.sendException(Thread.currentThread(), e2, false);
                }
                e = e2;
            }
            catch(Throwable e) {
                Log.badImplementation(e);
                return;
            }
        }
        try {
            ++i;
            goto label_4;
        }
        catch(Throwable e2) {
            goto label_30;
        }
    }

    void updatePauseButton() {
        this.mProcessPauseButton.setImageResource((this.processPaused ? 0x7F02003D : 0x7F02003A));  // drawable:ic_play_arrow_white_18dp
    }

    public void updateSpeed(boolean update) {
        if(update) {
            this.lockApp();
        }
        double f = this.listSpeed.getSpeed();
        this.setSpeedInternal(0, f);
        Record record = MainService.instance.currentRecord;
        if(record != null) {
            record.write("gg.setSpeed(");
            record.write(Double.toString(f));
            record.write(")\n");
        }
    }

    void updateStatusBar() {
        if(ThreadManager.isInUiThread()) {
            this._updateStatusBar();
            return;
        }
        ThreadManager.runOnUiThread(() -> {
            TextView appName = MainService.this.mAppName;
            if(appName != null && MainService.this.processInfo == null && MainService.this.mDaemonManager.inOut.isStarted()) {
                appName.setText(Re.s(0x7F0700C8));  // string:loading "Loading..."
            }
            TextView bar = MainService.this.statusBar;
            if(bar != null) {
                bar.setText(Tools.concat(new CharSequence[]{Re.s(0x7F070000), " ", MainService.this.mDaemonManager.getStatus(), " ", Config.get(0x7F0B0081).asString(), " ", Integer.toString(Tools.getSelectedCount(MainService.this.getCheckList()))}));  // string:version_number "96.0"
            }
        });

        class android.ext.MainService.29 implements Runnable {
            @Override
            public void run() {
                MainService.this._updateStatusBar();
            }
        }

    }

    public void updateToolbars() {
        int v3;
        if(MainService.created) {
            int v = this.getTabIndex();
            int v1 = Tools.isLandscape();
            boolean showToolbar = (Config.toolbars & 1 << (v - 1) * 2 + v1) != 0;
            int backgrounds = Config.backgrounds;
            if(v - 1 >= 0 && this.oldBkg[v - 1] != backgrounds) {
                this.oldBkg[v - 1] = backgrounds;
                this.mMainScreen.getCurrentView().setBackgroundColor(((1 << v - 1 & backgrounds) == 0 ? 0 : 0xFF000000));
            }
            this.mMenuButton.setVisibility((showToolbar || v1 != 0 ? 8 : 0));
            ImageView imageView0 = this.mMenuButtonLand;
            if(v1 != 1) {
                v3 = 8;
            }
            else if(v - 1 >= 0) {
                v3 = 0;
            }
            else {
                v3 = 4;
            }
            imageView0.setVisibility(v3);
            this.mMoreButton.setVisibility((v1 == 0 ? 0 : 8));
            this.toolbarRow.setVisibility((v - 1 < 0 || !showToolbar ? 8 : 0));
            for(int j = 0; j < this.toolbars.length; ++j) {
                WrapLayout toolbar = this.toolbars[j];
                toolbar.setFill(Config.fillToolbar - 1);
                toolbar.setVisibility((j == v - 1 ? 0 : 8));
                long set = Config.toolbarButtons[j * 2 + v1];
                for(int i = 0; i < toolbar.getChildCount(); ++i) {
                    try {
                        View view0 = toolbar.getChildAt(i);
                        if(view0 != null) {
                            int v7 = (1L << i & set) == 0L ? 8 : 0;
                            view0.setVisibility(v7);
                        }
                    }
                    catch(ArrayIndexOutOfBoundsException unused_ex) {
                    }
                    catch(Throwable e) {
                        Log.badImplementation(e);
                    }
                }
            }
        }
    }
}

