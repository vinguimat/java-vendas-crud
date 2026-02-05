package com.projeto.vendas.repository;

import com.projeto.vendas.model.ProductEntry;
import com.projeto.vendas.model.Sell;
import com.projeto.vendas.model.ProductSell;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SellRepository extends JpaRepository<Sell, Long> {
    @Query("""
        SELECT 
            ps 
        FROM 
            ProductSell ps
        JOIN FETCH 
            ps.product
    """)
    List<ProductEntry> findAllWithProduct();
}
