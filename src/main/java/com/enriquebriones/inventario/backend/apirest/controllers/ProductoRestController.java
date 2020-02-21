package com.enriquebriones.inventario.backend.apirest.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.enriquebriones.inventario.backend.apirest.models.entity.PageParams;
import com.enriquebriones.inventario.backend.apirest.models.entity.Producto;
import com.enriquebriones.inventario.backend.apirest.models.entity.Tipo;
import com.enriquebriones.inventario.backend.apirest.models.services.IProductoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = { "http://localhost:4200","*" })
@RestController
@RequestMapping("/api")
public class ProductoRestController {

    @Autowired
    private IProductoService productoService;

    @GetMapping("/productos")
    public List<Producto> index(){
        return productoService.findAllProductosByOrderByFechaCaducidadAsc();
    }
    @GetMapping("/productos/caducados")
    public List<Producto> caducados(){
        return productoService.findByExpirationDate();
    }

    @GetMapping("/productos/page/{page}")
    public Page<Producto> index(@PathVariable Integer page){
        return productoService.findAllByOrderByFechaCaducidadAsc(PageRequest.of(page,4));
    }

    
    @PostMapping("/productos/custom")
    public Page<Producto> indexPaginado(@RequestBody PageParams params){
        return productoService.findAllByOrderByFechaCaducidadAsc(PageRequest.of(params.getPageIndex(),params.getPageSize()));
    }

    @PostMapping("/productos/tipo")
    public Page<Producto> indexPorTipo(@RequestBody PageParams params){
        return productoService.findAllByTipoByOrderByFechaCaducidadAsc(params.getTipoId(),PageRequest.of(params.getPageIndex(), params.getPageSize()));
    }



    @GetMapping("/productos/page/caducados/{page}")
    public Page<Producto> caducados(@PathVariable Integer page){
        return productoService.findByExpirationDate(PageRequest.of(page,4));
    }

    @GetMapping("/productos/{id}")
    public ResponseEntity<?> show(@PathVariable Long id){
        Producto producto = null;
        Map<String,Object> response = new HashMap<>();
        try {
            producto= productoService.findById(id);    
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al realizar la consulta en la base de datos!");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
        if(producto==null){
            response.put("mensaje","El producto con el ID:".concat(id.toString()).concat(" no existe en la base de datos"));
            return new ResponseEntity<Map<String,Object>>(response,HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Producto>(producto,HttpStatus.OK);
    }


    @PostMapping("/productos")
    public ResponseEntity<?> create(@Valid @RequestBody Producto producto, BindingResult result) {

        Producto productoNew = null;
        Map<String, Object> response = new HashMap<>();

        if (result.hasErrors()) {
            List<String> errores = result.getFieldErrors().stream()
                    .map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
                    .collect(Collectors.toList());
            response.put("errores", errores);
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);

            // Por si se requiere devolver los errores separados.
            // Map<String, Object> errores = new HashMap<>();
            // result.getFieldErrors().forEach(err -> {
            //     errores.put(err.getField(), " El campo " + err.getField() + " " + err.getDefaultMessage());
            // });
            // return ResponseEntity.badRequest().body(errores);
        }

        try {
            productoNew = productoService.save(producto);

        } catch (DataAccessException e) {
            response.put("mensaje", "Error al realizar el insert en la base de datos!");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put("mensaje", "El producto ha sido creado con éxito!");
        response.put("producto", productoNew);
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    @PutMapping("/productos/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody Producto cliente, BindingResult result, @PathVariable Long id) {
        Producto productoActual = productoService.findById(id);
        Producto productoUpdated = null;
        Map<String, Object> response = new HashMap<>();

        if (result.hasErrors()) {
            List<String> errores = result.getFieldErrors().stream()
                    .map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
                    .collect(Collectors.toList());
            response.put("errores", errores);
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

        if (productoActual == null) {
            response.put("mensaje", "Error: no se pudo editar, el producto ID:".concat(id.toString())
                    .concat(" no existe en la base de datos!"));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        try {
            productoActual.setNombre(cliente.getNombre());
            productoActual.setDescripcion(cliente.getDescripcion());
            productoActual.setFechaEntrada(cliente.getFechaEntrada());
            productoActual.setFechaCaducidad(cliente.getFechaCaducidad());
            productoActual.setTipo(cliente.getTipo());

            productoUpdated = productoService.save(productoActual);
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al actualizar el producto en la base de datos!");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje", "El producto ha sido actualizado con éxito!");
        response.put("producto", productoUpdated);

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/productos/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            productoService.delete(id);

        } catch (DataAccessException e) {
            response.put("mensaje", "Error al eliminar el producto en la base de datos!");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put("mensaje", "Artículo eliminado con éxito!");
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }

    @GetMapping("/productos/tipos")
    public List<Tipo> listarTipos(){
        return productoService.findAllTipos();
    } 

}