package com.minsait.TestingControllers.Exceptions;

public class DineroInsuficienteException extends  RuntimeException{
    public DineroInsuficienteException(String mensaje){
        super(mensaje);
    }
}
