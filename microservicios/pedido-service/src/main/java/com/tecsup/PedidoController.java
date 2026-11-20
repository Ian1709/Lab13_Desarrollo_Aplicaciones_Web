package com.tecsup;

import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {
    @GetMapping
    public List<Map<String, Object>> listar() {
        return List.of(
                Map.of("id", 5001, "clienteId", 1, "total", 139.80, "estado", "CONFIRMADO"),
                Map.of("id", 5002, "clienteId", 2, "total", 49.90, "estado", "EN_PREPARACION")
        );
    }
}
