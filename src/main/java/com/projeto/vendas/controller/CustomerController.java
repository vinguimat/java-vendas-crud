package com.projeto.vendas.controller;

import com.projeto.vendas.model.Customer;
import com.projeto.vendas.repository.CityRepository;
import com.projeto.vendas.repository.CustomerRepository;
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
public class CustomerController {

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private CityRepository cityRepository;

    @GetMapping("/registerCustomer")
    public ModelAndView register(Customer customer) {
        ModelAndView mv = new ModelAndView("adm/customers/register");
        mv.addObject("customer", customer);
        mv.addObject("listCity", cityRepository.findAll());
        return mv;
    }

    @GetMapping("/listCustomer")
    public ModelAndView list() {
        ModelAndView mv = new ModelAndView("adm/customers/list");
        mv.addObject("listCustomer", customerRepository.findAll());
        return mv;
    }

    @GetMapping("/editCustomer/{id}")
    public ModelAndView edit(@PathVariable("id") Long id) {
        Optional<Customer> customer = customerRepository.findById(id);
        return register(customer.get());
    }

    @GetMapping("/removeCustomer/{id}")
    public ModelAndView remove(@PathVariable("id") Long id) {
        Optional<Customer> customer = customerRepository.findById(id);
        customerRepository.delete(customer.get());
        return list();
    }

    @PostMapping("/saveCustomer")
    public ModelAndView save(@Valid Customer customer, BindingResult result) {

        if(result.hasErrors()) {
            return register(customer);
        }
        customerRepository.saveAndFlush(customer);
        return register(new Customer());
    }
}
