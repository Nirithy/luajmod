package android.ext;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SystemSpinnerType extends SystemSpinner {
    Pair[] pairs;

    public SystemSpinnerType(Context context) {
        super(context);
    }

    public SystemSpinnerType(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SystemSpinnerType(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override  // android.ext.SystemSpinner
    protected void onClick() {
        this.pairs = AddressItem.getOrderedPairs(this.data, null);
        int sel = 0;
        for(int i = 0; i < this.pairs.length; ++i) {
            if(this.pairs[i].flags == this.selected) {
                sel = i;
                break;
            }
        }
        Alert.show(Alert.create(Tools.getDarkContext(this.getContext())).setSingleChoiceItems(new ArrayAdapter(MainService.context, 0x1090012, 0x1020014, this.pairs) {
            @Override  // android.ext.ArrayAdapter
            public View getView(int position, View convertView, ViewGroup parent) {
                View view1 = super.getView(position, convertView, parent);
                TextView tv = (TextView)view1.findViewById(0x1020014);
                if(tv != null) {
                    Tools.setTextAppearance(tv, 0x7F090002);  // style:SmallText
                    if(position > 0 && position < SystemSpinnerType.this.pairs.length && SystemSpinnerType.this.pairs[position] != null) {
                        tv.setTextColor(AddressItem.getColor(SystemSpinnerType.this.pairs[position].flags));
                    }
                }
                return view1;
            }
        }, sel, this));
    }

    @Override  // android.ext.SystemSpinner
    public void onClick(DialogInterface dialog, int which) {
        this.setSelected(this.pairs[which].flags);
        Tools.dismiss(dialog);
    }

    @Override  // android.ext.SystemSpinner
    public void setSelected(int code) {
        super.setSelected(code);
        if(Build.VERSION.SDK_INT < 11) {
            Tools.setButtonBackground(this, Tools.getDrawable(0x7F020002));  // drawable:btn_default_normal_holo_dark
        }
        this.setTextColor(AddressItem.getColor(code));
    }
}

