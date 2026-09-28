package com.example.transfer_api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.transfer_api.entity.Transfer;

public interface TransferRepository extends JpaRepository<Transfer, Long> {

    @EntityGraph(attributePaths = {"fromAccount", "toAccount"})
    Optional<Transfer> findById(Long id);
}
