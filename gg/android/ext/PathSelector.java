package android.ext;

import android.app.AlertDialog.Builder;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.fix.Button;
import android.fix.LayoutInflater;
import android.fix.ListView;
import android.fix.TextView;
import android.os.Build.VERSION;
import android.sup.ContainerHelpers;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.view.View.OnClickListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Locale;

public class PathSelector extends Button implements View.OnClickListener {
    class Impl implements DialogInterface.OnClickListener, View.OnClickListener, AdapterView.OnItemClickListener {
        private ArrayAdapter adapter;
        private int colorLasm;
        private int colorLua;
        private int colorTxt;
        private int colorZip;
        private EditText create;
        private File currentPath;
        private AlertDialog dialog;
        private EditText edit;
        private TextView failed;
        ListView files;
        private TextView path;
        private int pathType;
        private ImageView sort;

        private Impl() {
            this.colorLua = Tools.getColor(0x7F0A0020, 0xFF005500);  // color:file_lua
            this.colorTxt = Tools.getColor(0x7F0A0021, 0xFF000055);  // color:file_txt
            this.colorZip = Tools.getColor(0x7F0A0023, 0xAAAA5500);  // color:file_zip
            this.colorLasm = Tools.getColor(0x7F0A0022, 0xFF005555);  // color:file_lasm
        }

        Impl(Impl pathSelector$Impl0) {
        }

        private void create(String name) {
            android.ext.EditText editText0 = new android.ext.EditText(Tools.getContext());
            this.create = editText0;
            editText0.setText(name);
            Alert.show(Alert.create().setCustomTitle(Tools.getCustomTitle(Re.s(0x7F0702D2), this.currentPath.getAbsolutePath() + "/...")).setView(InternalKeyboard.getView(editText0, false)).setNegativeButton(Re.s(0x7F0700A1), null).setPositiveButton(Re.s(0x7F07009D), this));  // string:folder_new "New folder"
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            long lastModified;
            long length;
            boolean isDir;
            File file;
            View view;
            if(convertView == null) {
                view = LayoutInflater.from(PathSelector.this.getContext()).inflate(0x7F040010, null);  // layout:path_item
                ViewHolder viewHolder = new ViewHolder();
                viewHolder.name = (TextView)view.findViewById(0x7F0B0051);  // id:name
                viewHolder.date = (TextView)view.findViewById(0x7F0B0052);  // id:date
                viewHolder.size = (TextView)view.findViewById(0x7F0B0053);  // id:size
                viewHolder.icon = Config.setIconSize(((ImageView)view.findViewById(0x7F0B0050)));  // id:icon
                view.setTag(viewHolder);
            }
            else {
                view = convertView;
            }
            try {
                file = (File)this.adapter.getItem(position);
            }
            catch(IndexOutOfBoundsException e) {
                Log.w(("Failed get item: " + position), e);
                file = null;
            }
            if(file == null) {
                file = new File(this.currentPath, "???");
            }
            ViewHolder holder = (ViewHolder)view.getTag();
            String s = file.getName();
            if(s.endsWith(".lua")) {
                view.setBackgroundColor(this.colorLua);
            }
            else if(s.endsWith(".txt")) {
                view.setBackgroundColor(this.colorTxt);
            }
            else if(s.endsWith(".zip")) {
                view.setBackgroundColor(this.colorZip);
            }
            else if(s.endsWith(".lasm")) {
                view.setBackgroundColor(this.colorLasm);
            }
            else {
                view.setBackgroundColor(0);
            }
            holder.name.setText(s);
            try {
                isDir = PathSelector.isDir(file);
                length = isDir ? -1L : file.length();
                lastModified = file.lastModified();
            }
            catch(Throwable e) {
                Log.w(("Failed get info: " + file.getAbsolutePath()), e);
                isDir = false;
                length = -1L;
                lastModified = -1L;
            }
            holder.icon.setImageResource((isDir ? 0x7F020026 : 0x7F020023));  // drawable:ic_folder_outline_white_24dp
            holder.size.setText((length < 0L ? "" : Tools.formatFileSize(PathSelector.this.getContext(), length)));
            TextView textView0 = holder.date;
            CharSequence charSequence0 = lastModified > 0L ? DateFormat.format("yyyy-MM-dd kk:mm:ss", lastModified) : "";
            textView0.setText(charSequence0);
            return view;
        }

        private void go(String folder) {
            this.currentPath = new File(this.currentPath, folder);
        }

        @Override  // android.content.DialogInterface$OnClickListener
        public void onClick(DialogInterface dialog, int which) {
            if(dialog == this.dialog) {
                String text = this.currentPath.getAbsolutePath();
                if(this.pathType == 2) {
                    String name = new File(this.edit.getText().toString()).getName();
                    if(name == null || name.length() == 0) {
                        name = "file.txt";
                    }
                    text = text + "/" + name;
                }
                this.edit.setText(text);
                return;
            }
            EditText create = this.create;
            if(create != null) {
                String s2 = create.getText().toString().trim();
                if(s2.length() > 0) {
                    File file0 = new File(this.currentPath, s2);
                    if(file0.exists()) {
                        if(PathSelector.isDir(file0)) {
                            Tools.showToast(0x7F0702D3);  // string:folder_exists "This folder already exists"
                        }
                        else {
                            Tools.showToast(0x7F0702D4);  // string:folder_file "There is a file with that name"
                        }
                        this.create(s2);
                        return;
                    }
                    if(!file0.mkdirs()) {
                        Tools.showToast(0x7F0702D5);  // string:folder_failed "Failed to create folder"
                        this.create(s2);
                        return;
                    }
                    History.add(s2, 4);
                    this.update();
                    return;
                }
                Tools.showToast(0x7F0702D5);  // string:folder_failed "Failed to create folder"
                this.create(s2);
            }
        }

        @Override  // android.view.View$OnClickListener
        public void onClick(View v) {
            boolean z = false;
            switch(v.getId()) {
                case 0x7F0B0054: {  // id:add
                    this.create("");
                    return;
                }
                case 0x7F0B0055: {  // id:sort
                    boolean z1 = this.sort.getTag() != null;
                    PathSelector.sortByName = z1;
                    this.setSort(z1);
                    this.update();
                    this.scrollTo(0);
                    return;
                }
                case 0x7F0B0056: {  // id:up
                    File src = this.currentPath;
                    this.up();
                    this.update();
                    this.scrollTo(src, true);
                    return;
                }
                default: {
                    Object object0 = PathSelector.this.getTag();
                    if(!(object0 instanceof EditTextPath)) {
                        return;
                    }
                    this.edit = (EditTextPath)object0;
                    View view1 = LayoutInflater.inflateStatic(0x7F040011, null);  // layout:path_selector
                    this.path = (TextView)view1.findViewById(0x7F0B000A);  // id:path
                    Config.setIconSize(((ImageView)view1.findViewById(0x7F0B0054))).setOnClickListener(this);  // id:add
                    ImageView imageView0 = (ImageView)view1.findViewById(0x7F0B0055);  // id:sort
                    this.sort = imageView0;
                    Config.setIconSize(imageView0).setOnClickListener(this);
                    this.setSort(PathSelector.sortByName);
                    Config.setIconSize(((ImageView)view1.findViewById(0x7F0B0056))).setOnClickListener(this);  // id:up
                    this.failed = (TextView)view1.findViewById(0x7F0B0057);  // id:failed
                    ListView listView0 = (ListView)view1.findViewById(0x7F0B0058);  // id:files
                    this.files = listView0;
                    android.ext.PathSelector.Impl.2 pathSelector$Impl$20 = new ArrayAdapter(PathSelector.this.getContext(), new ArrayList()) {
                        @Override  // android.ext.ArrayAdapter
                        public View getView(int position, View convertView, ViewGroup parent) {
                            return Impl.this.getView(position, convertView, parent);
                        }
                    };
                    this.adapter = pathSelector$Impl$20;
                    listView0.setAdapter(pathSelector$Impl$20);
                    listView0.setOnItemClickListener(this);
                    this.pathType = ((EditTextPath)object0).getPathType();
                    File file0 = new File(((EditTextPath)object0).getText().toString());
                    this.currentPath = file0;
                    while(!PathSelector.isDir(this.currentPath)) {
                        this.up();
                    }
                    this.update();
                    if(this.pathType == 0) {
                        z = true;
                    }
                    this.scrollTo(file0, z);
                    AlertDialog.Builder alertDialog$Builder0 = Alert.create().setView(view1).setNegativeButton(Re.s(0x7F0700A1), null);  // string:cancel "Cancel"
                    if(this.pathType != 1) {
                        alertDialog$Builder0.setPositiveButton(Re.s(0x7F07009D), this);  // string:ok "OK"
                    }
                    AlertDialog alertDialog0 = alertDialog$Builder0.create();
                    this.dialog = alertDialog0;
                    Alert.show(alertDialog0);
                }
            }
        }

        @Override  // android.widget.AdapterView$OnItemClickListener
        public void onItemClick(AdapterView adapterView0, View view, int position, long id) {
            File file;
            if(position >= 0 && position < this.adapter.getCount()) {
                try {
                    file = (File)this.adapter.getItem(position);
                }
                catch(Exception e) {
                    Log.w(("Failed get Item: " + position), e);
                    return;
                }
                if(file != null) {
                    if(PathSelector.isDir(file)) {
                        this.go(file.getName());
                        this.update();
                        this.scrollTo(0);
                        return;
                    }
                    if(this.pathType != 0) {
                        this.edit.setText(file.getAbsolutePath());
                        Tools.dismiss(this.dialog);
                    }
                }
            }
        }

        private void scrollTo(int best) {
            this.files.setSelection(best);
            this.files.post(new Runnable() {
                @Override
                public void run() {
                    Impl.this.files.setSelection(best);
                }
            });
        }

        private void scrollTo(File src, boolean isDir) {
            ArrayAdapter adapter = this.adapter;
            int v = adapter.getCount();
            int best = 0;
            String s = src.getAbsolutePath().toLowerCase(Locale.US);
            for(int i = 0; i < v; ++i) {
                File curr = (File)adapter.getItem(i);
                if(src.equals(curr)) {
                    best = i;
                    break;
                }
                if(isDir == PathSelector.isDir(curr) && s.compareTo(curr.getAbsolutePath().toLowerCase(Locale.US)) > 0) {
                    best = i;
                }
            }
            this.scrollTo(best);
        }

        private void setFailedLoadList(boolean status) {
            int v = 0;
            this.failed.setVisibility((status ? 0 : 8));
            ListView listView0 = this.files;
            if(status) {
                v = 8;
            }
            listView0.setVisibility(v);
        }

        private void setSort(boolean byName) {
            this.sort.setTag((byName ? null : this.sort));
            this.sort.setImageDrawable(Tools.getDrawable((byName ? 0x7F02004C : 0x7F02004B)));  // drawable:ic_sort_name_white_24dp
        }

        private void up() {
            this.currentPath = this.currentPath.getParentFile();
            if(this.currentPath == null) {
                this.currentPath = new File("/");
            }
        }

        private void update() {
            this.path.setText(this.currentPath.getAbsolutePath());
            String[] list = PathSelector.list(this.currentPath);
            this.setFailedLoadList(list == null);
            if(list == null) {
                list = ContainerHelpers.EMPTY_STRINGS;
            }
            ArrayList arrayList0 = PathSelector.sort(this.pathType, this.currentPath, list, null, this.sort.getTag() == null);
            this.adapter.clear();
            this.adapter.setNotifyOnChange(false);
            for(Object object0: arrayList0) {
                this.adapter.add(((SortItem)object0).file);
            }
            this.adapter.notifyDataSetChanged();
            this.adapter.setNotifyOnChange(true);
        }
    }

    static class SortItem {
        final File file;
        final boolean isDir;
        final String name;

        SortItem(File file, String name, boolean isDir) {
            this.file = file;
            this.name = name;
            this.isDir = isDir;
        }
    }

    static class ViewHolder {
        public TextView date;
        public ImageView icon;
        public TextView name;
        public TextView size;

    }

    public static final int FILE = 1;
    public static final int FOLDER = 0;
    public static final int NEW_FILE = 2;
    static boolean sortByName;

    static {
        PathSelector.sortByName = true;
    }

    public PathSelector(Context context) {
        super(context);
        this.init();
    }

    public PathSelector(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init();
    }

    public PathSelector(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init();
    }

    public PathSelector(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.init();
    }

    private static void addPath(String dir, ArrayList arrayList0, String data, String orig) {
        int si;
        if(orig != null) {
            String check = orig;
            int i = 0;
            if(data == null) {
                si = 1;
            }
            else {
                try {
                    si = 3;
                    while(true) {
                    label_7:
                        if(i >= si) {
                            return;
                        }
                        if(i != 1) {
                        label_11:
                            if(i != 2) {
                            label_14:
                                File file = new File(check);
                                for(int depth = 0; true; ++depth) {
                                    String s4 = file.getAbsolutePath();
                                    if(!s4.startsWith(dir)) {
                                        break;
                                    }
                                    if(file.isDirectory() || file.isFile()) {
                                        String path = s4.substring(dir.length()).split("/", -1)[0];
                                        if(!arrayList0.contains(path)) {
                                            arrayList0.add(path);
                                        }
                                    }
                                    file = file.getParentFile();
                                    if(file == null || (file.getAbsolutePath().length() <= 1 || depth >= 20)) {
                                        break;
                                    }
                                }
                            }
                            else if(orig.startsWith(data)) {
                                check = "/data/data/" + orig.substring(data.length());
                                goto label_14;
                            }
                        }
                        else if(orig.startsWith("/data/data/")) {
                            check = data + orig.substring(11);
                            goto label_11;
                        }
                        ++i;
                    }
                }
                catch(Throwable e) {
                    Log.badImplementation(e);
                    return;
                }
            }
            goto label_7;
        }
    }

    private void init() {
        this.setText("...");
        this.setOnClickListener(this);
    }

    static boolean isDir(File file) {
        try {
            return file.isDirectory();
        }
        catch(Throwable unused_ex) {
            return false;
        }
    }

    static String[] list(File dir) {
        String[] ret;
        try {
            ret = dir.list();
        }
        catch(SecurityException e) {
            ret = null;
            Log.w(("Failed list dir: " + dir.getAbsolutePath()), e);
        }
        if(ret != null) {
            return ret;
        }
        try {
            String s = dir.getAbsolutePath();
            String s1 = s.startsWith("/data/user/") ? "/data/user/" + s.split("/", -1)[3] + "/" : "/data/user/0/";
            String path = String.valueOf(s) + '/';
            ArrayList list = new ArrayList();
            PathSelector.addPath(path, list, s1, "/data/anr");
            PathSelector.addPath(path, list, s1, "/data/app");
            PathSelector.addPath(path, list, s1, "/data/app-lib");
            PathSelector.addPath(path, list, s1, "/data/app-private");
            PathSelector.addPath(path, list, s1, "/data/dalvik-cache/profiles");
            PathSelector.addPath(path, list, s1, "/data/dalvik-cache/x86");
            PathSelector.addPath(path, list, s1, "/data/dalvik-cache/armeabi");
            PathSelector.addPath(path, list, s1, "/data/dalvik-cache/armeabi-v7a");
            PathSelector.addPath(path, list, s1, "/data/dalvik-cache/arm");
            PathSelector.addPath(path, list, s1, "/data/dalvik-cache/arm64-v8a");
            PathSelector.addPath(path, list, s1, "/data/dalvik-cache/arm64");
            PathSelector.addPath(path, list, s1, "/data/dalvik-cache/x86");
            PathSelector.addPath(path, list, s1, "/data/dalvik-cache/x86_64");
            PathSelector.addPath(path, list, s1, "/data/local/tmp");
            PathSelector.addPath(path, list, s1, "/data/misc/user");
            PathSelector.addPath(path, list, s1, "/data/misc/zoneinfo");
            PathSelector.addPath(path, list, s1, "/data/resource-cache");
            PathSelector.addPath(path, list, s1, "/data/security");
            PathSelector.addPath(path, list, s1, "/data/system/cache");
            PathSelector.addPath(path, list, s1, "/data/system/registered_services");
            PathSelector.addPath(path, list, s1, "/data/system/shared_prefs");
            PathSelector.addPath(path, list, s1, "/data/system/users");
            PathSelector.addPath(path, list, s1, "/data/tmp");
            PathSelector.addPath(path, list, s1, s1);
            PathSelector.addPath(path, list, s1, "/storage/emulated/legacy");
            PathSelector.addPath(path, list, s1, "/storage/emulated/0");
            PathSelector.addPath(path, list, s1, "/storage/emulated/sdcard0");
            PathSelector.addPath(path, list, s1, "/storage/emulated/sdcard1");
            PathSelector.addPath(path, list, s1, "/storage/sdcard0");
            PathSelector.addPath(path, list, s1, "/storage/sdcard1");
            PathSelector.addPath(path, list, s1, "/sdcard");
            PathSelector.addPath(path, list, s1, Tools.getSdcardPath());
            Iterator iterator0 = Tools.getPackageManager().getInstalledApplications(0).iterator();
            while(true) {
                if(!iterator0.hasNext()) {
                    return list.size() == 0 ? null : ((String[])list.toArray(new String[list.size()]));
                }
                Object object0 = iterator0.next();
                ApplicationInfo ai = (ApplicationInfo)object0;
                PathSelector.addPath(path, list, s1, ai.dataDir);
                PathSelector.addPath(path, list, s1, ai.nativeLibraryDir);
                PathSelector.addPath(path, list, s1, ai.publicSourceDir);
                PathSelector.addPath(path, list, s1, ai.sourceDir);
                if(Build.VERSION.SDK_INT >= 21 && ai.splitPublicSourceDirs != null) {
                    String[] arr_s1 = ai.splitPublicSourceDirs;
                    for(int v = 0; v < arr_s1.length; ++v) {
                        PathSelector.addPath(path, list, s1, arr_s1[v]);
                    }
                }
                if(Build.VERSION.SDK_INT >= 24) {
                    PathSelector.addPath(path, list, s1, ai.deviceProtectedDataDir);
                }
                PathSelector.addPath(path, list, s1, "/data/data/" + ai.packageName);
                if(Build.VERSION.SDK_INT >= 21) {
                    PathSelector.addPath(path, list, s1, "/data/app/" + ai.packageName + "-1/base.apk");
                    PathSelector.addPath(path, list, s1, "/data/app/" + ai.packageName + "-2/base.apk");
                }
                else {
                    PathSelector.addPath(path, list, s1, "/data/app/" + ai.packageName + "-1.apk");
                    PathSelector.addPath(path, list, s1, "/data/app/" + ai.packageName + "-2.apk");
                }
            }
        }
        catch(Throwable e) {
            Log.badImplementation(e);
            return null;
        }
    }

    @Override  // android.view.View$OnClickListener
    public void onClick(View v) {
        new Impl(this, null).onClick(v);
    }

    static ArrayList sort(int pathType, File parent, String[] list, String start, boolean byName) {
        ArrayList items = new ArrayList();
        for(int i = 0; i < list.length; ++i) {
            String name = list[i];
            String s2 = name.toLowerCase(Locale.US);
            if(start == null || s2.startsWith(start)) {
                File file1 = new File(parent, name);
                boolean z1 = PathSelector.isDir(file1);
                if(pathType != 0 || z1) {
                    items.add(new SortItem(file1, s2, z1));
                }
            }
        }
        if(items.size() > 1) {
            android.ext.PathSelector.1 pathSelector$10 = new Comparator() {
                public int compare(SortItem o1, SortItem o2) {
                    boolean isDir1 = o1.isDir;
                    if(isDir1 != o2.isDir) {
                        return isDir1 ? -1 : 1;
                    }
                    try {
                        if(byName) {
                            return NaturalOrderComparator.compare(o1.name, o2.name);
                        }
                        return o1.file.lastModified() <= o2.file.lastModified() ? 1 : -1;
                    }
                    catch(Throwable e) {
                        Log.badImplementation(e);
                        return 0;
                    }
                }

                @Override
                public int compare(Object object0, Object object1) {
                    return this.compare(((SortItem)object0), ((SortItem)object1));
                }
            };
            try {
                Collections.sort(items, pathSelector$10);
                Log.d(("Sort " + items.size() + " in " + 0L));
            }
            catch(IllegalArgumentException e) {
                StringBuilder log = new StringBuilder();
                log.append("Failed sort:\n");
                android.ext.PathSelector.2 cmp = new Comparator() {
                    public int compare(SortItem o1, SortItem o2) {
                        int ret;
                        int v = 68;
                        boolean isDir1 = o1.isDir;
                        boolean isDir2 = o2.isDir;
                        if(isDir1 == isDir2) {
                            ret = NaturalOrderComparator.compare(o1.name, o2.name);
                        }
                        else if(isDir1) {
                            ret = -1;
                        }
                        else {
                            ret = 1;
                        }
                        log.append(((char)(isDir1 ? 68 : 70)));
                        log.append(':');
                        log.append(o1);
                        log.append(':');
                        log.append(o1.name);
                        log.append('\n');
                        StringBuilder stringBuilder0 = log;
                        if(!isDir2) {
                            v = 70;
                        }
                        stringBuilder0.append(((char)v));
                        log.append(':');
                        log.append(o2);
                        log.append(':');
                        log.append(o2.name);
                        log.append('\n');
                        log.append(ret);
                        log.append('\n');
                        return ret;
                    }

                    @Override
                    public int compare(Object object0, Object object1) {
                        return this.compare(((SortItem)object0), ((SortItem)object1));
                    }
                };
                try {
                    Collections.sort(items, cmp);
                }
                catch(IllegalArgumentException unused_ex) {
                    ExceptionHandler.sendMessage(log.toString());
                }
                Log.badImplementation(e);
            }
            return items;
        }
        return items;
    }
}

