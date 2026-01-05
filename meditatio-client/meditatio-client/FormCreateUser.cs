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
    public partial class FormCreateUser : Form
    {
        public FormCreateUser()
        {
            InitializeComponent();
            // Populează ComboBox-ul cu roluri
            cmbRole.Items.Add("ROLE_STUDENT");
            cmbRole.Items.Add("ROLE_PROFESOR");
            cmbRole.Items.Add("ROLE_ADMIN"); // Opțional, dacă vrei ca adminii să creeze alți admini
        }
        public FormCreateUser(string defaultRole) : this() // Apelează constructorul de bază
        {
            // Setează rolul primit ca default
            cmbRole.SelectedItem = defaultRole;
        }

        private async void btnCreate_Click(object sender, EventArgs e)
        {
            // Validări simple
            if (string.IsNullOrWhiteSpace(txtUsername.Text) ||
                string.IsNullOrWhiteSpace(txtPassword.Text) ||
                cmbRole.SelectedItem == null)
            {
                MessageBox.Show("Te rog completează toate câmpurile.", "Eroare", MessageBoxButtons.OK, MessageBoxIcon.Warning);
                return;
            }

            string username = txtUsername.Text;
            string password = txtPassword.Text;
            string role = cmbRole.SelectedItem.ToString();

            btnCreate.Enabled = false;
            btnCreate.Text = "Se creează...";

            try
            {
                // Apelăm metoda de creare din ApiService
                var newUser = await ApiService.CreateUserAsync(username, password, role);

                MessageBox.Show($"Utilizatorul '{newUser.Username}' a fost creat cu succes.", "Succes", MessageBoxButtons.OK, MessageBoxIcon.Information);

                // Setăm rezultatul formularului ca OK și îl închidem
                this.DialogResult = DialogResult.OK;
                this.Close();
            }
            catch (Exception ex)
            {
                MessageBox.Show($"Eroare la crearea utilizatorului:\n{ex.Message}", "Eroare API", MessageBoxButtons.OK, MessageBoxIcon.Error);
                btnCreate.Enabled = true;
                btnCreate.Text = "Creează";
            }

        }

        private void btnCancel_Click(object sender, EventArgs e)
        {
            this.DialogResult = DialogResult.Cancel;
            this.Close();

        }
    }
}
