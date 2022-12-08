package com.minsait.TestingControllers.Controllers;

import com.minsait.TestingControllers.Models.Banco;
import com.minsait.TestingControllers.Models.Cuenta;
import com.minsait.TestingControllers.Models.TransferirDTO;
import com.minsait.TestingControllers.Services.CuentaService;

import java.math.BigDecimal;
import java.util.Optional;

public class Datos {

    public static Optional<Cuenta> crearCuenta1(){
       return Optional.of(new Cuenta(1L,"Ricardo", new BigDecimal(1000)));

    }
    public static Optional<Cuenta> crearCuenta2(){
        return Optional.of(new Cuenta(2L,"Canelo", new BigDecimal(5000000)));

    }
    public static Optional<Banco> crearBanco(){
        return Optional.of(new Banco(1L,"Banco Azteca",0));

    }

    /*
    public static Optional<Banco> crearBanco2(){
        return Optional.of(new Banco(2L,"BBVA",0));

    }

     */
}
