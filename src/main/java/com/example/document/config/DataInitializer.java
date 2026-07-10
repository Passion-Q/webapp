package com.example.document.config;

import com.example.document.dto.RegisterRequest;
import com.example.document.entity.DocumentType;
import com.example.document.entity.User;
import com.example.document.repository.DocumentTypeRepository;
import com.example.document.repository.UserRepository;
import com.example.document.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(UserService userService, DocumentTypeRepository documentTypeRepository, 
                                      UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (!documentTypeRepository.existsByCode("NOTICE")) {
                DocumentType notice = new DocumentType();
                notice.setCode("NOTICE");
                notice.setName("通知");
                notice.setDescription("用于发布通知事项");
                documentTypeRepository.save(notice);
            }
            
            if (!documentTypeRepository.existsByCode("REPORT")) {
                DocumentType report = new DocumentType();
                report.setCode("REPORT");
                report.setName("报告");
                report.setDescription("用于向上级汇报工作");
                documentTypeRepository.save(report);
            }
            
            if (!documentTypeRepository.existsByCode("REQUEST")) {
                DocumentType request = new DocumentType();
                request.setCode("REQUEST");
                request.setName("请示");
                request.setDescription("用于向上级请求指示");
                documentTypeRepository.save(request);
            }
            
            if (!documentTypeRepository.existsByCode("REPLY")) {
                DocumentType reply = new DocumentType();
                reply.setCode("REPLY");
                reply.setName("批复");
                reply.setDescription("用于答复下级请示");
                documentTypeRepository.save(reply);
            }
            
            if (!documentTypeRepository.existsByCode("LETTER")) {
                DocumentType letter = new DocumentType();
                letter.setCode("LETTER");
                letter.setName("函");
                letter.setDescription("用于平行单位之间的公务往来");
                documentTypeRepository.save(letter);
            }
            
            if (!documentTypeRepository.existsByCode("DECISION")) {
                DocumentType decision = new DocumentType();
                decision.setCode("DECISION");
                decision.setName("决定");
                decision.setDescription("用于对重要事项作出决策");
                documentTypeRepository.save(decision);
            }
            
            if (!documentTypeRepository.existsByCode("ORDER")) {
                DocumentType order = new DocumentType();
                order.setCode("ORDER");
                order.setName("命令");
                documentTypeRepository.save(order);
            }
            
            if (!documentTypeRepository.existsByCode("ANNOUNCEMENT")) {
                DocumentType announcement = new DocumentType();
                announcement.setCode("ANNOUNCEMENT");
                announcement.setName("公告");
                documentTypeRepository.save(announcement);
            }
            
            User admin = userRepository.findByUsername("admin").orElse(null);
            if (admin == null) {
                admin = new User();
                admin.setUsername("admin");
            }
            admin.setPassword(passwordEncoder.encode("Admin@123"));
            admin.setRealName("管理员");
            admin.setEmail("admin@example.com");
            admin.setRole("ADMIN");
            admin.setEnabled(true);
            userRepository.save(admin);
            System.out.println("管理员用户已创建/更新");
            
            User user = userRepository.findByUsername("user").orElse(null);
            if (user == null) {
                user = new User();
                user.setUsername("user");
            }
            user.setPassword(passwordEncoder.encode("User@123"));
            user.setRealName("普通用户");
            user.setEmail("user@example.com");
            user.setRole("USER");
            user.setEnabled(true);
            userRepository.save(user);
            System.out.println("普通用户已创建/更新");
        };
    }
}