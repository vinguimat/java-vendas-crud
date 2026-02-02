package com.projeto.vendas.repository;

import com.projeto.vendas.model.Entry;
import com.projeto.vendas.model.ProductEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EntryRepository extends JpaRepository<Entry, Long> {
    @Query("""
        SELECT 
            pe 
        FROM 
            ProductEntry pe
        JOIN FETCH 
            pe.product
    """)
    List<ProductEntry> findAllWithProduct();
}
