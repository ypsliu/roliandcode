package com.roiland.platform.template.generator.java;

import com.roiland.platform.template.core.CodeGenerator;
import junit.framework.TestCase;
import org.apache.commons.lang.SystemUtils;

import java.nio.file.Paths;

public class JavaCodeGeneratorTest extends TestCase {

    public void testHandle() throws Exception {
        String template1 = FileUtils.readResourceFile("template1");
        String template2 = FileUtils.readResourceFile("template2");
        CodeGenerator gen = new JavaCodeGenerator();
        gen.codeGen(Paths.get(SystemUtils.USER_DIR, "src/test/java"), template1, template2);
    }
}