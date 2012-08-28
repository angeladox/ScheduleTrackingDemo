package org.motechproject.mobileforms.api.service;

import java.util.ArrayList;
import java.util.List;

import org.motechproject.mobileforms.api.utils.Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsersServiceImpl implements UsersService {
    private Encoder encoder;

    @Autowired
    public UsersServiceImpl(Encoder encoder) {
        this.encoder = encoder;
    }

    @Override
    public List<Object[]> getUsers() {
        String[] userAccounts = "motech|ghs|7357658437bd298b4a48b7357489357,guyzb|daniel123|135df6eacf3e3f21866ecff10378035edbf7"
                .split(",");
        List<Object[]> users = new ArrayList<Object[]>();
        for (int i = 0; i < userAccounts.length; i++) {
            String[] userDetails = userAccounts[i].split("\\|");
            String userName = userDetails[0];
            String password = userDetails[1];
            String salt = userDetails[2];
            users.add(new Object[] { i + 1, userName, encoder.sha(password, salt), salt });
        }
        return users;
    }
}
