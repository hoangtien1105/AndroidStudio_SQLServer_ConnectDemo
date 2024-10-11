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
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.w3c.dom.Text;

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
    Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listViewMembers);
        memberList = new ArrayList<>();
        msg = findViewById(R.id.textView);
        button = findViewById(R.id.btnAdd);



        ConnectionClass connectionClass = new ConnectionClass();
        con = connectionClass.CONN();

        connectAndLoadData();

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

        button.setOnClickListener(v -> showAddDialog());

    }

    public void connectAndLoadData() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            try {
                if (con == null) {
                    str = "Error";
                } else {
                    str = "Connected with SQL Server";

                    // Truy vấn SQL
                    String query = "SELECT MemberId, Email, CompanyName, City, Country, Password FROM [SaleManagement].[dbo].[Member]";
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);

                    // Lấy dữ liệu từ ResultSet
                    while (rs.next()) {
                        int memberId = rs.getInt("MemberId");
                        String email = rs.getString("Email");
                        String companyName = rs.getString("CompanyName");
                        String city = rs.getString("City");
                        String country = rs.getString("Country");
                        String password = rs.getString("Password");

                        // Thêm member vào danh sách
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

                // Hiển thị danh sách lên ListView
                adapter = new MemberAdapter(MainActivity.this, R.layout.item_member, memberList);
                listView.setAdapter(adapter);

                Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
                msg.setText("Connected to SQL successfully");

            });
        });
    }

    // Hàm hiển thị dialog để xóa thành viên
    private void showDeleteDialog(Member member, int position) {
        AlertDialog.Builder dialogXoa = new AlertDialog.Builder(this);
        dialogXoa.setMessage("Bạn có chắc chắn muốn xóa thành viên " + member.getEmail() + "?");

        dialogXoa.setPositiveButton("Yes", (dialogInterface, i) -> {
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            executorService.execute(() -> {
                try {
                    if (con != null) {
                        String deleteQuery = "DELETE FROM [SaleManagement].[dbo].[Member] WHERE MemberId = ?";
                        PreparedStatement stmt = con.prepareStatement(deleteQuery);
                        stmt.setInt(1, member.getMemberId());
                        stmt.executeUpdate();

                        memberList.remove(position);

                        runOnUiThread(() -> {
                            adapter.notifyDataSetChanged();
                            Toast.makeText(MainActivity.this, "Đã xóa thành công!", Toast.LENGTH_SHORT).show();
                        });
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        });

        dialogXoa.setNegativeButton("No", (dialogInterface, i) -> dialogInterface.dismiss());
        dialogXoa.show();
    }

    // Hàm hiển thị dialog để cập nhật thông tin thành viên
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
                        String updateQuery = "UPDATE [SaleManagement].[dbo].[Member] SET Email = ?, CompanyName = ? WHERE MemberId = ?";
                        PreparedStatement stmt = con.prepareStatement(updateQuery);
                        stmt.setString(1, newEmail);
                        stmt.setString(2, newCompanyName);
                        stmt.setInt(3, member.getMemberId());
                        stmt.executeUpdate();

                        // Cập nhật lại thông tin trong danh sách
                        member.setEmail(newEmail);
                        member.setCompanyName(newCompanyName);

                        runOnUiThread(() -> {
                            adapter.notifyDataSetChanged();
                            Toast.makeText(MainActivity.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        });
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        });

        // Điền dữ liệu hiện tại vào dialog
        edtEmail.setText(member.getEmail());
        edtCompanyName.setText(member.getCompanyName());



        dialog.show();

        dialog.getWindow().setLayout(800, 1000); // Set width and height
    }


    private void showAddDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_update_member);

        EditText edtEmail = dialog.findViewById(R.id.editEmail);
        EditText edtCompanyName = dialog.findViewById(R.id.editCompanyName);
        Button btnAdd = dialog.findViewById(R.id.btnUpdate);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);

        // Đặt title cho dialog (vì đây là form thêm mới)
        TextView txtTitle = dialog.findViewById(R.id.txtTitle);
        txtTitle.setText("Thêm Thành Viên");

        // Đổi text của nút Update thành Add
        btnAdd.setText("Add");

        btnAdd.setOnClickListener(v -> {
            String email = edtEmail.getText().toString().trim();
            String companyName = edtCompanyName.getText().toString().trim();

            if (email.isEmpty() || companyName.isEmpty()) {
                Toast.makeText(MainActivity.this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            ExecutorService executorService = Executors.newSingleThreadExecutor();
            executorService.execute(() -> {
                try {
                    if (con != null) {
                        // Giá trị mặc định cho các field còn lại
                        String city = "test";
                        String country = "example";
                        String password = "example";

                        // Thực hiện lệnh INSERT để thêm thành viên mới
                        String insertQuery = "INSERT INTO [SaleManagement].[dbo].[Member] (Email, CompanyName, City, Country, Password) " +
                                "VALUES (?, ?, ?, ?, ?)";
                        PreparedStatement stmt = con.prepareStatement(insertQuery);
                        stmt.setString(1, email);
                        stmt.setString(2, companyName);
                        stmt.setString(3, city);
                        stmt.setString(4, country);
                        stmt.setString(5, password);
                        stmt.executeUpdate();

                        // Thêm member mới vào danh sách và cập nhật giao diện
                        Member newMember = new Member(0, email, companyName, city, country, password); // MemberId sẽ tự động tăng
                        memberList.add(newMember);

                        runOnUiThread(() -> {
                            adapter.notifyDataSetChanged();
                            Toast.makeText(MainActivity.this, "Thêm thành viên thành công!", Toast.LENGTH_SHORT).show();
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

        // Điều chỉnh kích thước dialog nếu cần
        dialog.getWindow().setLayout(800, 800); // Set width and height
    }


}
