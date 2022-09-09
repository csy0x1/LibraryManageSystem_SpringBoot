package com.example.vuetest.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class Jwtutil {

    private static final long TOKEN_EXPIRE_TIME = 30*24*60*60;  //TOKEN过期时间30天
    public static final String JWTID = "tokenid";
    private static final String JWT_SECRET = "1123581321";
    //创建JWT
    public static String createJWT(Map<String,Object> claims, Long time) {
        SecretKey secretKey = generalKey();
        Date now = new Date(System.currentTimeMillis());
        long nowMillis = System.currentTimeMillis();
        JwtBuilder builder = Jwts.builder()
                .setClaims(claims)
                .setId(JWTID)
                .setIssuedAt(now)
                .signWith(SignatureAlgorithm.HS256,secretKey);
        if(time >= 0){
            long expMillis = nowMillis + time;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }
        return "JWT_TOKEN"+builder.compact();
    }
    //验证JWT
    public static Claims verifyJwt(String token){
        SecretKey key = generalKey();
        token = token.replace("JWT_TOKEN","");
        Claims claims;
        try{
            claims = Jwts.parser()
                    .setSigningKey(key)
                    .parseClaimsJws(token).getBody();
        } catch (Exception e){
            claims = null;
        }
        return claims;
    }
    //生成加密key
    public static SecretKey generalKey(){
        byte[] stringkey = JWT_SECRET.getBytes();
        byte[] encodedKey = Base64.getDecoder().decode(stringkey);  //JDK过新会导致javax.xml.bind.DatatypeConverter出现问题，需添加依赖
        return new SecretKeySpec(encodedKey,"AES");
    }

    //根据username生成Token
    public static String generateToken(String username){
        Map<String,Object> map = new HashMap<>();
        map.put("username",username);
        return createJWT(map,TOKEN_EXPIRE_TIME);
    }
}
