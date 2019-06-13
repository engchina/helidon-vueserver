/*
 * Copyright (c) 2018, 2019 Oracle and/or its affiliates. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.helidon.vueserver;

import java.util.Collections;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * A simple JAX-RS resource to greet you. Examples:
 *
 * Get default greeting message: curl -X GET http://localhost:8080/greet
 *
 * Get greeting message for Joe: curl -X GET http://localhost:8080/greet/Joe
 *
 * Change greeting curl -X PUT -H "Content-Type: application/json" -d
 * '{"greeting" : "Howdy"}' http://localhost:8080/greet/greeting
 *
 * The message is returned as a JSON object.
 */
@Path("/greet")
@RequestScoped
public class GreetResource {

  private static final JsonBuilderFactory JSON = Json.createBuilderFactory(Collections.emptyMap());

  /**
   * The greeting message provider.
   */
  private final GreetingProvider greetingProvider;

  /**
   * Using constructor injection to get a configuration property. By default this
   * gets the value from META-INF/microprofile-config
   *
   * @param greetingConfig the configured greeting message
   */
  @Inject
  public GreetResource(GreetingProvider greetingConfig) {
    this.greetingProvider = greetingConfig;
  }

  @Inject
  SessionManager sessionManager;

  /**
   * Return a wordly greeting message.
   *
   * @return {@link JsonObject}
   */
  @SuppressWarnings("checkstyle:designforextension")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public JsonObject getDefaultMessage() {
    return createResponse("World");
  }

  @SuppressWarnings("checkstyle:designforextension")
  @Path("/questions")
  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public String getQuestions() {
    String result = "[" + "{" + "\"title\": \"Where is my order?\","
        + "\"content\": \"Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat. Ut wisi enim ad minim veniam, quis nostrud exerci tation ullamcorper suscipit lobortis nisl ut aliquip ex ea commodo consequat. Duis autem vel eum iriure dolor in hendrerit in vulputate velit esse molestie consequat, vel illum dolore eu feugiat nulla facilisis at vero eros.\""
        + "}," + "{" + "\"title\": \"Where do you ship?\","
        + "\"content\": \"Qui et quod dolorem eaque. Soluta ut dolor dolor debitis. Molestias sunt in necessitatibus odit quo odio omnis odit. Atque deleniti reprehenderit sapiente consectetur consectetur quia autem repudiandae.\""
        + "}," + "{" + "\"title\": \"How do i return an item?\","
        + "\"content\": \"Voluptate cupiditate officia quia accusantium. Fugiat ut praesentium quia ut et labore reiciendis fugit. Voluptas eos maiores itaque aut. Sequi harum dolor neque sunt rerum iste ducimus. Quas sapiente cumque voluptatem repudiandae ipsum. Natus quis aut aut fugiat. Nisi non sed reprehenderit mollitia commodi et qui error. Velit autem omnis et repellendus facere libero praesentium. Sit aut possimus eligendi consectetur beatae. Iste et officia delectus modi ratione inventore enim voluptatem.\""
        + "}," + "{" + "\"title\": \"Why has my order been cancelled?\","
        + "\"content\": \"Consequatur labore repellat quo eaque provident natus et. Fuga molestias quibusdam quam maiores at debitis. Molestias occaecati iste dignissimos voluptatem quis est quidem. Expedita natus porro id ut nesciunt cupiditate quis. Doloribus suscipit ipsa ipsam qui. Voluptatem voluptatem ut numquam ex natus iste.\""
        + "}," + "{" + "\"title\": \"Why won’t my discount code work?\","
        + "\"content\": \"Inventore iste reprehenderit aut reiciendis repellendus. Quas cumque aliquam accusantium et itaque quisquam voluptatem. Commodi quo quia occaecati dicta ratione qui at tempore. At saepe est et saepe accusamus voluptates.\""
        + "}" + "]";
    return result;
  }

  /**
   * Return a greeting message using the name that was provided.
   *
   * @param name the name to greet
   * @return {@link JsonObject}
   */
  @SuppressWarnings("checkstyle:designforextension")
  @Path("/{name}")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public JsonObject getMessage(@PathParam("name") String name) {
    return createResponse(name);
  }

  /**
   * Set the greeting to use in future messages.
   *
   * @param jsonObject JSON containing the new greeting
   * @return {@link Response}
   */
  @SuppressWarnings("checkstyle:designforextension")
  @Path("/greeting")
  @PUT
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response updateGreeting(JsonObject jsonObject) {

    if (!jsonObject.containsKey("greeting")) {
      JsonObject entity = JSON.createObjectBuilder().add("error", "No greeting provided").build();
      return Response.status(Response.Status.BAD_REQUEST).entity(entity).build();
    }

    String newGreeting = jsonObject.getString("greeting");

    greetingProvider.setMessage(newGreeting);
    return Response.status(Response.Status.NO_CONTENT).build();
  }

  private JsonObject createResponse(String who) {
    String msg = String.format("%s %s!", greetingProvider.getMessage(), who);

    return JSON.createObjectBuilder().add("message", msg).build();
  }

  @SuppressWarnings("checkstyle:designforextension")
  @Path("/login")
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public GreetUser getGreetUser(GreetUser user) throws Exception {
    GreetUser findUser = sessionManager.get(user);
    if (findUser == null) {
      throw new Exception("Username or Password is wrong!");
    }
    return findUser;
  }

  @SuppressWarnings("checkstyle:designforextension")
  @Path("/signup")
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public GreetUser addGreetUser(GreetUser user) {
    sessionManager.add(user);
    return user;
  }

  @SuppressWarnings("checkstyle:designforextension")
  @Path("/logout")
  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response logout() {
    return Response.ok(JSON.createObjectBuilder().add("message", "ok").build()).build();
  }
}
