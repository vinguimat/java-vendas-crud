package com.projeto.vendas.repository;

import com.projeto.vendas.model.ProductEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductEntryRepository extends JpaRepository<ProductEntry, Long> {
    @Query("SELECT pe FROM ProductEntry pe WHERE pe.entry.id = ?1")
    List<ProductEntry> searchForEntry(Long id);
}
