package com.example.document.service;

import com.example.document.dto.DocumentRequest;
import com.example.document.dto.ReviewRequest;
import com.example.document.entity.Document;
import com.example.document.entity.User;
import com.example.document.repository.DocumentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final UserService userService;

    public DocumentService(DocumentRepository documentRepository, UserService userService) {
        this.documentRepository = documentRepository;
        this.userService = userService;
    }

    @Transactional
    public Document createDocument(DocumentRequest request) {
        User currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            throw new RuntimeException("用户未登录");
        }

        Document document = new Document();
        document.setTitle(request.getTitle());
        document.setDocumentNo(request.getDocumentNo());
        document.setType(request.getType());
        document.setContent(request.getContent());
        document.setPriority(request.getPriority());
        document.setStatus("DRAFT");
        document.setCreator(currentUser);

        return documentRepository.save(document);
    }

    public Document getDocumentById(Long id) {
        return documentRepository.findByIdWithCreatorAndReviewer(id).orElse(null);
    }

    public List<Document> getAllDocuments() {
        return documentRepository.findAllWithCreatorAndReviewer();
    }

    public List<Document> getDocumentsByStatus(String status) {
        return documentRepository.findByStatusWithCreatorAndReviewer(status);
    }

    public List<Document> getPendingDocuments() {
        return documentRepository.findByStatusWithCreatorAndReviewer("PENDING_REVIEW");
    }

    public List<Document> getDocumentsByCreator(Long creatorId) {
        return documentRepository.findByCreatorIdWithCreatorAndReviewer(creatorId);
    }

    public List<Document> getMyDocuments() {
        User currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            throw new RuntimeException("用户未登录");
        }
        return documentRepository.findByCreatorIdWithCreatorAndReviewer(currentUser.getId());
    }

    @Transactional
    public Document submitForReview(Long documentId) {
        Document document = documentRepository.findByIdWithCreatorAndReviewer(documentId).orElseThrow(() -> 
                new RuntimeException("文档不存在"));
        
        User currentUser = userService.getCurrentUser();
        if (!document.getCreator().getId().equals(currentUser.getId())) {
            throw new RuntimeException("无权操作此文档");
        }

        document.setStatus("PENDING_REVIEW");
        return documentRepository.save(document);
    }

    @Transactional
    public Document reviewDocument(Long documentId, ReviewRequest request) {
        Document document = documentRepository.findByIdWithCreatorAndReviewer(documentId).orElseThrow(() -> 
                new RuntimeException("文档不存在"));
        
        User reviewer = userService.getCurrentUser();
        if (!"PENDING_REVIEW".equals(document.getStatus())) {
            throw new RuntimeException("文档状态不允许审核");
        }

        document.setReviewer(reviewer);
        document.setReviewComment(request.getComment());
        document.setReviewedAt(LocalDateTime.now());

        if ("APPROVE".equalsIgnoreCase(request.getResult())) {
            document.setStatus("APPROVED");
        } else {
            document.setStatus("REJECTED");
        }

        return documentRepository.save(document);
    }

    @Transactional
    public void deleteDocument(Long documentId) {
        Document document = documentRepository.findById(documentId).orElseThrow(() -> 
                new RuntimeException("文档不存在"));
        
        User currentUser = userService.getCurrentUser();
        if (!document.getCreator().getId().equals(currentUser.getId())) {
            throw new RuntimeException("无权删除此文档");
        }

        documentRepository.delete(document);
    }
}