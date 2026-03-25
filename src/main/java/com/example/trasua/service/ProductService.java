package com.example.trasua.service;

import com.example.trasua.model.Product;
import com.example.trasua.model.ProductVariant;
import com.example.trasua.repository.ProductRepository;
import com.example.trasua.repository.ProductVariantRepository;
import com.example.trasua.repository.SizeRepository;
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
    @Autowired private SizeRepository sizeRepo;

    @Value("${app.upload.dir}")
    private String uploadDir;

    public Page<Product> search(String keyword, Long categoryId, String status, Pageable pageable) {
        String kw = (keyword == null || keyword.isBlank()) ? null : keyword.trim();
        String st = (status == null || status.isBlank()) ? null : status;
        return productRepo.search(kw, categoryId, st, pageable);
    }

    public Product findById(Long id) {
        return productRepo.findById(id).orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));
    }

    public List<ProductVariant> findVariantsByProductId(Long productId) {
        return variantRepo.findByProductIdWithSize(productId);
    }

    @Transactional
    public Product save(Product product, MultipartFile imageFile) throws IOException {
        // Generate slug nếu chưa có
        if (product.getSlug() == null || product.getSlug().isBlank()) {
            product.setSlug(generateSlug(product.getName()));
        }
        // Ensure slug unique
        String baseSlug = product.getSlug();
        String finalSlug = baseSlug;
        int i = 1;
        while (productRepo.existsBySlug(finalSlug)) {
            // If editing an existing product and slug didn't change, allow it.
            if (product.getId() != null) {
                Product existing = productRepo.findById(product.getId()).orElse(null);
                if (existing != null && finalSlug.equals(existing.getSlug())) {
                    break;
                }
            }
            finalSlug = baseSlug + "-" + i++;
        }
        product.setSlug(finalSlug);

        // Upload ảnh nếu có
        if (imageFile != null && !imageFile.isEmpty()) {
            String fileName = saveImage(imageFile);
            product.setDefaultImage(fileName);
        }
        return productRepo.save(product);
    }

    @Transactional
    public void saveVariants(Long productId, List<ProductVariant> variants) {
        variantRepo.deleteByProductId(productId);
        for (ProductVariant v : variants) {
            v.setProductId(productId);
            variantRepo.save(v);
        }
    }

    @Transactional
    public void delete(Long id) {
        variantRepo.deleteByProductId(id);
        productRepo.deleteById(id);
    }

    public long countByStatus(String status) {
        return productRepo.countByStatus(status);
    }

    public long countAll() {
        return productRepo.count();
    }

    private String saveImage(MultipartFile file) throws IOException {
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) Files.createDirectories(uploadPath);
        String ext = getExtension(file.getOriginalFilename());
        String fileName = UUID.randomUUID() + "." + ext;
        Files.copy(file.getInputStream(), uploadPath.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
        return fileName;
    }

    private String getExtension(String fileName) {
        if (fileName == null) return "jpg";
        int dot = fileName.lastIndexOf('.');
        return (dot >= 0) ? fileName.substring(dot + 1) : "jpg";
    }

    public static String generateSlug(String name) {
        if (name == null) return "";
        String slug = name.toLowerCase()
                .replaceAll("[àáạảãâầấậẩẫăằắặẳẵ]", "a")
                .replaceAll("[èéẹẻẽêềếệểễ]", "e")
                .replaceAll("[ìíịỉĩ]", "i")
                .replaceAll("[òóọỏõôồốộổỗơờớợởỡ]", "o")
                .replaceAll("[ùúụủũưừứựửữ]", "u")
                .replaceAll("[ỳýỵỷỹ]", "y")
                .replaceAll("[đ]", "d")
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-+", "-")
                .replaceAll("^-|-$", "");
        return slug;
    }
}
