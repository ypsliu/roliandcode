package com.roiland.platform.template.core.support;

import java.util.Date;

/**
 * @author leon.chen
 * @since 2016/4/7
 */
public class Bean2 {
    private long var1;
    private Double var2;
    private Date var3;
    private Object var4;

    public long getVar1() {
        return var1;
    }

    public void setVar1(long var1) {
        this.var1 = var1;
    }

    public Double getVar2() {
        return var2;
    }

    public void setVar2(Double var2) {
        this.var2 = var2;
    }

    public Date getVar3() {
        return var3;
    }

    public void setVar3(Date var3) {
        this.var3 = var3;
    }

    public Object getVar4() {
        return var4;
    }

    public void setVar4(Object var4) {
        this.var4 = var4;
    }

    @Override
    public String toString() {
        return "Bean2{" +
                "var1=" + var1 +
                ", var2=" + var2 +
                ", var3=" + var3 +
                ", var4=" + var4 +
                '}';
    }
}
