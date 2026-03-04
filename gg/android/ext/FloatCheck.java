package android.ext;

import android.content.SharedPreferences;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public final class FloatCheck {
    private static final String FLOAT_OFF = "float-off";
    private static final boolean FLOAT_OFF_DEFAULT;

    public FloatCheck(boolean async) {
        if(async) {
            new DaemonThread(() -> {
                SharedPreferences sharedPreferences0 = Tools.getSharedPreferences();
                if(sharedPreferences0.contains("float-off")) {
                    if(sharedPreferences0.getBoolean("float-off", false)) {
                        FloatCheck.this.floatOff("pref");
                    }
                    return;
                }
                FloatCheck.this.remix();
            }, "FloatCheck").start();
            return;
        }
        this.check();

        class android.ext.FloatCheck.1 implements Runnable {
            @Override
            public void run() {
                FloatCheck.this.check();
            }
        }

    }

    // 检测为 Lambda 实现
    void check() [...]

    private void floatOff(String reason) {
        Log.d(("FloatCheck: float off by " + reason));
        SystemConstants.useFloatWindows = false;
    }

    private void remix() {
        try {
            BufferedReader bufferedReader0 = new BufferedReader(new InputStreamReader(Tools.exec(new String[]{"getprop"}).getInputStream()));
            String s;
            while((s = bufferedReader0.readLine()) != null) {
                if(s.contains("ro.build.remixos.version")) {
                    Log.d(("Remix-detect: " + s));
                    if(!s.contains("[2.")) {
                        break;
                    }
                    this.floatOff(s);
                    break;
                }
            }
            bufferedReader0.close();
        }
        catch(Throwable e) {
            Log.e("Remix-detect", e);
        }
    }

    public static void setPref(boolean value) {
        new SPEditor().putBoolean("float-off", value, false).commit();
    }
}

