package com.rizomm.ipii.steven;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named
@RequestScoped
public class UserController implements Serializable {

    private static final long serialVersionUID = 1L;

    private boolean isError;

    public String doCreateUser() {
        return "listeUser.xhtml";
    }

    public String pageCreateUser() {
        return "index.xhtml";
    }


    public String doFindUser() {
        return "viewUser.xhtml";
    }

    public boolean isError() {
        return isError;
    }
}