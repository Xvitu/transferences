package com.xvitu.transferences.boundary.controller;


import com.xvitu.transferences.application.command.TransferCommand;
import com.xvitu.transferences.application.usecase.CreateTransferenceUseCase;
import com.xvitu.transferences.boundary.controller.requests.TransferRequest;
import com.xvitu.transferences.domain.entity.Transference;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransferController {

    private final CreateTransferenceUseCase createTransferenceUseCase;

    public TransferController(CreateTransferenceUseCase createTransferenceUseCase) {
        this.createTransferenceUseCase = createTransferenceUseCase;
    }

    @PostMapping("/transfer")
    @ResponseStatus(HttpStatus.CREATED)
    public Transference transfer(@RequestBody @Valid TransferRequest request) {
        TransferCommand command = new TransferCommand(request.value(), request.payer(), request.payee());
        return createTransferenceUseCase.execute(command);
    }
}
