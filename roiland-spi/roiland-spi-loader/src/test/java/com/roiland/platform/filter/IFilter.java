package com.roiland.platform.filter;

import com.roiland.platform.spi.annotation.SPI;

/**
 * Copyright 2015 roiland.com
 * If you have any question or issue about this RESTful framework.
 * Please mail to leon.chen@roiland.com or jeffy.yang@roiland.com
 *
 * @author leon.chen
 * @since 2016/3/16
 */
@SPI
public interface IFilter {
    public void doFilter();
}
