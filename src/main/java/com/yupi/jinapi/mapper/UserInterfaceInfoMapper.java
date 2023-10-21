package com.yupi.jinapi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yupi.jinapicommon.model.entity.UserInterfaceInfo;

import java.util.List;

/**
* @author SanJin
* @description 针对表【user_interface_info(用户调用接口信息)】的数据库操作Mapper
* @createDate 2023-10-12 07:05:18
* @Entity com.yupi.jinapi.model.entity.UserInterfaceInfo
*/
public interface UserInterfaceInfoMapper extends BaseMapper<UserInterfaceInfo> {
    //select interfaceInfoId,sum(totalNum) as totalNum
    //from user_interface_info
    //group by interfaceInfoId
    //order by totalNum desc
    //limit 3;
    List<UserInterfaceInfo> listTopInvokeInterfaceInfo(int limit);
}




