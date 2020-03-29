package Jwt;

public final class SecurityConstants {

    public static final String AUTH_LOGIN_URL = "/login";
    // Signing key for HS512 algorithm
    public static final String JWT_SECRET = "SalmonSkinRoll";
    // JWT token defaults
    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String TOKEN_TYPE = "JWT";
    public static final String TOKEN_ISSUER = "secure-api";
    public static final String TOKEN_AUDIENCE = "secure-app";

    private SecurityConstants() {
        throw new IllegalStateException("Cannot create instance of static util class");
    }
}
