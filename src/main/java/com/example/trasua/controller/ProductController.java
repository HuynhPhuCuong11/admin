package com.example.trasua.controller;

import com.example.trasua.model.Product;
import com.example.trasua.model.ProductVariant;
import com.example.trasua.service.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin/products")
public class ProductController {

    @Autowired private ProductService  productService;
    @Autowired private CategoryService categoryService;
    @Autowired private SizeService     sizeService;

    @GetMapping
    public String list(@RequestParam(defaultValue="") String keyword,
                       @RequestParam(required=false) Long categoryId,
                       @RequestParam(defaultValue="") String status,
                       @RequestParam(defaultValue="0") int page, Model m) {
        m.addAttribute("products",   productService.search(keyword, categoryId, status,
                PageRequest.of(page, 10, Sort.by("createdAt").descending())));
        m.addAttribute("categories", categoryService.findAll());
        m.addAttribute("keyword",    keyword);
        m.addAttribute("categoryId", categoryId);
        m.addAttribute("status",     status);
        m.addAttribute("pageTitle",  "Sản phẩm");
        m.addAttribute("activePage", "products");
        return "admin/products/list";
    }

    @GetMapping("/create")
    public String createForm(Model m) {
        m.addAttribute("product",    new Product());
        m.addAttribute("variants",   List.of());
        m.addAttribute("categories", categoryService.findAll());
        m.addAttribute("sizes",      sizeService.findAll());
        m.addAttribute("pageTitle",  "Thêm sản phẩm");
        m.addAttribute("activePage", "products");
        return "admin/products/form";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model m) {
        m.addAttribute("product",    productService.findById(id));
        m.addAttribute("variants",   productService.findVariants(id));
        m.addAttribute("categories", categoryService.findAll());
        m.addAttribute("sizes",      sizeService.findAll());
        m.addAttribute("pageTitle",  "Sửa sản phẩm");
        m.addAttribute("activePage", "products");
        return "admin/products/form";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute Product product, BindingResult br,
                       @RequestParam(value="imageFile", required=false) MultipartFile img,
                       @RequestParam(value="vSizeId",  required=false) List<Long>       sizeIds,
                       @RequestParam(value="vPrice",   required=false) List<BigDecimal> prices,
                       @RequestParam(value="vSku",     required=false) List<String>     skus,
                       @RequestParam(value="vInStock", required=false) List<String>     stocks,
                       RedirectAttributes ra, Model m) {
        if (br.hasErrors()) {
            m.addAttribute("categories", categoryService.findAll());
            m.addAttribute("sizes",      sizeService.findAll());
            m.addAttribute("variants",   product.getId() != null ? productService.findVariants(product.getId()) : List.of());
            m.addAttribute("pageTitle",  "Sản phẩm");
            m.addAttribute("activePage", "products");
            return "admin/products/form";
        }
        try {
            Product saved = productService.save(product, img);
            // Only save variants if they were actually submitted in the form
            // Check if any variant parameter was provided (form may include variants on edit)
            if (sizeIds != null && !sizeIds.isEmpty()) {
                List<ProductVariant> variants = new ArrayList<>();
                for (int i = 0; i < sizeIds.size(); i++) {
                    ProductVariant v = new ProductVariant();
                    v.setSizeId(sizeIds.get(i));
                    v.setPrice(prices != null && i < prices.size() ? prices.get(i) : BigDecimal.ZERO);
                    v.setSku(skus != null && i < skus.size() ? skus.get(i) : "");
                    // stocks contains "on" for checked checkboxes; if stocks has more than i items, checkbox was checked
                    v.setInStock(stocks != null && i < stocks.size());
                    variants.add(v);
                }
                // Only update variants if they've actually changed (to avoid duplicate SKU error)
                List<ProductVariant> oldVariants = productService.findVariants(saved.getId());
                if (!variantsEqual(oldVariants, variants)) {
                    productService.saveVariants(saved.getId(), variants);
                }
            }
            ra.addFlashAttribute("success", "Lưu sản phẩm thành công!");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }
        return "redirect:/admin/products";
    }

    // Helper method to check if variant lists are equal (to avoid unnecessary updates)
    private boolean variantsEqual(List<ProductVariant> old, List<ProductVariant> newV) {
        if (old.size() != newV.size()) return false;
        for (int i = 0; i < old.size(); i++) {
            ProductVariant o = old.get(i);
            ProductVariant n = newV.get(i);
            if (!o.getSizeId().equals(n.getSizeId()) ||
                !o.getPrice().equals(n.getPrice()) ||
                !o.getSku().equals(n.getSku()) ||
                !o.getInStock().equals(n.getInStock())) {
                return false;
            }
        }
        return true;
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        try { productService.delete(id); ra.addFlashAttribute("success","Đã xóa sản phẩm!"); }
        catch (Exception e) { ra.addFlashAttribute("error","Không thể xóa: "+e.getMessage()); }
        return "redirect:/admin/products";
    }

    @PostMapping("/toggle-featured/{id}")
    public String toggleFeatured(@PathVariable Long id) {
        productService.toggleFeatured(id);
        return "redirect:/admin/products";
    }
}
