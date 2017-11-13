package com.xdercat.wrapper.util;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.internal.types.JavaTypeResolverDefaultImpl;

import java.sql.Types;

public class MyJavaTypeResolver extends JavaTypeResolverDefaultImpl {

    @Override
    public FullyQualifiedJavaType calculateJavaType(
            IntrospectedColumn introspectedColumn) {
        if(introspectedColumn.getJdbcType() == Types.SMALLINT && introspectedColumn.getLength() >= 5){
            return new FullyQualifiedJavaType(Integer.class.getName());
        }

        return super.calculateJavaType(introspectedColumn);
    }
}