package android.ext;

import android.content.Context;
import android.util.AttributeSet;

public class EditTextMulti extends EditText {
    public EditTextMulti(Context context) {
        super(context);
        this.setInputType(this.getInputType() | 0x40000);
        this.setHorizontallyScrolling(false);
        this.setMaxLines(0x7FFFFFFF);
    }

    public EditTextMulti(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setInputType(this.getInputType() | 0x40000);
        this.setHorizontallyScrolling(false);
        this.setMaxLines(0x7FFFFFFF);
    }

    public EditTextMulti(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setInputType(this.getInputType() | 0x40000);
        this.setHorizontallyScrolling(false);
        this.setMaxLines(0x7FFFFFFF);
    }

    public EditTextMulti(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.setInputType(this.getInputType() | 0x40000);
        this.setHorizontallyScrolling(false);
        this.setMaxLines(0x7FFFFFFF);
    }

    @Override  // android.widget.TextView
    public void setHorizontallyScrolling(boolean whether) {
        super.setHorizontallyScrolling(false);
    }

    @Override  // android.widget.TextView
    public void setMaxLines(int maxlines) {
        super.setMaxLines(0x7FFFFFFF);
    }
}

