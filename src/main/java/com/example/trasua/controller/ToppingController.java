package com.example.trasua.controller;

import com.example.trasua.model.Topping;
import com.example.trasua.service.ToppingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/toppings")
public class ToppingController {
    @Autowired private ToppingService svc;

    @GetMapping
    public String list(@RequestParam(defaultValue="") String keyword,
                       @RequestParam(defaultValue="") String status,
                       @RequestParam(defaultValue="0") int page, Model m) {
        m.addAttribute("toppings", svc.search(keyword, status, PageRequest.of(page,15,Sort.by("id").ascending())));
        m.addAttribute("keyword",keyword); m.addAttribute("status",status);
        m.addAttribute("pageTitle","Topping"); m.addAttribute("activePage","toppings");
        return "admin/toppings/list";
    }

    @GetMapping("/create")
    public String createForm(Model m) {
        m.addAttribute("topping",new Topping());
        m.addAttribute("pageTitle","Thêm topping"); m.addAttribute("activePage","toppings");
        return "admin/toppings/form";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model m) {
        m.addAttribute("topping", svc.findById(id));
        m.addAttribute("pageTitle","Sửa topping"); m.addAttribute("activePage","toppings");
        return "admin/toppings/form";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute Topping t, BindingResult br,
                       RedirectAttributes ra, Model m) {
        if (br.hasErrors()) {
            m.addAttribute("pageTitle","Topping"); m.addAttribute("activePage","toppings");
            return "admin/toppings/form";
        }
        try { svc.save(t); ra.addFlashAttribute("success","Lưu topping thành công!"); }
        catch (Exception e) { ra.addFlashAttribute("error","Lỗi: "+e.getMessage()); }
        return "redirect:/admin/toppings";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        try { svc.delete(id); ra.addFlashAttribute("success","Đã xóa!"); }
        catch (Exception e) { ra.addFlashAttribute("error","Không thể xóa: "+e.getMessage()); }
        return "redirect:/admin/toppings";
    }

    @PostMapping("/toggle-stock/{id}")
    public String toggleStock(@PathVariable Long id) { svc.toggleStock(id); return "redirect:/admin/toppings"; }
}
