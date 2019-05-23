package com.leyou.user.api.inner;

import com.leyou.user.pojo.User;

public interface IUerService {

    /**
     * 校验user数据的合法性
     * @param data
     * @param type
     * @return
     */
    Boolean checkUser(String data, Integer type);

    /**
     * 发送短信验证码
     * @param phone
     */
    void sendVerifyCode(String phone);

    /**
     * 注册
     * @param user
     * @param code
     * @return
     */
    Boolean register(User user, String code);

    /**
     * 根据username和password查询用户
     * @param username
     * @param password
     * @return
     */
    User queryUser(String username, String password);
}
