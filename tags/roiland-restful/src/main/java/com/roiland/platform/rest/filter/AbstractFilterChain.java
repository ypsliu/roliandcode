package com.roiland.platform.rest.filter;

/**
 * Created by user on 2015/11/26.
 */
public abstract class AbstractFilterChain implements IFilterChain {
    protected IFilterChain chain;

    public void setChain(IFilterChain chain) {
        this.chain = chain;
    }
}
