package com.example.tacocloud.web;

import com.example.tacocloud.Ingredient;
import com.example.tacocloud.Ingredient.Type;
import com.example.tacocloud.Order;
import com.example.tacocloud.Taco;
import com.example.tacocloud.User;
import com.example.tacocloud.data.IngredientRepository;
import com.example.tacocloud.data.TacoRepository;
import com.example.tacocloud.data.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/design")
@SessionAttributes("order")
public class DesignTacoController {
    private final IngredientRepository ingredientRepository;

    private final TacoRepository tacoRepository;

    private final UserRepository userRepository;
    @ModelAttribute(name = "order")
    public Order order(){
        return new Order();
    }

    @ModelAttribute(name="taco")
    public Taco taco(){
        return new Taco();
    }
    @Autowired
    public DesignTacoController(IngredientRepository ingredientRepository,TacoRepository tacoRepository,UserRepository userRepository){
        this.ingredientRepository = ingredientRepository;
        this.tacoRepository = tacoRepository;
        this.userRepository = userRepository;
    }
    @GetMapping
    public String showDesignForm(Model model, Principal principal){
        List<Ingredient> ingredients = new ArrayList<>();
        ingredientRepository.findAll().forEach(i->ingredients.add(i));

        Type[] types = Ingredient.Type.values();
        for (Type type : types) {
            model.addAttribute(type.toString().toLowerCase(),
                    filterByType(ingredients, type));
        }

        String username = principal.getName();
        User user = userRepository.findByUsername(username);
        model.addAttribute("user",user);

        return "design";
    }
    @PostMapping
    public String processDesign(@Valid Taco design, Errors errors, @ModelAttribute Order order){
        if(errors.hasErrors()){
            return "design";
        }
        Taco saved = tacoRepository.save(design);
        order.addDesign(saved);
        return "redirect:/orders/current";
    }
    private List<Ingredient> filterByType(List<Ingredient> ingredients, Type type){
        return ingredients
                .stream()
                .filter(x -> x.getType().equals(type))
                .collect(Collectors.toList());
    }
}
