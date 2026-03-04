package android.ext;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcel;
import android.text.SpannableString;
import android.text.style.URLSpan;
import android.util.AndroidRuntimeException;
import android.view.View;
import android.view.WindowManager.BadTokenException;
import android.widget.TextView;

public class DefensiveURLSpan extends URLSpan {
    public DefensiveURLSpan(Parcel src) {
        super(src);
    }

    public DefensiveURLSpan(String url) {
        super(url);
    }

    public static void fixTextView(TextView tv) {
        DefensiveURLSpan.fixTextView(tv.getText());
    }

    public static void fixTextView(CharSequence text) {
        if(text instanceof SpannableString) {
            URLSpan[] spans = (URLSpan[])((SpannableString)text).getSpans(0, ((SpannableString)text).length(), URLSpan.class);
            for(int v = 0; v < spans.length; ++v) {
                URLSpan span = spans[v];
                int v1 = ((SpannableString)text).getSpanStart(span);
                int v2 = ((SpannableString)text).getSpanEnd(span);
                ((SpannableString)text).removeSpan(span);
                ((SpannableString)text).setSpan(new DefensiveURLSpan(span.getURL()), v1, v2, 0);
            }
        }
    }

    @Override  // android.text.style.URLSpan
    public void onClick(View widget) {
        Intent intent0;
        Context context0;
        AndroidRuntimeException e = null;
        try {
            super.onClick(widget);
            goto label_11;
        }
        catch(AndroidRuntimeException ex) {
        }
        catch(WindowManager.BadTokenException ex) {
            e = ex;
            goto label_11;
        }
        catch(SecurityException ex) {
            goto label_10;
        }
        catch(ActivityNotFoundException e) {
            Log.w("failed call activity", e);
            return;
        }
        try {
            e = ex;
            goto label_11;
        label_10:
            e = ex;
        label_11:
            if(e != null) {
                Log.w(("failed onClick on URLSpan: " + this.getURL()), e);
                Uri uri0 = Uri.parse(this.getURL());
                context0 = widget.getContext();
                intent0 = new Intent("android.intent.action.VIEW", uri0);
                intent0.putExtra("com.android.browser.application_id", Tools.getPackageName());
                intent0.setFlags(0x10000000);
                goto label_18;
            }
            goto label_21;
        }
        catch(ActivityNotFoundException e) {
            Log.w("failed call activity", e);
            return;
        }
        catch(SecurityException ex) {
            Log.w(("failed onClick on URLSpan: " + this.getURL()), ex);
            return;
        }
        try {
        label_18:
            context0.startActivity(intent0);
            goto label_21;
        }
        catch(Throwable unused_ex) {
            try {
                Log.w(("failed2 onClick on URLSpan: " + this.getURL()), e);
            label_21:
                Alert.dismissAll();
                return;
            }
            catch(ActivityNotFoundException e) {
            }
            catch(SecurityException ex) {
                Log.w(("failed onClick on URLSpan: " + this.getURL()), ex);
                return;
            }
        }
        Log.w("failed call activity", e);
    }
}

