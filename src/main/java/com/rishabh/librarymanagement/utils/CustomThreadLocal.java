package com.rishabh.librarymanagement.utils;

import io.jsonwebtoken.Claims;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class CustomThreadLocal {

    private InheritableThreadLocal<Claims> customThreadLocal = new InheritableThreadLocal<>();
}
