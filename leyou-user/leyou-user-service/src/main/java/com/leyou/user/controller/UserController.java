package com.leyou.user.controller;

import com.leyou.user.api.inner.IUerService;
import com.leyou.user.pojo.User;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.regex.Pattern;

@Controller
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    private static final String PHONE_REGEXP = "^((13[0-9])|(15[^4])|(18[0,2,3,5-9])|(17[0-8])|(147))\\d{8}$";

    @Autowired
    private IUerService uerService;

    /**
     * 校验user数据的合法性
     * @param data
     * @param type
     * @return
     */
    @GetMapping("check/{data}/{type}")
    public ResponseEntity<Boolean> checkUser(@PathVariable("data") String data, @PathVariable("type") Integer type){

        LOGGER.info("request-log: (GET) check/" + data + "/" + type);

        if (StringUtils.isBlank(data) || (type != 1 && type != 2))
            return ResponseEntity.badRequest().build();

        Boolean flag = uerService.checkUser(data, type);

        return ResponseEntity.ok(flag);
    }

    /**
     * 发送短信验证码
     * @param phone
     */
    @PostMapping("code")
    public ResponseEntity<Void> sendVerifyCode(@RequestParam("phone") String phone){

        LOGGER.info("request-log: (POST) code?phone=" + phone);

        if (!Pattern.matches(PHONE_REGEXP, phone))
            return ResponseEntity.badRequest().build();

        uerService.sendVerifyCode(phone);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 注册
     * @param user
     * @param code
     * @return
     */
    @PostMapping("register")
    public ResponseEntity<Void> register(@Valid User user, @RequestParam("code") String code){

        LOGGER.info("request-log: (POST) register?username=" + user.getUsername() + "&password=" + user.getPassword()
                                                                            + "&phone=" + user.getPhone() + "&code=" + code);

        Boolean flag = uerService.register(user, code);

        if (flag == null || !flag)
            return ResponseEntity.badRequest().build();

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 根据username和password查询用户
     * @param username
     * @param password
     * @return
     */
    @PostMapping("query")
    public ResponseEntity<User> queryUser(@RequestParam("username") String username, @RequestParam("password") String password){

        LOGGER.info("request-log: (POST) query?username=" + username + "&password=" + password);

        if (username == null || password == null)
            return ResponseEntity.badRequest().build();

        User user = uerService.queryUser(username, password);

        if (user == null)
            return ResponseEntity.badRequest().build();

        return ResponseEntity.ok(user);
    }
}
