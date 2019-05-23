package com.leyou.user.api.outer;

import com.leyou.user.pojo.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


public interface UserApi {

    /**
     * 根据username和password查询用户
     * @param username
     * @param password
     * @return
     */
    @PostMapping("query")
    public User queryUser(@RequestParam("username") String username, @RequestParam("password") String password);
}
