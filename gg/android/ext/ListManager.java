package android.ext;

import android.app.AlertDialog.Builder;
import android.app.AlertDialog;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnShowListener;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.fix.LayoutInflater;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ListManager implements DialogInterface.OnClickListener, DialogInterface.OnDismissListener, DialogInterface.OnShowListener, View.OnClickListener, View.OnLongClickListener {
    private static final String LIST_PATH = "save-path";
    public static final int LOAD_API = 4;
    public static final int LOAD_APPEND = 8;
    public static final int LOAD_FREEZE = 1;
    public static final int LOAD_VALUES = 2;
    public static final int LOAD_VALUES_FREEZE = 3;
    public static final String NEW_LINE = "\n";
    public static final int SAVE_AS_TEXT = 1;
    public static final char SEPARATOR = '|';
    public static final String TEXT_SEPARATOR = "; ";
    private CheckBox asText;
    private AlertDialog dialog;
    private EditText edit;
    boolean fromExport;
    private RadioGroup group;
    private static int lastMode;
    private SavedItem[] list;
    private ProcessInfo processInfo;

    static {
        ListManager.lastMode = -1;
    }

    public ListManager(ProcessInfo processInfo, SavedItem[] list) {
        this.fromExport = false;
        if(processInfo == null) {
            Alert.show(Alert.create().setCustomTitle(Tools.getCustomTitle(0x7F0700C1)).setMessage(Re.s(0x7F0700BD)).setNegativeButton(Re.s(0x7F07009D), null));  // string:load_list "Load list"
            return;
        }
        if(list != null && list.length == 0) {
            Alert.show(Alert.create().setMessage(Re.s(0x7F070143)).setNegativeButton(Re.s(0x7F07009D), null));  // string:empty_save "The saved list is empty. There is nothing to save."
            return;
        }
        MainService.instance.mDaemonManager.getRegionList();
        this.processInfo = processInfo;
        this.list = list;
        View view0 = LayoutInflater.inflateStatic(0x7F040015, null);  // layout:save
        ((TextView)view0.findViewById(0x7F0B000E)).setText(Re.s((list == null ? 0x7F0701A5 : 0x7F0701A4)));  // id:message
        EditTextPath edit = (EditTextPath)view0.findViewById(0x7F0B000F);  // id:file
        this.edit = edit;
        edit.setText(PkgPath.load("save-path", "-list", ".txt"));
        edit.setDataType(4);
        edit.setPathType((list == null ? 2 : 1));
        view0.findViewById(0x7F0B000B).setTag(edit);  // id:path_selector
        if(list == null) {
            view0.findViewById(0x7F0B005F).setVisibility(0);  // id:load_stuff
        }
        else {
            view0.findViewById(0x7F0B005D).setVisibility(0);  // id:save_stuff
        }
        RadioGroup radioGroup0 = (RadioGroup)view0.findViewById(0x7F0B0060);  // id:mode
        this.group = radioGroup0;
        if(ListManager.lastMode > 0) {
            radioGroup0.check(ListManager.lastMode);
        }
        ((RadioButton)view0.findViewById(0x7F0B0061)).setText(Re.s(0x7F07002C));  // id:mode_0
        ((RadioButton)view0.findViewById(0x7F0B0062)).setText(Re.s(0x7F07002D));  // id:mode_1
        ((RadioButton)view0.findViewById(0x7F0B0063)).setText(Re.s(0x7F07002E));  // id:mode_2
        CheckBox checkBox0 = (CheckBox)view0.findViewById(0x7F0B005E);  // id:as_text
        this.asText = checkBox0;
        Tools.setButtonHelpBackground(checkBox0);
        checkBox0.setOnLongClickListener(this);
        AlertDialog.Builder alertDialog$Builder0 = Alert.create().setView(InternalKeyboard.getView(view0, false)).setPositiveButton(Re.s((list == null ? 0x7F07008C : 0x7F0700AF)), this).setNegativeButton(Re.s(0x7F0700A1), null);  // string:save "Save"
        if(list == null && MainService.instance.savedList.getCount() != 0) {
            alertDialog$Builder0.setNeutralButton(Re.s(0x7F0701A6), this);  // string:append "Append"
        }
        AlertDialog alertDialog0 = alertDialog$Builder0.create();
        this.dialog = alertDialog0;
        Alert.setOnShowListener(alertDialog0, this);
        Alert.setOnDismissListener(alertDialog0, this);
        Alert.show(alertDialog0, edit);
    }

    private static String escape(String str) [...] // 潜在的解密器

    public static void loadList(int pid, String filename, int mode) throws IOException {
        File file0 = new File(filename);
        if(file0.exists()) {
            BufferedReader bufferedReader0 = new BufferedReader(new FileReader(file0));
            SavedListAdapter savedList = MainService.instance.savedList;
            if((mode & 8) == 0) {
                savedList.clear();
            }
            int lineNum = 0;
            boolean sameRun = false;
            while(true) {
                try {
                label_8:
                    String s1 = bufferedReader0.readLine();
                    if(s1 == null) {
                        break;
                    }
                    ++lineNum;
                    if(lineNum == 1) {
                        try {
                            sameRun = Integer.parseInt(s1) == pid;
                        }
                        catch(Throwable e) {
                            Log.w(("Failed parse pid: \'" + s1 + '\''), e);
                        }
                        goto label_8;
                    }
                    String[] arr_s = s1.split("\\|");
                    if(arr_s.length < 10) {
                        Log.w(("Failed parse line: \'" + s1 + "\' " + arr_s.length));
                        goto label_8;
                    }
                    try {
                        long v3 = ParserNumbers.parseBigLong(arr_s[1], 16);
                        long v4 = ParserNumbers.parseBigLong(arr_s[3], 16);
                        int v5 = Integer.parseInt(arr_s[2], 16);
                        String s2 = arr_s[0];
                        boolean z1 = (mode & 1) == 0 ? false : arr_s[4].equals("1");
                        SavedItem savedItem0 = new SavedItem(v3, v4, v5, s2, z1, Byte.parseByte(arr_s[5]), ParserNumbers.parseBigLong(arr_s[6], 16), ParserNumbers.parseBigLong(arr_s[7], 16));
                        long v6 = ParserNumbers.parseBigLong(arr_s[10], 16);
                        if(!sameRun) {
                            Region regionList$Region0 = RegionList.getRegionItem(savedItem0.address);
                            if(regionList$Region0 != null && regionList$Region0.getType().equals(arr_s[8]) && regionList$Region0.getInternalName().equals(arr_s[9]) && savedItem0.address == regionList$Region0.start + v6) {
                                Log.d(("ASLR: " + Long.toHexString(savedItem0.address) + " ??? " + Long.toHexString(regionList$Region0.start + v6) + "; " + arr_s[8] + ' ' + arr_s[9] + ' ' + Long.toHexString(v6) + "; " + regionList$Region0.getType() + ' ' + regionList$Region0.getInternalName()));
                            }
                            else {
                                Region region = RegionList.getRegionItem(arr_s[8], arr_s[9], v6);
                                if(region != null) {
                                    long address = region.start + v6;
                                    Log.d(("Fix: " + Long.toHexString(savedItem0.address) + " -> " + Long.toHexString(address) + "; " + arr_s[8] + ' ' + arr_s[9] + ' ' + Long.toHexString(v6) + "; " + region.getType() + ' ' + region.getInternalName()));
                                    savedItem0.address = address;
                                }
                            }
                        }
                        if((mode & 2) != 0) {
                            savedItem0.alter();
                        }
                        savedList.add(savedItem0, 0, false);
                    }
                    catch(Throwable e) {
                        Log.w(("Failed parse line: \'" + s1 + '\''), e);
                    }
                }
                catch(OutOfMemoryError e) {
                    Log.w("OOM on load list", e);
                    break;
                }
            }
            bufferedReader0.close();
            savedList.notifyDataSetChanged();
            savedList.reloadData();
            if(!sameRun && (mode & 4) == 0) {
                Alert.show(Alert.create().setCustomTitle(Tools.getCustomTitle(0x7F0700C1)).setMessage(Re.s(0x7F0700C2)).setNegativeButton(Re.s(0x7F07009D), null));  // string:load_list "Load list"
            }
        }
    }

    @Override  // android.content.DialogInterface$OnClickListener
    public void onClick(DialogInterface dialog, int which) {
        Button button0 = ((AlertDialog)dialog).getButton(which);
        if(button0 != null) {
            this.onClick(button0);
        }
    }

    @Override  // android.view.View$OnClickListener
    public void onClick(View v) {
        int mode;
        int v = 1;
        EditText edit = this.edit;
        if(v != null && edit != null) {
            String s = edit.getText().toString().trim();
            if(Tools.isPath(s)) {
                boolean load = this.list == null;
                if(!load || Tools.isFile(s)) {
                    History.add(s, 4);
                    if(load) {
                        MainService.instance.lockApp();
                        RadioGroup group = this.group;
                        if(group != null) {
                            int v1 = group.getCheckedRadioButtonId();
                            ListManager.lastMode = v1;
                            switch(v1) {
                                case 0x7F0B0062: {  // id:mode_1
                                    mode = 2;
                                    break;
                                }
                                case 0x7F0B0063: {  // id:mode_2
                                    mode = 0;
                                    break;
                                }
                                default: {
                                    mode = 3;
                                }
                            }
                            if(v.getTag() instanceof Integer) {
                                mode |= 8;
                            }
                            try {
                                ListManager.loadList(this.processInfo.pid, s, mode);
                                Tools.dismiss(this.dialog);
                                Record record = MainService.instance.currentRecord;
                                if(record != null) {
                                    record.write("gg.loadList(");
                                    Consts.logString(record, s);
                                    record.write(", ");
                                    Consts.logConst(record, record.consts.LOAD_, mode);
                                    record.write(")\n");
                                    return;
                                }
                            }
                            catch(IOException e) {
                                Log.w("Failed load list", e);
                                Tools.showToast(e.getMessage());
                                return;
                            }
                        }
                    }
                    else {
                        CheckBox asText = this.asText;
                        if(asText != null) {
                            try {
                                if(!asText.isChecked()) {
                                    v = 0;
                                }
                                ListManager.saveList(this.processInfo.pid, this.list, s, v);
                                Tools.dismiss(this.dialog);
                                Record record = MainService.instance.currentRecord;
                                if(record != null) {
                                    if(this.fromExport) {
                                        record.write("\nlocal prev = gg.getListItems()\n");
                                        record.write("gg.clearList()\n");
                                        record.write("local t = ");
                                        Converter.recordGetResults(record, true);
                                        record.write("gg.addListItems(t)\n");
                                        record.write("t = nil\n");
                                    }
                                    record.write("gg.saveList(");
                                    Consts.logString(record, s);
                                    record.write(", ");
                                    Consts.logConst(record, record.consts.SAVE_, v);
                                    record.write(")");
                                    if(this.fromExport) {
                                        record.write("\ngg.clearList()\n");
                                        record.write("gg.addListItems(prev)\n");
                                        record.write("prev = nil\n");
                                    }
                                    record.write("\n");
                                    return;
                                }
                            }
                            catch(IOException e) {
                                Log.w("Failed save list", e);
                                Tools.showToast(e.getMessage());
                                return;
                            }
                        }
                    }
                }
            }
        }
        try {
        }
        catch(IOException e) {
            Log.w("Failed load list", e);
            Tools.showToast(e.getMessage());
        }
    }

    @Override  // android.content.DialogInterface$OnDismissListener
    public void onDismiss(DialogInterface dialog) {
        EditText edit = this.edit;
        if(edit != null) {
            PkgPath.save(edit.getText().toString(), "save-path", "-list", ".txt");
        }
    }

    @Override  // android.view.View$OnLongClickListener
    public boolean onLongClick(View v) {
        if(v != null && v.getId() == 0x7F0B005E) {  // id:as_text
            Alert.show(Alert.create().setMessage(Re.s(0x7F070259)).setNegativeButton(Re.s(0x7F07009D), null));  // string:as_text_help "Saves the list in text format, convenient for human read. Loading 
                                                                                                                // of this format is not supported."
            return true;
        }
        return false;
    }

    @Override  // android.content.DialogInterface$OnShowListener
    public void onShow(DialogInterface dialog) {
        Tools.setButtonListener(dialog, -1, null, this);
        Tools.setButtonListener(dialog, -3, -3, this);
    }

    public static void saveList(int pid, SavedItem[] list, String filename, int mode) throws IOException {
        long offset;
        String name;
        String access;
        File file0 = new File(filename);
        File file1 = file0.getParentFile();
        if(file1 != null) {
            file1.mkdirs();
        }
        boolean asText = (mode & 1) != 0;
        FileWriter writer = new FileWriter(file0);
        if(!asText) {
            writer.write(pid + "\n");
        }
        for(int i = 0; i < list.length; ++i) {
            SavedItem item = list[i];
            if(item != null) {
                Region regionList$Region0 = RegionList.getRegionItem(item.address);
                if(regionList$Region0 != null && regionList$Region0.getInternalName().length() != 0) {
                    access = regionList$Region0.getType();
                    name = regionList$Region0.getInternalName();
                    offset = item.address - regionList$Region0.start;
                }
                else {
                    access = "";
                    name = "";
                    offset = 0L;
                }
                StringBuilder line = new StringBuilder();
                if(asText) {
                    line.append(RegionList.getRegion(item.address));
                    line.append("; ");
                    line.append(item.getStringAddress());
                    line.append("; ");
                    line.append("Var #00000000");
                    line.append("; ");
                    line.append(item.getStringDataTrim());
                    line.append("; ");
                    line.append(item.getNameShort());
                }
                else {
                    line.append("Var #00000000");
                    line.append('|');
                    line.append(Long.toHexString(item.address));
                    line.append('|');
                    line.append(Integer.toHexString(item.flags));
                    line.append('|');
                    line.append(Long.toHexString(item.data));
                    line.append('|');
                    line.append(((char)(item.freeze ? 49 : 0x30)));
                    line.append('|');
                    line.append(((int)item.freezeType));
                    line.append('|');
                    line.append(Long.toHexString(item.freezeFrom));
                    line.append('|');
                    line.append(Long.toHexString(item.freezeTo));
                    line.append('|');
                    line.append(ListManager.escape(access));
                    line.append('|');
                    line.append(ListManager.escape(name));
                    line.append('|');
                    line.append(Long.toHexString(offset));
                }
                line.append("\n");
                writer.write(line.toString());
            }
        }
        writer.close();
    }

    private static boolean updateOldList(String pkg) {
        try {
            SharedPreferences sharedPreferences0 = MainService.instance.getSharedPreferences(pkg, 0);
            int v = sharedPreferences0.getInt("size", 0);
            SavedItem[] arr_savedItem = new SavedItem[v];
            for(int i = 0; true; ++i) {
                if(i >= v) {
                    int v2 = sharedPreferences0.getInt("pid", 0);
                    for(int i = 0; true; ++i) {
                        if(i >= 10) {
                            return false;
                        }
                        String s1 = PkgPath.path(null, "save-path");
                        StringBuilder stringBuilder0 = new StringBuilder(String.valueOf(pkg));
                        String s2 = i == 0 ? "" : ((int)(i + 45));
                        File file0 = new File(s1, stringBuilder0.append(s2).append(".txt").toString());
                        if(!file0.exists()) {
                            try {
                                ListManager.saveList(v2, arr_savedItem, file0.getAbsolutePath(), 0);
                                if(file0.exists()) {
                                    return true;
                                }
                            }
                            catch(IOException e) {
                                Log.w("Failed save saved list", e);
                            }
                        }
                    }
                }
                try {
                    arr_savedItem[i] = new SavedItem(sharedPreferences0.getLong(i + "-address", 0L), sharedPreferences0.getLong(i + "-data", 0L), sharedPreferences0.getInt(i + "-flags", 4), sharedPreferences0.getString(i + "-name", null), sharedPreferences0.getBoolean(i + "-freeze", false), ((byte)sharedPreferences0.getInt(i + "-freezeType", 0)), sharedPreferences0.getLong(i + "-freezeFrom", 0L), sharedPreferences0.getLong(i + "-freezeTo", 0L));
                }
                catch(Exception e) {
                    Log.w(("Failed load item: " + i), e);
                }
            }
        }
        catch(Exception e) {
            Log.w("Failed load saved list", e);
            return false;
        }
    }

    public static void updateOldLists() {
        File file0 = Uninstaller.getSharedPrefsFile("tmp").getParentFile();
        if(file0.exists()) {
            String[] arr_s = file0.list();
            if(arr_s != null) {
                for(int v = 0; v < arr_s.length; ++v) {
                    String file = arr_s[v];
                    if(file != null && !file.endsWith("_preferences.xml") && file.endsWith(".xml")) {
                        String s1 = file.substring(0, file.length() - 4);
                        if(!s1.equals("null_preferences") && !s1.equals("DefaultStorage")) {
                            Log.d(("Try convert \'" + s1 + "\'."));
                            if(ListManager.updateOldList(s1)) {
                                Log.d(("All ok - remove \'" + s1 + "\'."));
                                new File(file0, file).delete();
                            }
                        }
                    }
                }
            }
        }
    }
}

