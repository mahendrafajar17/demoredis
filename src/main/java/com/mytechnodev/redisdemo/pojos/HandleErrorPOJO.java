package com.mytechnodev.redisdemo.pojos;
import lombok.Data;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.io.InputStream;

@Data
public class HandleErrorPOJO {
    private InputStream responseBody;
    private HttpHeaders responseHeader;
    private HttpStatus httpResponseCode;
}
