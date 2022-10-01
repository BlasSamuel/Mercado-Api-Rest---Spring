package com.morelos.mercado.persistence;

import com.morelos.mercado.domain.Purchase;
import com.morelos.mercado.domain.repository.PurchaseRepository;
import com.morelos.mercado.persistence.entity.Compra;
import com.morelos.mercado.persistence.mapper.PurchaseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import com.morelos.mercado.persistence.crud.CompraCrudRepository;

@Repository
public class CompraRepositry implements PurchaseRepository {
    @Autowired
    private CompraCrudRepository compraCrudRepository;

    @Autowired
    private PurchaseMapper mapper;


    @Override
    public List<Purchase> getall() {
        return mapper.toPurchases((List<Compra>) compraCrudRepository.findAll());
    }

    @Override
    public Optional<List<Purchase>> getByClient(String clientId) {
        return compraCrudRepository.findByIdCliente(clientId)
                .map(compras -> mapper.toPurchases(compras));
    }

    @Override
    public Purchase save(Purchase purchase) {
        Compra compra = mapper.toCompra(purchase);
        compra.getProductos().forEach(producto -> producto.setCompra(compra));
        return mapper.toPurchase(compraCrudRepository.save(compra));
    }
}
