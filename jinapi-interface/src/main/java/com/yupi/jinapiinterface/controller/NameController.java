package com.yupi.jinapiinterface.controller;

import com.yupi.jinapiclientsdk.model.User;
import com.yupi.jinapiclientsdk.utils.SignUtils;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 名称API
 *
 * @author San Jin
 */
@RestController()
@RequestMapping("/name")
public class NameController {
    @GetMapping("/get")
    public String getNameByGet(String name, HttpServletRequest request) {
        System.out.println(request.getHeader("yupi"));
        return "GET:你的名字" + name;
    }

    @PostMapping("/post")
    public String getNameByPost(@RequestParam String name) {
        return "POST:你的名字" + name;
    }

    @PostMapping("/user")
    public String getUsernameByPost(@RequestBody User user, HttpServletRequest request) {
//        String accessKey = request.getHeader("accessKey");
//        String nonce = request.getHeader("nonce");
//        String timestamp = request.getHeader("timestamp");
//        String sign = request.getHeader("sign");
//        String body = request.getHeader("body");
//        if (!accessKey.equals("sanjin")) {
//            throw new RuntimeException("无权限");
//        }
//        // todo 校验随机数，后端存储可以用hashmap或redis
//        if (Long.parseLong(nonce) > 10000) {
//            throw new RuntimeException("无权限");
//        }
//        // todo 校验时间

//        String serverSign = SignUtils.genSign(body, "aaa");
//        if (!sign.equals(serverSign)) {
//            throw new RuntimeException("无权限");
//        }
//        System.out.println(timestamp);

        return "POST 用户名是" + user.getUsername();
    }

}
