package android.ext;

import android.app.AlertDialog.Builder;
import android.app.AlertDialog;
import android.view.View;
import luaj.Varargs;

class Script.searchChoice.3 implements Runnable {
    final View val$inflate;
    final Varargs val$var;

    Script.searchChoice.3(Script.searchChoice.1 script$searchChoice$10, View view0, Varargs varargs0) {
        Script.searchChoice.1.this = script$searchChoice$10;
        this.val$inflate = view0;
        this.val$var = varargs0;
        super();
    }

    @Override
    public void run() {
        AlertDialog.Builder alertDialog$Builder0 = Alert.create().setView(this.val$inflate).setTitle(this.val$var.checkjstring(2));
        Script.searchChoice.1 script$searchChoice$10 = Script.searchChoice.1.this.the;
        AlertDialog alertDialog0 = alertDialog$Builder0.setPositiveButton(Re.s(0x7F07009D), script$searchChoice$10).create();  // string:ok "OK"
        Alert.setOnDismissListener(alertDialog0, Script.searchChoice.1.this.the);
        Alert.show(alertDialog0);
        MainService.instance.eye(true);
    }
}

