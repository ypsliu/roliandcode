package com.roiland.platform.template.core;

import com.roiland.platform.template.core.bean.ResourceBean;
import com.roiland.platform.template.core.support.TemplateSupport;
import junit.framework.TestCase;

import java.io.IOException;
import java.util.List;

/**
 * @author leon.chen
 * @since 2016/6/15
 */
public class ByteBindingTest extends TestCase {
    public void testBindByteArray() throws IOException {

        String param1 = FileUtils.readResourceFile("param6");
        String template1 = FileUtils.readResourceFile("template4");
        TemplateSupport templateSupport = new TemplateSupport();
        templateSupport.putContext("helper",new ByteHelper());
        ResourceBean bean = templateSupport.parseTemplate(template1);
        ResourceBean resource = templateSupport.bind(param1, bean);
        String paramValues = (String)resource.getParamValue().get(5);
        assertEquals("CQABIAUAPAEFDwYZDic1DwYZDicnAQcBDP7+/v4BAQMDAwMBAv////8BAwAAAP4BBAAAAP4BCwAAAAEBEAAAAP9hug==",paramValues);
    }

}
