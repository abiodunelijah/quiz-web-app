package com.coder2client.services;

import com.coder2client.dtos.AttemptDto;
import com.coder2client.dtos.SubmitRequest;

import java.util.List;

public interface AttemptService {
    AttemptDto startAttempt(Long quizId, Long userId);
    AttemptDto submitAttempt(Long attemptId, SubmitRequest request);
    AttemptDto getAttemptResult(Long attemptId);
    List<AttemptDto> getUserAttempts(Long userId);
}
