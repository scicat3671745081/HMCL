/*
 * Hello Minecraft! Launcher
 * Copyright (C) 2020  huangyuhui <huanghongxun2008@126.com> and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.jackhuang.hmcl.util.platform;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import static org.jackhuang.hmcl.util.platform.Bits.BIT_32;
import static org.jackhuang.hmcl.util.platform.Bits.BIT_64;

public enum Architecture {
    X86(BIT_32, "x86"),
    X86_64(BIT_64, "x86-64"),
    IA32(BIT_32, "IA-32"),
    IA64(BIT_64, "IA-64"),
    SPARC(BIT_32),
    SPARCV9(BIT_64, "SPARC V9"),
    ARM32(BIT_32),
    ARM64(BIT_64),
    MIPS(BIT_32),
    MIPS64(BIT_64),
    MIPSEL(BIT_32, "MIPSel"),
    MIPS64EL(BIT_64, "MIPS64el"),
    PPC(BIT_32, "PowerPC"),
    PPC64(BIT_64, "PowerPC-64"),
    PPCLE(BIT_32, "PowerPC (Little-Endian)"),
    PPC64LE(BIT_64, "PowerPC-64 (Little-Endian)"),
    S390(BIT_32),
    S390X(BIT_64, "S390x"),
    RISCV(BIT_64, "RISC-V"),
    UNKNOWN(Bits.UNKNOWN, "Unknown");

    private final String checkedName;
    private final String displayName;
    private final Bits bits;

    Architecture(Bits bits) {
        this.checkedName = this.toString().toLowerCase(Locale.ROOT);
        this.displayName = this.toString();
        this.bits = bits;
    }

    Architecture(Bits bits, String displayName) {
        this.checkedName = this.toString().toLowerCase(Locale.ROOT);
        this.displayName = displayName;
        this.bits = bits;
    }

    Architecture(Bits bits, String displayName, String identifier) {
        this.checkedName = identifier;
        this.displayName = displayName;
        this.bits = bits;
    }

    public Bits getBits() {
        return bits;
    }

    public String getCheckedName() {
        return checkedName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static final String CURRENT_ARCH_NAME;
    public static final Architecture CURRENT_ARCH;
    public static final Architecture SYSTEM_ARCH;

    private static final Pattern NORMALIZER = Pattern.compile("[^a-z0-9]+");

    public static Architecture parseArchName(String value) {
        if (value == null) {
            return UNKNOWN;
        }
        value = NORMALIZER.matcher(value.toLowerCase(Locale.ROOT).trim()).replaceAll("");

        switch (value) {
            case "x8664":
            case "amd64":
            case "ia32e":
            case "em64t":
            case "x64":
                return X86_64;
            case "x8632":
            case "x86":
            case "i386":
            case "i486":
            case "i586":
            case "i686":
            case "ia32":
            case "x32":
                return X86;
            case "aarch64":
                return ARM64;
            case "arm":
            case "arm32":
                return ARM32;
            case "mips64":
                return MIPS64;
            case "mips64el":
                return MIPS64EL;
            case "mips":
            case "mips32":
                return MIPS;
            case "mipsel":
            case "mips32el":
                return MIPSEL;
            case "riscv":
                return RISCV;
            case "ia64":
            case "ia64w":
            case "itanium64":
                return IA64;
            case "ia64n":
                return IA32;
            case "sparcv9":
            case "sparc64":
                return SPARCV9;
            case "sparc":
            case "sparc32":
                return SPARC;
            case "ppc64":
                return PPC64;
            case "ppc64le":
                return PPC64LE;
            case "ppc":
            case "ppc32":
                return PPC;
            case "ppcle":
            case "ppc32le":
                return PPCLE;
            case "s390":
                return S390;
            case "s390x":
                return S390X;
            default:
                return UNKNOWN;
        }
    }

    static {
        CURRENT_ARCH_NAME = System.getProperty("os.arch");

        CURRENT_ARCH = parseArchName(CURRENT_ARCH_NAME);

        Architecture sysArch = UNKNOWN;

        if (OperatingSystem.CURRENT_OS == OperatingSystem.WINDOWS) {
            sysArch = parseArchName(System.getenv("PROCESSOR_ARCHITECTURE"));
        } else {
            try {
                Process process = Runtime.getRuntime().exec("/usr/bin/arch");
                if (process.waitFor(1, TimeUnit.SECONDS)) {
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                        sysArch = parseArchName(reader.readLine());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Throwable ignored) {
            }
        }

        SYSTEM_ARCH = sysArch == UNKNOWN ? CURRENT_ARCH : sysArch;
    }
}
