package io.helidon.vueserver;

import java.io.IOException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

@Provider
public class CORSFilter implements ContainerResponseFilter {

        @Override
        public void filter(ContainerRequestContext request, ContainerResponseContext response) throws IOException {
                // The value of the 'Access-Control-Allow-Origin' header in the response must
                // not be the wildcard '*' when the request's credentials mode is 'include'.
                response.getHeaders().add("Access-Control-Allow-Origin", "http://localhost:8080");
                response.getHeaders().add("Access-Control-Allow-Headers",
                                "Origin, Content-Type, Accept, Authorization");
                response.getHeaders().add("Access-Control-Allow-Credentials", "true");
                response.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
        }
}