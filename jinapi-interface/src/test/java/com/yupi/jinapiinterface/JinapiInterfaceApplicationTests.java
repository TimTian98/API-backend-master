package com.yupi.jinapiinterface;

import com.yupi.jinapiclientsdk.client.JinApiClient;

import com.yupi.jinapiclientsdk.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class JinapiInterfaceApplicationTests {

    @Resource
    private JinApiClient jinApiClient;

    @Test
    void contextLoads() {
        String sanjin = jinApiClient.getNameByGet("sanjin");
        User user = new User();
		user.setUsername("zhangsanjin");
        String usernameByPost = jinApiClient.getUsernameByPost(user);
        System.out.println(sanjin);
        System.out.println(usernameByPost);
    }

}
