package cn.dwx.springboot_redis_423.mapper;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface EmpMapper {
    //插入多条emp记录
    int addEmp(List<Map> list);

    @Select("select * from emp")
    List<Map> findAll();

    @Insert("insert into emp values(default, #{name}, #{did})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int add(Map map);

    @Update("update emp set name = #{name}, did = #{did} where id = #{id}")
    int update(Map map);

    @Delete("delete from emp where id = #{id}")
    int delete(int id);

    @Select("select * from emp where id = #{id}")
    Map findById(int id);
}
