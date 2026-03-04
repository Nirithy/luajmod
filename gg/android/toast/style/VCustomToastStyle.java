package android.toast.style;

import android.content.Context;
import android.view.View;

public class VCustomToastStyle extends CustomToastStyle {
    public View view;

    public VCustomToastStyle(int v) {
        super(v);
    }

    public VCustomToastStyle(int v, int v1) {
        super(v, v1);
    }

    public VCustomToastStyle(int v, int v1, int v2, int v3) {
        super(v, v1, v2, v3);
    }

    public VCustomToastStyle(int v, int v1, int v2, int v3, float f, float f1) {
        super(v, v1, v2, v3, f, f1);
    }

    public VCustomToastStyle(View view0) {
        super(0);
        this.view = view0;
    }

    public VCustomToastStyle(View view0, int v) {
        super(0, v);
        this.view = view0;
    }

    public VCustomToastStyle(View view0, int v, int v1, int v2) {
        super(0, v, v1, v2);
        this.view = view0;
    }

    public VCustomToastStyle(View view0, int v, int v1, int v2, float f, float f1) {
        super(0, v, v1, v2, f, f1);
        this.view = view0;
    }

    public View createView() {
        return this.view;
    }

    @Override  // android.toast.style.CustomToastStyle
    public View createView(Context context0) {
        return this.view;
    }
}

