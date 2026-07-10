package com.example.document.controller;

import com.example.document.dto.DocumentRequest;
import com.example.document.dto.LoginRequest;
import com.example.document.dto.RegisterRequest;
import com.example.document.dto.ReviewRequest;
import com.example.document.entity.Document;
import com.example.document.entity.User;
import com.example.document.security.JwtTokenProvider;
import com.example.document.service.DocumentService;
import com.example.document.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final UserService userService;
    private final DocumentService documentService;
    private final JwtTokenProvider tokenProvider;

    public ApiController(UserService userService, DocumentService documentService, 
                         JwtTokenProvider tokenProvider) {
        this.userService = userService;
        this.documentService = documentService;
        this.tokenProvider = tokenProvider;
    }

    @PostMapping("/auth/login")
    public ResponseEntity<Map<String, Object>> login(@Valid @RequestBody LoginRequest request) {
        Authentication authentication = userService.login(request);
        String token = tokenProvider.generateToken((UserDetails) authentication.getPrincipal());
        
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("message", "登录成功");
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("/auth/register")
    public ResponseEntity<Map<String, Object>> register(@Valid @RequestBody RegisterRequest request) {
        try {
            User user = userService.register(request);
            
            Map<String, Object> response = new HashMap<>();
            response.put("id", user.getId());
            response.put("username", user.getUsername());
            response.put("message", "注册成功");
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/documents")
    public ResponseEntity<List<Document>> getDocuments() {
        return ResponseEntity.ok(documentService.getAllDocuments());
    }

    @GetMapping("/documents/my")
    public ResponseEntity<List<Document>> getMyDocuments() {
        return ResponseEntity.ok(documentService.getMyDocuments());
    }

    @GetMapping("/documents/{id}")
    public ResponseEntity<Document> getDocument(@PathVariable Long id) {
        Document document = documentService.getDocumentById(id);
        if (document == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(document);
    }

    @PostMapping("/documents")
    public ResponseEntity<?> createDocument(@Valid @RequestBody DocumentRequest request) {
        try {
            Document document = documentService.createDocument(request);
            return ResponseEntity.ok(document);
        } catch (RuntimeException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/documents/{id}/submit")
    public ResponseEntity<?> submitDocument(@PathVariable Long id) {
        try {
            Document document = documentService.submitForReview(id);
            return ResponseEntity.ok(document);
        } catch (RuntimeException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/documents/{id}/review")
    public ResponseEntity<?> reviewDocument(@PathVariable Long id, 
                                            @Valid @RequestBody ReviewRequest request) {
        try {
            Document document = documentService.reviewDocument(id, request);
            return ResponseEntity.ok(document);
        } catch (RuntimeException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/documents/{id}")
    public ResponseEntity<?> deleteDocument(@PathVariable Long id) {
        try {
            documentService.deleteDocument(id);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "删除成功");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}