package android.ext;

import java.util.Arrays;

public class ListSpeed {
    public static class MN {
        final int M;
        final int N;

        public MN(int m, int n) {
            this.M = m;
            this.N = n;
        }
    }

    private static final String EN_DEFAULT = "0.000000001;0.00000001;0.0000001;0.000001;0.00001;0.0001;0.001;0.002;0.005;0.01;0.02;0.05;0.1;0.2;0.5;0.6;0.75;0.9;1;1.2;1.3;1.5;2;3;4;5;6;9;12;15;20;30;60;120;180;300;600;1200;2400;3600";
    private static final String LIST_SPEEDS = "list-speeds";
    public static final double MAX = 1000000000.0;
    public static final double MIN = 1.000000E-09;
    private int currentIndex;
    private double currentSpeed;
    double[] list;

    public ListSpeed() {
        this.currentIndex = 0;
        this.currentSpeed = 1.0;
        this.list = new double[]{1.0};
        this.set(ParserNumbers.fixSeparatorsToLocale(Tools.getSharedPreferences().getString("list-speeds", "0.000000001;0.00000001;0.0000001;0.000001;0.00001;0.0001;0.001;0.002;0.005;0.01;0.02;0.05;0.1;0.2;0.5;0.6;0.75;0.9;1;1.2;1.3;1.5;2;3;4;5;6;9;12;15;20;30;60;120;180;300;600;1200;2400;3600")), false);
    }

    public static String format(double speed) {
        int v = Math.min(Math.max(((int)Math.log10(speed)), 0), 9);
        String s = Tools.stringFormat(("%,." + (9 - v) + 'f'), new Object[]{speed});
        return 9 - v == 0 ? s : s.replaceAll("[" + ParserNumbers.decimalSeparator + "]?0*$", "");
    }

    public String get() {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        double[] arr_f = this.list;
        for(int v = 0; v < arr_f.length; ++v) {
            double speed = arr_f[v];
            if(first) {
                first = false;
            }
            else {
                sb.append(';');
            }
            sb.append(ListSpeed.format(speed));
        }
        return sb.toString();
    }

    // 去混淆评级： 低(20)
    public static String getDefault() {
        return "0.000000001;0.00000001;0.0000001;0.000001;0.00001;0.0001;0.001;0.002;0.005;0.01;0.02;0.05;0.1;0.2;0.5;0.6;0.75;0.9;1;1.2;1.3;1.5;2;3;4;5;6;9;12;15;20;30;60;120;180;300;600;1200;2400;3600";
    }

    public static MN getMN(double speed) {
        double f1 = Math.pow(10.0, 9 - Math.max(0, ((int)Math.ceil(Math.log10(speed)))));
        int M = ((int)(((double)(((int)(2.0 * f1)))) * speed)) == 0 ? 1 : ((int)(((double)(((int)(2.0 * f1)))) * speed));
        int v1 = Tools.gcd(M, ((int)(2.0 * f1)));
        return new MN(M / v1, ((int)(2.0 * f1)) / v1);
    }

    public double getSpeed() {
        return this.currentSpeed;
    }

    private void indexToValue(double val) {
        int found = 0;
        double best = 1.797693E+308;
        for(int i = 0; i < this.list.length; ++i) {
            double f2 = Math.abs(val - this.list[i]);
            if(f2 < best) {
                found = i;
                best = f2;
            }
        }
        this.currentIndex = found;
        this.currentSpeed = this.list[found];
    }

    private void set(String localeConfig, boolean save) {
        int used;
        int used;
        Double double0;
        if(localeConfig.length() == 0) {
            localeConfig = "0.000000001;0.00000001;0.0000001;0.000001;0.00001;0.0001;0.001;0.002;0.005;0.01;0.02;0.05;0.1;0.2;0.5;0.6;0.75;0.9;1;1.2;1.3;1.5;2;3;4;5;6;9;12;15;20;30;60;120;180;300;600;1200;2400;3600";
        }
        String[] arr_s = localeConfig.split(";");
        double[] list = new double[arr_s.length + 1];
        boolean haveOne = false;
        int v = 0;
        int used = 0;
        while(v < arr_s.length) {
            String speed = arr_s[v];
            try {
                double0 = Tools.parseTime(speed);
            }
            catch(NumberFormatException e) {
                Log.w(("Failed parse speed: \'" + speed + '\''), e);
                used = used;
                goto label_23;
            }
            if(((double)double0) >= 1.000000E-09 && ((double)double0) <= 1000000000.0) {
                if(!haveOne && ((double)double0) == 1.0) {
                    haveOne = true;
                }
                used = used + 1;
                list[used] = (double)double0;
            }
            else {
                Log.w(("Speed outside range: " + double0 + " (" + 1.000000E-09 + "; " + 1000000000.0 + ')'));
                used = used;
            }
        label_23:
            ++v;
            used = used;
        }
        if(haveOne) {
            used = used;
        }
        else {
            used = used + 1;
            list[used] = 1.0;
        }
        if(list.length > used) {
            list = Arrays.copyOf(list, used);
        }
        if(Config.speedsParams != 0) {
            Arrays.sort(list);
            if(Config.speedsParams == 2) {
                double prev = 0.0;
                int used = 0;
                for(int i = 0; i < list.length; ++i) {
                    double value = list[i];
                    if(i <= 0 || value != prev) {
                        if(i != used) {
                            list[used] = value;
                        }
                        ++used;
                        prev = value;
                    }
                    else {
                        Log.w(("Speed duplicate: " + value + " (" + i + ')'));
                    }
                }
                if(list.length > used) {
                    list = Arrays.copyOf(list, used);
                }
            }
        }
        this.list = list;
        this.indexToValue(this.currentSpeed);
        if(save) {
            String s2 = ParserNumbers.fixSeparators(this.get());
            new SPEditor().putString("list-speeds", s2, "0.000000001;0.00000001;0.0000001;0.000001;0.00001;0.0001;0.001;0.002;0.005;0.01;0.02;0.05;0.1;0.2;0.5;0.6;0.75;0.9;1;1.2;1.3;1.5;2;3;4;5;6;9;12;15;20;30;60;120;180;300;600;1200;2400;3600").commit();
        }
    }

    public void set(String localeConfig) {
        this.set(localeConfig, true);
    }

    public void setCloseSpeed(double val) {
        this.indexToValue(val);
    }

    public void setSpeedOne() {
        this.setCloseSpeed(1.0);
    }

    public void toNext() {
        this.updateIndex(1);
    }

    public void toPrev() {
        this.updateIndex(-1);
    }

    private void updateIndex(int diff) {
        int index = this.currentIndex + diff >= 0 ? this.currentIndex + diff : 0;
        if(index >= this.list.length) {
            index = this.list.length - 1;
        }
        this.currentIndex = index;
        this.currentSpeed = this.list[index];
    }
}

