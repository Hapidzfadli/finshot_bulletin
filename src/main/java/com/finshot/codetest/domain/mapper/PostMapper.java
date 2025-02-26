package com.finshot.codetest.domain.mapper;

import com.finshot.codetest.domain.model.Post;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PostMapper {
    @Select("SELECT * FROM posts ORDER BY created_at DESC")
    List<Post> getAllPosts();

    @Select("SELECT * FROM posts WHERE id = #{id}")
    Post getPostById(Long id);

    @Insert("INSERT INTO posts (title, content, author, password, views, is_deleted, created_at, updated_at) VALUES (#{title}, #{content}, #{author}, #{password}, #{views}, #{isDeleted}, #{createdAt}, #{updatedAt})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertPost(Post post);

    @Update("UPDATE posts SET title = #{title}, content = #{content}, author = #{author}, views = #{views}, is_deleted = #{isDeleted}, created_at = #{createdAt}, updated_at = #{updatedAt} WHERE id = #{id}")
    void updatePost(Post post);

    default void save(Post post) {
        if (post.getId() == null) {
            insertPost(post);
        } else {
            updatePost(post);
        }
    }
}
