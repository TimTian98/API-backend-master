package com.yupi.jinapicommon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yupi.jinapicommon.model.entity.InterfaceInfo;


/**
* @author SanJin
* @description 针对表【interface_info(接口信息表)】的数据库操作Service
* @createDate 2023-09-10 04:32:04
*/
public interface InnerInterfaceInfoService {
    /**
     * 从数据库中查询接口是否存在
     * @param path
     * @param method
     * @return
     */
    InterfaceInfo getInterfaceInfo(String path,String method);
}
