package android.ext;

import android.content.Context;
import android.content.res.Resources;
import android.fix.KeyboardView;
import android.fix.LayoutInflater;
import android.inputmethodservice.Keyboard.Key;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener;
import android.util.AttributeSet;
import android.util.SparseIntArray;
import android.view.KeyEvent;
import android.view.View.OnFocusChangeListener;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.view.Window.Callback;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.EditText;
import android.widget.ScrollView;
import java.lang.ref.WeakReference;

public class InternalKeyboard extends KeyboardView {
    public static class Flags {
        public int value;

        public Flags(int value) {
            this.value = value;
        }
    }

    public static abstract class FocusListener implements View.OnFocusChangeListener {
        @Override  // android.view.View$OnFocusChangeListener
        public void onFocusChange(View v, boolean hasFocus) {
            if((Config.configClient & 1) != 0) {
                InternalKeyboard kbdView = (InternalKeyboard)v.getRootView().findViewById(0x7F0B002A);  // id:keyboard
                if(kbdView != null) {
                    boolean z1 = this.useExternal(v, hasFocus);
                    if(!z1) {
                        hasFocus = false;
                    }
                    kbdView.needExternal(v, hasFocus);
                    if(!z1) {
                        Tools.hideKeyboard(v);
                    }
                }
            }
        }

        protected abstract boolean useExternal(View arg1, boolean arg2);
    }

    public static final int COLON_TO_QUESTION = 1;
    public static final int PATTERN = 15;
    public static final int R_TO_DOLLAR = 8;
    public static final int SEMICOLON_TO_ASTERISK = 2;
    public static final int TILDE_TO_CARET = 4;
    private boolean allowUseInternal;
    private int fixed;
    private int flags;
    private boolean fn;
    private boolean hex;
    private boolean hide;
    private WeakReference weakWindow;

    public InternalKeyboard(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.allowUseInternal = true;
        this.hide = false;
        this.flags = 0;
        this.weakWindow = new WeakReference(null);
        this.fixed = -1;
        this.init(context);
    }

    public InternalKeyboard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.allowUseInternal = true;
        this.hide = false;
        this.flags = 0;
        this.weakWindow = new WeakReference(null);
        this.fixed = -1;
        this.init(context);
    }

    public InternalKeyboard(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.allowUseInternal = true;
        this.hide = false;
        this.flags = 0;
        this.weakWindow = new WeakReference(null);
        this.fixed = -1;
        this.init(context);
    }

    public static boolean allowUsage() {
        try {
            return LayoutInflater.inflateStatic(0x7F040006, null) == null ? false : true;  // layout:keyboard
        }
        catch(Throwable e) {
            Log.e("Check internal keyboard fail", e);
            return false;
        }
    }

    private void fixWidth(Window window, boolean horizontal) {
        WindowManager.LayoutParams windowManager$LayoutParams0 = window.getAttributes();
        View view0 = window.getDecorView();
        int viewWidth = view0.getWidth();
        try {
            view0.measure(-2, -2);
            viewWidth = Math.max(viewWidth, view0.getMeasuredWidth());
        }
        catch(Throwable e) {
            Log.badImplementation(e);
        }
        if(horizontal) {
            int v1 = this.getWidth();
            try {
                this.measure(-2, -2);
                viewWidth += Math.max(v1, this.getMeasuredWidth());
            }
            catch(Throwable e) {
                Log.badImplementation(e);
            }
        }
        else {
            viewWidth += Tools.dp2px48();
        }
        windowManager$LayoutParams0.width = viewWidth;
        if(windowManager$LayoutParams0.width >= MainService.context.getResources().getDisplayMetrics().widthPixels) {
            windowManager$LayoutParams0.width = -1;
        }
        window.setAttributes(windowManager$LayoutParams0);
    }

    public int getFlags() {
        return this.flags;
    }

    private int getKeyboardId(boolean useInternalKeyboard, boolean hex, boolean fn, boolean small) {
        if(useInternalKeyboard) {
            if(hex) {
                return 0x7F0B0154;  // id:k_hex
            }
            if(fn) {
                return 0x7F0B0155;  // id:k_fn
            }
            return small ? 0x7F0B0157 : 0x7F0B0153;  // id:k_small
        }
        return 0x7F0B0156;  // id:k_ext
    }

    public static View getView(View child) {
        return InternalKeyboard.getView(child, true);
    }

    public static View getView(View child, boolean allowUseInternal) {
        if(InternalKeyboard.allowUsage()) {
            View parent = (KeyboardLayout)LayoutInflater.inflateStatic(0x7F040006, null);  // layout:keyboard
            ViewGroup frame = (ViewGroup)((KeyboardLayout)parent).findViewById((child instanceof ScrollView ? 0x7F0B0029 : 0x7F0B0028));  // id:kbd_frame
            frame.addView(child);
            frame.setVisibility(0);
            InternalKeyboard kbd = (InternalKeyboard)((KeyboardLayout)parent).findViewById(0x7F0B002A);  // id:keyboard
            kbd.setAllowUseInternal(allowUseInternal);
            Object object0 = child.getTag();
            if(object0 instanceof Flags) {
                kbd.setFlags(((Flags)object0).value);
            }
            ((KeyboardLayout)parent).updateOrientation();
            return parent;
        }
        return child;
    }

    public static View getView2(View view0) {
        return InternalKeyboard.getView(view0, false);
    }

    private Window getWindow() {
        return (Window)this.weakWindow.get();
    }

    private void init(Context context) {
        this.hex = false;
        this.fn = false;
        if(this.isInEditMode()) {
            return;
        }
        try {
            this.setPreviewEnabled(false);
            this.setOnKeyboardActionListener(new KeyboardView.OnKeyboardActionListener() {
                @Override  // android.inputmethodservice.KeyboardView$OnKeyboardActionListener
                public void onKey(int primaryCode, int[] keyCodes) {
                    InternalKeyboard.this.processKey(primaryCode);
                }

                @Override  // android.inputmethodservice.KeyboardView$OnKeyboardActionListener
                public void onPress(int primaryCode) {
                }

                @Override  // android.inputmethodservice.KeyboardView$OnKeyboardActionListener
                public void onRelease(int primaryCode) {
                }

                @Override  // android.inputmethodservice.KeyboardView$OnKeyboardActionListener
                public void onText(CharSequence text) {
                }

                @Override  // android.inputmethodservice.KeyboardView$OnKeyboardActionListener
                public void swipeDown() {
                }

                @Override  // android.inputmethodservice.KeyboardView$OnKeyboardActionListener
                public void swipeLeft() {
                }

                @Override  // android.inputmethodservice.KeyboardView$OnKeyboardActionListener
                public void swipeRight() {
                }

                @Override  // android.inputmethodservice.KeyboardView$OnKeyboardActionListener
                public void swipeUp() {
                }
            });
        }
        catch(Throwable e) {
            Log.e("Failed init internal keyboard", e);
        }
    }

    public void load(Window window) {
        this.weakWindow = new WeakReference(window);
        if(!this.allowUseInternal || (Config.configClient & 1) == 0) {
            window.clearFlags(0x20000);
        }
        else {
            window.addFlags(0x20000);
        }
        this.setKeyboard();
        View view0 = window.getDecorView();
        Window.Callback window$Callback0 = window.getCallback();
        if(window$Callback0 instanceof WindowCallbackWrapper && view0.getWidth() == 0) {
            ((WindowCallbackWrapper)window$Callback0).setKeyboard(this);
            return;
        }
        this.onWindowFocusChanged();
    }

    public void needExternal(View v, boolean hasFocus) {
        try {
            if(v != null) {
                Window window0 = this.getWindow();
                if(window0 != null) {
                    Tools.externalKeyboard(window0, v, hasFocus);
                }
                if(hasFocus) {
                    this.setKeyboard(false);
                    return;
                }
                this.setKeyboard();
            }
        }
        catch(Throwable e) {
            Log.e(("Failed show/hide kbd: " + hasFocus), e);
        }
    }

    public static void needExternalKbd(View v, boolean needExternal) {
        InternalKeyboard kbdView = (InternalKeyboard)v.getRootView().findViewById(0x7F0B002A);  // id:keyboard
        if(kbdView != null) {
            kbdView.needExternal(v, needExternal);
            if(!needExternal) {
                Tools.hideKeyboard(v);
            }
        }
    }

    public void onWindowFocusChanged() {
        boolean z = true;
        Window window0 = this.getWindow();
        if(window0 != null) {
            int orientation = MainService.context.getResources().getConfiguration().orientation;
            if(this.fixed != orientation) {
                this.fixed = orientation;
                if(orientation == 1) {
                    z = false;
                }
                this.fixWidth(window0, z);
            }
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

    void processKey(int primaryCode) {
        Window window0 = this.getWindow();
        if(window0 != null) {
            View view0 = window0.getCurrentFocus();
            if(view0 instanceof EditText) {
                EditText edit = (EditText)view0;
                int v1 = Math.max(edit.getSelectionStart(), 0);
                int v2 = Math.max(edit.getSelectionEnd(), 0);
                String str = null;
                int key = 0;
                if(primaryCode < 5000) {
                    str = String.valueOf(((char)primaryCode));
                }
                else {
                    switch(primaryCode) {
                        case 5000: {
                            key = 67;
                            break;
                        }
                        case 5001: {
                            key = 0x70;
                            break;
                        }
                        case 5002: {
                            str = ParserNumbers.thousandSeparator;
                            break;
                        }
                        case 5003: {
                            str = ".";
                            break;
                        }
                        case 5004: 
                        case 5005: {
                            if(v1 != v2 || (v1 != 0 || primaryCode != 5004) && (v1 != edit.getText().length() || primaryCode != 5005)) {
                                key = primaryCode == 5004 ? 21 : 22;
                            }
                            break;
                        }
                        case 5006: {
                            this.setHex();
                            break;
                        }
                        case 5007: {
                            History.show(edit);
                            break;
                        }
                        case 5008: {
                            Tools.selectAll(edit);
                            break;
                        }
                        case 5009: {
                            this.setSmall();
                            break;
                        }
                        case 5010: {
                            this.setFn();
                        }
                    }
                }
                if(str != null) {
                    edit.getText().replace(Math.min(v1, v2), Math.max(v1, v2), str, 0, str.length());
                    edit.setSelection(Math.max(edit.getSelectionStart(), edit.getSelectionEnd()));
                }
                if(key != 0) {
                    try {
                        edit.dispatchKeyEvent(new KeyEvent(0, key));
                        edit.dispatchKeyEvent(new KeyEvent(1, key));
                    }
                    catch(Throwable e) {
                        Log.e("Failed send key to app", e);
                    }
                }
            }
        }
    }

    private void replaceKey(Keyboard.Key key) {
        if((this.flags & 1) != 0 && ":".equals(key.label)) {
            this.replaceKey(key, "?");
        }
        if((this.flags & 2) != 0 && ";".equals(key.label)) {
            this.replaceKey(key, "*");
        }
        if((this.flags & 4) != 0 && "~".equals(key.label)) {
            this.replaceKey(key, "^");
        }
        if((this.flags & 8) != 0 && "r".equals(key.label)) {
            this.replaceKey(key, "$");
        }
    }

    private void replaceKey(Keyboard.Key key, String replace) {
        key.label = replace;
        if(key.codes == null) {
            key.codes = new int[1];
        }
        key.codes[0] = replace.charAt(0);
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

    public void setAllowUseInternal(boolean allowUseInternal) {
        this.allowUseInternal = allowUseInternal;
    }

    public void setFlags(int flags) {
        this.flags = flags;
    }

    public static void setFlagsFor(View v, int flags) {
        Tools.setOnFocusChangeListener(v, new View.OnFocusChangeListener() {
            int oldFlags;

            {
                int v = flags;  // 捕获的参数 （可能与外部方法变量命名冲突；考虑手动重命名）
                this.oldFlags = -1;
            }

            @Override  // android.view.View$OnFocusChangeListener
            public void onFocusChange(View v, boolean hasFocus) {
                if((Config.configClient & 1) != 0) {
                    InternalKeyboard kbdView = (InternalKeyboard)v.getRootView().findViewById(0x7F0B002A);  // id:keyboard
                    if(kbdView != null) {
                        if(this.oldFlags == -1) {
                            this.oldFlags = kbdView.getFlags();
                        }
                        kbdView.setFlags((hasFocus ? flags : 0) | this.oldFlags);
                        kbdView.updateOrientation();
                    }
                }
            }
        });
    }

    private void setFn() {
        this.fn = !this.fn;
        this.setKeyboard();
    }

    private void setHex() {
        this.hex = !this.hex;
        this.setKeyboard();
    }

    public void setHideKeyboard(boolean hide) {
        boolean old = this.hide;
        this.hide = hide;
        if(old != hide) {
            this.setKeyboard();
        }
    }

    private void setKeyboard() {
        this.setKeyboard(this.allowUseInternal && (Config.configClient & 1) != 0);
    }

    private void setKeyboard(boolean useInternalKeyboard) {
        boolean h = this.hex;
        boolean f = this.fn;
        boolean s = (Config.configClient & 0x40) != 0;
        int resId = this.hide ? 0x7F050000 : 0x7F050001;  // xml:empty
        Keyboard keyboard0 = new Keyboard(MainService.context, resId, this.getKeyboardId(useInternalKeyboard, h, f, s));
        if(!this.hide) {
            if(h && keyboard0.getKeys().size() == 0) {
                h = false;
                keyboard0 = new Keyboard(MainService.context, resId, this.getKeyboardId(useInternalKeyboard, false, f, s));
            }
            if(f && keyboard0.getKeys().size() == 0) {
                f = false;
                keyboard0 = new Keyboard(MainService.context, resId, this.getKeyboardId(useInternalKeyboard, h, false, s));
            }
            if(s && keyboard0.getKeys().size() == 0) {
                keyboard0 = new Keyboard(MainService.context, resId, this.getKeyboardId(useInternalKeyboard, h, f, false));
            }
        }
        SparseIntArray coords = new SparseIntArray();
        for(Object object0: keyboard0.getKeys()) {
            Keyboard.Key key = (Keyboard.Key)object0;
            if(key != null) {
                coords.put(key.y, coords.get(key.y, 0) + 1);
                if(!f) {
                    this.replaceKey(key);
                }
                if(key.codes != null && key.codes.length != 0) {
                    if(key.codes[0] == 5002) {
                        key.label = ParserNumbers.thousandSeparator;
                    }
                    if(key.codes[0] == 5003) {
                        key.label = ".";
                    }
                }
            }
        }
        Resources resources0 = this.getContext().getResources();
        ViewGroup.LayoutParams viewGroup$LayoutParams0 = this.getLayoutParams();
        if(viewGroup$LayoutParams0 != null && coords.size() > 0) {
            viewGroup$LayoutParams0.width = Math.round(resources0.getDimension(0x7F080001) * ((float)coords.valueAt(0)));  // dimen:key_width
            viewGroup$LayoutParams0.height = Math.round(resources0.getDimension(0x7F080002) * ((float)coords.size()));  // dimen:key_height
            this.setLayoutParams(viewGroup$LayoutParams0);
            Log.d(("Kbd: " + coords.valueAt(0) + " x " + coords.size() + "; " + viewGroup$LayoutParams0.width + 'x' + viewGroup$LayoutParams0.height + "; " + keyboard0.getMinWidth() + 'x' + keyboard0.getHeight()));
        }
        this.setKeyboard(keyboard0);
    }

    private void setSmall() {
        int v = 1;
        boolean small = (Config.configClient & 0x40) == 0;
        Option config$Option0 = Config.get(0x7F0B00BF);  // id:config_kbd_small
        if(!small) {
            v = 0;
        }
        config$Option0.value = v;
        Config.save();
        this.setKeyboard();
    }

    public void updateOrientation() {
        this.setKeyboard();
        this.onWindowFocusChanged();
    }
}

