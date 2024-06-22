package com.example.miniproject1jt20is050_1jt20is031;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Vehicle extends AppCompatActivity {

    private EditText etAmount, etDownPayment, etRateOfInterest, etLoanTenure;
    private TextView tvEMI, tvPrincipal, tvTotalInterest, tvTotalAmount;
    private Button btnCalculate;
    private String loanTitle;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle);
        firestore = FirebaseFirestore.getInstance();

        loanTitle = getIntent().getStringExtra("loan_title");
        setTitle(loanTitle);
        TextView textViewLoanTitle = findViewById(R.id.textview_loan_title);
        textViewLoanTitle.setText(loanTitle);
        etAmount = findViewById(R.id.et_amount);
        etDownPayment = findViewById(R.id.et_down_payment);
        etRateOfInterest = findViewById(R.id.et_rate_of_interest);
        etLoanTenure = findViewById(R.id.et_loan_tenure);
        tvEMI = findViewById(R.id.tv_emi);
        tvPrincipal = findViewById(R.id.tv_principal);
        tvTotalInterest = findViewById(R.id.tv_total_interest);
        tvTotalAmount = findViewById(R.id.tv_total_amount);
        btnCalculate = findViewById(R.id.btn_calculate);

        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateEMI();
            }
        });
    }

    private void calculateEMI() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = currentUser.getUid();
        double amount = Double.parseDouble(etAmount.getText().toString());
        double downPayment = Double.parseDouble(etDownPayment.getText().toString());
        double rateOfInterest = Double.parseDouble(etRateOfInterest.getText().toString());
        double loanTenure = Double.parseDouble(etLoanTenure.getText().toString());

        double principalAmount = amount - downPayment;
        double monthlyInterestRate = rateOfInterest / (12 * 100);
        double loanTermMonths = loanTenure * 12;

        double emi = (principalAmount * monthlyInterestRate * Math.pow(1 + monthlyInterestRate, loanTermMonths))
                / (Math.pow(1 + monthlyInterestRate, loanTermMonths) - 1);
        double totalInterest = (emi * loanTermMonths) - principalAmount;
        double totalAmount = principalAmount + totalInterest;

        tvEMI.setText("Rs."+String.format("%.2f", emi)+" Per Month");
        tvPrincipal.setText("Rs."+String.format("%.2f", principalAmount));
        tvTotalInterest.setText("Rs."+String.format("%.2f", totalInterest));
        tvTotalAmount.setText("Rs."+String.format("%.2f", totalAmount));

        Map<String, Object> loanDetails = new HashMap<>();
        loanDetails.put("userId", userId);
        loanDetails.put("loan_title", loanTitle);
        loanDetails.put("amount", amount);
        loanDetails.put("down_payment", downPayment);
        loanDetails.put("rate_of_interest", rateOfInterest);
        loanDetails.put("loan_tenure", loanTenure);
        loanDetails.put("emi", emi);
        loanDetails.put("principal_amount", principalAmount);
        loanDetails.put("total_interest", totalInterest);
        loanDetails.put("total_amount", totalAmount);

        firestore.collection("loans")
                .add(loanDetails)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }
}
