package com.tecsup;

import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {
    @GetMapping
    public List<Map<String, Object>> listar() {
        return List.of(
                Map.of("id", 1, "nombre", "Electrónica"),
                Map.of("id", 2, "nombre", "Hogar")
        );
    }
}
