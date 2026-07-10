package com.example.document.repository;

import com.example.document.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {

    List<Document> findByCreatorId(Long creatorId);

    List<Document> findByStatus(String status);

    List<Document> findByType(String type);

    List<Document> findByCreatorIdAndStatus(Long creatorId, String status);

    List<Document> findByStatusIn(List<String> statuses);

    List<Document> findByTitleContaining(String title);

    @Query("SELECT d FROM Document d LEFT JOIN FETCH d.creator LEFT JOIN FETCH d.reviewer WHERE d.id = :id")
    Optional<Document> findByIdWithCreatorAndReviewer(@Param("id") Long id);

    @Query("SELECT d FROM Document d LEFT JOIN FETCH d.creator LEFT JOIN FETCH d.reviewer")
    List<Document> findAllWithCreatorAndReviewer();

    @Query("SELECT d FROM Document d LEFT JOIN FETCH d.creator LEFT JOIN FETCH d.reviewer WHERE d.status = :status")
    List<Document> findByStatusWithCreatorAndReviewer(@Param("status") String status);

    @Query("SELECT d FROM Document d LEFT JOIN FETCH d.creator LEFT JOIN FETCH d.reviewer WHERE d.creator.id = :creatorId")
    List<Document> findByCreatorIdWithCreatorAndReviewer(@Param("creatorId") Long creatorId);
}