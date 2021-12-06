package com.zonaunica.ZonaUnica.utils;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;

@Component
public class Autorizacion implements Filter{

    public static final String KEY="hssbjbsakdba";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
            
        HttpServletRequest httpServletRequest= (HttpServletRequest) request;
        HttpServletResponse httpServletResponse=(HttpServletResponse) response;
        httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type, Access-Control-Allow-Headers, X-Requested-With, X-Auth-Token, Origin");
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "DELETE, POST, GET, OPTIONS, PUT, PATCH");

        String url= httpServletRequest.getRequestURI();
            
        if(url.contains("/api/usuarios/login") || url.contains("/api/usuarios") || url.contains("index") || url.contains(".js") || url.contains("ico") || url.contains("css") || url.contains("assets") || url.contains("#")){
            chain.doFilter(request, response);
        }else{
            String hash = httpServletRequest.getHeader("Authorization");
            if(hash==null || hash.trim().equals("")){
                response.setContentType("application/json");
                String body= "{\"autorizacion \": \"NO\"}";
                response.getWriter().write(body);
            }
            try {
                Jws<Claims> claims= Jwts.parser().setSigningKey(KEY).parseClaimsJws(hash);
                if((url.contains("/api/municipios") || url.contains("/api/sitios") || url.contains("/api/platos") || url.contains("/api/verificar")) && (!claims.getBody().get("username").equals(""))){// && (!claims.getBody().get("username").equals(""))
                    chain.doFilter(request, response);
                }
            } catch (MalformedJwtException e) {
                response.setContentType("application/json");
                String body = "{\"autorizacion\":\"TOKEN NO VALIDO\"}";
                response.getWriter().write(body);
            } catch(ExpiredJwtException e){
                System.out.println(" Token expired ");
            }catch (Exception e){
                System.out.println(e);
            }
        }
        
    }
    
}
