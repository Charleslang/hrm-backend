package com.dysy.bysj.controller;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.dysy.bysj.bo.RoleBO;
import com.dysy.bysj.bo.UnitBO;
import com.dysy.bysj.common.Constants;
import com.dysy.bysj.common.Result;
import com.dysy.bysj.common.enums.UnitEnum;
import com.dysy.bysj.common.exception.BusinessException;
import com.dysy.bysj.common.exception.ClientException;
import com.dysy.bysj.common.exception.IncorrectVerfyCodeException;
import com.dysy.bysj.common.exception.UnkownAccountException;
import com.dysy.bysj.entity.Unit;
import com.dysy.bysj.entity.User;
import com.dysy.bysj.service.BusinessService;
import com.dysy.bysj.service.LoginService;
import com.dysy.bysj.service.MenuService;
import com.dysy.bysj.service.RoleService;
import com.dysy.bysj.service.UnitService;
import com.dysy.bysj.service.UserService;
import com.dysy.bysj.util.CommonUtils;
import com.dysy.bysj.util.EncrypUtils;
import com.dysy.bysj.util.MailUtils;
import com.dysy.bysj.util.RedisUtils;
import com.dysy.bysj.util.SMSUtils;
import com.dysy.bysj.vo.RoleMenuVO;
import com.dysy.bysj.vo.RoleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import java.text.MessageFormat;
import java.util.Objects;

/**
 * @author: Dai Junfeng
 * @create: 2021-02-05
 */
@RestController
@RequestMapping("/business")
@CrossOrigin
@Validated
public class BusinessController {

    @Autowired
    private UserService userService;

    @Autowired
    private UnitService unitService;

    @Autowired
    private BusinessService businessService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private MailUtils mailUtils;

    @Autowired
    private SMSUtils smsUtils;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private EncrypUtils encrypUtils;

    @PostMapping("/code")
    public Result getVerfiedCode(@NotBlank(message = "手机号或邮箱不能为空") String account, String register) {
        String verfyCode = "";
        if (CommonUtils.isValidatedEmail(account)) {
            existAccount(account, register);

            verfyCode = CommonUtils.generateNumber(Constants.APP_VERIFY_CODE_LENGTH);
            String mailHtml = MessageFormat.format(Constants.MAIL_HTML, verfyCode);
            mailUtils.sendHtmlMail(Constants.MAIL_SUBJECT, mailHtml, account);
            System.out.println(verfyCode);
            redisUtils.set(Constants.VERYFY_CODE_CACHE_PREFIX + account, verfyCode,
                    Constants.VERYFY_CODE_EXPIRE_TIME);
            return Result.ok();
        } else if (CommonUtils.isValidatedPhone(account)) {
            existAccount(account, register);
            verfyCode = CommonUtils.generateNumber(Constants.APP_VERIFY_CODE_LENGTH);
            String code = smsUtils.sendSms(verfyCode, account);
            if ("ok".equals(code)) {
                redisUtils.set(Constants.VERYFY_CODE_CACHE_PREFIX + account, verfyCode,
                        Constants.VERYFY_CODE_EXPIRE_TIME);
                return Result.ok();
            } else {
                throw new RuntimeException("服务器出错, 短信发送失败");
            }
        } else {
            throw new BusinessException("手机号或邮箱格式错误");
        }
    }

    private void existAccount(String account, String register) {
        boolean existUser = userService.existUser(account);
        if (StringUtils.isNotBlank(register) && existUser) {
            throw new ClientException("该账号已被注册");
        }
        if (StringUtils.isBlank(register) && !existUser) {
            throw new ClientException("账号不存在");
        }
    }

    @PostMapping("/register")
    public Result regist(@NotBlank(message = "公司名称不能为空") String unitName,
                         @NotBlank(message = "手机号不能为空") String telephone,
                         @NotBlank(message = "验证码不能为空") String code,
                         @NotBlank(message = "密码不能为空") String password) {
        String veryfyCode = (String)redisUtils.get(Constants.VERYFY_CODE_CACHE_PREFIX + telephone);
        if (Objects.equals(veryfyCode, code)) {
            boolean exist = unitService.existUnit(unitName);
            if (!exist) {
                Unit unit = new Unit();
                unit.setName(unitName);

                password = encrypUtils.base64Decode(password);
                String salt = encrypUtils.getSalt();
                password = encrypUtils.encryp(Constants.APP_ALGORITHM_NAME, password, salt,
                        Constants.APP_SECRET_HASH_ITERATIONS);

                User user = new User();
                user.setTelephone(telephone);
                user.setPassword(password);
                user.setSalt(encrypUtils.base64Encode(salt));
                user.setEnable(true);
                boolean success = businessService.saveSimpleUser(unit, user);
                if (success) {
                    return Result.ok();
                }
                throw new BusinessException("注册失败");
            } else {
                throw new ClientException("公司名称已被使用");
            }

        }
        throw new IncorrectVerfyCodeException();
    }

    @PutMapping("/unitanduser")
    public Result updateUnitInfo(@RequestBody UnitBO unitBO) {
        businessService.updateUnitInfo(unitBO);
        return Result.ok();
    }

    @PostMapping("/unitanduser")
    public Result addUnitAndUser(@RequestBody UnitBO unitBO) {
        businessService.addUnitAndUser(unitBO);
        return Result.ok();

    }

    @PutMapping("/roleandmenu")
    public Result updateRole(@RequestBody RoleBO roleBO) {
        businessService.updateRoleInfo(roleBO);
        return Result.ok();
    }

    @PostMapping("/rm")
    public Result addRoleMenu(@RequestBody RoleBO roleBO) {
        businessService.addRoleMenu(roleBO);
        return Result.ok();
    }
}
