package com.dau.cafeteria_portal.service;

import com.dau.cafeteria_portal.dto.CanteenDTO;
import com.dau.cafeteria_portal.entity.Canteen;
import com.dau.cafeteria_portal.mapper.CanteenMapper;
import com.dau.cafeteria_portal.repository.CanteenRepository;
import com.dau.cafeteria_portal.service.impl.CanteenServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CanteenServiceImplTest {

    @Mock
    private CanteenRepository canteenRepository;

    @Mock
    private CanteenMapper canteenMapper;

    @InjectMocks
    private CanteenServiceImpl canteenService;

    // ---------------------------------------------------------
    // TEST: Get All Canteens
    // ---------------------------------------------------------
    @Test
    void getAllCanteens_success() {
        Canteen c1 = new Canteen();
        Canteen c2 = new Canteen();

        CanteenDTO dto1 = new CanteenDTO();
        CanteenDTO dto2 = new CanteenDTO();

        when(canteenRepository.findAll()).thenReturn(Arrays.asList(c1, c2));
        when(canteenMapper.toDTO(c1)).thenReturn(dto1);
        when(canteenMapper.toDTO(c2)).thenReturn(dto2);

        List<CanteenDTO> result = canteenService.getAllCanteens();

        assertEquals(2, result.size());
        verify(canteenRepository, times(1)).findAll();
    }

    // ---------------------------------------------------------
    // TEST: Get Canteen By ID (success)
    // ---------------------------------------------------------
    @Test
    void getCanteenById_success() {
        Canteen entity = new Canteen();
        CanteenDTO dto = new CanteenDTO();

        when(canteenRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(canteenMapper.toDTO(entity)).thenReturn(dto);

        CanteenDTO result = canteenService.getCanteenById(1L);

        assertNotNull(result);
        verify(canteenRepository).findById(1L);
    }

    // ---------------------------------------------------------
    // TEST: Get Canteen By ID (not found)
    // ---------------------------------------------------------
    @Test
    void getCanteenById_notFound() {
        when(canteenRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> canteenService.getCanteenById(1L));
    }

    // ---------------------------------------------------------
    // TEST: Add Canteen
    // ---------------------------------------------------------
    @Test
    void addCanteen_success() {
        CanteenDTO dto = new CanteenDTO();
        Canteen entity = new Canteen();

        when(canteenMapper.toEntity(dto)).thenReturn(entity);

        canteenService.addCanteen(dto);

        verify(canteenRepository).save(entity);
    }

    // ---------------------------------------------------------
    // TEST: Update Canteen (success)
    // ---------------------------------------------------------
    @Test
    void updateCanteen_success() {
        Canteen existing = new Canteen();
        CanteenDTO updated = new CanteenDTO();

        updated.setCanteenName("New");
        updated.setInfo("New Info");

        when(canteenRepository.findById(1L)).thenReturn(Optional.of(existing));

        canteenService.updateCanteen(1L, updated);

        verify(canteenRepository).save(existing);
        assertEquals("New", existing.getCanteenName());
        assertEquals("New Info", existing.getInfo());
    }

    // ---------------------------------------------------------
    // TEST: Update Canteen (not found)
    // ---------------------------------------------------------
    @Test
    void updateCanteen_notFound() {
        when(canteenRepository.findById(1L)).thenReturn(Optional.empty());

        CanteenDTO updated = new CanteenDTO();
        assertThrows(RuntimeException.class,
                () -> canteenService.updateCanteen(1L, updated));
    }

    // ---------------------------------------------------------
    // TEST: Delete Canteen (success)
    // ---------------------------------------------------------
    @Test
    void deleteCanteen_success() {
        when(canteenRepository.existsById(1L)).thenReturn(true);

        canteenService.deleteCanteen(1L);

        verify(canteenRepository).deleteById(1L);
    }

    // ---------------------------------------------------------
    // TEST: Delete Canteen (not found)
    // ---------------------------------------------------------
    @Test
    void deleteCanteen_notFound() {
        when(canteenRepository.existsById(1L)).thenReturn(false);

        assertThrows(RuntimeException.class,
                () -> canteenService.deleteCanteen(1L));
    }
}
