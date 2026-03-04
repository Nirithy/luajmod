package android.ext;

import java.util.ArrayList;
import java.util.List;

public class CharSequenceBuilder {
    private final List array;

    public CharSequenceBuilder() {
        this.array = new ArrayList();
    }

    public CharSequenceBuilder append(CharSequence cs) {
        this.array.add(cs);
        return this;
    }

    public CharSequenceBuilder appendFormat(String format, CharSequence[] arr) {
        int offset = 0;
        int v2;
        for(int i = 0; (v2 = format.indexOf("%s", offset)) >= 0; ++i) {
            this.array.add(format.substring(offset, v2));
            this.array.add(arr[i]);
            offset = v2 + 2;
        }
        this.array.add(format.substring(offset, format.length()));
        return this;
    }

    public CharSequenceBuilder appendJoin(CharSequence delim, CharSequence[] arr) {
        for(int i = 0; i < arr.length; ++i) {
            if(i != 0) {
                this.array.add(delim);
            }
            this.array.add(arr[i]);
        }
        return this;
    }

    public int size() {
        return this.array.size();
    }

    public CharSequence toCharSequence() {
        return Tools.concat(((CharSequence[])this.array.toArray(new CharSequence[this.array.size()])));
    }
}

