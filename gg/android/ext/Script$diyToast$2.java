package android.ext;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

class Script.diyToast.2 extends Toast implements Runnable {
    public Script.diyToast.2(Context context0) {
        super(context0);
    }

    @SuppressLint("SetTextI18n")
    public static void show(Context context0, String s, int v, int v1, int v2, int v3) {
        View view0 = ((LayoutInflater)context0.getSystemService("layout_inflater")).inflate(0x7F040031, null);  // layout:diy_toast
        TextView textView0 = (TextView)view0.findViewById(0x7F0B015D);  // id:diy_toast_text
        ((GradientDrawable)textView0.getBackground()).setColor(v);
        textView0.setTextColor(v1);
        try {
            textView0.setText(String.valueOf(s));
        }
        catch(Exception unused_ex) {
            textView0.setText("该对象无法被转换为String");
        }
        textView0.setTextSize(((float)v2));
        Toast toast0 = new Toast(context0);
        toast0.setView(view0);
        toast0.setDuration(0);
        if(v3 == 1) {
            toast0.setGravity(17, 0, 0);
        }
        else {
            toast0.setGravity(80, 0, 84);
        }
        try {
            toast0.show();
        }
        catch(Throwable throwable0) {
            Log.w("Toast fail", throwable0);
        }
    }
}

