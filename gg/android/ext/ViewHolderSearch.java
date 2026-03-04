package android.ext;

import android.view.View.OnClickListener;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

class ViewHolderSearch extends ViewHolderBase {
    interface Listener extends android.ext.ViewHolderBase.Listener, View.OnClickListener {
    }

    final LinearLayout info;
    final ImageView remove;
    final TextView type;
    final TextView value;

    ViewHolderSearch(View view, Listener listener) {
        super(view, listener);
        this.value = (TextView)view.findViewById(0x7F0B0072);  // id:addr_item_value
        this.info = (LinearLayout)view.findViewById(0x7F0B0073);  // id:addr_item_info
        this.type = (TextView)view.findViewById(0x7F0B0074);  // id:addr_item_type
        this.remove = Config.setIconSize(((ImageView)view.findViewById(0x7F0B0076)));  // id:addr_item_remove
        this.remove.setOnClickListener(listener);
        this.remove.setOnLongClickListener(listener);
    }
}

