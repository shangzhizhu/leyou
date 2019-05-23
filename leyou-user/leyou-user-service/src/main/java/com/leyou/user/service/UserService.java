package com.leyou.user.service;

import com.leyou.common.utils.NumberUtils;
import com.leyou.user.api.inner.IUerService;
import com.leyou.user.mapper.UserMapper;
import com.leyou.user.pojo.User;
import com.leyou.user.utils.CodecUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class UserService implements IUerService {

    private static final String LEYOU_USER_VERIFY_PREFIX = "user:verify:";

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private AmqpTemplate amqpTemplate;

    /**
     * 校验user数据的合法性
     * @param data
     * @param type
     * @return
     */
    @Override
    public Boolean checkUser(String data, Integer type) {

        User user = new User();

        if (type == 1)
            user.setUsername(data);

        if (type == 2)
            user.setPhone(data);

        return userMapper.selectCount(user) == 0;
    }

    /**
     * 发送短信验证码
     * @param phone
     */
    @Override
    public void sendVerifyCode(String phone) {

        // 生成验证码
        String verifyCode = NumberUtils.generateCode(6);

        // 保存到redis中
        redisTemplate.opsForValue().set(LEYOU_USER_VERIFY_PREFIX + phone, verifyCode, 5, TimeUnit.MINUTES);

        // 封装map
        Map<String, String> map = new HashMap<>();
        map.put("phone", phone);
        map.put("code", verifyCode);

        // 发送消息
        amqpTemplate.convertAndSend("LEYOU-SMS-EXCHANGE", "sms.verifycode", map);
    }

    /**
     * 注册
     * @param user
     * @param code
     * @return
     */
    @Override
    public Boolean register(User user, String code) {

        // 校验验证码
        String redisCode = redisTemplate.opsForValue().get(LEYOU_USER_VERIFY_PREFIX + user.getPhone());

        if (!StringUtils.equals(redisCode, code))
            return false;

        // 密码加盐
        String salt = CodecUtils.generateSalt();
        user.setPassword(CodecUtils.md5Hex(user.getPassword(), salt));

        // 保存到数据库并删除redis中的验证码
        user.setId(null);
        user.setSalt(salt);
        user.setCreated(new Date());

        Boolean flag = userMapper.insertSelective(user) == 1;

        if (flag)
            redisTemplate.delete(LEYOU_USER_VERIFY_PREFIX + user.getPhone());

        return flag;
    }

    /**
     * 根据username和password查询用户
     * @param username
     * @param password
     * @return
     */
    @Override
    public User queryUser(String username, String password) {

        User user = new User();
        user.setUsername(username);

        User result = userMapper.selectOne(user);

        if (result == null)
            return null;

        // 比较密码是否正确
        if (!StringUtils.equals(result.getPassword(), CodecUtils.md5Hex(password, result.getSalt())))
            return null;

        return result;
    }
}
