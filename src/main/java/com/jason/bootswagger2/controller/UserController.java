package com.jason.bootswagger2.controller;

import com.jason.bootswagger2.entity.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Api(tags = "用户管理相关接口")
@RestController
@RequestMapping("/user")
public class UserController {
    Logger logger = LoggerFactory.getLogger(UserController.class);

    public static List<User> users = new ArrayList<User>();

    public static int id = 0;

    @PostMapping("/")
    @ApiOperation("添加用户接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "用户名", defaultValue = "王五"),
            @ApiImplicitParam(name = "address", value = "地址", defaultValue = "重庆", required = true)
    })
    public User addUser(String name, @RequestParam(required = true) String address) {
        User user = new User();
        user.setId(id);
        user.setUsername(name);
        user.setAddress(address);
        users.add(user);
        id++;
        return user;
    }

    @GetMapping("/{id}")
    @ApiOperation("根据Id查询用户接口")
    @ApiImplicitParam(name = "id", value = "用户id", defaultValue = "0", required = true)
    public User getUser(@PathVariable Integer id) {
        return users.get(id);
    }

    @PutMapping("/")
    @ApiOperation("更新用户数据")
    public User update(@RequestBody User user) {
        User u = users.get(user.getId());
        u.setUsername(user.getUsername());
        u.setAddress(user.getAddress());
        return user;
    }
}
