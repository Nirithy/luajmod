package android.ext;

import android.media.MediaPlayer;
import luaj.LuaValue;
import luaj.Varargs;

final class Script.playMusic extends ApiFunction {
    public static volatile boolean isPlaying;
    public static volatile MediaPlayer mediaPlayer;

    @Override  // android.ext.Script$ApiFunction
    protected int getMaxArgs() {
        return 1;
    }

    @Override  // android.ext.Script$ApiFunction
    public Varargs invoke2(Varargs varargs0) {
        if(varargs0.optjstring(1, "pause").equals("pause")) {
            if(Script.playMusic.mediaPlayer != null) {
                if(Script.playMusic.isPlaying) {
                    Script.playMusic.mediaPlayer.start();
                    Script.playMusic.isPlaying = false;
                    return LuaValue.TRUE;
                }
                Script.playMusic.mediaPlayer.pause();
                Script.playMusic.isPlaying = true;
                return LuaValue.TRUE;
            }
            return LuaValue.FALSE;
        }
        String s = varargs0.optjstring(1, "stop");
        if(s.equals("stop")) {
            if(Script.playMusic.mediaPlayer != null) {
                Script.playMusic.mediaPlayer.reset();
                return LuaValue.TRUE;
            }
            return LuaValue.FALSE;
        }
        if(Script.playMusic.mediaPlayer != null) {
            Script.playMusic.mediaPlayer.stop();
        }
        try {
            Script.playMusic.mediaPlayer = new MediaPlayer();
            Script.playMusic.mediaPlayer.setDataSource(s);
            Script.playMusic.mediaPlayer.prepare();
            Thread.sleep(100L);
            Script.playMusic.mediaPlayer.start();
            Script.playMusic.isPlaying = false;
            return LuaValue.TRUE;
        }
        catch(Exception unused_ex) {
            return LuaValue.FALSE;
        }
    }

    @Override  // android.ext.Script$ApiFunction
    String usage() {
        return "gg.playMusic(string url) -> nil";
    }
}

