package android.ext;

import android.content.res.Configuration;
import android.view.View.OnLongClickListener;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

class ViewHolderBase {
    interface Listener extends View.OnLongClickListener, CompoundButton.OnCheckedChangeListener {
    }

    static final int NOT_SELECTED = -2;
    final TextView address;
    final CheckBox ch;
    final int orientation;
    int pos;
    final TextView region;
    final TextView valueArm;
    final TextView valueArm8;
    final TextView valueHex;
    final TextView valueJava;
    final TextView valueRHex;
    final TextView valueString;
    final TextView valueThumb;
    final View view;

    ViewHolderBase(View view, Listener listener) {
        this.ch = (CheckBox)view.findViewById(0x7F0B006A);  // id:ch
        this.address = (TextView)view.findViewById(0x7F0B0049);  // id:address
        this.valueHex = (TextView)view.findViewById(0x7F0B006B);  // id:value_hex
        this.valueRHex = (TextView)view.findViewById(0x7F0B006C);  // id:value_rhex
        this.valueString = (TextView)view.findViewById(0x7F0B006D);  // id:value_string
        this.valueJava = (TextView)view.findViewById(0x7F0B006E);  // id:value_java
        this.valueArm = (TextView)view.findViewById(0x7F0B006F);  // id:value_arm
        this.valueThumb = (TextView)view.findViewById(0x7F0B0070);  // id:value_thumb
        this.valueArm8 = (TextView)view.findViewById(0x7F0B0071);  // id:value_arm8
        this.region = (TextView)view.findViewById(0x7F0B0075);  // id:addr_item_region
        this.view = view;
        this.orientation = MainService.context.getResources().getConfiguration().orientation;
        this.view.setTag(this);
        this.ch.setOnCheckedChangeListener(listener);
        this.ch.setOnLongClickListener(listener);
        this.ch.setTag(this);
    }

    void updateBackground(boolean checked, int selected) {
        int v1;
        View view0 = this.view;
        if(checked) {
            v1 = 0x80744294;
        }
        else {
            v1 = this.pos == selected ? 0x80949442 : 0;
        }
        view0.setBackgroundColor(v1);
    }

    boolean wrongOrientation() {
        Configuration configuration0 = MainService.context.getResources().getConfiguration();
        return this.orientation != configuration0.orientation;
    }
}

