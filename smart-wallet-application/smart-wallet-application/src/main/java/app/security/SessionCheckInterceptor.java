package app.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Set;

@Component
public class SessionCheckInterceptor implements HandlerInterceptor {

    private final Set<String> UNAUTHENTICATED_ENDPOINT = Set.of ("/", "/login", "/register");

    // този метод ще се изпълни преди всяка заявка
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String endpoint = request.getServletPath ();

        if (UNAUTHENTICATED_ENDPOINT.contains (endpoint)){
            // Ако иска да достъпи ендпойнт, за който не ни трябва сесия, пускаме завката напред да се обработва
            return true;
        }

        // request.getSession () -> вземам сесията, ако няма  се създава нова !!!!!!
        //  request.getSession (false) -> вземам сесията, ако има, ако пък няма се връща null.
        HttpSession currentUserSession = request.getSession (false);

        if (currentUserSession == null){
            response.sendRedirect ("/login");
            return false;
        }

        return true;
    }
}
