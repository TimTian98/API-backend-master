package com.yupi.jinapi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yupi.jinapicommon.model.entity.InterfaceInfo;

/**
* @author SanJin
* @description 针对表【interface_info(接口信息表)】的数据库操作Service
* @createDate 2023-09-10 04:32:04
*/
public interface InterfaceInfoService extends IService<InterfaceInfo> {
    /**
     * 校验
     *
     * @param interfaceInfo
     * @param add
     */
    void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add);
}
