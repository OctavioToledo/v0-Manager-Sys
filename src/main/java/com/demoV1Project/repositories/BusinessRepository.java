package com.demoV1Project.repositories;

import com.demoV1Project.model.Business;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BusinessRepository extends JpaRepository<Business, Long> {

   // @Query("SELECT b FROM Business b LEFT JOIN FETCH b.address LEFT JOIN FETCH b.category")
   //  List<Business> findAllWithRelations();
   @Query("SELECT b FROM Business b " +
           "LEFT JOIN FETCH b.address " +
           "LEFT JOIN FETCH b.category")
   List<Business> findAllWithRelations();
}
