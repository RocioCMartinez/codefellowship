package com.codefellowship.codefellowship.repos;

import com.codefellowship.codefellowship.models.Posts;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Posts, Long> {
}
