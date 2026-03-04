package android.ext;

import android.app.AlertDialog;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.DialogInterface;
import android.sup.ArrayListResults;
import android.sup.LongSparseArrayChecked;
import android.view.View;
import android.widget.ListView;

public class CopySelected extends MenuItem {
    static class Bytes {
        byte[] bytes;
        int len;
        long nextAddr;

        Bytes() {
            this.bytes = new byte[800];
            this.len = 0;
            this.nextAddr = 0L;
        }
    }

    private static final int HEX = 0;
    private static final int HEX_TEXT = 6;
    private static final int HEX_UTF16LE = 8;
    private static final int HEX_UTF8 = 7;
    private static final int HEX_UTF8_UTF16LE = 9;
    private static final int MAX_ITEMS = 100;
    private static final int RHEX = 1;
    private static final int SIZE = 3;
    private static final int TYPES = 2;
    private static final int UTF16LE = 5;
    private static final int UTF8 = 4;

    public CopySelected() {
        super(0x7F070277, 0x7F020016);  // string:copy_as_search "Copy as a group search"
    }

    private void add(Bytes bytes, StringBuilder out, AddressItem item, int mask) {
        if(bytes != null) {
            if(bytes.nextAddr == 0L) {
                bytes.nextAddr = item.address;
            }
            int v1 = item.getSize();
            long l = item.data;
            for(int i = 0; i < v1; ++i) {
                long addr = item.address + ((long)i);
                if(bytes.nextAddr <= addr) {
                    int v5 = bytes.len;
                    bytes.len = v5 + 1;
                    bytes.bytes[v5] = (byte)(((int)(0xFFL & l)));
                    bytes.nextAddr = addr;
                }
                l >>= 8;
            }
            return;
        }
        if(out.length() != 0) {
            out.append(';');
        }
        if((mask & 2) != 0) {
            out.append("0000000000000000");
            out.append('r');
        }
        else if((mask & 1) == 0) {
            out.append(item.getStringDataTrim());
        }
        else {
            out.append("0000000000000000");
            out.append('h');
        }
        if((mask & 4) != 0) {
            out.append(item.getShortName());
        }
    }

    void copy(int mask) {
        Object object0 = MainService.instance.getCheckList();
        StringBuilder out = new StringBuilder();
        int count = 0;
        int total = 0;
        long start = 0L;
        long end = 0L;
        int mode = Integer.numberOfTrailingZeros(mask & 0x3F0);
        Bytes bytes = mode == 0x20 ? null : new Bytes();
        if(object0 instanceof ArrayListResults) {
            AddressItem item = new AddressItem();
            int v6 = ((ArrayListResults)object0).size();
            for(int i = 0; i < v6; ++i) {
                if(((ArrayListResults)object0).checked(i)) {
                    if(count < 100) {
                        ((ArrayListResults)object0).get(i, item);
                        this.add(bytes, out, item, mask);
                        end = item.address;
                        if(count == 0) {
                            start = end;
                        }
                        ++count;
                    }
                    ++total;
                    if(total <= 100) {
                        continue;
                    }
                    break;
                }
            }
        }
        else if(object0 instanceof LongSparseArrayChecked) {
            int v8 = ((LongSparseArrayChecked)object0).size();
            for(int i = 0; i < v8; ++i) {
                if(((LongSparseArrayChecked)object0).checkAt(i)) {
                    SavedItem item = (SavedItem)((LongSparseArrayChecked)object0).valueAt(i);
                    if(item instanceof SavedItem) {
                        if(count < 100) {
                            this.add(bytes, out, item, mask);
                            end = item.address;
                            if(count == 0) {
                                start = end;
                            }
                            ++count;
                        }
                        ++total;
                        if(total <= 100) {
                            continue;
                        }
                        break;
                    }
                }
            }
        }
        else if(object0 instanceof boolean[]) {
            MemoryContentAdapter memAdapter = MainService.instance.mMemListAdapter;
            int s = ((boolean[])object0).length - 1;
            for(int i = 1; i < s; ++i) {
                if(((boolean[])object0)[i]) {
                    if(count < 100) {
                        AddressItem item = (AddressItem)memAdapter.getItem(i);
                        this.add(bytes, out, item, mask);
                        end = item.address;
                        if(count == 0) {
                            start = end;
                        }
                        ++count;
                    }
                    ++total;
                    if(total <= 100) {
                        continue;
                    }
                    break;
                }
            }
        }
        int need = bytes == null ? 2 : 1;
        if(count < need) {
            Tools.showToast(Tools.stringFormat(Re.s(0x7F070168), new Object[]{need}));  // string:need_select_more "Need select __d__ or more items."
            return;
        }
        if(total > count) {
            Tools.showToast(Tools.stringFormat(Re.s(0x7F070167), new Object[]{count}));  // string:used_only_first "Used only first __d__ items."
        }
        if(bytes != null) {
            switch(mode) {
                case 4: 
                case 5: {
                    out.append(((char)(mode == 5 ? 58 : 59)));
                    out.append(new String(bytes.bytes, 0, bytes.len, ParserNumbers.getCharset(mode == 5)));
                    break;
                }
                case 6: {
                    out.append('h');
                    char[] hexArr = HexText.hexArray;
                    byte[] b = bytes.bytes;
                    int is = bytes.len;
                    for(int i = 0; i < is; ++i) {
                        out.append(' ');
                        int data = b[i];
                        out.append(hexArr[(data & 0xF0) >> 4]);
                        out.append(hexArr[data & 15]);
                    }
                    break;
                }
                default: {
                    out.append('Q');
                    out.append(' ');
                    HexText.getText(out, 0, bytes.bytes, bytes.len, mode == 7 || mode == 9, mode == 8 || mode == 9, null);
                }
            }
        }
        else if((mask & 8) != 0) {
            out.append(':');
            out.append(end + 1L - start);
        }
        String s = out.toString();
        android.ext.CopySelected.3 copySelected$30 = new DialogInterface.OnClickListener() {
            @Override  // android.content.DialogInterface$OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                if(which == -1) {
                    Tools.copyText(s, true);
                    return;
                }
                MainService.instance.mMainScreen.setCurrentTab(1);
                MainService.instance.searchListener.openSearch(s);
            }
        };
        if(bytes == null && end - start >= 0xFFFFL) {
            Alert.show(Alert.create().setMessage(Tools.stringFormat(Re.s(0x7F070347), new Object[]{((long)(end + 1L - start)), 0xFFFF})).setPositiveButton(Re.s(0x7F07009D), (/* 缺少LAMBDA参数 */, /* 缺少LAMBDA参数 */) -> Alert.show(Alert.create().setMessage(this.val$text).setPositiveButton(Re.s(0x7F070161), this.val$listener).setNeutralButton(Re.s(0x7F07008B), this.val$listener).setNegativeButton(Re.s(0x7F0700A1), null))).setNegativeButton(Re.s(0x7F0700A1), null));  // string:copy "Copy"
            return;
        }
        this.dialog(s, copySelected$30);

        class android.ext.CopySelected.4 implements DialogInterface.OnClickListener {
            android.ext.CopySelected.4(String s, DialogInterface.OnClickListener dialogInterface$OnClickListener0) {
            }

            @Override  // android.content.DialogInterface$OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                CopySelected.this.dialog(this.val$text, this.val$listener);
            }
        }

    }

    // 检测为 Lambda 实现
    void dialog(String text, DialogInterface.OnClickListener listener) [...]

    @Override  // android.ext.MenuItem
    public void onClick(View v) {
        CharSequence[] arr_charSequence = {Re.s(0x7F070134), Re.s(0x7F070135), Re.s(0x7F070278), Re.s(0x7F070279), "UTF-8", "UTF-16LE", "HEX", "HEX + UTF-8", "HEX + UTF-16LE", "HEX + UTF-8 + UTF-16LE"};  // string:format_hex "Hex (little-endian) notation"
        int mask = Config.copyParams;
        boolean[] checked = new boolean[10];
        for(int i = 0; i < 10; ++i) {
            checked[i] = (1 << i & mask) != 0;
        }
        android.ext.CopySelected.1 listener = new DialogInterface.OnClickListener() {
            @Override  // android.content.DialogInterface$OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                int newMask = 0;
                if(which == -1) {
                    for(int i = 0; i < checked.length; ++i) {
                        newMask = checked[i] ? newMask | 1 << i : newMask & ~(1 << i);
                    }
                }
                Config.get(0x7F0B00C1).value = newMask;  // id:config_copy_params
                Config.save();
                CopySelected.this.copy(newMask);
            }
        };
        Alert.show(Alert.create(Tools.getDarkContext()).setCustomTitle(Tools.getCustomTitle(0x7F070277)).setMultiChoiceItems(arr_charSequence, checked, new DialogInterface.OnMultiChoiceClickListener() {  // string:copy_as_search "Copy as a group search"
            @Override  // android.content.DialogInterface$OnMultiChoiceClickListener
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                checked[which] = isChecked;
                if(isChecked) {
                    ListView listView0 = ((AlertDialog)dialog).getListView();
                    if(listView0 != null) {
                        for(int i = 0; i < checked.length; ++i) {
                            if((which < 4 ? 0 : which) != (i < 4 ? 0 : i)) {
                                checked[i] = false;
                                listView0.setItemChecked(i, false);
                            }
                        }
                    }
                }
            }
        }).setPositiveButton(Re.s(0x7F07009B), listener).setNegativeButton(Re.s(0x7F07009C), null));  // string:yes "Yes"
    }
}

