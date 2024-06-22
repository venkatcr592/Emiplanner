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

public class Home extends AppCompatActivity {

    private EditText etAmount, etInterestRate, etLoanTenure;
    private TextView tvEMI, tvPrincipalAmount, tvTotalInterest, tvTotalAmount;
    private Button btnCalculate;
    private String loanTitle;
    private double DownPayment;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        firestore = FirebaseFirestore.getInstance();
        loanTitle = "Home Loan";
        setTitle(loanTitle);
        TextView textViewLoanTitle = findViewById(R.id.textview_loan_title);
        textViewLoanTitle.setText(loanTitle);
        etAmount = findViewById(R.id.et_amount);
        etInterestRate = findViewById(R.id.et_interest_rate);
        etLoanTenure = findViewById(R.id.et_loan_tenure);
        tvEMI = findViewById(R.id.tv_emi);
        tvPrincipalAmount = findViewById(R.id.tv_principal_amount);
        tvTotalInterest = findViewById(R.id.tv_total_interest);
        tvTotalAmount = findViewById(R.id.tv_total_amount);
        btnCalculate = findViewById(R.id.btn_calculate);
        DownPayment = 0;

        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateLoan();
            }
        });
    }

    private void calculateLoan() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = currentUser.getUid();
        double amount = Double.parseDouble(etAmount.getText().toString());
        double interestRate = Double.parseDouble(etInterestRate.getText().toString());
        int loanTenure = Integer.parseInt(etLoanTenure.getText().toString());

        double principalAmount = amount;
        double monthlyInterestRate = interestRate / (12 * 100);
        int loanTermInMonths = loanTenure * 12;

        double emi = (principalAmount * monthlyInterestRate * Math.pow(1 + monthlyInterestRate, loanTermInMonths))
                / (Math.pow(1 + monthlyInterestRate, loanTermInMonths) - 1);
        double totalInterest = emi * loanTermInMonths - principalAmount;
        double totalAmount = emi * loanTermInMonths;

        tvEMI.setText("Rs."+String.format("%.2f", emi)+" Per Month");
        tvPrincipalAmount.setText("Rs."+String.format("%.2f", principalAmount));
        tvTotalInterest.setText("Rs."+String.format("%.2f", totalInterest));
        tvTotalAmount.setText("Rs."+String.format("%.2f", totalAmount));

        Map<String, Object> loanDetails = new HashMap<>();
        loanDetails.put("userId", userId);
        loanDetails.put("loan_title", loanTitle);
        loanDetails.put("amount", amount);
        loanDetails.put("down_payment", DownPayment);
        loanDetails.put("rate_of_interest", interestRate);
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
                        // Success message or further actions
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Failure message or error handling
                    }
                });
    }

}
