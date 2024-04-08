package net.elghz.siteservice.exception;

import java.io.IOException;

public class AttributeNotFoundException extends IOException {
    public AttributeNotFoundException(String message){
        super(message);
    }
}
