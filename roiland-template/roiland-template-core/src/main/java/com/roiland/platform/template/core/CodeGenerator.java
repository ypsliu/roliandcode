package com.roiland.platform.template.core;

import com.roiland.platform.template.core.ast.Resource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.file.Path;

/**
 * @author leon.chen
 * @since 2016/4/12
 */
public interface CodeGenerator {

    void codeGen(Path path,String... templates) throws IOException;

    void codeGen(Path path,byte[]... templates) throws IOException;

    void codeGen(Path path,InputStream... templates) throws IOException;

    void codeGen(Path path,Reader... templates) throws IOException;

    void codeGen(Path path,File... templates) throws IOException;

    void codeGen(Path path,Resource... templates) throws IOException;

}
