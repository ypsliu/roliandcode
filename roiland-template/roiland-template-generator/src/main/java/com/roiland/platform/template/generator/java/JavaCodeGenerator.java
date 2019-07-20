package com.roiland.platform.template.generator.java;

import com.roiland.platform.template.core.AbstractCodeGenerator;
import com.roiland.platform.template.core.bean.CodeGenResourceBean;
import com.roiland.platform.template.core.bean.CodeGenStructBean;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author leon.chen
 * @since 2016/4/12
 */
public class JavaCodeGenerator extends AbstractCodeGenerator {

    private static final String FILE_SUFFIX = ".java";

    private static final String BRACE_BEGIN = "{\n";

    private static final String BRACE_END = "}\n";

    private static final String SEMI = ";\n";

    private static final String COMMA = ",";

    private static final String BLANK = " ";

    private static final String TAB = "\t";

    private static final String PARENT_BEGIN = "(";

    private static final String PARENT_END = ")";

    @Override
    protected void handle(Path path,List<CodeGenResourceBean> list) throws IOException {
        new StructCodeGenerator().handle(path,list);
        new ServiceCodeGenerator().handle(path,list);
    }

    private static class ServiceCodeGenerator extends AbstractCodeGenerator {

        @Override
        protected void handle(Path path,List<CodeGenResourceBean> list) throws IOException {
            codeGen(path, list);
        }

        private void codeGen(Path path, List<CodeGenResourceBean> list) throws IOException {
            for (CodeGenResourceBean bean : list) {
                codeGen(path, bean);
            }
        }

        private void codeGen(Path path, CodeGenResourceBean bean) throws IOException {
            String resource = bean.getResource();
            int index = resource.lastIndexOf(".");
            String className = resource.substring(index + 1) + FILE_SUFFIX;
            if (index != -1) {
                String packagePath = resource.substring(0, index);
                packagePath = packagePath.replaceAll("\\.", "/");
                path = path.resolve(packagePath);
            }
            path = createFile(path, className);
            List<String> lines = codeGen(bean);
            Files.write(path, lines, Charset.forName("UTF-8"), StandardOpenOption.CREATE);
        }

        private List<String> codeGen(CodeGenResourceBean bean) {
            List<String> line = new ArrayList<>();
            genPackage(bean, line);
            genClass(bean, line);
            return line;
        }

        private void genPackage(CodeGenResourceBean bean, List<String> line) {
            int index = bean.getResource().lastIndexOf(".");
            if (index != -1) {
                line.add("package" + BLANK + bean.getResource().substring(0, index) + SEMI);
            }
        }

        private void genClass(CodeGenResourceBean bean, List<String> line) {
            int index = bean.getResource().lastIndexOf(".");
            String className = bean.getResource().substring(index + 1);
            line.add("public interface" + BLANK + className + BRACE_BEGIN);
            genAbstractMethod(bean, line);
            line.add(BRACE_END);
        }

        private void genAbstractMethod(CodeGenResourceBean bean, List<String> line) {
            bean.getMethod();
            bean.getParamName();
            bean.getParamType();
            bean.getReturnType();
            StringBuilder builder = new StringBuilder();
            builder.append(TAB);
            builder.append(bean.getReturnType());
            builder.append(BLANK);
            builder.append(bean.getMethod());
            builder.append(PARENT_BEGIN);
            Iterator<String> itType = bean.getParamType().iterator();
            Iterator<String> itName = bean.getParamName().iterator();
            while (itType.hasNext()) {
                String paramType = itType.next();
                String paramName = itName.next();
                builder.append(paramType);
                builder.append(BLANK);
                builder.append(paramName);
                if (itType.hasNext()) {
                    builder.append(COMMA);
                }
            }
            builder.append(PARENT_END);
            builder.append(SEMI);
            line.add(builder.toString());
        }
    }

    private static class StructCodeGenerator extends AbstractCodeGenerator {

        @Override
        protected void handle(Path path,List<CodeGenResourceBean> list) throws IOException {
            codeGen(path, list);
        }

        private void codeGen(Path path, List<CodeGenResourceBean> list) throws IOException {
            for (CodeGenResourceBean bean : list) {
                codeGen(path, bean);
            }
        }

        private void codeGen(Path path, CodeGenResourceBean bean) throws IOException {
            for (CodeGenStructBean struct : bean.getStructBean()) {
                codeGen(path, struct);
            }
        }

        private void codeGen(Path path, CodeGenStructBean struct) throws IOException {
            String resource = struct.getBeanName();
            int index = resource.lastIndexOf(".");
            String className = resource.substring(index + 1) + FILE_SUFFIX;
            if (index != -1) {
                String packagePath = resource.substring(0, index).replaceAll("\\.", "/");
                path = path.resolve(packagePath);
            }
            path = createFile(path, className);
            List<String> lines = codeGen(struct);
            Files.write(path, lines, Charset.forName("UTF-8"), StandardOpenOption.CREATE);
        }

        private List<String> codeGen(CodeGenStructBean struct) {
            List<String> line = new ArrayList<>();
            codeGen(struct, line);
            return line;
        }

        private void codeGen(CodeGenStructBean struct, List<String> line) {
            genPackage(struct, line);
            genClass(struct, line);
        }

        private void genPackage(CodeGenStructBean struct, List<String> line) {
            int index = struct.getBeanName().lastIndexOf(".");
            if (index != -1) {
                line.add("package" + BLANK + struct.getBeanName().substring(0, index) + SEMI);
            }
        }

        private void genClass(CodeGenStructBean bean, List<String> line) {
            int index = bean.getBeanName().lastIndexOf(".");
            String className = bean.getBeanName().substring(index + 1);
            line.add("public class" + BLANK + className + BRACE_BEGIN);
            genFields(bean, line);
            genGetter(bean, line);
            genSetter(bean, line);
            line.add(BRACE_END);
        }

        private void genSetter(CodeGenStructBean bean, List<String> line) {
            for (int i = 0; i < bean.getFieldType().size(); i++) {
                String fieldType = bean.getFieldType().get(i);
                String fieldName = bean.getFieldName().get(i);
                String setterMethodName = "set" + upperFirst(fieldName);
                StringBuilder builder = new StringBuilder();
                builder.append(TAB + "public" + BLANK + "void" + BLANK + setterMethodName + PARENT_BEGIN + fieldType + BLANK + fieldName + PARENT_END + BRACE_BEGIN);
                builder.append(TAB + TAB + "this." + fieldName + "=" + fieldName + SEMI);
                builder.append(TAB + BRACE_END);
                line.add(builder.toString());
            }
        }

        private void genGetter(CodeGenStructBean bean, List<String> line) {
            for (int i = 0; i < bean.getFieldType().size(); i++) {
                String fieldType = bean.getFieldType().get(i);
                String fieldName = bean.getFieldName().get(i);
                String getterMethodName = "get" + upperFirst(fieldName);
                StringBuilder builder = new StringBuilder();
                builder.append(TAB + "public" + BLANK + fieldType + BLANK + getterMethodName + PARENT_BEGIN + PARENT_END + BRACE_BEGIN);
                builder.append(TAB + TAB + "return" + BLANK + "this." + fieldName + SEMI);
                builder.append(TAB + BRACE_END);
                line.add(builder.toString());
            }
        }

        private void genFields(CodeGenStructBean bean, List<String> line) {
            for (int i = 0; i < bean.getFieldType().size(); i++) {
                String fieldType = bean.getFieldType().get(i);
                String fieldName = bean.getFieldName().get(i);
                line.add(TAB + "private" + BLANK + fieldType + BLANK + fieldName + SEMI);
            }
        }

        private static String upperFirst(String name) {
            return name.substring(0, 1).toUpperCase() + name.substring(1);
        }
    }

    private static Path createFile(Path path, String fileName) throws IOException {
        if (!Files.exists(path)) {
            path = Files.createDirectories(path);
        }
        path = path.resolve(fileName);
        if (!Files.exists(path)) {
            return Files.createFile(path);
        }
        return path;
    }
}
