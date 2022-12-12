package com.minsait.TestingControllers.Controllers;

import com.minsait.TestingControllers.Exceptions.DineroInsuficienteException;
import com.minsait.TestingControllers.Models.Cuenta;
import com.minsait.TestingControllers.Models.TransferirDTO;
import com.minsait.TestingControllers.Services.CuentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
//indicar ruta en  clase
@RequestMapping("/api/cuentas")
public class CuentaController {
    @Autowired
    private CuentaService service;

    @GetMapping("/listar")
    @ResponseStatus(HttpStatus.OK)
    public List<Cuenta> findAll() {
        return service.findAll();
    }

    @GetMapping("/listar/{id}")
    //@ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Cuenta> findById(@PathVariable Long id) {
        try {
            Cuenta cuenta = service.findById(id);
            return ResponseEntity.ok(cuenta);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }

    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public Cuenta guardar(@RequestBody Cuenta cuenta) {
        return service.save(cuenta);
    }


    @PostMapping("/transferir")
    public ResponseEntity<?> transferir(@RequestBody TransferirDTO dto) {
      //  Exception exception=new DineroInsuficienteException("Dinero Insuficiente");
        Map<String, Object> response = new HashMap<>();
        response.put("date", LocalDate.now().toString());
        response.put("transaccion", dto);
        try {
            service.transferir(dto.getCuentaOrigenId(), dto.getCuentaDestinoId(), dto.getMonto(), dto.getBancoId());

            response.put("date", LocalDate.now().toString());
            response.put("status", "OK");
            response.put("message", "transferencia realizada con exito");
            response.put("transaccion", dto);

            return ResponseEntity.ok(response);
        }catch (DineroInsuficienteException exception){
            response.put("status", "OK");
            response.put("message", exception);
            return ResponseEntity.ok(response);

        }
    }

}
