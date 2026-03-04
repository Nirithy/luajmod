package android.ext;

import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.DialogInterface.OnShowListener;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.fix.LayoutInflater;
import android.fix.SparseArray;
import android.os.Build.VERSION;
import android.view.View.OnClickListener;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AbsListView;
import android.widget.CheckedTextView;
import android.widget.ImageView.ScaleType;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.SeekBar;
import android.widget.TextView;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.Locale;

public class Config {
    public static class ContextFix extends ContextWrapper {
        public ContextFix(Context base) {
            super(base);
        }

        @Override  // android.content.Context
        public String getOpPackageName() {
            Log.d(("Use 2 " + Config.vSpacePkg + " instead of " + "com.ggdqo"));
            return Config.vSpacePkg;
        }

        @Override  // android.content.ContextWrapper
        public String getPackageName() {
            Log.d(("Use 1 " + Config.vSpacePkg + " instead of " + "com.ggdqo"));
            return Config.vSpacePkg;
        }
    }

    public static abstract class Option implements DialogInterface.OnClickListener {
        interface OnChangeListener {
            int onChange(DialogInterface arg1, int arg2);
        }

        interface OnChangedListener {
            void onChanged(int arg1);
        }

        boolean appDependent;
        protected OnChangeListener changeListener;
        protected OnChangedListener changedListener;
        int defaultValue;
        int helpRes;
        int nameRes;
        String storageKey;
        int value;

        public Option(int id, int nameRes, int defaultValue, String storageKey, boolean appDependent) {
            this.nameRes = nameRes;
            this.defaultValue = defaultValue;
            this.storageKey = storageKey;
            this.value = defaultValue;
            this.appDependent = appDependent;
            this.helpRes = -1;
            this.changeListener = null;
            this.init();
            Config.list.put(id, this);
        }

        public CharSequence asString() {
            return this.toString();
        }

        public abstract void change();

        protected void init() {
        }

        @Override  // android.content.DialogInterface$OnClickListener
        public final void onClick(DialogInterface dialog, int which) {
            OnChangeListener listener = this.changeListener;
            if(listener != null) {
                which = listener.onChange(dialog, which);
            }
            if(this.onClick_(dialog, which)) {
                OnChangedListener listener2 = this.changedListener;
                if(listener2 != null) {
                    listener2.onChanged(this.value);
                }
            }
        }

        public abstract boolean onClick_(DialogInterface arg1, int arg2);

        public void setOnChangeListner(OnChangeListener listener) {
            this.changeListener = listener;
        }

        public void setOnChangedListner(OnChangedListener listener) {
            this.changedListener = listener;
        }

        @Override
        public String toString() {
            return Re.s(this.nameRes) + ' ' + this.value;
        }
    }

    public static class OptionEdit extends Option implements DialogInterface.OnShowListener, View.OnClickListener {
        private boolean allowXor;
        private int editDataType;
        long max;
        int min;
        int unitRes;
        private boolean useConverter;
        private WeakReference weakDialog;
        private WeakReference weakEdit;

        public OptionEdit(int id, int nameRes, int unitRes, int min, long max, int defaultValue, String storageKey, boolean appDependent) {
            super(id, nameRes, defaultValue, storageKey, appDependent);
            this.weakDialog = new WeakReference(null);
            this.weakEdit = new WeakReference(null);
            this.useConverter = false;
            this.allowXor = true;
            this.editDataType = 8;
            this.unitRes = unitRes;
            this.min = min;
            this.max = max;
        }

        public OptionEdit(int id, int nameRes, int unitRes, int min, long max, int defaultValue, String storageKey, boolean appDependent, int helpRes) {
            this(id, nameRes, unitRes, min, max, defaultValue, storageKey, appDependent);
            this.helpRes = helpRes;
        }

        public OptionEdit(int id, int nameRes, int unitRes, int min, long max, int defaultValue, String storageKey, boolean appDependent, boolean useConverter, boolean allowXor) {
            this(id, nameRes, unitRes, min, max, defaultValue, storageKey, appDependent);
            this.useConverter = useConverter;
            this.allowXor = allowXor;
        }

        public OptionEdit(int id, int nameRes, int unitRes, int min, long max, int defaultValue, String storageKey, boolean appDependent, boolean useConverter, boolean allowXor, int editDataType) {
            this(id, nameRes, unitRes, min, max, defaultValue, storageKey, appDependent, useConverter, allowXor);
            this.editDataType = editDataType;
        }

        @Override  // android.ext.Config$Option
        public void change() {
            this.show();
        }

        private String getUnit() {
            return this.unitRes == 0 ? "" : Re.s(this.unitRes);
        }

        @Override  // android.view.View$OnClickListener
        public void onClick(View v) {
            if(v != null && v.getId() == 0x7F0B002D) {  // id:default_
                EditText edit = (EditText)this.weakEdit.get();
                if(edit != null) {
                    edit.setText(Tools.stringFormat("%,d", new Object[]{this.defaultValue}));
                }
                return;
            }
            this.onClick(((DialogInterface)this.weakDialog.get()), -1);
        }

        @Override  // android.ext.Config$Option
        public boolean onClick_(DialogInterface dialog, int which) {
            EditText edit = (EditText)this.weakEdit.get();
            if(edit == null) {
                return false;
            }
            try {
                String val = SearchMenuItem.checkScript(edit.getText().toString().trim());
                Result parserNumbers$Result0 = ParserNumbers.parseLong(val);
                long min = (long)this.min;
                long max = this.max;
                if(parserNumbers$Result0.value < min || parserNumbers$Result0.value > max) {
                    throw new NumberFormatException(Tools.stringFormat(Re.s(0x7F070122), new Object[]{val}) + Tools.stringFormat(" [%,d, %,d]", new Object[]{min, max}));  // string:number_out_of_range "Number \'__s__\' out of possible range:"
                }
                History.add(val, this.editDataType);
                this.value = Math.max(((int)Math.min(parserNumbers$Result0.value, this.max)), this.min);
                Config.save();
                Tools.dismiss(this.weakDialog);
                return true;
            }
            catch(NumberFormatException e) {
                SearchMenuItem.inputError(e, edit);
                return false;
            }
        }

        @Override  // android.content.DialogInterface$OnShowListener
        public void onShow(DialogInterface dialog) {
            Tools.setButtonListener(dialog, -1, null, this, ((EditText)this.weakEdit.get()));
        }

        protected void show() {
            View view0 = LayoutInflater.inflateStatic(0x7F04000E, null);  // layout:option_edit
            TextView tv = (TextView)view0.findViewById(0x7F0B000E);  // id:message
            tv.setText(Re.s(this.nameRes));
            if(this.helpRes != -1 && this.helpRes != 0) {
                Tools.setButtonHelpBackground(tv);
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override  // android.view.View$OnClickListener
                    public void onClick(View v) {
                        Alert.show(Alert.create().setMessage(Re.s(OptionEdit.this.nameRes) + '\n' + Re.s(OptionEdit.this.helpRes)).setNegativeButton(Re.s(0x7F07009D), null));  // string:ok "OK"
                    }
                });
            }
            ((TextView)view0.findViewById(0x7F0B002C)).setText(Tools.stringFormat(Re.s(0x7F0700CE), new Object[]{Tools.stringFormat("%,d", new Object[]{this.min}), Tools.stringFormat("%,d", new Object[]{this.max})}));  // id:hint
            ((TextView)view0.findViewById(0x7F0B004E)).setText(this.getUnit());  // id:unit
            TextView default_ = (TextView)view0.findViewById(0x7F0B002D);  // id:default_
            default_.setText(Tools.stripColon(0x7F07023E) + ": " + Tools.stringFormat("%,d", new Object[]{this.defaultValue}));  // string:default_ "Default:"
            default_.setOnClickListener(this);
            EditText edit = (EditText)view0.findViewById(0x7F0B004D);  // id:number
            edit.setText(Tools.stringFormat("%,d", new Object[]{this.value}));
            edit.setDataType(this.editDataType);
            this.weakEdit = new WeakReference(edit);
            HexConverter converter = (HexConverter)view0.findViewById(0x7F0B0042);  // id:number_converter
            converter.setTag(new Object[]{edit, 4});
            converter.setVisibility((this.useConverter ? 0 : 8));
            converter.setUseXor(this.allowXor);
            AlertDialog alertDialog0 = Alert.create().setView(InternalKeyboard.getView(view0)).setPositiveButton(0x7F07009D, this).setNegativeButton(0x7F0700A1, null).create();  // string:ok "OK"
            Alert.setOnShowListener(alertDialog0, this);
            this.weakDialog = new WeakReference(alertDialog0);
            Alert.show(alertDialog0, edit);
        }

        @Override  // android.ext.Config$Option
        public String toString() {
            return Re.s(this.nameRes) + ' ' + Tools.stringFormat("%,d", new Object[]{this.value}) + ' ' + this.getUnit();
        }
    }

    public static class OptionHidden extends Option {
        public OptionHidden(int id, int nameRes, int defaultValue, String storageKey, boolean appDependent) {
            super(id, nameRes, defaultValue, storageKey, appDependent);
        }

        @Override  // android.ext.Config$Option
        public void change() {
        }

        @Override  // android.ext.Config$Option
        public boolean onClick_(DialogInterface dialog, int which) {
            return false;
        }
    }

    public static class OptionMultiChoice extends OptionSingleChoice implements DialogInterface.OnMultiChoiceClickListener, DialogInterface.OnShowListener, AbsListView.OnScrollListener {
        boolean[] selected;

        public OptionMultiChoice(int id, int nameRes, int[] values, int defaultValue, String storageKey, boolean appDependent) {
            super(id, nameRes, values, defaultValue, storageKey, appDependent);
        }

        public OptionMultiChoice(int id, int nameRes, int[] values, int defaultValue, String storageKey, boolean appDependent, int helpRes) {
            this(id, nameRes, values, defaultValue, storageKey, appDependent);
            this.helpRes = helpRes;
        }

        protected void loadTabMap(boolean withOrientation) {
            if(this.map == null) {
                int[] map = new int[(withOrientation ? 6 : 3)];
                CharSequence[] stringValues = new CharSequence[map.length];
                if(withOrientation) {
                    map[0] = 1;
                    stringValues[0] = Re.s(0x7F07008B) + ", " + Re.s(0x7F07023A);  // string:search "Search"
                    map[1] = 2;
                    stringValues[1] = Re.s(0x7F07008B) + ", " + Re.s(0x7F070239);  // string:search "Search"
                    map[2] = 4;
                    stringValues[2] = Re.s(0x7F07019D) + ", " + Re.s(0x7F07023A);  // string:saved_list "Saved list"
                    map[3] = 8;
                    stringValues[3] = Re.s(0x7F07019D) + ", " + Re.s(0x7F070239);  // string:saved_list "Saved list"
                    map[4] = 16;
                    stringValues[4] = Re.s(0x7F07019E) + ", " + Re.s(0x7F07023A);  // string:memory_editor "Memory editor"
                    map[5] = 0x20;
                    stringValues[5] = Re.s(0x7F07019E) + ", " + Re.s(0x7F070239);  // string:memory_editor "Memory editor"
                }
                else {
                    map[0] = 1;
                    stringValues[0] = Re.s(0x7F07008B);  // string:search "Search"
                    map[1] = 2;
                    stringValues[1] = Re.s(0x7F07019D);  // string:saved_list "Saved list"
                    map[2] = 4;
                    stringValues[2] = Re.s(0x7F07019E);  // string:memory_editor "Memory editor"
                }
                this.map = map;
                this.stringValues = stringValues;
                this.values = null;
            }
        }

        @Override  // android.content.DialogInterface$OnMultiChoiceClickListener
        public void onClick(DialogInterface dialog, int which, boolean isChecked) {
            boolean[] sel = this.selected;
            if(sel == null) {
                Log.e("", new NullPointerException(this.toString()));
                return;
            }
            try {
                sel[which] = isChecked;
            }
            catch(ArrayIndexOutOfBoundsException e) {
                Log.e("", e);
            }
        }

        @Override  // android.ext.Config$OptionSingleChoice
        public boolean onClick_(DialogInterface dialog, int which) {
            if(which == -3 || which == -1) {
                boolean[] sel = this.selected;
                int newValue = this.defaultValue;
                if(sel != null && which == -1) {
                    for(int i = 0; i < sel.length; ++i) {
                        int v3 = this.idToValue(i);
                        newValue = sel[i] ? newValue | v3 : newValue & ~v3;
                    }
                    this.selected = null;
                }
                this.value = newValue;
                Config.save();
                Tools.dismiss(dialog);
                return true;
            }
            return false;
        }

        @Override  // android.widget.AbsListView$OnScrollListener
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            this.onScrollStateChanged(view, 0);
        }

        @Override  // android.widget.AbsListView$OnScrollListener
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            int i;
            try {
                int v1 = view.getChildCount();
                i = 0;
                while(true) {
                label_2:
                    if(i >= v1) {
                        return;
                    }
                    try {
                        View view0 = view.getChildAt(i);
                        if(view0 instanceof CheckedTextView) {
                            ((CheckedTextView)view0).refreshDrawableState();
                        }
                    }
                    catch(ArrayIndexOutOfBoundsException unused_ex) {
                    }
                    break;
                }
            }
            catch(Throwable e) {
                Log.badImplementation(e);
                return;
            }
            ++i;
            goto label_2;
        }

        @Override  // android.content.DialogInterface$OnShowListener
        public void onShow(DialogInterface d) {
            if(Build.VERSION.SDK_INT >= 23 && d instanceof AlertDialog) {
                ListView listView0 = ((AlertDialog)d).getListView();
                if(listView0 != null) {
                    listView0.setOnScrollListener(this);
                }
            }
        }

        @Override  // android.ext.Config$OptionSingleChoice
        protected void show(CharSequence[] items) {
            boolean[] sel = new boolean[items.length];
            this.selected = sel;
            int value_ = this.value;
            for(int i = 0; i < items.length; ++i) {
                int v2 = this.idToValue(i);
                sel[i] = (value_ & v2) == v2;
                if(Build.VERSION.SDK_INT < 11 && items[i] instanceof String) {
                    items[i] = Tools.colorize(items[i], -1);
                }
            }
            AlertDialog alertDialog0 = Alert.create(Tools.getDarkContext()).setCustomTitle(Config.getCustomTitle(this.nameRes, this.helpRes, this.appDependent)).setMultiChoiceItems(items, sel, this).setPositiveButton(0x7F07008C, this).setNeutralButton(0x7F07023F, this).setNegativeButton(0x7F0700A1, this).create();  // string:save "Save"
            Alert.setOnShowListener(alertDialog0, this);
            Alert.show(alertDialog0);
        }

        @Override  // android.ext.Config$OptionSingleChoice
        public String toString() {
            return Tools.stripColon(this.nameRes);
        }
    }

    public static class OptionMultiChoiceSh extends OptionMultiChoice {
        private final int cnt;

        public OptionMultiChoiceSh(int id, int nameRes, int[] values, int defaultValue, String storageKey, boolean appDependent, int cnt) {
            super(id, nameRes, values, defaultValue, storageKey, appDependent);
            this.cnt = cnt;
        }

        @Override  // android.ext.Config$OptionSingleChoice
        protected void loadMap() {
            super.loadMap();
            if(this.map == null) {
                int[] map = new int[this.cnt];
                CharSequence[] stringValues = new CharSequence[map.length];
                for(int i = 0; i < map.length; ++i) {
                    map[i] = 1 << i;
                    stringValues[i] = Integer.toString(i + 1);
                }
                this.map = map;
                this.stringValues = stringValues;
                this.values = null;
            }
        }

        @Override  // android.ext.Config$OptionMultiChoice
        public boolean onClick_(DialogInterface dialog, int which) {
            boolean z = super.onClick_(dialog, which);
            if(z) {
                Tools.showToast(0x7F07015B);  // string:need_restart "The target app must be restarted to apply these changes."
            }
            return z;
        }
    }

    public static class OptionSeek extends Option implements SeekBar.OnSeekBarChangeListener {
        int max;
        protected WeakReference weakSeek;

        public OptionSeek(int id, int nameRes, int max, int defaultValue, String storageKey, boolean appDependent) {
            super(id, nameRes, defaultValue, storageKey, appDependent);
            this.weakSeek = new WeakReference(null);
            this.max = max;
        }

        public OptionSeek(int id, int nameRes, int max, int defaultValue, String storageKey, boolean appDependent, int helpRes) {
            this(id, nameRes, max, defaultValue, storageKey, appDependent);
            this.helpRes = helpRes;
        }

        @Override  // android.ext.Config$Option
        public void change() {
            this.show();
        }

        @Override  // android.ext.Config$Option
        public boolean onClick_(DialogInterface dialog, int which) {
            SeekBar sb = (SeekBar)this.weakSeek.get();
            if(sb == null) {
                return false;
            }
            this.value = sb.getProgress();
            Config.save();
            return true;
        }

        @Override  // android.widget.SeekBar$OnSeekBarChangeListener
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        }

        @Override  // android.widget.SeekBar$OnSeekBarChangeListener
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override  // android.widget.SeekBar$OnSeekBarChangeListener
        public void onStopTrackingTouch(SeekBar seekBar) {
        }

        protected void show() {
            View view0 = LayoutInflater.inflateStatic(0x7F04000F, null);  // layout:option_seek
            SeekBar seek = (SeekBar)view0.findViewById(0x7F0B004F);  // id:seek
            seek.setMax(this.max);
            seek.setProgress(this.value);
            seek.setOnSeekBarChangeListener(this);
            this.weakSeek = new WeakReference(seek);
            Alert.show(Alert.create().setCustomTitle(Config.getCustomTitle(this.nameRes, this.helpRes, this.appDependent)).setView(view0).setPositiveButton(0x7F07009D, this).setNegativeButton(0x7F0700A1, null).create());  // string:ok "OK"
        }

        @Override  // android.ext.Config$Option
        public String toString() {
            return Tools.stripColon(this.nameRes);
        }
    }

    public static class OptionSingleChoice extends Option {
        int[] map;
        CharSequence[] stringValues;
        int[] values;

        public OptionSingleChoice(int id, int nameRes, int[] values, int defaultValue, String storageKey, boolean appDependent) {
            super(id, nameRes, defaultValue, storageKey, appDependent);
            this.stringValues = null;
            this.map = null;
            this.values = values;
        }

        public OptionSingleChoice(int id, int nameRes, int[] values, int defaultValue, String storageKey, boolean appDependent, int helpRes) {
            this(id, nameRes, values, defaultValue, storageKey, appDependent);
            this.helpRes = helpRes;
        }

        @Override  // android.ext.Config$Option
        public void change() {
            this.loadMap();
            CharSequence[] items = this.stringValues;
            if(this.values != null) {
                items = new CharSequence[this.values.length];
                for(int i = 0; i < this.values.length; ++i) {
                    items[i] = Re.s(this.values[i]);
                }
            }
            this.show(items);
        }

        public int idToValue(int id) {
            this.loadMap();
            return this.map == null ? id : this.map[id];
        }

        protected void loadMap() {
        }

        @Override  // android.ext.Config$Option
        public boolean onClick_(DialogInterface dialog, int which) {
            this.value = this.idToValue(which);
            Config.save();
            Tools.dismiss(dialog);
            return true;
        }

        protected void show(CharSequence[] items) {
            int v = this.valueToId(this.defaultValue);
            CharSequence[] items_ = new CharSequence[items.length];
            for(int i = 0; i < items.length; ++i) {
                CharSequence charSequence0 = i != v || items[i] == null ? items[i] : items[i] + " (" + Re.s(0x7F07023D) + ')';  // string:default_mark "default"
                items_[i] = charSequence0;
            }
            Alert.show(Alert.create().setCustomTitle(Config.getCustomTitle(this.nameRes, this.helpRes, this.appDependent)).setSingleChoiceItems(items_, this.valueToId(this.value), this));
        }

        @Override  // android.ext.Config$Option
        public String toString() {
            CharSequence val = null;
            try {
                int v = this.valueToId(this.value);
                val = this.values == null ? this.stringValues[v] : Re.s(this.values[v]);
            }
            catch(IndexOutOfBoundsException unused_ex) {
            }
            if(val == null) {
                val = "int:" + this.value;
            }
            return Tools.stripColon(this.nameRes) + ": " + val;
        }

        public int valueToId(int val) {
            this.loadMap();
            if(this.map == null) {
                return val;
            }
            for(int i = 1; true; ++i) {
                if(i >= this.map.length) {
                    return 0;
                }
                if(this.map[i] == val) {
                    return i;
                }
            }
        }
    }

    public static final int CONFIG_ALLOW_SUGGESTIONS = 8;
    public static final int CONFIG_CHECK_LIBS = 0x800;
    public static final int CONFIG_IGNORE_UNKNOWN_CHARS = 0x1000;
    public static final int CONFIG_KBD_SMALL = 0x40;
    public static final int CONFIG_PREVENT_UNLOAD_TOAST = 0x2000;
    public static final int CONFIG_SHOW_TIME_JUMP_PANEL = 0x80;
    public static final int CONFIG_USE_AUTOPAUSE = 4;
    public static final int CONFIG_USE_HARDWARE_ACCELERATION = 2;
    public static final int CONFIG_USE_INDENT = 16;
    public static final int CONFIG_USE_INTERNAL_KEYBOARD = 1;
    public static final int CONFIG_USE_NOTIFICATION = 0x100;
    public static final int CONFIG_USE_SOUND_EFFECTS = 0x20;
    public static final int CONFIG_VISIBLE_TYPE = 0x400;
    public static final int CONFIG_VSPACE_ROOT = 0x200;
    public static final int CONTEXT_ACTIVITY = 4;
    public static final int CONTEXT_APPLICATION = 3;
    public static final int CONTEXT_CREATE = 2;
    public static final int CONTEXT_INSTRUMENTATION = 0;
    public static final int CONTEXT_SERVICE = 1;
    public static final int HELPER_GO_TO = 1;
    public static final int HELPER_GO_TO_AND_RESTORE = 2;
    public static final int HELPER_OFF = 0;
    private static final String IGNORE = "ignore";
    public static final int IGNORE_BAD_APPS = 3;
    private static final long IGNORE_DEFAULT = 0L;
    public static final int IGNORE_FORCE_CLOSE = 6;
    public static final int IGNORE_LOCALE = 7;
    public static final int IGNORE_MIUI = 1;
    public static final int IGNORE_NOT_RANDOM_NAME = 4;
    public static final int IGNORE_PHOENIX_OS = 2;
    public static final int IGNORE_SLOW_EMULATOR = 5;
    private static final String MEMORY_FROM = "memory-from";
    private static final long MEMORY_FROM_DEFAULT = 0L;
    private static final String MEMORY_TO = "memory-to";
    private static final long MEMORY_TO_DEFAULT = -1L;
    private static final String NEARBY_DISTANCE = "nearby-distance";
    private static final long NEARBY_DISTANCE_DEFAULT = 0x200L;
    private static final int RAND_COUNT = 19;
    public static final int SPEEDS_NOTHING = 0;
    public static final int SPEEDS_SORT = 1;
    public static final int SPEEDS_SORT_AND_REMOVE = 2;
    public static final int STATE_ANONYMOUS = 0x20;
    public static final int STATE_ASHMEM = 0x80000;
    public static final int STATE_BAD = 0x20000;
    public static final int STATE_CODE_APP = 0x4000;
    public static final int STATE_CODE_SYS = 0x8000;
    private static final int STATE_COUNT = 15;
    public static final int STATE_C_ALLOC = 4;
    public static final int STATE_C_BSS = 16;
    public static final int STATE_C_DATA = 8;
    public static final int STATE_C_HEAP = 1;
    public static final int STATE_JAVA = 0x10000;
    public static final int STATE_JAVA_HEAP = 2;
    public static final int STATE_OTHER = 0xFFE03F80;
    public static final int STATE_PPSSPP = 0x40000;
    public static final int STATE_STACK = 0x40;
    public static final int STATE_VIDEO = 0x100000;
    private static final int TIMER_COUNT = 21;
    private static final String TOOLBAR_BUTTONS = "toolbar-buttons-";
    private static final long TOOLBAR_BUTTONS_DEFAULT = -1L;
    public static volatile int backgrounds;
    public static volatile int configClient;
    public static volatile int configDaemon;
    public static volatile int contextSource;
    public static volatile int copyParams;
    public static volatile int dataInRam;
    public static volatile int fillToolbar;
    public static volatile int floatFlags;
    public static volatile int floatType;
    public static volatile int freezeInterval;
    public static volatile int historyLimit;
    public static volatile int hotKey;
    public static volatile int iconsSize;
    public static volatile long ignore;
    public static volatile long interceptTimers;
    static SparseArray list;
    public static volatile long memoryFrom;
    public static volatile long memoryTo;
    public static volatile long nearbyDistance;
    public static volatile int savedListUpdatesInterval;
    public static volatile int searchHelper;
    public static volatile int smallItems;
    public static volatile int speedsParams;
    public static volatile int timeJumpLast;
    public static final long[] toolbarButtons;
    public static volatile int toolbars;
    static volatile int[] used;
    public static volatile int usedRanges;
    public static volatile boolean vSpace;
    public static volatile byte vSpace64;
    public static volatile boolean vSpaceGuest;
    public static volatile String vSpaceName;
    public static volatile String vSpacePkg;
    public static volatile boolean vSpaceReal;
    public static volatile int xorKey;

    static {
        Config.toolbarButtons = new long[MainService.menuItems.length * 2];
        Config.used = new int[]{0x72, 0x73, 0x74};
        Config.list = new SparseArray();
        new OptionHidden(0x7F0B00BD, 0x7F07002F, -1, "float-type", false);  // id:config_float_type
        new OptionHidden(0x7F0B00BE, 0x7F07002F, -1, "float-flags", false);  // id:config_float_flags
        new OptionHidden(0x7F0B00BF, 0x7F07002F, 0, "kbd-small", false);  // id:config_kbd_small
        new OptionHidden(0x7F0B00C0, 0x7F07002F, 0, "time-jump-last", true);  // id:config_time_jump_last
        new OptionHidden(0x7F0B00C1, 0x7F07002F, 0, "copy-params", false);  // id:config_copy_params
        new OptionHidden(0x7F0B00C2, 0x7F07002F, 0, "record-logcat", false);  // id:record_logcat
        new OptionEdit(0x7F0B008B, 0x7F070185, 0x7F070298, 0, 600000000L, 33000, "freeze-interval", true, 0x7F070052);  // id:config_freeze_interval
        new OptionEdit(0x7F0B008E, 0x7F070186, 0x7F070187, 100, 600000L, 1000, "saved-list-updates-interval", false, 0x7F070053);  // id:config_saved_list_updates_interval
        new OptionEdit(0x7F0B00BB, 0x7F0701A7, 0x7F07002F, 0x80000000, 0xFFFFFFFFL, 0, "xor-key", true, true, false, 1);  // id:config_xor_key
        new OptionEdit(0x7F0B009E, 0x7F070276, 0x7F07002F, 0, 100000L, 500, "history-limit", false);  // id:config_history_limit
        new OptionSeek(0x7F0B00A9, 0x7F070191, 12, 0, "icons-size", false, 0x7F07029C) {  // id:config_icons_size
            private WeakReference weakIcon;

            {
                int $anonymous0 = 0x7F0B00A9;  // 捕获的参数
                int $anonymous1 = 0x7F070191;  // 捕获的参数
                int $anonymous2 = 12;  // 捕获的参数
                int $anonymous3 = 0;  // 捕获的参数
                String $anonymous4 = "icons-size";  // 捕获的参数
                boolean $anonymous5 = false;  // 捕获的参数
                int $anonymous6 = 0x7F07029C;  // 捕获的参数
                this.weakIcon = new WeakReference(null);
            }

            @Override  // android.ext.Config$OptionSeek
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                ImageView icon = (ImageView)this.weakIcon.get();
                if(icon == null) {
                    return;
                }
                Config.setIconSize(icon, progress * 2 + 24);
            }

            @Override  // android.ext.Config$OptionSeek
            protected void show() {
                super.show();
                SeekBar seek = (SeekBar)this.weakSeek.get();
                if(seek != null) {
                    ImageView imageView0 = new ImageView(seek.getContext());
                    imageView0.setImageResource(0x7F02004F);  // drawable:ic_tooltip_edit_white_24dp
                    this.weakIcon = new WeakReference(imageView0);
                    Config.setIconSize(imageView0, Config.getIconSize(seek.getProgress()));
                    ViewParent viewParent0 = seek.getParent();
                    if(viewParent0 instanceof ViewGroup) {
                        ((ViewGroup)viewParent0).addView(imageView0);
                    }
                }
            }
        };
        new OptionSingleChoice(0x7F0B00A1, 0x7F070287, new int[]{0x7F07009C, 0x7F07009B}, 1, "ignore-unknown-chars", false, 0x7F07029D);  // id:config_ignore_unknown_chars
        new OptionSingleChoice(0x7F0B009C, 0x7F070206, new int[]{0x7F07009C, 0x7F07009B}, 0, "use-notification", false, 0x7F07029E);  // id:config_use_notification
        new OptionSingleChoice(0x7F0B00BC, 0x7F07005C, new int[]{0x7F07005D, 0x7F07005E, 0x7F07005F, 0x7F070060, 0x7F070061}, (Build.VERSION.SDK_INT < 29 ? 0 : 1), "context-source", false);  // id:config_context_source
        new OptionSingleChoice(0x7F0B00AF, 0x7F070201, new int[]{0x7F07009C, 0x7F07009B}, 1, "use-sound-effects", false, 0x7F07029F);  // id:config_use_sound_effects
        new OptionSingleChoice(0x7F0B00BA, 0x7F07002F, new int[]{0x7F0701F5, 0x7F0701F6}, 0, "selinux", false);  // id:selinux
        int v = InternalKeyboard.allowUsage() ? 1 : 0;
        new OptionSingleChoice(0x7F0B009F, 0x7F0700F9, new int[]{0x7F0700FB, 0x7F0700FA}, v, "use-internal-keyboard", false, 0x7F0702A0);  // id:config_keyboard
        new OptionSingleChoice(0x7F0B00A0, 0x7F07011C, new int[]{0x7F07009C, 0x7F07009B}, 0, "allow-suggestions", false, 0x7F0702A1);  // id:config_suggestions
        new OptionSingleChoice(0x7F0B00A2, 0x7F07015E, new int[]{0x7F07009C, 0x7F07009B}, 0, "use-indent", false);  // id:config_indent
        new OptionSingleChoice(0x7F0B008C, 0x7F070141, new int[]{0x7F07009C, 0x7F07009B}, 0, "use-autopause", false, 0x7F0702A2);  // id:config_autopause
        new OptionSingleChoice(0x7F0B00AE, 0x7F070106, new int[]{0x7F070108, 0x7F070068}, 0, "use-hardware-acceleration", false, 0x7F070310);  // id:config_acceleration
        new OptionSingleChoice(0x7F0B0089, 0x7F07026C, new int[]{0x7F07001E, 0x7F07026D, 0x7F070046}, 2, "skip-memory", true);  // id:config_skip_memory
        new OptionSingleChoice(0x7F0B0094, 0x7F07014E, new int[]{0x7F07014F, 0x7F070150, 0x7F070151}, 0, "memory-access", false) {  // id:config_memory_access
            @Override  // android.ext.Config$OptionSingleChoice
            public void change() {
                if(!Main.isRootMode()) {
                    return;
                }
                super.change();
            }
        };
        new OptionSingleChoice(0x7F0B0095, 0x7F0702EC, new int[]{0x7F07009C, 0x7F07001D}, 0, "method", false, 0x7F0702ED);  // id:config_deep_read
        new OptionSingleChoice(0x7F0B0096, 0x7F070291, new int[]{0x7F07014F, 0x7F070292}, 0, "calls", false, 0x7F070293);  // id:config_calls
        new OptionSingleChoice(0x7F0B0097, 0x7F070296, new int[]{0x7F07014F, 0x7F070292}, 0, "waitpid", false, 0x7F070297);  // id:config_waitpid
        new OptionSingleChoice(0x7F0B008A, 0x7F070320, new int[]{0x7F07009C, 0x7F07009B}, 0, "fast-freeze", true, 0x7F070321);  // id:config_fast_freeze
        new OptionSingleChoice(0x7F0B00A8, 0x7F07016E, new int[]{0x7F07016F, 0x7F070170, 0x7F070171}, 2, "fill-toolbar", false);  // id:config_fill_toolbar
        new OptionSingleChoice(0x7F0B008D, 0x7F070178, new int[]{0x7F070179, 0x7F07017A, 0x7F07017B}, 2, "search-helper", false);  // id:config_search_helper
        new OptionSingleChoice(0x7F0B0090, 0x7F070020, new int[]{0x7F07017C, 0x7F07017D, 0x7F07017E}, 2, "speeds-params", false);  // id:config_speeds_params
        new OptionSingleChoice(0x7F0B0091, 0x7F070030, new int[]{0x7F07009C, 0x7F07009B}, 1, "reset-on-exit", false);  // id:config_reset_on_exit
        new OptionSingleChoice(0x7F0B0092, 0x7F070255, new int[]{0x7F07009C, 0x7F07009B}, 1, "check-libs", false);  // id:config_check_libs
        new OptionSingleChoice(0x7F0B00A3, 0x7F070319, new int[]{0x7F07009C, 0x7F07009B}, 1, "visible-type", false, 0x7F07031A);  // id:config_visible_type
        new OptionSingleChoice(0x7F0B009B, 0x7F070214, new int[]{0x7F07009C, 0x7F070062, 0x7F070063, 0x7F070064}, 0, "prevent-unload", false, 0x7F0702C6) {  // id:config_prevent_unload
            @Override  // android.ext.Config$OptionSingleChoice
            public boolean onClick_(DialogInterface dialog, int which) {
                boolean z = super.onClick_(dialog, which);
                if(z && this.value == 3) {
                    Alert.show(Alert.create(Tools.getContext()).setMessage(Re.s(0x7F070331)).setNegativeButton(0x7F07009D, null));  // string:help_faq_33_a "Using the third level \"__prevent_unload__\" on some firmware 
                                                                                                                                    // leads to the fact that text messages (toasts) remain on the screen, or a dark circle 
                                                                                                                                    // is visible (empty message). If this happens, do not use the third level."
                }
                return z;
            }
        };
        new OptionSingleChoice(0x7F0B0088, 0x7F0702B3, new int[]{0x7F0702B4, 0x7F0702B5, 0x7F0702B6, 0x7F070179}, 3, "ptrace-bypass", true, 0x7F0702B7);  // id:config_ptrace_bypass_mode
        new OptionSingleChoice(0x7F0B0084, 0x7F070205, new int[]{0x7F07009C, 0x7F07009B}, 0, "time-jump-panel", true) {  // id:config_time_jump_panel
            @Override  // android.ext.Config$OptionSingleChoice
            public boolean onClick_(DialogInterface dialog, int which) {
                boolean z = super.onClick_(dialog, which);
                if(z) {
                    MainService.instance.hotPoint.updatePanels();
                }
                return z;
            }
        };
        new OptionSingleChoice(0x7F0B00B1, 0x7F07031D, null, 0, "number-locale", false) {  // id:config_number_locale
            private String example(String locale) {
                try {
                    if(locale.length() == 0) {
                        Locale locale0 = AppLocale.getLocaleObject();
                        return Tools.stripColon(0x7F07023E) + String.format(locale0, "\n[%,.1f | %.2e]", -1234.0, -0.0);  // string:default_ "Default:"
                    }
                    return locale + String.format(AppLocale.getLocaleObject(locale), "\n[%,.1f | %.2e]", -1234.0, -0.0);
                }
                catch(Throwable throwable0) {
                    Log.badImplementation(throwable0);
                    return locale;
                }
            }

            @Override  // android.ext.Config$Option
            protected void init() {
                if(this.value > 0) {
                    String[] arr_s = this.list();
                    if(this.value < arr_s.length) {
                        AppLocale.setNumberLocale(AppLocale.getLocaleObject(arr_s[this.value]));
                    }
                }
            }

            private String[] list() {
                return new String[]{"", "en_US", "in_ID", "ru_RU"};
            }

            @Override  // android.ext.Config$OptionSingleChoice
            protected void loadMap() {
                super.loadMap();
                if(this.stringValues == null) {
                    String[] arr_s = this.list();
                    CharSequence[] stringValues = new CharSequence[arr_s.length];
                    for(int i = 0; i < arr_s.length; ++i) {
                        stringValues[i] = this.example(arr_s[i]);
                    }
                    this.stringValues = stringValues;
                }
            }

            @Override  // android.ext.Config$OptionSingleChoice
            public boolean onClick_(DialogInterface dialog, int which) {
                boolean z = super.onClick_(dialog, which);
                if(z) {
                    ConfigListAdapter.needRestart();
                }
                return z;
            }
        };
        new OptionSingleChoice(0x7F0B009A, 0x7F070305, new int[]{0x7F07009C, 0x7F07006B}, 0, "vspace-root", false, 0x7F070079) {  // id:config_vspace_root
            private int old;

            {
                int $anonymous0 = 0x7F0B009A;  // 捕获的参数
                int $anonymous1 = 0x7F070305;  // 捕获的参数
                int[] $anonymous2 = {0x7F07009C, 0x7F07006B};  // 捕获的参数 （可能与外部方法变量命名冲突；考虑手动重命名）
                int $anonymous3 = 0;  // 捕获的参数
                String $anonymous4 = "vspace-root";  // 捕获的参数
                boolean $anonymous5 = false;  // 捕获的参数
                int $anonymous6 = 0x7F070079;  // 捕获的参数
                this.old = 0;
            }

            @Override  // android.ext.Config$OptionSingleChoice
            public boolean onClick_(DialogInterface dialog, int which) {
                boolean z = false;
                if(which < 0) {
                    if(which == -2) {
                        this.value = 0;
                        Config.save();
                    }
                    if(Config.vSpaceReal && this.old != this.value) {
                        ConfigListAdapter.needRestart();
                        return false;
                    }
                }
                else {
                    this.old = this.value;
                    z = super.onClick_(dialog, which);
                    if(z) {
                        if(which == 1) {
                            AlertDialog alertDialog0 = Alert.create(Tools.getContext()).setCustomTitle(Tools.getCustomTitle(this.nameRes)).setMessage(Re.s(this.helpRes)).setPositiveButton(0x7F07008C, this).setNegativeButton(0x7F0700A1, this).create();  // string:save "Save"
                            Alert.show(alertDialog0);
                            Alert.setOnShowListener(alertDialog0, new DialogInterface.OnShowListener() {
                                @Override  // android.content.DialogInterface$OnShowListener
                                public void onShow(DialogInterface dialog) {
                                    Tools.setClickableText(alertDialog0);
                                }
                            });
                            return true;
                        }
                        if(Config.vSpaceReal && this.old != this.value) {
                            ConfigListAdapter.needRestart();
                            return true;
                        }
                    }
                }
                return z;
            }
        };
        new OptionSingleChoice(0x7F0B0093, 0x7F07010A, null, 0x7FFFFFFF, "data-in-ram", false) {  // id:config_ram
            private static final float DATA_MUL = 5.0f;
            private static final float KB_IN_MB = 1024.0f;
            private static final int MIN_COUNTS = 8;

            private String getStringValue(int value) {
                switch(value) {
                    case 0: {
                        return Re.s(0x7F07009C);  // string:no "No"
                    }
                    case 0x7FFFFFFF: {
                        return Re.s(0x7F07009B);  // string:yes "Yes"
                    }
                    default: {
                        return Tools.stringFormat(Re.s(0x7F07010B), new Object[]{Math.round(5.0f * ((float)value) / 1024.0f)});  // string:first_mb "The first __d__ MB"
                    }
                }
            }

            @Override  // android.ext.Config$OptionSingleChoice
            protected void loadMap() {
                super.loadMap();
                if(this.map == null) {
                    int counts = 0;
                    int v1 = Tools.getRamSizeKb();
                    if(v1 > 0) {
                        counts = ((int)Math.floor(Math.log(((float)v1) / 5.0f) / 0.693147)) - 8;
                    }
                    if(counts <= 0) {
                        counts = 7;
                    }
                    if(counts >= 20) {
                        counts = 20;
                    }
                    int[] map = new int[counts + 2];
                    CharSequence[] stringValues = new CharSequence[counts + 2];
                    map[0] = 0;
                    stringValues[0] = Re.s(0x7F07009C);  // string:no "No"
                    map[map.length - 1] = 0x7FFFFFFF;
                    stringValues[map.length - 1] = Re.s(0x7F07009B);  // string:yes "Yes"
                    String s = Re.s(0x7F07010B);  // string:first_mb "The first __d__ MB"
                    for(int i = 1; i <= counts; ++i) {
                        int item = 1 << i + 8;
                        map[i] = item;
                        stringValues[i] = Tools.stringFormat(s, new Object[]{Math.round(((float)item) * 5.0f / 1024.0f)});
                    }
                    this.map = map;
                    this.stringValues = stringValues;
                    this.values = null;
                }
            }

            @Override  // android.ext.Config$OptionSingleChoice
            public String toString() {
                return Tools.stripColon(this.nameRes) + ": " + this.getStringValue(this.value);
            }
        };
        new OptionSingleChoice(0x7F0B009D, 0x7F070215, null, 0, "hot-key", false) {  // id:config_hot_key
            @Override  // android.ext.Config$OptionSingleChoice
            public void change() {
                if(!Main.isRootMode()) {
                    return;
                }
                super.change();
            }

            @Override  // android.ext.Config$OptionSingleChoice
            protected void loadMap() {
                super.loadMap();
                int count = Config.used.length;
                if(this.map == null || this.map.length != count + 1) {
                    int[] map = new int[count + 1];
                    CharSequence[] stringValues = new CharSequence[count + 1];
                    map[0] = 0;
                    stringValues[0] = Re.s(0x7F07009C);  // string:no "No"
                    for(int i = 0; i < count; ++i) {
                        int key = Config.used[i];
                        map[i + 1] = key;
                        stringValues[i + 1] = Config.getKeyName(key);
                    }
                    this.map = map;
                    this.stringValues = stringValues;
                    this.values = null;
                }
            }

            @Override  // android.ext.Config$OptionSingleChoice
            public String toString() {
                return Tools.stripColon(this.nameRes) + ": " + Config.getKeyName(this.value);
            }
        };
        new OptionMultiChoice(0x7F0B0081, 0x7F07008A, null, 0, "ranges", true) {  // id:config_ranges
            private int[] colors;
            private CharSequence[] shortValues;
            private CharSequence[] strValues;

            @Override  // android.ext.Config$Option
            public CharSequence asString() {
                if(this.map == null) {
                    this.loadMap();
                }
                CharSequence[] ranges = new CharSequence[this.map.length * 2];
                int used = 0;
                for(int i = 0; i < this.map.length; ++i) {
                    if((this.map[i] & this.value) == this.map[i]) {
                        if(used > 0) {
                            ranges[used] = ",";
                            ++used;
                        }
                        ranges[used] = this.shortValues[i];
                        ++used;
                    }
                }
                return Tools.concat(((CharSequence[])Arrays.copyOf(ranges, used)));
            }

            @Override  // android.ext.Config$Option
            protected void init() {
                this.defaultValue = 0x4003F;
            }

            @Override  // android.ext.Config$OptionSingleChoice
            protected void loadMap() {
                super.loadMap();
                if(this.map == null) {
                    int[] map = new int[15];
                    int[] colors = new int[15];
                    CharSequence[] stringValues = new CharSequence[15];
                    map[0] = 2;
                    colors[0] = Tools.getColor(0x7F0A0012);  // color:region_good
                    stringValues[0] = Tools.colorize("Jh: Java heap", colors[0]);
                    map[1] = 1;
                    colors[1] = Tools.getColor(0x7F0A0012);  // color:region_good
                    stringValues[1] = Tools.colorize("Ch: C++ heap", colors[1]);
                    map[2] = 4;
                    colors[2] = Tools.getColor(0x7F0A0012);  // color:region_good
                    stringValues[2] = Tools.colorize("Ca: C++ alloc", colors[2]);
                    map[3] = 8;
                    colors[3] = Tools.getColor(0x7F0A0012);  // color:region_good
                    stringValues[3] = Tools.colorize("Cd: C++ .data", colors[3]);
                    map[4] = 16;
                    colors[4] = Tools.getColor(0x7F0A0012);  // color:region_good
                    stringValues[4] = Tools.colorize("Cb: C++ .bss", colors[4]);
                    map[5] = 0x40000;
                    colors[5] = Tools.getColor(0x7F0A0012);  // color:region_good
                    stringValues[5] = Tools.colorize("PS: PPSSPP", colors[5]);
                    map[6] = 0x20;
                    colors[6] = Tools.getColor(0x7F0A0012);  // color:region_good
                    stringValues[6] = Tools.colorize("A: Anonymous", colors[6]);
                    map[7] = 0x10000;
                    colors[7] = Tools.getColor(0x7F0A0016);  // color:region_other
                    stringValues[7] = Tools.colorize("J: Java", colors[7]);
                    map[8] = 0x40;
                    colors[8] = Tools.getColor(0x7F0A0016);  // color:region_other
                    stringValues[8] = Tools.colorize("S: Stack", colors[8]);
                    map[9] = 0x80000;
                    colors[9] = Tools.getColor(0x7F0A0016);  // color:region_other
                    stringValues[9] = Tools.colorize("As: Ashmem", colors[9]);
                    map[10] = 0x100000;
                    colors[10] = Tools.getColor(0x7F0A0016);  // color:region_other
                    stringValues[10] = Tools.colorize("V: Video", colors[10]);
                    map[11] = 0xFFE03F80;
                    colors[11] = Tools.getColor(0x7F0A0017);  // color:region_slow
                    stringValues[11] = Tools.colorize(Re.s("O: Other (__slow__)"), colors[11]);
                    map[12] = 0x20000;
                    colors[12] = Tools.getColor(0x7F0A0013);  // color:region_bad
                    stringValues[12] = Tools.colorize(Re.s("B: Bad (__dangerous__)"), colors[12]);
                    map[13] = 0x4000;
                    colors[13] = Tools.getColor(0x7F0A0014);  // color:region_code
                    stringValues[13] = Tools.colorize(Re.s("Xa: Code app (__dangerous__)"), colors[13]);
                    map[14] = 0x8000;
                    colors[14] = Tools.getColor(0x7F0A0015);  // color:region_system_code
                    stringValues[14] = Tools.colorize(Re.s("Xs: Code system (__dangerous__)"), colors[14]);
                    CharSequence[] shortValues = new CharSequence[15];
                    for(int i = 0; i < 15; ++i) {
                        shortValues[i] = Tools.colorize(stringValues[i].toString().split(":")[0], colors[i]);
                    }
                    this.colors = colors;
                    this.map = map;
                    this.strValues = stringValues;
                    this.shortValues = shortValues;
                    this.values = null;
                    RegionList.setMaps(map, shortValues, colors);
                }
                CharSequence[] stringValues = new CharSequence[this.map.length];
                CharSequence[] strValues = this.strValues;
                Context context0 = Tools.getContext();
                for(int i = 0; i < strValues.length; ++i) {
                    String str = strValues[i].toString();
                    try {
                        str = str + " [" + Tools.formatFileSize(context0, RegionList.getSize(i)) + ']';
                    }
                    catch(Throwable e) {
                        Log.badImplementation(e);
                        ExceptionHandler.sendException(Thread.currentThread(), e, false);
                    }
                    stringValues[i] = Tools.colorize(str, this.colors[i]);
                }
                this.stringValues = stringValues;
            }

            @Override  // android.ext.Config$OptionMultiChoice
            public boolean onClick_(DialogInterface dialog, int which) {
                boolean z = super.onClick_(dialog, which);
                if(z) {
                    Record record = MainService.instance.currentRecord;
                    if(record != null) {
                        record.write("gg.setRanges(");
                        Consts.logConst(record, record.consts.REGION_, this.value);
                        record.write(")\n");
                    }
                }
                return z;
            }
        };
        new OptionMultiChoiceSh(0x7F0B0082, 0x7F07002A, null, -1, "intercept", true, 21);  // id:config_speedhack_intercept
        new OptionMultiChoiceSh(0x7F0B0085, 0x7F07004B, null, 0xFFF80FFF, "unrand-intercept", true, 19);  // id:config_unrandomizer_intercept
        new OptionMultiChoice(0x7F0B00A6, 0x7F07019C, null, -1, "toolbars", false) {  // id:config_toolbars
            @Override  // android.ext.Config$OptionSingleChoice
            protected void loadMap() {
                super.loadMap();
                this.loadTabMap(true);
            }
        };
        new OptionMultiChoice(0x7F0B00A4, 0x7F070311, null, 0, "small-items", false, 0x7F070312) {  // id:config_use_small_list_items
            @Override  // android.ext.Config$OptionSingleChoice
            protected void loadMap() {
                super.loadMap();
                if(this.changedListener == null) {
                    this.changedListener = new OnChangedListener() {
                        @Override  // android.ext.Config$Option$OnChangedListener
                        public void onChanged(int value) {
                            MainService.instance.onMemoryChanged();
                        }
                    };
                }
                this.loadTabMap(true);
            }
        };
        new OptionMultiChoice(0x7F0B00A5, 0x7F070266, null, 0, "backgrounds", false) {  // id:config_backgrounds
            @Override  // android.ext.Config$OptionSingleChoice
            protected void loadMap() {
                super.loadMap();
                this.loadTabMap(false);
            }
        };
        new OptionMultiChoice(0x7F0B0087, 0x7F07014C, null, 0, "hide", true) {  // id:config_hide_from_game
            @Override  // android.ext.Config$OptionSingleChoice
            protected void loadMap() {
                super.loadMap();
                if(this.map == null) {
                    int[] map = new int[4];
                    CharSequence[] stringValues = new CharSequence[4];
                    String s = " (" + Re.s(0x7F0700C5) + ')';  // string:slow "slow"
                    map[0] = 1;
                    stringValues[0] = "1" + s;
                    map[1] = 2;
                    stringValues[1] = "2" + s;
                    map[2] = 4;
                    stringValues[2] = "3" + s;
                    map[3] = 8;
                    stringValues[3] = "4" + s;
                    this.map = map;
                    this.stringValues = stringValues;
                    this.values = null;
                    if(this.changedListener == null) {
                        this.changedListener = new OnChangedListener() {
                            @Override  // android.ext.Config$Option$OnChangedListener
                            public void onChanged(int value) {
                                StringBuilder msg = null;
                                if((value & 1) != 0) {
                                    msg = new StringBuilder();
                                    msg.append(Re.s(0x7F070349));  // string:hide_1_warn "You selected the first item. Freezing values can cause severe 
                                                                   // lags in the game."
                                }
                                if((value & 8) != 0) {
                                    if(msg == null) {
                                        msg = new StringBuilder();
                                    }
                                    else {
                                        msg.append("\n\n");
                                    }
                                    msg.append(Re.s(0x7F07034A));  // string:hide_4_warn "You selected the fourth item. Some features will not work: __speedhack__, 
                                                                   // __unrandomizer__, __allocate_page__."
                                }
                                if(msg != null) {
                                    Alert.show(Alert.create().setMessage(msg.toString()).setPositiveButton(0x7F07009D, null));  // string:ok "OK"
                                }
                            }
                        };
                    }
                }
            }
        };
        Config.load();
        Config.vSpace = false;
        Config.vSpaceReal = false;
        Config.vSpaceGuest = false;
        Config.vSpacePkg = "unknown";
        Config.vSpaceName = "unknown";
        Config.vSpace64 = 0;
    }

    public static Context fixContext(Context base) {
        return base instanceof ContextFix ? base : new ContextFix(base);
    }

    public static Option get(int id) {
        return (Option)Config.list.get(id);
    }

    static View getCustomTitle(int nameRes, int helpRes, boolean appDependent) {
        String subTitle = null;
        if(appDependent) {
            ProcessInfo pi = MainService.instance.processInfo;
            String name = pi == null ? null : pi.name;
            if(name != null) {
                subTitle = "\"" + name + '\"';
            }
        }
        return Tools.getCustomTitle(Re.s(nameRes), subTitle, helpRes);
    }

    public static int getIconSize() [...] // 潜在的解密器

    public static int getIconSize(int size) [...] // Inlined contents

    static String getKeyName(int id) {
        switch(id) {
            case 0: {
                return Re.s(0x7F07009C);  // string:no "No"
            }
            case 1: {
                return "KEY_ESC";
            }
            case 2: {
                return "KEY_1";
            }
            case 3: {
                return "KEY_2";
            }
            case 4: {
                return "KEY_3";
            }
            case 5: {
                return "KEY_4";
            }
            case 6: {
                return "KEY_5";
            }
            case 7: {
                return "KEY_6";
            }
            case 8: {
                return "KEY_7";
            }
            case 9: {
                return "KEY_8";
            }
            case 10: {
                return "KEY_9";
            }
            case 11: {
                return "KEY_0";
            }
            case 12: {
                return "KEY_MINUS";
            }
            case 13: {
                return "KEY_EQUAL";
            }
            case 14: {
                return "KEY_BACKSPACE";
            }
            case 15: {
                return "KEY_TAB";
            }
            case 16: {
                return "KEY_Q";
            }
            case 17: {
                return "KEY_W";
            }
            case 18: {
                return "KEY_E";
            }
            case 19: {
                return "KEY_R";
            }
            case 20: {
                return "KEY_T";
            }
            case 21: {
                return "KEY_Y";
            }
            case 22: {
                return "KEY_U";
            }
            case 23: {
                return "KEY_I";
            }
            case 24: {
                return "KEY_O";
            }
            case 25: {
                return "KEY_P";
            }
            case 26: {
                return "KEY_LEFTBRACE";
            }
            case 27: {
                return "KEY_RIGHTBRACE";
            }
            case 28: {
                return "KEY_ENTER";
            }
            case 29: {
                return "KEY_LEFTCTRL";
            }
            case 30: {
                return "KEY_A";
            }
            case 0x1F: {
                return "KEY_S";
            }
            case 0x20: {
                return "KEY_D";
            }
            case 33: {
                return "KEY_F";
            }
            case 34: {
                return "KEY_G";
            }
            case 35: {
                return "KEY_H";
            }
            case 36: {
                return "KEY_J";
            }
            case 37: {
                return "KEY_K";
            }
            case 38: {
                return "KEY_L";
            }
            case 39: {
                return "KEY_SEMICOLON";
            }
            case 40: {
                return "KEY_APOSTROPHE";
            }
            case 41: {
                return "KEY_GRAVE";
            }
            case 42: {
                return "KEY_LEFTSHIFT";
            }
            case 43: {
                return "KEY_BACKSLASH";
            }
            case 44: {
                return "KEY_Z";
            }
            case 45: {
                return "KEY_X";
            }
            case 46: {
                return "KEY_C";
            }
            case 0x2F: {
                return "KEY_V";
            }
            case 0x30: {
                return "KEY_B";
            }
            case 49: {
                return "KEY_N";
            }
            case 50: {
                return "KEY_M";
            }
            case 51: {
                return "KEY_COMMA";
            }
            case 52: {
                return "KEY_DOT";
            }
            case 53: {
                return "KEY_SLASH";
            }
            case 54: {
                return "KEY_RIGHTSHIFT";
            }
            case 55: {
                return "KEY_KPASTERISK";
            }
            case 56: {
                return "KEY_LEFTALT";
            }
            case 57: {
                return "KEY_SPACE";
            }
            case 58: {
                return "KEY_CAPSLOCK";
            }
            case 59: {
                return "KEY_F1";
            }
            case 60: {
                return "KEY_F2";
            }
            case 61: {
                return "KEY_F3";
            }
            case 62: {
                return "KEY_F4";
            }
            case 0x3F: {
                return "KEY_F5";
            }
            case 0x40: {
                return "KEY_F6";
            }
            case 65: {
                return "KEY_F7";
            }
            case 66: {
                return "KEY_F8";
            }
            case 67: {
                return "KEY_F9";
            }
            case 68: {
                return "KEY_F10";
            }
            case 69: {
                return "KEY_NUMLOCK";
            }
            case 70: {
                return "KEY_SCROLLLOCK";
            }
            case 71: {
                return "KEY_KP7";
            }
            case 72: {
                return "KEY_KP8";
            }
            case 73: {
                return "KEY_KP9";
            }
            case 74: {
                return "KEY_KPMINUS";
            }
            case 75: {
                return "KEY_KP4";
            }
            case 76: {
                return "KEY_KP5";
            }
            case 77: {
                return "KEY_KP6";
            }
            case 78: {
                return "KEY_KPPLUS";
            }
            case 0x4F: {
                return "KEY_KP1";
            }
            case 80: {
                return "KEY_KP2";
            }
            case 81: {
                return "KEY_KP3";
            }
            case 82: {
                return "KEY_KP0";
            }
            case 83: {
                return "KEY_KPDOT";
            }
            case 85: {
                return "KEY_ZENKAKUHANKAKU";
            }
            case 86: {
                return "KEY_102ND";
            }
            case 87: {
                return "KEY_F11";
            }
            case 88: {
                return "KEY_F12";
            }
            case 89: {
                return "KEY_RO";
            }
            case 90: {
                return "KEY_KATAKANA";
            }
            case 91: {
                return "KEY_HIRAGANA";
            }
            case 92: {
                return "KEY_HENKAN";
            }
            case 93: {
                return "KEY_KATAKANAHIRAGANA";
            }
            case 94: {
                return "KEY_MUHENKAN";
            }
            case 0x5F: {
                return "KEY_KPJPCOMMA";
            }
            case 0x60: {
                return "KEY_KPENTER";
            }
            case 97: {
                return "KEY_RIGHTCTRL";
            }
            case 98: {
                return "KEY_KPSLASH";
            }
            case 99: {
                return "KEY_SYSRQ";
            }
            case 100: {
                return "KEY_RIGHTALT";
            }
            case 101: {
                return "KEY_LINEFEED";
            }
            case 102: {
                return "KEY_HOME";
            }
            case 103: {
                return "KEY_UP";
            }
            case 104: {
                return "KEY_PAGEUP";
            }
            case 105: {
                return "KEY_LEFT";
            }
            case 106: {
                return "KEY_RIGHT";
            }
            case 107: {
                return "KEY_END";
            }
            case 108: {
                return "KEY_DOWN";
            }
            case 109: {
                return "KEY_PAGEDOWN";
            }
            case 110: {
                return "KEY_INSERT";
            }
            case 0x6F: {
                return "KEY_DELETE";
            }
            case 0x70: {
                return "KEY_MACRO";
            }
            case 0x71: {
                return "KEY_MUTE";
            }
            case 0x72: {
                return "KEY_VOLUMEDOWN";
            }
            case 0x73: {
                return "KEY_VOLUMEUP";
            }
            case 0x74: {
                return "KEY_POWER";
            }
            case 0x75: {
                return "KEY_KPEQUAL";
            }
            case 0x76: {
                return "KEY_KPPLUSMINUS";
            }
            case 0x77: {
                return "KEY_PAUSE";
            }
            case 120: {
                return "KEY_SCALE";
            }
            case 0x79: {
                return "KEY_KPCOMMA";
            }
            case 0x7A: {
                return "KEY_HANGEUL";
            }
            case 0x7B: {
                return "KEY_HANJA";
            }
            case 0x7C: {
                return "KEY_YEN";
            }
            case 0x7D: {
                return "KEY_LEFTMETA";
            }
            case 0x7E: {
                return "KEY_RIGHTMETA";
            }
            case 0x7F: {
                return "KEY_COMPOSE";
            }
            case 0x80: {
                return "KEY_STOP";
            }
            case 0x81: {
                return "KEY_AGAIN";
            }
            case 130: {
                return "KEY_PROPS";
            }
            case 0x83: {
                return "KEY_UNDO";
            }
            case 0x84: {
                return "KEY_FRONT";
            }
            case 0x85: {
                return "KEY_COPY";
            }
            case 0x86: {
                return "KEY_OPEN";
            }
            case 0x87: {
                return "KEY_PASTE";
            }
            case 0x88: {
                return "KEY_FIND";
            }
            case 0x89: {
                return "KEY_CUT";
            }
            case 0x8A: {
                return "KEY_HELP";
            }
            case 0x8B: {
                return "KEY_MENU";
            }
            case 140: {
                return "KEY_CALC";
            }
            case 0x8D: {
                return "KEY_SETUP";
            }
            case 0x8E: {
                return "KEY_SLEEP";
            }
            case 0x8F: {
                return "KEY_WAKEUP";
            }
            case 0x90: {
                return "KEY_FILE";
            }
            case 0x91: {
                return "KEY_SENDFILE";
            }
            case 0x92: {
                return "KEY_DELETEFILE";
            }
            case 0x93: {
                return "KEY_XFER";
            }
            case 0x94: {
                return "KEY_PROG1";
            }
            case 0x95: {
                return "KEY_PROG2";
            }
            case 150: {
                return "KEY_WWW";
            }
            case 0x97: {
                return "KEY_MSDOS";
            }
            case 0x98: {
                return "KEY_SCREENLOCK";
            }
            case 0x99: {
                return "KEY_DIRECTION";
            }
            case 0x9A: {
                return "KEY_CYCLEWINDOWS";
            }
            case 0x9B: {
                return "KEY_MAIL";
            }
            case 0x9C: {
                return "KEY_BOOKMARKS";
            }
            case 0x9D: {
                return "KEY_COMPUTER";
            }
            case 0x9E: {
                return "KEY_BACK";
            }
            case 0x9F: {
                return "KEY_FORWARD";
            }
            case 0xA0: {
                return "KEY_CLOSECD";
            }
            case 0xA1: {
                return "KEY_EJECTCD";
            }
            case 0xA2: {
                return "KEY_EJECTCLOSECD";
            }
            case 0xA3: {
                return "KEY_NEXTSONG";
            }
            case 0xA4: {
                return "KEY_PLAYPAUSE";
            }
            case 0xA5: {
                return "KEY_PREVIOUSSONG";
            }
            case 0xA6: {
                return "KEY_STOPCD";
            }
            case 0xA7: {
                return "KEY_RECORD";
            }
            case 0xA8: {
                return "KEY_REWIND";
            }
            case 0xA9: {
                return "KEY_PHONE";
            }
            case 170: {
                return "KEY_ISO";
            }
            case 0xAB: {
                return "KEY_CONFIG";
            }
            case 0xAC: {
                return "KEY_HOMEPAGE";
            }
            case 0xAD: {
                return "KEY_REFRESH";
            }
            case 0xAE: {
                return "KEY_EXIT";
            }
            case 0xAF: {
                return "KEY_MOVE";
            }
            case 0xB0: {
                return "KEY_EDIT";
            }
            case 0xB1: {
                return "KEY_SCROLLUP";
            }
            case 0xB2: {
                return "KEY_SCROLLDOWN";
            }
            case 0xB3: {
                return "KEY_KPLEFTPAREN";
            }
            case 180: {
                return "KEY_KPRIGHTPAREN";
            }
            case 0xB5: {
                return "KEY_NEW";
            }
            case 0xB6: {
                return "KEY_REDO";
            }
            case 0xB7: {
                return "KEY_F13";
            }
            case 0xB8: {
                return "KEY_F14";
            }
            case 0xB9: {
                return "KEY_F15";
            }
            case 0xBA: {
                return "KEY_F16";
            }
            case 0xBB: {
                return "KEY_F17";
            }
            case 0xBC: {
                return "KEY_F18";
            }
            case 0xBD: {
                return "KEY_F19";
            }
            case 190: {
                return "KEY_F20";
            }
            case 0xBF: {
                return "KEY_F21";
            }
            case 0xC0: {
                return "KEY_F22";
            }
            case 0xC1: {
                return "KEY_F23";
            }
            case 0xC2: {
                return "KEY_F24";
            }
            case 200: {
                return "KEY_PLAYCD";
            }
            case 201: {
                return "KEY_PAUSECD";
            }
            case 202: {
                return "KEY_PROG3";
            }
            case 203: {
                return "KEY_PROG4";
            }
            case 204: {
                return "KEY_DASHBOARD";
            }
            case 205: {
                return "KEY_SUSPEND";
            }
            case 206: {
                return "KEY_CLOSE";
            }
            case 0xCF: {
                return "KEY_PLAY";
            }
            case 0xD0: {
                return "KEY_FASTFORWARD";
            }
            case 209: {
                return "KEY_BASSBOOST";
            }
            case 210: {
                return "KEY_PRINT";
            }
            case 0xD3: {
                return "KEY_HP";
            }
            case 0xD4: {
                return "KEY_CAMERA";
            }
            case 0xD5: {
                return "KEY_SOUND";
            }
            case 0xD6: {
                return "KEY_QUESTION";
            }
            case 0xD7: {
                return "KEY_EMAIL";
            }
            case 0xD8: {
                return "KEY_CHAT";
            }
            case 0xD9: {
                return "KEY_SEARCH";
            }
            case 0xDA: {
                return "KEY_CONNECT";
            }
            case 0xDB: {
                return "KEY_FINANCE";
            }
            case 220: {
                return "KEY_SPORT";
            }
            case 0xDD: {
                return "KEY_SHOP";
            }
            case 0xDE: {
                return "KEY_ALTERASE";
            }
            case 0xDF: {
                return "KEY_CANCEL";
            }
            case 0xE0: {
                return "KEY_BRIGHTNESSDOWN";
            }
            case 0xE1: {
                return "KEY_BRIGHTNESSUP";
            }
            case 0xE2: {
                return "KEY_MEDIA";
            }
            case 0xE3: {
                return "KEY_SWITCHVIDEOMODE";
            }
            case 0xE4: {
                return "KEY_KBDILLUMTOGGLE";
            }
            case 0xE5: {
                return "KEY_KBDILLUMDOWN";
            }
            case 230: {
                return "KEY_KBDILLUMUP";
            }
            case 0xE7: {
                return "KEY_SEND";
            }
            case 0xE8: {
                return "KEY_REPLY";
            }
            case 0xE9: {
                return "KEY_FORWARDMAIL";
            }
            case 0xEA: {
                return "KEY_SAVE";
            }
            case 0xEB: {
                return "KEY_DOCUMENTS";
            }
            case 0xEC: {
                return "KEY_BATTERY";
            }
            case 0xED: {
                return "KEY_BLUETOOTH";
            }
            case 0xEE: {
                return "KEY_WLAN";
            }
            case 0xEF: {
                return "KEY_UWB";
            }
            case 0xF0: {
                return "KEY_UNKNOWN";
            }
            case 0xF1: {
                return "KEY_VIDEO_NEXT";
            }
            case 0xF2: {
                return "KEY_VIDEO_PREV";
            }
            case 0xF3: {
                return "KEY_BRIGHTNESS_CYCLE";
            }
            case 0xF4: {
                return "KEY_BRIGHTNESS_AUTO";
            }
            case 0xF5: {
                return "KEY_DISPLAY_OFF";
            }
            case 0xF6: {
                return "KEY_WWAN";
            }
            case 0xF7: {
                return "KEY_RFKILL";
            }
            case 0xF8: {
                return "KEY_MICMUTE";
            }
            case 0x100: {
                return "BTN_0";
            }
            case 0x101: {
                return "BTN_1";
            }
            case 0x102: {
                return "BTN_2";
            }
            case 0x103: {
                return "BTN_3";
            }
            case 260: {
                return "BTN_4";
            }
            case 0x105: {
                return "BTN_5";
            }
            case 0x106: {
                return "BTN_6";
            }
            case 0x107: {
                return "BTN_7";
            }
            case 0x108: {
                return "BTN_8";
            }
            case 0x109: {
                return "BTN_9";
            }
            case 0x110: {
                return "BTN_LEFT";
            }
            case 273: {
                return "BTN_RIGHT";
            }
            case 274: {
                return "BTN_MIDDLE";
            }
            case 275: {
                return "BTN_SIDE";
            }
            case 276: {
                return "BTN_EXTRA";
            }
            case 277: {
                return "BTN_FORWARD";
            }
            case 278: {
                return "BTN_BACK";
            }
            case 279: {
                return "BTN_TASK";
            }
            case 0x120: {
                return "BTN_TRIGGER";
            }
            case 289: {
                return "BTN_THUMB";
            }
            case 290: {
                return "BTN_THUMB2";
            }
            case 291: {
                return "BTN_TOP";
            }
            case 292: {
                return "BTN_TOP2";
            }
            case 293: {
                return "BTN_PINKIE";
            }
            case 294: {
                return "BTN_BASE";
            }
            case 295: {
                return "BTN_BASE2";
            }
            case 296: {
                return "BTN_BASE3";
            }
            case 297: {
                return "BTN_BASE4";
            }
            case 298: {
                return "BTN_BASE5";
            }
            case 299: {
                return "BTN_BASE6";
            }
            case 303: {
                return "BTN_DEAD";
            }
            case 304: {
                return "BTN_A";
            }
            case 305: {
                return "BTN_B";
            }
            case 306: {
                return "BTN_C";
            }
            case 307: {
                return "BTN_X";
            }
            case 308: {
                return "BTN_Y";
            }
            case 309: {
                return "BTN_Z";
            }
            case 310: {
                return "BTN_TL";
            }
            case 311: {
                return "BTN_TR";
            }
            case 312: {
                return "BTN_TL2";
            }
            case 313: {
                return "BTN_TR2";
            }
            case 314: {
                return "BTN_SELECT";
            }
            case 315: {
                return "BTN_START";
            }
            case 316: {
                return "BTN_MODE";
            }
            case 317: {
                return "BTN_THUMBL";
            }
            case 318: {
                return "BTN_THUMBR";
            }
            case 320: {
                return "BTN_TOOL_PEN";
            }
            case 321: {
                return "BTN_TOOL_RUBBER";
            }
            case 322: {
                return "BTN_TOOL_BRUSH";
            }
            case 323: {
                return "BTN_TOOL_PENCIL";
            }
            case 324: {
                return "BTN_TOOL_AIRBRUSH";
            }
            case 325: {
                return "BTN_TOOL_FINGER";
            }
            case 326: {
                return "BTN_TOOL_MOUSE";
            }
            case 327: {
                return "BTN_TOOL_LENS";
            }
            case 328: {
                return "BTN_TOOL_QUINTTAP";
            }
            case 330: {
                return "BTN_TOUCH";
            }
            case 331: {
                return "BTN_STYLUS";
            }
            case 332: {
                return "BTN_STYLUS2";
            }
            case 333: {
                return "BTN_TOOL_DOUBLETAP";
            }
            case 334: {
                return "BTN_TOOL_TRIPLETAP";
            }
            case 0x14F: {
                return "BTN_TOOL_QUADTAP";
            }
            case 0x150: {
                return "BTN_WHEEL";
            }
            case 337: {
                return "BTN_GEAR_UP";
            }
            case 0x160: {
                return "KEY_OK";
            }
            case 353: {
                return "KEY_SELECT";
            }
            case 354: {
                return "KEY_GOTO";
            }
            case 355: {
                return "KEY_CLEAR";
            }
            case 356: {
                return "KEY_POWER2";
            }
            case 357: {
                return "KEY_OPTION";
            }
            case 358: {
                return "KEY_INFO";
            }
            case 359: {
                return "KEY_TIME";
            }
            case 360: {
                return "KEY_VENDOR";
            }
            case 361: {
                return "KEY_ARCHIVE";
            }
            case 362: {
                return "KEY_PROGRAM";
            }
            case 363: {
                return "KEY_CHANNEL";
            }
            case 364: {
                return "KEY_FAVORITES";
            }
            case 365: {
                return "KEY_EPG";
            }
            case 366: {
                return "KEY_PVR";
            }
            case 0x16F: {
                return "KEY_MHP";
            }
            case 0x170: {
                return "KEY_LANGUAGE";
            }
            case 369: {
                return "KEY_TITLE";
            }
            case 370: {
                return "KEY_SUBTITLE";
            }
            case 371: {
                return "KEY_ANGLE";
            }
            case 372: {
                return "KEY_ZOOM";
            }
            case 373: {
                return "KEY_MODE";
            }
            case 374: {
                return "KEY_KEYBOARD";
            }
            case 375: {
                return "KEY_SCREEN";
            }
            case 376: {
                return "KEY_PC";
            }
            case 377: {
                return "KEY_TV";
            }
            case 378: {
                return "KEY_TV2";
            }
            case 379: {
                return "KEY_VCR";
            }
            case 380: {
                return "KEY_VCR2";
            }
            case 381: {
                return "KEY_SAT";
            }
            case 382: {
                return "KEY_SAT2";
            }
            case 0x17F: {
                return "KEY_CD";
            }
            case 0x180: {
                return "KEY_TAPE";
            }
            case 385: {
                return "KEY_RADIO";
            }
            case 386: {
                return "KEY_TUNER";
            }
            case 387: {
                return "KEY_PLAYER";
            }
            case 388: {
                return "KEY_TEXT";
            }
            case 389: {
                return "KEY_DVD";
            }
            case 390: {
                return "KEY_AUX";
            }
            case 391: {
                return "KEY_MP3";
            }
            case 392: {
                return "KEY_AUDIO";
            }
            case 393: {
                return "KEY_VIDEO";
            }
            case 394: {
                return "KEY_DIRECTORY";
            }
            case 395: {
                return "KEY_LIST";
            }
            case 396: {
                return "KEY_MEMO";
            }
            case 397: {
                return "KEY_CALENDAR";
            }
            case 398: {
                return "KEY_RED";
            }
            case 0x18F: {
                return "KEY_GREEN";
            }
            case 400: {
                return "KEY_YELLOW";
            }
            case 401: {
                return "KEY_BLUE";
            }
            case 402: {
                return "KEY_CHANNELUP";
            }
            case 403: {
                return "KEY_CHANNELDOWN";
            }
            case 404: {
                return "KEY_FIRST";
            }
            case 405: {
                return "KEY_LAST";
            }
            case 406: {
                return "KEY_AB";
            }
            case 407: {
                return "KEY_NEXT";
            }
            case 408: {
                return "KEY_RESTART";
            }
            case 409: {
                return "KEY_SLOW";
            }
            case 410: {
                return "KEY_SHUFFLE";
            }
            case 411: {
                return "KEY_BREAK";
            }
            case 412: {
                return "KEY_PREVIOUS";
            }
            case 413: {
                return "KEY_DIGITS";
            }
            case 414: {
                return "KEY_TEEN";
            }
            case 0x19F: {
                return "KEY_TWEN";
            }
            case 0x1A0: {
                return "KEY_VIDEOPHONE";
            }
            case 417: {
                return "KEY_GAMES";
            }
            case 418: {
                return "KEY_ZOOMIN";
            }
            case 419: {
                return "KEY_ZOOMOUT";
            }
            case 420: {
                return "KEY_ZOOMRESET";
            }
            case 421: {
                return "KEY_WORDPROCESSOR";
            }
            case 422: {
                return "KEY_EDITOR";
            }
            case 423: {
                return "KEY_SPREADSHEET";
            }
            case 424: {
                return "KEY_GRAPHICSEDITOR";
            }
            case 425: {
                return "KEY_PRESENTATION";
            }
            case 426: {
                return "KEY_DATABASE";
            }
            case 427: {
                return "KEY_NEWS";
            }
            case 428: {
                return "KEY_VOICEMAIL";
            }
            case 429: {
                return "KEY_ADDRESSBOOK";
            }
            case 430: {
                return "KEY_MESSENGER";
            }
            case 0x1AF: {
                return "KEY_DISPLAYTOGGLE";
            }
            case 0x1B0: {
                return "KEY_SPELLCHECK";
            }
            case 433: {
                return "KEY_LOGOFF";
            }
            case 434: {
                return "KEY_DOLLAR";
            }
            case 435: {
                return "KEY_EURO";
            }
            case 436: {
                return "KEY_FRAMEBACK";
            }
            case 437: {
                return "KEY_FRAMEFORWARD";
            }
            case 438: {
                return "KEY_CONTEXT_MENU";
            }
            case 439: {
                return "KEY_MEDIA_REPEAT";
            }
            case 440: {
                return "KEY_10CHANNELSUP";
            }
            case 441: {
                return "KEY_10CHANNELSDOWN";
            }
            case 442: {
                return "KEY_IMAGES";
            }
            case 0x1C0: {
                return "KEY_DEL_EOL";
            }
            case 449: {
                return "KEY_DEL_EOS";
            }
            case 450: {
                return "KEY_INS_LINE";
            }
            case 451: {
                return "KEY_DEL_LINE";
            }
            case 0x1D0: {
                return "KEY_FN";
            }
            case 465: {
                return "KEY_FN_ESC";
            }
            case 466: {
                return "KEY_FN_F1";
            }
            case 467: {
                return "KEY_FN_F2";
            }
            case 468: {
                return "KEY_FN_F3";
            }
            case 469: {
                return "KEY_FN_F4";
            }
            case 470: {
                return "KEY_FN_F5";
            }
            case 471: {
                return "KEY_FN_F6";
            }
            case 472: {
                return "KEY_FN_F7";
            }
            case 473: {
                return "KEY_FN_F8";
            }
            case 474: {
                return "KEY_FN_F9";
            }
            case 475: {
                return "KEY_FN_F10";
            }
            case 476: {
                return "KEY_FN_F11";
            }
            case 477: {
                return "KEY_FN_F12";
            }
            case 478: {
                return "KEY_FN_1";
            }
            case 0x1DF: {
                return "KEY_FN_2";
            }
            case 480: {
                return "KEY_FN_D";
            }
            case 481: {
                return "KEY_FN_E";
            }
            case 482: {
                return "KEY_FN_F";
            }
            case 483: {
                return "KEY_FN_S";
            }
            case 484: {
                return "KEY_FN_B";
            }
            case 0x1F1: {
                return "KEY_BRL_DOT1";
            }
            case 0x1F2: {
                return "KEY_BRL_DOT2";
            }
            case 0x1F3: {
                return "KEY_BRL_DOT3";
            }
            case 500: {
                return "KEY_BRL_DOT4";
            }
            case 501: {
                return "KEY_BRL_DOT5";
            }
            case 502: {
                return "KEY_BRL_DOT6";
            }
            case 503: {
                return "KEY_BRL_DOT7";
            }
            case 504: {
                return "KEY_BRL_DOT8";
            }
            case 505: {
                return "KEY_BRL_DOT9";
            }
            case 506: {
                return "KEY_BRL_DOT10";
            }
            case 0x200: {
                return "KEY_NUMERIC_0";
            }
            case 0x201: {
                return "KEY_NUMERIC_1";
            }
            case 0x202: {
                return "KEY_NUMERIC_2";
            }
            case 0x203: {
                return "KEY_NUMERIC_3";
            }
            case 0x204: {
                return "KEY_NUMERIC_4";
            }
            case 0x205: {
                return "KEY_NUMERIC_5";
            }
            case 0x206: {
                return "KEY_NUMERIC_6";
            }
            case 0x207: {
                return "KEY_NUMERIC_7";
            }
            case 520: {
                return "KEY_NUMERIC_8";
            }
            case 0x209: {
                return "KEY_NUMERIC_9";
            }
            case 0x20A: {
                return "KEY_NUMERIC_STAR";
            }
            case 0x20B: {
                return "KEY_NUMERIC_POUND";
            }
            case 0x210: {
                return "KEY_CAMERA_FOCUS";
            }
            case 529: {
                return "KEY_WPS_BUTTON";
            }
            case 530: {
                return "KEY_TOUCHPAD_TOGGLE";
            }
            case 531: {
                return "KEY_TOUCHPAD_ON";
            }
            case 532: {
                return "KEY_TOUCHPAD_OFF";
            }
            case 533: {
                return "KEY_CAMERA_ZOOMIN";
            }
            case 534: {
                return "KEY_CAMERA_ZOOMOUT";
            }
            case 535: {
                return "KEY_CAMERA_UP";
            }
            case 536: {
                return "KEY_CAMERA_DOWN";
            }
            case 537: {
                return "KEY_CAMERA_LEFT";
            }
            case 538: {
                return "KEY_CAMERA_RIGHT";
            }
            case 539: {
                return "KEY_ATTENDANT_ON";
            }
            case 540: {
                return "KEY_ATTENDANT_OFF";
            }
            case 541: {
                return "KEY_ATTENDANT_TOGGLE";
            }
            case 542: {
                return "KEY_LIGHTS_TOGGLE";
            }
            case 0x220: {
                return "BTN_DPAD_UP";
            }
            case 545: {
                return "BTN_DPAD_DOWN";
            }
            case 546: {
                return "BTN_DPAD_LEFT";
            }
            case 547: {
                return "BTN_DPAD_RIGHT";
            }
            case 560: {
                return "KEY_ALS_TOGGLE";
            }
            case 0x240: {
                return "KEY_BUTTONCONFIG";
            }
            case 577: {
                return "KEY_TASKMANAGER";
            }
            case 578: {
                return "KEY_JOURNAL";
            }
            case 579: {
                return "KEY_CONTROLPANEL";
            }
            case 580: {
                return "KEY_APPSELECT";
            }
            case 581: {
                return "KEY_SCREENSAVER";
            }
            case 582: {
                return "KEY_VOICECOMMAND";
            }
            case 0x250: {
                return "KEY_BRIGHTNESS_MIN";
            }
            case 593: {
                return "KEY_BRIGHTNESS_MAX";
            }
            case 704: {
                return "BTN_TRIGGER_HAPPY1";
            }
            case 705: {
                return "BTN_TRIGGER_HAPPY2";
            }
            case 706: {
                return "BTN_TRIGGER_HAPPY3";
            }
            case 707: {
                return "BTN_TRIGGER_HAPPY4";
            }
            case 708: {
                return "BTN_TRIGGER_HAPPY5";
            }
            case 709: {
                return "BTN_TRIGGER_HAPPY6";
            }
            case 710: {
                return "BTN_TRIGGER_HAPPY7";
            }
            case 711: {
                return "BTN_TRIGGER_HAPPY8";
            }
            case 712: {
                return "BTN_TRIGGER_HAPPY9";
            }
            case 713: {
                return "BTN_TRIGGER_HAPPY10";
            }
            case 714: {
                return "BTN_TRIGGER_HAPPY11";
            }
            case 715: {
                return "BTN_TRIGGER_HAPPY12";
            }
            case 716: {
                return "BTN_TRIGGER_HAPPY13";
            }
            case 717: {
                return "BTN_TRIGGER_HAPPY14";
            }
            case 718: {
                return "BTN_TRIGGER_HAPPY15";
            }
            case 0x2CF: {
                return "BTN_TRIGGER_HAPPY16";
            }
            case 720: {
                return "BTN_TRIGGER_HAPPY17";
            }
            case 721: {
                return "BTN_TRIGGER_HAPPY18";
            }
            case 722: {
                return "BTN_TRIGGER_HAPPY19";
            }
            case 723: {
                return "BTN_TRIGGER_HAPPY20";
            }
            case 724: {
                return "BTN_TRIGGER_HAPPY21";
            }
            case 725: {
                return "BTN_TRIGGER_HAPPY22";
            }
            case 726: {
                return "BTN_TRIGGER_HAPPY23";
            }
            case 727: {
                return "BTN_TRIGGER_HAPPY24";
            }
            case 728: {
                return "BTN_TRIGGER_HAPPY25";
            }
            case 729: {
                return "BTN_TRIGGER_HAPPY26";
            }
            case 730: {
                return "BTN_TRIGGER_HAPPY27";
            }
            case 731: {
                return "BTN_TRIGGER_HAPPY28";
            }
            case 732: {
                return "BTN_TRIGGER_HAPPY29";
            }
            case 733: {
                return "BTN_TRIGGER_HAPPY30";
            }
            case 734: {
                return "BTN_TRIGGER_HAPPY31";
            }
            case 0x2DF: {
                return "BTN_TRIGGER_HAPPY32";
            }
            case 0x2E0: {
                return "BTN_TRIGGER_HAPPY33";
            }
            case 737: {
                return "BTN_TRIGGER_HAPPY34";
            }
            case 738: {
                return "BTN_TRIGGER_HAPPY35";
            }
            case 739: {
                return "BTN_TRIGGER_HAPPY36";
            }
            case 740: {
                return "BTN_TRIGGER_HAPPY37";
            }
            case 741: {
                return "BTN_TRIGGER_HAPPY38";
            }
            case 742: {
                return "BTN_TRIGGER_HAPPY39";
            }
            case 743: {
                return "BTN_TRIGGER_HAPPY40";
            }
            default: {
                return "UNKNOWN_" + id;
            }
        }
    }

    private static String getLoadedPackage() {
        ProcessInfo info = MainService.instance.processInfo;
        return info == null ? null : info.packageName;
    }

    public static void load() {
        boolean needSave = false;
        SharedPreferences sharedPreferences0 = Tools.getSharedPreferences();
        Config.load(sharedPreferences0);
        String s = Config.getLoadedPackage();
        int i = 0;
        while(i < Config.list.size()) {
            Option option = (Option)Config.list.valueAt(i);
            String key = option.storageKey;
            if(!option.appDependent) {
                option.value = sharedPreferences0.getInt(key, option.defaultValue);
                goto label_19;
                try {
                label_14:
                    option.value = sharedPreferences0.getBoolean(option.storageKey, option.defaultValue == 1) ? 1 : 0;
                    needSave = true;
                }
                catch(ClassCastException ex) {
                    Log.e(("Failed load data for " + option.storageKey), ex);
                }
            }
            else if(s != null) {
                try {
                    option.value = sharedPreferences0.getInt(String.valueOf(s) + '-' + key, option.defaultValue);
                    goto label_19;
                }
                catch(ClassCastException unused_ex) {
                }
                goto label_14;
            }
        label_19:
            ++i;
        }
        if(needSave) {
            Config.save();
            return;
        }
        Config.notifyChanges();
    }

    private static void load(SharedPreferences storage) {
        Config.ignore = storage.getLong("ignore", 0L);
        Config.memoryFrom = storage.getLong("memory-from", 0L);
        Config.memoryTo = storage.getLong("memory-to", -1L);
        Config.nearbyDistance = storage.getLong("nearby-distance", 0x200L);
        for(int i = 0; i < Config.toolbarButtons.length; ++i) {
            Config.toolbarButtons[i] = storage.getLong("toolbar-buttons-" + i, -1L);
        }
        if(MainService.created) {
            MainService.instance.updateToolbars();
        }
    }

    private static void notifyChanges() {
        Config.updateConst();
        if(MainService.instance != null) {
            if(MainService.instance.mConfigListView != null) {
                MainService.instance.notifyDataSetChanged(MainService.instance.mConfigListView.getAdapter());
                MainService.instance.setupSavedListUpdates();
            }
            MainService.instance.mDaemonManager.sendConfig();
            MainService.instance.updateStatusBar();
        }
    }

    public static void save() {
        SPEditor editor = new SPEditor();
        Config.save(editor);
        String s = Config.getLoadedPackage();
        for(int i = 0; i < Config.list.size(); ++i) {
            Option option = (Option)Config.list.valueAt(i);
            String key = option.storageKey;
            if(!option.appDependent) {
                editor.putInt(key, option.value, option.defaultValue);
            }
            else if(s != null) {
                editor.putInt(String.valueOf(s) + '-' + key, option.value, option.defaultValue);
            }
        }
        editor.commit();
        Config.notifyChanges();
    }

    private static void save(SPEditor editor) {
        editor.putLong("ignore", Config.ignore, 0L);
        editor.putLong("memory-from", Config.memoryFrom, 0L);
        editor.putLong("memory-to", Config.memoryTo, -1L);
        editor.putLong("nearby-distance", Config.nearbyDistance, 0x200L);
        for(int i = 0; i < Config.toolbarButtons.length; ++i) {
            editor.putLong("toolbar-buttons-" + i, Config.toolbarButtons[i], -1L);
        }
    }

    public static ImageView setIconSize(ImageView button) {
        int v = Tools.dp2px48();
        return Config.setIconSize(button, v, v);
    }

    public static ImageView setIconSize(ImageView button, int iconSize) {
        int v1 = Tools.dp2px48();
        return Config.setIconSize(button, v1, v1, iconSize);
    }

    public static ImageView setIconSize(ImageView button, int width, int height) {
        return Config.setIconSize(button, width, height, Config.getIconSize());
    }

    public static ImageView setIconSize(ImageView button, int width, int height, int size) {
        int padY = 0;
        button.setAdjustViewBounds(false);
        button.setMinimumWidth(width);
        button.setMinimumHeight(height);
        int iconSize = (int)Tools.dp2px(size);
        int padX = width <= iconSize ? 0 : (width - iconSize) / 2;
        if(height > iconSize) {
            padY = (height - iconSize) / 2;
        }
        button.setPadding(padX, padY, padX, padY);
        try {
            button.setScaleType(ImageView.ScaleType.FIT_CENTER);
        }
        catch(Throwable e) {
            Log.badImplementation(e);
        }
        button.invalidate();
        return button;
    }

    public static ImageView setIconSizeEx(ImageView button, int size) {
        button.setAdjustViewBounds(false);
        button.setMinimumWidth(size);
        button.setMinimumHeight(size);
        ViewGroup.LayoutParams viewGroup$LayoutParams0 = button.getLayoutParams();
        if(viewGroup$LayoutParams0 != null) {
            viewGroup$LayoutParams0.width = size;
            viewGroup$LayoutParams0.height = size;
            button.setLayoutParams(viewGroup$LayoutParams0);
        }
        button.setScaleType(ImageView.ScaleType.FIT_CENTER);
        button.invalidate();
        return button;
    }

    public static void updateConst() {
        SparseArray list = Config.list;
        int flags = ((Option)list.get(0x7F0B0091)).value == 1 ? 2 : 0;  // id:config_reset_on_exit
        if(((Option)list.get(0x7F0B00BA)).value == 1) {  // id:selinux
            flags |= 4;
        }
        if(((Option)list.get(0x7F0B0095)).value == 0) {  // id:config_deep_read
            flags |= 0x10000;
        }
        if(((Option)list.get(0x7F0B0096)).value == 1) {  // id:config_calls
            flags |= 0x800;
        }
        if(((Option)list.get(0x7F0B0097)).value == 1) {  // id:config_waitpid
            flags |= 0x1000;
        }
        if(((Option)list.get(0x7F0B008A)).value == 1) {  // id:config_fast_freeze
            flags |= 0x40000;
        }
        int value = ((Option)list.get(0x7F0B0089)).value;  // id:config_skip_memory
        if(value == 1) {
            flags |= 1;
        }
        if(value == 2) {
            flags |= 0x401;
        }
        int value = ((Option)list.get(0x7F0B0094)).value;  // id:config_memory_access
        if(value == 1) {
            flags |= 8;
        }
        if(value == 2) {
            flags |= 16;
        }
        int value = ((Option)list.get(0x7F0B009B)).value;  // id:config_prevent_unload
        if(value == 1) {
            flags |= 0x20;
        }
        if(value == 2 || value == 3) {
            flags |= 0x40;
        }
        int value = ((Option)list.get(0x7F0B0087)).value;  // id:config_hide_from_game
        if((value & 1) != 0) {
            flags |= 0x80;
        }
        if((value & 2) != 0) {
            flags |= 0x100;
        }
        if((value & 4) != 0) {
            flags |= 0x200;
        }
        if((value & 8) != 0) {
            flags |= 0x8000;
        }
        int value = ((Option)list.get(0x7F0B0088)).value;  // id:config_ptrace_bypass_mode
        if(value == 1) {
            flags |= 0x2000;
        }
        if(value == 2) {
            flags |= 0x4000;
        }
        if(value == 3) {
            flags |= 0x20000;
        }
        Config.configDaemon = flags;
        int flags = ((Option)list.get(0x7F0B009F)).value == 1 ? 1 : 0;  // id:config_keyboard
        if(((Option)list.get(0x7F0B00AE)).value == 1) {  // id:config_acceleration
            flags |= 2;
        }
        if(((Option)list.get(0x7F0B008C)).value == 1) {  // id:config_autopause
            flags |= 4;
        }
        if(((Option)list.get(0x7F0B00A0)).value == 1) {  // id:config_suggestions
            flags |= 8;
        }
        if(((Option)list.get(0x7F0B00A2)).value == 1) {  // id:config_indent
            flags |= 16;
        }
        if(((Option)list.get(0x7F0B00AF)).value == 1) {  // id:config_use_sound_effects
            flags |= 0x20;
        }
        if(((Option)list.get(0x7F0B00BF)).value == 1) {  // id:config_kbd_small
            flags |= 0x40;
        }
        if(((Option)list.get(0x7F0B0084)).value == 1) {  // id:config_time_jump_panel
            flags |= 0x80;
        }
        if(((Option)list.get(0x7F0B009C)).value == 1) {  // id:config_use_notification
            flags |= 0x100;
        }
        if(((Option)list.get(0x7F0B0092)).value == 1) {  // id:config_check_libs
            flags |= 0x800;
        }
        if(((Option)list.get(0x7F0B00A1)).value == 1) {  // id:config_ignore_unknown_chars
            flags |= 0x1000;
        }
        if(((Option)list.get(0x7F0B009B)).value == 3) {  // id:config_prevent_unload
            flags |= 0x2000;
        }
        if(((Option)list.get(0x7F0B009A)).value == 1) {  // id:config_vspace_root
            flags |= 0x200;
        }
        if(((Option)list.get(0x7F0B00A3)).value == 1) {  // id:config_visible_type
            flags |= 0x400;
        }
        Config.configClient = flags;
        ((Option)list.get(0x7F0B00B1)).init();  // id:config_number_locale
        Config.fillToolbar = ((Option)list.get(0x7F0B00A8)).value;  // id:config_fill_toolbar
        Config.dataInRam = ((Option)list.get(0x7F0B0093)).value;  // id:config_ram
        Config.usedRanges = ((Option)list.get(0x7F0B0081)).value;  // id:config_ranges
        Config.searchHelper = ((Option)list.get(0x7F0B008D)).value;  // id:config_search_helper
        Config.speedsParams = ((Option)list.get(0x7F0B0090)).value;  // id:config_speeds_params
        Config.freezeInterval = ((Option)list.get(0x7F0B008B)).value;  // id:config_freeze_interval
        Config.savedListUpdatesInterval = ((Option)list.get(0x7F0B008E)).value;  // id:config_saved_list_updates_interval
        Config.iconsSize = ((Option)list.get(0x7F0B00A9)).value;  // id:config_icons_size
        Config.toolbars = ((Option)list.get(0x7F0B00A6)).value;  // id:config_toolbars
        Config.smallItems = ((Option)list.get(0x7F0B00A4)).value;  // id:config_use_small_list_items
        Config.backgrounds = ((Option)list.get(0x7F0B00A5)).value;  // id:config_backgrounds
        Config.xorKey = ((Option)list.get(0x7F0B00BB)).value;  // id:config_xor_key
        Config.floatType = ((Option)list.get(0x7F0B00BD)).value;  // id:config_float_type
        Config.floatFlags = ((Option)list.get(0x7F0B00BE)).value;  // id:config_float_flags
        Config.timeJumpLast = ((Option)list.get(0x7F0B00C0)).value;  // id:config_time_jump_last
        Config.hotKey = ((Option)list.get(0x7F0B009D)).value;  // id:config_hot_key
        Config.historyLimit = ((Option)list.get(0x7F0B009E)).value;  // id:config_history_limit
        Config.copyParams = ((Option)list.get(0x7F0B00C1)).value;  // id:config_copy_params
        Config.contextSource = ((Option)list.get(0x7F0B00BC)).value;  // id:config_context_source
        Config.interceptTimers = ((long)((Option)list.get(0x7F0B0082)).value) & 0x1FFFFFL | ((long)((Option)list.get(0x7F0B0085)).value) << 21;  // id:config_speedhack_intercept
    }

    public static void useKeys(int[] ids) {
        int j;
        if(ids == null || ids.length == 0) {
            return;
        }
        int[] prev = Config.used;
        int nl = prev.length + ids.length;
        int[] next = new int[nl];
        System.arraycopy(prev, 0, next, 0, prev.length);
        System.arraycopy(ids, 0, next, prev.length, ids.length);
        Arrays.sort(next);
        int i = 1;
        int j;
        for(j = 1; i < nl; j = j) {
            if(next[i] == next[i - 1]) {
                j = j;
            }
            else {
                j = j + 1;
                next[j] = next[i];
            }
            ++i;
        }
        Config.used = Arrays.copyOf(next, j);
    }
}

