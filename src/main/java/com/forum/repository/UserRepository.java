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

    Optional<User> findByEmailIgnoreCase(String email);
    Optional<User> findByUserNameIgnoreCase(String userName);

    @Query("select u from User u LEFT JOIN FETCH u.posts where u.userName = :userName")
    Optional<User> findUserWithPostsByUserName(String userName);

    @Query("select u from User u LEFT JOIN FETCH u.posts where u.email = :email")
    Optional<User> findUserWithPostsByEmail(String email);

    @Query("select u from User u LEFT JOIN FETCH u.posts where u.id = :id")
    Optional<User> findUserWithPostsById(Long id);
}
