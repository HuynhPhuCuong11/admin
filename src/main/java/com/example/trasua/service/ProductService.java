package com.example.trasua.service;

import com.example.trasua.model.Product;
import com.example.trasua.model.ProductVariant;
import com.example.trasua.repository.ProductRepository;
import com.example.trasua.repository.ProductVariantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.UUID;

@Service
public class ProductService {

    @Autowired private ProductRepository productRepo;
    @Autowired private ProductVariantRepository variantRepo;

    @Value("${app.upload.dir}") private String uploadDir;

    public Page<Product> search(String kw, Long catId, String status, Pageable p) {
        return productRepo.search(nullIfBlank(kw), catId, nullIfBlank(status), p);
    }

    public Product findById(Long id) {
        return productRepo.findById(id).orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));
    }

    public List<ProductVariant> findVariants(Long productId) {
        return variantRepo.findByProductIdWithSize(productId);
    }

    @Transactional
    public Product save(Product p, MultipartFile imageFile) throws IOException {
        if (p.getSlug() == null || p.getSlug().isBlank());
        // Đảm bảo slug unique (trừ chính nó)
        String base = p.getSlug(), slug = base;
        int i = 1;
        while (productRepo.existsBySlug(slug)) {
            // Nếu là update và slug không đổi thì ok
            if (p.getId() != null) {
                final String check = slug; // capture current slug for lambda
                if (productRepo.findById(p.getId()).map(x -> x.getSlug().equals(check)).orElse(false)) break;
            }
            slug = base + "-" + i++;
        }
        p.setSlug(slug);
        if (imageFile != null && !imageFile.isEmpty()) {
            p.setDefaultImage(saveFile(imageFile));
        }  else if (p.getId() != null && (p.getDefaultImage() == null || p.getDefaultImage().isBlank()))  {
            // Giữ lại ảnh cũ nếu không upload ảnh mới
            productRepo.findById(p.getId()).ifPresent(existing -> p.setDefaultImage(existing.getDefaultImage()));
        }
        return productRepo.save(p);
    }

    @Transactional
    public void saveVariants(Long productId, List<ProductVariant> variants) {
        variantRepo.deleteByProductId(productId);
        for (ProductVariant v : variants) { v.setProductId(productId); variantRepo.save(v); }
    }

    @Transactional
    public void delete(Long id) { variantRepo.deleteByProductId(id); productRepo.deleteById(id); }

    @Transactional
    public void toggleFeatured(Long id) {
        Product p = findById(id);
        p.setIsFeatured(!Boolean.TRUE.equals(p.getIsFeatured()));
        productRepo.save(p);
    }

    public long countAll()              { return productRepo.count(); }
    public long countByStatus(String s) { return productRepo.countByStatus(s); }

    private String saveFile(MultipartFile f) throws IOException {
        Path dir = Paths.get(uploadDir);
        if (!Files.exists(dir)) Files.createDirectories(dir);
        String ext = f.getOriginalFilename() != null && f.getOriginalFilename().contains(".")
                ? f.getOriginalFilename().substring(f.getOriginalFilename().lastIndexOf('.') + 1) : "jpg";
        String name = UUID.randomUUID() + "." + ext;
        Files.copy(f.getInputStream(), dir.resolve(name), StandardCopyOption.REPLACE_EXISTING);
        return name;
    }

    private String nullIfBlank(String s) { return (s == null || s.isBlank()) ? null : s.trim(); }

    public static String toSlug(String name) {
        if (name == null) return "";
        return name.toLowerCase()
                .replaceAll("[àáạảãâầấậẩẫăằắặẳẵ]","a").replaceAll("[èéẹẻẽêềếệểễ]","e")
                .replaceAll("[ìíịỉĩ]","i").replaceAll("[òóọỏõôồốộổỗơờớợởỡ]","o")
                .replaceAll("[ùúụủũưừứựửữ]","u").replaceAll("[ỳýỵỷỹ]","y").replaceAll("đ","d")
                .replaceAll("[^a-z0-9\\s-]","").replaceAll("\\s+","-").replaceAll("-+","-")
                .replaceAll("^-|-$","");
    }
}
