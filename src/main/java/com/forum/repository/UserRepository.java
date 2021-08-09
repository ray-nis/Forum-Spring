package com.forum.repository;

import com.forum.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUserName(String userName);

    @Query("select p from User p left join fetch p.posts where p.userName = :userName")
    Optional<User> findUserWithPostsByUserName(String userName);
}
