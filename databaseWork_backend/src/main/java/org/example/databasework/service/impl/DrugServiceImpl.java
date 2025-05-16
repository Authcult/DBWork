package org.example.databasework.service.impl;

import org.example.databasework.model.Drug;
import org.example.databasework.repository.DrugRepository;
import org.example.databasework.service.DrugService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DrugServiceImpl implements DrugService {

    private final DrugRepository drugRepository;

    @Autowired
    public DrugServiceImpl(DrugRepository drugRepository) {
        this.drugRepository = drugRepository;
    }

    @Override
    @Transactional
    public Drug addDrug(Drug drug) {
        return drugRepository.save(drug);
    }

    @Override
    public List<Drug> getAllDrugs() {
        return drugRepository.findAll();
    }
    
    @Override
    public Page<Drug> getDrugsByPage(int page, int pageSize) {
        return drugRepository.findAll(PageRequest.of(page, pageSize));
    }

    @Override
    public Drug getDrugById(Integer drugId) {
        return drugRepository.findById(drugId)
                .orElseThrow(() -> new RuntimeException("药品不存在，ID: " + drugId));
    }

    @Override
    @Transactional
    public Drug updateDrug(Integer drugId, Drug drugDetails) {
        Drug drug = getDrugById(drugId);
        drug.setName(drugDetails.getName());
        drug.setPrice(drugDetails.getPrice());
        drug.setStock(drugDetails.getStock());
        return drugRepository.save(drug);
    }

    @Override
    @Transactional
    public void deleteDrug(Integer drugId) {
        Drug drug = getDrugById(drugId);
        drugRepository.delete(drug);
    }
}