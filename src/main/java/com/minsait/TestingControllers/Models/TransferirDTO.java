package com.minsait.TestingControllers.Models;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TransferirDTO {
    private Long cuentaOrigenId;
    private  Long cuentaDestinoId;
    private BigDecimal monto;
    private  Long bancoId;
}
