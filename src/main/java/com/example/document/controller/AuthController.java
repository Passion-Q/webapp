package com.example.document.controller;

import com.example.document.dto.ChangePasswordRequest;
import com.example.document.dto.LoginRequest;
import com.example.document.dto.RegisterRequest;
import com.example.document.dto.ResetPasswordRequest;
import com.example.document.entity.User;
import com.example.document.service.DocumentService;
import com.example.document.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class AuthController {

    private final UserService userService;
    private final DocumentService documentService;

    public AuthController(UserService userService, DocumentService documentService) {
        this.userService = userService;
        this.documentService = documentService;
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("loginRequest", new LoginRequest());
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("registerRequest", new RegisterRequest());
        return "register";
    }

    @PostMapping("/register")
    public String doRegister(@Valid RegisterRequest request, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("registerRequest", request);
            return "register";
        }
        try {
            userService.register(request);
            return "redirect:/login?registered=true";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("registerRequest", request);
            return "register";
        }
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        User user = userService.getCurrentUser();
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        
        model.addAttribute("pendingReviewCount", documentService.getDocumentsByStatus("PENDING_REVIEW").size());
        model.addAttribute("approvedCount", documentService.getDocumentsByStatus("APPROVED").size());
        model.addAttribute("draftCount", documentService.getDocumentsByStatus("DRAFT").size());
        model.addAttribute("userCount", userService.getUserCount());
        
        return "dashboard";
    }

    @GetMapping("/reset-password")
    public String resetPassword(Model model) {
        model.addAttribute("resetPasswordRequest", new ResetPasswordRequest());
        return "reset-password";
    }

    @PostMapping("/reset-password")
    public String doResetPassword(@Valid ResetPasswordRequest request, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("resetPasswordRequest", request);
            return "reset-password";
        }
        try {
            userService.resetPassword(request.getUsername(), request.getNewPassword());
            return "redirect:/login?resetSuccess=true";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("resetPasswordRequest", request);
            return "reset-password";
        }
    }

    @GetMapping("/change-password")
    public String changePassword(Model model) {
        User user = userService.getCurrentUser();
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("changePasswordRequest", new ChangePasswordRequest());
        model.addAttribute("user", user);
        return "change-password";
    }

    @PostMapping("/change-password")
    public String doChangePassword(@Valid ChangePasswordRequest request, BindingResult result, Model model) {
        User user = userService.getCurrentUser();
        if (user == null) {
            return "redirect:/login";
        }
        
        if (result.hasErrors()) {
            model.addAttribute("changePasswordRequest", request);
            model.addAttribute("user", user);
            return "change-password";
        }
        
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            model.addAttribute("error", "两次输入的新密码不一致");
            model.addAttribute("changePasswordRequest", request);
            model.addAttribute("user", user);
            return "change-password";
        }
        
        try {
            userService.changePassword(user.getUsername(), request.getOldPassword(), request.getNewPassword());
            return "redirect:/logout?passwordChanged=true";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("changePasswordRequest", request);
            model.addAttribute("user", user);
            return "change-password";
        }
    }
}