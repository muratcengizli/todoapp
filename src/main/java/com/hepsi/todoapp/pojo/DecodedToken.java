package com.hepsi.todoapp.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;

class RealmAccess {
    public ArrayList<String> roles;
}

class RealmManagement {
    public ArrayList<String> roles;
}

class KsManagement {
    public ArrayList<String> roles;
}

class Account {
    public ArrayList<String> roles;
}

class ResourceAccess {
    @JsonProperty("realm-management")
    public RealmManagement realmManagement;
    @JsonProperty("ks-management")
    public KsManagement ksManagement;
    public Account account;
}
@Data
public class DecodedToken {
    public String jti;
    public Integer exp;
    public Integer nbf;
    public Integer iat;
    public String iss;
    public ArrayList<String> aud;
    public String sub;
    public String typ;
    public String azp;
    public Integer auth_time;
    public String session_state;
    public String acr;
    public ArrayList<Object> amr;
    public RealmAccess realm_access;
    public ResourceAccess resource_access;
    public String scope;
    public Boolean email_verified;
    public String name;
    public String preferred_username;
    public String given_name;
    public String family_name;
    public String email;
}
