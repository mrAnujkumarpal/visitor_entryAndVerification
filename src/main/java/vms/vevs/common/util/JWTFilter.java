package vms.vevs.common.util;

import org.springframework.stereotype.Component;

@Component
public class JWTFilter /*extends OncePerRequestFilter*/ {
/*

    @Autowired
    private UserServiceImpl userServiceImpl;

    @Autowired
    private  JWTUtility jwtUtility;



    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

        String authorization= httpServletRequest.getHeader("Authorization");
        String token=null;
        String userName=null;
        if(null != authorization && authorization.startsWith("Bearer")){

            token=authorization.substring(7);
            userName=jwtUtility.extractUsername(token);

        }

        if(null != userName){
            UserDetails userDetails= userServiceImpl.loadUserByUsername(userName);


        if(jwtUtility.validateToken(token,userDetails)){

            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken=
                    new UsernamePasswordAuthenticationToken(userDetails,token, userDetails.getAuthorities());

            usernamePasswordAuthenticationToken.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));

            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        }
        filterChain.doFilter(httpServletRequest,httpServletResponse);
        }
    }
    */
}
