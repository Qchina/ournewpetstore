package org.csu.petstore.controller;


import jakarta.servlet.http.HttpSession;
import org.csu.petstore.entity.Category;
import org.csu.petstore.entity.Order;
import org.csu.petstore.service.AccountService;
import org.csu.petstore.service.CatalogService;
import org.csu.petstore.service.impl.CatalogServiceImpl;
import org.csu.petstore.vo.AccountVO;
import org.csu.petstore.vo.CategoryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/accounts")
@Validated
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private CatalogService catalogService;

    @Autowired
    HttpSession session;
    
    private static class ApiResponse<T> {
        private boolean success;
        private String message;
        private T data;
        
        public ApiResponse(boolean success, String message, T data) {
            this.success = success;
            this.message = message;
            this.data = data;
        }
        
        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public T getData() { return data; }
        public void setData(T data) { this.data = data; }
    }

    @GetMapping("/profile")
    public ApiResponse<AccountVO> getProfile() {
        AccountVO account = (AccountVO) session.getAttribute("account");
        if (account == null) {
            return new ApiResponse<>(false, "未登录", null);
        }
        return new ApiResponse<>(true, "获取成功", account);
    }

    @PostMapping("/login")
    public ApiResponse<AccountVO> login(@RequestParam("username") String username, @RequestParam("password") String password, @RequestParam("code") String code) {
        String captcha = (String) session.getAttribute("captcha");
        if (captcha == null || !captcha.equalsIgnoreCase(code)) {
            return new ApiResponse<>(false, "验证码错误", null);
        }
        int result = accountService.signonForm(username, password);
        if (result == 0) {
            AccountVO accountVO = accountService.getAccount(username);
            session.setAttribute("account", accountVO);
//            CategoryVO categoryVO = catalogService.getCategory(accountVO.getFavoriteGory());
//            session.setAttribute("myList", categoryVO.getProductList());
            return new ApiResponse<>(true, "登录成功", accountVO);
        } else if (result == 2) {
            return new ApiResponse<>(false, "密码错误", null);
        } else {
            return new ApiResponse<>(false, "用户名不存在", null);
        }
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout() {
        session.invalidate();
        return new ApiResponse<>(true, "退出成功", null);
    }

    @PostMapping("/register")
    public ApiResponse<AccountVO> register(@RequestBody AccountVO account) {
        if (account == null || account.getUsername() == null || account.getUsername().trim().isEmpty()) {
            return new ApiResponse<>(false, "用户名不能为空", null);
        }
        if (account.getPassword() == null || account.getPassword().trim().isEmpty()) {
            return new ApiResponse<>(false, "密码不能为空", null);
        }

        try {
            accountService.insertAccountInformation(account);
            session.setAttribute("account", account);
            return new ApiResponse<>(true, "注册成功", account);
        } catch (Exception e) {
            return new ApiResponse<>(false, "注册失败：" + e.getMessage(), null);
        }
    }

    @PutMapping("/profile")
    public ApiResponse<AccountVO> updateProfile(@RequestBody AccountVO account) {
        // 删除验证码验证逻辑
        String username = ((AccountVO) session.getAttribute("account")).getUsername();
        String password = ((AccountVO) session.getAttribute("account")).getPassword();
        account.setUsername(username);
        if (account.getPassword() == "") {
            account.setPassword(password);
        }
        accountService.updateAccountInformation(account);
        session.setAttribute("account", account);
        return new ApiResponse<>(true, "更新成功", account);
    }
}