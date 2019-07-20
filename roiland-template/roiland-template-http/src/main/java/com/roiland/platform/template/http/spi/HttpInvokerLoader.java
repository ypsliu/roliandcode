package com.roiland.platform.template.http.spi;

import com.roiland.platform.spi.annotation.Extension;
import com.roiland.platform.template.core.Invoker;
import com.roiland.platform.template.core.bean.ResourceBean;
import com.roiland.platform.template.core.spi.InvokerLoader;
import com.roiland.platform.template.core.support.TemplateSupport;
import com.roiland.platform.template.http.HttpInvoker;

import java.io.IOException;

/**
 * @author leon.chen
 * @since 2016/7/29
 */
@Extension("invoker")
public class HttpInvokerLoader implements InvokerLoader{

    @Override
    public Invoker getInvoker(String globalParam, String template) throws IOException {
        TemplateSupport templateSupport = new TemplateSupport();
        ResourceBean resourceBean = templateSupport.parseTemplate(template);
        templateSupport.preCompile(resourceBean);
        return HttpInvoker.create(resourceBean).globalParam(globalParam).context(templateSupport).build(false);
    }
}
