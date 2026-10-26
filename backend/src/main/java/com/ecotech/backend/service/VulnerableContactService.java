package com.ecotech.backend.service;

import com.ecotech.backend.domain.User;
import com.ecotech.backend.domain.VulnerableContact;
import com.ecotech.backend.dto.VulnerableContactDto;
import com.ecotech.backend.dto.VulnerableContactRequest;
import com.ecotech.backend.repository.UserRepository;
import com.ecotech.backend.repository.VulnerableContactRepository;
import com.ecotech.backend.repository.WardRepository;
import com.ecotech.backend.notification.NotificationService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class VulnerableContactService {

    private final VulnerableContactRepository repository;
    private final UserRepository userRepository;
    private final WardRepository wardRepository;
    private final NotificationService notificationService;

    public VulnerableContactService(VulnerableContactRepository repository,
                                    UserRepository userRepository,
                                    WardRepository wardRepository,
                                    NotificationService notificationService) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.wardRepository = wardRepository;
        this.notificationService = notificationService;
    }

    public VulnerableContactDto create(UUID userId, VulnerableContactRequest req) {
        userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));
        if (!req.isConsent()) {
            throw new IllegalStateException("Consent is required");
        }
        VulnerableContact c = new VulnerableContact();
        c.setUserId(userId);
        c.setName(req.getName());
        c.setPhone(req.getPhone());
        c.setWardId(req.getWardId());
        c.setConsent(true);
        c = repository.save(c);

        notifyCouncillorsInWard(req.getWardId(), c.getName());
        return toDto(c);
    }

    private void notifyCouncillorsInWard(UUID wardId, String contactName) {
        List<User> councillors = userRepository.findByWardId(wardId).stream()
                .filter(u -> u.getRole() == User.Role.COUNCILLOR)
                .toList();
        if (councillors.isEmpty()) {
            return;
        }
        String title = "Vulnerable contact registered";
        String body = "A vulnerable contact \"" + contactName + "\" has been registered with consent in your ward.";
        for (User councillor : councillors) {
            List<String> channels = List.of("console");
            notificationService.notify(councillor, channels, title, body);
        }
    }

    public List<VulnerableContactDto> list(UUID userId) {
        return repository.findByUserId(userId).stream().map(this::toDto).toList();
    }

    public void delete(UUID id, UUID userId) {
        VulnerableContact c = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Contact not found"));
        if (!c.getUserId().equals(userId)) {
            throw new SecurityException("Not your contact");
        }
        repository.delete(c);
    }

    public List<VulnerableContactDto> listByWard(UUID wardId) {
        return repository.findByWardId(wardId).stream().map(this::toDto).toList();
    }

    private VulnerableContactDto toDto(VulnerableContact c) {
        VulnerableContactDto d = new VulnerableContactDto();
        d.setId(c.getId());
        d.setUserId(c.getUserId());
        d.setName(c.getName());
        d.setPhone(c.getPhone());
        d.setWardId(c.getWardId());
        d.setConsent(c.isConsent());
        d.setCreatedAt(c.getCreatedAt());
        return d;
    }
}
