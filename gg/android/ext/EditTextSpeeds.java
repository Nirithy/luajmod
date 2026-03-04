package android.ext;

import android.content.Context;
import android.util.AttributeSet;

public class EditTextSpeeds extends EditTextMulti {
    public interface OnChangedListener {
        void onSelectionChanged(EditText arg1, int arg2, int arg3);

        void onTextChanged(EditText arg1, CharSequence arg2, int arg3, int arg4, int arg5);
    }

    private OnChangedListener changedListener;

    public EditTextSpeeds(Context context) {
        super(context);
        this.changedListener = null;
    }

    public EditTextSpeeds(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.changedListener = null;
    }

    public EditTextSpeeds(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.changedListener = null;
    }

    public EditTextSpeeds(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.changedListener = null;
    }

    @Override  // android.widget.TextView
    protected void onSelectionChanged(int selStart, int selEnd) {
        super.onSelectionChanged(selStart, selEnd);
        OnChangedListener changedListener = this.changedListener;
        if(changedListener != null) {
            changedListener.onSelectionChanged(this, selStart, selEnd);
        }
    }

    @Override  // android.widget.TextView
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        OnChangedListener changedListener = this.changedListener;
        if(changedListener != null) {
            changedListener.onTextChanged(this, text, start, lengthBefore, lengthAfter);
        }
    }

    public void setOnChangedListener(OnChangedListener listener) {
        this.changedListener = listener;
    }
}

