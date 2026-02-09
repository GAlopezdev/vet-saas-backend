package com.veterinaria.service.impl;

import com.veterinaria.dto.request.ProductoRequestDTO;
import com.veterinaria.dto.response.ProductoResponseDTO;
import com.veterinaria.exception.ResourceNotFoundException;
import com.veterinaria.mapper.ProductoMapper;
import com.veterinaria.model.entity.Producto;
import com.veterinaria.model.entity.Subcategoria;
import com.veterinaria.model.entity.Usuario;
import com.veterinaria.repository.ProductoRepository;
import com.veterinaria.repository.SubCategoriaRepository;
import com.veterinaria.repository.UsuarioRepository;
import com.veterinaria.service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;
    private final SubCategoriaRepository subcategoriaRepository;
    private final ProductoMapper productoMapper;

    @Transactional
    public ProductoResponseDTO crearProducto(ProductoRequestDTO requestDTO) {
        String emailUsuario = SecurityContextHolder.getContext().getAuthentication().getName();

        Usuario vendedor = usuarioRepository.findByCorreo(emailUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        Subcategoria subcategoria = subcategoriaRepository.findById(requestDTO.subcategoriaId())
                .orElseThrow(() -> new ResourceNotFoundException("Subcategoría no encontrada"));

        Producto producto = productoMapper.toEntity(requestDTO, vendedor, subcategoria);
        Producto guardado = productoRepository.save(producto);

        return productoMapper.toDto(guardado);
    }

    @Transactional(readOnly = true)
    public Page<ProductoResponseDTO> obtenerTodos(Pageable pageable) {
        return productoRepository.findByEstado("ACTIVO", pageable)
                .map(productoMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<ProductoResponseDTO> obtenerMisProductos(Pageable pageable) {
        String emailUsuario = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario vendedor = usuarioRepository.findByCorreo(emailUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        return productoRepository.findByUsuario_IdUsuario(vendedor.getIdUsuario(), pageable)
                .map(productoMapper::toDto);
    }
}
