package luaj;

import android.ext.Log;
import android.ext.Script.ApiFunction;
import android.ext.ToolsLight;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LuaTable extends LuaValue implements Metatable {
    static class BooleanValueEntry extends ValueEntry {
        private boolean value;

        BooleanValueEntry(LuaValue key, boolean value) {
            super(key);
            this.value = value;
        }

        @Override  // luaj.LuaTable$Entry
        public Entry set(LuaValue value) {
            if(value instanceof LuaBoolean) {
                this.value = value == LuaValue.TRUE;
                return this;
            }
            if(value instanceof LuaLong) {
                long v = value.tolong();
                return ((long)(((int)v))) == v ? new IntValueEntry(this.key, ((int)v)) : new LongValueEntry(this.key, v);
            }
            if(value instanceof LuaDouble) {
                double f = value.todouble();
                return ((double)(((float)f))) == f ? new FloatValueEntry(this.key, ((float)f)) : new DoubleValueEntry(this.key, f);
            }
            return new NormalEntry(this.key, value);
        }

        @Override  // luaj.LuaTable$Entry
        public LuaValue value() {
            return LuaTable.valueOf(this.value);
        }
    }

    static class DeadSlot implements Slot {
        private final Object key;
        private Slot next;

        private DeadSlot(LuaValue key, Slot next) {
            if(LuaTable.isLargeKey(key)) {
                key = new WeakReference(key);
            }
            this.key = key;
            this.next = next;
        }

        DeadSlot(LuaValue luaValue0, Slot luaTable$Slot0, DeadSlot luaTable$DeadSlot0) {
            this(luaValue0, luaTable$Slot0);
        }

        @Override  // luaj.LuaTable$Slot
        public Slot add(Slot newEntry) {
            return this.next == null ? newEntry : this.next.add(newEntry);
        }

        @Override  // luaj.LuaTable$Slot
        public int arraykey(int max) {
            return -1;
        }

        @Override  // luaj.LuaTable$Slot
        public StrongSlot find(LuaValue key) {
            return null;
        }

        @Override  // luaj.LuaTable$Slot
        public StrongSlot first() {
            return null;
        }

        // 去混淆评级： 低(20)
        private LuaValue key() {
            return this.key instanceof WeakReference ? ((LuaValue)((WeakReference)this.key).get()) : ((LuaValue)this.key);
        }

        @Override  // luaj.LuaTable$Slot
        public boolean keyeq(LuaValue key) {
            LuaValue luaValue1 = this.key();
            return luaValue1 != null && key.raweq(luaValue1);
        }

        @Override  // luaj.LuaTable$Slot
        public int keyindex(int hashMask) {
            return 0;
        }

        @Override  // luaj.LuaTable$Slot
        public Slot relink(Slot rest) {
            return rest;
        }

        @Override  // luaj.LuaTable$Slot
        public Slot remove(StrongSlot target) {
            if(this.key() != null) {
                this.next = this.next.remove(target);
                return this;
            }
            return this.next;
        }

        @Override  // luaj.LuaTable$Slot
        public Slot rest() {
            return this.next;
        }

        @Override  // luaj.LuaTable$Slot
        public Slot set(StrongSlot target, LuaValue value) {
            Slot luaTable$Slot0 = this.next == null ? null : this.next.set(target, value);
            if(this.key() != null) {
                this.next = luaTable$Slot0;
                return this;
            }
            return luaTable$Slot0;
        }

        @Override
        public String toString() {
            StringBuilder buf = new StringBuilder();
            buf.append("<dead");
            LuaValue luaValue0 = this.key();
            if(luaValue0 != null) {
                buf.append(": ");
                buf.append(luaValue0.toString());
            }
            buf.append('>');
            if(this.next != null) {
                buf.append("; ");
                buf.append(this.next.toString());
            }
            return buf.toString();
        }
    }

    static class DoubleValueEntry extends ValueEntry {
        private double value;

        DoubleValueEntry(LuaValue key, double value) {
            super(key);
            this.value = value;
        }

        @Override  // luaj.LuaTable$Entry
        public Entry set(LuaValue value) {
            if(value instanceof LuaBoolean) {
                return new BooleanValueEntry(this.key, value.toboolean());
            }
            LuaValue luaValue1 = value.tonumber();
            if(luaValue1.isnil()) {
                return new NormalEntry(this.key, value);
            }
            if(luaValue1 instanceof LuaLong) {
                long v = luaValue1.tolong();
                return ((long)(((int)v))) == v ? new IntValueEntry(this.key, ((int)v)) : new LongValueEntry(this.key, v);
            }
            double f = luaValue1.todouble();
            if(((double)(((float)f))) == f) {
                return new FloatValueEntry(this.key, ((float)f));
            }
            this.value = f;
            return this;
        }

        @Override  // luaj.LuaTable$Entry
        public LuaValue value() {
            return LuaTable.valueOf(this.value);
        }
    }

    static abstract class Entry extends Varargs implements StrongSlot {
        @Override  // luaj.LuaTable$Slot
        public Slot add(Slot entry) {
            return new LinkSlot(this, entry);
        }

        @Override  // luaj.Varargs
        public LuaValue arg(int i) {
            switch(i) {
                case 1: {
                    return this.key();
                }
                case 2: {
                    return this.value();
                }
                default: {
                    return LuaTable.NIL;
                }
            }
        }

        @Override  // luaj.Varargs
        public LuaValue arg1() {
            return this.key();
        }

        @Override  // luaj.LuaTable$Slot
        public int arraykey(int max) {
            return 0;
        }

        // 去混淆评级： 低(20)
        @Override  // luaj.LuaTable$Slot
        public StrongSlot find(LuaValue key) {
            return this.keyeq(key) ? this : null;
        }

        @Override  // luaj.LuaTable$Slot
        public StrongSlot first() {
            return this;
        }

        @Override  // luaj.LuaTable$StrongSlot
        public abstract LuaValue key();

        @Override  // luaj.LuaTable$Slot
        public abstract boolean keyeq(LuaValue arg1);

        @Override  // luaj.LuaTable$Slot
        public abstract int keyindex(int arg1);

        @Override  // luaj.Varargs
        public int narg() {
            return 2;
        }

        @Override  // luaj.LuaTable$Slot
        public Slot relink(Slot rest) {
            return rest != null ? new LinkSlot(this, rest) : this;
        }

        @Override  // luaj.LuaTable$Slot
        public Slot remove(StrongSlot target) {
            return new DeadSlot(this.key(), null, null);
        }

        @Override  // luaj.LuaTable$Slot
        public Slot rest() {
            return null;
        }

        abstract Entry set(LuaValue arg1);

        @Override  // luaj.LuaTable$Slot
        public Slot set(StrongSlot target, LuaValue value) {
            return this.set(value);
        }

        @Override  // luaj.Varargs
        public Varargs subargs(int start) {
            switch(start) {
                case 1: {
                    return this;
                }
                case 2: {
                    return this.value();
                }
                default: {
                    return LuaTable.NONE;
                }
            }
        }

        @Override  // luaj.LuaTable$StrongSlot
        public Varargs toVarargs() {
            return this;
        }

        @Override  // luaj.LuaTable$StrongSlot
        public abstract LuaValue value();
    }

    static class FloatValueEntry extends ValueEntry {
        private float value;

        FloatValueEntry(LuaValue key, float value) {
            super(key);
            this.value = value;
        }

        @Override  // luaj.LuaTable$Entry
        public Entry set(LuaValue value) {
            if(value instanceof LuaBoolean) {
                return new BooleanValueEntry(this.key, value.toboolean());
            }
            LuaValue luaValue1 = value.tonumber();
            if(luaValue1.isnil()) {
                return new NormalEntry(this.key, value);
            }
            if(luaValue1 instanceof LuaLong) {
                long v = luaValue1.tolong();
                return ((long)(((int)v))) == v ? new IntValueEntry(this.key, ((int)v)) : new LongValueEntry(this.key, v);
            }
            double f = luaValue1.todouble();
            if(((double)(((float)f))) == f) {
                this.value = (float)f;
                return this;
            }
            return new DoubleValueEntry(this.key, f);
        }

        @Override  // luaj.LuaTable$Entry
        public LuaValue value() {
            return LuaTable.valueOf(this.value);
        }
    }

    static class IntKeyEntry extends Entry {
        private final int key;
        private LuaValue value;

        IntKeyEntry(int key, LuaValue value) {
            this.key = key;
            this.value = value;
        }

        @Override  // luaj.LuaTable$Entry
        public int arraykey(int max) {
            return this.key < 1 || this.key > max ? 0 : this.key;
        }

        @Override  // luaj.LuaTable$Entry
        public LuaValue key() {
            return LuaTable.valueOf(this.key);
        }

        @Override  // luaj.LuaTable$Entry
        public boolean keyeq(LuaValue key) {
            return key.isnumber() ? this.key == key.toint() : false;
        }

        @Override  // luaj.LuaTable$Entry
        public int keyindex(int mask) {
            return ((int)(((long)this.key) >>> 0x20 ^ ((long)this.key))) & mask;
        }

        @Override  // luaj.LuaTable$Entry
        public Entry set(LuaValue value) {
            this.value = value;
            return this;
        }

        @Override  // luaj.LuaTable$Entry
        public LuaValue value() {
            return this.value;
        }
    }

    static class IntValueEntry extends ValueEntry {
        private int value;

        IntValueEntry(LuaValue key, int value) {
            super(key);
            this.value = value;
        }

        int getValue() {
            return this.value;
        }

        @Override  // luaj.LuaTable$Entry
        public Entry set(LuaValue value) {
            if(value instanceof LuaBoolean) {
                return new BooleanValueEntry(this.key, value.toboolean());
            }
            LuaValue luaValue1 = value.tonumber();
            if(luaValue1.isnil()) {
                return new NormalEntry(this.key, value);
            }
            if(luaValue1 instanceof LuaLong) {
                long v = luaValue1.tolong();
                if(((long)(((int)v))) == v) {
                    this.value = (int)v;
                    return this;
                }
                return new LongValueEntry(this.key, v);
            }
            double f = luaValue1.todouble();
            return ((double)(((float)f))) == f ? new FloatValueEntry(this.key, ((float)f)) : new DoubleValueEntry(this.key, f);
        }

        @Override  // luaj.LuaTable$Entry
        public LuaValue value() {
            return LuaTable.valueOf(this.value);
        }
    }

    public class Iterator {
        private StrongSlot first;
        private int i;
        private Slot slot;

        public Iterator() {
            this.i = -1;
            this.slot = null;
            this.first = null;
        }

        public int intkey() {
            return this.first == null ? this.i + 1 : 0;
        }

        public LuaValue key() {
            StrongSlot first = this.first;
            return first != null ? first.key() : LuaLong.valueOf(this.i + 1);
        }

        public boolean next() {
            StrongSlot luaTable$StrongSlot1;
            Slot slot;
            Slot slot = this.slot;
            while(true) {
                if(slot == null) {
                    this.slot = null;
                    this.first = null;
                    int i = this.i + 1;
                    while(true) {
                        if(i >= LuaTable.this.array.length) {
                            int i;
                            for(i = i - LuaTable.this.array.length; true; ++i) {
                                if(i >= LuaTable.this.hash.length) {
                                    return false;
                                }
                                slot = LuaTable.this.hash[i];
                            label_10:
                                if(slot != null) {
                                    break;
                                }
                            }
                            StrongSlot luaTable$StrongSlot0 = slot.first();
                            if(luaTable$StrongSlot0 == null) {
                                slot = slot.rest();
                                goto label_10;
                            }
                            this.i = LuaTable.this.array.length + i;
                            this.slot = slot;
                            this.first = luaTable$StrongSlot0;
                            return true;
                        }
                        if(LuaTable.this.array[i] != null && (LuaTable.this.m_metatable == null || LuaTable.this.m_metatable.arrayget(LuaTable.this.array, i) != null)) {
                            this.i = i;
                            return true;
                        }
                        ++i;
                    }
                }
                slot = slot.rest();
                if(slot != null) {
                    luaTable$StrongSlot1 = slot.first();
                    if(luaTable$StrongSlot1 != null) {
                        break;
                    }
                }
            }
            this.slot = slot;
            this.first = luaTable$StrongSlot1;
            return true;
        }

        public LuaValue value() {
            StrongSlot first = this.first;
            if(first != null) {
                return first.value();
            }
            return LuaTable.this.m_metatable == null ? LuaTable.this.array[this.i] : LuaTable.this.m_metatable.arrayget(LuaTable.this.array, this.i);
        }

        public Varargs varargs() {
            StrongSlot first = this.first;
            return first == null ? LuaTable.varargsOf(this.key(), this.value()) : first.toVarargs();
        }
    }

    static class LinkSlot implements StrongSlot {
        private Entry entry;
        private Slot next;

        LinkSlot(Entry entry, Slot next) {
            this.entry = entry;
            this.next = next;
        }

        @Override  // luaj.LuaTable$Slot
        public Slot add(Slot entry) {
            return this.setnext(this.next.add(entry));
        }

        @Override  // luaj.LuaTable$Slot
        public int arraykey(int max) {
            return this.entry.arraykey(max);
        }

        // 去混淆评级： 低(20)
        @Override  // luaj.LuaTable$Slot
        public StrongSlot find(LuaValue key) {
            return this.entry.keyeq(key) ? this : null;
        }

        @Override  // luaj.LuaTable$Slot
        public StrongSlot first() {
            return this.entry;
        }

        @Override  // luaj.LuaTable$StrongSlot
        public LuaValue key() {
            return this.entry.key();
        }

        @Override  // luaj.LuaTable$Slot
        public boolean keyeq(LuaValue key) {
            return this.entry.keyeq(key);
        }

        @Override  // luaj.LuaTable$Slot
        public int keyindex(int hashMask) {
            return this.entry.keyindex(hashMask);
        }

        @Override  // luaj.LuaTable$Slot
        public Slot relink(Slot rest) {
            return rest != null ? new LinkSlot(this.entry, rest) : this.entry;
        }

        @Override  // luaj.LuaTable$Slot
        public Slot remove(StrongSlot target) {
            if(this == target) {
                return new DeadSlot(this.key(), this.next, null);
            }
            this.next = this.next.remove(target);
            return this;
        }

        @Override  // luaj.LuaTable$Slot
        public Slot rest() {
            return this.next;
        }

        @Override  // luaj.LuaTable$Slot
        public Slot set(StrongSlot target, LuaValue value) {
            if(target == this) {
                this.entry = this.entry.set(value);
                return this;
            }
            return this.setnext(this.next.set(target, value));
        }

        private Slot setnext(Slot next) {
            if(next != null) {
                this.next = next;
                return this;
            }
            return this.entry;
        }

        @Override
        public String toString() {
            return this.entry + "; " + this.next;
        }

        @Override  // luaj.LuaTable$StrongSlot
        public Varargs toVarargs() {
            return this.entry.toVarargs();
        }

        @Override  // luaj.LuaTable$StrongSlot
        public LuaValue value() {
            return this.entry.value();
        }
    }

    static class LongValueEntry extends ValueEntry {
        private long value;

        LongValueEntry(LuaValue key, long value) {
            super(key);
            this.value = value;
        }

        long getValue() {
            return this.value;
        }

        @Override  // luaj.LuaTable$Entry
        public Entry set(LuaValue value) {
            if(value instanceof LuaBoolean) {
                return new BooleanValueEntry(this.key, value.toboolean());
            }
            LuaValue luaValue1 = value.tonumber();
            if(luaValue1.isnil()) {
                return new NormalEntry(this.key, value);
            }
            if(luaValue1 instanceof LuaLong) {
                long v = luaValue1.tolong();
                if(((long)(((int)v))) == v) {
                    return new IntValueEntry(this.key, ((int)v));
                }
                this.value = v;
                return this;
            }
            double f = luaValue1.todouble();
            return ((double)(((float)f))) == f ? new FloatValueEntry(this.key, ((float)f)) : new DoubleValueEntry(this.key, f);
        }

        @Override  // luaj.LuaTable$Entry
        public LuaValue value() {
            return LuaTable.valueOf(this.value);
        }
    }

    static class NormalEntry extends ValueEntry {
        private LuaValue value;

        NormalEntry(LuaValue key, LuaValue value) {
            super(key);
            this.value = value;
        }

        @Override  // luaj.LuaTable$Entry
        public Entry set(LuaValue value) {
            if(value instanceof LuaBoolean) {
                return new BooleanValueEntry(this.key, value.toboolean());
            }
            if(value instanceof LuaLong) {
                long v = value.tolong();
                return ((long)(((int)v))) == v ? new IntValueEntry(this.key, ((int)v)) : new LongValueEntry(this.key, v);
            }
            if(value instanceof LuaDouble) {
                double f = value.todouble();
                return ((double)(((float)f))) == f ? new FloatValueEntry(this.key, ((float)f)) : new DoubleValueEntry(this.key, f);
            }
            this.value = value;
            return this;
        }

        @Override  // luaj.LuaTable$Entry
        public Varargs toVarargs() {
            return this;
        }

        @Override  // luaj.LuaTable$Entry
        public LuaValue value() {
            return this.value;
        }
    }

    interface Slot {
        Slot add(Slot arg1);

        int arraykey(int arg1);

        StrongSlot find(LuaValue arg1);

        StrongSlot first();

        boolean keyeq(LuaValue arg1);

        int keyindex(int arg1);

        Slot relink(Slot arg1);

        Slot remove(StrongSlot arg1);

        Slot rest();

        Slot set(StrongSlot arg1, LuaValue arg2);
    }

    interface StrongSlot extends Slot {
        LuaValue key();

        Varargs toVarargs();

        LuaValue value();
    }

    static abstract class ValueEntry extends Entry {
        protected final LuaValue key;

        ValueEntry(LuaValue key) {
            this.key = key;
        }

        @Override  // luaj.LuaTable$Entry
        public final LuaValue key() {
            return this.key;
        }

        @Override  // luaj.LuaTable$Entry
        public final boolean keyeq(LuaValue key) {
            return key.raweq(this.key);
        }

        @Override  // luaj.LuaTable$Entry
        public final int keyindex(int mask) {
            return LuaTable.hashSlot(this.key, mask);
        }
    }

    public static final int MAX_LEN = 0xFFFFFF;
    private static final int MIN_HASH_CAPACITY = 2;
    private static final LuaString N;
    private static final Slot[] NOBUCKETS;
    private LuaValue[] array;
    private static volatile Set dumped;
    private Slot[] hash;
    protected int hashEntries;
    protected Metatable m_metatable;
    private static volatile int totalCnt;

    static {
        LuaTable.N = LuaTable.valueOf("n");
        LuaTable.NOBUCKETS = new Slot[0];
        LuaTable.dumped = null;
        LuaTable.totalCnt = 0;
    }

    public LuaTable() {
        this.array = LuaTable.NOVALS;
        this.hash = LuaTable.NOBUCKETS;
    }

    public LuaTable(int narray, int nhash) {
        this.presize(narray, nhash);
    }

    public LuaTable(Varargs varargs) {
        this(varargs, 1);
    }

    public LuaTable(Varargs varargs, int firstarg) {
        int v1 = Math.max(varargs.narg() - (firstarg - 1), 0);
        this.presize(v1, 1);
        LuaLong luaLong0 = LuaTable.valueOf(v1);
        this.set(LuaTable.N, luaLong0);
        for(int i = 1; i <= v1; ++i) {
            this.set(i, varargs.arg(i + (firstarg - 1)));
        }
    }

    public LuaTable(LuaValue[] named, LuaValue[] unnamed, Varargs lastarg) {
        int v = 0;
        super();
        int nn = named == null ? 0 : named.length;
        int nu = unnamed == null ? 0 : unnamed.length;
        if(lastarg != null) {
            v = lastarg.narg();
        }
        this.presize(nu + v, nn >> 1);
        for(int i = 0; i < nu; ++i) {
            this.rawset(i + 1, unnamed[i]);
        }
        if(lastarg != null) {
            int v5 = lastarg.narg();
            for(int i = 1; i <= v5; ++i) {
                this.rawset(nu + i, lastarg.arg(i));
            }
        }
        for(int i = 0; i < nn; i += 2) {
            if(!named[i + 1].isnil()) {
                this.rawset(named[i], named[i + 1]);
            }
        }
    }

    @Override  // luaj.Metatable
    public LuaValue arrayget(LuaValue[] array, int index) {
        return array[index];
    }

    private boolean arrayset(int key, LuaValue value) {
        if(key > 0 && key <= this.array.length) {
            LuaValue[] arr_luaValue = this.array;
            if(value.isnil()) {
                value = null;
            }
            else if(this.m_metatable != null) {
                value = this.m_metatable.wrap(value);
            }
            arr_luaValue[key - 1] = value;
            return true;
        }
        return false;
    }

    @Override  // luaj.LuaValue
    public LuaTable checktable() {
        return this;
    }

    private boolean compare(int i, int j, LuaValue cmpfunc) {
        LuaValue luaValue1 = this.get(i);
        LuaValue luaValue2 = this.get(j);
        if(luaValue1 == null || luaValue2 == null) {
            return false;
        }
        return cmpfunc == null ? luaValue1.lt_b(luaValue2) : cmpfunc.call(luaValue1, luaValue2).toboolean();
    }

    public LuaValue concat(LuaString sep, int i, int j) {
        Buffer sb = new Buffer();
        if(i <= j) {
            sb.append(this.get(i).checkstring());
            while(i < 0x7FFFFFFF) {
                ++i;
                if(i > j) {
                    break;
                }
                sb.append(sep);
                sb.append(this.get(i).checkstring());
            }
        }
        return sb.tostring();
    }

    private int countHashKeys() {
        int keys = 0;
        for(int i = 0; i < this.hash.length; ++i) {
            for(Slot slot = this.hash[i]; slot != null; slot = slot.rest()) {
                if(slot.first() != null) {
                    ++keys;
                }
            }
        }
        return keys;
    }

    private int countIntKeys(int[] nums) {
        int total = 0;
        int bit = 0;
        for(int i = 1; bit < 0x1F && i <= this.array.length; i = i) {
            int v3 = Math.min(this.array.length, 1 << bit);
            int c = 0;
            int i;
            for(i = i; i <= v3; ++i) {
                if(this.array[i - 1] != null) {
                    ++c;
                }
            }
            nums[bit] = c;
            total += c;
            ++bit;
        }
        for(int i = 0; i < this.hash.length; ++i) {
            for(Slot s = this.hash[i]; s != null; s = s.rest()) {
                int v7 = s.arraykey(0x7FFFFFFF);
                if(v7 > 0) {
                    int v8 = LuaTable.log2(v7);
                    ++nums[v8];
                    ++total;
                }
            }
        }
        return total;
    }

    protected static Entry defaultEntry(LuaValue key, LuaValue value) {
        if(key.isintnumber()) {
            return new IntKeyEntry(key.toint(), value);
        }
        if(value instanceof LuaBoolean) {
            return new BooleanValueEntry(key, value.toboolean());
        }
        if(value instanceof LuaLong) {
            long v = value.tolong();
            return ((long)(((int)v))) == v ? new IntValueEntry(key, ((int)v)) : new LongValueEntry(key, v);
        }
        if(value instanceof LuaDouble) {
            double f = value.todouble();
            return ((double)(((float)f))) == f ? new FloatValueEntry(key, ((float)f)) : new DoubleValueEntry(key, f);
        }
        return new NormalEntry(key, value);
    }

    private void dropWeakArrayValues() {
        for(int i = 0; i < this.array.length; ++i) {
            this.m_metatable.arrayget(this.array, i);
        }
    }

    private Appendable dump(String level, Appendable out, Map map0) throws IOException {
        Set d = LuaTable.dumped;
        if(d != null) {
            d.add(this);
        }
        out.append("{ -- ");
        out.append(this.typenamemt());
        out.append("(");
        out.append(Integer.toHexString(this.hashCode()));
        out.append(")\n");
        int v = this.length();
        int v1 = LuaTable.totalCnt + v;
        LuaTable.totalCnt = v1;
        List list = null;
        int digits = v == 0 ? 1 : ((int)Math.max(1.0, Math.min(10.0, Math.log10(Math.abs(v)) + 1.0)));
        String nextLevel = null;
        Iterator luaTable$Iterator0 = this.iterator();
        boolean useLevel = level.length() > 0;
        boolean allowLevel = level.length() == 1;
        int intKey = 1;
        boolean useOut = true;
        while(luaTable$Iterator0.next()) {
            LuaValue luaValue0 = luaTable$Iterator0.key();
            if(!allowLevel || map0 == null || map0.get(luaValue0) != null) {
                if(v1 < 10000 && useOut) {
                    if(luaValue0.isintnumber()) {
                        if(luaValue0.toint() == intKey) {
                            ++intKey;
                            goto label_30;
                        }
                        else {
                            ++intKey;
                        }
                    }
                    useOut = false;
                }
            label_30:
                if(!luaValue0.isDeprecated()) {
                    LuaValue luaValue1 = luaTable$Iterator0.value();
                    if(!luaValue1.isDeprecated()) {
                        Appendable val = useOut ? out : new StringBuilder();
                        if(useLevel) {
                            val.append(level);
                        }
                        val.append("\t[");
                        String strKey = null;
                        if(luaValue0.isintnumber()) {
                            ToolsLight.prefix(val, digits, ' ', Integer.toString(luaValue0.toint()));
                        }
                        else if(d == null || !(luaValue0 instanceof LuaTable)) {
                            if(luaValue0 instanceof LuaString) {
                                val.append('\'');
                                strKey = luaValue0.tojstring();
                                if(strKey.indexOf(39) >= 0) {
                                    strKey = strKey.replace("\'", "\\\'");
                                }
                                val.append(strKey);
                                val.append('\'');
                            }
                            else {
                                val.append(luaValue0.tojstring());
                            }
                        }
                        else if(d.contains(((LuaTable)luaValue0))) {
                            val.append("{ -- table(");
                            val.append(Integer.toHexString(((LuaTable)luaValue0).hashCode()));
                            val.append(")\n");
                            if(useLevel) {
                                val.append(level);
                            }
                            val.append("\t\t-- *** RECURSION ***\n");
                            if(useLevel) {
                                val.append(level);
                            }
                            val.append("\t}");
                        }
                        else {
                            if(nextLevel == null) {
                                nextLevel = String.valueOf(level) + '\t';
                            }
                            ((LuaTable)luaValue0).dump(nextLevel, val, map0);
                        }
                        val.append("] = ");
                        String note = null;
                        if(d != null && luaValue1 instanceof LuaTable) {
                            if(d.contains(((LuaTable)luaValue1))) {
                                val.append("{ -- table(");
                                val.append(Integer.toHexString(((LuaTable)luaValue1).hashCode()));
                                val.append(")\n");
                                if(useLevel) {
                                    val.append(level);
                                }
                                val.append("\t\t-- *** RECURSION ***\n");
                                if(useLevel) {
                                    val.append(level);
                                }
                                val.append("\t}");
                            }
                            else {
                                if(nextLevel == null) {
                                    nextLevel = String.valueOf(level) + '\t';
                                }
                                ((LuaTable)luaValue1).dump(nextLevel, val, map0);
                            }
                            val.append(',');
                        }
                        else if(luaValue1 instanceof LuaString) {
                            val.append('\'');
                            String strVal = luaValue1.tojstring();
                            if(strVal.indexOf(39) >= 0) {
                                strVal = strVal.replace("\'", "\\\'");
                            }
                            val.append(strVal);
                            val.append("\',");
                        }
                        else if(luaValue1 instanceof LuaLong) {
                            boolean isStrKey = strKey != null && luaValue0 instanceof LuaString;
                            if(!isStrKey || !strKey.equals("address")) {
                                val.append(luaValue1.tojstring());
                                if(isStrKey) {
                                    if(strKey.equals("flags")) {
                                        switch(luaValue1.toint()) {
                                            case 1: {
                                                note = "gg.TYPE_BYTE";
                                                break;
                                            }
                                            case 2: {
                                                note = "gg.TYPE_WORD";
                                                break;
                                            }
                                            case 4: {
                                                note = "gg.TYPE_DWORD";
                                                break;
                                            }
                                            case 8: {
                                                note = "gg.TYPE_XOR";
                                                break;
                                            }
                                            case 16: {
                                                note = "gg.TYPE_FLOAT";
                                                break;
                                            }
                                            case 0x20: {
                                                note = "gg.TYPE_QWORD";
                                                break;
                                            }
                                            case 0x40: {
                                                note = "gg.TYPE_DOUBLE";
                                            }
                                        }
                                    }
                                    else if(strKey.equals("freezeType")) {
                                        switch(luaValue1.toint()) {
                                            case 0: {
                                                note = "gg.FREEZE_NORMAL";
                                                break;
                                            }
                                            case 1: {
                                                note = "gg.FREEZE_MAY_INCREASE";
                                                break;
                                            }
                                            case 2: {
                                                note = "gg.FREEZE_MAY_DECREASE";
                                                break;
                                            }
                                            case 3: {
                                                note = "gg.FREEZE_IN_RANGE";
                                            }
                                        }
                                    }
                                }
                            }
                            else {
                                val.append("0x");
                                val.append(Long.toHexString(luaValue1.tolong()));
                            }
                            val.append(',');
                        }
                        else {
                            val.append(luaValue1.tojstring());
                            if(!(luaValue1 instanceof ApiFunction)) {
                                val.append(',');
                            }
                        }
                        if(note != null) {
                            val.append(" -- ");
                            val.append(note);
                        }
                        val.append('\n');
                        if(!useOut) {
                            if(list == null) {
                                int capacity = v - (intKey - 1);
                                list = capacity >= 0 ? new ArrayList(capacity) : new ArrayList();
                            }
                            list.add(val.toString());
                        }
                    }
                }
            }
        }
        if(list != null) {
            try {
                Collections.sort(list);
            }
            catch(Throwable e) {
                Log.badImplementation(e);
            }
            for(Object object0: list) {
                out.append(((String)object0));
            }
        }
        if(useLevel) {
            out.append(level);
        }
        out.append('}');
        return out;
    }

    @Override  // luaj.Metatable
    public Slot entry(LuaValue key, LuaValue value) {
        return LuaTable.defaultEntry(key, value);
    }

    @Override  // luaj.LuaValue
    public boolean eq_b(LuaValue val) {
        if(this == val) {
            return true;
        }
        return val.istable() ? LuaTable.eqmtcall(this, val) : false;
    }

    @Override  // luaj.LuaValue
    public LuaValue get(int key) {
        LuaValue v = this.rawget(key);
        return !v.isnil() || this.m_metatable == null ? v : LuaTable.gettable(this, LuaTable.valueOf(key));
    }

    @Override  // luaj.LuaValue
    public LuaValue get(LuaValue key) {
        LuaValue v = this.rawget(key);
        return !v.isnil() || this.m_metatable == null ? v : LuaTable.gettable(this, key);
    }

    public int getArrayLength() {
        return this.array.length;
    }

    public int getHashEntries() {
        return this.hashEntries;
    }

    public int getHashLength() {
        return this.hash.length;
    }

    public long getchecklong(LuaValue key) {
        StrongSlot luaTable$StrongSlot0 = this.slotget(key);
        if(luaTable$StrongSlot0 == null) {
            return this.m_metatable == null ? LuaTable.NIL.checklong() : LuaTable.gettable(this, key).checklong();
        }
        if(luaTable$StrongSlot0 instanceof IntValueEntry) {
            return (long)((IntValueEntry)luaTable$StrongSlot0).getValue();
        }
        return luaTable$StrongSlot0 instanceof LongValueEntry ? ((LongValueEntry)luaTable$StrongSlot0).getValue() : luaTable$StrongSlot0.value().checklong();
    }

    @Override  // luaj.LuaValue
    public LuaValue getmetatable() {
        return this.m_metatable == null ? null : this.m_metatable.toLuaValue();
    }

    private void hashRemove(LuaValue key) {
        if(this.hash.length > 0) {
            int v = this.hashSlot(key);
            for(Slot slot = this.hash[v]; slot != null; slot = slot.rest()) {
                StrongSlot luaTable$StrongSlot0 = slot.find(key);
                if(luaTable$StrongSlot0 != null) {
                    Slot[] arr_luaTable$Slot = this.hash;
                    arr_luaTable$Slot[v] = this.hash[v].remove(luaTable$StrongSlot0);
                    --this.hashEntries;
                    return;
                }
            }
        }
    }

    private int hashSlot(LuaValue key) {
        return key.hashCode() & this.hash.length - 1;
    }

    public static int hashSlot(LuaValue key, int hashMask) {
        return key.hashCode() & hashMask;
    }

    protected LuaValue hashget(LuaValue key) {
        StrongSlot luaTable$StrongSlot0 = this.slotget(key);
        return luaTable$StrongSlot0 == null ? LuaTable.NIL : luaTable$StrongSlot0.value();
    }

    public static int hashmod(int hashCode, int mask) [...] // Inlined contents

    public void hashset(LuaValue key, LuaValue value) {
        int index = 0;
        if(value.isnil()) {
            this.hashRemove(key);
            return;
        }
        if(this.hash.length > 0) {
            index = this.hashSlot(key);
            for(Slot slot = this.hash[index]; slot != null; slot = slot.rest()) {
                StrongSlot luaTable$StrongSlot0 = slot.find(key);
                if(luaTable$StrongSlot0 != null) {
                    Slot[] arr_luaTable$Slot = this.hash;
                    arr_luaTable$Slot[index] = this.hash[index].set(luaTable$StrongSlot0, value);
                    return;
                }
            }
        }
        if(this.hashEntries >= this.hash.length) {
            if(this.m_metatable != null && this.m_metatable.useWeakValues() || !key.isintnumber() || key.toint() <= 0) {
                this.rehash(-1);
            }
            else {
                this.rehash(key.toint());
                if(this.arrayset(key.toint(), value)) {
                    return;
                }
            }
            index = this.hashSlot(key);
        }
        Slot entry = this.m_metatable == null ? LuaTable.defaultEntry(key, value) : this.m_metatable.entry(key, value);
        Slot[] arr_luaTable$Slot1 = this.hash;
        if(this.hash[index] != null) {
            entry = this.hash[index].add(entry);
        }
        arr_luaTable$Slot1[index] = entry;
        ++this.hashEntries;
    }

    private void heapSort(int count, LuaValue cmpfunc) {
        this.heapify(count, cmpfunc);
        int end = count;
        while(end > 1) {
            LuaValue luaValue1 = this.get(end);
            this.set(end, this.get(1));
            this.set(1, luaValue1);
            --end;
            this.siftDown(1, end, cmpfunc);
        }
    }

    private void heapify(int count, LuaValue cmpfunc) {
        for(int start = count / 2; start > 0; --start) {
            this.siftDown(start, count, cmpfunc);
        }
    }

    @Override  // luaj.LuaValue
    public Varargs inext(LuaValue key) {
        int v = key.checkint();
        LuaValue luaValue1 = this.get(v + 1);
        return luaValue1.isnil() ? LuaTable.NONE : LuaTable.varargsOf(LuaLong.valueOf(v + 1), luaValue1);
    }

    public void insert(int pos, LuaValue value) {
        while(!value.isnil()) {
            LuaValue luaValue1 = this.get(pos);
            this.set(pos, value);
            value = luaValue1;
            ++pos;
        }
    }

    protected static boolean isLargeKey(LuaValue key) {
        switch(key.type()) {
            case 1: 
            case 3: {
                return false;
            }
            case 4: {
                return key.rawlen() > 0x20;
            }
            default: {
                return true;
            }
        }
    }

    @Override  // luaj.LuaValue
    public boolean istable() {
        return true;
    }

    public Iterator iterator() {
        return new Iterator(this);
    }

    public LuaValue[] keys() {
        ArrayList l = new ArrayList();
        Iterator luaTable$Iterator0 = this.iterator();
        while(luaTable$Iterator0.next()) {
            l.add(luaTable$Iterator0.key());
        }
        LuaValue[] a = new LuaValue[l.size()];
        l.toArray(a);
        return a;
    }

    @Override  // luaj.LuaValue
    public LuaValue len() {
        LuaValue luaValue0 = this.metatag(LuaTable.LEN);
        return luaValue0.toboolean() ? luaValue0.call(this) : LuaLong.valueOf(this.rawlen());
    }

    @Override  // luaj.LuaValue
    public int length() {
        if(this.m_metatable != null) {
            LuaValue luaValue0 = this.len();
            if(!luaValue0.isintnumber()) {
                throw new LuaError("table length is not an integer: " + luaValue0);
            }
            return luaValue0.toint();
        }
        return this.rawlen();
    }

    static int log2(int x) {
        int lg = 0;
        int v2 = x - 1;
        if(v2 < 0) {
            return 0x80000000;
        }
        if((0xFFFF0000 & v2) != 0) {
            lg = 16;
            v2 >>>= 16;
        }
        if((0xFF00 & v2) != 0) {
            lg += 8;
            v2 >>>= 8;
        }
        if((v2 & 0xF0) != 0) {
            lg += 4;
            v2 >>>= 4;
        }
        switch(v2) {
            case 0: {
                return 0;
            }
            case 1: {
                return lg + 1;
            }
            case 2: 
            case 3: {
                return lg + 2;
            }
            case 4: 
            case 5: 
            case 6: 
            case 7: {
                return lg + 3;
            }
            case 8: 
            case 9: 
            case 10: 
            case 11: 
            case 12: 
            case 13: 
            case 14: 
            case 15: {
                return lg + 4;
            }
            default: {
                return lg;
            }
        }
    }

    @Override  // luaj.LuaValue
    public Varargs next(LuaValue key) {
        Slot slot;
        int i = 0;
        if(!key.isnil()) {
            if(key.isintnumber()) {
                i = key.toint();
                if(i <= 0 || i > this.array.length) {
                    goto label_5;
                }
            }
            else {
            label_5:
                if(this.hash.length == 0) {
                    LuaTable.error(("invalid key to \'next\' 1: " + key));
                }
                int i = this.hashSlot(key);
                boolean found = false;
                for(Slot slot = this.hash[i]; true; slot = slot.rest()) {
                    if(slot == null) {
                        if(!found) {
                            LuaTable.error(("invalid key to \'next\' 2: " + key));
                        }
                        i = i + (this.array.length + 1);
                        break;
                    }
                    if(found) {
                        StrongSlot luaTable$StrongSlot0 = slot.first();
                        if(luaTable$StrongSlot0 != null) {
                            return luaTable$StrongSlot0.toVarargs();
                        }
                    }
                    else if(slot.keyeq(key)) {
                        found = true;
                    }
                }
            }
        }
        while(true) {
            if(i >= this.array.length) {
                for(int i = i - this.array.length; true; ++i) {
                    if(i >= this.hash.length) {
                        return LuaTable.NIL;
                    }
                    slot = this.hash[i];
                label_28:
                    if(slot != null) {
                        break;
                    }
                }
                StrongSlot luaTable$StrongSlot1 = slot.first();
                if(luaTable$StrongSlot1 == null) {
                    slot = slot.rest();
                    goto label_28;
                }
                return luaTable$StrongSlot1.toVarargs();
            }
            if(this.array[i] != null) {
                LuaValue value = this.m_metatable == null ? this.array[i] : this.m_metatable.arrayget(this.array, i);
                if(value != null) {
                    return LuaTable.varargsOf(LuaLong.valueOf(i + 1), value);
                }
            }
            ++i;
        }
    }

    @Override  // luaj.LuaValue
    public LuaTable opttable(LuaTable defval) {
        return this;
    }

    @Override  // luaj.LuaValue
    public void presize(int narray) {
        if(narray > this.array.length) {
            this.array = LuaTable.resize(this.array, 1 << LuaTable.log2(narray));
        }
    }

    public void presize(int narray, int nhash) {
        if(nhash > 0 && nhash < 2) {
            nhash = 2;
        }
        this.array = narray <= 0 ? LuaTable.NOVALS : new LuaValue[1 << LuaTable.log2(narray)];
        this.hash = nhash <= 0 ? LuaTable.NOBUCKETS : new Slot[1 << LuaTable.log2(nhash)];
        this.hashEntries = 0;
    }

    @Override  // luaj.LuaValue
    public LuaValue rawget(int key) {
        LuaValue v;
        if(key > 0 && key <= this.array.length) {
            if(this.m_metatable == null) {
                v = this.array[key - 1];
                return v == null ? LuaTable.NIL : v;
            }
            v = this.m_metatable.arrayget(this.array, key - 1);
            return v == null ? LuaTable.NIL : v;
        }
        return this.hashget(LuaLong.valueOf(key));
    }

    @Override  // luaj.LuaValue
    public LuaValue rawget(LuaValue key) {
        LuaValue v;
        if(key.isintnumber()) {
            int v = key.toint();
            if(v > 0 && v <= this.array.length) {
                if(this.m_metatable == null) {
                    v = this.array[v - 1];
                    return v == null ? LuaTable.NIL : v;
                }
                v = this.m_metatable.arrayget(this.array, v - 1);
                return v == null ? LuaTable.NIL : v;
            }
        }
        return this.hashget(key);
    }

    @Override  // luaj.LuaValue
    public int rawlen() {
        int v = this.getArrayLength();
        int n = v;
        int m = 0;
        if(n == 0 || !this.rawget(n).isnil()) {
            m = n;
            ++n;
            while(!this.rawget(n).isnil()) {
                m = n;
                n += this.getHashLength() + v + 1;
            }
        }
        while(n > m + 1) {
            int k = (m + n) / 2;
            if(this.rawget(k).isnil()) {
                n = k;
            }
            else {
                m = k;
            }
        }
        return m;
    }

    @Override  // luaj.LuaValue
    public void rawset(int key, LuaValue value) {
        if(value != null && !this.arrayset(key, value)) {
            this.hashset(LuaLong.valueOf(key), value);
        }
    }

    @Override  // luaj.LuaValue
    public void rawset(LuaValue key, LuaValue value) {
        if(key != null && value != null && (!key.isintnumber() || !this.arrayset(key.toint(), value))) {
            this.hashset(key, value);
        }
    }

    private void rehash(int newKey) {
        Slot newEntry;
        Slot[] newHash;
        int newHashMask;
        LuaValue[] newArray;
        if(this.m_metatable != null && (this.m_metatable.useWeakKeys() || this.m_metatable.useWeakValues())) {
            this.hashEntries = this.countHashKeys();
            if(this.m_metatable.useWeakValues()) {
                this.dropWeakArrayValues();
            }
        }
        int[] nums = new int[0x20];
        int total = this.countIntKeys(nums);
        if(newKey > 0) {
            ++total;
            int v2 = LuaTable.log2(newKey);
            ++nums[v2];
        }
        int keys = nums[0];
        int newArraySize = 0;
        for(int log = 1; log < 0x20; ++log) {
            keys += nums[log];
            if(total * 2 < 1 << log) {
                break;
            }
            if(keys >= 1 << log - 1) {
                newArraySize = 1 << log;
            }
        }
        LuaValue[] oldArray = this.array;
        Slot[] oldHash = this.hash;
        int movingToArray = newKey <= 0 || newKey > newArraySize ? 0 : -1;
        if(newArraySize == oldArray.length) {
            newArray = this.array;
        }
        else {
            newArray = new LuaValue[newArraySize];
            if(newArraySize > oldArray.length) {
                int i = LuaTable.log2(oldArray.length + 1);
                int v8 = LuaTable.log2(newArraySize);
                while(i < v8 + 1) {
                    movingToArray += nums[i];
                    ++i;
                }
            }
            else if(oldArray.length > newArraySize) {
                int i = LuaTable.log2(newArraySize + 1);
                int v10 = LuaTable.log2(oldArray.length);
                while(i < v10 + 1) {
                    movingToArray -= nums[i];
                    ++i;
                }
            }
            System.arraycopy(oldArray, 0, newArray, 0, Math.min(oldArray.length, newArraySize));
        }
        int newHashSize = this.hashEntries - movingToArray + (newKey >= 0 && newKey <= newArraySize ? 0 : 1);
        if(newHashSize > 0) {
            int newCapacity = newHashSize >= 2 ? 1 << LuaTable.log2(newHashSize) : 2;
            newHashMask = newCapacity - 1;
            newHash = new Slot[newCapacity];
        }
        else {
            newHashMask = 0;
            newHash = LuaTable.NOBUCKETS;
        }
        for(int i = 0; i < oldHash.length; ++i) {
            for(Slot slot = oldHash[i]; slot != null; slot = slot.rest()) {
                int v15 = slot.arraykey(newArraySize);
                if(v15 > 0) {
                    StrongSlot luaTable$StrongSlot0 = slot.first();
                    if(luaTable$StrongSlot0 != null) {
                        newArray[v15 - 1] = luaTable$StrongSlot0.value();
                    }
                }
                else if(!(slot instanceof DeadSlot)) {
                    int v16 = slot.keyindex(newHashMask);
                    newHash[v16] = slot.relink(newHash[v16]);
                }
            }
        }
        int i = newArraySize;
        while(i < oldArray.length) {
            LuaValue v = oldArray[i];
            if(v != null) {
                int v18 = ((int)(((long)(i + 1)) >>> 0x20 ^ ((long)(i + 1)))) & newHashMask;
                if(this.m_metatable == null) {
                label_80:
                    newEntry = LuaTable.defaultEntry(LuaTable.valueOf(i + 1), v);
                }
                else {
                    Slot luaTable$Slot1 = this.m_metatable.entry(LuaTable.valueOf(i + 1), v);
                    if(luaTable$Slot1 == null) {
                        ++i;
                        continue;
                    }
                    else {
                        newEntry = luaTable$Slot1;
                        goto label_81;
                    }
                    goto label_80;
                }
            label_81:
                if(newHash[v18] != null) {
                    newEntry = newHash[v18].add(newEntry);
                }
                newHash[v18] = newEntry;
            }
            ++i;
        }
        this.hash = newHash;
        this.array = newArray;
        this.hashEntries -= movingToArray;
    }

    public LuaValue remove(int pos, int n) {
        if(pos == 0) {
            pos = n;
        }
        else if(pos > n) {
            return LuaTable.NONE;
        }
        LuaValue luaValue0 = this.get(pos);
        LuaValue r = luaValue0;
        while(!r.isnil()) {
            r = this.get(pos + 1);
            this.set(pos, r);
            ++pos;
        }
        return luaValue0.isnil() ? LuaTable.NONE : luaValue0;
    }

    private static LuaValue[] resize(LuaValue[] old, int n) {
        LuaValue[] v = new LuaValue[n];
        System.arraycopy(old, 0, v, 0, old.length);
        return v;
    }

    @Override  // luaj.LuaValue
    public void set(int key, LuaValue value) {
        if(this.m_metatable == null || !this.rawget(key).isnil() || !LuaTable.settable(this, LuaLong.valueOf(key), value)) {
            this.rawset(key, value);
        }
    }

    @Override  // luaj.LuaValue
    public void set(LuaValue key, LuaValue value) {
        if(key == null || !key.isvalidkey() && !this.metatag(LuaTable.NEWINDEX).isfunction()) {
            throw new LuaError("value (\'" + key + "\') can not be used as a table index");
        }
        if(this.m_metatable == null || !this.rawget(key).isnil() || !LuaTable.settable(this, key, value)) {
            this.rawset(key, value);
        }
    }

    @Override  // luaj.LuaValue
    public LuaValue setmetatable(LuaValue metatable) {
        int v = 1;
        int hadWeakKeys = this.m_metatable == null || !this.m_metatable.useWeakKeys() ? 0 : 1;
        int hadWeakValues = this.m_metatable == null || !this.m_metatable.useWeakValues() ? 0 : 1;
        this.m_metatable = LuaTable.metatableOf(metatable);
        if(hadWeakKeys == (this.m_metatable == null || !this.m_metatable.useWeakKeys() ? 0 : 1)) {
            if(this.m_metatable == null || !this.m_metatable.useWeakValues()) {
                v = 0;
            }
            if(hadWeakValues != v) {
                this.rehash(0);
                return this;
            }
        }
        else {
            this.rehash(0);
        }
        return this;
    }

    private void siftDown(int start, int end, LuaValue cmpfunc) {
        while(start * 2 <= end) {
            int child = start * 2;
            child = child >= end || !this.compare(child, child + 1, cmpfunc) ? start * 2 : child + 1;
            if(!this.compare(start, child, cmpfunc)) {
                break;
            }
            LuaValue luaValue1 = this.get(start);
            this.set(start, this.get(child));
            this.set(child, luaValue1);
            start = child;
        }
    }

    private StrongSlot slotget(LuaValue key) {
        if(this.hashEntries > 0) {
            Slot slot = this.hash[this.hashSlot(key)];
            while(slot != null) {
                StrongSlot luaTable$StrongSlot0 = slot.find(key);
                if(luaTable$StrongSlot0 == null) {
                    slot = slot.rest();
                    continue;
                }
                return luaTable$StrongSlot0;
            }
        }
        return null;
    }

    public void sort(LuaValue comparator) {
        int v = this.length();
        if(v >= 0xFFFFFF) {
            throw new LuaError("array too big: " + v + " >= " + 0xFFFFFF);
        }
        if(this.m_metatable != null && this.m_metatable.useWeakValues()) {
            this.dropWeakArrayValues();
        }
        if(v > 1) {
            if(comparator.isnil()) {
                comparator = null;
            }
            this.heapSort(v, comparator);
        }
    }

    public LuaValue testIterator() {
        return LuaTable.NIL;
    }

    @Override  // luaj.Metatable
    public LuaValue toLuaValue() {
        return this;
    }

    // 去混淆评级： 低(20)
    @Override  // luaj.LuaValue
    public String tojstring() {
        return "{ -- table(10fe6be1)\n}";
    }

    public String tojstring(Appendable out, Map map0) throws IOException [...] // 潜在的解密器

    @Override  // luaj.LuaValue
    public int type() {
        return 5;
    }

    @Override  // luaj.LuaValue
    public String typename() {
        return "table";
    }

    public Varargs unpack(long i, long j) {
        if(j < i) {
            return LuaTable.NONE;
        }
        long count = j - i;
        if(count < 0L) {
            throw new LuaError("too many results to unpack: greater 9223372036854775807");
        }
        if(count >= 0xFFFFFFL) {
            throw new LuaError("too many results to unpack: " + count + " (max is " + 0xFFFFFFL + ')');
        }
        int n = (int)(count + 1L);
        boolean fitInt = ((long)(((int)i))) == i && ((long)(((int)j))) == j;
        if(n <= 0) {
            return LuaTable.NONE;
        }
        switch(n) {
            case 1: {
                return fitInt ? this.get(((int)i)) : this.get(LuaLong.valueOf(i));
            }
            case 2: {
                return fitInt ? LuaTable.varargsOf(this.get(((int)i)), this.get(((int)(i + 1L)))) : LuaTable.varargsOf(this.get(LuaLong.valueOf(i)), this.get(LuaLong.valueOf(i + 1L)));
            }
            default: {
                try {
                    LuaValue[] v = new LuaValue[n];
                    while(true) {
                        --n;
                        if(n < 0) {
                            return LuaTable.varargsOf(v);
                        }
                        v[n] = fitInt ? this.get(((int)(((long)n) + i))) : this.get(LuaLong.valueOf(((long)n) + i));
                    }
                }
                catch(OutOfMemoryError e) {
                    Log.e(("OOM on unpack: " + n), e);
                    throw new LuaError("too many results to unpack [out of memory]: " + n);
                }
            }
        }
    }

    public void unsafehashset(int index, LuaString key, double value) {
        Slot[] hash = this.hash;
        Slot entry = ((double)(((float)value))) == value ? new FloatValueEntry(key, ((float)value)) : new DoubleValueEntry(key, value);
        if(hash[index] != null) {
            entry = hash[index].add(entry);
        }
        hash[index] = entry;
        ++this.hashEntries;
    }

    public void unsafehashset(int index, LuaString key, float value) {
        Slot[] hash = this.hash;
        Slot entry = new FloatValueEntry(key, value);
        if(hash[index] != null) {
            entry = hash[index].add(entry);
        }
        hash[index] = entry;
        ++this.hashEntries;
    }

    public void unsafehashset(int index, LuaString key, int value) {
        Slot[] hash = this.hash;
        Slot entry = new IntValueEntry(key, value);
        if(hash[index] != null) {
            entry = hash[index].add(entry);
        }
        hash[index] = entry;
        ++this.hashEntries;
    }

    public void unsafehashset(int index, LuaString key, long value) {
        Slot[] hash = this.hash;
        Slot entry = ((long)(((int)value))) == value ? new IntValueEntry(key, ((int)value)) : new LongValueEntry(key, value);
        if(hash[index] != null) {
            entry = hash[index].add(entry);
        }
        hash[index] = entry;
        ++this.hashEntries;
    }

    public void unsafehashset(int index, LuaString key, boolean value) {
        Slot[] hash = this.hash;
        Slot entry = new BooleanValueEntry(key, value);
        if(hash[index] != null) {
            entry = hash[index].add(entry);
        }
        hash[index] = entry;
        ++this.hashEntries;
    }

    @Override  // luaj.Metatable
    public boolean useWeakKeys() {
        return false;
    }

    @Override  // luaj.Metatable
    public boolean useWeakValues() {
        return false;
    }

    @Override  // luaj.Metatable
    public LuaValue wrap(LuaValue value) {
        return value;
    }
}

