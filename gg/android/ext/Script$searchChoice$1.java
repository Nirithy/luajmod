package android.ext;

import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View.OnClickListener;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.regex.Pattern;
import luaj.LuaTable.Iterator;
import luaj.LuaTable;
import luaj.LuaValue;
import luaj.Varargs;

final class Script.searchChoice.1 implements DialogInterface.OnClickListener, DialogInterface.OnDismissListener, TextWatcher, View.OnClickListener, AdapterView.OnItemClickListener {
    ArrayAdapter b;
    LuaTable bool;
    LuaTable current;
    LuaTable item;
    int level;
    ListView ls;
    Script.searchChoice.1 the;

    @Override  // android.text.TextWatcher
    public void afterTextChanged(Editable editable0) {
    }

    @Override  // android.text.TextWatcher
    public void beforeTextChanged(CharSequence charSequence0, int v, int v1, int v2) {
    }

    public Varargs invoke(Varargs varargs0, int v) {
        this.the = this;
        this.current = new LuaTable();
        this.bool = new LuaTable();
        this.level = v;
        View view0 = LayoutInflater.from(Tools.getContext()).inflate(0x7F040035, null);  // layout:service_search_choice
        EditText editText0 = (EditText)view0.findViewById(0x7F0B015E);  // id:service_search_choice_exit
        ListView listView0 = (ListView)view0.findViewById(0x7F0B0161);  // id:service_search_choice_list
        ((Button)view0.findViewById(0x7F0B015F)).setOnClickListener(this);  // id:service_search_choice_select_all
        ((Button)view0.findViewById(0x7F0B0160)).setOnClickListener(this);  // id:service_search_choice_select_inverse
        ArrayList arrayList0 = new ArrayList();
        LuaTable luaTable0 = varargs0.checktable(1);
        this.item = luaTable0;
        Iterator luaTable$Iterator0 = luaTable0.iterator();
        int v1 = 0;
        while(luaTable$Iterator0.next()) {
            ++v1;
            if(luaTable$Iterator0.value().istable()) {
                arrayList0.add(luaTable$Iterator0.key().tojstring());
            }
            else {
                arrayList0.add(luaTable$Iterator0.value().tojstring());
            }
            this.current.rawset(v1, luaTable$Iterator0.key());
        }
        android.ext.ArrayAdapter arrayAdapter0 = new android.ext.ArrayAdapter(Tools.getContext(), 0x1090010, arrayList0);
        listView0.setAdapter(arrayAdapter0);
        listView0.setChoiceMode(2);
        listView0.setOnItemClickListener(this);
        editText0.addTextChangedListener(this);
        this.ls = listView0;
        this.b = arrayAdapter0;
        if(this.level == 1) {
            synchronized(this) {
                ThreadManager.runOnUiThread(new Script.searchChoice.2(this, view0, varargs0));
                Script.waitNotify(this);
                return this.bool;
            }
        }
        ThreadManager.runOnUiThread(new Script.searchChoice.3(this, view0, varargs0));
        return this.bool;
    }

    @Override  // android.content.DialogInterface$OnClickListener
    public void onClick(DialogInterface dialogInterface0, int v) {
    }

    @Override  // android.view.View$OnClickListener
    public void onClick(View view0) {
        int v = view0.getId();
        if(v == 0x7F0B015F) {  // id:service_search_choice_select_all
            for(int v1 = 0; v1 < this.current.length(); ++v1) {
                this.ls.setItemChecked(v1, true);
                this.bool.set(this.current.get(v1 + 1), LuaValue.TRUE);
            }
            return;
        }
        if(v == 0x7F0B0160) {  // id:service_search_choice_select_inverse
            SparseBooleanArray sparseBooleanArray0 = this.ls.getCheckedItemPositions();
            for(int v2 = 0; v2 < this.current.length(); ++v2) {
                if(sparseBooleanArray0.get(v2)) {
                    this.ls.setItemChecked(v2, false);
                    this.bool.set(this.current.get(v2 + 1), LuaValue.NIL);
                }
                else {
                    this.ls.setItemChecked(v2, true);
                    this.bool.set(this.current.get(v2 + 1), LuaValue.TRUE);
                }
            }
        }
    }

    @Override  // android.content.DialogInterface$OnDismissListener
    public void onDismiss(DialogInterface dialogInterface0) {
        if(this.level == 1) {
            synchronized(this) {
                this.notify();
            }
        }
        MainService.instance.eye(false);
    }

    @Override  // android.widget.AdapterView$OnItemClickListener
    public void onItemClick(AdapterView adapterView0, View view0, int v, long v1) {
        SparseBooleanArray sparseBooleanArray0 = this.ls.getCheckedItemPositions();
        LuaValue luaValue0 = this.current.get(v + 1);
        if(sparseBooleanArray0.get(v)) {
            this.ls.setItemChecked(v, true);
            LuaValue luaValue1 = this.item.get(luaValue0);
            if(luaValue1.istable()) {
                Script.searchChoice.1 script$searchChoice$10 = new Script.searchChoice.1();
                this.bool.set(luaValue0, script$searchChoice$10.invoke(LuaValue.varargsOf(new LuaValue[]{luaValue1, luaValue0}), 0).checkvalue(1));
                return;
            }
            this.bool.set(luaValue0, LuaValue.TRUE);
            return;
        }
        this.ls.setItemChecked(v, false);
        this.bool.set(luaValue0, LuaValue.NIL);
    }

    @Override  // android.text.TextWatcher
    public void onTextChanged(CharSequence charSequence0, int v, int v1, int v2) {
        this.current = new LuaTable();
        int v3 = 0;
        if(charSequence0.length() == 0) {
            this.ls.setAdapter(this.b);
            Iterator luaTable$Iterator0 = this.item.iterator();
            while(luaTable$Iterator0.next()) {
                ++v3;
                this.current.rawset(v3, luaTable$Iterator0.key());
            }
        }
        else {
            Pattern pattern0 = Pattern.compile(Tools.quoteFilter(charSequence0.toString()));
            ArrayList arrayList0 = new ArrayList();
            Iterator luaTable$Iterator1 = this.item.iterator();
            while(luaTable$Iterator1.next()) {
                String s = luaTable$Iterator1.value().tojstring();
                String s1 = luaTable$Iterator1.key().tojstring();
                if(pattern0.matcher(s).find() || luaTable$Iterator1.key().isstring() && pattern0.matcher(s1).find()) {
                    if(luaTable$Iterator1.value().istable()) {
                        arrayList0.add(s1);
                    }
                    else {
                        arrayList0.add(s);
                    }
                    ++v3;
                    this.current.rawset(v3, luaTable$Iterator1.key());
                }
            }
            this.ls.setAdapter(new android.ext.ArrayAdapter(Tools.getContext(), 0x1090010, arrayList0));
        }
        for(int v4 = 0; v4 < this.current.length(); ++v4) {
            LuaValue luaValue0 = this.bool.get(this.current.get(v4 + 1));
            this.ls.setItemChecked(v4, luaValue0.isboolean() && luaValue0.checkboolean());
        }
    }
}

