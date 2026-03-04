package android.ext;

import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface;
import android.fix.LayoutInflater;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.CompoundButton;
import android.widget.TextView;

public class Editor implements TextWatcher, View.OnClickListener, View.OnLongClickListener {
    public static final int MODE_EDIT = 1;
    public static final int MODE_EDIT_ALL = 2;
    public static final int MODE_EDIT_MEM = 3;
    public static final int MODE_EDIT_SAVED;
    final CheckBox addNotReplace;
    final CheckBox changeValue;
    private static volatile boolean editAllExtended;
    private final EditText editStep;
    private final View editStepRow;
    private String examplesList;
    private final Button extBtn;
    private final View fill;
    private final View fillHex;
    private final View fillHexUtf16le;
    private final View fillHexUtf8;
    private final View fillHexUtf8Utf16le;
    private final View fillUtf16le;
    private final View fillUtf8;
    private final View fillValues;
    final CheckBox freezeCheck;
    private final EditText freezeFrom;
    final View freezeRange;
    private final EditText freezeTo;
    private final SystemSpinner freezeType;
    private final AddressItem item;
    private final TextView message;
    final EditText name;
    private final View nameRow;
    private final View nameText;
    private final EditText number;
    private final CheckBox saveAs;
    private int type;
    private final TextView typeHint;
    private final int[][] typeMatrix;
    private final View[] typeMatrixHeader;
    private final View view;

    static {
        Editor.editAllExtended = false;
    }

    public Editor(int type, AddressItem item) {
        this.type = type;
        this.item = item.copy();
        this.view = LayoutInflater.inflateStatic(0x7F04001E, null);  // layout:service_editor
        this.message = (TextView)this.view.findViewById(0x7F0B000E);  // id:message
        this.nameRow = this.view.findViewById(0x7F0B0108);  // id:name_row
        this.saveAs = (CheckBox)this.view.findViewById(0x7F0B0109);  // id:save_as
        this.nameText = this.view.findViewById(0x7F0B010A);  // id:name_text
        this.name = (EditText)this.view.findViewById(0x7F0B0051);  // id:name
        this.name.setDataType(3);
        this.typeHint = (TextView)this.view.findViewById(0x7F0B00F2);  // id:type_hint
        this.number = (EditText)this.view.findViewById(0x7F0B004D);  // id:number
        this.number.setDataType(1);
        this.view.findViewById(0x7F0B0042).setTag(new Object[]{this.number, item.flags});  // id:number_converter
        this.editStepRow = this.view.findViewById(0x7F0B00F3);  // id:edit_step_row
        this.editStep = (EditText)this.view.findViewById(0x7F0B00F5);  // id:edit_step
        this.editStep.setDataType(1);
        this.view.findViewById(0x7F0B00F6).setTag(this.editStep);  // id:step_converter
        View view0 = this.view.findViewById(0x7F0B00F4);  // id:edit_step_label
        Tools.setButtonHelpBackground(view0);
        view0.setOnClickListener(this);
        this.addNotReplace = (CheckBox)this.view.findViewById(0x7F0B00F7);  // id:add_not_replace
        Tools.setButtonHelpBackground(this.addNotReplace);
        this.addNotReplace.setOnLongClickListener(this);
        this.fill = this.view.findViewById(0x7F0B00F8);  // id:fill
        this.fillValues = this.view.findViewById(0x7F0B00F9);  // id:fill_values
        this.fillUtf8 = this.view.findViewById(0x7F0B00FA);  // id:fill_utf8
        this.fillUtf16le = this.view.findViewById(0x7F0B00FB);  // id:fill_utf16le
        this.fillHex = this.view.findViewById(0x7F0B00FC);  // id:fill_hex
        this.fillHexUtf8 = this.view.findViewById(0x7F0B00FD);  // id:fill_hex_utf8
        this.fillHexUtf16le = this.view.findViewById(0x7F0B00FE);  // id:fill_hex_utf16le
        this.fillHexUtf8Utf16le = this.view.findViewById(0x7F0B00FF);  // id:fill_hex_utf8_utf16le
        this.changeValue = (CheckBox)this.view.findViewById(0x7F0B00F0);  // id:change_value
        this.freezeCheck = (CheckBox)this.view.findViewById(0x7F0B0101);  // id:freeze
        this.freezeType = (SystemSpinner)this.view.findViewById(0x7F0B0102);  // id:freeze_spinner
        this.freezeRange = this.view.findViewById(0x7F0B0103);  // id:freeze_range
        this.freezeFrom = (EditText)this.view.findViewById(0x7F0B0104);  // id:freeze_from
        this.freezeFrom.setDataType(1);
        this.view.findViewById(0x7F0B0105).setTag(this.freezeFrom);  // id:from_converter
        this.freezeTo = (EditText)this.view.findViewById(0x7F0B0106);  // id:freeze_to
        this.freezeTo.setDataType(1);
        this.view.findViewById(0x7F0B0107).setTag(this.freezeTo);  // id:to_converter
        this.extBtn = (Button)this.view.findViewById(0x7F0B0019);  // id:ext_btn
        android.ext.Editor.1 focus = new FocusListener() {
            @Override  // android.ext.InternalKeyboard$FocusListener
            protected boolean useExternal(View v, boolean hasFocus) {
                return Editor.this.updateKbd() || v == Editor.this.name;
            }
        };
        for(Object object0: this.view.getFocusables(2)) {
            View v = (View)object0;
            if(v instanceof EditText) {
                Tools.setOnFocusChangeListener(v, focus);
            }
        }
        this.typeMatrixHeader = new View[]{this.nameRow, this.saveAs, this.nameText, this.editStepRow, this.fill, this.extBtn, this.changeValue};
        int[][] arr2_v = new int[4][];
        int[] arr_v = new int[7];
        arr_v[0] = 1;
        arr_v[2] = 1;
        arr2_v[0] = arr_v;
        int[] arr_v1 = new int[7];
        arr_v1[0] = 1;
        arr_v1[1] = 1;
        arr2_v[1] = arr_v1;
        arr2_v[2] = new int[]{0, 0, 0, 1, 1, 1, 1};
        int[] arr_v2 = new int[7];
        arr_v2[0] = 1;
        arr_v2[1] = 1;
        arr2_v[3] = arr_v2;
        this.typeMatrix = arr2_v;
        if(type < 0 || type >= this.typeMatrix.length) {
            throw new IllegalArgumentException("Type must be between: 0 and " + (this.typeMatrix.length - 1) + " but got: " + type);
        }
        this.setVisibility(type);
        this.examplesList = Re.s(0x7F07003F);  // string:edit_examples "\n\t12345\n\t12BA0h\n\t12BA0r\n\n__help_xor_search_title__:\n\t12345X4\n\t12345X-4\n\t12BA0hX4\n\t12BA0hX-4\n\t12BA0rX4\n\t12BA0rX-4\n__text_examples__\n\n__rtfm__\n 
                                               //    "
        TextView value = (TextView)this.view.findViewById(0x7F0B001B);  // id:value
        if(type == 2) {
            Tools.setButtonHelpBackground(value);
            value.setOnClickListener(this);
            this.examplesList = Re.s(0x7F07003E);  // string:edit_all_examples "\n\t12345\n\t12BA0h\n\t12BA0r\n\t12;34;56;78;90\n\n__help_xor_search_title__:\n\t12345X4\n\t12345X-4\n\t12BA0hX4\n\t12BA0hX-4\n\t12BA0rX4\n\t12BA0rX-4\n\t12;34;56;78;90X4\n\t12;34;56;78;90X-4\n__text_examples__\n\n__rtfm__\n 
                                                   //    "
            this.setEditAllExtended(Editor.editAllExtended);
        }
        else if(type == 1) {
            Tools.setButtonHelpBackground(value);
            value.setOnClickListener(this);
        }
        this.extBtn.setOnClickListener(this);
        value.setText(Tools.stripColon(value.getText().toString()) + ':');
        this.init();
        this.name.addTextChangedListener(this);
    }

    @Override  // android.text.TextWatcher
    public void afterTextChanged(Editable s) {
        this.saveAs.setChecked(true);
    }

    @Override  // android.text.TextWatcher
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    public String getEditStep() {
        if(this.editStepRow.getVisibility() == 0) {
            String s = this.editStep.getText().toString().trim();
            History.add(s, 1);
            return s;
        }
        return "0";
    }

    public AddressItem getItem() {
        return this.item;
    }

    public String getName() {
        String s = this.name.getText().toString().trim();
        History.add(s, 2);
        return s;
    }

    public String getNumber() {
        String s = this.number.getText().toString().trim();
        History.add(s, 1);
        return s;
    }

    public EditText getNumberEdit() {
        return this.number;
    }

    public SavedItem getSavedItem() {
        return this.getSavedItem(this.item);
    }

    public SavedItem getSavedItem(AddressItem item) {
        SavedItem savedItem = new SavedItem(item);
        savedItem.freeze = this.freezeCheck.isChecked();
        savedItem.setFreezeType(this.freezeType.getSelected());
        if(savedItem.freezeType == 3) {
            savedItem.setRangeFromString(SearchMenuItem.checkScript(this.freezeFrom.getText().toString().trim()), SearchMenuItem.checkScript(this.freezeTo.getText().toString().trim()));
        }
        if(!savedItem.freeze && this.addNotReplace.isChecked()) {
            savedItem.flags |= 0x40000000;
        }
        if(this.saveAs.getVisibility() == 0 && this.saveAs.isChecked() || this.nameText.getVisibility() == 0) {
            String s = this.getName();
            if(!savedItem.getName().equals(s)) {
                savedItem.name = s;
            }
        }
        return savedItem;
    }

    public View getView() {
        return this.view;
    }

    public View getViewForAttach() {
        return InternalKeyboard.getView(this.getViewForAttachSimple());
    }

    public View getViewForAttachSimple() {
        return Tools.getViewForAttach(this.getView());
    }

    public void inSavedList() {
        this.saveAs.setChecked(true);
    }

    private void init() {
        String s = AddressItem.getLimits(this.item.flags);
        this.typeHint.setText(s);
        String s1 = this.item.getStringDataTrim();
        this.number.setText(s1);
        this.editStep.setText("0");
        this.changeValue.setChecked(true);
        android.ext.Editor.2 valueChanged = new TextWatcher() {
            private void action() {
                Editor.this.changeValue.setChecked(true);
                Editor.this.updateKbd();
            }

            @Override  // android.text.TextWatcher
            public void afterTextChanged(Editable s) {
                this.action();
            }

            @Override  // android.text.TextWatcher
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override  // android.text.TextWatcher
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                this.action();
            }
        };
        this.number.addTextChangedListener(valueChanged);
        this.editStep.addTextChangedListener(valueChanged);
        android.ext.Editor.3 editor$30 = new CompoundButton.OnCheckedChangeListener() {
            @Override  // android.widget.CompoundButton$OnCheckedChangeListener
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    Editor.this.freezeCheck.setChecked(false);
                }
                Editor.this.changeValue.setChecked(true);
            }
        };
        this.addNotReplace.setOnCheckedChangeListener(editor$30);
        android.ext.Editor.4 editor$40 = new CompoundButton.OnCheckedChangeListener() {
            @Override  // android.widget.CompoundButton$OnCheckedChangeListener
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    Editor.this.addNotReplace.setChecked(false);
                }
            }
        };
        this.freezeCheck.setOnCheckedChangeListener(editor$40);
        this.freezeType.setData(SavedItem.getFreezeNames());
        android.ext.Editor.5 editor$50 = new DialogInterface.OnClickListener() {
            @Override  // android.content.DialogInterface$OnClickListener
            public void onClick(DialogInterface dialog, int position) {
                Editor.this.freezeRange.setVisibility((position == 3 ? 0 : 8));
            }
        };
        this.freezeType.setOnItemSelectedListener(editor$50);
        if(this.item instanceof SavedItem) {
            String s2 = ((SavedItem)this.item).getName();
            this.name.setText(s2);
            this.freezeCheck.setChecked(((SavedItem)this.item).freeze);
            this.freezeType.setSelected(((int)((SavedItem)this.item).freezeType));
            String s3 = ((SavedItem)this.item).getRangeString(true);
            this.freezeFrom.setText(s3);
            String s4 = ((SavedItem)this.item).getRangeString(false);
            this.freezeTo.setText(s4);
            return;
        }
        this.freezeCheck.setChecked(false);
        this.freezeType.setSelected(0);
        this.freezeFrom.setText("0");
        this.freezeTo.setText("0");
    }

    // 去混淆评级： 中等(50)
    public boolean isNeedSave() {
        return this.freezeCheck.isChecked() ? true : this.nameRow.getVisibility() == 0 && (this.saveAs.getVisibility() == 0 && this.saveAs.isChecked() || this.nameText.getVisibility() == 0);
    }

    public boolean isValueChange() {
        return this.changeValue.isChecked();
    }

    @Override  // android.view.View$OnClickListener
    public void onClick(View v) {
        if(v == null) {
            return;
        }
        switch(v.getId()) {
            case 0x7F0B0019: {  // id:ext_btn
                if(this.type == 2) {
                    this.setEditAllExtended(!Editor.editAllExtended);
                }
                this.number.requestFocus();
                return;
            }
            case 0x7F0B001B: {  // id:value
                Searcher.showExamples(this.examplesList);
                return;
            }
            case 0x7F0B00F4: {  // id:edit_step_label
                ConfigListAdapter.showHelp(0x7F07020B);  // string:help_fill "* Fill (increment) values:\n\tIt often happens that too many values 
                                                         // are found, but they can not be changed from the game.\n\tIn this case, the fill 
                                                         // comes to the rescue.\n\n\tA typical example of use is the search for the price of 
                                                         // an item, with a subsequent replacement by a negative one.\n\n\t1. Select the desired 
                                                         // items.\n\t2. Select \"__edit_selected__\".\n\t3. Press the button \"__more__\" to 
                                                         // display \"__increments__\" field, if this field is not present.\n\t4. In \"__increments__\" 
                                                         // field, enter the increment value. Usually this is 1. And click \"__yes__\".\n\t5. 
                                                         // The selected items will receive new values.\n\t6. Go to the game and see how the 
                                                         // desired value has changed, moving across game screens.\n\t7. If the value has not 
                                                         // changed, then the desired element not among selected. Return to __app_name__ and 
                                                         // using the \"__delete_selected__\" -> \"__revert_and_remove__\", remove the selected 
                                                         // items, returning them the previous value. Now you can try everything from the beginning 
                                                         // with other elements, if there are any.\n\t8. If the value has changed, then the 
                                                         // desired element is one of the selected elements. Remember the new value and return 
                                                         // to __app_name__.\n\t9. In the list of selected items, search for the desired value. 
                                                         // Unselect it. Delete all other values, using \"__delete_selected__\" -> \"__revert_and_remove__\", 
                                                         // returning them the previous value.\n\t10. Modify the desired element value as you 
                                                         // need.\n\n\tTypical errors are removal without revert. After that, the game can crash.\n\n\tIf 
                                                         // you find a lot of values, try to do fill in small portions to avoid crashes.\n  
                                                         //   "
            }
        }
    }

    @Override  // android.view.View$OnLongClickListener
    public boolean onLongClick(View v) {
        if(v != null && v.getId() == 0x7F0B00F7) {  // id:add_not_replace
            ConfigListAdapter.showHelp(0x7F070254);  // string:help_add_to_value "* __help_add_to_value_title__:\nIf this option is checked, 
                                                     // the entered value will be added to the existing value, and not replace it. For example, 
                                                     // the element has a value of 5, and you entered 7, in the value field. If this option 
                                                     // is checked, the element will get a new value of 5 + 7, equal to 12. If not checked, 
                                                     // then 7.\nYou can enter both positive and negative values.\nThis option is incompatible 
                                                     // with the freezing of the value."
            return true;
        }
        return false;
    }

    @Override  // android.text.TextWatcher
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        this.saveAs.setChecked(true);
    }

    private void setEditAllExtended(boolean value) {
        Editor.editAllExtended = value;
        String s = value ? Re.s(0x7F070157) : Re.s(0x7F070156);  // string:less "Less"
        this.extBtn.setText(s);
        this.editStepRow.setVisibility((value ? 0 : 8));
        this.fill.setVisibility((value ? 0 : 8));
        this.fillUtf8.setVisibility((!value || this.item.flags != 1 ? 8 : 0));
        this.fillUtf16le.setVisibility((!value || (this.item.flags & 7) == 0 ? 8 : 0));
        int hex = !value || this.item.flags != 1 ? 8 : 0;
        this.fillHex.setVisibility(hex);
        this.fillHexUtf8.setVisibility(hex);
        this.fillHexUtf16le.setVisibility(hex);
        this.fillHexUtf8Utf16le.setVisibility(hex);
    }

    public void setFillListener(View.OnClickListener listener) {
        this.fillValues.setOnClickListener(listener);
        this.fillUtf8.setOnClickListener(listener);
        this.fillUtf16le.setOnClickListener(listener);
        this.fillHex.setOnClickListener(listener);
        this.fillHexUtf8.setOnClickListener(listener);
        this.fillHexUtf16le.setOnClickListener(listener);
        this.fillHexUtf8Utf16le.setOnClickListener(listener);
    }

    public void setMessage(CharSequence message) {
        this.message.setText(message);
    }

    public void setName(String name) {
        this.name.setText(name);
    }

    public void setNumber(String number) {
        this.number.setText(number.trim());
    }

    private void setVisibility(int type) {
        for(int i = 0; i < this.typeMatrix[type].length; ++i) {
            this.typeMatrixHeader[i].setVisibility((this.typeMatrix[type][i] == 1 ? 0 : 8));
        }
    }

    private boolean updateKbd() {
        boolean str = false;
        if((Config.configClient & 1) == 0) {
            return false;
        }
        if(this.number.isFocused() && ParserNumbers.needExtKbd(this.number.getText().toString())) {
            str = true;
        }
        InternalKeyboard.needExternalKbd(this.number, str);
        return str;
    }
}

