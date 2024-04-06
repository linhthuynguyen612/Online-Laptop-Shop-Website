package vn.hoidanit.laptopshop.controller.admin;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import vn.hoidanit.laptopshop.domain.Product;
import vn.hoidanit.laptopshop.service.ProductService;
import vn.hoidanit.laptopshop.service.UploadService;

import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;




@Controller
public class ProductController {

    private final UploadService uploadService;
    private final ProductService productService;

    public ProductController(ProductService productService, UploadService uploadService) {
        this.uploadService = uploadService;
        this.productService = productService;
    }
    
    @GetMapping("/admin/product")
    public String getProduct(Model model){
        List<Product> products = this.productService.getAllProducts();
        model.addAttribute("products", products);
        return "admin/product/show";
    }

    @GetMapping("/admin/product/{id}")
    public String getProductDetail(Model model, @PathVariable long id) {
        Product product = this.productService.getProductByID(id);
        model.addAttribute("product", product);
        model.addAttribute("id", id);
        return "admin/product/detail";
    }
    

    @GetMapping("/admin/product/create")
    public String getCreateProductPage(Model model) {
        model.addAttribute("newProduct", new Product());
        return "admin/product/create";
    }

    @PostMapping("/admin/product/create")
    public String postCreateProduct(Model model,
                                 @ModelAttribute("newProduct") @Valid Product product,
                                 BindingResult newProductBindingResult,
                                @RequestParam("anhFile") MultipartFile file) {
        // Validate
        if(newProductBindingResult.hasErrors()) {
            return "admin/product/create";
        }
        //
        

        String image= this.uploadService.handleSaveUploadFile(file, "product");
        product.setImage(image);
        this.productService.handleSaveProduct(product);
        return "redirect:/admin/product";
    }

    @GetMapping("/admin/product/update/{id}")
    public String getUpdateUserPage(Model model, @PathVariable long id) {
        Product currentProduct = this.productService.getProductByID(id);
        model.addAttribute("newProduct", currentProduct);
        return "admin/product/update";
    }

    @PostMapping("/admin/product/update")
    public String postUpdateProduct(Model model, @ModelAttribute("newProduct") @Valid Product product, BindingResult newProductBindingResult, @RequestParam("anhFile") MultipartFile file){
        if(newProductBindingResult.hasErrors()) {
            return "admin/product/update";
        }
        Product currentProduct = this.productService.getProductByID(product.getId());
        if (!file.isEmpty()){
            String img = this.uploadService.handleSaveUploadFile(file, "product");
            currentProduct.setImage(img);
        }
        
        currentProduct.setName(product.getName());
        currentProduct.setPrice(product.getPrice());
        currentProduct.setDetailDesc(product.getDetailDesc());
        currentProduct.setShortDesc(product.getShortDesc());
        currentProduct.setQuantity(product.getQuantity());
        currentProduct.setFactory(product.getFactory());
        currentProduct.setTarget(product.getTarget());
        this.productService.handleSaveProduct(currentProduct);
        
        return "redirect:/admin/product";
    }

    @GetMapping("/admin/product/delete/{id}")
    public String getMethodName(Model model, @PathVariable long id) {
        Product pr = new Product();
        pr.setId(id);
        model.addAttribute("newProduct", pr);
        model.addAttribute("id", id);
        return "admin/product/delete";

    }

    @PostMapping("/admin/product/delete")
    public String postMethodName(Model model, @ModelAttribute("newProduct") Product product) {
        this.productService.deleteProductByID(product.getId());
        return "redirect:/admin/product";
    }
    
    
    
    
    
    
}
