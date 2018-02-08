package com.build1.rapepreventionapp.Model;

import android.support.annotation.NonNull;

/**
 * Created by JEMYLA VELILLA on 08/02/2018.
 */

public class UserId {

    public String userId;

    public <T extends UserId> T withId(@NonNull final String id) {
        this.userId = id;
        return (T) this;
    }
}
