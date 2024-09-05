package authproject.security;

import authproject.exceptions.InvalidJwtAuthenticationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@Service
public class JwtTokenFilter extends GenericFilterBean {
  @Autowired
  private JwtTokenProvider tokenProvider;

  public JwtTokenFilter(JwtTokenProvider tokenProvider) {
    this.tokenProvider = tokenProvider;
  }

  @Override
  public void doFilter(
      ServletRequest servletRequest,
      ServletResponse servletResponse,
      FilterChain filterChain
  )
      throws IOException, ServletException {
    String token = tokenProvider.resolveToken((HttpServletRequest) servletRequest);

    try {
      if (token != null && tokenProvider.validateToken(token)) {
        Authentication auth = tokenProvider.getAuthentication(token);

        if (auth != null)
          SecurityContextHolder.getContext().setAuthentication(auth);
      }
    } catch (InvalidJwtAuthenticationException e) {
      throw new RuntimeException(e);
    }

    filterChain.doFilter(servletRequest, servletResponse);
  }
}