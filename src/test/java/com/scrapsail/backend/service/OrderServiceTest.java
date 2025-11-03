package com.scrapsail.backend.service;

import com.scrapsail.backend.model.ScrapOrder;
import com.scrapsail.backend.model.User;
import com.scrapsail.backend.model.Role;
import com.scrapsail.backend.repository.ScrapOrderRepository;
import com.scrapsail.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SuppressWarnings("null")
class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private ScrapOrderRepository scrapOrderRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CreditService creditService;

    private User testUser;
    private ScrapOrder testOrder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Setup test user
        testUser = new User();
        testUser.setId(1L);
        testUser.setName("Test User");
        testUser.setEmail("test@example.com");
        testUser.setRole(Role.user);

        // Setup test order
        testOrder = new ScrapOrder();
        testOrder.setId(1L);
        testOrder.setUser(testUser);
        testOrder.setItemType("Paper");
        testOrder.setWeight(10.0); // 10 kg = 10 points
        testOrder.setStatus("PENDING");
    }

    @Test
    void createOrder_ShouldCreateOrderSuccessfully() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(scrapOrderRepository.save(any(ScrapOrder.class))).thenReturn(testOrder);

        // Act
        ScrapOrder result = orderService.createOrder(1L, "Paper", 10.0);

        // Assert
        assertNotNull(result);
        assertEquals("PENDING", result.getStatus());
        assertEquals(10.0, result.getWeight());
        assertEquals("Paper", result.getItemType());
        verify(scrapOrderRepository).save(any(ScrapOrder.class));
    }

    @Test
    void updateOrderStatus_WhenCompletingOrder_ShouldAwardCredits() {
        // Arrange
        when(scrapOrderRepository.findById(1L)).thenReturn(Optional.of(testOrder));
        when(scrapOrderRepository.save(any(ScrapOrder.class))).thenReturn(testOrder);

        // Act
        orderService.updateOrderStatus(1L, "COMPLETED");

        // Assert
        verify(creditService).earnCredits(eq(1L), eq(10.0)); // Verify credits awarded based on weight
        verify(scrapOrderRepository).save(any(ScrapOrder.class));
    }

    @Test
    void updateOrderStatus_WhenNotCompleting_ShouldNotAwardCredits() {
        // Arrange
        when(scrapOrderRepository.findById(1L)).thenReturn(Optional.of(testOrder));
        when(scrapOrderRepository.save(any(ScrapOrder.class))).thenReturn(testOrder);

        // Act
        orderService.updateOrderStatus(1L, "IN_PROGRESS");

        // Assert
        verify(creditService, never()).earnCredits(any(), any()); // Verify credits were not awarded
        verify(scrapOrderRepository).save(any(ScrapOrder.class));
    }

    @Test
    void updateOrderStatus_WhenOrderNotFound_ShouldThrowException() {
        // Arrange
        when(scrapOrderRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            orderService.updateOrderStatus(99L, "COMPLETED");
        });
    }
}