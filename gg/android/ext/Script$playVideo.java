package android.ext;

import android.os.Handler;
import android.os.Looper;
import luaj.Varargs;

final class Script.playVideo extends ApiFunction {
    @Override  // android.ext.Script$ApiFunction
    protected int getMaxArgs() {
        return 1;
    }

    @Override  // android.ext.Script$ApiFunction
    public Varargs invoke2(Varargs varargs) {
        android.ext.Script.playVideo.1 runnable = new android.ext.Script.playVideo.1(this, varargs);
        new Handler(Looper.getMainLooper()).post(runnable);
        return Script.playVideo.NONE;
    }

    @Override  // android.ext.Script$ApiFunction
    String usage() {
        return "gg.playVideo(string url) -> nil";
    }
}

