package android.ext;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface;
import android.fix.ImageButtonView;
import android.util.AttributeSet;
import android.util.SparseIntArray;
import android.view.View.OnClickListener;
import android.view.View;
import android.widget.ListView;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RegionList extends ImageButtonView implements View.OnClickListener {
    public static class Region {
        private static int colorDefault;
        private static SparseIntArray colors;
        final long end;
        final int flags;
        String internal;
        String name;
        final long start;

        static {
            Region.colors = null;
            Region.colorDefault = 0xFFFFFF;
        }

        public Region(int flags, long start, long end) {
            this.flags = flags;
            this.start = start;
            this.end = end;
        }

        public String getInternalName() {
            return this.internal == null ? this.name : this.internal + ":bss";
        }

        public String getName() {
            return this.name;
        }

        public static int getPointerColor(Region region) {
            try {
                int out = 4;
                SparseIntArray cl = Region.colors;
                if(cl == null) {
                    cl = new SparseIntArray(8);
                    cl.append(4, Tools.getColor(0x7F0A000C));  // color:pointer_unknown
                    cl.append(1, Tools.getColor(0x7F0A000D));  // color:pointer_executable_writable
                    cl.append(2, Tools.getColor(0x7F0A000E));  // color:pointer_executable
                    cl.append(16, Tools.getColor(0x7F0A000F));  // color:pointer_writable
                    cl.append(8, Tools.getColor(0x7F0A0010));  // color:pointer_readable
                    Region.colors = cl;
                    Region.colorDefault = Tools.getColor(0x7F0A000C);  // color:pointer_unknown
                }
                if(region != null) {
                    out = cl.get(region.getPointerType());
                }
                return out == 4 ? Region.colorDefault : out;
            }
            catch(Throwable e) {
                Log.e(("Failed getPointerColor for: " + region), e);
                return Region.colorDefault;
            }
        }

        private int getPointerType() {
            int type = this.flags;
            if((type & 0x600) == 0x600) {
                return 1;
            }
            if((type & 0x400) == 0x400) {
                return 2;
            }
            if((type & 0x200) == 0x200) {
                return 16;
            }
            return (type & 0x100) == 0x100 ? 8 : 4;
        }

        public CharSequence getState() {
            return RegionList.shortValues[this.flags & 0xFF];
        }

        // 去混淆评级： 低(20)
        public String getType() {
            StringBuilder stringBuilder0 = new StringBuilder(String.valueOf(((this.flags & 0x100) == 0x100 ? "r" : "-"))).append(((this.flags & 0x200) == 0x200 ? "w" : "-")).append(((this.flags & 0x400) == 0x400 ? "x" : "-"));
            return (this.flags & 0x800) == 0x800 ? stringBuilder0.append("s").toString() : stringBuilder0.append("p").toString();
        }

        public CharSequence toCharSequence() {
            return Tools.colorize(this.toString(), RegionList.colors[this.flags & 0xFF]);
        }

        @Override
        public String toString() {
            return RegionList.shortValues[this.flags & 0xFF] + ": " + AddressItem.getStringAddress(this.start, 4) + '-' + AddressItem.getStringAddress(this.end, 4) + ' ' + this.getType() + " \'" + this.name + '\'';
        }
    }

    private static final int MODE_EXECUTE = 0x400;
    private static final int MODE_MASK = 0xF00;
    private static final int MODE_READ = 0x100;
    private static final int MODE_SHARED = 0x800;
    private static final int MODE_WRITE = 0x200;
    private static final int STATE_C_BSS = 16;
    private static final int STATE_MASK = 0xFF;
    private static final int STATE_SYS_C_BSS = 0x800;
    static int[] colors;
    private static volatile Region last;
    private static int[] map;
    private static volatile List sList;
    private static volatile SparseIntArray sSizes;
    static CharSequence[] shortValues;

    static {
        RegionList.sList = new ArrayList();
        RegionList.last = null;
        RegionList.sSizes = new SparseIntArray();
    }

    public RegionList(Context context) {
        super(context);
        this.init();
    }

    public RegionList(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init();
    }

    public RegionList(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init();
    }

    public RegionList(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.init();
    }

    public static List getList() {
        return RegionList.sList;
    }

    public static CharSequence getRegion(long addr) {
        Region regionList$Region0 = RegionList.getRegionItem(addr);
        return regionList$Region0 == null ? "?" : regionList$Region0.getState();
    }

    public static Region getRegionItem(long addr) {
        Region midVal;
        Region check = RegionList.last;
        if(check != null && Tools.unsignedLessOrEqual(check.start, addr) && Tools.unsignedLess(addr, check.end)) {
            return check;
        }
        List list = RegionList.sList;
        int low = 0;
        int high = list.size() - 1;
        while(true) {
            if(low > high) {
                return null;
            }
            int mid = low + high >>> 1;
            midVal = (Region)list.get(mid);
            if(Tools.unsignedLessOrEqual(midVal.end, addr)) {
                low = mid + 1;
            }
            else {
                if(!Tools.unsignedLess(addr, midVal.start)) {
                    break;
                }
                high = mid - 1;
            }
        }
        RegionList.last = midVal;
        return midVal;
    }

    public static Region getRegionItem(String access, String name, long offset) {
        String s2 = access.trim();
        String s3 = name.trim();
        if(s3.length() > 0 && offset >= 0L) {
            List list = RegionList.sList;
            Region[] matches = new Region[6];
            int v1 = 0;
            int v2 = 0;
            int v3 = 0;
            File file0 = new File(s3);
            String s4 = "/" + file0.getName();
            String halfBase = file0.getParentFile() == null ? null : "/" + file0.getParentFile().getName() + s4;
            int v4 = RegionList.getType(s2);
            for(Object object0: list) {
                Region region = (Region)object0;
                if(region != null) {
                    String s6 = region.getInternalName();
                    if(s6.length() != 0 && region.end - region.start > offset) {
                        int bits = (region.flags ^ v4) & 0xF00;
                        bits = bits == 0 ? (region.flags ^ v4) & 0xF00 : Integer.bitCount(bits);
                        if(s6.equals(s3)) {
                            if(bits == 0) {
                                matches[0] = region;
                                break;
                            }
                            if(matches[1] == null || v1 > bits) {
                                matches[1] = region;
                                v1 = bits;
                            }
                        }
                        if(halfBase != null && s6.endsWith(halfBase)) {
                            if(bits == 0) {
                                matches[2] = region;
                            }
                            else if(matches[3] == null || v2 > bits) {
                                matches[3] = region;
                                v2 = bits;
                            }
                        }
                        if(!s6.endsWith(s4)) {
                        }
                        else if(bits == 0) {
                            matches[4] = region;
                        }
                        else if(matches[5] == null || v3 > bits) {
                            matches[5] = region;
                            v3 = bits;
                        }
                    }
                }
            }
            int v6 = 0;
            while(v6 < 6) {
                Region region = matches[v6];
                if(region == null) {
                    ++v6;
                    continue;
                }
                return region;
            }
        }
        return null;
    }

    public static long getSize(int type) {
        return 0x1000L * ((long)RegionList.sSizes.get(type));
    }

    private static int getState(int state) {
        int ret = 0;
        for(int i = 0; i < RegionList.map.length; ++i) {
            if((RegionList.map[i] & state) == state) {
                ret = i;
            }
        }
        return ret;
    }

    public static int getType(String type) {
        int ret = 0;
        if(type.length() != 4) {
            return 0;
        }
        if(type.charAt(0) == 0x72) {
            ret = 0x100;
        }
        if(type.charAt(1) == 0x77) {
            ret |= 0x200;
        }
        if(type.charAt(2) == 120) {
            ret |= 0x400;
        }
        return type.charAt(3) == 0x73 ? ret | 0x800 : ret;
    }

    private void init() {
        this.setOnClickListener(this);
        if(!this.isInEditMode()) {
            this.setImageResource(0x7F02000C);  // drawable:ic_arrow_down_drop_circle_white_24dp
            Config.setIconSize(this);
        }
    }

    public static void loadData(BufferReader reader) {
        reader.reset();
        ArrayList list = new ArrayList();
        SparseIntArray sizes = new SparseIntArray();
        List old = RegionList.sList;
        if(old != null) {
            list.ensureCapacity(old.size());
        }
        RegionList.loadMaps();
        try {
            String lastName = "";
            int v;
            while((v = reader.readInt()) != 0) {
                int v1 = RegionList.getState(v);
                long v2 = reader.readLongLong();
                long v3 = reader.readLongLong();
                int flags = v1 | reader.readInt() << 8;
                if((v & 0x810) == 0) {
                    lastName = null;
                }
                int v5 = reader.readInt();
                if(v5 < 0 || v5 > 0x400) {
                    Log.e(("Bad name length: " + v5), new RuntimeException());
                    break;
                }
                Region region = RegionList.getRegionItem(v2);
                String s1 = reader.readString(v5, (region == null ? null : region.name)).trim().intern();
                if(region == null || region.flags != flags || region.start != v2 || region.end != v3) {
                    region = new Region(flags, v2, v3);
                }
                region.name = s1;
                region.internal = lastName;
                lastName = s1;
                list.add(region);
                sizes.put(v1, sizes.get(v1) + ((int)((region.end - region.start) / 0x1000L >= 0L ? (region.end - region.start) / 0x1000L : 0L)));
            }
        }
        catch(IOException e) {
            Log.e("???", e);
        }
        RegionList.sList = list;
        RegionList.sSizes = sizes;
        RegionList.last = null;
        MainService.instance.mMemListAdapter.forceUpdateCache();
    }

    private static void loadMaps() {
        Config.get(0x7F0B0081).asString();  // id:config_ranges
    }

    @Override  // android.view.View$OnClickListener
    public void onClick(View v) {
        this.onClick(v, (this instanceof RegionListFiltered ? Config.usedRanges : 0));
    }

    public void onClick(View v, int filter) {
        Object object0 = this.getTag();
        if(!(object0 instanceof EditText)) {
            return;
        }
        ((EditText)object0).requestFocus();
        if(v == null) {
            v = this;
        }
        boolean end = v.getId() == 0x7F0B0009;  // id:region_to
        String s = ((EditText)object0).getText().toString().trim();
        long addr = 0L;
        if(s.length() > 0) {
            try {
                addr = ParserNumbers.parseBigLong(s, 16);
            }
            catch(NumberFormatException e) {
                Log.w(("RegionList failed parse: " + s), e);
            }
        }
        List list = RegionList.sList;
        int selected = -1;
        ArrayList arrayList0 = new ArrayList(list.size());
        SparseIntArray map = new SparseIntArray();
        int i = 0;
        while(i < list.size()) {
            Region region = (Region)list.get(i);
            if(filter == 0) {
            label_24:
                map.append(arrayList0.size(), i);
                arrayList0.add(region.toCharSequence());
                if(Tools.unsignedLessOrEqual(region.start, addr)) {
                    selected = i;
                }
            }
            else {
                int state = region.flags & 0xFF;
                if(state >= RegionList.map.length || (RegionList.map[state] & filter) != 0) {
                    goto label_24;
                }
            }
            ++i;
        }
        if(arrayList0.size() == 0) {
            StringBuilder sb = new StringBuilder();
            sb.append(Re.s(0x7F070314));  // string:no_regions "No matching regions found."
            if(filter != 0) {
                int[] map_ = RegionList.map;
                CharSequence[] shortValues = RegionList.shortValues;
                boolean found = false;
                for(int i = 0; i < map_.length; ++i) {
                    if((filter & map_[i]) != 0) {
                        sb.append((found ? ", " : "\n["));
                        sb.append(shortValues[i]);
                        found = true;
                    }
                }
                if(found) {
                    sb.append(']');
                }
            }
            Tools.showToast(sb.toString());
            return;
        }
        AlertDialog alertDialog0 = Alert.create().setSingleChoiceItems(((CharSequence[])arrayList0.toArray(new CharSequence[arrayList0.size()])), selected, new DialogInterface.OnClickListener() {
            @Override  // android.content.DialogInterface$OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                long v2;
                try {
                    EditText editText0 = ((EditText)object0);
                    if(end) {
                        int v1 = map.get(which);
                        v2 = ((Region)list.get(v1)).end - 1L;
                    }
                    else {
                        int v3 = map.get(which);
                        v2 = ((Region)list.get(v3)).start;
                    }
                    editText0.setText(AddressItem.getStringAddress(v2, 4));
                    ((EditText)object0).requestFocus();
                }
                catch(Throwable e) {
                    Log.d(("RegionList failed with " + which), e);
                }
                Tools.dismiss(dialog);
            }
        }).create();
        ListView listView0 = alertDialog0.getListView();
        if(listView0 != null) {
            Tools.setTextAppearance(listView0, 0x7F090002);  // style:SmallText
            FastScrollerFix.setFastScrollEnabled(listView0, true);
        }
        Alert.show(alertDialog0);
    }

    public static void setMaps(int[] map_, CharSequence[] shortValues_, int[] colors_) {
        RegionList.map = map_;
        RegionList.shortValues = shortValues_;
        RegionList.colors = colors_;
    }
}

