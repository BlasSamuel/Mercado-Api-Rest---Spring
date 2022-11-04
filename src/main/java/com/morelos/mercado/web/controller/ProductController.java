package com.morelos.mercado.web.controller;

import com.morelos.mercado.domain.Product;
import com.morelos.mercado.domain.service.ProductService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired //indica que se está inyectando este elemento, y spring se encarga de la instancia
    private ProductService productService;

    @GetMapping("/all")
    //@ApiOperation("Listar todos los productos") //descripción para la dacumentación de swagger2
    @ApiOperation(value = "Listar todos los productos", authorizations = { @Authorization(value="JWT") })
    @ApiResponse(code = 200, message = "OK") //estatus para documentación
    public ResponseEntity<List<Product>> getAll() {
        return new ResponseEntity<>(productService.getAll(), HttpStatus.OK);
    }

    @PostMapping("/{id}")
    @ApiOperation(value = "Buscar un producto por ID", authorizations = { @Authorization(value="JWT") }) //descripción para la documentación de swagger2
    @ApiResponses({//declaración de un objeto porque puede retornar culaquiera de los siguientes valores
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Producto no encontrado")
    })
    public ResponseEntity<Product> getProduct(
            // @Apiparam() anotación para identificador que recibe la petición, es obligatorio
            @ApiParam(value = "El id del producto", required = true, example = "7") @PathVariable("id") int productId) {
        return productService.getProduct(productId).map(product -> new ResponseEntity<>(product, HttpStatus.OK)).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/category/{categoryId}")
    @ApiOperation(value = "Retornar lista de productos por el id de la categoria", authorizations = { @Authorization(value="JWT") })
    @ApiResponses({//declaración de un objeto porque puede retornar culaquiera de los siguientes valores
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Productos no encontrados")
    })
    public ResponseEntity<List<Product>> getByCategory(@PathVariable("categoryId") int categoryId) {
        return productService.getByCategory(categoryId).map(products -> products.isEmpty() ? new ResponseEntity<>(products, HttpStatus.NOT_FOUND) : new ResponseEntity<>(products, HttpStatus.OK)
        ).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/save")
    @ApiOperation(value = "guardar un producto", authorizations = { @Authorization(value="JWT") })
    @ApiResponse(code = 201, message = "CREATED")
    public ResponseEntity<Product> save(@RequestBody Product product) {
        return new ResponseEntity<>(productService.save(product), HttpStatus.CREATED);
    }


    @DeleteMapping("/delete/{id}")
    @ApiOperation(value = "eliminar un producto por id", authorizations = { @Authorization(value="JWT") })
    @ApiResponse(code = 404, message = "NOT_FOUND")
    public ResponseEntity delete(@PathVariable("id") int productoId) {
        return new ResponseEntity(productService.delete(productoId) ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    /*@PostMapping("/remove/{id}")
    public Product remove(@RequestBody int productId) {
        return productService.remove(productId);
    }*/
}
