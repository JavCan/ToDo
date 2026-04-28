package com.itesm.infrastructure.mapper;

import com.itesm.domain.models.Comment;
import com.itesm.infrastructure.persistence.entity.CommentEntity;

public class CommentMapper {
    public static CommentEntity toEntity(Comment comment) {
        if (comment == null) return null;
        CommentEntity entity = new CommentEntity();
        entity.setId(comment.getUuid());
        entity.setContent(comment.getContent());
        entity.setCreatedAt(comment.getCreatedAt());
        if (comment.getAuthor() != null) {
            entity.setAuthor(UserMapper.toEntity(comment.getAuthor()));
        }
        return entity;
    }

    public static Comment toDomain(CommentEntity entity) {
        if (entity == null) return null;
        Comment comment = new Comment();
        comment.setUuid(entity.getId());
        comment.setContent(entity.getContent());
        comment.setCreatedAt(entity.getCreatedAt());
        if (entity.getAuthor() != null) {
            comment.setAuthor(UserMapper.toDomain(entity.getAuthor()));
        }
        return comment;
    }
}
