package android.ext;

import android.app.AlertDialog;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnShowListener;
import android.content.DialogInterface;
import android.sup.ArrayListResults;
import android.sup.LongSparseArrayChecked;
import android.text.SpannableString;
import android.util.SparseIntArray;
import android.view.View.OnClickListener;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import java.util.ArrayList;
import java.util.List;

public class EditorListener extends MenuItem implements AdapterView.OnItemClickListener {
    class Impl implements DialogInterface.OnClickListener, DialogInterface.OnDismissListener, DialogInterface.OnShowListener, View.OnClickListener, AdapterView.OnItemClickListener {
        private List checked;
        private AlertDialog dialog;
        private boolean editAll;
        int editFlags;
        private Editor editor;
        int memoryFlags;
        private int tab;
        private boolean updateSave;

        private Impl() {
            this.updateSave = false;
            this.tab = 0;
            this.checked = null;
            this.memoryFlags = 0;
            this.editFlags = 0;
        }

        Impl(Impl editorListener$Impl0) {
        }

        private boolean fill(View v) {
            StringBuilder text = null;
            switch(v.getId()) {
                case 0x7F0B00F9: {  // id:fill_values
                    text = new StringBuilder();
                    int v1 = this.checked.size();
                    for(int i = 0; i < v1; ++i) {
                        AddressItem item = (AddressItem)this.checked.get(i);
                        if(i != 0) {
                            text.append(';');
                        }
                        text.append(item.getStringDataTrim());
                    }
                    break;
                }
                case 0x7F0B00FA:   // id:fill_utf8
                case 0x7F0B00FB: {  // id:fill_utf16le
                    text = new StringBuilder();
                    boolean utf8 = v.getId() == 0x7F0B00FA;  // id:fill_utf8
                    text.append(((char)(utf8 ? 58 : 59)));
                    if(utf8) {
                    label_51:
                        long lastAddr = 0L;
                        int size = this.checked.size();
                        byte[] bytes = new byte[size];
                        for(int i = 0; i < size; ++i) {
                            AddressItem item = (AddressItem)this.checked.get(i);
                            if(lastAddr != 0L && lastAddr + 1L != item.address) {
                                size = i;
                                break;
                            }
                            lastAddr = item.address;
                            bytes[i] = (byte)(((int)item.data));
                        }
                        text.append(new String(bytes, 0, size, ParserNumbers.getCharset(!utf8)));
                    }
                    else {
                        switch(this.editor.getItem().flags) {
                            case 1: {
                                goto label_51;
                            }
                            case 2: {
                                long lastAddr = 0L;
                                int size = this.checked.size();
                                char[] chars = new char[size];
                                for(int i = 0; i < size; ++i) {
                                    AddressItem item = (AddressItem)this.checked.get(i);
                                    if(lastAddr != 0L && lastAddr + 2L != item.address) {
                                        size = i;
                                        break;
                                    }
                                    lastAddr = item.address;
                                    chars[i] = (char)(((int)item.data));
                                }
                                text.append(new String(chars, 0, size));
                                break;
                            }
                            case 4: {
                                long lastAddr = 0L;
                                int size = this.checked.size();
                                char[] chars = new char[size * 2];
                                for(int i = 0; i < size; ++i) {
                                    AddressItem item = (AddressItem)this.checked.get(i);
                                    if(lastAddr != 0L && lastAddr + 4L != item.address) {
                                        size = i;
                                        break;
                                    }
                                    lastAddr = item.address;
                                    chars[i * 2] = (char)(((int)item.data));
                                    chars[i * 2 + 1] = (char)(((int)(item.data >> 16)));
                                }
                                text.append(new String(chars, 0, size * 2));
                            }
                        }
                    }
                    break;
                }
                case 0x7F0B00FC: {  // id:fill_hex
                    text = new StringBuilder();
                    text.append('h');
                    long lastAddr = 0L;
                    int v12 = this.checked.size();
                    char[] hexArr = HexText.hexArray;
                    for(int i = 0; i < v12; ++i) {
                        AddressItem item = (AddressItem)this.checked.get(i);
                        if(lastAddr != 0L && lastAddr + 1L != item.address) {
                            break;
                        }
                        lastAddr = item.address;
                        text.append(' ');
                        int data = (int)item.data;
                        text.append(hexArr[(data & 0xF0) >> 4]);
                        text.append(hexArr[data & 15]);
                    }
                    break;
                }
                case 0x7F0B00FD:   // id:fill_hex_utf8
                case 0x7F0B00FE:   // id:fill_hex_utf16le
                case 0x7F0B00FF: {  // id:fill_hex_utf8_utf16le
                    text = new StringBuilder();
                    text.append('Q');
                    text.append(' ');
                    int v15 = v.getId();
                    long lastAddr = 0L;
                    int size = this.checked.size();
                    byte[] bytes = new byte[size];
                    for(int i = 0; i < size; ++i) {
                        AddressItem item = (AddressItem)this.checked.get(i);
                        if(lastAddr != 0L && lastAddr + 1L != item.address) {
                            size = i;
                            break;
                        }
                        lastAddr = item.address;
                        bytes[i] = (byte)(((int)item.data));
                    }
                    HexText.getText(text, 0, bytes, size, v15 == 0x7F0B00FD || v15 == 0x7F0B00FF, v15 == 0x7F0B00FE || v15 == 0x7F0B00FF, null);  // id:fill_hex_utf8
                }
            }
            if(text != null) {
                this.editor.setNumber(text.toString());
            }
            return text != null;
        }

        void internalEdit(AddressItem item) {
            int v10;
            this.editAll = false;
            this.updateSave = false;
            int total = 0;
            if(item == null) {
                this.editAll = true;
                Object object0 = MainService.instance.getCheckList();
                if(Tools.getSelectedCount(object0) == 0) {
                    Tools.showToast(0x7F07013C);  // string:nothing_selected "Nothing selected"
                    return;
                }
                if(this.checked == null) {
                    this.checked = new ArrayList();
                    if(object0 instanceof boolean[]) {
                        this.tab = 3;
                        if(this.memoryFlags == 0) {
                            this.checked = null;
                            SparseIntArray counts = new SparseIntArray();
                            new TypeSelector(AddressItem.getDataForEditAll(MainService.instance.mMemListAdapter.getPossibleTypes(((boolean[])object0), counts)), counts, "0", Re.s(0x7F0700EF), new DialogInterface.OnClickListener() {  // string:type_change_request "Select the data type to change"
                                @Override  // android.content.DialogInterface$OnClickListener
                                public void onClick(DialogInterface dialog, int flags) {
                                    Impl.this.memoryFlags = flags;
                                    Impl.this.internalEdit(null);
                                }
                            });
                            return;
                        }
                        MemoryContentAdapter memAdapter = MainService.instance.mMemListAdapter;
                        int s = ((boolean[])object0).length - 1;
                        for(int j = 1; j < s; ++j) {
                            if(((boolean[])object0)[j]) {
                                AddressItem addressItem1 = new AddressItem(memAdapter.getAddr(j), 0L, this.memoryFlags);
                                if(addressItem1.isPossibleItem()) {
                                    addressItem1.data = MainService.instance.getContentInAddress(addressItem1.address, addressItem1.flags);
                                    this.checked.add(addressItem1);
                                }
                            }
                        }
                        this.memoryFlags = 0;
                    }
                    else if(object0 instanceof LongSparseArrayChecked) {
                        this.tab = 2;
                        int v3 = ((LongSparseArrayChecked)object0).size();
                        for(int j = 0; j < v3; ++j) {
                            if(((LongSparseArrayChecked)object0).checkAt(j)) {
                                this.checked.add(((AddressItem)((LongSparseArrayChecked)object0).valueAt(j)));
                            }
                        }
                    }
                    else {
                        if(!(object0 instanceof ArrayListResults)) {
                            throw new IllegalArgumentException("Obj is unknown: " + object0);
                        }
                        this.tab = 1;
                        int v5 = ((ArrayListResults)object0).size();
                        for(int i = 0; i < v5; ++i) {
                            if(((ArrayListResults)object0).checked(i)) {
                                this.checked.add(((ArrayListResults)object0).get(i, null));
                            }
                        }
                    }
                }
                if(this.checked != null) {
                    if(this.checked.size() == 0) {
                        Tools.showToast(0x7F07013C);  // string:nothing_selected "Nothing selected"
                        Log.d("It must never happen.");
                        return;
                    }
                    if(this.editFlags == 0) {
                        int curFlags = 0;
                        SparseIntArray counts = new SparseIntArray();
                        for(Object object1: this.checked) {
                            int flags = ((AddressItem)object1).flags;
                            curFlags |= flags;
                            counts.put(flags, counts.get(flags) + 1);
                        }
                        new TypeSelector(AddressItem.getDataForEditAll(curFlags), counts, "0", Re.s(0x7F0700EF), new DialogInterface.OnClickListener() {  // string:type_change_request "Select the data type to change"
                            @Override  // android.content.DialogInterface$OnClickListener
                            public void onClick(DialogInterface dialog, int flags) {
                                Impl.this.editFlags = flags;
                                Impl.this.internalEdit(null);
                            }
                        });
                        return;
                    }
                    for(int j = 0; j < this.checked.size(); ++j) {
                        if(((AddressItem)this.checked.get(j)).flags != this.editFlags) {
                            this.checked.remove(j);
                            --j;
                        }
                    }
                    this.editFlags = 0;
                    if(this.checked.size() == 0) {
                        Tools.showToast(0x7F07013E);  // string:no_items_for_editing "There are no items to edit."
                        Log.d(("It must never happen. " + this.editFlags));
                        return;
                    }
                    item = ((AddressItem)this.checked.get(0)).copy();
                    total = this.checked.size();
                    if(total == 1) {
                        this.editAll = false;
                    }
                }
                if(item == null) {
                    throw new NullPointerException("Something going wrong");
                }
            }
            if(!this.editAll) {
                SavedItem savedItem0 = MainService.instance.savedList.getItemByAddress(item.address);
                if(savedItem0 == null || savedItem0.getType() != item.getType()) {
                    item = new SavedItem(item);
                }
                else {
                    SavedItem savedItem = savedItem0.copy();
                    savedItem.data = item.data;
                    item = savedItem;
                    this.updateSave = true;
                }
            }
            if(this.editAll) {
                item.flags = item.getType();
            }
            if(this.editAll) {
                v10 = 2;
            }
            else {
                v10 = this.updateSave ? 0 : 1;
            }
            this.editor = new Editor(v10, item);
            if(this.editAll) {
                String input = SearchButtonListener.lastTextSearch != null && this.tab == 1 ? SearchButtonListener.lastTextSearch : item.getStringDataTrim();
                this.editor.setNumber(input);
                this.editor.setFillListener(this);
            }
            else if(this.updateSave) {
                this.editor.inSavedList();
            }
            CharSequence charSequence0 = item.getNameShort();
            this.editor.setMessage(Tools.colorizeText(new SpannableString((this.editAll ? Tools.stringFormat(Re.s(0x7F070140), new Object[]{total, charSequence0}) : Tools.stringFormat(Re.s(0x7F07013F), new Object[]{"00000000", charSequence0}))), charSequence0, item.getColor()));  // string:edit_all_info "Edit __d__ addresses as __s__"
            AlertDialog alertDialog0 = Alert.create().setView(this.editor.getViewForAttach()).setPositiveButton(Re.s(0x7F07009B), this).setNegativeButton(Re.s(0x7F07009C), null).create();  // string:yes "Yes"
            this.dialog = alertDialog0;
            if(!this.editAll) {
                alertDialog0.setButton(-3, Re.s(0x7F07008D), this);  // string:goto1 "Goto"
            }
            Alert.setOnShowListener(alertDialog0, this);
            Alert.setOnDismissListener(alertDialog0, this);
            Alert.show(alertDialog0, this.editor.getNumberEdit());
        }

        @Override  // android.content.DialogInterface$OnClickListener
        public void onClick(DialogInterface dialog, int which) {
            if(which == -3 && !this.editAll) {
                AddressItem addressItem0 = this.editor.getItem();
                MainService.instance.goToAddress((EditorListener.this.inMemoryEditor ? addressItem0.address : null), addressItem0.getStringAddress(), Tools.stripColon(0x7F07024E) + ": (" + addressItem0 + ')');  // string:from_item "From the item:"
            }
        }

        @Override  // android.view.View$OnClickListener
        public void onClick(View v) {
            long v13;
            long v11;
            long v9;
            if(v == null || v.getTag() instanceof MenuItem) {
                this.startEdit(null);
                return;
            }
            try {
                if(!this.editAll) {
                    String data = SearchMenuItem.checkScript(this.editor.getNumber());
                    XorMode searchButtonListener$XorMode0 = SearchButtonListener.parseXorMode(data, false);
                    int x = searchButtonListener$XorMode0 == null ? 0 : searchButtonListener$XorMode0.x;
                    if(searchButtonListener$XorMode0 != null) {
                        data = searchButtonListener$XorMode0.input;
                    }
                    SavedItem savedItem0 = this.editor.getSavedItem();
                    savedItem0.setDataFromString(data);
                    savedItem0.alter(x);
                    boolean z = this.editor.isNeedSave();
                    if(z || this.updateSave) {
                        MainService.instance.savedList.add(savedItem0);
                    }
                    Tools.dismiss(this.dialog);
                    this.record(true, z, data);
                    MainService.instance.onMemoryChanged();
                    return;
                }
                if(!this.fill(v)) {
                    try {
                        String input = SearchMenuItem.checkScript(this.editor.getNumber());
                        XorMode searchButtonListener$XorMode1 = SearchButtonListener.parseXorMode(input, false);
                        int x = searchButtonListener$XorMode1 == null ? 0 : searchButtonListener$XorMode1.x;
                        String data = searchButtonListener$XorMode1 == null ? input : searchButtonListener$XorMode1.input;
                        boolean z1 = this.editor.isNeedSave();
                        String step = SearchMenuItem.checkScript(this.editor.getEditStep());
                        int j = 0;
                        int v3 = this.checked.size();
                        boolean z2 = this.editor.isValueChange();
                        String[] parts = null;
                        byte[] bytes = null;
                        char[] chars = null;
                        int flags = this.editor.getItem().flags;
                        if(flags == 1 && ParserNumbers.isString(data)) {
                            bytes = ParserNumbers.getBytes(data);
                        }
                        else if((flags & 6) != 0 && (ParserNumbers.isString(data) && data.charAt(0) == 59)) {
                            chars = data.substring(1).toCharArray();
                        }
                        else if(data.indexOf(59) != -1) {
                            parts = data.split(";");
                            if(parts.length == 0) {
                                parts = null;
                            }
                        }
                        SavedListAdapter savedList = MainService.instance.savedList;
                        Steps steps = null;
                        Result result = null;
                        Result[] results = parts == null ? null : new Result[parts.length];
                        int n = 0;
                        long lastAddr = 0L;
                        for(int i = 0; true; ++i) {
                            if(i >= v3) {
                                savedList.notifyDataSetChanged();
                                MainService.instance.onMemoryChanged();
                                Tools.dismiss(this.dialog);
                                if(SearchButtonListener.lastTextSearch != null && this.tab == 1 && (bytes != null || chars != null)) {
                                    SearchButtonListener.lastTextSearch = input;
                                }
                                this.record(false, z1, input);
                                return;
                            }
                            AddressItem item = (AddressItem)this.checked.get(i);
                            SavedItem savedItem1 = MainService.instance.savedList.getItemByAddress(item.address);
                            boolean updateSave = savedItem1 != null && savedItem1.getType() == item.getType();
                            SavedItem savedItem2 = this.editor.getSavedItem(item);
                            if(z2) {
                                if(bytes != null) {
                                    int n = lastAddr + 1L == item.address ? n : 0;
                                    lastAddr = item.address;
                                    if(n < bytes.length) {
                                        n = n + 1;
                                        v9 = 0xFFL & ((long)bytes[n]);
                                    }
                                    else {
                                        v9 = 0L;
                                        n = n;
                                    }
                                    savedItem2.data = v9;
                                }
                                else if(chars == null) {
                                    if(parts != null) {
                                        n = i % parts.length;
                                        data = parts[n];
                                        Result parserNumbers$Result1 = results[n];
                                        if(parserNumbers$Result1 == null) {
                                            result = AddressItem.parseString(null, data, savedItem2.flags, 0x7F0700CF, item.address);  // string:number_name "number"
                                            results[n] = result;
                                        }
                                        else {
                                            result = parserNumbers$Result1;
                                        }
                                    }
                                    else if(result == null) {
                                        result = AddressItem.parseString(null, data, savedItem2.flags, 0x7F0700CF, item.address);  // string:number_name "number"
                                    }
                                    ++j;
                                    steps = savedItem2.dataFromString(steps, result, data, item.address, step, j);
                                }
                                else {
                                    boolean word = item.flags == 2;
                                    int n = ((long)(word ? 2 : 4)) + lastAddr == item.address ? n : 0;
                                    lastAddr = item.address;
                                    if(n < chars.length) {
                                        v11 = 0xFFFFL & ((long)chars[n]);
                                        ++n;
                                    }
                                    else {
                                        v11 = 0L;
                                    }
                                    savedItem2.data = v11;
                                    if(word) {
                                        n = n;
                                    }
                                    else {
                                        long v12 = savedItem2.data;
                                        if(n < chars.length) {
                                            n = n + 1;
                                            v13 = (0xFFFFL & ((long)chars[n])) << 16;
                                        }
                                        else {
                                            v13 = 0L;
                                            n = n;
                                        }
                                        savedItem2.data = v13 | v12;
                                    }
                                }
                                savedItem2.alter(x);
                            }
                            if(z1 || updateSave) {
                                savedList.add(savedItem2, 0, false);
                            }
                        }
                    }
                    catch(NumberFormatException e) {
                        SearchMenuItem.inputError(e, this.editor.getNumberEdit());
                    }
                    catch(Throwable e) {
                        Log.e("Exception in EditorListener", e);
                    }
                }
            }
            catch(NumberFormatException e) {
                SearchMenuItem.inputError(e, this.editor.getNumberEdit());
            }
            catch(Throwable e) {
                Log.e("Exception in EditorListener", e);
            }
        }

        @Override  // android.content.DialogInterface$OnDismissListener
        public void onDismiss(DialogInterface dialog) {
            try {
                this.editor.getViewForAttachSimple();
            }
            catch(Throwable e) {
                Log.badImplementation(e);
            }
        }

        @Override  // android.widget.AdapterView$OnItemClickListener
        public void onItemClick(AdapterView adapterView0, View view, int position, long id) {
            AddressItem item = null;
            Object object0 = adapterView0.getItemAtPosition(position);
            if(object0 instanceof GotoAddress) {
                MainService.instance.goToAddress(((GotoAddress)object0).getSource(), ((GotoAddress)object0).getAddress(), Tools.stripColon(0x7F070250));  // string:from_boundary "From the boundary"
                return;
            }
            if(object0 instanceof AddressItem) {
                item = (AddressItem)object0;
            }
            if(item != null) {
                if(adapterView0.getId() == 0x7F0B00E8 && ((Config.configClient & 0x400) == 0 || Integer.bitCount(item.flags) != 1)) {  // id:mem_listview
                    new TypeSelector(AddressItem.getDataForEditAll(0x7F, item.address), "0", Re.s(0x7F0700EF), new DialogInterface.OnClickListener() {  // string:type_change_request "Select the data type to change"
                        @Override  // android.content.DialogInterface$OnClickListener
                        public void onClick(DialogInterface dialog, int which) {
                            item.flags = which;
                            Impl.this.startEdit(item);
                        }
                    });
                    return;
                }
                this.startEdit(item);
            }
        }

        @Override  // android.content.DialogInterface$OnShowListener
        public void onShow(DialogInterface dialog) {
            Tools.setButtonListener(dialog, -1, null, this, this.editor.getNumberEdit());
        }

        private void record(boolean single, boolean needSave, String input) {
            String note;
            if(single) {
                int v = MainService.instance.getTabIndex();
                if(v == 1 && MainService.instance.mResultCount == 1L) {
                    this.tab = 1;
                    goto label_7;
                }
                else if(v == 2 && MainService.instance.savedList.getCount() == 1) {
                    this.tab = 2;
                    goto label_7;
                }
            }
            else {
            label_7:
                if(this.tab == 1 || this.tab == 2) {
                    Record record = MainService.instance.currentRecord;
                    if(record != null) {
                        SavedItem savedItem0 = this.editor.getSavedItem();
                        if(this.tab == 2 || needSave) {
                            record.write("\nrevert = ");
                            if(this.tab == 1) {
                                Converter.recordGetResults(record, true);
                            }
                            else {
                                record.write("gg.getListItems()\n");
                            }
                            record.write("local t = ");
                            if(this.tab == 1) {
                                Converter.recordGetResults(record, true);
                            }
                            else {
                                record.write("gg.getListItems()\n");
                            }
                            record.write("for i, v in ipairs(t) do\n");
                            record.write("\tif v.flags == ");
                            Consts.logConst(record, record.consts.TYPE_, savedItem0.flags);
                            record.write(" then\n");
                            record.write("\t\tv.value = ");
                            if((savedItem0.flags & 0x40000000) == 0x40000000) {
                                savedItem0.flags &= 0xBFFFFFFF;
                                record.write("v.value + ");
                            }
                            Consts.logNumberString(record, input);
                            record.write("\n");
                            if(this.tab == 2 || savedItem0.freeze) {
                                record.write("\t\tv.freeze = ");
                                record.write(Boolean.toString(savedItem0.freeze));
                                record.write("\n");
                                if(this.tab == 2 || savedItem0.freezeType != 0) {
                                    switch(savedItem0.freezeType) {
                                        case 0: {
                                            note = "gg.FREEZE_NORMAL";
                                            break;
                                        }
                                        case 1: {
                                            note = "gg.FREEZE_MAY_INCREASE";
                                            break;
                                        }
                                        case 2: {
                                            note = "gg.FREEZE_MAY_DECREASE";
                                            break;
                                        }
                                        case 3: {
                                            note = "gg.FREEZE_IN_RANGE";
                                            break;
                                        }
                                        default: {
                                            note = Integer.toString(savedItem0.freezeType);
                                        }
                                    }
                                    record.write("\t\tv.freezeType = ");
                                    record.write(note);
                                    record.write("\n");
                                    if(savedItem0.freezeType == 3) {
                                        record.write("\t\tv.freezeFrom = ");
                                        Consts.logNumberString(record, savedItem0.getRangeString(true));
                                        record.write("\n");
                                        record.write("\t\tv.freezeTo = ");
                                        Consts.logNumberString(record, savedItem0.getRangeString(false));
                                        record.write("\n");
                                    }
                                }
                            }
                            record.write("\tend\n");
                            record.write("end\n");
                            record.write("gg.addListItems(t)\n");
                            record.write("t = nil\n\n");
                            return;
                        }
                        record.write("revert = ");
                        Converter.recordGetResults(record, true);
                        if((savedItem0.flags & 0x40000000) == 0x40000000) {
                            savedItem0.flags &= 0xBFFFFFFF;
                            record.write("local t = ");
                            Converter.recordGetResults(record, true);
                            record.write("for i, v in ipairs(t) do\n");
                            record.write("\tif v.flags == ");
                            Consts.logConst(record, record.consts.TYPE_, savedItem0.flags);
                            record.write(" then\n");
                            record.write("\t\tv.value = ");
                            record.write("v.value + ");
                            Consts.logNumberString(record, input);
                            record.write("\n");
                            record.write("\tend\n");
                            record.write("end\n");
                            record.write("gg.setValues(t)\n");
                            record.write("t = nil\n\n");
                            return;
                        }
                        record.write("gg.editAll(");
                        Consts.logNumberString(record, input);
                        record.write(", ");
                        Consts.logConst(record, record.consts.TYPE_, savedItem0.flags);
                        record.write(")\n");
                    }
                }
            }
        }

        void startEdit(AddressItem item) {
            this.checked = null;
            this.memoryFlags = 0;
            this.editFlags = 0;
            this.tab = 0;
            this.internalEdit(item);
        }
    }

    private final boolean inMemoryEditor;

    public EditorListener() {
        this(false);
    }

    public EditorListener(boolean inMemoryEditor) {
        super(0x7F070139, 0x7F02004F);  // string:edit_selected "Edit selected"
        this.inMemoryEditor = inMemoryEditor;
    }

    @Override  // android.ext.MenuItem
    public void onClick(View v) {
        new Impl(this, null).onClick(v);
    }

    @Override  // android.widget.AdapterView$OnItemClickListener
    public void onItemClick(AdapterView adapterView0, View view, int position, long id) {
        new Impl(this, null).onItemClick(adapterView0, view, position, id);
    }
}

