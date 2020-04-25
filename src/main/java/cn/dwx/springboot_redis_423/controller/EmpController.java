package cn.dwx.springboot_redis_423.controller;

import cn.dwx.springboot_redis_423.servie.EmpService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@CrossOrigin
public class EmpController {
    @Resource
    EmpService empService;

    @GetMapping("/getByPage")
    public PageInfo getByPage(@RequestParam(defaultValue = "1") Integer pageNum) {
        return empService.getPage(pageNum);
    }

    @GetMapping("/findById")
    public Map findById(@RequestParam("id") Integer id) {
        return empService.findById(id);
    }

    @PostMapping("/add")
    public int add(@RequestParam Map map) {
        int add = empService.add(map);
        return add;
    }

    @DeleteMapping("/del")
    public int del(@RequestParam("id") Integer id) {
        int delete = empService.delete(id);
        return delete;
    }

    @PutMapping("/update")
    public int update(@RequestParam Map map) {
        return empService.update(map);
    }
}
