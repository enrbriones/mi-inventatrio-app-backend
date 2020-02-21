package com.enriquebriones.inventario.backend.apirest.models.services;

import java.util.List;

import com.enriquebriones.inventario.backend.apirest.models.dao.IProductoDao;
import com.enriquebriones.inventario.backend.apirest.models.entity.Producto;
import com.enriquebriones.inventario.backend.apirest.models.entity.Tipo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductoServiceImpl implements IProductoService {

    @Autowired
    private IProductoDao productoDao;

    @Override
    @Transactional(readOnly = true)
    public List<Producto> findAll() {
        return (List<Producto>) productoDao.findAll();
    }

    @Override
    public Page<Producto> findAllByOrderByFechaCaducidadAsc(Pageable pageable) {
        return productoDao.findAllByOrderByFechaCaducidadAsc(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Producto findById(Long id) {
        return productoDao.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public Producto save(Producto producto) {
        return productoDao.save(producto);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        productoDao.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Tipo> findAllTipos() {
        return productoDao.findAllTipos();
    }

    @Override
    public List<Producto> findByExpirationDate() {
        return productoDao.findByExpirationDate();
    }

    @Override
    public Page<Producto> findByExpirationDate(Pageable pageable) {
        return productoDao.findByExpirationDate(pageable);
    }

    @Override
    public List<Producto> findAllProductosByOrderByFechaCaducidadAsc() {
        return productoDao.findAllProductosByOrderByFechaCaducidadAsc();
    }

    @Override
    public Page<Producto> findAllByTipoByOrderByFechaCaducidadAsc(Long tipo_id, Pageable pageable) {
        return productoDao.findAllByTipoByOrderByFechaCaducidadAsc(tipo_id, pageable);
    }

}
