package android.ext;

import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface;
import android.fix.Button;
import android.fix.SparseArray;
import android.util.AttributeSet;
import android.view.View.OnClickListener;
import android.view.View;

public class SystemSpinner extends Button implements DialogInterface.OnClickListener, View.OnClickListener {
    protected SparseArray data;
    protected DialogInterface.OnClickListener mListener;
    protected int selected;

    public SystemSpinner(Context context) {
        super(context);
        this.data = null;
    }

    public SystemSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.data = null;
    }

    public SystemSpinner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.data = null;
    }

    public int getSelected() {
        return this.selected;
    }

    protected void onClick() {
        SparseArray data = this.data;
        int v = data.size();
        CharSequence[] arr = new CharSequence[v];
        for(int i = 0; i < v; ++i) {
            arr[i] = (CharSequence)data.valueAt(v - 1 - i);
        }
        Alert.show(Alert.create(this.getContext()).setSingleChoiceItems(arr, v - 1 - data.indexOfKey(this.selected), this));
    }

    @Override  // android.content.DialogInterface$OnClickListener
    public void onClick(DialogInterface dialog, int which) {
        this.setSelected(this.data.keyAt(this.data.size() - 1 - which));
        Tools.dismiss(dialog);
    }

    @Override  // android.view.View$OnClickListener
    public void onClick(View v) {
        if(this.data != null && this.data.size() != 0) {
            this.onClick();
        }
    }

    public void setData(SparseArray sparseArray0) {
        if(sparseArray0 != null) {
            this.data = sparseArray0;
            int v = sparseArray0.size();
            if(v != 0) {
                this.selected = sparseArray0.keyAt(v - 1);
                this.setOnClickListener(this);
            }
        }
    }

    public void setOnItemSelectedListener(DialogInterface.OnClickListener listener) {
        this.mListener = listener;
    }

    public void setSelected(int code) {
        SparseArray data = this.data;
        if(data != null) {
            CharSequence name = (CharSequence)data.get(code);
            if(name == null) {
                if(data.size() == 1) {
                    name = (CharSequence)data.valueAt(0);
                    code = data.keyAt(0);
                }
                else {
                    name = "???";
                }
            }
            this.setText(name);
            this.selected = code;
            if(this.mListener != null) {
                this.mListener.onClick(null, code);
            }
        }
    }
}

