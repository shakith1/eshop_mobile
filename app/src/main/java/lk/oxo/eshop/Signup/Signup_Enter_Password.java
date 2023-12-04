package lk.oxo.eshop.Signup;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import lk.oxo.eshop.R;
import lk.oxo.eshop.util.UIMode;

public class Signup_Enter_Password extends Fragment {

    private FirebaseAuth firebaseAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signup__enter__password, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();

        EditText password = view.findViewById(R.id.password_signup);
        TextView password_hint = view.findViewById(R.id.textView15);
        Button create_account = view.findViewById(R.id.button14);

//        UI Customization

        Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in_animation);

        if (UIMode.getUiModeFlags(getContext()) != Configuration.UI_MODE_NIGHT_NO)
            create_account.setBackgroundResource(R.drawable.button_background_continue_night_disable);

        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String pass = password.getText().toString().trim();
                String message = getMessage(pass);

                if (!password_hint.getText().toString().equals(message)) {
                    password_hint.setText(message);
                    password_hint.startAnimation(animation);
                }

                if (message.isEmpty()) {
                    create_account.setEnabled(true);
                    if (UIMode.getUiModeFlags(getContext()) == Configuration.UI_MODE_NIGHT_YES)
                        create_account.setBackgroundResource(R.drawable.button_background_continue_night);
                } else {
                    if (UIMode.getUiModeFlags(getContext()) == Configuration.UI_MODE_NIGHT_YES)
                        create_account.setBackgroundResource(R.drawable.button_background_continue_night_disable);
                    else
                        create_account.setBackgroundResource(R.drawable.disable_button);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        password.addTextChangedListener(watcher);

//        End UI Customization

        create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstname, lastname, email;
                String pass = password.getText().toString();

                Bundle data = getArguments();
                System.out.println(data);
                if (data != null) {
                    email = getString(R.string.email_bundle);

                    Bundle data_second = new Bundle();
                    data_second.putBundle(getString(R.string.user_bundle), data);
                    data_second.putString(getString(R.string.password_bundle), pass);

                    Signup_Enter_Mobile enter_mobile = new Signup_Enter_Mobile();
                    enter_mobile.setArguments(data_second);

                    getActivity().getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(R.id.fragmentContainerView, enter_mobile, null)
                            .addToBackStack(null)
                            .commit();

//                    firebaseAuth.createUserWithEmailAndPassword(email, pass)
//                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                                @Override
//                                public void onComplete(@NonNull Task<AuthResult> task) {
//                                    if (task.isSuccessful()) {
//                                        FirebaseUser user = firebaseAuth.getCurrentUser();
////                                        if (user != null) {
////                                            user.updateProfile(new UserProfileChangeRequest
////                                                            .Builder()
////                                                            .setDisplayName(firstname + " " + lastname).build())
////                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
////                                                        @Override
////                                                        public void onComplete(@NonNull Task<Void> task) {
////                                                            if (task.isSuccessful()) {
////                                                                Toast.makeText(getContext(),
////                                                                        getString(R.string.email_user_created),
////                                                                        Toast.LENGTH_SHORT).show();
////                                                                user.sendEmailVerification();
////                                                            }
////                                                        }
////                                                    });
////                                        }
//                                    } else {
//                                        Toast.makeText(getContext(),
//                                                getString(R.string.user_creation_error),
//                                                Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//                            });

                }
            }
        });
    }

    private String getMessage(String password) {
        if (password.isEmpty()) {
            return getString(R.string.password_hint_empty);
        } else if (password.length() < 8) {
            if (!password.matches(".*[a-zA-Z].*")) {
                return getString(R.string.password_hint_1);
            } else if (!password.matches(".*[0-9\\W_].*")) {
                return getString(R.string.password_hint_2);
            } else {
                return getString(R.string.password_hint_3);
            }
        } else if (!password.matches(".*[a-zA-Z].*")) {
            if (!password.matches(".*[0-9\\W_].*")) {
                return getString(R.string.password_hint_4);
            } else {
                return getString(R.string.password_hint_5);
            }
        } else if (!password.matches(".*[0-9\\W_].*")) {
            return getString(R.string.password_hint_6);
        } else {
            return "";
        }
    }
}