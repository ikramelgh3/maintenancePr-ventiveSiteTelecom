package net.elghz.siteservice.exception;

import java.io.IOException;

public class NotFoundException extends IOException {
    public NotFoundException(String message){
        super(message);
    }
}
