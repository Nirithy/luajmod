package android.toast.config;

import android.view.View;
import android.widget.TextView;

public interface IToast {
    void cancel();

    default TextView findMessageView(View view0) {
        if(view0 instanceof TextView) {
            switch(view0.getId()) {
                case -1: {
                    view0.setId(0x102000B);
                    return (TextView)view0;
                }
                case 0x102000B: {
                    return (TextView)view0;
                }
                default: {
                    throw new IllegalArgumentException("You must set the ID value of TextView to android.R.id.message");
                }
            }
        }
        View view1 = view0.findViewById(0x102000B);
        if(!(view1 instanceof TextView)) {
            throw new IllegalArgumentException("You must include a TextView with an ID value of message (xml code: android:id=\"@android:id/message\", java code: view.setId(android.R.id.message))");
        }
        return (TextView)view1;
    }

    int getDuration();

    int getGravity();

    float getHorizontalMargin();

    float getVerticalMargin();

    View getView();

    int getXOffset();

    int getYOffset();

    void setDuration(int arg1);

    void setGravity(int arg1, int arg2, int arg3);

    void setMargin(float arg1, float arg2);

    void setText(int arg1);

    void setText(CharSequence arg1);

    void setView(View arg1);

    void show();
}

