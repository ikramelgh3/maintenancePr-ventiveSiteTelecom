package net.elghz.siteservice.exception;

import java.io.IOException;

public class SiteNotFoundException extends IOException {
    public SiteNotFoundException(String message){
        super(message);
    }
}
