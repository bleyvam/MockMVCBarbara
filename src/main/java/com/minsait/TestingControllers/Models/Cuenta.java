package com.minsait.TestingControllers.Models;

import com.minsait.TestingControllers.Exceptions.DineroInsuficienteException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name="Cuentas")
public class Cuenta implements Serializable {
    final static private long serialversionUID=1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String persona;
    private BigDecimal saldo;

    public void retirar(BigDecimal monto){
      BigDecimal nuevoSaldo=this.saldo.subtract(monto);

      if(nuevoSaldo.compareTo(BigDecimal.ZERO)<0){
          throw new DineroInsuficienteException("Dinero Insuficiente");

      }
      this.saldo=nuevoSaldo;
    }

    public void depositar(BigDecimal monto){

        this.saldo=saldo.add(monto);
    }

}
