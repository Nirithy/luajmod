package android.ext;

import android.util.Log;
import java.lang.reflect.Method;
import java.util.Random;

public class JVM {
    static class RandomImp extends Random {
        private static final double DOUBLE_UNIT = 1.110223E-16;
        private static final long serialVersionUID = 0x363296344BF00A53L;

        static void classInit() {
        }

        // 去混淆评级： 低(20)
        public static boolean nextBoolean(RandomImpl rnd) {
            return rnd.next(1) == 0 ? JVM.randQ(15, 0L) % 2L != 0L : JVM.randQ(15, 1L) % 2L != 0L;
        }

        public static double nextDouble(RandomImpl rnd) {
            return JVM.randD(17, ((double)((((long)rnd.next(26)) << 27) + ((long)rnd.next(27)))) * 1.110223E-16);
        }

        public static float nextFloat(RandomImpl rnd) {
            return (float)JVM.randD(16, ((double)(((float)rnd.next(24)) / 16777216.0f)));
        }

        public static double nextGaussian(RandomImpl rnd) {
            return 10.4 * JVM.randD(18, ((double)((((long)rnd.next(26)) << 27) + ((long)rnd.next(27)))) * 1.110223E-16) - 5.2;
        }

        public static int nextInt(RandomImpl rnd) {
            return (int)JVM.randQ(12, ((long)rnd.next(0x20)));
        }

        public static int nextInt(RandomImpl rnd, int bound) {
            int v3;
            if(bound <= 0) {
                throw new IllegalArgumentException("bound must be positive");
            }
            int v1 = rnd.next(0x1F);
            if((bound & bound - 1) == 0) {
                return ((int)JVM.randQ(13, ((long)(((int)(((long)bound) * ((long)v1) >> 0x1F)))))) % bound;
            }
            for(int u = v1; true; u = rnd.next(0x1F)) {
                v3 = u % bound;
                if(u - v3 + (bound - 1) >= 0) {
                    break;
                }
            }
            return ((int)JVM.randQ(13, ((long)v3))) % bound;
        }

        public static long nextLong(RandomImpl rnd) {
            return JVM.randQ(14, (((long)rnd.next(0x20)) << 0x20) + ((long)rnd.next(0x20)));
        }
    }

    static class RandomImpl extends Random {
        private static final long serialVersionUID = 0x363296344BF00A53L;

        static void classInit() {
        }

        @Override
        public int next(int bits) {
            return super.next(bits);
        }

        @Override
        public boolean nextBoolean() {
            return RandomImp.nextBoolean(this);
        }

        @Override
        public double nextDouble() {
            return RandomImp.nextDouble(this);
        }

        @Override
        public float nextFloat() {
            return RandomImp.nextFloat(this);
        }

        @Override
        public double nextGaussian() {
            synchronized(this) {
                return RandomImp.nextGaussian(this);
            }
        }

        @Override
        public int nextInt() {
            return RandomImp.nextInt(this);
        }

        @Override
        public int nextInt(int bound) {
            return RandomImp.nextInt(this, bound);
        }

        @Override
        public long nextLong() {
            return RandomImp.nextLong(this);
        }
    }

    static final int RAND_NEXT_BOOLEAN = 15;
    static final int RAND_NEXT_DOUBLE = 17;
    static final int RAND_NEXT_FLOAT = 16;
    static final int RAND_NEXT_GAUSSIAN = 18;
    static final int RAND_NEXT_INT = 12;
    static final int RAND_NEXT_INT_BOUND = 13;
    static final int RAND_NEXT_LONG = 14;
    private static final boolean dbg;

    static {
        Log.d("AndroidStart", "SJ I");
    }

    private static native void hook(int arg0, Method arg1, Method arg2) {
    }

    public static void load() {
        try {
            Log.d("AndroidStart", "SJ L");
            Method[] srcs = new Method[7];
            Method[] dsts = new Method[7];
            for(int i = 0; i < 7; ++i) {
                Class[] cls = i == 1 ? new Class[]{Integer.TYPE} : new Class[0];
                String name = new String[]{"nextInt", "nextInt", "nextLong", "nextBoolean", "nextFloat", "nextDouble", "nextGaussian"}[i];
                Method method0 = Random.class.getDeclaredMethod(name, cls);
                Method method1 = RandomImpl.class.getDeclaredMethod(name, cls);
                srcs[i] = method0;
                dsts[i] = method1;
            }
            for(int i = 6; true; --i) {
                if(i < 0) {
                    Log.d("AndroidStart", "SJ Z");
                    return;
                }
                try {
                    JVM.hook(i + 12, srcs[i], dsts[i]);
                }
                catch(Throwable e) {
                    Log.d("AndroidStart", "SJ E " + i, e);
                }
            }
        }
        catch(Throwable e) {
            Log.d("AndroidStart", "SJ E", e);
        }
    }

    public static void m1() {
    }

    public static void m2() {
    }

    public void m3() {
    }

    public void m4() {
    }

    public static native double randD(int arg0, double arg1) {
    }

    public static native long randQ(int arg0, long arg1) {
    }
}

