package com.xvitu.transferences.domain.vo;

import com.xvitu.transferences.domain.entity.User;
import com.xvitu.transferences.domain.entity.Wallet;

public record ValidatedTransference(Wallet payeerWallet, Wallet payeeWallet, User payer, User payee) {}
