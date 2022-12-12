package com.minsait.TestingControllers;

import com.minsait.TestingControllers.Models.Cuenta;
import com.minsait.TestingControllers.Repository.CuentaRepository;
//import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class IntegrationJpaTest {
    @Autowired
    CuentaRepository repository;

    @Test
    void testFindById(){
        Optional<Cuenta> cuenta = repository.findById(1L);
        assertTrue(cuenta.isPresent());
        assertEquals("Ricardo",cuenta.get().getPersona());
    }
    @Test
    void testFindPersona(){
        Optional<Cuenta> cuenta =repository.findByPersona("Ricardo");
        assertTrue(cuenta.isPresent());
        assertEquals("Ricardo",cuenta.get().getPersona());
    }

    @Test
    void testFindByPersonaException(){
        Optional<Cuenta> cuenta =repository.findByPersona("Javier");
        assertThrows(NoSuchElementException.class,cuenta::orElseThrow);
        assertFalse(cuenta.isPresent());

    }

    @Test
    void save(){
        Cuenta cuentaJavier= new Cuenta(null,"Javier",new BigDecimal(1000000));
        Cuenta cuenta=repository.save(cuentaJavier);
        assertEquals("Javier",cuenta.getPersona());
        assertEquals("1000000",cuenta.getSaldo().toPlainString());
        assertEquals(3,cuenta.getId());
    }

    @Test
    void testUpdate(){
        //crea cuenta y nos aseguramos que se guardó
        Cuenta cuentaJavier= new Cuenta(null,"Javier",new BigDecimal(1000000));
        Cuenta cuenta=repository.save(cuentaJavier);

        assertEquals("Javier",cuenta.getPersona());
        assertEquals("1000000",cuenta.getSaldo().toPlainString());
       // assertEquals(3,cuenta.getId());

        cuenta.setSaldo(new BigDecimal(10000));
        Cuenta cuentaActualizada=repository.save(cuenta);
//prueba para actualizar
        assertEquals("Javier",cuentaActualizada.getPersona());
        assertEquals("10000",cuentaActualizada.getSaldo().toPlainString());
       // assertEquals(3,cuentaActualizada.getId());

       // Optional<Cuenta> cuenta2 =repository.findById(3L);
        //assertEquals("10000",cuenta2.get().getSaldo().toPlainString());


    }
    @Test
    void testDelete(){
        Cuenta cuenta =repository.findById(2L).orElseThrow();
        assertEquals("Canelo",cuenta.getPersona());

        repository.delete(cuenta);
        assertThrows(NoSuchElementException.class, () ->{
            repository.findById(2L).orElseThrow();
        });

        assertEquals(1,repository.findAll().size());
    }
}
