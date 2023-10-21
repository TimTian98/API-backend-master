package com.yupi.jinapi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yupi.jinapicommon.model.entity.UserInterfaceInfo;

/**
* @author SanJin
* @description 针对表【user_interface_info(用户调用接口信息)】的数据库操作Service
* @createDate 2023-10-12 07:05:18
*/
public interface UserInterfaceInfoService extends IService<UserInterfaceInfo> {
    /**
     * 校验
     *
     * @param userinterfaceInfo
     * @param add
     */
    void validUserInterfaceInfo(UserInterfaceInfo userinterfaceInfo, boolean add);

    /**
     * 调用接口次数统计
     * @param interfaceInfoId
     * @param userId
     * @return
     */
    boolean invokeCount(long interfaceInfoId,long userId);

}
