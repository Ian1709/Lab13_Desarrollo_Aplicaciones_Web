package com.tecsup;

import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {
    @GetMapping
    public List<Map<String, Object>> listar() {
        return List.of(
                Map.of("id", 101, "nombre", "Teclado", "precio", 89.90),
                Map.of("id", 102, "nombre", "Mouse", "precio", 49.90)
        );
    }
}
