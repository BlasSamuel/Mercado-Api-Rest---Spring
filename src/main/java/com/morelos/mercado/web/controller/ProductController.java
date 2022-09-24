package com.morelos.mercado.web.controller;

import com.morelos.mercado.domain.Product;
import com.morelos.mercado.domain.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired //indica que se está inyectando este elemento, y spring se encarga de la instancia
    private ProductService productService;

    @GetMapping("/all")
    public ResponseEntity<List<Product>> getAll() {
        return new ResponseEntity<>(productService.getAll(), HttpStatus.OK);
    }

    @PostMapping("/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable("id") int productId) {
        return productService.getProduct(productId).map(product -> new ResponseEntity<>(product, HttpStatus.OK)).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<Product>> getByCategory(@PathVariable("categoryId") int categoryId) {
        return productService.getByCategory(categoryId).map(products -> products.isEmpty() ? new ResponseEntity<>(products, HttpStatus.OK ) : new ResponseEntity<>(products,HttpStatus.NOT_FOUND )
        ).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/save")
    public ResponseEntity<Product> save(@RequestBody Product product) {
        return new ResponseEntity<>(productService.save(product), HttpStatus.CREATED);
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity delete(@PathVariable("id") int productoId) {
        return new ResponseEntity(productService.delete(productoId) ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    /*@PostMapping("/remove/{id}")
    public Product remove(@RequestBody int productId) {
        return productService.remove(productId);
    }*/
}
