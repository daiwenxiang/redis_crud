package cn.dwx.springboot_redis_423;

import cn.dwx.springboot_redis_423.mapper.EmpMapper;
import cn.dwx.springboot_redis_423.servie.EmpService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageInfo;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;
import java.util.*;

@SpringBootTest
class SpringbootRedis423ApplicationTests {

    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Resource
    RedisTemplate redisTemplate;

    @Resource
    EmpMapper empMapper;

    @Resource
    EmpService empService;

    @Test
    void contextLoads() {
        stringRedisTemplate.opsForValue().set("a1", 123 + "");
        String a1 = stringRedisTemplate.opsForValue().get("a1");
        System.out.println("a1 = " + a1);
    }

    @Test
    void contextLoads2() throws JsonProcessingException {
        HashMap map = new HashMap();
        map.put("aa", "11");
        map.put("bb", "22");

        //把map转成json字符串
        String jsonStr = new ObjectMapper().writeValueAsString(map);
        System.out.println("jsonStr = " + jsonStr);

        stringRedisTemplate.opsForValue().set("map1", jsonStr);
        String a1 = stringRedisTemplate.opsForValue().get("map1");

        assert a1 != null;
        HashMap hashMap = new ObjectMapper().readValue(a1, HashMap.class);
        System.out.println("hashMap = " + hashMap);

//        System.out.println("a1 = " + a1);
    }

    @Test
    void contextLoads3() {
        Map map = new HashMap<>();
        map.put("aa", 11);
        map.put("bb", "qq");

        redisTemplate.opsForValue().set("map2", map);
    }

    @Test
    void contextLoads4() {
        Map map2 = (Map) redisTemplate.opsForValue().get("map2");
        System.out.println("map2 = " + map2);
    }

    @Test
    void contextLoads5() {
        List<Map> list = new ArrayList<>();
        Map map;
        int did;
        Random random = new Random();
        for (int i = 1; i <= 100; i++) {
            map = new HashMap<>();
            map.put("name", "name" + i);
            did = random.nextInt(5) + 1;
            map.put("did", did);
            list.add(map);
        }
        System.out.println(empMapper.addEmp(list));
    }

    @Test
    void contextLoads6() {
        if (redisTemplate.hasKey("emp")) {
            List<Map> emp = (List<Map>) redisTemplate.opsForValue().get("emp");
            System.out.println("从redis里面拿emp-------------" + emp);
        } else {
            List<Map> all = empMapper.findAll();
            System.out.println("从mysql里面拿emp-------------" + all);
            redisTemplate.opsForValue().set("emp", all);
        }
    }

    @Test
    void contextLoads8() {
        List<Map> all = empMapper.findAll();
        redisTemplate.opsForValue().set("empList", all);
    }

    @Test
    void contextLoads9() {
//        List<Map> all = empMapper.findAll();
//        for (Map emp : all) {
//            redisTemplate.opsForList().rightPush("empList2", emp);
//        }

        Long empList2 = redisTemplate.opsForList().size("empList2");
        System.out.println("empList2 = " + empList2);
    }

    @Test
    void contextLoads10() {
        List list = redisTemplate.opsForList().range("empList2", 0, -1);
        System.out.println("list = " + list);
    }

    @Test
    void contextLoads11() {
        List<Map> all = empMapper.findAll();
        //遍历每个emp对象,把它加入到redis的list中
        //按照对象的id作为分值
        for (Map emp : all) {
            int id = (int) emp.get("id");
            redisTemplate.opsForZSet().add("empList3", emp, id);
        }
    }

    @Test
    void contextLoads12() {
//        Long empList3 = redisTemplate.opsForZSet().size("empList3");
//        System.out.println("empList3 = " + empList3);

        Set empzset = redisTemplate.opsForZSet().range("empList3", 0, 3);
        System.out.println("empzset = " + empzset);

//        List<Map> all = empMapper.findAll();
//        for (Map emp : all) {
//
//        }
    }

    @Test
    void contextLoads13() {
        PageInfo page = empService.getPage(1);
    }

    //测试添加
    @Test
    void contextLoads14() {
        Map map = new HashMap();
        map.put("name", "daiwenxiang");
        map.put("did", "1");

        int add = empService.add(map);
        System.out.println("add = " + add);
    }

    //测试删除
    @Test
    void contextLoads15() {
        int delete = empService.delete(100);
        System.out.println("delete = " + delete);
    }

    //测试修改
    @Test
    void contextLoads16() {
        Map map = new HashMap();
        map.put("id", "99");
        map.put("name", "ddd");
        map.put("did", "1");

        int update = empService.update(map);
        System.out.println("update = " + update);
    }

    @Test
    void contextLoads17() {

    }

    @Test
    void contextLoads18() {

    }

    @Test
    void contextLoads19() {

    }

    @Test
    void contextLoads20() {

    }

    @Test
    void contextLoads21() {

    }

    @Test
    void contextLoads22() {

    }

    @Test
    void contextLoads23() {

    }
}
