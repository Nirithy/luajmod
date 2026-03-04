package android.ext;

import android.content.Context;
import android.content.res.Resources;
import com.ggdqo.R.raw;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import luaj.LuaValue;
import luaj.Varargs;

public final class Script.getRaw extends ApiFunction {
    public static volatile Context context;

    Script.getRaw(Script script0) {
        Script.this = script0;
    }

    private static void extractLib(String s, String s1) {
        File file0 = new File(s);
        if(!file0.isFile()) {
            Log.d(("Extract file " + file0));
            Resources resources0 = Tools.getContext().getResources();
            int v = Re.i(s1, raw.class);
            if(v == 0) {
                Log.d(("Nothing extract file " + s1 + ' ' + file0.getAbsolutePath()));
                return;
            }
            try {
                byte[] arr_b = new byte[0x2000];
                InputStream inputStream0 = resources0.openRawResource(v);
                FileOutputStream fileOutputStream0 = new FileOutputStream(file0);
                while(true) {
                    int v1 = inputStream0.read(arr_b);
                    if(v1 <= 0) {
                        fileOutputStream0.close();
                        inputStream0.close();
                        return;
                    }
                    fileOutputStream0.write(arr_b, 0, v1);
                }
            }
            catch(Throwable throwable0) {
                Log.d(("Failed extract file " + s1 + ' ' + file0.getAbsolutePath()), throwable0);
            }
        }
    }

    @Override  // android.ext.Script$ApiFunction
    protected int getMaxArgs() {
        return 1;
    }

    @Override  // android.ext.Script$ApiFunction
    public Varargs invoke2(Varargs varargs0) {
        Script.getRaw.extractLib(varargs0.checkjstring(1), varargs0.checkjstring(2));
        return LuaValue.valueOf(true);
    }

    @Override  // android.ext.Script$ApiFunction
    String usage() {
        return "gg.getRaw(Copied_path Resource_name) -> nil";
    }
}

