package com.tfg_rm.backend_restaurantmanager.util;

import com.tfg_rm.backend_restaurantmanager.dto.Role;
import com.tfg_rm.backend_restaurantmanager.security.JwtService;

/**
 * Generates real JWT tokens for use in controller tests.
 * Uses the same JwtService as production with the test secret.
 */
public class JwtTestHelper {

    public static final String TEST_SECRET = "test-secret-key-must-be-at-least-32-chars!!";
    public static final Long EMPLOYEE_ID = 1L;
    public static final Long RESTAURANT_ID = 10L;

    private static final JwtService JWT_SERVICE = new JwtService(TEST_SECRET);

    public static String generateToken(Role role) {
        return JWT_SERVICE.generateToken(EMPLOYEE_ID, RESTAURANT_ID, role);
    }

    public static String waiterToken() {
        return generateToken(Role.WAITER);
    }

    public static String managerToken() {
        return generateToken(Role.MANAGER);
    }

    public static String cookerToken() {
        return generateToken(Role.COOKER);
    }

    public static JwtService jwtService() {
        return JWT_SERVICE;
    }
}
