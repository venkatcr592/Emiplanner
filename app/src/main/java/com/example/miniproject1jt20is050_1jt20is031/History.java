package com.example.miniproject1jt20is050_1jt20is031;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class History extends AppCompatActivity {
    private FirebaseFirestore firestore;
    private LoanAdapter loanAdapter;
    private RecyclerView recyclerViewLoans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (currentUser != null) {
            String currentUserId = currentUser.getUid();
            recyclerViewLoans = findViewById(R.id.recyclerViewLoans);
            recyclerViewLoans.setLayoutManager(new LinearLayoutManager(this));
            loanAdapter = new LoanAdapter();
            recyclerViewLoans.setAdapter(loanAdapter);
            firestore = FirebaseFirestore.getInstance();

            CollectionReference loansCollection = firestore.collection("loans");
            Query query = loansCollection.whereEqualTo("userId", currentUserId);
            query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    List<Loan> loans = new ArrayList<>();

                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        // Get loan details from documentSnapshot
                        String loanTitle = documentSnapshot.getString("loan_title");
                        double amount = documentSnapshot.getDouble("amount");
                        double downPayment = documentSnapshot.getDouble("down_payment");
                        double rateOfInterest = documentSnapshot.getDouble("rate_of_interest");
                        double loanTenure = documentSnapshot.getDouble("loan_tenure");
                        double emi = documentSnapshot.getDouble("emi");
                        double principalAmount = documentSnapshot.getDouble("principal_amount");
                        double totalInterest = documentSnapshot.getDouble("total_interest");
                        double totalAmount = documentSnapshot.getDouble("total_amount");

                        // Create a Loan object and add it to the loans list
                        Loan loan = new Loan(loanTitle, amount, downPayment, rateOfInterest, loanTenure,
                                emi, principalAmount, totalInterest, totalAmount);
                        loans.add(loan);
                    }

                    // Display the loans or perform further actions
                    displayLoans(loans);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // Handle failure or display an error message
                }
            });
        }
    }
    private static class Loan {
        private String loanTitle;
        private double amount;
        private double downPayment;
        private double rateOfInterest;
        private double loanTenure;
        private double emi;
        private double principalAmount;
        private double totalInterest;
        private double totalAmount;

        public Loan(String loanTitle, double amount, double downPayment, double rateOfInterest, double loanTenure,
                    double emi, double principalAmount, double totalInterest, double totalAmount) {
            this.loanTitle = loanTitle;
            this.amount = amount;
            this.downPayment = downPayment;
            this.rateOfInterest = rateOfInterest;
            this.loanTenure = loanTenure;
            this.emi = emi;
            this.principalAmount = principalAmount;
            this.totalInterest = totalInterest;
            this.totalAmount = totalAmount;
        }

        // Getter methods
        public String getLoanTitle() {
            return loanTitle;
        }

        public double getAmount() {
            return amount;
        }

        public double getDownPayment() {
            return downPayment;
        }

        public double getRateOfInterest() {
            return rateOfInterest;
        }

        public double getLoanTenure() {
            return loanTenure;
        }

        public double getEmi() {
            return emi;
        }

        public double getPrincipalAmount() {
            return principalAmount;
        }

        public double getTotalInterest() {
            return totalInterest;
        }

        public double getTotalAmount() {
            return totalAmount;
        }
    }
    private void displayLoans(List<Loan> loans) {
        loanAdapter.setLoans(loans);
    }
    private class LoanAdapter extends RecyclerView.Adapter<LoanAdapter.LoanViewHolder> {
        private List<Loan> loans;
        public LoanAdapter() {
            this.loans = new ArrayList<>();
        }

        public void setLoans(List<Loan> loans) {
            this.loans = loans;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public LoanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_loan, parent, false);
            return new LoanViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull LoanViewHolder holder, int position) {
            Loan loan = loans.get(position);
            holder.bind(loan);
        }

        @Override
        public int getItemCount() {
            return loans.size();
        }

        public class LoanViewHolder extends RecyclerView.ViewHolder {
            private TextView textViewLoanTitle;
            private TextView textViewAmount;
            private TextView textViewEMI;

            private TextView textViewDownPayment;

            private TextView textViewRateOfIntrest;

            private TextView textViewLoanTenure;

            private TextView textViewPrincipalAmount;
            private TextView textViewTotalInterest;
            private TextView textViewTotalAmount;

            public LoanViewHolder(@NonNull View itemView) {
                super(itemView);
                textViewLoanTitle = itemView.findViewById(R.id.textViewLoanTitle);
                textViewAmount = itemView.findViewById(R.id.textViewAmount);
                textViewDownPayment = itemView.findViewById(R.id.textViewDownPayment);
                textViewEMI = itemView.findViewById(R.id.textViewEMI);
                textViewRateOfIntrest = itemView.findViewById(R.id.textViewRateOfInterest);
                textViewLoanTenure = itemView.findViewById(R.id.textViewLoanTenure);
                textViewPrincipalAmount = itemView.findViewById(R.id.textViewPrincipalAmount);
                textViewTotalInterest = itemView.findViewById(R.id.textViewTotalInterest);
                textViewTotalAmount = itemView.findViewById(R.id.textViewTotalAmount);
            }

            public void bind(Loan loan) {
                textViewLoanTitle.setText(loan.getLoanTitle());
                textViewAmount.setText("Amount: " + String.format("%.2f", loan.getAmount()));
                textViewDownPayment.setText("Down Payment: " + String.format("%.2f", loan.getDownPayment()));
                textViewRateOfIntrest.setText("Rate of Intrest: " + String.format("%.2f", loan.getRateOfInterest()));
                textViewLoanTenure.setText("Tenure: " + String.format("%.2f", loan.getLoanTenure()));
                textViewPrincipalAmount.setText("Principal Amount: " + String.format("%.2f", loan.getPrincipalAmount()));
                textViewTotalInterest.setText("Total Interest: " + String.format("%.2f", loan.getTotalInterest()));
                textViewTotalAmount.setText("Total Amount: " + String.format("%.2f", loan.getTotalAmount()));
                textViewEMI.setText("EMI: " + String.format("%.2f", loan.getEmi()));
            }
        }

    }
}