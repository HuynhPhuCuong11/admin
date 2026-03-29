package com.example.trasua.service;

import com.example.trasua.model.Size;
import com.example.trasua.repository.SizeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class SizeService {
    @Autowired private SizeRepository repo;
    public List<Size> findAll()    { return repo.findAll(); }
    public Size findById(Long id)  { return repo.findById(id).orElseThrow(()->new RuntimeException("Size không tồn tại")); }
    @Transactional public Size save(Size s)   { return repo.save(s); }
    @Transactional public void delete(Long id){ repo.deleteById(id); }
}
