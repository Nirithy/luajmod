package android.lang;

import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Map;
import java.util.Set;

final class ProcessEnvironment {
    static abstract class ExternalData {
        protected final byte[] bytes;
        protected final String str;

        protected ExternalData(String str, byte[] bytes) {
            this.str = str;
            this.bytes = bytes;
        }

        // 去混淆评级： 低(20)
        @Override
        public boolean equals(Object o) {
            return o instanceof ExternalData && ProcessEnvironment.arrayEquals(this.getBytes(), ((ExternalData)o).getBytes());
        }

        public byte[] getBytes() {
            return this.bytes;
        }

        @Override
        public int hashCode() {
            return ProcessEnvironment.arrayHash(this.getBytes());
        }

        @Override
        public String toString() {
            return this.str;
        }
    }

    static class StringEntry implements Map.Entry {
        private final Map.Entry e;

        public StringEntry(Map.Entry map$Entry0) {
            this.e = map$Entry0;
        }

        // 去混淆评级： 低(20)
        @Override
        public boolean equals(Object o) {
            return o instanceof StringEntry && this.e.equals(((StringEntry)o).e);
        }

        @Override
        public Object getKey() {
            return this.getKey();
        }

        public String getKey() {
            return ((Variable)this.e.getKey()).toString();
        }

        @Override
        public Object getValue() {
            return this.getValue();
        }

        public String getValue() {
            return ((Value)this.e.getValue()).toString();
        }

        @Override
        public int hashCode() {
            return this.e.hashCode();
        }

        @Override
        public Object setValue(Object object0) {
            return this.setValue(((String)object0));
        }

        public String setValue(String newValue) {
            Value processEnvironment$Value0 = Value.valueOf(newValue);
            return ((Value)this.e.setValue(processEnvironment$Value0)).toString();
        }

        @Override
        public String toString() {
            return this.getKey() + '=' + this.getValue();
        }
    }

    static class StringEntrySet extends AbstractSet {
        private final Set s;

        public StringEntrySet(Set set0) {
            this.s = set0;
        }

        @Override
        public void clear() {
            this.s.clear();
        }

        @Override
        public boolean contains(Object o) {
            Map.Entry map$Entry0 = StringEntrySet.vvEntry(o);
            return this.s.contains(map$Entry0);
        }

        // 去混淆评级： 低(20)
        @Override
        public boolean equals(Object o) {
            return o instanceof StringEntrySet && this.s.equals(((StringEntrySet)o).s);
        }

        @Override
        public int hashCode() {
            return this.s.hashCode();
        }

        @Override
        public boolean isEmpty() {
            return this.s.isEmpty();
        }

        @Override
        public Iterator iterator() {
            return new Iterator() {
                Iterator i;

                {
                    this.i = processEnvironment$StringEntrySet0.s.iterator();
                }

                @Override
                public boolean hasNext() {
                    return this.i.hasNext();
                }

                @Override
                public Object next() {
                    return this.next();
                }

                public Map.Entry next() {
                    Object object0 = this.i.next();
                    return new StringEntry(((Map.Entry)object0));
                }

                @Override
                public void remove() {
                    this.i.remove();
                }
            };
        }

        @Override
        public boolean remove(Object o) {
            Map.Entry map$Entry0 = StringEntrySet.vvEntry(o);
            return this.s.remove(map$Entry0);
        }

        @Override
        public int size() {
            return this.s.size();
        }

        private static Map.Entry vvEntry(Object o) {
            return o instanceof StringEntry ? ((StringEntry)o).e : new Map.Entry() {
                public Variable getKey() {
                    return Variable.valueOfQueryOnly(((Map.Entry)o).getKey());
                }

                @Override
                public Object getKey() {
                    return this.getKey();
                }

                public Value getValue() {
                    return Value.valueOfQueryOnly(((Map.Entry)o).getValue());
                }

                @Override
                public Object getValue() {
                    return this.getValue();
                }

                public Value setValue(Value value) {
                    throw new UnsupportedOperationException();
                }

                @Override
                public Object setValue(Object object0) {
                    return this.setValue(((Value)object0));
                }
            };
        }
    }

    static class StringEnvironment extends AbstractMap {
        private Map m;

        public StringEnvironment(Map map0) {
            this.m = map0;
        }

        @Override
        public void clear() {
            this.m.clear();
        }

        @Override
        public boolean containsKey(Object key) {
            return this.m.containsKey(Variable.valueOfQueryOnly(key));
        }

        @Override
        public boolean containsValue(Object value) {
            return this.m.containsValue(Value.valueOfQueryOnly(value));
        }

        @Override
        public Set entrySet() {
            return new StringEntrySet(this.m.entrySet());
        }

        @Override
        public Object get(Object object0) {
            return this.get(object0);
        }

        public String get(Object key) {
            return StringEnvironment.toString(((Value)this.m.get(Variable.valueOfQueryOnly(key))));
        }

        @Override
        public boolean isEmpty() {
            return this.m.isEmpty();
        }

        @Override
        public Set keySet() {
            return new StringKeySet(this.m.keySet());
        }

        @Override
        public Object put(Object object0, Object object1) {
            return this.put(((String)object0), ((String)object1));
        }

        public String put(String key, String value) {
            return StringEnvironment.toString(((Value)this.m.put(Variable.valueOf(key), Value.valueOf(value))));
        }

        @Override
        public Object remove(Object object0) {
            return this.remove(object0);
        }

        public String remove(Object key) {
            return StringEnvironment.toString(((Value)this.m.remove(Variable.valueOfQueryOnly(key))));
        }

        @Override
        public int size() {
            return this.m.size();
        }

        public byte[] toEnvironmentBlock(int[] envc) {
            int count = this.m.size() * 2;
            for(Object object0: this.m.entrySet()) {
                byte[] arr_b = ((Variable)((Map.Entry)object0).getKey()).getBytes();
                byte[] arr_b1 = ((Value)((Map.Entry)object0).getValue()).getBytes();
                count = count + arr_b.length + arr_b1.length;
            }
            byte[] block = new byte[count];
            int i = 0;
            for(Object object1: this.m.entrySet()) {
                byte[] arr_b3 = ((Variable)((Map.Entry)object1).getKey()).getBytes();
                byte[] arr_b4 = ((Value)((Map.Entry)object1).getValue()).getBytes();
                System.arraycopy(arr_b3, 0, block, i, arr_b3.length);
                int i = i + arr_b3.length;
                block[i] = 61;
                System.arraycopy(arr_b4, 0, block, i + 1, arr_b4.length);
                i = i + 1 + (arr_b4.length + 1);
            }
            envc[0] = this.m.size();
            return block;
        }

        private static String toString(Value v) {
            return v == null ? null : v.toString();
        }

        @Override
        public Collection values() {
            return new StringValues(this.m.values());
        }
    }

    static class StringKeySet extends AbstractSet {
        private final Set s;

        public StringKeySet(Set set0) {
            this.s = set0;
        }

        @Override
        public void clear() {
            this.s.clear();
        }

        @Override
        public boolean contains(Object o) {
            Variable processEnvironment$Variable0 = Variable.valueOfQueryOnly(o);
            return this.s.contains(processEnvironment$Variable0);
        }

        @Override
        public boolean isEmpty() {
            return this.s.isEmpty();
        }

        @Override
        public Iterator iterator() {
            return new Iterator() {
                Iterator i;

                {
                    this.i = processEnvironment$StringKeySet0.s.iterator();
                }

                @Override
                public boolean hasNext() {
                    return this.i.hasNext();
                }

                @Override
                public Object next() {
                    return this.next();
                }

                public String next() {
                    Object object0 = this.i.next();
                    return ((Variable)object0).toString();
                }

                @Override
                public void remove() {
                    this.i.remove();
                }
            };
        }

        @Override
        public boolean remove(Object o) {
            Variable processEnvironment$Variable0 = Variable.valueOfQueryOnly(o);
            return this.s.remove(processEnvironment$Variable0);
        }

        @Override
        public int size() {
            return this.s.size();
        }
    }

    static class StringValues extends AbstractCollection {
        private final Collection c;

        public StringValues(Collection collection0) {
            this.c = collection0;
        }

        @Override
        public void clear() {
            this.c.clear();
        }

        @Override
        public boolean contains(Object o) {
            Value processEnvironment$Value0 = Value.valueOfQueryOnly(o);
            return this.c.contains(processEnvironment$Value0);
        }

        // 去混淆评级： 低(20)
        @Override
        public boolean equals(Object o) {
            return o instanceof StringValues && this.c.equals(((StringValues)o).c);
        }

        @Override
        public int hashCode() {
            return this.c.hashCode();
        }

        @Override
        public boolean isEmpty() {
            return this.c.isEmpty();
        }

        @Override
        public Iterator iterator() {
            return new Iterator() {
                Iterator i;

                {
                    this.i = processEnvironment$StringValues0.c.iterator();
                }

                @Override
                public boolean hasNext() {
                    return this.i.hasNext();
                }

                @Override
                public Object next() {
                    return this.next();
                }

                public String next() {
                    Object object0 = this.i.next();
                    return ((Value)object0).toString();
                }

                @Override
                public void remove() {
                    this.i.remove();
                }
            };
        }

        @Override
        public boolean remove(Object o) {
            Value processEnvironment$Value0 = Value.valueOfQueryOnly(o);
            return this.c.remove(processEnvironment$Value0);
        }

        @Override
        public int size() {
            return this.c.size();
        }
    }

    static class Value extends ExternalData implements Comparable {
        protected Value(String str, byte[] bytes) {
            super(str, bytes);
        }

        public int compareTo(Value value) {
            return ProcessEnvironment.arrayCompare(this.getBytes(), value.getBytes());
        }

        @Override
        public int compareTo(Object object0) {
            return this.compareTo(((Value)object0));
        }

        // 去混淆评级： 低(20)
        @Override  // android.lang.ProcessEnvironment$ExternalData
        public boolean equals(Object o) {
            return o instanceof Value && super.equals(o);
        }

        public static Value valueOf(String str) {
            ProcessEnvironment.validateValue(str);
            return Value.valueOfQueryOnly(str);
        }

        public static Value valueOf(byte[] bytes) {
            return new Value(new String(bytes), bytes);
        }

        public static Value valueOfQueryOnly(Object str) {
            return Value.valueOfQueryOnly(((String)str));
        }

        public static Value valueOfQueryOnly(String str) {
            return new Value(str, str.getBytes());
        }
    }

    static class Variable extends ExternalData implements Comparable {
        protected Variable(String str, byte[] bytes) {
            super(str, bytes);
        }

        public int compareTo(Variable variable) {
            return ProcessEnvironment.arrayCompare(this.getBytes(), variable.getBytes());
        }

        @Override
        public int compareTo(Object object0) {
            return this.compareTo(((Variable)object0));
        }

        // 去混淆评级： 低(20)
        @Override  // android.lang.ProcessEnvironment$ExternalData
        public boolean equals(Object o) {
            return o instanceof Variable && super.equals(o);
        }

        public static Variable valueOf(String str) {
            ProcessEnvironment.validateVariable(str);
            return Variable.valueOfQueryOnly(str);
        }

        public static Variable valueOf(byte[] bytes) {
            return new Variable(new String(bytes), bytes);
        }

        public static Variable valueOfQueryOnly(Object str) {
            return Variable.valueOfQueryOnly(((String)str));
        }

        public static Variable valueOfQueryOnly(String str) {
            return new Variable(str, str.getBytes());
        }
    }

    static final int MIN_NAME_LENGTH;
    private static final HashMap theEnvironment;
    private static final Map theUnmodifiableEnvironment;

    static {
        byte[][] arr2_b = ProcessEnvironment.environ();
        ProcessEnvironment.theEnvironment = new HashMap(arr2_b.length / 2 + 3);
        for(int i = arr2_b.length - 1; i > 0; i -= 2) {
            Variable processEnvironment$Variable0 = Variable.valueOf(arr2_b[i - 1]);
            Value processEnvironment$Value0 = Value.valueOf(arr2_b[i]);
            ProcessEnvironment.theEnvironment.put(processEnvironment$Variable0, processEnvironment$Value0);
        }
        ProcessEnvironment.theUnmodifiableEnvironment = Collections.unmodifiableMap(new StringEnvironment(ProcessEnvironment.theEnvironment));
    }

    private static int arrayCompare(byte[] x, byte[] y) {
        int min = x.length >= y.length ? y.length : x.length;
        for(int i = 0; true; ++i) {
            if(i >= min) {
                return x.length - y.length;
            }
            if(x[i] != y[i]) {
                return x[i] - y[i];
            }
        }
    }

    private static boolean arrayEquals(byte[] x, byte[] y) {
        if(x.length == y.length) {
            for(int i = 0; true; ++i) {
                if(i >= x.length) {
                    return true;
                }
                if(x[i] != y[i]) {
                    break;
                }
            }
        }
        return false;
    }

    private static int arrayHash(byte[] x) {
        int hash = 0;
        for(int i = 0; i < x.length; ++i) {
            hash = hash * 0x1F + x[i];
        }
        return hash;
    }

    static Map emptyEnvironment(int capacity) {
        return new StringEnvironment(new HashMap(capacity));
    }

    private static native byte[][] environ() {
    }

    static Map environment() {
        return new StringEnvironment(((Map)ProcessEnvironment.theEnvironment.clone()));
    }

    static String getenv(String name) {
        return (String)ProcessEnvironment.theUnmodifiableEnvironment.get(name);
    }

    static Map getenv() {
        return ProcessEnvironment.theUnmodifiableEnvironment;
    }

    static byte[] toEnvironmentBlock(Map map0, int[] envc) {
        return map0 == null ? null : ((StringEnvironment)map0).toEnvironmentBlock(envc);
    }

    private static void validateValue(String value) {
        if(value.indexOf(0) != -1) {
            throw new IllegalArgumentException("Invalid environment variable value: \"" + value + '\"');
        }
    }

    private static void validateVariable(String name) {
        if(name.indexOf(61) != -1 || name.indexOf(0) != -1) {
            throw new IllegalArgumentException("Invalid environment variable name: \"" + name + '\"');
        }
    }
}

