package com.projeto.vendas.controller;

import com.projeto.vendas.model.Sell;
import com.projeto.vendas.model.Product;
import com.projeto.vendas.model.ProductSell;
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
import java.util.Objects;
import java.util.Optional;

@Controller
public class SellController {

    @Autowired
    private SellRepository sellRepository;
    @Autowired
    private ProductSellRepository productsellRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private CustomerRepository customerRepository;

    @GetMapping("/registerSell")
    public ModelAndView register(Sell sell, ProductSell productSell, HttpSession session) {
        ModelAndView mv = new ModelAndView("adm/sells/register");

        Sell sellFromSession = (Sell) session.getAttribute("sell");
        if (sellFromSession != null) {
            sell = sellFromSession; // usa o da sessão
        }

        List<ProductSell> listOfProductSell = (List<ProductSell>) session.getAttribute("listOfProductSell");
        if (listOfProductSell == null) {
            listOfProductSell = new ArrayList<>();
        }

        mv.addObject("sell", sell);
        mv.addObject("productSell", productSell);
        mv.addObject("listOfProductSell", listOfProductSell);
        mv.addObject("listEmployee", employeeRepository.findAll());
        mv.addObject("listCustomer", customerRepository.findAll());
        mv.addObject("listProduct", productRepository.findAll());

        return mv;
    }

    @GetMapping("/listSell")
    public ModelAndView list() {
        ModelAndView mv = new ModelAndView("adm/sells/list");
        mv.addObject("listSell", sellRepository.findAll());
        return mv;
    }

    @GetMapping("/editSell/{id}")
    public ModelAndView edit(@PathVariable("id") Long id, HttpSession session) {
        Optional<Sell> sell = sellRepository.findById(id);

        List<ProductSell> items = productsellRepository.searchForSell(sell.get().getId());

        session.setAttribute("listOfProductSell", items);

        return register(sell.get(), new ProductSell(), session);
    }

    @GetMapping("/removeSell/{id}")
    public ModelAndView remove(@PathVariable("id") Long id) {
        Optional<Sell> sell = sellRepository.findById(id);
        sellRepository.delete(sell.get());
        return list();
    }

    @PostMapping("/saveSell")
    public ModelAndView save(String action, Sell sell, ProductSell productSell, BindingResult result, HttpSession session) {
        // SE TIVER ERRO PARA AQUI MESMO
        if(result.hasErrors()) {
            return register(sell, productSell, session);
        }

        // ----------
        // RECUPERA A VENDA E A LISTA DA SESSÃO
        Sell sellFromSession = (Sell) session.getAttribute("sell");
        if (sellFromSession != null) {
            sell = sellFromSession; // usa o da sessão
        }

        // VERIFICA LISTA DA SESSÃO
        List<ProductSell> listOfProductSell = (List<ProductSell>) session.getAttribute("listOfProductSell");
        if (listOfProductSell == null) {
            listOfProductSell = new ArrayList<>();
        }
        // -----------

        // SE O BOTÃO ADICIONAR FOR ACIONADO
        if (action.equals("products")) {

            double subtotal = productSell.getPrice() * productSell.getQuantity();
            productSell.setSubTotal(subtotal);

            listOfProductSell.add(productSell);// ADICIONA PRODUTO NA LISTA

            sell.setTotalQuantity(sell.getTotalQuantity() + productSell.getQuantity());
            sell.setTotalValue(sell.getTotalValue() + productSell.getPrice());

        } else if (action.equals("save")) { // SE O BOTAO SALVAR FOR ACIONADO

            sell.setTotalQuantity(sell.getTotalQuantity());
            sell.setTotalValue(sell.getTotalValue());

            sellRepository.saveAndFlush(sell); // SALVA SAIDA NO BANCO

            for (ProductSell pe : listOfProductSell) { // PERCORRE A LISTA COM UM FOREACH NOS PRODUTOS

                pe.setSell(sell);  // Associa a venda (relacionamento)

                productsellRepository.saveAndFlush(pe); // SALVA ProductSell NO BANCO

                Optional<Product> prod = productRepository.findById(pe.getProduct().getId());

                Product newProduct = prod.get();
                newProduct.setStock(newProduct.getStock() - pe.getQuantity());

                productRepository.save(newProduct);
            }

            return register(new Sell(), new ProductSell(), session);

        } else if (action.equals("remove")) {

            for (ProductSell pr : listOfProductSell) {
                if (Objects.equals(pr.getId(), productSell.getId())) {
                    listOfProductSell.remove(pr);
                    break;
                }
            }
            recalculateTotals(sell, listOfProductSell);
            return register(sell, new ProductSell(), session);
        }

        session.setAttribute("sell", sell);
        session.setAttribute("listOfProductSell", listOfProductSell);
        return register(sell, new ProductSell(), session);
    }

    private void recalculateTotals(Sell sell, List<ProductSell> list) {
        double totalQty = 0.0;
        double totalVal = 0.0;

        for (ProductSell item : list) {
            totalQty += item.getQuantity();
            totalVal += item.getSubTotal();
        }

        sell.setTotalQuantity(totalQty);
        sell.setTotalValue(totalVal);
    }
}
