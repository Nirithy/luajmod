package android.ext;

import java.util.Locale;

public class Arm64Dis {
    private static final int Sys_AT = 0x10000;
    private static final int Sys_DC = 0x20000;
    private static final int Sys_IC = 0x30000;
    private static final int Sys_SYS = 0;
    private static final int Sys_SYS_OFFSET = 16;
    private static final int Sys_TLBI = 0x40000;
    private static final int arg_Cm = 0x20;
    private static final int arg_Cn = 0x1F;
    private static final int arg_Dn = 0x8F;
    private static final int arg_Dt = 0x8E;
    private static final int arg_F16 = 0xB0;
    private static final int arg_F32 = 0xB1;
    private static final int arg_F64 = 0xB2;
    private static final int arg_FPidx = 0x72;
    private static final int arg_FPidxk = 0xA2;
    private static final int arg_FPjn2 = 0x97;
    private static final int arg_FPjt = 0x70;
    private static final int arg_FPjt2 = 150;
    private static final int arg_FPk5t = 0xBD;
    private static final int arg_FPm = 85;
    private static final int arg_FPn = 0x77;
    private static final int arg_FPnj = 0x94;
    private static final int arg_FPst = 89;
    private static final int arg_FPt = 82;
    private static final int arg_FPz2m = 0x81;
    private static final int arg_FPz2n = 0x80;
    private static final int arg_FPz2t = 0x7F;
    private static final int arg_FPz3m = 0x8D;
    private static final int arg_FPz3n = 0x85;
    private static final int arg_FPz3t = 0x84;
    private static final int arg_FPz4n = 0x86;
    private static final int arg_FPz4t = 0x8B;
    private static final int arg_FPz5d = 0xC0;
    private static final int arg_FPz5m = 190;
    private static final int arg_FPz5n = 0xBA;
    private static final int arg_FPz5t = 0xB8;
    private static final int arg_Hm = 0x76;
    private static final int arg_Hn = 0x75;
    private static final int arg_Ht = 0x74;
    private static final int arg_NONE = 0;
    private static final int arg_Qi = 42;
    private static final int arg_Qi1 = 46;
    private static final int arg_Qi2 = 45;
    private static final int arg_Qi3 = 44;
    private static final int arg_Qn = 110;
    private static final int arg_Qt = 105;
    private static final int arg_R2n = 0xA1;
    private static final int arg_RCTX = 0xC3;
    private static final int arg_RSP = 0xC1;
    private static final int arg_Rd = 77;
    private static final int arg_Rd1 = 78;
    private static final int arg_Rm = 12;
    private static final int arg_Rn = 7;
    private static final int arg_RnS = 4;
    private static final int arg_Rom = 91;
    private static final int arg_Rsom = 98;
    private static final int arg_Rt = 9;
    private static final int arg_Rt1 = 0x4F;
    private static final int arg_RtS = 3;
    private static final int arg_SKIP = 1;
    private static final int arg_Sn = 106;
    private static final int arg_St = 0x6F;
    private static final int arg_Vd16b = 0xB6;
    private static final int arg_Vd4s = 0xB7;
    private static final int arg_Vm16b = 0xB5;
    private static final int arg_Vm2d = 180;
    private static final int arg_Vm4s = 107;
    private static final int arg_VmH1 = 0x7A;
    private static final int arg_VmHs = 0x9A;
    private static final int arg_VmT = 0x83;
    private static final int arg_VmT3 = 0xA9;
    private static final int arg_VmTs = 0x99;
    private static final int arg_VmTs2 = 0x9B;
    private static final int arg_VmTs4b = 0xB3;
    private static final int arg_Vmzq = 0x7D;
    private static final int arg_Vn116b = 0x9C;
    private static final int arg_Vn16b = 104;
    private static final int arg_Vn1d = 0xBB;
    private static final int arg_Vn216b = 0x9D;
    private static final int arg_Vn2d = 0x88;
    private static final int arg_Vn2h = 0x89;
    private static final int arg_Vn316b = 0x9E;
    private static final int arg_Vn416b = 0x9F;
    private static final int arg_Vn4s = 109;
    private static final int arg_VnH1 = 0x79;
    private static final int arg_VnT = 130;
    private static final int arg_VnT2 = 0xA4;
    private static final int arg_VnT3 = 0x87;
    private static final int arg_VnTa = 0x95;
    private static final int arg_Vnj = 0x71;
    private static final int arg_Vnj2 = 0x92;
    private static final int arg_Vnz = 0x8A;
    private static final int arg_Vnz3 = 0xA5;
    private static final int arg_Vnzq = 0x7C;
    private static final int arg_Vnzq2 = 0xA6;
    private static final int arg_Vt16b = 103;
    private static final int arg_Vt1d = 0xBC;
    private static final int arg_Vt2B = 0x3F;
    private static final int arg_Vt2D = 66;
    private static final int arg_Vt2H = 0x40;
    private static final int arg_Vt2S = 65;
    private static final int arg_Vt2T = 36;
    private static final int arg_Vt2d = 0xAF;
    private static final int arg_Vt3B = 55;
    private static final int arg_Vt3D = 58;
    private static final int arg_Vt3H = 56;
    private static final int arg_Vt3S = 57;
    private static final int arg_Vt3T = 37;
    private static final int arg_Vt4B = 68;
    private static final int arg_Vt4D = 71;
    private static final int arg_Vt4H = 69;
    private static final int arg_Vt4S = 70;
    private static final int arg_Vt4T = 38;
    private static final int arg_Vt4s = 108;
    private static final int arg_VtB = 0x2F;
    private static final int arg_VtD = 50;
    private static final int arg_VtH = 0x30;
    private static final int arg_VtH1 = 120;
    private static final int arg_VtS = 49;
    private static final int arg_VtT = 35;
    private static final int arg_VtT3 = 0xA8;
    private static final int arg_VtT4 = 170;
    private static final int arg_Vtj = 0xA0;
    private static final int arg_Vtj2 = 0x91;
    private static final int arg_Vtjq = 0x73;
    private static final int arg_Vtz = 140;
    private static final int arg_Vtz3 = 0x98;
    private static final int arg_Vtzq = 0x7B;
    private static final int arg_Vtzq2 = 0xA3;
    private static final int arg_Wd = 80;
    private static final int arg_Wm = 101;
    private static final int arg_Wn = 100;
    private static final int arg_Wt = 81;
    private static final int arg_Xd = 102;
    private static final int arg_Xm = 43;
    private static final int arg_XmS = 0xC4;
    private static final int arg_Xn = 33;
    private static final int arg_XnS = 40;
    private static final int arg_Xt = 0xC2;
    private static final int arg_Xt_opt = 26;
    private static final int arg_amountj = 92;
    private static final int arg_amountj2 = 0x5F;
    private static final int arg_amountj3 = 0x60;
    private static final int arg_amountjs = 94;
    private static final int arg_amountk2_opt = 0xAD;
    private static final int arg_amountk_opt = 0xAC;
    private static final int arg_amountz = 93;
    private static final int arg_at = 18;
    private static final int arg_b = 34;
    private static final int arg_c = 13;
    private static final int arg_dc = 21;
    private static final int arg_exts = 99;
    private static final int arg_fbits = 0xB9;
    private static final int arg_i = 5;
    private static final int arg_i1 = 51;
    private static final int arg_i12 = 61;
    private static final int arg_i16 = 67;
    private static final int arg_i2 = 52;
    private static final int arg_i24 = 62;
    private static final int arg_i3 = 59;
    private static final int arg_i32 = 72;
    private static final int arg_i4 = 53;
    private static final int arg_i6 = 60;
    private static final int arg_i8 = 54;
    private static final int arg_i_opt = 15;
    private static final int arg_ib = 8;
    private static final int arg_ic = 25;
    private static final int arg_im4_opt = 87;
    private static final int arg_imm64 = 0xAE;
    private static final int arg_imm8 = 0xAB;
    private static final int arg_is4_opt = 84;
    private static final int arg_iz4_opt = 86;
    private static final int arg_j = 11;
    private static final int arg_j12_opt = 6;
    private static final int arg_j16_opt = 10;
    private static final int arg_j_opt = 90;
    private static final int arg_jz = 0xBF;
    private static final int arg_labeli4 = 14;
    private static final int arg_labelij1 = 2;
    private static final int arg_nRt = 88;
    private static final int arg_offe = 41;
    private static final int arg_offs = 39;
    private static final int arg_prf_op = 83;
    private static final int arg_pstate = 16;
    private static final int arg_sh = 17;
    private static final int arg_shift8 = 0xA7;
    private static final int arg_shiftj_opt = 97;
    private static final int arg_shlshift = 0x93;
    private static final int arg_shrshift = 0x90;
    private static final int arg_simd0 = 0x7E;
    private static final int arg_sysreg = 30;
    private static final int arg_tlbi = 27;
    private static final int arg_z = 73;
    private static final int arg_z2 = 75;
    private static final int arg_z3 = 74;
    private static final int arg_z4 = 76;
    private static final int args_max = 8;
    private static final boolean debug;

    private static boolean MoveWidePreferred(int sf, int immN, int imms, int immr) {
        if((sf != 1 || immN == 1) && (sf != 0 || immN == 0 && imms >> 5 == 0)) {
            if(imms < 16) {
                return (-immr & 15) <= 15 - imms;
            }
            return sf == 1 ? imms >= 49 && (immr & 15) <= imms - 49 : imms >= 17 && (immr & 15) <= imms - 17;
        }
        return false;
    }

    private static int SysOp(int code) {
        switch(code) {
            case 904: {
                return 0x30000;
            }
            case 936: {
                return 0x30001;
            }
            case 945: {
                return 0x20000;
            }
            case 946: {
                return 0x20001;
            }
            case 947: {
                return 0x20002;
            }
            case 948: {
                return 0x20003;
            }
            case 949: {
                return 0x20004;
            }
            case 950: {
                return 0x20005;
            }
            case 960: {
                return 0x10000;
            }
            case 961: {
                return 0x10001;
            }
            case 962: {
                return 0x10002;
            }
            case 963: {
                return 0x10003;
            }
            case 968: {
                return 0x10004;
            }
            case 969: {
                return 0x10005;
            }
            case 978: {
                return 0x20006;
            }
            case 980: {
                return 0x20007;
            }
            case 982: {
                return 0x20008;
            }
            case 1010: {
                return 0x20009;
            }
            case 0x3F4: {
                return 0x2000A;
            }
            case 0x3F6: {
                return 0x2000B;
            }
            case 0x408: {
                return 0x40000;
            }
            case 0x409: {
                return 0x40001;
            }
            case 0x40A: {
                return 0x40002;
            }
            case 0x40B: {
                return 0x40003;
            }
            case 0x40D: {
                return 0x40004;
            }
            case 0x40F: {
                return 0x40005;
            }
            case 1041: {
                return 0x40006;
            }
            case 1043: {
                return 0x40007;
            }
            case 1045: {
                return 0x40008;
            }
            case 1047: {
                return 0x40009;
            }
            case 1048: {
                return 0x4000A;
            }
            case 1049: {
                return 0x4000B;
            }
            case 1050: {
                return 0x4000C;
            }
            case 1051: {
                return 0x4000D;
            }
            case 1053: {
                return 0x4000E;
            }
            case 0x41F: {
                return 0x4000F;
            }
            case 1065: {
                return 0x40010;
            }
            case 1067: {
                return 0x40011;
            }
            case 1069: {
                return 0x40012;
            }
            case 0x42F: {
                return 0x40013;
            }
            case 1073: {
                return 0x40014;
            }
            case 1075: {
                return 0x40015;
            }
            case 1077: {
                return 0x40016;
            }
            case 1079: {
                return 0x40017;
            }
            case 1080: {
                return 0x40018;
            }
            case 1081: {
                return 0x40019;
            }
            case 1082: {
                return 0x4001A;
            }
            case 1083: {
                return 0x4001B;
            }
            case 1085: {
                return 0x4001C;
            }
            case 0x43F: {
                return 0x4001D;
            }
            case 7073: {
                return 0x2000C;
            }
            case 7075: {
                return 0x2000D;
            }
            case 7076: {
                return 0x2000E;
            }
            case 7081: {
                return 0x30002;
            }
            case 7121: {
                return 0x2000F;
            }
            case 7123: {
                return 0x20010;
            }
            case 7125: {
                return 0x20011;
            }
            case 7129: {
                return 0x20012;
            }
            case 7137: {
                return 0x20013;
            }
            case 7139: {
                return 0x20014;
            }
            case 7141: {
                return 0x20015;
            }
            case 7145: {
                return 0x20016;
            }
            case 7147: {
                return 0x20017;
            }
            case 7149: {
                return 0x20018;
            }
            case 0x1BF1: {
                return 0x20019;
            }
            case 0x1BF3: {
                return 0x2001A;
            }
            case 0x1BF5: {
                return 0x2001B;
            }
            case 0x23C0: {
                return 0x10006;
            }
            case 9153: {
                return 0x10007;
            }
            case 9156: {
                return 0x10008;
            }
            case 9157: {
                return 0x10009;
            }
            case 9158: {
                return 0x1000A;
            }
            case 9159: {
                return 0x1000B;
            }
            case 0x2401: {
                return 0x4001E;
            }
            case 0x2402: {
                return 0x4001F;
            }
            case 0x2405: {
                return 0x40020;
            }
            case 0x2406: {
                return 0x40021;
            }
            case 0x2408: {
                return 0x40022;
            }
            case 0x2409: {
                return 0x40023;
            }
            case 0x240C: {
                return 0x40024;
            }
            case 0x240D: {
                return 0x40025;
            }
            case 9230: {
                return 0x40026;
            }
            case 9233: {
                return 0x40027;
            }
            case 9237: {
                return 0x40028;
            }
            case 9240: {
                return 0x40029;
            }
            case 9241: {
                return 0x4002A;
            }
            case 9244: {
                return 0x4002B;
            }
            case 9245: {
                return 0x4002C;
            }
            case 9246: {
                return 0x4002D;
            }
            case 0x2420: {
                return 0x4002E;
            }
            case 9249: {
                return 0x4002F;
            }
            case 9250: {
                return 0x40030;
            }
            case 9251: {
                return 0x40031;
            }
            case 9252: {
                return 0x40032;
            }
            case 9253: {
                return 0x40033;
            }
            case 9254: {
                return 0x40034;
            }
            case 9255: {
                return 0x40035;
            }
            case 9257: {
                return 0x40036;
            }
            case 9261: {
                return 0x40037;
            }
            case 9265: {
                return 0x40038;
            }
            case 9269: {
                return 0x40039;
            }
            case 9272: {
                return 0x4003A;
            }
            case 9273: {
                return 0x4003B;
            }
            case 9276: {
                return 0x4003C;
            }
            case 9277: {
                return 0x4003D;
            }
            case 9278: {
                return 0x4003E;
            }
            case 0x33C0: {
                return 0x1000C;
            }
            case 0x33C1: {
                return 0x1000D;
            }
            case 0x3408: {
                return 0x4003F;
            }
            case 0x3409: {
                return 0x40040;
            }
            case 0x340D: {
                return 0x40041;
            }
            case 0x3411: {
                return 0x40042;
            }
            case 0x3415: {
                return 0x40043;
            }
            case 0x3418: {
                return 0x40044;
            }
            case 0x3419: {
                return 0x40045;
            }
            case 0x341D: {
                return 0x40046;
            }
            case 0x3429: {
                return 0x40047;
            }
            case 0x342D: {
                return 0x40048;
            }
            case 0x3431: {
                return 0x40049;
            }
            case 0x3435: {
                return 0x4004A;
            }
            case 0x3438: {
                return 0x4004B;
            }
            case 0x3439: {
                return 0x4004C;
            }
            case 0x343D: {
                return 0x4004D;
            }
            default: {
                return 0;
            }
        }
    }

    public static int[] args() {
        return new int[8];
    }

    public static StringBuilder disasm(int[] args, long addr, int ic32, StringBuilder str) {
        // 错误 - 方法未被反编译
        // Self interruption (operation timed-out, maximum duration allowed was 60000 ms)
    }

    private static long disasm_dbm(int k, int j, int i) {
        long r = -1L;
        int l;
        for(l = 6; l >= 0 && (1 << l & (k << 6 | ~j & 0x3F)) == 0; --l) {
        }
        if(l >= 1) {
            int l = (1 << l) - 1;
            int v6 = j & l;
            if(v6 != l) {
                long m = -1L >>> 0x3F - v6;
                long m = m >>> (i & l) | m << (1 << l) - (i & l);
                r = m;
                for(int v9 = 1 << l; v9 < 0x40; v9 += 1 << l) {
                    r |= m << v9;
                }
            }
        }
        return r;
    }

    private static String disasm_str(String s, int n) {
        if(s == null) {
            return "?";
        }
        int pos = 0;
        while(true) {
            if(n <= 0) {
                if(pos == s.length()) {
                    return "?";
                }
                int v2 = s.indexOf(0, pos);
                if(v2 >= 0) {
                    return s.substring(pos, v2);
                }
                return pos == 0 ? s : s.substring(pos);
            }
            int pos = s.indexOf(0, pos);
            if(pos < 0) {
                return "?";
            }
            pos = pos + 1;
            --n;
        }
    }

    private static String disasm_sysreg(int p, int k, int n, int m, int j) {
        switch(p) {
            case 2: {
                switch(k) {
                    case 0: {
                        switch(n) {
                            case 0: {
                                switch(m) {
                                    case 0: {
                                        return Arm64Dis.disasm_str("?\u0000?\u0000OSDTRRX_EL1", j);
                                    }
                                    case 2: {
                                        return Arm64Dis.disasm_str("MDCCINT_EL1\u0000?\u0000MDSCR_EL1", j);
                                    }
                                    case 3: {
                                        return Arm64Dis.disasm_str("?\u0000?\u0000OSDTRTX_EL1", j);
                                    }
                                    case 6: {
                                        return Arm64Dis.disasm_str("?\u0000?\u0000OSECCR_EL1", j);
                                    }
                                    default: {
                                        switch(j) {
                                            case 4: {
                                                return Arm64Dis.disasm_str("DBGBVR0_EL1\u0000DBGBVR1_EL1\u0000DBGBVR2_EL1\u0000DBGBVR3_EL1\u0000DBGBVR4_EL1\u0000DBGBVR5_EL1\u0000DBGBVR6_EL1\u0000DBGBVR7_EL1", m);
                                            }
                                            case 5: {
                                                return Arm64Dis.disasm_str("DBGBCR0_EL1\u0000DBGBCR1_EL1\u0000DBGBCR2_EL1\u0000DBGBCR3_EL1\u0000DBGBCR4_EL1\u0000DBGBCR5_EL1\u0000DBGBCR6_EL1\u0000DBGBCR7_EL1", m);
                                            }
                                            case 6: {
                                                return Arm64Dis.disasm_str("DBGWVR0_EL1\u0000DBGWVR1_EL1\u0000DBGWVR2_EL1\u0000DBGWVR3_EL1\u0000DBGWVR4_EL1\u0000DBGWVR5_EL1\u0000DBGWVR6_EL1\u0000DBGWVR7_EL1", m);
                                            }
                                            case 7: {
                                                return Arm64Dis.disasm_str("DBGWCR0_EL1\u0000DBGWCR1_EL1\u0000DBGWCR2_EL1\u0000DBGWCR3_EL1\u0000DBGWCR4_EL1\u0000DBGWCR5_EL1\u0000DBGWCR6_EL1\u0000DBGWCR7_EL1", m);
                                            }
                                            default: {
                                                return null;
                                            }
                                        }
                                    }
                                }
                            }
                            case 1: {
                                if(m == 0) {
                                    return Arm64Dis.disasm_str("MDRAR_EL1\u0000?\u0000?\u0000?\u0000OSLAR_EL1", j);
                                }
                                return j == 4 ? Arm64Dis.disasm_str("OSLSR_EL1\u0000?\u0000OSDLR_EL1\u0000DBGPRCR_EL1", m) : null;
                            }
                            case 7: {
                                return j == 6 ? Arm64Dis.disasm_str("?\u0000?\u0000?\u0000?\u0000?\u0000?\u0000?\u0000?\u0000DBGCLAIMSET_EL1\u0000DBGCLAIMCLR_EL1\u0000?\u0000?\u0000?\u0000?\u0000DBGAUTHSTATUS_EL1", m) : null;
                            }
                            default: {
                                return null;
                            }
                        }
                    }
                    case 3: {
                        return n != 0 || j != 0 ? null : Arm64Dis.disasm_str("?\u0000MDCCSR_EL0\u0000?\u0000?\u0000DBGDTR_EL0\u0000DBGDTRRX_EL0", m);
                    }
                    case 4: {
                        return n != 0 || m != 7 ? null : Arm64Dis.disasm_str("DBGVCR32_EL2", j);
                    }
                    default: {
                        return null;
                    }
                }
            }
            case 3: {
                switch(k) {
                    case 0: {
                        switch(n) {
                            case 0: {
                                return m == 0 ? Arm64Dis.disasm_str("MIDR_EL1\u0000?\u0000?\u0000?\u0000?\u0000MPIDR_EL1\u0000REVIDR_EL1\u0000?\u0000ID_PFR0_EL1\u0000ID_PFR1_EL1\u0000ID_DFR0_EL1\u0000ID_AFR0_EL1\u0000ID_MMFR0_EL1\u0000ID_MMFR1_EL1\u0000ID_MMFR2_EL1\u0000ID_MMFR3_EL1\u0000ID_ISAR0_EL1\u0000ID_ISAR1_EL1\u0000ID_ISAR2_EL1\u0000ID_ISAR2_EL1\u0000ID_ISAR3_EL1\u0000ID_ISAR4_EL1\u0000ID_ISAR5_EL1\u0000ID_MMFR4_EL1\u0000?\u0000MVFR0_EL1\u0000MVFR1_EL1\u0000MVFR2_EL1\u0000?\u0000?\u0000?\u0000?\u0000?\u0000ID_A64PFR0_EL1\u0000ID_A64PFR1_EL1\u0000?\u0000?\u0000ID_A64ZFR0_EL1\u0000?\u0000?\u0000?\u0000ID_A64DFR0_EL1\u0000ID_A64DFR1_EL1\u0000?\u0000?\u0000ID_A64AFR0_EL1\u0000ID_A64AFR1_EL1\u0000?\u0000?\u0000ID_A64ISAR0_EL1\u0000ID_A64ISAR1_EL1\u0000?\u0000?\u0000?\u0000?\u0000?\u0000?\u0000ID_A64MMFR0_EL1\u0000ID_A64MMFR1_EL1\u0000ID_A64MMFR2_EL1", j) : null;
                            }
                            case 1: {
                                switch(m) {
                                    case 0: {
                                        return Arm64Dis.disasm_str("SCTLR_EL1\u0000ACTLR_EL1\u0000CPACR_EL1", j);
                                    }
                                    case 2: {
                                        return Arm64Dis.disasm_str("ZCR_EL1", j);
                                    }
                                    default: {
                                        return null;
                                    }
                                }
                            }
                            case 2: {
                                return m == 0 ? Arm64Dis.disasm_str("TTBR0_EL1\u0000TTBR1_EL1\u0000TCR_EL1", j) : null;
                            }
                            case 4: {
                                switch(m) {
                                    case 0: {
                                        return Arm64Dis.disasm_str("SPSR_EL1\u0000ELR_EL1", j);
                                    }
                                    case 1: {
                                        return Arm64Dis.disasm_str("SP_EL0", j);
                                    }
                                    case 2: {
                                        return Arm64Dis.disasm_str("SPSel\u0000?\u0000CurrentEL\u0000PAN\u0000UAO", j);
                                    }
                                    case 6: {
                                        return Arm64Dis.disasm_str("ICC_PMR_EL1", j);
                                    }
                                    default: {
                                        return null;
                                    }
                                }
                            }
                            case 5: {
                                switch(m) {
                                    case 1: {
                                        return Arm64Dis.disasm_str("AFSR0_EL1\u0000AFSR1_EL1", j);
                                    }
                                    case 2: {
                                        return Arm64Dis.disasm_str("ESR_EL1", j);
                                    }
                                    case 3: {
                                        return Arm64Dis.disasm_str("ERRIDR_EL1\u0000ERRSELR_EL1", j);
                                    }
                                    case 4: {
                                        return Arm64Dis.disasm_str("ERXFR_EL1\u0000ERXCTLR_EL1\u0000ERXSTATUS_EL1\u0000ERXADDR_EL1", j);
                                    }
                                    case 5: {
                                        return Arm64Dis.disasm_str("ERXMISC0_EL1\u0000ERXMISC1_EL1", j);
                                    }
                                    default: {
                                        return null;
                                    }
                                }
                            }
                            case 6: {
                                return m == 0 ? Arm64Dis.disasm_str("FAR_EL1", j) : null;
                            }
                            case 7: {
                                return m == 4 ? Arm64Dis.disasm_str("PAR_EL1", j) : null;
                            }
                            case 9: {
                                switch(m) {
                                    case 9: {
                                        return Arm64Dis.disasm_str("PMSCR_EL1\u0000?\u0000PMSICR_EL1\u0000PMSIRR_EL1\u0000PMSFCR_EL1\u0000PMSEVFR_EL1\u0000PMSLATFR_EL1\u0000PMSIDR_EL1\u0000PMSIDR_EL1", j);
                                    }
                                    case 10: {
                                        return Arm64Dis.disasm_str("PMBLIMITR_EL1\u0000PMBPTR_EL1\u0000?\u0000PMBSR_EL1\u0000?\u0000?\u0000?\u0000PMBIDR_EL1", j);
                                    }
                                    case 14: {
                                        return Arm64Dis.disasm_str("?\u0000PMINTENSET_EL1\u0000PMINTENCLR_EL1", j);
                                    }
                                    default: {
                                        return null;
                                    }
                                }
                            }
                            case 10: {
                                if(m == 4) {
                                    return Arm64Dis.disasm_str("LORSA_EL1\u0000LOREA_EL1\u0000LORN_EL1\u0000LORC_EL1\u0000?\u0000?\u0000?\u0000LORID_EL1", j);
                                }
                                return m == 4 || j != 0 ? null : Arm64Dis.disasm_str("?\u0000?\u0000MAIR_EL1\u0000AMAIR_EL1", m);
                            }
                            case 12: {
                                switch(m) {
                                    case 0: {
                                        return Arm64Dis.disasm_str("VBAR_EL1\u0000RVBAR_EL1\u0000RMR_EL1", j);
                                    }
                                    case 1: {
                                        return Arm64Dis.disasm_str("ISR_EL1\u0000DISR_EL1", j);
                                    }
                                    case 8: {
                                        return Arm64Dis.disasm_str("ICC_IAR0_EL1\u0000ICC_EOIR0_EL1\u0000ICC_HPPIR0_EL1\u0000ICC_BPR0_EL1\u0000ICC_AP0R0_EL1\u0000ICC_AP0R1_EL1\u0000ICC_AP0R2_EL1\u0000ICC_AP0R3_EL1", j);
                                    }
                                    case 9: {
                                        return Arm64Dis.disasm_str("ICC_AP1R0_EL1\u0000ICC_AP1R1_EL1\u0000ICC_AP1R2_EL1\u0000ICC_AP1R3_EL1", j);
                                    }
                                    case 11: {
                                        return Arm64Dis.disasm_str("?\u0000ICC_DIR_EL1\u0000?\u0000ICC_RPR_EL1\u0000?\u0000ICC_SGI1R_EL1\u0000ICC_ASGI1R_EL1\u0000ICC_SGI0R_EL1", j);
                                    }
                                    case 12: {
                                        return Arm64Dis.disasm_str("ICC_IAR1_EL1\u0000ICC_EOIR1_EL1\u0000ICC_HPPIR1_EL1\u0000ICC_BPR1_EL1\u0000ICC_CTLR_EL1\u0000ICC_SRE_EL1\u0000ICC_IGRPEN0_EL1\u0000ICC_IGRPEN1_EL1", j);
                                    }
                                    default: {
                                        return null;
                                    }
                                }
                            }
                            case 13: {
                                return m == 0 ? Arm64Dis.disasm_str("?\u0000CONTEXTIDR_EL1\u0000?\u0000?\u0000TPIDR_EL1", j) : null;
                            }
                            case 14: {
                                return m == 1 ? Arm64Dis.disasm_str("CNTKCTL_EL1", j) : null;
                            }
                            default: {
                                return null;
                            }
                        }
                    }
                    case 1: {
                        return n != 0 || m != 0 ? null : Arm64Dis.disasm_str("CCSIDR_EL1\u0000CLIDR_EL1\u0000?\u0000?\u0000?\u0000?\u0000?\u0000AIDR_EL1", j);
                    }
                    case 2: {
                        return n != 0 || m != 0 ? null : Arm64Dis.disasm_str("CSSELR_EL1", j);
                    }
                    case 3: {
                        switch(n) {
                            case 0: {
                                return m == 0 ? Arm64Dis.disasm_str("?\u0000CTR_EL0\u0000?\u0000?\u0000?\u0000?\u0000?\u0000DCZID_EL0", j) : null;
                            }
                            case 4: {
                                switch(m) {
                                    case 2: {
                                        return Arm64Dis.disasm_str("NZCV\u0000DAIF", j);
                                    }
                                    case 4: {
                                        return Arm64Dis.disasm_str("FPCR\u0000FPSR", j);
                                    }
                                    case 5: {
                                        return Arm64Dis.disasm_str("DSPSR_EL0\u0000DLR_EL0", j);
                                    }
                                    default: {
                                        return null;
                                    }
                                }
                            }
                            case 9: {
                                switch(m) {
                                    case 12: {
                                        return Arm64Dis.disasm_str("PMCR_EL0\u0000PMCNTENSET_EL0\u0000PMCNTENCLR_EL0\u0000PMOVSCLR_EL0\u0000PMSWINC_EL0\u0000PMSELR_EL0\u0000PMCEID0_EL0\u0000PMCEID1_EL0", j);
                                    }
                                    case 13: {
                                        return Arm64Dis.disasm_str("PMCCNTR_EL0\u0000PMXEVTYPER_EL0\u0000PMXEVCNTR_EL0", j);
                                    }
                                    case 14: {
                                        return Arm64Dis.disasm_str("PMUSERENR_EL0\u0000?\u0000?\u0000PMOVSSET_EL0", j);
                                    }
                                    default: {
                                        return null;
                                    }
                                }
                            }
                            case 13: {
                                return m == 0 ? Arm64Dis.disasm_str("?\u0000?\u0000TPIDR_EL0\u0000TPIDRRO_EL0", j) : null;
                            }
                            case 14: {
                                switch(m) {
                                    case 0: {
                                        return Arm64Dis.disasm_str("CNTFRQ_EL0\u0000CNTPCT_EL0\u0000CNTVCT_EL0", j);
                                    }
                                    case 2: {
                                        return Arm64Dis.disasm_str("CNTP_TVAL_EL0\u0000CNTP_CTL_EL0\u0000CNTP_CVAL_EL0", j);
                                    }
                                    case 3: {
                                        return Arm64Dis.disasm_str("CNTV_TVAL_EL0\u0000CNTV_CTL_EL0\u0000CNTV_CVAL_EL0", j);
                                    }
                                    default: {
                                        return null;
                                    }
                                }
                            }
                            default: {
                                return null;
                            }
                        }
                    }
                    case 4: {
                        switch(n) {
                            case 0: {
                                return m == 0 ? Arm64Dis.disasm_str("VPIDR_EL2\u0000?\u0000?\u0000?\u0000?\u0000VMPIDR_EL2", j) : null;
                            }
                            case 1: {
                                switch(m) {
                                    case 0: {
                                        return Arm64Dis.disasm_str("SCTLR_EL2\u0000ACTLR_EL2", j);
                                    }
                                    case 1: {
                                        return Arm64Dis.disasm_str("HCR_EL2\u0000MDCR_EL2\u0000CPTR_EL2\u0000HSTR_EL2\u0000?\u0000?\u0000?\u0000HACR_EL2", j);
                                    }
                                    case 2: {
                                        return Arm64Dis.disasm_str("ZCR_EL2", j);
                                    }
                                    default: {
                                        return null;
                                    }
                                }
                            }
                            case 2: {
                                switch(m) {
                                    case 0: {
                                        return Arm64Dis.disasm_str("TTBR0_EL2\u0000?\u0000TCR_EL2", j);
                                    }
                                    case 1: {
                                        return Arm64Dis.disasm_str("VTTBR0_EL2\u0000?\u0000VTCR_EL2", j);
                                    }
                                    default: {
                                        return null;
                                    }
                                }
                            }
                            case 3: {
                                return m == 0 ? Arm64Dis.disasm_str("DACR32_EL2", j) : null;
                            }
                            case 4: {
                                switch(m) {
                                    case 0: {
                                        return Arm64Dis.disasm_str("SPSR_EL2\u0000ELR_EL2", j);
                                    }
                                    case 1: {
                                        return Arm64Dis.disasm_str("SP_EL1", j);
                                    }
                                    case 3: {
                                        return Arm64Dis.disasm_str("SPSR_irq\u0000SPSR_abt\u0000SPSR_und\u0000SPSR_fiq", j);
                                    }
                                    default: {
                                        return null;
                                    }
                                }
                            }
                            case 5: {
                                switch(m) {
                                    case 0: {
                                        return Arm64Dis.disasm_str("?\u0000IFSR32_EL2", j);
                                    }
                                    case 1: {
                                        return Arm64Dis.disasm_str("AFSR0_EL2\u0000AFSR1_EL2", j);
                                    }
                                    case 2: {
                                        return Arm64Dis.disasm_str("ESR_EL2\u0000?\u0000?\u0000VSESR_EL2", j);
                                    }
                                    case 3: {
                                        return Arm64Dis.disasm_str("FPEXC32_EL2", j);
                                    }
                                    default: {
                                        return null;
                                    }
                                }
                            }
                            case 6: {
                                return m == 0 ? Arm64Dis.disasm_str("FAR_EL2\u0000?\u0000?\u0000?\u0000HPFAR_EL2", j) : null;
                            }
                            case 9: {
                                return m == 9 ? Arm64Dis.disasm_str("PMSCR_EL2", j) : null;
                            }
                            case 10: {
                                switch(m) {
                                    case 2: {
                                        return Arm64Dis.disasm_str("MAIR_EL2", j);
                                    }
                                    case 3: {
                                        return Arm64Dis.disasm_str("AMAIR_EL2", j);
                                    }
                                    default: {
                                        return null;
                                    }
                                }
                            }
                            case 12: {
                                switch(m) {
                                    case 0: {
                                        return Arm64Dis.disasm_str("VBAR_EL2\u0000RVBAR_EL2\u0000RMR_EL2", j);
                                    }
                                    case 1: {
                                        return Arm64Dis.disasm_str("?\u0000VDISR_EL2", j);
                                    }
                                    case 8: {
                                        return Arm64Dis.disasm_str("ICH_AP0R0_EL2\u0000ICH_AP0R1_EL2\u0000ICH_AP0R2_EL2\u0000ICH_AP0R3_EL2", j);
                                    }
                                    case 9: {
                                        return Arm64Dis.disasm_str("ICH_AP1R0_EL2\u0000ICH_AP1R1_EL2\u0000ICH_AP1R2_EL2\u0000ICH_AP1R3_EL2\u0000ICC_SRE_EL2", j);
                                    }
                                    case 11: {
                                        return Arm64Dis.disasm_str("ICH_HCR_EL2\u0000ICH_VTR_EL2\u0000ICH_MISR_EL2\u0000ICH_EISR_EL2\u0000?\u0000ICH_ELRSR_EL2\u0000?\u0000ICH_VMCR_EL2", j);
                                    }
                                    case 12: {
                                        return Arm64Dis.disasm_str("ICH_LR0_EL2\u0000ICH_LR1_EL2\u0000ICH_LR2_EL2\u0000ICH_LR3_EL2\u0000ICH_LR4_EL2\u0000ICH_LR5_EL2\u0000ICH_LR6_EL2\u0000ICH_LR7_EL2", j);
                                    }
                                    case 13: {
                                        return Arm64Dis.disasm_str("ICH_LR8_EL2\u0000ICH_LR9_EL2\u0000ICH_LR10_EL2\u0000ICH_LR11_EL2\u0000ICH_LR12_EL2\u0000ICH_LR13_EL2\u0000ICH_LR14_EL2\u0000ICH_LR15_EL2", j);
                                    }
                                    default: {
                                        return null;
                                    }
                                }
                            }
                            case 13: {
                                return m == 0 ? Arm64Dis.disasm_str("?\u0000CONTEXTIDR_EL2\u0000TPIDR_EL2", j) : null;
                            }
                            case 14: {
                                switch(m) {
                                    case 0: {
                                        return Arm64Dis.disasm_str("?\u0000?\u0000?\u0000CNTVOFF_EL2", j);
                                    }
                                    case 1: {
                                        return Arm64Dis.disasm_str("CNTHCTL_EL2", j);
                                    }
                                    case 2: {
                                        return Arm64Dis.disasm_str("CNTHP_TVAL_EL2\u0000CNTHP_CTL_EL2\u0000CNTHP_CVAL_EL2", j);
                                    }
                                    case 3: {
                                        return Arm64Dis.disasm_str("CNTHV_TVAL_EL2\u0000CNTHV_CTL_EL2\u0000CNTHV_CVAL_EL2", j);
                                    }
                                    default: {
                                        return null;
                                    }
                                }
                            }
                            default: {
                                return null;
                            }
                        }
                    }
                    case 5: {
                        return n != 4 || m != 0 ? null : Arm64Dis.disasm_str("SPSR_EL12\u0000ELR_EL12", j);
                    }
                    case 6: {
                        return n != 4 || m != 1 ? null : Arm64Dis.disasm_str("SP_EL2", j);
                    }
                    case 7: {
                        return n != 14 || m != 2 ? null : Arm64Dis.disasm_str("CNTPS_TVAL_EL1\u0000CNTPS_CTL_EL1\u0000CNTPS_CVAL_EL1", j);
                    }
                    default: {
                        return null;
                    }
                }
            }
            default: {
                return null;
            }
        }
    }

    private static final int line() {
        return 0;
    }

    private static void sprintf_x(StringBuilder str, int val) {
        if(val < 0 && val >= 0xF0000001) {
            val = -val;
            str.append('-');
        }
        str.append("0x");
        str.append(Integer.toHexString(val).toUpperCase(Locale.US));
    }

    private static void sprintf_x(StringBuilder str, long val) {
        if(val < 0L && val >= 0xF000000000000001L) {
            val = -val;
            str.append('-');
        }
        str.append("0x");
        str.append(Long.toHexString(val).toUpperCase(Locale.US));
    }
}

