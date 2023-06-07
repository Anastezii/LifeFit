package com.example.fitness;

import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import androidx.test.core.app.ActivityScenario;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.example.fitness.controller.RegisterActivity;
import com.google.firebase.auth.FirebaseAuth;

@RunWith(JUnit4.class)
public class RegistrationActivityTest {

    private FirebaseAuth firebaseAuth;

    @Before
    public void setUp() {
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @After
    public void tearDown() {
        firebaseAuth.signOut();
    }

    private EditText name_registration;
    private Spinner age_registration;
    private RadioGroup radioGroup_registration;
    private RadioButton radioButton_female;
    private RadioButton radioButton_male;
    private EditText height_registration;
    private EditText weight_registration;
    private Spinner goals_registration;
    private EditText email_registration;
    private EditText password_registration;
    private Button registration_button;

    @Before
    public void setup() {
        ActivityScenario.launch(RegisterActivity.class).onActivity(activity -> {
            name_registration = activity.findViewById(R.id.name_client_registration);
            age_registration = activity.findViewById(R.id.age_client_registration);
            radioGroup_registration = activity.findViewById(R.id.radioGroup_registration);
            radioButton_female = activity.findViewById(R.id.radioButton_female);
            radioButton_male = activity.findViewById(R.id.radioButton_male);
            height_registration = activity.findViewById(R.id.height_registration);
            weight_registration = activity.findViewById(R.id.weight_registration);
            goals_registration = activity.findViewById(R.id.spinner_goals_registration);
            email_registration = activity.findViewById(R.id.email_registration);
            password_registration = activity.findViewById(R.id.password_registartion);
            registration_button = activity.findViewById(R.id.registration_button);
        });
    }

    @Test
    public void testRegistrationWithValidData() {
        name_registration.setText("John Doe");
        age_registration.setSelection(2);  // Select a valid age
        radioButton_male.setChecked(true);  // Select male radio button
        height_registration.setText("180");
        weight_registration.setText("75");
        goals_registration.setSelection(1);  // Select a valid goal
        email_registration.setText("john.doe@example.com");
        password_registration.setText("password123");

        registration_button.performClick();

        // Verify that registration is successful

    }

    @Test
    public void testRegistrationWithEmptyName() {
        // Set other fields with valid data
        name_registration.setText("");  // Empty name

        registration_button.performClick();

        // TODO: Assert the expected behavior when name is empty
    }

    @Test
    public void testRegistrationWithEmptyGender() {
        // Set other fields with valid data
        radioGroup_registration.clearCheck();  // No gender selected

        registration_button.performClick();

        // TODO: Assert the expected behavior when gender is not selected
    }
}
