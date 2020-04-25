package cn.dwx.springboot_redis_423.servie;

import com.github.pagehelper.PageInfo;

import java.util.Map;

public interface EmpService {

    PageInfo getPage(int pageNum);

    int add(Map map);

    int update(Map map);

    int delete(Integer id);

    Map findById(Integer id);
}
