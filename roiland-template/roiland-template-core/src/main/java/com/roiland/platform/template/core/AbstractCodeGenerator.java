package com.roiland.platform.template.core;

import com.roiland.platform.template.core.ast.Resource;
import com.roiland.platform.template.core.bean.CodeGenResourceBean;
import com.roiland.platform.template.core.support.TemplateSupport;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.file.Path;
import java.util.List;

/**
 * @author leon.chen
 * @since 2016/4/12
 */
public abstract class AbstractCodeGenerator implements CodeGenerator {

    @Override
    public void codeGen(Path path, String... strTemplates) throws IOException {
        List<CodeGenResourceBean> list = getTemplateSupport().codeGenResourceBean(strTemplates);
        handle(path, list);
    }

    @Override
    public void codeGen(Path path, byte[]... templates) throws IOException {
        List<CodeGenResourceBean> list = getTemplateSupport().codeGenResourceBean(templates);
        handle(path, list);
    }

    @Override
    public void codeGen(Path path, InputStream... templates) throws IOException {
        List<CodeGenResourceBean> list = getTemplateSupport().codeGenResourceBean(templates);
        handle(path, list);
    }

    @Override
    public void codeGen(Path path, Reader... templates) throws IOException {
        List<CodeGenResourceBean> list = getTemplateSupport().codeGenResourceBean(templates);
        handle(path, list);
    }

    @Override
    public void codeGen(Path path, File... templates) throws IOException {
        List<CodeGenResourceBean> list = getTemplateSupport().codeGenResourceBean(templates);
        handle(path, list);
    }

    @Override
    public void codeGen(Path path, Resource... templates) throws IOException {
        List<CodeGenResourceBean> list = getTemplateSupport().codeGenResourceBean(templates);
        handle(path, list);
    }

    protected TemplateSupport getTemplateSupport() {
        return new TemplateSupport();
    }

    protected abstract void handle(Path path, List<CodeGenResourceBean> list) throws IOException;
}
