package com.anteprj.push.repository;

import com.anteprj.entity.Push;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PushRepository extends JpaRepository<Push, Long> {
    Optional<Push> findByToken(String token);

    @Query("SELECT p.token FROM Push p")
    List<String> findAllToken();
}
