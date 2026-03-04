package android.ext;

import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView;
import android.widget.TextView;
import java.util.ArrayList;

public class ItemContextMenu implements AdapterView.OnItemLongClickListener {
    private final String[] cache;
    private final String[] hexCache;
    private final int inMemoryEditor;
    private static final ThreadLocal lastArmAddr;
    private final int[] list;
    private final String[] rhexCache;

    static {
        ItemContextMenu.lastArmAddr = new ThreadLocal();
    }

    public ItemContextMenu(int inMemoryEditor) {
        this.inMemoryEditor = inMemoryEditor;
        this.list = new int[]{0, 4, 16, 0x40, 2, 1, 0x20, 8};
        this.cache = new String[this.list.length];
        this.hexCache = new String[this.list.length];
        this.rhexCache = new String[this.list.length];
    }

    @Override  // android.widget.AdapterView$OnItemLongClickListener
    public boolean onItemLongClick(AdapterView adapterView0, View view, int position, long id) {
        int showMask;
        int canBePointer;
        Object object0 = adapterView0.getItemAtPosition(position);
        if(!(object0 instanceof AddressItem)) {
            return false;
        }
        SavedItem savedItem0 = new SavedItem(((AddressItem)object0));
        String s = savedItem0.getStringAddress();
        long addr = savedItem0.address;
        int flags = savedItem0.flags & 0x7F;
        long data = savedItem0.data;
        ProcessInfo processInfo = MainService.instance.processInfo;
        int size = this.inMemoryEditor == 3 ? 8 : AddressItem.getSize(flags);
        if((3L & addr) == 0L && size >= 4) {
            canBePointer = processInfo == null || !processInfo.x64 || size != 8 || (0xFFFFFFFF00000000L & data) == 0L ? 1 : 2;
        }
        else {
            canBePointer = 0;
        }
        boolean canBeHex = (flags & -81) != 0;
        ArrayList texts = new ArrayList();
        ArrayList items = new ArrayList();
        ArrayList icons = new ArrayList();
        texts.add(s);
        items.add(Re.s(0x7F07016D));  // string:offset_calculator "Offset calculator"
        icons.add(0x7F020010);  // drawable:ic_calculator_white_24dp
        texts.add(s);
        items.add(Re.s(0x7F07025A));  // string:help_pointer_search_title "Pointer search"
        icons.add(0x7F020040);  // drawable:ic_pointer_white_24dp
        texts.add(s);
        items.add(Re.s(0x7F07034E));  // string:search_nearby "Search nearby"
        icons.add(0x7F020046);  // drawable:ic_search_nearby_white_24dp
        if(adapterView0 != MainService.instance.mMemListView) {
            texts.add(s);
            items.add(Re.s(0x7F070100) + ' ' + s);  // string:goto_address "Go to the address:"
            icons.add(0x7F02002A);  // drawable:ic_forward_white_24dp
        }
        for(int i = 0; i < canBePointer; ++i) {
            int pointer = i == 0 ? 4 : 0x20;
            String s1 = AddressItem.getStringHexData(addr, data, pointer);
            texts.add(s1);
            items.add(Re.s(0x7F07016C) + ' ' + s1);  // string:goto_pointer "Go to the pointer:"
            icons.add(0x7F02002A);  // drawable:ic_forward_white_24dp
            if((data & 0xFFFFFFFFFFFFF000L) != 0L && RegionList.getRegionItem(data & 0xFFFFFFFFFFFFF000L) == null) {
                String pointerValue = AddressItem.getStringHexData(addr, data & 0xFFFFFFFFFFFFF000L, pointer);
                texts.add(pointerValue);
                items.add(Tools.stripColon(0x7F0702DE) + ": " + pointerValue);  // string:allocate_page "Allocate memory page"
                icons.add(0x7F020015);  // drawable:ic_code_braces_white_24dp
            }
        }
        if((addr & 0xFFFFFFFFFFFFF000L) != 0L && RegionList.getRegionItem(addr & 0xFFFFFFFFFFFFF000L) == null) {
            String s3 = AddressItem.getStringAddress(addr & 0xFFFFFFFFFFFFF000L, 4);
            texts.add(s3);
            items.add(Tools.stripColon(0x7F0702DE) + ": " + s3);  // string:allocate_page "Allocate memory page"
            icons.add(0x7F020015);  // drawable:ic_code_braces_white_24dp
        }
        texts.add(s);
        items.add(Re.s(0x7F0700FF) + ' ' + s);  // string:copy_address "Copy the address:"
        icons.add(0x7F020016);  // drawable:ic_content_copy_white_24dp
        String opArm = null;
        String opThumb = null;
        String opArm64 = null;
        int v9 = MainService.instance.getTabIndex();
        if(v9 == 1) {
            showMask = AddressArrayAdapter.showMask;
        }
        else {
            switch(v9) {
                case 2: {
                    showMask = SavedListAdapter.showMask;
                    break;
                }
                case 3: {
                    showMask = MainService.instance.mMemListAdapter.showMask;
                    break;
                }
                default: {
                    showMask = 0;
                }
            }
        }
        if(showMask != 0) {
            for(int i = 0; i < 3; ++i) {
                if((i != 0 || (3L & addr) == 0L && (showMask & 16) != 0) && ((i != 1 || (1L & addr) == 0L && (showMask & 0x20) != 0) && (i != 2 || (3L & addr) == 0L && (showMask & 0x40) != 0))) {
                    ItemContextMenu.lastArmAddr.set(0L);
                    if(i == 0) {
                        opArm = ArmDis.getArmOpcode(null, addr, data);
                    }
                    if(i == 1) {
                        opThumb = ArmDis.getThumbOpcode(null, addr, data);
                    }
                    if(i == 2) {
                        opArm64 = Arm64Dis.disasm(Arm64Dis.args(), addr, ((int)data), new StringBuilder()).toString();
                    }
                    String prefix = null;
                    long v12 = (long)(((Long)ItemContextMenu.lastArmAddr.get()));
                    if(v12 != 0L) {
                        if(i == 0) {
                            prefix = "ARM (x32)";
                        }
                        if(i == 1) {
                            prefix = "Thumb";
                        }
                        if(i == 2) {
                            prefix = "ARM (x64)";
                        }
                        String s8 = AddressItem.getStringAddress(v12, 4);
                        texts.add(s8);
                        items.add(prefix + ": " + Re.s(0x7F070100) + ' ' + s8);  // string:goto_address "Go to the address:"
                        icons.add(0x7F02002A);  // drawable:ic_forward_white_24dp
                        texts.add(s8);
                        items.add(prefix + ": " + Re.s(0x7F0700FF) + ' ' + s8);  // string:copy_address "Copy the address:"
                        icons.add(0x7F020016);  // drawable:ic_content_copy_white_24dp
                        if((v12 & 0xFFFFFFFFFFFFF000L) != 0L && RegionList.getRegionItem(v12 & 0xFFFFFFFFFFFFF000L) == null) {
                            String s9 = AddressItem.getStringAddress(v12 & 0xFFFFFFFFFFFFF000L, 4);
                            texts.add(s9);
                            items.add(prefix + ": " + Tools.stripColon(0x7F0702DE) + ": " + s9);  // string:allocate_page "Allocate memory page"
                            icons.add(0x7F020015);  // drawable:ic_code_braces_white_24dp
                        }
                    }
                }
            }
        }
        int used = 0;
        int[] list = this.list;
        String[] cache = this.cache;
        String[] hexCache = this.hexCache;
        String[] rhexCache = this.rhexCache;
        for(int i = 0; i < list.length; ++i) {
            int check = list[i];
            if(check == 0 || (flags & check) != 0) {
                if(check == 0) {
                    check = flags;
                }
                String s10 = AddressItem.getStringDataTrim(addr, data, check);
                boolean found = false;
                for(int j = 0; j < used; ++j) {
                    if(cache[j].equals(s10)) {
                        found = true;
                        break;
                    }
                }
                if(!found) {
                    cache[used] = s10;
                    if(canBeHex) {
                        hexCache[used] = (check & -81) == 0 ? null : AddressItem.getStringHexData(addr, data, check);
                        rhexCache[used] = (check & -81) == 0 ? null : AddressItem.getStringRhexData(addr, data, check);
                    }
                    ++used;
                }
            }
        }
        if(used > 0) {
            for(int i = 0; i < used; ++i) {
                texts.add(cache[i]);
                items.add(Re.s(0x7F0700FE) + ' ' + cache[i]);  // string:copy_value "Copy the value:"
                icons.add(0x7F020016);  // drawable:ic_content_copy_white_24dp
                if(canBeHex && hexCache[i] != null) {
                    texts.add(hexCache[i]);
                    items.add(Re.s(0x7F07016B) + ' ' + hexCache[i]);  // string:copy_hex_value "Copy the hex value:"
                    icons.add(0x7F020016);  // drawable:ic_content_copy_white_24dp
                    texts.add(rhexCache[i]);
                    items.add(Re.s(0x7F07027A) + ' ' + rhexCache[i]);  // string:copy_rhex_value "Copy the reverse hex value:"
                    icons.add(0x7F020016);  // drawable:ic_content_copy_white_24dp
                }
            }
        }
        for(int i = 0; i < 3; ++i) {
            String op = null;
            String name = null;
            if(i == 0) {
                op = opArm;
                name = "ARM (x32)";
            }
            if(i == 1) {
                op = opThumb;
                name = "Thumb";
            }
            if(i == 2) {
                op = opArm64;
                name = "ARM (x64)";
            }
            if(op != null) {
                String op = op.trim();
                int v19 = op.indexOf(59);
                if(v19 != -1) {
                    op = op.substring(0, v19).trim();
                }
                if(op.length() > 0) {
                    texts.add(op);
                    items.add(Tools.stringFormat(Re.s(0x7F07034D), new Object[]{name}) + ' ' + op);  // string:copy_opcode "Copy __s__ opcode:"
                    icons.add(0x7F020016);  // drawable:ic_content_copy_white_24dp
                }
            }
        }
        if(adapterView0 == MainService.instance.mSavedListView) {
            texts.add(s);
            items.add(Re.s(0x7F0702F9));  // string:change_address_type "Change address / type"
            icons.add(0x7F020019);  // drawable:ic_cursor_mover_white_24dp
        }
        android.ext.ItemContextMenu.1 itemContextMenu$10 = new ArrayAdapter(MainService.context, items) {
            @Override  // android.ext.ArrayAdapter
            public View getView(int position, View convertView, ViewGroup parent) {
                View view1 = super.getView(position, convertView, parent);
                Drawable drawable0 = Tools.getDrawable(((int)(((Integer)icons.get(position)))));
                TextView tv = (TextView)view1.findViewById(0x1020014);
                if(tv != null) {
                    Tools.setTextAppearance(tv, 0x7F090007);  // style:ListItemText
                    Tools.addIconToTextView(tv, drawable0, 24);
                }
                return view1;
            }
        };
        Alert.show(Alert.create(Tools.getDarkContext()).setAdapter(itemContextMenu$10, new DialogInterface.OnClickListener() {
            private Long getSource() {
                return ItemContextMenu.this.inMemoryEditor == 3 && ((AddressItem)object0) != null ? ((AddressItem)object0).address : null;
            }

            @Override  // android.content.DialogInterface$OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                if(which < 0 || which >= icons.size()) {
                    Log.e(("Unknown index in menu: " + which + " for " + icons.size()));
                    return;
                }
                switch(((int)(((Integer)icons.get(which))))) {
                    case 0x7F020010: {  // drawable:ic_calculator_white_24dp
                        new OffsetCalculator(this.getSource()).show(((String)texts.get(which)));
                        break;
                    }
                    case 0x7F020015: {  // drawable:ic_code_braces_white_24dp
                        Allocate.show(((String)texts.get(which)));
                        break;
                    }
                    case 0x7F020016: {  // drawable:ic_content_copy_white_24dp
                        Tools.copyText(((String)texts.get(which)));
                        break;
                    }
                    case 0x7F020019: {  // drawable:ic_cursor_mover_white_24dp
                        AddressEditor.onClick(((String)texts.get(which)));
                        break;
                    }
                    case 0x7F02002A: {  // drawable:ic_forward_white_24dp
                        MainService.instance.goToAddress(this.getSource(), ((String)texts.get(which)), Tools.stripColon(0x7F07024F) + ": (" + ((AddressItem)object0) + ')');  // string:from_context "From the context menu:"
                        break;
                    }
                    case 0x7F020040: {  // drawable:ic_pointer_white_24dp
                        new PointerButtonListener().show(((String)texts.get(which)));
                        break;
                    }
                    case 0x7F020046: {  // drawable:ic_search_nearby_white_24dp
                        MainService.instance.searchListener.searchNearby(((String)texts.get(which)));
                        break;
                    }
                    default: {
                        Log.e(("Unknown icon in menu: " + Integer.toHexString(((int)(((Integer)icons.get(which)))))));
                    }
                }
                if(dialog != null) {
                    Tools.dismiss(dialog);
                }
            }
        }).setNegativeButton(Re.s(0x7F0700A1), null));  // string:cancel "Cancel"
        return true;
    }

    public static long setLastArmAddr(long addr) {
        ItemContextMenu.lastArmAddr.set(addr);
        return addr;
    }
}

