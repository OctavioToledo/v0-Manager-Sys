package com.demoV1Project.domain.repository;

import com.demoV1Project.domain.model.Business;
import com.demoV1Project.domain.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BusinessRepository extends JpaRepository<Business, Long> {

        // @Query("SELECT b FROM Business b LEFT JOIN FETCH b.address LEFT JOIN FETCH
        // b.category")
        // List<Business> findAllWithRelations();
        @Query("SELECT b FROM Business b " +
                        "LEFT JOIN FETCH b.address " +
                        "LEFT JOIN FETCH b.category")
        List<Business> findAllWithRelations();

        @Query("SELECT b FROM Business b " +
                        "LEFT JOIN FETCH b.address a " +
                        "LEFT JOIN FETCH b.category c " +
                        "WHERE (:name IS NULL OR :name = '' OR LOWER(b.name) LIKE LOWER(CONCAT('%', :name, '%'))) " +
                        "AND (:categoryName IS NULL OR :categoryName = '' OR LOWER(c.name) LIKE LOWER(CONCAT('%', :categoryName, '%'))) "
                        +
                        "AND (:city IS NULL OR :city = '' OR LOWER(a.city) LIKE LOWER(CONCAT('%', :city, '%')))")
        List<Business> searchBusinesses(@Param("name") String name,
                        @Param("categoryName") String categoryName,
                        @Param("city") String city);

}
