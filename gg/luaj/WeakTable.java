package luaj;

import java.lang.ref.WeakReference;

public class WeakTable implements Metatable {
    static class WeakKeyAndValueSlot extends WeakSlot {
        private final int keyhash;

        protected WeakKeyAndValueSlot(LuaValue key, LuaValue value, Slot next) {
            super(WeakTable.weaken(key), WeakTable.weaken(value), next);
            this.keyhash = key.hashCode();
        }

        protected WeakKeyAndValueSlot(WeakKeyAndValueSlot copyFrom, Slot next) {
            super(copyFrom.key, copyFrom.value, next);
            this.keyhash = copyFrom.keyhash;
        }

        @Override  // luaj.WeakTable$WeakSlot
        protected WeakSlot copy(Slot next) {
            return new WeakKeyAndValueSlot(this, next);
        }

        @Override  // luaj.WeakTable$WeakSlot
        public int keyindex(int hashMask) {
            return this.keyhash & hashMask;
        }

        @Override  // luaj.WeakTable$WeakSlot
        public Slot set(LuaValue value) {
            this.value = WeakTable.weaken(value);
            return this;
        }

        @Override  // luaj.WeakTable$WeakSlot
        public LuaValue strongkey() {
            return WeakTable.strengthen(this.key);
        }

        @Override  // luaj.WeakTable$WeakSlot
        public LuaValue strongvalue() {
            return WeakTable.strengthen(this.value);
        }
    }

    static class WeakKeySlot extends WeakSlot {
        private final int keyhash;

        protected WeakKeySlot(LuaValue key, LuaValue value, Slot next) {
            super(WeakTable.weaken(key), value, next);
            this.keyhash = key.hashCode();
        }

        protected WeakKeySlot(WeakKeySlot copyFrom, Slot next) {
            super(copyFrom.key, copyFrom.value, next);
            this.keyhash = copyFrom.keyhash;
        }

        @Override  // luaj.WeakTable$WeakSlot
        protected WeakSlot copy(Slot rest) {
            return new WeakKeySlot(this, rest);
        }

        @Override  // luaj.WeakTable$WeakSlot
        public int keyindex(int mask) {
            return this.keyhash & mask;
        }

        @Override  // luaj.WeakTable$WeakSlot
        public Slot set(LuaValue value) {
            this.value = value;
            return this;
        }

        @Override  // luaj.WeakTable$WeakSlot
        public LuaValue strongkey() {
            return WeakTable.strengthen(this.key);
        }
    }

    public static abstract class WeakSlot implements Slot {
        protected Object key;
        protected Slot next;
        protected Object value;

        protected WeakSlot(Object key, Object value, Slot next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

        @Override  // luaj.LuaTable$Slot
        public Slot add(Slot entry) {
            if(this.next != null) {
                entry = this.next.add(entry);
            }
            this.next = entry;
            return this.strongkey() != null && this.strongvalue() != null ? this : this.next;
        }

        @Override  // luaj.LuaTable$Slot
        public int arraykey(int max) {
            return 0;
        }

        protected abstract WeakSlot copy(Slot arg1);

        @Override  // luaj.LuaTable$Slot
        public StrongSlot find(LuaValue key) {
            StrongSlot luaTable$StrongSlot0 = this.first();
            return luaTable$StrongSlot0 == null ? null : luaTable$StrongSlot0.find(key);
        }

        @Override  // luaj.LuaTable$Slot
        public StrongSlot first() {
            LuaValue luaValue0 = this.strongkey();
            LuaValue luaValue1 = this.strongvalue();
            if(luaValue0 != null && luaValue1 != null) {
                return new NormalEntry(luaValue0, luaValue1);
            }
            this.key = null;
            this.value = null;
            return null;
        }

        @Override  // luaj.LuaTable$Slot
        public boolean keyeq(LuaValue key) {
            StrongSlot luaTable$StrongSlot0 = this.first();
            return luaTable$StrongSlot0 != null && luaTable$StrongSlot0.keyeq(key);
        }

        @Override  // luaj.LuaTable$Slot
        public abstract int keyindex(int arg1);

        @Override  // luaj.LuaTable$Slot
        public Slot relink(Slot rest) {
            if(this.strongkey() != null && this.strongvalue() != null) {
                return rest != null || this.next != null ? this.copy(rest) : this;
            }
            return rest;
        }

        @Override  // luaj.LuaTable$Slot
        public Slot remove(StrongSlot target) {
            LuaValue luaValue0 = this.strongkey();
            if(luaValue0 == null) {
                return this.next.remove(target);
            }
            if(target.keyeq(luaValue0)) {
                this.value = null;
                return this;
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
            LuaValue luaValue1 = this.strongkey();
            if(luaValue1 != null && target.find(luaValue1) != null) {
                return this.set(value);
            }
            if(luaValue1 != null) {
                this.next = this.next.set(target, value);
                return this;
            }
            return this.next.set(target, value);
        }

        public abstract Slot set(LuaValue arg1);

        public LuaValue strongkey() {
            return (LuaValue)this.key;
        }

        public LuaValue strongvalue() {
            return (LuaValue)this.value;
        }
    }

    static final class WeakUserdata extends WeakValue {
        private final LuaValue mt;
        private final WeakReference ob;

        private WeakUserdata(LuaValue luaValue0) {
            super(luaValue0);
            this.ob = new WeakReference(luaValue0.touserdata());
            this.mt = luaValue0.getmetatable();
        }

        WeakUserdata(LuaValue luaValue0, WeakTable.WeakUserdata-IA weakTable$WeakUserdata-IA0) {
            this(luaValue0);
        }

        @Override  // luaj.WeakTable$WeakValue
        public LuaValue strongvalue() {
            Object object0 = this.ref.get();
            if(object0 != null) {
                return (LuaValue)object0;
            }
            Object object1 = this.ob.get();
            if(object1 != null) {
                LuaValue luaValue0 = LuaValue.userdataOf(object1, this.mt);
                this.ref = new WeakReference(luaValue0);
                return luaValue0;
            }
            return null;
        }
    }

    static class WeakValue extends LuaValue {
        WeakReference ref;

        protected WeakValue(LuaValue value) {
            this.ref = new WeakReference(value);
        }

        @Override  // luaj.LuaValue
        public boolean raweq(LuaValue rhs) {
            Object object0 = this.ref.get();
            return object0 != null && rhs.raweq(((LuaValue)object0));
        }

        @Override  // luaj.LuaValue
        public LuaValue strongvalue() {
            return (LuaValue)this.ref.get();
        }

        @Override  // luaj.LuaValue
        public String toString() {
            return "weak<" + this.ref.get() + '>';
        }

        @Override  // luaj.LuaValue
        public int type() {
            this.illegal("type", "weak value");
            return 0;
        }

        @Override  // luaj.LuaValue
        public String typename() {
            this.illegal("typename", "weak value");
            return null;
        }
    }

    static class WeakValueSlot extends WeakSlot {
        protected WeakValueSlot(LuaValue key, LuaValue value, Slot next) {
            super(key, WeakTable.weaken(value), next);
        }

        protected WeakValueSlot(WeakValueSlot copyFrom, Slot next) {
            super(copyFrom.key, copyFrom.value, next);
        }

        @Override  // luaj.WeakTable$WeakSlot
        protected WeakSlot copy(Slot next) {
            return new WeakValueSlot(this, next);
        }

        @Override  // luaj.WeakTable$WeakSlot
        public int keyindex(int mask) {
            return LuaTable.hashSlot(this.strongkey(), mask);
        }

        @Override  // luaj.WeakTable$WeakSlot
        public Slot set(LuaValue value) {
            this.value = WeakTable.weaken(value);
            return this;
        }

        @Override  // luaj.WeakTable$WeakSlot
        public LuaValue strongvalue() {
            return WeakTable.strengthen(this.value);
        }
    }

    private final LuaValue backing;
    private final boolean weakkeys;
    private final boolean weakvalues;

    public WeakTable(boolean weakkeys, boolean weakvalues, LuaValue backing) {
        this.weakkeys = weakkeys;
        this.weakvalues = weakvalues;
        this.backing = backing;
    }

    @Override  // luaj.Metatable
    public LuaValue arrayget(LuaValue[] array, int index) {
        LuaValue value = array[index];
        if(value != null) {
            value = WeakTable.strengthen(value);
            if(value == null) {
                array[index] = null;
            }
        }
        return value;
    }

    @Override  // luaj.Metatable
    public Slot entry(LuaValue key, LuaValue value) {
        LuaValue luaValue2 = value.strongvalue();
        if(luaValue2 == null) {
            return null;
        }
        if(this.weakkeys && !key.isnumber() && !key.isstring() && !key.isboolean()) {
            return this.weakvalues && !luaValue2.isnumber() && !luaValue2.isstring() && !luaValue2.isboolean() ? new WeakKeyAndValueSlot(key, luaValue2, null) : new WeakKeySlot(key, luaValue2, null);
        }
        return this.weakvalues && !luaValue2.isnumber() && !luaValue2.isstring() && !luaValue2.isboolean() ? new WeakValueSlot(key, luaValue2, null) : LuaTable.defaultEntry(key, luaValue2);
    }

    public static LuaTable make(boolean weakkeys, boolean weakvalues) {
        LuaString luaString0;
        if(weakkeys && weakvalues) {
            luaString0 = LuaString.valueOf("kv");
        }
        else if(weakkeys) {
            luaString0 = LuaString.valueOf("k");
        }
        else if(weakvalues) {
            luaString0 = LuaString.valueOf("v");
        }
        else {
            return LuaTable.tableOf();
        }
        LuaTable luaTable0 = LuaTable.tableOf();
        luaTable0.setmetatable(LuaTable.tableOf(new LuaValue[]{LuaValue.MODE, luaString0}));
        return luaTable0;
    }

    protected static LuaValue strengthen(Object ref) {
        if(ref instanceof WeakReference) {
            ref = ((WeakReference)ref).get();
        }
        return ref instanceof WeakValue ? ((WeakValue)ref).strongvalue() : ((LuaValue)ref);
    }

    @Override  // luaj.Metatable
    public LuaValue toLuaValue() {
        return this.backing;
    }

    @Override  // luaj.Metatable
    public boolean useWeakKeys() {
        return this.weakkeys;
    }

    @Override  // luaj.Metatable
    public boolean useWeakValues() {
        return this.weakvalues;
    }

    protected static LuaValue weaken(LuaValue luaValue0) {
        switch(luaValue0.type()) {
            case 7: {
                return new WeakUserdata(luaValue0, null);
            }
            case 5: 
            case 6: 
            case 8: {
                return new WeakValue(luaValue0);
            }
            default: {
                return luaValue0;
            }
        }
    }

    // 去混淆评级： 低(20)
    @Override  // luaj.Metatable
    public LuaValue wrap(LuaValue value) {
        return this.weakvalues ? WeakTable.weaken(value) : value;
    }
}

