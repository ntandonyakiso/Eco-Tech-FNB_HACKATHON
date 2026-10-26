package com.ecotech.backend.service;

import com.ecotech.backend.dto.AlertDto;

import java.util.List;

public interface AlertSource {

    List<AlertDto> fetch();
}
