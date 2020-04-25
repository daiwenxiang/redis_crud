package cn.dwx.springboot_redis_423.servie.impl;

import cn.dwx.springboot_redis_423.mapper.EmpMapper;
import cn.dwx.springboot_redis_423.servie.EmpService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@Transactional
public class EmpServiceImpl implements EmpService {

    @Resource
    EmpMapper empMapper;
    @Resource
    RedisTemplate redisTemplate;

    @Override
    public PageInfo getPage(int pageNum) {
        int pageSize = 5;
        int start = (pageNum - 1) * pageSize;
        int end = pageNum * pageSize - 1;

        //判断数据是否在redis中
        if (!redisTemplate.hasKey("empzset")) {
            //把emp2表中的数据存入redis
            //使用zset保存,score采用id值
            List<Map> all = empMapper.findAll();
            for (Map map : all) {
                redisTemplate.opsForZSet().add("empzset", map, (Integer) map.get("id"));
            }
        }

        long total = redisTemplate.opsForZSet().size("empzset");

        //从redis中取zset的部分数据
        Set empzset = redisTemplate.opsForZSet().range("empzset", start, end);
        System.out.println("empzset = " + empzset);

        Page page = new Page();
        page.setPageNum(pageNum);
        page.setPageSize(pageSize);
        page.setTotal(total);

        page.addAll(empzset);

        PageInfo pageInfo = new PageInfo(page, 5);
        System.out.println("pageInfo = " + pageInfo);

        return pageInfo;
    }

    //把数据写入数据库
    //清除emp缓存,或者把新的emp添加到缓存
    @Override
    public int add(Map map) {
        int add = empMapper.add(map);
        String id = String.valueOf(map.get("id"));
        int id2 = Integer.parseInt(id);
        redisTemplate.opsForZSet().add("empzset", map, id2);
        return add;
    }

    @Override
    public int update(Map map) {
        int update = empMapper.update(map);

        //先在redis中删除对应的map
//        int id = Integer.parseInt((String) map.get("id"));
        int id = Integer.parseInt(String.valueOf(map.get("id")));
//        int id = Integer.parseInt(map.get("id").toString());

        redisTemplate.opsForZSet().removeRangeByScore("empzset", id, id);

        //如果map中的数据不全,需要再去根据id进行查询复制给map
        map = empMapper.findById(id);

        //把修改过的map新增到redis
        redisTemplate.opsForZSet().add("empzset", map, id);
        return update;
    }

    @Override
    public int delete(Integer id) {
        int delete = empMapper.delete(id);
        redisTemplate.opsForZSet().removeRangeByScore("empzset", id, id);
        return delete;
    }

    @Override
    public Map findById(Integer id) {
        return empMapper.findById(id);
    }
}
