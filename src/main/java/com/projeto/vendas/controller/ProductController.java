package com.projeto.vendas.controller;

import com.projeto.vendas.model.Product;
import com.projeto.vendas.repository.ProductRepository;
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
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/registerProduct")
    public ModelAndView register(Product product) {
        ModelAndView mv = new ModelAndView("adm/products/register");
        mv.addObject("product", product);
        return mv;
    }

    @GetMapping("/listProduct")
    public ModelAndView list() {
        ModelAndView mv = new ModelAndView("adm/products/list");
        mv.addObject("listProduct", productRepository.findAll());
        return mv;
    }

    @GetMapping("/editProduct/{id}")
    public ModelAndView edit(@PathVariable("id") Long id) {
        Optional<Product> product = productRepository.findById(id);
        return register(product.get());
    }

    @GetMapping("/removeProduct/{id}")
    public ModelAndView remove(@PathVariable("id") Long id) {
        Optional<Product> product = productRepository.findById(id);
        productRepository.delete(product.get());
        return list();
    }

    @PostMapping("/saveProduct")
    public ModelAndView save(@Valid Product product, BindingResult result) {

        if(result.hasErrors()) {
            return register(product);
        }
        productRepository.saveAndFlush(product);
        return register(new Product());
    }
}
