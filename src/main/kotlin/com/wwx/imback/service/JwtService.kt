package com.wwx.imback.service

import com.wwx.imback.dto.TokenDecodeResultDto
import com.wwx.imback.dto.TokenDto
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.util.*

@Service
class JwtService {
    @Value("\${app.config.tokenPeriod}")
    private var tokenPeriod: Long = 0

    @Value("\${app.config.tokenKey}")
    private var tokenKey: String = ""

    fun encodeToken(serverUserId: Int, payload: Map<String, Any>): TokenDto {
        val expireDate = Date(System.currentTimeMillis() + tokenPeriod * 1000)
        val token: String = Jwts.builder().setSubject(serverUserId.toString()).addClaims(payload)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, this.tokenKey)
                .compact()
        return TokenDto(token, expireDate)
    }

    fun decodeToken(token: String): TokenDecodeResultDto {
        val jws: Jws<Claims> = Jwts.parser().setSigningKey(this.tokenKey).parseClaimsJws(token)
        return TokenDecodeResultDto(jws.body.subject.toInt(), jws.body)
    }
}