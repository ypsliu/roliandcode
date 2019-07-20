package com.roiland.platform.template.socket.spi;

import com.roiland.platform.spi.annotation.Extension;
import com.roiland.platform.template.core.Invoker;
import com.roiland.platform.template.core.bean.ResourceBean;
import com.roiland.platform.template.core.spi.InvokerLoader;
import com.roiland.platform.template.core.support.TemplateSupport;
import com.roiland.platform.template.socket.SocketInvoker;

/**
 * @author leon.chen
 * @since 2016/7/29
 */
@Extension("invoker")
public class SocketInvokerLoader implements InvokerLoader {
    @Override
    public Invoker getInvoker(String globalParam, String template) throws Exception {
        TemplateSupport templateSupport = new TemplateSupport();
        ResourceBean resourceBean = templateSupport.parseTemplate(template);
        templateSupport.preCompile(resourceBean);
        return SocketInvoker.create(resourceBean).globalParam(globalParam).context(templateSupport).build(false);
    }
}
