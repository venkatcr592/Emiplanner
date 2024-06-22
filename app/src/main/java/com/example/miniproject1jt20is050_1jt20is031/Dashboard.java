package com.example.miniproject1jt20is050_1jt20is031;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Dashboard extends AppCompatActivity {

    private String domain, subdomain;
    private TextView tvdomain, tvsubdomian;
    private Spinner domainSpinner, subdomainSpinner;
    private ArrayAdapter<CharSequence> domainAdapter, subdomainAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        ImageButton btnRetrieveLoans = findViewById(R.id.btnRetrieveLoans);
        domainSpinner = findViewById(R.id.spinner_domain);
        subdomainSpinner = findViewById(R.id.spinner_sub_domain);
        tvdomain = findViewById(R.id.textView_domain);
        tvsubdomian = findViewById(R.id.textView_sub_domain);
        Button submitButton = findViewById(R.id.button_submit);

        domainAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_domain, R.layout.spinner_layout);
        domainAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        domainSpinner.setAdapter(domainAdapter);

        domainSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                domain = domainSpinner.getSelectedItem().toString();
                if (domain.equals("Select Your Domain")) {
                    subdomainAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                            R.array.array_sub_domain, R.layout.spinner_layout);
                } else if (domain.equals("Vehicle Loan")) {
                    subdomainAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                            R.array.array_vehicle_loan, R.layout.spinner_layout);
                } else if (domain.equals("Electronics")) {
                    subdomainAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                            R.array.array_electronics, R.layout.spinner_layout);
                } else if (domain.equals("Home Loan")) {
                    subdomainAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                            R.array.array_home_loan, R.layout.spinner_layout);
                } else if (domain.equals("Personal Loan")) {
                    subdomainAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                            R.array.array_personal_loan, R.layout.spinner_layout);
                }
                subdomainAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                subdomainSpinner.setAdapter(subdomainAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        subdomainSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                subdomain = subdomainSpinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (domain.equals("Select Your Domain")) {
                    Toast.makeText(Dashboard.this, "Please select your domain from the list", Toast.LENGTH_LONG).show();
                    tvdomain.setError("Domain is required!");
                    tvdomain.requestFocus();
                } else if (subdomain.equals("Select Your Sub-Domain")) {
                    Toast.makeText(Dashboard.this, "Please select your sub-domain from the list", Toast.LENGTH_LONG).show();
                    tvsubdomian.setError("Sub-Domain is required!");
                    tvsubdomian.requestFocus();
                    tvdomain.setError(null);
                } else {
                    tvdomain.setError(null);
                    tvsubdomian.setError(null);
                    Toast.makeText(Dashboard.this, "Selected Domain: " + domain + "\nSelected Sub-Domain: " + subdomain, Toast.LENGTH_LONG).show();

                    if (domain.equals("Vehicle Loan")) {
                        String loanTitle = subdomain + " Loan";
                        Intent intent = new Intent(Dashboard.this, Vehicle.class);
                        intent.putExtra("loan_title", loanTitle);
                        startActivity(intent);
                    } else if (domain.equals("Electronics")) {
                        String loanTitle = subdomain + " Loan";
                        Intent intent = new Intent(Dashboard.this, Electronics.class);
                        intent.putExtra("loan_title", loanTitle);
                        startActivity(intent);
                    } else if (domain.equals("Home Loan")) {
                        Intent intent = new Intent(Dashboard.this, Home.class);
                        startActivity(intent);
                    } else if (domain.equals("Personal Loan")) {
                        Intent intent = new Intent(Dashboard.this, Personal.class);
                        startActivity(intent);
                    }
                }
            }
        });
        btnRetrieveLoans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this, History.class);
                startActivity(intent);
            }
        });

    }




}
