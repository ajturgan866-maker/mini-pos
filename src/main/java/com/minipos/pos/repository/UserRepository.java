package com.minipos.pos.repository;

import com.minipos.pos.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Твой старый метод findByUsername теперь пишется в одну строку:
    Optional<User> findByUsername(String username);

}