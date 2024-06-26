package br.com.banco.application.usecaseImpl;

import br.com.banco.application.gateway.CreateUserGateway;
import br.com.banco.core.domain.TransactionPin;
import br.com.banco.core.domain.User;
import br.com.banco.core.domain.Wallet;
import br.com.banco.core.exception.EmailException;
import br.com.banco.core.exception.InternalServerErrorException;
import br.com.banco.core.exception.TaxNumberException;
import br.com.banco.core.exception.TrasactionPinException;
import br.com.banco.core.exception.enums.ErrorCodeEnum;
import br.com.banco.usecase.CreateUserUseCase;
import br.com.banco.usecase.EmailAvailableUseCase;
import br.com.banco.usecase.TaxNumberAvailableUseCase;

import java.math.BigDecimal;

public class CreateUserUseCaseImpl implements CreateUserUseCase {

    private TaxNumberAvailableUseCase taxNumberAvailableUseCase;
    private EmailAvailableUseCase emailAvailableUseCase;
    private CreateUserGateway createUserGateway;


    public CreateUserUseCaseImpl(TaxNumberAvailableUseCase taxNumberAvailableUseCase, EmailAvailableUseCase emailAvailableUseCase, CreateUserGateway createUserGateway) {
        this.taxNumberAvailableUseCase = taxNumberAvailableUseCase;
        this.emailAvailableUseCase = emailAvailableUseCase;
        this.createUserGateway = createUserGateway;
    }

    @Override
    public void create(User user, String pin) throws TaxNumberException, EmailException, TrasactionPinException, InternalServerErrorException {

        if (!taxNumberAvailableUseCase.available(user.getTaxNumber().getValue())) {
            throw new TaxNumberException(ErrorCodeEnum.ON0002.getCode(), ErrorCodeEnum.ON0002.getMessage());
        }

        if (!emailAvailableUseCase.available(user.getEmail())) {
            throw new EmailException(ErrorCodeEnum.ON0003.getCode(), ErrorCodeEnum.ON0003.getMessage());
        }

        if (!createUserGateway.create(user, new Wallet(new TransactionPin(pin), BigDecimal.ZERO, user))) {
            throw new InternalServerErrorException(ErrorCodeEnum.ON0004.getMessage(), ErrorCodeEnum.ON0004.getCode());
        }


    }


}
