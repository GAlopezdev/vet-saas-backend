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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;
    private final SubCategoriaRepository subcategoriaRepository;
    private final ProductoMapper productoMapper;
    private final CloudinaryService cloudinaryService;

    @Transactional
    public ProductoResponseDTO crearProducto(ProductoRequestDTO requestDTO, MultipartFile imagen) {
        String emailUsuario = SecurityContextHolder.getContext().getAuthentication().getName();

        Usuario vendedor = usuarioRepository.findByCorreo(emailUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        Subcategoria subcategoria = subcategoriaRepository.findById(requestDTO.subcategoriaId())
                .orElseThrow(() -> new ResourceNotFoundException("Subcategoría no encontrada"));

        String urlImagen;
        try {
            urlImagen = cloudinaryService.uploadImage(imagen, "productos");
        } catch (IOException e) {
            throw new RuntimeException("Error al procesar la imagen");
        }

        Producto producto = productoMapper.toEntity(requestDTO, vendedor, subcategoria);
        producto.setImagenUrl(urlImagen);
        Producto guardado = productoRepository.save(producto);

        return productoMapper.toDto(guardado);
    }

    @Override
    @Transactional
    public ProductoResponseDTO actualizarProducto(Long id, ProductoRequestDTO requestDTO, MultipartFile imagen) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con ID: " + id));

        String emailUsuario = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!producto.getUsuario().getCorreo().equals(emailUsuario)) {
            throw new RuntimeException("No tienes permiso para editar este producto");
        }

        if (!producto.getSubcategoria().getIdSubcategoria().equals(requestDTO.subcategoriaId())) {
            Subcategoria subcategoria = subcategoriaRepository.findById(requestDTO.subcategoriaId())
                    .orElseThrow(() -> new ResourceNotFoundException("Subcategoría no encontrada"));
            producto.setSubcategoria(subcategoria);
        }

        producto.setNombre(requestDTO.nombre());
        producto.setDescripcion(requestDTO.descripcion());
        producto.setPrecio(requestDTO.precio());
        producto.setStock(requestDTO.stock());

        if (imagen != null && !imagen.isEmpty()) {
            try {
                String nuevaUrl = cloudinaryService.uploadImage(imagen, "productos");
                producto.setImagenUrl(nuevaUrl);
            } catch (IOException e) {
                throw new RuntimeException("Error al subir la nueva imagen");
            }
        }

        return productoMapper.toDto(productoRepository.save(producto));
    }

    @Override
    @Transactional
    public void eliminarProducto(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));

        String emailUsuario = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!producto.getUsuario().getCorreo().equals(emailUsuario)) {
            throw new RuntimeException("No tienes permiso para eliminar este producto");
        }

        producto.setEstado("INACTIVO");
        productoRepository.save(producto);
    }

    @Transactional(readOnly = true)
    public Page<ProductoResponseDTO> obtenerTodos(Pageable pageable) {
        return productoRepository.findAllActivos( pageable)
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

    @Override
    @Transactional(readOnly = true)
    public ProductoResponseDTO obtenerPorId(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con ID: " + id));

        // Opcional: Validar que el producto no esté INACTIVO
        if ("INACTIVO".equals(producto.getEstado())) {
            throw new ResourceNotFoundException("El producto no está disponible");
        }

        return productoMapper.toDto(producto);
    }
}
