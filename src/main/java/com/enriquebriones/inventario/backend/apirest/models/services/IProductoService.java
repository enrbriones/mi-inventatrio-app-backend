package com.enriquebriones.inventario.backend.apirest.models.services;

import java.util.List;

import com.enriquebriones.inventario.backend.apirest.models.entity.Producto;
import com.enriquebriones.inventario.backend.apirest.models.entity.Tipo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IProductoService {
    
    public List<Producto> findAll();

    public Page<Producto> findAllByOrderByFechaCaducidadAsc(Pageable pageable);

    public Producto findById(Long id);

    public Producto save(Producto producto);

    public void delete(Long id);

    public List<Tipo> findAllTipos();

    public List<Producto> findByExpirationDate();
    public Page<Producto> findByExpirationDate(Pageable pageable);

    public List<Producto> findAllProductosByOrderByFechaCaducidadAsc();

    public Page<Producto> findAllByTipoByOrderByFechaCaducidadAsc(Long tipo_id, Pageable pageable);

}
