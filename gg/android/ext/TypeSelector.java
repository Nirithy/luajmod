package android.ext;

import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface;
import android.fix.SparseArray;
import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TypeSelector implements DialogInterface.OnClickListener {
    private final DialogInterface.OnClickListener listener;
    final Pair[] pairs;
    private final String value;

    public TypeSelector(SparseArray sparseArray0, SparseIntArray counts, String value, String message, DialogInterface.OnClickListener listener) {
        this.pairs = AddressItem.getOrderedPairs(sparseArray0, counts);
        this.value = value;
        this.listener = listener;
        if(sparseArray0.size() == 1) {
            this.onClick(null, 0);
            return;
        }
        Alert.show(Alert.create(Tools.getDarkContext()).setCustomTitle(Tools.getCustomTitle(message)).setAdapter(new ArrayAdapter(MainService.context, this.pairs) {
            @Override  // android.ext.ArrayAdapter
            public View getView(int position, View convertView, ViewGroup parent) {
                View view1 = super.getView(position, convertView, parent);
                TextView tv = (TextView)view1.findViewById(0x1020014);
                if(tv != null) {
                    Tools.setTextAppearance(tv, 0x7F090002);  // style:SmallText
                    if(position >= 0 && position < TypeSelector.this.pairs.length && TypeSelector.this.pairs[position] != null) {
                        tv.setTextColor(AddressItem.getColor(TypeSelector.this.pairs[position].flags));
                    }
                }
                return view1;
            }
        }, this).setNegativeButton(Re.s(0x7F0700A1), null));  // string:cancel "Cancel"
    }

    public TypeSelector(SparseArray sparseArray0, String value, String message, DialogInterface.OnClickListener listener) {
        this(sparseArray0, null, value, message, listener);
    }

    @Override  // android.content.DialogInterface$OnClickListener
    public void onClick(DialogInterface dialog, int which) {
        try {
            int flags = this.pairs[which].flags;
            if(!"0".equals(this.value)) {
                AddressItem.dataFromString(0L, this.value, flags);
            }
            if(dialog != null) {
                Tools.dismiss(dialog);
            }
            this.listener.onClick(dialog, flags);
        }
        catch(NumberFormatException e) {
            SearchMenuItem.inputError(e, null);
        }
    }
}

