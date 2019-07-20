package com.roiland.platform.rest;

import com.roiland.platform.rest.filter.AbstractFilterChain;
import com.roiland.platform.rest.filter.FilterBean;
import com.roiland.platform.rest.filter.IFilterChain;
import com.roiland.platform.rest.filter.auth.Filter1;
import com.roiland.platform.rest.filter.auth.Filter2;
import com.roiland.platform.rest.filter.auth.Filter3;

import javax.ws.rs.WebApplicationException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2015/12/10.
 */
public class Test {
    private IFilterChain constructChain(List<AbstractFilterChain> list) {
        IFilterChain last = new IFilterChain() {
            @Override
            public void doFilter(FilterBean bean) throws WebApplicationException {

            }
        };
        for (int i = list.size() - 1; i >= 0; i--) {
            AbstractFilterChain filter = list.get(i);
            filter.setChain(last);
            last = filter;
        }
        return last;
    }

    public static void main(String[] args) {
        List<AbstractFilterChain> list = new ArrayList<>();
        list.add(new Filter1());
        list.add(new Filter2());
        list.add(new Filter3());
        IFilterChain chain = new Test().constructChain(list);
        chain.doFilter(new FilterBean(null,null,null));
    }
}
