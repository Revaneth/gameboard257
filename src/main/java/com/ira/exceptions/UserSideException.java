package com.ira.exceptions;

import org.omg.CORBA.UserException;

public class UserSideException extends UserException {
    public UserSideException(String message) {
      super(message);
    }

}
