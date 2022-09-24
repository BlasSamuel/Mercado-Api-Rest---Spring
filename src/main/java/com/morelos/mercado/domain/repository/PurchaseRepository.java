package com.morelos.mercado.domain.repository;

import java.util.List;
import java.util.Optional;

import com.morelos.mercado.domain.Purchase;

public interface PurchaseRepository {
    List<Purchase> getall();
    Optional<List<Purchase>> getByClient(String clientId);
    Purchase save(Purchase purchase);
}
