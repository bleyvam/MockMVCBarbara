package com.minsait.TestingControllers.Services;

import com.minsait.TestingControllers.Models.Banco;
import com.minsait.TestingControllers.Models.Cuenta;
import com.minsait.TestingControllers.Repository.BancoRepository;
import com.minsait.TestingControllers.Repository.CuentaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
@Service
public class CuentaServiceImp implements CuentaService {

   @Autowired
    private CuentaRepository cuentaRepository;
    @Autowired
   private BancoRepository bancoRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Cuenta> findAll() {
        return cuentaRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Cuenta findById(Long id) {
        return cuentaRepository.findById(id).orElseThrow();
    }

    @Override
    @Transactional(readOnly = true)
    public Integer revisarTotalTransferencias(Long bancoId) {
       Banco banco=bancoRepository.findById(bancoId).orElseThrow();
        return banco.getTotalTransferencias();
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal revisarSaldo(Long cuentaId) {
        Cuenta cuenta =cuentaRepository.findById(cuentaId).orElseThrow();
        return cuenta.getSaldo();
    }

    @Override
    @Transactional
    public void transferir(Long cuentaOrigen, Long cuentaDestino, BigDecimal monto, Long bancoId) {
        Cuenta origen=cuentaRepository.findById(cuentaOrigen).orElseThrow();
        origen.retirar(monto);
        cuentaRepository.save(origen);


        Cuenta destino =cuentaRepository.findById(cuentaDestino).orElseThrow();
        destino.depositar(monto);
        cuentaRepository.save(destino);


        Banco banco =bancoRepository.findById(bancoId).orElseThrow();
        int totalTransferencias= banco.getTotalTransferencias();
        banco.setTotalTransferencias(++totalTransferencias);
        bancoRepository.save(banco);

    }
    @Transactional
    @Override
    public Cuenta save(Cuenta cuenta) {
        return cuentaRepository.save(cuenta);
    }
}
