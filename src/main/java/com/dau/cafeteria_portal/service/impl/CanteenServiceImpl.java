package com.dau.cafeteria_portal.service.impl;

import com.dau.cafeteria_portal.dto.CanteenDTO;
import com.dau.cafeteria_portal.entity.Canteen;
import com.dau.cafeteria_portal.mapper.CanteenMapper;
import com.dau.cafeteria_portal.repository.CanteenRepository;
import com.dau.cafeteria_portal.repository.QuickFeedbackRepository;
import com.dau.cafeteria_portal.service.CanteenService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CanteenServiceImpl implements CanteenService {

    private final CanteenRepository canteenRepository;
    private final CanteenMapper canteenMapper;
    private final QuickFeedbackRepository quickFeedbackRepository;

    public CanteenServiceImpl(CanteenRepository canteenRepository, CanteenMapper canteenMapper,
                               QuickFeedbackRepository quickFeedbackRepository) {
        this.canteenRepository = canteenRepository;
        this.canteenMapper = canteenMapper;
        this.quickFeedbackRepository = quickFeedbackRepository;
    }

    @Override
    public List<CanteenDTO> getAllCanteens() {
        List<Canteen> canteens = canteenRepository.findAll();

        // Was previously 1 + 2N queries (findAll, then an avg() + count()
        // per canteen inside the mapper). Now it's 2 total: this one bulk
        // GROUP BY plus the findAll above. This was the main cause of the
        // canteens list/page taking several seconds to load.
        Map<Long, QuickFeedbackRepository.CanteenRatingAgg> ratingsByCanteen =
                quickFeedbackRepository.findRatingAggregates().stream()
                        .collect(Collectors.toMap(
                                QuickFeedbackRepository.CanteenRatingAgg::getCanteenId,
                                agg -> agg));

        return canteens.stream()
                .map(c -> {
                    QuickFeedbackRepository.CanteenRatingAgg agg = ratingsByCanteen.get(c.getCanteenId());
                    Double avg = agg != null ? agg.getAvgRating() : null;
                    Long count = agg != null ? agg.getRatingCount() : 0L;
                    return canteenMapper.toDTO(c, avg, count);
                })
                .collect(Collectors.toList());
    }

    @Override
    public CanteenDTO getCanteenById(Long id) {
        Canteen canteen = canteenRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Canteen not found with id: " + id));
        return canteenMapper.toDTO(canteen);
    }

    @Override
    public CanteenDTO addCanteen(CanteenDTO canteenDTO) {
        Canteen canteen = canteenMapper.toEntity(canteenDTO);
        Canteen saved = canteenRepository.save(canteen);
        return canteenMapper.toDTO(saved);
    }

    @Override
    public void updateCanteen(Long id, CanteenDTO updatedCanteen) {
        Canteen existing = canteenRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Canteen not found with id: " + id));

        existing.setCanteenName(updatedCanteen.getCanteenName());
        existing.setInfo(updatedCanteen.getInfo());
        if (updatedCanteen.getTagline() != null) existing.setTagline(updatedCanteen.getTagline());
        if (updatedCanteen.getCuisine() != null) existing.setCuisine(updatedCanteen.getCuisine());
        if (updatedCanteen.getLocation() != null) existing.setLocation(updatedCanteen.getLocation());
        if (updatedCanteen.getHours() != null) existing.setHours(updatedCanteen.getHours());
        if (updatedCanteen.getPriceLevel() != null) existing.setPriceLevel(updatedCanteen.getPriceLevel());
        // Only overwrite these when the request actually carries a value.
        // Photos/certificates are uploaded through separate endpoints, so a
        // plain "save details" edit sends these as null — blindly setting
        // them here was silently wiping out an already-uploaded canteen
        // photo (or FSSAI certificate) every time an admin edited the
        // canteen's name, hours, etc.
        if (updatedCanteen.getFssaiCertificateUrl() != null) existing.setFssaiCertificateUrl(updatedCanteen.getFssaiCertificateUrl());
        if (updatedCanteen.getImageUrl() != null) existing.setImageUrl(updatedCanteen.getImageUrl());
        if (updatedCanteen.getAccent() != null && !updatedCanteen.getAccent().isBlank()) {
            existing.setAccent(updatedCanteen.getAccent());
        }

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
