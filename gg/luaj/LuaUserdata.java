package luaj;

public class LuaUserdata extends LuaValue {
    public Object m_instance;
    public LuaValue m_metatable;

    public LuaUserdata(Object object0) {
        this.m_instance = object0;
    }

    public LuaUserdata(Object object0, LuaValue luaValue0) {
        this.m_instance = object0;
        this.m_metatable = luaValue0;
    }

    @Override  // luaj.LuaValue
    public Object checkuserdata() {
        return this.m_instance;
    }

    @Override  // luaj.LuaValue
    public Object checkuserdata(Class class0) {
        return class0.isAssignableFrom(this.m_instance.getClass()) ? this.m_instance : this.typerror(class0.getName());
    }

    // 去混淆评级： 低(20)
    @Override  // luaj.LuaValue
    public LuaValue eq(LuaValue luaValue0) {
        return this.eq_b(luaValue0) ? LuaValue.TRUE : LuaValue.FALSE;
    }

    @Override  // luaj.LuaValue
    public boolean eq_b(LuaValue luaValue0) {
        if(luaValue0.raweq(this)) {
            return true;
        }
        if(this.m_metatable != null && luaValue0.isuserdata()) {
            LuaValue luaValue1 = luaValue0.getmetatable();
            return luaValue1 != null && LuaValue.eqmtcall(this, this.m_metatable, luaValue0, luaValue1);
        }
        return false;
    }

    public boolean eqmt(LuaValue luaValue0) {
        return this.m_metatable == null || !luaValue0.isuserdata() ? false : LuaValue.eqmtcall(this, this.m_metatable, luaValue0, luaValue0.getmetatable());
    }

    @Override  // luaj.LuaValue
    public boolean equals(Object object0) {
        if(this == object0) {
            return true;
        }
        return object0 instanceof LuaUserdata ? this.m_instance.equals(((LuaUserdata)object0).m_instance) : false;
    }

    @Override  // luaj.LuaValue
    public LuaValue get(LuaValue luaValue0) {
        return this.m_metatable == null ? LuaValue.NIL : LuaValue.gettable(this, luaValue0);
    }

    public Object getObject(LuaValue luaValue0) {
        if(luaValue0.isnil()) {
            return null;
        }
        if(luaValue0.isnumber()) {
            if(luaValue0.isint()) {
                return luaValue0.checkint();
            }
            return luaValue0.islong() ? luaValue0.checklong() : luaValue0.checkdouble();
        }
        if(luaValue0.isboolean()) {
            return Boolean.valueOf(luaValue0.checkboolean());
        }
        if(luaValue0.istable()) {
            return luaValue0.checktable();
        }
        return luaValue0.isstring() ? luaValue0.checkjstring() : luaValue0;
    }

    @Override  // luaj.LuaValue
    public LuaValue getmetatable() {
        return this.m_metatable;
    }

    @Override
    public int hashCode() {
        return this.m_instance.hashCode();
    }

    @Override  // luaj.LuaValue
    public boolean isnil() {
        return this.m_instance == null;
    }

    @Override  // luaj.LuaValue
    public boolean isuserdata() {
        return true;
    }

    @Override  // luaj.LuaValue
    public boolean isuserdata(Class class0) {
        return class0.isAssignableFrom(this.m_instance.getClass());
    }

    @Override  // luaj.LuaValue
    public LuaValue not() {
        return this.m_instance == null ? LuaUserdata.TRUE : LuaUserdata.FALSE;
    }

    @Override  // luaj.LuaValue
    public Object optuserdata(Class class0, Object object0) {
        if(!class0.isAssignableFrom(this.m_instance.getClass())) {
            this.typerror(class0.getName());
        }
        return this.m_instance;
    }

    @Override  // luaj.LuaValue
    public Object optuserdata(Object object0) {
        return this.m_instance;
    }

    // 去混淆评级： 低(20)
    @Override  // luaj.LuaValue
    public boolean raweq(LuaUserdata luaUserdata0) {
        return this == luaUserdata0 || this.m_metatable == luaUserdata0.m_metatable && this.m_instance.equals(luaUserdata0.m_instance);
    }

    @Override  // luaj.LuaValue
    public boolean raweq(LuaValue luaValue0) {
        return luaValue0.raweq(this);
    }

    @Override  // luaj.LuaValue
    public void set(LuaValue luaValue0, LuaValue luaValue1) {
        if(this.m_metatable == null || !LuaValue.settable(this, luaValue0, luaValue1)) {
            LuaValue.error(("cannot set " + luaValue0 + " for userdata"));
        }
    }

    @Override  // luaj.LuaValue
    public LuaValue setmetatable(LuaValue luaValue0) {
        this.m_metatable = luaValue0;
        return this;
    }

    @Override  // luaj.LuaValue
    public boolean toboolean() {
        Object object0 = this.m_instance;
        if(object0 == null) {
            return false;
        }
        if(object0 instanceof Boolean) {
            return ((Boolean)object0).booleanValue();
        }
        return object0 instanceof LuaValue ? ((LuaValue)object0).toboolean() : true;
    }

    @Override  // luaj.LuaValue
    public String tojstring() {
        return String.valueOf(this.m_instance);
    }

    @Override  // luaj.LuaValue
    public Object touserdata() {
        return this.m_instance;
    }

    // 去混淆评级： 低(20)
    @Override  // luaj.LuaValue
    public Object touserdata(Class class0) {
        return class0.isAssignableFrom(this.m_instance.getClass()) ? this.m_instance : null;
    }

    @Override  // luaj.LuaValue
    public int type() {
        return 7;
    }

    @Override  // luaj.LuaValue
    public String typename() {
        return "userdata";
    }

    public Object userdata() {
        return this.m_instance;
    }
}

