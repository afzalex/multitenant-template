package com.example.multitenancy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.multitenancy.entity.SampleData;

@Repository
public interface SampleDataRepository extends JpaRepository<SampleData, Long> {
}
