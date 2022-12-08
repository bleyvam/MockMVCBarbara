package com.minsait.TestingControllers.Repository;

import com.minsait.TestingControllers.Models.Banco;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BancoRepository extends JpaRepository<Banco,Long> {
}
