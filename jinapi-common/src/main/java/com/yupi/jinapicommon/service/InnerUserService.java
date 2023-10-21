package com.yupi.jinapicommon.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.yupi.jinapicommon.model.entity.User;

/**
 * 用户服务
 *
 * @author yupi
 */
public interface InnerUserService  {
    /**
     * 从数据库中查看是否已经分配给用户密钥
     * @param accessKey
     * @return
     */
    User getInvokeUser(String accessKey);
}
