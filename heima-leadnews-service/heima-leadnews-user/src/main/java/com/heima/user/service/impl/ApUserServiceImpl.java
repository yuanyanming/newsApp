package com.heima.user.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.user.dtos.LoginDto;
import com.heima.model.user.pojos.ApUser;
import com.heima.user.mapper.ApUserMapper;
import com.heima.user.service.ApUserService;
import com.heima.utils.common.AppJwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@Transactional
public class ApUserServiceImpl extends ServiceImpl<ApUserMapper, ApUser> implements ApUserService {


    /**
     * app端登录功能
     * @param dto
     * @return
     */
    @Override
    public ResponseResult login(LoginDto dto) {

        if(dto==null){
            return ResponseResult.errorResult(123,"123为空");
        }
        String phone = dto.getPhone();
        String password = dto.getPassword();
        Map<String, Object> map = new HashMap<>();
        //1. 用户登录，用户名和密码
        if(StringUtils.isNotBlank(phone)&&StringUtils.isNotBlank(password)){
            //1.1. 根据手机号查询用户信息
            ApUser dbUser = getOne(Wrappers.<ApUser>lambdaQuery().eq(ApUser::getPhone, phone));
            if(dbUser==null){
                return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST,"用户信息不存在");
            }
            //1.2. 比对密码
            String salt = dbUser.getSalt();
            String pswd = DigestUtils.md5DigestAsHex((password+salt).getBytes());
            if(!dbUser.getPassword().equals(pswd)){
                return ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_PASSWORD_ERROR,"密码错误");
            }

            //1.3. 返回数据，user和jwt
            String token = AppJwtUtil.getToken(dbUser.getId().longValue());

            map.put("token",token);
            //TODO 设置成null和空的区别
            dbUser.setSalt(null);
            dbUser.setPassword(null);
            map.put("user",dbUser);
        }else{
            //2. 不登录
            map.put("token",AppJwtUtil.getToken(0L));
        }

        return ResponseResult.okResult(map);
    }
}
