package com.enriquebriones.inventario.backend.apirest.models.dao;

import java.util.List;

import com.enriquebriones.inventario.backend.apirest.models.entity.Producto;
import com.enriquebriones.inventario.backend.apirest.models.entity.Tipo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IProductoDao extends JpaRepository<Producto,Long> {

    @Query("from Tipo")
    public List<Tipo> findAllTipos();

    @Query(value="select * from Producto p order by case when p.fecha_caducidad <= sysdate() then 1 when p.fecha_caducidad <= date_add(sysdate(),interval 1 month) then 2 else 3 end", nativeQuery = true)
    public List<Producto> findByExpirationDate();

    @Query("select p from Producto p where p.fechaCaducidad <= sysdate()")
    public Page<Producto> findByExpirationDate(Pageable pageable);

    public List<Producto> findAllProductosByOrderByFechaCaducidadAsc();

    public Page<Producto> findAllByOrderByFechaCaducidadAsc(Pageable pageable);

    // Query principal de la aplicación. Devuelve datos paginados y según el tipo enviado.
    // Reemplazamos la query comentada para que funcione en PostgreSQL.
    // @Query("from Producto p where p.tipo.id= case when ?1 is null then p.tipo.id else ?1 end order by p.fechaCaducidad asc")
    @Query("select p from Producto p where p.tipo.id= (case when ?1 < 1L then p.tipo.id else ?1 end) order by p.fechaCaducidad asc")
    public Page<Producto> findAllByTipoByOrderByFechaCaducidadAsc(Long tipo_id,Pageable pageable);

}
