package com.roiland.platform.template.core;

import com.roiland.platform.template.core.bean.ResourceBean;
import com.roiland.platform.template.core.support.Bean1;
import com.roiland.platform.template.core.support.TemplateSupport;
import junit.framework.TestCase;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class TemplateBindingTest extends TestCase {

    public void testBind1() throws IOException {
        String param1 = FileUtils.readResourceFile("param1");
        String template1 = FileUtils.readResourceFile("template1");
        TemplateSupport templateSupport = new TemplateSupport();
        ResourceBean bean = templateSupport.parseTemplate(template1);
        templateSupport.preCompile(bean);
        ResourceBean resource = templateSupport.bind(param1, bean);
        List<Object> paramValues = resource.getParamValue();
        assertEquals("2929103912039101023910",paramValues.get(0));
        assertEquals("LVAM0009000919",paramValues.get(1));
        assertEquals("abcddd",paramValues.get(2));
        assertEquals("fields1" ,((String[])paramValues.get(3))[0]);
        assertEquals("fields2" ,((String[])paramValues.get(3))[1]);
        assertEquals("fields3" ,((String[])paramValues.get(3))[2]);
        assertEquals("0000010000", ((Map<String, String>) paramValues.get(4)).get("var1"));
    }

    public void testBind2() throws IOException {
        String param1 = FileUtils.readResourceFile("param2");
        String template1 = FileUtils.readResourceFile("template2");
        TemplateSupport templateSupport = new TemplateSupport();
        ResourceBean bean = templateSupport.parseTemplate(template1);
        templateSupport.preCompile(bean);
        ResourceBean resource = templateSupport.bind(param1, bean);
        Bean1 bean1 = (Bean1)resource.getParamValue().get(0);
        assertEquals("leon.chen",bean1.getVar1());
        assertEquals(100,bean1.getVar2());
        assertEquals(100,bean1.getVar2());
        assertEquals(new BigDecimal("13901392013910391033901293"),bean1.getVar3());
        assertEquals(32190321l,bean1.getVar4().getVar1());
        assertEquals(11231.12312d,bean1.getVar4().getVar2());
        assertEquals(new Date(Timestamp.valueOf("2015-12-24 10:00:00").getTime()),bean1.getVar4().getVar3());
        assertEquals(true,bean1.getVar4().getVar4());
    }

    public void testBind3() throws IOException {

        String param3 = FileUtils.readResourceFile("param3");
        String param4 = FileUtils.readResourceFile("param4");
        String param5 = FileUtils.readResourceFile("param5");
        String template3 = FileUtils.readResourceFile("template3");
        TemplateSupport templateSupport = new TemplateSupport();
        ResourceBean bean = templateSupport.parseTemplate(template3);
        templateSupport.preCompile(bean);
        ResourceBean resource = templateSupport.bind(param3, bean);
        List<Object> paramValues = resource.getParamValue();
        assertEquals("00000000000000000093f37d6f2abd602a7438e146fc78d6d1",((Map<String,String>)paramValues.get(0)).get("access_token"));
        assertEquals(10, ((List<String>) paramValues.get(1)).size());

        resource = templateSupport.bind(param4, bean);
        paramValues = resource.getParamValue();
        assertEquals("0000000000000000000000aaaaaaaaaaaaaaaaaaaaaaaaaaaa",((Map<String,String>)paramValues.get(0)).get("access_token"));
        assertEquals(null,paramValues.get(1));

        resource = templateSupport.bind(param5, bean);
        paramValues = resource.getParamValue();
        assertEquals("00000000000000000000000000000000ssssssssssssssssss",((Map<String,String>)paramValues.get(0)).get("access_token"));
        assertEquals(3,((List<String>)paramValues.get(1)).size());
    }

}