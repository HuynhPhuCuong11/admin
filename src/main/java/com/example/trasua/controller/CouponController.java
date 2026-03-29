package com.example.trasua.controller;

import com.example.trasua.model.Coupon;
import com.example.trasua.service.CategoryService;
import com.example.trasua.service.CouponService;
import com.example.trasua.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/coupons")
public class CouponController {
    @Autowired private CouponService   svc;
    @Autowired private CategoryService categorySvc;
    @Autowired private ProductService  productSvc;

    @GetMapping
    public String list(@RequestParam(defaultValue="") String keyword,
                       @RequestParam(required=false) Boolean active,
                       @RequestParam(defaultValue="0") int page, Model m) {
        m.addAttribute("coupons",  svc.search(keyword, active,
                PageRequest.of(page, 15, Sort.by("createdAt").descending())));
        m.addAttribute("keyword", keyword);
        m.addAttribute("active",  active);
        m.addAttribute("pageTitle","Mã giảm giá"); m.addAttribute("activePage","coupons");
        return "admin/coupons/list";
    }

    @GetMapping("/create")
    public String createForm(Model m) {
        m.addAttribute("coupon",     new Coupon());
        m.addAttribute("categories", categorySvc.findAll());
        m.addAttribute("pageTitle","Thêm mã giảm giá"); m.addAttribute("activePage","coupons");
        return "admin/coupons/form";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model m) {
        m.addAttribute("coupon",     svc.findById(id));
        m.addAttribute("categories", categorySvc.findAll());
        m.addAttribute("pageTitle","Sửa mã giảm giá"); m.addAttribute("activePage","coupons");
        return "admin/coupons/form";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute Coupon c, BindingResult br,
                       RedirectAttributes ra, Model m) {
        if (br.hasErrors()) {
            m.addAttribute("categories", categorySvc.findAll());
            m.addAttribute("pageTitle","Mã giảm giá"); m.addAttribute("activePage","coupons");
            return "admin/coupons/form";
        }
        try { svc.save(c); ra.addFlashAttribute("success","Lưu coupon thành công!"); }
        catch (Exception e) { ra.addFlashAttribute("error","Lỗi: "+e.getMessage()); }
        return "redirect:/admin/coupons";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        try { svc.delete(id); ra.addFlashAttribute("success","Đã xóa!"); }
        catch (Exception e) { ra.addFlashAttribute("error","Không thể xóa: "+e.getMessage()); }
        return "redirect:/admin/coupons";
    }

    @PostMapping("/toggle/{id}")
    public String toggle(@PathVariable Long id) {
        svc.toggle(id);
        return "redirect:/admin/coupons";
    }
}
