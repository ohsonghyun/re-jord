package com.dev6.rejordbe.presentation.controller

import com.dev6.rejordbe.application.user.signup.SignUpService
import spock.lang.Specification

class UserControllerSpec extends Specification {

    SignUpService signUpService;

    def setup() {
        signUpService = Mock(SignUpService.class)
    }


}
