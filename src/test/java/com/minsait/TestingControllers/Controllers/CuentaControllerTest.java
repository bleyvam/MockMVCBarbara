package com.minsait.TestingControllers.Controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.minsait.TestingControllers.Exceptions.DineroInsuficienteException;
import com.minsait.TestingControllers.Models.Banco;
import com.minsait.TestingControllers.Models.Cuenta;
import com.minsait.TestingControllers.Models.TransferirDTO;
import com.minsait.TestingControllers.Repository.CuentaRepository;
import com.minsait.TestingControllers.Services.CuentaService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.IllegalTransactionStateException;
import org.springframework.util.MimeTypeUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
class CuentaControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private CuentaService service;

    CuentaRepository repository;

    ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper();
    }

    @Test
    void testListarId() throws Exception {
        Mockito.when(service.findById(1L)).thenReturn(Datos.crearCuenta1().orElseThrow());
        mvc.perform(get("/api/cuentas/listar/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.persona").value("Ricardo"))
                .andExpect(jsonPath("$.saldo").value("1000"))
        ;
        verify(service).findById(1L);


    }

    @Test
    void testListarCuentas() throws Exception {
        Mockito.when(service.findAll()).thenReturn(
                List.of(Datos.crearCuenta1().get(),
                        Datos.crearCuenta2().get()));
        mvc.perform(get("/api/cuentas/listar").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].persona").value("Ricardo"))
                .andExpect(jsonPath("$.[0].id").value(1L))
                .andExpect(jsonPath("$.[0].saldo").value("1000"))
                // .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[1].persona").value("Canelo"))
                .andExpect(jsonPath("$.[1].id").value(2L))
                .andExpect(jsonPath("$.[1].saldo").value("5000000"))

        ;
        verify(service).findAll();

    }

    @Test
    public void testTransferir() throws Exception {

        TransferirDTO dtoTest = new TransferirDTO();
        dtoTest.setCuentaOrigenId(2L);
        dtoTest.setCuentaDestinoId(1L);
        dtoTest.setMonto(new BigDecimal(500));
        //dtoTest.setMonto(500);
        dtoTest.setBancoId(1L);

        Map<String, Object> transac = new HashMap<>();
        transac.put("date", LocalDate.now().toString());
        transac.put("status", "OK");
        transac.put("message", "transferencia realizada con exito");
        transac.put("transaccion", dtoTest);

        mvc.perform(post("/api/cuentas/transferir")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dtoTest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.date").value(LocalDate.now().toString()))
                .andExpect(jsonPath("$.message").value("transferencia realizada con exito"))
                .andExpect(jsonPath("$.transaccion.cuentaOrigenId").value(dtoTest.getCuentaOrigenId()))
                .andExpect(content().json(mapper.writeValueAsString(transac)));
        //   .equals(status().isAccepted());


    }
    @Test
    void testTransferirExcepcion() throws Exception{
     //   Cuenta trans= new Cuenta();
        Cuenta trans = new Cuenta(2L, "Canelo", new BigDecimal(5000000));


        TransferirDTO dtoTest = new TransferirDTO();



        try{
            // ejecutamos la código que debiera lanzar la excepción

            trans.retirar(new BigDecimal(89000000));


            dtoTest.setCuentaOrigenId(2L);
            dtoTest.setCuentaDestinoId(1L);
            // dtoTest.setMonto(new BigDecimal(60000600));
            //dtoTest.setMonto(500);
            dtoTest.setBancoId(1L);
            fail(); // si ejecución llega hasta esta línea, entonces significa que no se lanzó la excepción esperada y por ende la prueba ha fallado
        }
        catch(DineroInsuficienteException ex){
          /* si estamos aqui, entonces se ha producido la excepción esperada y no es necesario
             hacer nada pues a menos que se indique que algo ha ido mail, se asume todo estuvo
             bien */
            Map<String, Object> transac = new HashMap<>();
            // transac.put("date", LocalDate.now().toString());
            // transac.put("transaccion", dtoTest);
            transac.put("status", "OK");
            transac.put("message", "Dinero Insuficiente");
            // transac.put("transaccion", dtoTest);

        }
        catch(Exception ex){

            Exception exception=assertThrows(DineroInsuficienteException.class,()-> {
                service.transferir(
                        Datos.crearCuenta2().get().getId(), Datos.crearCuenta1().get().getId(), new BigDecimal(89000000),Datos.crearBanco().get().getId());




            });


            String actual= exception.getMessage();
            assertEquals("Dinero Insuficiente",actual);

            // si estamos aqui, es que la excepción que se ha lanzado, no es la esperada, por lo tanto la prueba ha fallado
            mvc.perform(post("/api/cuentas/transferir")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(dtoTest)))
                    .andExpect(status().isOk())

                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.message").value("Dinero Insuficiente"));
                   // .andExpect(content().json(mapper.writeValueAsString(transac)));
            fail();
        }

    }


    @Test
    void testVacioTransf() throws Exception {
        TransferirDTO dtoTest = new TransferirDTO();

      /*  dtoTest.setCuentaOrigenId(null);
        dtoTest.setCuentaDestinoId(null);
        dtoTest.setMonto(new BigDecimal((char[]) null));
        //dtoTest.setMonto(500);
        dtoTest.setBancoId(null);

       */
        mvc.perform(post("/api/cuentas/transferir")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dtoTest)))

                .andExpect(status().isOk())
                .equals(dtoTest.equals(null));


    }

    @Test
    void testGuardar() throws Exception {
        Cuenta cuenta = new Cuenta(null, "Javier", new BigDecimal(1000000));
        when(service.save(any())).then(invocationsOnMock -> {
            Cuenta cuenta1 = invocationsOnMock.getArgument(0);
            cuenta1.setId(3L);
            return cuenta1;
        });
        mvc.perform(post("/api/cuentas").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(cuenta)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", Matchers.is(3)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.persona", Matchers.is("Javier")))
                .andExpect(jsonPath("$.saldo", Matchers.is(1000000)))
        ;
        // verify(service.save(any()));
    }

    @Test
    void testSaldo() {
        Cuenta saldo = new Cuenta(2L, "Canelo", new BigDecimal(5000000));
        when(service.revisarSaldo(anyLong())).thenReturn(Datos.crearCuenta2().get().getSaldo());
        //assertEquals("5000000",service.revisarSaldo(2L).toPlainString());

        assertEquals(new BigDecimal(5000000), service.revisarSaldo(2L));
        assertNotNull(service);


    }

    @Test
    void testTotalTransferencia() {
        Cuenta saldo = new Cuenta(2L, "Canelo", new BigDecimal(5000000));
        when(service.revisarTotalTransferencias(anyLong())).thenReturn(Datos.crearBanco().get().getTotalTransferencias());
        assertNotNull(service);
        assertEquals(service.revisarTotalTransferencias(1L), 0);

    }
    @Test
    void testDepositar(){
        Cuenta dep = new Cuenta(1L, "Canelo", new BigDecimal(1000));
        dep.depositar(new BigDecimal(200));
        assertNotNull(dep);


    }


}