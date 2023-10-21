package com.yupi.jinapicommon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yupi.jinapicommon.model.entity.UserInterfaceInfo;

/**
* @author SanJin
* @description 针对表【user_interface_info(用户调用接口信息)】的数据库操作Service
* @createDate 2023-10-04 23:29:40
*/
public interface InnerUserInterfaceInfoService {

    /**
     * 调用接口次数统计
     * @param interfaceInfoId
     * @param userId
     * @return
     */
    boolean invokeCount(long interfaceInfoId,long userId);
}
