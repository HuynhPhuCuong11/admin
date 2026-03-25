package com.example.trasua.controller;

import com.example.trasua.model.Product;
import com.example.trasua.model.ProductVariant;
import com.example.trasua.service.CategoryService;
import com.example.trasua.service.ProductService;
import com.example.trasua.service.SizeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

    @Autowired private ProductService productService;
    @Autowired private CategoryService categoryService;
    @Autowired private SizeService sizeService;

    @GetMapping
    public String list(@RequestParam(defaultValue = "") String keyword,
                       @RequestParam(required = false) Long categoryId,
                       @RequestParam(defaultValue = "") String status,
                       @RequestParam(defaultValue = "0") int page,
                       Model model) {
        var pageResult = productService.search(keyword, categoryId, status,
                PageRequest.of(page, 10, Sort.by("createdAt").descending()));

        model.addAttribute("products",    pageResult);
        model.addAttribute("categories",  categoryService.findAll());
        model.addAttribute("keyword",     keyword);
        model.addAttribute("categoryId",  categoryId);
        model.addAttribute("status",      status);
        model.addAttribute("pageTitle",   "Quản lý sản phẩm");
        model.addAttribute("activePage",  "products");
        return "admin/products/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("product",    new Product());
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("sizes",      sizeService.findAll());
        model.addAttribute("pageTitle",  "Thêm sản phẩm");
        model.addAttribute("activePage", "products");
        return "admin/products/form";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Product product = productService.findById(id);
        List<ProductVariant> variants = productService.findVariantsByProductId(id);

        model.addAttribute("product",    product);
        model.addAttribute("variants",   variants);
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("sizes",      sizeService.findAll());
        model.addAttribute("pageTitle",  "Chỉnh sửa sản phẩm");
        model.addAttribute("activePage", "products");
        return "admin/products/form";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute Product product,
                       BindingResult bindingResult,
                       @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                       @RequestParam(value = "variantSizeId",  required = false) List<Long> sizeIds,
                       @RequestParam(value = "variantPrice",   required = false) List<BigDecimal> prices,
                       @RequestParam(value = "variantSku",     required = false) List<String> skus,
                       @RequestParam(value = "variantInStock", required = false) List<String> inStocks,
                       RedirectAttributes redirect,
                       Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categoryService.findAll());
            model.addAttribute("sizes",      sizeService.findAll());
            model.addAttribute("pageTitle",  "Sản phẩm");
            model.addAttribute("activePage", "products");
            return "admin/products/form";
        }

        try {
            Product saved = productService.save(product, imageFile);

            // Lưu variants nếu có
            if (sizeIds != null && !sizeIds.isEmpty()) {
                List<ProductVariant> variants = new ArrayList<>();
                for (int i = 0; i < sizeIds.size(); i++) {
                    ProductVariant v = new ProductVariant();
                    v.setSizeId(sizeIds.get(i));
                    v.setPrice(prices != null && i < prices.size() ? prices.get(i) : BigDecimal.ZERO);
                    v.setSku(skus != null && i < skus.size() ? skus.get(i) : "");
                    v.setInStock(inStocks == null || !inStocks.contains(String.valueOf(i)) ? true : true);
                    variants.add(v);
                }
                productService.saveVariants(saved.getId(), variants);
            }

            redirect.addFlashAttribute("success", "Lưu sản phẩm thành công!");
        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }
        return "redirect:/admin/products";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            productService.delete(id);
            redirect.addFlashAttribute("success", "Đã xóa sản phẩm!");
        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Không thể xóa: " + e.getMessage());
        }
        return "redirect:/admin/products";
    }

    @PostMapping("/toggle-featured/{id}")
    public String toggleFeatured(@PathVariable Long id, RedirectAttributes redirect) {
        Product p = productService.findById(id);
        p.setIsFeatured(!Boolean.TRUE.equals(p.getIsFeatured()));
        try { productService.save(p, null); } catch (Exception ignored) {}
        return "redirect:/admin/products";
    }
}
