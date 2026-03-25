package com.example.trasua.service;

import com.example.trasua.model.Size;
import com.example.trasua.repository.SizeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SizeService {
    @Autowired private SizeRepository sizeRepo;

    public List<Size> findAll() { return sizeRepo.findAll(); }

    public Size findById(Long id) {
        return sizeRepo.findById(id).orElseThrow(() -> new RuntimeException("Size không tồn tại"));
    }

    public Size save(Size size) { return sizeRepo.save(size); }

    public void delete(Long id) { sizeRepo.deleteById(id); }
}
