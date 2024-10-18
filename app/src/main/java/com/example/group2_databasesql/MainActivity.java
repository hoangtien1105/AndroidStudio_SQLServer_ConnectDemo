package com.example.group2_databasesql;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    Connection con;
    String str;
    ListView listView;
    List<Member> memberList;
    MemberAdapter adapter;
    TextView msg;
    Button btnConnectSQLServer, btnConnectMySQL, btnAdd, btnDisconnect;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listViewMembers);
        memberList = new ArrayList<>();
        msg = findViewById(R.id.textView);
        btnConnectSQLServer = findViewById(R.id.btnConnectSQLServer);
        btnConnectMySQL = findViewById(R.id.btnConnectMySQL);
        btnAdd = findViewById(R.id.btnAdd);
        btnDisconnect = findViewById(R.id.btnDisconnect);


        btnConnectSQLServer.setOnClickListener(v -> {
            ConnectionClass connectionClass = new ConnectionClass();
            con = connectionClass.CONN_SQLServer();
            connectAndLoadDataSQLServer();  // Call the SQL Server-specific method
        });

        // MySQL Button
        btnConnectMySQL.setOnClickListener(v -> {
            ConnectionClass connectionClass = new ConnectionClass();
            con = connectionClass.CONN_MySQL();
            connectAndLoadDataMySQL();  // Call the MySQL-specific method
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Member selectedMember = memberList.get(position);
                showDeleteDialog(selectedMember, position);
                return true;
            }
        });

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Member selectedMember = memberList.get(position);
            showUpdateDialog(selectedMember, position);
        });

        btnAdd.setOnClickListener(v -> showAddDialog());
        btnDisconnect.setOnClickListener(v -> {
            try {
                if (con != null && !con.isClosed()) {
                    con.close(); // Close the connection
                    Toast.makeText(MainActivity.this, "Disconnected from database", Toast.LENGTH_SHORT).show();

                    // Hide the ListView and Add button
                    listView.setVisibility(View.GONE);
                    btnAdd.setVisibility(View.GONE);

                    // Show the connect buttons again
                    btnConnectMySQL.setVisibility(View.VISIBLE);
                    btnConnectSQLServer.setVisibility(View.VISIBLE);

                    // Hide the Disconnect button
                    btnDisconnect.setVisibility(View.GONE);

                    msg.setText("Disconnected successfully");
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, "Error while disconnecting!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void connectAndLoadDataSQLServer() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            try {
                if (con == null) {
                    str = "Error";
                } else {
                    str = "Connected with SQL Server";

                    // Query SQL Server
                    String query = "SELECT MemberId, Email, CompanyName, City, Country, Password FROM [SaleManagement].[dbo].[Member]";
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);

                    // Fetch data from ResultSet
                    while (rs.next()) {
                        int memberId = rs.getInt("MemberId");
                        String email = rs.getString("Email");
                        String companyName = rs.getString("CompanyName");
                        String city = rs.getString("City");
                        String country = rs.getString("Country");
                        String password = rs.getString("Password");

                        // Add member to the list
                        memberList.add(new Member(memberId, email, companyName, city, country, password));
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            // Update UI in the main thread
            runOnUiThread(() -> {
                // Set up ListView adapter
                adapter = new MemberAdapter(MainActivity.this, R.layout.item_member, memberList);
                listView.setAdapter(adapter);
                listView.setVisibility(View.VISIBLE);
                btnAdd.setVisibility(View.VISIBLE);

                // Show the Disconnect button and hide Connect buttons
                btnDisconnect.setVisibility(View.VISIBLE);
                btnConnectMySQL.setVisibility(View.GONE);
                btnConnectSQLServer.setVisibility(View.GONE);

                // Display success message
                Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
                msg.setText("Connected to SQL successfully");
            });
        });
    }

public void connectAndLoadDataMySQL() {
    ExecutorService executorService = Executors.newSingleThreadExecutor();
    executorService.execute(() -> {
        try {
            if (con == null) {
                str = "Error";
            } else {
                str = "Connected with MySQL";

                // MySQL query
                String query = "SELECT MemberId, Email, CompanyName, City, Country, Password FROM Member";
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                // Retrieve data from ResultSet
                while (rs.next()) {
                    int memberId = rs.getInt("MemberId");
                    String email = rs.getString("Email");
                    String companyName = rs.getString("CompanyName");
                    String city = rs.getString("City");
                    String country = rs.getString("Country");
                    String password = rs.getString("Password");

                    // Add member to the list
                    memberList.add(new Member(memberId, email, companyName, city, country, password));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        runOnUiThread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            // Display the list in ListView
            adapter = new MemberAdapter(MainActivity.this, R.layout.item_member, memberList);
            listView.setAdapter(adapter);
            listView.setVisibility(View.VISIBLE);
            btnAdd.setVisibility(View.VISIBLE);
            btnDisconnect.setVisibility(View.VISIBLE); // Show the Disconnect button
            btnConnectMySQL.setVisibility(View.GONE);
            btnConnectSQLServer.setVisibility(View.GONE); // Ẩn nút kết nối DB

            Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
            msg.setText("Connected to MySQL successfully");
        });
    });
}


    private void showDeleteDialog(Member member, int position) {
        AlertDialog.Builder dialogDelete = new AlertDialog.Builder(this);
        dialogDelete.setMessage("Are you sure you want to delete member " + member.getEmail() + "?");

        dialogDelete.setPositiveButton("Yes", (dialogInterface, i) -> {
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            executorService.execute(() -> {
                try {
                    if (con != null) {
                        String deleteQuery = "DELETE FROM Member WHERE MemberId = ?";
                        PreparedStatement stmt = con.prepareStatement(deleteQuery);
                        stmt.setInt(1, member.getMemberId());
                        stmt.executeUpdate();

                        memberList.remove(position);

                        runOnUiThread(() -> {
                            adapter.notifyDataSetChanged();
                            Toast.makeText(MainActivity.this, "Successfully deleted!", Toast.LENGTH_SHORT).show();
                        });
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        });
        dialogDelete.setNegativeButton("No", (dialogInterface, i) -> dialogInterface.dismiss());
        dialogDelete.show();
    }

    private void showUpdateDialog(Member member, int position) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_update_member);

        EditText edtEmail = dialog.findViewById(R.id.editEmail);
        EditText edtCompanyName = dialog.findViewById(R.id.editCompanyName);
        Button btnUpdate = dialog.findViewById(R.id.btnUpdate);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);

        btnUpdate.setOnClickListener(v -> {
            String newEmail = edtEmail.getText().toString().trim();
            String newCompanyName = edtCompanyName.getText().toString().trim();

            ExecutorService executorService = Executors.newSingleThreadExecutor();
            executorService.execute(() -> {
                try {
                    if (con != null) {
                        String updateQuery = "UPDATE Member SET Email = ?, CompanyName = ? WHERE MemberId = ?";
                        PreparedStatement stmt = con.prepareStatement(updateQuery);
                        stmt.setString(1, newEmail);
                        stmt.setString(2, newCompanyName);
                        stmt.setInt(3, member.getMemberId());
                        stmt.executeUpdate();

                        // Update the list with new information
                        member.setEmail(newEmail);
                        member.setCompanyName(newCompanyName);

                        runOnUiThread(() -> {
                            adapter.notifyDataSetChanged();
                            Toast.makeText(MainActivity.this, "Updated successfully!", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        });
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        });

        edtEmail.setText(member.getEmail());
        edtCompanyName.setText(member.getCompanyName());
        btnCancel.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
        dialog.getWindow().setLayout(800, 1000);
        // Set width and height
    }

    private void showAddDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_update_member);

        EditText edtEmail = dialog.findViewById(R.id.editEmail);
        EditText edtCompanyName = dialog.findViewById(R.id.editCompanyName);
        Button btnAdd = dialog.findViewById(R.id.btnUpdate);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);

        // Set title for the dialog (this is the add new member form)
        TextView txtTitle = dialog.findViewById(R.id.txtTitle);
        txtTitle.setText("Add Member");

        // Change text of the Update button to Add
        btnAdd.setText("Add");

        btnAdd.setOnClickListener(v -> {
            String email = edtEmail.getText().toString().trim();
            String companyName = edtCompanyName.getText().toString().trim();

            if (email.isEmpty() || companyName.isEmpty()) {
                Toast.makeText(MainActivity.this, "Please fill in all information!", Toast.LENGTH_SHORT).show();
                return;
            }

            ExecutorService executorService = Executors.newSingleThreadExecutor();
            executorService.execute(() -> {
                try {
                    if (con != null) {
                        // Default values for other fields
                        String city = "test";
                        String country = "example";
                        String password = "example";

                        // Perform INSERT to add new member
                        String insertQuery = "INSERT INTO Member (Email, CompanyName, City, Country, Password) VALUES (?, ?, ?, ?, ?)";
                        PreparedStatement stmt = con.prepareStatement(insertQuery);
                        stmt.setString(1, email);
                        stmt.setString(2, companyName);
                        stmt.setString(3, city);
                        stmt.setString(4, country);
                        stmt.setString(5, password);
                        stmt.executeUpdate();

                        // Add new member to the list and update the UI
                        Member newMember = new Member(0, email, companyName, city, country, password); // MemberId will auto-increment
                        memberList.add(newMember);

                        runOnUiThread(() -> {
                            adapter.notifyDataSetChanged();
                            Toast.makeText(MainActivity.this, "Member added successfully!", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        });
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
        dialog.getWindow().setLayout(800, 800); // Set width and height
    }

}
