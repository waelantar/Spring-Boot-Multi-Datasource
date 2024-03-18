package com.example.springbootmultidatasource.controller;

import com.example.springbootmultidatasource.config.UserDataSourceConfiguration;
import com.example.springbootmultidatasource.entity.User.User;
import com.example.springbootmultidatasource.entity.Product.Product;
import com.example.springbootmultidatasource.service.ProductService;
import com.example.springbootmultidatasource.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.sql.DataSource;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

@Controller
public class DemoController {
@Autowired
    private UserService userService;
@Autowired
    private ProductService productService;
    @Qualifier("userDataSource")
    @Autowired
    private DataSource dataSource;
    @Qualifier("productDataSource")
    @Autowired
    private DataSource dataSourceP;
    @GetMapping("/")
    public String showDemoPage(Model model) throws SQLException {
        DatabaseMetaData metaData = dataSource.getConnection().getMetaData();
        String dbName = metaData.getURL();
        DatabaseMetaData metaDataP = dataSourceP.getConnection().getMetaData();
        String dbNameP = metaDataP.getURL();
        model.addAttribute("userDbTitle", dbName);
        model.addAttribute("productDbTitle", dbNameP);
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("products", productService.getAllProducts());

        return "demo";
    }
    @GetMapping("/redirect")
    public String redirectToAnotherPage(Model model) {
        model.addAttribute("user", new User());
        return "add_user_form";  }
    @GetMapping("/predirect")
    public String redirectToProductPage(Model model) {
        model.addAttribute("product", new Product());
        return "add_product_form";  }
    @PostMapping("/users/add")
    public String addUser(@ModelAttribute User user, RedirectAttributes redirectAttributes) {
        userService.saveUser(user);
        redirectAttributes.addFlashAttribute("successMessage", "User added successfully");
        return "redirect:/";
    }

    @PostMapping("/products/add")
    public String addProduct(@ModelAttribute Product product, RedirectAttributes redirectAttributes) {
        productService.saveProduct(product);
        redirectAttributes.addFlashAttribute("successMessage", "Product added successfully");
        return "redirect:/";
    }
}
