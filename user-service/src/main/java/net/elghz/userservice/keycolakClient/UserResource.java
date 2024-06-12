package net.elghz.userservice.keycolakClient;

import java.util.*;
import java.util.stream.Collectors;

import jakarta.ws.rs.core.Response;
import net.elghz.userservice.config.KeycloakSecurityUtil;
import net.elghz.userservice.dtos.Role1;
import net.elghz.userservice.dtos.User1;
import net.elghz.userservice.service.EmailService;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.common.util.CollectionUtil;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/keycloak")
public class UserResource {

    @Autowired
    EmailService emailService;
    @Autowired
    private KeycloakSecurityUtil keycloakUtil;

    @Value("${realm}")
    private String realm;

    @GetMapping("/users")
    public List<User1> getUsers() {
        Keycloak keycloak = keycloakUtil.getKeycloakInstance();
        List<UserRepresentation> userRepresentations = keycloak.realm(realm).users().list();
        return mapUsers(userRepresentations);
    }

    @GetMapping("/totalUser")
    public int getTotalUsers() {
        Keycloak keycloak = keycloakUtil.getKeycloakInstance();
        List<UserRepresentation> userRepresentations = keycloak.realm(realm).users().list();
        return userRepresentations.size();
    }
    @GetMapping("/users/with-roles")
    public List<User1> getUsersWithRoles() {
        Keycloak keycloak = keycloakUtil.getKeycloakInstance();

        // Récupérer la liste des utilisateurs ayant le rôle "technicien"
        List<UserRepresentation> technicians = keycloak.realm(realm)
                .roles().get("TECHNICIEN").getRoleUserMembers().stream()
                .map(userId -> keycloak.realm(realm).users().get(userId.getId()).toRepresentation())
                .collect(Collectors.toList());

        // Récupérer la liste des utilisateurs ayant le rôle "injecteur"
        List<UserRepresentation> injectors = keycloak.realm(realm)
                .roles().get("INJECTEUR").getRoleUserMembers().stream()
                .map(userId -> keycloak.realm(realm).users().get(userId.getId()).toRepresentation())
                .collect(Collectors.toList());

        // Récupérer la liste des utilisateurs ayant le rôle "responsable_de_maintenance"
        List<UserRepresentation> maintenanceResponsibles = keycloak.realm(realm)
                .roles().get("RESPONSABLE DE MAINTENANCE").getRoleUserMembers().stream()
                .map(userId -> keycloak.realm(realm).users().get(userId.getId()).toRepresentation())
                .collect(Collectors.toList());

        // Fusionner les listes de techniciens, d'injecteurs et de responsables de maintenance
        List<UserRepresentation> usersWithRoles = new ArrayList<>(technicians);
        usersWithRoles.addAll(injectors);
        usersWithRoles.addAll(maintenanceResponsibles);

        return mapUsers(usersWithRoles);
    }



    @GetMapping("/users/{id}")
    public User1 getUser(@PathVariable("id") String id) {
        Keycloak keycloak = keycloakUtil.getKeycloakInstance();
        UserRepresentation userRep = keycloak.realm(realm).users().get(id).toRepresentation();
        User1 user = mapUser(userRep);


        List<String> roles = keycloak.realm(realm).users().get(id)
                .roles().realmLevel().listAll().stream()
                .filter(role -> !role.getName().equals("default-roles-microservices-realm")) // Filtrer le rôle indésirable
                .map(RoleRepresentation::getName)
                .collect(Collectors.toList());
        user.setRoleName(roles.toString());
        return user;
    }


    @PostMapping("/user")
    public Response createUser(@RequestBody User1 user) {
        UserRepresentation userRep = mapUserRep(user);
        Keycloak keycloak = keycloakUtil.getKeycloakInstance();
        Response res = keycloak.realm(realm).users().create(userRep);
        return Response.ok(user).build();
    }

    @PutMapping("/user")
    public Response updateUser(@RequestBody User1 user) {
        Keycloak keycloak = keycloakUtil.getKeycloakInstance();

        // Obtenir l'utilisateur existant
        UserRepresentation existingUserRep = keycloak.realm(realm).users().get(user.getId()).toRepresentation();

        // Mise à jour des informations de l'utilisateur
        UserRepresentation updatedUserRep = mapUserRep(user);


        keycloak.realm(realm).users().get(user.getId()).update(updatedUserRep);

        // Mise à jour des rôles de l'utilisateur
        if (user.getRoleName() != null && !user.getRoleName().isEmpty()) {
            List<String> roles = Arrays.asList(user.getRoleName().replace("[", "").replace("]", "").split(","));
            List<RoleRepresentation> roleRepresentations = keycloak.realm(realm).roles().list();
            for (RoleRepresentation roleRepresentation : roleRepresentations) {
                if (roles.contains(roleRepresentation.getName())) {
                    keycloak.realm(realm).users().get(user.getId()).roles().realmLevel().add(Arrays.asList(roleRepresentation));
                } else {
                    keycloak.realm(realm).users().get(user.getId()).roles().realmLevel().remove(Arrays.asList(roleRepresentation));
                }
            }
        }

        return Response.ok(user).build();
    }


    @DeleteMapping("/users/{id}")
    public Response deleteUser(@PathVariable("id") String id) {
        Keycloak keycloak = keycloakUtil.getKeycloakInstance();
        keycloak.realm(realm).users().delete(id);
        return Response.ok().build();
    }

    @GetMapping("/users/{id}/roles1")
    public List<Role1> getRoles1(@PathVariable("id") String id) {
        Keycloak keycloak = keycloakUtil.getKeycloakInstance();
        return RoleResource.mapRoles(keycloak.realm(realm).users().get(id).roles().realmLevel().listAll());
    }

    @GetMapping("/users/{id}/roles")
    public List<Role1> getRoles(@PathVariable("id") String id) {
        Keycloak keycloak = keycloakUtil.getKeycloakInstance();
        List<RoleRepresentation> roles = keycloak.realm(realm).users().get(id).roles().realmLevel().listAll();

        List<RoleRepresentation> filteredRoles = roles.stream()
                .filter(role -> !role.getName().equals("default-roles-microservices-realm"))
                .collect(Collectors.toList());

        return RoleResource.mapRoles(filteredRoles);
    }

    @GetMapping("/roles/{roleName}/users")
    public List<User1> getUsersByRole(@PathVariable("roleName") String roleName) {
        Keycloak keycloak = keycloakUtil.getKeycloakInstance();
        List<UserRepresentation> usersWithRole = keycloak.realm(realm).roles().get(roleName).getRoleUserMembers().stream()
                .map(userRepresentation -> keycloak.realm(realm).users().get(userRepresentation.getId()).toRepresentation())
                .collect(Collectors.toList());
        return mapUsers(usersWithRole);
    }

    @PostMapping("/users/{id}/roles/{roleName}")
    public Response createRole(@PathVariable("id") String id, @PathVariable("roleName") String roleName) {
        Keycloak keycloak = keycloakUtil.getKeycloakInstance();
        RoleRepresentation role = keycloak.realm(realm).roles().get(roleName).toRepresentation();
        keycloak.realm(realm).users().get(id).roles().realmLevel().add(Arrays.asList(role));
        return Response.ok().build();
    }

    @GetMapping("/users/filter-by-city/{city}")
    public List<User1> filterUsersByCity(@PathVariable("city") String city) {
        Keycloak keycloak = keycloakUtil.getKeycloakInstance();

        // Récupérer la liste de tous les utilisateurs
        List<UserRepresentation> userRepresentations = keycloak.realm(realm).users().list();

        // Filtrer les utilisateurs par ville
        List<UserRepresentation> filteredUsers = userRepresentations.stream()
                .filter(user -> hasCity(user, city))
                .collect(Collectors.toList());

        return mapUsers(filteredUsers);
    }

    @GetMapping("/users/available-technicians")
    public List<User1> getAvailableTechnicians() {
        Keycloak keycloak = keycloakUtil.getKeycloakInstance();

        // Récupérer la liste des utilisateurs ayant le rôle "technicien"
        List<UserRepresentation> technicians = keycloak.realm(realm)
                .roles().get("technicien").getRoleUserMembers().stream()
                .map(userId -> keycloak.realm(realm).users().get(userId.getId()).toRepresentation())
                .collect(Collectors.toList());

        return mapUsers(technicians);
    }

    @GetMapping("/users/Notavailable-technicians")
    public List<User1> getNotAvailableTechnicians() {
        Keycloak keycloak = keycloakUtil.getKeycloakInstance();

        // Récupérer la liste des utilisateurs ayant le rôle "technicien"
        List<UserRepresentation> technicians = keycloak.realm(realm)
                .roles().get("technicien").getRoleUserMembers().stream()
                .map(userId -> keycloak.realm(realm).users().get(userId.getId()).toRepresentation())
                .collect(Collectors.toList());

        // Filtrer les techniciens disponibles
        List<UserRepresentation> availableTechnicians = technicians.stream()
                .filter(user -> isTechnicianNotAvailable(user))
                .collect(Collectors.toList());

        return mapUsers(availableTechnicians);
    }


    @GetMapping("/users/search-by-fullname/{fullName}")
    public List<User1> searchUsersByFullName(@PathVariable("fullName") String fullName) {
        Keycloak keycloak = keycloakUtil.getKeycloakInstance();

        // Diviser le nom complet en prénom et nom
        String[] names = fullName.split(" ");
        String firstName = names[0];
        String lastName = names[1];

        // Récupérer la liste de tous les utilisateurs
        List<UserRepresentation> userRepresentations = keycloak.realm(realm).users().list();

        // Filtrer les utilisateurs par nom complet
        List<UserRepresentation> filteredUsers = userRepresentations.stream()
                .filter(user -> hasFullName(user, firstName, lastName))
                .collect(Collectors.toList());

        return mapUsers(filteredUsers);
    }

    private boolean hasFullName(UserRepresentation user, String firstName, String lastName) {
        return user.getFirstName().equalsIgnoreCase(firstName) && user.getLastName().equalsIgnoreCase(lastName);
    }
    @GetMapping("/users/exceptAdmin")

    public List<User1> getUsersWithMaintenanceRole() {
        Keycloak keycloak = keycloakUtil.getKeycloakInstance();

        // Récupérer la liste des utilisateurs ayant le rôle "technicien responsable de maintenance et d'injection"
        List<UserRepresentation> maintenanceTechnicians = keycloak.realm(realm)
                .roles().get("technicien-responsable-maintenance-INJECTEUR").getRoleUserMembers().stream()
                .map(userId -> keycloak.realm(realm).users().get(userId.getId()).toRepresentation())
                .collect(Collectors.toList());

        return mapUsers(maintenanceTechnicians);
    }

    @GetMapping("/users/technicians-extern")
    public List<User1> getTechniciansExtern() {
        Keycloak keycloak = keycloakUtil.getKeycloakInstance();

        // Récupérer la liste des utilisateurs ayant le rôle "technicien"
        List<UserRepresentation> technicians = keycloak.realm(realm)
                .roles().get("technicien").getRoleUserMembers().stream()
                .map(userId -> keycloak.realm(realm).users().get(userId.getId()).toRepresentation())
                .collect(Collectors.toList());

        // Filtrer les techniciens internes
        List<UserRepresentation> techniciansInternal = technicians.stream()
                .filter(user -> isTechnicianExtern(user))
                .collect(Collectors.toList());

        return mapUsers(techniciansInternal);
    }

    private boolean isTechnicianExtern(UserRepresentation user) {
        Map<String, List<String>> attributes = user.getAttributes();
        if (attributes != null) {
            List<String> type = attributes.get("type");
            return type != null && type.contains("Externe");
        }
        return false;
    }


    @GetMapping("/toto")
    public int totale(){
        return  getTechnicians().size();
    }

    @GetMapping("/users/technicians")
    public List<User1> getTechnicians() {
        Keycloak keycloak = keycloakUtil.getKeycloakInstance();

        // Récupérer la liste des utilisateurs ayant le rôle "technicien"
        List<UserRepresentation> technicians = keycloak.realm(realm)
                .roles().get("technicien").getRoleUserMembers().stream()
                .map(userId -> keycloak.realm(realm).users().get(userId.getId()).toRepresentation())
                .collect(Collectors.toList());
        return mapUsers(technicians);
    }
    @GetMapping("/users/technicians-internal")
    public List<User1> getTechniciansInternal() {
        Keycloak keycloak = keycloakUtil.getKeycloakInstance();

        // Récupérer la liste des utilisateurs ayant le rôle "technicien"
        List<UserRepresentation> technicians = keycloak.realm(realm)
                .roles().get("technicien").getRoleUserMembers().stream()
                .map(userId -> keycloak.realm(realm).users().get(userId.getId()).toRepresentation())
                .collect(Collectors.toList());

        // Filtrer les techniciens internes
        List<UserRepresentation> techniciansInternal = technicians.stream()
                .filter(user -> isTechnicianInternal(user))
                .collect(Collectors.toList());

        return mapUsers(techniciansInternal);
    }

    private boolean isTechnicianInternal(UserRepresentation user) {
        Map<String, List<String>> attributes = user.getAttributes();
        if (attributes != null) {
            List<String> type = attributes.get("type");
            return type != null && type.contains("Interne");
        }
        return false;
    }


    private boolean isTechnicianNotAvailable(UserRepresentation user) {
        Map<String, List<String>> attributes = user.getAttributes();
        if (attributes != null) {
            List<String> available = attributes.get("available");
            return available != null && available.contains("false");
        }
        return false;
    }

    private boolean isTechnicianAvailable(UserRepresentation user) {
        Map<String, List<String>> attributes = user.getAttributes();
        if (attributes != null) {
            List<String> available = attributes.get("available");
            return available != null && available.contains("true");
        }
        return false;
    }


    private boolean hasCity(UserRepresentation user, String city) {
        Map<String, List<String>> attributes = user.getAttributes();
        if (attributes != null) {
            List<String> userCities = attributes.get("city");
            return userCities != null && userCities.contains(city);
        }
        return false;
    }

    private List<User1> mapUsers(List<UserRepresentation> userRepresentations) {
        List<User1> users = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(userRepresentations)) {
            userRepresentations.forEach(userRep -> users.add(mapUser(userRep)));
        }
        return users;
    }

    private User1 mapUser(UserRepresentation userRep) {
        User1 user = new User1();
        user.setId(userRep.getId());
        user.setFirstName(userRep.getFirstName());
        user.setLastName(userRep.getLastName());
        user.setEmail(userRep.getEmail());
        user.setUserName(userRep.getUsername());

        Map<String, List<String>> attributes = userRep.getAttributes();
        if (attributes != null) {
            user.setCity(attributes.getOrDefault("city", Collections.singletonList("")).get(0));
            user.setType(attributes.getOrDefault("type", Collections.singletonList("")).get(0));
            //user.setType(attributes.getOrDefault("IdCentreTechnique", Collections.singletonList("")).get(0));
           // user.setSkills(attributes.getOrDefault("skills", Collections.singletonList("")).toArray(new String[0]));
            user.setPhone_number(attributes.getOrDefault("phone_number", Collections.singletonList("")).get(0));

            user.setAvailable(attributes.getOrDefault("available", Collections.singletonList("")).get(0));
            user.setJobTitle(attributes.getOrDefault("job_title", Collections.singletonList("")).get(0));
           // user.setAvailable(Boolean.parseBoolean(attributes.getOrDefault("available", Collections.singletonList("false")).get(0)));
        }

        return user;
    }

    @GetMapping("/users/{id}/centre-technique")
    public String getCentreTechnique(@PathVariable("id") String id) {
        Keycloak keycloak = keycloakUtil.getKeycloakInstance();
        UserRepresentation userRep = keycloak.realm(realm).users().get(id).toRepresentation();
        String centreTechnique = userRep.getAttributes() != null ?
                userRep.getAttributes().getOrDefault("available", Collections.singletonList("")).get(0) : null;

        return centreTechnique;
    }


    @PostMapping("/Adduser")
    public Response createUserWithRole(@RequestBody User1 userWithRoleRequest) {
        Keycloak keycloak = keycloakUtil.getKeycloakInstance();
        UserRepresentation userRep = new UserRepresentation();
        userRep.setFirstName(userWithRoleRequest.getFirstName());
        userRep.setLastName(userWithRoleRequest.getLastName());
        userRep.setUsername(userWithRoleRequest.getUserName());
        userRep.setEmail(userWithRoleRequest.getEmail());
        userRep.setEnabled(true);
        userRep.setEmailVerified(true);
        List<CredentialRepresentation> creds = new ArrayList<>();
        CredentialRepresentation cred = new CredentialRepresentation();
        cred.setTemporary(true);
        cred.setValue(userWithRoleRequest.getPassword());
        creds.add(cred);
        cred.setType(CredentialRepresentation.PASSWORD);
        userRep.setCredentials(creds);
        Map<String, List<String>> attributes = new HashMap<>();
        attributes.put("city", Collections.singletonList(userWithRoleRequest.getCity()));
        attributes.put("available", Collections.singletonList(userWithRoleRequest.getAvailable()));
       // attributes.put("available", Collections.singletonList(String.valueOf(true)));
      //  attributes.put("IdCentreTechnique", Collections.singletonList(userWithRoleRequest.getIdCentreTechnique()));
        //attributes.put("enabled", Collections.singletonList(String.valueOf(true)));
        attributes.put("type", Collections.singletonList(userWithRoleRequest.getType()));
        attributes.put("phone_number", Collections.singletonList(userWithRoleRequest.getPhone_number()));
        userRep.setAttributes(attributes);

        Response response = keycloak.realm(realm).users().create(userRep);
        if (response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL) {
            String userId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");
            RoleRepresentation role = keycloak.realm(realm).roles().get(userWithRoleRequest.getRoleName()).toRepresentation();
            keycloak.realm(realm).users().get(userId).roles().realmLevel().add(Arrays.asList(role));

            String subject = "Votre compte a été créé";
            String text = String.format("Bonjour %s %s,\n\nVotre compte a été créé avec succès.\n\nNom d'utilisateur : %s\nMot de passe : %s\n\nMerci,\nL'équipe %s",
                    userRep.getFirstName(), userRep.getLastName(), userRep.getUsername(), userWithRoleRequest.getPassword(), "TelecomTrack");


            emailService.sendEmail(userWithRoleRequest.getEmail(), subject, text);
            return Response.ok(userWithRoleRequest).build();
        } else {
            return Response.status(response.getStatus()).build();
        }
    }

    @GetMapping("/users/check-username/{username}")
    public boolean checkUsernameExists(@PathVariable String username) {
        Keycloak keycloak = keycloakUtil.getKeycloakInstance();
        List<UserRepresentation> users = keycloak.realm(realm).users().search(username);
        return users.stream().anyMatch(user -> user.getUsername().equals(username));
    }

    @GetMapping("/users/check-email/{email}")
    public boolean checkEmailExists(@PathVariable String email) {
        Keycloak keycloak = keycloakUtil.getKeycloakInstance();
        List<UserRepresentation> users = keycloak.realm(realm).users().search(null, null, null, email, null, null);
        return users.stream().anyMatch(user -> user.getEmail().equalsIgnoreCase(email));
    }

    @GetMapping("/users/check-email1/{email}/{userId}")
    public boolean checkEmailExists1(@PathVariable String email, @PathVariable String userId) {
        Keycloak keycloak = keycloakUtil.getKeycloakInstance();
        List<UserRepresentation> users = keycloak.realm(realm).users().search(null, null, null, email, null, null);
        return users.stream()
                .anyMatch(user -> user.getEmail().equalsIgnoreCase(email) && !user.getId().equals(userId));
    }

    private UserRepresentation mapUserRep(User1 user) {
        UserRepresentation userRep = new UserRepresentation();
        userRep.setId(user.getId());
        userRep.setUsername(user.getUserName());
        userRep.setFirstName(user.getFirstName());
        userRep.setLastName(user.getLastName());
        userRep.setEmail(user.getEmail());
        userRep.setEnabled(true);
        userRep.setEmailVerified(true);

        List<CredentialRepresentation> creds = new ArrayList<>();
        CredentialRepresentation cred = new CredentialRepresentation();
        cred.setTemporary(false);
        cred.setValue(user.getPassword());
        creds.add(cred);
        userRep.setCredentials(creds);

        Map<String, List<String>> attributes = new HashMap<>();
        attributes.put("city", Collections.singletonList(user.getCity()));
        attributes.put("type", Collections.singletonList(user.getType()));
        attributes.put("phone_number", Collections.singletonList(user.getPhone_number()));
     //   attributes.put("IdCentreTechnique", Collections.singletonList(user.getIdCentreTechnique()));
        //attributes.put("job_title", Collections.singletonList(user.getJobTitle()));
        attributes.put("available", Collections.singletonList(user.getAvailable()));
       // attributes.put("available", Collections.singletonList(String.valueOf(user.isAvailable())));
        userRep.setAttributes(attributes);

        return userRep;
    }


    @GetMapping("/send")
    public String sendTestEmail(@RequestParam String to, @RequestParam String subject, @RequestParam String text) {
        emailService.sendEmail(to, subject, text);
        return "Email sent to " + to;
    }

    @GetMapping("/users/search/{keyword}")
    public List<User1> searchUsers(@PathVariable("keyword") String keyword) {
        Keycloak keycloak = keycloakUtil.getKeycloakInstance();
        List<UserRepresentation> userRepresentations = keycloak.realm(realm).users().search(keyword);
        return mapUsers(userRepresentations);
    }

}