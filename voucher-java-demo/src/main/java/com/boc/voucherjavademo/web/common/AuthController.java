package com.boc.voucherjavademo.web.common;

import com.boc.voucherjavademo.tools.AbstractController;
import com.train.base.utils.TokenUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Tag(name = "认证服务")
@RequestMapping("/bocsh-service-auth")
public class AuthController extends AbstractController {

    @Operation(summary = "用户认证",description = "用户认证")
    @PostMapping("/login")
    @ResponseStatus(code = HttpStatus.OK)
    public Map<String,Object> login(String user, String password, HttpServletResponse response) throws Exception {
        HashMap<String, Object> result = new HashMap<String, Object>();
        String givenRoles = "MSA_USER";
        String userName = "张三";
        String bankId = "06015";
        String bankName = "中国银行上海市分行营业部";
        String superBankName = "中国银行上海市分行";
        if (user.equals("admin")) {
            givenRoles = "MSA_USER,MSA_ADMIN";
            userName = "管理员";
            bankId = "00010";
            bankName = "中国银行上海市分行";
            superBankName = "中国银行上海市分行";

        }
        result.put("access_token", TokenUtil.buildAccessToken(user,userName,bankId,bankName,superBankName,givenRoles));
        result.put("refresh_token",TokenUtil.buildRefreshToken(user,userName,bankId,bankName,superBankName,givenRoles));
        return  result;

    }

    @Operation(summary = "用户信息查询",description = "用户信息查询")
    @GetMapping("/get_info")
    @ResponseStatus(code = HttpStatus.OK)
    public Map<String,Object> getInfo(String token) throws Exception {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name",TokenUtil.parseValueFromToken(token,"userName"));
        result.put("user_id",TokenUtil.parseValueFromToken(token,"userId"));
        result.put("bankName",TokenUtil.parseValueFromToken(token,"bankName"));
        result.put("superBankName",TokenUtil.parseValueFromToken(token,"superBankName"));
        List<String> roles = Arrays.asList(TokenUtil.parseValueFromToken(token, "roles").split(","));
        result.put("access",roles);
        return result;
    }
}
