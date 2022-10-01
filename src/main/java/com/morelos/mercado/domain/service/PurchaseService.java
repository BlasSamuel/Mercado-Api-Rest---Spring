package com.morelos.mercado.domain.service;

import com.morelos.mercado.domain.Product;

import com.morelos.mercado.domain.Purchase;
import com.morelos.mercado.domain.repository.PurchaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service //Es un componente de tipo service
public class PurchaseService {
    @Autowired
    private PurchaseRepository purchaseRepository;

    public List<Purchase> getAll(){
        return purchaseRepository.getall();
    }
    public Optional<List<Purchase>> getByClient(String clienteId){
        return purchaseRepository.getByClient(clienteId);
    }

    public Purchase save(Purchase purchase){
        return purchaseRepository.save(purchase);
    }
    /*public boolean delete(int productId){
        return getProduct(productId).map(product -> {
            productRepository.delete(productId);
            return true;
        }).orElse(false);
    }*/

}
