package com.enriquebriones.inventario.backend.apirest.models.entity;

//Para manejar la paginación en Angular Material.

public class PageParams{
    private Long tipoId;
    private int pageIndex;
    private int pageSize;

    public PageParams() {
        
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public Long getTipoId() {
        return tipoId;
    }

    public void setTipoId(Long tipoId) {
        this.tipoId = tipoId;
    }

}