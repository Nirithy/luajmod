package android.ext;

import android.util.Base64;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

public class UsedList {
    private static final int CAPACITY = 0x20;
    private int[] data;
    private final int index;
    private int used;

    public UsedList(int index) {
        this.used = 0;
        this.index = index;
        this.data = new int[0x20];
    }

    public void add(int value) {
        int used = this.used;
        int[] data = this.data;
        for(int i = 0; true; ++i) {
            if(i >= used) {
                if(used == data.length) {
                    data = Arrays.copyOf(data, data.length + 0x20);
                    this.data = data;
                }
                data[used] = value;
                this.used = used + 1;
                this.saveData();
                return;
            }
            if(data[i] == value) {
                break;
            }
        }
    }

    private void saveData() {
        int used = this.used;
        int[] data = this.data;
        ByteBuffer byteBuffer0 = ByteBuffer.allocate(used * 4);
        byteBuffer0.order(ByteOrder.LITTLE_ENDIAN);
        for(int i = 0; i < used; ++i) {
            byteBuffer0.putInt(data[i]);
        }
        String s = Base64.encodeToString(byteBuffer0.array(), 11);
        new SPEditor().putString("used-" + this.index, s).commit();
    }
}

