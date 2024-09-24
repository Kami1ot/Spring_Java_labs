package com.example.Barber_lab6.Repository;

import com.example.Barber_lab6.Models.Clients;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ClientsRepository extends JpaRepository<Clients, Long> {
    @Query("SELECT f FROM Clients f " +
            "WHERE (:initials IS NULL OR LOWER(f.initials) LIKE LOWER(CONCAT('%', :initials, '%'))) " +
            "AND (:start_date IS NULL OR f.sessionDateTime >= :start_date)" +
            "AND (:end_date is null or f.sessionDateTime <= :end_date)" +
            "AND (:service_name IS NULL OR f.service_name = :service_name)")
    List<Clients> findByParams(
            @Param("initials") String initials,
            @Param("start_date") LocalDateTime start_date,
            @Param("end_date") LocalDateTime end_date,
            @Param("service_name") String service_name,
            Sort sort);
    @Query("SELECT DATE(f.sessionDateTime), COUNT(f) FROM Clients f GROUP BY DATE(f.sessionDateTime)")
    List<Object[]> findClientIssueStats();

}
