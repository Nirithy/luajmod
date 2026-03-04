package luaj;

import android.content.Context;
import android.ext.Script;
import android.graphics.Canvas;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import luaj.lib.ResourceFinder;
import luaj.lib.jse.CoerceJavaToLua;

public class LuaView extends View implements ResourceFinder {
    public final Globals globals;

    public LuaView(Context context0) {
        super(context0);
        this.globals = Script.instanceGlobals;
        this.globals.finder = this;
    }

    @Override  // android.view.View
    public void draw(Canvas canvas0) {
        LuaValue luaValue0 = this.globals.get("onDraw");
        if(!luaValue0.isnil()) {
            try {
                luaValue0.call(CoerceJavaToLua.coerce(canvas0));
            }
            catch(Exception exception0) {
                exception0.printStackTrace();
            }
            return;
        }
        super.draw(canvas0);
    }

    public boolean f(int v, KeyEvent keyEvent0) {
        LuaValue luaValue0 = this.globals.get("onKeyDown");
        if(!luaValue0.isnil()) {
            try {
                return luaValue0.call(CoerceJavaToLua.coerce(new Integer(v)), CoerceJavaToLua.coerce(keyEvent0)).toboolean();
            }
            catch(Exception exception0) {
                exception0.printStackTrace();
                return true;
            }
        }
        return super.onKeyDown(v, keyEvent0);
    }

    public InputStream findResource(int v) {
        try {
            return this.getContext().getResources().openRawResource(v);
        }
        catch(Exception unused_ex) {
            return null;
        }
    }

    @Override  // luaj.lib.ResourceFinder
    public InputStream findResource(String s) {
        try {
            return this.getContext().getAssets().open(s);
        }
        catch(IOException unused_ex) {
            try {
                return (InputStream)this.getContext().getAssets().openXmlResourceParser(s);
            }
            catch(Exception unused_ex) {
                try {
                    return new FileInputStream(new File(s));
                }
                catch(Exception unused_ex) {
                    return null;
                }
            }
        }
    }

    @Override  // android.view.View
    public boolean onKeyUp(int v, KeyEvent keyEvent0) {
        LuaValue luaValue0 = this.globals.get("onKeyUp");
        if(!luaValue0.isnil()) {
            try {
                return luaValue0.call(CoerceJavaToLua.coerce(new Integer(v)), CoerceJavaToLua.coerce(keyEvent0)).toboolean();
            }
            catch(Exception exception0) {
                exception0.printStackTrace();
                return true;
            }
        }
        return super.onKeyUp(v, keyEvent0);
    }

    @Override  // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent0) {
        LuaValue luaValue0 = this.globals.get("onTouchEvent");
        if(!luaValue0.isnil()) {
            try {
                return luaValue0.call(CoerceJavaToLua.coerce(motionEvent0)).toboolean();
            }
            catch(Exception exception0) {
                exception0.printStackTrace();
                return true;
            }
        }
        return super.onTouchEvent(motionEvent0);
    }

    @Override  // android.view.View
    public boolean onTrackballEvent(MotionEvent motionEvent0) {
        LuaValue luaValue0 = this.globals.get("onTrackballEvent");
        if(!luaValue0.isnil()) {
            try {
                return luaValue0.call(CoerceJavaToLua.coerce(motionEvent0)).toboolean();
            }
            catch(Exception exception0) {
                exception0.printStackTrace();
                return true;
            }
        }
        return super.onTrackballEvent(motionEvent0);
    }

    @Override  // android.view.View
    public void onWindowFocusChanged(boolean z) {
        LuaValue luaValue0 = this.globals.get("onWindowFocusChanged");
        if(!luaValue0.isnil()) {
            try {
                luaValue0.call(CoerceJavaToLua.coerce(new Boolean(z)));
            }
            catch(Exception exception0) {
                exception0.printStackTrace();
            }
        }
    }

    @Override  // android.view.View
    public void onWindowSystemUiVisibilityChanged(int v) {
        LuaValue luaValue0 = this.globals.get("onWindowSystemUiVisibilityChanged");
        if(!luaValue0.isnil()) {
            try {
                luaValue0.call(CoerceJavaToLua.coerce(new Integer(v)));
            }
            catch(Exception exception0) {
                exception0.printStackTrace();
            }
        }
    }
}

