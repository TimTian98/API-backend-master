package com.yupi.jinapi.service.impl.inner;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yupi.jinapi.common.ErrorCode;
import com.yupi.jinapi.exception.BusinessException;
import com.yupi.jinapi.mapper.InterfaceInfoMapper;
import com.yupi.jinapicommon.model.entity.InterfaceInfo;
import com.yupi.jinapicommon.service.InnerInterfaceInfoService;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

@DubboService
public class InnerInterfaceInfoServiceImpl implements InnerInterfaceInfoService {

    @Resource
    private InterfaceInfoMapper interfaceInfoMapper;
    @Override
    public InterfaceInfo getInterfaceInfo(String path, String method) {
        if(StringUtils.isAnyBlank(path,method)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("url",path);
        queryWrapper.eq("method",method);
        return interfaceInfoMapper.selectOne(queryWrapper);

    }
}
