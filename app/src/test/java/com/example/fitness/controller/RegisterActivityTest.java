package com.example.fitness.controller;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.os.Build;
import android.os.Looper;

import com.example.fitness.model.ReadWriteUserDetails;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;


import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
public class RegisterActivityTest {


    @Test
    public void testRegisterUser_SuccessfulRegistration() {
        // Mock FirebaseAuth and related objects
        FirebaseAuth authMock = mock(FirebaseAuth.class);
        Task<AuthResult> authResultTaskMock = mock(Task.class);
        AuthResult authResultMock = mock(AuthResult.class);
        FirebaseUser firebaseUserMock = mock(FirebaseUser.class);

        // Mock DatabaseReference and related objects
        FirebaseDatabase firebaseDatabaseMock = mock(FirebaseDatabase.class);
        DatabaseReference databaseReferenceMock = mock(DatabaseReference.class);

        // Create an instance of the RegisterActivity
        RegisterActivity registerActivity = new RegisterActivity();

        // Set the mocked FirebaseAuth and DatabaseReference instances
        registerActivity.setAuth(authMock);
        when(authMock.getCurrentUser()).thenReturn(firebaseUserMock);
        when(authMock.createUserWithEmailAndPassword(anyString(), anyString())).thenReturn(authResultTaskMock);
        when(authResultTaskMock.isSuccessful()).thenReturn(true);
        when(authResultTaskMock.getResult()).thenReturn(authResultMock);

        when(FirebaseDatabase.getInstance()).thenReturn(firebaseDatabaseMock);
        when(firebaseDatabaseMock.getReference("Registered User")).thenReturn(databaseReferenceMock);

        // Set the necessary input parameters for user registration
        String txt_name = "John Doe";
        String txt_email = "john@example.com";
        String txt_gender = "Male";
        String txt_age = "25";
        String txt_goals = "Fitness";
        String txt_height = "180";
        String txt_weight = "75";
        String txt_password = "password123";

        // Call the registerUser method
        registerActivity.registerUser(txt_name, txt_email, txt_gender, txt_age, txt_goals, txt_height, txt_weight, txt_password);

        // Verify that the appropriate methods were called with the expected arguments
        verify(authMock).createUserWithEmailAndPassword(txt_email, txt_password);
        verify(firebaseUserMock).updateProfile(any(UserProfileChangeRequest.class));
        verify(databaseReferenceMock).child(firebaseUserMock.getUid());
        verify(databaseReferenceMock).setValue(any(ReadWriteUserDetails.class));
        verify(firebaseUserMock).sendEmailVerification();

        Assert.assertTrue(registerActivity.isRegistrationSuccessful());
    }

}
