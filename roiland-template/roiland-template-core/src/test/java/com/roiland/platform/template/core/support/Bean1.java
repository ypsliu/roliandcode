package com.roiland.platform.template.core.support;

import java.math.BigDecimal;

/**
 * @author leon.chen
 * @since 2016/4/7
 */
public class Bean1 {
    private String var1;
    private int var2;
    private BigDecimal var3;
    private Bean2 var4;

    public String getVar1() {
        return var1;
    }

    public void setVar1(String var1) {
        this.var1 = var1;
    }

    public int getVar2() {
        return var2;
    }

    public void setVar2(int var2) {
        this.var2 = var2;
    }

    public BigDecimal getVar3() {
        return var3;
    }

    public void setVar3(BigDecimal var3) {
        this.var3 = var3;
    }

    public Bean2 getVar4() {
        return var4;
    }

    public void setVar4(Bean2 var4) {
        this.var4 = var4;
    }

    @Override
    public String toString() {
        return "Bean1{" +
                "var1='" + var1 + '\'' +
                ", var2=" + var2 +
                ", var3=" + var3 +
                ", var4=" + var4 +
                '}';
    }
}
