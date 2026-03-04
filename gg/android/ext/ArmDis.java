package android.ext;

import java.util.Locale;

public class ArmDis {
    static class Arm {
        static final int[] NUM;
        static final String[] STR;

        static {
            Arm.NUM = new int[]{0xE1A00000, -1, 0xE7F000F0, 0xFFF000F0, 0x12FFF10, 0xFFFFFF0, 0x90, 0xFE000F0, 0x200090, 0xFE000F0, 0x1000090, 0xFB00FF0, 0x800090, 0xFA000F0, 0xA00090, 0xFA000F0, 0x320F005, 0xFFFFFFF, 0xE1000070, 0xFFF000F0, 0xE1000040, 0xFFF00FF0, 0xE1200040, 0xFFF00FF0, 0xE1400040, 0xFFF00FF0, 0xE1000240, 0xFFF00FF0, 0xE1200240, 0xFFF00FF0, 0xE1400240, 0xFFF00FF0, 0x160006E, 0xFFFFFFF, 0x1400070, 0xFF000F0, 0x710F010, 0xFF0F0F0, 0x730F010, 0xFF0F0F0, 0xF410F000, 0xFC70F000, 0xF450F000, 0xFD70F000, 0x320F0F0, 0xFFFFFF0, 0xF57FF051, -13, 0xF57FF041, -13, 0xF57FF050, -16, 0xF57FF040, -16, 0xF57FF060, -16, 0x7C0001F, 0xFE0007F, 0x7C00010, 0xFE00070, 0x600090, 0xFF000F0, 0x2000B0, 0xF3000F0, 0x300090, 0xF3000F0, 0x300090, 0xF300090, 0x3000000, 0xFF00000, 0x3400000, 0xFF00000, 0x6FF0F30, 0xFFF0FF0, 0x7A00050, 0xFA00070, 0x1600070, 0xFF000F0, 0xF57FF01F, -1, 0x1D00F9F, 0xFF00FFF, 0x1B00F9F, 0xFF00FFF, 0x1F00F9F, 0xFF00FFF, 0x1C00F90, 0xFF00FF0, 0x1A00F90, 0xFF00FF0, 0x1E00F90, 0xFF00FF0, 0x320F001, 0xFFFFFFF, 0x320F002, 0xFFFFFFF, 0x320F003, 0xFFFFFFF, 0x320F004, 0xFFFFFFF, 0x320F000, 0xFFFFF00, 0xF1080000, -449, 0xF10A0000, -480, 0xF10C0000, -449, 0xF10E0000, -480, 0xF1000000, 0xFFF1FE20, 0x6800010, 0xFF00FF0, 0x6800010, 0xFF00070, 0x6800050, 0xFF00FF0, 0x6800050, 0xFF00070, 0x1900F9F, 0xFF00FFF, 0x6200F10, 0xFF00FF0, 0x6200F90, 0xFF00FF0, 0x6200F30, 0xFF00FF0, 0x6200F70, 0xFF00FF0, 0x6200FF0, 0xFF00FF0, 0x6200F50, 0xFF00FF0, 0x6100F10, 0xFF00FF0, 0x6100F90, 0xFF00FF0, 0x6100F30, 0xFF00FF0, 0x6300F10, 0xFF00FF0, 0x6300F90, 0xFF00FF0, 0x6300F30, 0xFF00FF0, 0x6300F70, 0xFF00FF0, 0x6300FF0, 0xFF00FF0, 0x6300F50, 0xFF00FF0, 0x6100F70, 0xFF00FF0, 0x6100FF0, 0xFF00FF0, 0x6100F50, 0xFF00FF0, 0x6500F10, 0xFF00FF0, 0x6500F90, 0xFF00FF0, 0x6500F30, 0xFF00FF0, 0x6700F10, 0xFF00FF0, 0x6700F90, 0xFF00FF0, 0x6700F30, 0xFF00FF0, 0x6700F70, 0xFF00FF0, 0x6700FF0, 0xFF00FF0, 0x6700F50, 0xFF00FF0, 0x6600F10, 0xFF00FF0, 0x6600F90, 0xFF00FF0, 0x6600F30, 0xFF00FF0, 0x6600F70, 0xFF00FF0, 0x6600FF0, 0xFF00FF0, 0x6600F50, 0xFF00FF0, 0x6500F70, 0xFF00FF0, 0x6500FF0, 0xFF00FF0, 0x6500F50, 0xFF00FF0, 0x6BF0F30, 0xFFF0FF0, 0x6BF0FB0, 0xFFF0FF0, 0x6FF0FB0, 0xFFF0FF0, 0xF8100A00, 0xFE50FFFF, 0x6BF0070, 0xFFF0FF0, 0x6BF0470, 0xFFF0FF0, 0x6BF0870, 0xFFF0FF0, 0x6BF0C70, 0xFFF0FF0, 0x68F0070, 0xFFF0FF0, 110036080, 0xFFF0FF0, 0x68F0870, 0xFFF0FF0, 0x68F0C70, 0xFFF0FF0, 0x6AF0070, 0xFFF0FF0, 0x6AF0470, 0xFFF0FF0, 0x6AF0870, 0xFFF0FF0, 0x6AF0C70, 0xFFF0FF0, 0x6FF0070, 0xFFF0FF0, 0x6FF0470, 0xFFF0FF0, 0x6FF0870, 0xFFF0FF0, 0x6FF0C70, 0xFFF0FF0, 0x6CF0070, 0xFFF0FF0, 0x6CF0470, 0xFFF0FF0, 0x6CF0870, 0xFFF0FF0, 0x6CF0C70, 0xFFF0FF0, 0x6EF0070, 0xFFF0FF0, 0x6EF0470, 0xFFF0FF0, 0x6EF0870, 0xFFF0FF0, 0x6EF0C70, 0xFFF0FF0, 0x6B00070, 0xFF00FF0, 0x6B00470, 0xFF00FF0, 0x6B00870, 0xFF00FF0, 0x6B00C70, 0xFF00FF0, 0x6800070, 0xFF00FF0, 109053040, 0xFF00FF0, 0x6800870, 0xFF00FF0, 0x6800C70, 0xFF00FF0, 0x6A00070, 0xFF00FF0, 0x6A00470, 0xFF00FF0, 0x6A00870, 0xFF00FF0, 0x6A00C70, 0xFF00FF0, 0x6F00070, 0xFF00FF0, 0x6F00470, 0xFF00FF0, 0x6F00870, 0xFF00FF0, 0x6F00C70, 0xFF00FF0, 0x6C00070, 0xFF00FF0, 0x6C00470, 0xFF00FF0, 0x6C00870, 0xFF00FF0, 0x6C00C70, 0xFF00FF0, 0x6E00070, 0xFF00FF0, 0x6E00470, 0xFF00FF0, 0x6E00870, 0xFF00FF0, 0x6E00C70, 0xFF00FF0, 0x6800FB0, 0xFF00FF0, 0xF1010000, 0xFFFFFC00, 0x700F010, 0xFF0F0D0, 0x700F050, 0xFF0F0D0, 0x7000010, 0xFF000D0, 0x7400010, 0xFF000D0, 0x7000050, 0xFF000D0, 0x7400050, 0xFF000D0, 0x750F010, 0xFF0F0D0, 0x7500010, 0xFF000D0, 0x75000D0, 0xFF000D0, 0xF84D0500, 0xFE5FFFE0, 0x6A00010, 0xFE00FF0, 0x6A00010, 0xFE00070, 0x6A00050, 0xFE00070, 0x6A00F30, 0xFF00FF0, 0x1800F90, 0xFF00FF0, 0x400090, 0xFF000F0, 0x780F010, 0xFF0F0F0, 0x7800010, 0xFF000F0, 0x6E00010, 0xFE00FF0, 0x6E00010, 0xFE00070, 0x6E00050, 0xFE00070, 0x6E00F30, 0xFF00FF0, 0x12FFF20, 0xFFFFFF0, 0xE1200070, 0xFFF000F0, 0xFA000000, 0xFE000000, 0x12FFF30, 0xFFFFFF0, 0x16F0F10, 0xFFF0FF0, 0xD0, 0xE1000F0, 0xF0, 0xE1000F0, 0xF550F000, 0xFD70F000, 0x1000080, 0xFF000F0, 0x10000A0, 0xFF000F0, 0x10000C0, 0xFF000F0, 0x10000E0, 0xFF000F0, 0x1200080, 0xFF000F0, 0x12000C0, 0xFF000F0, 0x1400080, 0xFF000F0, 0x14000A0, 0xFF000F0, 0x14000C0, 0xFF000F0, 0x14000E0, 0xFF000F0, 0x1600080, 0xFF0F0F0, 0x16000A0, 0xFF0F0F0, 0x16000C0, 0xFF0F0F0, 0x16000E0, 0xFF0F0F0, 0x12000A0, 0xFF0F0F0, 0x12000E0, 0xFF0F0F0, 0x1000050, 0xFF00FF0, 0x1400050, 0xFF00FF0, 0x1200050, 0xFF00FF0, 0x1600050, 0xFF00FF0, 0x52D0004, 0xFFF0FFF, 0x4400000, 0xE500000, 0x4000000, 0xE500000, 0x6400000, 0xE500FF0, 0x6000000, 0xE500FF0, 0x4400000, 0xC500010, 0x4000000, 0xC500010, 0x4400000, 0xE500000, 0x6400000, 0xE500010, 0x4000B0, 0xE5000F0, 0xB0, 0xE500FF0, 0x500090, 0xE5000F0, 0x500090, 0xE500090, 0x100090, 0xE500FF0, 0x100090, 0xE500F90, 0x2000000, 0xFE00000, 0, 0xFE00010, 16, 0xFE00090, 0x2200000, 0xFE00000, 0x200000, 0xFE00010, 0x200010, 0xFE00090, 0x2400000, 0xFE00000, 0x400000, 0xFE00010, 0x400010, 0xFE00090, 0x2600000, 0xFE00000, 0x600000, 0xFE00010, 0x600010, 0xFE00090, 0x2800000, 0xFE00000, 0x800000, 0xFE00010, 0x800010, 0xFE00090, 0x2A00000, 0xFE00000, 0xA00000, 0xFE00010, 0xA00010, 0xFE00090, 0x2C00000, 0xFE00000, 0xC00000, 0xFE00010, 0xC00010, 0xFE00090, 0x2E00000, 0xFE00000, 0xE00000, 0xFE00010, 0xE00010, 0xFE00090, 0x120F200, 0xFB0F200, 0x120F000, 0xDB0F000, 0x1000000, 0xFB00CFF, 0x3100000, 0xFF00000, 0x1100000, 0xFF00010, 0x1100010, 0xFF00090, 0x3300000, 0xFF00000, 0x1300000, 0xFF00010, 0x1300010, 0xFF00090, 0x3500000, 0xFF00000, 0x1500000, 0xFF00010, 0x1500010, 0xFF00090, 0x3700000, 0xFF00000, 0x1700000, 0xFF00010, 0x1700010, 0xFF00090, 0x3800000, 0xFE00000, 0x1800000, 0xFE00010, 0x1800010, 0xFE00090, 0x3A00000, 0xFEF0000, 0x1A00000, 0xDEF0FF0, 0x1A00000, 0xDEF0060, 0x1A00020, 0xDEF0060, 0x1A00040, 0xDEF0060, 0x1A00060, 0xDEF0FF0, 0x1A00060, 0xDEF0060, 0x3C00000, 0xFE00000, 0x1C00000, 0xFE00010, 0x1C00010, 0xFE00090, 0x3E00000, 0xFE00000, 0x1E00000, 0xFE00010, 0x1E00010, 0xFE00090, 0x6000010, 0xE000010, 0x49D0004, 0xFFF0FFF, 0x4500000, 0xC500000, 0x4300000, 0xD700000, 0x4100000, 0xC500000, 0x92D0001, 0xFFFFFFF, 0x92D0002, 0xFFFFFFF, 0x92D0004, 0xFFFFFFF, 0x92D0008, 0xFFFFFFF, 0x92D0010, 0xFFFFFFF, 0x92D0020, 0xFFFFFFF, 0x92D0040, 0xFFFFFFF, 0x92D0080, 0xFFFFFFF, 0x92D0100, 0xFFFFFFF, 0x92D0200, 0xFFFFFFF, 0x92D0400, 0xFFFFFFF, 0x92D0800, 0xFFFFFFF, 0x92D1000, 0xFFFFFFF, 0x92D2000, 0xFFFFFFF, 0x92D4000, 0xFFFFFFF, 0x92D8000, 0xFFFFFFF, 0x92D0000, 0xFFF0000, 0x8800000, 0xFF00000, 0x8000000, 0xE100000, 0x8BD0001, 0xFFFFFFF, 0x8BD0002, 0xFFFFFFF, 0x8BD0004, 0xFFFFFFF, 0x8BD0008, 0xFFFFFFF, 0x8BD0010, 0xFFFFFFF, 0x8BD0020, 0xFFFFFFF, 0x8BD0040, 0xFFFFFFF, 0x8BD0080, 0xFFFFFFF, 0x8BD0100, 0xFFFFFFF, 0x8BD0200, 0xFFFFFFF, 0x8BD0400, 0xFFFFFFF, 0x8BD0800, 0xFFFFFFF, 0x8BD1000, 0xFFFFFFF, 0x8BD2000, 0xFFFFFFF, 0x8BD4000, 0xFFFFFFF, 0x8BD8000, 0xFFFFFFF, 0x8BD0000, 0xFFF0000, 0x8900000, 0xF900000, 0x8100000, 0xE100000, 0xA000000, 0xE000000, 0xF000000, 0xF000000, 0, 0};
            Arm.STR = new String[]{"NOP\t ; (MOV R0, R0)", "UDF#%e", "BX%c\t %0-3r", "MUL%20\'S%c\t %16-19R, %0-3R, %8-11R", "MLA%20\'S%c\t %16-19R, %0-3R, %8-11R, %12-15R", "SWP%22\'B%c\t %12-15RU, %0-3Ru, [%16-19RuU]", "%22?SUMULL%20\'S%c\t %12-15Ru, %16-19Ru, %0-3R, %8-11R", "%22?SUMLAL%20\'S%c\t %12-15Ru, %16-19Ru, %0-3R, %8-11R", "SEVL", "HLT0x%16-19X%12-15X%8-11X%0-3X", "CRC32B%12-15R, %16-19R, %0-3R", "CRC32H%12-15R, %16-19R, %0-3R", "CRC32W%12-15R, %16-19R, %0-3R", "CRC32CB%12-15R, %16-19R, %0-3R", "CRC32CH%12-15R, %16-19R, %0-3R", "CRC32CW%12-15R, %16-19R, %0-3R", "ERET%c", "HVC%c\t %e", "SDIV%c\t %16-19r, %0-3r, %8-11r", "UDIV%c\t %16-19r, %0-3r, %8-11r", "PLDW\t %a", "PLI\t %P", "DBG%c\t #%0-3d", "DMB\t %U", "DSB\t %U", "DMB\t %U", "DSB\t %U", "ISB\t %U", "BFC%c\t %12-15R, %E", "BFI%c\t %12-15R, %0-3r, %E", "MLS%c\t %16-19R, %0-3R, %8-11R, %12-15R", "STRHT%c\t %12-15R, %S", "\t \t ; <UNDEFINED> instruction: %0-31x", "LDR%6\'S%5?HBT%c\t %12-15R, %S", "MOVW%c\t %12-15R, %V", "MOVT%c\t %12-15R, %V", "RBIT%c\t %12-15R, %0-3R", "%22?USBFX%c\t %12-15r, %0-3r, #%7-11d, #%16-20W", "SMC%c\t %e", "CLREX", "LDREXB%c\t %12-15R, [%16-19R]", "LDREXD%c\t %12-15r, [%16-19R]", "LDREXH%c\t %12-15R, [%16-19R]", "STREXB%c\t %12-15R, %0-3R, [%16-19R]", "STREXD%c\t %12-15R, %0-3r, [%16-19R]", "STREXH%c\t %12-15R, %0-3R, [%16-19R]", "YIELD%c", "WFE%c", "WFI%c", "SEV%c", "NOP%c\t {%0-7d}", "CPSIE%8\'A%7\'I%6\'F", "CPSIE%8\'A%7\'I%6\'F,#%0-4d", "CPSID%8\'A%7\'I%6\'F", "CPSID%8\'A%7\'I%6\'F,#%0-4d", "CPS#%0-4d", "PKHBT%c\t %12-15R, %16-19R, %0-3R", "PKHBT%c\t %12-15R, %16-19R, %0-3R, LSL #%7-11d", "PKHTB%c\t %12-15R, %16-19R, %0-3R, ASR #32", "PKHTB%c\t %12-15R, %16-19R, %0-3R, ASR #%7-11d", "LDREX%c\t r%12-15d, [%16-19R]", "QADD16%c\t %12-15R, %16-19R, %0-3R", "QADD8%c\t %12-15R, %16-19R, %0-3R", "QASX%c\t %12-15R, %16-19R, %0-3R", "QSUB16%c\t %12-15R, %16-19R, %0-3R", "QSUB8%c\t %12-15R, %16-19R, %0-3R", "QSAX%c\t %12-15R, %16-19R, %0-3R", "SADD16%c\t %12-15R, %16-19R, %0-3R", "SADD8%c\t %12-15R, %16-19R, %0-3R", "SASX%c\t %12-15R, %16-19R, %0-3R", "SHADD16%c\t %12-15R, %16-19R, %0-3R", "SHADD8%c\t %12-15R, %16-19R, %0-3R", "SHASX%c\t %12-15R, %16-19R, %0-3R", "SHSUB16%c\t %12-15R, %16-19R, %0-3R", "SHSUB8%c\t %12-15R, %16-19R, %0-3R", "SHSAX%c\t %12-15R, %16-19R, %0-3R", "SSUB16%c\t %12-15R, %16-19R, %0-3R", "SSUB8%c\t %12-15R, %16-19R, %0-3R", "SSAX%c\t %12-15R, %16-19R, %0-3R", "UADD16%c\t %12-15R, %16-19R, %0-3R", "UADD8%c\t %12-15R, %16-19R, %0-3R", "UASX%c\t %12-15R, %16-19R, %0-3R", "UHADD16%c\t %12-15R, %16-19R, %0-3R", "UHADD8%c\t %12-15R, %16-19R, %0-3R", "UHASX%c\t %12-15R, %16-19R, %0-3R", "UHSUB16%c\t %12-15R, %16-19R, %0-3R", "UHSUB8%c\t %12-15R, %16-19R, %0-3R", "UHSAX%c\t %12-15R, %16-19R, %0-3R", "UQADD16%c\t %12-15R, %16-19R, %0-3R", "UQADD8%c\t %12-15R, %16-19R, %0-3R", "UQASX%c\t %12-15R, %16-19R, %0-3R", "UQSUB16%c\t %12-15R, %16-19R, %0-3R", "UQSUB8%c\t %12-15R, %16-19R, %0-3R", "UQSAX%c\t %12-15R, %16-19R, %0-3R", "USUB16%c\t %12-15R, %16-19R, %0-3R", "USUB8%c\t %12-15R, %16-19R, %0-3R", "USAX%c\t %12-15R, %16-19R, %0-3R", "REV%c\t %12-15R, %0-3R", "REV16%c\t %12-15R, %0-3R", "REVSH%c\t %12-15R, %0-3R", "RFE%23?ID%24?BA\t %16-19r%21\'!", "SXTH%c\t %12-15R, %0-3R", "SXTH%c\t %12-15R, %0-3R, ROR #8", "SXTH%c\t %12-15R, %0-3R, ROR #16", "SXTH%c\t %12-15R, %0-3R, ROR #24", "SXTB16%c\t %12-15R, %0-3R", "SXTB16%c\t %12-15R, %0-3R, ROR #8", "SXTB16%c\t %12-15R, %0-3R, ROR #16", "SXTB16%c\t %12-15R, %0-3R, ROR #24", "SXTB%c\t %12-15R, %0-3R", "SXTB%c\t %12-15R, %0-3R, ROR #8", "SXTB%c\t %12-15R, %0-3R, ROR #16", "SXTB%c\t %12-15R, %0-3R, ROR #24", "UXTH%c\t %12-15R, %0-3R", "UXTH%c\t %12-15R, %0-3R, ROR #8", "UXTH%c\t %12-15R, %0-3R, ROR #16", "UXTH%c\t %12-15R, %0-3R, ROR #24", "UXTB16%c\t %12-15R, %0-3R", "UXTB16%c\t %12-15R, %0-3R, ROR #8", "UXTB16%c\t %12-15R, %0-3R, ROR #16", "UXTB16%c\t %12-15R, %0-3R, ROR #24", "UXTB%c\t %12-15R, %0-3R", "UXTB%c\t %12-15R, %0-3R, ROR #8", "UXTB%c\t %12-15R, %0-3R, ROR #16", "UXTB%c\t %12-15R, %0-3R, ROR #24", "SXTAH%c\t %12-15R, %16-19r, %0-3R", "SXTAH%c\t %12-15R, %16-19r, %0-3R, ROR #8", "SXTAH%c\t %12-15R, %16-19r, %0-3R, ROR #16", "SXTAH%c\t %12-15R, %16-19r, %0-3R, ROR #24", "SXTAB16%c\t %12-15R, %16-19r, %0-3R", "SXTAB16%c\t %12-15R, %16-19r, %0-3R, ROR #8", "SXTAB16%c\t %12-15R, %16-19r, %0-3R, ROR #16", "SXTAB16%c\t %12-15R, %16-19r, %0-3R, ROR #24", "SXTAB%c\t %12-15R, %16-19r, %0-3R", "SXTAB%c\t %12-15R, %16-19r, %0-3R, ROR #8", "SXTAB%c\t %12-15R, %16-19r, %0-3R, ROR #16", "SXTAB%c\t %12-15R, %16-19r, %0-3R, ROR #24", "UXTAH%c\t %12-15R, %16-19r, %0-3R", "UXTAH%c\t %12-15R, %16-19r, %0-3R, ROR #8", "UXTAH%c\t %12-15R, %16-19r, %0-3R, ROR #16", "UXTAH%c\t %12-15R, %16-19r, %0-3R, ROR #24", "UXTAB16%c\t %12-15R, %16-19r, %0-3R", "UXTAB16%c\t %12-15R, %16-19r, %0-3R, ROR #8", "UXTAB16%c\t %12-15R, %16-19r, %0-3R, ROR #16", "UXTAB16%c\t %12-15R, %16-19r, %0-3R, ROR #24", "UXTAB%c\t %12-15R, %16-19r, %0-3R", "UXTAB%c\t %12-15R, %16-19r, %0-3R, ROR #8", "UXTAB%c\t %12-15R, %16-19r, %0-3R, ROR #16", "UXTAB%c\t %12-15R, %16-19r, %0-3R, ROR #24", "SEL%c\t %12-15R, %16-19R, %0-3R", "SETEND%9?BLE", "SMUAD%5\'X%c\t %16-19R, %0-3R, %8-11R", "SMUSD%5\'X%c\t %16-19R, %0-3R, %8-11R", "SMLAD%5\'X%c\t %16-19R, %0-3R, %8-11R, %12-15R", "SMLALD%5\'X%c\t %12-15Ru, %16-19Ru, %0-3R, %8-11R", "SMLSD%5\'X%c\t %16-19R, %0-3R, %8-11R, %12-15R", "SMLSLD%5\'X%c\t %12-15Ru, %16-19Ru, %0-3R, %8-11R", "SMMUL%5\'R%c\t %16-19R, %0-3R, %8-11R", "SMMLA%5\'R%c\t %16-19R, %0-3R, %8-11R, %12-15R", "SMMLS%5\'R%c\t %16-19R, %0-3R, %8-11R, %12-15R", "SRS%23?ID%24?BA\t %16-19r%21\'!, #%0-4d", "SSAT%c\t %12-15R, #%16-20W, %0-3R", "SSAT%c\t %12-15R, #%16-20W, %0-3R, LSL #%7-11d", "SSAT%c\t %12-15R, #%16-20W, %0-3R, ASR #%7-11d", "SSAT16%c\t %12-15r, #%16-19W, %0-3r", "STREX%c\t %12-15R, %0-3R, [%16-19R]", "UMAAL%c\t %12-15R, %16-19R, %0-3R, %8-11R", "USAD8%c\t %16-19R, %0-3R, %8-11R", "USADA8%c\t %16-19R, %0-3R, %8-11R, %12-15R", "USAT%c\t %12-15R, #%16-20d, %0-3R", "USAT%c\t %12-15R, #%16-20d, %0-3R, LSL #%7-11d", "USAT%c\t %12-15R, #%16-20d, %0-3R, ASR #%7-11d", "USAT16%c\t %12-15R, #%16-19d, %0-3R", "BXJ%c\t %0-3R", "BKPT\t 0x%16-19X%12-15X%8-11X%0-3X", "BLX\t %B", "BLX%c\t %0-3R", "CLZ%c\t %12-15R, %0-3R", "LDRD%c\t %12-15r, %s", "STRD%c\t %12-15r, %s", "PLD%a", "SMLABB%c\t %16-19R, %0-3R, %8-11R, %12-15R", "SMLATB%c\t %16-19R, %0-3R, %8-11R, %12-15R", "SMLABT%c\t %16-19R, %0-3R, %8-11R, %12-15R", "SMLATT%c\t %16-19r, %0-3r, %8-11R, %12-15R", "SMLAWB%c\t %16-19R, %0-3R, %8-11R, %12-15R", "SMLAWT%c\t %16-19R, %0-3r, %8-11R, %12-15R", "SMLALBB%c\t %12-15Ru, %16-19Ru, %0-3R, %8-11R", "SMLALTB%c\t %12-15Ru, %16-19Ru, %0-3R, %8-11R", "SMLALBT%c\t %12-15Ru, %16-19Ru, %0-3R, %8-11R", "SMLALTT%c\t %12-15Ru, %16-19Ru, %0-3R, %8-11R", "SMULBB%c\t %16-19R, %0-3R, %8-11R", "SMULTB%c\t %16-19R, %0-3R, %8-11R", "SMULBT%c\t %16-19R, %0-3R, %8-11R", "SMULTT%c\t %16-19R, %0-3R, %8-11R", "SMULWB%c\t %16-19R, %0-3R, %8-11R", "SMULWT%c\t %16-19R, %0-3R, %8-11R", "QADD%c\t %12-15R, %0-3R, %16-19R", "QDADD%c\t %12-15R, %0-3R, %16-19R", "QSUB%c\t %12-15R, %0-3R, %16-19R", "QDSUB%c\t %12-15R, %0-3R, %16-19R", "PUSH%c\t {%12-15r}\t \t ; (STR%c %12-15r, %a)", "STRB%t%c\t %12-15R, %a", "STR%t%c\t %12-15r, %a", "STRB%t%c\t %12-15R, %a", "STR%t%c\t %12-15r, %a", "STRB%t%c\t %12-15R, %a", "STR%t%c\t %12-15r, %a", "STRB%c\t %12-15R, %a", "STRB%c\t %12-15R, %a", "STRH%c\t %12-15R, %s", "STRH%c\t %12-15R, %s", "\t \t ; <UNDEFINED> instruction: %0-31x", "LDR%6\'S%5?HB%c\t %12-15R, %s", "\t \t ; <UNDEFINED> instruction: %0-31x", "LDR%6\'S%5?HB%c\t %12-15R, %s", "AND%20\'S%c\t %12-15r, %16-19r, %o", "AND%20\'S%c\t %12-15r, %16-19r, %o", "AND%20\'S%c\t %12-15R, %16-19R, %o", "EOR%20\'S%c\t %12-15r, %16-19r, %o", "EOR%20\'S%c\t %12-15r, %16-19r, %o", "EOR%20\'S%c\t %12-15R, %16-19R, %o", "SUB%20\'S%c\t %12-15r, %16-19r, %o", "SUB%20\'S%c\t %12-15r, %16-19r, %o", "SUB%20\'S%c\t %12-15R, %16-19R, %o", "RSB%20\'S%c\t %12-15r, %16-19r, %o", "RSB%20\'S%c\t %12-15r, %16-19r, %o", "RSB%20\'S%c\t %12-15R, %16-19R, %o", "ADD%20\'S%c\t %12-15r, %16-19r, %o", "ADD%20\'S%c\t %12-15r, %16-19r, %o", "ADD%20\'S%c\t %12-15R, %16-19R, %o", "ADC%20\'S%c\t %12-15r, %16-19r, %o", "ADC%20\'S%c\t %12-15r, %16-19r, %o", "ADC%20\'S%c\t %12-15R, %16-19R, %o", "SBC%20\'S%c\t %12-15r, %16-19r, %o", "SBC%20\'S%c\t %12-15r, %16-19r, %o", "SBC%20\'S%c\t %12-15R, %16-19R, %o", "RSC%20\'S%c\t %12-15r, %16-19r, %o", "RSC%20\'S%c\t %12-15r, %16-19r, %o", "RSC%20\'S%c\t %12-15R, %16-19R, %o", "MSR%c\t %C, %0-3r", "MSR%c\t %C, %o", "MRS%c\t %12-15R, %R", "TST%p%c\t %16-19r, %o", "TST%p%c\t %16-19r, %o", "TST%p%c\t %16-19R, %o", "TEQ%p%c\t %16-19r, %o", "TEQ%p%c\t %16-19r, %o", "TEQ%p%c\t %16-19R, %o", "CMP%p%c\t %16-19r, %o", "CMP%p%c\t %16-19r, %o", "CMP%p%c\t %16-19R, %o", "CMN%p%c\t %16-19r, %o", "CMN%p%c\t %16-19r, %o", "CMN%p%c\t %16-19R, %o", "ORR%20\'S%c\t %12-15r, %16-19r, %o", "ORR%20\'S%c\t %12-15r, %16-19r, %o", "ORR%20\'S%c\t %12-15R, %16-19R, %o", "MOV%20\'S%c\t %12-15r, %o", "MOV%20\'S%c\t %12-15r, %0-3r", "LSL%20\'S%c\t %12-15R, %q", "LSR%20\'S%c\t %12-15R, %q", "ASR%20\'S%c\t %12-15R, %q", "RRX%20\'S%c\t %12-15r, %0-3r", "ROR%20\'S%c\t %12-15R, %q", "BIC%20\'S%c\t %12-15r, %16-19r, %o", "BIC%20\'S%c\t %12-15r, %16-19r, %o", "BIC%20\'S%c\t %12-15R, %16-19R, %o", "MVN%20\'S%c\t %12-15r, %o", "MVN%20\'S%c\t %12-15r, %o", "MVN%20\'S%c\t %12-15R, %o", "\t \t ; <UNDEFINED> instruction: %0-31x", "POP%c\t {%12-15r}\t \t ; (LDR%c %12-15r, %a)", "LDRB%t%c\t %12-15R, %a", "LDRT%c\t %12-15R, %a", "LDR%c\t %12-15r, %a", "STMFD%c\t %16-19R!, %m", "STMFD%c\t %16-19R!, %m", "STMFD%c\t %16-19R!, %m", "STMFD%c\t %16-19R!, %m", "STMFD%c\t %16-19R!, %m", "STMFD%c\t %16-19R!, %m", "STMFD%c\t %16-19R!, %m", "STMFD%c\t %16-19R!, %m", "STMFD%c\t %16-19R!, %m", "STMFD%c\t %16-19R!, %m", "STMFD%c\t %16-19R!, %m", "STMFD%c\t %16-19R!, %m", "STMFD%c\t %16-19R!, %m", "STMFD%c\t %16-19R!, %m", "STMFD%c\t %16-19R!, %m", "STMFD%c\t %16-19R!, %m", "PUSH%c\t %m", "STM%c\t %16-19R%21\'!, %m%22\'^", "STM%23?ID%24?BA%c\t %16-19R%21\'!, %m%22\'^", "LDMFD%c\t %16-19R!, %m", "LDMFD%c\t %16-19R!, %m", "LDMFD%c\t %16-19R!, %m", "LDMFD%c\t %16-19R!, %m", "LDMFD%c\t %16-19R!, %m", "LDMFD%c\t %16-19R!, %m", "LDMFD%c\t %16-19R!, %m", "LDMFD%c\t %16-19R!, %m", "LDMFD%c\t %16-19R!, %m", "LDMFD%c\t %16-19R!, %m", "LDMFD%c\t %16-19R!, %m", "LDMFD%c\t %16-19R!, %m", "LDMFD%c\t %16-19R!, %m", "LDMFD%c\t %16-19R!, %m", "LDMFD%c\t %16-19R!, %m", "LDMFD%c\t %16-19R!, %m", "POP%c\t %m", "LDM%c\t %16-19R%21\'!, %m%22\'^", "LDM%23?ID%24?BA%c\t %16-19R%21\'!, %m%22\'^", "B%24\'L%c\t %b", "SVC%c\t %0-23x", "\t \t ; <UNDEFINED> instruction: %0-31x"};
        }

        static int getOpcode(ArmDis ret, long addr, String data, int dbg) {
            int rotate;
            Input armDis$Input0 = new Input(data);
            ret.input = armDis$Input0;
            int out = Coproc.getOpcode(ret, addr, data, false, dbg);
            if(out == -1) {
                out = Neon.getOpcode(ret, addr, data, false, dbg);
                if(out == -1) {
                    int[] NUM = Arm.NUM;
                    String[] STR = Arm.STR;
                    int i = 0;
                alab2:
                    while(true) {
                        if(i >= STR.length) {
                            return -1;
                        }
                        out = NUM[i * 2];
                        armDis$Input0.reset();
                        try {
                            String asm = STR[i];
                            int mask = NUM[i * 2 + 1];
                            if(mask != 0) {
                                int fail = -1;
                                int j = 0;
                                int v7 = asm.length();
                                while(j < v7) {
                                    int c = ArmDis.charAt(asm, v7, j);
                                    if(c <= 0x20) {
                                        c = 0x20;
                                        while(j < v7 && ArmDis.charAt(asm, v7, j + 1) <= 0x20) {
                                            ++j;
                                        }
                                    }
                                    if(c == 37) {
                                    label_32:
                                        int relative = 0;
                                        int relative = 0;
                                        ++j;
                                        int c = ArmDis.charAt(asm, v7, j);
                                    alab1:
                                        switch(c) {
                                            case 37: {
                                                if(!armDis$Input0.check('%')) {
                                                    goto label_460;
                                                }
                                                break;
                                            }
                                            case 0x30: 
                                            case 49: 
                                            case 50: 
                                            case 51: 
                                            case 52: 
                                            case 53: 
                                            case 54: 
                                            case 55: 
                                            case 56: 
                                            case 57: {
                                                int value = -1;
                                                int j_ = j;
                                                ret.valuep = out;
                                                j = ArmDis.decodeBitfield(asm, v7, j, ret, -1);
                                                int v14 = ret.widthp;
                                                int c = ArmDis.charAt(asm, v7, j);
                                                switch(c) {
                                                    case 0x3F: {
                                                        int v17 = armDis$Input0.getAndNext();
                                                        int k = 0;
                                                        int sk = 1 << v14;
                                                        while(k < sk) {
                                                            if(ArmDis.charAt(asm, v7, j + sk - k) == v17) {
                                                                value = k;
                                                                if(true) {
                                                                    break;
                                                                }
                                                            }
                                                            else {
                                                                ++k;
                                                            }
                                                        }
                                                        if(value == -1) {
                                                            goto label_460;
                                                        }
                                                        else {
                                                            ret.valuep = out;
                                                            ArmDis.decodeBitfield(asm, v7, j_, ret, value);
                                                            out = ret.valuep;
                                                            j += 1 << v14;
                                                            break alab1;
                                                        }
                                                        goto label_79;
                                                    }
                                                    case 87: {
                                                    label_91:
                                                        int v21 = armDis$Input0.getInt();
                                                        ret.valuep = out;
                                                        ArmDis.decodeBitfield(asm, v7, j_, ret, v21 - 1);
                                                        out = ret.valuep;
                                                        if((~((1 << v14) - 1) & v21 - 1) != 0) {
                                                            fail = j;
                                                        }
                                                        break alab1;
                                                    }
                                                    case 88: {
                                                        int v22 = armDis$Input0.getAndNext();
                                                        if((v22 < 0x30 || v22 > 57) && (v22 < 65 || v22 > 70)) {
                                                            goto label_460;
                                                        }
                                                        else {
                                                            ret.valuep = out;
                                                            ArmDis.decodeBitfield(asm, v7, j_, ret, (v22 < 0x30 || v22 > 57 ? v22 - 55 : v22 - 0x30));
                                                            out = ret.valuep;
                                                            break alab1;
                                                        }
                                                        goto label_104;
                                                    }
                                                    case 39: 
                                                    case 0x60: {
                                                        ++j;
                                                        int c = ArmDis.charAt(asm, v7, j);
                                                        if(c != 69) {
                                                            if(c == 76) {
                                                                if(armDis$Input0.check("LEQ")) {
                                                                    armDis$Input0.pos -= 3;
                                                                }
                                                                else if(armDis$Input0.check("LS") || armDis$Input0.check("LT") || armDis$Input0.check("LE")) {
                                                                    armDis$Input0.pos -= 2;
                                                                    break alab1;
                                                                }
                                                            }
                                                        }
                                                        else if(armDis$Input0.check("EQ")) {
                                                            armDis$Input0.pos -= 2;
                                                            break alab1;
                                                        }
                                                        if(armDis$Input0.check(((char)c)) == (c == 39)) {
                                                            out = ret.valuep;
                                                        }
                                                        break alab1;
                                                    }
                                                    case 98: {
                                                    label_104:
                                                        int value = armDis$Input0.getInt();
                                                        if((value & 7) != 0) {
                                                            fail = j;
                                                        }
                                                        ret.valuep = out;
                                                        ArmDis.decodeBitfield(asm, v7, j_, ret, value >> 3);
                                                        out = ret.valuep;
                                                        if((~((1 << v14) - 1) & value >> 3) != 0) {
                                                            fail = j;
                                                        }
                                                        break alab1;
                                                    }
                                                    case 100: {
                                                        int value = armDis$Input0.getInt();
                                                        ret.valuep = out;
                                                        ArmDis.decodeBitfield(asm, v7, j_, ret, value);
                                                        out = ret.valuep;
                                                        if((~((1 << v14) - 1) & value) != 0) {
                                                            fail = j;
                                                        }
                                                        break alab1;
                                                    }
                                                    case 82: 
                                                    case 84: 
                                                    case 0x72: {
                                                    label_79:
                                                        int value = armDis$Input0.index(ArmDis.REG);
                                                        if(value == -1) {
                                                            goto label_460;
                                                        }
                                                        else {
                                                            if(c == 84) {
                                                                --value;
                                                            }
                                                            if(ArmDis.charAt(asm, v7, j + 1) == 0x75) {
                                                                ++j;
                                                            }
                                                            if(ArmDis.charAt(asm, v7, j + 1) == 85) {
                                                                ++j;
                                                            }
                                                            ret.valuep = out;
                                                            ArmDis.decodeBitfield(asm, v7, j_, ret, value);
                                                            out = ret.valuep;
                                                            break alab1;
                                                        }
                                                        goto label_91;
                                                    }
                                                    case 120: {
                                                        if(armDis$Input0.check("0X")) {
                                                            int value = (int)armDis$Input0.getHex();
                                                            ret.valuep = out;
                                                            ArmDis.decodeBitfield(asm, v7, j_, ret, value);
                                                            out = ret.valuep;
                                                            if((~((1 << v14) - 1) & value) != 0) {
                                                                fail = j;
                                                            }
                                                        }
                                                        else {
                                                            goto label_460;
                                                        }
                                                        break alab1;
                                                    }
                                                    default: {
                                                        throw new RuntimeException("71 Invalid format char \'" + ((char)c) + "\'; " + i + "; " + j + "; \'" + asm + "\'");
                                                    }
                                                }
                                            }
                                            case 66: {
                                                if(armDis$Input0.check('+')) {
                                                    relative = 1;
                                                }
                                                else if(armDis$Input0.check('-')) {
                                                    relative = -1;
                                                }
                                                if(armDis$Input0.check("0X")) {
                                                    long address = armDis$Input0.getHex();
                                                    if(relative != 0) {
                                                        address = addr + ((long)relative) * address;
                                                    }
                                                    if((2L & address) != 0L) {
                                                        if((0x1000000 & mask) != 0 && (0x1000000 & out) == 0) {
                                                            goto label_460;
                                                        }
                                                        else {
                                                            out |= 0x1000000;
                                                            address -= 2L;
                                                        }
                                                    }
                                                    int offset = (int)(address - addr - 8L);
                                                    if((offset & 3) != 0) {
                                                        fail = j;
                                                    }
                                                    out |= 0xFFFFFF & offset >> 2;
                                                    int offset = offset >> 2 & 0xFF000000;
                                                    if(offset != 0) {
                                                        out |= 0x800000;
                                                        if(offset != 0xFF000000) {
                                                            fail = j;
                                                        }
                                                    }
                                                }
                                                else {
                                                    goto label_460;
                                                }
                                                break;
                                            }
                                            case 67: {
                                                while((0x2000200 & out) == 0x200) {
                                                    int v29 = ArmDis.bankedRegname(armDis$Input0);
                                                    if(v29 != -1) {
                                                        out |= (v29 & 0x30) << 4 | (v29 & 0x4F) << 16;
                                                        break alab1;
                                                    }
                                                    out |= 0x2000000;
                                                    if((0x2000000 & mask) == 0) {
                                                        continue;
                                                    }
                                                    goto label_460;
                                                }
                                                if(armDis$Input0.check('S')) {
                                                    out |= 0x400000;
                                                }
                                                else if(!armDis$Input0.check('C')) {
                                                    goto label_460;
                                                }
                                                if(armDis$Input0.check("PSR_")) {
                                                    if(armDis$Input0.check('F')) {
                                                        out |= 0x80000;
                                                    }
                                                    if(armDis$Input0.check('S')) {
                                                        out |= 0x40000;
                                                    }
                                                    if(armDis$Input0.check('X')) {
                                                        out |= 0x20000;
                                                    }
                                                    if(armDis$Input0.check('C')) {
                                                        out |= 0x10000;
                                                    }
                                                }
                                                else {
                                                    goto label_460;
                                                }
                                                break;
                                            }
                                            case 69: {
                                                if(armDis$Input0.check('#')) {
                                                    int v30 = armDis$Input0.getInt();
                                                    if((v30 & 0xFFFFFFE0) != 0) {
                                                        fail = j;
                                                    }
                                                    out |= v30 << 7;
                                                    if(armDis$Input0.check(", #")) {
                                                        int v31 = armDis$Input0.getInt();
                                                        if(v31 > 0) {
                                                            int msb = v31 + v30 - 1;
                                                            out |= msb << 16;
                                                            if((msb & 0xFFFFFFE0) != 0) {
                                                                fail = j;
                                                            }
                                                        }
                                                        else {
                                                            goto label_460;
                                                        }
                                                    }
                                                    else {
                                                        goto label_460;
                                                    }
                                                }
                                                else {
                                                    goto label_460;
                                                }
                                                break;
                                            }
                                            case 82: {
                                            label_270:
                                                int v38 = ArmDis.bankedRegname(armDis$Input0);
                                                if(v38 == -1) {
                                                    goto label_460;
                                                }
                                                else {
                                                    out |= (v38 & 0x30) << 4 | (v38 & 0x4F) << 16;
                                                    break;
                                                }
                                                goto label_274;
                                            }
                                            case 85: {
                                                if((out & 0xF0) != 0x60) {
                                                    int v47 = ArmDis.dataBarrierOption(armDis$Input0);
                                                    if(v47 == -1) {
                                                        goto label_460;
                                                    }
                                                    else {
                                                        out |= v47;
                                                        if((out & 15) != v47) {
                                                            fail = j;
                                                        }
                                                    }
                                                }
                                                else if(armDis$Input0.check("SY")) {
                                                    out |= 0x6F;
                                                }
                                                else if(armDis$Input0.check('#')) {
                                                    int v46 = armDis$Input0.getInt();
                                                    out |= v46 & 15 | 0x60;
                                                    if((v46 & -16) != 0) {
                                                        fail = j;
                                                    }
                                                }
                                                else {
                                                    goto label_460;
                                                }
                                                break;
                                            }
                                            case 86: {
                                                if(armDis$Input0.check('#')) {
                                                    int v48 = armDis$Input0.getInt();
                                                    out |= (0xF000 & v48) << 4 | v48 & 0xFFF;
                                                    if((0xFFFF0000 & v48) != 0) {
                                                        fail = j;
                                                    }
                                                }
                                                else {
                                                    goto label_460;
                                                }
                                                break;
                                            }
                                            case 80: 
                                            case 97: {
                                                if(armDis$Input0.check('[')) {
                                                    int v33 = armDis$Input0.index(ArmDis.REG);
                                                    if(v33 == -1) {
                                                        goto label_460;
                                                    }
                                                    else {
                                                        out |= v33 << 16;
                                                        while((0xF0000 & out) == 0xF0000 && (0x2000000 & out) == 0) {
                                                            if(armDis$Input0.check(",#")) {
                                                                if(c != 80) {
                                                                    out |= 0x1000000;
                                                                }
                                                                if(!armDis$Input0.check('-')) {
                                                                    out |= 0x800000;
                                                                }
                                                                int v34 = armDis$Input0.getInt();
                                                                out |= v34;
                                                                if(armDis$Input0.check(']')) {
                                                                    if(armDis$Input0.check('!')) {
                                                                        out |= 0x200000;
                                                                    }
                                                                    if((v34 & 0xFFFFF000) != 0) {
                                                                        fail = j;
                                                                    }
                                                                    break alab1;
                                                                }
                                                                else {
                                                                    goto label_460;
                                                                }
                                                            }
                                                            if(armDis$Input0.check(']')) {
                                                                if(!armDis$Input0.check(", ")) {
                                                                    if(c != 80) {
                                                                        out |= 0x1000000;
                                                                    }
                                                                    out |= 0x800000;
                                                                    break alab1;
                                                                }
                                                                else if(armDis$Input0.check('#')) {
                                                                    if(!armDis$Input0.check('-')) {
                                                                        out |= 0x800000;
                                                                    }
                                                                    int v35 = armDis$Input0.getInt();
                                                                    out |= v35;
                                                                    if((v35 & 0xFFFFF000) != 0) {
                                                                        fail = j;
                                                                    }
                                                                    break alab1;
                                                                }
                                                                else {
                                                                    out |= 0x2000000;
                                                                    if((0x2000000 & mask) == 0) {
                                                                        armDis$Input0.pos -= 3;
                                                                        continue;
                                                                    }
                                                                    else {
                                                                        goto label_460;
                                                                    }
                                                                }
                                                            }
                                                            out |= 0x2000000;
                                                            if((0x2000000 & mask) == 0) {
                                                                continue;
                                                            }
                                                            goto label_460;
                                                        }
                                                        if(armDis$Input0.check(',')) {
                                                            if(c != 80) {
                                                                out |= 0x1000000;
                                                            }
                                                            while((0x2000000 & out) == 0) {
                                                                if(armDis$Input0.check('#')) {
                                                                    if(!armDis$Input0.check('-')) {
                                                                        out |= 0x800000;
                                                                    }
                                                                    int v36 = armDis$Input0.getInt();
                                                                    out |= v36;
                                                                    if((v36 & 0xFFFFF000) != 0) {
                                                                        fail = j;
                                                                    }
                                                                    goto label_243;
                                                                }
                                                                out |= 0x2000000;
                                                                if((0x2000000 & mask) == 0) {
                                                                    continue;
                                                                }
                                                                goto label_460;
                                                            }
                                                            if(!armDis$Input0.check('-')) {
                                                                out |= 0x800000;
                                                            }
                                                            out = ArmDis.decodeShift(out, armDis$Input0, true);
                                                        label_243:
                                                            if(!armDis$Input0.check(']')) {
                                                                goto label_460;
                                                            }
                                                            else if(armDis$Input0.check('!')) {
                                                                out |= 0x200000;
                                                            }
                                                        }
                                                        else {
                                                            if(!armDis$Input0.check(']')) {
                                                                goto label_460;
                                                            }
                                                            else if(armDis$Input0.check(", ")) {
                                                                do {
                                                                    if((0x2000000 & out) != 0) {
                                                                        if(!armDis$Input0.check('-')) {
                                                                            out |= 0x800000;
                                                                        }
                                                                        out = ArmDis.decodeShift(out, armDis$Input0, true);
                                                                        break alab1;
                                                                    }
                                                                    if(armDis$Input0.check('#')) {
                                                                        if(!armDis$Input0.check('-')) {
                                                                            out |= 0x800000;
                                                                        }
                                                                        int v37 = armDis$Input0.getInt();
                                                                        out |= v37;
                                                                        if((v37 & 0xFFFFF000) != 0) {
                                                                            fail = j;
                                                                        }
                                                                        break alab1;
                                                                    }
                                                                    out |= 0x2000000;
                                                                }
                                                                while((0x2000000 & mask) == 0);
                                                                goto label_460;
                                                            }
                                                            else if((0x2000000 & out) == 0) {
                                                                if(c != 80) {
                                                                    out |= 0x1000000;
                                                                }
                                                                out |= 0x800000;
                                                                break;
                                                            }
                                                            else {
                                                                goto label_460;
                                                            }
                                                            goto label_270;
                                                        }
                                                    }
                                                }
                                                else {
                                                    goto label_460;
                                                }
                                                break;
                                            }
                                            case 98: {
                                                if(armDis$Input0.check('+')) {
                                                    relative = 1;
                                                }
                                                else if(armDis$Input0.check('-')) {
                                                    relative = -1;
                                                }
                                                if(armDis$Input0.check("0X")) {
                                                    long address = armDis$Input0.getHex();
                                                    if(relative != 0) {
                                                        address = addr + ((long)relative) * address;
                                                    }
                                                    int disp = 0x800000 + (((int)(address - addr)) - 8 >> 2) ^ 0x800000;
                                                    out |= 0xFFFFFF & disp;
                                                    if((0xFF000000 & disp) != 0) {
                                                        fail = j;
                                                    }
                                                }
                                                else {
                                                    goto label_460;
                                                }
                                                break;
                                            }
                                            case 99: {
                                                int value = armDis$Input0.index(ArmDis.COND);
                                                if(value == -1) {
                                                    goto label_460;
                                                }
                                                else {
                                                    if(value > 15) {
                                                        value = 14;
                                                    }
                                                    out |= value << 28;
                                                    break;
                                                }
                                                goto label_387;
                                            }
                                            case 101: {
                                            label_387:
                                                int v52 = armDis$Input0.getInt();
                                                out |= (0xFFF0 & v52) << 4 | v52 & 15;
                                                if((0xFFFF0000 & v52) != 0) {
                                                    fail = j;
                                                }
                                                break;
                                            }
                                            case 109: {
                                                if(armDis$Input0.check('{')) {
                                                    int range = -1;
                                                    while(true) {
                                                        int v54 = armDis$Input0.index(ArmDis.REG);
                                                        int v55 = armDis$Input0.getAndNext();
                                                        if(v54 == -1) {
                                                            break;
                                                        }
                                                        if(range == -1) {
                                                            range = v54;
                                                        }
                                                        out |= (2 << v54 - range) - 1 << range;
                                                        if(v55 == 45) {
                                                            range = v54;
                                                        }
                                                        else {
                                                            if(v55 != 44) {
                                                                break;
                                                            }
                                                            range = -1;
                                                        }
                                                    }
                                                    if(v55 != 0x7D) {
                                                        goto label_460;
                                                    }
                                                }
                                                else {
                                                    goto label_460;
                                                }
                                                break;
                                            }
                                            case 0x6F: {
                                                if(armDis$Input0.check('#')) {
                                                    if((0x2000000 & mask) != 0 && (0x2000000 & out) == 0) {
                                                        goto label_460;
                                                    }
                                                    else {
                                                        out |= 0x2000000;
                                                        int immed = armDis$Input0.getInt();
                                                        if(armDis$Input0.check(", ")) {
                                                            rotate = armDis$Input0.getInt();
                                                            if((rotate & 0xFFFFFFE1) != 0 || (immed & 0xFFFFFF00) != 0) {
                                                                fail = j;
                                                            }
                                                            out |= rotate << 7 | immed;
                                                        label_421:
                                                            ++j;
                                                            continue;
                                                        }
                                                        else {
                                                            for(rotate = 0; rotate < 0x20; rotate += 2) {
                                                                int a = immed << rotate | immed >> 0x20 - rotate;
                                                                if(a <= 0xFF) {
                                                                    out |= rotate << 7 | a;
                                                                    break alab1;
                                                                }
                                                            }
                                                            goto label_460;
                                                        }
                                                    }
                                                    goto label_432;
                                                }
                                                else {
                                                    out = ArmDis.decodeShift(out, armDis$Input0, true);
                                                    if((0x2000000 & out) != 0) {
                                                        fail = j;
                                                    }
                                                }
                                                break;
                                            }
                                            case 0x70: {
                                            label_432:
                                                if(armDis$Input0.check("PLS") || armDis$Input0.check("PLT") || armDis$Input0.check("PLE")) {
                                                    armDis$Input0.pos -= 3;
                                                }
                                                else if(armDis$Input0.check("PL")) {
                                                    armDis$Input0.pos -= 2;
                                                    break;
                                                }
                                                if(armDis$Input0.check('P')) {
                                                    out |= 0xF000;
                                                }
                                                break;
                                            }
                                            case 0x71: {
                                                out = ArmDis.decodeShift(out, armDis$Input0, false);
                                                break;
                                            }
                                            case 83: 
                                            case 0x73: {
                                            label_274:
                                                if(armDis$Input0.check('[')) {
                                                    int v39 = armDis$Input0.index(ArmDis.REG);
                                                    if(v39 == -1) {
                                                        goto label_460;
                                                    }
                                                    else {
                                                        out |= v39 << 16;
                                                        if((0x4F0000 & out) == 0x4F0000) {
                                                        label_324:
                                                            if(v39 != 15) {
                                                                goto label_460;
                                                            }
                                                            else if(armDis$Input0.check(",#")) {
                                                                out |= 0x1000000;
                                                                out = armDis$Input0.check('-') ? out | 0x1000000 : out | 0x800000;
                                                                int v44 = armDis$Input0.getInt();
                                                                out |= (v44 & 0xF0) << 4 | v44 & 15;
                                                                if(!armDis$Input0.check(']')) {
                                                                    goto label_460;
                                                                }
                                                                else if((v44 & 0xFFFFFF00) != 0) {
                                                                    fail = j;
                                                                }
                                                            }
                                                            else if(!armDis$Input0.check(']')) {
                                                                goto label_460;
                                                            }
                                                            else if(armDis$Input0.check(", #")) {
                                                                if(!armDis$Input0.check('-')) {
                                                                    out |= 0x800000;
                                                                }
                                                                int v45 = armDis$Input0.getInt();
                                                                out |= (v45 & 0xF0) << 4 | v45 & 15;
                                                                if((v45 & 0xFFFFFF00) != 0) {
                                                                    fail = j;
                                                                }
                                                            }
                                                            else {
                                                                out |= 0x1800000;
                                                            }
                                                        }
                                                        else if(armDis$Input0.check(",")) {
                                                            out |= 0x1000000;
                                                            if(!armDis$Input0.check('#')) {
                                                                if(!armDis$Input0.check('-')) {
                                                                    out |= 0x800000;
                                                                }
                                                                int reg = armDis$Input0.index(ArmDis.REG);
                                                                if(reg == -1) {
                                                                    goto label_460;
                                                                }
                                                                else {
                                                                    out |= reg;
                                                                    if((0x400000 & out) != 0) {
                                                                        fail = j;
                                                                    }
                                                                }
                                                            }
                                                            else if((0x400000 & mask) != 0 && (0x400000 & out) == 0) {
                                                                goto label_460;
                                                            }
                                                            else {
                                                                out |= 0x400000;
                                                                out = armDis$Input0.check('-') ? out | 0x400000 : out | 0x800000;
                                                                int v41 = armDis$Input0.getInt();
                                                                out |= (v41 & 0xF0) << 4 | v41 & 15;
                                                                if((v41 & 0xFFFFFF00) != 0) {
                                                                    fail = j;
                                                                }
                                                            }
                                                            if(!armDis$Input0.check(']')) {
                                                                goto label_460;
                                                            }
                                                            else if(armDis$Input0.check('!')) {
                                                                out |= 0x200000;
                                                            }
                                                        }
                                                        else if(!armDis$Input0.check(']')) {
                                                            goto label_460;
                                                        }
                                                        else if(!armDis$Input0.check(", ")) {
                                                            out |= 0x1000000;
                                                            if((0x400000 & mask) != 0 && (0x400000 & out) == 0) {
                                                                goto label_460;
                                                            }
                                                            else {
                                                                out |= 0xC00000;
                                                                break;
                                                            }
                                                            goto label_324;
                                                        }
                                                        else if(!armDis$Input0.check('#')) {
                                                            if(!armDis$Input0.check('-')) {
                                                                out |= 0x800000;
                                                            }
                                                            int reg = armDis$Input0.index(ArmDis.REG);
                                                            if(reg == -1) {
                                                                goto label_460;
                                                            }
                                                            else {
                                                                out |= reg;
                                                                if((0x400000 & out) != 0) {
                                                                    fail = j;
                                                                }
                                                            }
                                                        }
                                                        else if((0x400000 & mask) != 0 && (0x400000 & out) == 0) {
                                                            goto label_460;
                                                        }
                                                        else {
                                                            out |= 0x400000;
                                                            out = armDis$Input0.check('-') ? out | 0x400000 : out | 0x800000;
                                                            int v43 = armDis$Input0.getInt();
                                                            out |= (v43 & 0xF0) << 4 | v43 & 15;
                                                            if((v43 & 0xFFFFFF00) != 0) {
                                                                fail = j;
                                                            }
                                                        }
                                                    }
                                                }
                                                else {
                                                    goto label_460;
                                                }
                                                break;
                                            }
                                            case 0x74: {
                                                if(armDis$Input0.check('T')) {
                                                    out |= 0x200000;
                                                }
                                                break;
                                            }
                                            default: {
                                                throw new RuntimeException("81 Invalid format char \'" + ((char)c) + "\'; " + i + "; " + j + "; \'" + asm + "\'");
                                            }
                                        }
                                        ++j;
                                        continue;
                                    }
                                    else {
                                        if(c == 59) {
                                            break;
                                        }
                                        else {
                                            if(97 <= c && c <= 0x7A) {
                                                c = (char)(c - 0x20);
                                            }
                                            if(armDis$Input0.check(((char)c))) {
                                                goto label_421;
                                            }
                                            else {
                                                goto label_460;
                                            }
                                        }
                                        goto label_32;
                                    }
                                    ++i;
                                    continue alab2;
                                }
                                armDis$Input0.check(' ');
                                if(fail == -1) {
                                    int v59 = armDis$Input0.get();
                                    if(v59 > 0x20 && v59 != 59 || (out & mask) != NUM[i * 2]) {
                                        goto label_460;
                                    }
                                    else {
                                        int k = 0;
                                        while(k < i) {
                                            int cmask = NUM[k * 2 + 1];
                                            if((out & cmask) == NUM[k * 2] && ((0xF0000000 & out) != 0xF0000000 || (0xF0000000 & cmask) == 0xF0000000)) {
                                                goto label_460;
                                            }
                                            ++k;
                                        }
                                        return out;
                                    }
                                }
                                else {
                                    goto label_460;
                                }
                            }
                            while(true) {
                                ++i;
                                continue alab2;
                            label_460:
                                if(armDis$Input0.pos <= armDis$Input0.best) {
                                    break;
                                }
                                armDis$Input0.best = armDis$Input0.pos;
                                armDis$Input0.bestOut = out;
                            }
                            ++i;
                        }
                        catch(NextAsm unused_ex) {
                            goto label_460;
                        }
                    }
                }
            }
            return out;
        }

        static String getOpcode(ArmDis ret, long addr, int data) {
            String coproc = Coproc.getOpcode(ret, addr, data, false);
            if(coproc != null) {
                return coproc;
            }
            String s1 = Neon.getOpcode(ret, addr, data, false);
            if(s1 != null) {
                return s1;
            }
            int i = 0;
            while(true) {
                if(i >= Arm.STR.length) {
                    throw new RuntimeException("170 No ARM match for " + Integer.toHexString(data).toUpperCase(Locale.US) + "; " + Integer.toHexString(Integer.reverseBytes(data)).toUpperCase(Locale.US));
                }
                int mask = Arm.NUM[i * 2 + 1];
                int value = Arm.NUM[i * 2];
                if((data & mask) == value && ((0xF0000000 & data) != 0xF0000000 || (0xF0000000 & mask) == 0xF0000000 || mask == 0 && value == 0)) {
                    boolean WRITEBACK_BIT_SET = (0x200000 & data) != 0;
                    boolean IMMEDIATE_BIT_SET = (0x400000 & data) != 0;
                    boolean NEGATIVE_BIT_SET = (0x800000 & data) == 0;
                    boolean PRE_BIT_SET = (0x1000000 & data) != 0;
                    StringBuilder out = new StringBuilder();
                    int uReg = 16;
                    int UReg = 16;
                    boolean isUnpredictable = false;
                    int commentValue = 0;
                    long commentValueLong = 0L;
                    String asm = Arm.STR[i];
                    int j = 0;
                    int v10 = asm.length();
                    while(true) {
                        if(j >= v10) {
                            if(commentValue > 0x20 || commentValue < -16) {
                                out.append("\t ; 0x").append(Integer.toHexString(commentValue).toUpperCase(Locale.US));
                            }
                            else if(commentValueLong > 0x20L || commentValueLong < -16L) {
                                out.append("\t ; 0x").append(Long.toHexString(commentValueLong).toUpperCase(Locale.US));
                            }
                            if(isUnpredictable) {
                                out.append("\t ; <UNPREDICTABLE>");
                            }
                            return out.toString();
                        }
                        int v11 = ArmDis.charAt(asm, v10, j);
                        if(v11 == 37) {
                            int offset = 0;
                            boolean allowUnpredictable = false;
                            ++j;
                            int c = ArmDis.charAt(asm, v10, j);
                        alab1:
                            switch(c) {
                                case 37: {
                                    out.append('%');
                                    break;
                                }
                                case 0x30: 
                                case 49: 
                                case 50: 
                                case 51: 
                                case 52: 
                                case 53: 
                                case 54: 
                                case 55: 
                                case 56: 
                                case 57: {
                                    j = ArmDis.decodeBitfield(asm, v10, j, data, ret);
                                    int value = ret.valuep;
                                    int v15 = ret.widthp;
                                    int c = ArmDis.charAt(asm, v10, j);
                                    switch(c) {
                                        case 39: {
                                            ++j;
                                            int c = ArmDis.charAt(asm, v10, j);
                                            if(value == (1 << v15) - 1) {
                                                ArmDis.append(out, ((char)c));
                                            }
                                            break alab1;
                                        }
                                        case 0x3F: {
                                            ArmDis.append(out, ArmDis.charAt(asm, v10, (1 << v15) + j - value));
                                            j += 1 << v15;
                                            break alab1;
                                        }
                                        case 82: {
                                            if(value == 15) {
                                                isUnpredictable = true;
                                            }
                                            goto label_62;
                                        }
                                        case 87: {
                                            out.append(value + 1);
                                            commentValue = value + 1;
                                            break alab1;
                                        }
                                        case 88: {
                                            out.append(Integer.toHexString(value & 15).toUpperCase(Locale.US));
                                            commentValue = value;
                                            break alab1;
                                        }
                                        case 0x60: {
                                            ++j;
                                            int c = ArmDis.charAt(asm, v10, j);
                                            if(value == 0) {
                                                ArmDis.append(out, ((char)c));
                                            }
                                            break alab1;
                                        }
                                        case 98: {
                                            out.append(value * 8);
                                            commentValue = value * 8;
                                            break alab1;
                                        }
                                        case 100: {
                                            out.append(value);
                                            commentValue = value;
                                            break alab1;
                                        }
                                        case 84: 
                                        case 0x72: {
                                        label_62:
                                            if(c == 84) {
                                                ++value;
                                            }
                                            if(ArmDis.charAt(asm, v10, j + 1) == 0x75) {
                                                ++j;
                                                if(uReg == value) {
                                                    isUnpredictable = true;
                                                }
                                                uReg = value;
                                            }
                                            if(ArmDis.charAt(asm, v10, j + 1) == 85) {
                                                ++j;
                                                if(UReg == value) {
                                                    isUnpredictable = true;
                                                }
                                                UReg = value;
                                            }
                                            out.append(ArmDis.REG[value]);
                                            break alab1;
                                        }
                                        case 120: {
                                            out.append("0x");
                                            ToolsLight.prefixIntegerHex(out, 8, value);
                                            if((0xFFFFFFF & data) == 0xFF00000) {
                                                out.append("\t ; IMB");
                                            }
                                            else if((0xFFFFFFF & data) == 0xFF00001) {
                                                out.append("\t ; IMBRange");
                                            }
                                            break alab1;
                                        }
                                        default: {
                                            throw new RuntimeException("70 Invalid format char \'" + ((char)c) + "\'; " + i + "; " + j + "; \'" + asm + "\'");
                                        }
                                    }
                                }
                                case 66: {
                                    if(!NEGATIVE_BIT_SET) {
                                        offset = 0xFF000000;
                                    }
                                    ArmDis.printAddr(out, addr, ((0x1000000 & data) == 0 ? ((long)(offset + (0xFFFFFF & data) << 2)) + addr + 8L : ((long)(offset + (0xFFFFFF & data) << 2)) + addr + 10L) - addr);
                                    break;
                                }
                                case 67: {
                                    if((0x2000200 & data) == 0x200) {
                                        ArmDis.bankedRegname(out, (0x4F0000 & data) >> 16 | (data & 0x300) >> 4);
                                    }
                                    else {
                                        out.append(((char)((0x400000 & data) == 0 ? 67 : 83))).append("PSR_");
                                        if((0x80000 & data) != 0) {
                                            out.append('f');
                                        }
                                        if((0x40000 & data) != 0) {
                                            out.append('s');
                                        }
                                        if((0x20000 & data) != 0) {
                                            out.append('x');
                                        }
                                        if((0x10000 & data) != 0) {
                                            out.append('c');
                                        }
                                    }
                                    break;
                                }
                                case 69: {
                                    int msb = (0x1F0000 & data) >> 16;
                                    int lsb = (data & 0xF80) >> 7;
                                    int w = msb - lsb + 1;
                                    if(w > 0) {
                                        out.append('#').append(lsb).append(", #").append(w);
                                    }
                                    else {
                                        out.append("<invalid: ").append(lsb).append(':').append(msb).append('>');
                                    }
                                    break;
                                }
                                case 80: {
                                    commentValueLong = Arm.printArmAddress(out, addr, 0x1000000 | data);
                                    break;
                                }
                                case 82: {
                                    ArmDis.bankedRegname(out, (0x4F0000 & data) >> 16 | (data & 0x300) >> 4);
                                    break;
                                }
                                case 83: {
                                    allowUnpredictable = true;
                                    goto label_194;
                                }
                                case 85: {
                                    if((data & 0xF0) != 0x60) {
                                        ArmDis.dataBarrierOption(out, data & 15);
                                    }
                                    else if((data & 15) == 15) {
                                        out.append("sy");
                                    }
                                    else {
                                        out.append('#').append(data & 15);
                                    }
                                    break;
                                }
                                case 86: {
                                    int imm16 = (0xF0000 & data) >> 4 | data & 0xFFF;
                                    out.append('#').append(imm16);
                                    commentValue = imm16;
                                    break;
                                }
                                case 97: {
                                    commentValueLong = Arm.printArmAddress(out, addr, data);
                                    break;
                                }
                                case 98: {
                                    ArmDis.printAddr(out, addr, ((long)(((0xFFFFFF & data ^ 0x800000) - 0x800000) * 4 + 8)));
                                    break;
                                }
                                case 99: {
                                    if((data >> 28 & 15) != 14) {
                                        out.append(ArmDis.COND[data >> 28 & 15]);
                                    }
                                    break;
                                }
                                case 101: {
                                    int imm = data & 15 | (0xFFF00 & data) >> 4;
                                    out.append(imm);
                                    commentValue = imm;
                                    break;
                                }
                                case 109: {
                                    boolean started = false;
                                    out.append('{');
                                    for(int reg = 0; reg < 16; ++reg) {
                                        if((1 << reg & data) != 0) {
                                            if(reg >= 2 && reg <= 12 && (1 << reg - 2 & data) != 0 && (1 << reg - 1 & data) != 0) {
                                                out.setLength(out.length() - 1 - ArmDis.REG[reg - 1].length());
                                                out.append('-');
                                            }
                                            else if(started) {
                                                out.append(',');
                                            }
                                            started = true;
                                            out.append(ArmDis.REG[reg]);
                                        }
                                    }
                                    out.append('}');
                                    if(!started) {
                                        isUnpredictable = true;
                                    }
                                    break;
                                }
                                case 0x6F: {
                                    if((0x2000000 & data) == 0) {
                                        ArmDis.decodeShift(out, data, true);
                                    }
                                    else {
                                        int rotate = (data & 0xF00) >> 7;
                                        int a = (data & 0xFF) << 0x20 - rotate | (data & 0xFF) >> rotate;
                                        int ix;
                                        for(ix = 0; ix < 0x20 && (a << ix | a >> 0x20 - ix) > 0xFF; ix += 2) {
                                        }
                                        if(ix == rotate) {
                                            out.append('#').append(a);
                                        }
                                        else {
                                            out.append('#').append(data & 0xFF).append(", ").append(rotate);
                                        }
                                        commentValue = a;
                                    }
                                    break;
                                }
                                case 0x70: {
                                    if((0xF000 & data) == 0xF000) {
                                        out.append('P');
                                    }
                                    break;
                                }
                                case 0x71: {
                                    ArmDis.decodeShift(out, data, false);
                                    break;
                                }
                                case 0x73: {
                                label_194:
                                    if((0x4F0000 & data) == 0x4F0000) {
                                        int offset = (data & 0xF00) >> 4 | data & 15;
                                        if(PRE_BIT_SET) {
                                            if(offset == 0 && !NEGATIVE_BIT_SET) {
                                                out.append("[PC]\t ; ");
                                            }
                                            else {
                                                out.append("[PC,#");
                                                if(NEGATIVE_BIT_SET) {
                                                    out.append('-');
                                                }
                                                out.append(offset).append("]\t ; ");
                                            }
                                            if(NEGATIVE_BIT_SET) {
                                                offset = -offset;
                                            }
                                            ArmDis.printAddr(out, addr, ((long)(offset + 8)));
                                        }
                                        else {
                                            out.append("[PC], #");
                                            if(NEGATIVE_BIT_SET) {
                                                out.append('-');
                                            }
                                            out.append(offset);
                                            if(!allowUnpredictable) {
                                                isUnpredictable = true;
                                            }
                                        }
                                    }
                                    else {
                                        int offset = (data & 0xF00) >> 4 | data & 15;
                                        out.append('[').append(ArmDis.REG[data >> 16 & 15]);
                                        if(PRE_BIT_SET) {
                                            if(IMMEDIATE_BIT_SET) {
                                                if(WRITEBACK_BIT_SET || NEGATIVE_BIT_SET || offset != 0) {
                                                    out.append(",#");
                                                    if(NEGATIVE_BIT_SET) {
                                                        out.append('-');
                                                    }
                                                    out.append(offset);
                                                }
                                                if(NEGATIVE_BIT_SET) {
                                                    offset = -offset;
                                                }
                                                commentValue = offset;
                                            }
                                            else {
                                                out.append(",");
                                                if(NEGATIVE_BIT_SET) {
                                                    out.append('-');
                                                }
                                                out.append(ArmDis.REG[data & 15]);
                                                if(!allowUnpredictable && WRITEBACK_BIT_SET && (data & 15) == (data >> 12 & 15)) {
                                                    isUnpredictable = true;
                                                }
                                            }
                                            out.append(']');
                                            if(WRITEBACK_BIT_SET) {
                                                out.append('!');
                                            }
                                        }
                                        else {
                                            if(IMMEDIATE_BIT_SET) {
                                                out.append("], #");
                                                if(NEGATIVE_BIT_SET) {
                                                    out.append('-');
                                                }
                                                out.append(offset);
                                                if(NEGATIVE_BIT_SET) {
                                                    offset = -offset;
                                                }
                                                commentValue = offset;
                                            }
                                            else {
                                                out.append("], ");
                                                if(NEGATIVE_BIT_SET) {
                                                    out.append('-');
                                                }
                                                out.append(ArmDis.REG[data & 15]);
                                                if(!allowUnpredictable && (data & 15) == (data >> 12 & 15)) {
                                                    isUnpredictable = true;
                                                }
                                            }
                                            if(!allowUnpredictable && (WRITEBACK_BIT_SET || !IMMEDIATE_BIT_SET && (data & 15) == 15)) {
                                                isUnpredictable = true;
                                            }
                                        }
                                    }
                                    break;
                                }
                                case 0x74: {
                                    if((0x1200000 & data) == 0x200000) {
                                        out.append('T');
                                    }
                                    break;
                                }
                                default: {
                                    throw new RuntimeException("80 Invalid format char \'" + ((char)c) + "\'; " + i + "; " + j + "; \'" + asm + "\'");
                                }
                            }
                        }
                        else {
                            ArmDis.append(out, ((char)v11));
                        }
                        ++j;
                    }
                }
                ++i;
            }
        }

        static long printArmAddress(StringBuilder out, long addr, int data) {
            long offset;
            boolean PRE_BIT_SET = false;
            boolean WRITEBACK_BIT_SET = (0x200000 & data) != 0;
            boolean NEGATIVE_BIT_SET = (0x800000 & data) == 0;
            if((0x1000000 & data) != 0) {
                PRE_BIT_SET = true;
            }
            long offset = 0L;
            if((0xF0000 & data) == 0xF0000 && (data & 0x2000000) == 0) {
                long offset = (long)(data & 0xFFF);
                out.append("[PC");
                if(PRE_BIT_SET) {
                    if(WRITEBACK_BIT_SET || NEGATIVE_BIT_SET || offset != 0L) {
                        out.append(",#");
                        if(NEGATIVE_BIT_SET) {
                            out.append('-');
                        }
                        out.append(offset);
                    }
                    if(NEGATIVE_BIT_SET) {
                        offset = -offset;
                    }
                    offset = offset + (addr + 8L);
                    out.append(']');
                    if(WRITEBACK_BIT_SET) {
                        out.append('!');
                    }
                }
                else {
                    out.append("], #");
                    if(NEGATIVE_BIT_SET) {
                        out.append('-');
                    }
                    out.append(offset);
                    offset = addr + 8L;
                }
                out.append("\t ; ");
                ArmDis.printAddr(out, addr, offset - addr);
                return 0L;
            }
            out.append('[').append(ArmDis.REG[data >> 16 & 15]);
            if(PRE_BIT_SET) {
                if((data & 0x2000000) == 0) {
                    offset = (long)(data & 0xFFF);
                    if(WRITEBACK_BIT_SET || NEGATIVE_BIT_SET || offset != 0L) {
                        out.append(",#");
                        if(NEGATIVE_BIT_SET) {
                            out.append('-');
                        }
                        out.append(offset);
                    }
                }
                else {
                    out.append(',');
                    if(NEGATIVE_BIT_SET) {
                        out.append('-');
                    }
                    ArmDis.decodeShift(out, data, true);
                }
                out.append(']');
                if(WRITEBACK_BIT_SET) {
                    out.append('!');
                    return offset;
                }
                return offset;
            }
            if((data & 0x2000000) == 0) {
                out.append("], #");
                if(NEGATIVE_BIT_SET) {
                    out.append('-');
                }
                out.append(((long)(data & 0xFFF)));
                return (long)(data & 0xFFF);
            }
            out.append("], ");
            if(NEGATIVE_BIT_SET) {
                out.append('-');
            }
            ArmDis.decodeShift(out, data, true);
            return 0L;
        }
    }

    public static class AsmFailedException extends NumberFormatException {
        String data;
        EditText edit;
        String suggestion;

        public AsmFailedException(String s) {
            super(s);
        }

        public String getSuggestion() {
            return this.suggestion;
        }
    }

    static class Coproc {
        static final int COND_UNCOND = 16;
        static final int[] NUM;
        static final String[] STR;

        static {
            Coproc.NUM = new int[]{0xE000100, 0xFF08F10, 0xE100100, 0xFF08F10, 0xE200100, 0xFF08F10, 0xE300100, 0xFF08F10, 0xE400100, 0xFF08F10, 0xE500100, 0xFF08F10, 0xE600100, 0xFF08F10, 0xE700100, 0xFF08F10, 0xE800100, 0xFF08F10, 0xE900100, 0xFF08F10, 0xEA00100, 0xFF08F10, 0xEB00100, 0xFF08F10, 0xEC00100, 0xFF08F10, 0xE008100, 0xFF08F10, 0xE108100, 0xFF08F10, 0xE208100, 0xFF08F10, 0xE308100, 0xFF08F10, 0xE408100, 0xFF08F10, 0xE508100, 0xFF08F10, 0xE608100, 0xFF08F10, 0xE708100, 0xFF08F10, 0xE808100, 0xFF08F10, 0xE908100, 0xFF08F10, 0xEA08100, 0xFF08F10, 0xEB08100, 0xFF08F10, 0xEC08100, 0xFF08F10, 0xED08100, 0xFF08F10, 0xEE08100, 0xFF08F10, 0xEF08100, 0xFF08F10, 0xE000110, 0xFF00F1F, 0xE100110, 0xFFF0F98, 0xE200110, 0xFFF0FFF, 0xE300110, 0xFFF0FFF, 0xE400110, 0xFFF0FFF, 0xE500110, 0xFFF0FFF, 0xE90F110, 0xFF8FFF0, 0xEB0F110, 0xFF8FFF0, 0xED0F110, 0xFF8FFF0, 0xEF0F110, 0xFF8FFF0, 0xC000100, 0xE100F00, 0xC100100, 0xE100F00, 0xC000200, 0xE100F00, 0xC100200, 0xE100F00, 0xD2D0B00, 0xFBF0F01, 0xD200B00, 0xFB00F01, 0xD300B00, 0xFB00F01, 0xC800B00, 0xF900F01, 0xCBD0B00, 0xFBF0F01, 0xC900B00, 0xF900F01, 0xD000B00, 0xF300F00, 0xD100B00, 0xF300F00, 0xD2D0A00, 0xFBF0F00, 0xD200A00, 0xFB00F00, 0xD300A00, 0xFB00F00, 0xC800A00, 0xF900F00, 0xCBD0A00, 0xFBF0F00, 0xC900A00, 0xF900F00, 0xD000A00, 0xF300F00, 0xD100A00, 0xF300F00, 0xD200B01, 0xFB00F01, 0xD300B01, 0xFB00F01, 0xC800B01, 0xF900F01, 0xC900B01, 0xF900F01, 0xE800B10, 0xFF00F70, 0xE800B30, 0xFF00F70, 0xEA00B10, 0xFF00F70, 0xEA00B30, 0xFF00F70, 0xEC00B10, 0xFF00F70, 0xEE00B10, 0xFF00F70, 0xC400B10, 0xFF00FD0, 0xC500B10, 0xFF00FD0, 0xE000B10, 0xFD00F70, 0xE100B10, 0xF500F70, 0xE000B30, 0xFD00F30, 0xE100B30, 0xF500F30, 0xE400B10, 0xFD00F10, 0xE500B10, 0xF500F10, 0xEB20B40, 0xFBF0F50, 0xEB30B40, 0xFBF0F50, 0xEB20A40, 0xFBF0F50, 0xEB30A40, 0xFBF0F50, 0xEE00A10, 0xFFF0FFF, 0xEE10A10, 0xFFF0FFF, 0xEE60A10, 0xFFF0FFF, 0xEE70A10, 0xFFF0FFF, 0xEE80A10, 0xFFF0FFF, 0xEE90A10, 0xFFF0FFF, 0xEEA0A10, 0xFFF0FFF, 0xEF00A10, 0xFFF0FFF, 0xEF1FA10, 0xFFFFFFF, 0xEF10A10, 0xFFF0FFF, 0xEF60A10, 0xFFF0FFF, 0xEF70A10, 0xFFF0FFF, 0xEF80A10, 0xFFF0FFF, 0xEF90A10, 0xFFF0FFF, 0xEFA0A10, 0xFFF0FFF, 0xE000B10, 0xFD00FFF, 0xE100B10, 0xFD00FFF, 0xEE00A10, 0xFF00FFF, 0xEF00A10, 0xFF00FFF, 0xE000A10, 0xFF00F7F, 0xE100A10, 0xFF00F7F, 0xEB50A40, 0xFBF0F70, 0xEB50B40, 0xFBF0F70, 0xEB00A40, 0xFBF0FD0, 0xEB00AC0, 0xFBF0FD0, 0xEB00B40, 0xFBF0FD0, 0xEB00BC0, 0xFBF0FD0, 0xEB10A40, 0xFBF0FD0, 0xEB10AC0, 0xFBF0FD0, 0xEB10B40, 0xFBF0FD0, 0xEB10BC0, 0xFBF0FD0, 0xEB70AC0, 0xFBF0FD0, 0xEB70BC0, 0xFBF0FD0, 0xEB80A40, 0xFBF0F50, 0xEB80B40, 0xFBF0F50, 0xEB40A40, 0xFBF0F50, 0xEB40B40, 0xFBF0F50, 0xEBA0A40, 0xFBE0F50, 247073600, 0xFBE0F50, 0xEBC0A40, 0xFBE0F50, 0xEBC0B40, 0xFBE0F50, 0xEBE0A40, 0xFBE0F50, 0xEBE0B40, 0xFBE0F50, 0xC500B10, 0xFB00FF0, 0xEB00A00, 0xFB00FF0, 0xEB00B00, 0xFB00FF0, 0xC400A10, 0xFF00FD0, 0xC400B10, 0xFF00FD0, 0xC500A10, 0xFF00FD0, 0xE000A00, 0xFB00F50, 0xE000A40, 0xFB00F50, 0xE000B00, 0xFB00F50, 0xE000B40, 0xFB00F50, 0xE100A00, 0xFB00F50, 0xE100A40, 0xFB00F50, 0xE100B00, 0xFB00F50, 0xE100B40, 0xFB00F50, 0xE200A00, 0xFB00F50, 0xE200A40, 0xFB00F50, 0xE200B00, 0xFB00F50, 0xE200B40, 0xFB00F50, 0xE300A00, 0xFB00F50, 0xE300A40, 0xFB00F50, 0xE300B00, 0xFB00F50, 0xE300B40, 0xFB00F50, 0xE800A00, 0xFB00F50, 0xE800B00, 0xFB00F50, 0xEA00A00, 0xFB00F50, 0xEA00B00, 0xFB00F50, 0xEA00A40, 0xFB00F50, 0xEA00B40, 0xFB00F50, 0xE900A40, 0xFB00F50, 0xE900B40, 0xFB00F50, 0xE900A00, 0xFB00F50, 0xE900B00, 0xFB00F50, 0xFE000A00, 0xFF800F00, 0xFE000B00, 0xFF800F00, 0xFE800A00, 0xFFB00F40, 0xFE800B00, 0xFFB00F40, 0xFE800A40, 0xFFB00F40, 0xFE800B40, 0xFFB00F40, 0xFEBC0A40, 0xFFBC0F50, 0xFEBC0B40, 0xFFBC0F50, 0xEB60A40, 0xFBE0F50, 0xEB60B40, 0xFBE0F50, 0xFEB80A40, 0xFFBC0F50, 0xFEB80B40, 0xFFBC0F50, 0xC400000, 0xFF00000, 0xC500000, 0xFF00000, 0xE000000, 0xF000010, 0xE10F010, 0xF10F010, 0xE100010, 0xF100010, 0xE000010, 0xF100010, 0xC000000, 0xE100000, 0xC100000, 0xE100000, 0xFC500000, 0xFFF00000, 0xFC400000, 0xFFF00000, 0xFC100000, 0xFE100000, 0xFC000000, 0xFE100000, 0xFE000000, 0xFF000010, 0xFE000010, 0xFF100010, 0xFE100010, 0xFF100010};
            Coproc.STR = new String[]{"ADF%c%P%R\t %12-14f, %16-18f, %0-3f", "MUF%c%P%R\t %12-14f, %16-18f, %0-3f", "SUF%c%P%R\t %12-14f, %16-18f, %0-3f", "RSF%c%P%R\t %12-14f, %16-18f, %0-3f", "DVF%c%P%R\t %12-14f, %16-18f, %0-3f", "RDF%c%P%R\t %12-14f, %16-18f, %0-3f", "POW%c%P%R\t %12-14f, %16-18f, %0-3f", "RPW%c%P%R\t %12-14f, %16-18f, %0-3f", "RMF%c%P%R\t %12-14f, %16-18f, %0-3f", "FML%c%P%R\t %12-14f, %16-18f, %0-3f", "FDV%c%P%R\t %12-14f, %16-18f, %0-3f", "FRD%c%P%R\t %12-14f, %16-18f, %0-3f", "POL%c%P%R\t %12-14f, %16-18f, %0-3f", "MVF%c%P%R\t %12-14f, %0-3f", "MNF%c%P%R\t %12-14f, %0-3f", "ABS%c%P%R\t %12-14f, %0-3f", "RND%c%P%R\t %12-14f, %0-3f", "SQT%c%P%R\t %12-14f, %0-3f", "LOG%c%P%R\t %12-14f, %0-3f", "LGN%c%P%R\t %12-14f, %0-3f", "EXP%c%P%R\t %12-14f, %0-3f", "SIN%c%P%R\t %12-14f, %0-3f", "COS%c%P%R\t %12-14f, %0-3f", "TAN%c%P%R\t %12-14f, %0-3f", "ASN%c%P%R\t %12-14f, %0-3f", "ACS%c%P%R\t %12-14f, %0-3f", "ATN%c%P%R\t %12-14f, %0-3f", "URD%c%P%R\t %12-14f, %0-3f", "NRM%c%P%R\t %12-14f, %0-3f", "FLT%c%P%R\t %16-18f, %12-15r", "FIX%c%R\t %12-15r, %0-2f", "WFS%c\t %12-15r", "RFS%c\t %12-15r", "WFC%c\t %12-15r", "RFC%c\t %12-15r", "CMF%c\t %16-18f, %0-3f", "CNF%c\t %16-18f, %0-3f", "CMFE%c\t %16-18f, %0-3f", "CNFE%c\t %16-18f, %0-3f", "STF%c%Q\t %12-14f, %A", "LDF%c%Q\t %12-14f, %A", "SFM%c\t %12-14f, %F, %A", "LFM%c\t %12-14f, %F, %A", "VPUSH%c\t %B", "VSTMDB%c\t %16-19r!, %B", "VLDMDB%c\t %16-19r!, %B", "VSTMIA%c\t %16-19r%21\'!, %B", "VPOP%c\t %B", "VLDMIA%c\t %16-19r%21\'!, %B", "VSTR%c\t %12-15,22D, %A", "VLDR%c\t %12-15,22D, %A", "VPUSH%c\t %y3", "VSTMDB%c\t %16-19r!, %y3", "VLDMDB%c\t %16-19r!, %y3", "VSTMIA%c\t %16-19r%21\'!, %y3", "VPOP%c\t %y3", "VLDMIA%c\t %16-19r%21\'!, %y3", "VSTR%c\t %y1, %A", "VLDR%c\t %y1, %A", "FSTMDBX%c\t %16-19r!, %z3\t ;@ Deprecated", "FLDMDBX%c\t %16-19r!, %z3\t ;@ Deprecated", "FSTMIAX%c\t %16-19r%21\'!, %z3\t ;@ Deprecated", "FLDMIAX%c\t %16-19r%21\'!, %z3\t ;@ Deprecated", "VDUP%c.32\t %16-19,7D, %12-15r", "VDUP%c.16\t %16-19,7D, %12-15r", "VDUP%c.32\t %16-19,7Q, %12-15r", "VDUP%c.16\t %16-19,7Q, %12-15r", "VDUP%c.8\t %16-19,7D, %12-15r", "VDUP%c.8\t %16-19,7Q, %12-15r", "VMOV%c\t %0-3,5D, %12-15r, %16-19r", "VMOV%c\t %12-15r, %16-19r, %0-3,5D", "VMOV%c.32\t %16-19,7D[%21d], %12-15r", "VMOV%c.32\t %12-15r, %16-19,7D[%21d]", "VMOV%c.16\t %16-19,7D[%6,21d], %12-15r", "VMOV%c.%23?US16\t %12-15r, %16-19,7D[%6,21d]", "VMOV%c.8\t %16-19,7D[%5,6,21d], %12-15r", "VMOV%c.%23?US8\t %12-15r, %16-19,7D[%5,6,21d]", "VCVT%7?TB%c.F64.F16\t %z1, %y0", "VCVT%7?TB%c.F16.F64\t %y1, %z0", "VCVT%7?TB%c.F32.F16\t %y1, %y0", "VCVT%7?TB%c.F16.F32\t %y1, %y0", "VMSR%c\t fpsid, %12-15r", "VMSR%c\t fpscr, %12-15r", "VMSR%c\t mvfr1, %12-15r", "VMSR%c\t mvfR0, %12-15r", "VMSR%c\t fpexc, %12-15r", "VMSR%c\t fpinst, %12-15r\t @ Impl def", "VMSR%c\t fpinst2, %12-15r\t @ Impl def", "VMRS%c\t %12-15r, fpsid", "VMRS%c\t APSR_nzcv, fpscr", "VMRS%c\t %12-15r, fpscr", "VMRS%c\t %12-15r, mvfr1", "VMRS%c\t %12-15r, mvfR0", "VMRS%c\t %12-15r, fpexc", "VMRS%c\t %12-15r, fpinst\t @ Impl def", "VMRS%c\t %12-15r, fpinst2\t @ Impl def", "VMOV%c.32\t %z2[%21d], %12-15r", "VMOV%c.32\t %12-15r, %z2[%21d]", "VMSR%c\t <impl def %16-19x>, %12-15r", "VMRS%c\t %12-15r, <impl def %16-19x>", "VMOV%c\t %y2, %12-15r", "VMOV%c\t %12-15r, %y2", "VCMP%7\'E%c.F32\t %y1, #0.0", "VCMP%7\'E%c.F64\t %z1, #0.0", "VMOV%c.F32\t %y1, %y0", "VABS%c.F32\t %y1, %y0", "VMOV%c.F64\t %z1, %z0", "VABS%c.F64\t %z1, %z0", "VNEG%c.F32\t %y1, %y0", "VSQRT%c.F32\t %y1, %y0", "VNEG%c.F64\t %z1, %z0", "VSQRT%c.F64\t %z1, %z0", "VCVT%c.F64.F32\t %z1, %y0", "VCVT%c.F32.F64\t %y1, %z0", "VCVT%c.F32.%7?SU32\t %y1, %y0", "VCVT%c.F64.%7?SU32\t %z1, %y0", "VCMP%7\'E%c.F32\t %y1, %y0", "VCMP%7\'E%c.F64\t %z1, %z0", "VCVT%c.F32.%16?US%7?31%7?26\t %y1, %y1, #%5,0-3k", "VCVT%c.F64.%16?US%7?31%7?26\t %z1, %z1, #%5,0-3k", "VCVT%7`R%c.%16?SU32.F32\t %y1, %y0", "VCVT%7`R%c.%16?SU32.F64\t %y1, %z0", "VCVT%c.%16?US%7?31%7?26.F32\t %y1, %y1, #%5,0-3k", "VCVT%c.%16?US%7?31%7?26.F64\t %z1, %z1, #%5,0-3k", "VMOV%c\t %12-15r, %16-19r, %z0", "VMOV%c.F32\t %y1, #%0-3,16-19d", "VMOV%c.F64\t %z1, #%0-3,16-19d", "VMOV%c\t %y4, %12-15r, %16-19r", "VMOV%c\t %z0, %12-15r, %16-19r", "VMOV%c\t %12-15r, %16-19r, %y4", "VMLA%c.F32\t %y1, %y2, %y0", "VMLS%c.F32\t %y1, %y2, %y0", "VMLA%c.F64\t %z1, %z2, %z0", "VMLS%c.F64\t %z1, %z2, %z0", "VNMLS%c.F32\t %y1, %y2, %y0", "VNMLA%c.F32\t %y1, %y2, %y0", "VNMLS%c.F64\t %z1, %z2, %z0", "VNMLA%c.F64\t %z1, %z2, %z0", "VMUL%c.F32\t %y1, %y2, %y0", "VNMUL%c.F32\t %y1, %y2, %y0", "VMUL%c.F64\t %z1, %z2, %z0", "VNMUL%c.F64\t %z1, %z2, %z0", "VADD%c.F32\t %y1, %y2, %y0", "VSUB%c.F32\t %y1, %y2, %y0", "VADD%c.F64\t %z1, %z2, %z0", "VSUB%c.F64\t %z1, %z2, %z0", "VDIV%c.F32\t %y1, %y2, %y0", "VDIV%c.F64\t %z1, %z2, %z0", "VFMA%c.F32\t %y1, %y2, %y0", "VFMA%c.F64\t %z1, %z2, %z0", "VFMS%c.F32\t %y1, %y2, %y0", "VFMS%c.F64\t %z1, %z2, %z0", "VFNMA%c.F32\t %y1, %y2, %y0", "VFNMA%c.F64\t %z1, %z2, %z0", "VFNMS%c.F32\t %y1, %y2, %y0", "VFNMS%c.F64\t %z1, %z2, %z0", "VSEL%20-21c%u.F32\t %y1, %y2, %y0", "VSEL%20-21c%u.F64\t %z1, %z2, %z0", "VMAXNM%u.F32\t %y1, %y2, %y0", "VMAXNM%u.F64\t %z1, %z2, %z0", "VMINNM%u.F32\t %y1, %y2, %y0", "VMINNM%u.F64\t %z1, %z2, %z0", "VCVT%16-17?MPNA%u.%7?SU32.F32\t %y1, %y0", "VCVT%16-17?MPNA%u.%7?SU32.F64\t %y1, %z0", "VRINT%7,16??XZR%c.F32\t %y1, %y0", "VRINT%7,16??XZR%c.F64\t %z1, %z0", "VRINT%16-17?MPNA%u.F32\t %y1, %y0", "VRINT%16-17?MPNA%u.F64\t %z1, %z0", "MCRR%c\t %8-11d, %4-7d, %12-15R, %16-19r, CR%0-3d", "MRRC%c\t %8-11d, %4-7d, %12-15Ru, %16-19Ru, CR%0-3d", "CDP%c\t %8-11d, %20-23d, CR%12-15d, CR%16-19d, CR%0-3d, {%5-7d}", "MRC%c\t %8-11d, %21-23d, APSR_nzcv, CR%16-19d, CR%0-3d, {%5-7d}", "MRC%c\t %8-11d, %21-23d, %12-15r, CR%16-19d, CR%0-3d, {%5-7d}", "MCR%c\t %8-11d, %21-23d, %12-15R, CR%16-19d, CR%0-3d, {%5-7d}", "STC%22\'L%c\t %8-11d, CR%12-15d, %A", "LDC%22\'L%c\t %8-11d, CR%12-15d, %A", "MRRC2%c\t %8-11d, %4-7d, %12-15Ru, %16-19Ru, CR%0-3d", "MCRR2%c\t %8-11d, %4-7d, %12-15R, %16-19R, CR%0-3d", "LDC2%22\'L%c\t %8-11d, CR%12-15d, %A", "STC2%22\'L%c\t %8-11d, CR%12-15d, %A", "CDP2%c\t %8-11d, %20-23d, CR%12-15d, CR%16-19d, CR%0-3d, {%5-7d}", "MCR2%c\t %8-11d, %21-23d, %12-15R, CR%16-19d, CR%0-3d, {%5-7d}", "MRC2%c\t %8-11d, %21-23d, %12-15r, CR%16-19d, CR%0-3d, {%5-7d}"};
        }

        static int getOpcode(ArmDis ret, long addr, String data, boolean thumb, int dbg) {
            int count;
            int bits;
            int bits;
            int bits;
            int bits;
            int offset;
            int v35;
            int value;
            Input in = ret.input;
            int[] NUM = Coproc.NUM;
            String[] STR = Coproc.STR;
            int i = 0;
        alab2:
            while(true) {
                if(i >= STR.length) {
                    return -1;
                }
                int out = NUM[i * 2];
                in.reset();
                try {
                    String asm = STR[i];
                    int mask = NUM[i * 2 + 1];
                    if(thumb) {
                        mask |= 0xF0000000;
                        out |= 0xE0000000;
                    }
                    if(mask != 0) {
                        int fail = -1;
                        int j = 0;
                        int v7 = asm.length();
                        while(j < v7) {
                            int c = ArmDis.charAt(asm, v7, j);
                            if(c <= 0x20) {
                                c = 0x20;
                                while(j < v7 && ArmDis.charAt(asm, v7, j + 1) <= 0x20) {
                                    ++j;
                                }
                            }
                            if(c == 37) {
                            label_30:
                                ++j;
                                int c = ArmDis.charAt(asm, v7, j);
                            alab1:
                                switch(c) {
                                    case 37: {
                                        if(!in.check('%')) {
                                            goto label_318;
                                        }
                                        break;
                                    }
                                    case 0x30: 
                                    case 49: 
                                    case 50: 
                                    case 51: 
                                    case 52: 
                                    case 53: 
                                    case 54: 
                                    case 55: 
                                    case 56: 
                                    case 57: {
                                        int value = -1;
                                        int j_ = j;
                                        ret.valuep = out;
                                        j = ArmDis.decodeBitfield(asm, v7, j, ret, -1);
                                        int v12 = ret.widthp;
                                        int c = ArmDis.charAt(asm, v7, j);
                                        switch(c) {
                                            case 0x3F: {
                                                int v15 = in.getAndNext();
                                                int k = 0;
                                                int sk = 1 << v12;
                                                while(k < sk) {
                                                    if(ArmDis.charAt(asm, v7, j + sk - k) == v15) {
                                                        value = k;
                                                        if(true) {
                                                            break;
                                                        }
                                                    }
                                                    else {
                                                        ++k;
                                                    }
                                                }
                                                if(value == -1) {
                                                    goto label_318;
                                                }
                                                else {
                                                    ret.valuep = out;
                                                    ArmDis.decodeBitfield(asm, v7, j_, ret, value);
                                                    out = ret.valuep;
                                                    j += 1 << v12;
                                                    break alab1;
                                                }
                                                goto label_75;
                                            }
                                            case 68: {
                                            label_75:
                                                if(in.check('D')) {
                                                    int value = in.getInt();
                                                    ret.valuep = out;
                                                    ArmDis.decodeBitfield(asm, v7, j_, ret, value);
                                                    out = ret.valuep;
                                                    if((~((1 << v12) - 1) & value) != 0) {
                                                        fail = j;
                                                    }
                                                }
                                                else {
                                                    goto label_318;
                                                }
                                                break alab1;
                                            }
                                            case 81: {
                                                if(in.check('Q')) {
                                                    int v19 = in.getInt();
                                                    ret.valuep = out;
                                                    ArmDis.decodeBitfield(asm, v7, j_, ret, v19 << 1);
                                                    out = ret.valuep;
                                                    if((~((1 << v12) - 1) & v19 << 1) != 0) {
                                                        fail = j;
                                                    }
                                                }
                                                else {
                                                    goto label_318;
                                                }
                                                break alab1;
                                            }
                                            case 39: 
                                            case 0x60: {
                                                ++j;
                                                int c = ArmDis.charAt(asm, v7, j);
                                                if(c != 69) {
                                                    if(c == 76) {
                                                        if(in.check("LEQ")) {
                                                            in.pos -= 3;
                                                        }
                                                        else if(in.check("LS") || in.check("LT") || in.check("LE")) {
                                                            in.pos -= 2;
                                                            break alab1;
                                                        }
                                                    }
                                                }
                                                else if(in.check("EQ")) {
                                                    in.pos -= 2;
                                                    break alab1;
                                                }
                                                if(in.check(((char)c)) == (c == 39)) {
                                                    out = ret.valuep;
                                                }
                                                break alab1;
                                            }
                                            case 99: {
                                            label_99:
                                                if(in.check("EQ")) {
                                                    value = 0;
                                                }
                                                else if(in.check("VS")) {
                                                    value = 1;
                                                }
                                                else if(in.check("GE")) {
                                                    value = 2;
                                                }
                                                else if(in.check("GT")) {
                                                    value = 3;
                                                }
                                                else {
                                                    goto label_318;
                                                }
                                                ret.valuep = out;
                                                ArmDis.decodeBitfield(asm, v7, j_, ret, value);
                                                out = ret.valuep;
                                                break alab1;
                                            }
                                            case 100: {
                                                int value = in.getInt();
                                                ret.valuep = out;
                                                ArmDis.decodeBitfield(asm, v7, j_, ret, value);
                                                out = ret.valuep;
                                                if((~((1 << v12) - 1) & value) != 0) {
                                                    fail = j;
                                                }
                                                break alab1;
                                            }
                                            case 102: {
                                                if(in.check('#')) {
                                                    int value = in.index(ArmDis.FP);
                                                    if(value == -1) {
                                                        goto label_318;
                                                    }
                                                    else {
                                                        ret.valuep = out;
                                                        ArmDis.decodeBitfield(asm, v7, j_, ret, value | 8);
                                                        out = ret.valuep;
                                                        break alab1;
                                                    }
                                                    goto label_128;
                                                }
                                                else {
                                                label_128:
                                                    if(in.check('F')) {
                                                        int value = in.getInt();
                                                        ret.valuep = out;
                                                        ArmDis.decodeBitfield(asm, v7, j_, ret, value & 7);
                                                        out = ret.valuep;
                                                        if((value & -8) != 0) {
                                                            fail = j;
                                                        }
                                                    }
                                                    else {
                                                        goto label_318;
                                                    }
                                                }
                                                break alab1;
                                            }
                                            case 107: {
                                                int value = ((out & 0x80) == 0 ? 0x20 : 16) - in.getInt();
                                                ret.valuep = out;
                                                ArmDis.decodeBitfield(asm, v7, j_, ret, value);
                                                out = ret.valuep;
                                                if((~((1 << v12) - 1) & value) != 0) {
                                                    fail = j;
                                                }
                                                break alab1;
                                            }
                                            case 82: 
                                            case 0x72: {
                                                int value = in.index(ArmDis.REG);
                                                if(value == -1) {
                                                    goto label_318;
                                                }
                                                else {
                                                    if(ArmDis.charAt(asm, v7, j + 1) == 0x75) {
                                                        ++j;
                                                    }
                                                    ret.valuep = out;
                                                    ArmDis.decodeBitfield(asm, v7, j_, ret, value);
                                                    out = ret.valuep;
                                                    break alab1;
                                                }
                                                goto label_99;
                                            }
                                            case 120: {
                                                if(in.check("0X")) {
                                                    long v26 = in.getHex();
                                                    ret.valuep = out;
                                                    ArmDis.decodeBitfield(asm, v7, j_, ret, ((int)v26));
                                                    out = ret.valuep;
                                                    if((~((1 << v12) - 1) & ((int)v26)) != 0) {
                                                        fail = j;
                                                    }
                                                }
                                                else {
                                                    goto label_318;
                                                }
                                                break alab1;
                                            }
                                            default: {
                                                throw new RuntimeException("10 Invalid format char \'" + ((char)c) + "\'; " + i + "; " + j + "; \'" + asm + "\'");
                                            }
                                        }
                                    }
                                    case 65: {
                                        if(in.check('[')) {
                                            int v27 = in.index(ArmDis.REG);
                                            if(v27 == -1) {
                                                goto label_318;
                                            }
                                            else {
                                                out |= v27 << 16;
                                                boolean checkW = false;
                                                if(in.check(",#")) {
                                                    out |= 0x1000000;
                                                    out = in.check('-') ? out | 0x1000000 : out | 0x800000;
                                                    int v28 = in.getInt();
                                                    if((v28 & 3) != 0) {
                                                        fail = j;
                                                    }
                                                    out |= v28 >> 2 & 0xFF;
                                                    if(in.check(']')) {
                                                        if(v28 >> 2 == 0) {
                                                            checkW = true;
                                                        }
                                                        else if(in.check('!')) {
                                                            out |= 0x200000;
                                                        }
                                                        if((v28 >> 2 & 0xFFFFFF00) != 0) {
                                                            fail = j;
                                                        }
                                                    }
                                                    else {
                                                        goto label_318;
                                                    }
                                                }
                                                else if(!in.check(']')) {
                                                    goto label_318;
                                                }
                                                else if(in.check(", #")) {
                                                    if(!in.check('-')) {
                                                        out |= 0x800000;
                                                    }
                                                    out |= 0x200000;
                                                    int v29 = in.getInt();
                                                    if((v29 & 3) != 0) {
                                                        fail = j;
                                                    }
                                                    out |= v29 >> 2 & 0xFF;
                                                    if((v29 >> 2 & 0xFFFFFF00) != 0) {
                                                        fail = j;
                                                    }
                                                }
                                                else if(in.check(", {")) {
                                                    boolean neg = in.get() == 45;
                                                    int v30 = in.getInt();
                                                    if(v30 == 0 && !neg) {
                                                        out |= 0x800000;
                                                    }
                                                    out |= v30 & 0xFF;
                                                    for(int k = 0; k < i; ++k) {
                                                        int cmask = NUM[k * 2 + 1];
                                                        if(((0xF0000000 & out) != 0xF0000000 || (0xF0000000 & cmask) == 0xF0000000) && (out & cmask) == NUM[k * 2]) {
                                                            out |= 0x800000;
                                                            break;
                                                        }
                                                    }
                                                    if(!in.check('}')) {
                                                        goto label_318;
                                                    }
                                                    else if((v30 & 0xFFFFFF00) != 0) {
                                                        fail = j;
                                                    }
                                                }
                                                else {
                                                    out |= 0x1800000;
                                                    checkW = true;
                                                }
                                                if(checkW) {
                                                    for(int k = 0; k < i; ++k) {
                                                        int cmask = NUM[k * 2 + 1];
                                                        if(((0xF0000000 & out) != 0xF0000000 || (0xF0000000 & cmask) == 0xF0000000) && (out & cmask) == NUM[k * 2]) {
                                                            out |= 0x200000;
                                                            break;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        else {
                                            goto label_318;
                                        }
                                        break;
                                    }
                                    case 66: {
                                        if(in.check("{D")) {
                                            v35 = in.getInt();
                                            offset = in.check("-D") ? in.getInt() - (v35 - 1) : 1;
                                        }
                                        else if(in.check('D')) {
                                            v35 = in.getInt();
                                            offset = 0;
                                        }
                                        else {
                                            goto label_318;
                                        }
                                        out = out | (offset & 0x3F) << 1 | ((v35 & 15) << 12 | (v35 & 16) << 18);
                                        if(offset != 0 && !in.check('}')) {
                                            goto label_318;
                                        }
                                        else {
                                            if((offset & 0xFFFFFFC0) != 0 || (v35 & 0xFFFFFFE0) != 0) {
                                                fail = j;
                                            }
                                            if(offset != 0 && (v35 + offset - 1 & 0xFFFFFFE0) != 0) {
                                                fail = j;
                                            }
                                        }
                                        break;
                                    }
                                    case 70: {
                                        switch(in.getAndNext()) {
                                            case 49: {
                                                bits = 0x8000;
                                                out |= bits;
                                                ++j;
                                                continue;
                                            }
                                            case 50: {
                                                bits = 0x400000;
                                                out |= bits;
                                                ++j;
                                                continue;
                                            }
                                            case 51: {
                                                bits = 0x408000;
                                                out |= bits;
                                                ++j;
                                                continue;
                                            }
                                            case 52: {
                                                break alab1;
                                            }
                                            default: {
                                                goto label_318;
                                            }
                                        }
                                    }
                                    case 80: {
                                        switch(in.getAndNext()) {
                                            case 68: {
                                                bits = 0x80;
                                                out |= bits;
                                                ++j;
                                                continue;
                                            }
                                            case 69: {
                                                bits = 0x80000;
                                                out |= bits;
                                                ++j;
                                                continue;
                                            }
                                            case 83: {
                                                break alab1;
                                            }
                                            default: {
                                                goto label_318;
                                            }
                                        }
                                    }
                                    case 81: {
                                        switch(in.getAndNext()) {
                                            case 68: {
                                                bits = 0x8000;
                                                out |= bits;
                                                ++j;
                                                continue;
                                            }
                                            case 69: {
                                                bits = 0x400000;
                                                out |= bits;
                                                ++j;
                                                continue;
                                            }
                                            case 80: {
                                                bits = 0x408000;
                                                out |= bits;
                                                ++j;
                                                continue;
                                            }
                                            case 83: {
                                                break alab1;
                                            }
                                            default: {
                                                goto label_318;
                                            }
                                        }
                                    }
                                    case 82: {
                                        switch(in.getAndNext()) {
                                            case 77: {
                                                bits = 0x40;
                                                break;
                                            }
                                            case 80: {
                                                bits = 0x20;
                                                break;
                                            }
                                            case 90: {
                                                bits = 0x60;
                                                break;
                                            }
                                            default: {
                                                bits = 0;
                                                --in.pos;
                                            }
                                        }
                                        out |= bits;
                                        break;
                                    }
                                    case 99: 
                                    case 0x75: {
                                        int value = in.index(ArmDis.COND);
                                        if(value == -1) {
                                            goto label_318;
                                        }
                                        else {
                                            if(value > 15) {
                                                value = 14;
                                            }
                                            out |= value << 28;
                                            break;
                                        }
                                        goto label_273;
                                    }
                                    case 0x79: 
                                    case 0x7A: {
                                    label_273:
                                        ++j;
                                        int c = ArmDis.charAt(asm, v7, j);
                                        if(c == 51 && !in.check('{') || !in.check(((char)(c == 0x79 ? 68 : 83)))) {
                                            goto label_318;
                                        }
                                        else {
                                            int v43 = in.getInt();
                                            switch(c) {
                                                case 50: {
                                                    out = c == 0x79 ? out | (v43 & 15) << 16 | (v43 >> 4 & 1) << 7 : out | (v43 & 1) << 7 | (v43 >> 1 & 15) << 16;
                                                    break;
                                                }
                                                case 49: 
                                                case 51: {
                                                    out = c == 0x79 ? out | (v43 & 15) << 12 | (v43 >> 4 & 1) << 22 : out | (v43 & 1) << 22 | (v43 >> 1 & 15) << 12;
                                                    if(c == 51) {
                                                        if(!in.check('-')) {
                                                            count = 0;
                                                        }
                                                        else if(in.check(((char)(c == 0x79 ? 68 : 83)))) {
                                                            count = in.getInt() - v43;
                                                            if(count == 0) {
                                                                fail = j;
                                                            }
                                                        }
                                                        else {
                                                            goto label_318;
                                                        }
                                                        int count = c == 0x79 ? count + 1 : count + 1 << 1;
                                                        out |= count & 0xFF;
                                                        if(!in.check('}')) {
                                                            goto label_318;
                                                        }
                                                        else if((count & 0xFFFFFF00) != 0) {
                                                            fail = j;
                                                        }
                                                    }
                                                    break;
                                                }
                                                case 0x30: 
                                                case 52: {
                                                    out = c == 0x79 ? out | v43 & 15 | (v43 >> 4 & 1) << 5 : out | (v43 & 1) << 5 | v43 >> 1 & 15;
                                                    if(c == 52) {
                                                        if(!in.check(", ") || !in.check(((char)(c == 0x79 ? 68 : 83)))) {
                                                            goto label_318;
                                                        }
                                                        else if(in.getInt() != v43 + 1) {
                                                            fail = j;
                                                        }
                                                    }
                                                    break;
                                                }
                                                default: {
                                                    throw new RuntimeException("21 Invalid format char \'" + ((char)c) + "\'; " + i + "; " + j + "; \'" + asm + "\'");
                                                }
                                            }
                                            if((v43 & 0xFFFFFFE0) != 0) {
                                                fail = j;
                                            }
                                        }
                                        break;
                                    }
                                    default: {
                                        throw new RuntimeException("30 Invalid format char \'" + ((char)c) + "\'; " + i + "; " + j + "; \'" + asm + "\'");
                                    }
                                }
                                ++j;
                                continue;
                            }
                            else {
                                if(c == 59) {
                                    break;
                                }
                                else {
                                    if(97 <= c && c <= 0x7A) {
                                        c = (char)(c - 0x20);
                                    }
                                    if(in.check(((char)c))) {
                                        ++j;
                                        continue;
                                    }
                                    else {
                                        goto label_318;
                                    }
                                }
                                goto label_30;
                            }
                            ++i;
                            continue alab2;
                        }
                        in.check(' ');
                        if(fail == -1) {
                            int v46 = in.get();
                            if(v46 > 0x20 && v46 != 59 || (0xF0000000 & out) == 0xF0000000 && (0xF0000000 & mask) != 0xF0000000 || (out & mask) != NUM[i * 2]) {
                                goto label_318;
                            }
                            else {
                                int k = 0;
                                while(k < i) {
                                    int cmask = NUM[k * 2 + 1];
                                    if(((0xF0000000 & out) != 0xF0000000 || (0xF0000000 & cmask) == 0xF0000000) && (out & cmask) == NUM[k * 2]) {
                                        goto label_318;
                                    }
                                    ++k;
                                }
                                return out;
                            }
                        }
                        else {
                            goto label_318;
                        }
                    }
                    while(true) {
                        ++i;
                        continue alab2;
                    label_318:
                        if(in.pos <= in.best) {
                            break;
                        }
                        in.best = in.pos;
                        in.bestOut = out;
                    }
                    ++i;
                }
                catch(NextAsm unused_ex) {
                    goto label_318;
                }
            }
        }

        static String getOpcode(ArmDis ret, long addr, int data, boolean thumb) {
            int regno;
            int cond;
            int commentValue;
            boolean isUnpredictable;
            int uReg;
            boolean WRITEBACK_BIT_SET = (0x200000 & data) != 0;
            boolean NEGATIVE_BIT_SET = (0x800000 & data) == 0;
            boolean PRE_BIT_SET = (0x1000000 & data) != 0;
            int i;
            for(i = 0; true; ++i) {
                if(i >= Coproc.STR.length) {
                    return null;
                }
                uReg = 16;
                isUnpredictable = false;
                commentValue = 0;
                int mask = Coproc.NUM[i * 2 + 1];
                int value = Coproc.NUM[i * 2];
                if(thumb) {
                    mask |= 0xF0000000;
                    value |= 0xE0000000;
                    cond = 16;
                }
                else if((0xF0000000 & data) == 0xF0000000) {
                    mask |= 0xF0000000;
                    cond = 16;
                }
                else {
                    int v8 = data >> 28 & 15;
                    cond = v8 == 14 ? 16 : v8;
                }
                if((data & mask) == value) {
                    break;
                }
            }
            StringBuilder out = new StringBuilder();
            String asm = Coproc.STR[i];
            int j = 0;
            int v10 = asm.length();
            while(true) {
                if(j >= v10) {
                    if(commentValue > 0x20 || commentValue < -16) {
                        out.append("\t ; 0x").append(Integer.toHexString(commentValue).toUpperCase(Locale.US));
                    }
                    if(isUnpredictable) {
                        out.append("\t ; <UNPREDICTABLE>");
                    }
                    return out.toString();
                }
                int v11 = ArmDis.charAt(asm, v10, j);
                if(v11 == 37) {
                    ++j;
                    int c = ArmDis.charAt(asm, v10, j);
                alab1:
                    switch(c) {
                        case 37: {
                            out.append('%');
                            break;
                        }
                        case 0x30: 
                        case 49: 
                        case 50: 
                        case 51: 
                        case 52: 
                        case 53: 
                        case 54: 
                        case 55: 
                        case 56: 
                        case 57: {
                            j = ArmDis.decodeBitfield(asm, v10, j, data, ret);
                            int value = ret.valuep;
                            int v14 = ret.widthp;
                            int c = ArmDis.charAt(asm, v10, j);
                            switch(c) {
                                case 39: {
                                    ++j;
                                    int c = ArmDis.charAt(asm, v10, j);
                                    if(value == (1 << v14) - 1) {
                                        ArmDis.append(out, ((char)c));
                                    }
                                    break alab1;
                                }
                                case 0x3F: {
                                    ArmDis.append(out, ArmDis.charAt(asm, v10, (1 << v14) + j - value));
                                    j += 1 << v14;
                                    break alab1;
                                }
                                case 68: {
                                    out.append('D').append(value);
                                    break alab1;
                                }
                                case 81: {
                                    if((value & 1) == 0) {
                                        out.append('Q').append(value >> 1);
                                    }
                                    else {
                                        out.append("<illegal reg Q").append(value >> 1).append(".5>");
                                    }
                                    break alab1;
                                }
                                case 82: {
                                    if(value == 15) {
                                        isUnpredictable = true;
                                    }
                                    goto label_96;
                                }
                                case 0x60: {
                                    ++j;
                                    int c = ArmDis.charAt(asm, v10, j);
                                    if(value == 0) {
                                        ArmDis.append(out, ((char)c));
                                    }
                                    break alab1;
                                }
                                case 99: {
                                    switch(value) {
                                        case 0: {
                                            out.append("EQ");
                                            break;
                                        }
                                        case 1: {
                                            out.append("VS");
                                            break;
                                        }
                                        case 2: {
                                            out.append("GE");
                                            break;
                                        }
                                        case 3: {
                                            out.append("GT");
                                            break;
                                        }
                                        default: {
                                            out.append("??");
                                        }
                                    }
                                    break alab1;
                                }
                                case 100: {
                                    out.append(value);
                                    commentValue = value;
                                    break alab1;
                                }
                                case 102: {
                                    if(value > 7) {
                                        out.append('#').append(ArmDis.FP[value & 7]);
                                    }
                                    else {
                                        out.append('F').append(value);
                                    }
                                    break alab1;
                                }
                                case 107: {
                                    out.append(((data & 0x80) == 0 ? 0x20 : 16) - value);
                                    break alab1;
                                }
                                case 0x72: {
                                label_96:
                                    if(ArmDis.charAt(asm, v10, j + 1) == 0x75) {
                                        ++j;
                                        if(uReg == value) {
                                            isUnpredictable = true;
                                        }
                                        uReg = value;
                                    }
                                    out.append(ArmDis.REG[value]);
                                    break alab1;
                                }
                                case 120: {
                                    out.append("0x").append(Integer.toHexString(value).toUpperCase(Locale.US));
                                    break alab1;
                                }
                                default: {
                                    throw new RuntimeException("10 Invalid format char \'" + ((char)c) + "\'; " + i + "; " + j + "; \'" + asm + "\'");
                                }
                            }
                        }
                        case 65: {
                            int rn = data >> 16 & 15;
                            int offset = data & 0xFF;
                            out.append('[');
                            out.append(ArmDis.REG[rn]);
                            if(PRE_BIT_SET || WRITEBACK_BIT_SET) {
                                offset *= 4;
                                offset = NEGATIVE_BIT_SET ? -offset : offset * 4;
                                if(rn != 15) {
                                    commentValue = offset;
                                }
                            }
                            if(!PRE_BIT_SET) {
                                out.append(']');
                                if(!WRITEBACK_BIT_SET) {
                                    out.append(", {");
                                    if(NEGATIVE_BIT_SET && offset == 0) {
                                        out.append('-');
                                    }
                                    out.append(offset);
                                    out.append('}');
                                    commentValue = offset;
                                }
                                else if(offset != 0) {
                                    out.append(", #");
                                    out.append(offset);
                                }
                                else if(NEGATIVE_BIT_SET) {
                                    out.append(", #-0");
                                }
                            }
                            else if(offset != 0) {
                                out.append(",#");
                                out.append(offset);
                                out.append(']');
                                if(WRITEBACK_BIT_SET) {
                                    out.append('!');
                                }
                            }
                            else if(NEGATIVE_BIT_SET) {
                                out.append(",#-0]");
                            }
                            else {
                                out.append(']');
                            }
                            if(rn == 15 && (PRE_BIT_SET || WRITEBACK_BIT_SET)) {
                                out.append("\t ; ");
                                ArmDis.printAddr(out, addr, ((long)((thumb ? 2 : 4) * 2 + offset)) - (3L & addr));
                            }
                            break;
                        }
                        case 66: {
                            int regno = data >> 12 & 15 | data >> 18 & 16;
                            int offset = data >> 1 & 0x3F;
                            if(offset == 0) {
                                out.append('D').append(regno);
                            }
                            else if(offset == 1) {
                                out.append("{D").append(regno).append('}');
                            }
                            else if(regno + offset > 0x20) {
                                out.append("{D").append(regno).append("-<overflow reg D").append(regno + offset - 1).append(">}");
                            }
                            else {
                                out.append("{D").append(regno).append("-D").append(regno + offset - 1).append('}');
                            }
                            break;
                        }
                        case 70: {
                            switch(0x408000 & data) {
                                case 0: {
                                    out.append('4');
                                    break;
                                }
                                case 0x8000: {
                                    out.append('1');
                                    break;
                                }
                                case 0x400000: {
                                    out.append('2');
                                    break;
                                }
                                default: {
                                    out.append('3');
                                }
                            }
                            break;
                        }
                        case 80: {
                            switch(0x80080 & data) {
                                case 0: {
                                    out.append('S');
                                    break;
                                }
                                case 0x80: {
                                    out.append('D');
                                    break;
                                }
                                case 0x80000: {
                                    out.append('E');
                                    break;
                                }
                                default: {
                                    out.append("<illegal precision>");
                                }
                            }
                            break;
                        }
                        case 81: {
                            switch(0x408000 & data) {
                                case 0: {
                                    out.append('S');
                                    break;
                                }
                                case 0x8000: {
                                    out.append('D');
                                    break;
                                }
                                case 0x400000: {
                                    out.append('E');
                                    break;
                                }
                                default: {
                                    out.append('P');
                                }
                            }
                            break;
                        }
                        case 82: {
                            switch(data & 0x60) {
                                case 0: {
                                    break alab1;
                                }
                                case 0x20: {
                                    out.append('P');
                                    break alab1;
                                }
                                case 0x40: {
                                    out.append('M');
                                    break alab1;
                                }
                                default: {
                                    out.append('Z');
                                    break alab1;
                                }
                            }
                        }
                        case 99: {
                            out.append(ArmDis.COND[cond]);
                            break;
                        }
                        case 0x75: {
                            if(cond != 16) {
                                isUnpredictable = true;
                            }
                            out.append(ArmDis.COND[cond]);
                            break;
                        }
                        case 0x79: 
                        case 0x7A: {
                            ++j;
                            int c = ArmDis.charAt(asm, v10, j);
                            switch(c) {
                                case 50: {
                                    int regno = data >> 16 & 15;
                                    regno = c == 0x79 ? regno + ((data >> 7 & 1) << 4) : (regno << 1) + (data >> 7 & 1);
                                    break;
                                }
                                case 49: 
                                case 51: {
                                    if(c == 51) {
                                        out.append('{');
                                    }
                                    int regno = data >> 12 & 15;
                                    regno = c == 0x79 ? (regno << 1) + (data >> 22 & 1) : regno + ((data >> 22 & 1) << 4);
                                    break;
                                }
                                case 0x30: 
                                case 52: {
                                    regno = c == 0x79 ? ((data & 15) << 1) + (data >> 5 & 1) : (data & 15) + ((data >> 5 & 1) << 4);
                                    break;
                                }
                                default: {
                                    throw new RuntimeException("20 Invalid format char \'" + ((char)c) + "\'; " + i + "; " + j + "; \'" + asm + "\'");
                                }
                            }
                            out.append(((char)(c == 0x79 ? 68 : 83))).append(regno);
                            if(c == 51) {
                                int count = c == 0x79 ? data & 0xFF : (data & 0xFF) >> 1;
                                if(count - 1 != 0) {
                                    out.append('-').append(((char)(c == 0x79 ? 68 : 83))).append(regno + (count - 1));
                                }
                                out.append('}');
                            }
                            else if(c == 52) {
                                out.append(", ").append(((char)(c == 0x79 ? 68 : 83))).append(regno + 1);
                            }
                            break;
                        }
                        default: {
                            throw new RuntimeException("30 Invalid format char \'" + ((char)c) + "\'; " + i + "; " + j + "; \'" + asm + "\'");
                        }
                    }
                }
                else {
                    ArmDis.append(out, ((char)v11));
                }
                ++j;
            }
        }
    }

    static class Input {
        int best;
        int bestOut;
        final String data;
        final int len;
        int pos;

        Input(String data) {
            this.best = -1;
            this.bestOut = -1;
            String s1 = data.toUpperCase(Locale.US);
            this.data = s1;
            this.len = s1.length();
            this.reset();
        }

        boolean check(char var) {
            if(this.get() != var) {
                return false;
            }
            ++this.pos;
            return true;
        }

        boolean check(String var) {
            String data = this.data;
            int pos = this.pos;
            int left = this.len - pos;
            if(left >= 0) {
                int v2 = var.length();
                if(v2 == 0) {
                    return true;
                }
                if(v2 <= left) {
                    for(int j = 0; true; ++j) {
                        if(j >= v2) {
                            this.pos = pos + v2;
                            return true;
                        }
                        if(data.charAt(pos + j) != var.charAt(j)) {
                            break;
                        }
                    }
                }
            }
            return false;
        }

        char get() {
            String data = this.data;
            int pos = this.pos;
            int len = this.len;
            char ret = ArmDis.charAt(data, len, pos);
            if(pos < len && ret <= 0x20) {
                ret = ' ';
                while(pos < len && ArmDis.charAt(data, len, pos + 1) <= 0x20) {
                    ++pos;
                }
                this.pos = pos;
            }
            return ret;
        }

        char getAndNext() {
            char c = this.get();
            ++this.pos;
            return c;
        }

        float getFloat() {
            String data = this.data;
            int pos = this.pos;
            int v1 = data.length();
            if(pos >= v1) {
                throw ArmDis.nextAsm;
            }
            try {
                float f = Float.parseFloat(data.substring(pos, (data.indexOf(59, pos) == -1 ? v1 : data.indexOf(59, pos))));
                this.pos = v1;
                return f;
            }
            catch(NumberFormatException unused_ex) {
                throw ArmDis.nextAsm;
            }
        }

        long getHex() {
            int cur = this.getAndNext();
            if((cur < 0x30 || cur > 57) && (cur < 65 || cur > 70)) {
                throw ArmDis.nextAsm;
            }
            long imm = 0L;
            do {
                imm = (imm << 4) + ((long)(cur < 0x30 || cur > 57 ? cur - 55 : cur - 0x30));
                cur = this.getAndNext();
            }
            while(cur >= 0x30 && cur <= 57 || cur >= 65 && cur <= 70);
            --this.pos;
            return imm;
        }

        int getInt() {
            int cur = this.getAndNext();
            boolean neg = false;
            if(cur == 45) {
                cur = this.getAndNext();
                neg = true;
            }
            if(cur < 0x30 || cur > 57) {
                throw ArmDis.nextAsm;
            }
            int imm = 0;
            do {
                imm = imm * 10 + (cur - 0x30);
                cur = this.getAndNext();
            }
            while(cur >= 0x30 && cur <= 57);
            --this.pos;
            return neg ? -imm : imm;
        }

        int index(String[] vars) {
            String data = this.data;
            int pos = this.pos;
            int left = this.len - pos;
            int ret = -1;
            if(left >= 0) {
                for(int i = vars.length - 1; i >= 0; --i) {
                    String var = vars[i];
                    int v4 = var.length();
                    if(v4 == 0) {
                        ret = i;
                    }
                    else if(v4 <= left) {
                        for(int j = 0; true; ++j) {
                            if(j >= v4) {
                                this.pos = pos + v4;
                                return i;
                            }
                            if(data.charAt(pos + j) != var.charAt(j)) {
                                break;
                            }
                        }
                    }
                }
            }
            return ret;
        }

        void next() {
            ++this.pos;
        }

        void reset() {
            String data = this.data;
            int len = this.len;
            int pos;
            for(pos = 0; pos < len && ArmDis.charAt(data, len, pos) <= 0x20; ++pos) {
            }
            this.pos = pos;
        }
    }

    static class Neon {
        static final byte[] ENC;
        static final int[] NUM;
        static final String[] STR;

        static {
            Neon.NUM = new int[]{0xF2B00840, 0xFFB00850, 0xF2B00000, 0xFFB00810, 0xF3B40C00, 0xFFB70F90, 0xF3B20C00, 0xFFB30F90, 0xF3B10C00, 0xFFB10F90, 0xF3B00800, 0xFFB00C50, 0xF3B00840, 0xFFB00C50, 0xF3B60600, 0xFFBF0FD0, 0xF3B60700, 0xFFBF0FD0, 0xF2000C10, 0xFFA00F10, 0xF2200C10, 0xFFA00F10, 0xF3BA0400, 0xFFBF0C10, 0xF3BB0000, 0xFFBF0C10, 0xF3B00300, 0xFFBF0FD0, 0xF3B00340, 0xFFBF0FD0, 0xF3B00380, 0xFFBF0FD0, 0xF3B003C0, 0xFFBF0FD0, 0xF3B902C0, 0xFFBF0FD0, 0xF3BA0380, 0xFFBF0FD0, 0xF3BA03C0, 0xFFBF0FD0, 0xF2880A10, 0xFEBF0FD0, 0xF2900A10, 0xFEBF0FD0, 0xF2A00A10, 0xFEBF0FD0, 0xF3B00500, 0xFFBF0F90, 0xF3B00580, 0xFFBF0F90, 0xF3B20000, 0xFFBF0F90, 0xF3B20200, 0xFFB30FD0, 0xF3B20240, 0xFFB30FD0, 0xF3B20280, 0xFFB30FD0, 0xF3B202C0, 0xFFB30FD0, 0xF3B20300, 0xFFB30FD0, 0xF3BB0400, 0xFFBF0E90, 0xF3BB0480, 0xFFBF0E90, 0xF3B00000, 0xFFB30F90, 0xF3B00080, 0xFFB30F90, 0xF3B00100, 0xFFB30F90, 0xF3B00400, 0xFFB30F90, 0xF3B00480, 0xFFB30F90, 0xF3B00700, 0xFFB30F90, 0xF3B00780, 0xFFB30F90, 0xF3B20080, 0xFFB30F90, 0xF3B20100, 0xFFB30F90, 0xF3B20180, 0xFFB30F90, 0xF3B10000, 0xFFB30B90, 0xF3B10080, 0xFFB30B90, 0xF3B10100, 0xFFB30B90, 0xF3B10180, 0xFFB30B90, 0xF3B10200, 0xFFB30B90, 0xF3B10300, 0xFFB30B90, -206503040, 0xFFB30B90, 0xF3B00200, 0xFFB30F10, 0xF3B00600, 0xFFB30F10, 0xF3B30600, 0xFFB30E10, 0xF2000C40, 0xFFB00F50, 0xF2100C40, 0xFFB00F50, 0xF2200C40, 0xFFB00F50, 0xF2300C40, 0xFFB00F50, 0xF3000C40, 0xFFB00F50, 0xF3100C40, 0xFFB00F50, 0xF3200C40, 0xFFB00F50, 0xF3000F10, 0xFFA00F10, 0xF3200F10, 0xFFA00F10, 0xF2000110, 0xFFB00F10, 0xF2100110, 0xFFB00F10, 0xF2200110, 0xFFB00F10, 0xF2300110, 0xFFB00F10, 0xF3000110, 0xFFB00F10, 0xF3100110, 0xFFB00F10, 0xF3200110, 0xFFB00F10, 0xF3300110, 0xFFB00F10, 0xF2000D00, 0xFFA00F10, 0xF2000D10, 0xFFA00F10, 0xF2000E00, 0xFFA00F10, 0xF2000F00, 0xFFA00F10, 0xF2000F10, 0xFFA00F10, 0xF2200D00, 0xFFA00F10, 0xF2200D10, 0xFFA00F10, 0xF2200F00, 0xFFA00F10, 0xF2200F10, 0xFFA00F10, 0xF3000D00, 0xFFA00F10, 0xF3000D10, 0xFFA00F10, 0xF3000E00, 0xFFA00F10, 0xF3000E10, 0xFFA00F10, 0xF3000F00, 0xFFA00F10, 0xF3200D00, 0xFFA00F10, 0xF3200E00, 0xFFA00F10, 0xF3200E10, 0xFFA00F10, 0xF3200F00, 0xFFA00F10, 0xF2000800, 0xFF800F10, 0xF2000810, 0xFF800F10, 0xF2000900, 0xFF800F10, 0xF2000B00, 0xFF800F10, 0xF2000B10, 0xFF800F10, 0xF3000800, 0xFF800F10, 0xF3000810, 0xFF800F10, 0xF3000900, 0xFF800F10, 0xF3000B00, 0xFF800F10, 0xF2000000, 0xFE800F10, 0xF2000010, 0xFE800F10, 0xF2000100, 0xFE800F10, 0xF2000200, 0xFE800F10, 0xF2000210, 0xFE800F10, 0xF2000300, 0xFE800F10, 0xF2000310, 0xFE800F10, 0xF2000400, 0xFE800F10, 0xF2000410, 0xFE800F10, 0xF2000500, 0xFE800F10, 0xF2000510, 0xFE800F10, 0xF2000600, 0xFE800F10, 0xF2000610, 0xFE800F10, 0xF2000700, 0xFE800F10, 0xF2000710, 0xFE800F10, 0xF2000910, 0xFE800F10, 0xF2000A00, 0xFE800F10, 0xF2000A10, 0xFE800F10, 0xF2800E10, 0xFEB80FB0, 0xF2800E30, 0xFEB80FB0, 0xF2800F10, 0xFEB80FB0, 0xF2800810, 0xFEB80DB0, 0xF2800830, 0xFEB80DB0, 0xF2800910, 0xFEB80DB0, 0xF2800930, 0xFEB80DB0, 0xF2800C10, 0xFEB80EB0, 0xF2800C30, 0xFEB80EB0, 0xF2800110, 0xFEB809B0, 0xF2800130, 0xFEB809B0, 0xF2800010, 0xFEB808B0, 0xF2800030, 0xFEB808B0, 0xF2880810, 0xFFB80FD0, -225966000, 0xFFB80FD0, 0xF3880810, 0xFFB80FD0, 0xF3880850, 0xFFB80FD0, 0xF2880910, 0xFEB80FD0, 0xF2880950, 0xFEB80FD0, 0xF2880A10, 0xFEB80FD0, 0xF2900810, 0xFFB00FD0, 0xF2900850, 0xFFB00FD0, 0xF2880510, 0xFFB80F90, 0xF3880410, 0xFFB80F90, 0xF3880510, 0xFFB80F90, 0xF3880610, 0xFFB80F90, 0xF3900810, 0xFFB00FD0, 0xF3900850, 0xFFB00FD0, 0xF2900910, 0xFEB00FD0, 0xF2900950, 0xFEB00FD0, 0xF2900A10, 0xFEB00FD0, 0xF2880010, 0xFEB80F90, 0xF2880110, 0xFEB80F90, 0xF2880210, 0xFEB80F90, 0xF2880310, 0xFEB80F90, 0xF2880710, 0xFEB80F90, 0xF2A00810, 0xFFA00FD0, 0xF2A00850, 0xFFA00FD0, 0xF2900510, 0xFFB00F90, 0xF3900410, 0xFFB00F90, 0xF3900510, 0xFFB00F90, 0xF3900610, 0xFFB00F90, 0xF2A00A10, 0xFEA00FD0, 0xF2900010, 0xFEB00F90, 0xF2900110, 0xFEB00F90, 0xF2900210, 0xFEB00F90, 0xF2900310, 0xFEB00F90, 0xF2900710, 0xFEB00F90, 0xF3A00810, 0xFFA00FD0, 0xF3A00850, 0xFFA00FD0, 0xF2A00910, 0xFEA00FD0, 0xF2A00950, 0xFEA00FD0, 0xF2A00510, 0xFFA00F90, 0xF3A00410, 0xFFA00F90, 0xF3A00510, 0xFFA00F90, 0xF3A00610, 0xFFA00F90, 0xF2A00010, 0xFEA00F90, 0xF2A00110, 0xFEA00F90, 0xF2A00210, 0xFEA00F90, 0xF2A00310, 0xFEA00F90, 0xF2A00710, 0xFEA00F90, 0xF2800590, 0xFF800F90, 0xF3800490, 0xFF800F90, 0xF3800590, 0xFF800F90, 0xF3800690, 0xFF800F90, 0xF2800090, 0xFE800F90, 0xF2800190, 0xFE800F90, 0xF2800290, 0xFE800F90, 0xF2800390, 0xFE800F90, 0xF2800790, 0xFE800F90, 0xF2A00E10, 0xFEA00E90, 0xF2A00E00, 0xFEB00F50, 0xF2800E00, 0xFEA00F50, 0xF2800400, 0xFF800F50, 0xF2800600, 0xFF800F50, 0xF2800900, 0xFF800F50, 0xF2800B00, 0xFF800F50, 0xF2800D00, 0xFF800F50, 0xF3800400, 0xFF800F50, 0xF3800600, 0xFF800F50, 0xF2800000, 0xFE800F50, 0xF2800100, 0xFE800F50, 0xF2800200, 0xFE800F50, 0xF2800300, 0xFE800F50, 0xF2800500, 0xFE800F50, 0xF2800700, 0xFE800F50, 0xF2800800, 0xFE800F50, 0xF2800A00, 0xFE800F50, 0xF2800C00, 0xFE800F50, 0xF2800040, 0xFF800F50, 0xF2800140, 0xFF800F50, 0xF2800340, 0xFF800F50, 0xF2800440, 0xFF800F50, 0xF2800540, 0xFF800F50, 0xF2800740, 0xFF800F50, 0xF2800840, 0xFF800F50, 0xF2800940, 0xFF800F50, 0xF2800B40, 0xFF800F50, 0xF2800C40, 0xFF800F50, 0xF2800D40, 0xFF800F50, 0xF3800040, 0xFF800F50, 0xF3800140, 0xFF800F50, 0xF3800440, 0xFF800F50, 0xF3800540, 0xFF800F50, 0xF3800840, 0xFF800F50, 0xF3800940, 0xFF800F50, 0xF3800C40, 0xFF800F50, 0xF3800D40, 0xFF800F50, 0xF2800240, 0xFE800F50, 0xF2800640, 0xFE800F50, 0xF2800A40, 0xFE800F50, 0xF4A00FC0, 0xFFB00FC0, 0xF4A00C00, 0xFFB00F00, 0xF4A00D00, 0xFFB00F00, 0xF4A00E00, 0xFFB00F00, 0xF4A00F00, 0xFFB00F00, 0xF4000200, 0xFF900F00, 0xF4000300, 0xFF900F00, 0xF4000400, 0xFF900F00, 0xF4000500, 0xFF900F00, 0xF4000600, 0xFF900F00, 0xF4000700, 0xFF900F00, 0xF4000800, 0xFF900F00, 0xF4000900, 0xFF900F00, 0xF4000A00, 0xFF900F00, 0xF4000000, 0xFF900E00, 0xF4800000, 0xFF900300, 0xF4800100, 0xFF900300, 0xF4800200, 0xFF900300, 0xF4800300, 0xFF900300};
            Neon.STR = new String[]{"VEXT%c.8\t %12-15,22R, %16-19,7R, %0-3,5R, #%8-11d", "VEXT%c.8\t %12-15,22R, %16-19,7R, %0-3,5R, #%8-11d", "VDUP%c.32\t %12-15,22R, %0-3,5D[%19d]", "VDUP%c.16\t %12-15,22R, %0-3,5D[%18-19d]", "VDUP%c.8\t %12-15,22R, %0-3,5D[%17-19d]", "VTBL%c.8\t %12-15,22D, %F, %0-3,5D", "VTBX%c.8\t %12-15,22D, %F, %0-3,5D", "VCVT%c.F16.F32\t %12-15,22D, %0-3,5Q", "VCVT%c.F32.F16\t %12-15,22Q, %0-3,5D", "VFMA%c.F%20U0\t %12-15,22R, %16-19,7R, %0-3,5R", "VFMS%c.F%20U0\t %12-15,22R, %16-19,7R, %0-3,5R", "VRINT%7-9?P?M?ZAXN%u.F32\t %12-15,22R, %0-3,5R", "VCVT%8-9?MPNA%u.%7?US32.F32\t %12-15,22R, %0-3,5R", "AESE%u.8\t %12-15,22Q, %0-3,5Q", "AESD%u.8\t %12-15,22Q, %0-3,5Q", "AESMC%u.8\t %12-15,22Q, %0-3,5Q", "AESIMC%u.8\t %12-15,22Q, %0-3,5Q", "SHA1H%u.32\t %12-15,22Q, %0-3,5Q", "SHA1SU1%u.32\t %12-15,22Q, %0-3,5Q", "SHA256SU0%u.32\t %12-15,22Q, %0-3,5Q", "VMOVL%c.%24?US8\t %12-15,22Q, %0-3,5D", "VMOVL%c.%24?US16\t %12-15,22Q, %0-3,5D", "VMOVL%c.%24?US32\t %12-15,22Q, %0-3,5D", "VCNT%c.8\t %12-15,22R, %0-3,5R", "VMVN%c\t %12-15,22R, %0-3,5R", "VSWP%c\t %12-15,22R, %0-3,5R", "VMOVN%c.i%18-19T2\t %12-15,22D, %0-3,5Q", "VQMOVUN%c.s%18-19T2\t %12-15,22D, %0-3,5Q", "VQMOVN%c.s%18-19T2\t %12-15,22D, %0-3,5Q", "VQMOVN%c.u%18-19T2\t %12-15,22D, %0-3,5Q", "VSHLL%c.i%18-19S2\t %12-15,22Q, %0-3,5D, #%18-19S2", "VRECPE%c.%8?FU%18-19S2\t %12-15,22R, %0-3,5R", "VRSQRTE%c.%8?FU%18-19S2\t %12-15,22R, %0-3,5R", "VREV64%c.%18-19S2\t %12-15,22R, %0-3,5R", "VREV32%c.%18-19S2\t %12-15,22R, %0-3,5R", "VREV16%c.%18-19S2\t %12-15,22R, %0-3,5R", "VCLS%c.s%18-19S2\t %12-15,22R, %0-3,5R", "VCLZ%c.i%18-19S2\t %12-15,22R, %0-3,5R", "VQABS%c.s%18-19S2\t %12-15,22R, %0-3,5R", "VQNEG%c.s%18-19S2\t %12-15,22R, %0-3,5R", "VTRN%c.%18-19S2\t %12-15,22R, %0-3,5R", "VUZP%c.%18-19S2\t %12-15,22R, %0-3,5R", "VZIP%c.%18-19S2\t %12-15,22R, %0-3,5R", "VCGT%c.%10?FS%18-19S2\t %12-15,22R, %0-3,5R, #0", "VCGE%c.%10?FS%18-19S2\t %12-15,22R, %0-3,5R, #0", "VCEQ%c.%10?FI%18-19S2\t %12-15,22R, %0-3,5R, #0", "VCLE%c.%10?FS%18-19S2\t %12-15,22R, %0-3,5R, #0", "VCLT%c.%10?FS%18-19S2\t %12-15,22R, %0-3,5R, #0", "VABS%c.%10?FS%18-19S2\t %12-15,22R, %0-3,5R", "VNEG%c.%10?FS%18-19S2\t %12-15,22R, %0-3,5R", "VPADDL%c.%7?US%18-19S2\t %12-15,22R, %0-3,5R", "VPADAL%c.%7?US%18-19S2\t %12-15,22R, %0-3,5R", "VCVT%c.%7-8?USFF%18-19Sa.%7-8?FFUS%18-19Sa\t %12-15,22R, %0-3,5R", "SHA1C%u.32\t %12-15,22Q, %16-19,7Q, %0-3,5Q", "SHA1P%u.32\t %12-15,22Q, %16-19,7Q, %0-3,5Q", "SHA1M%u.32\t %12-15,22Q, %16-19,7Q, %0-3,5Q", "SHA1SU0%u.32\t %12-15,22Q, %16-19,7Q, %0-3,5Q", "SHA256H%u.32\t %12-15,22Q, %16-19,7Q, %0-3,5Q", "SHA256H2%u.32\t %12-15,22Q, %16-19,7Q, %0-3,5Q", "SHA256SU1%u.32\t %12-15,22Q, %16-19,7Q, %0-3,5Q", "VMAXNM%u.F%20U0\t %12-15,22R, %16-19,7R, %0-3,5R", "VMINNM%u.F%20U0\t %12-15,22R, %16-19,7R, %0-3,5R", "VAND%c\t %12-15,22R, %16-19,7R, %0-3,5R", "VBIC%c\t %12-15,22R, %16-19,7R, %0-3,5R", "VORR%c\t %12-15,22R, %16-19,7R, %0-3,5R", "VORN%c\t %12-15,22R, %16-19,7R, %0-3,5R", "VEOR%c\t %12-15,22R, %16-19,7R, %0-3,5R", "VBSL%c\t %12-15,22R, %16-19,7R, %0-3,5R", "VBIT%c\t %12-15,22R, %16-19,7R, %0-3,5R", "VBIF%c\t %12-15,22R, %16-19,7R, %0-3,5R", "VADD%c.F%20U0\t %12-15,22R, %16-19,7R, %0-3,5R", "VMLA%c.F%20U0\t %12-15,22R, %16-19,7R, %0-3,5R", "VCEQ%c.F%20U0\t %12-15,22R, %16-19,7R, %0-3,5R", "VMAX%c.F%20U0\t %12-15,22R, %16-19,7R, %0-3,5R", "VRECPS%c.F%20U0\t %12-15,22R, %16-19,7R, %0-3,5R", "VSUB%c.F%20U0\t %12-15,22R, %16-19,7R, %0-3,5R", "VMLS%c.F%20U0\t %12-15,22R, %16-19,7R, %0-3,5R", "VMIN%c.F%20U0\t %12-15,22R, %16-19,7R, %0-3,5R", "VRSQRTS%c.F%20U0\t %12-15,22R, %16-19,7R, %0-3,5R", "VPADD%c.F%20U0\t %12-15,22R, %16-19,7R, %0-3,5R", "VMUL%c.F%20U0\t %12-15,22R, %16-19,7R, %0-3,5R", "VCGE%c.F%20U0\t %12-15,22R, %16-19,7R, %0-3,5R", "VACGE%c.F%20U0\t %12-15,22R, %16-19,7R, %0-3,5R", "VPMAX%c.F%20U0\t %12-15,22R, %16-19,7R, %0-3,5R", "VABD%c.F%20U0\t %12-15,22R, %16-19,7R, %0-3,5R", "VCGT%c.F%20U0\t %12-15,22R, %16-19,7R, %0-3,5R", "VACGT%c.F%20U0\t %12-15,22R, %16-19,7R, %0-3,5R", "VPMIN%c.F%20U0\t %12-15,22R, %16-19,7R, %0-3,5R", "VADD%c.i%20-21S3\t %12-15,22R, %16-19,7R, %0-3,5R", "VTST%c.%20-21S2\t %12-15,22R, %16-19,7R, %0-3,5R", "VMLA%c.i%20-21S2\t %12-15,22R, %16-19,7R, %0-3,5R", "VQDMULH%c.s%20-21S6\t %12-15,22R, %16-19,7R, %0-3,5R", "VPADD%c.i%20-21S2\t %12-15,22R, %16-19,7R, %0-3,5R", "VSUB%c.i%20-21S3\t %12-15,22R, %16-19,7R, %0-3,5R", "VCEQ%c.i%20-21S2\t %12-15,22R, %16-19,7R, %0-3,5R", "VMLS%c.i%20-21S2\t %12-15,22R, %16-19,7R, %0-3,5R", "VQRDMULH%c.s%20-21S6\t %12-15,22R, %16-19,7R, %0-3,5R", "VHADD%c.%24?US%20-21S2\t %12-15,22R, %16-19,7R, %0-3,5R", "VQADD%c.%24?US%20-21S3\t %12-15,22R, %16-19,7R, %0-3,5R", "VRHADD%c.%24?US%20-21S2\t %12-15,22R, %16-19,7R, %0-3,5R", "VHSUB%c.%24?US%20-21S2\t %12-15,22R, %16-19,7R, %0-3,5R", "VQSUB%c.%24?US%20-21S3\t %12-15,22R, %16-19,7R, %0-3,5R", "VCGT%c.%24?US%20-21S2\t %12-15,22R, %16-19,7R, %0-3,5R", "VCGE%c.%24?US%20-21S2\t %12-15,22R, %16-19,7R, %0-3,5R", "VSHL%c.%24?US%20-21S3\t %12-15,22R, %0-3,5R, %16-19,7R", "VQSHL%c.%24?US%20-21S3\t %12-15,22R, %0-3,5R, %16-19,7R", "VRSHL%c.%24?US%20-21S3\t %12-15,22R, %0-3,5R, %16-19,7R", "VQRSHL%c.%24?US%20-21S3\t %12-15,22R, %0-3,5R, %16-19,7R", "VMAX%c.%24?US%20-21S2\t %12-15,22R, %16-19,7R, %0-3,5R", "VMIN%c.%24?US%20-21S2\t %12-15,22R, %16-19,7R, %0-3,5R", "VABD%c.%24?US%20-21S2\t %12-15,22R, %16-19,7R, %0-3,5R", "VABA%c.%24?US%20-21S2\t %12-15,22R, %16-19,7R, %0-3,5R", "VMUL%c.%24?PI%20-21S2\t %12-15,22R, %16-19,7R, %0-3,5R", "VPMAX%c.%24?US%20-21S2\t %12-15,22R, %16-19,7R, %0-3,5R", "VPMIN%c.%24?US%20-21S2\t %12-15,22R, %16-19,7R, %0-3,5R", "VMOV%c.i8\t %12-15,22R, %E", "VMOV%c.i64\t %12-15,22R, %E", "VMOV%c.F32\t %12-15,22R, %E", "VMOV%c.i16\t %12-15,22R, %E", "VMVN%c.i16\t %12-15,22R, %E", "VORR%c.i16\t %12-15,22R, %E", "VBIC%c.i16\t %12-15,22R, %E", "VMOV%c.i32\t %12-15,22R, %E", "VMVN%c.i32\t %12-15,22R, %E", "VORR%c.i32\t %12-15,22R, %E", "VBIC%c.i32\t %12-15,22R, %E", "VMOV%c.i32\t %12-15,22R, %E", "VMVN%c.i32\t %12-15,22R, %E", "VSHRN%c.i16\t %12-15,22D, %0-3,5Q, #%16-18e", "VRSHRN%c.i16\t %12-15,22D, %0-3,5Q, #%16-18e", "VQSHRUN%c.s16\t %12-15,22D, %0-3,5Q, #%16-18e", "VQRSHRUN%c.s16\t %12-15,22D, %0-3,5Q, #%16-18e", "VQSHRN%c.%24?US16\t %12-15,22D, %0-3,5Q, #%16-18e", "VQRSHRN%c.%24?US16\t %12-15,22D, %0-3,5Q, #%16-18e", "VSHLL%c.%24?US8\t %12-15,22Q, %0-3,5D, #%16-18d", "VSHRN%c.i32\t %12-15,22D, %0-3,5Q, #%16-19e", "VRSHRN%c.i32\t %12-15,22D, %0-3,5Q, #%16-19e", "VSHL%c.%24?US8\t %12-15,22R, %0-3,5R, #%16-18d", "VSRI%c.8\t %12-15,22R, %0-3,5R, #%16-18e", "VSLI%c.8\t %12-15,22R, %0-3,5R, #%16-18d", "VQSHLU%c.s8\t %12-15,22R, %0-3,5R, #%16-18d", "VQSHRUN%c.s32\t %12-15,22D, %0-3,5Q, #%16-19e", "VQRSHRUN%c.s32\t %12-15,22D, %0-3,5Q, #%16-19e", "VQSHRN%c.%24?US32\t %12-15,22D, %0-3,5Q, #%16-19e", "VQRSHRN%c.%24?US32\t %12-15,22D, %0-3,5Q, #%16-19e", "VSHLL%c.%24?US16\t %12-15,22Q, %0-3,5D, #%16-19d", "VSHR%c.%24?US8\t %12-15,22R, %0-3,5R, #%16-18e", "VSRA%c.%24?US8\t %12-15,22R, %0-3,5R, #%16-18e", "VRSHR%c.%24?US8\t %12-15,22R, %0-3,5R, #%16-18e", "VRSRA%c.%24?US8\t %12-15,22R, %0-3,5R, #%16-18e", "VQSHL%c.%24?US8\t %12-15,22R, %0-3,5R, #%16-18d", "VSHRN%c.i64\t %12-15,22D, %0-3,5Q, #%16-20e", "VRSHRN%c.i64\t %12-15,22D, %0-3,5Q, #%16-20e", "VSHL%c.%24?US16\t %12-15,22R, %0-3,5R, #%16-19d", "VSRI%c.16\t %12-15,22R, %0-3,5R, #%16-19e", "VSLI%c.16\t %12-15,22R, %0-3,5R, #%16-19d", "VQSHLU%c.s16\t %12-15,22R, %0-3,5R, #%16-19d", "VSHLL%c.%24?US32\t %12-15,22Q, %0-3,5D, #%16-20d", "VSHR%c.%24?US16\t %12-15,22R, %0-3,5R, #%16-19e", "VSRA%c.%24?US16\t %12-15,22R, %0-3,5R, #%16-19e", "VRSHR%c.%24?US16\t %12-15,22R, %0-3,5R, #%16-19e", "VRSRA%c.%24?US16\t %12-15,22R, %0-3,5R, #%16-19e", "VQSHL%c.%24?US16\t %12-15,22R, %0-3,5R, #%16-19d", "VQSHRUN%c.s64\t %12-15,22D, %0-3,5Q, #%16-20e", "VQRSHRUN%c.s64\t %12-15,22D, %0-3,5Q, #%16-20e", "VQSHRN%c.%24?US64\t %12-15,22D, %0-3,5Q, #%16-20e", "VQRSHRN%c.%24?US64\t %12-15,22D, %0-3,5Q, #%16-20e", "VSHL%c.%24?US32\t %12-15,22R, %0-3,5R, #%16-20d", "VSRI%c.32\t %12-15,22R, %0-3,5R, #%16-20e", "VSLI%c.32\t %12-15,22R, %0-3,5R, #%16-20d", "VQSHLU%c.s32\t %12-15,22R, %0-3,5R, #%16-20d", "VSHR%c.%24?US32\t %12-15,22R, %0-3,5R, #%16-20e", "VSRA%c.%24?US32\t %12-15,22R, %0-3,5R, #%16-20e", "VRSHR%c.%24?US32\t %12-15,22R, %0-3,5R, #%16-20e", "VRSRA%c.%24?US32\t %12-15,22R, %0-3,5R, #%16-20e", "VQSHL%c.%24?US32\t %12-15,22R, %0-3,5R, #%16-20d", "VSHL%c.%24?US64\t %12-15,22R, %0-3,5R, #%16-21d", "VSRI%c.64\t %12-15,22R, %0-3,5R, #%16-21e", "VSLI%c.64\t %12-15,22R, %0-3,5R, #%16-21d", "VQSHLU%c.s64\t %12-15,22R, %0-3,5R, #%16-21d", "VSHR%c.%24?US64\t %12-15,22R, %0-3,5R, #%16-21e", "VSRA%c.%24?US64\t %12-15,22R, %0-3,5R, #%16-21e", "VRSHR%c.%24?US64\t %12-15,22R, %0-3,5R, #%16-21e", "VRSRA%c.%24?US64\t %12-15,22R, %0-3,5R, #%16-21e", "VQSHL%c.%24?US64\t %12-15,22R, %0-3,5R, #%16-21d", "VCVT%c.%24,8?USFF32.%24,8?FFUS32\t %12-15,22R, %0-3,5R, #%16-20e", "VMULL%c.p64\t %12-15,22Q, %16-19,7D, %0-3,5D", "VMULL%c.p%20S0\t %12-15,22Q, %16-19,7D, %0-3,5D", "VADDHN%c.i%20-21T2\t %12-15,22D, %16-19,7Q, %0-3,5Q", "VSUBHN%c.i%20-21T2\t %12-15,22D, %16-19,7Q, %0-3,5Q", "VQDMLAL%c.s%20-21S6\t %12-15,22Q, %16-19,7D, %0-3,5D", "VQDMLSL%c.s%20-21S6\t %12-15,22Q, %16-19,7D, %0-3,5D", "VQDMULL%c.s%20-21S6\t %12-15,22Q, %16-19,7D, %0-3,5D", "VRADDHN%c.i%20-21T2\t %12-15,22D, %16-19,7Q, %0-3,5Q", "VRSUBHN%c.i%20-21T2\t %12-15,22D, %16-19,7Q, %0-3,5Q", "VADDL%c.%24?US%20-21S2\t %12-15,22Q, %16-19,7D, %0-3,5D", "VADDW%c.%24?US%20-21S2\t %12-15,22Q, %16-19,7Q, %0-3,5D", "VSUBL%c.%24?US%20-21S2\t %12-15,22Q, %16-19,7D, %0-3,5D", "VSUBW%c.%24?US%20-21S2\t %12-15,22Q, %16-19,7Q, %0-3,5D", "VABAL%c.%24?US%20-21S2\t %12-15,22Q, %16-19,7D, %0-3,5D", "VABDL%c.%24?US%20-21S2\t %12-15,22Q, %16-19,7D, %0-3,5D", "VMLAL%c.%24?US%20-21S2\t %12-15,22Q, %16-19,7D, %0-3,5D", "VMLSL%c.%24?US%20-21S2\t %12-15,22Q, %16-19,7D, %0-3,5D", "VMULL%c.%24?US%20-21S2\t %12-15,22Q, %16-19,7D, %0-3,5D", "VMLA%c.i%20-21S6\t %12-15,22D, %16-19,7D, %D", "VMLA%c.F%20-21Sa\t %12-15,22D, %16-19,7D, %D", "VQDMLAL%c.s%20-21S6\t %12-15,22Q, %16-19,7D, %D", "VMLS%c.i%20-21S6\t %12-15,22D, %16-19,7D, %D", "VMLS%c.F%20-21S6\t %12-15,22D, %16-19,7D, %D", "VQDMLSL%c.s%20-21S6\t %12-15,22Q, %16-19,7D, %D", "VMUL%c.i%20-21S6\t %12-15,22D, %16-19,7D, %D", "VMUL%c.F%20-21Sa\t %12-15,22D, %16-19,7D, %D", "VQDMULL%c.s%20-21S6\t %12-15,22Q, %16-19,7D, %D", "VQDMULH%c.s%20-21S6\t %12-15,22D, %16-19,7D, %D", "VQRDMULH%c.s%20-21S6\t %12-15,22D, %16-19,7D, %D", "VMLA%c.i%20-21S6\t %12-15,22Q, %16-19,7Q, %D", "VMLA%c.F%20-21Sa\t %12-15,22Q, %16-19,7Q, %D", "VMLS%c.i%20-21S6\t %12-15,22Q, %16-19,7Q, %D", "VMLS%c.F%20-21Sa\t %12-15,22Q, %16-19,7Q, %D", "VMUL%c.i%20-21S6\t %12-15,22Q, %16-19,7Q, %D", "VMUL%c.F%20-21Sa\t %12-15,22Q, %16-19,7Q, %D", "VQDMULH%c.s%20-21S6\t %12-15,22Q, %16-19,7Q, %D", "VQRDMULH%c.s%20-21S6\t %12-15,22Q, %16-19,7Q, %D", "VMLAL%c.%24?US%20-21S6\t %12-15,22Q, %16-19,7D, %D", "VMLSL%c.%24?US%20-21S6\t %12-15,22Q, %16-19,7D, %D", "VMULL%c.%24?US%20-21S6\t %12-15,22Q, %16-19,7D, %D", "VLD4%c.32\t %C", "VLD1%c.%6-7S2\t %C", "VLD2%c.%6-7S2\t %C", "VLD3%c.%6-7S2\t %C", "VLD4%c.%6-7S2\t %C", "V%21?LS%21?DT1%c.%6-7S3\t %A", "V%21?LS%21?DT2%c.%6-7S2\t %A", "V%21?LS%21?DT3%c.%6-7S2\t %A", "V%21?LS%21?DT3%c.%6-7S2\t %A", "V%21?LS%21?DT1%c.%6-7S3\t %A", "V%21?LS%21?DT1%c.%6-7S3\t %A", "V%21?LS%21?DT2%c.%6-7S2\t %A", "V%21?LS%21?DT2%c.%6-7S2\t %A", "V%21?LS%21?DT1%c.%6-7S3\t %A", "V%21?LS%21?DT4%c.%6-7S2\t %A", "V%21?LS%21?DT1%c.%10-11S2\t %B", "V%21?LS%21?DT2%c.%10-11S2\t %B", "V%21?LS%21?DT3%c.%10-11S2\t %B", "V%21?LS%21?DT4%c.%10-11S2\t %B"};
            Neon.ENC = new byte[]{4, 20, 4, 4, 3, 19, 3, 1, 2, 18, 2, 0, 0, 0, 0, 0};
        }

        static int getOpcode(ArmDis ret, long addr, String data, boolean thumb, int dbg) {
            int bits;
            int rm;
            int rm;
            int rm;
            int limit;
            int out;
            Input in = ret.input;
            int[] NUM = Neon.NUM;
            String[] STR = Neon.STR;
            int i = 0;
        alab1:
            while(true) {
                if(i >= STR.length) {
                    return -1;
                }
                out = NUM[i * 2];
                in.reset();
                try {
                    String asm = STR[i];
                    int mask = NUM[i * 2 + 1];
                    if(mask != 0) {
                        int fail = -1;
                        int j = 0;
                        int v7 = asm.length();
                        while(j < v7) {
                            int c = ArmDis.charAt(asm, v7, j);
                            if(c <= 0x20) {
                                c = 0x20;
                                while(j < v7 && ArmDis.charAt(asm, v7, j + 1) <= 0x20) {
                                    ++j;
                                }
                            }
                            if(c == 37) {
                            label_27:
                                ++j;
                                int c = ArmDis.charAt(asm, v7, j);
                            alab2:
                                switch(c) {
                                    case 37: {
                                        if(!in.check('%')) {
                                            break alab1;
                                        }
                                        break;
                                    }
                                    case 0x30: 
                                    case 49: 
                                    case 50: 
                                    case 51: 
                                    case 52: 
                                    case 53: 
                                    case 54: 
                                    case 55: 
                                    case 56: 
                                    case 57: {
                                        int value = -1;
                                        int j_ = j;
                                        ret.valuep = out;
                                        j = ArmDis.decodeBitfield(asm, v7, j, ret, -1);
                                        int v12 = ret.widthp;
                                        int c = ArmDis.charAt(asm, v7, j);
                                        switch(c) {
                                            case 0x3F: {
                                                int v15 = in.getAndNext();
                                                int k = 0;
                                                int sk = 1 << v12;
                                                while(k < sk) {
                                                    if(ArmDis.charAt(asm, v7, j + sk - k) == v15) {
                                                        value = k;
                                                        if(true) {
                                                            break;
                                                        }
                                                    }
                                                    else {
                                                        ++k;
                                                    }
                                                }
                                                if(value == -1) {
                                                    break alab1;
                                                }
                                                else {
                                                    if(v15 != 70 || v12 != 2 || value != 0 && value != 2 || ArmDis.charAt(asm, v7, j - value + 3) != 70) {
                                                        ret.valuep = out;
                                                        ArmDis.decodeBitfield(asm, v7, j_, ret, value);
                                                        out = ret.valuep;
                                                    }
                                                    j += 1 << v12;
                                                    break alab2;
                                                }
                                                goto label_73;
                                            }
                                            case 68: {
                                            label_73:
                                                if(in.check('D')) {
                                                    int value = in.getInt();
                                                    ret.valuep = out;
                                                    ArmDis.decodeBitfield(asm, v7, j_, ret, value);
                                                    out = ret.valuep;
                                                    if((~((1 << v12) - 1) & value) != 0) {
                                                        fail = j;
                                                    }
                                                }
                                                else {
                                                    break alab1;
                                                }
                                                break alab2;
                                            }
                                            case 81: {
                                            label_90:
                                                if(in.check('Q')) {
                                                    int v20 = in.getInt();
                                                    ret.valuep = out;
                                                    ArmDis.decodeBitfield(asm, v7, j_, ret, v20 << 1);
                                                    out = ret.valuep;
                                                    if((~((1 << v12) - 1) & v20 << 1) != 0) {
                                                        fail = j;
                                                    }
                                                }
                                                else {
                                                    break alab1;
                                                }
                                                break alab2;
                                            }
                                            case 82: {
                                                if((out & 0x40) != 0 || !in.check('D')) {
                                                    out |= 0x40;
                                                    goto label_90;
                                                }
                                                else {
                                                    int value = in.getInt();
                                                    ret.valuep = out;
                                                    ArmDis.decodeBitfield(asm, v7, j_, ret, value);
                                                    out = ret.valuep;
                                                    if((~((1 << v12) - 1) & value) != 0) {
                                                        fail = j;
                                                    }
                                                }
                                                break alab2;
                                            }
                                            case 83: 
                                            case 84: 
                                            case 85: {
                                                ++j;
                                                int c = ArmDis.charAt(asm, v7, j);
                                                if(c >= 0x30 && c <= 57) {
                                                    limit = c - 0x30;
                                                }
                                                else {
                                                    if(c < 97 || c > 102) {
                                                        throw new RuntimeException("41 Invalid format char \'" + ((char)c) + "\'; " + i + "; " + j + "; \'" + asm + "\'");
                                                    }
                                                    limit = c - 87;
                                                }
                                                int value = in.getInt();
                                                int value = Integer.numberOfTrailingZeros(value) - (c - 80);
                                                ret.valuep = out;
                                                ArmDis.decodeBitfield(asm, v7, j_, ret, value);
                                                out = ret.valuep;
                                                if(value != 1 << Integer.numberOfTrailingZeros(value)) {
                                                    fail = j;
                                                }
                                                if(value < limit >> 2 || value > (limit & 3)) {
                                                    fail = j;
                                                }
                                                if((~((1 << v12) - 1) & value) != 0) {
                                                    fail = j;
                                                }
                                                break alab2;
                                            }
                                            case 39: 
                                            case 0x60: {
                                                ++j;
                                                int c = ArmDis.charAt(asm, v7, j);
                                                if(c != 69) {
                                                    if(c == 76) {
                                                        if(in.check("LEQ")) {
                                                            in.pos -= 3;
                                                        }
                                                        else if(in.check("LS") || in.check("LT") || in.check("LE")) {
                                                            in.pos -= 2;
                                                            break alab2;
                                                        }
                                                    }
                                                }
                                                else if(in.check("EQ")) {
                                                    in.pos -= 2;
                                                    break alab2;
                                                }
                                                if(in.check(((char)c)) == (c == 39)) {
                                                    out = ret.valuep;
                                                }
                                                break alab2;
                                            }
                                            case 100: {
                                                int value = in.getInt();
                                                ret.valuep = out;
                                                ArmDis.decodeBitfield(asm, v7, j_, ret, value);
                                                out = ret.valuep;
                                                if((~((1 << v12) - 1) & value) != 0) {
                                                    fail = j;
                                                }
                                                break alab2;
                                            }
                                            case 101: {
                                                int value = (1 << v12) - in.getInt();
                                                ret.valuep = out;
                                                ArmDis.decodeBitfield(asm, v7, j_, ret, value);
                                                out = ret.valuep;
                                                if((~((1 << v12) - 1) & value) != 0) {
                                                    fail = j;
                                                }
                                                break alab2;
                                            }
                                            case 0x72: {
                                                int value = in.index(ArmDis.REG);
                                                if(value == -1) {
                                                    break alab1;
                                                }
                                                else {
                                                    ret.valuep = out;
                                                    ArmDis.decodeBitfield(asm, v7, j_, ret, value);
                                                    out = ret.valuep;
                                                    break alab2;
                                                }
                                                goto label_138;
                                            }
                                            default: {
                                                throw new RuntimeException("50 Invalid format char \'" + ((char)c) + "\'; " + i + "; " + j + "; \'" + asm + "\'");
                                            }
                                        }
                                    }
                                    case 65: {
                                    label_138:
                                        if(in.check('{')) {
                                            int type = out >> 8 & 15;
                                            int n = Neon.ENC[type] & 15;
                                            int stride = (Neon.ENC[type] >> 4) + 1;
                                            if(in.check('D')) {
                                                int v31 = in.getInt();
                                                out |= (v31 & 15) << 12 | (v31 >> 4 & 1) << 22;
                                                if((v31 & 0xFFFFFFE0) != 0) {
                                                    fail = j;
                                                }
                                                if(in.check(",D")) {
                                                    if(stride <= 1) {
                                                        if((mask >> 8 & 15) == 14) {
                                                            out |= 0x100;
                                                            n = Neon.ENC[type + 1] & 15;
                                                            stride = (Neon.ENC[type + 1] >> 4) + 1;
                                                        }
                                                        else {
                                                            fail = j;
                                                        }
                                                    }
                                                    for(int ix = 1; ix != n; ++ix) {
                                                        if(ix != 1 && !in.check(",D")) {
                                                            break alab1;
                                                        }
                                                        if(in.getInt() != ix * stride + v31) {
                                                            fail = j;
                                                        }
                                                    }
                                                }
                                                else if(!in.check("-D")) {
                                                    if(n != 1) {
                                                        fail = j;
                                                    }
                                                }
                                                else if(in.getInt() != v31 + n - 1) {
                                                    fail = j;
                                                }
                                                if(in.check("}, [")) {
                                                    int v33 = in.index(ArmDis.REG);
                                                    if(v33 == -1) {
                                                        break alab1;
                                                    }
                                                    else {
                                                        out |= v33 << 16;
                                                        if(in.check(" :")) {
                                                            int v34 = in.getInt();
                                                            if(Integer.bitCount(v34) != 1) {
                                                                fail = j;
                                                            }
                                                            int v35 = Integer.numberOfTrailingZeros(v34);
                                                            out |= (v35 - 5 & 3) << 4;
                                                            if((v35 - 5 & -4) != 0) {
                                                                fail = j;
                                                            }
                                                        }
                                                        if(in.check(']')) {
                                                            if(in.check('!')) {
                                                                rm = 13;
                                                            }
                                                            else if(in.check(", ")) {
                                                                int v37 = in.index(ArmDis.REG);
                                                                if(v37 == -1) {
                                                                    break alab1;
                                                                }
                                                                else {
                                                                    rm = v37;
                                                                }
                                                            }
                                                            else {
                                                                rm = 15;
                                                            }
                                                            out |= rm;
                                                            break;
                                                        }
                                                        else {
                                                            break alab1;
                                                        }
                                                    }
                                                }
                                                else {
                                                    break alab1;
                                                }
                                            }
                                            else {
                                                break alab1;
                                            }
                                        }
                                        else {
                                            break alab1;
                                        }
                                        goto label_193;
                                    }
                                    case 66: {
                                    label_193:
                                        if(in.check('{')) {
                                            int length = (out >> 8 & 3) + 1;
                                            if(in.check('D')) {
                                                int v39 = in.getInt();
                                                out |= (v39 & 15) << 12 | (v39 >> 4 & 1) << 22;
                                                if((v39 & 0xFFFFFFE0) != 0) {
                                                    fail = j;
                                                }
                                                if(in.check('[')) {
                                                    int v40 = in.getInt();
                                                    if((v40 & -16) != 0) {
                                                        fail = j;
                                                    }
                                                    if(in.check(']')) {
                                                        int size = out >> 10 & 3;
                                                        int idx_align = 0;
                                                        int stride = 1;
                                                        for(int ix = 1; true; ++ix) {
                                                            if(ix >= length) {
                                                                if(stride != 1 && length <= 1) {
                                                                    fail = j;
                                                                }
                                                                if(in.check("}, [")) {
                                                                    int v45 = in.index(ArmDis.REG);
                                                                    if(v45 == -1) {
                                                                        break alab1;
                                                                    }
                                                                    else {
                                                                        out |= v45 << 16;
                                                                        int align = in.check(" :") ? in.getInt() : 0;
                                                                        switch(length) {
                                                                            case 1: {
                                                                                if((1 << size & idx_align) != 0) {
                                                                                    fail = j;
                                                                                }
                                                                                if(size <= 0) {
                                                                                    if(align != 0) {
                                                                                        fail = j;
                                                                                    }
                                                                                }
                                                                                else if(align != 0) {
                                                                                    if(Integer.bitCount(align) != 1) {
                                                                                        fail = j;
                                                                                    }
                                                                                    if(Integer.numberOfTrailingZeros(align) != size + 3) {
                                                                                        fail = j;
                                                                                    }
                                                                                    idx_align |= (1 << size) - 1;
                                                                                }
                                                                                break;
                                                                            }
                                                                            case 2: {
                                                                                if(align != 0) {
                                                                                    if(Integer.bitCount(align) != 1) {
                                                                                        fail = j;
                                                                                    }
                                                                                    if(Integer.numberOfTrailingZeros(align) != size + 4) {
                                                                                        fail = j;
                                                                                    }
                                                                                    idx_align |= 1;
                                                                                }
                                                                                break;
                                                                            }
                                                                            case 3: {
                                                                                break;
                                                                            }
                                                                            case 4: {
                                                                                if(size == 2) {
                                                                                    if(Integer.bitCount(align) > 1) {
                                                                                        fail = j;
                                                                                    }
                                                                                    idx_align |= align >> 6 & 3;
                                                                                }
                                                                                else if(align != 0) {
                                                                                    if(Integer.bitCount(align) != 1) {
                                                                                        fail = j;
                                                                                    }
                                                                                    if(Integer.numberOfTrailingZeros(align) != size + 5) {
                                                                                        fail = j;
                                                                                    }
                                                                                    idx_align |= 1;
                                                                                }
                                                                                break;
                                                                            }
                                                                            default: {
                                                                                throw new RuntimeException("151 Invalid length " + length + " for format char \'" + 'B' + "\'; " + i + "; " + j + "; \'" + asm + "\'");
                                                                            }
                                                                        }
                                                                        int idx_align = idx_align | v40 << size + 1;
                                                                        out |= (idx_align & 15) << 4;
                                                                        if((idx_align & -16) != 0) {
                                                                            fail = j;
                                                                        }
                                                                        if(in.check(']')) {
                                                                            if(in.check('!')) {
                                                                                rm = 13;
                                                                            }
                                                                            else if(in.check(", ")) {
                                                                                int v49 = in.index(ArmDis.REG);
                                                                                if(v49 == -1) {
                                                                                    break alab1;
                                                                                }
                                                                                else {
                                                                                    rm = v49;
                                                                                }
                                                                            }
                                                                            else {
                                                                                rm = 15;
                                                                            }
                                                                            out |= rm;
                                                                            break alab2;
                                                                        }
                                                                        else {
                                                                            break alab1;
                                                                        }
                                                                    }
                                                                }
                                                                else {
                                                                    break alab1;
                                                                }
                                                            }
                                                            if(!in.check(",D")) {
                                                                break alab1;
                                                            }
                                                            int v50 = in.getInt();
                                                            if(ix == 1) {
                                                                stride = v50 - v39;
                                                                if(stride == 2) {
                                                                    if(size == 0) {
                                                                        fail = j;
                                                                    }
                                                                    idx_align |= 1 << size;
                                                                }
                                                            }
                                                            if(v50 != ix * stride + v39) {
                                                                fail = j;
                                                            }
                                                            if(!in.check('[')) {
                                                                break alab1;
                                                            }
                                                            if(in.getInt() != v40) {
                                                                fail = j;
                                                            }
                                                            if(!in.check(']')) {
                                                                break alab1;
                                                            }
                                                        }
                                                    }
                                                    else {
                                                        break alab1;
                                                    }
                                                }
                                                else {
                                                    break alab1;
                                                }
                                            }
                                            else {
                                                break alab1;
                                            }
                                        }
                                        else {
                                            break alab1;
                                        }
                                        goto label_282;
                                    }
                                    case 67: {
                                    label_282:
                                        if(in.check('{')) {
                                            int type = out >> 8 & 3;
                                            int n = type + 1;
                                            if(in.check('D')) {
                                                int v53 = in.getInt();
                                                out |= (v53 & 15) << 12 | (v53 >> 4 & 1) << 22;
                                                if((v53 & 0xFFFFFFE0) != 0) {
                                                    fail = j;
                                                }
                                                if(in.check("[]")) {
                                                    if(in.check(",D")) {
                                                        out |= 0x20;
                                                        if(n == 1) {
                                                            n = 2;
                                                        }
                                                        for(int ix = 1; ix != n; ++ix) {
                                                            if(ix != 1 && !in.check(",D")) {
                                                                break alab1;
                                                            }
                                                            if(in.getInt() != ix * 2 + v53) {
                                                                fail = j;
                                                            }
                                                            if(!in.check("[]")) {
                                                                break alab1;
                                                            }
                                                        }
                                                    }
                                                    else if(in.check("-D")) {
                                                        if(n == 1) {
                                                            out |= 0x20;
                                                            n = 2;
                                                        }
                                                        if(in.getInt() != v53 + n - 1) {
                                                            fail = j;
                                                        }
                                                        if(in.check("[]")) {
                                                            goto label_311;
                                                        }
                                                        break alab1;
                                                    }
                                                label_311:
                                                    if(in.check("}, [")) {
                                                        int v55 = in.index(ArmDis.REG);
                                                        if(v55 == -1) {
                                                            break alab1;
                                                        }
                                                        else {
                                                            out |= v55 << 16;
                                                            if(in.check(" :")) {
                                                                out |= 16;
                                                                int v56 = in.getInt();
                                                                int size = Integer.numberOfTrailingZeros(v56) - Integer.numberOfTrailingZeros((type + 1) * 8);
                                                                if((type + 1) * 8 << size != v56) {
                                                                    fail = j;
                                                                }
                                                                if(type == 3) {
                                                                    if(size != 1) {
                                                                        if(size > 1) {
                                                                            ++size;
                                                                        }
                                                                    }
                                                                    else if((out >> 6 & 3 | 1) != 1) {
                                                                        size = 2;
                                                                    }
                                                                }
                                                                if(type == 2 || type == 0 && size == 0) {
                                                                    fail = j;
                                                                }
                                                                out |= (size & 3) << 6;
                                                                if(size != (out >> 6 & 3)) {
                                                                    fail = j;
                                                                }
                                                                if((size & -4) != 0) {
                                                                    fail = j;
                                                                }
                                                            }
                                                            if(in.check(']')) {
                                                                if(in.check('!')) {
                                                                    rm = 13;
                                                                }
                                                                else if(in.check(", ")) {
                                                                    int v59 = in.index(ArmDis.REG);
                                                                    if(v59 == -1) {
                                                                        break alab1;
                                                                    }
                                                                    else {
                                                                        rm = v59;
                                                                    }
                                                                }
                                                                else {
                                                                    rm = 15;
                                                                }
                                                                out |= rm;
                                                                break;
                                                            }
                                                            else {
                                                                break alab1;
                                                            }
                                                        }
                                                    }
                                                    else {
                                                        break alab1;
                                                    }
                                                }
                                                else {
                                                    break alab1;
                                                }
                                            }
                                            else {
                                                break alab1;
                                            }
                                        }
                                        else {
                                            break alab1;
                                        }
                                        goto label_347;
                                    }
                                    case 68: {
                                    label_347:
                                        if(in.check('D')) {
                                            int size = out >> 20 & 3;
                                            int v61 = in.getInt();
                                            out |= v61 & 15 | (v61 & 16) << 1;
                                            if(in.check('[')) {
                                                int ix = in.getInt() << 2 << size;
                                                out |= ix & 15 | (ix & 16) << 1;
                                                if(in.check(']')) {
                                                    if((~((4 << size) - 1) & v61) != 0) {
                                                        fail = j;
                                                    }
                                                    if((ix & 0xFFFFFFE0) != 0) {
                                                        fail = j;
                                                    }
                                                }
                                                else {
                                                    break alab1;
                                                }
                                            }
                                            else {
                                                break alab1;
                                            }
                                        }
                                        else {
                                            break alab1;
                                        }
                                        break;
                                    }
                                    case 69: {
                                        if(in.check('#')) {
                                            int cmode = out >> 8 & 15;
                                            if(cmode == 15) {
                                                int v74 = Float.floatToRawIntBits(in.getFloat());
                                                bits = v74 >> 19 & 0x7F | v74 >> 24 & 0x80;
                                                if((v74 >> 24 & 0x7C) != ((bits & 0x40) == 0 ? 0x40 : 60)) {
                                                    fail = j;
                                                }
                                            }
                                            else if(cmode != 14 || (out >> 5 & 1) == 0) {
                                                int v64 = in.getInt();
                                                if(cmode < 8) {
                                                    int shift = v64 == 0 ? 0 : Integer.numberOfTrailingZeros(v64) >> 3;
                                                    out |= shift << 1 << 8;
                                                    bits = v64 >> shift * 8 & 0xFF;
                                                    if((~(0xFF << shift * 8) & v64) != 0) {
                                                        fail = j;
                                                    }
                                                }
                                                else if(cmode < 12) {
                                                    int shift = (v64 & 0xFF) == 0 ? 1 : 0;
                                                    out |= shift << 1 << 8;
                                                    bits = v64 >> shift * 8 & 0xFF;
                                                    if((~(0xFF << shift * 8) & v64) != 0) {
                                                        fail = j;
                                                    }
                                                }
                                                else if(cmode < 14) {
                                                    int shift = (0xFFFF & v64) == 0xFFFF ? 2 : 1;
                                                    out |= shift - 1 << 8;
                                                    bits = v64 >> shift * 8 & 0xFF;
                                                    if((~(0xFF << shift * 8) & v64) != (1 << shift * 8) - 1) {
                                                        fail = j;
                                                    }
                                                }
                                                else {
                                                    bits = v64;
                                                }
                                            }
                                            else {
                                                long v69 = in.getHex();
                                                int hival = (int)(v69 >> 0x20);
                                                int value = (int)v69;
                                                bits = 0;
                                                for(int ix = 7; ix >= 0; --ix) {
                                                    int tmp = (ix > 3 ? hival : value) >> (ix > 3 ? ix - 4 : ix) * 8 & 0xFF;
                                                    if(tmp != 0) {
                                                        if(tmp != 0xFF) {
                                                            fail = j;
                                                        }
                                                        bits |= 1 << ix;
                                                    }
                                                }
                                            }
                                            out = out | (bits >> 7 & 1) << 24 | (bits >> 4 & 7) << 16 | bits & 15;
                                            break;
                                        }
                                        else {
                                            break alab1;
                                        }
                                        goto label_407;
                                    }
                                    case 70: {
                                    label_407:
                                        if(in.check("{D")) {
                                            int v75 = in.getInt();
                                            int num = in.check("-D") ? in.getInt() - v75 : 0;
                                            out = out | ((v75 & 15) << 16 | (v75 & 16) << 3) | (num & 3) << 8;
                                            if(num + v75 >= 0x20) {
                                                fail = j;
                                            }
                                            if((v75 & 0xFFFFFFE0) != 0 || (num & -4) != 0) {
                                                fail = j;
                                            }
                                            if(in.check('}')) {
                                                break;
                                            }
                                        }
                                        break alab1;
                                    }
                                    case 99: 
                                    case 0x75: {
                                        break;
                                    }
                                    default: {
                                        throw new RuntimeException("61 Invalid format char \'" + ((char)c) + "\'; " + i + "; " + j + "; \'" + asm + "\'");
                                    }
                                }
                            }
                            else {
                                if(c == 59) {
                                    break;
                                }
                                else {
                                    if(97 <= c && c <= 0x7A) {
                                        c = (char)(c - 0x20);
                                    }
                                    if(in.check(((char)c))) {
                                        goto label_416;
                                    }
                                    else {
                                        break alab1;
                                    }
                                }
                                goto label_27;
                            }
                        label_416:
                            ++j;
                        }
                        in.check(' ');
                        if(fail == -1) {
                            int v77 = in.get();
                            if(v77 > 0x20 && v77 != 59 || (out & mask) != NUM[i * 2]) {
                                break;
                            }
                            else {
                                for(int k = 0; k < i; ++k) {
                                    if((out & NUM[k * 2 + 1]) == NUM[k * 2]) {
                                        break alab1;
                                    }
                                }
                                return out;
                            }
                        }
                        else {
                            break;
                        }
                    }
                label_428:
                    ++i;
                    continue;
                }
                catch(NextAsm unused_ex) {
                }
                break;
            }
            if(in.pos <= in.best) {
                goto label_428;
            }
            in.best = in.pos;
            in.bestOut = out;
            goto label_428;
        }

        static String getOpcode(ArmDis ret, long addr, int data, boolean thumb) {
            int size;
            int value;
            int limit;
            if(thumb) {
                if((0xEF000000 & data) != 0xEF000000) {
                    if((0xFF000000 & data) == 0xF9000000) {
                        data ^= 0xD000000;
                        goto label_11;
                    }
                    return null;
                }
                else if((0x10000000 & data) != 0) {
                    data = data & 0xFFFFFF | 0xF3000000;
                }
                else {
                    data = data & 0xFFFFFF | 0xF2000000;
                }
            }
        label_11:
            int i;
            for(i = 0; true; ++i) {
                if(i >= Neon.STR.length) {
                    return null;
                }
                if((Neon.NUM[i * 2 + 1] & data) == Neon.NUM[i * 2]) {
                    break;
                }
            }
            StringBuilder out = new StringBuilder();
            int commentValue = 0;
            String asm = Neon.STR[i];
            int j = 0;
            int v5 = asm.length();
            while(true) {
                if(j >= v5) {
                    if(commentValue > 0x20 || commentValue < -16) {
                        out.append("\t ; 0x").append(Integer.toHexString(commentValue).toUpperCase(Locale.US));
                    }
                    return out.toString();
                }
                int v6 = ArmDis.charAt(asm, v5, j);
                if(v6 == 37) {
                    ++j;
                    int c = ArmDis.charAt(asm, v5, j);
                alab1:
                    switch(c) {
                        case 37: {
                            out.append('%');
                            break;
                        }
                        case 0x30: 
                        case 49: 
                        case 50: 
                        case 51: 
                        case 52: 
                        case 53: 
                        case 54: 
                        case 55: 
                        case 56: 
                        case 57: {
                            j = ArmDis.decodeBitfield(asm, v5, j, data, ret);
                            int v8 = ret.valuep;
                            int v9 = ret.widthp;
                            int c = ArmDis.charAt(asm, v5, j);
                            switch(c) {
                                case 39: {
                                    ++j;
                                    int c = ArmDis.charAt(asm, v5, j);
                                    if(v8 == (1 << v9) - 1) {
                                        ArmDis.append(out, ((char)c));
                                    }
                                    break alab1;
                                }
                                case 0x3F: {
                                    ArmDis.append(out, ArmDis.charAt(asm, v5, (1 << v9) + j - v8));
                                    j += 1 << v9;
                                    break alab1;
                                }
                                case 68: {
                                    out.append('D').append(v8);
                                    break alab1;
                                }
                                case 81: {
                                label_55:
                                    if((v8 & 1) == 0) {
                                        out.append('Q').append(v8 >> 1);
                                    }
                                    else {
                                        out.append("<illegal reg Q").append(v8 >> 1).append(".5>");
                                    }
                                    break alab1;
                                }
                                case 82: {
                                    if((data & 0x40) == 0) {
                                        out.append('D').append(v8);
                                        break alab1;
                                    }
                                    goto label_55;
                                }
                                case 83: 
                                case 84: 
                                case 85: {
                                    int base = 8 << c - 83;
                                    ++j;
                                    int c = ArmDis.charAt(asm, v5, j);
                                    if(c < 0x30 || c > 57) {
                                        if(c < 97 || c > 102) {
                                            throw new RuntimeException("40 Invalid format char \'" + ((char)c) + "\'; " + i + "; " + j + "; \'" + asm + "\'");
                                        }
                                        limit = c - 87;
                                    label_68:
                                        if(v8 >= limit >> 2 && v8 <= (limit & 3)) {
                                            out.append(base << v8);
                                        }
                                        else {
                                            out.append("<illegal width ").append(base << v8).append('>');
                                        }
                                        break alab1;
                                    }
                                    else {
                                        limit = c - 0x30;
                                        goto label_68;
                                    }
                                    goto label_74;
                                }
                                case 0x60: {
                                label_74:
                                    ++j;
                                    int c = ArmDis.charAt(asm, v5, j);
                                    if(v8 == 0) {
                                        ArmDis.append(out, ((char)c));
                                    }
                                    break alab1;
                                }
                                case 100: {
                                    out.append(v8);
                                    commentValue = v8;
                                    break alab1;
                                }
                                case 101: {
                                    out.append((1 << v9) - v8);
                                    break alab1;
                                }
                                case 0x72: {
                                    out.append(ArmDis.REG[v8]);
                                    break alab1;
                                }
                                default: {
                                    throw new RuntimeException("50 Invalid format char \'" + ((char)c) + "\'; " + i + "; " + j + "; \'" + asm + "\'");
                                }
                            }
                        }
                        case 65: {
                            int rd = data >> 12 & 15 | (data >> 22 & 1) << 4;
                            int align = data >> 4 & 3;
                            int type = data >> 8 & 15;
                            int n = Neon.ENC[type] & 15;
                            int stride = (Neon.ENC[type] >> 4) + 1;
                            out.append('{');
                            if(stride > 1) {
                                for(int ix = 0; ix != n; ++ix) {
                                    if(ix != 0) {
                                        out.append(',');
                                    }
                                    out.append('D').append(ix * stride + rd);
                                }
                            }
                            else if(n == 1) {
                                out.append('D').append(rd);
                            }
                            else {
                                out.append('D').append(rd).append("-D").append(rd + n - 1);
                            }
                            out.append("}, [").append(ArmDis.REG[data >> 16 & 15]);
                            if(align != 0) {
                                out.append(" :").append(0x20 << align);
                            }
                            out.append(']');
                            if((data & 15) == 13) {
                                out.append('!');
                            }
                            else if((data & 15) != 15) {
                                out.append(", ").append(ArmDis.REG[data & 15]);
                            }
                            break;
                        }
                        case 66: {
                            int idx_align = data >> 4 & 15;
                            int align = 0;
                            int size = data >> 10 & 3;
                            int idx = idx_align >> size + 1;
                            int length = (data >> 8 & 3) + 1;
                            int stride = length <= 1 || size <= 0 || (1 << size & idx_align) == 0 ? 1 : 2;
                            switch(length) {
                                case 1: {
                                    if((1 << size & idx_align) != 0) {
                                        return null;
                                    }
                                    if(size > 0) {
                                        int amask = (1 << size) - 1;
                                        if((idx_align & amask) == amask) {
                                            align = 8 << size;
                                        }
                                        else if((idx_align & amask) != 0) {
                                            return null;
                                        }
                                    }
                                    break;
                                }
                                case 2: {
                                    if(size == 2 && (idx_align & 2) != 0) {
                                        return null;
                                    }
                                    if((idx_align & 1) != 0) {
                                        align = 16 << size;
                                    }
                                    break;
                                }
                                case 3: {
                                    if(size == 2 && (idx_align & 3) != 0 || (idx_align & 1) != 0) {
                                        return null;
                                    }
                                    break;
                                }
                                case 4: {
                                    if(size == 2) {
                                        if((idx_align & 3) == 3) {
                                            return null;
                                        }
                                        align = (idx_align & 3) * 0x40;
                                    }
                                    else if((idx_align & 1) != 0) {
                                        align = 0x20 << size;
                                    }
                                    break;
                                }
                                default: {
                                    throw new RuntimeException("150 Invalid length " + length + " for format char \'" + 'B' + "\'; " + i + "; " + j + "; \'" + asm + "\'");
                                }
                            }
                            out.append('{');
                            for(int ix = 0; ix < length; ++ix) {
                                if(ix != 0) {
                                    out.append(',');
                                }
                                out.append('D').append(ix * stride + (data >> 12 & 15 | (data >> 22 & 1) << 4)).append('[').append(idx).append(']');
                            }
                            out.append("}, [").append(ArmDis.REG[data >> 16 & 15]);
                            if(align != 0) {
                                out.append(" :").append(align);
                            }
                            out.append(']');
                            if((data & 15) == 13) {
                                out.append('!');
                            }
                            else if((data & 15) != 15) {
                                out.append(", ").append(ArmDis.REG[data & 15]);
                            }
                            break;
                        }
                        case 67: {
                            int rd = data >> 12 & 15 | (data >> 22 & 1) << 4;
                            int size = data >> 6 & 3;
                            int type = data >> 8 & 3;
                            int n = type + 1;
                            int stride = data >> 5 & 1;
                            if(stride == 0 || n != 1) {
                                ++stride;
                            }
                            else {
                                n = 2;
                            }
                            out.append('{');
                            if(stride > 1) {
                                for(int ix = 0; ix != n; ++ix) {
                                    if(ix != 0) {
                                        out.append(',');
                                    }
                                    out.append('D').append(ix * stride + rd).append("[]");
                                }
                            }
                            else if(n == 1) {
                                out.append('D').append(rd).append("[]");
                            }
                            else {
                                out.append('D').append(rd).append("[]-D").append(rd + n - 1).append("[]");
                            }
                            out.append("}, [").append(ArmDis.REG[data >> 16 & 15]);
                            if((data >> 4 & 1) != 0) {
                                int align = type != 3 || size <= 1 ? (type + 1) * 8 << size : (type + 1) * 8 << size >> 1;
                                if(type != 2 && (type != 0 || size != 0)) {
                                    out.append(" :").append(align);
                                }
                                else {
                                    out.append(" :<bad align ").append(align).append('>');
                                }
                            }
                            out.append(']');
                            if((data & 15) == 13) {
                                out.append('!');
                            }
                            else if((data & 15) != 15) {
                                out.append(", ").append(ArmDis.REG[data & 15]);
                            }
                            break;
                        }
                        case 68: {
                            int raw_reg = data & 15 | data >> 1 & 16;
                            int size = data >> 20 & 3;
                            out.append('D').append(raw_reg & (4 << size) - 1).append('[').append(raw_reg >> size >> 2).append(']');
                            break;
                        }
                        case 69: {
                            int cmode = data >> 8 & 15;
                            int op = data >> 5 & 1;
                            int hival = 0;
                            boolean isfloat = false;
                            int bits = (data >> 24 & 1) << 7 | (data >> 16 & 7) << 4 | data & 15;
                            if(cmode < 8) {
                                value = bits << (cmode >> 1 & 3) * 8;
                                size = 0x20;
                                goto label_244;
                            }
                            else if(cmode < 12) {
                                value = bits << (cmode >> 1 & 1) * 8;
                                size = 16;
                                goto label_244;
                            }
                            else if(cmode < 14) {
                                int shift = (cmode & 1) + 1;
                                value = bits << shift * 8 | (1 << shift * 8) - 1;
                                size = 0x20;
                                goto label_244;
                            }
                            else if(cmode == 14) {
                                if(op == 0) {
                                    value = bits;
                                    size = 8;
                                }
                                else {
                                    value = 0;
                                    for(int ix = 7; ix >= 0; --ix) {
                                        int mask = (bits >> ix & 1) == 0 ? 0 : 0xFF;
                                        if(ix <= 3) {
                                            value = value << 8 | mask;
                                        }
                                        else {
                                            hival = hival << 8 | mask;
                                        }
                                    }
                                    size = 0x40;
                                }
                                goto label_244;
                            }
                            else {
                                if(op == 0) {
                                    value = (bits & 0x7F) << 19 | (bits & 0x80) << 24 | ((bits & 0x40) == 0 ? 0x40 : 60) << 24;
                                    size = 0x20;
                                    isfloat = true;
                                label_244:
                                    out.append('#');
                                    switch(size) {
                                        case 8: 
                                        case 16: 
                                        case 0x20: {
                                            if(isfloat) {
                                                out.append(Float.intBitsToFloat(value));
                                            }
                                            else {
                                                out.append(value);
                                            }
                                            out.append("\t ; 0x");
                                            ToolsLight.prefixIntegerHex(out, size >> 2, value);
                                            break;
                                        }
                                        case 0x40: {
                                            ToolsLight.prefixIntegerHex(out, 8, hival);
                                            ToolsLight.prefixIntegerHex(out, 8, value);
                                            break;
                                        }
                                        default: {
                                            throw new RuntimeException("160 Invalid size " + size + " for format char \'" + 'E' + "\'; " + i + "; " + j + "; \'" + asm + "\'");
                                        }
                                    }
                                }
                                else {
                                    out.append("<illegal constant ");
                                    ToolsLight.prefixIntegerHex(out, 8, bits);
                                    out.append(':').append(Integer.toHexString(cmode).toUpperCase(Locale.US)).append(':').append(Integer.toHexString(op).toUpperCase(Locale.US)).append('>');
                                }
                                break;
                            }
                            goto label_262;
                        }
                        case 70: {
                        label_262:
                            int regno = data >> 16 & 15 | data >> 3 & 16;
                            int num = data >> 8 & 3;
                            out.append("{D").append(regno);
                            if(num != 0) {
                                if(num + regno >= 0x20) {
                                    out.append("-<overflow reg D").append(regno + num);
                                }
                                else {
                                    out.append("-D").append(regno + num);
                                }
                            }
                            out.append('}');
                            break;
                        }
                        case 99: 
                        case 0x75: {
                            break;
                        }
                        default: {
                            throw new RuntimeException("60 Invalid format char \'" + ((char)c) + "\'; " + i + "; " + j + "; \'" + asm + "\'");
                        }
                    }
                }
                else {
                    ArmDis.append(out, ((char)v6));
                }
                ++j;
            }
        }
    }

    static class NextAsm extends RuntimeException {
    }

    static class Thumb16 {
        static final short[] NUM;
        static final String[] STR;

        static {
            Thumb16.NUM = new short[]{0xFFFFBF50, -1, 0xFFFFBA80, 0xFFFFFFC0, 0xFFFFBF00, -1, 0xFFFFBF10, -1, 0xFFFFBF20, -1, 0xFFFFBF30, -1, 0xFFFFBF40, -1, 0xFFFFBF00, 0xFFFFFF0F, 0xFFFFB900, 0xFFFFFD00, 0xFFFFB100, 0xFFFFFD00, 0xFFFFBF00, 0xFFFFFF00, 0xFFFFB660, -8, 0xFFFFB670, -8, 0x4600, 0xFFFFFFC0, 0xFFFFBA00, 0xFFFFFFC0, 0xFFFFBA40, 0xFFFFFFC0, 0xFFFFBAC0, 0xFFFFFFC0, 0xFFFFB650, -9, 0xFFFFB200, 0xFFFFFFC0, 0xFFFFB240, 0xFFFFFFC0, 0xFFFFB280, 0xFFFFFFC0, 0xFFFFB2C0, 0xFFFFFFC0, 0xFFFFBE00, 0xFFFFFF00, 0x4780, 0xFFFFFF87, 0x46C0, -1, 0x4000, 0xFFFFFFC0, 0x4040, 0xFFFFFFC0, 0x4080, 0xFFFFFFC0, 0x40C0, 0xFFFFFFC0, 0x4100, 0xFFFFFFC0, 0x4140, 0xFFFFFFC0, 0x4180, 0xFFFFFFC0, 0x41C0, 0xFFFFFFC0, 0x4200, 0xFFFFFFC0, 0x4240, 0xFFFFFFC0, 0x4280, 0xFFFFFFC0, 0x42C0, 0xFFFFFFC0, 0x4300, 0xFFFFFFC0, 0x4340, 0xFFFFFFC0, 0x4380, 0xFFFFFFC0, 0x43C0, 0xFFFFFFC0, 0xFFFFB000, 0xFFFFFF80, 0xFFFFB080, 0xFFFFFF80, 0x4700, 0xFFFFFF80, 0x4400, 0xFFFFFF00, 0x4500, 0xFFFFFF00, 0x4600, 0xFFFFFF00, 0xFFFFB400, 0xFFFFFE00, 0xFFFFBC00, 0xFFFFFE00, 0x1800, 0xFFFFFE00, 0x1A00, 0xFFFFFE00, 0x1C00, 0xFFFFFE00, 0x1E00, 0xFFFFFE00, 0x5200, 0xFFFFFE00, 0x5A00, 0xFFFFFE00, 0x5600, 0xFFFFF600, 0x5000, 0xFFFFFA00, 0x5800, 0xFFFFFA00, 0, 0xFFFFFFC0, 0, 0xFFFFF800, 0x800, 0xFFFFF800, 0x1000, 0xFFFFF800, 0x2000, 0xFFFFF800, 0x2800, 0xFFFFF800, 0x3000, 0xFFFFF800, 0x3800, 0xFFFFF800, 0x4800, 0xFFFFF800, 0x6000, 0xFFFFF800, 0x6800, 0xFFFFF800, 0x7000, 0xFFFFF800, 0x7800, 0xFFFFF800, 0xFFFF8000, 0xFFFFF800, 0xFFFF8800, 0xFFFFF800, 0xFFFF9000, 0xFFFFF800, 0xFFFF9800, 0xFFFFF800, 0xFFFFA000, 0xFFFFF800, 0xFFFFA800, 0xFFFFF800, 0xFFFFC000, 0xFFFFF800, 0xFFFFC800, 0xFFFFF800, 0xFFFFDF00, 0xFFFFFF00, 0xFFFFDE00, 0xFFFFFF00, 0xFFFFDF00, 0xFFFFFF00, 0xFFFFD000, 0xFFFFF000, 0xFFFFE000, 0xFFFFF800, 0, 0};
            Thumb16.STR = new String[]{"SEVL%c", "HLT\t %0-5x", "NOP%c", "YIELD%c", "WFE%c", "WFI%c", "SEV%c", "NOP%c\t {%4-7d}", "CBNZ\t %0-2r, %b%X", "CBZ\t %0-2r, %b%X", "IT%I%X", "CPSIE%2\'A%1\'I%0\'F%X", "CPSID%2\'A%1\'I%0\'F%X", "MOV%c\t %0-2r, %3-5r", "REV%c\t %0-2r, %3-5r", "REV16%c\t %0-2r, %3-5r", "REVSH%c\t %0-2r, %3-5r", "SETEND%3?BLE%X", "SXTH%c\t %0-2r, %3-5r", "SXTB%c\t %0-2r, %3-5r", "UXTH%c\t %0-2r, %3-5r", "UXTB%c\t %0-2r, %3-5r", "BKPT\t %0-7x", "BLX%c\t %3-6r%x", "NOP%c\t \t \t ; (MOV R8, R8)", "AND%C\t %0-2r, %3-5r", "EOR%C\t %0-2r, %3-5r", "LSL%C\t %0-2r, %3-5r", "LSR%C\t %0-2r, %3-5r", "ASR%C\t %0-2r, %3-5r", "ADC%C\t %0-2r, %3-5r", "SBC%C\t %0-2r, %3-5r", "ROR%C\t %0-2r, %3-5r", "TST%c\t %0-2r, %3-5r", "NEG%C\t %0-2r, %3-5r", "CMP%c\t %0-2r, %3-5r", "CMN%c\t %0-2r, %3-5r", "ORR%C\t %0-2r, %3-5r", "MUL%C\t %0-2r, %3-5r", "BIC%C\t %0-2r, %3-5r", "MVN%C\t %0-2r, %3-5r", "ADD%c\t SP, #%0-6W", "SUB%c\t SP, #%0-6W", "BX%c\t %S%x", "ADD%c\t %D, %S", "CMP%c\t %D, %S", "MOV%c\t %D, %S", "PUSH%c\t %N", "POP%c\t %O", "ADD%C\t %0-2r, %3-5r, %6-8r", "SUB%C\t %0-2r, %3-5r, %6-8r", "ADD%C\t %0-2r, %3-5r, #%6-8d", "SUB%C\t %0-2r, %3-5r, #%6-8d", "STRH%c\t %0-2r, [%3-5r,%6-8r]", "LDRH%c\t %0-2r, [%3-5r,%6-8r]", "LDRS%11?HB%c\t %0-2r, [%3-5r,%6-8r]", "STR%10\'B%c\t %0-2r, [%3-5r,%6-8r]", "LDR%10\'B%c\t %0-2r, [%3-5r,%6-8r]", "MOV%C\t %0-2r, %3-5r", "LSL%C\t %0-2r, %3-5r, #%6-10d", "LSR%C\t %0-2r, %3-5r, %s", "ASR%C\t %0-2r, %3-5r, %s", "MOV%C\t %8-10r, #%0-7d", "CMP%c\t %8-10r, #%0-7d", "ADD%C\t %8-10r, #%0-7d", "SUB%C\t %8-10r, #%0-7d", "LDR%c\t %8-10r, [PC,#%0-7W]\t ; (%0-7a)", "STR%c\t %0-2r, [%3-5r,#%6-10W]", "LDR%c\t %0-2r, [%3-5r,#%6-10W]", "STRB%c\t %0-2r, [%3-5r,#%6-10d]", "LDRB%c\t %0-2r, [%3-5r,#%6-10d]", "STRH%c\t %0-2r, [%3-5r,#%6-10H]", "LDRH%c\t %0-2r, [%3-5r,#%6-10H]", "STR%c\t %8-10r, [SP,#%0-7W]", "LDR%c\t %8-10r, [SP,#%0-7W]", "ADD%c\t %8-10r, PC, #%0-7W\t ; (ADR %8-10r, %0-7a)", "ADD%c\t %8-10r, SP, #%0-7W", "STMIA%c\t %8-10r!, %M", "LDMIA%c\t %8-10r%W, %M", "SVC%c\t %0-7d", "UDF%c\t #%0-7d", "\t \t ; <UNDEFINED> instruction: %0-31x", "B%8-11c.N\t %0-7B%X", "B%c.N\t %0-10B%x", "\t \t ; <UNDEFINED> instruction: %0-31x"};
        }

        static int getOpcode(ArmDis ret, long addr, String data, int dbg) {
            int reg;
            int out;
            Input armDis$Input0 = new Input(data);
            ret.input = armDis$Input0;
            short[] NUM = Thumb16.NUM;
            String[] STR = Thumb16.STR;
            int i = 0;
        alab1:
            while(true) {
                if(i >= STR.length) {
                    return -1;
                }
                out = NUM[i * 2];
                armDis$Input0.reset();
                try {
                    String asm = STR[i];
                    int mask = NUM[i * 2 + 1];
                    if(mask != 0) {
                        int fail = -1;
                        int j = 0;
                        int v7 = asm.length();
                        while(j < v7) {
                            int c = ArmDis.charAt(asm, v7, j);
                            if(c <= 0x20) {
                                c = 0x20;
                                while(j < v7 && ArmDis.charAt(asm, v7, j + 1) <= 0x20) {
                                    ++j;
                                }
                            }
                            if(c == 37) {
                            label_28:
                                int relative = 0;
                                ++j;
                                int c = ArmDis.charAt(asm, v7, j);
                            alab2:
                                switch(c) {
                                    case 37: {
                                        if(!armDis$Input0.check('%')) {
                                            break alab1;
                                        }
                                        break;
                                    }
                                    case 0x30: 
                                    case 49: 
                                    case 50: 
                                    case 51: 
                                    case 52: 
                                    case 53: 
                                    case 54: 
                                    case 55: 
                                    case 56: 
                                    case 57: {
                                        int bitstart = c - 0x30;
                                        int bitend = 0;
                                        int j = j + 1;
                                        int c;
                                        for(c = ArmDis.charAt(asm, v7, j); c >= 0x30 && c <= 57; c = ArmDis.charAt(asm, v7, j)) {
                                            bitstart = bitstart * 10 + c - 0x30;
                                            ++j;
                                        }
                                        switch(c) {
                                            case 39: {
                                                j = j + 1;
                                                if(armDis$Input0.check(ArmDis.charAt(asm, v7, j))) {
                                                    out |= 1 << bitstart;
                                                }
                                                break alab2;
                                            }
                                            case 45: {
                                                j = j + 1;
                                                int c;
                                                for(c = ArmDis.charAt(asm, v7, j); c >= 0x30 && c <= 57; c = ArmDis.charAt(asm, v7, j)) {
                                                    bitend = bitend * 10 + c - 0x30;
                                                    ++j;
                                                }
                                                if(bitend == 0) {
                                                    throw new RuntimeException("181 Zero bitend; " + i + "; " + j + "; " + asm);
                                                }
                                                int relative = 0;
                                                switch(c) {
                                                    case 66: {
                                                        if(armDis$Input0.check('+')) {
                                                            relative = 1;
                                                        }
                                                        else if(armDis$Input0.check('-')) {
                                                            relative = -1;
                                                        }
                                                        if(armDis$Input0.check("0X")) {
                                                            long address = armDis$Input0.getHex();
                                                            if(relative != 0) {
                                                                address = addr + ((long)relative) * address;
                                                            }
                                                            reg = (1 << bitend) + (((int)(address - addr)) - 4 >> 1) ^ 1 << bitend;
                                                            break;
                                                        }
                                                        else {
                                                            break alab1;
                                                        }
                                                        goto label_73;
                                                    }
                                                    case 99: {
                                                        int v19 = armDis$Input0.index(ArmDis.COND);
                                                        if(v19 == -1) {
                                                            break alab1;
                                                        }
                                                        else {
                                                            reg = v19;
                                                            break;
                                                        }
                                                        goto label_87;
                                                    }
                                                    case 72: 
                                                    case 87: 
                                                    case 100: {
                                                    label_73:
                                                        reg = armDis$Input0.getInt();
                                                        if(c == 72) {
                                                            if((reg & 1) != 0) {
                                                                fail = j;
                                                            }
                                                            reg >>= 1;
                                                        }
                                                        if(c == 87) {
                                                            if((reg & 3) != 0) {
                                                                fail = j;
                                                            }
                                                            reg >>= 2;
                                                        }
                                                        break;
                                                    }
                                                    case 0x72: {
                                                    label_87:
                                                        reg = armDis$Input0.index(ArmDis.REG);
                                                        if(reg == -1) {
                                                            break alab1;
                                                        }
                                                        else {
                                                            break;
                                                        }
                                                        goto label_90;
                                                    }
                                                    case 120: {
                                                    label_90:
                                                        if(armDis$Input0.check("0X")) {
                                                            reg = (int)armDis$Input0.getHex();
                                                        }
                                                        else {
                                                            break alab1;
                                                        }
                                                        break;
                                                    }
                                                    default: {
                                                        throw new RuntimeException("91 Invalid format char \'" + ((char)c) + "\'; " + i + "; " + j + "; \'" + asm + "\'");
                                                    }
                                                }
                                                out |= ((2 << bitend - bitstart) - 1 & reg) << bitstart;
                                                if((~((2 << bitend - bitstart) - 1) & reg) != 0) {
                                                    fail = j;
                                                }
                                                break alab2;
                                            }
                                            case 0x3F: {
                                                if(armDis$Input0.check(ArmDis.charAt(asm, v7, j + 1))) {
                                                    out |= 1 << bitstart;
                                                    j = j + 2;
                                                    break alab2;
                                                }
                                                else if(armDis$Input0.check(ArmDis.charAt(asm, v7, j + 2))) {
                                                    j = j + 2;
                                                    break alab2;
                                                }
                                                else {
                                                    break alab1;
                                                }
                                                goto label_103;
                                            }
                                            default: {
                                                throw new RuntimeException("101 Invalid format char \'" + ((char)c) + "\'; " + i + "; " + j + "; \'" + asm + "\'");
                                            }
                                        }
                                    }
                                    case 67: {
                                    label_103:
                                        if(!armDis$Input0.check('S')) {
                                            break alab1;
                                        }
                                        break;
                                    }
                                    case 68: {
                                        int v20 = armDis$Input0.index(ArmDis.REG);
                                        if(v20 == -1) {
                                            break alab1;
                                        }
                                        else {
                                            out |= v20 & 7 | (v20 & 8) << 4;
                                            break;
                                        }
                                        goto label_109;
                                    }
                                    case 73: {
                                    label_109:
                                        int suff = 0;
                                        int last = 0;
                                        int used = 0;
                                        for(int tmp = 8; true; tmp >>= 1) {
                                            if(armDis$Input0.check(' ')) {
                                                int v25 = armDis$Input0.index(ArmDis.COND);
                                                if(v25 == -1 || (v25 & -16) != 0) {
                                                    fail = j;
                                                }
                                                if((v25 & 1) != 0 && (suff | last) != 0) {
                                                    suff ^= used;
                                                }
                                                out = out | v25 << 4 | ((suff | last) == 0 ? 8 : suff | last);
                                                break alab2;
                                            }
                                            if(armDis$Input0.check('E')) {
                                                suff |= tmp;
                                            }
                                            else if(!armDis$Input0.check('T')) {
                                                break alab1;
                                            }
                                            if(tmp == 1) {
                                                break alab1;
                                            }
                                            last = tmp >> 1;
                                            used |= tmp;
                                        }
                                    }
                                    case 77: 
                                    case 78: 
                                    case 0x4F: {
                                        if(armDis$Input0.check('{')) {
                                            int range = -1;
                                            while(true) {
                                                int v27 = armDis$Input0.index(ArmDis.REG);
                                                int v28 = armDis$Input0.getAndNext();
                                                if(v27 >= 8) {
                                                    break;
                                                }
                                                if(v27 == -1) {
                                                    goto label_147;
                                                }
                                                if(range == -1) {
                                                    range = v27;
                                                }
                                                out |= (2 << v27 - range) - 1 << range;
                                                switch(v28) {
                                                    case 44: {
                                                        range = -1;
                                                        break;
                                                    }
                                                    case 45: {
                                                        range = v27;
                                                        break;
                                                    }
                                                    default: {
                                                        goto label_147;
                                                    }
                                                }
                                            }
                                            if((c != 78 || v27 != 14) && (c != 0x4F || v27 != 15)) {
                                                break alab1;
                                            }
                                            else {
                                                out |= 0x100;
                                            }
                                        label_147:
                                            if(v28 != 0x7D) {
                                                break alab1;
                                            }
                                        }
                                        else {
                                            break alab1;
                                        }
                                        break;
                                    }
                                    case 83: {
                                        int v29 = armDis$Input0.index(ArmDis.REG);
                                        if(v29 == -1) {
                                            break alab1;
                                        }
                                        else {
                                            out |= (v29 & 7) << 3 | (v29 & 8) << 3;
                                            break;
                                        }
                                        goto label_153;
                                    }
                                    case 87: {
                                    label_153:
                                        if(!armDis$Input0.check('!')) {
                                            out |= 1 << ((out & 0x700) >> 8);
                                        }
                                        break;
                                    }
                                    case 98: {
                                        if(armDis$Input0.check('+')) {
                                            relative = 1;
                                        }
                                        else if(armDis$Input0.check('-')) {
                                            relative = -1;
                                        }
                                        if(armDis$Input0.check("0X")) {
                                            long address = armDis$Input0.getHex();
                                            if(relative != 0) {
                                                address = addr + ((long)relative) * address;
                                            }
                                            int offset = ((int)(address - addr)) - 4;
                                            out |= offset << 2 & 0xF8 | offset << 3 & 0x200;
                                            if((offset & 0xFFFFFF81) != 0) {
                                                fail = j;
                                            }
                                        }
                                        else {
                                            break alab1;
                                        }
                                        break;
                                    }
                                    case 0x73: {
                                        if(armDis$Input0.check('#')) {
                                            int imm = armDis$Input0.getInt();
                                            if(imm == 0x20) {
                                                imm = 0;
                                            }
                                            if((imm & 0xFFFFFFE0) != 0) {
                                                fail = j;
                                            }
                                            out |= (imm & 0x1F) << 6;
                                        }
                                        else {
                                            break alab1;
                                        }
                                        break;
                                    }
                                    case 88: 
                                    case 99: 
                                    case 120: {
                                        break;
                                    }
                                    default: {
                                        throw new RuntimeException("111 Invalid format char \'" + ((char)c) + "\'; " + i + "; " + j + "; \'" + asm + "\'");
                                    }
                                }
                            }
                            else {
                                if(c == 59) {
                                    break;
                                }
                                else {
                                    if(97 <= c && c <= 0x7A) {
                                        c = (char)(c - 0x20);
                                    }
                                    if(armDis$Input0.check(((char)c))) {
                                        goto label_177;
                                    }
                                    else {
                                        break alab1;
                                    }
                                }
                                goto label_28;
                            }
                        label_177:
                            ++j;
                        }
                        armDis$Input0.check(' ');
                        if(fail == -1) {
                            int v33 = armDis$Input0.get();
                            if(v33 > 0x20 && v33 != 59 || (out & mask) != NUM[i * 2]) {
                                break;
                            }
                            else {
                                for(int k = 0; k < i; ++k) {
                                    if((out & NUM[k * 2 + 1]) == NUM[k * 2]) {
                                        break alab1;
                                    }
                                }
                                return out;
                            }
                        }
                        else {
                            break;
                        }
                    }
                label_189:
                    ++i;
                    continue;
                }
                catch(NextAsm unused_ex) {
                }
                break;
            }
            if(armDis$Input0.pos <= armDis$Input0.best) {
                goto label_189;
            }
            armDis$Input0.best = armDis$Input0.pos;
            armDis$Input0.bestOut = out;
            goto label_189;
        }

        static String getOpcode(long addr, int data) {
            int i;
            for(i = 0; true; ++i) {
                if(i >= Thumb16.STR.length) {
                    throw new RuntimeException("190 No Thumb match for " + Integer.toHexString(data).toUpperCase(Locale.US) + "; " + Integer.toHexString(Integer.reverseBytes(data)).toUpperCase(Locale.US));
                }
                if((Thumb16.NUM[i * 2 + 1] & ((short)data)) == Thumb16.NUM[i * 2]) {
                    break;
                }
            }
            StringBuilder out = new StringBuilder();
            int commentValue = 0;
            String asm = Thumb16.STR[i];
            int j = 0;
            int v5 = asm.length();
            while(true) {
                if(j >= v5) {
                    if(commentValue > 0x20 || commentValue < -16) {
                        out.append("\t ; 0x").append(Integer.toHexString(commentValue).toUpperCase(Locale.US));
                    }
                    return out.toString();
                }
                int v6 = ArmDis.charAt(asm, v5, j);
                boolean doMaskPc = false;
                boolean doMaskLr = false;
                if(v6 == 37) {
                    ++j;
                    int c = ArmDis.charAt(asm, v5, j);
                alab1:
                    switch(c) {
                        case 37: {
                            out.append('%');
                            break;
                        }
                        case 0x30: 
                        case 49: 
                        case 50: 
                        case 51: 
                        case 52: 
                        case 53: 
                        case 54: 
                        case 55: 
                        case 56: 
                        case 57: {
                            int bitstart = c - 0x30;
                            int bitend = 0;
                            int j = j + 1;
                            int c;
                            for(c = ArmDis.charAt(asm, v5, j); c >= 0x30 && c <= 57; c = ArmDis.charAt(asm, v5, j)) {
                                bitstart = bitstart * 10 + c - 0x30;
                                ++j;
                            }
                            switch(c) {
                                case 39: {
                                    j = j + 1;
                                    int c = ArmDis.charAt(asm, v5, j);
                                    if((1 << bitstart & data) != 0) {
                                        ArmDis.append(out, ((char)c));
                                    }
                                    break alab1;
                                }
                                case 45: {
                                    j = j + 1;
                                    int c;
                                    for(c = ArmDis.charAt(asm, v5, j); c >= 0x30 && c <= 57; c = ArmDis.charAt(asm, v5, j)) {
                                        bitend = bitend * 10 + c - 0x30;
                                        ++j;
                                    }
                                    if(bitend == 0) {
                                        throw new RuntimeException("180 Zero bitend; " + i + "; " + j + "; " + asm);
                                    }
                                    int reg = data >> bitstart & (2 << bitend - bitstart) - 1;
                                    switch(c) {
                                        case 66: {
                                            ArmDis.printAddr(out, addr, ((long)(((1 << bitend ^ reg) - (1 << bitend)) * 2 + 4)));
                                            commentValue = 0;
                                            break;
                                        }
                                        case 97: {
                                            ArmDis.printAddr(out, -4L & addr, ((long)((reg << 2) + 4)));
                                            commentValue = 0;
                                            break;
                                        }
                                        case 99: {
                                            out.append(ArmDis.COND[reg]);
                                            break;
                                        }
                                        case 72: 
                                        case 87: 
                                        case 100: {
                                            if(c == 72) {
                                                reg <<= 1;
                                            }
                                            if(c == 87) {
                                                reg <<= 2;
                                            }
                                            out.append(reg);
                                            commentValue = reg;
                                            break;
                                        }
                                        case 0x72: {
                                            out.append(ArmDis.REG[reg]);
                                            break;
                                        }
                                        case 120: {
                                            out.append("0x");
                                            ToolsLight.prefixIntegerHex(out, 4, reg);
                                            break;
                                        }
                                        default: {
                                            throw new RuntimeException("90 Invalid format char \'" + ((char)c) + "\'; " + i + "; " + j + "; \'" + asm + "\'");
                                        }
                                    }
                                    break alab1;
                                }
                                case 0x3F: {
                                    int c = ArmDis.charAt(asm, v5, j + 1);
                                    if((1 << bitstart & data) == 0) {
                                        j = j + 2;
                                        ArmDis.append(out, ArmDis.charAt(asm, v5, j));
                                    }
                                    else {
                                        ArmDis.append(out, ((char)c));
                                        j = j + 2;
                                    }
                                    break alab1;
                                }
                                default: {
                                    throw new RuntimeException("100 Invalid format char \'" + ((char)c) + "\'; " + i + "; " + j + "; \'" + asm + "\'");
                                }
                            }
                        }
                        case 67: {
                            out.append('S');
                            break;
                        }
                        case 68: {
                            out.append(ArmDis.REG[((data & 0x80) == 0 ? data & 7 : (data & 7) + 8)]);
                            break;
                        }
                        case 73: {
                            for(int tmp = data << 1; (tmp & 15) != 0; tmp <<= 1) {
                                out.append(((char)(((data ^ tmp) & 16) == 0 ? 84 : 69)));
                            }
                            out.append("\t ").append(ArmDis.COND[data >> 4 & 15]);
                            break;
                        }
                        case 77: {
                        label_99:
                            boolean started = false;
                            out.append('{');
                            for(int reg = 0; reg < 8; ++reg) {
                                if((1 << reg & data) != 0) {
                                    if(reg >= 2 && reg <= 12 && (1 << reg - 2 & data) != 0 && (1 << reg - 1 & data) != 0) {
                                        out.setLength(out.length() - 1 - ArmDis.REG[reg - 1].length());
                                        out.append('-');
                                    }
                                    else if(started) {
                                        out.append(',');
                                    }
                                    started = true;
                                    out.append(ArmDis.REG[reg]);
                                }
                            }
                            if(doMaskLr) {
                                if(started) {
                                    out.append(',');
                                }
                                started = true;
                                out.append("LR");
                            }
                            if(doMaskPc) {
                                if(started) {
                                    out.append(',');
                                }
                                out.append("PC");
                            }
                            out.append('}');
                            break;
                        }
                        case 78: {
                            if((data & 0x100) != 0) {
                                doMaskLr = true;
                            }
                            goto label_97;
                        }
                        case 0x4F: {
                        label_97:
                            if(c == 0x4F && (data & 0x100) != 0) {
                                doMaskPc = true;
                            }
                            goto label_99;
                        }
                        case 83: {
                            out.append(ArmDis.REG[((data & 0x40) == 0 ? data >> 3 & 7 : (data >> 3 & 7) + 8)]);
                            break;
                        }
                        case 87: {
                            if((1 << ((data & 0x700) >> 8) & data) == 0) {
                                out.append('!');
                            }
                            break;
                        }
                        case 98: {
                            ArmDis.printAddr(out, addr, ((long)(((data & 0xF8) >> 2) + 4 + ((data & 0x200) >> 3))));
                            break;
                        }
                        case 0x73: {
                            out.append('#').append(((data & 0x7C0) >> 6 == 0 ? 0x20 : (data & 0x7C0) >> 6));
                            break;
                        }
                        case 88: 
                        case 99: 
                        case 120: {
                            break;
                        }
                        default: {
                            throw new RuntimeException("110 Invalid format char \'" + ((char)c) + "\'; " + i + "; " + j + "; \'" + asm + "\'");
                        }
                    }
                }
                else {
                    ArmDis.append(out, ((char)v6));
                }
                ++j;
            }
        }
    }

    static class Thumb32 {
        static final int[] NUM;
        static final String[] STR;

        static {
            Thumb32.NUM = new int[]{0xF3AF8005, -1, 0xF78F8000, -4, 0xE8C00F8F, 0xFFF00FFF, 0xE8C00F9F, 0xFFF00FFF, 0xE8C00FAF, 0xFFF00FFF, 0xE8C00FC0, 0xFFF00FF0, 0xE8C00FD0, 0xFFF00FF0, 0xE8C00FE0, 0xFFF00FF0, 0xE8C000F0, 0xFFF000F0, 0xE8D00F8F, 0xFFF00FFF, 0xE8D00F9F, 0xFFF00FFF, 0xE8D00FAF, 0xFFF00FFF, 0xE8D00FCF, 0xFFF00FFF, 0xE8D00FDF, 0xFFF00FFF, 0xE8D00FEF, 0xFFF00FFF, 0xE8D000FF, 0xFFF000FF, 0xFAC0F080, 0xFFF0F0F0, 0xFAC0F090, 0xFFF0F0F0, 0xFAC0F0A0, 0xFFF0F0F0, 0xFAD0F080, 0xFFF0F0F0, 0xFAD0F090, 0xFFF0F0F0, 0xFAD0F0A0, 0xFFF0F0F0, 0xF910F000, 0xFF70F000, -206602000, -16, 0xF3BF8F51, -13, 0xF3BF8F41, -13, 0xF3BF8F50, -16, 0xF3BF8F40, -16, 0xF3BF8F60, -16, 0xFB90F0F0, 0xFFF0F0F0, 0xFBB0F0F0, 0xFFF0F0F0, 0xF7E08000, 0xFFF0F000, 0xF830F000, 0xFF70F000, 0xF7F08000, 0xFFF0F000, 0xF3AF8000, -1, 0xF3AF8001, -1, 0xF3AF8002, -1, 0xF3AF8003, -1, 0xF3AF8004, -1, 0xF3AF8000, 0xFFFFFF00, 0xF7F0A000, 0xFFF0F000, 0xF3BF8F2F, -1, 0xF3AF8400, 0xFFFFFF1F, -206600704, 0xFFFFFF1F, 0xF3C08F00, 0xFFF0FFFF, 0xE810C000, 0xFFD0FFFF, 0xE990C000, 0xFFD0FFFF, 0xF3E08000, 0xFFE0F000, 0xF3AF8100, 0xFFFFFFE0, 0xE8D0F000, 0xFFF0FFF0, 0xE8D0F010, 0xFFF0FFF0, -206600960, 0xFFFFFF00, 0xF3AF8700, 0xFFFFFF00, 0xF3DE8F00, 0xFFFFFF00, 0xF3808000, 0xFFE0F000, 0xE8500F00, 0xFFF00FFF, 0xE8D00F4F, 0xFFF00FEF, 0xE800C000, 0xFFD0FFE0, 0xE980C000, 0xFFD0FFE0, 0xFA0FF080, 0xFFFFF0C0, 0xFA1FF080, 0xFFFFF0C0, 0xFA2FF080, 0xFFFFF0C0, 0xFA3FF080, 0xFFFFF0C0, 0xFA4FF080, 0xFFFFF0C0, 0xFA5FF080, 0xFFFFF0C0, 0xE8400000, 0xFFF000FF, 0xE8D0007F, 0xFFF000FF, 0xFA80F000, 0xFFF0F0F0, 0xFA80F010, 0xFFF0F0F0, 0xFA80F020, 0xFFF0F0F0, 0xFA80F040, 0xFFF0F0F0, 0xFA80F050, 0xFFF0F0F0, 0xFA80F060, 0xFFF0F0F0, 0xFA80F080, 0xFFF0F0F0, 0xFA80F090, 0xFFF0F0F0, 0xFA80F0A0, 0xFFF0F0F0, 0xFA80F0B0, 0xFFF0F0F0, 0xFA90F000, 0xFFF0F0F0, 0xFA90F010, 0xFFF0F0F0, 0xFA90F020, 0xFFF0F0F0, 0xFA90F040, 0xFFF0F0F0, 0xFA90F050, 0xFFF0F0F0, 0xFA90F060, 0xFFF0F0F0, 0xFA90F080, 0xFFF0F0F0, 0xFA90F090, 0xFFF0F0F0, 0xFA90F0A0, 0xFFF0F0F0, 0xFA90F0B0, 0xFFF0F0F0, 0xFAA0F000, 0xFFF0F0F0, 0xFAA0F010, 0xFFF0F0F0, 0xFAA0F020, 0xFFF0F0F0, 0xFAA0F040, 0xFFF0F0F0, 0xFAA0F050, 0xFFF0F0F0, 0xFAA0F060, 0xFFF0F0F0, 0xFAA0F080, 0xFFF0F0F0, 0xFAB0F080, 0xFFF0F0F0, 0xFAC0F000, 0xFFF0F0F0, 0xFAC0F010, 0xFFF0F0F0, 0xFAC0F020, 0xFFF0F0F0, 0xFAC0F040, 0xFFF0F0F0, 0xFAC0F050, 0xFFF0F0F0, 0xFAC0F060, 0xFFF0F0F0, 0xFAD0F000, 0xFFF0F0F0, 0xFAD0F010, 0xFFF0F0F0, 0xFAD0F020, 0xFFF0F0F0, 0xFAD0F040, 0xFFF0F0F0, 0xFAD0F050, 0xFFF0F0F0, 0xFAD0F060, 0xFFF0F0F0, 0xFAE0F000, 0xFFF0F0F0, 0xFAE0F010, 0xFFF0F0F0, 0xFAE0F020, 0xFFF0F0F0, 0xFAE0F040, 0xFFF0F0F0, 0xFAE0F050, 0xFFF0F0F0, 0xFAE0F060, 0xFFF0F0F0, 0xFB00F000, 0xFFF0F0F0, 0xFB70F000, 0xFFF0F0F0, 0xFA00F000, 0xFFE0F0F0, 0xFA20F000, 0xFFE0F0F0, 0xFA40F000, 0xFFE0F0F0, 0xFA60F000, 0xFFE0F0F0, 0xE8C00F40, 0xFFF00FE0, 0xF3200000, 0xFFF0F0E0, 0xF3A00000, 0xFFF0F0E0, 0xFB20F000, 0xFFF0F0E0, 0xFB30F000, 0xFFF0F0E0, 0xFB40F000, 0xFFF0F0E0, 0xFB50F000, 0xFFF0F0E0, 0xFA00F080, 0xFFF0F0C0, 0xFA10F080, 0xFFF0F0C0, 0xFA20F080, 0xFFF0F0C0, 0xFA30F080, 0xFFF0F0C0, 0xFA40F080, 0xFFF0F0C0, 0xFA50F080, 0xFFF0F0C0, 0xFB10F000, 0xFFF0F0C0, 0xF36F0000, 0xFFFF8020, 0xEA100F00, 0xFFF08F00, 0xEA900F00, 0xFFF08F00, 0xEB100F00, 0xFFF08F00, 0xEBB00F00, 0xFFF08F00, 0xF0100F00, 0xFBF08F00, 0xF0900F00, 0xFBF08F00, 0xF1100F00, 0xFBF08F00, 0xF1B00F00, 0xFBF08F00, 0xEA4F0000, 0xFFEF8000, 0xEA6F0000, 0xFFEF8000, 0xE8C00070, 0xFFF000F0, 0xFB000000, 0xFFF000F0, 0xFB000010, 0xFFF000F0, 0xFB700000, 0xFFF000F0, 0xFB800000, 0xFFF000F0, 0xFBA00000, 0xFFF000F0, 0xFBC00000, 0xFFF000F0, 0xFBE00000, 0xFFF000F0, 0xFBE00060, 0xFFF000F0, 0xE8500F00, 0xFFF00F00, 0xF04F0000, 0xFBEF8000, 0xF06F0000, 0xFBEF8000, 0xF810F000, 0xFF70F000, 0xFB200000, 0xFFF000E0, 0xFB300000, 0xFFF000E0, 0xFB400000, 0xFFF000E0, 0xFB500000, 0xFFF000E0, 0xFB600000, 0xFFF000E0, 0xFBC000C0, 0xFFF000E0, 0xFBD000C0, 0xFFF000E0, 0xEAC00000, 0xFFF08030, 0xEAC00020, 0xFFF08030, 0xF3400000, 0xFFF08020, 0xF3C00000, 0xFFF08020, 0xF8000E00, 0xFF900F00, 0xFB100000, 0xFFF000C0, 0xFBC00080, 0xFFF000C0, 0xF3600000, 0xFFF08020, 0xF8100E00, 0xFE900F00, 0xF3000000, 0xFFD08020, 0xF3800000, 0xFFD08020, 0xF2000000, 0xFBF08000, 0xF2400000, 0xFBF08000, 0xF2A00000, 0xFBF08000, 0xF2C00000, 0xFBF08000, 0xEA000000, 0xFFE08000, 0xEA200000, 0xFFE08000, 0xEA400000, 0xFFE08000, 0xEA600000, 0xFFE08000, 0xEA800000, 0xFFE08000, 0xEB000000, 0xFFE08000, 0xEB400000, 0xFFE08000, 0xEB600000, 0xFFE08000, 0xEBA00000, 0xFFE08000, 0xEBC00000, 0xFFE08000, 0xE8400000, 0xFFF00000, 0xF0000000, 0xFBE08000, 0xF0200000, 0xFBE08000, 0xF0400000, 0xFBE08000, 0xF0600000, 0xFBE08000, 0xF0800000, 0xFBE08000, 0xF1000000, 0xFBE08000, 0xF1400000, 0xFBE08000, 0xF1600000, 0xFBE08000, 0xF1A00000, 0xFBE08000, 0xF1C00000, 0xFBE08000, 0xE8800000, 0xFFD00000, 0xE8900000, 0xFFD00000, 0xE9000000, 0xFFD00000, 0xE9100000, 0xFFD00000, 0xE9C00000, 0xFFD000FF, 0xE9D00000, 0xFFD000FF, 0xE9400000, 0xFF500000, 0xE9500000, 0xFF500000, 0xE8600000, 0xFF700000, 0xE8700000, 0xFF700000, 0xF8000000, 0xFF100000, 0xF8100000, 0xFE100000, 0xF3C08000, 0xFBC0D000, 0xF3808000, 0xFBC0D000, 0xF0008000, 0xF800D000, 0xF0009000, 0xF800D000, 0xF000C000, 0xF800D001, 0xF000D000, 0xF800D000, 0, 0};
            Thumb32.STR = new String[]{"SEVL%c.W", "DCPS%0-1d.W", "STLB%c.W\t %12-15r, [%16-19R]", "STLH%c.W\t %12-15r, [%16-19R]", "STL%c.W\t %12-15r, [%16-19R]", "STLEXB%c.W\t %0-3r, %12-15r, [%16-19R]", "STLEXH%c.W\t %0-3r, %12-15r, [%16-19R]", "STLEX%c.W\t %0-3r, %12-15r, [%16-19R]", "STLEXD%c.W\t %0-3r, %12-15r, %8-11r, [%16-19R]", "LDAB%c.W\t %12-15r, [%16-19R]", "LDAH%c.W\t %12-15r, [%16-19R]", "LDA%c.W\t %12-15r, [%16-19R]", "LDAEXB%c.W\t %12-15r, [%16-19R]", "LDAEXH%c.W\t %12-15r, [%16-19R]", "LDAEX%c.W\t %12-15r, [%16-19R]", "LDAEXD%c.W\t %12-15r, %8-11r, [%16-19R]", "CRC32B%8-11S, %16-19S, %0-3S.W", "CRC32H%9-11S, %16-19S, %0-3S.W", "CRC32W%8-11S, %16-19S, %0-3S.W", "CRC32CB%8-11S, %16-19S, %0-3S.W", "CRC32CH%8-11S, %16-19S, %0-3S.W", "CRC32CW%8-11S, %16-19S, %0-3S.W", "PLI%c.W\t %a", "DBG%c.W\t #%0-3d", "DMB%c.W\t %U", "DSB%c.W\t %U", "DMB%c.W\t %U", "DSB%c.W\t %U", "ISB%c.W\t %U", "SDIV%c.W\t %8-11r, %16-19r, %0-3r", "UDIV%c.W\t %8-11r, %16-19r, %0-3r", "HVC%c.W\t %V", "PLDW%c.W\t %a", "SMC%c.W\t %K", "NOP%c.W", "YIELD%c.W", "WFE%c.W", "WFI%c.W", "SEV%c.W", "NOP%c.W\t {%0-7d}", "UDF%c.W\t %H", "CLREX%c.W", "CPSIE.W%7\'A%6\'I%5\'F%X.W", "CPSID.W%7\'A%6\'I%5\'F%X.W", "BXJ%c.W\t %16-19r%x", "RFEDB%c.W\t %16-19r%21\'!", "RFEIA%c.W\t %16-19r%21\'!", "MRS%c.W\t %8-11r, %D", "CPS#%0-4d%X.W", "TBB%c.W\t [%16-19r,%0-3r]%x", "TBH%c.W\t [%16-19r, %0-3r,LSL #1]%x", "CPSIE%7\'A%6\'I%5\'F, #%0-4d%X.W", "CPSID%7\'A%6\'I%5\'F, #%0-4d%X.W", "SUBS%c.W\t PC, LR, #%0-7d", "MSR%c.W\t %C, %16-19r", "LDREX%c.W\t %12-15r, [%16-19r]", "LDREX%4?HB%c.W\t %12-15r, [%16-19r]", "SRSDB%c.W\t %16-19r%21\'!, #%0-4d", "SRSIA%c.W\t %16-19r%21\'!, #%0-4d", "SXTH%c.W\t %8-11r, %0-3r%R", "UXTH%c.W\t %8-11r, %0-3r%R", "SXTB16%c.W\t %8-11r, %0-3r%R", "UXTB16%c.W\t %8-11r, %0-3r%R", "SXTB%c.W\t %8-11r, %0-3r%R", "UXTB%c.W\t %8-11r, %0-3r%R", "STREX%c.W\t %8-11r, %12-15r, [%16-19r]", "LDREXD%c.W\t %12-15r, %8-11r, [%16-19r]", "SADD8%c.W\t %8-11r, %16-19r, %0-3r", "QADD8%c.W\t %8-11r, %16-19r, %0-3r", "SHADD8%c.W\t %8-11r, %16-19r, %0-3r", "UADD8%c.W\t %8-11r, %16-19r, %0-3r", "UQADD8%c.W\t %8-11r, %16-19r, %0-3r", "UHADD8%c.W\t %8-11r, %16-19r, %0-3r", "QADD%c.W\t %8-11r, %0-3r, %16-19r", "QDADD%c.W\t %8-11r, %0-3r, %16-19r", "QSUB%c.W\t %8-11r, %0-3r, %16-19r", "QDSUB%c.W\t %8-11r, %0-3r, %16-19r", "SADD16%c.W\t %8-11r, %16-19r, %0-3r", "QADD16%c.W\t %8-11r, %16-19r, %0-3r", "SHADD16%c.W\t %8-11r, %16-19r, %0-3r", "UADD16%c.W\t %8-11r, %16-19r, %0-3r", "UQADD16%c.W\t %8-11r, %16-19r, %0-3r", "UHADD16%c.W\t %8-11r, %16-19r, %0-3r", "REV%c.W\t %8-11r, %16-19r", "REV16%c.W\t %8-11r, %16-19r", "RBIT%c.W\t %8-11r, %16-19r", "REVSH%c.W\t %8-11r, %16-19r", "SASX%c.W\t %8-11r, %16-19r, %0-3r", "QASX%c.W\t %8-11r, %16-19r, %0-3r", "SHASX%c.W\t %8-11r, %16-19r, %0-3r", "UASX%c.W\t %8-11r, %16-19r, %0-3r", "UQASX%c.W\t %8-11r, %16-19r, %0-3r", "UHASX%c.W\t %8-11r, %16-19r, %0-3r", "SEL%c.W\t %8-11r, %16-19r, %0-3r", "CLZ%c.W\t %8-11r, %16-19r", "SSUB8%c.W\t %8-11r, %16-19r, %0-3r", "QSUB8%c.W\t %8-11r, %16-19r, %0-3r", "SHSUB8%c.W\t %8-11r, %16-19r, %0-3r", "USUB8%c.W\t %8-11r, %16-19r, %0-3r", "UQSUB8%c.W\t %8-11r, %16-19r, %0-3r", "UHSUB8%c.W\t %8-11r, %16-19r, %0-3r", "SSUB16%c.W\t %8-11r, %16-19r, %0-3r", "QSUB16%c.W\t %8-11r, %16-19r, %0-3r", "SHSUB16%c.W\t %8-11r, %16-19r, %0-3r", "USUB16%c.W\t %8-11r, %16-19r, %0-3r", "UQSUB16%c.W\t %8-11r, %16-19r, %0-3r", "UHSUB16%c.W\t %8-11r, %16-19r, %0-3r", "SSAX%c.W\t %8-11r, %16-19r, %0-3r", "QSAX%c.W\t %8-11r, %16-19r, %0-3r", "SHSAX%c.W\t %8-11r, %16-19r, %0-3r", "USAX%c.W\t %8-11r, %16-19r, %0-3r", "UQSAX%c.W\t %8-11r, %16-19r, %0-3r", "UHSAX%c.W\t %8-11r, %16-19r, %0-3r", "MUL%c.W\t %8-11r, %16-19r, %0-3r", "USAD8%c.W\t %8-11r, %16-19r, %0-3r", "LSL%20\'S%c.W\t %8-11R, %16-19R, %0-3R", "LSR%20\'S%c.W\t %8-11R, %16-19R, %0-3R", "ASR%20\'S%c.W\t %8-11R, %16-19R, %0-3R", "ROR%20\'S%c.W\t %8-11r, %16-19r, %0-3r", "STREX%4?HB%c.W\t %0-3r, %12-15r, [%16-19r]", "SSAT16%c.W\t %8-11r, #%0-4d, %16-19r", "USAT16%c.W\t %8-11r, #%0-4d, %16-19r", "SMUAD%4\'X%c.W\t %8-11r, %16-19r, %0-3r", "SMULW%4?TB%c.W\t %8-11r, %16-19r, %0-3r", "SMUSD%4\'X%c.W\t %8-11r, %16-19r, %0-3r", "SMMUL%4\'R%c.W\t %8-11r, %16-19r, %0-3r", "SXTAH%c.W\t %8-11r, %16-19r, %0-3r%R", "UXTAH%c.W\t %8-11r, %16-19r, %0-3r%R", "SXTAB16%c.W\t %8-11r, %16-19r, %0-3r%R", "UXTAB16%c.W\t %8-11r, %16-19r, %0-3r%R", "SXTAB%c.W\t %8-11r, %16-19r, %0-3r%R", "UXTAB%c.W\t %8-11r, %16-19r, %0-3r%R", "SMUL%5?TB%4?TB%c.W\t %8-11r, %16-19r, %0-3r", "BFC%c.W\t %8-11r, %E", "TST%c.W\t %16-19r, %S", "TEQ%c.W\t %16-19r, %S", "CMN%c.W\t %16-19r, %S", "CMP%c.W\t %16-19r, %S", "TST%c.W\t %16-19r, %M", "TEQ%c.W\t %16-19r, %M", "CMN%c.W\t %16-19r, %M", "CMP%c.W\t %16-19r, %M", "MOV%20\'S%c.W\t %8-11r, %S", "MVN%20\'S%c.W\t %8-11r, %S", "STREXD%c.W\t %0-3r, %12-15r, %8-11r, [%16-19r]", "MLA%c.W\t %8-11r, %16-19r, %0-3r, %12-15r", "MLS%c.W\t %8-11r, %16-19r, %0-3r, %12-15r", "USADA8%c.W\t %8-11R, %16-19R, %0-3R, %12-15R", "SMULL%c.W\t %12-15R, %8-11R, %16-19R, %0-3R", "UMULL%c.W\t %12-15R, %8-11R, %16-19R, %0-3R", "SMLAL%c.W\t %12-15R, %8-11R, %16-19R, %0-3R", "UMLAL%c.W\t %12-15R, %8-11R, %16-19R, %0-3R", "UMAAL%c.W\t %12-15R, %8-11R, %16-19R, %0-3R", "LDREX%c.W\t %12-15r, [%16-19r,#%0-7W]", "MOV%20\'S%c.W\t %8-11r, %M", "MVN%20\'S%c.W\t %8-11r, %M", "PLD%c.W\t %a", "SMLAD%4\'X%c.W\t %8-11R, %16-19R, %0-3R, %12-15R", "SMLAW%4?TB%c.W\t %8-11R, %16-19R, %0-3R, %12-15R", "SMLSD%4\'X%c.W\t %8-11R, %16-19R, %0-3R, %12-15R", "SMMLA%4\'R%c.W\t %8-11R, %16-19R, %0-3R, %12-15R", "SMMLS%4\'R%c.W\t %8-11R, %16-19R, %0-3R, %12-15R", "SMLALD%4\'X%c.W\t %12-15R, %8-11R, %16-19R, %0-3R", "SMLSLD%4\'X%c.W\t %12-15R, %8-11R, %16-19R, %0-3R", "PKHBT%c.W\t %8-11r, %16-19r, %S", "PKHTB%c.W\t %8-11r, %16-19r, %S", "SBFX%c.W\t %8-11r, %16-19r, %F", "UBFX%c.W\t %8-11r, %16-19r, %F", "STR%wt%c.W\t %12-15r, %a", "SMLA%5?TB%4?TB%c.W\t %8-11r, %16-19r, %0-3r, %12-15r", "SMLAL%5?TB%4?TB%c.W\t %12-15r, %8-11r, %16-19r, %0-3r", "BFI%c.W\t %8-11r, %16-19r, %E", "LDR%wt%c.W\t %12-15r, %a", "SSAT%c.W\t %8-11r, #%0-4d, %16-19r%s", "USAT%c.W\t %8-11r, #%0-4d, %16-19r%s", "ADDW%c.W\t %8-11r, %16-19r, %I", "MOVW%c.W\t %8-11r, %J", "SUBW%c.W\t %8-11r, %16-19r, %I", "MOVT%c.W\t %8-11r, %J", "AND%20\'S%c.W\t %8-11r, %16-19r, %S", "BIC%20\'S%c.W\t %8-11r, %16-19r, %S", "ORR%20\'S%c.W\t %8-11r, %16-19r, %S", "ORN%20\'S%c.W\t %8-11r, %16-19r, %S", "EOR%20\'S%c.W\t %8-11r, %16-19r, %S", "ADD%20\'S%c.W\t %8-11r, %16-19r, %S", "ADC%20\'S%c.W\t %8-11r, %16-19r, %S", "SBC%20\'S%c.W\t %8-11r, %16-19r, %S", "SUB%20\'S%c.W\t %8-11r, %16-19r, %S", "RSB%20\'S%c.W\t %8-11r, %16-19r, %S", "STREX%c.W\t %8-11r, %12-15r, [%16-19r,#%0-7W]", "AND%20\'S%c.W\t %8-11r, %16-19r, %M", "BIC%20\'S%c.W\t %8-11r, %16-19r, %M", "ORR%20\'S%c.W\t %8-11r, %16-19r, %M", "ORN%20\'S%c.W\t %8-11r, %16-19r, %M", "EOR%20\'S%c.W\t %8-11r, %16-19r, %M", "ADD%20\'S%c.W\t %8-11r, %16-19r, %M", "ADC%20\'S%c.W\t %8-11r, %16-19r, %M", "SBC%20\'S%c.W\t %8-11r, %16-19r, %M", "SUB%20\'S%c.W\t %8-11r, %16-19r, %M", "RSB%20\'S%c.W\t %8-11r, %16-19r, %M", "STMIA%c.W\t %16-19r%21\'!, %m", "LDMIA%c.W\t %16-19r%21\'!, %m", "STMDB%c.W\t %16-19r%21\'!, %m", "LDMDB%c.W\t %16-19r%21\'!, %m", "STRD%c.W\t %12-15r, %8-11r, [%16-19r]", "LDRD%c.W\t %12-15r, %8-11r, [%16-19r]", "STRD%c.W\t %12-15r, %8-11r, [%16-19r,#%23`-%0-7W]%21\'!%L", "LDRD%c.W\t %12-15r, %8-11r, [%16-19r,#%23`-%0-7W]%21\'!%L", "STRD%c.W\t %12-15r, %8-11r, [%16-19r], #%23`-%0-7W%L", "LDRD%c.W\t %12-15r, %8-11r, [%16-19r], #%23`-%0-7W%L", "STR%w%c.W\t %12-15r, %a", "LDR%w%c.W\t %12-15r, %a", "UNDEFINED(BCC, COND=0xF).W", "UNDEFINED(BCC, COND=0xE).W", "B%22-25c.W\t %b%X", "B%c.W\t %B%x", "BLX%c.W\t %B%x", "BL%c.W\t %B%x", "\t \t ; <UNDEFINED> instruction: %0-31x.W"};
        }

        static int getOpcode(ArmDis ret, long addr, String data, int dbg) {
            return -1;
        }

        static String getOpcode(ArmDis ret, long addr, int data) {
            int offset;
            int imm;
            String coproc = Coproc.getOpcode(ret, addr, data, true);
            if(coproc != null) {
                return coproc;
            }
            String s1 = Neon.getOpcode(ret, addr, data, true);
            if(s1 != null) {
                return s1;
            }
            int i;
            for(i = 0; true; ++i) {
                if(i >= Thumb32.STR.length) {
                    throw new RuntimeException("200 No Thumb32 match for " + Integer.toHexString(data).toUpperCase(Locale.US) + "; " + Integer.toHexString(Integer.reverseBytes(data)).toUpperCase(Locale.US));
                }
                if((Thumb32.NUM[i * 2 + 1] & data) == Thumb32.NUM[i * 2]) {
                    break;
                }
            }
            boolean WRITEBACK_BIT_SET = (0x200000 & data) != 0;
            boolean NEGATIVE_BIT_SET = (0x800000 & data) == 0;
            StringBuilder out = new StringBuilder();
            boolean isUnpredictable = false;
            int commentValue = 0;
            String asm = Thumb32.STR[i];
            int j = 0;
            int v5 = asm.length();
            while(true) {
                if(j >= v5) {
                    if(commentValue > 0x20 || commentValue < -16) {
                        out.append("\t ; 0x").append(Integer.toHexString(commentValue).toUpperCase(Locale.US));
                    }
                    if(isUnpredictable) {
                        out.append("\t ; <UNPREDICTABLE>");
                    }
                    return out.toString();
                }
                int v6 = ArmDis.charAt(asm, v5, j);
                if(v6 == 37) {
                    ++j;
                    int c = ArmDis.charAt(asm, v5, j);
                alab1:
                    switch(c) {
                        case 37: {
                            out.append('%');
                            break;
                        }
                        case 0x30: 
                        case 49: 
                        case 50: 
                        case 51: 
                        case 52: 
                        case 53: 
                        case 54: 
                        case 55: 
                        case 56: 
                        case 57: {
                            j = ArmDis.decodeBitfield(asm, v5, j, data, ret);
                            int v8 = ret.valuep;
                            int v9 = ret.widthp;
                            int c = ArmDis.charAt(asm, v5, j);
                            switch(c) {
                                case 39: {
                                    ++j;
                                    int c = ArmDis.charAt(asm, v5, j);
                                    if(v8 == (1 << v9) - 1) {
                                        ArmDis.append(out, ((char)c));
                                    }
                                    break alab1;
                                }
                                case 0x3F: {
                                    ArmDis.append(out, ArmDis.charAt(asm, v5, (1 << v9) + j - v8));
                                    j += 1 << v9;
                                    break alab1;
                                }
                                case 82: {
                                label_52:
                                    if(v8 == 15) {
                                        isUnpredictable = true;
                                    }
                                    out.append(ArmDis.REG[v8]);
                                    break alab1;
                                }
                                case 83: {
                                    if(v8 == 13) {
                                        isUnpredictable = true;
                                    }
                                    goto label_52;
                                }
                                case 87: {
                                    out.append(v8 * 4);
                                    commentValue = v8 * 4;
                                    break alab1;
                                }
                                case 0x60: {
                                    ++j;
                                    int c = ArmDis.charAt(asm, v5, j);
                                    if(v8 == 0) {
                                        ArmDis.append(out, ((char)c));
                                    }
                                    break alab1;
                                }
                                case 99: {
                                    out.append(ArmDis.COND[v8]);
                                    break alab1;
                                }
                                case 100: {
                                    out.append(v8);
                                    commentValue = v8;
                                    break alab1;
                                }
                                case 0x72: {
                                    out.append(ArmDis.REG[v8]);
                                    break alab1;
                                }
                                case 120: {
                                    out.append("0x").append(Integer.toHexString(v8).toUpperCase(Locale.US));
                                    break alab1;
                                }
                                default: {
                                    throw new RuntimeException("120 Invalid format char \'" + ((char)c) + "\'; " + i + "; " + j + "; \'" + asm + "\'");
                                }
                            }
                        }
                        case 65: {
                            out.append('[').append(ArmDis.REG[(0xF0000 & data) >> 16]);
                            if((0x1000000 & data) == 0) {
                                out.append("], ");
                                if(WRITEBACK_BIT_SET) {
                                    out.append('#').append(((char)(NEGATIVE_BIT_SET ? 45 : 43))).append((data & 0xFF) * 4);
                                    commentValue = (data & 0xFF) * 4 == 0 || NEGATIVE_BIT_SET ? -1 : 1;
                                }
                                else {
                                    out.append('{').append(data & 0xFF).append('}');
                                    commentValue = data & 0xFF;
                                }
                            }
                            else {
                                if((data & 0xFF) != 0 || NEGATIVE_BIT_SET) {
                                    out.append(", #").append(((char)(NEGATIVE_BIT_SET ? 45 : 43))).append((data & 0xFF) * 4);
                                    commentValue = (data & 0xFF) * 4 == 0 || NEGATIVE_BIT_SET ? -1 : 1;
                                }
                                out.append(']');
                                if(WRITEBACK_BIT_SET) {
                                    out.append('!');
                                }
                            }
                            break;
                        }
                        case 66: {
                            int S = (0x4000000 & data) >> 26;
                            ArmDis.printAddr(out, addr, ((data & 0x1000) == 0 ? (((long)(1 - S << 24)) | ((long)(1 - ((data & 0x2000) >> 13 ^ S) << 23)) | ((long)(1 - ((data & 0x800) >> 11 ^ S) << 22)) | ((long)((0x3FF0000 & data) >> 4)) | ((long)((data & 0x7FF) << 1))) - 0x1000000L + (addr + 4L) & -3L : (((long)(1 - S << 24)) | ((long)(1 - ((data & 0x2000) >> 13 ^ S) << 23)) | ((long)(1 - ((data & 0x800) >> 11 ^ S) << 22)) | ((long)((0x3FF0000 & data) >> 4)) | ((long)((data & 0x7FF) << 1))) - 0x1000000L + (addr + 4L)) - addr);
                            break;
                        }
                        case 67: {
                            if((data & 0xFF) == 0) {
                                out.append(((char)((0x100000 & data) == 0 ? 67 : 83))).append("PSR_");
                                if((data & 0x800) != 0) {
                                    out.append('f');
                                }
                                if((data & 0x400) != 0) {
                                    out.append('s');
                                }
                                if((data & 0x200) != 0) {
                                    out.append('x');
                                }
                                if((data & 0x100) != 0) {
                                    out.append('c');
                                }
                            }
                            else if((data & 0x20) == 0x20) {
                                ArmDis.bankedRegname(out, (data & 0xF00) >> 8 | data & 0x30 | (0x100000 & data) >> 14);
                            }
                            else {
                                out.append(Thumb32.psrName(data & 0xFF));
                            }
                            break;
                        }
                        case 68: {
                            if((data & 0xFF) != 0 && (data & 0x20) != 0x20) {
                                out.append(Thumb32.psrName(data & 0xFF));
                            }
                            else {
                                ArmDis.bankedRegname(out, (0xF0000 & data) >> 16 | data & 0x30 | (0x100000 & data) >> 14);
                            }
                            break;
                        }
                        case 69: {
                            int lsb = (data & 0xC0) >> 6 | (data & 0x7000) >> 10;
                            out.append('#').append(lsb).append(", #").append((data & 0x1F) - lsb + 1);
                            break;
                        }
                        case 70: {
                            out.append('#').append((data & 0xC0) >> 6 | (data & 0x7000) >> 10).append(", #").append((data & 0x1F) + 1);
                            break;
                        }
                        case 72: {
                            int imm = (0xF0000 & data) >> 4 | data & 0xFFF;
                            out.append('#').append(imm);
                            commentValue = imm;
                            break;
                        }
                        case 73: {
                            int imm12 = data & 0xFF | (data & 0x7000) >> 4 | (0x4000000 & data) >> 15;
                            out.append('#').append(imm12);
                            commentValue = imm12;
                            break;
                        }
                        case 74: {
                            int imm = data & 0xFF | (data & 0x7000) >> 4 | (0x4000000 & data) >> 15 | (0xF0000 & data) >> 4;
                            out.append('#').append(imm);
                            commentValue = imm;
                            break;
                        }
                        case 75: {
                            int imm = (0xF0000 & data) >> 16 | data & 0xFF0 | (data & 15) << 12;
                            out.append('#').append(imm);
                            commentValue = imm;
                            break;
                        }
                        case 76: {
                            if((data >> 16 & 15) == 15) {
                                out.append("\t ; ");
                                ArmDis.printAddr(out, addr, (-4L & addr) + 4L + ((long)((0x800000 & data) == 0 ? -((data & 0xFF) * 4) : (data & 0xFF) * 4)) - addr);
                            }
                            break;
                        }
                        case 77: {
                            int bits = data & 0xFF | (data & 0x7000) >> 4 | (0x4000000 & data) >> 15;
                            int imm8 = bits & 0xFF;
                            switch((bits & 0xF00) >> 8) {
                                case 0: {
                                    imm = imm8;
                                    break;
                                }
                                case 1: {
                                    imm = imm8 << 16 | imm8;
                                    break;
                                }
                                case 2: {
                                    imm = imm8 << 24 | imm8 << 8;
                                    break;
                                }
                                case 3: {
                                    imm = imm8 << 24 | imm8 << 16 | imm8 << 8 | imm8;
                                    break;
                                }
                                default: {
                                    int mod = (bits & 0xF80) >> 7;
                                    int imm8 = bits & 0x7F | 0x80;
                                    imm = imm8 << 0x20 - mod | imm8 >> mod;
                                }
                            }
                            out.append('#').append(imm);
                            commentValue = imm;
                            break;
                        }
                        case 82: {
                            int rot = (data & 0x30) >> 4;
                            if(rot != 0) {
                                out.append(", ROR #").append(rot * 8);
                            }
                            break;
                        }
                        case 83: {
                            int imm = (data & 0xC0) >> 6 | (data & 0x7000) >> 10;
                            out.append(ArmDis.REG[data & 15]);
                            switch((data & 0x30) >> 4) {
                                case 0: {
                                    if(imm > 0) {
                                        out.append(", LSL #").append(imm);
                                    }
                                    break alab1;
                                }
                                case 1: {
                                    if(imm == 0) {
                                        imm = 0x20;
                                    }
                                    out.append(", LSR #").append(imm);
                                    break alab1;
                                }
                                case 2: {
                                    if(imm == 0) {
                                        imm = 0x20;
                                    }
                                    out.append(", ASR #").append(imm);
                                    break alab1;
                                }
                                case 3: {
                                    if(imm == 0) {
                                        out.append(", RRX");
                                    }
                                    else {
                                        out.append(", ROR #").append(imm);
                                    }
                                    break alab1;
                                }
                                default: {
                                    break alab1;
                                }
                            }
                        }
                        case 85: {
                            if((data & 0xF0) != 0x60) {
                                ArmDis.dataBarrierOption(out, data & 15);
                            }
                            else if((data & 15) == 15) {
                                out.append("sy");
                            }
                            else {
                                out.append('#').append(data & 15);
                            }
                            break;
                        }
                        case 86: {
                            int imm = data & 0xFFF | (0xF0000 & data) >> 4;
                            out.append('#').append(imm);
                            commentValue = imm;
                            break;
                        }
                        case 97: {
                            int Rn = (0xF0000 & data) >> 16;
                            int op = (data & 0xF00) >> 8;
                            boolean writeback = false;
                            boolean postind = false;
                            out.append('[').append(ArmDis.REG[Rn]);
                            if(!NEGATIVE_BIT_SET) {
                                offset = data & 0xFFF;
                                if(Rn != 15) {
                                    commentValue = offset;
                                }
                            }
                            else if(Rn == 15) {
                                offset = -(data & 0xFFF);
                            }
                            else if(op == 0) {
                                int sh = (data & 0x30) >> 4;
                                out.append(", ").append(ArmDis.REG[data & 15]);
                                if(sh != 0) {
                                    out.append(", LSL #").append(sh);
                                }
                                out.append(']');
                                break;
                            }
                            else {
                                switch(op) {
                                    case 9: {
                                        offset = -(data & 0xFF);
                                        postind = true;
                                        break;
                                    }
                                    case 11: {
                                        offset = data & 0xFF;
                                        postind = true;
                                        break;
                                    }
                                    case 12: {
                                        offset = -(data & 0xFF);
                                        break;
                                    }
                                    case 13: {
                                        offset = -(data & 0xFF);
                                        writeback = true;
                                        break;
                                    }
                                    case 14: {
                                        offset = data & 0xFF;
                                        break;
                                    }
                                    case 15: {
                                        offset = data & 0xFF;
                                        writeback = true;
                                        break;
                                    }
                                    default: {
                                        out.append(", <undefined>]");
                                        break alab1;
                                    }
                                }
                            }
                            if(postind) {
                                out.append("], #").append(offset);
                            }
                            else {
                                if(offset != 0) {
                                    out.append(", #").append(offset);
                                }
                                out.append(']');
                                if(writeback) {
                                    out.append('!');
                                }
                            }
                            if(Rn == 15) {
                                out.append("\t ; ");
                                ArmDis.printAddr(out, addr, (addr + 4L & -4L) + ((long)offset) - addr);
                            }
                            break;
                        }
                        case 98: {
                            ArmDis.printAddr(out, addr, ((long)((1 - ((0x4000000 & data) >> 26) << 20 | (data & 0x800) >> 11 << 19 | (data & 0x2000) >> 13 << 18 | (0x3F0000 & data) >> 4 | (data & 0x7FF) << 1) - 0xFFFFC)));
                            break;
                        }
                        case 109: {
                            boolean started = false;
                            out.append('{');
                            for(int reg = 0; reg < 16; ++reg) {
                                if((1 << reg & data) != 0) {
                                    if(reg >= 2 && reg <= 12 && (1 << reg - 2 & data) != 0 && (1 << reg - 1 & data) != 0) {
                                        out.setLength(out.length() - 1 - ArmDis.REG[reg - 1].length());
                                        out.append('-');
                                    }
                                    else if(started) {
                                        out.append(',');
                                    }
                                    started = true;
                                    out.append(ArmDis.REG[reg]);
                                }
                            }
                            out.append('}');
                            break;
                        }
                        case 0x73: {
                            int shift = (data & 0xC0) >> 6 | (data & 0x7000) >> 10;
                            if(WRITEBACK_BIT_SET) {
                                out.append(", ASR #").append(shift);
                            }
                            else if(shift != 0) {
                                out.append(", LSL #").append(shift);
                            }
                            break;
                        }
                        case 0x77: {
                            boolean Sbit = (0x1000000 & data) != 0;
                            switch((0x600000 & data) >> 21) {
                                case 0: {
                                    if(Sbit) {
                                        out.append('s');
                                    }
                                    out.append('b');
                                    break alab1;
                                }
                                case 1: {
                                    if(Sbit) {
                                        out.append('s');
                                    }
                                    out.append('h');
                                    break alab1;
                                }
                                case 2: {
                                    if(Sbit) {
                                        out.append("??");
                                    }
                                    break alab1;
                                }
                                case 3: {
                                    out.append("??");
                                    break alab1;
                                }
                                default: {
                                    break alab1;
                                }
                            }
                        }
                        case 88: 
                        case 99: 
                        case 120: {
                            break;
                        }
                        default: {
                            throw new RuntimeException("130 Invalid format char \'" + ((char)c) + "\'; " + i + "; " + j + "; \'" + asm + "\'");
                        }
                    }
                }
                else {
                    ArmDis.append(out, ((char)v6));
                }
                ++j;
            }
        }

        static String psrName(int regno) {
            switch(regno) {
                case 0: {
                    return "APSR";
                }
                case 1: {
                    return "IAPSR";
                }
                case 2: {
                    return "EAPSR";
                }
                case 3: {
                    return "PSR";
                }
                case 5: {
                    return "IPSR";
                }
                case 6: {
                    return "EPSR";
                }
                case 7: {
                    return "IEPSR";
                }
                case 8: {
                    return "MSP";
                }
                case 9: {
                    return "PSP";
                }
                case 16: {
                    return "PRIMASK";
                }
                case 17: {
                    return "BASEPRI";
                }
                case 18: {
                    return "BASEPRI_MAX";
                }
                case 19: {
                    return "FAULTMASK";
                }
                case 20: {
                    return "CONTROL";
                }
                default: {
                    return "<unknown>";
                }
            }
        }
    }

    static final String[] COND = null;
    static final String[] FP = null;
    static final int I_BIT = 22;
    static final int P_BIT = 24;
    static final String[] REG = null;
    static final String[] SHIFT = null;
    static final String UNDEFINED_INSTRUCTION = "\t \t ; <UNDEFINED> instruction: %0-31x";
    static final String UNPREDICTABLE_INSTRUCTION = "\t ; <UNPREDICTABLE>";
    static final int U_BIT = 23;
    static final int W_BIT = 21;
    Input input;
    static final NextAsm nextAsm;
    private int valuep;
    private int widthp;

    static {
        ArmDis.COND = new String[]{"EQ", "NE", "CS", "CC", "MI", "PL", "VS", "VC", "HI", "LS", "GE", "LT", "GT", "LE", "AL", "<UND>", ""};
        ArmDis.REG = new String[]{"R0", "R1", "R2", "R3", "R4", "R5", "R6", "R7", "R8", "R9", "R10", "R11", "R12", "SP", "LR", "PC"};
        ArmDis.FP = new String[]{"0.0", "1.0", "2.0", "3.0", "4.0", "5.0", "0.5", "10.0"};
        ArmDis.SHIFT = new String[]{"LSL", "LSR", "ASR", "ROR"};
        ArmDis.nextAsm = new NextAsm();
    }

    static final void append(StringBuilder out, char c) {
        if(c != 0) {
            out.append(c);
        }
    }

    static int bankedRegname(Input in) {
        if(in.check("CPSR")) {
            return 15;
        }
        if(in.check("R8_USR")) {
            return 0x20;
        }
        if(in.check("R9_USR")) {
            return 33;
        }
        if(in.check("R10_USR")) {
            return 34;
        }
        if(in.check("R11_USR")) {
            return 35;
        }
        if(in.check("R12_USR")) {
            return 36;
        }
        if(in.check("SP_USR")) {
            return 37;
        }
        if(in.check("LR_USR")) {
            return 38;
        }
        if(in.check("R8_FIQ")) {
            return 40;
        }
        if(in.check("R9_FIQ")) {
            return 41;
        }
        if(in.check("R10_FIQ")) {
            return 42;
        }
        if(in.check("R11_FIQ")) {
            return 43;
        }
        if(in.check("R12_FIQ")) {
            return 44;
        }
        if(in.check("SP_FIQ")) {
            return 45;
        }
        if(in.check("LR_FIQ")) {
            return 46;
        }
        if(in.check("LR_IRQ")) {
            return 0x30;
        }
        if(in.check("SP_IRQ")) {
            return 49;
        }
        if(in.check("LR_SVC")) {
            return 50;
        }
        if(in.check("SP_SVC")) {
            return 51;
        }
        if(in.check("LR_ABT")) {
            return 52;
        }
        if(in.check("SP_ABT")) {
            return 53;
        }
        if(in.check("LR_UND")) {
            return 54;
        }
        if(in.check("SP_UND")) {
            return 55;
        }
        if(in.check("LR_MON")) {
            return 60;
        }
        if(in.check("SP_MON")) {
            return 61;
        }
        if(in.check("ELR_HYP")) {
            return 62;
        }
        if(in.check("SP_HYP")) {
            return 0x3F;
        }
        if(in.check("SPSR_FIQ")) {
            return 110;
        }
        if(in.check("SPSR_IRQ")) {
            return 0x70;
        }
        if(in.check("SPSR_SVC")) {
            return 0x72;
        }
        if(in.check("SPSR_ABT")) {
            return 0x74;
        }
        if(in.check("SPSR_UND")) {
            return 0x76;
        }
        if(in.check("SPSR_MON")) {
            return 0x7C;
        }
        if(in.check("SPSR_HYP")) {
            return 0x7E;
        }
        if(in.check("SPSR")) {
            return 0x4F;
        }
        if(!in.check("(UNDEF: ")) {
            return -1;
        }
        int v = in.getInt();
        if((v & 0xFFFFFF80) != 0) {
            throw ArmDis.nextAsm;
        }
        if(!in.check(')')) {
            throw ArmDis.nextAsm;
        }
        return v;
    }

    static void bankedRegname(StringBuilder out, int reg) {
        String name;
        switch(reg) {
            case 15: {
                name = "CPSR";
                break;
            }
            case 0x20: {
                name = "R8_usr";
                break;
            }
            case 33: {
                name = "R9_usr";
                break;
            }
            case 34: {
                name = "R10_usr";
                break;
            }
            case 35: {
                name = "R11_usr";
                break;
            }
            case 36: {
                name = "R12_usr";
                break;
            }
            case 37: {
                name = "SP_usr";
                break;
            }
            case 38: {
                name = "LR_usr";
                break;
            }
            case 40: {
                name = "R8_fiq";
                break;
            }
            case 41: {
                name = "R9_fiq";
                break;
            }
            case 42: {
                name = "R10_fiq";
                break;
            }
            case 43: {
                name = "R11_fiq";
                break;
            }
            case 44: {
                name = "R12_fiq";
                break;
            }
            case 45: {
                name = "SP_fiq";
                break;
            }
            case 46: {
                name = "LR_fiq";
                break;
            }
            case 0x30: {
                name = "LR_irq";
                break;
            }
            case 49: {
                name = "SP_irq";
                break;
            }
            case 50: {
                name = "LR_svc";
                break;
            }
            case 51: {
                name = "SP_svc";
                break;
            }
            case 52: {
                name = "LR_abt";
                break;
            }
            case 53: {
                name = "SP_abt";
                break;
            }
            case 54: {
                name = "LR_und";
                break;
            }
            case 55: {
                name = "SP_und";
                break;
            }
            case 60: {
                name = "LR_mon";
                break;
            }
            case 61: {
                name = "SP_mon";
                break;
            }
            case 62: {
                name = "ELR_hyp";
                break;
            }
            case 0x3F: {
                name = "SP_hyp";
                break;
            }
            case 0x4F: {
                name = "SPSR";
                break;
            }
            case 110: {
                name = "SPSR_fiq";
                break;
            }
            case 0x70: {
                name = "SPSR_irq";
                break;
            }
            case 0x72: {
                name = "SPSR_svc";
                break;
            }
            case 0x74: {
                name = "SPSR_abt";
                break;
            }
            case 0x76: {
                name = "SPSR_und";
                break;
            }
            case 0x7C: {
                name = "SPSR_mon";
                break;
            }
            case 0x7E: {
                name = "SPSR_hyp";
                break;
            }
            default: {
                out.append("(UNDEF: ").append(reg).append(')');
                return;
            }
        }
        out.append(name);
    }

    static final char charAt(String str, int sj, int j) {
        return j >= sj ? '\u0000' : str.charAt(j);
    }

    static int dataBarrierOption(Input in) {
        if(in.check("OSHLD")) {
            return 1;
        }
        if(in.check("OSHST")) {
            return 2;
        }
        if(in.check("OSH")) {
            return 3;
        }
        if(in.check("NSHLD")) {
            return 5;
        }
        if(in.check("UNST")) {
            return 6;
        }
        if(in.check("UN")) {
            return 7;
        }
        if(in.check("ISHLD")) {
            return 9;
        }
        if(in.check("ISHST")) {
            return 10;
        }
        if(in.check("ISH")) {
            return 11;
        }
        if(in.check("LD")) {
            return 13;
        }
        if(in.check("ST")) {
            return 14;
        }
        if(in.check("SY")) {
            return 15;
        }
        if(!in.check('#')) {
            return -1;
        }
        int v = in.getInt();
        if((v & -16) != 0) {
            throw ArmDis.nextAsm;
        }
        return v;
    }

    static void dataBarrierOption(StringBuilder out, int option) {
        String name;
        switch(option & 15) {
            case 1: {
                name = "oshld";
                break;
            }
            case 2: {
                name = "oshst";
                break;
            }
            case 3: {
                name = "osh";
                break;
            }
            case 5: {
                name = "nshld";
                break;
            }
            case 6: {
                name = "unst";
                break;
            }
            case 7: {
                name = "un";
                break;
            }
            case 9: {
                name = "ishld";
                break;
            }
            case 10: {
                name = "ishst";
                break;
            }
            case 11: {
                name = "ish";
                break;
            }
            case 13: {
                name = "ld";
                break;
            }
            case 14: {
                name = "st";
                break;
            }
            case 15: {
                name = "sy";
                break;
            }
            default: {
                out.append('#').append(option & 15);
                return;
            }
        }
        out.append(name);
    }

    static int decodeBitfield(String asm, int sj, int j, int data, ArmDis ret) {
        int end;
        int value = 0;
        int width = 0;
        do {
            int c = ArmDis.charAt(asm, sj, j);
            int start = 0;
            while(c >= 0x30 && c <= 57) {
                start = start * 10 + c - 0x30;
                ++j;
                c = ArmDis.charAt(asm, sj, j);
            }
            if(c == 45) {
                ++j;
                c = ArmDis.charAt(asm, sj, j);
                end = 0;
                while(c >= 0x30 && c <= 57) {
                    end = end * 10 + c - 0x30;
                    ++j;
                    c = ArmDis.charAt(asm, sj, j);
                }
            }
            else {
                end = start;
            }
            int bits = end - start;
            if(bits < 0) {
                throw new RuntimeException("140 Bits is negative: " + bits + "; " + start + "; " + end + "; \'" + asm + "\'; " + j + "; " + sj + "; 0x" + Integer.toHexString(data));
            }
            value |= (data >> start & (2 << bits) - 1) << width;
            width += bits + 1;
            ++j;
        }
        while(c == 44);
        ret.valuep = value;
        ret.widthp = width;
        return j - 1;
    }

    static int decodeBitfield(String asm, int sj, int j, ArmDis ret, int value) {
        int end;
        int width = 0;
        int out = ret.valuep;
        do {
            int c = ArmDis.charAt(asm, sj, j);
            int start = 0;
            while(c >= 0x30 && c <= 57) {
                start = start * 10 + c - 0x30;
                ++j;
                c = ArmDis.charAt(asm, sj, j);
            }
            if(c == 45) {
                ++j;
                c = ArmDis.charAt(asm, sj, j);
                end = 0;
                while(c >= 0x30 && c <= 57) {
                    end = end * 10 + c - 0x30;
                    ++j;
                    c = ArmDis.charAt(asm, sj, j);
                }
            }
            else {
                end = start;
            }
            int bits = end - start;
            if(bits < 0) {
                throw new RuntimeException("141 Bits is negative: " + bits + "; " + start + "; " + end + "; \'" + asm + "\'; " + j + "; " + sj + "; 0x" + Integer.toHexString(value));
            }
            int mask = (2 << bits) - 1;
            int val = (value >> width & mask) << start;
            out |= val;
            if((out & (-1 >> width & mask) << start) != val) {
                throw ArmDis.nextAsm;
            }
            width += bits + 1;
            ++j;
        }
        while(c == 44);
        ret.valuep = out;
        ret.widthp = width;
        return j - 1;
    }

    static int decodeShift(int out, Input in, boolean printShift) {
        int v1 = in.index(ArmDis.REG);
        if(v1 == -1) {
            throw ArmDis.nextAsm;
        }
        int v2 = out | v1;
        if(in.check(", ")) {
            if(in.check("RRX")) {
                return v2 | 0x60;
            }
            if(printShift) {
                int v3 = in.index(ArmDis.SHIFT);
                if(v3 == -1) {
                    throw ArmDis.nextAsm;
                }
                v2 |= v3 << 5;
                if(!in.check(' ')) {
                    throw ArmDis.nextAsm;
                }
            }
            if(in.check('#')) {
                int amount = in.getInt();
                if(amount < 0 || amount > 0x20) {
                    throw ArmDis.nextAsm;
                }
                if(amount == 0x20) {
                    amount = 0;
                }
                return v2 | amount << 7;
            }
            int reg = in.index(ArmDis.REG);
            if(reg == -1) {
                throw ArmDis.nextAsm;
            }
            return v2 | 16 | reg << 8;
        }
        return v2;
    }

    static void decodeShift(StringBuilder out, int data, boolean printShift) {
        out.append(ArmDis.REG[data & 15]);
        if((data & 0xFF0) != 0) {
            if((data & 16) == 0) {
                int amount = (data & 0xF80) >> 7;
                int shift = (data & 0x60) >> 5;
                if(amount == 0) {
                    if(shift == 3) {
                        out.append(", RRX");
                        return;
                    }
                    amount = 0x20;
                }
                out.append(", ");
                if(printShift) {
                    out.append(ArmDis.SHIFT[shift]).append(' ');
                }
                out.append('#').append(amount);
                return;
            }
            if((data & 0x80) == 0x80) {
                out.append(" <illegal shifter operand>");
                return;
            }
            out.append(", ");
            if(printShift) {
                out.append(ArmDis.SHIFT[(data & 0x60) >> 5]).append(' ');
            }
            out.append(ArmDis.REG[(data & 0xF00) >> 8]);
        }
    }

    public static int getArmOpcode(ArmDis ret, long addr, String data) {
        if(ret == null) {
            ret = new ArmDis();
        }
        int v1 = Arm.getOpcode(ret, addr, data, -1);
        if(v1 == -1) {
            String suggestion = ArmDis.getArmOpcode(ret, addr, ((long)ret.input.bestOut)).trim();
            AsmFailedException armDis$AsmFailedException0 = new AsmFailedException(Tools.stringFormat(Re.s(0x7F07034B), new Object[]{"ARM"}).trim() + "\n\'" + data + "\'\n\n" + Tools.stringFormat(Re.s(0x7F07034C), new Object[]{"\n\'" + suggestion + "\'\n"}).trim());  // string:asm_failed "Failed to recognize __s__ opcode:"
            int v2 = suggestion.indexOf(59);
            if(v2 != -1) {
                suggestion = suggestion.substring(0, v2).trim();
            }
            armDis$AsmFailedException0.suggestion = suggestion;
            armDis$AsmFailedException0.data = data;
            throw armDis$AsmFailedException0;
        }
        return v1;
    }

    public static String getArmOpcode(ArmDis ret, long addr, long data) {
        try {
            if(ret == null) {
                ret = new ArmDis();
            }
            return Arm.getOpcode(ret, addr, ((int)data));
        }
        catch(Throwable e) {
            Log.d("Failed get OP 2", e);
            return e.toString().replace("java.lang.RuntimeException:", "RE:");
        }
    }

    public static int getThumbOpcode(ArmDis ret, long addr, String data) {
        if(ret == null) {
            ret = new ArmDis();
        }
        int v1 = Thumb16.getOpcode(ret, addr, data, -1);
        if(v1 == -1) {
            String suggestion = ArmDis.getThumbOpcode(ret, addr, ((long)ret.input.bestOut)).trim();
            AsmFailedException armDis$AsmFailedException0 = new AsmFailedException(Tools.stringFormat(Re.s(0x7F07034B), new Object[]{"Thumb"}).trim() + "\n\'" + data + "\'\n\n" + Tools.stringFormat(Re.s(0x7F07034C), new Object[]{"\n\'" + suggestion + "\'\n"}).trim());  // string:asm_failed "Failed to recognize __s__ opcode:"
            int v2 = suggestion.indexOf(59);
            if(v2 != -1) {
                suggestion = suggestion.substring(0, v2).trim();
            }
            armDis$AsmFailedException0.suggestion = suggestion;
            armDis$AsmFailedException0.data = data;
            throw armDis$AsmFailedException0;
        }
        return 0xFFFF & v1;
    }

    public static String getThumbOpcode(ArmDis ret, long addr, long data) {
        try {
            if((0xF800L & data) == 0xF800L || (0xF800L & data) == 0xF000L || (0xF800L & data) == 0xE800L) {
                if(ret == null) {
                    ret = new ArmDis();
                }
                return Thumb32.getOpcode(ret, addr, ((int)((0xFFFFL & data) << 16 | (0xFFFFFFFFFFFF0000L & data) >> 16 & 0xFFFFL)));
            }
            return Thumb16.getOpcode(addr, ((int)data));
        }
        catch(Throwable e) {
            Log.d("Failed get OP 1", e);
            return e.toString().replace("java.lang.RuntimeException:", "RE:");
        }
    }

    public static void main(String[] args) throws Throwable {
    }

    static void printAddr(StringBuilder out, long addr, long offset) {
        int v2 = 43;
        if(addr == 0L) {
            if(offset < 0L) {
                out.append('-');
                offset = -offset;
            }
            else {
                out.append('+');
            }
        }
        out.append("0x");
        long v3 = addr + offset;
        if(v3 != 0L) {
            ItemContextMenu.setLastArmAddr(v3);
        }
        ToolsLight.prefixLongHex(out, 8, v3);
        StringBuilder stringBuilder1 = out.append("; ");
        if(offset <= 0L) {
            v2 = 45;
        }
        stringBuilder1.append(((char)v2)).append("0x").append(Long.toHexString((offset <= 0L ? -offset : offset)).toUpperCase(Locale.US)).append(' ').append(((char)(offset <= 0L ? 8593 : 8595)));
    }

    private static boolean test(int type, ArmDis armDis, long offset, int i, int data) {
        return false;
    }
}

