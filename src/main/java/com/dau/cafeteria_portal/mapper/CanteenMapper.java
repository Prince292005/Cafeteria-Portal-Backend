package com.dau.cafeteria_portal.mapper;

import com.dau.cafeteria_portal.dto.CanteenDTO;
import com.dau.cafeteria_portal.entity.Canteen;
import com.dau.cafeteria_portal.repository.QuickFeedbackRepository;
import org.springframework.stereotype.Component;

@Component
public class CanteenMapper {

    private final QuickFeedbackRepository quickFeedbackRepository;

    public CanteenMapper(QuickFeedbackRepository quickFeedbackRepository) {
        this.quickFeedbackRepository = quickFeedbackRepository;
    }

    public CanteenDTO toDTO(Canteen canteen) {
        if (canteen == null) return null;

        Double avg = quickFeedbackRepository.findAverageRatingByCanteenId(canteen.getCanteenId());
        long count = quickFeedbackRepository.countByCanteen_CanteenId(canteen.getCanteenId());
        return toDTO(canteen, avg, count);
    }

    /**
     * Used when the caller already has the rating aggregate on hand (e.g.
     * from a single bulk GROUP BY query for a whole list of canteens), so
     * this doesn't hit the DB again per canteen. See toDTO(Canteen) for the
     * single-canteen convenience path that still queries per call.
     */
    public CanteenDTO toDTO(Canteen canteen, Double avgRating, Long ratingCount) {
        if (canteen == null) return null;

        CanteenDTO dto = new CanteenDTO();
        dto.setId(canteen.getCanteenId());
        dto.setCanteenName(canteen.getCanteenName());
        dto.setInfo(canteen.getInfo());
        dto.setTagline(canteen.getTagline());
        dto.setCuisine(canteen.getCuisine());
        dto.setLocation(canteen.getLocation());
        dto.setHours(canteen.getHours());
        dto.setPriceLevel(canteen.getPriceLevel() != null ? canteen.getPriceLevel() : 2);
        dto.setFssaiCertificateUrl(canteen.getFssaiCertificateUrl());
        dto.setImageUrl(canteen.getImageUrl());
        dto.setMenuFilePath(canteen.getMenuFilePath());
        dto.setAccent(canteen.getAccent());

        dto.setAverageRating(avgRating != null ? Math.round(avgRating * 10.0) / 10.0 : 0.0);
        dto.setRatingCount(ratingCount != null ? ratingCount : 0L);
        return dto;
    }

    public Canteen toEntity(CanteenDTO dto) {
        if (dto == null) return null;

        Canteen canteen = new Canteen();
        canteen.setCanteenId(dto.getId());
        canteen.setCanteenName(dto.getCanteenName());
        canteen.setInfo(dto.getInfo());
        canteen.setTagline(dto.getTagline());
        canteen.setCuisine(dto.getCuisine());
        canteen.setLocation(dto.getLocation());
        canteen.setHours(dto.getHours());
        if (dto.getPriceLevel() != null) canteen.setPriceLevel(dto.getPriceLevel());
        canteen.setFssaiCertificateUrl(dto.getFssaiCertificateUrl());
        canteen.setImageUrl(dto.getImageUrl());
        canteen.setMenuFilePath(dto.getMenuFilePath());
        if (dto.getAccent() != null && !dto.getAccent().isBlank()) {
            canteen.setAccent(dto.getAccent());
        }
        return canteen;
    }
}