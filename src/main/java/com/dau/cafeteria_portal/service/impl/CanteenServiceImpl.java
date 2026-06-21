package com.dau.cafeteria_portal.service.impl;

import com.dau.cafeteria_portal.dto.CanteenDTO;
import com.dau.cafeteria_portal.entity.Canteen;
import com.dau.cafeteria_portal.mapper.CanteenMapper;
import com.dau.cafeteria_portal.repository.CanteenRepository;
import com.dau.cafeteria_portal.service.CanteenService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CanteenServiceImpl implements CanteenService {

    private final CanteenRepository canteenRepository;
    private final CanteenMapper canteenMapper;

    public CanteenServiceImpl(CanteenRepository canteenRepository, CanteenMapper canteenMapper) {
        this.canteenRepository = canteenRepository;
        this.canteenMapper = canteenMapper;
    }

    @Override
    public List<CanteenDTO> getAllCanteens() {
        return canteenRepository.findAll()
                .stream()
                .map(canteenMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CanteenDTO getCanteenById(Long id) {
        Canteen canteen = canteenRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Canteen not found with id: " + id));
        return canteenMapper.toDTO(canteen);
    }

    @Override
    public void addCanteen(CanteenDTO canteenDTO) {
        Canteen canteen = canteenMapper.toEntity(canteenDTO);
        canteenRepository.save(canteen);
    }

    @Override
    public void updateCanteen(Long id, CanteenDTO updatedCanteen) {
        Canteen existing = canteenRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Canteen not found with id: " + id));

        existing.setCanteenName(updatedCanteen.getCanteenName());
        existing.setInfo(updatedCanteen.getInfo());
        existing.setFssaiCertificateUrl(updatedCanteen.getFssaiCertificateUrl());
        existing.setImageUrl(updatedCanteen.getImageUrl());

        canteenRepository.save(existing);
    }

    @Override
    public void deleteCanteen(Long id) {
        if (!canteenRepository.existsById(id)) {
            throw new RuntimeException("Canteen not found with id: " + id);
        }
        canteenRepository.deleteById(id);
    }
    @Override
    public void updateCanteenImage(Long id, String imagePath) {
        Canteen canteen = canteenRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Canteen not found"));
        canteen.setImageUrl(imagePath);
        canteenRepository.save(canteen);
    }

    @Override
    public void updateFssaiCertificate(Long id, String certificatePath) {
        Canteen canteen = canteenRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Canteen not found"));
        canteen.setFssaiCertificateUrl(certificatePath);
        canteenRepository.save(canteen);
    }

    @Override
    public void updateMenuFile(Long id, String menuPath) {
        Canteen canteen = canteenRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Canteen not found"));
        canteen.setMenuFilePath(menuPath);
        canteenRepository.save(canteen);
    }
}
