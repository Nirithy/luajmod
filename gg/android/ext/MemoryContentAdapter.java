package android.ext;

import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.DialogInterface;
import android.fix.LayoutInflater;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseIntArray;
import android.view.View.OnFocusChangeListener;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.CompoundButton;
import android.widget.Filter.FilterResults;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.SectionIndexer;
import android.widget.TextView;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.Locale;
import java.util.regex.Pattern;

class MemoryContentAdapter extends BaseAdapterLC implements Listener, Filterable, SectionIndexer {
    class FilterMenuItem extends MenuItem implements DialogInterface.OnClickListener, TextWatcher, CompoundButton.OnCheckedChangeListener {
        boolean ext;
        private WeakReference weakEdit;
        private WeakReference weakEnds;
        private WeakReference weakStarts;

        public FilterMenuItem() {
            super(0x7F070090, 0x7F020024);  // string:filter "Filter"
            this.weakEdit = new WeakReference(null);
            this.weakStarts = new WeakReference(null);
            this.weakEnds = new WeakReference(null);
            this.ext = false;
        }

        @Override  // android.text.TextWatcher
        public void afterTextChanged(Editable s) {
            boolean z = true;
            String s = s.toString();
            CheckBox starts = (CheckBox)this.weakStarts.get();
            if(starts != null) {
                starts.setChecked(s.indexOf(94) != -1);
                CheckBox ends = (CheckBox)this.weakEnds.get();
                if(ends != null) {
                    if(s.indexOf(36) == -1) {
                        z = false;
                    }
                    ends.setChecked(z);
                }
            }
        }

        @Override  // android.text.TextWatcher
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override  // android.widget.CompoundButton$OnCheckedChangeListener
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(buttonView != null) {
                EditText edit = (EditText)this.weakEdit.get();
                if(edit != null) {
                    switch(buttonView.getId()) {
                        case 0x7F0B0043: {  // id:starts
                            String s = edit.getText().toString();
                            if(s.contains("^") != isChecked) {
                                if(isChecked) {
                                    edit.getText().replace(0, 0, "^");
                                    return;
                                }
                                int v = s.indexOf("^");
                                edit.getText().replace(v, v + 1, "");
                                return;
                            }
                            break;
                        }
                        case 0x7F0B0044: {  // id:ends
                            String s1 = edit.getText().toString();
                            if(s1.contains("$") != isChecked) {
                                if(isChecked) {
                                    edit.getText().replace(s1.length(), s1.length(), "$");
                                    return;
                                }
                                int v1 = s1.indexOf("$");
                                edit.getText().replace(v1, v1 + 1, "");
                                return;
                            }
                            break;
                        }
                        case 0x7F0B0045: {  // id:external
                            this.ext = isChecked;
                            View.OnFocusChangeListener view$OnFocusChangeListener0 = edit.getOnFocusChangeListener();
                            if(view$OnFocusChangeListener0 != null) {
                                view$OnFocusChangeListener0.onFocusChange(edit, true);
                                return;
                            }
                            break;
                        }
                    }
                }
            }
        }

        @Override  // android.content.DialogInterface$OnClickListener
        public void onClick(DialogInterface dialog, int which) {
            String filter;
            if(which == -1) {
                EditText edit = (EditText)this.weakEdit.get();
                if(edit == null) {
                    return;
                }
                filter = edit.getText().toString();
                if(filter.length() > 0) {
                    History.add(filter, (this.ext ? 2 : 1));
                }
            }
            else {
                filter = "";
            }
            MemoryContentAdapter.this.filterString = filter;
            MainService.instance.mFilter.setText((filter.length() <= 0 ? Re.s(0x7F070262) : Tools.stripColon(0x7F070090) + ": " + filter));  // string:no_filter "No filter."
            MemoryContentAdapter.this.notifyDataSetChanged();
        }

        @Override  // android.ext.MenuItem
        public void onClick(View view) {
            if(view != null && view.getId() == 0x7F0B000E) {  // id:message
                Alert.show(Alert.create().setMessage(Re.s(0x7F07018F) + ":\n" + Re.s(0x7F070042)).setPositiveButton(Re.s(0x7F07012B), new DialogInterface.OnClickListener() {  // string:examples "Examples"
                    @Override  // android.content.DialogInterface$OnClickListener
                    public void onClick(DialogInterface dialog, int which) {
                        ConfigListAdapter.showHelp(0x7F070264);  // string:help_memory_filter "* __help_memory_filter_title__:\nYou can specify a filter 
                                                                 // in the memory editor. Only those results that match the filter will be displayed.\nThe 
                                                                 // filter supports wildcards:\n\t__caret__ - the start of the data,\n\t__dollar__ - 
                                                                 // the end of the data,\n\t__asterisk__ - any number of any characters,\n\t__question__ 
                                                                 // - the one any character.\nFor example, to find the number 42, you need to enter 
                                                                 // __caret__42__dollar__."
                    }
                }).setNegativeButton(Re.s(0x7F0700A1), null));  // string:cancel "Cancel"
                return;
            }
            View view1 = LayoutInflater.inflateStatic(0x7F04000C, null);  // layout:memory_filter
            TextView message = (TextView)view1.findViewById(0x7F0B000E);  // id:message
            Tools.setButtonHelpBackground(message);
            message.setOnClickListener(this);
            CheckBox external = (CheckBox)view1.findViewById(0x7F0B0045);  // id:external
            external.setChecked(this.ext);
            external.setText(Re.s(0x7F070041));  // string:keyboard_external "__keyboard__ __external__"
            external.setOnCheckedChangeListener(this);
            CheckBox starts = (CheckBox)view1.findViewById(0x7F0B0043);  // id:starts
            starts.setOnCheckedChangeListener(this);
            this.weakStarts = new WeakReference(starts);
            CheckBox ends = (CheckBox)view1.findViewById(0x7F0B0044);  // id:ends
            ends.setOnCheckedChangeListener(this);
            this.weakEnds = new WeakReference(ends);
            String filter = MemoryContentAdapter.this.filterString == null ? "" : MemoryContentAdapter.this.filterString;
            android.ext.MemoryContentAdapter.FilterMenuItem.2 focus = new FocusListener() {
                @Override  // android.ext.InternalKeyboard$FocusListener
                protected boolean useExternal(View v, boolean hasFocus) {
                    return FilterMenuItem.this.ext;
                }
            };
            EditText edit = (EditText)view1.findViewById(0x7F0B0041);  // id:filter
            edit.setText(filter);
            edit.setDataType(3);
            Tools.setOnFocusChangeListener(edit, focus);
            edit.addTextChangedListener(this);
            this.weakEdit = new WeakReference(edit);
            this.afterTextChanged(edit.getText());
            ((HexConverter)view1.findViewById(0x7F0B0042)).setTag(edit);  // id:number_converter
            view1.setTag(new Flags(15));
            Alert.show(Alert.create().setView(InternalKeyboard.getView(view1)).setPositiveButton(0x7F07009D, this).setNegativeButton(0x7F0700A1, null).setNeutralButton(0x7F070091, this).create(), edit);  // string:ok "OK"
        }

        @Override  // android.text.TextWatcher
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
    }

    class MemoryFilter extends Filter {
        int[] mArgs;
        ArmDis mRet;

        private MemoryFilter() {
            this.mRet = null;
            this.mArgs = null;
        }

        MemoryFilter(MemoryFilter memoryContentAdapter$MemoryFilter0) {
        }

        @Override  // android.widget.Filter
        protected Filter.FilterResults performFiltering(CharSequence constraint) {
            int j;
            int[] args;
            ArmDis ret;
            Filter.FilterResults results = new Filter.FilterResults();
            results.values = null;
            try {
                boolean noText = (MemoryContentAdapter.this.showMask & 12) == 0;
                if(constraint != null && constraint.length() > 0 && MemoryContentAdapter.this.isDataGood()) {
                    String check = Tools.quoteFilter((noText ? this.prepareNoText(constraint.toString()) : constraint.toString()));
                    Log.d(("Filter: \'" + check + '\''));
                    Pattern pattern0 = Pattern.compile(check, 8);
                    if((MemoryContentAdapter.this.showMask & 0x30) == 0) {
                        ret = null;
                    }
                    else if(this.mRet == null) {
                        ret = new ArmDis();
                        this.mRet = ret;
                    }
                    else {
                        ret = this.mRet;
                    }
                    if((MemoryContentAdapter.this.showMask & 0x40) == 0) {
                        args = null;
                    }
                    else if(this.mArgs == null) {
                        args = Arm64Dis.args();
                        this.mArgs = args;
                    }
                    else {
                        args = this.mArgs;
                    }
                    int max = (short)MemoryContentAdapter.this.getRawCountItems();
                    short[] filtered = new short[max];
                    short i = 0;
                    for(int j = 0; true; j = j) {
                        if(i >= max) {
                            results.values = Arrays.copyOf(filtered, j);
                            return results;
                        }
                        String str = MemoryContentAdapter.this.getString(ret, args, ((int)i));
                        if(noText) {
                            str = this.prepareNoText(str);
                        }
                        if(pattern0.matcher(str).find()) {
                            j = j + 1;
                            filtered[j] = i;
                        }
                        else {
                            j = j;
                        }
                        i = (short)(i + 1);
                    }
                }
            }
            catch(Throwable e) {
                Log.d("Failed filter", e);
                ExceptionHandler.sendException(Thread.currentThread(), e, false);
            }
            return results;
        }

        private String prepareNoText(String in) {
            return in.toLowerCase(Locale.US).replace(ParserNumbers.thousandSeparator, "");
        }

        @Override  // android.widget.Filter
        protected void publishResults(CharSequence constraint, Filter.FilterResults results) {
            MemoryContentAdapter.this.filtered = (short[])results.values;
            MemoryContentAdapter.this.superNotifyDataSetChanged();
        }
    }

    static class OldChecks {
        short[] filtered;
        int shift;

        private OldChecks(int shift, short[] filtered) {
            this.shift = shift;
            this.filtered = filtered;
        }

        OldChecks(int v, short[] arr_v, OldChecks memoryContentAdapter$OldChecks0) {
            this(v, arr_v);
        }
    }

    private static final int AREA = 0x2000;
    static final int F_ARM = 4;
    static final int F_ARM8 = 6;
    private static final int F_BYTE = 11;
    private static final int F_DOUBLE = 9;
    private static final int F_DWORD = 7;
    private static final int F_FLOAT = 8;
    static final int F_HEX = 0;
    static final int F_JAVA = 3;
    private static final int F_OFFSET = 7;
    private static final int F_QWORD = 12;
    static final int F_RHEX = 1;
    static final int F_STR = 2;
    static final int F_THUMB = 5;
    private static final int F_WORD = 10;
    private static final int F_XOR = 13;
    private static final int MASK_DEFAULT = 0xFFFFC181;
    private static final String MEMORY_EDITOR_FORMAT = "memory-editor-format";
    private static final int PAGES = 2;
    private static final int PAGE_SIZE = 0x1000;
    private static final int SECTIONS_COUNT = 0x20;
    private static final String VALUE_SEPARATOR = "\n";
    private ByteBuffer buffer;
    private int cacheFlags;
    private int cacheMask;
    private final CharSequence[] cacheRegion;
    private long cacheRegionOffset;
    private int cacheShift;
    private long cacheShiftCheck;
    private boolean[] checks;
    private static volatile int[] colors;
    private final byte[] data;
    private volatile long dataOffset;
    private Filter filter;
    private FilterMenuItem filterMenuItem;
    String filterString;
    short[] filtered;
    private static int[] mArgs;
    private static ArmDis mRet;
    private long offsetBuffer;
    private OldChecks oldChecks;
    private final Result result;
    private int selected;
    private static volatile CharSequence[] shortNames;
    volatile int showMask;

    static {
        MemoryContentAdapter.mRet = null;
        MemoryContentAdapter.mArgs = null;
    }

    public MemoryContentAdapter() {
        this.filterString = "";
        this.filter = null;
        this.filtered = null;
        this.selected = -1;
        this.checks = null;
        this.showMask = 0xFFFFC181;
        this.oldChecks = null;
        this.cacheShift = 2;
        this.cacheShiftCheck = 0L;
        this.result = new Result();
        this.cacheRegion = new CharSequence[2];
        this.cacheRegionOffset = 1L;
        this.cacheFlags = 0;
        this.cacheMask = 0;
        this.filterMenuItem = null;
        this.showMask = Tools.getSharedPreferences().getInt("memory-editor-format", 0xFFFFC181);
        this.checkCount();
        this.data = new byte[8200];
        this.buffer = ByteBuffer.wrap(this.data);
        this.buffer.order(ByteOrder.LITTLE_ENDIAN);
        this.offsetBuffer = 0L;
        this.dataOffset = 0L;
        MainService.instance.mValueFormat.setText(this.getValueFormat());
    }

    private void checkCount() {
        int v = this.getCount();
        boolean[] checks = this.checks;
        OldChecks old = this.oldChecks;
        int v1 = this.getShift();
        short[] filtered = this.filtered;
        if(checks == null || v != checks.length || old == null || v1 != old.shift || filtered != this.filtered) {
            boolean[] arr_z1 = new boolean[v];
            this.checks = arr_z1;
            if(old != null) {
                int oldShift = old.shift;
                short[] oldFiltered = old.filtered;
                for(int i = 1; i < v - 1; ++i) {
                    int j = MemoryContentAdapter.pos2index(i - 1, filtered, v1) << v1;
                    if((j & (1 << oldShift) - 1) == 0) {
                        int j = j >> oldShift;
                        int v6 = MemoryContentAdapter.index2pos(j, oldFiltered);
                        if(j == MemoryContentAdapter.pos2index(v6, oldFiltered, oldShift) && v6 + 1 >= 0 && v6 + 1 < checks.length && checks[v6 + 1]) {
                            arr_z1[i] = true;
                        }
                    }
                }
            }
            this.oldChecks = new OldChecks(v1, filtered, null);
        }
    }

    public void chooseValueFormat() {
        boolean savedList = MainService.instance.getTabIndex() == 2;
        CharSequence[] list = MemoryContentAdapter.getNames();
        int mask = this.showMask;
        if(savedList) {
            list = (CharSequence[])Arrays.copyOf(list, 7);
            mask = SavedListAdapter.showMask;
        }
        boolean[] checked = new boolean[list.length];
        CharSequence[] arr_charSequence1 = MemoryContentAdapter.getShortNames();
        int[] arr_v = MemoryContentAdapter.getColors();
        for(int i = 0; i < list.length; ++i) {
            list[i] = Tools.colorize((arr_charSequence1[i] + ": " + list[i]), arr_v[i]);
            checked[i] = (1 << i & mask) != 0;
        }
        android.ext.MemoryContentAdapter.2 listener = new DialogInterface.OnClickListener() {
            @Override  // android.content.DialogInterface$OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                int newMask = savedList ? 0 : 0xFFFFC181;
                if(which == -1) {
                    for(int i = 0; i < checked.length; ++i) {
                        newMask = checked[i] ? newMask | 1 << i : newMask & ~(1 << i);
                    }
                }
                if(!savedList && ((1 << checked.length) - 1 & newMask) == 0) {
                    newMask |= 8;
                }
                if(savedList) {
                    SavedListAdapter.setShowMask(newMask);
                    return;
                }
                MemoryContentAdapter.this.updateShowMask(newMask);
            }
        };
        Alert.show(Alert.create(Tools.getDarkContext()).setCustomTitle(Tools.getCustomTitle(0x7F070133)).setMultiChoiceItems(list, checked, new DialogInterface.OnMultiChoiceClickListener() {  // string:value_format "Value format"
            @Override  // android.content.DialogInterface$OnMultiChoiceClickListener
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                checked[which] = isChecked;
            }
        }).setPositiveButton(Re.s(0x7F07009B), listener).setNeutralButton(Re.s(0x7F07023F), listener).setNegativeButton(Re.s(0x7F07009C), null));  // string:yes "Yes"
    }

    static void fillData(ViewHolderBase holder, long address, long data, int showMask, boolean goodData, int size) {
        CharSequence charSequence4;
        CharSequence charSequence3;
        String val;
        CharSequence charSequence2;
        int[] arr_v;
        CharSequence charSequence1;
        ArmDis armDis1;
        CharSequence charSequence0;
        ArmDis armDis0;
        TextView textView0 = holder.valueThumb;
        if((1L & address) == 0L && (showMask & 0x20) != 0) {
            if(MemoryContentAdapter.mRet == null) {
                armDis0 = new ArmDis();
                MemoryContentAdapter.mRet = armDis0;
            }
            else {
                armDis0 = MemoryContentAdapter.mRet;
            }
            charSequence0 = ArmDis.getThumbOpcode(armDis0, address, data) + ';';
        }
        else {
            charSequence0 = null;
        }
        int v4 = MemoryContentAdapter.setText(textView0, charSequence0, size, 2);
        TextView textView1 = holder.valueArm;
        if((3L & address) == 0L && (showMask & 16) != 0) {
            if(MemoryContentAdapter.mRet == null) {
                armDis1 = new ArmDis();
                MemoryContentAdapter.mRet = armDis1;
            }
            else {
                armDis1 = MemoryContentAdapter.mRet;
            }
            charSequence1 = ArmDis.getArmOpcode(armDis1, address, data) + ';';
        }
        else {
            charSequence1 = null;
        }
        int v5 = MemoryContentAdapter.setText(textView1, charSequence1, v4, 4);
        TextView textView2 = holder.valueArm8;
        if((3L & address) == 0L && (showMask & 0x40) != 0) {
            if(MemoryContentAdapter.mArgs == null) {
                arr_v = Arm64Dis.args();
                MemoryContentAdapter.mArgs = arr_v;
            }
            else {
                arr_v = MemoryContentAdapter.mArgs;
            }
            charSequence2 = Arm64Dis.disasm(arr_v, address, ((int)data), new StringBuilder()).append(';').toString();
        }
        else {
            charSequence2 = null;
        }
        Region region = null;
        int v6 = MemoryContentAdapter.setText(textView2, charSequence2, v5, 4);
        boolean skipRHex = false;
        if((showMask & 1) == 0) {
            MemoryContentAdapter.setText(holder.valueHex, null);
        }
        else {
            if(goodData) {
                if(v6 != 8) {
                    data &= (1L << v6 * 8) - 1L;
                }
                val = ToolsLight.prefixLongHex(v6 * 2, data) + "h;";
                ProcessInfo processInfo = MainService.instance.processInfo;
                if(v6 == 4 || processInfo != null && processInfo.x64 && v6 == 8) {
                    region = RegionList.getRegionItem(data);
                }
            }
            else {
                val = "?";
            }
            MemoryContentAdapter.setText(holder.valueHex, val);
            int v7 = Region.getPointerColor(region);
            holder.valueHex.setTextColor(v7);
            if(v6 == 1) {
                skipRHex = true;
            }
        }
        if(skipRHex || (showMask & 2) == 0) {
            MemoryContentAdapter.setText(holder.valueRHex, null);
        }
        else {
            String val = goodData ? ToolsLight.prefixLongHex(v6 * 2, (v6 == 8 ? Long.reverseBytes(data) >> 0x40 - v6 * 8 : Long.reverseBytes(data) >> 0x40 - v6 * 8 & (1L << v6 * 8) - 1L)) + "r;" : "?";
            MemoryContentAdapter.setText(holder.valueRHex, val);
        }
        TextView textView3 = holder.valueString;
        if((showMask & 4) == 0) {
            charSequence3 = null;
        }
        else {
            charSequence3 = goodData ? "\'" + MemoryContentAdapter.longToString(data, v6) + "\';" : "?";
        }
        MemoryContentAdapter.setText(textView3, charSequence3);
        TextView textView4 = holder.valueJava;
        if((showMask & 8) == 0) {
            charSequence4 = null;
        }
        else {
            charSequence4 = goodData ? "\"" + MemoryContentAdapter.longToJava(data, v6) + "\";" : "?";
        }
        MemoryContentAdapter.setText(textView4, charSequence4);
    }

    public void forceUpdateCache() {
        long v = this.getOffset();
        this.cacheRegionOffset = v;
        boolean changed = false;
        for(int i = 0; i < this.cacheRegion.length; ++i) {
            CharSequence charSequence0 = RegionList.getRegion(((long)i) * 0x1000L + v);
            if(!charSequence0.equals(this.cacheRegion[i])) {
                this.cacheRegion[i] = charSequence0;
                changed = true;
            }
        }
        if(changed) {
            this.notifyDataSetChanged();
        }
    }

    public long getAddr(int pos) {
        return this.getRawAddr(((long)this.pos2index(pos - 1)));
    }

    public boolean[] getChecks() {
        return this.checks;
    }

    static int[] getColors() {
        if(MemoryContentAdapter.colors == null) {
            int v = Tools.getColor(0x7F0A0014);  // color:region_code
            int[] arr_v = {-1, -1, -1, -1, v, Tools.getColor(0x7F0A0015), v, AddressItem.getColor(4), AddressItem.getColor(16), AddressItem.getColor(0x40), AddressItem.getColor(2), AddressItem.getColor(1), AddressItem.getColor(0x20), AddressItem.getColor(8)};  // color:region_system_code
            MemoryContentAdapter.colors = arr_v;
            return arr_v;
        }
        return MemoryContentAdapter.colors;
    }

    @Override  // android.widget.Adapter
    public int getCount() {
        return this.getCountItems() + 2;
    }

    private int getCountItems() {
        return this.filtered == null ? this.getRawCountItems() : this.filtered.length;
    }

    @Override  // android.widget.Filterable
    public Filter getFilter() {
        Filter filter = this.filter;
        if(filter == null) {
            filter = new MemoryFilter(this, null);
            this.filter = filter;
        }
        return filter;
    }

    public MenuItem getFilterMenuItem() {
        FilterMenuItem filterMenuItem = this.filterMenuItem;
        if(filterMenuItem == null) {
            filterMenuItem = new FilterMenuItem(this);
            this.filterMenuItem = filterMenuItem;
        }
        return filterMenuItem;
    }

    private int getFlags() {
        int mask = this.showMask;
        if(mask != this.cacheMask) {
            int ret = 0;
            for(int i = 0; i < 14; ++i) {
                if((1 << i & mask) != 0) {
                    ret |= new int[]{0, 0, 0, 0, 4, 2, 4, 4, 16, 0x40, 2, 1, 0x20, 8}[i];
                }
            }
            this.cacheMask = mask;
            this.cacheFlags = ret;
        }
        return this.cacheFlags;
    }

    @Override  // android.widget.Adapter
    public Object getItem(int pos) {
        if(pos == 0 || pos == this.getCount() - 1) {
            long[] arr_v = this.getSpecialOffset(pos != 0);
            return new GotoAddress(arr_v[0], arr_v[1]);
        }
        long v1 = this.getAddr(pos);
        int v2 = AddressItem.getTypeForAddress(v1, false);
        int v3 = this.getFlags();
        if((v2 & v3) != 0) {
            return new AddressItem(v1, this.buffer.getLong(((int)(0x1FFFL & v1))), v2 & v3);
        }
        int flags = AddressItem.getTypeForAddress(v1, true);
        return new AddressItem(v1, this.buffer.getLong(((int)(0x1FFFL & v1))), flags);
    }

    @Override  // android.widget.Adapter
    public long getItemId(int pos) {
        return this.getAddr(pos);
    }

    private int getJumpPos(long addr) {
        int v1 = this.getPos(addr);
        this.selected = v1;
        return v1;
    }

    static CharSequence[] getNames() {
        return new CharSequence[]{Re.s(0x7F070134), Re.s(0x7F070135), Re.s(0x7F070148), "UTF-16LE", Tools.stringFormat(Re.s(0x7F070313), new Object[]{"ARM (x32)"}), Tools.stringFormat(Re.s(0x7F070313), new Object[]{"Thumb"}), Tools.stringFormat(Re.s(0x7F070313), new Object[]{"ARM (x64)"}), Re.s(0x7F070009), Re.s(0x7F07000B), Re.s(0x7F07000D), Re.s(0x7F070008), Re.s(0x7F070007), Re.s(0x7F07000C), Re.s(0x7F07000A)};  // string:format_hex "Hex (little-endian) notation"
    }

    private View getNewView(ViewGroup parent) {
        View view0 = LayoutInflater.inflateStatic(0x7F040022, parent, false);  // layout:service_memory_item
        ViewHolderMemory viewHolder = new ViewHolderMemory(view0, this);
        viewHolder.valueDword = (TextView)view0.findViewById(0x7F0B0123);  // id:value_dword
        viewHolder.valueFloat = (TextView)view0.findViewById(0x7F0B0124);  // id:value_float
        viewHolder.valueDouble = (TextView)view0.findViewById(0x7F0B0125);  // id:value_double
        viewHolder.valueWord = (TextView)view0.findViewById(0x7F0B0126);  // id:value_word
        viewHolder.valueByte = (TextView)view0.findViewById(0x7F0B0127);  // id:value_byte
        viewHolder.valueQword = (TextView)view0.findViewById(0x7F0B0128);  // id:value_qword
        viewHolder.valueXor = (TextView)view0.findViewById(0x7F0B0129);  // id:value_xor
        return view0;
    }

    private int getOffset(long address) [...] // Inlined contents

    private long getOffset() {
        return this.offsetBuffer;
    }

    public int getPos(long addr) {
        return this.index2pos(((int)(addr - this.getOffset() >> this.getShift()))) + 1;
    }

    @Override  // android.widget.SectionIndexer
    public int getPositionForSection(int sectionIndex) {
        return this.getPos(this.offsetBuffer + ((long)sectionIndex) * 0x100L);
    }

    public int getPossibleTypes(boolean[] list, SparseIntArray counts) {
        int ret = 0;
        int s = list.length - 1;
        for(int i = 1; i < s; ++i) {
            if(list[i]) {
                ret |= AddressItem.getTypeForAddress(this.getAddr(i), false, counts);
            }
        }
        if((Config.configClient & 0x400) != 0) {
            int v3 = this.getFlags();
            return Integer.bitCount(v3) != 1 || (ret & v3) == 0 ? ret : ret & v3;
        }
        return ret;
    }

    public long getRawAddr(long index) {
        return (index << this.getShift()) + this.getOffset();
    }

    int getRawCountItems() {
        return 0x2000 >> this.getShift();
    }

    private CharSequence getRegion(long addr) {
        CharSequence[] cacheRegion = this.cacheRegion;
        long v1 = this.getOffset();
        if(this.cacheRegionOffset != v1) {
            this.cacheRegionOffset = v1;
            for(int i = 0; i < cacheRegion.length; ++i) {
                cacheRegion[i] = RegionList.getRegion(((long)i) * 0x1000L + v1);
            }
        }
        for(int i = 0; true; ++i) {
            if(i >= cacheRegion.length) {
                return "??";
            }
            if(Tools.unsignedLessOrEqual(addr, ((long)(i + 1)) * 0x1000L + v1 - 1L)) {
                return cacheRegion[i];
            }
        }
    }

    @Override  // android.widget.SectionIndexer
    public int getSectionForPosition(int pos) {
        int ret = (int)((this.getAddr(pos) - this.getOffset()) / 0x100L);
        if(ret < 0) {
            ret = 0;
        }
        return ret < 0x20 ? ret : 0x1F;
    }

    @Override  // android.widget.SectionIndexer
    public Object[] getSections() {
        Object[] ret = new Object[0x20];
        for(int i = 0; i < 0x20; ++i) {
            ret[i] = AddressItem.getStringAddress(this.offsetBuffer + ((long)i) * 0x100L, 4);
        }
        return ret;
    }

    private int getShift() {
        int shift = this.cacheShift;
        int mask = this.showMask;
        if((0x400000000L | ((long)mask)) != this.cacheShiftCheck) {
            if((mask & 0x800) == 0) {
                shift = (mask & 0x420) == 0 ? 2 : 1;
            }
            else {
                shift = 0;
            }
            this.cacheShift = shift;
            this.cacheShiftCheck = 0x400000000L | ((long)mask);
        }
        return shift;
    }

    private static CharSequence[] getShortNames() {
        if(MemoryContentAdapter.shortNames == null) {
            CharSequence[] arr_charSequence = {Re.s(0x7F07001A), Re.s(0x7F07001B), "S", "J", "A", "T", "A8", Re.s(0x7F070011), Re.s(0x7F070013), Re.s(0x7F070015), Re.s(0x7F070010), Re.s(0x7F07000F), Re.s(0x7F070014), Re.s(0x7F070012)};  // string:hex "h"
            int[] arr_v = MemoryContentAdapter.getColors();
            for(int i = 0; i < 14; ++i) {
                arr_charSequence[i] = Tools.colorize(arr_charSequence[i], arr_v[i]);
            }
            MemoryContentAdapter.shortNames = arr_charSequence;
            return arr_charSequence;
        }
        return MemoryContentAdapter.shortNames;
    }

    private int getSize(long address, long data, int size, StringBuilder out, int id, int type, int typeSize, int align) {
        if((((long)(align - 1)) & address) == 0L && (this.showMask & 1 << id) != 0) {
            out.append(AddressItem.getStringDataTrim(address, data, type));
            out.append("\n");
            return size == 0 ? typeSize : size;
        }
        return size;
    }

    private int getSize(boolean goodData, AddressItemSet revertMap, long address, long data, int size, TextView view, int id, int type, int typeSize, int align) {
        String value;
        if((((long)(align - 1)) & address) == 0L && (this.showMask & 1 << id) != 0) {
            if(goodData) {
                String val = AddressItem.getStringDataTrim(address, data, type);
                Result result = this.result;
                revertMap.get(address, type, result);
                if(result.found) {
                    val = val + " (" + AddressItem.getStringData(address, result.data, type) + ')';
                }
                value = String.valueOf(val) + AddressItem.getShortName(type) + ';';
            }
            else {
                value = "?";
            }
            MemoryContentAdapter.setText(view, value);
            return size == 0 ? typeSize : size;
        }
        MemoryContentAdapter.setText(view, null);
        return size;
    }

    private long[] getSpecialOffset(boolean last) {
        long offset = this.getOffset();
        if(!last) {
            return offset == 0L ? new long[]{0L, 0L} : new long[]{offset - 1L, offset};
        }
        return offset + 0x2000L == 0L ? new long[]{-1L, 0L} : new long[]{offset + 0x2000L, offset + 0x2000L};
    }

    String getString(ArmDis ret, int[] args, int index) {
        if(!this.isDataGood()) {
            return "";
        }
        long v1 = this.getRawAddr(((long)index));
        long data = this.buffer.getLong(((int)(0x1FFFL & v1)));
        StringBuilder out = new StringBuilder();
        int size = this.getSize(v1, data, this.getSize(v1, data, this.getSize(v1, data, this.getSize(v1, data, this.getSize(v1, data, this.getSize(v1, data, this.getSize(v1, data, 0, out, 9, 0x40, 8, 4), out, 12, 0x20, 8, 4), out, 8, 16, 4, 4), out, 7, 4, 4, 4), out, 13, 8, 4, 4), out, 10, 2, 2, 2), out, 11, 1, 1, 1);
        if(size == 0) {
            size = 1 << this.getShift();
        }
        boolean skipRHex = false;
        if((this.showMask & 1) != 0) {
            if(size != 8) {
                data &= (1L << size * 8) - 1L;
            }
            out.append(ToolsLight.prefixLongHex(size * 2, data));
            out.append("\n");
            if(size == 1) {
                skipRHex = true;
            }
        }
        if(!skipRHex && (this.showMask & 2) != 0) {
            out.append(ToolsLight.prefixLongHex(size * 2, (size == 8 ? Long.reverseBytes(data) >> 0x40 - size * 8 : Long.reverseBytes(data) >> 0x40 - size * 8 & (1L << size * 8) - 1L)));
            out.append("\n");
        }
        if((this.showMask & 4) != 0) {
            out.append(MemoryContentAdapter.longToString(data, size));
            out.append("\n");
        }
        if((this.showMask & 8) != 0) {
            out.append(MemoryContentAdapter.longToJava(data, size));
            out.append("\n");
        }
        if((this.showMask & 16) != 0) {
            out.append(ArmDis.getArmOpcode(ret, v1, data));
            out.append("\n");
        }
        if((this.showMask & 0x20) != 0) {
            out.append(ArmDis.getThumbOpcode(ret, v1, data));
            out.append("\n");
        }
        if((this.showMask & 0x40) != 0) {
            Arm64Dis.disasm(args, v1, ((int)data), out);
            out.append("\n");
        }
        return out.toString();
    }

    private CharSequence getValueFormat() {
        return MemoryContentAdapter.getValueFormat(this.showMask);
    }

    static CharSequence getValueFormat(int mask) {
        CharSequence[] arr_charSequence = MemoryContentAdapter.getShortNames();
        CharSequence[] out = new CharSequence[arr_charSequence.length * 2];
        int used = 0;
        for(int i = 0; i < arr_charSequence.length; ++i) {
            if((1 << i & mask) != 0) {
                if(used > 0) {
                    out[used] = ",";
                    ++used;
                }
                out[used] = arr_charSequence[i];
                ++used;
            }
        }
        return Tools.concat(((CharSequence[])Arrays.copyOf(out, used)));
    }

    @Override  // android.widget.Adapter
    public View getView(int pos, View convertView, ViewGroup parent) {
        if(convertView == null || !(convertView.getTag() instanceof ViewHolderMemory)) {
            convertView = this.getNewView(parent);
        }
        ViewHolderMemory holder = (ViewHolderMemory)convertView.getTag();
        if(holder.wrongOrientation()) {
            convertView = this.getNewView(parent);
            holder = (ViewHolderMemory)convertView.getTag();
        }
        this.updateHolder(holder, pos);
        return convertView;
    }

    @Override  // android.widget.BaseAdapter
    public boolean hasStableIds() {
        return false;
    }

    private int index2pos(int index) {
        return MemoryContentAdapter.index2pos(index, this.filtered);
    }

    private static int index2pos(int index, short[] filtered) {
        if(filtered == null) {
            return index;
        }
        int found = Arrays.binarySearch(filtered, ((short)index));
        if(found < 0) {
            found = -found - 2;
            return found >= 0 ? found : 0;
        }
        return found;
    }

    boolean isDataGood() {
        return this.dataOffset == this.getOffset();
    }

    public void loadData() {
        MainService.instance.lockApp();
        MainService.instance.mDaemonManager.getMemory(this.getOffset(), 0x2004);
    }

    public void loadData(BufferReader reader) {
        long v;
        try {
            v = this.getOffset();
            reader.reset();
            long v1 = reader.readLong();
            int v2 = reader.readInt();
            if(v1 != v) {
                throw new RuntimeException("Offset mismatch: " + v1 + " (" + Long.toHexString(v1).toUpperCase(Locale.US) + ") != " + v + " (" + Long.toHexString(v).toUpperCase(Locale.US) + ")");
            }
            if(v2 < 0x2000) {
                throw new RuntimeException("Size mismatch: " + v2 + " (" + Integer.toHexString(v2).toUpperCase(Locale.US) + ") != " + 0x2000 + " (" + "2000" + ")");
            }
            reader.read(this.data, 0, v2);
        }
        catch(RuntimeException | IOException e) {
            Log.w("Failed load data to memory editor", e);
            Arrays.fill(this.data, 0);
        }
        this.dataOffset = v;
        MainService.instance.mMemListView.post(() -> {
            MemoryContentAdapter.this.getFilter().filter(MemoryContentAdapter.this.filterString);
            MemoryContentAdapter.this.superNotifyDataSetChanged();
        });

        class android.ext.MemoryContentAdapter.1 implements Runnable {
            @Override
            public void run() {
                MemoryContentAdapter.this.notifyDataSetChanged();
            }
        }

    }

    public static String longToJava(long in, int size) {
        byte[] arr_b = ParserNumbers.getBytes(in);
        int s = arr_b[8];
        if(s > size) {
            s = size;
        }
        if((s & 1) == 0) {
            ++s;
        }
        return new String(arr_b, 0, s + 1, ParserNumbers.getCharset(true));
    }

    public static String longToString(long in, int size) {
        byte[] arr_b = {((byte)(((int)(0xFFL & in)))), ((byte)(((int)((0xFF00L & in) >> 8)))), ((byte)(((int)((0xFF0000L & in) >> 16)))), ((byte)(((int)((0xFF000000L & in) >> 24)))), ((byte)(((int)((0xFF00000000L & in) >> 0x20)))), ((byte)(((int)((0xFF0000000000L & in) >> 40)))), ((byte)(((int)((0xFF000000000000L & in) >> 0x30)))), ((byte)(((int)((0xFF00000000000000L & in) >> 56))))};
        for(int i = 0; i < 8; ++i) {
            if(arr_b[i] < 0x20) {
                arr_b[i] = 46;
            }
        }
        return new String(arr_b).substring(0, size);
    }

    // 检测为 Lambda 实现
    @Override  // android.widget.BaseAdapter
    public void notifyDataSetChanged() [...]

    @Override  // android.widget.CompoundButton$OnCheckedChangeListener
    public void onCheckedChanged(CompoundButton v, boolean isChecked) {
        ViewHolderBase holder = (ViewHolderBase)v.getTag();
        if(holder == null) {
            return;
        }
        try {
            this.checks[holder.pos] = isChecked;
            MainService.instance.updateStatusBar();
        }
        catch(IndexOutOfBoundsException e) {
            Log.e("ArrayAdapter onCheckedChanged failed", e);
        }
        holder.updateBackground(isChecked, this.selected);
    }

    private int pos2index(int pos) {
        return MemoryContentAdapter.pos2index(pos, this.filtered, this.getShift());
    }

    private static int pos2index(int pos, short[] filtered, int shift) {
        if(filtered == null || pos < 0) {
            return pos;
        }
        return pos >= filtered.length ? pos - filtered.length + (0x2000 >> shift) : filtered[pos];
    }

    public void reloadAll() {
        this.loadData();
    }

    public void setBaseAddress(long addr, int top) {
        if((addr & 0xFFFFFFFFFFFFE000L) != this.offsetBuffer) {
            this.offsetBuffer = addr & 0xFFFFFFFFFFFFE000L;
            Arrays.fill(this.checks, false);
            this.notifyDataSetChanged();
        }
        Tools.setSelectionFromTop(MainService.instance.mMemListView, this.getJumpPos(addr), top);
    }

    private static int setText(TextView view, CharSequence text, int size, int min) {
        MemoryContentAdapter.setText(view, text);
        return text == null || size >= min ? size : min;
    }

    private static void setText(TextView view, CharSequence text) {
        if(text == null) {
            view.setVisibility(8);
            return;
        }
        view.setVisibility(0);
        view.setText(text);
    }

    void superNotifyDataSetChanged() {
        this.checkCount();
        super.notifyDataSetChanged();
    }

    private void updateHolder(ViewHolderMemory holder, int pos) {
        boolean checked;
        ViewGroup.LayoutParams viewGroup$LayoutParams0 = holder.ch.getLayoutParams();
        viewGroup$LayoutParams0.height = (Config.smallItems & 1 << Tools.isLandscape() + 4) == 0 ? Tools.dp2px48() : -2;
        if(pos == 0 || pos == this.getCount() - 1) {
            holder.ch.setVisibility(4);
            holder.address.setVisibility(8);
            holder.valueHex.setVisibility(8);
            holder.valueRHex.setVisibility(8);
            holder.valueString.setVisibility(0);
            holder.valueJava.setVisibility(8);
            holder.valueArm.setVisibility(8);
            holder.valueThumb.setVisibility(8);
            holder.valueArm8.setVisibility(8);
            holder.valueDword.setVisibility(8);
            holder.valueFloat.setVisibility(8);
            holder.valueDouble.setVisibility(8);
            holder.valueWord.setVisibility(8);
            holder.valueByte.setVisibility(8);
            holder.valueQword.setVisibility(8);
            holder.valueXor.setVisibility(8);
            holder.region.setVisibility(8);
            holder.updateBackground(false, -2);
            long[] arr_v = this.getSpecialOffset(pos != 0);
            holder.valueString.setText(Re.s(0x7F070100) + ' ' + AddressItem.getStringAddress(arr_v[0], 0x20));  // string:goto_address "Go to the address:"
            return;
        }
        holder.ch.setVisibility(0);
        holder.address.setVisibility(0);
        holder.region.setVisibility(0);
        holder.pos = pos;
        long v1 = this.getAddr(pos);
        holder.region.setText(this.getRegion(v1));
        holder.address.setText(AddressItem.getStringAddress(v1, 0x20));
        try {
            checked = this.checks[pos];
        }
        catch(IndexOutOfBoundsException e) {
            Log.e("ArrayAdapter getView failed", e);
            checked = false;
        }
        holder.ch.setChecked(checked);
        holder.updateBackground(checked, this.selected);
        long v2 = this.buffer.getLong(((int)(0x1FFFL & v1)));
        boolean z1 = this.isDataGood();
        AddressItemSet revertMap = MainService.instance.revertMap;
        int size = this.getSize(z1, revertMap, v1, v2, this.getSize(z1, revertMap, v1, v2, this.getSize(z1, revertMap, v1, v2, this.getSize(z1, revertMap, v1, v2, this.getSize(z1, revertMap, v1, v2, this.getSize(z1, revertMap, v1, v2, this.getSize(z1, revertMap, v1, v2, 0, holder.valueDouble, 9, 0x40, 8, 4), holder.valueQword, 12, 0x20, 8, 4), holder.valueFloat, 8, 16, 4, 4), holder.valueDword, 7, 4, 4, 4), holder.valueXor, 13, 8, 4, 4), holder.valueWord, 10, 2, 2, 2), holder.valueByte, 11, 1, 1, 1);
        if(size == 0) {
            size = 1 << this.getShift();
        }
        MemoryContentAdapter.fillData(holder, v1, v2, this.showMask, z1, size);
    }

    void updateShowMask(int mask) {
        if(mask == this.showMask) {
            return;
        }
        int v1 = this.getShift();
        ScrollPos tools$ScrollPos0 = Tools.getScrollPos(MainService.instance.mMemListView);
        long v2 = this.getAddr(tools$ScrollPos0.top);
        long oldPos = this.selected >= 0 ? this.getAddr(this.selected) : -1L;
        this.showMask = mask;
        new SPEditor().putInt("memory-editor-format", this.showMask, 0xFFFFC181).commit();
        this.notifyDataSetChanged();
        if(v1 != this.getShift()) {
            tools$ScrollPos0.top = this.getPos(v2);
            Tools.setScrollPos(MainService.instance.mMemListView, tools$ScrollPos0);
            if(this.selected >= 0) {
                this.selected = this.getPos(oldPos);
            }
        }
        MainService.instance.mValueFormat.setText(this.getValueFormat());
    }
}

