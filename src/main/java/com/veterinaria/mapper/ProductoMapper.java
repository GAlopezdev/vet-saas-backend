package com.veterinaria.mapper;

import com.veterinaria.dto.request.ProductoRequestDTO;
import com.veterinaria.dto.response.ProductoResponseDTO;
import com.veterinaria.model.entity.Producto;
import com.veterinaria.model.entity.Subcategoria;
import com.veterinaria.model.entity.Usuario;
import org.springframework.stereotype.Component;

@Component
public class ProductoMapper {

    public Producto toEntity(ProductoRequestDTO dto, Usuario vendedor, Subcategoria subcategoria) {
        if (dto == null) return null;

        return Producto.builder()
                .nombre(dto.nombre())
                .descripcion(dto.descripcion())
                .precio(dto.precio())
                .stock(dto.stock())
                .imagenUrl(dto.imagenUrl())
                .estado("ACTIVO")
                .usuario(vendedor)
                .subcategoria(subcategoria)
                .build();
    }

    public ProductoResponseDTO toDto(Producto entity) {
        if (entity == null) return null;

        return new ProductoResponseDTO(
                entity.getIdProducto(),
                entity.getNombre(),
                entity.getDescripcion(),
                entity.getPrecio(),
                entity.getStock(),
                entity.getImagenUrl(),
                entity.getEstado(),
                entity.getUsuario().getCorreo(),
                entity.getSubcategoria().getNombre()
        );
    }
}
