package com.morelos.mercado.web.controller;

import com.morelos.mercado.domain.Product;
import com.morelos.mercado.domain.Purchase;
import com.morelos.mercado.domain.service.ProductService;
import com.morelos.mercado.domain.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/purchases")
public class PurchaseController {
    @Autowired //indica que se está inyectando este elemento, y spring se encarga de la instancia
    private PurchaseService purchaseService;

    @GetMapping("/all")
    public ResponseEntity<List<Purchase>> getAll() {
        return new ResponseEntity<>(purchaseService.getAll(), HttpStatus.OK);
    }

    @PostMapping("/{id}")
    public ResponseEntity<List<Purchase>> getProduct(@PathVariable("id") String clienteId) {
        return purchaseService.getByClient(clienteId).map(purchases -> purchases.isEmpty() ? new ResponseEntity<>(purchases, HttpStatus.NOT_FOUND) : new ResponseEntity<>(purchases,HttpStatus.OK)).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
       // return productService.getByCategory(categoryId).map(products -> products.isEmpty() ? new ResponseEntity<>(products, HttpStatus.OK ) : new ResponseEntity<>(products,HttpStatus.NOT_FOUND )
    }

    @PostMapping("/save")
    public ResponseEntity<Purchase> save(@RequestBody Purchase purchase) {
        return new ResponseEntity<>(purchaseService.save(purchase), HttpStatus.CREATED);
    }


    /*@DeleteMapping("/delete/{id}")
    public ResponseEntity delete(@PathVariable("id") int productoId) {
        return new ResponseEntity(productService.delete(productoId) ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }*/


}
