package com.projeto.vendas.controller;

import com.projeto.vendas.model.City;
import com.projeto.vendas.model.Supplier;
import com.projeto.vendas.repository.SupplierRepository;
import com.projeto.vendas.repository.CityRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
public class SupplierController {

    @Autowired
    private SupplierRepository supplierRepository;
    @Autowired
    private CityRepository cityRepository;

    @GetMapping("/registerSupplier")
    public ModelAndView register(Supplier supplier) {
        ModelAndView mv = new ModelAndView("adm/suppliers/register");
        mv.addObject("supplier", supplier);
        mv.addObject("listCity", cityRepository.findAll());
        return mv;
    }

    @GetMapping("/listSupplier")
    public ModelAndView list() {
        ModelAndView mv = new ModelAndView("adm/suppliers/list");
        mv.addObject("listSupplier", supplierRepository.findAll());
        return mv;
    }

    @GetMapping("/editSupplier/{id}")
    public ModelAndView edit(@PathVariable("id") Long id) {
        Optional<Supplier> supplier = supplierRepository.findById(id);
        return register(supplier.get());
    }

    @GetMapping("/removeSupplier/{id}")
    public ModelAndView remove(@PathVariable("id") Long id) {
        Optional<Supplier> supplier = supplierRepository.findById(id);
        supplierRepository.delete(supplier.get());
        return list();
    }

    @PostMapping("/saveSupplier")
    public ModelAndView save(@Valid Supplier supplier, BindingResult result) {

        if(result.hasErrors()) {
            return register(supplier);
        }

        Long cityId = supplier.getCity().getId();
        City city = cityRepository.findById(cityId)
                .orElseThrow(() -> new IllegalArgumentException("Cidade inválida"));

        supplier.setCity(city);

        supplierRepository.saveAndFlush(supplier);
        return register(new Supplier());
    }
}
