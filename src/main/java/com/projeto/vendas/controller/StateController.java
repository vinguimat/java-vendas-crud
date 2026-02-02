package com.projeto.vendas.controller;

import com.projeto.vendas.model.State;
import com.projeto.vendas.repository.StateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
public class StateController {

    @Autowired
    private StateRepository stateRepository;

    @GetMapping("/registerState")
    public ModelAndView register(State state) {
        ModelAndView mv = new ModelAndView("adm/states/register");
        mv.addObject("state", state);
        return mv;
    }

    @GetMapping("/listState")
    public ModelAndView list() {
        ModelAndView mv = new ModelAndView("adm/states/list");
        mv.addObject("listState", stateRepository.findAll());
        return mv;
    }

    @GetMapping("/editState/{id}")
    public ModelAndView edit(@PathVariable("id") Long id) {
        Optional<State> state = stateRepository.findById(id);
        return register(state.get());
    }

    @GetMapping("/removeState/{id}")
    public ModelAndView remove(@PathVariable("id") Long id) {
        Optional<State> state = stateRepository.findById(id);
        stateRepository.delete(state.get());
        return list();
    }

    @PostMapping("/saveState")
    public ModelAndView save(State state, BindingResult result) {
        if(result.hasErrors()) {
            return register(state);
        }
        stateRepository.saveAndFlush(state);
        return register(new State());
    }
}
