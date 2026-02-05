package com.projeto.vendas.repository;

import com.projeto.vendas.model.ProductSell;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductSellRepository extends JpaRepository<ProductSell, Long> {
    @Query("SELECT ps FROM ProductSell ps WHERE ps.sell.id = ?1")
    List<ProductSell> searchForSell(Long id);
}
