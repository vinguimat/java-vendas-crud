package com.projeto.vendas.controller;

import com.projeto.vendas.model.Employee;
import com.projeto.vendas.repository.EmployeeRepository;
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
public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private CityRepository cityRepository;

    @GetMapping("/registerEmployee")
    public ModelAndView register(Employee employee) {
        ModelAndView mv = new ModelAndView("adm/employees/register");
        mv.addObject("employee", employee);
        mv.addObject("listCity", cityRepository.findAll());
        return mv;
    }

    @GetMapping("/listEmployee")
    public ModelAndView list() {
        ModelAndView mv = new ModelAndView("adm/employees/list");
        mv.addObject("listEmployee", employeeRepository.findAll());
        return mv;
    }

    @GetMapping("/editEmployee/{id}")
    public ModelAndView edit(@PathVariable("id") Long id) {
        Optional<Employee> employee = employeeRepository.findById(id);
        return register(employee.get());
    }

    @GetMapping("/removeEmployee/{id}")
    public ModelAndView remove(@PathVariable("id") Long id) {
        Optional<Employee> employee = employeeRepository.findById(id);
        employeeRepository.delete(employee.get());
        return list();
    }

    @PostMapping("/saveEmployee")
    public ModelAndView save(@Valid Employee employee, BindingResult result) {

        if(result.hasErrors()) {
            return register(employee);
        }
        employeeRepository.saveAndFlush(employee);
        return register(new Employee());
    }
}
