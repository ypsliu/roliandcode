package com.roiland.platform.template.core.spi;

import com.roiland.platform.spi.annotation.SPI;
import com.roiland.platform.template.core.Invoker;

/**
 * @author leon.chen
 * @since 2016/7/29
 */
@SPI
public interface InvokerLoader {
    Invoker getInvoker(String globalParam, String template) throws Exception;
}
