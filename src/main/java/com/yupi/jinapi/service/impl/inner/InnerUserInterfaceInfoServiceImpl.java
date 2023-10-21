package com.yupi.jinapi.service.impl.inner;

import com.yupi.jinapi.service.UserInterfaceInfoService;
import com.yupi.jinapicommon.model.entity.UserInterfaceInfo;
import com.yupi.jinapicommon.service.InnerUserInterfaceInfoService;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;
@DubboService
public class InnerUserInterfaceInfoServiceImpl implements InnerUserInterfaceInfoService {
    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;

    @Override
    public boolean invokeCount(long interfaceInfoId, long userId) {
        return userInterfaceInfoService.invokeCount(interfaceInfoId,userId);
    }
}
