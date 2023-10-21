package com.yupi.jinapi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yupi.jinapi.common.ErrorCode;
import com.yupi.jinapi.exception.BusinessException;
import com.yupi.jinapi.exception.ThrowUtils;
import com.yupi.jinapi.mapper.InterfaceInfoMapper;
import com.yupi.jinapi.service.InterfaceInfoService;
import com.yupi.jinapicommon.model.entity.InterfaceInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
* @author SanJin
* @description 针对表【interface_info(接口信息表)】的数据库操作Service实现
* @createDate 2023-09-10 04:32:04
*/
@Service
public class InterfaceInfoServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfo>
    implements InterfaceInfoService{


    @Override
    public void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add) {
        if (interfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long id = interfaceInfo.getId();
        String name = interfaceInfo.getName();
        String url = interfaceInfo.getUrl();
        String requestHeader = interfaceInfo.getRequestHeader();
        String responseHeader = interfaceInfo.getResponseHeader();
        String method = interfaceInfo.getMethod();
        // 创建时，参数不能为空
        if (add) {
            ThrowUtils.throwIf(StringUtils.isAnyBlank(name, url, requestHeader,responseHeader,method), ErrorCode.PARAMS_ERROR);
        }
        // 有参数则校验
        if (StringUtils.isNotBlank(name) && name.length() > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "名称过长");
        }
        if (StringUtils.isNotBlank(url) && url.length() > 8192) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "地址过长");
        }
        if (StringUtils.isNotBlank(requestHeader) && requestHeader.length() > 8192) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求头过长");
        }
        if (StringUtils.isNotBlank(responseHeader) && responseHeader.length() > 8192) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "相应头过长");
        }
        if (StringUtils.isNotBlank(method) && method.length() > 8192) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求类型过长");
        }

    }

}




