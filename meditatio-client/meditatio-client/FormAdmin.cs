using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace meditatio_client
{
    public partial class FormAdmin : Form
    {
        public FormAdmin()
        {
            InitializeComponent();
            // Setăm DataGridView-urile să nu poată fi editate direct
            dgvStudents.ReadOnly = true;
            dgvProfessors.ReadOnly = true;
            dgvAssignments.ReadOnly = true;
            dgvStudents.SelectionMode = DataGridViewSelectionMode.FullRowSelect;
            dgvProfessors.SelectionMode = DataGridViewSelectionMode.FullRowSelect;
            dgvAssignments.SelectionMode = DataGridViewSelectionMode.FullRowSelect;
        }
        
        private async void btnLoadUsers_Click(object sender, EventArgs e)
        {
        }

        private async void FormAdmin_Load(object sender, EventArgs e)
        {
            await LoadAllData();
        }
        private async Task LoadAllData()
        {
            this.Text = "Panou Admin - Se încarcă datele...";
            try
            {
                // Încarcă toate cele 3 tabele în paralel
                var studentsTask = ApiService.GetStudentsAsync();
                var professorsTask = ApiService.GetProfessorsAsync();
                var assignmentsTask = ApiService.GetAllAssignmentsAsync();

                // Așteaptă finalizarea tuturor
                await Task.WhenAll(studentsTask, professorsTask, assignmentsTask);

                // Populează tabelele
                dgvStudents.DataSource = await studentsTask;
                dgvProfessors.DataSource = await professorsTask;
                dgvAssignments.DataSource = await assignmentsTask;

                this.Text = "Panou Admin - Gata";
            }
            catch (Exception ex)
            {
                MessageBox.Show($"Eroare la încărcarea datelor: {ex.Message}");
                this.Text = "Panou Admin - Eroare la încărcare";
            }
        }

        // --- EXEMPLU BUTON ȘTERGERE STUDENT ---

        private async void btnDeleteStudent_Click(object sender, EventArgs e)
        {
            // 1. Verifică dacă e selectat un rând
            if (dgvStudents.SelectedRows.Count == 0)
            {
                MessageBox.Show("Te rog selectează un student din tabel.");
                return;
            }

            // 2. Obține ID-ul
            User selectedUser = (User)dgvStudents.SelectedRows[0].DataBoundItem;
            int userId = selectedUser.UserId;

            // 3. Confirmare
            var confirm = MessageBox.Show($"Ești sigur că vrei să ștergi studentul '{selectedUser.Username}' (ID: {userId})?",
                                        "Confirmare Ștergere", MessageBoxButtons.YesNo, MessageBoxIcon.Warning);

            if (confirm == DialogResult.No) return;

            // 4. Apel API
            try
            {
                bool success = await ApiService.DeleteUserAsync(userId);
                if (success)
                {
                    MessageBox.Show("Student șters!");
                    await LoadAllData(); // Reîncarcă tot
                }
                else
                {
                    MessageBox.Show("Ștergerea a eșuat.");
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show($"Eroare: {ex.Message}");
            }
        }

        // --- EXEMPLU BUTON CREARE STUDENT ---

        private async void btnCreateStudent_Click(object sender, EventArgs e)
        {
            // Pentru creare, cel mai bine e să deschizi un formular nou (Pop-up)
            // care cere Username, Password.

            // Exemplu rapid (neproductiv, doar de test):
            string username = "student_nou"; // Ia asta dintr-un InputBox
            string password = "parola123"; // Ia asta dintr-un InputBox

            try
            {
                await ApiService.CreateUserAsync(username, password, "ROLE_STUDENT");
                MessageBox.Show("Student creat!");
                await LoadAllData(); // Reîncarcă
            }
            catch (Exception ex)
            {
                MessageBox.Show($"Eroare la creare: {ex.Message}");
            }
        }
        // --- BUTON ȘTERGERE PROFESOR ---

        private async void btnDeleteProfessor_Click(object sender, EventArgs e)
        {
            // 1. Verifică selecția în dgvProfessors
            if (dgvProfessors.SelectedRows.Count == 0)
            {
                MessageBox.Show("Te rog selectează un profesor din tabel.");
                return;
            }

            // 2. Obține ID-ul (este tot un obiect 'User')
            User selectedUser = (User)dgvProfessors.SelectedRows[0].DataBoundItem;
            int userId = selectedUser.UserId;

            // 3. Confirmare
            var confirm = MessageBox.Show($"Ești sigur că vrei să ștergi profesorul '{selectedUser.Username}' (ID: {userId})?",
                                        "Confirmare Ștergere", MessageBoxButtons.YesNo, MessageBoxIcon.Warning);

            if (confirm == DialogResult.No) return;

            // 4. Apel API
            try
            {
                // Folosim aceeași funcție ca la student, deoarece serverul
                // are un singur endpoint pentru useri: /api/admin/users/{id}
                bool success = await ApiService.DeleteUserAsync(userId);
                if (success)
                {
                    MessageBox.Show("Profesor șters!");
                    await LoadAllData(); // Reîncarcă tot
                }
                else
                {
                    MessageBox.Show("Ștergerea a eșuat.");
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show($"Eroare: {ex.Message}");
            }
        }
        // --- BUTON ȘTERGERE TEMĂ ---

        private async void btnDeleteAssignment_Click(object sender, EventArgs e)
        {
            // 1. Verifică selecția în dgvAssignments
            if (dgvAssignments.SelectedRows.Count == 0)
            {
                MessageBox.Show("Te rog selectează o temă din tabel.");
                return;
            }

            // 2. Obține ID-ul (folosind obiectul 'Assignment')
            Assignment selectedAssignment = (Assignment)dgvAssignments.SelectedRows[0].DataBoundItem;
            int assignmentId = selectedAssignment.AssignmentId;

            // 3. Confirmare
            var confirm = MessageBox.Show($"Ești sigur că vrei să ștergi tema '{selectedAssignment.Title}' (ID: {assignmentId})?",
                                        "Confirmare Ștergere", MessageBoxButtons.YesNo, MessageBoxIcon.Warning);

            if (confirm == DialogResult.No) return;

            // 4. Apel API
            try
            {
                // Folosim funcția specifică pentru teme
                bool success = await ApiService.DeleteAssignmentAsync(assignmentId);
                if (success)
                {
                    MessageBox.Show("Temă ștearsă!");
                    await LoadAllData(); // Reîncarcă tot
                }
                else
                {
                    MessageBox.Show("Ștergerea a eșuat.");
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show($"Eroare: {ex.Message}");
            }
        }

        private async void btnCreateStudent_Click_1(object sender, EventArgs e)
        {
            // Deschidem noul formular, pre-selectând rolul "ROLE_STUDENT"
            using (FormCreateUser createUserForm = new FormCreateUser("ROLE_STUDENT"))
            {
                // Îl deschidem ca "Dialog". Codul de aici va aștepta
                // până când formularul pop-up se închide.
                var result = createUserForm.ShowDialog();

                // Dacă user-ul a fost creat cu succes (am apăsat "Creează")
                if (result == DialogResult.OK)
                {
                    // Reîncărcăm toate datele pentru a vedea noul student
                    await LoadAllData();
                }
            }
        }

        private async void btnCreateProfessors_Click(object sender, EventArgs e)
        {
            // Deschidem noul formular, pre-selectând rolul "ROLE_PROFESOR"
            using (FormCreateUser createUserForm = new FormCreateUser("ROLE_PROFESOR"))
            {
                var result = createUserForm.ShowDialog();

                // Dacă user-ul a fost creat cu succes
                if (result == DialogResult.OK)
                {
                    // Reîncărcăm toate datele pentru a vedea noul profesor
                    await LoadAllData();
                }
            }
        }

        private async void btnDeleteAssignments_Click(object sender, EventArgs e)
        {
            // 1. Verifică dacă e selectat un rând în tabelul de teme
            if (dgvAssignments.SelectedRows.Count == 0)
            {
                MessageBox.Show("Te rog selectează o temă din tabel.");
                return;
            }

            // 2. Obține ID-ul temei din rândul selectat
            Assignment selectedAssignment = (Assignment)dgvAssignments.SelectedRows[0].DataBoundItem;
            int assignmentId = selectedAssignment.AssignmentId;

            // 3. Cere confirmarea
            var confirm = MessageBox.Show($"Ești sigur că vrei să ștergi tema '{selectedAssignment.Title}' (ID: {assignmentId})?",
                                        "Confirmare Ștergere", MessageBoxButtons.YesNo, MessageBoxIcon.Warning);

            if (confirm == DialogResult.No) return;

            // 4. Apel API pentru ștergere
            try
            {
                // Apelează funcția specifică din ApiService
                bool success = await ApiService.DeleteAssignmentAsync(assignmentId);

                if (success)
                {
                    MessageBox.Show("Temă ștearsă!");
                    await LoadAllData(); // Reîncarcă toate tabelele
                }
                else
                {
                    MessageBox.Show("Ștergerea a eșuat.");
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show($"Eroare: {ex.Message}");
            }
        }

        private async void btnDeleteStudent_Click_1(object sender, EventArgs e)
        {
            // 1. Verifică selecția în dgvStudents
            if (dgvStudents.SelectedRows.Count == 0)
            {
                MessageBox.Show("Te rog selectează un student din tabel.");
                return;
            }

            // 2. Obține ID-ul (este un obiect 'User')
            User selectedUser = (User)dgvStudents.SelectedRows[0].DataBoundItem;
            int userId = selectedUser.UserId;

            // 3. Confirmare
            var confirm = MessageBox.Show($"Ești sigur că vrei să ștergi studentul '{selectedUser.Username}' (ID: {userId})?",
                                        "Confirmare Ștergere", MessageBoxButtons.YesNo, MessageBoxIcon.Warning);

            if (confirm == DialogResult.No) return;

            // 4. Apel API
            try
            {
                // Apelează funcția de ștergere user
                bool success = await ApiService.DeleteUserAsync(userId);

                if (success)
                {
                    MessageBox.Show("Student șters!");
                    await LoadAllData(); // Reîncarcă tot
                }
                else
                {
                    MessageBox.Show("Ștergerea a eșuat.");
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show($"Eroare: {ex.Message}");
            }

        }

        private async void btnDeleteProfessors_Click(object sender, EventArgs e)
        {
            // 1. Verifică selecția în dgvProfessors
            if (dgvProfessors.SelectedRows.Count == 0)
            {
                MessageBox.Show("Te rog selectează un profesor din tabel.");
                return;
            }

            // 2. Obține ID-ul (este tot un obiect 'User')
            User selectedUser = (User)dgvProfessors.SelectedRows[0].DataBoundItem;
            int userId = selectedUser.UserId;

            // 3. Confirmare
            var confirm = MessageBox.Show($"Ești sigur că vrei să ștergi profesorul '{selectedUser.Username}' (ID: {userId})?",
                                        "Confirmare Ștergere", MessageBoxButtons.YesNo, MessageBoxIcon.Warning);

            if (confirm == DialogResult.No) return;

            // 4. Apel API
            try
            {
                // Folosim aceeași funcție ca la student
                bool success = await ApiService.DeleteUserAsync(userId);

                if (success)
                {
                    MessageBox.Show("Profesor șters!");
                    await LoadAllData(); // Reîncarcă tot
                }
                else
                {
                    MessageBox.Show("Ștergerea a eșuat.");
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show($"Eroare: {ex.Message}");
            }
        }
    }
}
