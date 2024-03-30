package net.elghz.siteservice.exception;

import java.io.IOException;

public class ActiviteNotFoundException extends IOException {
    public ActiviteNotFoundException(String message){
        super(message);
    }
}
