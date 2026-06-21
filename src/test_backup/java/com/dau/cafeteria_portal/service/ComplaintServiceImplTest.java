//package com.dau.cafeteria_portal.service;
//
//import com.dau.cafeteria_portal.dto.ComplaintDTO;
//import com.dau.cafeteria_portal.entity.Canteen;
//import com.dau.cafeteria_portal.entity.Complaint;
//import com.dau.cafeteria_portal.entity.User;
//import com.dau.cafeteria_portal.enums.ComplaintStatus;
//import com.dau.cafeteria_portal.mapper.ComplaintMapper;
//import com.dau.cafeteria_portal.repository.CanteenRepository;
//import com.dau.cafeteria_portal.repository.ComplaintRepository;
//import com.dau.cafeteria_portal.repository.UserRepository;
//import com.dau.cafeteria_portal.service.EscalationMailService;
//import com.dau.cafeteria_portal.service.impl.ComplaintServiceImpl;
//import jakarta.mail.MessagingException;
//import org.junit.jupiter.api.*;
//import org.mockito.*;
//
//import java.util.*;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class ComplaintServiceImplTest {
//
//    @Mock private ComplaintRepository complaintRepository;
//    @Mock private UserRepository userRepository;
//    @Mock private CanteenRepository canteenRepository;
//    @Mock private EscalationMailService escalationMailService;
//
//    @InjectMocks
//    private ComplaintServiceImpl complaintService;
//
//    private MockedStatic<ComplaintMapper> mapperMock;
//
//    @BeforeEach
//    void init() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @AfterEach
//    void release() {
//        if (mapperMock != null) mapperMock.close();
//    }
//
//    // -------------------------------------------------------------
//    // TEST: createComplaint()
//    // -------------------------------------------------------------
//    @Test
//    void testCreateComplaint() {
//
//        String email = "test@dau.ac.in";
//        Long canteenId = 5L;
//
//        User user = new User();
//        user.setEmailId(email);
//
//        Canteen canteen = new Canteen();
//        canteen.setCanteenId(5L);
//
//        ComplaintDTO dto = new ComplaintDTO();
//        dto.setTitle("Food Issue");
//        dto.setDescription("Bad food");
//
//        Complaint complaint = new Complaint();
//        complaint.setTitle("Food Issue");
//
//        Complaint saved = new Complaint();
//        saved.setComplainId(10L);
//        saved.setTitle("Food Issue");
//
//        ComplaintDTO savedDTO = new ComplaintDTO();
//        savedDTO.setComplainId(10L);
//
//        when(userRepository.findByEmailId(email)).thenReturn(Optional.of(user));
//        when(canteenRepository.findById(canteenId)).thenReturn(Optional.of(canteen));
//        when(complaintRepository.save(any())).thenReturn(saved);
//
//        mapperMock = mockStatic(ComplaintMapper.class);
//        mapperMock.when(() -> ComplaintMapper.toEntity(dto, user, canteen)).thenReturn(complaint);
//        mapperMock.when(() -> ComplaintMapper.toDTO(saved)).thenReturn(savedDTO);
//
//        ComplaintDTO result = complaintService.createComplaint(dto, email, canteenId);
//
//        assertEquals(10L, result.getComplainId());
//        verify(complaintRepository, times(1)).save(complaint);
//    }
//
//    // -------------------------------------------------------------
//    // TEST: getMyComplaints()
//    // -------------------------------------------------------------
//    @Test
//    void testGetMyComplaints() {
//
//        String email = "abc@dau.ac.in";
//        User user = new User();
//        user.setEmailId(email);
//
//        Complaint c1 = new Complaint();
//        c1.setComplainId(1L);
//        ComplaintDTO d1 = new ComplaintDTO();
//        d1.setComplainId(1L);
//
//        mapperMock = mockStatic(ComplaintMapper.class);
//        mapperMock.when(() -> ComplaintMapper.toDTO(c1)).thenReturn(d1);
//
//        when(userRepository.findByEmailId(email)).thenReturn(Optional.of(user));
//        when(complaintRepository.findByUser(user)).thenReturn(List.of(c1));
//
//        List<ComplaintDTO> list = complaintService.getMyComplaints(email);
//
//        assertEquals(1, list.size());
//        assertEquals(1L, list.get(0).getComplainId());
//    }
//
//    // -------------------------------------------------------------
//    // TEST: getComplaintDetails()
//    // -------------------------------------------------------------
//    @Test
//    void testGetComplaintDetails() {
//        Complaint c = new Complaint();
//        c.setComplainId(1L);
//
//        ComplaintDTO dto = new ComplaintDTO();
//        dto.setComplainId(1L);
//
//        mapperMock = mockStatic(ComplaintMapper.class);
//        mapperMock.when(() -> ComplaintMapper.toDTO(c)).thenReturn(dto);
//
//        when(complaintRepository.findById(1L)).thenReturn(Optional.of(c));
//
//        Optional<ComplaintDTO> result = complaintService.getComplaintDetails(1L);
//
//        assertTrue(result.isPresent());
//        assertEquals(1L, result.get().getComplainId());
//    }
//
//    // -------------------------------------------------------------
//    // TEST: getAllComplaints()
//    // -------------------------------------------------------------
//    @Test
//    void testGetAllComplaints() {
//
//        Complaint c = new Complaint();
//        c.setComplainId(100L);
//
//        ComplaintDTO dto = new ComplaintDTO();
//        dto.setComplainId(100L);
//
//        when(complaintRepository.findAll()).thenReturn(List.of(c));
//
//        mapperMock = mockStatic(ComplaintMapper.class);
//        mapperMock.when(() -> ComplaintMapper.toDTO(c)).thenReturn(dto);
//
//        List<ComplaintDTO> result = complaintService.getAllComplaints();
//
//        assertEquals(1, result.size());
//        assertEquals(100L, result.get(0).getComplainId());
//    }
//
//    // -------------------------------------------------------------
//    // TEST: updateComplaintStatus()
//    // -------------------------------------------------------------
//    @Test
//    void testUpdateComplaintStatus() {
//
//        Complaint c = new Complaint();
//        c.setComplainId(1L);
//        c.setComplaintStatus(ComplaintStatus.PENDING);
//
//        when(complaintRepository.findById(1L)).thenReturn(Optional.of(c));
//
//        complaintService.updateComplaintStatus(1L, ComplaintStatus.RESOLVED);
//
//        assertEquals(ComplaintStatus.RESOLVED, c.getComplaintStatus());
//        verify(complaintRepository).save(c);
//    }
//
//    // -------------------------------------------------------------
//    // TEST: escalateComplaint() SUCCESS
//    // -------------------------------------------------------------
//    @Test
//    void testEscalateComplaintSuccess() throws MessagingException {
//
//        Complaint c = new Complaint();
//        c.setComplainId(1L);
//        c.setTitle("Slow Service");
//        c.setDescription("Food delivery late");
//
//        when(complaintRepository.findById(1L)).thenReturn(Optional.of(c));
//
//        doNothing().when(escalationMailService).sendEscalationMail(
//                anyString(), anyString(), anyString(), anyString()
//        );
//
//        String response = complaintService.escalateComplaint(1L);
//
//        assertEquals("Complaint escalated and email sent to CMC.", response);
//        verify(escalationMailService, times(1))
//                .sendEscalationMail(anyString(), anyString(), anyString(), anyString());
//    }
//
//    // -------------------------------------------------------------
//    // TEST: escalateComplaint() FAILURE
//    // -------------------------------------------------------------
//    @Test
//    void testEscalateComplaintFailure() throws MessagingException {
//
//        Complaint c = new Complaint();
//        c.setComplainId(1L);
//
//        when(complaintRepository.findById(1L)).thenReturn(Optional.of(c));
//
//        doThrow(new MessagingException("SMTP ERROR"))
//                .when(escalationMailService)
//                .sendEscalationMail(anyString(), anyString(), anyString(), anyString());
//
//        RuntimeException ex = assertThrows(RuntimeException.class,
//                () -> complaintService.escalateComplaint(1L));
//
//        assertTrue(ex.getMessage().contains("Failed to send escalation mail"));
//    }
//}
