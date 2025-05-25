package app.security;

import app.user.model.User;
import app.user.model.UserRole;
import app.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Set;
import java.util.UUID;

@Component
public class SessionCheckInterceptor implements HandlerInterceptor {

    private final Set<String> UNAUTHENTICATED_ENDPOINT = Set.of ("/", "/login", "/register");
    private final Set<String> ADMIN_ENDPOINT =Set.of ("/users", "/reports");

    private final UserService userService;

    @Autowired
    public SessionCheckInterceptor(UserService userService) {
        this.userService = userService;
    }

    // this method will be executed before every request
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String endpoint = request.getServletPath ();

        if (UNAUTHENTICATED_ENDPOINT.contains (endpoint)){
            // If it wants to access an endpoint for which we don't need a session, we pass the request forward to be processed.
            return true;
        }


        // request.getSession () -> I take the session, if not a new one is created!!!!!!
        //  request.getSession (false) -> I get the session if it exists, if it doesn't, null is returned.
        HttpSession currentUserSession = request.getSession (false);

        if (currentUserSession == null){
            response.sendRedirect ("/login");
            return false;
        }

        // have a session and who is it ?
        UUID userId = (UUID) currentUserSession.getAttribute ("user_id");
        User user = userService.getById (userId);

        // are you active or inactive
        if (!user.isActive ()){

            currentUserSession.invalidate ();
            response.sendRedirect ("/");
            return false;
        }

        // Variant 1 !!!!!!!!!!!!!
//        if (ADMIN_ENDPOINT.contains (endpoint) && user.getRole () != UserRole.ADMIN){
////
////            response.setStatus (HttpStatus.FORBIDDEN.value ());
////            response.getWriter ().write ("Access denied, you don't  have a necessary permission!");
////            return false;
////        }

        // Variant 2

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        if (handlerMethod.hasMethodAnnotation (RequireAdminRole.class) && user.getRole () != UserRole.ADMIN){
            response.setStatus (HttpStatus.FORBIDDEN.value ());
            response.getWriter ().write ("Access denied, you don't  have a necessary permission!");
            return false;
        }

        return true;
    }
}
