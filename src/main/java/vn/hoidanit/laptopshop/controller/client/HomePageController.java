package vn.hoidanit.laptopshop.controller.client;


import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import vn.hoidanit.laptopshop.domain.Product;
import vn.hoidanit.laptopshop.service.ProductService;
import org.springframework.web.bind.annotation.RequestParam;




@Controller
public class HomePageController {

    private final ProductService productService;
    

    public HomePageController(ProductService productService) {
        this.productService = productService;
    }
    
    @GetMapping("/")
    public String getHomePage(Model model) {
        List<Product> products = this.productService.getAllProducts();
        model.addAttribute("products", products);
        return "client/homepage/show";
    }
    
    @GetMapping("/access-denied")
    public String getAccessDenyPage() {
        return "client/auth/deny";
    }
    
}
