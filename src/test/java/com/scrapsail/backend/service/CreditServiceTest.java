package com.scrapsail.backend.service;

import com.scrapsail.backend.model.User;
import com.scrapsail.backend.model.Role;
import com.scrapsail.backend.model.CarbonWallet;
import com.scrapsail.backend.model.CarbonCredit;
import com.scrapsail.backend.repository.CarbonCreditRepository;
import com.scrapsail.backend.repository.WalletRepository;
import com.scrapsail.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SuppressWarnings("null")
class CreditServiceTest {

    @InjectMocks
    private CreditService creditService;

    @Mock
    private CarbonCreditRepository carbonCreditRepository;

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private UserRepository userRepository;

    private User testUser;
    private CarbonCredit testCredit;
    private CarbonWallet testWallet;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Setup test user
        testUser = new User();
        testUser.setId(1L);
        testUser.setName("Test User");
        testUser.setEmail("test@example.com");
        testUser.setRole(Role.user);

        // Setup test credit
        testCredit = new CarbonCredit();
        testCredit.setUser(testUser);
        testCredit.setEarned(100.0);
        testCredit.setRedeemed(0.0);
        testCredit.setBalance(100.0);

        // Setup test wallet
        testWallet = new CarbonWallet();
        testWallet.setUser(testUser);
        testWallet.setPoints(100.0);
        testWallet.setBalance(100.0);
    }

    @Test
    void earnCredits_ShouldUpdateBothCreditAndWallet() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(carbonCreditRepository.findByUserId(1L)).thenReturn(List.of(testCredit));
        when(walletRepository.findByUserId(1L)).thenReturn(Optional.of(testWallet));
        when(carbonCreditRepository.save(any(CarbonCredit.class))).thenReturn(testCredit);
        when(walletRepository.save(any(CarbonWallet.class))).thenReturn(testWallet);

        // Act
        CarbonCredit result = creditService.earnCredits(1L, 50.0);

        // Assert
        assertNotNull(result);
        verify(carbonCreditRepository).save(any(CarbonCredit.class));
        verify(walletRepository).save(any(CarbonWallet.class));
        
        // Verify credit updates
        assertEquals(150.0, testCredit.getEarned()); // 100 + 50
        assertEquals(150.0, testCredit.getBalance());
        
        // Verify wallet updates
        assertEquals(150.0, testWallet.getPoints()); // 100 + 50
        assertEquals(150.0, testWallet.getBalance());
    }

    @Test
    void earnCredits_WithNewUser_ShouldCreateNewRecords() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(carbonCreditRepository.findByUserId(1L)).thenReturn(new ArrayList<>());
        when(walletRepository.findByUserId(1L)).thenReturn(Optional.empty());
        when(carbonCreditRepository.save(any(CarbonCredit.class))).thenAnswer(i -> i.getArgument(0));
        when(walletRepository.save(any(CarbonWallet.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        CarbonCredit result = creditService.earnCredits(1L, 50.0);

        // Assert
        assertNotNull(result);
        assertEquals(50.0, result.getEarned());
        assertEquals(50.0, result.getBalance());
        assertEquals(0.0, result.getRedeemed());
        
        verify(carbonCreditRepository).save(any(CarbonCredit.class));
        verify(walletRepository).save(any(CarbonWallet.class));
    }

    @Test
    void redeemCredits_ShouldUpdateBothCreditAndWallet() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(carbonCreditRepository.findByUserId(1L)).thenReturn(List.of(testCredit));
        when(walletRepository.findByUserId(1L)).thenReturn(Optional.of(testWallet));
        when(carbonCreditRepository.save(any(CarbonCredit.class))).thenReturn(testCredit);
        when(walletRepository.save(any(CarbonWallet.class))).thenReturn(testWallet);

        // Act
        CarbonCredit result = creditService.redeemCredits(1L, 50.0);

        // Assert
        assertNotNull(result);
        assertEquals(50.0, testCredit.getRedeemed());
        assertEquals(50.0, testCredit.getBalance());
        assertEquals(50.0, testWallet.getBalance());
        
        verify(carbonCreditRepository).save(any(CarbonCredit.class));
        verify(walletRepository).save(any(CarbonWallet.class));
    }

    @Test
    void redeemCredits_WithInsufficientBalance_ShouldThrowException() {
        // Arrange
        testCredit.setBalance(30.0);
        testWallet.setBalance(30.0);
        
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(carbonCreditRepository.findByUserId(1L)).thenReturn(List.of(testCredit));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            creditService.redeemCredits(1L, 50.0);
        });
        
        // Verify no saves occurred
        verify(carbonCreditRepository, never()).save(any());
        verify(walletRepository, never()).save(any());
    }

    @Test
    void getUserBalance_ShouldReturnCorrectBalance() {
        // Arrange
        when(carbonCreditRepository.findByUserId(1L)).thenReturn(List.of(testCredit));

        // Act
        Double balance = creditService.getUserBalance(1L);

        // Assert
        assertEquals(100.0, balance);
    }

    @Test
    void getUserBalance_WithNoCredits_ShouldReturnZero() {
        // Arrange
        when(carbonCreditRepository.findByUserId(1L)).thenReturn(new ArrayList<>());

        // Act
        Double balance = creditService.getUserBalance(1L);

        // Assert
        assertEquals(0.0, balance);
    }
}