package android.ext;

import android.os.Handler;
import android.os.Message;
import luaj.LuaFunction;

public class Post {
    public static boolean flags;
    public static Thread trd;

    static {
        Post.flags = false;
    }

    public void postFunc(LuaFunction luaFunction0) {
        try {
            Post.flags = false;
            Post.trd = new Thread(new Runnable() {
                private final LuaFunction val$luaFunction;

                {
                    LuaFunction luaFunction0 = luaFunction0;  // 捕获的参数 （可能与外部方法变量命名冲突；考虑手动重命名）
                    Post.this = post0;
                    this.val$luaFunction = luaFunction0;
                }

                static Post access$0(android.ext.Post.1 post$10) {
                    return Post.this;
                }

                @Override
                public void run() {
                    BaseActivity.err_msg = null;
                    Message message0 = new Message();
                    message0.what = 0x400;
                    message0.obj = this.val$luaFunction;
                    Handler handler0 = BaseActivity.instance.getHandler();
                    handler0.sendMessage(message0);
                    while(!Post.flags) {
                        if(BaseActivity.err_msg != null) {
                            handler0.removeMessages(0x400);
                            break;
                        }
                        if(false) {
                            break;
                        }
                    }
                    Tools.showToast("子线程结束");
                }
            });
            Post.trd.start();
            try {
                Tools.showToast("进入线程等待");
                Post.trd.join();
            }
            catch(InterruptedException unused_ex) {
            }
        }
        catch(Exception unused_ex) {
        }
    }

    public void unlock() {
        if(Post.trd != null) {
            try {
                Post.trd.interrupt();
            }
            catch(Exception unused_ex) {
            }
            try {
                MainService.instance.interruptScript();
            }
            catch(Exception unused_ex) {
            }
            Post.trd = null;
        }
        Post.flags = true;
    }
}

