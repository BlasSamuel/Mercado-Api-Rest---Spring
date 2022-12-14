package com.morelos.mercado.web.controller;
import com.morelos.mercado.domain.JsonResponse;
import com.morelos.mercado.domain.Product;
import com.morelos.mercado.domain.service.ProductService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;



@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired //indica que se está inyectando este elemento, y spring se encarga de la instancia
    private ProductService productService;

    @GetMapping("/all")
    //@ApiOperation("Listar todos los productos") //descripción para la documentación de swagger2
    @ApiOperation(value = "Listar todos los productos", authorizations = {@Authorization(value = "JWT")})
    @ApiResponse(code = 200, message = "OK") //estatus para documentación
    public ResponseEntity<?> getAll() {
        JsonResponse json = new JsonResponse(true, new ArrayList<>(), "");
        HttpStatus status = HttpStatus.NOT_FOUND;
        try {
            List<Product> products = productService.getAll();
            status = (products.isEmpty() ? HttpStatus.OK : HttpStatus.FORBIDDEN);
            json.data = (ArrayList) products;
        } catch (Exception e) {
            json.error = e.getMessage();
        }
        return new ResponseEntity<>(json, status);
    }

    @PostMapping("/{id}")
    @ApiOperation(value = "Buscar un producto por ID", authorizations = {@Authorization(value = "JWT")})
    //descripción para la documentación de swagger2
    @ApiResponses({//declaración de un objeto porque puede retornar culaquiera de los siguientes valores
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Producto no encontrado")
    })
    public ResponseEntity<?> getProduct(
            // @Apiparam() anotación para identificador que recibe la petición, es obligatorio
            @ApiParam(value = "El id del producto", required = true, example = "7") @PathVariable("id") int productId) {
        JsonResponse json = new JsonResponse(false, new ArrayList<>(), "");
        HttpStatus status = HttpStatus.NOT_FOUND;
        try {

            Product p = productService.getProduct(productId).orElse(null);
            if (Objects.isNull(p)) {
                throw new Exception("producto no disponible");
            } else {
                ArrayList<Product> data = new ArrayList<Product>();
                data.add(p);
                json.data = data;
                status = HttpStatus.OK;
                json.success = true;
            }

        } catch (Exception e) {
            json.error = e.getMessage();
        }
        return new ResponseEntity<>(json, status);

    }

    @GetMapping("/category/{categoryId}")
    @ApiOperation(value = "Retornar lista de productos por el id de la categoria", authorizations = {@Authorization(value = "JWT")})
    @ApiResponses({//declaración de un objeto porque puede retornar culaquiera de los siguientes valores
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Productos no encontrados")
    })
    public ResponseEntity<JsonResponse> getByCategory(@PathVariable("categoryId") int categoryId) {
        JsonResponse json = new JsonResponse(true, new ArrayList<>(), "");
        HttpStatus status = HttpStatus.NOT_FOUND;
        try {
            List<Product> products = productService.getByCategory(categoryId).orElse(new ArrayList<>());
            status = (products.isEmpty() ? HttpStatus.OK : HttpStatus.FORBIDDEN);
            json.data = (ArrayList) products;
        } catch (Exception e) {
            json.error = e.getMessage();
        }
        return new ResponseEntity<>(json, status);
    }

    @PostMapping("/save")
    @ApiOperation(value = "guardar un producto", authorizations = {@Authorization(value = "JWT")})
    @ApiResponse(code = 201, message = "CREATED")
    public ResponseEntity<Product> save(@RequestBody Product product) {
        return new ResponseEntity<>(productService.save(product), HttpStatus.CREATED);
    }


    @DeleteMapping("/delete/{id}")
    @ApiOperation(value = "eliminar un producto por id", authorizations = {@Authorization(value = "JWT")})
    @ApiResponse(code = 404, message = "NOT_FOUND")
    public ResponseEntity delete(@PathVariable("id") int productoId) {
        JsonResponse json = new JsonResponse(true, new ArrayList<>(), "");
        HttpStatus status = HttpStatus.NOT_FOUND;
        try {
            Boolean delete = productService.delete(productoId);
            status = (delete ? HttpStatus.OK : HttpStatus.FORBIDDEN);
            if(!delete){
                json.success = false;
                json.error = "producto no encontrado";
            }
        } catch (Exception e) {
            json.error = e.getMessage();
        }
        return new ResponseEntity<>(json, status);
    }

    /*@PostMapping("/remove/{id}")
    public Product remove(@RequestBody int productId) {
        return productService.remove(productId);
    }*/
}
