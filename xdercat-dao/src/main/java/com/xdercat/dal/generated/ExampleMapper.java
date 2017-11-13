package com.xdercat.dal.generated;

import com.xdercat.domain.Example;
import com.xdercat.domain.ExampleExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface ExampleMapper {
    int countByExample(ExampleExample example);

    int deleteByExample(ExampleExample example);

    int deleteByPrimaryKey(Integer id);

    @Insert
    int insert(Example record);

    @Insert
    int insertSelective(Example record);

    List<Example> selectByExample(ExampleExample example);

    Example selectByPrimaryKey(Integer id);

    @Update
    int updateByExampleSelective(@Param("record") Example record, @Param("example") ExampleExample example);

    @Update
    int updateByExample(@Param("record") Example record, @Param("example") ExampleExample example);

    @Update
    int updateByPrimaryKeySelective(Example record);

    @Update
    int updateByPrimaryKey(Example record);
}