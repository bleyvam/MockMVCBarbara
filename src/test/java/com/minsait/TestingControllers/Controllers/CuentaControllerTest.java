package com.minsait.TestingControllers.Controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.minsait.TestingControllers.Models.Cuenta;
import com.minsait.TestingControllers.Models.TransferirDTO;
import com.minsait.TestingControllers.Services.CuentaService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
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
import org.springframework.util.MimeTypeUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
class CuentaControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private CuentaService service;



    ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        mapper=new ObjectMapper();
    }

    @Test
    void  testListarId() throws Exception {
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
    void testListarCuentas() throws Exception{
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

        TransferirDTO dtoTest= new TransferirDTO();
        dtoTest.setCuentaOrigenId(2L);
        dtoTest.setCuentaDestinoId(1L);
        dtoTest.setMonto(new BigDecimal(500));
        //dtoTest.setMonto(500);
        dtoTest.setBancoId(1L);

        mvc.perform(post("/api/cuentas/transferir")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dtoTest)))

                .andExpect(status().isOk())

                .equals(status().isAccepted());

    }

    @Test
    void testVacioTransf() throws Exception {
        TransferirDTO dtoTest= new TransferirDTO();

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
    void testGuardar() throws Exception{
        Cuenta cuenta = new Cuenta(null,"Javier",new BigDecimal(1000000));
        when(service.save(any())).then(invocationsOnMock ->{
            Cuenta cuenta1=invocationsOnMock.getArgument(0);
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



}