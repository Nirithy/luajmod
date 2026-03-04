package android.ext;

import android.view.View;
import android.widget.TextView;

class ViewHolderMemory extends ViewHolderBase {
    TextView valueByte;
    TextView valueDouble;
    TextView valueDword;
    TextView valueFloat;
    TextView valueQword;
    TextView valueWord;
    TextView valueXor;

    ViewHolderMemory(View view, Listener listener) {
        super(view, listener);
    }
}

