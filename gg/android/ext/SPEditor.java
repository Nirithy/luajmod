package android.ext;

import android.content.SharedPreferences.Editor;
import java.util.Set;

public class SPEditor implements SharedPreferences.Editor {
    final SharedPreferences.Editor base;

    public SPEditor() {
        this.base = Tools.getSharedPreferences().edit();
    }

    public SPEditor(SharedPreferences.Editor base) {
        this.base = base;
    }

    @Override  // android.content.SharedPreferences$Editor
    public void apply() {
        this.base.apply();
    }

    @Override  // android.content.SharedPreferences$Editor
    public SharedPreferences.Editor clear() {
        return this.clear();
    }

    public SPEditor clear() {
        this.base.clear();
        return this;
    }

    @Override  // android.content.SharedPreferences$Editor
    public boolean commit() {
        return this.base.commit();
    }

    @Override  // android.content.SharedPreferences$Editor
    public SharedPreferences.Editor putBoolean(String s, boolean z) {
        return this.putBoolean(s, z);
    }

    public SPEditor putBoolean(String key, boolean value) {
        this.base.putBoolean(key, value);
        return this;
    }

    public SPEditor putBoolean(String key, boolean value, boolean def) {
        if(value == def) {
            this.base.remove(key);
            return this;
        }
        this.base.putBoolean(key, value);
        return this;
    }

    @Override  // android.content.SharedPreferences$Editor
    public SharedPreferences.Editor putFloat(String s, float f) {
        return this.putFloat(s, f);
    }

    public SPEditor putFloat(String key, float value) {
        this.base.putFloat(key, value);
        return this;
    }

    public SPEditor putFloat(String key, float value, float def) {
        if(value == def) {
            this.base.remove(key);
            return this;
        }
        this.base.putFloat(key, value);
        return this;
    }

    @Override  // android.content.SharedPreferences$Editor
    public SharedPreferences.Editor putInt(String s, int v) {
        return this.putInt(s, v);
    }

    public SPEditor putInt(String key, int value) {
        this.base.putInt(key, value);
        return this;
    }

    public SPEditor putInt(String key, int value, int def) {
        if(value == def) {
            this.base.remove(key);
            return this;
        }
        this.base.putInt(key, value);
        return this;
    }

    @Override  // android.content.SharedPreferences$Editor
    public SharedPreferences.Editor putLong(String s, long v) {
        return this.putLong(s, v);
    }

    public SPEditor putLong(String key, long value) {
        this.base.putLong(key, value);
        return this;
    }

    public SPEditor putLong(String key, long value, long def) {
        if(value == def) {
            this.base.remove(key);
            return this;
        }
        this.base.putLong(key, value);
        return this;
    }

    @Override  // android.content.SharedPreferences$Editor
    public SharedPreferences.Editor putString(String s, String s1) {
        return this.putString(s, s1);
    }

    public SPEditor putString(String key, String value) {
        this.base.putString(key, value);
        return this;
    }

    public SPEditor putString(String key, String value, String def) {
        if(value.equals(def)) {
            this.base.remove(key);
            return this;
        }
        this.base.putString(key, value);
        return this;
    }

    @Override  // android.content.SharedPreferences$Editor
    public SharedPreferences.Editor putStringSet(String s, Set set0) {
        return this.putStringSet(s, set0);
    }

    public SPEditor putStringSet(String key, Set set0) {
        this.base.putStringSet(key, set0);
        return this;
    }

    @Override  // android.content.SharedPreferences$Editor
    public SharedPreferences.Editor remove(String s) {
        return this.remove(s);
    }

    public SPEditor remove(String key) {
        this.base.remove(key);
        return this;
    }
}

