package android.toast.config;

import android.content.Context;
import android.view.View;

public interface IToastStyle {
    View createView(Context arg1);

    default int getGravity() {
        return 80;
    }

    default float getHorizontalMargin() {
        return 0.0f;
    }

    default float getVerticalMargin() {
        return 0.0f;
    }

    default int getXOffset() {
        return 0;
    }

    default int getYOffset() {
        return 100;
    }
}

