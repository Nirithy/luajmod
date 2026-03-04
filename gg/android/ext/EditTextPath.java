package android.ext;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources.Theme;
import android.fix.LayoutInflater;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.DragEvent;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Filter.FilterResults;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView.BufferType;
import android.widget.TextView;
import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Locale;

public class EditTextPath extends AutoCompleteTextView implements EditTextExt {
    class PathAdapter extends BaseAdapter implements Filterable {
        class PathFilter extends Filter {
            private String getStr(CharSequence constraint) {
                return constraint != null && constraint.length() != 0 ? constraint.toString() : "/";
            }

            @Override  // android.widget.Filter
            protected Filter.FilterResults performFiltering(CharSequence constraint) {
                Filter.FilterResults results = new Filter.FilterResults();
                if(constraint != null) {
                    String s = this.getStr(constraint);
                    File file = new File(s);
                    boolean z = s.endsWith("/");
                    File parent = z ? file : file.getParentFile();
                    String start = (z ? "" : file.getName()).toLowerCase(Locale.US);
                    if(parent.exists()) {
                        String[] arr_s = PathSelector.list(parent);
                        if(arr_s != null && arr_s.length > 0) {
                            ArrayList arrayList0 = PathSelector.sort(EditTextPath.this.pathType, parent, arr_s, start, true);
                            int cnt = arrayList0.size();
                            if(cnt == 1 && ((SortItem)arrayList0.get(0)).name.equals(start)) {
                                cnt = 0;
                            }
                            if(cnt > 0) {
                                File[] files = new File[cnt];
                                for(int i = 0; i < cnt; ++i) {
                                    files[i] = ((SortItem)arrayList0.get(i)).file;
                                }
                                results.values = files;
                                results.count = cnt;
                            }
                        }
                    }
                }
                return results;
            }

            @Override  // android.widget.Filter
            protected void publishResults(CharSequence constraint, Filter.FilterResults results) {
                PathAdapter.this.items = results != null && results.values instanceof File[] ? ((File[])results.values) : null;
                PathAdapter.this.notifyDataSetChanged();
            }
        }

        final Filter filter;
        private File[] items;

        public PathAdapter() {
            this.items = null;
            this.filter = new PathFilter(this);
        }

        @Override  // android.widget.Adapter
        public int getCount() {
            return this.items == null ? 0 : this.items.length;
        }

        @Override  // android.widget.Filterable
        public Filter getFilter() {
            return this.filter;
        }

        @Override  // android.widget.Adapter
        public Object getItem(int position) {
            File[] items = this.items;
            return items == null || position < 0 || position >= items.length ? null : items[position];
        }

        @Override  // android.widget.Adapter
        public long getItemId(int position) {
            return (long)position;
        }

        @Override  // android.widget.Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null) {
                convertView = LayoutInflater.inflateStatic(0x7F040012, parent, false);  // layout:path_suggestion
            }
            Object object0 = this.getItem(position);
            if(object0 instanceof File) {
                ((TextView)convertView.findViewById(0x7F0B0059)).setText(".../" + ((File)object0).getName());  // id:text1
            }
            return convertView;
        }
    }

    private int dataType;
    private static final int imeForce = 0x72000000;
    private int pathType;

    public EditTextPath(Context context) {
        super(context);
        this.pathType = 0;
        this.dataType = -1;
        this.setAdapter(new PathAdapter(this));
        if(!this.isInEditMode()) {
            this.setInputType(((Config.configClient & 8) == 0 ? 0x80001 : 1));
        }
        this.setSingleLine(true);
        this.setSelectAllOnFocus(true);
        this.setMinEms(10);
        this.setCursorVisible(true);
        this.setFocusableInTouchMode(true);
        try {
            Field field0 = TextView.class.getDeclaredField("mCursorDrawableRes");
            field0.setAccessible(true);
            field0.set(this, 0);
        }
        catch(Exception unused_ex) {
        }
        if(!this.isInEditMode()) {
            EditText.checkContrast(this);
        }
        this.setInputType(this.getInputType() | 0x40000);
        this.setHorizontallyScrolling(false);
        this.setMaxLines(0x7FFFFFFF);
    }

    public EditTextPath(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.pathType = 0;
        this.dataType = -1;
        this.setAdapter(new PathAdapter(this));
        if(!this.isInEditMode()) {
            this.setInputType(((Config.configClient & 8) == 0 ? 0x80001 : 1));
        }
        this.setSingleLine(true);
        this.setSelectAllOnFocus(true);
        this.setMinEms(10);
        this.setCursorVisible(true);
        this.setFocusableInTouchMode(true);
        try {
            Field field0 = TextView.class.getDeclaredField("mCursorDrawableRes");
            field0.setAccessible(true);
            field0.set(this, 0);
        }
        catch(Exception unused_ex) {
        }
        if(!this.isInEditMode()) {
            EditText.checkContrast(this);
        }
        this.setInputType(this.getInputType() | 0x40000);
        this.setHorizontallyScrolling(false);
        this.setMaxLines(0x7FFFFFFF);
    }

    public EditTextPath(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.pathType = 0;
        this.dataType = -1;
        this.setAdapter(new PathAdapter(this));
        if(!this.isInEditMode()) {
            this.setInputType(((Config.configClient & 8) == 0 ? 0x80001 : 1));
        }
        this.setSingleLine(true);
        this.setSelectAllOnFocus(true);
        this.setMinEms(10);
        this.setCursorVisible(true);
        this.setFocusableInTouchMode(true);
        try {
            Field field0 = TextView.class.getDeclaredField("mCursorDrawableRes");
            field0.setAccessible(true);
            field0.set(this, 0);
        }
        catch(Exception unused_ex) {
        }
        if(!this.isInEditMode()) {
            EditText.checkContrast(this);
        }
        this.setInputType(this.getInputType() | 0x40000);
        this.setHorizontallyScrolling(false);
        this.setMaxLines(0x7FFFFFFF);
    }

    public EditTextPath(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.pathType = 0;
        this.dataType = -1;
        this.setAdapter(new PathAdapter(this));
        if(!this.isInEditMode()) {
            this.setInputType(((Config.configClient & 8) == 0 ? 0x80001 : 1));
        }
        this.setSingleLine(true);
        this.setSelectAllOnFocus(true);
        this.setMinEms(10);
        this.setCursorVisible(true);
        this.setFocusableInTouchMode(true);
        try {
            Field field0 = TextView.class.getDeclaredField("mCursorDrawableRes");
            field0.setAccessible(true);
            field0.set(this, 0);
        }
        catch(Exception unused_ex) {
        }
        if(!this.isInEditMode()) {
            EditText.checkContrast(this);
        }
        this.setInputType(this.getInputType() | 0x40000);
        this.setHorizontallyScrolling(false);
        this.setMaxLines(0x7FFFFFFF);
    }

    public EditTextPath(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes, Resources.Theme popupTheme) {
        super(context, attrs, defStyleAttr, defStyleRes, popupTheme);
        this.pathType = 0;
        this.dataType = -1;
        this.setAdapter(new PathAdapter(this));
        if(!this.isInEditMode()) {
            this.setInputType(((Config.configClient & 8) == 0 ? 0x80001 : 1));
        }
        this.setSingleLine(true);
        this.setSelectAllOnFocus(true);
        this.setMinEms(10);
        this.setCursorVisible(true);
        this.setFocusableInTouchMode(true);
        try {
            Field field0 = TextView.class.getDeclaredField("mCursorDrawableRes");
            field0.setAccessible(true);
            field0.set(this, 0);
        }
        catch(Exception unused_ex) {
        }
        if(!this.isInEditMode()) {
            EditText.checkContrast(this);
        }
        this.setInputType(this.getInputType() | 0x40000);
        this.setHorizontallyScrolling(false);
        this.setMaxLines(0x7FFFFFFF);
    }

    @Override  // android.view.View
    public void dispatchWindowFocusChanged(boolean hasFocus) {
        try {
            super.dispatchWindowFocusChanged(hasFocus);
        }
        catch(Throwable e) {
            Log.badImplementation(e);
        }
    }

    @Override  // android.widget.TextView
    protected void drawableStateChanged() {
        try {
            super.drawableStateChanged();
        }
        catch(ArrayIndexOutOfBoundsException e) {
            Log.badImplementation(e);
        }
    }

    @Override  // android.ext.EditTextExt
    public int getDataType() {
        return this.dataType;
    }

    public int getPathType() {
        return this.pathType;
    }

    public boolean getSecClipboardEnabled() {
        return false;
    }

    @Override  // android.widget.TextView
    public void invalidateDrawable(Drawable drawable) {
        try {
            super.invalidateDrawable(drawable);
        }
        catch(NoSuchFieldError e) {
            Log.badImplementation(e);
        }
    }

    @Override  // android.widget.TextView
    public boolean onDragEvent(DragEvent event) {
        try {
            return super.onDragEvent(event);
        }
        catch(Throwable e) {
            Log.badImplementation(e);
            return false;
        }
    }

    @Override  // android.widget.TextView
    protected void onDraw(Canvas canvas) {
        try {
            super.onDraw(canvas);
        }
        catch(NullPointerException e) {
            Log.badImplementation(e);
        }
        catch(ArrayIndexOutOfBoundsException e) {
            Log.badImplementation(e);
        }
    }

    @Override  // android.widget.AutoCompleteTextView
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        try {
            int v1 = super.getImeOptions();
            if((v1 & 0x72000000) != 0x72000000) {
                super.setImeOptions(v1 | 0x72000000);
            }
        }
        catch(Throwable e) {
            Log.badImplementation(e);
        }
        try {
            super.onFocusChanged(focused, direction, previouslyFocusedRect);
        }
        catch(Throwable e) {
            Log.badImplementation(e);
        }
    }

    @Override  // android.view.View
    @TargetApi(14)
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        try {
            super.onInitializeAccessibilityNodeInfo(info);
        }
        catch(NullPointerException e) {
            Log.badImplementation(e);
        }
    }

    @Override  // android.widget.AutoCompleteTextView
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        return keyCode != 66 || !this.isPopupShowing() ? super.onKeyPreIme(keyCode, event) : true;
    }

    @Override  // android.widget.EditText
    public boolean onKeyShortcut(int keyCode, KeyEvent event) {
        try {
            return super.onKeyShortcut(keyCode, event);
        }
        catch(NullPointerException e) {
            Log.badImplementation(e);
            return false;
        }
    }

    @Override  // android.widget.TextView
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        try {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
        catch(Throwable e) {
            Log.badImplementation(e);
            ExceptionHandler.sendException(Thread.currentThread(), e, false);
        }
    }

    @Override  // android.widget.EditText
    public boolean onTextContextMenuItem(int id) {
        try {
            return super.onTextContextMenuItem(id);
        }
        catch(Throwable e) {
            Log.badImplementation(e);
            return false;
        }
    }

    @Override  // android.widget.TextView
    public boolean onTouchEvent(MotionEvent event) {
        try {
            return super.onTouchEvent(event);
        }
        catch(Throwable e) {
            Log.badImplementation(e);
            return false;
        }
    }

    @Override  // android.widget.AutoCompleteTextView
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        try {
            super.onWindowFocusChanged(hasWindowFocus);
        }
        catch(Throwable e) {
            Log.badImplementation(e);
        }
    }

    @Override  // android.widget.TextView
    public boolean performLongClick() {
        try {
            return super.performLongClick();
        }
        catch(Throwable e) {
            Log.badImplementation(e);
            return false;
        }
    }

    @Override  // android.view.View
    public void playSoundEffect(int soundConstant) {
        try {
            super.playSoundEffect(soundConstant);
        }
        catch(Throwable e) {
            Log.badImplementation(e);
        }
    }

    @Override  // android.view.View
    public void refreshDrawableState() {
        try {
            super.refreshDrawableState();
        }
        catch(Throwable e) {
            Log.badImplementation(e);
        }
    }

    @Override  // android.view.View
    public void sendAccessibilityEvent(int eventType) {
        try {
            super.sendAccessibilityEvent(eventType);
        }
        catch(Throwable e) {
            Log.badImplementation(e);
        }
    }

    @Override  // android.widget.TextView
    public void setCompoundDrawables(Drawable left, Drawable top, Drawable right, Drawable bottom) {
        try {
            super.setCompoundDrawables(left, top, right, bottom);
        }
        catch(StringIndexOutOfBoundsException e) {
            Log.badImplementation(e);
        }
    }

    @Override  // android.ext.EditTextExt
    public void setDataType(int dataType) {
        this.dataType = dataType;
    }

    @Override  // android.widget.TextView
    public void setHorizontallyScrolling(boolean whether) {
        super.setHorizontallyScrolling(false);
    }

    @Override  // android.widget.TextView
    public void setImeOptions(int imeOptions) {
        try {
            super.setImeOptions(0x72000000 | imeOptions);
        }
        catch(Throwable e) {
            Log.badImplementation(e);
        }
    }

    @Override  // android.widget.TextView
    public void setMaxLines(int maxlines) {
        super.setMaxLines(0x7FFFFFFF);
    }

    public void setPathType(int pathType) {
        this.pathType = pathType;
    }

    @Override  // android.widget.EditText
    public void setText(CharSequence text, TextView.BufferType type) {
        try {
            super.setText(text, type);
        }
        catch(SecurityException | NullPointerException e) {
            Log.badImplementation(e);
        }
    }
}

