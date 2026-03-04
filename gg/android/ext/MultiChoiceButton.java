package android.ext;

import android.content.Context;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.DialogInterface;
import android.fix.SparseArray;
import android.os.Build.VERSION;
import android.util.AttributeSet;

public class MultiChoiceButton extends SystemSpinner implements DialogInterface.OnMultiChoiceClickListener {
    protected int current;

    public MultiChoiceButton(Context context) {
        super(context);
    }

    public MultiChoiceButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MultiChoiceButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override  // android.ext.SystemSpinner
    protected void onClick() {
        SparseArray data = this.data;
        int v = data.size();
        CharSequence[] arr = new CharSequence[v];
        boolean[] sel = new boolean[arr.length];
        this.current = 0;
        for(int i = 0; i < v; ++i) {
            int v2 = data.keyAt(v - 1 - i);
            arr[i] = (CharSequence)data.valueAt(v - 1 - i);
            boolean isChecked = (this.selected & v2) != 0;
            sel[i] = isChecked;
            this.onClick(null, i, isChecked);
        }
        Alert.show(Alert.create(Tools.getDarkContext(this.getContext())).setMultiChoiceItems(arr, sel, this).setNegativeButton(Re.s(0x7F0700A1), null).setNeutralButton(Re.s(0x7F07023F), this).setPositiveButton(Re.s(0x7F07009D), this));  // string:cancel "Cancel"
    }

    @Override  // android.ext.SystemSpinner
    public void onClick(DialogInterface dialog, int which) {
        int v1 = this.data.size();
        int sel = 0;
        if(which == -1) {
            for(int i = 0; i < v1; ++i) {
                if((this.current & 1 << i) != 0) {
                    sel |= this.data.keyAt(v1 - 1 - i);
                }
            }
        }
        this.setSelected(sel);
        Tools.dismiss(dialog);
    }

    @Override  // android.content.DialogInterface$OnMultiChoiceClickListener
    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
        if(isChecked) {
            this.current |= 1 << which;
            return;
        }
        this.current &= ~(1 << which);
    }

    @Override  // android.ext.SystemSpinner
    public void setSelected(int code) {
        SparseArray data = this.data;
        if(data != null) {
            int v1 = data.size();
            CharSequenceBuilder out = new CharSequenceBuilder();
            for(int i = 0; i < v1; ++i) {
                if((code & data.keyAt(v1 - 1 - i)) != 0) {
                    if(out.size() != 0) {
                        out.append(",");
                    }
                    CharSequence full = (CharSequence)data.valueAt(v1 - 1 - i);
                    out.append(full.subSequence(0, full.toString().indexOf(58)));
                }
            }
            if(Build.VERSION.SDK_INT < 11) {
                Tools.setButtonBackground(this, Tools.getDrawable(0x7F020002));  // drawable:btn_default_normal_holo_dark
            }
            this.setText(out.toCharSequence());
            this.selected = code;
            if(this.mListener != null) {
                this.mListener.onClick(null, code);
            }
        }
    }
}

