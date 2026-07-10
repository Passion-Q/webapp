package com.example.document.controller;

import com.example.document.dto.DocumentRequest;
import com.example.document.dto.ReviewRequest;
import com.example.document.entity.Document;
import com.example.document.entity.User;
import com.example.document.service.DocumentService;
import com.example.document.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/documents")
public class DocumentController {

    private final DocumentService documentService;
    private final UserService userService;

    public DocumentController(DocumentService documentService, UserService userService) {
        this.documentService = documentService;
        this.userService = userService;
    }

    @GetMapping
    public String listDocuments(Model model) {
        User user = userService.getCurrentUser();
        if (user == null) {
            return "redirect:/login";
        }
        
        List<Document> documents = documentService.getAllDocuments();
        model.addAttribute("documents", documents);
        model.addAttribute("user", user);
        return "documents/list";
    }

    @GetMapping("/my")
    public String myDocuments(Model model) {
        User user = userService.getCurrentUser();
        if (user == null) {
            return "redirect:/login";
        }
        
        List<Document> documents = documentService.getMyDocuments();
        model.addAttribute("documents", documents);
        model.addAttribute("user", user);
        return "documents/list";
    }

    @GetMapping("/pending")
    public String pendingDocuments(Model model) {
        User user = userService.getCurrentUser();
        if (user == null) {
            return "redirect:/login";
        }
        
        List<Document> documents = documentService.getPendingDocuments();
        model.addAttribute("documents", documents);
        model.addAttribute("user", user);
        return "documents/list";
    }

    @GetMapping("/create")
    public String createDocumentForm(Model model) {
        User user = userService.getCurrentUser();
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("documentRequest", new DocumentRequest());
        model.addAttribute("user", user);
        return "documents/create";
    }

    @PostMapping("/create")
    public String createDocument(@Valid DocumentRequest request, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("documentRequest", request);
            return "documents/create";
        }
        try {
            documentService.createDocument(request);
            return "redirect:/documents/my";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("documentRequest", request);
            return "documents/create";
        }
    }

    @GetMapping("/{id}")
    public String viewDocument(@PathVariable Long id, Model model) {
        User user = userService.getCurrentUser();
        if (user == null) {
            return "redirect:/login";
        }
        Document document = documentService.getDocumentById(id);
        if (document == null) {
            return "redirect:/documents";
        }
        model.addAttribute("document", document);
        model.addAttribute("user", user);
        return "documents/view";
    }

    @GetMapping("/{id}/submit")
    public String submitDocument(@PathVariable Long id) {
        User user = userService.getCurrentUser();
        if (user == null) {
            return "redirect:/login";
        }
        try {
            documentService.submitForReview(id);
        } catch (RuntimeException e) {
            System.err.println(e.getMessage());
        }
        return "redirect:/documents/my";
    }

    @GetMapping("/{id}/review")
    public String reviewDocumentForm(@PathVariable Long id, Model model) {
        User user = userService.getCurrentUser();
        if (user == null) {
            return "redirect:/login";
        }
        if (!"ADMIN".equals(user.getRole())) {
            return "redirect:/documents";
        }
        Document document = documentService.getDocumentById(id);
        if (document == null) {
            return "redirect:/documents";
        }
        model.addAttribute("document", document);
        model.addAttribute("reviewRequest", new ReviewRequest());
        model.addAttribute("user", user);
        return "documents/review";
    }

    @PostMapping("/{id}/review")
    public String reviewDocument(@PathVariable Long id, @Valid ReviewRequest request, 
                                  BindingResult result, Model model) {
        User user = userService.getCurrentUser();
        if (user == null || !"ADMIN".equals(user.getRole())) {
            return "redirect:/documents";
        }
        
        Document document = documentService.getDocumentById(id);
        if (document == null) {
            return "redirect:/documents";
        }
        
        if (result.hasErrors()) {
            model.addAttribute("document", document);
            model.addAttribute("reviewRequest", request);
            return "documents/review";
        }
        
        try {
            documentService.reviewDocument(id, request);
            return "redirect:/documents";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("document", document);
            model.addAttribute("reviewRequest", request);
            return "documents/review";
        }
    }

    @GetMapping("/{id}/delete")
    public String deleteDocument(@PathVariable Long id) {
        User user = userService.getCurrentUser();
        if (user == null) {
            return "redirect:/login";
        }
        try {
            documentService.deleteDocument(id);
        } catch (RuntimeException e) {
            System.err.println(e.getMessage());
        }
        return "redirect:/documents/my";
    }
}