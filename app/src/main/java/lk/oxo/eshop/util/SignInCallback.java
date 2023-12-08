package lk.oxo.eshop.util;

import lk.oxo.eshop.model.User;

public interface SignInCallback {
    void onResult(User user);
}
