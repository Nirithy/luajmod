package android.ext;

import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface;
import android.view.View.OnClickListener;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class TypicalValues implements DialogInterface.OnClickListener, View.OnClickListener {
    private EditText edit;
    private final String[] names;
    private final String[] values;

    public TypicalValues(String[] names, String[] values) {
        this.names = names;
        this.values = values;
        if(names.length != values.length) {
            throw new IllegalArgumentException("Params must be with same length: " + names.length + " != " + values.length);
        }
    }

    @Override  // android.content.DialogInterface$OnClickListener
    public void onClick(DialogInterface dialog, int which) {
        if(which < 0 || which >= this.values.length) {
            Log.w(("Bad index: " + which + " for " + this.values.length), new IndexOutOfBoundsException());
            return;
        }
        if(this.edit == null) {
            Log.w("Unkwnon edit", new NullPointerException());
            return;
        }
        this.edit.setText(this.values[which]);
    }

    @Override  // android.view.View$OnClickListener
    public void onClick(View v) {
        if(v == null) {
            Log.w("Unkwnon view", new NullPointerException());
            return;
        }
        Object object0 = v.getTag();
        if(!(object0 instanceof EditText)) {
            Log.w(("Unkwnon tag: " + object0), new IllegalArgumentException());
            return;
        }
        this.edit = (EditText)object0;
        Alert.show(Alert.create().setCustomTitle(Tools.getCustomTitle(0x7F07025E)).setItems(this.names, this).setNegativeButton(Re.s(0x7F0700A1), null));  // string:typical_values "Typical values"
    }

    public static void setCacheDirs(Button btn, EditText pathEdit) {
        String external;
        String internal;
        try {
            internal = Tools.getContext().getFilesDir().getAbsolutePath();
        }
        catch(Throwable e) {
            internal = "error";
            Log.w("Failed get files dir", e);
        }
        try {
            external = Tools.getContext().getExternalFilesDir(null).getAbsolutePath();
        }
        catch(Throwable e) {
            external = "error";
            Log.w("Failed get ext files dir", e);
        }
        btn.setOnClickListener(new TypicalValues(new String[]{Re.s(0x7F0701A1), Re.s(0x7F0701A2)}, new String[]{internal, external}));  // string:internal_storage "Internal storage"
        btn.setTag(pathEdit);
    }
}

