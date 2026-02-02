package com.projeto.vendas.controller;

import com.projeto.vendas.model.City;
import com.projeto.vendas.repository.CityRepository;
import com.projeto.vendas.repository.StateRepository;
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
public class CityController {

    @Autowired
    private CityRepository cityRepository;
    @Autowired
    private StateRepository stateRepository;

    @GetMapping("/registerCity")
    public ModelAndView register(City city) {
        ModelAndView mv = new ModelAndView("adm/cities/register");
        mv.addObject("city", city);
        mv.addObject("listState", stateRepository.findAll());
        return mv;
    }

    @GetMapping("/listCity")
    public ModelAndView list() {
        ModelAndView mv = new ModelAndView("adm/cities/list");
        mv.addObject("listCity", cityRepository.findAll());
        return mv;
    }

    @GetMapping("/editCity/{id}")
    public ModelAndView edit(@PathVariable("id") Long id) {
        Optional<City> city = cityRepository.findById(id);
        return register(city.get());
    }

    @GetMapping("/removeCity/{id}")
    public ModelAndView remove(@PathVariable("id") Long id) {
        Optional<City> city = cityRepository.findById(id);
        cityRepository.delete(city.get());
        return list();
    }

    @PostMapping("/saveCity")
    public ModelAndView save(@Valid City city, BindingResult result) {

        if(result.hasErrors()) {
            return register(city);
        }
        cityRepository.saveAndFlush(city);
        return register(new City());
    }
}
