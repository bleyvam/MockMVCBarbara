package com.minsait.TestingControllers.Repository;

import com.minsait.TestingControllers.Models.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CuentaRepository extends JpaRepository<Cuenta,Long> {
}
