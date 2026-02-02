package com.projeto.vendas.controller;

import com.projeto.vendas.model.*;
import com.projeto.vendas.repository.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class EntryController {

    @Autowired
    private EntryRepository entryRepository;
    @Autowired
    private ProductEntryRepository productentryRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private SupplierRepository supplierRepository;

    @GetMapping("/registerEntry")
    public ModelAndView register(Entry entry, ProductEntry productEntry, HttpSession session) {
        ModelAndView mv = new ModelAndView("adm/entrys/register");

        List<ProductEntry> listOfProductEntry = (List<ProductEntry>) session.getAttribute("listOfProductEntry");
        if (listOfProductEntry == null) {
            listOfProductEntry = new ArrayList<>();
        }

        mv.addObject("entry", entry);
        mv.addObject("productEntry", productEntry);
        mv.addObject("listOfProductEntry", listOfProductEntry);
        mv.addObject("listEmployee", employeeRepository.findAll());
        mv.addObject("listSupplier", supplierRepository.findAll());
        mv.addObject("listProduct", productRepository.findAll());
        return mv;
    }

    @GetMapping("/listEntry")
    public ModelAndView list() {
        ModelAndView mv = new ModelAndView("adm/entrys/list");
        mv.addObject("listEntry", entryRepository.findAll());
        return mv;
    }

    @GetMapping("/editEntry/{id}")
    public ModelAndView edit(@PathVariable("id") Long id, HttpSession session) {
        Optional<Entry> entry = entryRepository.findById(id);

        List<ProductEntry> items = productentryRepository.searchForEntry(entry.get().getId());

        session.setAttribute("listOfProductEntry", items);

        return register(entry.get(), new ProductEntry(), session);
    }

    @GetMapping("/removeEntry/{id}")
    public ModelAndView remove(@PathVariable("id") Long id) {
        Optional<Entry> entry = entryRepository.findById(id);
        entryRepository.delete(entry.get());
        return list();
    }

    @PostMapping("/saveEntry")
    public ModelAndView save(String action, Entry entry, ProductEntry productEntry, BindingResult result, HttpSession session) {
        // SE TIVER ERRO PARA AQUI MESMO
        if(result.hasErrors()) {
            return register(entry, productEntry, session);
        }
        // VERIFICA LISTA DA SESSÃO
        List<ProductEntry> listOfProductEntry = (List<ProductEntry>) session.getAttribute("listOfProductEntry");
        if (listOfProductEntry == null) {
            listOfProductEntry = new ArrayList<>();
        }

        // SE O BOTÃO ADICIONAR FOR ACIONADO
        if (action.equals("products")) {

            listOfProductEntry.add(productEntry);// ADICIONA PRODUTO NA LISTA
            entry.setTotalQuantity(entry.getTotalQuantity() + productEntry.getQuantity());
            entry.setTotalValue(entry.getTotalValue() + productEntry.getPrice());
            session.setAttribute("listOfProductEntry", listOfProductEntry); // ✅ Salva na sessão


        } else if (action.equals("save")) { // SE O BOTAO SALVAR FOR ACIONADO

            entryRepository.saveAndFlush(entry); // SALVA ENTRADA NO BANCO

            for (ProductEntry pe : listOfProductEntry) { // PERCORRE A LISTA COM UM FOREACH NOS PRODUTOS

                pe.setEntry(entry);
                productentryRepository.saveAndFlush(pe); // SALVA PRODUCTENTRY NO BANCO

                Optional<Product> prod = productRepository.findById(pe.getProduct().getId());

                Product newProduct = prod.get();
                newProduct.setStock(newProduct.getStock() + pe.getQuantity());
                newProduct.setPriceSell(pe.getPrice());
                newProduct.setPriceCost(pe.getPriceCost());

                productRepository.save(newProduct);
            }
            session.removeAttribute("listOfProductEntry");
            return register(new Entry(), new ProductEntry(), session);
        }
        return register(entry, new ProductEntry(), session);
    }
}
