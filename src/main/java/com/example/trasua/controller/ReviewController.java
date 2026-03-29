package com.example.trasua.controller;

import com.example.trasua.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/reviews")
public class ReviewController {
    @Autowired private ReviewService svc;

    @GetMapping
    public String list(@RequestParam(defaultValue="") String keyword,
                       @RequestParam(defaultValue="") String status,
                       @RequestParam(required=false) Integer rating,
                       @RequestParam(defaultValue="0") int page, Model m) {
        m.addAttribute("reviews", svc.search(keyword, status, rating,
                PageRequest.of(page, 15, Sort.by("createdAt").descending())));
        m.addAttribute("keyword",keyword); m.addAttribute("status",status); m.addAttribute("rating",rating);
        m.addAttribute("pageTitle","Đánh giá"); m.addAttribute("activePage","reviews");
        return "admin/reviews/list";
    }

    @PostMapping("/{id}/approve")
    public String approve(@PathVariable Long id, RedirectAttributes ra) {
        svc.approve(id); ra.addFlashAttribute("success","Đã duyệt đánh giá!");
        return "redirect:/admin/reviews";
    }

    @PostMapping("/{id}/reject")
    public String reject(@PathVariable Long id, RedirectAttributes ra) {
        svc.reject(id); ra.addFlashAttribute("success","Đã từ chối đánh giá!");
        return "redirect:/admin/reviews";
    }

    @PostMapping("/{id}/reply")
    public String reply(@PathVariable Long id,
                        @RequestParam String replyText, RedirectAttributes ra) {
        svc.reply(id, replyText); ra.addFlashAttribute("success","Đã gửi phản hồi!");
        return "redirect:/admin/reviews";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        try { svc.delete(id); ra.addFlashAttribute("success","Đã xóa!"); }
        catch (Exception e) { ra.addFlashAttribute("error","Lỗi: "+e.getMessage()); }
        return "redirect:/admin/reviews";
    }
}
