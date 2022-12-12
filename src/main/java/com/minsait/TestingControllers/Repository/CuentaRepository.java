package com.minsait.TestingControllers.Repository;

import com.minsait.TestingControllers.Models.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CuentaRepository extends JpaRepository<Cuenta,Long> {
 //   @Query("select c from Cuenta c where c.persona=?1") //HQL
    Optional<Cuenta> findByPersona(String persona);
}
