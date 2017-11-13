/**
 * Copyright (c) 2003-2014 All Rights Reserved.
 */
package com.xdercat.web.util;

import com.google.common.collect.ImmutableList;
import org.mybatis.generator.api.*;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MySQLPaginationPlugin extends PluginAdapter {

    public static void generate() {
        String config = MySQLPaginationPlugin.class.getClassLoader().getResource(
                "generatorConfig.xml").getFile();
        String[] arg = {"-configfile", config, "-overwrite"};
        ShellRunner.main(arg);
    }
    public static void main(String[] args) {
        generate();
    }

    @Override
    public boolean sqlMapInsertElementGenerated(XmlElement element,
                                                IntrospectedTable introspectedTable) {
        Attribute attribute = new Attribute("useGeneratedKeys", "true");
        Attribute attribute1 = new Attribute("keyProperty", "id");
        element.addAttribute(attribute);
        element.addAttribute(attribute1);
        return true;
    }


    @Override
    public boolean sqlMapInsertSelectiveElementGenerated(XmlElement element,
                                                         IntrospectedTable introspectedTable) {
        Attribute attribute = new Attribute("useGeneratedKeys", "true");
        Attribute attribute1 = new Attribute("keyProperty", "id");
        element.addAttribute(attribute);
        element.addAttribute(attribute1);
        return true;
    }
    @Override
    public boolean modelExampleClassGenerated(TopLevelClass topLevelClass,
                                              IntrospectedTable introspectedTable) {
        //mybaties自动生成mapper的时候是追加操作,但是我们平常都是删除重新生成,所以需要扩展删除原来的mapper让mybaties再生成一个
        List<GeneratedXmlFile> generatedXmlFiles = introspectedTable.getGeneratedXmlFiles();
        for (GeneratedFile generatedFile : generatedXmlFiles) {
            generatedFile.getFormattedContent();

            File project = new File(generatedFile.getTargetProject() + generatedFile.getTargetPackage());
            File file = new File(project, generatedFile.getFileName());
            if (file.exists()) {
                file.delete();
            }
        }

        // add field, getter, setter for limit clause
        addPage(topLevelClass, introspectedTable, "page");
//        booleanField(topLevelClass);
        return super.modelExampleClassGenerated(topLevelClass,
                introspectedTable);
    }

    public static final List<String> DELETED_FIELDS = ImmutableList.of(
            "andIsDeleteNotBetween",
            "andIsDeleteNotEqualTo",
            "andIsDeleteLessThan",
            "andIsDeleteLessThanOrEqualTo",
            "andIsDeleteGreaterThan",
            "andIsDeleteGreaterThanOrEqualTo",
            "andIsDeleteIn",
            "andIsDeleteNotIn",
            "andIsDeleteBetween",
            "andIsDeleteNotBetween"
    );

    private void booleanField(TopLevelClass topLevelClass) {
        List<InnerClass> innerClasses = topLevelClass.getInnerClasses();
        for (InnerClass innerClass : innerClasses) {
            if (innerClass.getType().getShortName().equals("GeneratedCriteria")) {
                List<Method> methods = innerClass.getMethods();
                Iterator<Method> iterable = methods.iterator();
                while (iterable.hasNext()) {
                    Method method = iterable.next();
                    if (DELETED_FIELDS.contains(method.getName())) {
                        iterable.remove();
                        continue;
                    }
                    if ((method.getName().equals("andIsDeleteEqualTo"))) {
                        method.getParameters().clear();
                        method.getParameters().add(new Parameter(FullyQualifiedJavaType.getBooleanPrimitiveInstance(), "value"));
                        List<String> bodyLines = method.getBodyLines();
                        bodyLines.clear();
                        bodyLines.add("Integer flag = value ? 1 : 0;");
                        bodyLines.add("addCriterion(\"is_delete =\", flag, \"isDelete\");");
                        bodyLines.add("return (Criteria) this;");
                    }
                }
            }
        }
    }

    @Override
    public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        interfaze.addImportedType(new FullyQualifiedJavaType(
                "org.springframework.stereotype.Repository"));
        interfaze.addImportedType(new FullyQualifiedJavaType(
                "org.springframework.transaction.annotation.Transactional"));
        interfaze.addAnnotation("@Repository");
        interfaze.addAnnotation("@Transactional");
        addDaoAnno(interfaze);

        return super.clientGenerated(interfaze, topLevelClass, introspectedTable);
    }

    private void addDaoAnno(Interface interfaze) {
        List<Method> methods = interfaze.getMethods();
        for (Method method : methods) {
            if (method.getName().contains("insert")) {
                interfaze.addImportedType(new FullyQualifiedJavaType(
                        "Insert"));
                method.addAnnotation("@Insert");

            }
            if (method.getName().contains("update")) {
                interfaze.addImportedType(new FullyQualifiedJavaType(
                        "Update"));
                method.addAnnotation("@Update");
            }
        }
    }

    @Override
    public boolean sqlMapDocumentGenerated(Document document,
                                           IntrospectedTable introspectedTable) {
        XmlElement parentElement = document.getRootElement();
        // 产生分页语句前半部分
//        XmlElement paginationPrefixElement = new XmlElement("sql");
//        paginationPrefixElement.addAttribute(new Attribute("id",
//                "MySQLDialectPrefix"));
//        XmlElement pageStart = new XmlElement("if");
//        pageStart.addAttribute(new Attribute("test", "page != null"));
//        pageStart.addElement(new TextElement(
//                "select * from ( "));
//        paginationPrefixElement.addElement(pageStart);
//        parentElement.addElement(paginationPrefixElement);

        // 产生分页语句后半部分
        XmlElement paginationSuffixElement = new XmlElement("sql");
        paginationSuffixElement.addAttribute(new Attribute("id",
                "MySQLDialectSuffix"));
        XmlElement pageEnd = new XmlElement("if");
        pageEnd.addAttribute(new Attribute("test", "page != null"));
        pageEnd.addElement(new TextElement(
                "LIMIT #{page.dbBeginIndex}, #{page.itemsPerPage}"));
        paginationSuffixElement.addElement(pageEnd);
        parentElement.addElement(paginationSuffixElement);

        return super.sqlMapDocumentGenerated(document, introspectedTable);
    }

    public static final String FIELD_IS_DELETE = "isDelete";

    @Override
    public boolean modelFieldGenerated(Field field,
                                       TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn,
                                       IntrospectedTable introspectedTable,
                                       ModelClassType modelClassType) {
        if (field.getName().equals(FIELD_IS_DELETE)) {
            field.setType(FullyQualifiedJavaType.getBooleanPrimitiveInstance());
        }
        return true;
    }

    @Override
    public boolean modelGetterMethodGenerated(Method method,
                                              TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn,
                                              IntrospectedTable introspectedTable,
                                              ModelClassType modelClassType) {
        if (method.getName().equals("getIsDelete")) {
            method.setName("isDelete");
            method.setReturnType(FullyQualifiedJavaType.getBooleanPrimitiveInstance());
        }
        return false;
    }

    @Override
    public boolean modelSetterMethodGenerated(Method method,
                                              TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn,
                                              IntrospectedTable introspectedTable,
                                              ModelClassType modelClassType) {
        if (method.getName().equals("setIsDelete")) {
            method.setName("setDelete");

            method.getParameters().clear();
            method.getParameters().add(new Parameter(FullyQualifiedJavaType.getBooleanPrimitiveInstance(), "isDelete"));
        }
        return false;
    }


    @Override
    public boolean sqlMapSelectByExampleWithoutBLOBsElementGenerated(
            XmlElement element, IntrospectedTable introspectedTable) {

//        XmlElement pageStart = new XmlElement("include"); //$NON-NLS-1$
//        pageStart.addAttribute(new Attribute("refid", "MySQLDialectPrefix"));
//        element.getElements().add(0, pageStart);

        XmlElement isNotNullElement = new XmlElement("include"); //$NON-NLS-1$
        isNotNullElement.addAttribute(new Attribute("refid",
                "MySQLDialectSuffix"));
        element.getElements().add(isNotNullElement);

        return super.sqlMapUpdateByExampleWithoutBLOBsElementGenerated(element,
                introspectedTable);
    }

    /**
     * @param topLevelClass
     * @param introspectedTable
     * @param name
     */
    private void addPage(TopLevelClass topLevelClass,
                         IntrospectedTable introspectedTable, String name) {
        topLevelClass.addImportedType(new FullyQualifiedJavaType(
                "Page"));
        CommentGenerator commentGenerator = context.getCommentGenerator();
        Field field = new Field();
        field.setVisibility(JavaVisibility.PROTECTED);
        field.setType(new FullyQualifiedJavaType("Page"));
        field.setName(name);
        commentGenerator.addFieldComment(field, introspectedTable);
        topLevelClass.addField(field);
        char c = name.charAt(0);
        String camel = Character.toUpperCase(c) + name.substring(1);
        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setName("set" + camel);
        method.addParameter(new Parameter(new FullyQualifiedJavaType(
                "Page"), name));
        method.addBodyLine("this." + name + "=" + name + ";");
        commentGenerator.addGeneralMethodComment(method, introspectedTable);
        topLevelClass.addMethod(method);
        method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(new FullyQualifiedJavaType(
                "Page"));
        method.setName("get" + camel);
        method.addBodyLine("return " + name + ";");
        commentGenerator.addGeneralMethodComment(method, introspectedTable);
        topLevelClass.addMethod(method);
    }


    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        generateToString(introspectedTable, topLevelClass);
        topLevelClass.addImportedType(new FullyQualifiedJavaType("lombok.Data"));
        topLevelClass.addAnnotation("@Data");
        return true;
    }

    @Override
    public boolean modelRecordWithBLOBsClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        generateToString(introspectedTable, topLevelClass);
        return true;
    }

    @Override
    public boolean modelPrimaryKeyClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        generateToString(introspectedTable, topLevelClass);
        return true;
    }

    private void generateToString(IntrospectedTable introspectedTable, TopLevelClass topLevelClass) {
        List<Field> fields = topLevelClass.getFields();
        Map<String, Field> map = new HashMap<String, Field>();
        for (Field field : fields) {
            map.put(field.getName(), field);
        }
        List<IntrospectedColumn> columns = introspectedTable.getAllColumns();
        for (IntrospectedColumn column : columns) {
            Field f = map.get(column.getJavaProperty());
            if (f != null) {
                f.getJavaDocLines().clear();
                if (column.getRemarks() != null) {
                    f.addJavaDocLine("/** " + column.getRemarks() + " */");
                }
            }
        }
    }

    /**
     * This plugin is always valid - no properties are required
     */
    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

}

