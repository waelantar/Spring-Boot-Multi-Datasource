package com.example.springbootmultidatasource.controller;

import com.example.springbootmultidatasource.config.UserDataSourceConfiguration;
import com.example.springbootmultidatasource.entity.User.User;
import com.example.springbootmultidatasource.entity.Product.Product;
import com.example.springbootmultidatasource.service.ProductService;
import com.example.springbootmultidatasource.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.sql.DataSource;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

@Controller
public class DemoController {
    private final UserService userService;
    private final ProductService productService;
    private final DataSource userDataSource;
    private final DataSource productDataSource;

    @Autowired
    public DemoController(UserService userService, ProductService productService,
                          @Qualifier("userDataSource") DataSource userDataSource,
                          @Qualifier("productDataSource") DataSource productDataSource) {
        this.userService = userService;
        this.productService = productService;
        this.userDataSource = userDataSource;
        this.productDataSource = productDataSource;
    }
    @GetMapping("/")
    public String showDemoPage(Model model) throws SQLException {
        DatabaseMetaData metaData = userDataSource.getConnection().getMetaData();
        String dbName = metaData.getURL();
        DatabaseMetaData metaDataP = productDataSource.getConnection().getMetaData();
        String dbNameP = metaDataP.getURL();
        model.addAttribute("userDbTitle", dbName);
        model.addAttribute("productDbTitle", dbNameP);
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("products", productService.getAllProducts());

        return "demo";
    }
    @GetMapping("/redirect/{type}")
    public String redirectToPage(Model model, @PathVariable("type") String type) {
        if ("user".equalsIgnoreCase(type)) {
            model.addAttribute("user", new User());
            return "add_user_form";
        } else if ("product".equalsIgnoreCase(type)) {
            model.addAttribute("product", new Product());
            return "add_product_form";
        } else {
            // Handle invalid type
            return "error"; // Or redirect to an error page
        }
    }
    @PostMapping("/users/add")
    public String addUser(@ModelAttribute User user, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            // If there are validation errors, handle them appropriately
            return "add_user_form";
        }

        try {
            userService.saveUser(user);
            redirectAttributes.addFlashAttribute("successMessage", "User added successfully");
            return "redirect:/";
        } catch (DataIntegrityViolationException e) {
            // Handle database constraint violations
            redirectAttributes.addFlashAttribute("errorMessage", "Error: User already exists.");
            return "redirect:/";
        } catch (Exception e) {
            // Handle other unexpected errors
            redirectAttributes.addFlashAttribute("errorMessage", "Error: Failed to add user.");
            return "redirect:/";
        }
    }

    @PostMapping("/products/add")
    public String addProduct(@ModelAttribute Product product, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            // If there are validation errors, handle them appropriately
            return "add_product_form";
        }

        try {
            productService.saveProduct(product);
            redirectAttributes.addFlashAttribute("successMessage", "Product added successfully");
            return "redirect:/";
        } catch (DataIntegrityViolationException e) {
            // Handle database constraint violations
            redirectAttributes.addFlashAttribute("errorMessage", "Error: Product already exists.");
            return "redirect:/";
        } catch (Exception e) {
            // Handle other unexpected errors
            redirectAttributes.addFlashAttribute("errorMessage", "Error: Failed to add product.");
            return "redirect:/";
        }
    }
    @ExceptionHandler(WebExchangeBindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleBindingErrors(WebExchangeBindException ex, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", "Error: Invalid input data.");
        return "redirect:/";
    }
}
