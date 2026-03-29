package com.example.trasua.controller;

import com.example.trasua.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/orders")
public class OrderController {
    @Autowired private OrderService svc;

    @GetMapping
    public String list(@RequestParam(defaultValue="") String keyword,
                       @RequestParam(defaultValue="") String orderStatus,
                       @RequestParam(defaultValue="") String payStatus,
                       @RequestParam(defaultValue="0") int page, Model m) {
        m.addAttribute("orders", svc.search(keyword, orderStatus, payStatus,
                PageRequest.of(page,15,Sort.by("createdAt").descending())));
        m.addAttribute("keyword",keyword); m.addAttribute("orderStatus",orderStatus); m.addAttribute("payStatus",payStatus);
        m.addAttribute("pageTitle","Đơn hàng"); m.addAttribute("activePage","orders");
        return "admin/orders/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model m) {
        m.addAttribute("order", svc.findById(id));
        m.addAttribute("pageTitle","Chi tiết đơn"); m.addAttribute("activePage","orders");
        return "admin/orders/detail";
    }

    @PostMapping("/{id}/status")
    public String updateStatus(@PathVariable Long id,
                               @RequestParam(required=false) String orderStatus,
                               @RequestParam(required=false) String payStatus,
                               @RequestParam(required=false) String note,
                               @RequestParam(required=false) String tracking,
                               RedirectAttributes ra) {
        try { svc.updateStatus(id, orderStatus, payStatus, note, tracking);
              ra.addFlashAttribute("success","Cập nhật thành công!"); }
        catch (Exception e) { ra.addFlashAttribute("error","Lỗi: "+e.getMessage()); }
        return "redirect:/admin/orders/"+id;
    }

    @PostMapping("/{id}/cancel")
    public String cancel(@PathVariable Long id, @RequestParam(required=false) String reason, RedirectAttributes ra) {
        try { svc.cancel(id, reason); ra.addFlashAttribute("success","Đã hủy đơn!"); }
        catch (Exception e) { ra.addFlashAttribute("error","Lỗi: "+e.getMessage()); }
        return "redirect:/admin/orders/"+id;
    }
}
